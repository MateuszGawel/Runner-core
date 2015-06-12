package com.apptogo.runner.actors;

import com.apptogo.runner.enums.CharacterAnimationState;

public class Bulb extends Obstacle
{	
	public Bulb(float x, float y)
	{
		super();
		
		this.createAnimation("bulb", 2, 0.5f, CharacterAnimationState.IDLE, true);
		setAnimate(true);
		
		isGameObstacle = false;
		
		setPosition(x, y);		
	}
}
