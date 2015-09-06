package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ViewFinder extends Group implements Poolable{

	public boolean alive;
	private GameWorld gameWorld;
	//parameters
	private int level = 1;

	private TextureAtlas atlas;
	private Character characterTarget;
	private ViewFinderImage vfImage;
	private Vector2 currentPosition = new Vector2(0,0);
	public ParticleEffectActor explosionParticleOne;
	private PooledEffect pooledEffect;
	private Sound shot, shotClicks;
	
	public ViewFinder(World world, GameWorld gameWorld){
		this.gameWorld = gameWorld;
		vfImage = new ViewFinderImage();
		setSize(1,1);
        this.addActor(vfImage);
        setOrigin(getWidth()/2, getHeight()/2);
        explosionParticleOne = new ParticleEffectActor("explosion_lvl1.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
        this.addActor(explosionParticleOne);
        shot = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/shot.ogg");
        shotClicks = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/shotClicks.ogg");
	}
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	gameWorld.worldStage.addActor(this);
    	this.characterTarget = characterOwner;	
        alive = true;
        setVisible(true);
        vfImage.init();
        CustomActionManager.getInstance().registerAction(new CustomAction(2.5f) {
			@Override
			public void perform() {
				pooledEffect = explosionParticleOne.obtainAndStart(getWidth()/2, getHeight()/2+0.3f, 0);
				characterTarget.dieDismemberment();
				shot.play();
			}
		});
        CustomActionManager.getInstance().registerAction(new CustomAction(1.5f) {
			@Override
			public void perform() {
				shotClicks.play();
			}
		});
    }

    @Override
    public void act(float delta){
    	super.act(delta);
    	currentPosition.set(getX(), getY());
    	if(alive){
	    	setPosition(characterTarget.getX(), characterTarget.getY());
	    	
	    	explosionParticleOne.setPosition(getWidth()/2, getHeight()/2+0.3f, 0);
	    	if(pooledEffect!=null){ 
	    		pooledEffect.setPosition(getWidth()/2, getHeight()/2+0.3f);
		    	if(pooledEffect.isComplete() && !vfImage.reseting){
		    		vfImage.addAction(vfImage.scaleToAction);
		    		vfImage.reseting = true;
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
	public void reset() {
		setVisible(false);
		remove();
        alive = false;
        vfImage.reset();
        pooledEffect = null;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
	private class ViewFinderImage extends Actor{
    	private AtlasRegion currentFrame;
    	MoveToAction moveAction;
    	RotateToAction rotateAction;
    	ScaleToAction scaleToAction;
    	boolean reseting;
    	
    	public ViewFinderImage() {
    		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
    		currentFrame = atlas.findRegion("viewfinder");
    		setSize(currentFrame.getRegionWidth()/PPM, currentFrame.getRegionHeight()/PPM);
    		
            moveAction = Actions.action(MoveToAction.class);
            moveAction.setPosition(-0.35f, 0);
            moveAction.setDuration(1f);
            
            rotateAction = Actions.action(RotateToAction.class);
            rotateAction.setRotation(90);
            rotateAction.setDuration(1f);
            
            scaleToAction = Actions.action(ScaleToAction.class);
            scaleToAction.setScale(0);
            scaleToAction.setDuration(0.2f);
            
            setOrigin(getWidth()/2, getHeight()/2);
		}
    	
    	public void init(){ 
    		moveAction.reset();
    		rotateAction.reset();
    		scaleToAction.reset();
    		moveAction.setInterpolation(Interpolation.swingOut);
    		rotateAction.setInterpolation(Interpolation.exp10);
    		scaleToAction.setInterpolation(Interpolation.exp10);
    		
    		
    		
    		setVisible(true);
    		setPosition(-5, -2);
    		SequenceAction sequenceAction = new SequenceAction(moveAction, rotateAction);
            this.addAction(sequenceAction);
    	}
    	
    	@Override
    	public void draw(Batch batch, float parentAlpha) {
    		super.draw(batch, parentAlpha);	
    		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    	}
    
    	public void reset() {
    		reseting = false;
    		setScale(1);
    		setRotation(0);
    	}
    }
	
}
