package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
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
		setSize(60, 60);
		setPosition(0, parentHeight/2 - getHeight()/2);
	}
	
	@Override public void act(float delta){
		percent = (character.getBody().getPosition().x - 10.5f) / (((gameWorld.mapSize.x)/PPM-10.5f-10));
		if(character.getX() < gameWorld.mapSize.x/PPM-10) 
			setPosition((parentWidth) * percent - getWidth()/2, getY());
	}
}
