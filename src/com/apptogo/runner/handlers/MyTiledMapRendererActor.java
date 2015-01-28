package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyTiledMapRendererActor extends Actor{

	private MyTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	public MyTiledMapRendererActor(MyTiledMapRenderer renderer, OrthographicCamera camera) {
		this.renderer = renderer;
		this.camera = camera;
		setWidth(TiledMapLoader.getInstance().getMapSize().x/PPM);
		setHeight(TiledMapLoader.getInstance().getMapSize().y/PPM);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
    	renderer.setView(camera);
		renderer.render();    	
	}

}
