package com.apptogo.runner.widget;

import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class Widget
{	
	public enum WidgetFadingType
	{
		NONE,
		ALPHA_ANIMATION,
		LEFT_TO_RIGHT,
		RIGHT_TO_LEFT,
		TOP_TO_BOTTOM,
		BOTTOM_TO_TOP
	}
	
	private final float FADE_IN_DURATION = 0.75f;
	private final int WIDGET_TAB_COUNT = 10;
	
	protected Widget widget = this;
	
	public Group window;
	
	private Image widgetBackground;
	private Image blackOut;
	private Image closeWidget;
	
	public ClickListener hideWidgetListener;
	public ClickListener toggleWidgetListener;
	
	protected float hiddenPartWidth;

	private WidgetFadingType fadeInType;
	
	private Interpolation interpolation;
	
	private boolean isShowed = false;
	private boolean setBlackOut = false;
	
	protected Array< Array<Actor> > widgetTab;
	protected int currentTab = 0;
	
	//x/y Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w pionie/poziomie
	public Widget(float x, float y, float hiddenPartWidth, WidgetType widgetType, WidgetFadingType fadeInType, boolean blackOut)
	{
		this.hiddenPartWidth = hiddenPartWidth;
		this.fadeInType = fadeInType;
		this.setBlackOut = blackOut;
		
		float width = WidgetType.getWidth( widgetType );
		float height = WidgetType.getHeight( widgetType );
		x = ( x == (float)Align.center )?( -width/2.0f ):x;
		y = ( y == (float)Align.center )?( -height/2.0f ):y;
		
		hideWidgetListener = new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {
            	if( widget.isShowed )
            	{
            		widget.toggleWidget();
            	}
            }
        };
        
        toggleWidgetListener = new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {
                 widget.toggleWidget();
            }
        };
					
		initializeWindow(x, y, width, height, WidgetType.getWidgetBackgroundRegionName( widgetType ), WidgetType.showCloseWidgetButton( widgetType ));
		
		initializeWidgetTabs();
				
		this.hideWidget();
	}
	
	//---Initializing widget
	private void initializeWindow(float x, float y, float width, float height, String backgroundRegionName, boolean showCloseButton)
	{
		this.window = new Group();
		this.window.setTransform(false);
		
		blackOut = new Image( ResourcesManager.getInstance().getAtlasRegion("widgetBlack") );
		blackOut.setSize(4096.0f, 4096.0f);
		blackOut.setPosition( -2048.0f, -2048.0f);
		
		blackOut.addListener( hideWidgetListener );
		
		blackOut.setVisible(false);
		
		this.window.addActor(this.blackOut);
		
		AtlasRegion region = null;
		
		if(backgroundRegionName != null)
		{	
			region = ResourcesManager.getInstance().getAtlasRegion(backgroundRegionName);
			widgetBackground = new Image( region );
			widgetBackground.setPosition(x, y);
		}
		
		closeWidget = new Image( ResourcesManager.getInstance().getAtlasRegion("closeWidget") );
		closeWidget.setPosition( x + width - 100.0f, y + height - 100.0f);
		closeWidget.addListener( hideWidgetListener );
		
		if( !showCloseButton )
		{
			closeWidget.setVisible(false);
		}
		
		if(widgetBackground != null)
		{
			this.window.addActor(widgetBackground);
		}
		
		this.window.addActor(closeWidget);
		
		if( this.fadeInType == WidgetFadingType.ALPHA_ANIMATION || this.fadeInType == WidgetFadingType.NONE)
		{
			window.setVisible(false);
		}	
	}

	private void initializeWidgetTabs()
	{
		this.widgetTab = new Array< Array<Actor> >();
		
		for(int i = 0; i < WIDGET_TAB_COUNT; i++) 
		{
			widgetTab.add( new Array<Actor>() );
		}
	}
	
	public void setEasing(Interpolation interpolation)
	{
		this.interpolation = interpolation;
	}
	//---
	
	//---Getting and adding actors
	public Group actor()
	{		
		return this.window;
	}
	
	public void addActor(Actor actor)
	{
		window.addActor(actor);
	}
	
	public void addActorToTab(Actor actor, int tabIndex)
	{
		widgetTab.get(tabIndex).add(actor);
		
		actor.setVisible(false);
		window.addActor(actor);
	}
	//---
	
	//---Toggling widget
	public void toggleWidget()
	{
		if( this.isShowed ) this.hideWidget();
		else this.showWidget();
		
		this.isShowed = !this.isShowed;
	}
	
	public void showWidget()
	{
		blackOut.setVisible(setBlackOut);
		
		this.window.setVisible(true);
		
		setCurrentTabVisible(true);
		
		this.window.toFront();
		playFading(false);
	}
	
	public void hideWidget()
	{
		blackOut.setVisible(false);
		
		this.window.setVisible(false);
		
		setCurrentTabVisible(false);
		
		playFading(true);
	}

	private void playFading(boolean hide)
	{
		TemporalAction action = null;
				
		this.window.setPosition(0f, 0f);
		
		if( this.fadeInType == WidgetFadingType.NONE )
		{
			if( hide ) 
			{
				this.window.setVisible(false);
			}
			else 
			{
				this.window.setVisible(true);
			}
			
			return; 
		}
		else if( this.fadeInType == WidgetFadingType.ALPHA_ANIMATION )
		{
			this.window.setColor( this.window.getColor().r, this.window.getColor().g, this.window.getColor().b, 0f);
			action = Actions.alpha( 1f, FADE_IN_DURATION, Interpolation.sine ); 		
		}
		else if( this.fadeInType == WidgetFadingType.TOP_TO_BOTTOM )
		{			
			action = Actions.moveTo( 0f, -this.hiddenPartWidth, FADE_IN_DURATION);	
		}
		else if( this.fadeInType == WidgetFadingType.BOTTOM_TO_TOP )
		{			
			action = Actions.moveTo( 0f, this.hiddenPartWidth, FADE_IN_DURATION);
		}
		else if( this.fadeInType == WidgetFadingType.LEFT_TO_RIGHT )
		{			
			action = Actions.moveTo( this.hiddenPartWidth, 0f, FADE_IN_DURATION );	
		}
		else if( this.fadeInType == WidgetFadingType.RIGHT_TO_LEFT)
		{			
			action = Actions.moveTo( -this.hiddenPartWidth, 0f, FADE_IN_DURATION );	
		}
		
		action.restart();
		
		if( !hide ) 
		{
			action.setInterpolation(interpolation);
		}
		else
		{
			action.setReverse(true);
			action.setDuration(0);
		}
		
		this.window.clearActions();
		this.window.addAction(action);
	}
	//---
	
	//---Operating on widget tabs
	public void setCurrentTab(int widgetTabIndex)
	{
		currentTab = widgetTabIndex;
	}
	
	public void setCurrentTabVisible(boolean visibility)
	{
		for(Actor actor: widgetTab.get(currentTab))
		{
			actor.setVisible(visibility);
		}
	}
	
	public void changeWidgetTab(int widgetTabIndex)
	{
		setCurrentTabVisible(false);
		
		if(widgetTabIndex >= 0 && widgetTabIndex < WIDGET_TAB_COUNT)
		{
			currentTab = widgetTabIndex;
		}
		else if(widgetTabIndex < 0 )
		{
			currentTab = 0;
		}
		else
		{
			currentTab = WIDGET_TAB_COUNT - 1;
		}
		
		setCurrentTabVisible(true);
	}

	public ClickListener getChangeWidgetTabListener(final int widgetTabIndex)
	{
		return new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {
            	widget.changeWidgetTab(widgetTabIndex);
            }
		};
	}
	
	public void addTabButton(int tabIndex, String buttonName)
	{
		Image activeButton = new Image( ResourcesManager.getInstance().getAtlasRegion(buttonName) );
		activeButton.setX( -550.0f + ((tabIndex - 1) * 300)  );
		activeButton.setY( 1160.0f );
		
		Image nonActiveButton = new Image( ResourcesManager.getInstance().getAtlasRegion(buttonName) );
		nonActiveButton.setX( -550.0f + ((tabIndex - 1) * 300)  );
		nonActiveButton.setY( 1160.0f );
		
		nonActiveButton.getColor().a /= 3.0f;
		
		activeButton.setVisible(false);
		nonActiveButton.setVisible(false);
		
		addTabButton(tabIndex, activeButton, nonActiveButton);
	}
		
	public void addTabButton(int tabIndex, Image activeButton, Image nonActiveButton)
	{
		activeButton.addListener( this.getChangeWidgetTabListener( tabIndex ) );
		nonActiveButton.addListener( this.getChangeWidgetTabListener( tabIndex ) );
		
		int counter = 0;
		
		for(Array<Actor> tab: widgetTab)
		{
			if(counter == tabIndex)
			{
				tab.add(activeButton);
				window.addActor(activeButton);
			}
			else
			{
				tab.add(nonActiveButton);
				window.addActor(nonActiveButton);
			}
			
			counter++;
		}
	}
	//---		
	public void dispose()
	{
		this.widget = null;
	}
}
