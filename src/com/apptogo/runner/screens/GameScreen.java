package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.PhysicsGenerator;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
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
	
	private OrthogonalTiledMapRenderer tiledMapRendered;
	TiledMap tileMap;
	private Stage stage;
	private Viewport viewport;
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private PhysicsGenerator physicsGenerator;
    
	public GameScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void show() {
		
		stage = new Stage();
		
		TmxMapLoader loader = new TmxMapLoader();
		tileMap = loader.load( "gfx/game/levels/map.tmx" );
		
		tiledMapRendered = new OrthogonalTiledMapRenderer( tileMap );
		
		viewport = new StretchViewport( 800, 480, camera);
		
		world = new World(new Vector2(0f, -9.2f), true);
		
		//body definition
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set( new Vector2(100f, 400f) );
		
		//shape definition
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50, 50);
		
		//fixture definition
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1f;
		fdef.friction = 0.5f;
		fdef.restitution = 1f;
		
		//creating body
		Body body = world.createBody(bdef);
		body.createFixture(fdef);
		
		
		
		debugRenderer = new Box2DDebugRenderer();

		
		
		physicsGenerator = new PhysicsGenerator(world);
		physicsGenerator.createPhysics(tileMap);		
		
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1/60f, 1, 1);
	    //spriteBatch.begin();
			    
		tiledMapRendered.setView( (OrthographicCamera) viewport.getCamera() );
	    tiledMapRendered.render();

	    spriteBatch.begin();
	    debugRenderer.render(world, viewport.getCamera().combined);
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
