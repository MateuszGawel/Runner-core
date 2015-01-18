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
	
	public MyTiledMapRenderer(TiledMap map, float unitScale) {
		super(map, unitScale);
	}
	
	
	public void renderFrontLayer() {
		beginRender();
		for (MapLayer layer : map.getLayers()) {
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer && layer.getName().equals("FrontLayer")) {
					renderTileLayer((TiledMapTileLayer)layer);
				}
			}
		}
		endRender();
	}
	//sprobowac to wrzycic do render()
	
	@Override
	public void render () {
		beginRender();
		for (MapLayer layer : map.getLayers()) {
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer) {
					if(!layer.getName().equals("FrontLayer"))
						//Logger.log(this, "szerokosc " + layer.getName() + " wynosi: " + ((TiledMapTileLayer)layer).getTileWidth() * getUnitScale());
						renderTileLayer((TiledMapTileLayer)layer);
				} else {
					for (MapObject object : layer.getObjects()) {
						renderObject(object);
					}
				}
			}
		}
		endRender();
	}

}
