package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;
import java.util.Iterator;

import box2dLight.RayHandler;

import com.apptogo.runner.actors.Barrel;
import com.apptogo.runner.actors.Bonfire;
import com.apptogo.runner.vars.Box2DVars;
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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;


public class TiledMapLoader 
{	
	private class jointBody
	{
		public Body body;
		public String jointAnchorX;
		public String jointAnchorY;
		public jointBody(Body body, String jointAnchorX, String jointAnchorY)
		{
			this.body = body;
			this.jointAnchorX = jointAnchorX;
			this.jointAnchorY = jointAnchorY;
		}
	}
	
	private HashMap<String, jointBody> jointHandles = new HashMap<String, jointBody>();
	private HashMap<String, jointBody> jointObjects = new HashMap<String, jointBody>();
	
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
				
				createJoints(jointHandles, jointObjects);
				
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
			bodyDef.position.x = Float.parseFloat( mapObject.getProperties().get("x").toString() ) / Box2DVars.PPM;
			bodyDef.position.y = Float.parseFloat( mapObject.getProperties().get("y").toString() ) / Box2DVars.PPM;
			
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
	
			Body body = null;
			
			if( checkObjectType(object, "jakisObiekt") )
			{
				//stworzJakisObiekt(object);
			}
			//else if ( checkObjectType(object, "innyobiekt") ) { do sth... }
			else
			{
				BodyDef bodyDef = new BodyDef();
				
				bodyDef.type = BodyDef.BodyType.StaticBody;
				if( isPropertyTrue(object, "isDynamic"))
				{
					bodyDef.type = BodyDef.BodyType.DynamicBody;
				}
				
				bodyDef.position.x = Float.parseFloat( object.getProperties().get("x").toString() ) / Box2DVars.PPM;
				bodyDef.position.y = Float.parseFloat( object.getProperties().get("y").toString() ) / Box2DVars.PPM;
				
				objectFixture.shape = createShape(object);
				body = world.createBody(bodyDef);
				body.createFixture(objectFixture).setUserData("nonkilling");
				body.setUserData("nonkilling");
				
				if( isPropertyTrue(object, "isGhost"))
				{
					for(Fixture f: body.getFixtureList()) 
					{ 
						f.setSensor(true); 
					}
				}
				
				if( checkObjectType(object, "jointHandle") )
				{					
					addObjectToJointHandles(object, body);
				}
				
				bodies.add(body);
			}
			
