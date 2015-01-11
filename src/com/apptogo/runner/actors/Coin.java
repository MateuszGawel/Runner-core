package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
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
	
	public enum CoinAnimationState
	{
		NORMAL
	}
	
	public Coin(MapObject object, World world, GameWorld gameWorld)
	{
		super("gfx/game/levels/coin.pack", "coin", 16, 0.03f, CoinAnimationState.NORMAL);
		setAnimate(true);
		gameWorld.getWorldStage().addActor(this);

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
	
	public void attachBody(){
		if(getBody() == null){
			poolableBody = CoinsManager.getInstance().coinBodiesPool.obtain();
			Body body = getPoolableBody().body;
			body.setTransform(new Vector2(coinPosition.x/PPM - 16/PPM, coinPosition.y/PPM - 16/PPM), 0);
			setBody(body);
			
		}
	}
	public void freeBody(){
		if(getBody()!=null){
			CoinsManager.getInstance().coinBodiesPool.free(getPoolableBody());
			setBody(null);
		}
		
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
	//	if(active) Logger.log(this, getX() + " " + gameWorld.player.character.getBody().getPosition().x + " ROZNICA: " + (getX() - gameWorld.player.character.getBody().getPosition().x));
		if(active && coinPosition.x/PPM < gameWorld.player.character.getBody().getPosition().x - 5){
			Logger.log(this, "Sciagam monete z: " + coinPosition.x/PPM + " a player jest na: " + gameWorld.player.character.getBody().getPosition().x);
			CoinsManager.getInstance().coinsPool.free(this);
			containingCoinField.usedCoins.removeValue(this, true);
			active = false;
			containingCoinField = null;
		}
//		if(active){
//			if(((UserData)getBody().getUserData()).collected)
//			{
//				gainCoinResult();
//				getBody().setTransform(new Vector2(-100f, 0), 0);
//				((UserData)getBody().getUserData()).collected = false;
//				CoinsManager.getInstance().coinsPool.free(this);
//				containingCoinField.usedCoins.removeValue(this, true);
//				containingCoinField.coinPositions.remove(this.position);
//				active = false;
//				containingCoinField = null;
//				getBody().setActive(false);
//			}
//			else{
//				effectActor.setPosition(getX() + getWidth()/2, getY() + getHeight()/2);
//			}
//		}
		
	}
	
	
	public void initEmpty(Vector2 nextPosition){
		setOffset(-16/PPM, -16/PPM);
		this.coinPosition = nextPosition;
		Logger.log(this, "inicuje monete na: " + nextPosition.x/PPM);
		animationManager.setCurrentAnimationState(null);
		animationManager.setCurrentAnimationState(CoinAnimationState.NORMAL);
		setPosition(nextPosition.x/PPM -32/PPM, nextPosition.y/PPM-32/PPM);
		active = true;	
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
