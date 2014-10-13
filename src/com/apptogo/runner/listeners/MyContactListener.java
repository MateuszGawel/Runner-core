package com.apptogo.runner.listeners;

import static java.lang.Math.sqrt;

import java.util.ArrayList;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class MyContactListener implements ContactListener
{
	private GameWorld gameWorld;
	private World world;
	
	private ArrayList<Body> arrowsToSetInactive = new ArrayList<Body>();
	private ArrayList<Body> barrelsToSetActive = new ArrayList<Body>();
	
	public MyContactListener(GameWorld gameWorld, World world)
	{
		this.world = world;
		this.gameWorld = gameWorld;
	}

	@Override
	public void beginContact(Contact contact)
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		Player player = checkIfFixtureIsPlayer(fa, fb);		
		
		//smierc TOP
		if(checkFixturesTypes(fa, fb, "killingTop", "player")){	
			Logger.log(this, "smierc dla1: "+player.getName());
			if(player.character.isAlive() && !player.character.isImmortal()){
				Logger.log(this, "smierc dla2: "+player.getName());
				player.character.dieTop();
			}
		}
		
		//smierc BOTTOM
		if( checkFixturesTypes(fa, fb, "killingBottom", "player")){
			Logger.log(this, "smierc dla1: "+player.getName());
			if(player.character.isAlive() && !player.character.isImmortal()){
				Logger.log(this, "smierc dla2: "+player.getName());
				player.character.dieBottom();
			}
		}
		
		//smierc od ogniska
		if( checkFixturesTypes(fa, fb, "bonfire", "player")){
			if(player.character.isAlive() && !player.character.isImmortal()){
				player.character.dieDismemberment();
			}
		}

		//smierc od beczek
		if(checkFixturesTypes(fa, fb, "barrel", "player")){
			if(player.character.isAlive() && !player.character.isImmortal()){
				Fixture fixture = getFixtureByType(fa, fb, "barrel");
				if((Math.abs(fixture.getBody().getLinearVelocity().x) > 5f || fixture.getBody().getLinearVelocity().x > 5f)){
					player.character.dieBottom();
				}
			}
		}

		//sensory playera
		if(checkFixturesTypes(fa, fb, "footSensor", "nonkilling")){
			player.character.incrementFootSensor();
			player.character.land();
		}
		
		if(checkFixturesTypes(fa, fb, "wallSensor", "nonkilling")){
			player.character.incrementWallSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "standupSensor", "nonkilling")){
			player.character.incrementStandupSensor();
		}
		
		//zmienianie typu beczek
		if(checkFixturesTypes(fa, fb, "barrel", "player")){
			Fixture fixture = getFixtureByType(fa, fb, "barrel");
			barrelsToSetActive.add(fixture.getBody());
		}
		if(checkFixturesTypes(fa, fb, "barrel", "barrel")){
			barrelsToSetActive.add(fa.getBody());
			barrelsToSetActive.add(fb.getBody());
		}

		//bagno
		if(checkFixturesTypes(fa, fb, "swamp", "footSensor")){
			if(player.character.isAlive() && !player.character.isImmortal()){
				((UserData)player.character.getBody().getUserData()).slowPercent = 0.8f;
			}
		}
		
		//trampolina
		if(checkFixturesTypes(fa, fb, "mushroom", "footSensor")){
			Fixture mushroomFixture = getFixtureByType(fa, fb, "mushroom");
			Fixture footSensorFixture = getFixtureByType(fa, fb, "footSensor");		
			if(player.character.isAlive()){	
				player.character.jump();
				mushroomFixture.setUserData( new UserData("mushroomWorking") );
				float v0 = (float) sqrt( -world.getGravity().y * 16 );
				footSensorFixture.getBody().setLinearVelocity( footSensorFixture.getBody().getLinearVelocity().x + 10, v0);
			}
		}
		
		//katapulta
		if( checkFixturesTypes(fa, fb, "catapult", "footSensor")){
			Fixture catapultFixture = getFixtureByType(fa, fb, "catapult");
			Fixture footSensorFixture = getFixtureByType(fa, fb, "footSensor");
			if(player.character.isAlive())
			{		
				player.character.jump();
				catapultFixture.getBody().setUserData( new UserData("catapultWorking") );
				float v0 = (float) sqrt( -world.getGravity().y * 12 );
				footSensorFixture.getBody().setLinearVelocity( catapultFixture.getBody().getLinearVelocity().x + 20, v0);
			}		
		}
		
		//coin
		if( checkFixturesTypes(fa, fb, "coin", "wallSensor") ){
			Fixture fixture = getFixtureByType(fa, fb, "coin");
			fixture.getBody().setUserData(new UserData("inactive"));
			//fixture.getBody().setType(BodyType.DynamicBody);
			//fixture.setUserData( new UserData("gainedCoin") );
			//fixture.getBody().setUserData( new UserData("gainedCoin") );

			//gameWorld.addBodyToDestroy( fixture.getBody() );
			//fixture.getBody().setUserData( new UserData("inactive") );
		}
		
		//powerup
		if(checkFixturesTypes(fa, fb, "powerup", "player")){
			//dzia³a tylko na samego siebie. Inni nie podnosza moich powerupow
			if(((UserData)getFixtureByType(fa, fb, "standupSensor").getBody().getUserData()).me){
				if(!gameWorld.player.character.isPowerupSet() )
				{
					Fixture fixture = getFixtureByType(fa, fb, "powerup");
					
					String powerupKey = ((UserData)fixture.getUserData()).key;
					gameWorld.player.character.setPowerup( PowerupType.parseFromString(powerupKey) );
	
					fixture.getBody().setUserData(new UserData("inactive"));
				}
			}
		}
		
		//podnoszenie aliena dziala tylko w singlu - do zrobienia
		if(checkIsOneOfType(fa, fb, "liftField") && ((Alien)gameWorld.player.character).liftField.isActive()){
			Fixture fixture = getFixtureByType(fa, fb, "liftField");
			fixture = ( fixture == fb )?fa:fb; //bo chcemy fixture tego drugiego a nie liftField
			
			String fixtureType = ((UserData)fb.getUserData()).key;
			
			if( !( fixtureType.equals("player") || fixtureType.equals("wallSensor") || fixtureType.equals("footSensor") ) )
			{
				((Alien)gameWorld.player.character).liftField.addBodyToLift( fixture.getBody() );
				
				fixture.getBody().applyLinearImpulse(0, ((Alien)gameWorld.player.character).liftField.initialBoost, 0, 0, true);
			}
		}
		//meta - koniec gry
		if(checkFixturesTypes(fa, fb, "player", "finishingLine")){
			player.character.endGame();
			player.character.setRunning(false);
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		Player player = checkIfFixtureIsPlayer(fa, fb);
		
		if(checkFixturesTypes(fa, fb, "footSensor", "nonkilling")){
			player.character.decrementFootSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "wallSensor", "nonkilling")){
			player.character.decrementWallSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "standupSensor", "nonkilling")){
			player.character.decrementStandupSensor();
		}
		
		if(checkIsOneOfType(fa, fb, "liftField")){
			Fixture fixture = getFixtureByType(fa, fb, "liftField");
			((Alien)gameWorld.player.character).liftField.removeBodyToLift( fixture.getBody() );
		}
		
		//bagno
		if( checkFixturesTypes(fa, fb, "swamp", "footSensor") ){
			if(player.character.isAlive() && !gameWorld.player.character.isImmortal()){
				Fixture fixture = getFixtureByType(fa, fb, "footSensor");
				((UserData)fixture.getBody().getUserData()).slowPercent = 0;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{	
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if( checkFixturesTypes(fa, fb, "arrow", "nonkilling") || checkFixturesTypes(fa, fb, "arrow", "killing") )
		{
			float[] impulses = impulse.getNormalImpulses();
			
			if(impulses[0] > 0.2f)
			{
				Fixture fixture = getFixtureByType(fa, fb, "arrow");
				arrowsToSetInactive.add( fixture.getBody() );
			}	
		}		
	}
	
	public void postStep()
	{
		for(Body body : arrowsToSetInactive)
		{
			body.setActive(false);
		}
		arrowsToSetInactive.clear();
		
		for(Body body : barrelsToSetActive)
		{
			body.setType(BodyType.DynamicBody);
		}
		barrelsToSetActive.clear();
	}
	
	private boolean checkFixturesTypes(Fixture fixtureA, Fixture fixtureB, String typeA, String typeB)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if //wiem polecialem ale tego gowna nie da sie inaczej rozczytac
			(
				( 
						((UserData)fixtureA.getUserData()).key.equals( typeA ) 
						&& 
						((UserData)fixtureB.getUserData()).key.equals( typeB ) 
				) 
				|| 
				( 
						((UserData)fixtureA.getUserData()).key.equals( typeB ) 
						&& 
						((UserData)fixtureB.getUserData()).key.equals( typeA ) 
				)
			)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkIsOneOfType(Fixture fixtureA, Fixture fixtureB, String type)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if
			(
					((UserData)fixtureA.getUserData()).key.equals( type ) 
					||
					((UserData)fixtureB.getUserData()).key.equals( type ) 
			)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private Fixture getFixtureByType(Fixture fixtureA, Fixture fixtureB, String type)
	{
		return ((UserData)fixtureA.getUserData()).key.equals(type) ? fixtureA : fixtureB;
	}
	
	private Player findPlayerByName(String playerName){
		Player player = null;
		if(((UserData)gameWorld.player.character.getBody().getUserData()).playerName.equals(playerName)){
			player = gameWorld.player;
		}
		else{
			try {
				player = gameWorld.getEnemy(playerName);
			} catch (PlayerDoesntExistException e) {
				Logger.log(this, "There is no player with name: " + playerName);
			}
		}
		return player;
	}
	
	private Player checkIfFixtureIsPlayer(Fixture fa, Fixture fb){
		Player player = null;
		if(checkIsOneOfType(fa, fb, "player")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "player").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "footSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "footSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "wallSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "wallSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "standupSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "standupSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		return player;
	}
}
