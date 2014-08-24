package com.apptogo.runner.screens;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MultiplayerScreen extends BaseScreen
{			
	private TextButton createRoomButton;
    private TextButton joinRandomButton;
	private TextButton backButton;
	
	private Texture currentHeadTexture;
	private Image currentHead;
	
	public MultiplayerScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
	}
	
	@Override
	public void prepare()
	{		
		setBackground("ui/menuBackgrounds/mainMenuScreenBackground.png");
				
		backButton = new TextButton( "<--", skin, "default");
        backButton.setSize(200f, 100f);
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
        createRoomButton = new TextButton( "create room", skin, "default");
        createRoomButton.setPosition( -(createRoomButton.getWidth() / 2.0f), 200.0f );
		
		joinRandomButton = new TextButton( "random room", skin, "default");
		joinRandomButton.setPosition( -(joinRandomButton.getWidth() / 2.0f), 0.0f );
		
		final SelectBox<String> selectCharacter = new SelectBox<String>(skin, "default");
        selectCharacter.setSize(250f, 50f);
        selectCharacter.setPosition(-500.0f, -220.0f);
        selectCharacter.setItems(new String[]{ CharacterType.BANDIT.toString(), CharacterType.ARCHER.toString(), CharacterType.ALIEN.toString() });
        selectCharacter.setSelected( player.getCurrentCharacter().toString() );
        
        selectCharacter.addListener( new ChangeListener()
        {
			public void changed(ChangeListener.ChangeEvent event, Actor actor)
            {
				setCurrentHead( (String)selectCharacter.getSelected() );
            }
		});
         
        setCurrentHead( player.getCurrentCharacter().toString() );
        
		addToScreen(createRoomButton);
		addToScreen(joinRandomButton);
		addToScreen(backButton);
		
		addToScreen(selectCharacter);
	}
	
	public void step()
	{
		if ( Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE) )
		{
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		}
	}
	
	private void setCurrentHead(String selectedCharacterString)
	{
		currentHeadTexture = new Texture( CharacterType.convertToCharacterHead( selectedCharacterString ) );
		
		player.setCurrentCharacter(CharacterType.parseFromString( selectedCharacterString ));
		SaveManager.getInstance().savePlayer(player);
		
		if( currentHead != null )
		{
			currentHead.clear();
			currentHead.remove();
			currentHead.setVisible(false);
		}

		currentHead = new Image( currentHeadTexture );
		
		currentHead.setSize(150.0f, 150.0f); //one powinny byc po prostu w tym rozmiarze
        currentHead.setPosition(-450.0f, -380.0f);
        currentHead.setUserObject("currentHeadImage");
        
        currentHead.setVisible(true);
        addToScreen(currentHead);
        
        Logger.log(this, stage.getActors().size);
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
		currentHeadTexture.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_MULTIPLAYER;
	}


}
