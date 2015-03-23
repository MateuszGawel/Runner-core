package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Bush extends Obstacle{

	private float initialX, initialY;
	private UserData userData;
	private Sound sound;
	
	public Bush(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/bush.png");
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.DynamicBody, Materials.bushBody, "bush");
		initialX = getBody().getPosition().x;
		initialY = getBody().getPosition().y;
		setOffset(-12/PPM, -12/PPM);
		userData = ((UserData)getBody().getUserData());
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/bush.ogg");
	}
	@Override
	public void act(float delta){
		super.act(delta);
		
		if(getBody().getAngularVelocity() < 5)
			getBody().applyTorque(5f, true);
		
		if(Math.abs(getBody().getLinearVelocity().x) > 15){
			getBody().applyTorque(-10f, true);
			getBody().setLinearVelocity(-10, 1);
		}
		else if(getBody().getLinearVelocity().x > -10)
			getBody().applyForceToCenter(-15, 0, true);
		
		if(getBody().getPosition().x < 0)
			getBody().setTransform(initialX, initialY, 0);
		if(getBody().getLinearVelocity().x < 1)
			getBody().applyForceToCenter(-100, 100, true);
		
		if(userData.playSound){
			userData.playSound = false;
			sound.play(getSoundVolume());
		}
			
	}
}
