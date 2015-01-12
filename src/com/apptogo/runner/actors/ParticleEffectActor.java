package com.apptogo.runner.actors;

import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectActor extends Image {

	private ParticleEffect effect;
	private boolean started; 
	public boolean removeAfterComplete;
	private boolean pooled;
	
	public ParticleEffectPool particleEffectPool;
	Array<PooledEffect> pooledEffects = new Array<PooledEffect>();
	
	public ParticleEffectActor(String particleName) {
		super();
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("gfx/game/particles/" + particleName), Gdx.files.internal("gfx/game/particles"));	
	}
	public ParticleEffectActor(String particleName, int initialPoolCapacity, int maxPool, int initialValue, float effectScale){
		ParticleEffect tempEffect = new ParticleEffect();	
		tempEffect.load(Gdx.files.internal("gfx/game/particles/" + particleName), Gdx.files.internal("gfx/game/particles"));
		tempEffect.scaleEffect(effectScale);
		particleEffectPool = new ParticleEffectPool(tempEffect, initialPoolCapacity, maxPool);
		pooled = true;
		int ctr = 0;
		Array<PooledEffect> tempArray = new Array<PooledEffect>();
		for (int i=0; i<initialValue; i++){
			PooledEffect effect = particleEffectPool.obtain();
			tempArray.add(effect);
			Logger.log(this, "tworze: " + ctr++);
		}
		particleEffectPool.freeAll(tempArray);
		
	}

	public void obtainAndStart(float posX, float posY){
		PooledEffect pooledEffect = particleEffectPool.obtain();
		pooledEffect.setPosition(posX, posY);
		super.setPosition(posX, posY);
		pooledEffects.add(pooledEffect);
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
		if(started && !pooled)
			effect.update(delta);
		if(!pooled && effect.isComplete() && removeAfterComplete){
			effect.dispose();
			this.remove();
		}
		
		//dla poola
		for (int i = pooledEffects.size - 1; i >= 0; i--) {
		    PooledEffect effect = pooledEffects.get(i);
		    effect.update(delta);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(started && !pooled)
			effect.draw(batch);
		
		//dla poola
		for (int i = pooledEffects.size - 1; i >= 0; i--) {
		    PooledEffect effect = pooledEffects.get(i);
		    effect.draw(batch);
		    if (effect.isComplete()) {
		        effect.free();
		        pooledEffects.removeIndex(i);
		    }
		}
	}
	
	public ParticleEffect getEffect(){ return this.effect; }
}
