package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BodyMember extends Actor{

	private Body body;
	private Character player;
	private Random random = new Random();
	private TextureRegion currentFrame;
	private float offsetX=0, offsetY=0, angle=0;
	
	public BodyMember(Character player, World world, Shape shape, String path){
        this.player = player;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		

		FixtureDef fixtureDef;
		
		body = world.createBody(bodyDef);
		body.setUserData("bodyMember");


		fixtureDef = Materials.bodyMemberBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData("bodyMember");
		
		this.currentFrame = new TextureRegion((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), path));
		body.setTransform(-100, 0, 0);
	}
	
	public BodyMember(Character player, World world, Shape shape, String path, float offsetX, float offsetY, float angle){
		this(player, world, shape, path);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.angle = angle;
	}

    public void init() {
        body.setTransform(player.getX() + offsetX, player.getY() + offsetY, angle);
        body.applyForce(0, 0, random.nextInt(20)-10/PPM, random.nextInt(20)-10/PPM, true);
		setOrigin(currentFrame.getRegionWidth()/2/PPM,  currentFrame.getRegionHeight()/2/PPM);
    }
    
	public void reset() {
		body.setTransform(-100, 0, 0);
	}
	
	@Override
	public void act(float delta){
        setPosition(body.getPosition().x - currentFrame.getRegionWidth()/2/PPM, body.getPosition().y - currentFrame.getRegionHeight()/2/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
}
