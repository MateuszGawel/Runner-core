package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
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
	private Character character;
	public ParticleEffectPool particleEffectPool;
	private String particleName;
	private Array<PooledEffect> pooledEffects = new Array<PooledEffect>();
	
	public ParticleEffectActor(String particleName, TextureAtlas atlas) {
		super();
		this.particleName = particleName;
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("gfx/game/particles/" + particleName), atlas);	
	}
	public ParticleEffectActor(String particleName, int initialPoolCapacity, int maxPool, int initialValue, float effectScale, TextureAtlas atlas){
		ParticleEffect tempEffect = new ParticleEffect();
		this.particleName = particleName;
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
		super.setPosition(posX-Runner.SCREEN_WIDTH/PPM, posY);
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
		onReset();
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
		    
		    if(character!=null && (character.getBody().getPosition().x - 10 > effect.getEmitters().get(0).getX() || character.getBody().getPosition().x + 15 < effect.getEmitters().get(0).getX())){
		    	effect.free();
		        pooledEffects.removeIndex(i);
		    }
		}
	}

	public void onReset(){
		
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
	public void setCharacter(Character character) {
		this.character = character;
	}
	public Character getCharacter() {
		return character;
	}
}