			if( !checkObjectType(object, "jointHandle") && isPropertyTrue(object, "isJointed") )
			{
				addObjectToJointObjects(object, body);
			}
		}
	}
	
	private void createObstaclesLayer(MapLayer layer, Iterator<MapObject> objectIt)
	{		
		while(objectIt.hasNext()) 
		{
			MapObject object = objectIt.next();
			if (object instanceof TextureMapObject)
			{
				continue;
			}		
			
			Body body = null;
			
			if( checkObjectType(object, "barrel") )
			{
				body = createBarrel(object);
				
				bodies.add( body );
			}
			else if( checkObjectType(object, "bonfire") )
			{
				body = createBonfire(object);
								
				bodies.add( body );
			}
			//else if ( checkObjectType(object, "innaprzeszkoda") ) { do sth... }
			else
			{
				BodyDef bodyDef = new BodyDef();
				
				bodyDef.type = BodyDef.BodyType.StaticBody;
				if( isPropertyTrue(object, "isDynamic"))
				{
					bodyDef.type = BodyDef.BodyType.DynamicBody;
				}
				
				bodyDef.position.x = Float.parseFloat( object.getProperties().get("x").toString() ) / Box2DVars.PPM;
				bodyDef.position.y = Float.parseFloat( object.getProperties().get("y").toString() ) / Box2DVars.PPM;
				
				obstacleFixture.shape = createShape(object);
				
				body = world.createBody(bodyDef);
				body.createFixture(obstacleFixture).setUserData("killing");
				body.setUserData("killing");
				
				if( isPropertyTrue(object, "isGhost"))
				{
					for(Fixture f: body.getFixtureList()) 
					{ 
						f.setUserData("nonkilling");
						f.setSensor(true); 
					}
				}
				
				if( checkObjectType(object, "jointHandle") )
				{
					addObjectToJointHandles(object, body);
				}
				
				bodies.add(body);
			}
			
			if( !checkObjectType(object, "jointHandle") && isPropertyTrue(object, "isJointed") )
			{
				addObjectToJointObjects(object, body);
			}
		}
	}
	
	private Body createBarrel(MapObject object)
	{
		Barrel barrel = new Barrel(object, world, gameWorld);
		return barrel.getBody();
	}
	
	private Body createBonfire(MapObject object)
	{
		Bonfire bonfire = new Bonfire(object, world, gameWorld);
		return bonfire.getBody();
	}
	
	private void addObjectToJointHandles(MapObject object, Body body)
	{
		if(body == null)
		{
			return;
		}
		
		String jointAnchorX = null;
		String jointAnchorY = null;
		
		if(object.getProperties().containsKey("jointX"))
		jointAnchorX = object.getProperties().get("jointX").toString(); 
		
		if(object.getProperties().containsKey("jointY"))
		jointAnchorY = object.getProperties().get("jointY").toString();
		
		jointHandles.put(((String)object.getProperties().get("jointId")), new jointBody(body,jointAnchorX,jointAnchorY));
	}
	
	private void addObjectToJointObjects(MapObject object, Body body)
	{
		if(body == null)
		{
			return;
		}
		
		String jointAnchorX = null;
		String jointAnchorY = null;
		
		if(object.getProperties().containsKey("jointX"))
		jointAnchorX = object.getProperties().get("jointX").toString(); 
		
		if(object.getProperties().containsKey("jointY"))
		jointAnchorY = object.getProperties().get("jointY").toString();
		
		jointObjects.put(((String)object.getProperties().get("jointId")), new jointBody(body,jointAnchorX,jointAnchorY));
	}
	
	private void createJoints(HashMap<String, jointBody> jointHandles, HashMap<String, jointBody> jointObjects)
	{		
		Iterator<String> handlesIter = jointHandles.keySet().iterator();
		
		while(handlesIter.hasNext())
		{
			String jointId = (String)handlesIter.next();
			jointBody jointBodyA = (jointBody)jointHandles.get(jointId);	
			Body bodyA = jointBodyA.body;		
			
			jointBody jointBodyB = null;
			Body bodyB = null;
			int anchorBIndex = 0;
			
			Iterator<String> objectsIter = jointObjects.keySet().iterator();
			
			while(objectsIter.hasNext())
			{
				String bodyJointIds = (String)objectsIter.next();

				Array<String> jointIds = new Array<String>( bodyJointIds.split(";") );
				
				if( jointIds.contains(jointId, false) )
				{
					jointBodyB = (jointBody)jointObjects.get(bodyJointIds);
					bodyB = jointBodyB.body;
					
					anchorBIndex = jointIds.indexOf(jointId, false);
							
					break;
				}
				else continue;
			}
			
			if( bodyB != null )
			{	
				DistanceJointDef jointDef = new DistanceJointDef();
				jointDef.bodyA = bodyA;
				jointDef.bodyB = bodyB;
			
				float anchorAX;
				float anchorAY;
				float anchorBX;
				float anchorBY;
				
				if( jointBodyA.jointAnchorX == null || jointBodyA.jointAnchorX.length() == 0 )
					anchorAX = 0;
				else
					anchorAX = Float.parseFloat( jointBodyA.jointAnchorX );
				//---
				if( jointBodyA.jointAnchorY == null || jointBodyA.jointAnchorY.length() == 0 )
					anchorAY = 0;
				else
					anchorAY = Float.parseFloat( jointBodyA.jointAnchorY );
				//---
				if( jointBodyB.jointAnchorX == null || jointBodyB.jointAnchorX.length() == 0 )
					anchorBX = 0;
				else
					anchorBX = Float.parseFloat( new Array<String>( jointBodyB.jointAnchorX.split(";") ).get(anchorBIndex) );
				//---
				if( jointBodyB.jointAnchorY == null || jointBodyB.jointAnchorY.length() == 0 )
					anchorBY = 0;
				else
					anchorBY = Float.parseFloat( new Array<String>( jointBodyB.jointAnchorY.split(";") ).get(anchorBIndex) );
				
				jointDef.localAnchorA.set( new Vector2(anchorAX, anchorAY) );
				jointDef.localAnchorB.set( new Vector2(anchorBX, anchorBY) );
				
				jointDef.length = ( new Vector2( (bodyB.getPosition().x + anchorBX) - (bodyA.getPosition().x + anchorAX), (bodyB.getPosition().y + anchorBY) - (bodyA.getPosition().y + anchorAY) ) ).len();
				
				world.createJoint(jointDef).setUserData(jointId);
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

		polygon.setAsBox(rectangle.width * 0.5f / PPM, rectangle.height * 0.5f / PPM);
		return polygon;
	}
	private Shape getShape(PolygonMapObject obj)
	{
		PolygonShape polygon = new PolygonShape();
		float[] vertices = obj.getPolygon().getVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) 
		{
			Logger.log(this, "VERTICE: " + String.valueOf(vertices[i]) );
		    worldVertices[i] = vertices[i] / PPM;
		}

		polygon.set(worldVertices);
		return polygon;
	}
	private Shape getShape(PolylineMapObject obj)
	{
		float[] vertices = obj.getPolyline().getVertices();
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