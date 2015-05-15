package com.apptogo.runner.screens;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.shop.ShopItem;
import com.apptogo.runner.shop.ShopManager;
import com.apptogo.runner.widget.ShopWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Group;
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
	private ShopWidget descriptionWidget;
	
	private Label coinLabel;
	
	private Table table;
		
	public ShopScreen(Runner runner)
	{
		super(runner);
	}
	
	public void prepare() 
	{	
		setBackground("mainMenuScreenBackground");
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
               
        descriptionWidget = new ShopWidget(); //Widget(Align.center, Align.center, 0, WidgetType.SMALL, WidgetFadingType.NONE, true);
        
        createShopWidget();
        
        coinLabel = new Label("coins: " + String.valueOf(player.coins), skin, "coinLabel");
        coinLabel.setPosition(320, 320);
        
        addToScreen( shopWidget.actor() );
        addToScreen( descriptionWidget.actor() );
        addToScreen(backButton);
        addToScreen(coinLabel);
	}
	
	private void createShopWidget()
	{
		shopWidget = new Widget(Align.center, -360.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
	
		shopWidget.setCurrentTab(1);
		
        table = new Table();
        table.debug();
                
        fillTable();
                
        Container<ScrollPane> container = createScroll(table, 800.0f, 633.0f, true);
        container.debug();
        container.setPosition(-400.0f, -350.0f);
        
        shopWidget.addActorToTab(container, 1);
        
        shopWidget.toggleWidget(); 
	}
	
	private void fillTable()
	{
		for(final ShopItem item : ShopManager.getInstance().getShopItems( player ))
        {
        	table.row().pad(0, 0, 0, 0);
        	
        	Image image = createImage(item.thumbnailName, 0, 0);
        	image.setSize(150, 150);
        	
        	table.add(image).width(150).height(150).pad(15, 30, 15, 10);
        	
        	//description + stars
        	Group descriptionGroup = new Group();
        	
        	Label title = new Label(item.title,  skin, "default");
        	title.setPosition(10, 100);
        	title.setWrap(true);
        	
        	descriptionGroup.addActor( title );
        	
        	for(int i = 0; i < item.maxLevel; i++)
        	{
        		if(i < item.currentLevel)
        		{
        			descriptionGroup.addActor( this.createImage("starSmallFull", (i*50)+((i+1) * 10), 10) );
        		}
        		else
        		{
        			descriptionGroup.addActor( this.createImage("starSmallEmpty", (i*50)+((i+1) * 10), 10) );
        		}
        	}
        	
        	table.add(descriptionGroup).width(340).height(150).pad(15, 15, 15, 15);
        	
        	Group buyGroup = new Group();
        	
        	TextButton buyButton = new TextButton("BUY", this.skin);
        	buyButton.setSize(120, 90);
        	buyButton.setPosition(35, 45);
        	
        	buyButton.addListener(getBuyListener(item));
        	
        	buyGroup.addActor(buyButton);
        	        	
        	Label cost = new Label(item.getCostLabel(),  skin, "default");
        	cost.setPosition(95 - cost.getWidth()/2, 10);
        	
        	buyGroup.addActor(cost);
        	
        	table.add(buyGroup).width(190).height(150).pad(15, 15, 15, 35);
        }
	}
	
	private ClickListener getBuyListener(final ShopItem item)
	{
		ClickListener listener = new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {
            	descriptionWidget.setItem( item );
            	descriptionWidget.toggleWidget();
            	
            	descriptionWidget.setListener(new ClickListener() 
            	{
			        public void clicked(InputEvent event, float x, float y) 
			        {
			        	if( !ShopManager.getInstance().buyShopItem(descriptionWidget.item, player) )
			        	{
			        		descriptionWidget.shakePrice();
			        	}
			        	else
			        	{		
			        		coinLabel.setText("coins: " + String.valueOf(player.coins));
			        		
			        		table.clear();
			        		fillTable();
			        		
			        		descriptionWidget.toggleWidget();
			        	}
			        }
			    });
            }
		};
	    
	    return listener;
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
