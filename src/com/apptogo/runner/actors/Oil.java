package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.actors.Boar.BoarAnimationState;
import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Oil extends Actor implements Poolable{

	public boolean alive;
	private Body oilBody, oilSensor;
	private GameWorld gameWorld;
	//parameters
	private int level = 1;
	private AtlasRegion currentFrame;
	private AnimationManager animationManager;
	private TextureAtlas atlas;
	
	public enum OilAnimationState{
		LVL1, LVL2, LVL3
	}
	
	private void setAnimationState(){
		switch(level){
			case 1:
				animationManager.setCurrentAnimationState(OilAnimationState.LVL1);
				break;
			case 2:
				animationManager.setCurrentAnimationState(OilAnimationState.LVL2);
				break;
			case 3:
				animationManager.setCurrentAnimationState(OilAnimationState.LVL3);
				break;
		}
	}
	
	public Oil(World world, GameWorld gameWorld){
		this.gameWorld = gameWorld;
		//setAnimationState();
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
		currentFrame = atlas.findRegion("oil");
        animationManager = new AnimationManager();
//		animationManager.createAnimation(new MyAnimation(0.03f, BoarAnimationState.LVL1, animationManager.createFrames(1, "oil"), true));	
//		animationManager.createAnimation(new MyAnimation(0.03f, BoarAnimationState.LVL2, animationManager.createFrames(1, "oil"), true));	
//		animationManager.createAnimation(new MyAnimation(0.03f, BoarAnimationState.LVL3, animationManager.createFrames(1, "oil"), true));	
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.1f);
		UserData userData = new UserData("oil");
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyWidth = shapeWidth;
		
		FixtureDef fixtureDef = Materials.oilBody;
		fixtureDef.shape = shape;
		
		oilBody = world.createBody(bodyDef);
		oilBody.createFixture(fixtureDef).setUserData( userData );
		oilBody.setUserData(new UserData(userData));
		
		gameWorld.getWorldStage().addActor(this);	
	}
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	setAnimationState();
    	oilBody.setTransform(characterOwner.getX()-10/PPM-new Random().nextInt(10)/PPM, characterOwner.getY()+new Random().nextInt(10)/PPM, 0);
    	oilBody.setLinearVelocity(characterOwner.getSpeed()/10, 0);
    	oilBody.getFixtureList().get(0).setUserData(new UserData("bomb", characterOwner.playerName));
        alive = true;
        setVisible(true);
    }
    

    @Override
    public void act(float delta){
    	super.act(delta);
    	if(currentFrame != null){
	    	setPosition(oilBody.getPosition().x, oilBody.getPosition().y);
	        setWidth(currentFrame.getRegionWidth() / PPM);
	        setHeight(currentFrame.getRegionHeight() / PPM);
	        setOrigin(getWidth()/2, getHeight()/2);
    	}
    	//currentFrame = (AtlasRegion)animationManager.animate(delta);
    }
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(currentRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		AtlasRegion currentRegion = (AtlasRegion)currentFrame;
		batch.draw(currentFrame.getTexture(),  //Texture texture
				   getX() + ( (currentRegion.offsetX) / PPM) - currentRegion.originalWidth/2/PPM, //float x
                   getY() + ( (currentRegion.offsetY) / PPM) - currentRegion.originalHeight/2/PPM - 0.2f, //float y
                   getOriginX(),  //float originX
                   getOriginY(),  //float originY
                   getWidth(),    //float width
                   getHeight(),   //float height
                   getScaleX(),             //float scaleX
                   getScaleY(),             //float scaleY
                   getRotation(), //float rotation
                   currentFrame.getRegionX(), //int srcX
                   currentFrame.getRegionY(), //int srcY
                   currentFrame.getRegionWidth(), //int srcWidth
                   currentFrame.getRegionHeight(),//int srcHeight 
                   false, //boolean flipX
                   false  //boolean flipY
                  );
	}
	
	@Override
	public void reset() {
		oilBody.setTransform(-100, 0, 0);
        alive = false;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
