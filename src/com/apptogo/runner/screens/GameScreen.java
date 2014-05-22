package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends BaseScreen{
	
	private final boolean DRAW_DEBUG = true;
		
	private Stage stage;
	private Viewport viewport;
	private Box2DDebugRenderer debugRenderer;
	
	private World world;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
    
	public GameScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void show() {
		
		stage = new Stage();
		viewport = new StretchViewport( 800, 480, camera);
		
		if(DRAW_DEBUG) debugRenderer = new Box2DDebugRenderer();
		
		if( TiledMapLoader.getInstance().load( "gfx/game/levels/map.tmx" ) )
		{		
			tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
			world = TiledMapLoader.getInstance().getWorld();
		}
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.step(1/60f, 1, 1);
			    
		tiledMapRenderer.setView( (OrthographicCamera) viewport.getCamera() );
	    tiledMapRenderer.render();
	    
	    spriteBatch.begin();
	    	if(DRAW_DEBUG) debugRenderer.render(world, viewport.getCamera().combined);
	    spriteBatch.end();
	    
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
		Logger.log(this, "width: "+width+" height: "+height);
		viewport.update(width, height);		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_GAME;
	}


}
