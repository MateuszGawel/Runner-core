package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ForestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -80f);


	public ParallaxBackground tree1, tree2, tree3;
	private ForestBackgroundRenderer background;
	private TextureAtlas atlas;
	public ForestWorld(String mapPath, Player player)
	{
		super(mapPath, player, GameWorldType.FOREST);
		super.world.setGravity(GRAVITY);
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/forestBackground.pack");
		createBackground();
		
		music = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/forestMusic.ogg");
		music.setVolume(0.4f);
	}
	
	private void createBackground(){

		background = new ForestBackgroundRenderer();
		backgroundStage.addActor(background);
		
		
		tree3 = new ParallaxBackground(atlas.findRegion("tree1"), mapSize, 0.3f, -284/mapSize.y, player.character, 0, 80/PPM);
		tree3.setScale(0.7f);
		tree3.setDarken(0.8f);
		backgroundStage.addActor(tree3);
		
		tree2 = new ParallaxBackground(atlas.findRegion("tree1"), mapSize, 0.4f, -384/mapSize.y, player.character, 0, -20/PPM);
		tree2.setScale(0.85f);
		tree2.setDarken(0.9f);
		backgroundStage.addActor(tree2);

		tree1 = new ParallaxBackground(atlas.findRegion("tree1"), mapSize, 0.8f, -584/mapSize.y, player.character, 0, -170/PPM);
		backgroundStage.addActor(tree1);
		
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
