package com.apptogo.runner.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter {

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Input.x = x;
		Input.y = y;
		Input.down = true;
		return true;
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		Input.x = x;
		Input.y = y;
		Input.down = false;
		return true;
	}
	
	@Override
	public boolean keyDown(int k) {
		if(k == Keys.LEFT) Input.setKey(Input.LEFT, true);
		if(k == Keys.RIGHT) Input.setKey(Input.RIGHT, true); 
		if(k == Keys.SPACE) Input.setKey(Input.START, true); 
		return true;
	}
	
	@Override
	public boolean keyUp(int k) {
		if(k == Keys.LEFT) Input.setKey(Input.LEFT, false);
		if(k == Keys.RIGHT) Input.setKey(Input.RIGHT, false); 
		if(k == Keys.SPACE) Input.setKey(Input.START, true); 
		return true;
	}

}
