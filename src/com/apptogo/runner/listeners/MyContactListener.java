package com.apptogo.runner.listeners;

import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.FlagsHandler;
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
	
	public MyContactListener(GameWorld gameWorld)
	{
		this.gameWorld = gameWorld;
	}

	@Override
	public void beginContact(Contact contact)
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		Player player = checkIfFixtureIsPlayer(fa, fb);		

		if(player != null){
			FlagsHandler flags = player.character.flags;
			
			//smierc TOP
			if(checkFixturesTypes(fa, fb, "killingTop", "mainBody")){	
				if(flags.isCanDie()){
					flags.setDieTop(true);
				}
			}
			
			//smierc BOTTOM
			if( checkFixturesTypes(fa, fb, "killingBottom", "mainBody")){
				if(flags.isCanDie()){
					flags.setDieBottom(true);
				}
			}
			
			//smierc od ogniska
			if( checkFixturesTypes(fa, fb, "bonfire", "mainBody")){
				if(flags.isCanDie()){
					flags.setDieDismemberment(true);
				}
			}	
	
			//sensory playera
			if(checkFixturesTypes(fa, fb, "footSensor", "nonkilling")){
				flags.incrementFootSensor();
			}		
			if(checkFixturesTypes(fa, fb, "jumpSensor", "nonkilling")){
				flags.incrementJumpSensor();
			}	
			if(checkFixturesTypes(fa, fb, "wallSensor", "nonkilling")){
				flags.incrementWallSensor();
			}
			if(checkFixturesTypes(fa, fb, "standupSensor", "nonkilling")){
				flags.incrementStandupSensor();
			}
			if(checkFixturesTypes(fa, fb, "headSensor", "nonkilling")){
				flags.incrementHeadSensor();
			}
			if(checkFixturesTypes(fa, fb, "footSensor", "barrel")){
				flags.incrementBarrelSensor();
			}
	//		if(checkFixturesTypes(fa, fb, "leftRotationSensor", "nonkilling")){
	//			player.character.incrementLeftRotationSensor();
	//		}
	//		if(checkFixturesTypes(fa, fb, "rightRotationSensor", "nonkilling")){
	//			player.character.incrementRightRotationSensor();
	//		}
			
			//boost po l¹dowaniu
			if( checkFixturesTypes(fa, fb, "nonkilling", "mainBody") || checkFixturesTypes(fa, fb, "barrel", "mainBody")){		
				if(flags.getQueuedBoost() == 0){
					flags.setQueuedBoost(player.character.getBody().getLinearVelocity().x);
					Logger.log(this, "kolejkuje z predkoscia: " + player.character.getBody().getLinearVelocity().x);
				}
			}	
			
			//beczki
			if(checkFixturesTypes(fa, fb, "barrel", "mainBody") || checkFixturesTypes(fa, fb, "barrel", "wallSensorBody")){
				
				Fixture playerFixture = getFixtureByType(fa, fb, "mainBody");
				if(playerFixture == null)
					playerFixture = getFixtureByType(fa, fb, "wallSensorBody");
				Fixture barrelFixture = getFixtureByType(fa, fb, "barrel");			
				
				if(!((UserData)barrelFixture.getBody().getUserData()).active){
					((UserData)barrelFixture.getBody().getUserData()).active = true;
					((UserData)barrelFixture.getBody().getUserData()).playSound = true;
				}
					
				if(flags.isCanDie() &&
						(Math.abs(barrelFixture.getBody().getLinearVelocity().x) > 10f 
								|| Math.abs(barrelFixture.getBody().getLinearVelocity().y) > 7f ))
				{
					flags.setDieBottom(true);
					((UserData)barrelFixture.getBody().getUserData()).playSound = true;
				}
			}
	
			//bagno
			if(checkFixturesTypes(fa, fb, "swamp", "footSensor")){
				if(flags.isCanDie())
					flags.incrementSwampSensor();
			}
			
			//trampolina grzyb
			if(checkFixturesTypes(fa, fb, "mushroom", "footSensor")){
				Fixture mushroomFixture = getFixtureByType(fa, fb, "mushroom");		
				if(flags.isAlive()){	
					flags.incrementMushroomSensor();
					mushroomFixture.setUserData( new UserData("mushroomWorking") );
				}
			}
			
			//katapulta
			if( checkFixturesTypes(fa, fb, "catapult", "footSensor")){
				Fixture catapultFixture = getFixtureByType(fa, fb, "catapult");
				if(flags.isAlive())
				{		
					flags.incrementCatapultSensor();
					catapultFixture.getBody().setUserData( new UserData("catapultWorking") );
				}		
			}
			
			//coin
			if( checkFixturesTypes(fa, fb, "coin", "coinCollectorSensor") ){
				Fixture fixture = getFixtureByType(fa, fb, "coin");
				fixture.getBody().setUserData(new UserData("inactive"));
				player.character.incrementCoinCounter();
			}
			
			//powerup
			if(checkFixturesTypes(fa, fb, "powerup", "mainBody")){
				if(!flags.isPowerupSet())
				{
					Fixture fixture = getFixtureByType(fa, fb, "powerup");
					
					String powerupKey = ((UserData)fixture.getUserData()).powerup;
					player.character.setPowerup( PowerupType.parseFromString(powerupKey) );
	
					fixture.getBody().setUserData(new UserData("inactive"));
				}
			}
			
			//podnoszenie aliena dziala tylko w singlu - do zrobienia
//			if(checkIsOneOfType(fa, fb, "liftField") && ((Alien)gameWorld.player.character).liftField.isActive()){
//				Fixture fixture = getFixtureByType(fa, fb, "liftField");
//				fixture = ( fixture == fb )?fa:fb; //bo chcemy fixture tego drugiego a nie liftField
//				
//				String fixtureType = ((UserData)fb.getUserData()).key;
//				
//				if( !( fixtureType.equals("player") || fixtureType.equa)ls("wallSensor") || fixtureType.equals("footSensor") ) )
//				{
//					((Alien)gameWorld.player.character).liftField.addBodyToLift( fixture.getBody() );
//					
//					fixture.getBody().applyLinearImpulse(0, ((Alien)gameWorld.player.character).liftField.initialBoost, 0, 0, true);
//				}
//			}
			
			//meta - koniec gry
			if(checkFixturesTypes(fa, fb, "mainBody", "finishingLine")){
				flags.setFinished(true);
			}
		}
		
		
		
		//zmiana kierunku je¿a
		if( checkFixturesTypes(fa, fb, "hedgehog", "hedgehogStopper")){
			Fixture hedgeHogFixture = getFixtureByType(fa, fb, "hedgehog");
			((UserData)hedgeHogFixture.getBody().getUserData()).changeDirection = true;
		}
		if(checkFixturesTypes(fa, fb, "barrel", "barrel")){
			((UserData)fa.getBody().getUserData()).active = true;
			((UserData)fb.getBody().getUserData()).active = true;
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		Player player = checkIfFixtureIsPlayer(fa, fb);
		
		if(player != null){
			FlagsHandler flags = player.character.flags;
			
			if(checkFixturesTypes(fa, fb, "footSensor", "nonkilling")){
				flags.decrementFootSensor();
			}
			if(checkFixturesTypes(fa, fb, "wallSensor", "nonkilling")){
				flags.decrementWallSensor();
			}
			if(checkFixturesTypes(fa, fb, "jumpSensor", "nonkilling")){
				flags.decrementJumpSensor();
			}
			if(checkFixturesTypes(fa, fb, "standupSensor", "nonkilling")){
				flags.decrementStandupSensor();
			}
			if(checkFixturesTypes(fa, fb, "headSensor", "nonkilling")){
				flags.decrementHeadSensor();
			}
			if(checkFixturesTypes(fa, fb, "footSensor", "mushroom")){
				flags.decrementMushroomSensor();
			}
			if(checkFixturesTypes(fa, fb, "footSensor", "swamp")){
				flags.decrementSwampSensor();
			}
			if(checkFixturesTypes(fa, fb, "footSensor", "catapult")){
				flags.decrementCatapultSensor();
			}
	//		if(checkFixturesTypes(fa, fb, "leftRotationSensor", "nonkilling")){
	//			player.character.decrementLeftRotationSensor();
	//		}
	//		if(checkFixturesTypes(fa, fb, "rightRotationSensor", "nonkilling")){
	//			player.character.decrementRightRotationSensor();
	//		}
			
			//liftField
	//		if(checkIsOneOfType(fa, fb, "liftField")){
	//			Fixture fixture = getFixtureByType(fa, fb, "liftField");
	//			((Alien)gameWorld.player.character).liftField.removeBodyToLift( fixture.getBody() );
	//		}
			
			//barrel
			if(checkFixturesTypes(fa, fb, "footSensor", "barrel")){
				flags.decrementBarrelSensor();
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		Player player = checkIfFixtureIsPlayer(fa, fb);
		
		if(player != null)
		{			
			if(checkFixturesTypes(fa, fb, "mainBody", "nonkilling") || checkFixturesTypes(fa, fb, "mainBody", "barrel")){
				Fixture fixture = getFixtureByType(fa, fb, "footSensor");
				if(player != null) player.character.speedBeforeLand = fixture.getBody().getLinearVelocity().x;
			}
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
//		else{
//			try {
//				player = gameWorld.getEnemy(playerName);
//			} catch (PlayerDoesntExistException e) {
//				Logger.log(this, "There is no player with name: " + playerName);
//			}
//		}
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
		else if(checkIsOneOfType(fa, fb, "coinCollectorSensor")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "coinCollectorSensor").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		else if(checkIsOneOfType(fa, fb, "mainBody")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "mainBody").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		return player;
	}
}
