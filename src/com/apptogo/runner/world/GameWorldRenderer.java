package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
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
       
        camera.position.x = gameWorld.character.getBody().getPosition().x;  
        camera.position.y = gameWorld.character.getBody().getPosition().y;  
        
        tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
        
    }  
    
    public void render(){  	
		tiledMapRenderer.setView(camera);
	
	    camera.position.set(
	            Math.min(gameWorld.maxCameraX, Math.max(gameWorld.character.getBody().getPosition().x, gameWorld.minCameraX)),
	            Math.min(gameWorld.maxCameraY, Math.max(gameWorld.character.getBody().getPosition().y, gameWorld.minCameraY)),
	            0);
		gameWorld.backgroundCamera.position.set(Runner.SCREEN_WIDTH/2/PPM, Runner.SCREEN_HEIGHT/2/PPM, 0); 
		
    	camera.update();
		gameWorld.backgroundCamera.update();

		
    	gameWorld.backgroundStage.draw();
    	tiledMapRenderer.render();
    	gameWorld.worldStage.draw();
    	debugRenderer.render(gameWorld.world, camera.combined);
    	
    	
    	
    	//�wiat�a powoduja spadek wydajnosci
    	/*
    	if(gameWorld.rayHandler != null)
    	{
    		gameWorld.rayHandler.setCombinedMatrix(camera.combined);
    		gameWorld.rayHandler.updateAndRender();
    	}
    	*/
    }
}
