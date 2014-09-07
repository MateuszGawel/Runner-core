package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.NotificationManager;
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

public class Archer extends Character{

	private World world;
	private Vector2 bodySize;
	public CharacterAbilityType defaultAbility = CharacterAbilityType.ARROW;

    private final Array<Arrow> activeArrows = new Array<Arrow>();
    private final Pool<Arrow> arrowsPool = new Pool<Arrow>() {
	    @Override
	    protected Arrow newObject() {
	    	Arrow arrow = new Arrow((Archer)character, world);
	    	((Archer)character).getStage().addActor(arrow);
	    	return arrow;
	    }
    };
	
	public Archer(World world){
		super(world, "gfx/game/characters/archer.pack", "archerJumpButton", "archerSlideButton", "archerSlowButton");
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
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDING, AnimationManager.createFrames(10, "land"), false){
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
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, AnimationManager.createFrames(21, "idle"), true, 10){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.BORED);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BORED, AnimationManager.createFrames(30, "bored"), true, 5){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.FLYARROW, AnimationManager.createFrames(10, "flyarrow"), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNARROW, AnimationManager.createFrames(15, "runarrow"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		
		animationManager.createAnimation(30, 0.03f, "fly", CharacterAnimationState.FLYING, true);
		animationManager.createAnimation(8, 0.03f, "slide", CharacterAnimationState.SLIDING, true);
		animationManager.createAnimation(9, 0.03f, "diebottom", CharacterAnimationState.DIEINGBOTTOM, false);
		animationManager.createAnimation(9, 0.03f, "dietop", CharacterAnimationState.DIEINGTOP, false);
	}
	
	@Override
	public void useAbility(CharacterAbilityType abilityType)
	{
		if( abilityType == CharacterAbilityType.ARROW ) ((Archer)character).shootArrows();
	}
	
	public void shootArrows()
	{
		if(alive){
			if(!touchGround){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYARROW);
			}
			else{
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNARROW);
			}
			Arrow arrow = arrowsPool.obtain();
			arrow.init();
	        activeArrows.add(arrow);
		}
	}
	
	private void freePools(){
		Arrow item;
        int len = activeArrows.size;
        for (int i = len; --i >= 0;) {
            item = activeArrows.get(i);
            if (item.alive == false) {
            	activeArrows.removeIndex(i);
                arrowsPool.free(item);
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
		
	public Button getAbilityButton()
	{
		Button abilityButton = new Button(guiSkin, "archerArrowAbilityButton");
		
		abilityButton.setPosition(20/PPM, abilityButton.getHeight()/PPM + 20/PPM + 40/PPM);
		abilityButton.setSize(abilityButton.getWidth()/PPM, abilityButton.getHeight()/PPM);
		abilityButton.setBounds(abilityButton.getX(), abilityButton.getY(), abilityButton.getWidth(), abilityButton.getHeight());
		
		abilityButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				character.useAbility(defaultAbility);
		        return true;
		    }
		});
		
		return abilityButton;
	}
	
	@Override
	public CharacterType getCharacterType()
	{
		return CharacterType.ARCHER;
	}
}
