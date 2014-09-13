package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WildWestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -60f);

	public Image mountains;
	public ParallaxBackground rocks;
	public Image skyBlue;
	public Actor sand;
	
	
	public WildWestWorld(String mapPath, Player player)
	{
		super(mapPath, player);
		super.world.setGravity(GRAVITY);
		createBackground();
	}
	
	private void createBackground(){

		skyBlue = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/skyBlue.png"));
		skyBlue.setPosition(0, 500/PPM);
		background.addActor(skyBlue);
		
		mountains = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/mountains.png"), mapSize, -0.05f, character, 0, 350/PPM);
		background.addActor(mountains);
		
		rocks = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/rocks.png"), mapSize, -0.1f, character, 0, 400/PPM);
		background.addActor(rocks);
		
		sand = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/sand.png"), -0.5f, -0.15f, mapSize, character, 0, 80/PPM);
		background.addActor(sand);
	}
	
	@Override
	public void dispose(){
		
	}
}
