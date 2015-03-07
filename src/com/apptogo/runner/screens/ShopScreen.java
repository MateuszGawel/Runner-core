package com.apptogo.runner.screens;

import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ShopManager;
import com.apptogo.runner.handlers.ShopManager.ShopItem;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ShopScreen extends BaseScreen
{		
	Button backButton;
	
	private Widget shopWidget;
		
	public ShopScreen(Runner runner)
	{
		super(runner);	
		
		loadPlayer();
		
		fadeInOnStart();
	}
	
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
        
        createShopWidget();
        
        addToScreen( shopWidget.actor() );
        addToScreen(backButton);
	}
	
	private void createShopWidget()
	{
		shopWidget = new Widget(Align.center, -360.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
		
		TextButton cat1B = new TextButton("C1", skin, "categoryTab");
		cat1B.setSize(230.0f, 75.0f);
		cat1B.setPosition(-350.0f, 70.0f);
		
		TextButton cat2B = new TextButton("C2", skin, "categoryTab");
		cat2B.setSize(230.0f, 75.0f);
		cat2B.setPosition(-90.0f, 70.0f);
		
		TextButton cat3B = new TextButton("C3", skin, "categoryTab");
		cat3B.setSize(230.0f, 75.0f);
		cat3B.setPosition(170.0f, 70.0f);
		
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
        
        for(ShopItem item : ShopManager.getInstance().getPowerups())
        {
        	table.row().pad(0, 0, 0, 0);
        	table.add().width(50).height(200);
        	
        	Image image = createImage(item.thumbnailPath, 0, 0);
        	image.setSize(150, 150);
        	Label title = createLabel("Super hipnotyzer", FontType.BLACKBOARDMEDIUM, true);
        	Label cash = createLabel("3000$", FontType.BLACKBOARDSMALL);
        	Label itDes = createLabel(item.description, FontType.BLACKBOARDSMALL, true);
        	
        	table.add(image).width(150).height(150).center();
        	table.add().width(25).height(200);
        	
        	Table descTable = new Table();
        	descTable.debug();
        	
        	descTable.add(title).width(400).height(100);
        	descTable.row();
        	descTable.add(cash).width(400).height(40);
        	descTable.row();
        	descTable.add().width(400).height(60);
        	
        	table.add(descTable).width(400).height(200);
        	
        	table.add().width(150).height(200);
        	table.add().width(50).height(200);
        	
        	//table.row();
        	//table.add().width(800).height(75).colspan(4);
        	//table.row();
        	        	
        	/*
        	Table innerTable = new Table();
        	innerTable.debug();
        	
        	innerTable.add(title).width(390).height(50);
        	innerTable.row();
        	innerTable.add(itDes).width(390).height(150);
        	
        	table.add(innerTable).width(390).height(200).pad(15, 20, 15, 10);
        		
        	table.add().width(80).height(200).pad(15,  0, 15, 20);
        	
        	table.row();*/
        }
                
        Container<ScrollPane> container = createScroll(table, 800.0f, 420.0f, true);
        container.debug();
        container.setPosition(-400.0f, -360.0f);
        
        shopWidget.addActorToTab(container, 1);
        
        shopWidget.toggleWidget(); 
	}
		
	public void step()
	{
		handleInput();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			WarpController.getInstance().stopApp();
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
	}
	
	@Override
	public void hide() 
	{
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_SHOP;
	}
}
