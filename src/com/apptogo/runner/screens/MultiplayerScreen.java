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
import com.apptogo.runner.player.Player;
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

public class MultiplayerScreen extends BaseScreen implements WarpListener
{			
	private TextButton joinRandomButton;
	
	private DialogWidget confirmationDialog;
	
	private Widget profileWidget;
	private Widget friendsWidget;
	private Widget shopWidget;
	private Widget rankingWidget;
	
	private Group achievementsGroup;
	
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
        joinRandomButton.setPosition( -368.0f, -300.0f );
        joinRandomButton.addListener( new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut( ScreenType.SCREEN_WAITING_ROOM );
            }
		});
        
        
        
        createRoomButton = new Button(skin, "inviteFriends");
        createRoomButton.setPosition( 205.0f, -300.0f );
        createRoomButton.addListener( friendsWidget.toggleWidgetListener );
		
		//manageProfileButton.addListener( manageWidget.toggleWidgetListener );
		
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
		profileWidget = new Widget(Align.center, -90.0f, 0.0f, WidgetType.MEDIUM, WidgetFadingType.NONE, false);
        profileWidget.toggleWidget();        
        		
		currentCharacterAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), -330.0f, 60.0f, true);
		currentCharacterAnimation.setVisible(true);
        
		Image ground = createImage( CharacterType.convertToGroundPath( player.getCharacterType() ) , -330.0f, 20.0f);
				        
        Table table = new Table();
		
		table.setSize(700.0f, 320.0f);
		table.setPosition(-350.0f, -40.0f);
		//table.debug();
		
		Image flag = createImage("temp/exampleFlag.png", 0, 0);
		Label playerName = createLabel( player.getName(), FontType.BLACKBOARDMEDIUM);
		
		table.add(flag).width(32.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).center().top();
		table.add(playerName).width(628.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f).left().top();
		
		table.row();
		table.add().width(32.0f).height(192.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);
		
		Table innerTable = new Table();
		
		innerTable.setSize(350.0f, 192.0f);
		//innerTable.debug();
		
		innerTable.add( createLabel("Ligue:", FontType.BLACKBOARDSMALL) ).width(170.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).align(Align.right);
		innerTable.add( createLabel("A", FontType.BLACKBOARDSMALL) ).width(100.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f);
		innerTable.add().width(140.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f);
		
		innerTable.row();
		innerTable.add( createLabel("Position:", FontType.BLACKBOARDSMALL) ).width(170.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).align(Align.right);
		innerTable.add( createLabel("123", FontType.BLACKBOARDSMALL) ).width(100.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f);
		innerTable.add().width(140.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f);
		
		innerTable.row();
		innerTable.add( createLabel("Best time:", FontType.BLACKBOARDSMALL) ).width(170.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).align(Align.right);
		innerTable.add( createLabel("01:00:12", FontType.BLACKBOARDSMALL) ).width(100.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f);
		innerTable.add().width(140.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f);
		
		innerTable.row();
		innerTable.add( createLabel("Wins:", FontType.BLACKBOARDSMALL) ).width(170.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f).align(Align.right);
		innerTable.add( createLabel("15", FontType.BLACKBOARDSMALL) ).width(100.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 0.0f);
		innerTable.add().width(140.0f).height(32.0f).pad(10.0f, 10.0f, 0.0f, 10.0f);
		
		table.add(innerTable).height(192.0f).pad(18.0f, 10.0f, 0.0f, 10.0f).top().right();
		
		table.row();
		table.add().colspan(2).height(50.0f).pad(18.0f, 10.0f, 0.0f, 0.0f);

		//--to ofc tymczasowe
		Image b1 = createImage("temp/b1.png", -300.0f, -40.0f);
		Image b2 = createImage("temp/b2.png", -100.0f, -40.0f);
		Image b3 = createImage("temp/b3.png", 100.0f, -40.0f);
		
		b1.addListener( shopWidget.getChangeWidgetTabListener(1) );
		b1.addListener( shopWidget.toggleWidgetListener );
		
		b2.addListener( shopWidget.getChangeWidgetTabListener(2) );
		b2.addListener( shopWidget.toggleWidgetListener );
		
		b3.addListener( shopWidget.getChangeWidgetTabListener(3) );
		b3.addListener( shopWidget.toggleWidgetListener );
		
		//--
		
		profileWidget.addActor(table);
		
		profileWidget.addActor(b1);
		profileWidget.addActor(b2);
		profileWidget.addActor(b3);
	
        profileWidget.addActor(ground);
        profileWidget.addActor(currentCharacterAnimation);
	}
	
	private void createFriendsWidget()
	{
		friendsWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		friendsWidget.setEasing( Interpolation.elasticOut );
		
		friendsWidget.addTabButton(1, "contactsTab");
		friendsWidget.addTabButton(2, "findFriendsTab");
		        
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
	
	private Image addAchievement(String achievmentName)
	{
		Image image = createImage("temp/"+achievmentName+".png", 0,0);//, achievmentMargin, -achievmentMarginY);
		/*Label label = createLabel(achievmentName, FontType.WOODFONT);
		label.setPosition(image.getX() + (image.getWidth()/2.0f) - (label.getWidth()/2.0f), image.getY() - 60.0f);
		
		achievmentMargin += 256.0f;
		if(achievmentMargin > 512.0f) achievmentMargin = 0;
		if(achievmentMargin == 0) achievmentMarginY += 256.0f;
		
		achievementsGroup.addActor(image);
		achievementsGroup.addActor(label);*/
		
		return image;
	}
	
	private void createShopWidget()
	{
		shopWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		shopWidget.setEasing( Interpolation.elasticOut );
		
		shopWidget.addTabButton(1, "profileTab");
		shopWidget.addTabButton(2, "shopTab");
		shopWidget.addTabButton(3, "achievementsTab");
		
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
             
        achievementsGroup = new Group();
        
        achievementsGroup.setBounds(-400, 1000, 800, 400);
                
        /*addAchievement("a1");
        addAchievement("a2");
        addAchievement("a3");
        addAchievement("a4");
        addAchievement("a5");
        addAchievement("a6");
        addAchievement("a7");
        addAchievement("a8");
        addAchievement("a9");
        */
        
		shopWidget.addActorToTab(removePlayer, 1);
		
		//shopWidget.addActorToTab(achievementsGroup, 3);
		
		String reallyLongString = "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n"
		        + "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n"
		        + "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n";

		//--
        final Label text = new Label(reallyLongString, skin);
        text.setAlignment(Align.center);
        text.setWrap(true);

        final Table scrollTable = new Table();
        //scrollTable.add(text);
        scrollTable.add( addAchievement("a1") ).pad(20.0f);
        scrollTable.add();
        scrollTable.add();
        scrollTable.add();
        scrollTable.add( addAchievement("a2") ).pad(20.0f);
        scrollTable.add( addAchievement("a3") ).pad(20.0f);
        scrollTable.row();
        scrollTable.add( addAchievement("a4") ).pad(20.0f);
        scrollTable.add( addAchievement("a5") ).pad(20.0f);
        scrollTable.add( addAchievement("a6") ).pad(20.0f);
        scrollTable.row();
        scrollTable.add( addAchievement("a7") ).pad(20.0f);
        scrollTable.add( addAchievement("a8") ).pad(20.0f);
        scrollTable.add( addAchievement("a9") ).pad(20.0f);

        scrollTable.debug();
        
        final ScrollPane scroller = new ScrollPane(scrollTable, skin);
        scroller.setScrollingDisabled(true, false);
        scroller.setFadeScrollBars(false);
        
        Container<ScrollPane> container = new Container<ScrollPane>();
        container.setSize(920, 480);
        container.setPosition(-500, 670);
        container.setActor(scroller);
        
		shopWidget.addActorToTab(container, 3);
		
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
