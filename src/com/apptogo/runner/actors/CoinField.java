package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;

import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class CoinField extends Obstacle 
{
	public int id = -1;
	private boolean isActive = false;
	
	public HashMap<Vector2, Coin> coinPositions;
	public int counter = 0;
	
	public CoinField(MapObject object, World world, GameWorld gameWorld) 
	{
		super(object, world);
		
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "coinField");	
		gameWorld.getWorldStage().addActor(this);
		
		coinPositions = new HashMap<Vector2, Coin>();
		
		createCoinsPosition(object);
	}

	@Override
	public void act(float delta) {
    	super.act(delta);
		
		if( this.getBodyUserData().active )
		{
			if( !isActive )
			{
				isActive = true;
				CoinsManager.getInstance().activeCoinFields.add(this);
			}
		}
		else
		{
			if( isActive )
			{
				isActive = false;
				CoinsManager.getInstance().activeCoinFields.removeValue(this, true);
			}
		}
		
	}
	
	private void createCoinsPosition(MapObject mapObject) 
	{
		// converting Polygon to vertices array
		float[] vertices_raw = ((PolygonMapObject) mapObject).getPolygon().getTransformedVertices();
		
		Vector2[] vertices_vector = new Vector2[vertices_raw.length / 2];

		for (int i = 0; i < vertices_raw.length / 2; ++i) 
		{
			vertices_vector[i] = new Vector2();
			vertices_vector[i].x = vertices_raw[i * 2];
			vertices_vector[i].y = vertices_raw[i * 2 + 1];
		}

		Array<Vector2> vertices = new Array<Vector2>(vertices_vector);

		// getting polygon bounding box
		Vector2 bottomLeft = new Vector2(vertices.get(0).x, vertices.get(0).y);
		Vector2 topRight = new Vector2(vertices.get(0).x, vertices.get(0).y);

		for (Vector2 v : vertices_vector) 
		{
			if (v.x < bottomLeft.x)
				bottomLeft.x = v.x;
			if (v.x > topRight.x)
				topRight.x = v.x;
			if (v.y < bottomLeft.y)
				bottomLeft.y = v.y;
			if (v.y > topRight.y)
				topRight.y = v.y;
		}

		// calculating coins amount
		int coinsInRow = Math.abs((int) (topRight.x - bottomLeft.x)) / 32;
		int coinsInColumn = Math.abs((int) (topRight.y - bottomLeft.y)) / 32;

		Vector2 coinPosition = new Vector2();
		coinPosition.x = bottomLeft.x;
		coinPosition.y = bottomLeft.y;

		for (int i = 0; i < coinsInRow; i++) {
			coinPosition.x += 32.0f;
			coinPosition.y = bottomLeft.y;

			for (int k = 0; k < coinsInColumn; k++) {
				coinPosition.y += 32.0f;

				if (Intersector.isPointInPolygon(vertices, coinPosition)) {
					coinPositions.put(new Vector2(coinPosition.x, coinPosition.y), null);
				}
			}
		}
	}
}
