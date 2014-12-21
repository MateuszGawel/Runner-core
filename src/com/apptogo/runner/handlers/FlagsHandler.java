package com.apptogo.runner.handlers;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.logger.Logger;

public class FlagsHandler {
	
	//ZAKOLEJKOWANE AKCJE
	private boolean queuedJump;

	//ZMIENNE
	private Character character;
	private AnimationManager animManager;
	
	//KOÑCOWE
	private boolean canBegin;
	private boolean onGround;
	private boolean canJump;
	private boolean canDoubleJump;
	private boolean canLand;
	private boolean queuedBoost;
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
	private boolean dieDismemberment;
	private boolean shouldFly;
	private boolean stopFlyingAction;
	private boolean canUseAbility;

	//SK£ADOWE
	private boolean began;
	private boolean boostedOnce;
	private boolean sliding;
	private boolean doubleJumped;
	private boolean minimumSlidingTimePassed;
	private boolean swampSlowedOnce;
	private boolean immortal;
	private boolean stopped;
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
		
		if((footSensor>0 || barrelSensor>0) && !onGround && character.getBody().getLinearVelocity().y <= 5) //5 mo¿e byæ z³¹ wartoœci¹
			canLand = true;
		else
			canLand = false;
			
		if(footSensor>0 || barrelSensor>0)
			onGround = true;
		else
			onGround = false;
		
		if(alive && began && character.speed <= character.playerSpeedLimit-character.playerSlowAmmount)
			canRun = true;
		else
			canRun = false;
		
		if(onGround && character.speed>1 && animManager.getCurrentAnimationState() == CharacterAnimationState.IDLE)
			shouldChangeToRunningState = true;
		else
			shouldChangeToRunningState = false;
		
		if((jumpSensor > 0 || wallSensor > 0) && alive && began && !finished && headSensor<=0)
			canJump = true;
		else
			canJump = false;
		
		if(!canJump && !doubleJumped)
			canDoubleJump = true;
		else
			canDoubleJump = false;
		
		if((animManager.getCurrentAnimationState() == CharacterAnimationState.FLYING || animManager.getCurrentAnimationState() == CharacterAnimationState.LANDING) && !boostedOnce && alive && !sliding && began && !finished && wallSensor <= 0 && canRun)
			canBoost = true;
		else
			canBoost = false;
	}
	
	public boolean isQueuedJump() {
		return queuedJump;
	}

	public void setQueuedJump(boolean queuedJump) {
		this.queuedJump = queuedJump;
		
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
	public boolean isQueuedBoost() {
		return queuedBoost;
	}
	public void setQueuedBoost(boolean canBoost) {
		this.queuedBoost = canBoost;
		
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
	public void setPowerupSet(boolean powerupSet) {
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
		wallSensor--;
	}
	public void incrementFootSensor(){
		footSensor++;
	}
	public void decrementFootSensor(){
		footSensor--;
	}
	public void incrementStandupSensor(){
		standupSensor++;
	}
	public void decrementStandupSensor(){
		standupSensor--;
	}
	public void incrementJumpSensor(){
		jumpSensor++;
	}
	public void decrementJumpSensor(){
		jumpSensor--;
	}
	public void incrementBarrelSensor(){
		barrelSensor++;
	}
	public void decrementBarrelSensor(){
		barrelSensor--;
	}
	public void incrementHeadSensor(){
		headSensor++;
	}
	public void decrementHeadSensor(){
		headSensor--;
	}
	public void incrementSwampSensor(){
		swampSensor++;
	}
	public void decrementSwampSensor(){
		swampSensor--;
	}
	public void incrementCatapultSensor(){
		catapultSensor++;
	}
	public void decrementCatapultSensor(){
		catapultSensor--;
	}
	public void incrementMushroomSensor(){
		mushroomSensor++;
	}
	public void decrementMushroomSensor(){
		mushroomSensor--;
	}
}
