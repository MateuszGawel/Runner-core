package com.apptogo.runner.screens;

import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class ParticleScreen implements Screen
{
	ParticleEffectActor effectActor;
	Stage particleStage;

	
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		particleStage.act(delta);
		particleStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		particleStage.getViewport().update(width, height, true);
		
	}

	@Override
	public void show() {

		effectActor = new ParticleEffectActor("test.p");
		effectActor.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		particleStage = new Stage(new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT));
		
		particleStage.addActor(effectActor);
		effectActor.start();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
