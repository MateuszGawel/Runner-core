package com.apptogo.runner.handlers;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.actors.Character;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class MyTiledMapRenderer extends OrthogonalTiledMapRenderer{

	 Array<Character> playerLayer;
	
	public MyTiledMapRenderer(TiledMap map, float unitScale, Array<Character> playerLayer) {
		super(map, unitScale);
		this.playerLayer = playerLayer; 
	}
	
	@Override
	public void render () {
		beginRender();
		for (MapLayer layer : map.getLayers()) {
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer) {
					renderTileLayer((TiledMapTileLayer)layer);
				} else {
					if(layer.getName().equals("PlayerLayer")){
						for(Character character : playerLayer){
							character.draw(getSpriteBatch(), 1);
						}
					}
					for (MapObject object : layer.getObjects()) {
						renderObject(object);
					}
				}
			}
		}
		endRender();
	}

}
