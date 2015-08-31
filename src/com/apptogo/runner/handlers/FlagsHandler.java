package com.apptogo.runner.handlers;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.badlogic.gdx.physics.box2d.Body;

public class FlagsHandler {
	
	private boolean isMe;
	
	//ZAKOLEJKOWANE AKCJE
	private boolean queuedJump;
	private int queuedLift;
	private int queuedSnare;
	private boolean queuedDeathDismemberment;
	private boolean queuedDeathTop;
	private boolean queuedDeathBottom;
	private boolean queuedCatapultJump;
	private boolean queuedMushroomJump;
	private Body queuedTeleportToBody;
	
	//ZMIENNE
	private Character character;
	private AnimationManager animManager;
	
	//KO�COWE
	private boolean canBegin;
	private boolean onGround;
	private boolean canJump;
	private boolean canDoubleJump;
	private boolean canLand;
	private float queuedBoost;
	private boolean canBoost;
	private boolean canSlide;
	private boolean slideButtonPressed;
	private boolean canStandUp;
	private boolean forceStandUp;
	private boolean canDie;	
	private boolean alive = true;
	private boolean powerupSet;
	private boolean finished;
	private boolean shouldStop;
	private boolean shouldStart;
	private boolean shouldChangeToRunningState;
	private boolean dieBottom;
	private boolean dieTop;
	private boolean canRun;
	private boolean canBeSnared;
	private boolean canBeBlackHoleTeleported;
	private boolean dieDismemberment;
	private boolean shouldFly;
	private boolean stopFlyingAction;
	private boolean canUseAbility;
	private boolean jumped;
	private boolean jumpedQueued; //flaga ustawiana zanim przekr�ci si� update bo zdarzenia sa wykonywane poza update.
	private boolean gravityInversed;
	private boolean gravityRotationSwitch;
	private boolean lifted;
	private boolean snared;
	private boolean teleport;
	private boolean diedInAir;
 	
	//UMIEJETNOSCI
	private boolean canBeLifted;
	

	//TEMP
	private boolean tempRunFlag;
	public boolean isTempRunFlag() {
		return tempRunFlag;
	}

	public void setTempRunFlag(boolean tempRunFlag) {
		this.tempRunFlag = tempRunFlag;
	}
	
	//SK�ADOWE
	private boolean began;
	private boolean boostedOnce;
	private boolean sliding;
	private boolean doubleJumped;
	private boolean minimumSlidingTimePassed;
	private boolean swampSlowedOnce;
	private boolean immortal;
	private boolean stopped; //do zastopowania animacji running
	private boolean flying;
	
	//SENSORY
	private int wallSensor;
	private int footSensor;
	private int standupSensor;
	private int jumpSensor;
	private int barrelSensor;
	private int headSensor;
	private int swampSensor;
	private int catapultSensor;
	private int mushroomSensor;
//	private int leftRotationSensor;
//	private int rightRotationSensor;	

	
	public void setCharacter(Character character){
		this.character = character;
		this.animManager = character.animationManager;
	}
	
