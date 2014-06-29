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

    public GameWorldRenderer(GameWorld gameWorld)  
    {  
        this.gameWorld = gameWorld;  
        this.debugRenderer = new Box2DDebugRenderer();  
        this.camera = (OrthographicCamera) gameWorld.worldStage.getCamera();  
       
        camera.position.x = gameWorld.player.getPlayerBody().getPosition().x;  
        camera.position.y = gameWorld.player.getPlayerBody().getPosition().y;  
        
        tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
        
    }  
    
    public void render(){
    	
		tiledMapRenderer.setView(camera);
	    tiledMapRenderer.render();
	    
	    camera.position.set(
	            Math.min(gameWorld.maxCameraX, Math.max(gameWorld.player.getPlayerBody().getPosition().x, gameWorld.minCameraX)),
	            Math.min(gameWorld.maxCameraY, Math.max(gameWorld.player.getPlayerBody().getPosition().y, gameWorld.minCameraY)),
	            0);
    	camera.update();
    	
    	debugRenderer.render(gameWorld.world, camera.combined);
    	
    	//œwiat³a powoduja spadek wydajnosci
    	/*
    	if(gameWorld.rayHandler != null)
    	{
    		gameWorld.rayHandler.setCombinedMatrix(camera.combined);
    		gameWorld.rayHandler.updateAndRender();
    	}
    	*/
    	gameWorld.worldStage.draw();
    
    }
}
