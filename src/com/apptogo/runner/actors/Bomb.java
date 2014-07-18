package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sun.org.apache.bcel.internal.generic.BALOAD;

public class Bomb extends Actor implements Poolable{

	private Vector2 position;
	public boolean alive;
	private Body bombBody;
	private Bandit player;
	private World world;
	private float timeToExplode = 2;
	
	private TextureAtlas bombAtlas;
	private final int BOMB_FRAMES_COUNT = 5;
	private final int EXPLOSION_FRAMES_COUNT = 6;
	
	private AtlasRegion[] bombFrames;
	private AtlasRegion[] explosionFrames;
	
	private Animation bombAnimation;
	private Animation explosionAnimation;
	
	private TextureRegion currentFrame;
	private float stateTime;
	private boolean exploding;
	
	public Bomb(Bandit player, World world){
		this.position = new Vector2();
        this.alive = false;
        this.player = player;
        this.world = world;
        bombAtlas = ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/bomb.pack");

        
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		FixtureDef fixtureDef;
		
		bombBody = world.createBody(bodyDef);
		bombBody.setUserData("bomb");

		shape.setRadius(14/PPM);
		fixtureDef = Materials.bombBody;
		fixtureDef.shape = shape;
		bombBody.createFixture(fixtureDef).setUserData("player");
		
		//animation
		bombFrames = new AtlasRegion[BOMB_FRAMES_COUNT];
		for(int i=0; i<BOMB_FRAMES_COUNT; i++){
			bombFrames[i] = bombAtlas.findRegion("bomb" + i);
		}
		bombAnimation = new Animation(0.1f, bombFrames);
		
		explosionFrames = new AtlasRegion[EXPLOSION_FRAMES_COUNT];
		for(int i=0; i<EXPLOSION_FRAMES_COUNT; i++){
			explosionFrames[i] = bombAtlas.findRegion("bombExplosion" + i);
		}
		explosionAnimation = new Animation(0.05f, explosionFrames);
	}

    public void init() {
    	position.set(player.getX(), player.getY());
    	stateTime = 0;
        exploding = false;
        bombBody.setTransform(position, 0);
        bombBody.setLinearVelocity(player.getSpeed()/2, 0);
        alive = true;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				stateTime = 0;
				exploding = true;
			}
		}, timeToExplode);
		setOrigin(position.x/PPM,  position.y/PPM);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		bombBody.setTransform(position, 0);
        alive = false;
	}
	
	@Override
	public void act(float delta){
		stateTime += delta;
		if(!exploding)
			currentFrame = bombAnimation.getKeyFrame(stateTime, true);
		else{
			currentFrame = explosionAnimation.getKeyFrame(stateTime, false);
			if(explosionAnimation.isAnimationFinished(stateTime)){
				alive = false;
			}
		}
		
        setPosition(bombBody.getPosition().x - currentFrame.getRegionWidth()/2/PPM, bombBody.getPosition().y - currentFrame.getRegionHeight()/2/PPM + 7/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(bombBody.getAngle() * MathUtils.radiansToDegrees);
        
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), currentFrame.getRegionWidth()/2/PPM, currentFrame.getRegionHeight()/2/PPM -7/PPM, getWidth(), getHeight(), 1, 1, getRotation());
	}
}
