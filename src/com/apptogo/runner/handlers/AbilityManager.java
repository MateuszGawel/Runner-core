package com.apptogo.runner.handlers;

import com.apptogo.runner.actors.Arrow;
import com.apptogo.runner.actors.Bomb;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class AbilityManager 
{
	private static AbilityManager INSTANCE;
	
	private World world;
	private GameWorld gameWorld;
	
	public static void create()
	{		
		INSTANCE = new AbilityManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static AbilityManager getInstance()
	{
		return INSTANCE;
	}
	public void init(World world, GameWorld gameWorld){
		this.world = world;
		this.gameWorld = gameWorld;
	}
	
	public void useAbility(Character character, CharacterAbilityType abilityType, int abilityLevel){
		switch(abilityType){
			case BOMB:
				throwBombs(character, abilityLevel);
				break;
			case ARROW:
				shootArrow(character, abilityLevel);
				break;
			case LIFT:
				break;
			default:
				break;
		}
	}
	
	private void throwBombs(Character character, int abilityLevel)
	{
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYBOMB);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNBOMB);
		}

		for(int i=0; i<abilityLevel; i++){
			Bomb bomb = bombsPool.obtain();

			bomb.init(character);
			activeBombs.add(bomb);
		}
	}
	
	private void shootArrow(Character character, int abilityLevel)
	{
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYARROW);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNARROW);
		}

		for(int i=0; i<abilityLevel*5; i++){
			Arrow arrow = arrowsPool.obtain();
			//arrow.setLevel(abilityLevel);
			arrow.init(character, i);
			activeArrows.add(arrow);
		}
	}
	
	public void act(){
		freePools();
	}
	
	/*---------POOLS---------*/
	private final Array<Bomb> activeBombs = new Array<Bomb>();
    private final Pool<Bomb> bombsPool = new Pool<Bomb>() {
	    @Override
	    protected Bomb newObject() {
	    	Bomb bomb = new Bomb(world, gameWorld, 1);
	    	gameWorld.worldStage.addActor(bomb);
	    	return bomb;
	    }
    };
    
    private final Array<Arrow> activeArrows = new Array<Arrow>();
    private final Pool<Arrow> arrowsPool = new Pool<Arrow>() {
	    @Override
	    protected Arrow newObject() {
	    	Arrow arrow = new Arrow(world, gameWorld);
	    	gameWorld.worldStage.addActor(arrow);
	    	return arrow;
	    }
    };
    
	private void freePools(){
        int len = activeBombs.size;
        for (int i = len; --i >= 0;) {
            if (activeBombs.get(i).alive == false) {
                bombsPool.free(activeBombs.removeIndex(i));
            }
        }
        len = activeArrows.size;
        for (int i = len; --i >= 0;) {
            if (activeArrows.get(i).alive == false) {
                arrowsPool.free(activeArrows.removeIndex(i));
            }
        }
	}
}
