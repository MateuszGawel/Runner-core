package com.apptogo.runner.world;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ForestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -80f);


	public Image tree4;
	public Actor tree3;
	public Actor tree2;
	public Actor tree1;
	
	
	public ForestWorld(String mapPath, Player player)
	{
		super(mapPath, player, GameWorldType.FOREST);
		super.world.setGravity(GRAVITY);
		createBackground();
		music = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/forestMusic.ogg");
		music.setVolume(0.4f);
	}
	
	private void createBackground(){

		tree4 = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree4.png"));
		tree4.setPosition(0, 0);
		backgroundStage.addActor(tree4);
		
		//tree3 = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree3.png"), -0.4f, -38/mapSize.y, mapSize, player.character, 0, 200/PPM);
		//background.addActor(tree3);
		
		//tree2 = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree2.png"), -0.5f, -89/mapSize.y, mapSize, player.character, 0, 130/PPM);
		//background.addActor(tree2);
		
		//tree1 = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree1.png"), -0.8f, -192/mapSize.y, mapSize, player.character, 0, -150/PPM);
		//mbackground.addActor(tree1);
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}
	
	@Override
	public void update(float delta){	
		Gdx.gl.glClearColor(0.855f, 0.639f, 0.321f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.update(delta);
	}
}
