package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static java.lang.Math.*;

public class Enemy extends Actor{

	private World world;
	private float stateTime;
	private PlayerState currentState = PlayerState.IDLE;
	
	private final int SHEET_ROWS = 11;
	private final int SHEET_COLUMNS = 12;
	private Body playerBody;
	private TextureRegion currentFrame;
	TextureRegion[] playerFrames;
	
	TextureRegion[] breatheFrames;
	TextureRegion[] blinkFrames;
	TextureRegion[] runFrames;
	TextureRegion[] startFrames;
	TextureRegion[] dieTopFrames;
	TextureRegion[] dieBottomFrames;
	TextureRegion[] slideFrames;
	TextureRegion[] jumpFrames;
	TextureRegion[] landFrames;
	TextureRegion[] standupFrames;
	
	Animation breatheAnimation;
	Animation blinkAnimation;
	Animation runAnimation;
	Animation startAnimation;
	Animation dieTopAnimation;
	Animation dieBottomAnimation;
	Animation slideAnimation;
	Animation jumpAnimation;
	Animation landAnimation;
	Animation standupAnimation;
	
	Texture bandit;
	
	public Enemy(World world){
		this.world = world;
		createPlayerBody();
		createPlayerAnimation();
	}
	
	public enum PlayerState{
		IDLE, RUNNING, JUMPING
	}
	
	private void createPlayerBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(50 / PPM, 200 / PPM));
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(16 / PPM, 40 / PPM); // ludzik ma 0.5m x 1m (troche wiecej niz 1m)
		
		FixtureDef fixtureDef = Materials.enemyBody;
		fixtureDef.shape = shape;
		
		playerBody = world.createBody(bodyDef);
		playerBody.createFixture(fixtureDef);
	}
	
	private void createPlayerAnimation(){
		bandit = (Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/bandit.png");
		/*TextureRegion[][] playerRegionsTemp = TextureRegion.split(playerSheet, playerSheet.getWidth()/12, playerSheet.getHeight()/11);
		
		playerFrames = new TextureRegion[SHEET_ROWS * SHEET_COLUMNS];
        int index = 0;
        for (int i = 0; i < SHEET_ROWS; i++) {
            for (int j = 0; j < SHEET_COLUMNS; j++) {
            	playerFrames[index++] = playerRegionsTemp[i][j];
            }
        }
        blinkFrames = getFrames(0, 5, playerFrames);
    	breatheFrames  = getFrames(6, 23, playerFrames);
    	dieTopFrames = getFrames(24, 36, playerFrames);
    	dieBottomFrames = getFrames(37, 48, playerFrames);
    	jumpFrames = getFrames(49, 61, playerFrames);
    	landFrames = getFrames(62, 74, playerFrames);
    	runFrames = getFrames(75, 105, playerFrames);
    	slideFrames = getFrames(106, 112, playerFrames);
    	standupFrames = getFrames(113, 118, playerFrames);
    	startFrames = getFrames(119, 131, playerFrames);
    	
    	breatheAnimation = new Animation(0.1f, breatheFrames);
    	blinkAnimation = new Animation(0.02f, blinkFrames);
    	runAnimation = new Animation(0.014f, runFrames);
    	startAnimation = new Animation(0.010f, startFrames);
    	dieTopAnimation = new Animation(0.02f, dieTopFrames);
    	dieBottomAnimation = new Animation(0.02f, dieBottomFrames);
    	slideAnimation = new Animation(0.02f, slideFrames);
    	jumpAnimation = new Animation(0.02f, jumpFrames);
    	landAnimation = new Animation(0.02f, landFrames);
    	standupAnimation = new Animation(0.02f, standupFrames);
    	*/
    	stateTime = 0f;
	}
	
	private TextureRegion[] getFrames(int from, int to, TextureRegion[] frames){
		TextureRegion[] temp = new TextureRegion[to - from + 1];
		int index = 0;
		for(int i=from; i<=to; i++){
			temp[index] = frames[i];
			index++;
		}
		return temp;
	}
	
	public void jump(double meters){
		float v0 = (float) sqrt( 20 * meters );
		System.out.println("MASA: "+playerBody.getMass());
		playerBody.setLinearVelocity(0, v0); 
	}
	
	public void startRunning(){
		currentState = PlayerState.RUNNING;
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		/*stateTime += delta;
		if(currentState == PlayerState.IDLE){
			currentFrame = breatheAnimation.getKeyFrame(stateTime, true);
		}
		if(currentState == PlayerState.RUNNING){
			playerBody.setLinearVelocity(5, playerBody.getLinearVelocity().y);
			currentFrame = runAnimation.getKeyFrame(stateTime, true);
		}
        setPosition(playerBody.getPosition().x, playerBody.getPosition().y);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(playerBody.getAngle() * MathUtils.radiansToDegrees);
        setOrigin(getWidth() / 2, getHeight() / 2);*/
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(bandit, 2, 10, bandit.getWidth() / PPM, bandit.getHeight() / PPM);
	}
	
	public PlayerState getCurrentState(){ return this.currentState; }
	public Body getPlayerBody(){ return this.playerBody; }
}
