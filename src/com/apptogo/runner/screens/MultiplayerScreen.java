package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	
	private Actor changeAnimationActor;
	
	Image ground;
	
	Array<Image> headImages = new Array<Image>();
	    
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
		
		shopButtonAnimation = new Animation("shopButton", 2, 1f, CharacterAnimationState.IDLE, true, true);
		shopButtonAnimation.setPosition(420, -350);
		shopButtonAnimation.setVisible(true);
		
		shopButtonAnimation.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut(ScreenType.SCREEN_SHOP);
            }
		});		
		
		
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
		
	private void refreshHeads()
	{
		for(Image head : headImages) 
		{
			if( (CharacterType)head.getUserObject() == player.getCharacterType() )
			{
				head.getColor().a = 1;
			}
			else
			{
				head.getColor().a = 0.3f;
			}
		}
	}
	
	private void refreshCurrentCharacterAnimation(boolean running)
	{
		if(currentCharacterAnimation != null) currentCharacterAnimation.remove();
		if(ground != null) ground.remove();
		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), running);
		
		if( player.getCharacterType() == CharacterType.BANDIT)
		{
			currentCharacterAnimation.setPosition(-200, -225);
		}
		else if( player.getCharacterType() == CharacterType.ARCHER)
		{
			currentCharacterAnimation.setPosition(-208, -219);
		}
		else //ALIEN
		{
			currentCharacterAnimation.setPosition(-217, -222);
		}
		
		currentCharacterAnimation.setVisible(true);
		
		ground = createImage( CharacterType.convertToGroundPath( player.getCharacterType() ) , -170.0f, -260.0f);
		ground.setWidth(128.0f);
		
		profileWidget.addActor(ground);
        profileWidget.addActor(currentCharacterAnimation);
        
        changeAnimationActor.toFront();
	}
	
	private void createProfileWidget()
	{
		profileWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.MEDIUM, WidgetFadingType.NONE, false);
		profileWidget.showWidget();
		
		//heads
		final Image alienHead = new Image( ResourcesManager.getInstance().getAtlasRegion("alienProgressBarHead") );
        final Image archerHead = new Image( ResourcesManager.getInstance().getAtlasRegion("archerProgressBarHead") );
        final Image banditHead = new Image( ResourcesManager.getInstance().getAtlasRegion("banditProgressBarHead") );
		
        alienHead.setUserObject(CharacterType.ALIEN);
        archerHead.setUserObject(CharacterType.ARCHER);
        banditHead.setUserObject(CharacterType.BANDIT);
        
        headImages = new Array<Image>( new Image[]{alienHead, archerHead, banditHead} );
        
        ClickListener headListener = new ClickListener() 
        {
        	public void clicked(InputEvent event, float x, float y)
        	{
				player.setCharacterType( (CharacterType)event.getTarget().getUserObject() );
				player.save();
				
				refreshCurrentCharacterAnimation( !currentCharacterAnimation.frameRegionName.contains("idle") );
				refreshHeads();	
        	}
        };
        
        alienHead.addListener(headListener);
        archerHead.addListener(headListener);
        banditHead.addListener(headListener);
                
		Table headTable = new Table();
		headTable.setSize(120, 300);
		//headTable.debug();
		headTable.add(alienHead).size(95).pad(0, 0, 0, 25);
		headTable.row();
		headTable.add(archerHead).size(95).pad(7, 0, 7, 25);
		headTable.row();
		headTable.add(banditHead).size(95).pad(0, 0, 0, 25);

		//summary
		Table summaryTable = new Table();
		summaryTable.setSize(465, 300);
		//summaryTable.debug();
		summaryTable.add( new Label( player.getName(), skin, "coinLabelBig" ) ).width(465).height(40).colspan(3);
		summaryTable.row();
		summaryTable.add().width(465).height(20).colspan(3);
		summaryTable.row();
		summaryTable.add().width(165).height(60).pad(30,0,0,0);
		summaryTable.add( new Label( "Total:", skin, "medium") ).width(140).height(60).pad(30,0,0,0);
		summaryTable.add( new Label( "13'25\"", skin, "coinLabel") ).width(160).height(40).pad(50,0,0,0);
		summaryTable.row();
		summaryTable.add().width(165).height(60);
		summaryTable.add( new Label( "Wins:", skin, "medium") ).width(140).height(60);
		summaryTable.add( new Label( "39", skin, "coinLabel") ).width(160).height(40).pad(20,0,0,0);
		summaryTable.row();
		summaryTable.add().width(165).height(60).pad(0,0,30,0);
		summaryTable.add( new Label( "Stars:", skin, "medium") ).width(140).height(60).pad(0,0,30,0);
		summaryTable.add( new Label( "35 / 60", skin, "coinLabel") ).width(160).height(40).pad(20,0,30,0);
		
		//main table
		Table table = new Table();
		//table.debug();
		table.setSize(680, 300);
		setCenterPosition(table, -285);

		table.add(headTable).width(120).height(300);
		table.add().width(75).height(300);
		table.add(summaryTable).width(465).height(300);
		
		//changeHead actor
		changeAnimationActor = new Actor();
		changeAnimationActor.setSize(130, 200);
		changeAnimationActor.setPosition(-170.0f, -260.0f);
		
		changeAnimationActor.addListener(new ClickListener() {public void clicked(InputEvent event, float x, float y){ 
			refreshCurrentCharacterAnimation( currentCharacterAnimation.frameRegionName.contains("idle") );
        }});
        
		profileWidget.addActor(table);
		profileWidget.addActor(changeAnimationActor);
		
		refreshHeads();
		refreshCurrentCharacterAnimation( false );
	}
	
	private void createFriendsWidget()
	{
		friendsWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		friendsWidget.setEasing( Interpolation.elasticOut );
		
		friendsWidget.addTabButton(1, "contactsTab");
		friendsWidget.addTabButton(2, "findFriendsTab");
		
		//player.friends.addAll("Maciej", "Miet", "Franciszek", "Michal", "Ola", "Piotrek");
		
		if( player.friends.size > 0 )
		{
			Array<Contact> contactsArray = new Array<Contact>();
			
			for(String friend: player.friends)
			{
				boolean online = false;
				//if( isOnline(friend) )
				if( friend.compareTo("online") >= 0 )
				{
					online = true;
				}
				
				contactsArray.add( new Contact(friend, online) );
			}
			
			contactsArray.sort();
			
			Table contactsTable = new Table();
			//contactsTable.debug();
			contactsTable.setSize(820.0f, 340.0f);	
			contactsTable.setPosition(-410.0f, 770.0f);
			
			Label nameTitle = new Label("Name", skin, "coinLabel");
			nameTitle.setAlignment(Align.center);
			
			Label statusTitle = new Label("Status", skin, "coinLabel");
			statusTitle.setAlignment(Align.center);
			
			contactsTable.add().width(130.0f).height(50.0f).center().pad(0,0,10,0);
			contactsTable.add(nameTitle).width(450.0f).height(50.0f).colspan(2).center().pad(0,0,10,30);
			contactsTable.add(statusTitle).width(210.0f).height(50.0f).center().pad(0,0,10,0);
			
			Table contactsScrollTable = new Table();
			//contactsScrollTable.debug();
			for(final Contact contact: contactsArray)
			{
				CheckBox check = new CheckBox("", skin);
				Label name = new Label( contact.name, skin, "woodMedium");
				
				contactsScrollTable.row();
				contactsScrollTable.add(check).width(50.0f).height(50.0f).center().pad(30,50,0,30);
				contactsScrollTable.add().width(32.0f).height(32.0f).center().pad(39,0,0,18);
				contactsScrollTable.add(name).width(400.0f).height(50.0f).center().pad(30,0,0,30);
				contactsScrollTable.add().width(32.0f).height(32.0f).center().pad(39,94,0,94);
			}
						
			ScrollPane contactsContainer = createScroll(contactsScrollTable, 820.0f, 290.0f, true);
			
			contactsTable.row();
			contactsTable.add(contactsContainer).colspan(4);
					
			Button inviteButton = new Button(skin, "invite");
			setCenterPosition(inviteButton, 660.0f);
			
			friendsWidget.addActorToTab(contactsTable, 1);
			friendsWidget.addActorToTab(inviteButton, 1);
		}
		else
		{
			Image aloneImage = createImage("alone", 100, 700);
			
			Label dontHaveLabel = new Label("You do not have any friends yet...", skin, "woodMedium");
			
			dontHaveLabel.setPosition(-500, 1020);
			
			Label findLabel = new Label("Find them now!", skin, "coinLabelBig");
			
			findLabel.setPosition(-450, 900);
			
			Button goToFindButton = new Button(skin, "search");
			goToFindButton.setPosition(findLabel.getX() + (findLabel.getWidth()-goToFindButton.getWidth())/2f, 790);
			goToFindButton.addListener( friendsWidget.getChangeWidgetTabListener(2) );
			
			friendsWidget.addActorToTab(aloneImage, 1);
			friendsWidget.addActorToTab(dontHaveLabel, 1);
			friendsWidget.addActorToTab(findLabel, 1);
			friendsWidget.addActorToTab(goToFindButton, 1);
		}
		
		Label searchName = new Label("name:", skin, "coinLabel");
		
		TextField searchTextField = new TextField("", skin, "default");
		
		searchTextField.getStyle().background.setLeftWidth(10);
		searchTextField.getStyle().background.setBottomHeight(10);
		
		searchTextField.setSize(360f, 50f);
		setCenterPosition(searchTextField, 1040.0f);
		searchTextField.setX( searchTextField.getX() - 100.0f + 20 + searchName.getWidth() / 2f );
		searchTextField.setOnlyFontChars(true);
		searchTextField.setMaxLength(9);
		
		searchName.setPosition(searchTextField.getX() - 20 - searchName.getWidth(), searchTextField.getY() - 5);
		
		Button searchButton = new Button(skin, "search");
		searchButton.setPosition(searchTextField.getX() + searchTextField.getWidth() + 40, searchTextField.getY() - 20);
		
		CheckBox hideMeCheckBox = new CheckBox(" Hide me from others", skin, "default");
		hideMeCheckBox.setPosition(-400f, 1090f);
		
		friendsWidget.addActorToTab(searchName, 2);
		friendsWidget.addActorToTab(searchTextField, 2);	
		friendsWidget.addActorToTab(searchButton, 2);
		friendsWidget.setCurrentTab(1);
	}
	
	private void createRankWidget()
	{
		rankWidget = new Widget(Align.center, -350.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
		rankWidget.toggleWidget(); 
        
        Table table = new Table();
        //table.debug();
        
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
