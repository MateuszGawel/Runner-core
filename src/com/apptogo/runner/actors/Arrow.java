package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Arrow extends Actor implements Poolable{

	private Vector2 position;
	public boolean alive;
	private Body arrowBody;
	private Archer player;
	private World world;
	private float timeToDisappear = 2;
	
	private Texture arrowTexture;
	private TextureRegion arrowRegion;
	
	public Arrow(Archer player, World world){
		this.position = new Vector2();
        this.alive = false;
        this.player = player;
        this.world = world;
        
        arrowTexture = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/arrow.png");

        
        Vector2 bodySize = new Vector2(25 / PPM, 2 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
		
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDef;
		
		arrowBody = world.createBody(bodyDef);
		arrowBody.setUserData("arrow");
		
		shape.setAsBox(bodySize.x, bodySize.y);
		fixtureDef = Materials.arrowBody;
		fixtureDef.shape = shape;
		arrowBody.createFixture(fixtureDef).setUserData("player");
		arrowRegion = new TextureRegion(arrowTexture);
		
		shape.setAsBox(3/PPM, 3/PPM, new Vector2(12/PPM, 0), 0);
		fixtureDef = Materials.arrowheadBody;
		fixtureDef.shape = shape;
		arrowBody.createFixture(fixtureDef).setUserData("player");
	}

    public void init() {
    	position.set(player.getX()+10/PPM, player.getY()-10/PPM);
    	//http://www.iforce2d.net/b2dtut/sticky-projectiles
    	arrowBody.setTransform(position, 0);
    	arrowBody.setLinearVelocity(30, 30);
        alive = true;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				reset();
			}
		}, timeToDisappear);
		
		setOrigin(arrowTexture.getWidth()/2/PPM,  arrowTexture.getHeight()/2/PPM);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		arrowBody.setTransform(position, 0);
        alive = false;
	}
	
	@Override
	public void act(float delta){
        setPosition(arrowBody.getPosition().x - arrowTexture.getWidth()/2/PPM, arrowBody.getPosition().y - arrowTexture.getHeight()/2/PPM);
        setWidth(arrowTexture.getWidth() / PPM);
        setHeight(arrowTexture.getHeight() / PPM);
        setRotation(arrowBody.getAngle() * MathUtils.radiansToDegrees);
        
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(arrowRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
}
