package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.SpaceWorld;
import com.apptogo.runner.world.WildWestWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class GameProgressBar extends Group{

	private TextureRegion barTextureBegin, barTextureMiddle, barTextureEnd;
	private GameWorld gameWorld;
	private Actor bar;
	private Array<Character> characters;
	float percent;
	private float actorWidth;
	private TextureAtlas atlas;
	
	public GameProgressBar(GameWorld gameWorld){
		
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/gameGuiAtlas.pack");
		this.setTransform(false);
		if(gameWorld instanceof WildWestWorld){
			this.barTextureBegin = atlas.findRegion("wildwestProgressBarBegin");
			this.barTextureMiddle = atlas.findRegion("wildwestProgressBarMiddle");
			this.barTextureEnd = atlas.findRegion("wildwestProgressBarEnd");
		}
		else if(gameWorld instanceof ForestWorld){
			this.barTextureBegin = atlas.findRegion("forestProgressBarBegin");
			this.barTextureMiddle = atlas.findRegion("forestProgressBarMiddle");
			this.barTextureEnd = atlas.findRegion("forestProgressBarEnd");
		}
		else if(gameWorld instanceof SpaceWorld){
			this.barTextureBegin = atlas.findRegion("planetProgressBarBegin");
			this.barTextureMiddle = atlas.findRegion("planetProgressBarMiddle");
			this.barTextureEnd = atlas.findRegion("planetProgressBarEnd");
		}

		this.gameWorld = gameWorld;
		actorWidth = barTextureBegin.getRegionWidth() + barTextureMiddle.getRegionWidth()*10 + barTextureEnd.getRegionWidth();
		setSize(actorWidth, 20);
		
		setPosition(Runner.SCREEN_WIDTH/2 - getWidth()/2, Runner.SCREEN_HEIGHT - 40);
		
		
		this.characters = new Array<Character>();
		characters.add(gameWorld.player.character);
		for(Player player : gameWorld.enemies){
			characters.add(player.character);
		}
		//barTextureMiddle.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		bar = new Actor(){
			@Override
			public void draw(Batch batch, float parentAlpha){
				
				for(int i=0; i<10; i++){
					batch.draw(barTextureMiddle, getX() + barTextureBegin.getRegionWidth() + barTextureMiddle.getRegionWidth()*i, getY(), barTextureMiddle.getRegionWidth(), barTextureMiddle.getRegionHeight());
				}
				batch.draw(barTextureBegin, getX(), getY(), barTextureBegin.getRegionWidth(), barTextureBegin.getRegionHeight());
				batch.draw(barTextureEnd, getX() + getWidth() - barTextureEnd.getRegionWidth(), getY(), barTextureEnd.getRegionWidth(), barTextureEnd.getRegionHeight());
			}
		};
		bar.setSize(getWidth(), getHeight());
		bar.setPosition(0, 0);
		addActor(bar);
		
		
		for(Character character : characters){
			switch(character.getCharacterType()){
			case BANDIT:
				addActor(new GameProgressBarHead(atlas.findRegion("banditProgressBarHead"), gameWorld, character, getWidth(), getHeight()));
				break;
			case ARCHER:
				addActor(new GameProgressBarHead(atlas.findRegion("archerProgressBarHead"), gameWorld, character, getWidth(), getHeight()));
				break;
			case ALIEN:
				addActor(new GameProgressBarHead(atlas.findRegion("alienProgressBarHead"), gameWorld, character, getWidth(), getHeight()));
				break;
			default:
				break;
			}	
		}
		
	}
}
