package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class ForestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -150f);


	public ParallaxBackground tree1, tree2, tree3, grass;
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
		
		
		tree3 = new ParallaxBackground(atlas.findRegion("trees"), mapSize, 0.3f, -284/mapSize.y, player.character, 0, 180/PPM);
		tree3.setScale(0.7f);
		tree3.setDarken(0.8f);
		backgroundStage.addActor(tree3);
		
		tree2 = new ParallaxBackground(atlas.findRegion("trees"), mapSize, 0.4f, -384/mapSize.y, player.character, 0, 110/PPM);
		tree2.setScale(0.85f);
		tree2.setDarken(0.9f);
		backgroundStage.addActor(tree2);

		tree1 = new ParallaxBackground(atlas.findRegion("trees"), mapSize, 0.8f, -584/mapSize.y, player.character, 0, 30/PPM);
		backgroundStage.addActor(tree1);
		
		grass = new ParallaxBackground(atlas.findRegion("grass"), mapSize, 0.8f, -584/mapSize.y, player.character, 0, -80/PPM);
		backgroundStage.addActor(grass);
		grass = new ParallaxBackground(atlas.findRegion("grass"), mapSize, 0.8f, -584/mapSize.y, player.character, 0, -320/PPM);
		backgroundStage.addActor(grass);
		grass = new ParallaxBackground(atlas.findRegion("grass"), mapSize, 0.8f, -584/mapSize.y, player.character, 0, -560/PPM);
		backgroundStage.addActor(grass);
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
