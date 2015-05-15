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
        
        this.specialAbilities.add(CharacterAbilityType.SNARES);
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
				if(getSpeed() < 0.001f)
					animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.LANDINGIDLE, animationManager.createFrames(8, "archer_land"), false){
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
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.BEGINSLIDING, animationManager.createFrames(6, "archer_beginslide"), false){
			@Override
			public void onAnimationFinished(){
					animationManager.setCurrentAnimationState(CharacterAnimationState.SLIDING);
			}
		});
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.STANDINGUP, animationManager.createFrames(6, "archer_standup"), false){
			@Override
			public void onAnimationFinished(){
				if(getSpeed() < 0.001f)
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
		animationManager.createAnimation(new MyAnimation(0.01f, CharacterAnimationState.FLYARROW, animationManager.createFrames(10, "archer_flyarrow"), true){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			}
		});	
		animationManager.createAnimation(new MyAnimation(0.01f, CharacterAnimationState.RUNARROW, animationManager.createFrames(15, "archer_runarrow"), false){
			@Override
			public void onAnimationFinished(){
				if(speed > 0.001f)
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
	
	public void createBodyMembers(){
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(20/PPM);
		bodyMembers.add(new BodyMember(this, world, circleShape, "archerHead", 20/PPM, 20/PPM, 0 * MathUtils.degreesToRadians));
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(15/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerTorso", 20/PPM, -15/PPM, 0 * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 10/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerLeg", 25/PPM, -50/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerLeg", 10/PPM, -50/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerFoot", 35/PPM, -70/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerFoot", 5/PPM, -70/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerArm", 35/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerArm", 5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerHand", 45/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerHand", -5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		polygonShape.setAsBox(9/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerBow", 65/PPM, -10/PPM, 200f * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 15/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "archerArrows", 0/PPM, -10/PPM, 0 * MathUtils.degreesToRadians));
		
		for(BodyMember bodyMember : bodyMembers){
			gameWorld.worldStage.addActor(bodyMember);
		}
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
