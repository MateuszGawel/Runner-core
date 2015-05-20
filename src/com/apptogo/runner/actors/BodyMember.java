package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BodyMember extends Actor
{
	private Body body;
	private Character player;
	private Random random = new Random();
	private TextureRegion currentFrame;
	private float offsetX=0, offsetY=0, angle=0;
	
	boolean applyForce = true;
		
	public BodyMember(Character player, World world, String path, Shape shape)
	{
		this.player = player;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
				
		UserData userData = new UserData("bodyMember");
		userData.bodyWidth = Box2DVars.getShapeWidth(shape);
		
		FixtureDef fixtureDef;
		fixtureDef = Materials.bodyMemberBody;
		fixtureDef.shape = shape;
		
		body = world.createBody(bodyDef);
		
		body.createFixture(fixtureDef).setUserData( userData );
		body.setUserData( userData );
		
		this.currentFrame = (TextureRegion)ResourcesManager.getInstance().getAtlasRegion(path);
		body.setTransform(-100, 0, 0);
	}
	
	public BodyMember(Character player, World world, String path, Shape shape, Body jointBody, Vector2 anchorA, Vector2 anchorB)
	{
		this(player, world, path, shape);
		
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.bodyA = body;
		jointDef.bodyB = jointBody;
		
		jointDef.localAnchorA.set( anchorA );
		jointDef.localAnchorB.set( anchorB );
		
		jointDef.length = 0;
		
		world.createJoint(jointDef);
		
		applyForce = false;
	}
	
	public BodyMember(Character player, World world, Shape shape, String path, float offsetX, float offsetY, float angle)
	{
		this(player, world, path, shape);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.angle = 0;//angle;
	}
	
	public BodyMember(Character player, World world, Shape shape, String path, float offsetX, float offsetY, float angle, Body jointBody, Vector2 anchorA, Vector2 anchorB)
	{
		this(player, world, path, shape, jointBody, anchorA, anchorB);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.angle = 0;//angle;
	}
	
	public Body getBody()
	{
		return this.body;
	}

    public void init()
    {
    	body.setTransform(player.getX() + offsetX, player.getY() + offsetY + 1.5f, angle);
    	
    	if(applyForce)
    	{
	        body.applyForce(90, 45, 0, 0, true); //random.nextInt(20)-10/PPM, random.nextInt(20)-10/PPM, true);
    	}
    	
    	setOrigin(currentFrame.getRegionWidth()/2/PPM,  currentFrame.getRegionHeight()/2/PPM);
    }
    
	public void reset() 
	{
		body.setTransform(-100, 0, 0);
	}
	
	@Override
	public void act(float delta) 
	{
        setPosition(body.getPosition().x - currentFrame.getRegionWidth()/2/PPM, body.getPosition().y - currentFrame.getRegionHeight()/2/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
}
