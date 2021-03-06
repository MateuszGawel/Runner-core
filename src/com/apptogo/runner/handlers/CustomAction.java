package com.apptogo.runner.handlers;


public abstract class CustomAction {

	/*�eby uzyc trzeba wywolac metode registerAction na character
	 * podac jako parametr new CharacterAction implementujac metode perform
	 * czyli to co ma sie stac po uplynieciu delaya
	 */
	
	protected float stateTime = 0;
	protected float timeElapsed = 0;
	private float delay;
	private boolean finished;
	private int loopCount = 0;


	private int loops = 1;
	protected Object[] args;
	private boolean registered;
	
	public CustomAction(float delay){
		this.delay = delay;
	}
	
	/**loops: 0 - nieskonczona, 1 - tylko raz, x - ilosc razy */
	public CustomAction(float delay, int loops){
		this(delay);
		this.loops = loops;
	}
	
	/**loops: 0 - nieskonczona, 1 - tylko raz, x - ilosc razy */
	public CustomAction(float delay, int loops, Object... args){
		this(delay, loops);
		this.args = args;
	}
	
	public abstract void perform();
	public void onFinish(){

	}
	
	public void additionalTask(){
		
	}
	
	public void act(float delta){
		additionalTask();
		if(delta <= 0.1)
			stateTime += delta;

		if((stateTime - timeElapsed)/delay >= 1 && !finished){
			loopCount++;
			timeElapsed+=delay;
			perform();
			if(loops > 0 && loopCount >= loops){
				this.finished = true;
				registered = false;
				onFinish();
			}	
		}
	}
	
	public boolean isFinished(){ return this.finished; }
	public void setFinished(boolean isFinished){ this.finished = isFinished; }
	public void resetAction(){
		finished = false;
		stateTime = 0;
		loopCount = 0;
		timeElapsed = 0;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	public int getLoopCount() {
		return loopCount;
	}
}
