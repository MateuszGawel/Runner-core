package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Arrow;
import com.apptogo.runner.actors.BlackHole;
import com.apptogo.runner.actors.Boar;
import com.apptogo.runner.actors.Bomb;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.ForceField;
import com.apptogo.runner.actors.Jaws;
import com.apptogo.runner.actors.LiftField;
import com.apptogo.runner.actors.Oil;
import com.apptogo.runner.actors.Parachute;
import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.actors.SmallBomb;
import com.apptogo.runner.actors.Snares;
import com.apptogo.runner.actors.Ufo;
import com.apptogo.runner.actors.ViewFinder;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
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
		blackHoleInParticleEffectActor = new ParticleEffectActor("blackHoleIn.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreenClass.GAME, "gfx/game/characters/charactersAtlas.pack"));
		gameWorld.getWorldStage().addActor(blackHoleInParticleEffectActor);
		
		blackHoleOutParticleEffectActor = new ParticleEffectActor("blackHoleOut.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreenClass.GAME, "gfx/game/characters/charactersAtlas.pack"));
		gameWorld.getWorldStage().addActor(blackHoleOutParticleEffectActor);
		
	}
	
	public void useAbility(Character character, CharacterAbilityType abilityType, int abilityLevel){
		Logger.log(this, "uzywam: " + abilityType);
		switch(abilityType){
			case BOMB:
				useBombs(character, abilityLevel);
				break;
			case ARROW:
				useArrow(character, abilityLevel);
				break;
			case LIFT:
				useLift(character, abilityLevel);
				break;
			case SNARES:
				useSnares(character, abilityLevel);
				break;
			case BLACKHOLE:
				useBlackHole(character, abilityLevel);
				break;
			case FORCEFIELD:
				useForceField(character, abilityLevel);
				break;
			case BOAR:
				useBoar(character, abilityLevel);
				break;
			case OIL:
				useOil(character, abilityLevel);
				break;
			case PARACHUTE:
				useParachute(character, abilityLevel);
				break;
			case DEATH:
				useDeath(character, abilityLevel);
				break;
			default:
				break;
		}
	}
	
	private void useBombs(Character character, int abilityLevel)
	{
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}

		for(int i=0; i<abilityLevel; i++){
			Bomb bomb = bombsPool.obtain();
			//niestety w tym momencie ustawienie tego levelu nic nie robi bo bomba jest poolowana i wiele spraw ustalana jest przy tworzeniu. Nie chce tego przerabiac dopoki nie bedzie ustalonego sposobu implementacji levelu zeby potem nei przepisywac
			bomb.init(character, abilityLevel);
			activeBombs.add(bomb);
		}
	}
	
	private void useArrow(Character character, int abilityLevel)
	{
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
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
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}
		LiftField liftField = liftFieldsPool.obtain();
		liftField.init(character, abilityLevel);
		activeLiftFields.add(liftField);
	}
	
	public void useSnares(Character character, int abilityLevel){
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}
		Snares snares = snaresPool.obtain();
		activeSnares.add(snares);
		snares.init(character, abilityLevel);
	}
	
	public void useBoar(Character character, int abilityLevel){
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}


		Boar boar = boarsPool.obtain();
		activeBoars.add(boar);
		boar.init(character, 1);

		if(abilityLevel == 2 || abilityLevel == 3){
			for(int i=0; i<3; i++){
				Boar boar2 = boarsPool.obtain();
				activeBoars.add(boar2);
				boar2.init(character, 2);
			}
		}
		if(abilityLevel == 3){
			Boar boar3 = boarsPool.obtain();
			activeBoars.add(boar3);
			boar3.init(character, 3);
		}
	}
	
	private void createOilJoint(Body bodyA, Body bodyB)
	{Logger.log(this, "JOINT MIEDZY " + bodyA.getUserData() + " A " + bodyB.getUserData());
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = bodyA;
		jointDef.bodyB = bodyB;
			
		jointDef.maxLength = 50/PPM;
		jointDef.collideConnected = true;
		//jointDef.length = 50/PPM;
		
		world.createJoint(jointDef);
	}
	
	public void useOil(Character character, int abilityLevel)
	{
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}

		Oil previousOil = null;
		
		switch(abilityLevel){
		case 1:
			for(int i=0; i<5; i++){
				Oil oil = oilsPool.obtain();
				activeOils.add(oil);
				oil.init(character, 1, (i-2)*2, i);
				
				if( previousOil != null ) createOilJoint(previousOil.oilBody, oil.oilBody);
				previousOil = oil;
			}
			break;
		case 2:
			for(int i=0; i<11; i++){
				Oil oil = oilsPool.obtain();
				activeOils.add(oil);
				oil.init(character, 2, (i-5), i);
				
				if( previousOil != null ) createOilJoint(previousOil.oilBody, oil.oilBody);
				previousOil = oil;
			}
			break;
		case 3:
			for(int i=0; i<17; i++){
				Oil oil = oilsPool.obtain();
				activeOils.add(oil);
				oil.init(character, 3, (i-8), i);
				
				if( previousOil != null ) createOilJoint(previousOil.oilBody, oil.oilBody);
				previousOil = oil;
			}
			break;
		}
	}
	
	public void useParachute(Character character, int abilityLevel){
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}
		new Parachute(world, gameWorld, character, smallBombsPool);
	}
	
	public void useBlackHole(Character character, int abilityLevel){
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}
		BlackHole blackHole = blackHolesPool.obtain();
		blackHole.init(character, abilityLevel);
		activeBlackHoles.add(blackHole);
	}
	
	public void useForceField(Character character, int abilityLevel){
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}
		ForceField forceField = forceFieldsPool.obtain();
		forceField.init(character, abilityLevel);
		activeForceFields.add(forceField);
	}
	
	public void useDeath(Character character, int abilityLevel){
		if(!character.flags.isOnGround()){
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
		}
		else{
			character.animationManager.setCurrentAnimationState(CharacterAnimationState.RUNABILITY);
		}
		
		Array<Character> orderedEnemies = new Array<Character>();
		for(Player player : gameWorld.enemies){
			orderedEnemies.add(player.character);
		}
		orderedEnemies.sort();
		
		switch(gameWorld.gameWorldType){
		case WILDWEST:
			try{
				switch(abilityLevel){
				case 1:
					ViewFinder viewFinder = viewFindersPool.obtain();
					viewFinder.init(orderedEnemies.get(0), abilityLevel);
					activeViewFinders.add(viewFinder);
					break;
				case 2:
					for(int i=0; i<2; i++){
						viewFinder = viewFindersPool.obtain();
						viewFinder.init(orderedEnemies.get(i), abilityLevel);
						activeViewFinders.add(viewFinder);
					}
					break;
				case 3:
					for(int i=0; i<3; i++){
						viewFinder = viewFindersPool.obtain();
						viewFinder.init(orderedEnemies.get(i), abilityLevel);
						activeViewFinders.add(viewFinder);
					}
					break;
				}
			}
			catch(IndexOutOfBoundsException e){
				Logger.log(this, "Not enough Players. Not all ViewFinders used");
			}
			break;
		case FOREST:
			try{
				switch(abilityLevel){
				case 1:
					Jaws jaws = jawsPool.obtain();
					jaws.init(orderedEnemies.get(0), abilityLevel);
					activeJaws.add(jaws);
					break;
				case 2:
					for(int i=0; i<2; i++){
						jaws = jawsPool.obtain();
						jaws.init(orderedEnemies.get(i), abilityLevel);
						activeJaws.add(jaws);
					}
					break;
				case 3:
					for(int i=0; i<3; i++){
						jaws = jawsPool.obtain();
						jaws.init(orderedEnemies.get(i), abilityLevel);
						activeJaws.add(jaws);
					}
					break;
				}
			}
			catch(IndexOutOfBoundsException e){
				Logger.log(this, "Not enough Players. Not all ViewFinders used");
			}
			break;
		case SPACE:
			try{
				switch(abilityLevel){
				case 1:
					Ufo ufo = ufosPool.obtain();
					ufo.init(orderedEnemies.get(0), abilityLevel);
					activeUfos.add(ufo);
					break;
				case 2:
					for(int i=0; i<2; i++){
						ufo = ufosPool.obtain();
						ufo.init(orderedEnemies.get(i), abilityLevel);
						activeUfos.add(ufo);
					}
					break;
				case 3:
					for(int i=0; i<3; i++){
						ufo = ufosPool.obtain();
						ufo.init(orderedEnemies.get(i), abilityLevel);
						activeUfos.add(ufo);
					}
					break;
				}
			}
			catch(IndexOutOfBoundsException e){
				Logger.log(this, "Not enough Players. Not all Ufos used");
			}
			
			break;
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
    
	private final Array<BlackHole> activeBlackHoles = new Array<BlackHole>();
    private final Pool<BlackHole> blackHolesPool = new Pool<BlackHole>() {
	    @Override
	    protected BlackHole newObject() {
	    	BlackHole blackHole = new BlackHole(world, gameWorld);
	    	gameWorld.worldStage.addActor(blackHole);
	    	return blackHole;
	    }
    };
    
	private final Array<ForceField> activeForceFields = new Array<ForceField>();
    private final Pool<ForceField> forceFieldsPool = new Pool<ForceField>() {
	    @Override
	    protected ForceField newObject() {
	    	ForceField forceFiel = new ForceField(world, gameWorld);
	    	gameWorld.worldStage.addActor(forceFiel);
	    	return forceFiel;
	    }
    };
    
	private final Array<Snares> activeSnares = new Array<Snares>();
    private final Pool<Snares> snaresPool = new Pool<Snares>() {
	    @Override
	    protected Snares newObject() {
	    	Snares snares = new Snares(world, gameWorld);
	    	gameWorld.worldStage.addActor(snares);
	    	return snares;
	    }
    };
    
	private final Array<Boar> activeBoars = new Array<Boar>();
    private final Pool<Boar> boarsPool = new Pool<Boar>() {
	    @Override
	    protected Boar newObject() {
	    	Boar boar = new Boar(world, gameWorld);
	    	gameWorld.worldStage.addActor(boar);
	    	return boar;
	    }
    };
    
	private final Array<Oil> activeOils = new Array<Oil>();
    private final Pool<Oil> oilsPool = new Pool<Oil>() {
	    @Override
	    protected Oil newObject() {
	    	Oil oil = new Oil(world, gameWorld);
	    	gameWorld.worldStage.addActor(oil);
	    	return oil;
	    }
    };
    
	private final Array<Ufo> activeUfos = new Array<Ufo>();
    private final Pool<Ufo> ufosPool = new Pool<Ufo>() {
	    @Override
	    protected Ufo newObject() {
	    	Ufo ufo = new Ufo(world, gameWorld);
	    	gameWorld.worldStage.addActor(ufo);
	    	return ufo;
	    }
    };
    
	private final Array<ViewFinder> activeViewFinders = new Array<ViewFinder>();
    private final Pool<ViewFinder> viewFindersPool = new Pool<ViewFinder>() {
	    @Override
	    protected ViewFinder newObject() {
	    	ViewFinder viewFinder = new ViewFinder(world, gameWorld);
	    	return viewFinder;
	    }
    };
    
	private final Array<Jaws> activeJaws = new Array<Jaws>();
    private final Pool<Jaws> jawsPool = new Pool<Jaws>() {
	    @Override
	    protected Jaws newObject() {
	    	Jaws jaws = new Jaws(world, gameWorld);
	    	return jaws;
	    }
    };
    
	public final Array<SmallBomb> activeSmallBombs = new Array<SmallBomb>();
    public final Pool<SmallBomb> smallBombsPool = new Pool<SmallBomb>() {
	    @Override
	    protected SmallBomb newObject() {
	    	SmallBomb smallBomb = new SmallBomb(world, gameWorld);
	    	activeSmallBombs.add(smallBomb);
	    	return smallBomb;
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
        len = activeBlackHoles.size;
        for (int i = len; --i >= 0;) {
            if (activeBlackHoles.get(i).alive == false) {
            	blackHolesPool.free(activeBlackHoles.removeIndex(i));
            }
        }
        len = activeForceFields.size;
        for (int i = len; --i >= 0;) {
            if (activeForceFields.get(i).alive == false) {
            	forceFieldsPool.free(activeForceFields.removeIndex(i));
            }
        }
        len = activeSnares.size;
        for (int i = len; --i >= 0;) {
            if (activeSnares.get(i).alive == false) {
            	snaresPool.free(activeSnares.removeIndex(i));
            }
        }
        len = activeBoars.size;
        for (int i = len; --i >= 0;) {
            if (activeBoars.get(i).alive == false) {
            	boarsPool.free(activeBoars.removeIndex(i));
            }
        }
        len = activeOils.size;
        for (int i = len; --i >= 0;) {
            if (activeOils.get(i).alive == false) {
            	oilsPool.free(activeOils.removeIndex(i));
            }
        }
        len = activeUfos.size;
        for (int i = len; --i >= 0;) {
            if (activeUfos.get(i).alive == false) {
            	ufosPool.free(activeUfos.removeIndex(i));
            }
        }
        len = activeViewFinders.size;
        for (int i = len; --i >= 0;) {
            if (activeViewFinders.get(i).alive == false) {
            	viewFindersPool.free(activeViewFinders.removeIndex(i));
            }
        }
        len = activeSmallBombs.size;
        for (int i = len; --i >= 0;) {
            if (activeSmallBombs.get(i).alive == false) {
            	smallBombsPool.free(activeSmallBombs.removeIndex(i));
            	Logger.log(this, "usuwam, w poolu: " + smallBombsPool.getFree() + " high " + smallBombsPool.peak);
            }
        }
	}
	
	//----PARTICLES----//
	public ParticleEffectActor blackHoleInParticleEffectActor;
	public ParticleEffectActor blackHoleOutParticleEffectActor;
}
