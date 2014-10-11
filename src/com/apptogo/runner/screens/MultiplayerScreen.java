package com.apptogo.runner.screens;

import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerScreen extends BaseScreen implements WarpListener
{			
	private TextButton createRoomButton;
	private TextButton joinRandomButton;
	private TextButton manageProfileButton;
	
	private Widget manageWidget;
	
	private DialogWidget confirmWidget;
	
    private Button backButton;
    
    private CharacterAnimation currentCharacterAnimation;
	
    private CharacterAnimation manageBanditAnimation;
    private CharacterAnimation manageArcherAnimation;
    private CharacterAnimation manageAlienAnimation;
    
    private Button manageBanditAnimationButton;
    private Button manageArcherAnimationButton;
    private Button manageAlienAnimationButton;
    
    private Label tab1;
    private Label tab2;
    private Label tab3;
    
    private boolean isTextFieldClicked = false;
    private boolean isTextFieldLastActorClicked = false;
    
	public MultiplayerScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
		
		fadeInOnStart();
		
		if( !(WarpController.getInstance().isOnline) )
		{
			Logger.log(this, "odpalam");
			WarpController.getInstance().startApp( player.getName() );
		}
		WarpController.getInstance().setMultiplayerScreenListener(this);
		NotificationManager.getInstance().prepareManager(player.getName(), player.getCharacterType());
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
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
        
        createRoomButton = new TextButton( "create room", skin, "default");
        setTextButtonFont(createRoomButton, FontType.WOODFONT);
        createRoomButton.setPosition( -(createRoomButton.getWidth() / 2.0f), 100.0f );
		
        joinRandomButton = new TextButton( "random room", skin, "default");
        setTextButtonFont(joinRandomButton, FontType.WOODFONT);
		joinRandomButton.setPosition( -(joinRandomButton.getWidth() / 2.0f), -100.0f );
		joinRandomButton.addListener( new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut( ScreenType.SCREEN_WAITING_ROOM );
            }
		});
		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), 275.0f, -300.0f, true);
		currentCharacterAnimation.setVisible(true);
		
		createManageWidget();
		
		confirmWidget = new DialogWidget("To jest jakas wiadomosc", null, null);
		createRoomButton.addListener( confirmWidget.toggleWidgetListener );
		
		manageProfileButton = new TextButton( "your profile", skin, "default");
		setTextButtonFont(manageProfileButton, FontType.WOODFONT);
		manageProfileButton.setPosition( -(joinRandomButton.getWidth() / 2.0f), -300.0f );
		manageProfileButton.addListener( manageWidget.toggleWidgetListener );
		
		addToScreen(createRoomButton);
		addToScreen(joinRandomButton);
		addToScreen(manageProfileButton);
		addToScreen(backButton);
		
		addToScreen(currentCharacterAnimation);
		addToScreen(manageWidget.actor());
		addToScreen(confirmWidget.actor());
	}
	
	public void step()
	{
		handleInput();
	}
	
	private void changeCurrentCharacterAnimation()
	{
		currentCharacterAnimation.setVisible(false);
		currentCharacterAnimation.clear();
		currentCharacterAnimation.remove();
		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), 275.0f, -300.0f, true);
		
		currentCharacterAnimation.setVisible(true);
		
		addToScreen(currentCharacterAnimation);
	}
	
	private void createManageWidget()
	{
		manageWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		manageWidget.setEasing( Interpolation.elasticOut );
		manageWidget.getToFrontOnClick = true;
		
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
				
				player.setCharacterType(CharacterType.BANDIT);
				player.save();
				
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
				
				player.setCharacterType(CharacterType.ARCHER);
				player.save();
				
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
				
				player.setCharacterType(CharacterType.ALIEN);
				player.save();
				
				changeCurrentCharacterAnimation();
            }
		});
		
		Label nameLabel = new Label("name:", skin, "default");
		setLabelFont(nameLabel, FontType.BIG);
		nameLabel.setPosition(-25, 1175);
		
		final TextField textField = new TextField(player.getName(), skin);
		//setTextFieldFont(textField, FontType.SMALL); - niestety nie da sie tak prosto, textField musi miec podana odpowiednia czcionke juz w momencie tworzenia bo w ten sposob okresla wielkosc jednej pozycji kursora :(
        textField.setSize(450f, 50f);
        textField.setPosition(0f, 1115f);
        textField.setOnlyFontChars(true);
        
        //--Ten kod obsluguje utracenie focusa na textField, moga pojawic sie problemy przy nastepnych textFieldach + to na pewno nie jest najlepsze wyjscie moze da sie to zrobic inaczej?
        textField.addListener( new ClickListener()
		{
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
        	{
        		isTextFieldClicked = true;
        		return true;
        	};
        	
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) 
        	{
        		isTextFieldClicked = false;
        		isTextFieldLastActorClicked = true;
        	};
		});
        
        stage.addListener( new ClickListener()
		{
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
        	{
        		if( !isTextFieldClicked && isTextFieldLastActorClicked ) 
    			{
        			isTextFieldLastActorClicked = false;
        			
        			Logger.log(this, "teraz odkliknieto");
        			stage.setKeyboardFocus(null);
        			player.setName( textField.getText() );
            		player.save();
    			}
        		
        		return true;
        	};
		});
        //---
        
        //creating tabs
        tab1 = new Label("TO JEST TAB 1", skin);
        tab1.setPosition(0, 800);
        
        tab2 = new Label("TO JEST TAB 2", skin);
        tab2.setPosition(0, 800);
        
        tab3 = new Label("TO JEST TAB 3", skin);
        tab3.setPosition(0, 800);
        
        manageWidget.addActorToTab(tab1, 1);
        manageWidget.addActorToTab(tab2, 2);
        manageWidget.addActorToTab(tab3, 3);
        
        manageWidget.setCurrentTab(1);
        
        manageBanditAnimationButton.addListener( manageWidget.getChangeWidgetTabListener(1) );
        manageArcherAnimationButton.addListener( manageWidget.getChangeWidgetTabListener(2) );
        manageAlienAnimationButton.addListener( manageWidget.getChangeWidgetTabListener(3) );
        //tabs created
		
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
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
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

	@Override
	public void onWaitingStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameUpdateReceived(String message) {
		Logger.log(this, "przyszed³ update ale zly listener");
		
	}


}
