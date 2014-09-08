package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Iterator;

import box2dLight.RayHandler;

import com.apptogo.runner.actors.Barrel;
import com.apptogo.runner.actors.Bonfire;
import com.apptogo.runner.actors.Bonfire;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;


public class TiledMapLoader 
{	
	private static final TiledMapLoader INSTANCE = new TiledMapLoader();
	public static TiledMapLoader getInstance(){ return INSTANCE; }
	
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private World world;
	private Array<Body> bodies = new Array<Body>();

	private FixtureDef groundFixture;
	private FixtureDef objectFixture;
	private FixtureDef obstacleFixture;
	
	private GameWorld gameWorld;
	
	private TiledMap tiledMap;
	MapProperties mapProperties;
	private RayHandler rayHandler;
	
	public void loadMap(String mapPath)
	{
		groundFixture = Materials.groundBody;
		objectFixture = Materials.objectBody;
		obstacleFixture = Materials.obstacleBody;
		
		tiledMap = new TmxMapLoader().load( mapPath );
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/PPM);
		
		//initLights();
		
		createPhysics(tiledMap);
	}
	
	public Vector2 getMapSize()
	{
		mapProperties = tiledMap.getProperties();
		
		int mapWidth = mapProperties.get("width", Integer.class);
		int mapHeight = mapProperties.get("height", Integer.class);
		int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		int tilePixelHeight = mapProperties.get("tileheight", Integer.class);

		int mapPixelWidth = mapWidth * tilePixelWidth;
		int mapPixelHeight = mapHeight * tilePixelHeight;

		return new Vector2(mapPixelWidth, mapPixelHeight);
	}
	
	private void initLights()
	{
		if( isPropertyTrue(tiledMap, "LightsEnabled") )
		{
			rayHandler = new RayHandler(world);		
		}
		else rayHandler = null;
	}
	
	private void createPhysics(TiledMap map) 
	{	
		MapLayers layers = map.getLayers();
		Iterator<MapLayer> layersIt = layers.iterator();
		
		while(layersIt.hasNext())
		{
			MapLayer layer = layersIt.next();
			
			if( isPropertyTrue(layer, "PhysicsEnabled") )
			{Logger.log(this, "MAMY FIZYKE");
				MapObjects objects = layer.getObjects();
				Iterator<MapObject> objectIt = objects.iterator();
				
				if( isPropertyTrue(layer, "GroundLayer") )
				{
					createGroundLayer(layer, objectIt);
				}
				else if( isPropertyTrue(layer, "ObjectsLayer") )
				{
					createObjectsLayer(layer, objectIt);
				}
				else if( isPropertyTrue(layer, "ObstaclesLayer") )
				{
					createObstaclesLayer(layer, objectIt);
				}
				
				if( rayHandler != null )
				{
					//objectIt.remove();
					//objectIt = objects.iterator(); - to i powyzsze zeby go "przewinac" na poczatek
					//createLights(layerm objectIt); - tego na razie nie obslugujemy
				}
			}
		}
	}
	
	private void createGroundLayer(MapLayer layer, Iterator<MapObject> objectIt)
	{
		while(objectIt.hasNext()) 
		{
			MapObject mapObject = objectIt.next();
			if (mapObject instanceof TextureMapObject)
			{
				continue;
			}

			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.StaticBody;
			
			groundFixture.shape = createShape(mapObject);
			Body body = world.createBody(bodyDef);
			body.createFixture(groundFixture).setUserData("nonkilling");
			body.setUserData("nonkilling");
			
			bodies.add(body);
		}
	}
	
	private void createObjectsLayer(MapLayer layer, Iterator<MapObject> objectIt)
	{
		while(objectIt.hasNext()) 
		{
			MapObject object = objectIt.next();
			if (object instanceof TextureMapObject)
			{
				continue;
			}
	
			if( checkObjectType(object, "jakisObiekt") )
			{
				//stworzJakisObiekt(object);
			}
			//else if ( checkObjectType(object, "innyobiekt") ) { do sth... }
			else
			{
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				
				objectFixture.shape = createShape(object);
				Body body = world.createBody(bodyDef);
				body.createFixture(objectFixture).setUserData("nonkilling");
				body.setUserData("nonkilling");
				bodies.add(body);
			}
		}
	}
	
	private void createObstaclesLayer(MapLayer layer, Iterator<MapObject> objectIt)
	{
		//ponizej opisane czemu nie bd dzialac
		Array<MapObject> objectsToJoint = new Array<MapObject>();
		
		while(objectIt.hasNext()) 
		{
			MapObject object = objectIt.next();
			if (object instanceof TextureMapObject){
				continue;
			}		
			
			if( checkObjectType(object, "barrel") )
			{
				createBarrel(object);
			}
			if( checkObjectType(object, "bonfire") )
			{
				createBonfire(object);
			}
			//ponizej opisane czemu nie bd dzialac
			if( checkObjectType(object, "jointHandle") || isPropertyTrue(object, "isJointed") )
			{
				objectsToJoint.add(object);
			}
			//else if ( checkObjectType(object, "innaprzeszkoda") ) { do sth... }
			else
			{
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				
				obstacleFixture.shape = createShape(object);
				Body body = world.createBody(bodyDef);
				body.createFixture(obstacleFixture).setUserData("killing");
				body.setUserData("killing");
				bodies.add(body);
			}
		}
		
		//ponizej opisane czemu nie bd dzialac
		createJoints( objectsToJoint );
	}
	
	private void createBarrel(MapObject object)
	{
		Barrel barrel = new Barrel(object, world, gameWorld);
		bodies.add(barrel.getBody());
	}
	
	private void createBonfire(MapObject object)
	{
		Bonfire bonfire = new Bonfire(object, world, gameWorld);
		bodies.add(bonfire.getBody());
	}
	
	// totalnie do obczajenia - mamy problem taki ze body sa ustawiane na 0,0 a ich fixtury dopiero maja odpowiednie pozycje i to chyba jest b niedobrze... zajme sie tym jak wroce z urlopu M.A.
	private void createJoints( Array<MapObject> objectsToJoint )
	{Logger.log(this, "ROBIMY JOINT");
		while( objectsToJoint.size > 0 )
		{Logger.log(this, "0| oTJ.size = " + objectsToJoint.size );
			MapObject objectA = objectsToJoint.first();
			objectsToJoint.removeIndex(0);
			Logger.log(this, "1| oTJ.size = " + objectsToJoint.size );
			
			MapObject objectB = null;
			int objectBIndex = 0;
			
			for(MapObject oB: objectsToJoint)
			{
				if( ((String)oB.getProperties().get("jointId")).equals( (String)objectA.getProperties().get("jointId") ) )
				{
					objectB = oB;
					
					break;
				}
				else
				{
					objectBIndex++;
				}
			}
			
			if( objectB != null )
			{		/*
				//biedoswap
				if( !checkObjectType(objectA, "jointHandle") )
				{
					MapObject objectC = objectA;
					objectA = objectB;
					objectB = objectC;
				}
				
				Logger.log(this, "OBJECTA jointId = " + objectA.getProperties().get("myName").toString() );
				Logger.log(this, "OBJECTB jointId = " + objectB.getProperties().get("myName").toString() );
				
				Logger.log(this, "OBJECTA x = " + objectA.getProperties().get("x").toString() );
				Logger.log(this, "OBJECTA y = " + objectA.getProperties().get("y").toString() );
				
				objectsToJoint.removeIndex(objectBIndex);
				Logger.log(this, "2| oTJ.size = " + objectsToJoint.size );
				//---creating body A
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				bodyDef.position.set( Float.parseFloat( objectA.getProperties().get("x").toString() ) / PPM, Float.parseFloat( objectA.getProperties().get("y").toString() ) / PPM );
				
				obstacleFixture.shape = createShape(objectA);
				
				Body bodyA = world.createBody(bodyDef);
				bodyA.createFixture(obstacleFixture).setUserData("nonkilling");
				bodyA.setUserData("nonkilling");
				bodies.add(bodyA);
				
				//---creating body B
				bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.DynamicBody;
				bodyDef.position.set( Float.parseFloat( objectA.getProperties().get("x").toString() ) / PPM, Float.parseFloat( objectA.getProperties().get("y").toString() ) / PPM );
				
				obstacleFixture.shape = createShape(objectB);
				
				Body bodyB = world.createBody(bodyDef);
				bodyB.createFixture(obstacleFixture).setUserData("nonkilling");
				bodyB.setUserData("nonkilling");
				bodies.add(bodyB);
				
				Logger.log(this, "BODYA position = " + bodyA.getPosition().toString() );
				Logger.log(this, "BODYB position = " + bodyB.getPosition().toString() );
				
				//---creating joint between body A and B
				RopeJointDef jointDef = new RopeJointDef();
				jointDef.bodyA = bodyA;
				jointDef.bodyB = bodyB;
				jointDef.localAnchorA.set( new Vector2(0,0) );
				jointDef.localAnchorB.set( new Vector2(0,0) );
				jointDef.maxLength = 3;
				jointDef.collideConnected = true;
				//jointDef.length = (float) Math.sqrt( Math.abs( (double)( ((bodyA.getLocalCenter().x - bodyB.getLocalCenter().x) * (bodyA.getLocalCenter().x - bodyB.getLocalCenter().x)) + ((bodyA.getLocalCenter().y - bodyB.getLocalCenter().y) * (bodyA.getLocalCenter().y - bodyB.getLocalCenter().y)) ) / PPM ) );
				
				world.createJoint(jointDef);
				//Logger.log(this, "joint count = " + String.valueOf( world.getJointCount() ));*/
				
				BodyDef bD1 = new BodyDef();
				FixtureDef fD1 = new FixtureDef();
				BodyDef bD2 = new BodyDef();
				FixtureDef fD2 = new FixtureDef();
				
				PolygonShape bS1 = new PolygonShape();
				bS1.setAsBox(0.2f, 0.2f);
				
				PolygonShape bS2 = new PolygonShape();
				bS2.setAsBox(0.2f, 0.2f);
				
				bD1.position.x = 10.0f;
				bD1.position.y = 10.0f;
				bD1.type = BodyType.DynamicBody;
				bD2.position.x = 14.0f;
				bD2.position.y = 10.0f;
				bD2.type = BodyType.StaticBody;
				
				fD1.shape = bS1;
				Body b1 = world.createBody(bD1);
				b1.createFixture(fD1);
				
				fD2.shape = bS2;
				Body b2 = world.createBody(bD2);
				b2.createFixture(fD2).setUserData("nonkilling");
				b2.setUserData("nonkilling");
				
				bodies.add(b1);
				bodies.add(b2);
				
				DistanceJointDef dJD = new DistanceJointDef();
				dJD.bodyA = b1;
				dJD.bodyB = b2;
				dJD.length = 4.0f;
				
				world.createJoint(dJD);
			}
		}
	}
			
	public Shape createShape(MapObject object)
	{
		Shape shape = null;
		if (object instanceof PolygonMapObject)   shape = getShape( (PolygonMapObject)object );
		else if (object instanceof PolylineMapObject)  shape = getShape( (PolylineMapObject)object );
		else if (object instanceof EllipseMapObject)   shape = getShape( (EllipseMapObject)object );
		else
		{                                           
				try
		     	{
					shape = getShape( (RectangleMapObject)object );
		     	}
		     	catch(Exception e) { }
		}
		return shape;
	}
	
	private Shape getShape(RectangleMapObject obj)
	{
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
	private Shape getShape(PolygonMapObject obj)
	{
		PolygonShape polygon = new PolygonShape();
		float[] vertices = obj.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
		    worldVertices[i] = vertices[i] / PPM;
		}

		polygon.set(worldVertices);
		return polygon;
	}
	private Shape getShape(PolylineMapObject obj)
	{
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
	private Shape getShape(EllipseMapObject obj)
	{
		Ellipse ellipse = obj.getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ( (ellipse.width < ellipse.height) ? ellipse.width : ellipse.height ) / 2f; //minimalizowanie elipsy do kolka
		
		ellipseShape.setRadius(radius / PPM);
		ellipseShape.setPosition(new Vector2( (ellipse.x + radius) / PPM, (ellipse.y + radius) / PPM));
		return ellipseShape;
	}
	
	private boolean isPropertyTrue(TiledMap map, String propertyKey)
	{
		if( "true".equals((String)map.getProperties().get( propertyKey )) ) return true;
		return false;
	}
	private boolean isPropertyTrue(MapLayer layer, String propertyKey)
	{
		if( "true".equals((String)layer.getProperties().get( propertyKey )) ) return true;
		return false;
	}
	private boolean isPropertyTrue(MapObject object, String propertyKey)
	{
		if( "true".equals((String)object.getProperties().get( propertyKey )) ) return true;
		return false;
	}
	
	private boolean checkObjectType(MapObject object, String typeName)
	{
		if( typeName.equals((String)object.getProperties().get( "type" )) ) return true;
		return false;
	}
	
	public void setWorld(World world){ this.world = world; }
	public void setGameWorld(GameWorld gameWorld){ this.gameWorld = gameWorld; }
	public OrthogonalTiledMapRenderer getMapRenderer(){ return tiledMapRenderer; }
	public RayHandler getRayHandler(){ return rayHandler; }
}