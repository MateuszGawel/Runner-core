package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Oil extends Actor implements Poolable{

	public boolean alive;
	public Body oilBody;
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
	
    public void init(Character characterOwner, int level, float linearVelocity, Object o) {
    	this.level = level;
    	setAnimationState();
    	oilBody.setTransform(characterOwner.getX()-10/PPM, characterOwner.getY() + 10/PPM, 0);
    	//oilBody.setLinearVelocity(linearVelocity, 0);
    	oilBody.applyLinearImpulse(new Vector2(linearVelocity, 0), oilBody.getWorldCenter(), true);
    	oilBody.setUserData("oil number " + o);
    	oilBody.getFixtureList().get(0).setUserData(new UserData("oil", characterOwner.playerName));
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
	        
	        //join powinien sie zerwac na krawedzi bo olej zawisnie w powietrzu
	        for(JointEdge jointEdge : oilBody.getJointList())
			{
	        	if( Math.abs(jointEdge.other.getPosition().y - oilBody.getPosition().y ) > 2/PPM )
	        	{
	        		this.gameWorld.world.destroyJoint(jointEdge.joint);
	        	}
			}
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
		
		for(JointEdge jointEdge : oilBody.getJointList())
		{
			if( jointEdge.joint != null )
			{
				this.gameWorld.world.destroyJoint(jointEdge.joint);
			}
		}
		
		oilBody.setTransform(-100, 0, 0);
        alive = false;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
