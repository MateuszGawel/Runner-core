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

public class BackgroundRenderer extends Actor{
	private ShapeRenderer shapeRenderer;
	private Color lightBlue, brown, lightBrown;
	private int currentWidth=0, currentHeight=0;
	
	public BackgroundRenderer() {
		shapeRenderer = new ShapeRenderer();
		lightBlue = new Color(0.823f, 0.960f, 1, 1);
		brown = new Color(0.576f, 0.349f, 0.247f, 1);
		lightBrown = new Color(0.855f, 0.639f, 0.321f, 1);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(currentWidth==0 && currentHeight==0 && ScreensManager.getInstance().getCurrentScreen() instanceof GameScreen){
			currentWidth = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).worldRenderer.currentScreenWidth;
			currentHeight = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).worldRenderer.currentScreenHeight;
		}
		batch.end();
			shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.rect(0, currentHeight*2/3-200, currentWidth, currentHeight/3+200, Color.WHITE, Color.WHITE, lightBlue, lightBlue);
				shapeRenderer.rect(0, currentHeight*1/3-200, currentWidth, currentHeight/3+100, brown, brown, brown, brown);
				shapeRenderer.rect(0, 0, currentWidth, currentHeight/3-200, lightBrown, lightBrown, lightBrown, lightBrown);
			shapeRenderer.end();
		batch.begin();
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
	}

}
