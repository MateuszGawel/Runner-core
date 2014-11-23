package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Powerup extends Obstacle{

	private float stateTime = 0;
	private float timeElapsed = 0;
	private float interval = 1f;
	private Vector2 velocity = new Vector2(0, 0.2f);
	private ParticleEffectActor effectActor;
	private Sound sound;
	
	public enum PowerupAnimationState
	{
		NORMAL
	}
	
	public Powerup(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/powerup.pack", "powerup", 36, 0.03f, PowerupAnimationState.NORMAL);
		super.animate = true;
				
		PowerupType powerup = PowerupType.getRandom();
		UserData userData = new UserData("powerup");
		userData.powerup = powerup.toString();
		
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.KinematicBody, Materials.obstacleGhostBody, "active");
		createFixture(Materials.obstacleSensor, userData);
		setOffset(-40f/PPM, -35f/PPM);
        
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/powerup.ogg");
		
        effectActor = new ParticleEffectActor("powerup.p");
		effectActor.scaleBy(1/PPM);
		gameWorld.getWorldStage().addActor(effectActor);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);	
		stateTime += delta;
		if((stateTime - timeElapsed)/interval >= 1){
			timeElapsed+=interval;
			velocity.y *= -1;
		}
		getBody().setLinearVelocity(velocity);
		
		if(((UserData)getBody().getUserData()).key.equals("inactive") )
		{
			getBody().setTransform(new Vector2(-100f, 0), 0);
			effectActor.start();
			sound.play();
			this.remove();
//			if(effectActor.getEffect().isComplete())
//				effectActor.remove();
		}
		else
			effectActor.setPosition(getX() + getWidth()/2, getY() + getHeight()/2);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