	public void update(){
		
			if(alive && !finished && !began)
				canBegin = true;
			else
				canBegin = false;
			
			if((footSensor>0 || barrelSensor>0) && alive && !onGround && began && !jumpedQueued && animManager.getCurrentAnimationState()!=CharacterAnimationState.LANDING && animManager.getCurrentAnimationState()!=CharacterAnimationState.LANDINGIDLE)
				canLand = true;
			else
				canLand = false;
				
			if(footSensor>0 || barrelSensor>0)
				onGround = true;
			else
				onGround = false;
			
			if(alive && began && !finished && /*character.speed <= character.playerSpeedLimit-character.playerSlowAmmount && */ tempRunFlag && !lifted && !snared)
				canRun = true;
			else
				canRun = false;
			
			if(began && alive && character.speed>0.1f && !stopped && (animManager.getCurrentAnimationState() == CharacterAnimationState.IDLE || animManager.getCurrentAnimationState() == CharacterAnimationState.BORED) )
				shouldChangeToRunningState = true;
			else
				shouldChangeToRunningState = false;
			
			if((jumpSensor > 0 || wallSensor > 0) && alive && began && !finished && headSensor<=0 && swampSensor<=0 && !snared)
				canJump = true;
			else
				canJump = false;
			
			if(!canJump && !doubleJumped && alive && began && !finished && swampSensor<=0 && !snared)
				canDoubleJump = true;
			else
				canDoubleJump = false;
			
			if((animManager.getCurrentAnimationState() == CharacterAnimationState.FLYING || animManager.getCurrentAnimationState() == CharacterAnimationState.LANDING) && !boostedOnce && alive && !sliding && began && !finished && wallSensor <= 0)
				canBoost = true;
			else
				canBoost = false;
			
			if(began && !finished && alive && !immortal)
				canDie = true;
			else
				canDie = false;
			
			if(onGround && alive && !finished && began && !sliding && !jumped)
				canSlide = true;
			else
				canSlide = false;
			
			if(sliding && alive && !finished && began && !slideButtonPressed && minimumSlidingTimePassed && standupSensor <= 0)
				canStandUp = true;
			else
				canStandUp = false;
			
			if(began && character.speed<0.1f && alive && !stopped && onGround && !sliding && animManager.getCurrentAnimationState() != CharacterAnimationState.LANDINGIDLE && animManager.getCurrentAnimationState() != CharacterAnimationState.STANDINGUP)
				shouldStop = true;
			else
				shouldStop = false;	
			
			if(began && !finished && character.speed>0.1f && alive && stopped)
				shouldStart = true;
			else
				shouldStart = false;
			
			if(alive && began && !finished && !onGround && animManager.getCurrentAnimationState() == CharacterAnimationState.RUNNING)
				shouldFly = true;
			else 
				shouldFly = false;
			
			if(/*began && */ alive && !sliding && powerupSet && !finished)
				canUseAbility = true;
			else 
				canUseAbility = false;
			
			if(alive && !immortal)
				canBeLifted = true;
			else 
				canBeLifted = false;
			
			if(alive && !immortal)
				canBeSnared = true;
			else 
				canBeSnared = false;
			
			if(alive && !immortal)
				canBeBlackHoleTeleported = true;
			else 
				canBeBlackHoleTeleported = false;
			
			jumpedQueued = false;
//		else
//		{
//			canBegin = false;                  
//			onGround = false;                  
//			canJump = false;                   
//			canDoubleJump = false;             
//			canLand = false;                   
//			queuedBoost = false;               
//			canBoost = false;                  
//			canSlide = false;                  
//			slideButtonPressed = false;        
//			canStandUp = false;                
//			forceStandUp = false;              
//			canDie = false;	                   
//			alive = false;              
//			powerupSet = false;                
//			finished = false;                  
//			shouldStop = false;                
//			shouldStart = false;               
//			shouldChangeToRunningState = false;
//			dieBottom = false;                 
//			dieTop = false;                    
//			canRun = false;                    
//			dieDismemberment = false;          
//			shouldFly = false;                 
//			stopFlyingAction = false;          
//			canUseAbility = false;             
//		}
	}
	
