package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterType;
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
	private Group mainGroup;
	private Group rankGroup;
	
	private Button moveToMainLeftButton;
	private Button moveToRankButton;
	
	private ClickListener moveToMainListener;
	private ClickListener moveToRankListener;
	
	private DialogWidget confirmationDialog;
	
	private Widget profileWidget;
	private Widget friendsWidget;
	private Widget rankWidget;
	
	private InfoWidget confirmWidget;
    
	private Animation currentCharacterAnimation;
	private Animation shopButtonAnimation;
	    
	public MultiplayerScreen(Runner runner)
	{
		super(runner);
		
		if( !(WarpController.getInstance().isOnline) )
		{
			WarpController.getInstance().startApp( player.getName() );
		}
		WarpController.getInstance().setMultiplayerScreenListener(this);
		NotificationManager.prepareManager(player.getName(), player.getCharacterType());
	}
	
	@Override
	public void prepare()
	{	
		setBackground("mainMenuScreenBackground");
		
		group = new Group();
		group.setTransform(false);
		
		mainGroup = new Group();
		mainGroup.setTransform(false);
		
		rankGroup = new Group();
		rankGroup.setTransform(false);
			
		moveToMainListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(-Runner.SCREEN_WIDTH, 0.0f) );
			moveToMainLeftButton.setVisible(false);
			moveToRankButton.setVisible(true);
		} };
		
		moveToRankListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(-2*Runner.SCREEN_WIDTH, 0.0f) );
			moveToMainLeftButton.setVisible(true);
			moveToRankButton.setVisible(false);
		} };
		
		moveToMainLeftButton = new Button(skin, "campaignArrowLeft");
		moveToMainLeftButton.setPosition(-570.0f + (2 * Runner.SCREEN_WIDTH), -100.0f);
		moveToMainLeftButton.addListener(moveToMainListener);
				
		moveToRankButton = new Button(skin, "campaignArrowRight");
		moveToRankButton.setPosition(470.0f + Runner.SCREEN_WIDTH, -100.0f);
		moveToRankButton.addListener(moveToRankListener);
		
		confirmationDialog = new DialogWidget("", null, null);
		
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
		        
		mainGroup.addActor(profileWidget.actor());
		mainGroup.addActor(createRoomButton);
		mainGroup.addActor(joinRandomButton);
		
		mainGroup.setPosition(Runner.SCREEN_WIDTH, 0.0f);
		
		rankGroup.addActor( rankWidget.actor() );
		rankGroup.setPosition(Runner.SCREEN_WIDTH * 2,  0.0f);
		
		group.addActor(mainGroup);
		group.addActor(rankGroup);
		
		group.addActor(moveToMainLeftButton);
		group.addActor(moveToRankButton);
		
		moveToMainLeftButton.setVisible(false);
		moveToRankButton.setVisible(true);
		
		group.setPosition(-Runner.SCREEN_WIDTH, 0.0f);
		
		shopButtonAnimation = new Animation("shop", 2, 1f, CharacterAnimationState.IDLE, true, true);
		shopButtonAnimation.setPosition(420, -350);
		shopButtonAnimation.setVisible(true);
		
		addToScreen(shopButtonAnimation);
		
		addToScreen(group);
				
		addToScreen(confirmWidget.actor());
		addToScreen(friendsWidget.actor());
		
		addToScreen( confirmationDialog.actor() );
		
		addToScreen(backButton);
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
		
	private void createProfileWidget()
	{
		profileWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.MEDIUM, WidgetFadingType.NONE, false);
        profileWidget.showWidget();     
        		
		currentCharacterAnimation = new Animation("alien_idle", 10, 0.03f, CharacterAnimationState.IDLE, true, true); // CharacterType.convertToCharacterAnimation(player.getCharacterType(), -340.0f, -220.0f, true);
		currentCharacterAnimation.setPosition(0, 0);
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
		
		profileWidget.addActor(table);
			
        profileWidget.addActor(ground);
        profileWidget.addActor(currentCharacterAnimation);
        
        profileWidget.showWidget();
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
		
		Label nameTitle = new Label("Name", skin, "default");
		nameTitle.setAlignment(Align.center);
		
		Label statusTitle = new Label("Status", skin, "default");
		statusTitle.setAlignment(Align.center);
		
		contactsTable.add().width(130.0f).height(50.0f).center().pad(0,0,0,0);
		contactsTable.add(nameTitle).width(500.0f).height(50.0f).colspan(2).center().pad(0,0,0,30);
		contactsTable.add(statusTitle).width(210.0f).height(50.0f).center().pad(0,50,0,0);
		
		Table contactsScrollTable = new Table();
		
		for(final Contact contact: contactsArray)
		{
			CheckBox check = new CheckBox("", skin);
			Label name = new Label( contact.name, skin, "default");
			
			contactsScrollTable.row();
			contactsScrollTable.add(check).width(50.0f).height(50.0f).center().pad(30,0,0,80);
			contactsScrollTable.add().width(32.0f).height(32.0f).center().pad(39,0,0,18);
			contactsScrollTable.add(name).width(450.0f).height(50.0f).center().pad(30,0,0,30);
			contactsScrollTable.add().width(32.0f).height(32.0f).center().pad(39,144,0,94);
		}
					
		Container<ScrollPane> contactsContainer = createScroll(contactsScrollTable, 920.0f, 328.0f, true);
		
		contactsTable.row();
		contactsTable.add(contactsContainer).colspan(4);
				
		Label inviteLabel = new Label("Invite!", skin, "default");
		setCenterPosition(inviteLabel, 660.0f);
		
		friendsWidget.addActorToTab(contactsTable, 1);
		friendsWidget.addActorToTab(inviteLabel, 1);
		
		TextField searchTextField = new TextField("", skin, "default");
		searchTextField.setSize(500.0f, 35.0f);
		searchTextField.getStyle().fontColor = new Color(0,0,0,1);
		setCenterPosition(searchTextField, 1090.0f);
		searchTextField.setX( searchTextField.getX() - 95.0f );
		searchTextField.setMaxLength(18);
		
		
		
		CheckBox hideMeCheckBox = new CheckBox(" Hide me from others", skin, "default");
		hideMeCheckBox.setPosition(-400f, 1090f);
		
		friendsWidget.addActorToTab(searchTextField, 2);		
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
	}


}
