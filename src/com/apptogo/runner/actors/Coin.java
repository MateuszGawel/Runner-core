package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
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

public class Coin extends Obstacle
{	
	private ParticleEffectActor effectActor;
	private Sound sound;
	
	public enum CoinAnimationState
	{
		NORMAL
	}
	
	public Coin(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/coin.pack", "coin", 16, 0.03f, CoinAnimationState.NORMAL);
		super.animate = true;
		
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleGhostBody, "active");

		createFixture(Materials.obstacleSensor, "coin");
		
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/coin.ogg");
		
		effectActor = new ParticleEffectActor("coins.p");
		effectActor.scaleBy(1/PPM);
		gameWorld.getWorldStage().addActor(effectActor);
	}
	
	private void gainCoinResult()
	{
		effectActor.start();
		sound.play(0.3f);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		if(((UserData)getBody().getUserData()).key.equals("inactive") )
		{
			gainCoinResult();
			
			getBody().setTransform(new Vector2(-100f, 0), 0);
			getBody().setAwake(true);
			getBody().setActive(false);
			this.remove();
		}
		else
			effectActor.setPosition(getX() + getWidth()/2, getY() + getHeight()/2);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
