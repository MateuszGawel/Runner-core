package com.apptogo.runner.listeners;

import static java.lang.Math.sqrt;

import java.util.ArrayList;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

public class MyContactListener implements ContactListener{
	private GameWorld gameWorld;
	private World world;
	private ArrayList<Body> arrowsToSetInactive = new ArrayList<Body>();
	private ArrayList<Body> barrelsToSetActive = new ArrayList<Body>();
	int coinV = 0;
	public MyContactListener(GameWorld gameWorld, World world){
		this.world = world;
		this.gameWorld = gameWorld;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		//kontakt z przeszkoda ktora tylko zabija mozna zamienic w taki sposob zeby ich userdata od razu okreslalo typ smierci
		
		//smierc od zabijajacych
		if(!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive() && ("killingTop".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ) )
				|| ("killingTop".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ))))){
			
			if( gameWorld.player.character.dieTop())
			{
				NotificationManager.getInstance().notifyDieTop();
			}
			
		}
		if(!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive() && ("killingBottom".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ) )
				|| ("killingBottom".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ))))){
			
			if( gameWorld.player.character.dieBottom())
			{
				NotificationManager.getInstance().notifyDieBottom();
			}
			
		}
		
		
		//smierc od ogniska
		if(!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive() && ("bonfire".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ) )
				|| ("bonfire".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ))))){
			
			if( gameWorld.player.character.dieDismemberment())
			{
				NotificationManager.getInstance().notifyDieBottom();
			}
			
		}
		
		//smierc od krzaczka
		if(!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive() && ("bush".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ) )
				|| ("bush".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ))))){
			
			if( gameWorld.player.character.dieTop())
			{
				NotificationManager.getInstance().notifyDieBottom();
			}
			
		}
		
		//skok i sciany
		if(("footSensor".equals( UserData.key( fa.getUserData() ) ) && "nonkilling".equals( UserData.key( fb.getUserData() ) )) 
				|| ("footSensor".equals( UserData.key( fb.getUserData() ) ) && "nonkilling".equals( UserData.key( fa.getUserData() )))){
			gameWorld.player.character.incrementFootSensor();
			gameWorld.player.character.land();
		}
		if(("wallSensor".equals( UserData.key( fa.getUserData() ) ) && "nonkilling".equals( UserData.key( fb.getUserData() ))) 
				|| ("wallSensor".equals( UserData.key( fb.getUserData() ) ) && "nonkilling".equals(UserData.key( fa.getUserData() )))){
			gameWorld.player.character.incrementWallSensor();
		}
		
		//zmienianie typu beczek
		if(("barrel".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ) ) 
				|| ("barrel".equals( UserData.key( fb.getUserData() ) ) && ("player".equals( UserData.key( fa.getUserData() )))))){
			barrelsToSetActive.add(fb.getBody());
		}
		if(("barrel".equals( UserData.key( fa.getUserData() ) ) && "barrel".equals( UserData.key( fb.getUserData() ))) 
				|| ("barrel".equals( UserData.key( fb.getUserData() ) ) && "barrel".equals( UserData.key( fa.getUserData() )))){
			barrelsToSetActive.add(fb.getBody());
			barrelsToSetActive.add(fa.getBody());
		}
		
		//smierc od beczek
		if("barrel".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ))){
			if((!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive())){		
				if((Math.abs(fa.getBody().getLinearVelocity().x) > 5f || fa.getBody().getLinearVelocity().x > 5f) && gameWorld.player.character.dieBottom() )
				{
					NotificationManager.getInstance().notifyDieBottom();
				}
			}
		}
		if("barrel".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ))){
			if((!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive())){		
				if((Math.abs(fb.getBody().getLinearVelocity().x) > 5f || fb.getBody().getLinearVelocity().x > 5f) && gameWorld.player.character.dieBottom() )
				{
					NotificationManager.getInstance().notifyDieBottom();
				}
			}
		}
		//smierc od je¿a
		if("hedgehog".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ))){
			if((!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive())){		
				gameWorld.player.character.dieBottom();
				NotificationManager.getInstance().notifyDieBottom();
			}
		}
		if("hedgehog".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ))){
			if((!gameWorld.player.character.isImmortal() && gameWorld.player.character.isAlive())){		
				gameWorld.player.character.dieBottom();
				NotificationManager.getInstance().notifyDieBottom();
			}
		}
		
		//podnoszenie aliena
		if("liftField".equals( UserData.key( fa.getUserData() ) ) && ((Alien)gameWorld.player.character).liftField.isActive()){
			if(!UserData.key( fb.getUserData() ).equals("player") && !UserData.key( fa.getUserData() ).equals("wallSensor") && !UserData.key( fa.getUserData() ).equals("footSensor")){
				((Alien)gameWorld.player.character).liftField.addBodyToLift(fb.getBody());
				fb.getBody().applyLinearImpulse(0, ((Alien)gameWorld.player.character).liftField.initialBoost, 0, 0, true);;
			}
		}
		if("liftField".equals( UserData.key( fb.getUserData() ) ) && ((Alien)gameWorld.player.character).liftField.isActive()){
			if(!UserData.key( fa.getUserData() ).equals("player") && !UserData.key( fa.getUserData() ).equals("wallSensor") && !UserData.key( fa.getUserData() ).equals("footSensor")){
				((Alien)gameWorld.player.character).liftField.addBodyToLift(fa.getBody());
				fa.getBody().applyLinearImpulse(0, ((Alien)gameWorld.player.character).liftField.initialBoost, 0, 0, true);
			}
		}
		
		//trampolina
		if("mushroom".equals( UserData.key( fa.getUserData() ) ) && "footSensor".equals( UserData.key( fb.getUserData() ))){
			if(gameWorld.player.character.isAlive()){	
				gameWorld.player.character.jump();
				fa.setUserData( new UserData("mushroomWorking") );
				float v0 = (float) sqrt(-world.getGravity().y*2 * 8 );
				fb.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 10, v0);
			}
		}
		if("mushroom".equals( UserData.key( fb.getUserData() ) ) && "footSensor".equals( UserData.key( fa.getUserData() ))){
			if(gameWorld.player.character.isAlive()){	
				gameWorld.player.character.jump();
				fb.setUserData( new UserData("mushroomWorking") );
				float v0 = (float) sqrt(-world.getGravity().y*2 * 8 );
				fa.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 10, v0);
			}
		}
		
		//katapulta
		if("catapult".equals( UserData.key( fa.getUserData() ) ) && "footSensor".equals( UserData.key( fb.getUserData() ))){
			if(gameWorld.player.character.isAlive()){		
				gameWorld.player.character.jump();
				fa.getBody().setUserData( new UserData("catapultWorking") );
				float v0 = (float) sqrt(-world.getGravity().y*2 * 6 );
				fb.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 20, v0);
			}
		}
		if("catapult".equals( UserData.key( fb.getUserData() ) ) && "footSensor".equals( UserData.key( fa.getUserData() ))){
			if(gameWorld.player.character.isAlive()){
				gameWorld.player.character.jump();
				fb.getBody().setUserData( new UserData("catapultWorking") );
				float v0 = (float) sqrt(-world.getGravity().y*2 * 6 );
				fa.getBody().setLinearVelocity(fb.getBody().getLinearVelocity().x + 20, v0);
			}
		}
		
		//coin
		if("coin".equals( UserData.key( fa.getUserData() ) ) && "wallSensor".equals( UserData.key( fb.getUserData() ) ) || "coin".equals( UserData.key( fb.getUserData() ) ) && "wallSensor".equals( UserData.key( fa.getUserData() )))
		{
			Fixture fixture = ( ( UserData.key( fa.getUserData() ) ).equals("coin") )?fa:fb;
			
			coinV++;
			fixture.getBody().setUserData( new UserData("inactive") );
			//fixture.getBody().setType(BodyType.DynamicBody);
			//fixture.setUserData( new UserData("gainedCoin") );
			//fixture.getBody().setUserData( new UserData("gainedCoin") );

			//gameWorld.addBodyToDestroy( fixture.getBody() );
			//fixture.getBody().setUserData( new UserData("inactive") );
		}
		
		//powerup
		if( ( ( UserData.key( fa.getUserData() ) ).contains("powerup") && "player".equals( UserData.key( fb.getUserData() ) ) ) || ( ( UserData.key( fb.getUserData() ) ).contains("powerup") && "player".equals( UserData.key( fa.getUserData() ) ) ) )
		{
			if( !gameWorld.player.character.isPowerupSet() )
			{
				Fixture fixture = ( ( UserData.key( fa.getUserData() ) ).contains("powerup") )?fa:fb;
				
				String powerupKey = ((UserData)fixture.getUserData()).arg1;
				gameWorld.player.character.setPowerup( PowerupType.parseFromString(powerupKey) );
							Logger.log(this, powerupKey);

				fixture.getBody().setUserData( new UserData("inactive") );
			}
		}
		
		//meta - koniec gry
		if( ( "finishingLine".equals( UserData.key( fa.getUserData() ) ) && "player".equals( UserData.key( fb.getUserData() ) ) ) || ( "finishingLine".equals( UserData.key( fb.getUserData() ) ) && "player".equals( UserData.key( fa.getUserData() ) ) ) )
		{
			gameWorld.player.character.endGame();
			gameWorld.player.character.setRunning(false);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if(("footSensor".equals( UserData.key( fa.getUserData() ) ) && "nonkilling".equals( UserData.key( fb.getUserData() ))) 
				|| ("footSensor".equals( UserData.key( fb.getUserData() ) ) && "nonkilling".equals( UserData.key( fa.getUserData() )))){
			gameWorld.player.character.decrementFootSensor();
		}
		if(("wallSensor".equals( UserData.key( fa.getUserData() ) ) && "nonkilling".equals( UserData.key( fb.getUserData() ))) 
				|| ("wallSensor".equals( UserData.key( fb.getUserData() ) ) && "nonkilling".equals( UserData.key( fa.getUserData() )))){
			gameWorld.player.character.decrementWallSensor();
		}
		
		if("liftField".equals( UserData.key( fa.getUserData() ))){
			((Alien)gameWorld.player.character).liftField.removeBodyToLift(fb.getBody());
		}
		if("liftField".equals( UserData.key( fb.getUserData() ))){
			((Alien)gameWorld.player.character).liftField.removeBodyToLift(fa.getBody());
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

		if((("arrow".equals( UserData.key( fa.getUserData() ) ) && ("nonkilling".equals( UserData.key( fb.getUserData() ) ) || "killing".equals( UserData.key( fb.getUserData() )))))
				|| (("arrow".equals( UserData.key( fb.getUserData() ) ) && ("nonkilling".equals( UserData.key( fa.getUserData() ) ) || "killing".equals( UserData.key( fa.getUserData() )))))){
			float[] impulses = impulse.getNormalImpulses();
			if(impulses[0] > 0.2f){
				if("arrow".equals( UserData.key( fa.getUserData() )))
					arrowsToSetInactive.add(fa.getBody());
				else if("arrow".equals( UserData.key( fb.getUserData() )))
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
