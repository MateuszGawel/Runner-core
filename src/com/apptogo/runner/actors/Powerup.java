package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Powerup extends Obstacle{

	public enum PowerupAnimationState
	{
		NORMAL
	}
	
	public Powerup(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/powerup.pack", "powerup", 36, 0.03f, PowerupAnimationState.NORMAL);
		super.animate = true;
		
		String userData = "powerup";
		
		//losowanie powerupa - na razie na abilities to robie [zeby bylo byle co] ale trzeba bedzie dodac nowy enum
		//info o powerupie dodaje w userdata bo musze jakos w contactListenerze miec do tego dostep
		CharacterAbilityType ability = CharacterAbilityType.values()[(int)(Math.random() * 3.9)];
		
		userData += "|" + ability.toString();
		
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.powerUpBody, userData, true);
		
		setOffset(-40f/PPM, -35f/PPM);
	}
}
