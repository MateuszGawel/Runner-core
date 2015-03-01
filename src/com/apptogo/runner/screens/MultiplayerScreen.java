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
import com.apptogo.runner.player.Contact;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.InfoWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
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

public class MultiplayerScreen extends BaseScreen implements WarpListener
{			
	private Button backButton;
    private Button createRoomButton;
	
	private TextButton joinRandomButton;
	
	private Group group;
	private Group shopGroup;
	private Group mainGroup;
	private Group rankGroup;
	
	private Button moveToMainRightButton;
	private Button moveToMainLeftButton;
	private Button moveToShopButton;
	private Button moveToRankButton;
	
	private ClickListener moveToShopListener;
	private ClickListener moveToMainListener;
	private ClickListener moveToRankListener;
	
	private DialogWidget confirmationDialog;
	
	private Widget shopWidget;
	private Widget profileWidget;
	private Widget friendsWidget;
	private Widget rankWidget;
	
	private InfoWidget confirmWidget;
    
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
		
		group = new Group();
		
		shopGroup = new Group();
		mainGroup = new Group();
		rankGroup = new Group();
		
		moveToShopListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(0.0f, 0.0f) ); 
			moveToMainRightButton.setVisible(true); 
			moveToMainLeftButton.setVisible(false);
			moveToShopButton.setVisible(false);
			moveToRankButton.setVisible(false);
		} };    
			
		moveToMainListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(-Runner.SCREEN_WIDTH, 0.0f) );
			moveToMainRightButton.setVisible(false); 
			moveToMainLeftButton.setVisible(false); 
			moveToShopButton.setVisible(true);
			moveToRankButton.setVisible(true);
		} };
		
		moveToRankListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(-2*Runner.SCREEN_WIDTH, 0.0f) );
			moveToMainRightButton.setVisible(false); 
			moveToMainLeftButton.setVisible(true); 
			moveToShopButton.setVisible(false);
			moveToRankButton.setVisible(false);
		} };
		
		moveToMainRightButton = new Button(skin, "campaignArrowRight");
		moveToMainRightButton.setPosition(470.0f, -100.0f);
		moveToMainRightButton.addListener(moveToMainListener);
		
		moveToMainLeftButton = new Button(skin, "campaignArrowLeft");
		moveToMainLeftButton.setPosition(-570.0f + (2 * Runner.SCREEN_WIDTH), -100.0f);
		moveToMainLeftButton.addListener(moveToMainListener);
		
		moveToShopButton = new Button(skin, "campaignArrowLeft");
		moveToShopButton.setPosition(-570.0f + Runner.SCREEN_WIDTH, -100.0f);
		moveToShopButton.addListener(moveToShopListener);
		
		moveToRankButton = new Button(skin, "campaignArrowRight");
		moveToRankButton.setPosition(470.0f + Runner.SCREEN_WIDTH, -100.0f);
		moveToRankButton.addListener(moveToRankListener);
		
		confirmationDialog = new DialogWidget("", null, null);
		
		createShopWidget();
		
		createFriendsWidget();
		createProfileWidget();
		
		createRankWidget();
		
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
		
        shopGroup.addActor( shopWidget.actor() );
        
        shopGroup.setPosition(0.0f, 0.0f);
        
		mainGroup.addActor(profileWidget.actor());
		mainGroup.addActor(createRoomButton);
		mainGroup.addActor(joinRandomButton);
		
		mainGroup.setPosition(Runner.SCREEN_WIDTH, 0.0f);
		
		rankGroup.addActor( rankWidget.actor() );
		rankGroup.setPosition(Runner.SCREEN_WIDTH * 2,  0.0f);
		
		group.addActor(shopGroup);
		group.addActor(mainGroup);
		group.addActor(rankGroup);
		
		group.addActor(moveToMainRightButton);
		group.addActor(moveToMainLeftButton);
		group.addActor(moveToShopButton);
		group.addActor(moveToRankButton);
		
		moveToMainRightButton.setVisible(false); 
		moveToMainLeftButton.setVisible(false); 
		moveToShopButton.setVisible(true);
		moveToRankButton.setVisible(true);
		
		group.setPosition(-Runner.SCREEN_WIDTH, 0.0f);
		
		addToScreen(group);
		
		addToScreen(backButton);
		
		addToScreen(confirmWidget.actor());
		addToScreen(friendsWidget.actor());
		
		addToScreen( confirmationDialog.actor() );
	}
	
	private MoveToAction getMoveAction(float x, float y)
	{
		MoveToAction moveToAction = new MoveToAction();
    	moveToAction.setPosition(x, y);
    	moveToAction.setDuration(0.2f);
    	
    	return moveToAction;
	}
	
	public void step()
	{
		handleInput();
	}
	
	private void createShopWidget()
	{
		shopWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
		
		TextButton cat1B = new TextButton("category1", skin, "categoryTab");
		cat1B.setSize(200.0f, 100.0f);
		cat1B.setPosition(-340.0f, 170.0f);
		
		TextButton cat2B = new TextButton("category2", skin, "categoryTab");
		cat2B.setSize(200.0f, 100.0f);
		cat2B.setPosition(-100.0f, 170.0f);
		
		TextButton cat3B = new TextButton("category3", skin, "categoryTab");
		cat3B.setSize(200.0f, 100.0f);
		cat3B.setPosition(140.0f, 170.0f);
		
		shopWidget.addTabButton(1, cat1B);
		shopWidget.addTabButton(2, cat2B);
		shopWidget.addTabButton(3, cat3B);
		
		CharacterAnimation alienCharacterAnimation = CharacterType.convertToCharacterAnimation(CharacterType.ALIEN, -600.0f, -20.0f, true);
		CharacterAnimation archerCharacterAnimation = CharacterType.convertToCharacterAnimation(CharacterType.ARCHER, -600.0f, -180.0f, true);
		CharacterAnimation banditCharacterAnimation = CharacterType.convertToCharacterAnimation(CharacterType.BANDIT, -600.0f, -340.0f, true);
		
		shopWidget.addActorToTab(alienCharacterAnimation, 2);
		shopWidget.addActorToTab(archerCharacterAnimation, 2);
		shopWidget.addActorToTab(banditCharacterAnimation, 2);
		
		Image comingSoon = createImage("gfx/menu/comingSoon.png", -200, -200);
		shopWidget.addActorToTab(comingSoon, 3);
		
		shopWidget.setCurrentTab(1);
		
        Table table = new Table();
        table.debug();
        
        //table.setSize(640.0f, 920.0f);
        //able.setPosition(-320.0f, -300.0f);
        
        table.add().width(32 * 02).height(32 * 02).center().top();
        table.add().width(32 * 18).height(32 * 02).center().top().colspan(2);
        
        table.row();
        table.add().width(32 * 20).height(32 * 01).colspan(3);
        
        table.row();
        table.add().width(32 * 03).height(32 * 106).center().top();
        table.add().width(32 * 03).height(32 * 106).center().top();
        table.add().width(32 * 16).height(32 * 106).center().top();
        
        table.row();
        table.add().width(32 * 05).height(32 * 01).center().top().colspan(2);
        table.add().width(32 * 16).height(32 * 01).center().top();
        
        Container<ScrollPane> container = createScroll(table, 672.0f, 390.0f, true);
        container.debug();
        container.setPosition(-340.0f, -300.0f);
        
        shopWidget.addActorToTab(container, 1);
        
        shopWidget.toggleWidget(); 
	}
	
	private void createProfileWidget()
	{
		profileWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.MEDIUM, WidgetFadingType.NONE, false);
        profileWidget.toggleWidget();        
        		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), -340.0f, -220.0f, true);
		currentCharacterAnimation.setVisible(true);
        
		Image ground = createImage( CharacterType.convertToGroundPath( player.getCharacterType() ) , -320.0f, -260.0f);
		ground.setWidth(128.0f);		        
		
        Table table = new Table();
        table.debug();
        
        table.setSize(640.0f, 320.0f);
        table.setPosition(-320.0f, -300.0f);
        
        table.add().width(32 * 02).height(32 * 02).center().top();
        table.add().width(32 * 18).height(32 * 02).center().top().colspan(2);
        
        table.row();
        table.add().width(32 * 20).height(32 * 01).colspan(3);
        
        table.row();
        table.add().width(32 * 02).height(32 * 06).center().top();
        table.add().width(32 * 02).height(32 * 06).center().top();
        table.add().width(32 * 16).height(32 * 06).center().top();
        
        table.row();
        table.add().width(32 * 04).height(32 * 01).center().top().colspan(2);
        table.add().width(32 * 16).height(32 * 01).center().top();
        /*
		
		
		table.debug();
		
		Image flag = createImage("temp/exampleFlag.png", 0, 0);
		Label playerName = createLabel( player.getName(), FontType.BLACKBOARDMEDIUM);
		
		table.add(flag).width(32.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).center().top();
		table.add(playerName).width(628.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f).left().top();
		
		table.row();
		table.add().width(30.0f).height(192.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);		
		
		//inner table part
		Table innerTable = new Table();
		{
			innerTable.setSize(628.0f, 192.0f);
			innerTable.debug();
						
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
		}
		
		table.add(innerTable).width(330.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 10.0f);
		
		Image coinImage = createImage("temp/coin.png", 0, 0);
		coinImage.setScaling(Scaling.none);
		coinImage.setAlign(Align.right);
		
		Image diamondImage = createImage("temp/diamond.png", 0, 0);
		diamondImage.setScaling(Scaling.none);
		diamondImage.setAlign(Align.right);*/
		
		/*
		table.row();
		table.add( diamondImage ).width(30.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add( createLabel("99999", FontType.BLACKBOARDSMALL) ).width(70.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add( coinImage ).width(30.0f).height(30.0f).pad(18.0f, 20.0f, 0.0f, 0.0f);
		table.add( createLabel("9999999999", FontType.BLACKBOARDSMALL) ).width(150.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		table.add().width(330.0f).height(50.0f).pad(18.0f, 10.0f, 0.0f, 10.0f);

		//--to ofc tymczasowe
		Image b1 = createImage("temp/settingsButton.png", -100.0f, -130.0f);
		
		Image b3 = createImage("temp/achievementsButton.png", -100.0f, -230.0f);
			
		//--
		*/
		
		profileWidget.addActor(table);
			
        profileWidget.addActor(ground);
        profileWidget.addActor(currentCharacterAnimation);
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
	
	private void createRankWidget()
	{
		rankWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
		rankWidget.toggleWidget(); 
        
        Table table = new Table();
        table.debug();
        
        table.setSize(640.0f, 320.0f);
        table.setPosition(-320.0f, -300.0f);
        
        table.add().width(32 * 02).height(32 * 02).center().top();
        table.add().width(32 * 18).height(32 * 02).center().top().colspan(2);
        
        table.row();
        table.add().width(32 * 20).height(32 * 01).colspan(3);
        
        table.row();
        table.add().width(32 * 02).height(32 * 06).center().top();
        table.add().width(32 * 02).height(32 * 06).center().top();
        table.add().width(32 * 16).height(32 * 06).center().top();
        
        table.row();
        table.add().width(32 * 04).height(32 * 01).center().top().colspan(2);
        table.add().width(32 * 16).height(32 * 01).center().top();
        
        rankWidget.addActor(table);
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
