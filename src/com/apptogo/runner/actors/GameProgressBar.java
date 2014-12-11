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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class GameProgressBar extends Group{

	private Texture barTextureBegin, barTextureMiddle, barTextureEnd;
	private GameWorld gameWorld;
	private Actor bar;
	private Array<Character> characters;
	float percent;
	
	public GameProgressBar(GameWorld gameWorld){
		
		
		if(gameWorld instanceof WildWestWorld){
			this.barTextureBegin = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/wildwestProgressBarBegin.png");
			this.barTextureMiddle = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/wildwestProgressBarMiddle.png");
			this.barTextureEnd = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/wildwestProgressBarEnd.png");
		}
		else if(gameWorld instanceof ForestWorld){
			this.barTextureBegin = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/forestProgressBarBegin.png");
			this.barTextureMiddle = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/forestProgressBarMiddle.png");
			this.barTextureEnd = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/forestProgressBarEnd.png");
		}
		else if(gameWorld instanceof SpaceWorld){
			this.barTextureBegin = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/planetProgressBarBegin.png");
			this.barTextureMiddle = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/planetProgressBarMiddle.png");
			this.barTextureEnd = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/planetProgressBarEnd.png");
		}

		this.gameWorld = gameWorld;
		setSize(Runner.SCREEN_WIDTH - 100, 20);
		setPosition(Runner.SCREEN_WIDTH/2 - getWidth()/2, Runner.SCREEN_HEIGHT - 40);
		
		
		this.characters = new Array<Character>();
		characters.add(gameWorld.player.character);
		for(Player player : gameWorld.enemies){
			characters.add(player.character);
		}
		
		
		barTextureMiddle.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		bar = new Image(barTextureMiddle){
			@Override
			public void draw(Batch batch, float parentAlpha){
				//super.draw(batch, parentAlpha); 
				batch.draw(barTextureMiddle, getX() + barTextureBegin.getWidth(), getY(), getWidth()  - 2*barTextureBegin.getWidth(), getHeight(), 0, 0, getWidth() / barTextureMiddle.getWidth(), 1);
				batch.draw(barTextureBegin, getX(), getY(), barTextureBegin.getWidth(), barTextureBegin.getHeight());
				batch.draw(barTextureEnd, getWidth() - barTextureEnd.getWidth(), getY(), barTextureEnd.getWidth(), barTextureEnd.getHeight());
			}
		};
		bar.setSize(getWidth(), getHeight());
		bar.setPosition(0, 0);
		addActor(bar);
		
		
		for(Character character : characters){
			switch(character.getCharacterType()){
			case BANDIT:
				addActor(new GameProgressBarHead(((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/banditProgressBarHead.png")), gameWorld, character, getWidth(), getHeight()));
				break;
			case ARCHER:
				addActor(new GameProgressBarHead(((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/archerProgressBarHead.png")), gameWorld, character, getWidth(), getHeight()));
				break;
			case ALIEN:
				addActor(new GameProgressBarHead(((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/alienProgressBarHead.png")), gameWorld, character, getWidth(), getHeight()));
				break;
			default:
				break;
			}	
		}
		
	}
}
