package com.apptogo.runner.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyTiledMapRenderer extends OrthogonalTiledMapRenderer{
	
	public MyTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
		super(map, unitScale, batch);
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
