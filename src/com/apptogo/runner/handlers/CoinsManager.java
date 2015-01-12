package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Coin;
import com.apptogo.runner.actors.CoinField;
import com.apptogo.runner.actors.PoolableBody;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
	
	private World world;
	private GameWorld gameWorld;
	private int coinCounter;
	private Array<CoinField> coinFields = new Array<CoinField>();
	//private HashMap<Vector2, Boolean> coinPositions = new HashMap<Vector2, Boolean>();
	
	FPSLogger fps = new FPSLogger();
	
	public final Pool<Coin> coinsPool = new Pool<Coin>() 
	{
	    @Override
	    protected Coin newObject() 
	    {
	    	EllipseMapObject coinTemplate = new EllipseMapObject();
			coinTemplate.getEllipse().setSize(32.0f, 32.0f);
	    	
	    	Coin coin = new Coin(coinTemplate, world, gameWorld);
	    	
	    	return coin;
	    }
    };
    
	public final Pool<PoolableBody> coinBodiesPool = new Pool<PoolableBody>() 
	{
	    @Override
	    protected PoolableBody newObject() 
	    {
	    	CircleShape circleShape = new CircleShape();
	    	circleShape.setRadius(16/PPM);
	  
	    	Logger.log(this, "tworze body numer: " + coinCounter++);
			FixtureDef fixtureDef = Materials.obstacleSensor;
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;

			fixtureDef.shape = circleShape;
			
			PoolableBody body = new PoolableBody(bodyDef, fixtureDef, "coin", world);
			
	    	return body;
	    }
    };
	
	public void createCoinsToPool(int numberOfCoins){
		Array<Coin> coins = new Array<Coin>();
		for(int i=0; i<numberOfCoins; i++){
			coins.add(coinsPool.obtain());		
		}
		coinsPool.freeAll(coins);
	}
	public void createBodiesToPool(int numberOfBodies){
		Array<PoolableBody> bodies = new Array<PoolableBody>();
		for(int i=0; i<numberOfBodies; i++){
			bodies.add(coinBodiesPool.obtain());		
		}
		coinBodiesPool.freeAll(bodies);
	}
		
	public void setWorlds(World world, GameWorld gameWorld)
	{ 
		this.world = world;
		this.gameWorld = gameWorld;
	}
	
	public void update(){
		
		fps.log();
		
		//Logger.log(this,"W poolu mam jeszcze: " + CoinsManager.getInstance().coinsPool.getFree());
		//Logger.log(this,"pBodies mam jeszcze: " + CoinsManager.getInstance().coinBodiesPool.getFree());
		
		for(CoinField coinField : coinFields){
			for(Vector2 coinPosition : coinField.coinPositions.keySet()){
				if( coinField.coinPositions.size() > 0 && !coinField.coinPositions.get(coinPosition) && 
						coinPosition.x / PPM - gameWorld.player.character.getBody().getPosition().x <= 6 && coinPosition.x / PPM - gameWorld.player.character.getBody().getPosition().x > 2 ){
					Coin coin = coinsPool.obtain();
					coin.initEmpty(coinPosition);
					coinField.coinPositions.put(coinPosition, true);
					
					coinField.usedCoins.add(coin);
					coin.setContainingCoinField(coinField);
				}
			}
		}
	}
	
	public void addCoinField(CoinField coinField){
		this.coinFields.add(coinField);
//		for(Vector2 coinPosition : coinField.coinPositions.keySet())
//			coinPositions.put(coinPosition, false);
	}
	

}
