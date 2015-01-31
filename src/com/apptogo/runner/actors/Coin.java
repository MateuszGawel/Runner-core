package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Coin extends Obstacle implements Poolable
{		
	private boolean active;
	public boolean collected = false;
	private int coinFieldId;
	private CoinsManager coinsManager = CoinsManager.getInstance();
	
	public final float radius = 16.0f;
	
	public enum CoinAnimationState
	{
		NORMAL
	}
	
	public Coin(MapObject object, GameWorld gameWorld)
	{
		super("coin", 16, 0.03f, CoinAnimationState.NORMAL);
		setAnimate(true);

		this.updatePosition = false;
		this.gameWorld = gameWorld;
		
		this.coinFieldId = -1;
		this.handleSoundVolume = false;
	}
	

	@Override
	public void act(float delta) {
    	super.act(delta);
		
		if(active)
		{
			
			if(getPosition().x/PPM < gameWorld.player.character.getBody().getPosition().x - 12)
			{
				coinsManager.coinsPool.free(this);
				
				//coinsManager.removeCoinPosition(this, coinFieldId);
				coinsManager.removeCoinPosition(this, coinFieldId, true); //czy jest sens w ogole zostawiac ta pozycje? Przeciez i tak nie bedziemy dogenerowywac monety :) chyba ze bedziemy to wtedy trzeba wywalic ta linijke i odkomentowac ta powyzej 
				
				active = false;
				coinFieldId = -1;
			}
			else if( this.collected )				
			{					
				if(System.nanoTime() - coinsManager.lastTimePlayed > 50000000){
					coinsManager.sound.stop();
					coinsManager.soundId = coinsManager.sound.play(0.3f);	
					coinsManager.lastTimePlayed = System.nanoTime();
					coinsManager.pooledEffectActor.obtainAndStart(getX(), getY());
				}

				this.remove();

				coinsManager.coinsPool.free(this);
				coinsManager.removeCoinPosition(this, coinFieldId, true);
				
				this.collected = false;
				this.active = false;
				this.coinFieldId = -1;
			}
		}	
	}

	public void initEmpty(Vector2 nextPosition, int coinFieldId )
	{
		gameWorld.getWorldStage().addActor(this);
		
		setOffset(-16/PPM, -16/PPM);
		this.position = nextPosition;
		
		animationManager.setCurrentAnimationState(null);
		animationManager.setCurrentAnimationState(CoinAnimationState.NORMAL);
		setPosition(nextPosition.x/PPM -32/PPM, nextPosition.y/PPM-32/PPM);
		active = true;	
		
		this.coinFieldId = coinFieldId;
	}

	@Override
	public void reset() {
		
		
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
