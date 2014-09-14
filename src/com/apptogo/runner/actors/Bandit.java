package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.ArrayList;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Bandit extends Character{

	private World world;
	private Vector2 bodySize;
	private GameWorld gameWorld;
	public CharacterAbilityType defaultAbility = CharacterAbilityType.BOMB;

    private final Array<Bomb> activeBombs = new Array<Bomb>();
    private final Pool<Bomb> bombsPool = new Pool<Bomb>() {
	    @Override
	    protected Bomb newObject() {
	    	Bomb bomb = new Bomb((Bandit)character, world);
	    	((Bandit)character).getStage().addActor(bomb);
	    	return bomb;
	    }
    };
	
	public Bandit(World world, GameWorld gameWorld){
		super(world, "gfx/game/characters/bandit.pack", "banditJumpButton", "banditSlideButton", "banditSlowButton");
		this.gameWorld = gameWorld;
		initAnimations();
		this.world = world;
		bodySize = new Vector2(25 / PPM, 65 / PPM);
		createBody(bodySize);
		
		createBodyMembers();
		 
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
				animationManager.setCurrentAnimationState(CharacterAnimationState.BORED);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BORED, AnimationManager.createFrames(31, "moonwalk"), true, 5){
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
	
	@Override
	public void useAbility(CharacterAbilityType abilityType)
	{
		if( abilityType == CharacterAbilityType.BOMB ) ((Bandit)character).throwBombs();
	}
	
	public void throwBombs()
	{
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
	
	public void createBodyMembers(){
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(20/PPM);
		bodyMembers.add(new BodyMember(this, world, circleShape, "gfx/game/characters/banditHead.png", 20/PPM, 20/PPM, 0 * MathUtils.degreesToRadians));
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(15/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditTorso.png", 20/PPM, -15/PPM, 0 * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 10/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditLeg.png", 25/PPM, -50/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditLeg.png", 10/PPM, -50/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditFoot.png", 35/PPM, -70/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditFoot.png", 5/PPM, -70/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditArm.png", 35/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditArm.png", 5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditHand.png", 45/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditHand.png", -5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 15/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/banditBag.png", 0/PPM, -10/PPM, 0 * MathUtils.degreesToRadians));
		
		for(BodyMember bodyMember : bodyMembers){
			gameWorld.worldStage.addActor(bodyMember);
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
			
	public Button getAbilityButton()
	{
		Button bombButton = new Button(guiSkin, "banditBombAbilityButton");
		
		bombButton.setPosition(20/PPM, bombButton.getHeight()/PPM + 20/PPM + 40/PPM);
		bombButton.setSize(bombButton.getWidth()/PPM, bombButton.getHeight()/PPM);
		bombButton.setBounds(bombButton.getX(), bombButton.getY(), bombButton.getWidth(), bombButton.getHeight());
		
		bombButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				character.useAbility(defaultAbility);
		        return true;
		    }
		});
		
		return bombButton;
	}
	
	@Override
	public CharacterType getCharacterType()
	{
		return CharacterType.BANDIT;
	}
}
