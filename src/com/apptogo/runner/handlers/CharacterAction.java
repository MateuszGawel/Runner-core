package com.apptogo.runner.handlers;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.actors.Character;

public abstract class CharacterAction {

	/*¿eby uzyc trzeba wywolac metode registerAction na character
	 * podac jako parametr new CharacterAction implementujac metode perform
	 * czyli to co ma sie stac po uplynieciu delaya
	 */
	
	private float stateTime = 0;
	private float timeElapsed = 0;
	private float delay;
	private boolean finished;
	
	public CharacterAction(Character character, float delay){
		this.delay = delay;
	}
	
	public abstract void perform();
	
	public void act(float delta){
		stateTime += delta;
		
		if((stateTime - timeElapsed)/delay >= 1){
			timeElapsed+=delay;
			perform();
			this.finished = true;
		}
	}
	
	public boolean isFinished(){ return this.finished; }
}
