package com.apptogo.runner.world;

import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.GameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ForestBackgroundRenderer extends Actor{
	private ShapeRenderer shapeRenderer;
	private Color lightGreen, darkGreen;
	private int currentWidth=0, currentHeight=0;
	
	public ForestBackgroundRenderer() {
		shapeRenderer = new ShapeRenderer();
		lightGreen = new Color(0.13f, 0.32f, 0.09f, 1);
		darkGreen = new Color(0.09f, 0.23f, 0.06f, 1);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(currentWidth==0 && currentHeight==0 && ScreensManager.getInstance().getCurrentScreen() instanceof GameScreen){
			currentWidth = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).worldRenderer.currentScreenWidth;
			currentHeight = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).worldRenderer.currentScreenHeight;
		}
		batch.end();
			shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.rect(0, 0, currentWidth, currentHeight, darkGreen, darkGreen, lightGreen, lightGreen);
			shapeRenderer.end();
		batch.begin();
	}
}
