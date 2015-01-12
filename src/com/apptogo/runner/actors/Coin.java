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
	private ParticleEffectActor effectActor;
	private PoolableBody poolableBody;
	
	private Sound sound;
	public Vector2 coinPosition;
	private boolean active;
	private CoinField containingCoinField;
	private static int counter = 0;
	public int myId = counter++;
	
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
		
		effectActor = new ParticleEffectActor("coins.p", 1, 5);
		gameWorld.getWorldStage().addActor(effectActor);
	}
	
	private void gainCoinResult()
	{
		effectActor.obtainAndStart(getX() + getWidth()/2, getY() + getHeight()/2, 1/PPM);
		sound.play(0.3f);
	}
	
	//int ctr222 = 0; int ctr111 = 0;
	public void attachBody(){//ctr111++;
		if(getBody() == null){//ctr222++;
			poolableBody = CoinsManager.getInstance().coinBodiesPool.obtain();
			Body body = getPoolableBody().body;
			body.setTransform(new Vector2(coinPosition.x/PPM - 16/PPM, coinPosition.y/PPM - 16/PPM), 0);
			setBody(body);
			log("ZAATACHOWANO BODY!");
		}
		//else
			//if(myId == 999 ) Logger.log(this, "BODY PODPIETE " + getBody().getPosition().x);
	}
	public void freeBody(){log("FREE BODY");
		if(getBody()!=null){
			CoinsManager.getInstance().coinBodiesPool.free(getPoolableBody());
			setBody(null);
		}
		
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if(active)
		{
			if(coinPosition.x/PPM < gameWorld.player.character.getBody().getPosition().x - 5)
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
				gainCoinResult();
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
			else
			{
				effectActor.setPosition(getX() + getWidth()/2, getY() + getHeight()/2);
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

	public PoolableBody getPoolableBody() {
		return poolableBody;
	}

	public void setPoolableBody(PoolableBody poolableBody) {
		this.poolableBody = poolableBody;
	}

	public CoinField getContainingCoinField() {
		return containingCoinField;
	}

	public void setContainingCoinField(CoinField containingCoinField) {
		this.containingCoinField = containingCoinField;
	}
}
