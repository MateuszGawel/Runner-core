package com.apptogo.runner.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ParticleEffectActor extends Image {

	private ParticleEffect effect;
	private boolean started; 
	
	public ParticleEffectActor(String particleName) {
		super();
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("gfx/game/particles/" + particleName), Gdx.files.internal("gfx/game/particles"));
	}

	@Override
	public void scaleBy(float scaleFactor){
		effect.scaleEffect(scaleFactor);
	}
	
	@Override
	public void setPosition(float x, float y){
		super.setPosition(x, y);
		effect.setPosition(x, y);
	}
	
	public void start() {
		if(!started)effect.start();
		started = true;
	}
	
	public void stop() {
		started = false;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(started)
			effect.update(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(started)
			effect.draw(batch);
	}
	
	public ParticleEffect getEffect(){ return this.effect; }
}
