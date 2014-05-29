package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Iterator;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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


public class TiledMapLoader {	
	private static final TiledMapLoader INSTANCE = new TiledMapLoader();
	
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private World world;
	private Array<Body> bodies = new Array<Body>();
	private FixtureDef groundFixture;
	
	private RayHandler rayHandler;
	
	public void loadMap(String mapPath){
		TiledMap tiledMap = new TmxMapLoader().load( mapPath );
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/PPM);
		
		groundFixture = Materials.groundBody;
		
		//enabling lights if enableLight parameter is set
		if( "true".equals( (String)tiledMap.getProperties().get("enableLight") ) )
		{
			if( "true".equals( (String)tiledMap.getProperties().get("enableGammaCorrection") ) )
			{
				//RayHandler.setGammaCorrection(true);
			}
			
			rayHandler = new RayHandler(world);		
			
			if( "true".equals( (String)tiledMap.getProperties().get("enableAmbientLight") ) )
			{
				String[] colors = ( (String)tiledMap.getProperties().get("ambientLightColor") ).split(",");
				
				rayHandler.setAmbientLight(new Color( Float.parseFloat(colors[0]), 
													  Float.parseFloat(colors[1]),
													  Float.parseFloat(colors[2]),
													  Float.parseFloat(colors[3]) ));
			}
			
		}
		else rayHandler = null;
		
		createPhysics(tiledMap);
	}
	
	private void createPhysics(TiledMap map) {
		
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
						
					if( object.getName() != null && object.getName().toString().equals("light") )
					{
						if( rayHandler != null )
						{
							float x = ( (EllipseMapObject)object ).getEllipse().x / PPM;
							float y = ( (EllipseMapObject)object ).getEllipse().y / PPM;
							int rays = Integer.parseInt( (String)object.getProperties().get("lightRays") );
							float distance = Float.parseFloat( (String)object.getProperties().get("lightDistance") );
							
							Color lightColor = object.getColor();
							lightColor.a = Float.parseFloat( (String)object.getProperties().get("opacity") );
							
							new PointLight(rayHandler, rays, lightColor, distance, x, y);
						}
					}
					else
					{
		
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
						else{                                           
								try
						     	{
									shape = getShape( (RectangleMapObject)object );
						     	}
						     	catch(Exception e) { continue; } //rozpaczliwa proba przerzutowania byle czego na kwadraciaki :) niestety nie wszystko sie da [np Ellipse... sie nie da] wiec zrobilem tak
						}
						//creating body
						groundFixture.shape = shape;
						Body body = world.createBody(bodyDef);
						body.createFixture(groundFixture);
			
						bodies.add(body);
					}
		
				}
			}
		}
		
		
	}
	
	
	
	private Shape getShape(RectangleMapObject obj){
		Rectangle rectangle = obj.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
		                           (rectangle.y + rectangle.height * 0.5f ) / PPM);
		polygon.setAsBox(rectangle.width * 0.5f / PPM,
		                 rectangle.height * 0.5f / PPM,
		                 size,
		                 0.0f);
		return polygon;
	}
	private Shape getShape(PolygonMapObject obj){
		PolygonShape polygon = new PolygonShape();
		float[] vertices = obj.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
		    worldVertices[i] = vertices[i] / PPM;
		}

		polygon.set(worldVertices);
		return polygon;
	}
	private Shape getShape(PolylineMapObject obj){
		float[] vertices = obj.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
		    worldVertices[i] = new Vector2();
		    worldVertices[i].x = vertices[i * 2] / PPM;
		    worldVertices[i].y = vertices[i * 2 + 1] / PPM;
		}

		ChainShape chain = new ChainShape(); 
		chain.createChain(worldVertices);
		return chain;
	}
	private Shape getShape(EllipseMapObject obj){
		Ellipse ellipse = obj.getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ( (ellipse.width < ellipse.height) ? ellipse.width : ellipse.height ) / 2f; //minimalizowanie elipsy do kolka
		
		ellipseShape.setRadius(radius / PPM);
		ellipseShape.setPosition(new Vector2( (ellipse.x + radius) / PPM, (ellipse.y + radius) / PPM));

		return ellipseShape;
	}

	
	
	public void destroyPhysics() {
		for (Body body : bodies) {
			world.destroyBody(body);
		}
		bodies.clear();
	}
	
	public static TiledMapLoader getInstance(){ return INSTANCE; }
	public void setWorld(World world){ this.world = world; }
	public OrthogonalTiledMapRenderer getMapRenderer(){ return tiledMapRenderer; }
	public RayHandler getRayHandler(){ return rayHandler; }
}