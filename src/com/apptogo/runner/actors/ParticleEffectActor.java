package com.apptogo.runner.actors;

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
	public ParticleEffectActor(String particleName, int initialPoolCapacity, int maxPool){
		this(particleName);
		particleEffectPool = new ParticleEffectPool(effect, 1, 2);
		pooled = true;
	}

	public void obtainAndStart(float posX, float posY, float scale){
		PooledEffect pooledEffect = particleEffectPool.obtain();
		pooledEffect.setPosition(posX, posY);
		super.setPosition(posX, posY);
		pooledEffect.scaleEffect(scale);
		pooledEffects.add(pooledEffect);
		pooledEffect.start();
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
		if(effect.isComplete() && removeAfterComplete){
			effect.dispose();
			this.remove();
		}
		
		//dla poola
		for (int i = pooledEffects.size - 1; i >= 0; i--) {
		    PooledEffect effect = pooledEffects.get(i);
		    effect.update(delta);
		    if (effect.isComplete()) {
		        effect.free();
		        pooledEffects.removeIndex(i);
		    }
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
