package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Obstacle extends Actor{

	private Body body;
	private World world; 
	private MapObject object;
	private BodyDef bodyDef;
	private Vector2 framePosition = new Vector2();
	protected boolean animate;
	
	private float offsetX = 0;
	private float offsetY = 0;
	
	protected TextureRegion currentFrame;
	protected AnimationManager animationManager;

	//ta klasa odpowiada za stworzenie obiektu animowanego lub sta³ego w odpowiednim miejscu a nastepnie jego body
	
	private Obstacle(MapObject object, World world){
		this.world = world;
		this.object = object;
	}
	
	public Obstacle(MapObject object, World world, String texturePath){	
		this(object, world);
		
		this.currentFrame = new TextureRegion((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), texturePath));
	}
	
	public Obstacle(MapObject object, World world, String atlasPath, String regionName, int frameCount, float frameDuration, Object animationState){
		this(object, world);
		animationManager = new AnimationManager(atlasPath);	
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, true);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
	}
	
	public void createAnimation(String regionName, int frameCount, float frameDuration, Object animationState, boolean looping){
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, looping);
	}

	private Shape createShape(MapObject object)
	{
		Shape shape = null;
		if (object instanceof PolygonMapObject)   shape = getShape( (PolygonMapObject)object );
		//if (object instanceof PolylineMapObject)  shape = getShape( (PolylineMapObject)object );
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
	
	private Shape getShape(EllipseMapObject object){
		Ellipse ellipse = object.getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ((ellipse.width < ellipse.height) ? ellipse.width : ellipse.height)/2f;
		ellipseShape.setRadius(radius/PPM);
		ellipseShape.setPosition(new Vector2(radius/PPM, radius/PPM));
		bodyDef.position.set(new Vector2(ellipse.x/PPM, ellipse.y/PPM));
		return ellipseShape;
	}
	
	private Shape getShape(RectangleMapObject obj){
		//do zrobienia
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
		    if(i % 2 == 0)
		    	worldVertices[i] = vertices[i] / PPM - vertices[0] / PPM;
		    else
		    	worldVertices[i] = vertices[i] / PPM - vertices[1] / PPM;
		}
		bodyDef.position.set(new Vector2(vertices[0]/PPM, vertices[1]/PPM));
		polygon.set(worldVertices);
		return polygon;
	}
	
	public void createBody(BodyType bodyType, FixtureDef fixtureDef, String userData){
		bodyDef = new BodyDef();
		bodyDef.type = bodyType;
				
		FixtureDef material = fixtureDef;
		material.shape = createShape(object);
		
		body = world.createBody(bodyDef);
		body.createFixture(material).setUserData(userData);
		body.setUserData(userData);
		setOrigin(0, 0);
	}
	
	public Fixture createFixture(FixtureDef material, Shape shape, String userData){
		material.shape = shape;
		Fixture fixture = body.createFixture(material);
		fixture.setUserData(userData);
		return fixture;
	}
	
	public void setOffset(float offsetX, float offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	@Override
	public void act(float delta){
		setPosition(body.getPosition().x + offsetX, body.getPosition().y + offsetY);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        
        if(animationManager != null && animate)
        	currentFrame = animationManager.animate(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public Body getBody(){ return this.body; }
}
