package com.apptogo.runner.handlers;

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
	protected Object[] args;
	protected Character character;
	
	public CharacterAction(float delay){
		this.delay = delay;
	}
	
	public CharacterAction(Character character, float delay){
		this.delay = delay;
		this.character = character;
	}
	
	public CharacterAction(float delay, Object... args){
		this.delay = delay;
		this.args = args;
	}
	
	public CharacterAction(Character character, float delay, Object... args){
		this.delay = delay;
		this.args = args;
		this.character = character;
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
