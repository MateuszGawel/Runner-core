package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.AbilityManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class Archer extends Character{

	private World world;
	private GameWorld gameWorld;
	public CharacterAbilityType defaultAbility = CharacterAbilityType.ARROW;
	
	public Archer(World world, GameWorld gameWorld, int startingPosition, String playerName, HashMap<String, Integer> abilities){
		super(world, "gfx/game/characters/characters.pack", "archerJumpButton", "archerSlideButton", "archerSlowButton", playerName, abilities);
		this.gameWorld = gameWorld;
		initAnimations();
		this.world = world;
		createBody(startingPosition);
        
		createBodyMembers();
		
        addSounds();
        
        customOffsetX = 50.0f / PPM;
        customOffsetY = 20.0f / PPM;
        this.setOrigin(90/PPM, 70/PPM);
        
        this.specialAbilities.add(CharacterAbilityType.ARROW);
        this.specialAbilities.add(CharacterAbilityType.SNARES);
        this.specialAbilities.add(CharacterAbilityType.BOAR);
	}
	
	public void createBodyMembers()
	{
		//torso				
		PolygonShape torsoShape = new PolygonShape();
		torsoShape.setAsBox(8/PPM, 12/PPM);
		
		BodyMember torso = new BodyMember(this, world, torsoShape, "archerTorso", 0/PPM, 0/PPM, 0 * MathUtils.degreesToRadians);			
		
		//head
		PolygonShape headShape = new PolygonShape();
		headShape.setAsBox(11/PPM, 11/PPM, new Vector2(0,0), (float)Math.toRadians(45));
		
		BodyMember head = new BodyMember(this, world, headShape, "archerHead", 0f/PPM, 31f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(-1/PPM, -17.5f/PPM), new Vector2(-0.5f/PPM, 11/PPM), -10, 30);				
		head.go();
		//legs
		PolygonShape legShape = new PolygonShape();
		legShape.setAsBox(3/PPM, 6/PPM);
		
		//left
		BodyMember leftLeg = new BodyMember(this, world, legShape, "archerLeg", -3.5f/PPM, -15/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(-3.5f/PPM, -10f/PPM), -60, 30);		
		
		//right
		BodyMember rightLeg = new BodyMember(this, world, legShape, "archerLeg", 3/PPM, -15/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(3f/PPM, -10f/PPM), -60, 30);			
		
		//foots
		PolygonShape footShape = new PolygonShape();
		footShape.setAsBox(3/PPM, 3/PPM, new Vector2(0,0), (float)Math.toRadians(45));
		
		//left
		BodyMember leftFoot = new BodyMember(this, world, footShape, "archerFoot", -0.5f/PPM, -26/PPM, 0 * MathUtils.degreesToRadians, leftLeg.getBody(), new Vector2(-3/PPM, 6/PPM), new Vector2(0/PPM, -5/PPM), 0, 90);		
		
		//right
		BodyMember rightFoot = new BodyMember(this, world, footShape, "archerFoot", 6/PPM, -26/PPM, 0 * MathUtils.degreesToRadians, rightLeg.getBody(), new Vector2(-3/PPM, 6/PPM), new Vector2(0/PPM, -5/PPM), 0, 90);		
		
		//arm
		PolygonShape armShape = new PolygonShape();
		armShape.setAsBox(2/PPM, 5/PPM);
		
		BodyMember leftArm = new BodyMember(this, world, armShape, "archerArm", -3/PPM, -3f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(-3/PPM, 2/PPM), -160, 30);
		
		BodyMember rightArm = new BodyMember(this, world, armShape, "archerArm", -3/PPM, -3f/PPM, 0 * MathUtils.degreesToRadians, torso.getBody(), new Vector2(0/PPM, 5/PPM), new Vector2(-3/PPM, 2/PPM), -160, 30);
		
		//hands
		PolygonShape handShape = new PolygonShape();
		handShape.setAsBox(2/PPM, 6/PPM);
		
		//left
		BodyMember leftHand = new BodyMember(this, world, handShape, "archerHand", -3/PPM, -9.5f/PPM, 0 * MathUtils.degreesToRadians, leftArm.getBody(), new Vector2(0/PPM, 4.5f/PPM), new Vector2(0/PPM, -4/PPM), -90, 0);		
				
		//right
		BodyMember rightHand = new BodyMember(this, world, handShape, "archerHand", -3/PPM, -9.5f/PPM, 0 * MathUtils.degreesToRadians, rightArm.getBody(), new Vector2(0/PPM, 4.5f/PPM), new Vector2(0/PPM, -4/PPM), -90, 0);		
		
		//stuff
		PolygonShape stuffShape = new PolygonShape();
		stuffShape.setAsBox(3/PPM, 12/PPM);
		
		BodyMember stuff = new BodyMember(this, world, stuffShape, "archerArrows", -12/PPM, -2/PPM, -15 * MathUtils.degreesToRadians);		
		
		
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
	
	private void addSounds(){
		ResourcesManager rm = ResourcesManager.getInstance();
    	BaseScreen cs = ScreensManager.getInstance().getCurrentScreen();
    	
    	sounds.put(CharacterSound.DOUBLEJUMP, (Sound)rm.getResource(cs, "mfx/game/characters/archerJump.ogg"));
    	sounds.put(CharacterSound.DEATH, (Sound)rm.getResource(cs, "mfx/game/characters/archerDeath.ogg"));
    	sounds.put(CharacterSound.EXPLODE, (Sound)rm.getResource(cs, "mfx/game/characters/archerExplode.ogg"));
    	sounds.put(CharacterSound.VICTORY, (Sound)rm.getResource(cs, "mfx/game/characters/archerVictory.ogg"));
	}
	
	private void initAnimations(){

		AtlasRegion[] ar = animationManager.createFrames(6, "archer_jump");
		
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.JUMPING, ar, false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDING, animationManager.createFrames(10, "archer_land"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.1f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDINGIDLE, animationManager.createFrames(8, "archer_land"), false){
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
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BEGINSLIDING, animationManager.createFrames(6, "archer_beginslide"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.SLIDING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.STANDINGUP, animationManager.createFrames(6, "archer_standup"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.1f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});		
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, animationManager.createFrames(18, "archer_run"), true){
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(1/getSpeed() * 0.4f);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, animationManager.createFrames(21, "archer_idle"), true, 8 + randonGenerator.nextInt(5)){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.BORED);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BORED, animationManager.createFrames(30, "archer_bored"), true, 5){
			@Override
			public void onAnimationFinished(){
				this.resetLoops();
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.01f, CharacterAnimationState.FLYABILITY, animationManager.createFrames(10, "archer_flyarrow"), true){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.01f, CharacterAnimationState.RUNABILITY, animationManager.createFrames(15, "archer_runarrow"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.1f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
		});	
		
		animationManager.createAnimation(30, 0.03f, "archer_fly", CharacterAnimationState.FLYING, true);
		animationManager.createAnimation(8, 0.03f, "archer_slide", CharacterAnimationState.SLIDING, true);
		animationManager.createAnimation(9, 0.03f, "archer_diebottom", CharacterAnimationState.DIEINGBOTTOM, false);
		animationManager.createAnimation(9, 0.03f, "archer_dietop", CharacterAnimationState.DIEINGTOP, false);
	}

	
	@Override
	public void act(float delta) {
    	super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(currentFrame.getTexture(), getX() - (110 / PPM), getY() - (100 / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation(), currentFrame.getRegionX(), currentFrame.getRegionY(), currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), flipX, flipY);
	}
		
	public Button getAbilityButton()
	{
		return null;
		/* do zaimplementowania potem przy umiejetnosciach klasowych
		Button abilityButton = new Button(guiSkin, "archerArrowAbilityButton");
		
		abilityButton.setPosition(20/PPM, 20/PPM);
		abilityButton.setSize(abilityButton.getWidth()/PPM, abilityButton.getHeight()/PPM);
		abilityButton.setBounds(abilityButton.getX(), abilityButton.getY(), abilityButton.getWidth(), abilityButton.getHeight());
		
		abilityButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				character.useAbility(defaultAbility);
		        return true;
		    }
		});
		
		return abilityButton;*/
	}
	
	@Override
	public CharacterType getCharacterType()
	{
		return CharacterType.ARCHER;
	}
}
