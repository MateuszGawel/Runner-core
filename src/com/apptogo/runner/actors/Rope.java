package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Rope extends Obstacle{

	Body[] segments;
	RevoluteJoint[] joints;
	World world;
	
	final float SEGMENT_SIZE = 6/PPM;
	
	public Rope(World world, GameWorld gameWorld, int count, float x, float y)
	{
		super();
		Logger.log(this , x + ", " + y);
		this.world = world;
		
		//initializing segments and joints arrays
		segments = new Body[count];
		joints = new RevoluteJoint[count-1];
		
		for(int i = 0; i < count; i++)
		{
			if( i == 0 ) segments[i] = createSegment(BodyType.StaticBody, false);
			else 
			{
				segments[i] = createSegment();
			}
		}
		
		//defining rope joint
		
		
		//creating rope joints between segments
		for(int i = 0; i < count - 1; i++)
		{
			RevoluteJointDef ropeJointDef = new RevoluteJointDef();
			ropeJointDef.localAnchorA.set(0, -SEGMENT_SIZE*1.5f);
			ropeJointDef.localAnchorB.set(0, SEGMENT_SIZE*1.5f);
			ropeJointDef.collideConnected = false;
			//ropeJointDef.maxLength = 1/PPM;
			
			ropeJointDef.bodyA = segments[i];
			ropeJointDef.bodyB = segments[i+1];
			
			ropeJointDef.enableLimit = true;
			ropeJointDef.upperAngle = (float)Math.toRadians(0);
			ropeJointDef.lowerAngle = (float)Math.toRadians(0);
			
			joints[i] = (RevoluteJoint)world.createJoint(ropeJointDef);
		}
		
		//creating the static body that we will attach rope to
		//Body staticOne = createSegment(BodyType.StaticBody, true);
		
		//defining revolute joint
		//RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
				
		//creating revolute joint between beggining of rope and static body
		//revoluteJointDef.bodyA = staticOne;
		//revoluteJointDef.bodyB = segments[0];
		
		//world.createJoint(revoluteJointDef);
		
		//setting the static body's position
		//staticOne.setTransform(new Vector2(x, y), 0);
		
		//setting the segments positions
		for(int i = 0; i < count; i++)
		{ 
			segments[i].setTransform(new Vector2(x, y), (float)Math.toRadians(90));
			x += 3 * SEGMENT_SIZE;
			//y -= SEGMENT_SIZE;
		}
		
		gameWorld.getWorldStage().addActor(this);
	}
	
	Body createSegment()
	{
		return createSegment(BodyType.DynamicBody, false);
	}
	
	Body createSegment(BodyType bodyType, boolean isSensor)
	{
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = bodyType;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(SEGMENT_SIZE / 3f, SEGMENT_SIZE); //!!
		
		Body body = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = Materials.bodyMemberBody;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = isSensor;
		
		body.createFixture(fixtureDef);
		
		return body;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if(Gdx.input.isKeyPressed(Keys.G)) 
		{
			for(Body b : segments) b.setActive(true);
		}
		else for(Body b : segments) b.setActive(false);
	}
}
