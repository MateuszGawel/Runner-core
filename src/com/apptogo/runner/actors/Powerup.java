package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.SpaceWorld;
import com.apptogo.runner.world.WildWestWorld;
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
	private float dark = 1;
	private boolean growing;
	
	public enum PowerupAnimationState
	{
		NORMAL
	}
	
	public Powerup(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/powerup.pack", "powerup", 36, 0.03f, PowerupAnimationState.NORMAL, gameWorld);
		setAnimate(false);
				
		PowerupType powerup = PowerupType.getRandom();
		UserData userData = new UserData("powerup");
		userData.powerup = powerup.toString();
		
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.KinematicBody, Materials.obstacleGhostBody, "active");
		getBody().getFixtureList().get(0).setSensor(true);
		createFixture(Materials.obstacleSensor, userData);
		setOffset(-40f/PPM, -35f/PPM);
        
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/powerup.ogg");
		
        if(gameWorld instanceof WildWestWorld) effectActor = new ParticleEffectActor("powerup-wildwest.p");
        else if(gameWorld instanceof ForestWorld) effectActor = new ParticleEffectActor("powerup-forest.p");
        else if(gameWorld instanceof SpaceWorld) effectActor = new ParticleEffectActor("powerup-space.p");
		effectActor.scaleBy(1/PPM);
		gameWorld.getWorldStage().addActor(effectActor);
		
		CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 0) {
			
			@Override
			public void perform() {
				if(growing){
					dark+=0.008f;
					if(dark >= 1)
						growing = false;
				}
				else{
					dark -= 0.008f;
					if(dark <= 0.7)
						growing = true;
				}
			}
		});
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
		batch.setColor(dark, dark, dark, 1f);
		super.draw(batch, parentAlpha);
	}
}
