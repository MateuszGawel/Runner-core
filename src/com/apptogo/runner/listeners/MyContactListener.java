package com.apptogo.runner.listeners;

import static java.lang.Math.sqrt;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.Body;
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
			if(player.character.isAlive() && !player.character.isImmortal() && !getFixtureByType(fa, fb, "player").isSensor()){
				((UserData)getFixtureByType(fa, fb, "player").getBody().getUserData()).dieTop = true;
			}
		}
		
		//smierc BOTTOM
		if( checkFixturesTypes(fa, fb, "killingBottom", "player")){
			if(player.character.isAlive() && !player.character.isImmortal() && !getFixtureByType(fa, fb, "player").isSensor()){
				((UserData)getFixtureByType(fa, fb, "player").getBody().getUserData()).dieBottom = true;
			}
		}
		
		//smierc od ogniska
		if( checkFixturesTypes(fa, fb, "bonfire", "player")){
			if(player.character.isAlive() && !player.character.isImmortal() && !getFixtureByType(fa, fb, "player").isSensor()){
				player.character.dieDismemberment();
			}
		}
		
		//smierc od krzaczka (troche workaround i ginie sie na standup sensor)
		if( checkFixturesTypes(fa, fb, "bush", "standupSensor")){
			if(player.character.isAlive() && !player.character.isImmortal() && !getFixtureByType(fa, fb, "player").isSensor()){
				((UserData)getFixtureByType(fa, fb, "standupSensor").getBody().getUserData()).dieBottom = true;
			}
		}
		
		
		
		//zmiana kierunku je¿a
		if( checkFixturesTypes(fa, fb, "hedgehog", "hedgehogStopper")){
			Fixture hedgeHogFixture = getFixtureByType(fa, fb, "hedgehog");
			((UserData)hedgeHogFixture.getBody().getUserData()).changeDirection = true;
		}


		
		//sensory playera
		if(checkFixturesTypes(fa, fb, "footSensor", "nonkilling")){
			player.character.incrementFootSensor();
			//player.character.land();
		}
		
		if(checkFixturesTypes(fa, fb, "jumpSensor", "nonkilling")){
			player.character.incrementJumpSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "wallSensor", "nonkilling")){
			player.character.incrementWallSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "standupSensor", "nonkilling")){
			player.character.incrementStandupSensor();
		}
		if(checkFixturesTypes(fa, fb, "headSensor", "nonkilling")){
			player.character.incrementHeadSensor();
		}
		if(checkFixturesTypes(fa, fb, "footSensor", "barrel")){
			player.character.incrementBarrelSensor();
			//player.character.land();
		}
		if(checkFixturesTypes(fa, fb, "leftRotationSensor", "nonkilling")){
			player.character.incrementLeftRotationSensor();
		}
		if(checkFixturesTypes(fa, fb, "rightRotationSensor", "nonkilling")){
			player.character.incrementRightRotationSensor();
		}
		
		//boost po l¹dowaniu
		if( checkFixturesTypes(fa, fb, "nonkilling", "player") || checkFixturesTypes(fa, fb, "barrel", "player")){
			if(player.character.isAlive() && !player.character.isImmortal()){
				player.character.boostAfterLand();
			}
		}
		
		
		//beczki (tu moze byc problem bo jesli blednie wbiegnie na beczki to rozwali)
		if(checkFixturesTypes(fa, fb, "barrel", "player") || checkFixturesTypes(fa, fb, "barrel", "wallSensorBody")){
			
			Fixture playerFixture = getFixtureByType(fa, fb, "player");
			if(playerFixture == null)
				playerFixture = getFixtureByType(fa, fb, "wallSensorBody");
			Fixture barrelFixture = getFixtureByType(fa, fb, "barrel");
			
			
			if(!((UserData)barrelFixture.getBody().getUserData()).active){
				((UserData)barrelFixture.getBody().getUserData()).active = true;
				((UserData)barrelFixture.getBody().getUserData()).playSound = true;
			}
				
			if(player.character.isAlive() && !player.character.isImmortal() && 
					(Math.abs(barrelFixture.getBody().getLinearVelocity().x) > 10f 
							|| Math.abs(barrelFixture.getBody().getLinearVelocity().y) > 7f ))
			{
				((UserData)playerFixture.getBody().getUserData()).dieBottom = true;
				((UserData)barrelFixture.getBody().getUserData()).playSound = true;
			}
		}
		if(checkFixturesTypes(fa, fb, "barrel", "barrel")){
			((UserData)fa.getBody().getUserData()).active = true;
			((UserData)fb.getBody().getUserData()).active = true;
		}

		//bagno
		if(checkFixturesTypes(fa, fb, "swamp", "footSensor")){
			if(player.character.isAlive() && !player.character.isImmortal()){
				((UserData)player.character.getBody().getUserData()).slowPercent = 0.8f;
				((UserData)player.character.getBody().getUserData()).touchSwamp = true;
			}
		}
		
		//trampolina grzyb
		if(checkFixturesTypes(fa, fb, "mushroom", "footSensor")){
			Fixture mushroomFixture = getFixtureByType(fa, fb, "mushroom");		
			if(player.character.isAlive()){	
				player.character.incrementFootSensor();
				player.character.jump(1, 1.6f, 2, 0, true);
				mushroomFixture.setUserData( new UserData("mushroomWorking") );
			}
		}
		
		//katapulta
		if( checkFixturesTypes(fa, fb, "catapult", "footSensor")){
			Fixture catapultFixture = getFixtureByType(fa, fb, "catapult");
			if(player.character.isAlive())
			{		
				player.character.incrementFootSensor();
				player.character.jump(0, 1.2f, 25, 0, true);
				catapultFixture.getBody().setUserData( new UserData("catapultWorking") );
			}		
		}
		
		//coin
		if( checkFixturesTypes(fa, fb, "coin", "coinCollectorSensor") ){
			Fixture fixture = getFixtureByType(fa, fb, "coin");
			fixture.getBody().setUserData(new UserData("inactive"));
		}
		
		//powerup
		if(checkFixturesTypes(fa, fb, "powerup", "player")){
			//dzia³a tylko na samego siebie. Inni nie podnosza moich powerupow
			if(((UserData)getFixtureByType(fa, fb, "standupSensor").getBody().getUserData()).me){
				if(!gameWorld.player.character.isPowerupSet() )
				{
					Fixture fixture = getFixtureByType(fa, fb, "powerup");
					
					String powerupKey = ((UserData)fixture.getUserData()).powerup;
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
			//Logger.log(this, "endcontact: " + ((UserData)fa.getUserData()).key + ((UserData)fb.getUserData()).key );
		}
		
		if(checkFixturesTypes(fa, fb, "wallSensor", "nonkilling")){
			player.character.decrementWallSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "jumpSensor", "nonkilling")){
			player.character.decrementJumpSensor();
		}
		
		if(checkFixturesTypes(fa, fb, "standupSensor", "nonkilling")){
			player.character.decrementStandupSensor();
		}
		if(checkFixturesTypes(fa, fb, "headSensor", "nonkilling")){
			player.character.decrementHeadSensor();
		}
		if(checkFixturesTypes(fa, fb, "leftRotationSensor", "nonkilling")){
			player.character.decrementLeftRotationSensor();
		}
		if(checkFixturesTypes(fa, fb, "rightRotationSensor", "nonkilling")){
			player.character.decrementRightRotationSensor();
		}
		
		if(checkIsOneOfType(fa, fb, "liftField")){
			Fixture fixture = getFixtureByType(fa, fb, "liftField");
			((Alien)gameWorld.player.character).liftField.removeBodyToLift( fixture.getBody() );
		}
		if(checkFixturesTypes(fa, fb, "footSensor", "barrel")){
			player.character.decrementBarrelSensor();
		}
		
		//bagno
		if( checkFixturesTypes(fa, fb, "swamp", "footSensor") ){
			if(player.character.isAlive() && !gameWorld.player.character.isImmortal()){
				Fixture fixture = getFixtureByType(fa, fb, "footSensor");
				((UserData)fixture.getBody().getUserData()).slowPercent = 0;
				((UserData)player.character.getBody().getUserData()).touchSwamp = false;
			}
		}
		
		//trampolina grzyb
		if(checkFixturesTypes(fa, fb, "mushroom", "footSensor")){
			if(player.character.isAlive()){	
				player.character.decrementFootSensor();
			}
		}
		
		//katapulta
		if( checkFixturesTypes(fa, fb, "catapult", "footSensor")){
			if(player.character.isAlive())
			{		
				player.character.decrementFootSensor();
			}		
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		Player player = checkIfFixtureIsPlayer(fa, fb);
		
		if(checkFixturesTypes(fa, fb, "player", "nonkilling") || checkFixturesTypes(fa, fb, "player", "barrel")){
			Fixture fixture = getFixtureByType(fa, fb, "footSensor");
			((UserData)fixture.getBody().getUserData()).speedBeforeLand = fixture.getBody().getLinearVelocity().x;
		}
		
		//dŸwiêk krzaczka
		if( checkFixturesTypes(fa, fb, "bush", "nonkilling")){
			Body body = getFixtureByType(fa, fb, "bush").getBody();
			if(Math.abs(body.getLinearVelocity().x) > 5 && body.getLinearVelocity().y < -5)
				((UserData)body.getUserData()).playSound = true;
		}

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
				((UserData)fixture.getBody().getUserData()).active = false;
			}	
		}	
		
		//dŸwiêk beczki
		if( checkFixturesTypes(fa, fb, "barrel", "nonkilling")){
			Body body = getFixtureByType(fa, fb, "barrel").getBody();
			float[] impulses = impulse.getNormalImpulses();
			
			if(impulses[0] > 3000)
			{
				Logger.log(this, impulses[0]);
				((UserData)body.getUserData()).playSound = true;
			}
		}
	}
	
	public void postStep()
	{

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
		else if(checkIsOneOfType(fa, fb, "jumpSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "jumpSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "wallSensorBody")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "wallSensorBody").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "leftRotationSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "leftRotationSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "rightRotationSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "rightRotationSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "headSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "headSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		return player;
	}
}
