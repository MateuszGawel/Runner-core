package com.apptogo.runner.handlers;

import com.apptogo.runner.actors.Character;

public abstract class CharacterAction {

	/*�eby uzyc trzeba wywolac metode registerAction na character
	 * podac jako parametr new CharacterAction implementujac metode perform
	 * czyli to co ma sie stac po uplynieciu delaya
	 */
	
	private float stateTime = 0;
	protected float timeElapsed = 0;
	private float delay;
	private boolean finished;
	private int loopCount = 0;
	private int loops = 1;
	protected Object[] args;
	protected Character character;
	
	public CharacterAction(float delay){
		this.delay = delay;
	}
	
	/**loops: 0 - nieskonczona, 1 - tylko raz, x - ilosc razy */
	public CharacterAction(Character character, float delay, int loops){
		this(delay);
		this.character = character;
		this.loops = loops;
	}
	
	/**loops: 0 - nieskonczona, 1 - tylko raz, x - ilosc razy */
	public CharacterAction(Character character, float delay, int loops, Object... args){
		this(character, delay, loops);
		this.args = args;
	}
	
	public abstract void perform();
	
	public void act(float delta){
		stateTime += delta;

		if((stateTime - timeElapsed)/delay >= 1){
			loopCount++;
			timeElapsed+=delay;
			perform();
			if(loops > 0 && loopCount >= loops){
				this.finished = true;
			}	
		}
	}
	
	public boolean isFinished(){ return this.finished; }
}