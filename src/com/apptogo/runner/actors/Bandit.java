package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Bandit extends Character{

	private World world;
	private Vector2 bodySize;

    private final Array<Bomb> activeBombs = new Array<Bomb>();
    private final Pool<Bomb> bombsPool = new Pool<Bomb>() {
	    @Override
	    protected Bomb newObject() {
	    	Bomb bomb = new Bomb((Bandit)character, world);
	    	((Bandit)character).getStage().addActor(bomb);
	    	return bomb;
	    }
    };
	

	
	public Bandit(World world){
		super(world, "gfx/game/characters/bandit.pack");
		initAnimations();
		this.world = world;
		bodySize = new Vector2(25 / PPM, 65 / PPM);
		createBody(bodySize);
        setOrigin(0, 0);
	}
	
	private void initAnimations(){

		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.JUMPING, AnimationManager.createFrames(6, "jump"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDING, AnimationManager.createFrames(6, "land"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BEGINSLIDING, AnimationManager.createFrames(6, "beginslide"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.SLIDING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.STANDINGUP, AnimationManager.createFrames(6, "standup"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});		
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, AnimationManager.createFrames(18, "run"), true){
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(1/getSpeed() * 0.24f);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, AnimationManager.createFrames(22, "idle"), true, 10){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.MOONWALKING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.MOONWALKING, AnimationManager.createFrames(31, "moonwalk"), true, 5){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.FLYBOMB, AnimationManager.createFrames(10, "flybomb"), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNBOMB, AnimationManager.createFrames(10, "runbomb"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		
		animationManager.createAnimation(33, 0.03f, "fly", CharacterAnimationState.FLYING, true);
		animationManager.createAnimation(8, 0.03f, "slide", CharacterAnimationState.SLIDING, true);
		animationManager.createAnimation(9, 0.03f, "diebottom", CharacterAnimationState.DIEINGBOTTOM, false);
		animationManager.createAnimation(9, 0.03f, "dietop", CharacterAnimationState.DIEINGTOP, false);
	}
	

	public void throwBombs(){
		if(started && alive){
			if(!touchGround){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYBOMB);
			}
			else{
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNBOMB);
			}
			Bomb bomb = bombsPool.obtain();
			bomb.init();
	        activeBombs.add(bomb);
		}
	}
	
	private void freePools(){
		Bomb item;
        int len = activeBombs.size;
        for (int i = len; --i >= 0;) {
            item = activeBombs.get(i);
            if (item.alive == false) {
            	activeBombs.removeIndex(i);
                bombsPool.free(item);
            }
        }
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		freePools();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX() - (110 / PPM), getY() - (110 / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());	
	}
	
	@Override
	public Button getAbilityButton(CharacterAbilityType ability)
	{
		if( ability == CharacterAbilityType.BOMB ) return getBombAbilityButton();
		
		return new Button();
	}
	
	private Button getBombAbilityButton()
	{
Button bombButton = new Button(guiSkin, "banditBombAbilityButton");
		
		bombButton.setPosition(20/PPM, bombButton.getHeight()/PPM + 20/PPM + 40/PPM);
		bombButton.setSize(bombButton.getWidth()/PPM, bombButton.getHeight()/PPM);
		bombButton.setBounds(bombButton.getX(), bombButton.getY(), bombButton.getWidth(), bombButton.getHeight());
		
		bombButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				((Bandit)character).throwBombs();
		        return true;
		    }
		});
		
		return bombButton;
	}
}
