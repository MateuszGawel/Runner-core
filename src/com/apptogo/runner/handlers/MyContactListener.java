package com.apptogo.runner.handlers;

import java.util.ArrayList;

import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener{
	private GameWorld world;
	private ArrayList<Body> arrowsToSetInactive = new ArrayList<Body>();
	private ArrayList<Body> barrelsToSetActive = new ArrayList<Body>();
	
	public MyContactListener(GameWorld world){
		this.world = world;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		//Logger.log(this, "WYKRYTO: " + fa.getUserData() + ", " + fb.getUserData());
		
		//smierc od zabijajacych
		if(!world.character.isImmortal() && world.character.isAlive() && ("killing".equals(fa.getUserData()) && "player".equals(fb.getUserData())
				|| ("killing".equals(fb.getUserData()) && "player".equals(fa.getUserData())))){
			
			if( world.character.dieBottom() )
			{
				NotificationManager.getInstance().notifyDieBottom();
			}
			
			Logger.log(this, "Dotyka killing: " + fa.getUserData() + " oraz " + fb.getUserData() + " player jest niesmierelny: " + world.character.isImmortal());
		}
		
		//skok i sciany
		if(("footSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("footSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			world.character.incrementFootSensor();
			world.character.land();
		}
		if(("wallSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("wallSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			world.character.incrementWallSensor();
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
			if((!world.character.isImmortal() && world.character.isAlive())){		
				if((Math.abs(fa.getBody().getLinearVelocity().x) > 5f || fa.getBody().getLinearVelocity().x > 5f) && world.character.dieBottom() )
				{
					NotificationManager.getInstance().notifyDieBottom();
				}
			}
		}
		if("barrel".equals(fb.getUserData()) && "player".equals(fa.getUserData())){
			if((!world.character.isImmortal() && world.character.isAlive())){		
				if((Math.abs(fb.getBody().getLinearVelocity().x) > 5f || fb.getBody().getLinearVelocity().x > 5f) && world.character.dieBottom() )
				{
					NotificationManager.getInstance().notifyDieBottom();
				}
			}
		}

	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(("footSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("footSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			world.character.decrementFootSensor();
		}
		if(("wallSensor".equals(fa.getUserData()) && "nonkilling".equals(fb.getUserData())) 
				|| ("wallSensor".equals(fb.getUserData()) && "nonkilling".equals(fa.getUserData()))){
			world.character.decrementWallSensor();
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
