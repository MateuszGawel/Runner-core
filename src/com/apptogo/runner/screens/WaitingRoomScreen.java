package com.apptogo.runner.screens;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WaitingRoomScreen extends BaseScreen
{			
	private Button backButton;

	private LinkedHashMap<Player, Group> players;
	
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
        
        players = new LinkedHashMap<Player, Group>();
        
        Group playerGroup = createGroup( player );
        
        Player tmp = new Player();
        tmp.setCharacterType( CharacterType.ALIEN );
        tmp.setName("MARCIN");
        
        Player tmp2 = new Player();
        tmp2.setCharacterType( CharacterType.ARCHER );
        tmp2.setName("TOMASZ");
        
        Player tmp3 = new Player();
        tmp3.setCharacterType( CharacterType.BANDIT );
        tmp3.setName("PAVELOO");
        
        players.put(player, playerGroup);
        players.put(tmp, createGroup( null ));
        players.put(tmp2, createGroup( tmp2 ));
        players.put(tmp3, createGroup( tmp3 ));
        
        refreshPlayers();
        
        addToScreen(backButton);
        
        int y = -300;
        int margin = 75;
        
        Texture t1 = new Texture( Gdx.files.internal("gfx/e1.png") );
        t1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        final Image e1 = new Image( t1 );
        e1.setPosition(-32-64-64-margin-margin, y);
        
        e1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	showCloud(player, e1);
            }
        });
        
        Texture t2 = new Texture( Gdx.files.internal("gfx/e2.png") );
        t2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        final Image e2 = new Image( t2 );
        e2.setPosition(-32-64-margin, y);
               
        e2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	showCloud(player, e2);
            }
        });
        
        Texture t3 = new Texture( Gdx.files.internal("gfx/e3.png") );
        t3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        final Image e3 = new Image( t3 );
        e3.setPosition(-32, y);
        
        e3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	showCloud(player, e3);
            }
        });
        
        Texture t4 = new Texture( Gdx.files.internal("gfx/e4.png") );
        t4.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        final Image e4 = new Image( t4 );
        e4.setPosition(-32+64+margin, y);
        
        e4.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	showCloud(player, e4);
            }
        });
        
        Texture t5 = new Texture( Gdx.files.internal("gfx/e5.png") );
        t5.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        final Image e5 = new Image( t5 );
        e5.setPosition(-32+64+64+margin+margin, y);
        
        e5.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	showCloud(player, e5);
            }
        });
        
        addToScreen(e1);
        addToScreen(e2);
        addToScreen(e3);
        addToScreen(e4);
        addToScreen(e5);
	}
	
	private void showCloud(Player p, Image e)
	{
		Group g = players.get(p);
		
		for(Actor a : g.getChildren())
		{
			if(a.getUserObject() == "cloud")
			{	
				//Image e = new Image(ee.getDrawable());
				e.setPosition(g.getX() + a.getX() + a.getWidth() / 2.0f - e.getWidth() / 2.0f, g.getY() +  a.getY() + a.getHeight() / 2.0f - e.getHeight() / 2.0f);
				e.toFront();
				
				a.clearActions();
				a.getColor().a = 0;
				
				AlphaAction action1 = new AlphaAction();
				action1.setDuration(0.5f);
				action1.setAlpha(1);
				
				AlphaAction action2 = new AlphaAction();
				action2.setDuration(2);
				action2.setAlpha(1);
				
				AlphaAction action3 = new AlphaAction();
				action3.setDuration(0.5f);
				action3.setAlpha(0);

				SequenceAction s = new SequenceAction(action1, action2, action3);
				
				a.addAction(s);
				
				break;
			}
		}
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
	        
	        Texture t = new Texture( Gdx.files.internal("gfx/cloud.png") );
	        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	        
	        Image cloud = new Image( t );
	        cloud.setPosition(ground.getWidth() / 2.0f - cloud.getWidth() / 2.0f, 200);
	        cloud.getColor().a = 0;
	        cloud.setUserObject("cloud");
	        
	        group.addActor(ground);
	        group.addActor(characterAnimation);
	        group.addActor(name);
	        group.addActor(cloud);
		}
		else
		{
			Texture t = new Texture( Gdx.files.internal("gfx/none.png") );
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			Image none = new Image( t );
			none.setPosition(15, 30);				
			
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
