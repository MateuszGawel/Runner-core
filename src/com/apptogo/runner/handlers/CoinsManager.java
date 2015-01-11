package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Comparator;
import java.util.LinkedList;

import com.apptogo.runner.actors.Coin;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Intersector;
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
		
		//Logger.log(INSTANCE, "Manager has been created", LogLevel.LOW);
	}
	public static void destroy()
	{
		//Logger.log(INSTANCE, "Manager has been destroyed", LogLevel.LOW);
		
		INSTANCE = null;
	}
	public static CoinsManager getInstance()
	{
		return INSTANCE;
	}
	
	private final int AMOUNT = 50;
	
	private World world;
	private GameWorld gameWorld;
	private LinkedList<Vector2> coinPositions;
	private Array<Vector2> positionsArray;
	
	private Comparator<Vector2> vectorComparator = new Comparator<Vector2>() {
		@Override
		public int compare(Vector2 v1, Vector2 v2) 
		{
			if(v1.x < v2.x)
				return -1;
			
			if(v1.x > v2.x)
				return 1;
			
			return 0;
		}
	};
	int ctr = 0; FPSLogger fps = new FPSLogger();
	private final Pool<Coin> coinsPool = new Pool<Coin>() 
	{
	    @Override
	    protected Coin newObject() 
	    {ctr++;
	    	EllipseMapObject coinTemplate = new EllipseMapObject();
			coinTemplate.getEllipse().setSize(22.0f, 22.0f);
	    	
	    	Coin coin = new Coin(coinTemplate, world, gameWorld);
	    		    	
	    	return coin;
	    }
    };
	
	public CoinsManager()
	{
		coinPositions = new LinkedList<Vector2>();
		positionsArray = new Array<Vector2>();
	}
	
	public void freeCoin(Coin coin)
	{
		coinsPool.free(coin);
		
	}
	
	public void update()
	{Logger.log(this, "ILOSC MONET W POOLU: " + ctr);
	fps.log();
		if( coinPositions.size() > 0 && coinPositions.getFirst().x / PPM - gameWorld.player.character.getBody().getPosition().x <= 20 )
		{
			Logger.log(this, " GETFIRST: " + coinPositions.getFirst().x / PPM + ", BODYPOS: " + gameWorld.player.character.getBody().getPosition().x );
			
			Coin coin = coinsPool.obtain();
			coin.setNextPosition(getNextPosition());
		}
	}
		
	public void setWorlds(World world, GameWorld gameWorld)
	{  
		if(world == null) Logger.log(this, "NULL WORLD");
		if(gameWorld == null) Logger.log(this, "NULL gameWorld");
		
		this.world = world;
		this.gameWorld = gameWorld;
	}
	
	//sets first x coins
	public void copyToList()
	{ 	
		for(int i = 0; i < positionsArray.size; i++)
		{
			Logger.log(this, "! " + i + ",   " + positionsArray.get(i).x + ":" + positionsArray.get(i).y);
			coinPositions.addLast( positionsArray.get(i) );
		}
	}
	
	public Vector2 getNextPosition()
	{
		if( coinPositions.size() <= 0 )
		{
			return new Vector2(-100, 0);
		}
		else
		{
			Vector2 position = coinPositions.getFirst();
			
			Logger.log(this, "NXP: " + position.x + ":" + position.y);
			
			if( coinPositions.size() > 1 ) Logger.log(this, "NXXP: " + coinPositions.get(1).x + ":" + coinPositions.get(1).y);
			coinPositions.removeFirst();
			
			return position;
		}
	}
	
	public void addCoinField(PolylineMapObject object)
	{
		//converting Polyline to vertices array
		float[] vertices_raw = ((PolylineMapObject)object).getPolyline().getTransformedVertices();
		Vector2[] vertices_vector = new Vector2[vertices_raw.length / 2];

		for (int i = 0; i < vertices_raw.length / 2; ++i) 
		{
		    vertices_vector[i] = new Vector2();
		    vertices_vector[i].x = vertices_raw[i * 2];
		    vertices_vector[i].y = vertices_raw[i * 2 + 1];
		}
		
		Array<Vector2> vertices = new Array<Vector2>( vertices_vector );
		
		//getting polygon bounding box
		Vector2 bottomLeft = new Vector2( vertices.get(0).x, vertices.get(0).y );
		Vector2 topRight = new Vector2( vertices.get(0).x, vertices.get(0).y );
		
		for(Vector2 v : vertices_vector)
		{   
			if( v.x < bottomLeft.x ) bottomLeft.x = v.x;			
			if( v.x > topRight.x   ) topRight.x = v.x;		
			if( v.y < bottomLeft.y ) bottomLeft.y = v.y;			
			if( v.y > topRight.y   ) topRight.y = v.y;
		}
		
		//calculating coins amount
		int coinsInRow = Math.abs( (int) (topRight.x - bottomLeft.x) ) / 32;
		int coinsInColumn = Math.abs( (int) (topRight.y - bottomLeft.y) ) / 32;
				
		Vector2 coinPosition = new Vector2();
		coinPosition.x = bottomLeft.x;
		coinPosition.y = bottomLeft.y;
		//creating coin template object - coins will be creating on its example
		//EllipseMapObject coinTemplate = new EllipseMapObject();
		//coinTemplate.getEllipse().setSize(22.0f, 22.0f);
		//coinTemplate.getEllipse().setPosition(bottomLeft);
		
		for(int i = 0; i < coinsInRow; i++)
		{
			coinPosition.x += 32.0f;
			coinPosition.y = bottomLeft.y;
			
			for(int k = 0; k < coinsInColumn; k++)
			{
				coinPosition.y += 32.0f;
				
				if ( Intersector.isPointInPolygon( vertices, coinPosition ) ) 
				{
					positionsArray.add( new Vector2(coinPosition.x, coinPosition.y) ); 
				}
				else
				{}
			}
		}
		
		positionsArray.sort(vectorComparator);
	}
}
