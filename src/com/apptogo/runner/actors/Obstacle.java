package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.SpaceWorld;
import com.apptogo.runner.world.WildWestWorld;
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

	protected Body body;
	protected World world; 
	private MapObject object;
	private BodyDef bodyDef;

	protected boolean animate;
	
	private float offsetX = 0;
	private float offsetY = 0;
	
	protected boolean updatePosition = true;
	protected Vector2 position;
	protected TextureRegion currentFrame;
	protected AnimationManager animationManager;
	protected GameWorld gameWorld;
	private float soundVolume = 1;

	//ta klasa odpowiada za stworzenie obiektu animowanego lub sta³ego w odpowiednim miejscu a nastepnie jego body
	
	private Obstacle(MapObject object, World world){
		this.world = world;
		this.object = object;
	}
	
	public Obstacle(String atlasPath){	
		
		if(atlasPath.contains("powerup")){
	        if(gameWorld instanceof WildWestWorld) animationManager = new AnimationManager("gfx/game/levels/powerup.pack");	
	        else if(gameWorld instanceof ForestWorld) animationManager = new AnimationManager("gfx/game/levels/powerup.pack");	
	        else if(gameWorld instanceof SpaceWorld) animationManager = new AnimationManager("gfx/game/levels/powerup.pack");	
		}
		else
			animationManager = new AnimationManager(atlasPath);	
	}
	
	public Obstacle(MapObject object, World world, String texturePath){	
		this(object, world);
		this.currentFrame = new TextureRegion((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), texturePath));
	}
	
	public Obstacle(String atlasPath, String regionName, int frameCount, float frameDuration, Object animationState){
		animationManager = new AnimationManager(atlasPath);	
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, true);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
	}
	
	public Obstacle(MapObject object, World world, String atlasPath, String regionName, int frameCount, float frameDuration, Object animationState){
		this(object, world);
		animationManager = new AnimationManager(atlasPath);	
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, true);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
	}
	
	public Obstacle(MapObject object, World world, String atlasPath, String regionName, int frameCount, float frameDuration, Object animationState, GameWorld gameWorld){
		this(object, world, atlasPath, regionName, frameCount, frameDuration, animationState);
		this.gameWorld = gameWorld;
	}
	
	public void createAnimation(String regionName, int frameCount, float frameDuration, Object animationState, boolean looping){
		animationManager.createAnimation(frameCount, frameDuration, regionName, animationState, looping);
		animationManager.setCurrentAnimationState(animationState);
		currentFrame = animationManager.animate(0f);
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
	
	private Shape getShape(EllipseMapObject object)
	{
		Ellipse ellipse = object.getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ((ellipse.width < ellipse.height) ? ellipse.width : ellipse.height)/2f;
		ellipseShape.setRadius(radius/PPM);
		ellipseShape.setPosition(new Vector2(radius/PPM, radius/PPM));
		position = new Vector2(ellipse.x/PPM, ellipse.y/PPM);
		bodyDef.position.set(position);

		return ellipseShape;
	}
	
	private Shape getShape(RectangleMapObject obj){
		Rectangle rectangle = obj.getRectangle();
		PolygonShape polygonShape = new PolygonShape();
		position = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
		                           (rectangle.y + rectangle.height * 0.5f ) / PPM);
		polygonShape.setAsBox(rectangle.width * 0.5f / PPM, rectangle.height * 0.5f / PPM);
		bodyDef.position.set(position);
		return polygonShape;
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
		bodyDef.position.set(position);
		polygon.set(worldVertices);
		return polygon;
	}
	
	public void createShape(){
		createShape(object);
	}
	
	public void createBody(BodyType bodyType, FixtureDef fixtureDef, String userData)
	{
		bodyDef = new BodyDef();
		bodyDef.type = bodyType;

		fixtureDef.shape = createShape(object);
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData( new UserData(userData) );
		body.setUserData( new UserData(userData) );
	}
	public void createFixture(FixtureDef fixtureDef, String userData)
	{
		fixtureDef.shape = createShape(object);
		body.createFixture(fixtureDef).setUserData( new UserData(userData) );
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
			float playerPosition = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).world.player.character.getBody().getPosition().x;
			float distance = Math.abs(myPosition - playerPosition);
			
			//Logger.log(this, "my position: " + myPosition + " player position: " + playerPosition + " i roznica: " + (Math.abs(playerPosition - myPosition)));
			
			if(distance > 10){
				setSoundVolume(0);
			}
			else if(1/(distance)>=1){
				setSoundVolume(1);
			}
			else{
				setSoundVolume(1/(distance));
			}
			//Logger.log(this, "glosnosc: " + 1/(distance));
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
        
        if(animationManager != null && animate)
        	currentFrame = animationManager.animate(delta);
        
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
		setOrigin(-offsetX, -offsetY);
		handleSoundVolume();
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
		//Logger.log(this, getX() + " " + getY());
	}
	
	public Body getBody(){ return this.body; }
	public void setSoundVolume(float soundVolume){ this.soundVolume = soundVolume; }
	public float getSoundVolume(){ return this.soundVolume; }
	public void setUpdatePosition(boolean updatePosition){
		this.updatePosition = updatePosition;
		setPosition(position.x + offsetX, position.y + offsetY); 
	}
}