	public boolean isMe() {
		return isMe;
	}

	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}

	public boolean isQueuedJump() {
		return queuedJump;
	}

	public void setQueuedJump(boolean queuedJump) {
		this.queuedJump = queuedJump;
		
	}
	public boolean isQueuedDeathDismemberment() {
		return queuedDeathDismemberment;
	}

	public void setQueuedDeathDismemberment(boolean queuedDeathDismemberment) {
		this.queuedDeathDismemberment = queuedDeathDismemberment;
	}

	public boolean isQueuedDeathTop() {
		return queuedDeathTop;
	}

	public void setQueuedDeathTop(boolean queuedDeathTop) {
		this.queuedDeathTop = queuedDeathTop;
	}

	public boolean isQueuedDeathBottom() {
		return queuedDeathBottom;
	}

	public void setQueuedDeathBottom(boolean queuedDeathBottom) {
		this.queuedDeathBottom = queuedDeathBottom;
	}

	public boolean isCanBegin() {
		return canBegin;
	}
	public void setCanBegin(boolean canBegin) {
		this.canBegin = canBegin;
		
	}
	public boolean isOnGround() {
		return onGround;
	}
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
		
	}
	public boolean isCanJump() {
		return canJump;
	}
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
		
	}
	public boolean isCanDoubleJump() {
		return canDoubleJump;
	}
	public void setCanDoubleJump(boolean canDoubleJump) {
		this.canDoubleJump = canDoubleJump;
		
	}
	public boolean isCanLand() {
		return canLand;
	}
	public void setCanLand(boolean canLand) {
		this.canLand = canLand;
		
	}
	public float getQueuedBoost() {
		return queuedBoost;
	}
	public void setQueuedBoost(float queuedBoost) {
		this.queuedBoost = queuedBoost;
		
	}
	public boolean isCanBoost() {
		return canBoost;
	}

	public void setCanBoost(boolean canBoost) {
		this.canBoost = canBoost;
	}
	public boolean isCanSlide() {
		return canSlide;
	}
	public void setCanSlide(boolean canSlide) {
		this.canSlide = canSlide;
		
	}
	public boolean isSlideButtonPressed() {
		return slideButtonPressed;
	}
	public void setSlideButtonPressed(boolean slideButtonPressed) {
		this.slideButtonPressed = slideButtonPressed;
		
	}
	public boolean isCanStandUp() {
		return canStandUp;
	}
	public void setCanStandUp(boolean canStandUp) {
		this.canStandUp = canStandUp;
		
	}
	public boolean isForceStandUp() {
		return forceStandUp;
	}
	public void setForceStandUp(boolean forceStandUp) {
		this.forceStandUp = forceStandUp;
		
	}
	public boolean isCanDie() {
		return canDie;
	}
	public void setCanDie(boolean canDie) {
		this.canDie = canDie;
		
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
		
	}
	public boolean isPowerupSet() {
		return powerupSet;
	}
	public boolean isQueuedMushroomJump() {
		return queuedMushroomJump;
	}

	public void setQueuedMushroomJump(boolean queuedMushroomJump) {
		this.queuedMushroomJump = queuedMushroomJump;
	}

	public void setAbilitySet(boolean powerupSet) {
		this.powerupSet = powerupSet;
		
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
		
	}
	public boolean isShouldStop() {
		return shouldStop;
	}
	public void setShouldStop(boolean shouldStop) {
		this.shouldStop = shouldStop;
		
	}
	public boolean isShouldStart() {
		return shouldStart;
	}
	public void setShouldStart(boolean shouldStart) {
		this.shouldStart = shouldStart;
		
	}
	public boolean isShouldChangeToRunningState() {
		return shouldChangeToRunningState;
	}
	public void setShouldChangeToRunningState(boolean shouldChangeToRunningState) {
		this.shouldChangeToRunningState = shouldChangeToRunningState;
		
	}
	public boolean isDieBottom() {
		return dieBottom;
	}
	public void setDieBottom(boolean dieBottom) {
		this.dieBottom = dieBottom;
		
	}
	public boolean isDieTop() {
		return dieTop;
	}
	public void setDieTop(boolean dieTop) {
		this.dieTop = dieTop;
		
	}
	public boolean isDieDismemberment() {
		return dieDismemberment;
	}
	public void setDieDismemberment(boolean dieDismemberment) {
		this.dieDismemberment = dieDismemberment;
		
	}
	public boolean isShouldFly() {
		return shouldFly;
	}
	public void setShouldFly(boolean shouldFly) {
		this.shouldFly = shouldFly;
		
	}
	public boolean isStopFlyingAction() {
		return stopFlyingAction;
	}
	public void setStopFlyingAction(boolean stopFlyingAction) {
		this.stopFlyingAction = stopFlyingAction;
		
	}
	public boolean isJumped() {
		return jumped;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public boolean isGravityInversed() {
		return gravityInversed;
	}

	public void setGravityInversed(boolean gravityInversed) {
		this.gravityInversed = gravityInversed;
	}

	public boolean isGravityRotationSwitch() {
		return gravityRotationSwitch;
	}

	public void setGravityRotationSwitch(boolean gravityRotationSwitch) {
		this.gravityRotationSwitch = gravityRotationSwitch;
	}

	public int getQueuedLift() {
		return queuedLift;
	}

	public void setQueuedLift(int queuedLift) {
		this.queuedLift = queuedLift;
	}

	public boolean isCanBeLifted() {
		return canBeLifted;
	}

	public void setCanBeLifted(boolean canBeLifted) {
		this.canBeLifted = canBeLifted;
	}

	public boolean isCanRun() {
		return canRun;
	}
	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
		
	}
	public boolean isBegan() {
		return began;
	}
	public void setBegan(boolean began) {
		this.began = began;
		
	}
	public boolean isBoostedOnce() {
		return boostedOnce;
	}
	public void setBoostedOnce(boolean boostedOnce) {
		this.boostedOnce = boostedOnce;
		
	}
	public boolean isSliding() {
		return sliding;
	}
	public void setSliding(boolean sliding) {
		this.sliding = sliding;
		
	}
	public boolean isDoubleJumped() {
		return doubleJumped;
	}
	public void setDoubleJumped(boolean doubleJumped) {
		this.doubleJumped = doubleJumped;
		
	}
	public boolean isMinimumSlidingTimePassed() {
		return minimumSlidingTimePassed;
	}
	public void setMinimumSlidingTimePassed(boolean minimumSlidingTimePassed) {
		this.minimumSlidingTimePassed = minimumSlidingTimePassed;
		
	}
	public boolean isSwampSlowedOnce() {
		return swampSlowedOnce;
	}
	public void setSwampSlowedOnce(boolean swampSlowedOnce) {
		this.swampSlowedOnce = swampSlowedOnce;
		
	}
	public boolean isImmortal() {
		return immortal;
	}
	public void setImmortal(boolean immortal) {
		this.immortal = immortal;
		
	}
	public boolean isStopped() {
		return stopped;
	}
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
		
	}
	public boolean isFlying() {
		return flying;
	}
	public void setFlying(boolean flying) {
		this.flying = flying;
		
	}	
	public boolean isCanUseAbility() {
		return canUseAbility;
	}

	public void setCanUseAbility(boolean canUseAbility) {
		this.canUseAbility = canUseAbility;
		
	}
	
	
	public void incrementWallSensor(){
		wallSensor++;
	}
	public void decrementWallSensor(){
		if(wallSensor>0) wallSensor--;
	}
	public void incrementFootSensor(){
		footSensor++;
	}
	public void decrementFootSensor(){
		if(footSensor>0) footSensor--;
	}
	public void incrementStandupSensor(){
		standupSensor++;
	}
	public void decrementStandupSensor(){
		if(standupSensor>0) standupSensor--;
	}
	public void incrementJumpSensor(){
		jumpSensor++;
	}
	public void decrementJumpSensor(){
		if(jumpSensor>0) jumpSensor--;
	}
	public void incrementBarrelSensor(){
		barrelSensor++;
	}
	public void decrementBarrelSensor(){
		if(barrelSensor>0) barrelSensor--;
	}
	public void incrementHeadSensor(){
		headSensor++;
	}
	public void decrementHeadSensor(){
		if(headSensor>0) headSensor--;
	}
	public void incrementSwampSensor(){
		swampSensor++;
	}
	public void decrementSwampSensor(){
		if(swampSensor>0)swampSensor--;
	}
	public void incrementCatapultSensor(){
		catapultSensor++;
	}
	public void decrementCatapultSensor(){
		if(catapultSensor>0)catapultSensor--;
	}
	public void incrementMushroomSensor(){
		mushroomSensor++;
	}
	public void decrementMushroomSensor(){
		if(mushroomSensor>0)mushroomSensor--;
	}

	public int getWallSensor() {
		return wallSensor;
	}

	public void setWallSensor(int wallSensor) {
		this.wallSensor = wallSensor;
	}

	public int getFootSensor() {
		return footSensor;
	}

	public void setFootSensor(int footSensor) {
		this.footSensor = footSensor;
	}

	public int getStandupSensor() {
		return standupSensor;
	}

	public void setStandupSensor(int standupSensor) {
		this.standupSensor = standupSensor;
	}

	public int getJumpSensor() {
		return jumpSensor;
	}

	public void setJumpSensor(int jumpSensor) {
		this.jumpSensor = jumpSensor;
	}

	public int getBarrelSensor() {
		return barrelSensor;
	}

	public boolean isQueuedCatapultJump() {
		return queuedCatapultJump;
	}

	public void setQueuedCatapultJump(boolean queuedCatapultJump) {
		this.queuedCatapultJump = queuedCatapultJump;
	}

	public void setBarrelSensor(int barrelSensor) {
		this.barrelSensor = barrelSensor;
	}

	public int getHeadSensor() {
		return headSensor;
	}

	public void setHeadSensor(int headSensor) {
		this.headSensor = headSensor;
	}

	public int getSwampSensor() {
		return swampSensor;
	}

	public void setSwampSensor(int swampSensor) {
		this.swampSensor = swampSensor;
	}

	public int getCatapultSensor() {
		return catapultSensor;
	}

	public void setCatapultSensor(int catapultSensor) {
		this.catapultSensor = catapultSensor;
	}

	public int getMushroomSensor() {
		return mushroomSensor;
	}

	public void setMushroomSensor(int mushroomSensor) {
		this.mushroomSensor = mushroomSensor;
	}

	public Character getCharacter() {
		return character;
	}

	public boolean isLifted() {
		return lifted;
	}

	public void setLifted(boolean lifted) {
		this.lifted = lifted;
	}

	public boolean isSnared() {
		return snared;
	}

	public void setSnared(boolean snared) {
		this.snared = snared;
	}

	public int getQueuedSnare() {
		return queuedSnare;
	}

	public void setQueuedSnare(int queuedSnare) {
		this.queuedSnare = queuedSnare;
	}

	public boolean isCanBeSnared() {
		return canBeSnared;
	}

	public void setCanBeSnared(boolean canBeSnared) {
		this.canBeSnared = canBeSnared;
	}

	public boolean isJumpedQueued() {
		return jumpedQueued;
	}

	public void setJumpedQueued(boolean jumpedQueued) {
		this.jumpedQueued = jumpedQueued;
	}

	public boolean isCanBeBlackHoleTeleported() {
		return canBeBlackHoleTeleported;
	}

	public void setCanBeBlackHoleTeleported(boolean canBeBlackHoleTeleported) {
		this.canBeBlackHoleTeleported = canBeBlackHoleTeleported;
	}

	public Body getQueuedTeleportToBody() {
		return queuedTeleportToBody;
	}

	public void setQueuedTeleportToBody(Body queuedTeleportToBody) {
		this.queuedTeleportToBody = queuedTeleportToBody;
	}

	public boolean isTeleport() {
		return teleport;
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

	public boolean isDiedInAir() {
		return diedInAir;
	}

	public void setDiedInAir(boolean diedInAir) {
		this.diedInAir = diedInAir;
	}
}
