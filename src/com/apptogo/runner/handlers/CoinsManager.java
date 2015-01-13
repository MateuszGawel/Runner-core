package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;

import com.apptogo.runner.actors.Coin;
import com.apptogo.runner.actors.CoinField;
import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class CoinsManager 
{
	private static CoinsManager INSTANCE;
	
	public static void create()
	{		
		INSTANCE = new CoinsManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static CoinsManager getInstance()
	{
		return INSTANCE;
	}
	
	private GameWorld gameWorld;
	private int coinCounter;
	public ParticleEffectActor pooledEffectActor;
		
	private Array<CoinField> coinFields = new Array<CoinField>();
	public Array<CoinField> activeCoinFields = new Array<CoinField>();
	
	public Sound sound;
	public double soundId;
	public long lastTimePlayed = System.nanoTime();
	
	//private HashMap<Vector2, Boolean> coinPositions = new HashMap<Vector2, Boolean>();
	
	FPSLogger fps = new FPSLogger();
	
	public final Pool<Coin> coinsPool = new Pool<Coin>() 
	{
	    @Override
	    protected Coin newObject() 
	    {
	    	EllipseMapObject coinTemplate = new EllipseMapObject();
			coinTemplate.getEllipse().setSize(32.0f, 32.0f);
			Logger.log(this, "tworze coin numer: " + coinCounter++);
	    	Coin coin = new Coin(coinTemplate, gameWorld);
	    	
	    	return coin;
	    }
    };
    	
	public void createCoinsToPool(int numberOfCoins)
	{
		Array<Coin> coins = new Array<Coin>();
		for(int i=0; i<numberOfCoins; i++){
			coins.add(coinsPool.obtain());		
		}
		coinsPool.freeAll(coins);
		
		//pooledEffectActor = new ParticleEffectActor("coins.p", 40, 50, 40, 1/PPM);
		//gameWorld.getWorldStage().addActor(pooledEffectActor);
		
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/coin.ogg");
	}
			
	public void setGameWorld(GameWorld gameWorld)
	{
		this.gameWorld = gameWorld;
	}
	
	public void update()
	{	
		//fps.log();
		Logger.log(this, "W POOLU: " + coinsPool.getFree());
		//ustawianie monet wewnatrz coinFieldow
		for(CoinField coinField : coinFields)
		{			
			if( coinField.getBody().getPosition().x - gameWorld.player.character.getBody().getPosition().x <= 12 )
			{ 
				for( Vector2 coinPosition : coinField.coinPositions.keySet() )
				{
					if( coinField.coinPositions.size() > 0 && 
						coinField.coinPositions.get(coinPosition) == null && 
						coinPosition.x / PPM - gameWorld.player.character.getBody().getPosition().x <= 12 && 
						coinPosition.x / PPM - gameWorld.player.character.getBody().getPosition().x > -10 )
					{
						Coin coin = coinsPool.obtain();
						
						coin.initEmpty(coinPosition, coinField.id);
						
						coinField.coinPositions.put(coinPosition, coin);
					}
				}
			}
		}
		
		Vector2 distance = new Vector2();
		
		//obliczanie kolizji z monetami
		for(CoinField coinField : activeCoinFields)
		{
			for(Coin coin: coinField.coinPositions.values())
			{
				if(coin != null)
				{
					distance.set(coin.getPosition().x / PPM - gameWorld.player.character.getBody().getPosition().x,
								 coin.getPosition().y / PPM - gameWorld.player.character.getBody().getPosition().y);
					
					if( distance.len2() < (gameWorld.player.character.coinFixtureRadius / PPM) + (coin.radius / PPM) )
					{
						coin.collected = true;
					}
				}
			}
		}
	}
	
	public int addCoinField(CoinField coinField)
	{
		coinFields.add(coinField);
		
		return coinFields.size - 1;
	}
	
	public CoinField createCoinField(MapObject object, World world, GameWorld gameWorld)
	{
		CoinField newCoinFIeld = new CoinField(object, world, gameWorld);
		
		int cfID = this.addCoinField(newCoinFIeld);
		
		newCoinFIeld.id = cfID;
		
		return newCoinFIeld;
	}
		
	public void removeCoinPosition(Coin coin, int coinFieldId)
	{
		removeCoinPosition(coin, coinFieldId, false);
	}
	
	public void removeCoinPosition(Coin coin, int coinFieldId, boolean removePositionCompletely)
	{
		if( removePositionCompletely )
		{
			coinFields.get(coinFieldId).coinPositions.remove(coin.getPosition());
		}
		else
		{
			coinFields.get(coinFieldId).coinPositions.put(coin.getPosition(), null);
		}
	}

}
