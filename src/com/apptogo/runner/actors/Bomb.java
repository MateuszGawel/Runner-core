package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Mushroom.MushroomAnimationState;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Bomb extends Obstacle implements Poolable, Ability{

	public boolean alive;
	private Body explodeSensor;
	private boolean exploding;
	
	//parameters
	private int level = 3;
	private float timeToExplode = 2;
	
	private CustomAction explodeAction, explodingAction;
	public ParticleEffectActor explosionParticle;
	public ParticleEffectActor explosionParticlelvl1, explosionParticlelvl2, explosionParticlelvl3;
	public enum BombAnimationState{
		NORMAL
	}
	
	
	private String getRegionName(){
		if(level==1)
			return "bomblvlone";
		else if(level==2)
			return "bomblvltwo";	
		else if(level==3)
			return "bomblvlthree";
		else return null;
	}
	private float getExplosionRange(){
		if(level==1)
			return 2;
		else if(level==2)
			return 3;
		else if(level==3)
			return 5;
		else return 0;
	}
	private String getExplosionParticleName(){
		if(level==1)
			return "explosion_lvl1.p";
		else if(level==2)
			return "explosion_lvl2.p";
		else if(level==3)
			return "explosion_lvl3.p";
		else return null;
	}
	
	public Bomb(World world, GameWorld gameWorld, int level){
		super(new EllipseMapObject(0,0,32,32), world, "gfx/game/characters/charactersAtlas.pack");
		explosionParticle = new ParticleEffectActor(getExplosionParticleName(), 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
		createBody(BodyType.DynamicBody, Materials.bombBody, "bomb");
		createExplosion();
		animationManager.createAnimation(new MyAnimation(0.03f, BombAnimationState.NORMAL, animationManager.createFrames(5, getRegionName()), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(MushroomAnimationState.STATIC);
				setAnimate(false);
			}
		});
		
		
		gameWorld.getWorldStage().addActor(this);
		gameWorld.getWorldStage().addActor(explosionParticle);
		animationManager.setCurrentAnimationState(BombAnimationState.NORMAL);
		currentFrame = animationManager.animate(0f);
	}
	
    public void init(Character characterOwner) {
        body.setTransform(characterOwner.getX()-20/PPM, characterOwner.getY(), 0);
        body.setLinearVelocity(characterOwner.getSpeed()/3, 0);
        body.getFixtureList().get(0).setUserData(new UserData("bomb", characterOwner.playerName));
        explodeSensor.getFixtureList().get(0).setUserData(new UserData("bombExplosion", characterOwner.playerName));
        alive = true;
        setVisible(true);
        explodingAction = new CustomAction(0.5f) {	
			@Override
			public void perform() {
				exploding=false;
	    		reset();
			}
		};
		explodeAction = new CustomAction(timeToExplode) {	
			@Override
			public void perform() {
				exploding=true;
				setVisible(false);
				CustomActionManager.getInstance().registerAction(explodingAction);
				explosionParticle.obtainAndStart(body.getPosition().x+0.25f, body.getPosition().y+0.25f, 0);
			}
		};
		
		CustomActionManager.getInstance().registerAction(explodeAction);	
		
    }
    
    private void explode(){
    	if(!exploding){
	    	CustomActionManager.getInstance().unregisterAction(explodeAction);
	    	CustomActionManager.getInstance().registerAction(explodingAction);	
	    	setVisible(false);
			explosionParticle.obtainAndStart(body.getPosition().x+0.25f, body.getPosition().y+0.25f, 0);
	    	exploding = true;
    	}
    }
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(exploding){ 		 		
    		Vector2 offset = new Vector2((float)Math.sqrt(2)*0.25f, 0).setAngle((body.getAngle())*(float)(180/Math.PI)+45);
    		explodeSensor.setTransform(body.getPosition().x+offset.x, body.getPosition().y+offset.y, body.getAngle());
    	}
    	else if(explodeSensor.getPosition().x > -100)
    		explodeSensor.setTransform(-100, 0, 0);
    	
    	if(((UserData)body.getUserData()).collected){
    		explode();
    		((UserData)body.getUserData()).collected = false;
    	}
    }
    
    private void createExplosion(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		FixtureDef fixtureDef = Materials.obstacleSensor;
		CircleShape shape = new CircleShape();
		shape.setPosition(new Vector2(0, 0));
		shape.setRadius(getExplosionRange());
		fixtureDef.shape = shape;
		
		explodeSensor = world.createBody(bodyDef);
		explodeSensor.createFixture(fixtureDef);
		explodeSensor.setTransform(-100, 0, 0);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		body.setTransform(position, 0);
        alive = false;
	}
}
