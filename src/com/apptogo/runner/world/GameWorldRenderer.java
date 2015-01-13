package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.MyTiledMapRenderer;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameWorldRenderer 
{	
    GameWorld gameWorld;  
    OrthographicCamera camera;  
    Box2DDebugRenderer debugRenderer;
    MyTiledMapRenderer tiledMapRenderer;
	public int currentScreenWidth, currentScreenHeight;
	
    public GameWorldRenderer(GameWorld gameWorld)  
    {  
        this.gameWorld = gameWorld;  
        this.debugRenderer = new Box2DDebugRenderer();  
        this.camera = (OrthographicCamera) gameWorld.worldStage.getCamera();  
       
        camera.position.x = gameWorld.player.character.getBody().getPosition().x;  
        camera.position.y = gameWorld.player.character.getBody().getPosition().y;  
        
        tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
        
    }  
    
	public void resize(int width, int height){
		currentScreenWidth = width;
		currentScreenHeight = height;
	}
	
    public void render()
    {  	
		tiledMapRenderer.setView(camera);
	
	    camera.position.set(
	            Math.min(gameWorld.maxCameraX - 2, Math.max(gameWorld.player.character.getBody().getPosition().x + 2, gameWorld.minCameraX + 2)),
	            Math.min(gameWorld.maxCameraY - 1, Math.max(gameWorld.player.character.getBody().getPosition().y + 1, gameWorld.minCameraY + 1)),
	            0);
	    
		gameWorld.backgroundCamera.position.set(Runner.SCREEN_WIDTH/2/PPM, Runner.SCREEN_HEIGHT/2/PPM, 0); 
    	camera.update();
    	//camera.zoom = 2f;

		gameWorld.getBackgroundStage().getViewport().update(currentScreenWidth, currentScreenHeight);
    	gameWorld.getBackgroundStage().draw();
    	
    	tiledMapRenderer.renderFrontLayer();
		Batch batch = gameWorld.getWorldStage().getBatch();
		if (batch != null) {
			batch.begin();
			gameWorld.worldBackgroundGroup.draw(batch, 1);
			batch.end();
		}
    	tiledMapRenderer.render();
    	
    	gameWorld.worldStage.getViewport().update(currentScreenWidth, currentScreenHeight);
    	gameWorld.worldStage.draw();
    	

    	//debugRenderer.render(gameWorld.world, camera.combined);
    	
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
