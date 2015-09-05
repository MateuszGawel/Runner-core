package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class Bandit extends Character{

	private World world;

	public GameWorld gameWorld;
	public CharacterAbilityType defaultAbility = CharacterAbilityType.BOMB;
	
    private void addSounds(){
    	ResourcesManager rm = ResourcesManager.getInstance();
    	BaseScreen cs = ScreensManager.getInstance().getCurrentScreen();
    	
    	sounds.put(CharacterSound.DOUBLEJUMP, (Sound)rm.getResource(cs, "mfx/game/characters/banditJump.ogg"));
    	sounds.put(CharacterSound.DEATH, (Sound)rm.getResource(cs, "mfx/game/characters/banditDeath.ogg"));
    	sounds.put(CharacterSound.EXPLODE, (Sound)rm.getResource(cs, "mfx/game/characters/banditExplode.ogg"));
    	sounds.put(CharacterSound.VICTORY, (Sound)rm.getResource(cs, "mfx/game/characters/banditVictory.ogg"));
    }
    
	public Bandit(World world, GameWorld gameWorld, int startingPosition, String playerName, HashMap<String, Integer> abilities){
		super(world, "gfx/game/characters/characters.pack", "banditJumpButton", "banditSlideButton", "banditSlowButton", playerName, abilities);
		this.gameWorld = gameWorld;
		initAnimations();
		this.world = world;
		createBody(startingPosition);
		
		createBodyMembers();
		 
        addSounds();
        
        customOffsetX = 45.0f / PPM;
        customOffsetY = 30.0f / PPM;
        this.setOrigin(90/PPM, 80/PPM);
        
        this.specialAbilities.add(CharacterAbilityType.BOMB);
        this.specialAbilities.add(CharacterAbilityType.OIL);
        this.specialAbilities.add(CharacterAbilityType.LASSO);
	}
	
	private void initAnimations(){

		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.JUMPING, animationManager.createFrames(6, "bandit_jump"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDING, animationManager.createFrames(10, "bandit_land"), false){
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
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDINGIDLE, animationManager.createFrames(5, "bandit_land"), false){
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
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BEGINSLIDING, animationManager.createFrames(6, "bandit_beginslide"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.SLIDING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.STANDINGUP, animationManager.createFrames(6, "bandit_standup"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f){
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
					if(stepSoundPlayed){
						sounds.get(CharacterSound.STEPS).stop();
						stepSoundPlayed = false;
					}
				}
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});		
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, animationManager.createFrames(18, "bandit_run"), true){
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(1/getSpeed() * 0.4f);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, animationManager.createFrames(22, "bandit_idle"), true, 8 + randonGenerator.nextInt(5)){
			@Override
			public void onAnimationFinished(){
				//animationManager.setCurrentAnimationState(CharacterAnimationState.IDLETOMOONWALK);
				animationManager.setCurrentAnimationState(CharacterAnimationState.BORED);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BORED, animationManager.createFrames(31, "bandit_moonwalk"), true, 5){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				//animationManager.setCurrentAnimationState(CharacterAnimationState.MOONWALKTOIDLE);
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		/*animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.MOONWALKTOIDLE, animationManager.createFrames(6, "bandit_moonwalkToIdle"), false){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.IDLETOMOONWALK, animationManager.createFrames(6, "bandit_idleToMoonwalk"), false){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				animationManager.setCurrentAnimationState(CharacterAnimationState.BORED);
			}
		});	*/
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.FLYBOMB, animationManager.createFrames(10, "bandit_flybomb"), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNBOMB, animationManager.createFrames(10, "bandit_runbomb"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
				else{
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
					if(stepSoundPlayed){
						sounds.get(CharacterSound.STEPS).stop();
						stepSoundPlayed = false;
					}
				}
			}
		});	
		
		animationManager.createAnimation(33, 0.03f, "bandit_fly", CharacterAnimationState.FLYING, true);
		animationManager.createAnimation(8, 0.03f, "bandit_slide", CharacterAnimationState.SLIDING, true);
		animationManager.createAnimation(9, 0.03f, "bandit_diebottom", CharacterAnimationState.DIEINGBOTTOM, false);
		animationManager.createAnimation(9, 0.03f, "bandit_dietop", CharacterAnimationState.DIEINGTOP, false);
	}
	
	public void createBodyMembers()
	{
		//torso		
		CircleShape torsoShape = new CircleShape();
		torsoShape.setRadius(8.5f/PPM);
		
		BodyMember torso = new BodyMember(this, world, torsoShape, "banditTorso", 28.5f/PPM, -6.5f/PPM, 0 * MathUtils.degreesToRadians);		
		
		bodyMembers.add(torso);
		
		
		//head
		CircleShape headShape = new CircleShape();
		headShape.setRadius(10/PPM);
		
		BodyMember head = new BodyMember(this, world, headShape, "banditHead", 33/PPM, 15/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(-4/PPM, -12/PPM), new Vector2(0/PPM, 10/PPM), true);		
		
		bodyMembers.add(head);
		
		
		
		//legs
		CircleShape legShape = new CircleShape();
		legShape.setRadius(3/PPM);
		
		//left
		
		BodyMember leftLeg = new BodyMember(this, world, legShape, "banditLeg", 25/PPM, -23/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(1/PPM, 5/PPM), new Vector2(-2.5f/PPM, -11.5f/PPM));		
		
		bodyMembers.add(leftLeg);
		
		
		//right
		
		BodyMember rightLeg = new BodyMember(this, world, legShape, "banditLeg", 32/PPM, -23/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(-2/PPM, 5/PPM), new Vector2(1.5f/PPM, -11.5f/PPM));		
		
		bodyMembers.add(rightLeg);
		
		
		//foots
		CircleShape footShape = new CircleShape();
		footShape.setRadius(4/PPM);
		
		//left
		
		BodyMember leftFoot = new BodyMember(this, world, legShape, "banditFoot", 24/PPM, -34/PPM, 0 * MathUtils.degreesToRadians, leftLeg.getBody(), new Vector2(0/PPM, 6/PPM), new Vector2(-1/PPM, -5/PPM));		
		
		bodyMembers.add(leftFoot);
		
		//right
		
		BodyMember rightFoot = new BodyMember(this, world, legShape, "banditFoot", 33/PPM, -34/PPM, 0 * MathUtils.degreesToRadians, rightLeg.getBody(), new Vector2(-1/PPM, 6/PPM), new Vector2(0/PPM, -5/PPM));		
		
		bodyMembers.add(rightFoot);
		
		
		//arms
		CircleShape armShape = new CircleShape();
		armShape.setRadius(3/PPM);
		
		//left
		
		BodyMember leftArm = new BodyMember(this, world, armShape, "banditArm", 26.5f/PPM, -28.5f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(2/PPM, 2/PPM), new Vector2(0/PPM, 0/PPM));		
		
		bodyMembers.add(leftArm);
		
		
		//right
		
		BodyMember rightArm = new BodyMember(this, world, armShape, "banditArm", 26.5f/PPM, -28.5f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(2/PPM, 2/PPM), new Vector2(0/PPM, 0/PPM));		
		
		bodyMembers.add(rightArm);
		
		
		//hands
		CircleShape handShape = new CircleShape();
		handShape.setRadius(3/PPM);
		
		//left
		
		BodyMember leftHand = new BodyMember(this, world, armShape, "banditHand", 26.5f/PPM, -35.5f/PPM, 0 * MathUtils.degreesToRadians, leftArm.getBody(), new Vector2(0/PPM, 3.5f/PPM), new Vector2(0/PPM, -3.5f/PPM));		
		
		bodyMembers.add(leftHand);
		
		
		//right
		
		BodyMember rightHand = new BodyMember(this, world, armShape, "banditHand", 26.5f/PPM, -35.5f/PPM, 0 * MathUtils.degreesToRadians, rightArm.getBody(), new Vector2(0/PPM, 3.5f/PPM), new Vector2(0/PPM, -3.5f/PPM));		
		
		bodyMembers.add(rightHand);
		
		
		gameWorld.worldStage.addActor(rightLeg);
		gameWorld.worldStage.addActor(rightFoot);
		
		gameWorld.worldStage.addActor(leftLeg);
		gameWorld.worldStage.addActor(leftFoot);
		
		gameWorld.worldStage.addActor(leftArm);
		gameWorld.worldStage.addActor(leftHand);
		
		gameWorld.worldStage.addActor(torso);
		
		gameWorld.worldStage.addActor(rightArm);
		gameWorld.worldStage.addActor(rightHand);
		
		gameWorld.worldStage.addActor(head);
		
		
		
		
		/*
		
		
		bodyMembers.add(new BodyMember(this, world, circleShape, "banditHead", 20/PPM, 20/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0,0), new Vector2(0, 1)));
		
		polygonShape.setAsBox(5/PPM, 10/PPM);
		
		float top = 5/PPM;
		float center = 0;
		float bottom = -5/PPM;
		
		BodyMember rightLeg = new BodyMember(this, world, polygonShape, "banditLeg", 25/PPM, -50/PPM, 30f * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0, 1), new Vector2(1, -1));
		BodyMember leftLeg = new BodyMember(this, world, polygonShape, "banditLeg", 10/PPM, -50/PPM, -30f * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0, 1), new Vector2(-1, -1));
		BodyMember rightFoot = new BodyMember(this, world, polygonShape, "banditFoot", 35/PPM, -70/PPM, 30f * MathUtils.degreesToRadians, rightLeg.getBody(), new Vector2(0, 1), new Vector2(0, -1));
		BodyMember leftFoot  = new BodyMember(this, world, polygonShape, "banditFoot", 5/PPM, -70/PPM, -30f * MathUtils.degreesToRadians, leftLeg.getBody(), new Vector2(0, 1), new Vector2(0, -1));
		BodyMember rightArm = new BodyMember(this, world, polygonShape, "banditArm", 35/PPM, -10/PPM, 80f * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0, 1), new Vector2(1, 0));
		BodyMember leftArm  = new BodyMember(this, world, polygonShape, "banditArm", 5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0, 1), new Vector2(-1, 0));
		BodyMember rightHand = new BodyMember(this, world, polygonShape, "banditHand", 45/PPM, -10/PPM, 80f * MathUtils.degreesToRadians, rightArm.getBody(), new Vector2(0, 1), new Vector2(0, -1));
		BodyMember leftHand  = new BodyMember(this, world, polygonShape, "banditHand", -5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians, leftArm.getBody(), new Vector2(0, 1), new Vector2(0, -1));
		
		bodyMembers.add(rightLeg);
		bodyMembers.add(leftLeg);
		bodyMembers.add(rightFoot);
		bodyMembers.add(leftFoot);
		bodyMembers.add(rightArm);
		bodyMembers.add(leftArm);
		bodyMembers.add(rightHand);
		bodyMembers.add(leftHand);
		polygonShape.setAsBox(5/PPM, 15/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "banditBag", 0/PPM, -10/PPM, 0 * MathUtils.degreesToRadians));
		 */
	}
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		//Logger.log(this, "OFFSET : " + ((AtlasRegion)currentFrame).offsetY );
		//Logger.log(this, "NAME : " + ((AtlasRegion)currentFrame).name );
		//batch.draw(currentFrame.getTexture(), getX() - ( (110 - ((AtlasRegion)currentFrame).offsetX) / PPM), getY() - ( (110 - ((AtlasRegion)currentFrame).offsetY) / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation(), currentFrame.getRegionX(), currentFrame.getRegionY(), currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), flipX, flipY);
//		batch.draw(currentFrame.getTexture(), getX() + ( (((AtlasRegion)currentFrame).offsetX) * 1.1363f / PPM), 
//				                              getY() + ( (((AtlasRegion)currentFrame).offsetY) * 1.1363f / PPM), 
//				                              getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation(), currentFrame.getRegionX(), currentFrame.getRegionY(), currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), flipX, flipY);
	}
	
	private Button getAbilityButton()
	{
		return null;
		/* do zaimplementowania potem przy umiejetnosciach klasowych
		Button bombButton = new Button(guiSkin, "banditBombAbilityButton");
		
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
		return CharacterType.BANDIT;
	}
}
