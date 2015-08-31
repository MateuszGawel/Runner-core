package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LiftField extends Actor{

	private Body fieldBody;
	private GameWorld gameWorld;
	private int level = 1;
	public boolean alive = false;

	private ParticleEffectActor effectActorOne, effectActorTwo, effectActorThree;
	private Character ownerCharacter;
	
	public LiftField(World world, GameWorld gameWorld){

        this.gameWorld = gameWorld;
        BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(400/PPM);
		
		fieldBody = world.createBody(bodyDef);
		
		FixtureDef fixtureDef;
		fixtureDef = Materials.worldObjectBody;
		fixtureDef.shape = shape;
		
		Fixture fieldFixture = fieldBody.createFixture(fixtureDef);
		fieldFixture.setSensor(true);
		
    	UserData userData = new UserData("liftField");
    	float shapeWidth = Box2DVars.getShapeWidth(shape);
    	userData.bodyWidth = shapeWidth;
    	fieldBody.getFixtureList().get(0).setUserData( userData );
		fieldBody.setUserData( userData );
		
		fieldBody.setTransform(-100f, 0, 0);
			
		effectActorOne = new ParticleEffectActor("liftField-lvl1.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		effectActorTwo = new ParticleEffectActor("liftField-lvl2.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		effectActorThree = new ParticleEffectActor("liftField-lvl3.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		
		
		gameWorld.getBackgroundStage().addActor(this);
	}

	private ParticleEffectActor getParticleEffectActor(){
		switch(level){
		case 1:
			return effectActorOne;
		case 2:
			return effectActorTwo;
		case 3:
			return effectActorThree;
		default:
			return effectActorOne;
		
		}
	}
	
	private PooledEffect pooledEffect;
    public void init(Character character, int level) {
    	this.level = level;
    	alive = true;
    	gameWorld.getWorldStage().addActor(getParticleEffectActor());
    	this.ownerCharacter = character;
    	((UserData)fieldBody.getFixtureList().get(0).getUserData()).abilityLevel = level;
    	((UserData)fieldBody.getFixtureList().get(0).getUserData()).playerName = character.playerName;
		
		setPosition(ownerCharacter.getX(), ownerCharacter.getY());
		fieldBody.setTransform(ownerCharacter.getX() + 50/PPM, ownerCharacter.getY() + 5/PPM, 0);
		pooledEffect = getParticleEffectActor().obtainAndStart(fieldBody.getPosition().x+20/PPM, fieldBody.getPosition().y+90/PPM, 0);
    	
		CustomActionManager.getInstance().registerAction(new CustomAction(0.4f) {
			@Override
			public void perform() {
				reset();
			}
		});
    }
    
	public void reset() {
		alive = false;
		fieldBody.setTransform(-100f, 0, 45);
	}
	
	@Override
	public void act(float delta){
		if(alive){
			setPosition(ownerCharacter.getX(), ownerCharacter.getY());
			fieldBody.setTransform(ownerCharacter.getX() + 50/PPM, ownerCharacter.getY() + 5/PPM, 0);
			
		}
		if(!pooledEffect.isComplete()){
			getParticleEffectActor().setPosition(ownerCharacter.getX() + 50/PPM, ownerCharacter.getY()+ 5/PPM);
			pooledEffect.setPosition(ownerCharacter.getX() + 50/PPM, ownerCharacter.getY()+ 90/PPM);
		}
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
