package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private Bandit player;

	private float timeToExplode = 2;

	private TextureRegion currentFrame;
	
	private AnimationManager animationManager;
	public enum BombAnimationState{
		NORMAL, EXPLODING
	}
	
	public Bomb(Bandit player, World world){
		this.position = new Vector2();
        this.alive = false;
        this.player = player;
        
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
		
		animationManager = new AnimationManager("gfx/game/characters/bomb.pack");	
		animationManager.createAnimation(5, 0.1f, "bomb", BombAnimationState.NORMAL, true);
		animationManager.createAnimation(new MyAnimation(0.05f, BombAnimationState.EXPLODING, AnimationManager.createFrames(6, "bombExplosion"), false){
			@Override
			public void onAnimationFinished(){
				alive = false;
			}
		});
		animationManager.setCurrentAnimationState(BombAnimationState.NORMAL);
	}

    public void init() {
    	position.set(player.getX(), player.getY());
        bombBody.setTransform(position, 0);
        bombBody.setLinearVelocity(player.getSpeed()/2, 0);
        alive = true;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				animationManager.setCurrentAnimationState(BombAnimationState.EXPLODING);
			}
		}, timeToExplode);
		
		currentFrame = animationManager.animate(0f); //nie wiem czy tak powinno byc wtedy nie ma nulla, tak czy siak jednak rzuca tylko jedna bombe tez nie wiem czy to ok
		
		setOrigin(currentFrame.getRegionWidth()/2/PPM,  currentFrame.getRegionHeight()/2/PPM -7/PPM);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		bombBody.setTransform(position, 0);
        alive = false;
	}
	
	@Override
	public void act(float delta){
		currentFrame = animationManager.animate(delta);
        setPosition(bombBody.getPosition().x - currentFrame.getRegionWidth()/2/PPM, bombBody.getPosition().y - currentFrame.getRegionHeight()/2/PPM + 7/PPM);
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
