package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LiftField extends Actor{

	private Body fieldBody;
	private Alien player;
	private GameWorld gameWorld;

	private boolean active = false;

	private ParticleEffectActor effectActor;

	private String ownerPlayer;
	
	public LiftField(Alien player, World world, String ownerPlayer, GameWorld gameWorld){
        this.player = player;
        this.ownerPlayer = ownerPlayer;
        this.gameWorld = gameWorld;
        BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(400/PPM);
		
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		
		UserData userData = new UserData("liftField", ownerPlayer);
		userData.bodyWidth = shapeWidth;
		
		fieldBody = world.createBody(bodyDef);
		
		FixtureDef fixtureDef;
		fixtureDef = Materials.worldObjectBody;
		fixtureDef.shape = shape;
		
		Fixture fieldFixture = fieldBody.createFixture(fixtureDef);
		fieldFixture.setSensor(true);
		
		fieldFixture.setUserData( userData );
		fieldBody.setUserData( userData );
		
		fieldBody.setTransform(-100f, 0, 0);
	}

    public void init() {
    	active = true;
    	setPosition(player.getX(), player.getY());
		fieldBody.setTransform(player.getX() + 50/PPM, player.getY() + 5/PPM, 0);
		
		effectActor = new ParticleEffectActor("liftField-lvl1.p", (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
		effectActor.scaleBy(1/PPM);
		gameWorld.getWorldStage().addActor(effectActor);
		effectActor.setPosition(getX() + getWidth()/2, getY() + getHeight()/2);
		effectActor.removeAfterComplete = true;
    	effectActor.start();
    	
		CustomActionManager.getInstance().registerAction(new CustomAction(1f) {
			@Override
			public void perform() {
				reset();
			}
		});
    }
    
	public void reset() {
		active = false;
		fieldBody.setTransform(-100f, 0, 45);
	}
	
	@Override
	public void act(float delta){
		if(active){
			setPosition(player.getX(), player.getY());
			fieldBody.setTransform(player.getX() + 50/PPM, player.getY() + 5/PPM, 0);
		}
	}
}
