package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

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
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BodyMember extends Actor
{
	private Body body;
	private Character player;
	private TextureRegion currentFrame;
	private float offsetX=0, offsetY=0, angle=0;
	
	public boolean applyForce = false;
	
	boolean isGhost = false;
	
	public BodyMember(Character player, World world, Shape shape, String path)
	{
		this.player = player;
		
		this.currentFrame = (TextureRegion)ResourcesManager.getInstance().getAtlasRegion(path);
				
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = BodyDef.BodyType.DynamicBody;
				
		UserData userData = new UserData("bodyMember");
		userData.bodyWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyHeight = Box2DVars.getShapeHeight(shape);
		
		FixtureDef fixtureDef;
		fixtureDef = Materials.bodyMemberBody;
		fixtureDef.shape = shape;
				
		body = world.createBody(bodyDef);
		
		body.setAngularDamping(5);
		body.setLinearDamping(5);
		
		body.createFixture(fixtureDef).setUserData( userData );
		body.setUserData( userData );
		
		body.setTransform(-100, 0, 0);
	}
		
	public BodyMember(Character player, World world, Shape shape, String path, float offsetX, float offsetY, float angle)
	{
		this(player, world, shape, path);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.angle = angle;
	}
	
	public BodyMember(Character player, World world, Shape shape, String path, float offsetX, float offsetY, float angle, Body jointBody, Vector2 anchorA, Vector2 anchorB, float minAngle, float maxAngle)
	{
		this(player, world, shape, path, offsetX, offsetY, angle);
				
		RevoluteJointDef jointDef = new RevoluteJointDef();
		
		jointDef.bodyA = body;
		jointDef.bodyB = jointBody;
		
		jointDef.localAnchorA.set( anchorA );
		jointDef.localAnchorB.set( anchorB );
			
		jointDef.collideConnected = true;
		
		jointDef.enableLimit = true;
		jointDef.lowerAngle = (float) Math.toRadians( minAngle );
		jointDef.upperAngle = (float) Math.toRadians( maxAngle );
		
		world.createJoint(jointDef);
	}
		
	public Body getBody()
	{
		return this.body;
	}

    public void init(Vector2 linearVelocity)
    {
    	body.setTransform(player.getX() + offsetX, player.getY() + offsetY + 1.5f, angle);
    	
    	if( applyForce ) body.applyLinearImpulse(new Vector2(2, 0), body.getWorldCenter(), true);
    	
    	setOrigin(currentFrame.getRegionWidth()/2/PPM,  currentFrame.getRegionHeight()/2/PPM);
    }
    
	public void reset() 
	{
		body.setTransform(-100, 0, 0);
	}
	
	@Override
	public void act(float delta) 
	{
		float positionX = body.getPosition().x - (currentFrame.getRegionWidth() - ((UserData)body.getUserData()).bodyWidth)/2f/PPM;
		float positionY = body.getPosition().y - (currentFrame.getRegionHeight() - ((UserData)body.getUserData()).bodyHeight)/2f/PPM;
		
        setPosition(positionX, positionY);
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
