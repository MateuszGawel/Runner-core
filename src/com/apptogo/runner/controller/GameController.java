package com.apptogo.runner.controller;

import com.badlogic.gdx.InputAdapter;

public class GameController extends InputAdapter{
	
	//kontroler jest do ogarniecia jeszcze. To wyjdzie w trakcie jak trzeba bedzie robic screen gry
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = false;
		return true;
	}
}
