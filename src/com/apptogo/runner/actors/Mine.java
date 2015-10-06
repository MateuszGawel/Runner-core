package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Mine extends Actor{

	private GameWorld gameWorld;
	private Character characterOwner;
	private AtlasRegion currentFrame;
	private TextureAtlas atlas;
	private CustomAction move;
	private ParticleEffectActor explosion;
	
	public Mine(World world, GameWorld gameWorld, Character characterOwner){
		this.characterOwner = characterOwner;
		this.gameWorld = gameWorld;
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
		currentFrame = atlas.findRegion("mine");
		setPosition(characterOwner.getX()+0.5f, characterOwner.getY()+1f);
		setSize(currentFrame.getRegionWidth()/PPM, currentFrame.getRegionHeight()/PPM);
		gameWorld.getWorldStage().addActor(this);	
		explosion = new ParticleEffectActor("explosion_lvl2.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		gameWorld.getWorldStage().addActor(explosion);
		
		move = new CustomAction(0.01f, 10, characterOwner) {
			float offsetX=0.5f, offsetY=1f;
			@Override
			public void perform() {
				Character character = (Character)args[0];
				
				if(getLoopCount() < 5)
					offsetX+=0.2f;
				else
					offsetX-=0.2f;
				
				offsetY-=0.15f;
				setPosition(character.getX()+offsetX, character.getY()+offsetY);
			}
			
			@Override
			public void onFinish(){
				explosion.obtainAndStart(getX(), getY(), 0);
				remove();
			}
			
		};
		CustomActionManager.getInstance().registerAction(move);
	}
	
    
    @Override
    public void act(float delta){
    	super.act(delta);
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);	
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
