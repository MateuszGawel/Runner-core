package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class GameProgressBarHead extends Image{

	private Texture texture;
	private GameWorld gameWorld;
	private Character character;
	private float percent, parentWidth, parentHeight;
	
	public GameProgressBarHead(Texture texture, GameWorld gameWorld, Character character, float parentWidth, float parentHeight){
		super(texture);
		this.texture = texture;
		this.gameWorld = gameWorld;
		this.character = character;
		this.parentWidth = parentWidth;
		this.parentHeight = parentHeight;
		setSize(texture.getWidth(), texture.getHeight());
		setPosition(0, parentHeight/2 - getHeight()/2);
	}
	

	@Override
	public void act(float delta) {
    	long startTime = System.nanoTime();
		
		percent = (character.getBody().getPosition().x - 15.5f) / (((gameWorld.mapSize.x)/PPM-10.5f-20));
		if(character.getX() < gameWorld.mapSize.x/PPM-20) 
			setPosition((parentWidth) * percent - getWidth()/2, getY());
    	
        long endTime = System.nanoTime();
        if(ScreensManager.getInstance().getCurrentScreen() instanceof GameScreen)
        if(((GameScreen)ScreensManager.getInstance().getCurrentScreen()).world.gameProgressBarHead != null)
        ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).world.gameProgressBarHead.add(endTime - startTime);
		
	}

}
