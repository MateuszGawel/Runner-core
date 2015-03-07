package com.apptogo.runner.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectActor extends Image {

	private ParticleEffect effect;
	private boolean started; 
	public boolean removeAfterComplete;
	private boolean pooled;
	
	public ParticleEffectPool particleEffectPool;
	Array<PooledEffect> pooledEffects = new Array<PooledEffect>();
	
	public ParticleEffectActor(String particleName, TextureAtlas atlas) {
		super();
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("gfx/game/particles/" + particleName), atlas);	
	}
	public ParticleEffectActor(String particleName, int initialPoolCapacity, int maxPool, int initialValue, float effectScale, TextureAtlas atlas){
		ParticleEffect tempEffect = new ParticleEffect();	
		tempEffect.load(Gdx.files.internal("gfx/game/particles/" + particleName), atlas);
		tempEffect.scaleEffect(effectScale);
		particleEffectPool = new ParticleEffectPool(tempEffect, initialPoolCapacity, maxPool);
		pooled = true;
		Array<PooledEffect> tempArray = new Array<PooledEffect>();
		for (int i=0; i<initialValue; i++){
			PooledEffect effect = particleEffectPool.obtain();
			tempArray.add(effect);
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
	
	public void allowCompletion(){
		effect.allowCompletion();
	}
	
	public void disallowCompletion(){
		effect.disallowCompletion();
	}
	
	public boolean isStarted(){ return started; }
	
	public void reset()
	{
		stop();
		effect.reset();
	}
	
	public boolean isComplete()
	{
		return (!effect.isComplete() && !started) || (effect.isComplete() && started);
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
		//long startTime = System.nanoTime();
		for (int i = pooledEffects.size - 1; i >= 0; i--) {
		    PooledEffect effect = pooledEffects.get(i);
		    effect.draw(batch);
		    if (effect.isComplete()) {
		        effect.free();
		        pooledEffects.removeIndex(i);
		    }
		}
		//long endTime = System.nanoTime();
		//Logger.log(this, "draw PARTICLI: " + (endTime - startTime));
	}
	
	public ParticleEffect getEffect(){ return this.effect; }
}
