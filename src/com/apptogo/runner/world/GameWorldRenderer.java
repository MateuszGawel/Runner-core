package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameWorldRenderer 
{	
    GameWorld gameWorld;  
    OrthographicCamera camera;  
    Box2DDebugRenderer debugRenderer;
    
	public int currentScreenWidth, currentScreenHeight;
	private Rectangle cullingArea;
	
    public GameWorldRenderer(GameWorld gameWorld)  
    {  
        this.gameWorld = gameWorld;  
        this.debugRenderer = new Box2DDebugRenderer();  
        this.camera = (OrthographicCamera) gameWorld.getWorldStage().getCamera();  
       
        camera.position.x = gameWorld.player.character.getBody().getPosition().x;  
        camera.position.y = gameWorld.player.character.getBody().getPosition().y;  
        
        cullingArea = new Rectangle(camera.position.x - camera.viewportWidth * camera.zoom / 2, camera.position.y - camera.viewportHeight * camera.zoom / 2, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
    }  
    
	public void resize(int width, int height){
		currentScreenWidth = width;
		currentScreenHeight = height;
	}
	

	
    public void render()
    {  	
    	//ustawienia kamery
	    camera.position.set(
	            Math.min(gameWorld.maxCameraX - 2, Math.max(gameWorld.player.character.getBody().getPosition().x + 2, gameWorld.minCameraX + 2)),
	            Math.min(gameWorld.maxCameraY - 1, Math.max(gameWorld.player.character.getBody().getPosition().y + 1, gameWorld.minCameraY + 1)),
	            0);
	    
		gameWorld.backgroundCamera.position.set(Runner.SCREEN_WIDTH/2/PPM, Runner.SCREEN_HEIGHT/2/PPM, 0); 
    	camera.update();
    	//camera.zoom = 4f;
    	cullingArea.set(camera.position.x - camera.viewportWidth * camera.zoom / 2, camera.position.y - camera.viewportHeight * camera.zoom / 2, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom); 	

    	//backgroundStage
    	//gameWorld.getBackgroundStage().getRoot().setCullingArea(cullingArea);
		gameWorld.getBackgroundStage().getViewport().update(currentScreenWidth, currentScreenHeight);
    	gameWorld.getBackgroundStage().draw();
    	Logger.log(this, "liczba rendercalli BackgroundStage: " + ((SpriteBatch)gameWorld.getBackgroundStage().getBatch()).renderCalls);

    	
    	//worldStage
    	gameWorld.getWorldStage().getRoot().setCullingArea(cullingArea);
    	gameWorld.getWorldStage().getViewport().update(currentScreenWidth, currentScreenHeight);	
    	gameWorld.getWorldStage().draw();  
    	Logger.log(this, "liczba rendercalli WorldStage: " + ((SpriteBatch)gameWorld.getWorldStage().getBatch()).renderCalls);
    	
    	
    	debugRenderer.render(gameWorld.world, camera.combined);
    }
}
