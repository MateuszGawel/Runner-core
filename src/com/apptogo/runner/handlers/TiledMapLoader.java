package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Iterator;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.apptogo.runner.actors.Barrel;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
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
	private Array<Body> groundBodies = new Array<Body>();
	private Array<Body> killingBodies = new Array<Body>();
	private FixtureDef groundFixture;
	private FixtureDef killingFixture;
	private GameWorld gameWorld;
	
	private TiledMap tiledMap;
	MapProperties mapProperties;
	private RayHandler rayHandler;
	
	public Vector2 loadMap(String mapPath){
		tiledMap = new TmxMapLoader().load( mapPath );
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/PPM);
		groundFixture = Materials.groundBody;
		killingFixture = Materials.killingBody;
		//initLights();
		createPhysics(tiledMap);
		
		return calculateMapSize();
	}
	
	private Vector2 calculateMapSize(){
		mapProperties = tiledMap.getProperties();
		
		int mapWidth = mapProperties.get("width", Integer.class);
		int mapHeight = mapProperties.get("height", Integer.class);
		int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		int tilePixelHeight = mapProperties.get("tileheight", Integer.class);

		int mapPixelWidth = mapWidth * tilePixelWidth;
		int mapPixelHeight = mapHeight * tilePixelHeight;
		Logger.log(this,  mapPixelWidth);
		return new Vector2(mapPixelWidth, mapPixelHeight);
	}
	
	private void initLights(){
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
	}
	
	private void createPhysics(TiledMap map) {
		
		MapLayers layers = map.getLayers();
		Iterator<MapLayer> layersIt = layers.iterator();
		
		while(layersIt.hasNext())
		{
			MapLayer layer = layersIt.next();
			
			if("true".equals((String)layer.getProperties().get("physicsEnabled")))
			{
				MapObjects objects = layer.getObjects();
				Iterator<MapObject> objectIt = objects.iterator();
				
				createGroundLayer(layer, objectIt);
				createKillingLayer(layer, objectIt);
			}
		}	
	}
	
	private void createGroundLayer(MapLayer layer, Iterator<MapObject> objectIt){
		if("true".equals( (String)layer.getProperties().get("terrain"))){
			while(objectIt.hasNext()) 
			{
				MapObject object = objectIt.next();
				if (object instanceof TextureMapObject){
					continue;
				}

				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				
				groundFixture.shape = createShape(object);
				Body body = world.createBody(bodyDef);
				body.createFixture(groundFixture).setUserData("ground");
				body.setUserData("ground");
				
				groundBodies.add(body);
			}
		}
	}
	
	private void createKillingLayer(MapLayer layer, Iterator<MapObject> objectIt){
		if("true".equals( (String)layer.getProperties().get("killing"))){
			while(objectIt.hasNext()) 
			{
				MapObject object = objectIt.next();
				if (object instanceof TextureMapObject){
					continue;
				}
		
				if(!handleCustomObjects(object)){
					BodyDef bodyDef = new BodyDef();
					bodyDef.type = BodyDef.BodyType.StaticBody;
					
					killingFixture.shape = createShape(object);
					Body body = world.createBody(bodyDef);
					body.createFixture(killingFixture).setUserData("killing");
					body.setUserData("killing");
					killingBodies.add(body);
				}
			}
		}
	}
	
	private boolean handleCustomObjects(MapObject object){
		if(object.getName() != null && object.getName().equals("barrel")){
			Barrel barrel = new Barrel(object, world, gameWorld);
			killingBodies.add(barrel.getBody());
			return true;
		}
		return false;
	}
	
	private void createLights(MapObject object){
		if( object.getProperties().get("light") != null && object.getName().toString().equals("light") )
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
	}
	
	private Shape createShape(MapObject object){
		Shape shape = null;
		if (object instanceof PolygonMapObject)   shape = getShape( (PolygonMapObject)object );
		else if (object instanceof PolylineMapObject)  shape = getShape( (PolylineMapObject)object );
		else if (object instanceof EllipseMapObject)   shape = getShape( (EllipseMapObject)object );
		else{                                           
				try
		     	{
					shape = getShape( (RectangleMapObject)object );
		     	}
		     	catch(Exception e) { }
		}
		return shape;
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
		Logger.log(this, "tworze wektor elipsy: " + (ellipse.x + radius) / PPM + " : " + (ellipse.x + radius) / PPM);
		ellipseShape.setPosition(new Vector2( (ellipse.x + radius) / PPM, (ellipse.y + radius) / PPM));

		return ellipseShape;
	}

	
	
	public void destroyPhysics() {
		for (Body body : groundBodies) {
			world.destroyBody(body);
		}
		groundBodies.clear();
	}
	
	public static TiledMapLoader getInstance(){ return INSTANCE; }
	public void setWorld(World world){ this.world = world; }
	public void setGameWorld(GameWorld gameWorld){ this.gameWorld = gameWorld; }
	public OrthogonalTiledMapRenderer getMapRenderer(){ return tiledMapRenderer; }
	public RayHandler getRayHandler(){ return rayHandler; }
}