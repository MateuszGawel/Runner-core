package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class CoinField extends Obstacle
{
	private Vector2[] worldVertices;
	private GameWorld gameWorld;
	private Shape shape;
	public CoinField(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world);
		shape = createShape();
		this.gameWorld = gameWorld;
		
		createCoins(object);
	}
	
	private Array<Body> createCoins(MapObject mapObject)
	{
		Array<Body> coinBodies = new Array<Body>();
		
		ChainShape shape = (ChainShape)this.shape;
		worldVertices = new Vector2[shape.getVertexCount()];
		
		for(int i=0; i<shape.getVertexCount(); i++){
			worldVertices[i] = new Vector2();
			shape.getVertex(i, worldVertices[i]);
			
			worldVertices[i].x = (worldVertices[i].x*PPM + ((PolylineMapObject)mapObject).getPolyline().getTransformedVertices()[0]) -32f;
			worldVertices[i].y = (worldVertices[i].y*PPM + ((PolylineMapObject)mapObject).getPolyline().getTransformedVertices()[1]) -32f;
		}
		//getting polygon bounding box
		Vector2 bottomLeft = new Vector2( worldVertices[0].x, worldVertices[0].y );
		Vector2 topRight = new Vector2( worldVertices[0].x, worldVertices[0].y );
		
		for(Vector2 v : worldVertices)
		{   
			if( v.x < bottomLeft.x ) bottomLeft.x = v.x;			
			if( v.x > topRight.x   ) topRight.x = v.x;		
			if( v.y < bottomLeft.y ) bottomLeft.y = v.y;			
			if( v.y > topRight.y   ) topRight.y = v.y;
		}
		
		//calculating coins amount
		int coinsInRow = Math.abs( (int) (topRight.x - bottomLeft.x) ) / 32;
		int coinsInColumn = Math.abs( (int) (topRight.y - bottomLeft.y) ) / 32;
		
		//creating coin template object - coins will be creating on its example
		EllipseMapObject coinTemplate = new EllipseMapObject();
		coinTemplate.getEllipse().setSize(32.0f, 32.0f);
		coinTemplate.getEllipse().setPosition(bottomLeft); //jak tutaj zamiast bottomLeft ustawimy srodek to bedzie na srodku w razie gdyby moneta miala miec mniej 32f
		
		for(int i = 0; i < coinsInRow; i++)
		{
			coinTemplate.getEllipse().x += 32.0f;
			coinTemplate.getEllipse().y = bottomLeft.y;
			
			for(int k = 0; k < coinsInColumn; k++)
			{
				coinTemplate.getEllipse().y += 32.0f;
				
				//checking if coin is in polygon (for now we only know that it is in polygon bounding box)
				Vector2 coinTemplatePosition = new Vector2( coinTemplate.getEllipse().x, coinTemplate.getEllipse().y );
				
				if ( Intersector.isPointInPolygon( new Array(worldVertices), coinTemplatePosition ) ) 
				{
					//it is in polygon so we are creating coin
					Coin coin = new Coin(coinTemplate, world, gameWorld);
					coinBodies.add( coin.getBody() );
				}
				else
				{}
			}
		}
				
		return coinBodies;
	}
}
