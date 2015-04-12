package com.apptogo.runner.listeners;

import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
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

		if(player != null && !checkIsIgnored(fa, fb)){
			FlagsHandler flags = player.character.flags;

			//smierc
			if(checkFixturesTypes(fa, fb, "obstacle", "mainBody")){
				if(flags.isCanDie()){
					if(((UserData)getFixtureByType(fa, fb, "obstacle").getUserData()).killingBottom)
						flags.setDieBottom(true);
					else if(((UserData)getFixtureByType(fa, fb, "obstacle").getUserData()).killingTop)	
						flags.setDieTop(true);
					else if(((UserData)getFixtureByType(fa, fb, "obstacle").getUserData()).killingDismemberment)
						flags.setDieDismemberment(true);
				}
			}
			
			//powerup
			if(checkFixturesTypes(fa, fb, "powerup", "mainBody")){
				if(!flags.isPowerupSet())
				{
					Fixture fixture = getFixtureByType(fa, fb, "powerup");
					
					String powerupKey = ((UserData)fixture.getUserData()).powerup;
					player.character.setPowerup( PowerupType.parseFromString(powerupKey) );
	
					( (UserData) (fixture.getBody().getUserData()) ).key = "inactive";
				}
			}
			
			//boost po l¹dowaniu
			if( checkFixturesTypes(fa, fb, "nonkilling", "mainBody") || checkFixturesTypes(fa, fb, "barrel", "mainBody")){		
				if(flags.getQueuedBoost() == 0){
					flags.setQueuedBoost(player.character.getBody().getLinearVelocity().x);
				}
			}	
	
			//meta - koniec gry
			if(checkFixturesTypes(fa, fb, "mainBody", "finishingLine")){
				flags.setFinished(true);
			}
			
			//gravity field
			if(checkFixturesTypes(fa, fb, "gravityField", "mainBody")){
					Fixture bodyFixture = getFixtureByType(fa, fb, "gravityField");
					player.character.flags.setGravityRotationSwitch(true);
			}
			
			
			//INNE NI¯ MAINBODY
			
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
				
				Fixture f = getFixtureByType(fa, fb, "barrel");
				( (UserData)f.getUserData() ).key = "barrel_touched";
				
				Logger.log(this, "+ BARREL SENSOR = " + flags.getBarrelSensor() );
			}
	//		if(checkFixturesTypes(fa, fb, "leftRotationSensor", "nonkilling")){
	//			player.character.incrementLeftRotationSensor();
	//		}
	//		if(checkFixturesTypes(fa, fb, "rightRotationSensor", "nonkilling")){
	//			player.character.incrementRightRotationSensor();
	//		}
			
			//beczki
			if(checkFixturesTypes(fa, fb, "barrel", "mainBody") || checkFixturesTypes(fa, fb, "barrel", "wallSensorBody")){
				Fixture barrelFixture = getFixtureByType(fa, fb, "barrel");			
			
				((UserData)barrelFixture.getBody().getUserData()).active = true;
				if(((UserData)barrelFixture.getUserData()).killingBottom)
					flags.setDieBottom(true);
				
				((UserData)barrelFixture.getBody().getUserData()).playSound = true;
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
					flags.setQueuedMushroomJump(true);
					( (UserData) (mushroomFixture.getUserData()) ).key = "mushroomWorking";
				}
			}
			
			//katapulta
			if( checkFixturesTypes(fa, fb, "catapult", "footSensor")){
				Fixture catapultFixture = getFixtureByType(fa, fb, "catapult");
				if(flags.isAlive())
				{		
					flags.setQueuedCatapultJump(true);
					( (UserData) (catapultFixture.getBody().getUserData()) ).key = "catapultWorking";
				}		
			}

			//coinField
			if( checkFixturesTypes(fa, fb, "coinField", "mainBody") )
			{
				Fixture fixture = getFixtureByType(fa, fb, "coinField");
				((UserData)fixture.getBody().getUserData()).active = true;
			}
			
			//UMIEJETNOSCI
			//strzaly
			if(checkFixturesTypes(fa, fb, "mainBody", "arrow")){
				Fixture arrowFixture = getFixtureByType(fa, fb, "arrow");
				String arrowOwner = ((UserData)arrowFixture.getUserData()).playerName;
				if(/*player.character.flags.isMe() &&*/ arrowOwner!=player.getName())
					player.character.flags.setQueuedDeathBottom(true);
			}
			//bomby
			if(checkFixturesTypes(fa, fb, "mainBody", "bombExplosion")){
				Fixture bombExplosionFixture = getFixtureByType(fa, fb, "bombExplosion");
				String bombOwner = ((UserData)bombExplosionFixture.getUserData()).playerName;
				if(/*player.character.flags.isMe() &&*/ bombOwner!=player.getName())
					player.character.flags.setQueuedDeathDismemberment(true);
			}
			if(checkFixturesTypes(fa, fb, "mainBody", "bomb")){
				Fixture bomb = getFixtureByType(fa, fb, "bomb");
				String bombOwner = ((UserData)bomb.getUserData()).playerName;
				if(/*player.character.flags.isMe() &&*/ bombOwner!=player.getName())
					((UserData)bomb.getBody().getUserData()).collected = true;
			}
			//podnoszenie aliena
			if(checkFixturesTypes(fa, fb, "mainBody", "liftField")){
				Fixture liftFixture = getFixtureByType(fa, fb, "liftField");
				String liftFieldOwner = ((UserData)liftFixture.getUserData()).playerName;
				//moja umiejetnosc na mnie ma sie nie wykonac
				if(/*player.character.flags.isMe() &&*/ liftFieldOwner!=player.getName())
					player.character.flags.setQueuedLift(true);
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
		
		if(player != null && !checkIsIgnored(fa, fb)){
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
			//bagno
			if(checkFixturesTypes(fa, fb, "swamp", "footSensor")){
				if(flags.isCanDie())
					flags.decrementSwampSensor();
			}
			//barrel
			if(checkFixturesTypes(fa, fb, "footSensor", "barrel_touched")){
				flags.decrementBarrelSensor();
				
				Logger.log(this, "- BARREL SENSOR = " + flags.getBarrelSensor() );
			}
			//gravity field
			if(checkFixturesTypes(fa, fb, "gravityField", "mainBody")){
					Fixture bodyFixture = getFixtureByType(fa, fb, "gravityField");
					player.character.flags.setGravityRotationSwitch(true);
			}
			//coinField
			if( checkFixturesTypes(fa, fb, "coinField", "mainBody") ){
				Fixture fixture = getFixtureByType(fa, fb, "coinField");
				((UserData)fixture.getBody().getUserData()).active = false;
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
				Logger.log(this, "impulse; " + impulses[0]);
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
	
	private boolean checkIsIgnored(Fixture fixtureA, Fixture fixtureB)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if
			(
					((UserData)fixtureA.getUserData()).ignoreContact == true
					||
					((UserData)fixtureB.getUserData()).ignoreContact == true 
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
		else if(checkIsOneOfType(fa, fb, "mainBody")){
			String playerName = ((UserData)getFixtureByType(fa, fb, "mainBody").getBody().getUserData()).playerName;
			player = findPlayerByName(playerName);
		}
		return player;
	}
}
