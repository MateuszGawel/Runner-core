package com.apptogo.runner.listeners;

import static java.lang.Math.sqrt;

import java.util.ArrayList;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.appwarp.NotificationManager;
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
				fb.getBody().applyLinearImpulse(0, ((Alien)gameWorld.character).liftField.initialBoost, 0, 0, true);;
			}
		}
		if("liftField".equals(fb.getUserData()) && ((Alien)gameWorld.character).liftField.isActive()){
			if(!fa.getUserData().equals("player") && !fa.getUserData().equals("wallSensor") && !fa.getUserData().equals("footSensor")){
				((Alien)gameWorld.character).liftField.addBodyToLift(fa.getBody());
				fa.getBody().applyLinearImpulse(0, ((Alien)gameWorld.character).liftField.initialBoost, 0, 0, true);
			}
		}
		
		//trampolina
		if("mushroom".equals(fa.getUserData()) && "footSensor".equals(fb.getUserData())){
			if(gameWorld.character.isAlive()){	
				gameWorld.character.jump();
				fa.setUserData("mushroomWorking");
				float v0 = (float) sqrt(-world.getGravity().y*2 * 8 );
				fb.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 10, v0);
			}
		}
		if("mushroom".equals(fb.getUserData()) && "footSensor".equals(fa.getUserData())){
			if(gameWorld.character.isAlive()){	
				gameWorld.character.jump();
				fb.setUserData("mushroomWorking");
				float v0 = (float) sqrt(-world.getGravity().y*2 * 8 );
				fa.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 10, v0);
			}
		}
		
		//katapulta
		if("catapult".equals(fa.getUserData()) && "footSensor".equals(fb.getUserData())){
			if(gameWorld.character.isAlive()){		
				gameWorld.character.jump();
				fa.getBody().setUserData("catapultWorking");
				float v0 = (float) sqrt(-world.getGravity().y*2 * 6 );
				fb.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 20, v0);
			}
		}
		if("catapult".equals(fb.getUserData()) && "footSensor".equals(fa.getUserData())){
			if(gameWorld.character.isAlive()){
				gameWorld.character.jump();
				fb.getBody().setUserData("catapultWorking");
				float v0 = (float) sqrt(-world.getGravity().y*2 * 6 );
				fa.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 20, v0);
			}
		}
		
		//meta - koniec gry
		if( ( "finishingLine".equals(fa.getUserData()) && "player".equals(fb.getUserData()) ) || ( "finishingLine".equals(fb.getUserData()) && "player".equals(fa.getUserData()) ) )
		{
			gameWorld.character.endGame();
			gameWorld.character.setRunning(false);
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
		}
		if("liftField".equals(fb.getUserData())){
			((Alien)gameWorld.character).liftField.removeBodyToLift(fa.getBody());
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
