package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Jaws extends Group implements Poolable{

	public boolean alive;
	private GameWorld gameWorld;
	//parameters
	private int level = 1;

	private TextureAtlas atlas;
	private Character characterTarget;
	private AnimationManager animationManager;
	private AtlasRegion currentFrame;
	private RotateToAction rotateToAction;
	private float alpha;
	private Sound jaws, roar;
	public ParticleEffectActor explosionParticle;
	private PooledEffect pooledEffect;
	
	public enum DeathAnimationState{
		OPEN, CLOSE
	}
	
	public Jaws(World world, GameWorld gameWorld){
		this.gameWorld = gameWorld;		
		jaws = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/jaws.ogg");
		roar = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/roar.ogg");
        rotateToAction = Actions.action(RotateToAction.class);
        rotateToAction.setRotation(0);
        rotateToAction.setDuration(1.8f);

        
        explosionParticle = new ParticleEffectActor("jaws.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
        this.addActor(explosionParticle);
        
        animationManager = new AnimationManager();
		animationManager.createAnimation(new MyAnimation(0.03f, DeathAnimationState.OPEN, animationManager.createFrames(8, "jaws_open"), false){
			@Override
			public void onAnimationFinished(){
				
		        CustomActionManager.getInstance().registerAction(new CustomAction(1.5f) {
					@Override
					public void perform() {
						if(animationManager.getCurrentAnimationState() != DeathAnimationState.CLOSE){
							animationManager.setCurrentAnimationState(DeathAnimationState.CLOSE);
						}
					}
				});
		        CustomActionManager.getInstance().registerAction(new CustomAction(0.8f) {
					@Override
					public void perform() {
						jaws.play();
					}
				});
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, DeathAnimationState.CLOSE, animationManager.createFrames(2, "jaws_close"), false){
			@Override
			public void onAnimationFinished(){
				characterTarget.dieDismemberment();
				pooledEffect = explosionParticle.obtainAndStart(getWidth()/2, getHeight()/2+0.5f, 0);
				CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 20) {		
					@Override
					public void perform() {
						alpha-=0.05f;
					}
				});
			}
		});	
		
		animationManager.setCurrentAnimationState(DeathAnimationState.OPEN);
	}
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	alpha = 1;
    	roar.play();
		setRotation(20);
		
		rotateToAction.reset();
		rotateToAction.setInterpolation(Interpolation.exp10In);
		addAction(rotateToAction);
		
    	gameWorld.worldStage.addActor(this);
    	this.characterTarget = characterOwner;	
        alive = true;
        setVisible(true);
    }

    @Override
    public void act(float delta){
    	super.act(delta);
    	if(alive){
    		
    		currentFrame = (AtlasRegion)animationManager.animate(delta);
	    	setPosition(characterTarget.getX() - currentFrame.getRegionWidth()/PPM/2 + 0.4f, characterTarget.getY()+0.4f - currentFrame.getRegionHeight()/PPM/2);
			setSize(currentFrame.getRegionWidth()/PPM, currentFrame.getRegionHeight()/PPM);
			setOrigin(getWidth()/2, getHeight()/2);
			
			explosionParticle.setPosition(getWidth()/2, getHeight()/2+0.5f, 0);
			if(pooledEffect!=null){ 
	    		pooledEffect.setPosition(getWidth()/2, getHeight()/2+0.5f);
		    	if(pooledEffect.isComplete()){
		            CustomActionManager.getInstance().registerAction(new CustomAction(0.5f) {
		    			@Override
		    			public void perform() {
		    				reset();
		    			}
		    		});
		    	}
	    	}
    	}
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(1,1,1, alpha);
		batch.draw(currentFrame.getTexture(),  //Texture texture
				   getX() + ( (currentFrame.offsetX) / PPM), //float x
                getY() + ( (currentFrame.offsetY) / PPM), //float y
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
		batch.setColor(1,1,1, 1);
	}
	
	@Override
	public void reset() {
		setVisible(false);
		animationManager.setCurrentAnimationState(DeathAnimationState.OPEN);
		remove();
        alive = false;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
