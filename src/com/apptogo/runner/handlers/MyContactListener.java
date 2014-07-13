package com.apptogo.runner.handlers;

import com.apptogo.runner.actors.Player.PlayerAnimationState;
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
		//Logger.log(this, "WYKRYTO: " + fa.getUserData() + ", " + fb.getUserData());
		
		
		if((!world.player.isImmortal() && world.player.isAlive() && "killing".equals(fa.getUserData()) && "player".equals(fb.getUserData())) 
				|| ("killing".equals(fb.getUserData()) && "player".equals(fa.getUserData()))){
			world.player.dieBottom();
			Logger.log(this, "Dotyka killing");
		}
		
		if(("footSensor".equals(fa.getUserData()) && "ground".equals(fb.getUserData())) 
				|| ("footSensor".equals(fb.getUserData()) && "ground".equals(fa.getUserData()))){
			world.player.incrementJumpSensor();
			world.player.incrementFootSensor();
			world.player.land();
		}
		if(("wallSensor".equals(fa.getUserData()) && "ground".equals(fb.getUserData())) 
				|| ("wallSensor".equals(fb.getUserData()) && "ground".equals(fa.getUserData()))){
			world.player.incrementJumpSensor();
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(("footSensor".equals(fa.getUserData()) && "ground".equals(fb.getUserData())) 
				|| ("footSensor".equals(fb.getUserData()) && "ground".equals(fa.getUserData()))){
			world.player.decrementJumpSensor();
			world.player.decrementFootSensor();
		}
		if(("wallSensor".equals(fa.getUserData()) && "ground".equals(fb.getUserData())) 
				|| ("wallSensor".equals(fb.getUserData()) && "ground".equals(fa.getUserData()))){
			world.player.decrementJumpSensor();
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
