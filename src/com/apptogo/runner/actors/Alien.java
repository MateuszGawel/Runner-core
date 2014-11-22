package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class Alien extends Character{

	private World world;
	private GameWorld gameWorld;
	public CharacterAbilityType defaultAbility = CharacterAbilityType.LIFT;
	public LiftField liftField;
	
	public Alien(World world, GameWorld gameWorld){
		super(world, "gfx/game/characters/alien.pack", "alienJumpButton", "alienSlideButton", "alienSlowButton");
		this.gameWorld = gameWorld;
		initAnimations();
		this.world = world;
		createBody();
        setOrigin(0, 0);
        liftField = new LiftField(this, world);
        gameWorld.backgroundStage.addActor(liftField);
        addSounds();
	}
	
	private void addSounds(){
		ResourcesManager rm = ResourcesManager.getInstance();
    	BaseScreen cs = ScreensManager.getInstance().getCurrentScreen();
    	
    	sounds.put(CharacterSound.JUMP, (Sound)rm.getResource(cs, "mfx/game/characters/archerJump.ogg"));
    	sounds.put(CharacterSound.DEATH, (Sound)rm.getResource(cs, "mfx/game/characters/archerDeath.ogg"));
    	sounds.put(CharacterSound.EXPLODE, (Sound)rm.getResource(cs, "mfx/game/characters/archerExplode.ogg"));
    	sounds.put(CharacterSound.VICTORY, (Sound)rm.getResource(cs, "mfx/game/characters/archerVictory.ogg"));
	}
	
	private void initAnimations(){

		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.JUMPING, animationManager.createFrames(6, "jump"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDING, animationManager.createFrames(10, "land"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f){
					if(stepSoundPlayed){
						Logger.log(this, "stopuje");
						sounds.get(CharacterSound.STEPS).stop();
						stepSoundPlayed = false;
					}
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				}
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDINGIDLE, animationManager.createFrames(5, "land"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f){
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
					if(stepSoundPlayed){
						sounds.get(CharacterSound.STEPS).stop();
						stepSoundPlayed = false;
					}
				}
				else{
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
				}
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BEGINSLIDING, animationManager.createFrames(6, "beginslide"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.SLIDING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.STANDINGUP, animationManager.createFrames(6, "standup"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f){
					if(stepSoundPlayed){
						Logger.log(this, "stopuje");
						sounds.get(CharacterSound.STEPS).stop();
						stepSoundPlayed = false;
					}
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				}
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});		
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, animationManager.createFrames(18, "run"), true){
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(1/getSpeed() * 0.4f);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, animationManager.createFrames(21, "idle"), true, 10){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.BORED);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BORED, animationManager.createFrames(57, "bored"), true, 1){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.FLYLIFT, animationManager.createFrames(10, "flydzida"), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNLIFT, animationManager.createFrames(15, "rundzida"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
				else{
					if(stepSoundPlayed){
						Logger.log(this, "stopuje");
						sounds.get(CharacterSound.STEPS).stop();
						stepSoundPlayed = false;
					}
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				}
			}
		});	
		
		animationManager.createAnimation(30, 0.03f, "fly", CharacterAnimationState.FLYING, true);
		animationManager.createAnimation(8, 0.03f, "slide", CharacterAnimationState.SLIDING, true);
		animationManager.createAnimation(9, 0.03f, "diebottom", CharacterAnimationState.DIEINGBOTTOM, false);
		animationManager.createAnimation(9, 0.03f, "dietop", CharacterAnimationState.DIEINGTOP, false);
	}
	
	public void createBodyMembers(){
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(20/PPM);
		bodyMembers.add(new BodyMember(this, world, circleShape, "gfx/game/characters/alienHead.png", 20/PPM, 20/PPM, 0 * MathUtils.degreesToRadians));
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(15/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienTorso.png", 20/PPM, -15/PPM, 0 * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 10/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienLeg.png", 25/PPM, -50/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienLeg.png", 10/PPM, -50/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienFoot.png", 35/PPM, -70/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienFoot.png", 5/PPM, -70/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienArm.png", 35/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienArm.png", 5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienHand.png", 45/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienHand.png", -5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		polygonShape.setAsBox(9/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/alienStaff.png", 65/PPM, -10/PPM, 200f * MathUtils.degreesToRadians));
		
		for(BodyMember bodyMember : bodyMembers){
			gameWorld.worldStage.addActor(bodyMember);
		}
	}
	
	@Override
	public void useAbility(CharacterAbilityType abilityType)
	{
		if( abilityType == CharacterAbilityType.LIFT ) ((Alien)character).lift();
	}
	
	public void lift()
	{
		if(alive){
			if(!touchGround){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYLIFT);
			}
			else{
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNLIFT);
			}
			liftField.init();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX() - (110 / PPM), getY() - (110 / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());	
	}
		
	public Button getAbilityButton()
	{
		return null;
		/* do zaimplementowania potem przy umiejetnosciach klasowych
		Button bombButton = new Button(guiSkin, "alienLiftAbilityButton");
		
		bombButton.setPosition(20/PPM, 20/PPM);
		bombButton.setSize(bombButton.getWidth()/PPM, bombButton.getHeight()/PPM);
		bombButton.setBounds(bombButton.getX(), bombButton.getY(), bombButton.getWidth(), bombButton.getHeight());
		
		bombButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				character.useAbility(defaultAbility);
		        return true;
		    }
		});
		
		return bombButton;*/
	}
	
	@Override
	public CharacterType getCharacterType()
	{
		return CharacterType.ALIEN;
	}
}
