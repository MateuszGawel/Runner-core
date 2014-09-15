package com.apptogo.runner.screens;

import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.apptogo.runner.widget.Widget.WidgetType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerScreen extends BaseScreen
{			
	private TextButton createRoomButton;
	private TextButton joinRandomButton;
	private TextButton manageProfileButton;
	
	private Widget manageWidget;
	
    private Button backButton;
    
    private CharacterAnimation currentCharacterAnimation;
    private ClickListener changeCurrentCharacterAnimationListener;
	
    private CharacterAnimation manageBanditAnimation;
    private CharacterAnimation manageArcherAnimation;
    private CharacterAnimation manageAlienAnimation;
    
    private Button manageBanditAnimationButton;
    private Button manageArcherAnimationButton;
    private Button manageAlienAnimationButton;
    
	public MultiplayerScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
	}
	
	@Override
	public void prepare()
	{			
		setBackground("gfx/menu/menuBackgrounds/mainMenuScreenBackground.png");
				
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
        createRoomButton = new TextButton( "create room", skin, "default");
        createRoomButton.setPosition( -(createRoomButton.getWidth() / 2.0f), 100.0f );
		
        joinRandomButton = new TextButton( "random room", skin, "default");
		joinRandomButton.setPosition( -(joinRandomButton.getWidth() / 2.0f), -100.0f );
		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCurrentCharacter(), 275.0f, -300.0f, true);
		currentCharacterAnimation.setVisible(true);
		
		createManageWidget();
		
		manageProfileButton = new TextButton( "your profile", skin, "default");
		manageProfileButton.setPosition( -(joinRandomButton.getWidth() / 2.0f), -300.0f );
		manageProfileButton.addListener( manageWidget.getToggleListener() );
		
		addToScreen(createRoomButton);
		addToScreen(joinRandomButton);
		addToScreen(manageProfileButton);
		addToScreen(backButton);
		
		addToScreen(currentCharacterAnimation);
		addToScreen(manageWidget.actor());
	}
	
	public void step()
	{
		if ( Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE) )
		{
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		}
	}
	
	private void changeCurrentCharacterAnimation()
	{
		currentCharacterAnimation.setVisible(false);
		currentCharacterAnimation.clear();
		currentCharacterAnimation.remove();
		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCurrentCharacter(), 275.0f, -300.0f, true);
		
		currentCharacterAnimation.setVisible(true);
		
		addToScreen(currentCharacterAnimation);
	}
	
	private void createManageWidget()
	{
		manageWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		manageWidget.setEasing( Interpolation.elasticOut );

		manageBanditAnimation = CharacterType.convertToCharacterAnimation(CharacterType.BANDIT, -550.0f, 1075.0f, false);
		manageArcherAnimation = CharacterType.convertToCharacterAnimation(CharacterType.ARCHER, -400.0f, 1075.0f, false);
		manageAlienAnimation = CharacterType.convertToCharacterAnimation(CharacterType.ALIEN, -250.0f, 1075.0f, false);
		
		manageBanditAnimationButton = new Button(skin, "blackOut");
		manageBanditAnimationButton.setSize(130, 175);
		manageBanditAnimationButton.setPosition(-525, 1075);
		manageBanditAnimationButton.addListener( new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
				manageBanditAnimation.start();
				manageArcherAnimation.stop();
				manageAlienAnimation.stop();
				
				player.setCurrentCharacter(CharacterType.BANDIT);
				SaveManager.getInstance().savePlayer(player);
				
				changeCurrentCharacterAnimation();
            }
		});
		
		manageArcherAnimationButton = new Button(skin, "blackOut");
		manageArcherAnimationButton.setSize(130, 175);
		manageArcherAnimationButton.setPosition(-360, 1075);
		manageArcherAnimationButton.addListener( new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
				manageBanditAnimation.stop();
				manageArcherAnimation.start();
				manageAlienAnimation.stop();
				
				player.setCurrentCharacter(CharacterType.ARCHER);
				SaveManager.getInstance().savePlayer(player);
				
				changeCurrentCharacterAnimation();
            }
		});
		
		manageAlienAnimationButton = new Button(skin, "blackOut");
		manageAlienAnimationButton.setSize(130, 175);
		manageAlienAnimationButton.setPosition(-210, 1075);
		manageAlienAnimationButton.addListener( new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
				manageBanditAnimation.stop();
				manageArcherAnimation.stop();
				manageAlienAnimation.start();
				
				player.setCurrentCharacter(CharacterType.ALIEN);
				SaveManager.getInstance().savePlayer(player);
				
				changeCurrentCharacterAnimation();
            }
		});
		
		Label nameLabel = new Label("name:", skin, "default");
		nameLabel.setPosition(-25, 1175);
		
		final TextField textField = new TextField(player.getName(), skin);
        textField.setSize(450f, 50f);
        textField.setPosition(0f, 1115f);
        textField.setOnscreenKeyboard( textField.getOnscreenKeyboard() );
        
        textField.addListener( new InputListener() 
        {
        	public boolean keyUp(InputEvent event, int keycode)
        	{
        		player.setName( textField.getText() );
        		SaveManager.getInstance().savePlayer(player);
        		
        		return true;
        	}
		});
		
		manageWidget.addActor(manageBanditAnimation);
		manageWidget.addActor(manageArcherAnimation);
		manageWidget.addActor(manageAlienAnimation);
		
		manageWidget.addActor(manageBanditAnimationButton);
		manageWidget.addActor(manageArcherAnimationButton);
		manageWidget.addActor(manageAlienAnimationButton);
		
		manageWidget.addActor(nameLabel);
		manageWidget.addActor(textField);
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
		
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_MULTIPLAYER;
	}


}
