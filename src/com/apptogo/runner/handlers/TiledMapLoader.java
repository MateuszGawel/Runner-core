package com.apptogo.runner.handlers;

import java.util.Iterator;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;


public class TiledMapLoader {
	
	private static final TiledMapLoader INSTANCE = new TiledMapLoader();
	
	private static OrthogonalTiledMapRenderer tiledMapRenderer;
	
	private static World world;
	
	
	private static Array<Body> bodies = new Array<Body>();

	private static FixtureDef defaultFixture;

	private static float units = 1.0f;

	public static TiledMapLoader getInstance()
	{
		return INSTANCE;
	}
	
	public static boolean load(String mapPath)
	{
		TiledMap tileMap = new TmxMapLoader().load( mapPath );
		tiledMapRenderer = new OrthogonalTiledMapRenderer( tileMap );
		
		world = new World(new Vector2(0f, -9.2f), true);
		
		defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.8f;
		defaultFixture.restitution = 0.0f;
		
		createPhysics(tileMap);
		
		return true;
	}
	
	public static OrthogonalTiledMapRenderer getMapRenderer()
	{
		return tiledMapRenderer;
	}
	
	public static World getWorld()
	{
		return world;
	}
	
	public static void createPhysics(TiledMap map) {
		
		MapLayers layers = map.getLayers();
		Iterator<MapLayer> layersIt = layers.iterator();
		
		while(layersIt.hasNext()) //lece po wszystkich warstwach
		{
		
			MapLayer layer = layersIt.next();
			
			if( "true".equals( (String)layer.getProperties().get("physicsEnabled") ) ) //ale biore tylko te ktore maja parametr physicsEnabled ustawione na "true"
			{
			
				MapObjects objects = layer.getObjects();
				Iterator<MapObject> objectIt = objects.iterator();
		
				while(objectIt.hasNext()) 
				{
					MapObject object = objectIt.next();
		
					if (object instanceof TextureMapObject){
						continue;
					}
					
					//body definition
					BodyDef bodyDef = new BodyDef();
					bodyDef.type = BodyDef.BodyType.StaticBody;
					
					//shape definition
					Shape shape;
					     if (object instanceof PolygonMapObject)   shape = getShape( (PolygonMapObject)object );
					else if (object instanceof PolylineMapObject)  shape = getShape( (PolylineMapObject)object );
					else if (object instanceof EllipseMapObject)   shape = getShape( (EllipseMapObject)object );
					else                                           
							try
					     	{
								shape = getShape( (RectangleMapObject)object );
					     	}
					     	catch(Exception e) { continue; } //rozpaczliwa proba przerzutowania byle czego na kwadraciaki :) niestety nie wszystko sie da [np Ellipse... sie nie da] wiec zrobilem tak
					
					//creating body
					defaultFixture.shape = shape;
					Body body = world.createBody(bodyDef);
					body.createFixture(defaultFixture);
		
					bodies.add(body);
		
				}
			}
		}
	}
	
	private static Shape getShape(RectangleMapObject obj)
	{
		Rectangle rectangle = obj.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / units,
		                           (rectangle.y + rectangle.height * 0.5f ) / units);
		polygon.setAsBox(rectangle.width * 0.5f / units,
		                 rectangle.height * 0.5f / units,
		                 size,
		                 0.0f);
		return polygon;
	}
	
	private static Shape getShape(PolygonMapObject obj)
	{
		PolygonShape polygon = new PolygonShape();
		float[] vertices = obj.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
		    worldVertices[i] = vertices[i] / units;
		}

		polygon.set(worldVertices);
		return polygon;
	}
	
	private static Shape getShape(PolylineMapObject obj)
	{
		float[] vertices = obj.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
		    worldVertices[i] = new Vector2();
		    worldVertices[i].x = vertices[i * 2] / units;
		    worldVertices[i].y = vertices[i * 2 + 1] / units;
		}

		ChainShape chain = new ChainShape(); 
		chain.createChain(worldVertices);
		return chain;
	}
	
	private static Shape getShape(EllipseMapObject obj)
	{
		Ellipse ellipse = obj.getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ( (ellipse.width < ellipse.height) ? ellipse.width : ellipse.height ) / 2f; //minimalizowanie elipsy do kolka
		
		ellipseShape.setRadius(radius / units);
		ellipseShape.setPosition(new Vector2( (ellipse.x + radius) / units, (ellipse.y + radius) / units));

		return ellipseShape;
	}


	public void destroyPhysics() {
		for (Body body : bodies) {
			world.destroyBody(body);
		}

		bodies.clear();
	}
}