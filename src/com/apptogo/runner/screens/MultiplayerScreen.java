package com.apptogo.runner.screens;

import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Contact;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.InfoWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class MultiplayerScreen extends BaseScreen implements WarpListener
{			
	private TextButton joinRandomButton;
	
	private DialogWidget confirmationDialog;
	
	private Widget profileWidget;
	private Widget friendsWidget;
	private Widget shopWidget;
	private Widget rankingWidget;
	
	private InfoWidget confirmWidget;
	
    private Button backButton;
    private Button createRoomButton;
    
    private CharacterAnimation currentCharacterAnimation;
	    
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
		
		confirmationDialog = new DialogWidget("", null, null);
		
		createShopWidget();
		createFriendsWidget();
		createRankingWidget();
		createProfileWidget();
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
		
		confirmWidget = new InfoWidget( "Tutaj bêdzie dodawanie przyjació³ do gry" );
		
		joinRandomButton = new TextButton( "random room", skin, "default");
        setTextButtonFont(joinRandomButton, FontType.WOODFONT);
        joinRandomButton.setPosition( -368.0f, 120.0f );
        joinRandomButton.addListener( new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut( ScreenType.SCREEN_WAITING_ROOM );
            }
		});
                
        createRoomButton = new Button(skin, "inviteFriends");
        createRoomButton.setPosition( 205.0f, 120.0f );
        createRoomButton.addListener( friendsWidget.toggleWidgetListener );
		
		addToScreen(profileWidget.actor());
		addToScreen(createRoomButton);
		addToScreen(joinRandomButton);
		addToScreen(backButton);
		
		addToScreen(shopWidget.actor());
		addToScreen(confirmWidget.actor());
		addToScreen(friendsWidget.actor());
		addToScreen(rankingWidget.actor());
		
		addToScreen( confirmationDialog.actor() );
	}
	
	public void step()
	{
		handleInput();
	}
	
	private void createProfileWidget()
	{
		profileWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.MEDIUM, WidgetFadingType.NONE, false);
        profileWidget.toggleWidget();        
        		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), -330.0f, -200.0f, true);
		currentCharacterAnimation.setVisible(true);
        
		Image ground = createImage( CharacterType.convertToGroundPath( player.getCharacterType() ) , -330.0f, -240.0f);
				        
        Table table = new Table();
		
		table.setSize(700.0f, 320.0f);
		table.setPosition(-350.0f, -300.0f);
		//table.debug();
		
		Image flag = createImage("temp/exampleFlag.png", 0, 0);
		Label playerName = createLabel( player.getName(), FontType.BLACKBOARDMEDIUM);
		
		table.add(flag).width(32.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).center().top();
		table.add(playerName).colspan(4).width(628.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f).left().top();
		
		table.row();
		table.add().width(30.0f).height(192.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add().colspan(3).width(280.0f).height(192.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		
		
		Table innerTable = new Table();
		
		innerTable.setSize(330.0f, 192.0f);
		//innerTable.debug();
		
		Image coinImage = createImage("temp/coin.png", 0, 0);
		coinImage.setScaling(Scaling.none);
		coinImage.setAlign(Align.right);
		
		Image diamondImage = createImage("temp/diamond.png", 0, 0);
		diamondImage.setScaling(Scaling.none);
		diamondImage.setAlign(Align.right);
		
		Label ligueLabel = createLabel("Ligue:", FontType.BLACKBOARDSMALL);
		ligueLabel.setAlignment(Align.right);
		
		Label positionLabel = createLabel("Position:", FontType.BLACKBOARDSMALL);
		positionLabel.setAlignment(Align.right);
		
		Label worldPositionLabel = createLabel("World rank:", FontType.BLACKBOARDSMALL);
		worldPositionLabel.setAlignment(Align.right);
		
		innerTable.add( ligueLabel ).width(160.0f).height(64.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).right();
		innerTable.add( createLabel("A", FontType.BIG) ).width(140.0f).height(64.0f).pad(10.0f, 10.0f, 0.0f, 10.0f);
		
		innerTable.row();
		innerTable.add( positionLabel ).width(160.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).right();
		innerTable.add( createLabel("124", FontType.BLACKBOARDSMALL) ).width(140.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f);
		
		innerTable.row();
		innerTable.add( worldPositionLabel ).width(160.0f).height(32.0f).pad(10.0f, 10.0f, 36.0f, 0.0f).right();
		innerTable.add( createLabel("124 469", FontType.BLACKBOARDSMALL) ).width(140.0f).height(32.0f).pad(10.0f, 10.0f, 36.0f, 10.0f);
		
		Label more = createLabel("more...", FontType.MEDIUM, 200.0f, -280.0f);
		more.addListener(rankingWidget.toggleWidgetListener);
		profileWidget.addActor(more);
				
		table.add(innerTable).width(330.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 10.0f);
		
		table.row();
		table.add( diamondImage ).width(30.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add( createLabel("99999", FontType.BLACKBOARDSMALL) ).width(70.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add( coinImage ).width(30.0f).height(30.0f).pad(18.0f, 20.0f, 0.0f, 0.0f);
		table.add( createLabel("9999999999", FontType.BLACKBOARDSMALL) ).width(150.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add().width(330.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 10.0f);

		//--to ofc tymczasowe
		Image b1 = createImage("temp/settingsButton.png", -100.0f, -130.0f);
		
		Image b3 = createImage("temp/achievementsButton.png", -100.0f, -230.0f);
		
		b1.addListener( shopWidget.getChangeWidgetTabListener(1) );
		b1.addListener( shopWidget.toggleWidgetListener );
				
		b3.addListener( shopWidget.getChangeWidgetTabListener(2) );
		b3.addListener( shopWidget.toggleWidgetListener );
		
		//--
		
		profileWidget.addActor(table);
		
		profileWidget.addActor(b1);
		profileWidget.addActor(b3);
	
        profileWidget.addActor(ground);
        profileWidget.addActor(currentCharacterAnimation);
        
        Image shopSign = createImage("temp/shopSign.png", -600.0f, -400.0f);
        shopSign.setSize(150, 150);
        profileWidget.addActor(shopSign);
	}
	
	private void createFriendsWidget()
	{
		friendsWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		friendsWidget.setEasing( Interpolation.elasticOut );
		
		friendsWidget.addTabButton(1, "contactsTab");
		friendsWidget.addTabButton(2, "findFriendsTab");
		
		Array<Contact> contactsArray = new Array<Contact>();
		contactsArray.add( new Contact("Contact1", false) );
		contactsArray.add( new Contact("Contact2", true) );
		contactsArray.add( new Contact("Contact3", false) );
		contactsArray.add( new Contact("Contact4", true) );
		contactsArray.add( new Contact("Contact5", true) );	
		contactsArray.add( new Contact("Contact6", true) );
		contactsArray.add( new Contact("Contact7", false) );
		contactsArray.add( new Contact("Contact8", true) );
		contactsArray.add( new Contact("Contact9", true) );	
		
		contactsArray.sort();
		
		Table contactsTable = new Table();
		contactsTable.setSize(920.0f, 370.0f);	
		contactsTable.setPosition(-460.0f, 750.0f);
		
		Label nameTitle = createLabel("Name", FontType.WOODFONT);
		nameTitle.setAlignment(Align.center);
		
		Label statusTitle = createLabel("Status", FontType.WOODFONT);
		statusTitle.setAlignment(Align.center);
		
		contactsTable.add().width(130.0f).height(50.0f).center().pad(0,0,0,0);
		contactsTable.add(nameTitle).width(500.0f).height(50.0f).colspan(2).center().pad(0,0,0,30);
		contactsTable.add(statusTitle).width(210.0f).height(50.0f).center().pad(0,50,0,0);
		
		Table contactsScrollTable = new Table();
		
		for(final Contact contact: contactsArray)
		{
			CheckBox check = new CheckBox("", skin);
			Label name = createLabel( contact.name, FontType.WOODFONTSMALL);
			Image flag = createImage( "temp/exampleFlag.png", 0, 0);			
			Image status = createImage( (contact.status)?("temp/online.png"):("temp/offline.png"), 0, 0);

			contactsScrollTable.row();
			contactsScrollTable.add(check).width(50.0f).height(50.0f).center().pad(30,0,0,80);
			contactsScrollTable.add(flag).width(32.0f).height(32.0f).center().pad(39,0,0,18);
			contactsScrollTable.add(name).width(450.0f).height(50.0f).center().pad(30,0,0,30);
			contactsScrollTable.add(status).width(32.0f).height(32.0f).center().pad(39,144,0,94);
		}
					
		Container<ScrollPane> contactsContainer = createScroll(contactsScrollTable, 920.0f, 328.0f, true);
		
		contactsTable.row();
		contactsTable.add(contactsContainer).colspan(4);
				
		Label inviteLabel = createLabel("Invite!", FontType.WOODFONT);
		setCenterPosition(inviteLabel, 660.0f);
		
		friendsWidget.addActorToTab(contactsTable, 1);
		friendsWidget.addActorToTab(inviteLabel, 1);
		
		TextField searchTextField = new TextField("", skin, "default");
		searchTextField.setSize(500.0f, 35.0f);
		searchTextField.getStyle().fontColor = new Color(0,0,0,1);
		setCenterPosition(searchTextField, 1090.0f);
		searchTextField.setX( searchTextField.getX() - 95.0f );
		searchTextField.setMaxLength(18);
		
		Image findButton = createImage("temp/find.png", searchTextField.getX() + searchTextField.getWidth() + 20.0f , 1090.0f);
		
		CheckBox hideMeCheckBox = new CheckBox(" Hide me from others", skin, "default");
		hideMeCheckBox.setPosition(-400f, 1090f);
		
		friendsWidget.addActorToTab(searchTextField, 2);
		friendsWidget.addActorToTab(findButton, 2);
		
		friendsWidget.setCurrentTab(1);
	}
		
	int achievmentMargin = 0;
	int achievmentMarginY = 0;
		
	private void createShopWidget()
	{
		shopWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		shopWidget.setEasing( Interpolation.elasticOut );
		
		shopWidget.addTabButton(1, "profileTab");
		shopWidget.addTabButton(2, "achievementsTab");
		
        Label removePlayer = createLabel(getLangString("removePlayer"), FontType.WOODFONT, -450f, 1090f);
        removePlayer.addListener(new ClickListener(){
        	
        	public void clicked(InputEvent event, float x, float y) 
            {
        		final ClickListener confirmChangeListener = new ClickListener()
        		{
        			public void clicked(InputEvent event, float x, float y)
        			{
        				player = new Player();
        				player.save();
        				
        				ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_MAIN_MENU );	
        			}
        		};
        		
            	confirmationDialog.setYesListener( confirmChangeListener );
            	confirmationDialog.setLabel( getLangString("areYouSureToRemovePlayer"));
            	confirmationDialog.toggleWidget();
            }
        	
        });        
        
		shopWidget.addActorToTab(removePlayer, 1);
		
		//--tab2
		/*
		Table categoryTable = new Table();
		
		Label category1 = createLabel("category1", FontType.WOODFONT);
		Label category2 = createLabel("category2", FontType.WOODFONT);
		Label category3 = createLabel("category3", FontType.WOODFONT);
		Label category4 = createLabel("category4", FontType.WOODFONT);
		Label category5 = createLabel("category5", FontType.WOODFONT);
		Label category6 = createLabel("category6", FontType.WOODFONT);
		Label category7 = createLabel("category7", FontType.WOODFONT);
		Label category8 = createLabel("category8", FontType.WOODFONT);
		
		categoryTable.add(category1).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category2).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category3).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category4).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category5).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category6).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category7).center().expand().pad(20);
		categoryTable.row();
		categoryTable.add(category8).center().expand().pad(20);
		
		categoryTable.debug();
		
		Container<ScrollPane> categoryContainer = createScroll(categoryTable, 250.0f, 500.0f, true);
		categoryContainer.setPosition(-550, 650);
		
		shopWidget.addActorToTab(categoryContainer, 2);*/
		
		
		
		shopWidget.setCurrentTab(1);
	}
	
	private void createRankingWidget()
	{
		rankingWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		rankingWidget.setEasing( Interpolation.elasticOut );
		
		rankingWidget.addTabButton(1, "ligueTab");
		rankingWidget.addTabButton(2, "worldTab");
		rankingWidget.addTabButton(3, "contestTab");
		
		rankingWidget.setCurrentTab(1);
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
