package com.apptogo.runner.screens;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.apptogo.runner.widget.Widget.WidgetType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerScreen extends BaseScreen
{			
	private TextButton createRoomButton;
	private TextButton joinRandomButton;
	private TextButton upgradeButton;
	
    private Button backButton;
	
    private Widget characterWidget;
    
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
				
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
        createRoomButton = new TextButton( "create room", skin, "default");
        createRoomButton.setPosition( -(createRoomButton.getWidth() / 2.0f), 0.0f );
		
		joinRandomButton = new TextButton( "random room", skin, "default");
		joinRandomButton.setPosition( -(joinRandomButton.getWidth() / 2.0f), -200.0f );
		
		characterWidget = new Widget(350.0f, Align.center, 750.0f, WidgetType.BIG, WidgetFadingType.RIGHT_TO_LEFT, true);
		characterWidget.setEasing( Interpolation.elasticOut );
		
		upgradeButton = new TextButton( "upgrade", skin, "default");
		upgradeButton.setSize(220.0f, 220.0f);
		upgradeButton.setPosition( 385.0f, -200.0f );
		upgradeButton.addListener( characterWidget.getToggleListener() );
		
		final SelectBox<String> selectCharacter = new SelectBox<String>(skin, "default");
        selectCharacter.setSize(250f, 50f);
        selectCharacter.setPosition(375.0f, 220.0f);
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
		
		characterWidget.addActor(selectCharacter);
		characterWidget.addActor(upgradeButton);
		//addToScreen(selectCharacter);
		
		addToScreen( characterWidget.actor() );
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
        currentHead.setPosition(425.0f, 50.0f);
        currentHead.setUserObject("currentHeadImage");
        
        currentHead.setVisible(true);
        
        characterWidget.addActor(currentHead);
        //addToScreen(currentHead);
        
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
