package com.apptogo.runner.handlers;

import java.util.Iterator;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
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


public class PhysicsGenerator {
	private World world;
	//private float units;
	private Array<Body> bodies = new Array<Body>();
	//private ObjectMap<String, FixtureDef> materials = new ObjectMap<String, FixtureDef>();
	private FixtureDef defaultFixture;
	private float units = 1.0f;

	public PhysicsGenerator(World world){ //, float unitsPerPixel, FileHandle materialsFile, int loggingLevel) {

		this.world = world;
		//this.units = unitsPerPixel;

		defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.8f;
		defaultFixture.restitution = 0.0f;

		//materials.put("default", defaultFixture);


		//if (materialsFile != null) {
		//	loadMaterialsFile(materialsFile);
		//}
	}

	public void createPhysics(TiledMap map) {
		
		MapLayer layer = map.getLayers().get(1);
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
			if (object instanceof RectangleMapObject) {
				shape = getRectangle((RectangleMapObject)object);
			}
			else if (object instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject)object);
			}
			else if (object instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject)object);
			}
			else if (object instanceof CircleMapObject) {
				shape = getCircle((CircleMapObject)object);
			}
			else {
				continue;
			}

			
			//MapProperties properties = object.getProperties();
			//String material = properties.get("material", "default", String.class);
			FixtureDef fixtureDef = defaultFixture; //materials.get(material);

			fixtureDef.shape = shape;
			// ! fixtureDef.filter.categoryBits = Env.game.getCategoryBitsManager().getCategoryBits("level");

			Body body = world.createBody(bodyDef);
			body.createFixture(fixtureDef);

			bodies.add(body);

			//fixtureDef.shape = null;
			//shape.dispose();
		}
	}

	/**
	 * Destroys every static body that has been created using the manager.
	 */
	public void destroyPhysics() {
		for (Body body : bodies) {
			world.destroyBody(body);
		}

		bodies.clear();
	}

	private Shape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / units,
		                           (rectangle.y + rectangle.height * 0.5f ) / units);
		polygon.setAsBox(rectangle.width * 0.5f / units,
		                 rectangle.height * 0.5f / units,
		                 size,
		                 0.0f);
		return polygon;
	}

	private Shape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius / units);
		circleShape.setPosition(new Vector2(circle.x / units, circle.y / units));
		return circleShape;
	}

	private Shape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
		    worldVertices[i] = vertices[i] / units;
		}

		polygon.set(worldVertices);
		return polygon;
	}

	private Shape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getTransformedVertices();
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
}