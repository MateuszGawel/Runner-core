package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WildWestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -80f);

	public Image mountains;
	public ParallaxBackground rocks;
	public Image skyBlue;
	public Actor sand;
	
	public WildWestWorld(String mapPath, Player player)
	{
		super(mapPath, player);
		super.world.setGravity(GRAVITY);
		createBackground();
		music = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/wildWestMusic.ogg");
		music.setVolume(0.25f);
	}
	
	private void createBackground(){

		skyBlue = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/skyBlue.png"));
		skyBlue.setPosition(0, 400/PPM);
		background.addActor(skyBlue);
		Logger.log(this, "wsp: " + mapSize.y/PPM/266);
		mountains = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/mountains.png"), mapSize, -179/mapSize.y, player.character, 0, 270/PPM);
		background.addActor(mountains);
		
		rocks = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/rocks.png"), mapSize, -256/mapSize.y, player.character, 0, 260/PPM);
		background.addActor(rocks);
		
		sand = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/sand.png"), -0.4f, -384/mapSize.y, mapSize, player.character, 0, -60/PPM);
		background.addActor(sand);
	}

	@Override
	public void dispose(){
		super.dispose();
		Logger.log(this, "dispose wildwest");
	}
	
	@Override
	public void update(float delta){	
		Gdx.gl.glClearColor(0.855f, 0.639f, 0.321f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.update(delta);
	}
}
