package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WildWestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -80f);

	public ParallaxBackground mountains;
	public ParallaxBackground rocks;
	public Image skyBlue;
	public ParallaxBackground sand;
	private WildWestBackgroundRenderer background;
	
	private TextureRegion sandRegion, mountainsRegion, rocksRegion;
	//private TextureAtlas atlas;
	
	public WildWestWorld(String mapPath, Player player)
	{
		super(mapPath, player, GameWorldType.WILDWEST);
		super.world.setGravity(GRAVITY);
		TextureAtlas atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/wildwestBackground.pack");
		
		Logger.log(this, "TWORZENIE GS REGIONOW");
		
		sandRegion = atlas.findRegion("sand");
		mountainsRegion = atlas.findRegion("mountains");
		rocksRegion = atlas.findRegion("rocks");
		
		createBackground();
		
		music = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/wildWestMusic.ogg");
		music.setVolume(0.25f);	
	}
	
	private void createBackground(){

		background = new WildWestBackgroundRenderer();
		backgroundStage.addActor(background);
		
		mountains = new ParallaxBackground(mountainsRegion, mapSize, 0, -79/mapSize.y, player.character, 0, 300/PPM);
		backgroundStage.addActor(mountains);
		
		rocks = new ParallaxBackground(rocksRegion, mapSize, 0, -156/mapSize.y, player.character, 0, 260/PPM);
		backgroundStage.addActor(rocks);
		
		sand = new ParallaxBackground(sandRegion, mapSize, 0.4f, -484/mapSize.y, player.character, 0, 40/PPM);

		backgroundStage.addActor(sand);
		
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
