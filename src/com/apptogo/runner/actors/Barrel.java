package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
	
	public Barrel(MapObject object, World world, GameWorld gameWorld){
		this.texture = new TextureRegion((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/barrelSmall.png"));
		this.world = world;
		this.gameWorld = gameWorld;
		this.object = object;
		createBody();
		
		gameWorld.getWorldStage().addActor(this);

	}

	private void createBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
				
		FixtureDef fixtureDef = Materials.barrelBody;
		Ellipse ellipse = ((EllipseMapObject)object).getEllipse();
		CircleShape ellipseShape = new CircleShape();

		float radius = ((ellipse.width < ellipse.height) ? ellipse.width : ellipse.height)/2f;
		ellipseShape.setRadius(radius/PPM);
		ellipseShape.setPosition(new Vector2(radius/PPM, radius/PPM));
		fixtureDef.shape = ellipseShape;
		
		bodyDef.position.set(new Vector2(ellipse.x/PPM, ellipse.y/PPM));
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData("barrel");
		body.setUserData("barrel");
		
		setOrigin(0, 0);
		//body.setTransform(((EllipseMapObject)object).getEllipse().x/PPM, ((EllipseMapObject)object).getEllipse().x/PPM, 0);
		Logger.log(this, "shape x: " + ((EllipseMapObject)object).getEllipse().x + " shape y: " + ((EllipseMapObject)object).getEllipse().y);
		Logger.log(this, "body x: " + body.getPosition().x + " body y: " + body.getPosition().y);
	}
	
	@Override
	public void act(float delta){
		//Logger.log(this, body.getPosition().x + " " + texture.getRegionWidth());
        setPosition(body.getPosition().x, body.getPosition().y);
        setWidth(texture.getRegionWidth() / PPM);
        setHeight(texture.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());	
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public Body getBody(){ return this.body; }
}
