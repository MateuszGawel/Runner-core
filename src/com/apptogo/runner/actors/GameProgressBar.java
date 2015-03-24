package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.SpaceWorld;
import com.apptogo.runner.world.WildWestWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class GameProgressBar extends Group{

	private TextureRegion barTextureBegin, barTextureMiddle, barTextureEnd;
	private GameWorld gameWorld;
	private Actor bar;
	private Array<Character> characters;
	float percent;
	private float actorWidth;

	public GameProgressBar(GameWorld gameWorld){
		
		this.setTransform(false);
		if(gameWorld instanceof WildWestWorld){
			this.barTextureBegin = ResourcesManager.getInstance().getAtlasRegion("wildwestProgressBarBegin");
			this.barTextureMiddle = ResourcesManager.getInstance().getAtlasRegion("wildwestProgressBarMiddle"); 
			this.barTextureEnd = ResourcesManager.getInstance().getAtlasRegion("wildwestProgressBarEnd"); 
		}
		else if(gameWorld instanceof ForestWorld){
			this.barTextureBegin = ResourcesManager.getInstance().getAtlasRegion("forestProgressBarBegin");
			this.barTextureMiddle = ResourcesManager.getInstance().getAtlasRegion("forestProgressBarMiddle");
			this.barTextureEnd = ResourcesManager.getInstance().getAtlasRegion("forestProgressBarEnd");
		}
		else if(gameWorld instanceof SpaceWorld){
			this.barTextureBegin = ResourcesManager.getInstance().getAtlasRegion("planetProgressBarBegin");
			this.barTextureMiddle = ResourcesManager.getInstance().getAtlasRegion("planetProgressBarMiddle");
			this.barTextureEnd = ResourcesManager.getInstance().getAtlasRegion("planetProgressBarEnd");
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
				addActor(new GameProgressBarHead(ResourcesManager.getInstance().getAtlasRegion("banditProgressBarHead"), gameWorld, character, getWidth(), getHeight()));
				break;
			case ARCHER:
				addActor(new GameProgressBarHead(ResourcesManager.getInstance().getAtlasRegion("archerProgressBarHead"), gameWorld, character, getWidth(), getHeight()));
				break;
			case ALIEN:
				addActor(new GameProgressBarHead(ResourcesManager.getInstance().getAtlasRegion("alienProgressBarHead"), gameWorld, character, getWidth(), getHeight()));
				break;
			default:
				break;
			}	
		}
		
	}
}
