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
import com.apptogo.runner.widget.InfoWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerScreen extends BaseScreen implements WarpListener
{			
	private TextButton createRoomButton;
	private TextButton joinRandomButton;
	private TextButton manageProfileButton;
	
	private Widget profileView;
	private Widget manageWidget;
	
	private InfoWidget confirmWidget;
	
    private Button backButton;
    
    private CharacterAnimation currentCharacterAnimation;
	
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
		
		fadeInOnStart();
		
		if( !(WarpController.getInstance().isOnline) )
		{
			Logger.log(this, "odpalam");
			WarpController.getInstance().startApp( player.getName() );
		}
		WarpController.getInstance().setMultiplayerScreenListener(this);
		NotificationManager.prepareManager(player.getName(), player.getCharacterType());
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
        
        currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), -360.0f, 50.0f, true);
		currentCharacterAnimation.setVisible(true);
        
		Image ground = createImage("temp/ground.png", -360.0f, 5.0f);
		
		Label playerName = createLabel( player.getName(), FontType.WOODFONTSMALL, -320.0f, 230.0f);
		
		Label worldRankLabel = createLabel( getLangString("worldRank"), FontType.WOODFONTSMALL, -150.0f, 150.0f);
		Label worldRankValue = createLabel( "123", FontType.WOODFONTSMALL, 50.0f, 150.0f);
		Label ligueRankLabel = createLabel( getLangString("ligueRank"), FontType.WOODFONTSMALL, -150.0f, 100.0f);
		Label ligueRankValue = createLabel( "5", FontType.WOODFONTSMALL, 50.0f, 100.0f);
		
		Image achievmentsButton = createImage("temp/achievments.png", 190.0f, 5.0f);
		Image shopButton = createImage("temp/shop.png", 280.0f, 5.0f);
		
        profileView = new Widget(Align.center, -20.0f, 0.0f, WidgetType.SMALL, WidgetFadingType.NONE, false);
        profileView.toggleWidget();        
        
        profileView.addActor(playerName);
        profileView.addActor(ground);
        profileView.addActor(currentCharacterAnimation);
        profileView.addActor(worldRankLabel);
        profileView.addActor(worldRankValue);
        profileView.addActor(ligueRankLabel);
        profileView.addActor(ligueRankValue);
        profileView.addActor(achievmentsButton);
        profileView.addActor(shopButton);
		
		confirmWidget = new InfoWidget( "Tutaj bêdzie dodawanie przyjació³ do gry" );
		
		joinRandomButton = new TextButton( "random room", skin, "default");
        setTextButtonFont(joinRandomButton, FontType.WOODFONT);
        joinRandomButton.setPosition( -368.0f, -250.0f );
        joinRandomButton.addListener( new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut( ScreenType.SCREEN_WAITING_ROOM );
            }
		});
        
        createRoomButton = new TextButton( "P+", skin, "default");
        createRoomButton.setSize(163.0f, 163.0f);
        createRoomButton.setPosition( 205.0f, -250.0f );
        createRoomButton.addListener( confirmWidget.toggleWidgetListener );
        
        
		createManageWidget();
		
		//manageProfileButton.addListener( manageWidget.toggleWidgetListener );
		
		addToScreen(profileView.actor());
		addToScreen(createRoomButton);
		addToScreen(joinRandomButton);
		addToScreen(backButton);
		
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
		
		
		//CharacterAnimation playerCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), 275.0f, -300.0f, true);
		
		//Image playerCharacterAnimationBackgorund = createImage( CharacterType.convertToCharacterAnimationBackground , x, y)
		
		//manageWidget.addActor(playerName);
		//manageWidget.addActor(playerCharacterAnimation);
		
		
		
		/*		
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
		manageWidget.addActor(manageAlienAnimationButton);*/
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
