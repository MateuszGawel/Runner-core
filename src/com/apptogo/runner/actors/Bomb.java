package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

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

public class Bomb extends Actor implements Poolable{

	private Vector2 position;
	public boolean alive;
	private Body bombBody;
	private Player player;
	private World world;
	private float timeToExplode = 2;
	
	private TextureAtlas bombAtlas;
	private final int BOMB_FRAMES_COUNT = 3;
	private final int EXPLOSION_FRAMES_COUNT = 6;
	
	private AtlasRegion[] bombFrames;
	private AtlasRegion[] explosionFrames;
	
	private Animation bombAnimation;
	private Animation explosionAnimation;
	
	private TextureRegion currentFrame;
	
	public Bomb(Player player, World world){
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

		shape.setRadius(10/PPM);
		fixtureDef = Materials.bombBody;
		fixtureDef.shape = shape;
		bombBody.createFixture(fixtureDef).setUserData("player");
		
		//animation
		bombFrames = new AtlasRegion[BOMB_FRAMES_COUNT];
		for(int i=0; i<BOMB_FRAMES_COUNT; i++){
			bombFrames[i] = bombAtlas.findRegion("bomb" + i);
		}
		bombAnimation = new Animation(0.03f, bombFrames);
		
		explosionFrames = new AtlasRegion[EXPLOSION_FRAMES_COUNT];
		for(int i=0; i<EXPLOSION_FRAMES_COUNT; i++){
			explosionFrames[i] = bombAtlas.findRegion("bombExplosion" + i);
		}
		explosionAnimation = new Animation(0.03f, explosionFrames);
	}

    public void init() {
    	position.set(player.getX(), player.getY());
        bombBody.setTransform(position, 0);
        bombBody.setLinearVelocity(player.getPlayerSpeed()/2, 0);
        alive = true;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				alive = false;
			}
		}, timeToExplode);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		bombBody.setTransform(position, 0);
        alive = false;
	}
	
	@Override
	public void act(float delta){
		currentFrame = bombAnimation.getKeyFrame(delta, true);
        setPosition(bombBody.getPosition().x - currentFrame.getRegionWidth()/2/PPM, bombBody.getPosition().y - currentFrame.getRegionHeight()/2/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(bombBody.getAngle() * MathUtils.radiansToDegrees);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());	
	}
}
