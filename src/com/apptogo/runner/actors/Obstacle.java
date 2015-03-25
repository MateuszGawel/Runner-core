package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Obstacle extends Actor{

	protected Body body;
	protected World world; 
	private MapObject object;
	private BodyDef bodyDef;
	
	private float offsetX = 0;
	private float offsetY = 0;
	
	protected boolean updatePosition = false;
	protected Vector2 position;
	protected TextureRegion currentFrame;
	protected AnimationManager animationManager;
	protected GameWorld gameWorld;
	private float soundVolume = 1;
	protected boolean handleSoundVolume = true;
	protected String atlasPath = "";
	
	protected boolean scaleFrames = false;
	protected float frameScale = 0.0f;
	
	//jesli chcielibysmy sobie zrobic obstacle w menu to nie powinno byc dzielone przez PPM (draw)
	protected boolean isGameObstacle = true;
	
	//ta klasa odpowiada za stworzenie obiektu animowanego lub sta³ego w odpowiednim miejscu a nastepnie jego body
	
	public Obstacle()
	{
		animationManager = new AnimationManager();
	}
	
	public Obstacle(String atlasPath)
	{
		this.atlasPath = atlasPath;			
		animationManager = new AnimationManager(atlasPath);	
	}
	
	public Obstacle(MapObject object, World world, String atlasPath){
		this(atlasPath);
		this.world = world;
		this.object = object;
	}
	
	public Obstacle(MapObject object, World world){
		this.world = world;
		this.object = object;
	}
	
	public Obstacle(MapObject object, World world, String regionName, String atlasPath){	
		this(object, world, atlasPath);	
		this.currentFrame = ((TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), atlasPath)).findRegion(regionName);
	}
	
	public Obstacle(String regionName, int frameCount, float frameDuration, Object animationState, String atlasPath){
		this(atlasPath);
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, true);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
	}
	
	public Obstacle(TextureRegion currentFrame){
		this.currentFrame = currentFrame;
	}
	
	public Obstacle(MapObject object, World world, String regionName, int frameCount, float frameDuration, Object animationState, String atlasPath){
		this(object, world, atlasPath);
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, true);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
	}
	
	public void createAnimation(String regionName, int frameCount, float frameDuration, Object animationState, boolean looping)
	{
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, looping);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
	}

	private Shape createShape(MapObject object)
	{
		Shape shape = null;
		if (object instanceof PolygonMapObject)   shape = getShape( (PolygonMapObject)object );
		if (object instanceof PolylineMapObject)  shape = getShape( (PolylineMapObject)object );
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
	
	private Shape getShape(EllipseMapObject object)
	{
		Ellipse ellipse = object.getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ((ellipse.width < ellipse.height) ? ellipse.width : ellipse.height)/2f;
		ellipseShape.setRadius(radius/PPM);
		ellipseShape.setPosition(new Vector2(radius/PPM, radius/PPM));
		
		position = new Vector2(ellipse.x/PPM, ellipse.y/PPM);
		if(bodyDef != null){
			bodyDef.position.set(position);
		}
		return ellipseShape;
	}
	
	private Shape getShape(RectangleMapObject obj){
		Rectangle rectangle = obj.getRectangle();
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(rectangle.width * 0.5f / PPM, rectangle.height * 0.5f / PPM);
		
		position = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,(rectangle.y + rectangle.height * 0.5f ) / PPM);
		if(bodyDef != null){
			bodyDef.position.set(position);
		}
		return polygonShape;
	}
	
	
	private Shape getShape(PolylineMapObject obj)
	{
		ChainShape chain = new ChainShape(); 
		float[] vertices = obj.getPolyline().getTransformedVertices();
		
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
		    worldVertices[i] = new Vector2();
		    worldVertices[i].x = vertices[i * 2]/PPM - vertices[0]/PPM;
		    worldVertices[i].y = vertices[i * 2 + 1]/PPM - vertices[1]/PPM;
		}
		
		position = new Vector2(vertices[0]/PPM, vertices[1]/PPM);
		if(bodyDef != null){
			bodyDef.position.set(position);
		}
		chain.createChain(worldVertices);
		return chain;
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
		
		position = new Vector2(vertices[0]/PPM, vertices[1]/PPM);
		if(bodyDef != null){
			bodyDef.position.set(position);
		}
		polygon.set(worldVertices);
		return polygon;
	}
	
	public Shape createShape(){
		return createShape(object);
	}
	
	public void createBody(BodyType bodyType, FixtureDef fixtureDef, String userDataString)
	{
		bodyDef = new BodyDef();
		bodyDef.type = bodyType;

		Shape shape = createShape(object);
			
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		
		UserData userData = new UserData(userDataString);
		userData.bodyWidth = shapeWidth;
		
		fixtureDef.shape = shape;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData( userData );
		body.setUserData(new UserData(userData));
	}
	public void createFixture(FixtureDef fixtureDef, String userData)
	{
		fixtureDef.shape = createShape(object);
		body.createFixture(fixtureDef).setUserData( new UserData(userData) );
	}
	
	public Fixture createFixture(FixtureDef fixtureDef, String userData, MapObject object)
	{
		fixtureDef.shape = createShape(object);
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData( new UserData(userData) );
		return fixture;
	}
	
	public void createFixture(FixtureDef fixtureDef, UserData userData)
	{
		fixtureDef.shape = createShape(object);
		body.createFixture(fixtureDef).setUserData( userData );
	}
	
	public Fixture createFixture(FixtureDef material, Shape shape, String userData){
		material.shape = shape;
		Fixture fixture = body.createFixture(material);
		fixture.setUserData( new UserData(userData) );
		return fixture;
	}
	
	public void setOffset(float offsetX, float offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	private void handleSoundVolume(){
		if(getBody() != null && (ScreensManager.getInstance().getCurrentScreenType() == ScreenType.SCREEN_GAME_SINGLE || ScreensManager.getInstance().getCurrentScreenType() == ScreenType.SCREEN_GAME_MULTI)){
			float myPosition = getBody().getPosition().x;
			float playerPosition = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).gameWorld.player.character.getBody().getPosition().x;
			float distance = Math.abs(myPosition - playerPosition);
			
			if(distance > 10){
				setSoundVolume(0);
			}
			else if(1/(distance)>=1){
				setSoundVolume(1);
			}
			else{
				setSoundVolume(1/(distance));
			}
		}
	}
	@Override
	public void act(float delta){
		if(body != null){
			setPosition(body.getPosition().x + offsetX, body.getPosition().y + offsetY);
			setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		}
		else if(updatePosition)
			setPosition(position.x + offsetX, position.y + offsetY);  
        
        if(animationManager != null)
        {
        	currentFrame = animationManager.animate(delta);
        }
        
        if(currentFrame != null){
	        setWidth(currentFrame.getRegionWidth() / PPM);
	        setHeight(currentFrame.getRegionHeight() / PPM);
			setOrigin(-offsetX, -offsetY);
        }

        if(handleSoundVolume)
		handleSoundVolume();
		
	}
	
	public void setAnimate(boolean animate){
		animationManager.setAnimate(animate);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(currentFrame != null)
		{
			float frameWidth = currentFrame.getRegionWidth();
			float frameHeight = currentFrame.getRegionHeight();
			
			if( isGameObstacle )
			{
				frameWidth /= PPM;
				frameHeight /= PPM;
			}

			if(scaleFrames)
			{
				frameWidth *= frameScale;
				frameHeight *= frameScale;
			}
			
			batch.draw(currentFrame, 
					   getX() + ( (((AtlasRegion)currentFrame).offsetX) / PPM ), 
					   getY() + ( (((AtlasRegion)currentFrame).offsetY) / PPM ), 
					   getOriginX(), 
					   getOriginY(), 
					   frameWidth,
					   frameHeight, 
					   1, 
					   1, 
					   getRotation());
		}
	}
	
	public void scaleFrames(float frameScale)
	{
		this.scaleFrames = true;
		this.frameScale = frameScale;
	}
	
	public Body getBody(){ return this.body; }
	public void setBody(Body body) { this.body = body; }
	public void setSoundVolume(float soundVolume){ this.soundVolume = soundVolume; }
	public float getSoundVolume(){ return this.soundVolume; }
	public void setUpdatePosition(boolean updatePosition){
		this.updatePosition = updatePosition;
		setPosition(position.x + offsetX, position.y + offsetY); 
	}
	
	public Vector2 getPosition() { return position; }
	
	public UserData getBodyUserData() { return ( (UserData) ( this.getBody().getUserData() ) ); }
}
