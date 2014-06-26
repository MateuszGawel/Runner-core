package com.apptogo.runner.handlers;

import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener{
	private GameWorld world;
	
	public MyContactListener(GameWorld world){
		this.world = world;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(("killing".equals(fa.getUserData()) && "player".equals(fb.getUserData())) 
				|| ("killing".equals(fb.getUserData()) && "player".equals(fa.getUserData()))){
			Logger.log(this, "PLAYER DOTYKA GRZYBKA");
			world.player.die();
		}
		Logger.log(this, "WYKRYTO: " + fa.getUserData() + ", " + fb.getUserData());
		if(("player".equals(fa.getUserData()) && "ground".equals(fb.getUserData())) 
				|| ("player".equals(fb.getUserData()) && "ground".equals(fa.getUserData()))){
			world.player.incrementJumpSensor();
			Logger.log(this, "DOTYKA ZIEMI");
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(("player".equals(fa.getUserData()) && "ground".equals(fb.getUserData())) 
				|| ("player".equals(fb.getUserData()) && "ground".equals(fa.getUserData()))){
			world.player.decrementJumpSensor();
			Logger.log(this, "PUSZCZA ZIEMIE");
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}