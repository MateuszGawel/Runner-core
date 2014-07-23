package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Barrel extends Actor{

	private TextureRegion texture;
	private Body body;
	private World world; 
	private GameWorld gameWorld;
	private MapObject object;
	
	public Barrel(Shape shape, MapObject object, World world, GameWorld gameWorld){
		this.texture = new TextureRegion((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/levels/barrelSmall.png"));
		this.world = world;
		this.gameWorld = gameWorld;
		this.object = object;
		createBody(shape);
		
		gameWorld.getWorldStage().addActor(this);
	}

	private void createBody(Shape shape){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		FixtureDef fixtureDef = Materials.barrelBody;
		fixtureDef.shape = shape;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData("barrel");
		body.setUserData("barrel");
		
		body.setTransform(((EllipseMapObject)object).getEllipse().x/PPM, ((EllipseMapObject)object).getEllipse().x/PPM, 0);
	}
	
	@Override
	public void act(float delta){
		Logger.log(this, body.getPosition().x + " " + texture.getRegionWidth());
        setPosition(body.getPosition().x, body.getPosition().y);
        setWidth(texture.getRegionWidth() / PPM);
        setHeight(texture.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());	
		batch.draw(texture, getX(), getY(), texture.getRegionWidth()/2/PPM, texture.getRegionHeight()/2/PPM -7/PPM, getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public Body getBody(){ return this.body; }
}
