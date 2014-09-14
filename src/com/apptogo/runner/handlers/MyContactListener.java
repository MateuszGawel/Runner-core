package com.apptogo.runner.handlers;

import static java.lang.Math.sqrt;

import java.util.ArrayList;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class MyContactListener implements ContactListener{
	private GameWorld gameWorld;
	private World world;
	private ArrayList<Body> arrowsToSetInactive = new ArrayList<Body>();
	private ArrayList<Body> barrelsToSetActive = new ArrayList<Body>();
	
	public MyContactListener(GameWorld gameWorld, World world){
		this.world = world;
		this.gameWorld = gameWorld;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		//Logger.log(this, "WYKRYTO: " + fa.getUserData() + ", " + fb.getUserData());
		
		//smierc od zabijajacych
		if(!gameWorld.character.isImmortal() && gameWorld.character.isAlive() && ("killing".equals(fa.getUserData()) && "player".equals(fb.getUserData())
				|| ("killing".equals(fb.getUserData()) && "player".equals(fa.getUserData())))){
			
			if( gameWorld.character.dieDismemberment())
			{
				NotificationManager.getInstance().notifyDieBottom();
			}
			
			Logger.log(this, "Dotyka killing: " + fa.getUserData() + " oraz " + fb.getUserData() + " player jest niesmierelny: " + gameWorld.character.isImmortal());
		}
		
		//skok i sciany
		if(("footSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("footSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			gameWorld.character.incrementFootSensor();
			gameWorld.character.land();
		}
		if(("wallSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("wallSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			gameWorld.character.incrementWallSensor();
		}
		
		//zmienianie typu beczek
		if(("barrel".equals(fa.getUserData()) && "player".equals(fb.getUserData()) 
				|| ("barrel".equals(fb.getUserData()) && ("player".equals(fa.getUserData()))))){
			barrelsToSetActive.add(fb.getBody());
		}
		if(("barrel".equals(fa.getUserData()) && "barrel".equals(fb.getUserData())) 
				|| ("barrel".equals(fb.getUserData()) && "barrel".equals(fa.getUserData()))){
			Logger.log(this,  "dwie beczki!!!!!!!!!");
			barrelsToSetActive.add(fb.getBody());
			barrelsToSetActive.add(fa.getBody());
		}
		
		//smierc od beczek
		if("barrel".equals(fa.getUserData()) && "player".equals(fb.getUserData())){
			if((!gameWorld.character.isImmortal() && gameWorld.character.isAlive())){		
				if((Math.abs(fa.getBody().getLinearVelocity().x) > 5f || fa.getBody().getLinearVelocity().x > 5f) && gameWorld.character.dieBottom() )
				{
					NotificationManager.getInstance().notifyDieBottom();
				}
			}
		}
		if("barrel".equals(fb.getUserData()) && "player".equals(fa.getUserData())){
			if((!gameWorld.character.isImmortal() && gameWorld.character.isAlive())){		
				if((Math.abs(fb.getBody().getLinearVelocity().x) > 5f || fb.getBody().getLinearVelocity().x > 5f) && gameWorld.character.dieBottom() )
				{
					NotificationManager.getInstance().notifyDieBottom();
				}
			}
		}
		
		//podnoszenie aliena
		if("liftField".equals(fa.getUserData()) && ((Alien)gameWorld.character).liftField.isActive()){
			if(!fb.getUserData().equals("player") && !fa.getUserData().equals("wallSensor") && !fa.getUserData().equals("footSensor")){
				((Alien)gameWorld.character).liftField.addBodyToLift(fb.getBody());
				fb.getBody().applyLinearImpulse(0, ((Alien)gameWorld.character).liftField.initialBoost, 0, 0, true);
				Logger.log(this, fb.getUserData().toString());
			}
		}
		if("liftField".equals(fb.getUserData()) && ((Alien)gameWorld.character).liftField.isActive()){
			if(!fa.getUserData().equals("player") && !fa.getUserData().equals("wallSensor") && !fa.getUserData().equals("footSensor")){
				((Alien)gameWorld.character).liftField.addBodyToLift(fa.getBody());
				fa.getBody().applyLinearImpulse(0, ((Alien)gameWorld.character).liftField.initialBoost, 0, 0, true);
				Logger.log(this, fa.getUserData().toString());
			}
		}
		
		//trampolina
		if("mushroom".equals(fa.getUserData()) && "footSensor".equals(fb.getUserData())){
			if(gameWorld.character.isAlive()){		
				fa.setUserData("mushroomWorking");
				Logger.log(this, "weszlem");
				float v0 = (float) sqrt(-world.getGravity().y*2 * 8 );
				fb.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 10, v0);
			}
		}
		if("mushroom".equals(fb.getUserData()) && "footSensor".equals(fa.getUserData())){
			if(gameWorld.character.isAlive()){		
				fb.setUserData("mushroomWorking");
				Logger.log(this, "weszlem");
				float v0 = (float) sqrt(-world.getGravity().y*2 * 8 );
				fa.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 10, v0);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(("footSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("footSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			gameWorld.character.decrementFootSensor();
		}
		if(("wallSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("wallSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			gameWorld.character.decrementWallSensor();
		}
		
		if("liftField".equals(fa.getUserData())){
			((Alien)gameWorld.character).liftField.removeBodyToLift(fb.getBody());
			Logger.log(this, "jestem w A koniec");
		}
		if("liftField".equals(fb.getUserData())){
			((Alien)gameWorld.character).liftField.removeBodyToLift(fa.getBody());
			Logger.log(this, "jestem w B konec");
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if((("arrow".equals(fa.getUserData()) && ("nonkilling".equals(fb.getUserData()) || "killing".equals(fb.getUserData()))))
				|| (("arrow".equals(fb.getUserData()) && ("nonkilling".equals(fa.getUserData()) || "killing".equals(fa.getUserData()))))){
			float[] impulses = impulse.getNormalImpulses();
			if(impulses[0] > 0.2f){
				if("arrow".equals(fa.getUserData()))
					arrowsToSetInactive.add(fa.getBody());
				else if("arrow".equals(fb.getUserData()))
					arrowsToSetInactive.add(fb.getBody());
			}
				
		}		
	}
	
	public void postStep(){
		for(Body body : arrowsToSetInactive){
			body.setActive(false);
		}
		arrowsToSetInactive.clear();
		
		for(Body body : barrelsToSetActive){
			body.setType(BodyType.DynamicBody);
		}
		barrelsToSetActive.clear();
	}

}
