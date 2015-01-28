package com.apptogo.runner.handlers;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyTiledMapRendererActorFrontLayer extends Actor{

	private MyTiledMapRenderer renderer;
	
	public MyTiledMapRendererActorFrontLayer(MyTiledMapRenderer renderer) {
		this.renderer = renderer;
		setWidth(TiledMapLoader.getInstance().getMapSize().x/PPM);
		setHeight(TiledMapLoader.getInstance().getMapSize().y/PPM);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		renderer.renderFrontLayer();    	
	}

}
