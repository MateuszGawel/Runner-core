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
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
        this.specialAbilities.add(CharacterAbilityType.PARACHUTE);
	}
	
	public void createBodyMembers()
	{
		//torso				
		PolygonShape torsoShape = new PolygonShape();
		torsoShape.setAsBox(8/PPM, 12/PPM);
		
		BodyMember torso = new BodyMember(this, world, torsoShape, "banditTorso", 0/PPM, 0/PPM, 0 * MathUtils.degreesToRadians);			
		torso.applyForce = 4;
		//head
		PolygonShape headShape = new PolygonShape();
		headShape.setAsBox(10/PPM, 10/PPM, new Vector2(0,0), (float)Math.toRadians(45));
		
		BodyMember head = new BodyMember(this, world, headShape, "banditHead", 4f/PPM, 23.5f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(-3/PPM, -10/PPM), new Vector2(1.5f/PPM, 11/PPM), -10, 30);				
		head.applyForce = 3;
		//legs
		PolygonShape legShape = new PolygonShape();
		legShape.setAsBox(3/PPM, 6/PPM);
		
		//left
		BodyMember leftLeg = new BodyMember(this, world, legShape, "banditLeg", -3.5f/PPM, -15/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(-3.5f/PPM, -10f/PPM), -60, 30);		
		
		//right
		BodyMember rightLeg = new BodyMember(this, world, legShape, "banditLeg", 3/PPM, -15/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(3f/PPM, -10f/PPM), -60, 30);			
		
		//foots
		PolygonShape footShape = new PolygonShape();
		footShape.setAsBox(3/PPM, 3/PPM, new Vector2(0,0), (float)Math.toRadians(45));
		
		//left
		BodyMember leftFoot = new BodyMember(this, world, footShape, "banditFoot", -3.5f/PPM, -27/PPM, 0 * MathUtils.degreesToRadians, leftLeg.getBody(), new Vector2(0/PPM, 7/PPM), new Vector2(0/PPM, -5/PPM), 0, 90);		
		
		//right
		BodyMember rightFoot = new BodyMember(this, world, footShape, "banditFoot", 3/PPM, -27/PPM, 0 * MathUtils.degreesToRadians, rightLeg.getBody(), new Vector2(0/PPM, 7/PPM), new Vector2(0/PPM, -5/PPM), 0, 90);		
		
		//arm
		PolygonShape armShape = new PolygonShape();
		armShape.setAsBox(2/PPM, 5/PPM);
		
		BodyMember leftArm = new BodyMember(this, world, armShape, "banditArm", -3/PPM, -3f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(-3/PPM, 2/PPM), -160, 30);
		
		BodyMember rightArm = new BodyMember(this, world, armShape, "banditArm", -3/PPM, -3f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(-3/PPM, 2/PPM), -160, 30);
		
		//hands
		PolygonShape handShape = new PolygonShape();
		handShape.setAsBox(2/PPM, 8/PPM);
		
		//left
		BodyMember leftHand = new BodyMember(this, world, handShape, "banditHand", -3/PPM, -11/PPM, 0 * MathUtils.degreesToRadians, leftArm.getBody(), new Vector2(0/PPM, 7/PPM), new Vector2(0/PPM, -4/PPM), -90, 0);		
				
		//right
		BodyMember rightHand = new BodyMember(this, world, handShape, "banditHand", -3/PPM, -11/PPM, 0 * MathUtils.degreesToRadians, rightArm.getBody(), new Vector2(0/PPM, 7/PPM), new Vector2(0/PPM, -4/PPM), -90, 0);		
		
		//stuff
		PolygonShape stuffShape = new PolygonShape();
		stuffShape.setAsBox(7/PPM, 10/PPM);
		
		BodyMember stuff = new BodyMember(this, world, stuffShape, "banditBag", -14/PPM, -2/PPM, 0 * MathUtils.degreesToRadians);	
		
		bodyMembers.add(stuff);
		bodyMembers.add(leftArm);
		bodyMembers.add(leftHand);	
		bodyMembers.add(leftLeg);
		bodyMembers.add(rightLeg);	
		bodyMembers.add(torso);
		bodyMembers.add(head);
		bodyMembers.add(leftFoot);
		bodyMembers.add(rightFoot);
		bodyMembers.add(rightArm);
		bodyMembers.add(rightHand);
				
		gameWorld.worldStage.addActor(stuff);
		gameWorld.worldStage.addActor(leftArm);
		gameWorld.worldStage.addActor(leftHand);
		gameWorld.worldStage.addActor(leftLeg);
		gameWorld.worldStage.addActor(rightLeg);
		gameWorld.worldStage.addActor(torso);
		gameWorld.worldStage.addActor(head);
		gameWorld.worldStage.addActor(leftFoot);
		gameWorld.worldStage.addActor(rightFoot);
		gameWorld.worldStage.addActor(rightArm);
		gameWorld.worldStage.addActor(rightHand);
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
				if(getSpeed() < 0.1f){
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
				if(getSpeed() < 0.1f){
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
				if(getSpeed() < 0.1f){
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
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.FLYABILITY, animationManager.createFrames(10, "bandit_flybomb"), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNABILITY, animationManager.createFrames(10, "bandit_runbomb"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.1f)
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
