package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sun.org.apache.bcel.internal.generic.BALOAD;

public class Bonfire extends Actor{

	private Body body;
	private World world; 
	private MapObject object;
	private GameWorld gameWorld;
	private Vector2 framePosition = new Vector2();
	
	private TextureRegion currentFrame;
	
	private AnimationManager animationManager;
	public enum BonfireAnimationState{
		NORMAL
	}
	
	public Bonfire(MapObject object, World world, GameWorld gameWorld){
		this.world = world;
		this.gameWorld = gameWorld;
		this.object = object;
        createBody();
		
		animationManager = new AnimationManager("gfx/game/levels/bonfire.pack");	
		animationManager.createAnimation(33, 0.05f, "bonfire", BonfireAnimationState.NORMAL, true);
		animationManager.setCurrentAnimationState(BonfireAnimationState.NORMAL);
		
		currentFrame = animationManager.animate(0f);
		
        setPosition(framePosition.x - currentFrame.getRegionWidth()/2/PPM, framePosition.y - currentFrame.getRegionHeight()/2/PPM - 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        
        gameWorld.getWorldStage().addActor(this);
	}
	
	private void createBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		FixtureDef fixtureDef = new FixtureDef();
		
		float[] vertices = ((PolylineMapObject)object).getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
		    worldVertices[i] = new Vector2();
		    worldVertices[i].x = vertices[i * 2] / PPM;
		    worldVertices[i].y = vertices[i * 2 + 1] / PPM;
		}
		framePosition.x = worldVertices[1].x;
		framePosition.y = worldVertices[1].y;
		
		ChainShape chain = new ChainShape(); 
		chain.createChain(worldVertices);

		fixtureDef.shape = chain;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData("killing");
		body.setUserData("killing");
	}
	
	@Override
	public void act(float delta){
		currentFrame = animationManager.animate(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public Body getBody(){ return this.body; }
}
