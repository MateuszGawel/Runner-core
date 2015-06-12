package com.apptogo.runner.screens;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.actors.Emoticon;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.EmoticonType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WaitingRoomScreen extends BaseScreen
{			
	private Button backButton;

	private LinkedHashMap<Player, Group> players;
	
	TextButton sign;
	Player tmp2;
	
	public WaitingRoomScreen(Runner runner)
	{
		super(runner);	
	}
	
	public void prepare() 
	{
		setBackground("mainMenuScreenBackground");
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
        });
        
        sign = new TextButton("waiting", skin, "signboard");
        sign.getLabel().addAction( this.getBlinkAction(1, 0.5f, 0.5f) );
        setCenterPosition(sign, 200);
        
        players = new LinkedHashMap<Player, Group>();
        
        Group playerGroup = createGroup( player );
        
        Player tmp = new Player();
        tmp.setCharacterType( CharacterType.ALIEN );
        tmp.setName("MARCIN");
        
        tmp2 = new Player();
        tmp2.setCharacterType( CharacterType.ARCHER );
        tmp2.setName("TOMASZ");
        
        Player tmp3 = new Player();
        tmp3.setCharacterType( CharacterType.BANDIT );
        tmp3.setName("PAVELOO");
        
        players.put(player, playerGroup);
        players.put(tmp, createGroup( null ));
        players.put(tmp2, createGroup( tmp2 ));
        players.put(tmp3, createGroup( tmp3 ));
                
        addToScreen(backButton);
        addToScreen(sign);
        
        refreshPlayers();
        
        addToScreen(getButtons());
	}
	
	public Group getButtons()
	{
		Group buttons = new Group();
		
		int count = EmoticonType.values().length;
		float width = (new TextButton("", skin, "westWildCampaignButton")).getWidth();
		
		for(int i = 0; i < EmoticonType.values().length; i++)
		{
			float x = -(count * width + (count-1) * 80)/2f + i * width + i * 80;
			
			EmoticonType emoticonType = EmoticonType.values()[i];
			
			final TextButton button = new TextButton("", skin, "westWildCampaignButton");
			button.setPosition(x, -350);
			
			button.addListener(getEmoticonListener(emoticonType));
			
			final Image image = EmoticonType.convertToImage(emoticonType);
			image.setPosition(x + (button.getWidth() - image.getWidth()) / 2f, -350 + (button.getHeight() - image.getHeight()) / 2f);
			image.setTouchable(Touchable.disabled);
			
			buttons.addActor(button);
			buttons.addActor(image);
		}
		
		return buttons;
	}
	
	private ClickListener getEmoticonListener(final EmoticonType emoticonType)
	{
		ClickListener listener = new ClickListener(){
            public void clicked(InputEvent event, float x, float y) 
            {
            	for(Actor actor : players.get(tmp2).getChildren())
            	{
            		if(actor instanceof Emoticon)
            		{
            			((Emoticon) actor).show(emoticonType);
            		}
            	}
            }
        };
		
		return listener;
	}
		
	private void refreshPlayers()
	{
		int counter = 0;
		
		for(Entry<Player, Group> entry : players.entrySet()) 
		{
			entry.getValue().remove();
			
			entry.getValue().setPosition(-440 + (counter*250), -100);
			
			addToScreen(entry.getValue());
			
			counter++;
		}
	}
	
	private Group createGroup(Player p)
	{
		Group group = new Group();
		
		if( p != null)
		{	
			Animation characterAnimation = CharacterType.convertToCharacterAnimation(p.getCharacterType(), true);
			
			if( p.getCharacterType() == CharacterType.BANDIT)
			{
				characterAnimation.setPosition(-30, 35);
			}
			else if( p.getCharacterType() == CharacterType.ARCHER)
			{
				characterAnimation.setPosition(-38, 41);
			}
			else //ALIEN
			{
				characterAnimation.setPosition(-47, 38);
			}
			
	        Image ground = createImage( CharacterType.convertToGroundPath( p.getCharacterType() ) , 0, 0);
	        
	        Label name = new Label(p.getName(), skin, "medium");
	        name.setPosition(ground.getWidth() / 2.0f - name.getWidth() / 2.0f, -65);
	        	        
	        Emoticon emoticon = new Emoticon();
	        emoticon.setPosition((ground.getWidth() - emoticon.getWidth()) / 2f, 220);
	        
	        group.addActor(ground);
	        group.addActor(characterAnimation);
	        group.addActor(name);
	        group.addActor(emoticon);
		}
		else
		{
			Image none = this.createImage("none", 15, 30);
			
			Label name = new Label("waiting", skin, "medium");
	        name.setPosition(none.getWidth() / 2.0f - name.getWidth() / 2.0f + none.getX() / 2.0f, -65);
			
			group.addActor(none);
			group.addActor(name);
			
			group.addAction( this.getBlinkAction(1, 0.5f, 0.5f) );
		}

        return group;
	}
	
	public void step()
	{
		handleInput();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_WAITING_ROOM;
	}
}
