package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Arrow;
import com.apptogo.runner.actors.Bomb;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.LiftField;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.userdata.UserData;
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
				useLift(character, abilityLevel);
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
			//niestety w tym momencie ustawienie tego levelu nic nie robi bo bomba jest poolowana i wiele spraw ustalana jest przy tworzeniu. Nie chce tego przerabiac dopoki nie bedzie ustalonego sposobu implementacji levelu zeby potem nei przepisywac
			bomb.init(character, abilityLevel);
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

		switch(abilityLevel){
		case 1:
			for(int i=0; i<5; i++){
				Arrow arrow = arrowsPool.obtain();
				arrow.init(character, i, abilityLevel);
				activeArrows.add(arrow);
			}
			break;
		case 2:
			for(int i=0; i<8; i++){
				for(int j=0; j<2; j++){
					Arrow arrow = arrowsPool.obtain();
					arrow.init(character, i, abilityLevel);
					activeArrows.add(arrow);
				}
			}
			break;
		case 3:
			for(int i=0; i<8; i++){
				for(int j=0; j<3; j++){
					Arrow arrow = arrowsPool.obtain();
					arrow.init(character, i, abilityLevel);
					activeArrows.add(arrow);
				}
			}
			break;
		}

        CustomActionManager.getInstance().registerAction(new CustomAction(abilityLevel*5*0.1f, 1, character) {		
			@Override
			public void perform() {
				((Character)args[0]).animationManager.getCurrentAnimation().setAnimationFinished(true);
			}
		});
	}
    
	public void useLift(Character character, int abilityLevel)
	{
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYLIFT);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNLIFT);
		}
		LiftField liftField = liftFieldsPool.obtain();
		liftField.init(character, abilityLevel);
		activeLiftFields.add(liftField);
	}
	
	public void act(){
		freePools();
	}
	
	/*---------POOLS---------*/
	private final Array<Bomb> activeBombs = new Array<Bomb>();
    private final Pool<Bomb> bombsPool = new Pool<Bomb>() {
	    @Override
	    protected Bomb newObject() {
	    	Bomb bomb = new Bomb(world, gameWorld);
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
    
	private final Array<LiftField> activeLiftFields = new Array<LiftField>();
    private final Pool<LiftField> liftFieldsPool = new Pool<LiftField>() {
	    @Override
	    protected LiftField newObject() {
	    	LiftField liftField = new LiftField(world, gameWorld);
	    	gameWorld.worldStage.addActor(liftField);
	    	return liftField;
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
        len = activeLiftFields.size;
        for (int i = len; --i >= 0;) {
            if (activeLiftFields.get(i).alive == false) {
            	liftFieldsPool.free(activeLiftFields.removeIndex(i));
            }
        }
	}
}
