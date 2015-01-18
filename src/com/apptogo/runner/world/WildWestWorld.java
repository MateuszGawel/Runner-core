package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

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
	
	private TextureRegion sandRegion, mountainsRegion, rocksRegion;
	private TextureAtlas atlas;
	
	private ShapeRenderer shapeRenderer;
	private Color lightBlue, brown, lightBrown;
	
	public WildWestWorld(String mapPath, Player player)
	{
		super(mapPath, player);
		super.world.setGravity(GRAVITY);
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/wildwestBackground.pack");
		sandRegion = atlas.findRegion("sand");
		mountainsRegion = atlas.findRegion("mountains");
		rocksRegion = atlas.findRegion("rocks");
		createBackground();
		music = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/wildWestMusic.ogg");
		music.setVolume(0.25f);
		shapeRenderer = new ShapeRenderer();
		lightBlue = new Color(0.823f, 0.960f, 1, 1);
		brown = new Color(0.576f, 0.349f, 0.247f, 1);
		lightBrown = new Color(0.855f, 0.639f, 0.321f, 1);
	}
	
	private void createBackground(){

		//skyBlue = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/skyBlue.png"));
		//skyBlue.setPosition(0, 400/PPM);
		
		//background.addActor(skyBlue);

		mountains = new ParallaxBackground(mountainsRegion, mapSize, 0, -79/mapSize.y, player.character, 0, 270/PPM);
		background.addActor(mountains);
		
		rocks = new ParallaxBackground(rocksRegion, mapSize, 0, -156/mapSize.y, player.character, 0, 260/PPM);
		background.addActor(rocks);
		
		sand = new ParallaxBackground(sandRegion, mapSize, 0.4f, -484/mapSize.y, player.character, 0, 40/PPM);

		background.addActor(sand);
		
	}

	@Override
	public void dispose(){
		super.dispose();
		Logger.log(this, "dispose wildwest");
	}
	
	@Override
	public void update(float delta){
		//to jest na zlym viewporcie
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(0, Runner.SCREEN_HEIGHT*2/3-100, Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT/3+100+1, Color.WHITE, Color.WHITE, lightBlue, lightBlue);
			shapeRenderer.rect(0, Runner.SCREEN_HEIGHT*1/3-200, Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT/3+100+1, brown, brown, brown, brown);
			shapeRenderer.rect(0, 0, Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT/3-200+1, lightBrown, lightBrown, lightBrown, lightBrown);
		shapeRenderer.end();
		super.update(delta);

	}
}
