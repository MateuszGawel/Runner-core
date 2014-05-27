package com.apptogo.runner.world;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.apptogo.runner.handlers.TiledMapLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
      
        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units  
        this.camera = (OrthographicCamera) gameWorld.stage.getCamera();  
      
        // center the camera on bob (optional)  
        camera.position.x = gameWorld.player.getPlayerBody().getPosition().x;  
        camera.position.y = gameWorld.player.getPlayerBody().getPosition().y;  
        
        tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
    }  
    
    public void render(){
		tiledMapRenderer.setView(camera);
	    tiledMapRenderer.render();
	    
    	camera.position.x = gameWorld.player.getPlayerBody().getPosition().x;
    	camera.position.y = gameWorld.player.getPlayerBody().getPosition().y;
    	camera.update();
    	
    	debugRenderer.render(gameWorld.world, camera.combined);
    	
    	gameWorld.rayHandler.updateAndRender();
    	
    	gameWorld.stage.draw();
    }
}
