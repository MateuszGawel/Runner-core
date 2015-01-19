package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.screens.GameScreen;
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
    	long startTime = System.nanoTime();
		
    	super.act(delta);
		if(started && !pooled)
			effect.update(delta);
		if(!pooled && effect.isComplete() && removeAfterComplete){
			effect.dispose();
			this.remove();
		}
		
		//dla poola
		//long startTime = System.nanoTime();
		for (int i = pooledEffects.size - 1; i >= 0; i--) {
		    PooledEffect effect = pooledEffects.get(i);
		    effect.update(delta);
		}
		//long endTime = System.nanoTime();
		//Logger.log(this, "ACT PARTICLI: " + (endTime - startTime));
    	
        long endTime = System.nanoTime();
        if(ScreensManager.getInstance().getCurrentScreen() instanceof GameScreen)
        if(((GameScreen)ScreensManager.getInstance().getCurrentScreen()).world.particleEffectActorArray != null)
        ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).world.particleEffectActorArray.add(endTime - startTime);
		
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
