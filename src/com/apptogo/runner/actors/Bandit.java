package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Bandit extends Character{

	private World world;
	private Vector2 bodySize;
	private Bandit player = this;

    private final Array<Bomb> activeBombs = new Array<Bomb>();
    private final Pool<Bomb> bombsPool = new Pool<Bomb>() {
	    @Override
	    protected Bomb newObject() {
	    	Bomb bomb = new Bomb(player, world);
	    	player.getStage().addActor(bomb);
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
		animationManager.createAnimation(18, 0.03f, "run", CharacterAnimationState.RUNNING, true);
		animationManager.createAnimation(22, 0.06f, "idle", CharacterAnimationState.IDLE, true);
		animationManager.createAnimation(6, 0.03f, "jump", CharacterAnimationState.JUMPING, false);
		animationManager.createAnimation(6, 0.03f, "land", CharacterAnimationState.LANDING, false);
		animationManager.createAnimation(33, 0.03f, "fly", CharacterAnimationState.FLYING, true);
		animationManager.createAnimation(8, 0.03f, "slide", CharacterAnimationState.SLIDING, true);
		animationManager.createAnimation(6, 0.03f, "beginslide", CharacterAnimationState.BEGINSLIDING, false);
		animationManager.createAnimation(6, 0.03f, "standup", CharacterAnimationState.STANDINGUP, false);
		animationManager.createAnimation(9, 0.03f, "diebottom", CharacterAnimationState.DIEINGBOTTOM, false);
		animationManager.createAnimation(9, 0.03f, "dietop", CharacterAnimationState.DIEINGTOP, false);
		animationManager.createAnimation(31, 0.03f, "moonwalk", CharacterAnimationState.MOONWALKING, true);
		animationManager.createAnimation(10, 0.03f, "flybomb", CharacterAnimationState.FLYBOMB, false);
		animationManager.createAnimation(10, 0.03f, "runbomb", CharacterAnimationState.RUNBOMB, false);
	}
	

	public void throwBombs(){
		if(alive){
			if(inAir){
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
}
