package com.apptogo.runner.world;

import com.apptogo.runner.handlers.TiledMapLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameWorldRenderer {

	
    GameWorld gameWorld;  
    OrthographicCamera camera;  
    Box2DDebugRenderer debugRenderer;
    TiledMapRenderer tiledMapRenderer;
    private SpriteBatch sb;
  
    public GameWorldRenderer(GameWorld gameWorld)  
    {  
        this.gameWorld = gameWorld;  
        this.debugRenderer = new Box2DDebugRenderer();  
      
        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units  
        this.camera = (OrthographicCamera) gameWorld.stage.getCamera();  
      
        // center the camera on bob (optional)  
        camera.position.x = gameWorld.player.getPlayerBody().getPosition().x;  
        camera.position.y = gameWorld.player.getPlayerBody().getPosition().y;  
        
        tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
        
        sb = new SpriteBatch();
    }  
    
    public void render(){
    	
		tiledMapRenderer.setView(camera);
	    tiledMapRenderer.render();
	    
    	camera.position.x = gameWorld.player.getPlayerBody().getPosition().x;
    	camera.position.y = gameWorld.player.getPlayerBody().getPosition().y;
    	camera.update();
    	
    	debugRenderer.render(gameWorld.world, camera.combined);
    	
    	//rendering lights
    	if(gameWorld.rayHandler != null)
    	{
    		gameWorld.rayHandler.setCombinedMatrix(camera.combined);
    		gameWorld.rayHandler.updateAndRender();
    	}
    	
    	gameWorld.stage.draw();
    	
    	//FPS render
    	sb.begin();
    	BitmapFont font = new BitmapFont();
    	font.draw(sb, "FPS: "+String.valueOf( Gdx.graphics.getFramesPerSecond() ), 10f, 460f);
    	sb.end();
    }
}
