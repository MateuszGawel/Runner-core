package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class Parachute extends Actor{

	private GameWorld gameWorld;
	private Character characterOwner;
	private AtlasRegion mineRegion, parachuteRegion, currentRegion;
	private TextureAtlas atlas;
	private CustomAction moveMine, smallBombsSpawn;
	private ParticleEffectActor explosion;
	private boolean parachuteState;
	private Vector2 fallingForce;
	private Pool<SmallBomb> smallBombsPool;
	
	public Parachute(World world, GameWorld gameWorld, Character characterOwner, Pool<SmallBomb> smallBombsPool){
		this.characterOwner = characterOwner;
		this.gameWorld = gameWorld;
		this.smallBombsPool = smallBombsPool;
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
		mineRegion = atlas.findRegion("mine");
		parachuteRegion = atlas.findRegion("parachute");
		currentRegion = mineRegion;
		setPosition(characterOwner.getX()+0.5f, characterOwner.getY()+1f);
		setSize(mineRegion.getRegionWidth()/PPM, mineRegion.getRegionHeight()/PPM);
		gameWorld.getWorldStage().addActor(this);	
		explosion = new ParticleEffectActor("explosion_lvl2.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		gameWorld.getWorldStage().addActor(explosion);
		fallingForce = new Vector2(200f, 3500f);
		
		moveMine = new CustomAction(0.01f, 10, characterOwner) {
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
				setVisible(false);
				((Character)args[0]).jump(1.3f, 1.4f, 1, 1);
				parachuteState = true;
			}
			
			
			
		};
		smallBombsSpawn = new CustomAction(0.2f, 0, characterOwner, smallBombsPool) {
			@Override
			public void perform() {
				Character character = (Character)args[0];
				SmallBomb smallBomb = ((Pool<SmallBomb>)args[1]).obtain();
				smallBomb.init(character, 1);
				if(character.animationManager.getCurrentAnimationState()!=CharacterAnimationState.FLYABILITY)
					character.animationManager.setCurrentAnimationState(CharacterAnimationState.FLYABILITY);
			}
		};
		
		CustomActionManager.getInstance().registerAction(moveMine);
	}
	
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(parachuteState && characterOwner.getBody().getLinearVelocity().y < 0.5f && !characterOwner.flags.isCanLand()){
    		setVisible(true);
    		currentRegion = parachuteRegion;
    		setSize(parachuteRegion.getRegionWidth()/PPM, parachuteRegion.getRegionHeight()/PPM);
    		setPosition(characterOwner.getX()-1.3f, characterOwner.getY()+0.3f);
    		characterOwner.getBody().applyForceToCenter(fallingForce, true);
    		if(!smallBombsSpawn.isRegistered())
    			CustomActionManager.getInstance().registerAction(smallBombsSpawn);
    	}
    	else if(characterOwner.flags.isCanLand() && characterOwner.flags.isAlive()){
    		remove();
    		smallBombsSpawn.setFinished(true);
    	}
    	if(!characterOwner.flags.isAlive()){
    		moveMine.setFinished(true);
    		smallBombsSpawn.setFinished(true);
    		remove();
    	}
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);	
		batch.draw(currentRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
