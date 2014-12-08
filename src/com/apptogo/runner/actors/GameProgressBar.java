package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class GameProgressBar extends Group{

	private Texture barTexture;
	private GameWorld gameWorld;
	private Actor bar;
	private Array<Character> characters;
	float percent;
	
	public GameProgressBar(String textureName, GameWorld gameWorld){
		this.barTexture = (Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), textureName);
		this.gameWorld = gameWorld;
		setSize(Runner.SCREEN_WIDTH - 100, 17);
		setPosition(Runner.SCREEN_WIDTH/2 - getWidth()/2, Runner.SCREEN_HEIGHT - 20 - 17);
		
		
		this.characters = new Array<Character>();
		characters.add(gameWorld.player.character);
		for(Player player : gameWorld.enemies){
			characters.add(player.character);
		}
		
		
		barTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		bar = new Image(barTexture){
			@Override
			public void draw(Batch batch, float parentAlpha){
				super.draw(batch, parentAlpha); 
				batch.draw(barTexture, getX(), getY(), getWidth(), getHeight(), 0, 0, getWidth() / barTexture.getWidth(), 1);
			}
		};
		bar.setSize(getWidth(), getHeight());
		bar.setPosition(0, 0);
		addActor(bar);
		
		
		for(Character character : characters){
			switch(character.getCharacterType()){
			case BANDIT:
				addActor(new GameProgressBarHead(((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/banditHead.png")), gameWorld, character, getWidth(), getHeight()));
				break;
			case ARCHER:
				addActor(new GameProgressBarHead(((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/archerHead.png")), gameWorld, character, getWidth(), getHeight()));
				break;
			case ALIEN:
				addActor(new GameProgressBarHead(((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/alienHead.png")), gameWorld, character, getWidth(), getHeight()));
				break;
			default:
				break;
			}	
		}
		
	}
}
