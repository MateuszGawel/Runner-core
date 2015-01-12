package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Coin extends Obstacle implements Poolable
{	

	private Body body;
	
	private Sound sound;
	public Vector2 coinPosition;
	private boolean active;
	private CoinField containingCoinField;
	private static int counter = 0;
	public int myId = counter++;
	private CoinsManager coinsManager = CoinsManager.getInstance();
	
	public enum CoinAnimationState
	{
		NORMAL
	}
	
	public Coin(MapObject object, World world, GameWorld gameWorld)
	{
		super("gfx/game/levels/coin.pack", "coin", 16, 0.03f, CoinAnimationState.NORMAL); log("UTWORZONO!");
		setAnimate(true);

		this.updatePosition = false;
		this.gameWorld = gameWorld;
		
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/coin.ogg");
	}
	
	private void gainCoinResult()
	{
		long startTime = System.nanoTime();
		
		long endTime = System.nanoTime();
		Logger.log(this, "1 linia: " + (endTime - startTime));	
		
//		startTime = System.nanoTime();
//		//sound.play(0.3f);
//		endTime = System.nanoTime();
//		Logger.log(this, "2 linia: " + (endTime - startTime));	
	}
	

	public void attachBody(){
		if(getBody() == null){
			body = CoinsManager.getInstance().coinBodiesPool.obtain();
			body.setTransform(new Vector2(coinPosition.x/PPM - 16/PPM, coinPosition.y/PPM - 16/PPM), 0);
			setBody(body);
			log("ZAATACHOWANO BODY!");
		}
	}
	public void freeBody(){
		if(getBody()!=null){
			CoinsManager.getInstance().coinBodiesPool.free(body);
			body.setAwake(true);
			setBody(null);
		}
		
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if(active)
		{
			
			if(coinPosition.x/PPM < gameWorld.player.character.getBody().getPosition().x - 11)
			{
				CoinsManager.getInstance().coinsPool.free(this);
				containingCoinField.usedCoins.removeValue(this, true);
				log("DEAKTYWACJA!");
				active = false;
				freeBody();
				containingCoinField = null;
			}
			else if( getBody() != null && ((UserData)getBody().getUserData()).collected )				
			{
				coinsManager.pooledEffectActor.obtainAndStart(getX() + getWidth()/2, getY() + getHeight()/2);
				sound.play(0.3f);	
				getBody().setTransform(new Vector2(-100f, 0), 0);
				this.remove();
				((UserData)getBody().getUserData()).collected = false;
				CoinsManager.getInstance().coinsPool.free(this);
				containingCoinField.usedCoins.removeValue(this, true);
				containingCoinField.coinPositions.remove(this.position);
				active = false;
				containingCoinField = null;
				freeBody();
			}
		}
	}
		
	public void initEmpty(Vector2 nextPosition){

		gameWorld.getWorldStage().addActor(this);
		
		setOffset(-16/PPM, -16/PPM);
		this.coinPosition = nextPosition;
		//if(myId==999)		Logger.log(this, "BBB inicuje monete nr "+myId+" na: " + nextPosition.x/PPM);
		animationManager.setCurrentAnimationState(null);
		animationManager.setCurrentAnimationState(CoinAnimationState.NORMAL);
		setPosition(nextPosition.x/PPM -32/PPM, nextPosition.y/PPM-32/PPM);
		active = true;	
	}
	private void log(Object msg)
	{
		if(myId == 999) Logger.log(this, msg);
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

	public CoinField getContainingCoinField() {
		return containingCoinField;
	}

	public void setContainingCoinField(CoinField containingCoinField) {
		this.containingCoinField = containingCoinField;
	}
}
