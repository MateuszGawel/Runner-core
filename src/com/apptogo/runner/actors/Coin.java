package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Coin extends Obstacle implements Poolable
{	
	private ParticleEffectActor effectActor;
	private Sound sound;
	
	public enum CoinAnimationState
	{
		NORMAL
	}
	
	public Coin(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/coin.pack", "coin", 16, 0.03f, CoinAnimationState.NORMAL, gameWorld);
		setAnimate(true);

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
		
		if( ((UserData)getBody().getUserData()).key.equals("inactive") )
		{
			gainCoinResult();
			getBody().setTransform(new Vector2(-100f, 0), 0);
			CoinsManager.getInstance().freeCoin(this);
		}
		else if(gameWorld.player.character.getBody().getPosition().x - this.getX() > 20 && ((UserData)getBody().getUserData()).key.equals("inactive") )
		{Logger.log(this, "COIN____ BODYPOS: " + gameWorld.player.character.getBody().getPosition().x + ", GETX" + this.getX() );
			CoinsManager.getInstance().freeCoin(this);
		}
		else
		{
			effectActor.setPosition(getX() + getWidth()/2, getY() + getHeight()/2);
		}
	}
	
	public void setNextPosition(Vector2 nextPosition)
	{
		getBody().setTransform( new Vector2( nextPosition.x / PPM, nextPosition.y / PPM), 0);
		animationManager.setCurrentAnimationState(null);
		animationManager.setCurrentAnimationState(CoinAnimationState.NORMAL);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	@Override
	public void reset() 
	{
		((UserData)getBody().getUserData()).key = "active";
		//getBody().setAwake(true);
		//getBody().setActive(false);
		//this.remove();
	}
}
