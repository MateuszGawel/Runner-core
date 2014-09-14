package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.ArrayList;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.MyAnimation;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Archer extends Character{

	private World world;
	private GameWorld gameWorld;
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
	
	public Archer(World world, GameWorld gameWorld){
		super(world, "gfx/game/characters/archer.pack", "archerJumpButton", "archerSlideButton", "archerSlowButton");
		this.gameWorld = gameWorld;
		initAnimations();
		this.world = world;
		bodySize = new Vector2(25 / PPM, 65 / PPM);
		createBody(bodySize);
        setOrigin(0, 0);
        
        createBodyMembers();
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
	
	public void createBodyMembers(){
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(20/PPM);
		bodyMembers.add(new BodyMember(this, world, circleShape, "gfx/game/characters/archerHead.png", 20/PPM, 20/PPM, 0 * MathUtils.degreesToRadians));
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(15/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerTorso.png", 20/PPM, -15/PPM, 0 * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 10/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerLeg.png", 25/PPM, -50/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerLeg.png", 10/PPM, -50/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerFoot.png", 35/PPM, -70/PPM, 30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerFoot.png", 5/PPM, -70/PPM, -30f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerArm.png", 35/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerArm.png", 5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerHand.png", 45/PPM, -10/PPM, 80f * MathUtils.degreesToRadians));
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerHand.png", -5/PPM, -10/PPM, -80f * MathUtils.degreesToRadians));
		polygonShape.setAsBox(9/PPM, 25/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerBow.png", 65/PPM, -10/PPM, 200f * MathUtils.degreesToRadians));
		polygonShape.setAsBox(5/PPM, 15/PPM);
		bodyMembers.add(new BodyMember(this, world, polygonShape, "gfx/game/characters/archerArrows.png", 0/PPM, -10/PPM, 0 * MathUtils.degreesToRadians));
		
		for(BodyMember bodyMember : bodyMembers){
			gameWorld.worldStage.addActor(bodyMember);
		}
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
