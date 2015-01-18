package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.MyTiledMapRenderer;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameWorldRenderer 
{	
    GameWorld gameWorld;  
    OrthographicCamera camera;  
    Box2DDebugRenderer debugRenderer;
    MyTiledMapRenderer tiledMapRenderer;
	public int currentScreenWidth, currentScreenHeight;
	private Rectangle cullingArea;
	
    public GameWorldRenderer(GameWorld gameWorld)  
    {  
        this.gameWorld = gameWorld;  
        this.debugRenderer = new Box2DDebugRenderer();  
        this.camera = (OrthographicCamera) gameWorld.worldStage.getCamera();  
       
        camera.position.x = gameWorld.player.character.getBody().getPosition().x;  
        camera.position.y = gameWorld.player.character.getBody().getPosition().y;  
        
        tiledMapRenderer = TiledMapLoader.getInstance().getMapRenderer();
        cullingArea = new Rectangle(camera.position.x - camera.viewportWidth * camera.zoom / 2, camera.position.y - camera.viewportHeight * camera.zoom / 2, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        
    }  
    
	public void resize(int width, int height){
		currentScreenWidth = width;
		currentScreenHeight = height;
	}
	
    public void render()
    {  	
	    camera.position.set(
	            Math.min(gameWorld.maxCameraX - 2, Math.max(gameWorld.player.character.getBody().getPosition().x + 2, gameWorld.minCameraX + 2)),
	            Math.min(gameWorld.maxCameraY - 1, Math.max(gameWorld.player.character.getBody().getPosition().y + 1, gameWorld.minCameraY + 1)),
	            0);
	    
		gameWorld.backgroundCamera.position.set(Runner.SCREEN_WIDTH/2/PPM, Runner.SCREEN_HEIGHT/2/PPM, 0); 
//		camera.zoom = 4f;
//		gameWorld.backgroundCamera.zoom = 4f;
    	camera.update();
    	cullingArea.set(camera.position.x - camera.viewportWidth * camera.zoom / 2, camera.position.y - camera.viewportHeight * camera.zoom / 2, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
    	
    	tiledMapRenderer.setView(camera);
    	//Logger.log(this, "viewbound x: " + tiledMapRenderer.getViewBounds().x + " width: " + tiledMapRenderer.getViewBounds().width);

//    	gameWorld.getBackgroundStage().getRoot().setCullingArea(cullingArea);
		gameWorld.getBackgroundStage().getViewport().update(currentScreenWidth, currentScreenHeight);
		Logger.log(this, "Z renderera: " + currentScreenWidth + " " + gameWorld.getBackgroundStage().getViewport().getScreenWidth());
		//((SpriteBatch)gameWorld.getBackgroundStage().getBatch()).disableBlending();
    	gameWorld.getBackgroundStage().draw();
    	//Logger.log(this, "backgroudnStage rendercalls: " + ((SpriteBatch)gameWorld.getBackgroundStage().getBatch()).renderCalls); // -4
    	
    	tiledMapRenderer.renderFrontLayer();
		Batch batch = gameWorld.getWorldStage().getBatch();
		if (batch != null) {
			batch.begin();
			gameWorld.worldBackgroundGroup.draw(batch, 1);
			batch.end();
			//Logger.log(this, "bacgroundgroup rendercalls: " + ((SpriteBatch)gameWorld.worldStage.getBatch()).renderCalls);// -0
		}
		//((SpriteBatch)tiledMapRenderer.getSpriteBatch()).disableBlending();
    	tiledMapRenderer.render();
    	//Logger.log(this, "tiledMapRenderer rendercalls: " + ((SpriteBatch)tiledMapRenderer.getSpriteBatch()).renderCalls); //-1
    	
    	gameWorld.worldStage.getViewport().update(currentScreenWidth, currentScreenHeight);
    	//((SpriteBatch)gameWorld.worldStage.getBatch()).disableBlending();
    	
    	

    	gameWorld.worldStage.getRoot().setCullingArea(cullingArea);
    	gameWorld.worldStage.draw();

    	//debugRenderer.render(gameWorld.world, camera.combined);

    	
    	//œwiat³a powoduja spadek wydajnosci
    	/*
    	if(gameWorld.rayHandler != null)
    	{
    		gameWorld.rayHandler.setCombinedMatrix(camera.combined);
    		gameWorld.rayHandler.updateAndRender();
    	}
    	*/
    }
}
