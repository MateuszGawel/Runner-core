package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Ufo extends Actor implements Poolable{

	public boolean alive;
	private Body ufoBody;
	private GameWorld gameWorld;
	//parameters
	private int level = 1;
	private AtlasRegion currentFrame;
	private AnimationManager animationManager;
	private TextureAtlas atlas;
	private Character characterTarget;
	private CustomAction shootAction;
	private boolean flyAway, alreadyShot;
	private ParticleEffectActor shootLoadEffect;
	private PooledEffect pooledShoot;
	private Vector2 startingPos;
	private LaserShoot laserShoot;
	private Sound ufo, laser;
	
	public enum DeathAnimationState{
		NORMAL
	}
	
	public Ufo(World world, GameWorld gameWorld){
		this.gameWorld = gameWorld;
		ufo = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/ufo.ogg");
		laser = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/laser.ogg");
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));

        animationManager = new AnimationManager();
		animationManager.createAnimation(new MyAnimation(0.03f, DeathAnimationState.NORMAL, animationManager.createFrames(24, "ufo"), true));	
		animationManager.setCurrentAnimationState(DeathAnimationState.NORMAL);
		
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.fixedRotation = true;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.1f);
		UserData userData = new UserData("death");
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyWidth = shapeWidth;
		
		FixtureDef fixtureDef = Materials.obstacleSensor;
		fixtureDef.shape = shape;
		
		ufoBody = world.createBody(bodyDef);
		ufoBody.createFixture(fixtureDef).setUserData( userData );
		ufoBody.setUserData(new UserData(userData));
		gameWorld.getWorldStage().addActor(this);
		setSize(203/PPM, 121/PPM);
		
		laserShoot = new LaserShoot();
		
		shootAction = new CustomAction(2, 1) {
			@Override
			public void perform() {
				
				laserShoot.init();	

				alreadyShot = true;
				characterTarget.dieDismemberment();
				startingPos = new Vector2(ufoBody.getPosition());
			}
		};
		
		shootLoadEffect = new ParticleEffectActor("ufoShootLoad.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		laserShoot = new LaserShoot();
	}
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	this.characterTarget = characterOwner;
    	startingPos = new Vector2(characterOwner.getX()-15, characterOwner.getY()+10);
    	ufoBody.setTransform(startingPos, 0);
        flyAway = false;
        shootAction.resetAction();
    	ufoBody.getFixtureList().get(0).setUserData(new UserData("death", characterOwner.playerName));
        alive = true;
        setVisible(true);
        ufo.play();
        gameWorld.getWorldStage().addActor(shootLoadEffect);
    }
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(currentFrame != null && alive){
    		if(!flyAway)
    			ufoBody.setTransform(startingPos.lerp(new Vector2(characterTarget.getBody().getPosition().x+1, characterTarget.getBody().getPosition().y+5), 0.1f),0);
    		else{
    			ufoBody.setTransform(startingPos.lerp(new Vector2(characterTarget.getBody().getPosition().x+25, characterTarget.getBody().getPosition().y+10), 0.02f),0);
	    		if(ufoBody.getPosition().y >= characterTarget.getBody().getPosition().y+9){
	    			reset();
	    		}
	    	}
    		
    		if(!flyAway && !alreadyShot && characterTarget.getBody().getPosition().dst(ufoBody.getPosition()) <= 6 && !shootAction.isRegistered()){
    			pooledShoot = shootLoadEffect.obtainAndStart(ufoBody.getPosition().x, ufoBody.getPosition().y, 0);
    			laser.play();
    			CustomActionManager.getInstance().registerAction(shootAction);
    		}
    		if(pooledShoot != null){
    			shootLoadEffect.setPosition(ufoBody.getPosition().x, ufoBody.getPosition().y);
    			pooledShoot.setPosition(ufoBody.getPosition().x, ufoBody.getPosition().y);
    		}
	    	setPosition(ufoBody.getPosition().x - 101/PPM, ufoBody.getPosition().y-20/PPM);
    	}
    	currentFrame = (AtlasRegion)animationManager.animate(delta);
    }
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
        setWidth(currentFrame.getRegionWidth() / PPM );
        setHeight(currentFrame.getRegionHeight() / PPM);
		//batch.draw(currentRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.draw(currentFrame.getTexture(),  //Texture texture
				   getX() + currentFrame.offsetX / PPM, //float x
                   getY() + currentFrame.offsetY / PPM, //float y
                   getOriginX(),  //float originX
                   getOriginY(),  //float originY
                   getWidth(),    //float width
                   getHeight(),   //float height
                   getScaleX(),             //float scaleX
                   getScaleY(),             //float scaleY
                   getRotation(), //float rotation
                   currentFrame.getRegionX(), //int srcX
                   currentFrame.getRegionY(), //int srcY
                   currentFrame.getRegionWidth(), //int srcWidth
                   currentFrame.getRegionHeight(),//int srcHeight 
                   false, //boolean flipX
                   false  //boolean flipY
                  );
	}
	
	@Override
	public void reset() {
		ufoBody.setTransform(-100, 0, 0);
		setVisible(false);
        alive = false;
        flyAway = false;
        shootAction.resetAction();
        alreadyShot = false;
        shootLoadEffect.remove();
        pooledShoot = null;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	class LaserShoot extends Actor{
		private TextureRegion texture;
		private float width=18/PPM, height = 0;
		private Vector2 direction;
		private float alpha = 1;
		
		public LaserShoot(){
			texture = atlas.findRegion("laserPattern");
			gameWorld.getWorldStage().addActor(this);
		}
		
		public void init(){
			direction = new Vector2(characterTarget.getBody().getPosition().x, characterTarget.getBody().getPosition().y - 1);
			height = ufoBody.getPosition().dst(direction);
//			setRotation(ufoBody.getPosition().angle(direction)-180);
			Vector2 temp = new Vector2( direction.x - ufoBody.getPosition().x, direction.y - ufoBody.getPosition().y );
			setRotation(temp.angle() - 90);
			setPosition(ufoBody.getPosition().x, ufoBody.getPosition().y);
			alpha=1;
			CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 20) {		
				@Override
				public void perform() {
					alpha-=0.05f;
					if(alpha<=0){
						flyAway = true;
					}
				}
			});
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setColor(1,1,1, alpha);
			super.draw(batch, parentAlpha);
			batch.draw(texture, ufoBody.getPosition().x-width/2,  ufoBody.getPosition().y, width/2, 0, width, height, 1, 1, getRotation());
			batch.setColor(1, 1, 1, 1);
		}
	}

}
