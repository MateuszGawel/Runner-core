package com.apptogo.runner.widget;

import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class Widget
{	private static int CTR = 0;
	private String name = "";

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
	private final int WIDGET_TAB_COUNT = 5;
	
	private Skin skin;
	
	protected Widget widget = this;
	
	public Group window;
	
	private Texture widgetBackgroundTexture;
	private Image widgetBackground;
	
	private Button blackOutButton;
	private Button closeWidgetButton;
	
	public ClickListener hideWidgetListener;
	public ClickListener toggleWidgetListener;
	
	protected float hiddenPartWidth;

	private WidgetType widgetType;
	private WidgetFadingType fadeInType;
	
	private Interpolation interpolation;
	
	private boolean isShowed = false;
	private boolean setBlackOut = false;
	
	protected Array< Array<Actor> > widgetTab;
	protected int currentTab = 0;
	
	public boolean getToFrontOnClick = false;
	
	public Widget(String name, float x, float y, float hiddenPartWidth, WidgetType widgetType, WidgetFadingType fadeInType, boolean blackOut)
	{
		this(x,y,hiddenPartWidth,widgetType,fadeInType,blackOut);
		this.name = name;
	}
	/** @param hiddenPartWidth Jesli fading nie porusza widgetem to powinien byc ustawiony na 0 
	 *  @param x Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w pionie
	 *  @param y Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w poziomie*/
	public Widget(float x, float y, float hiddenPartWidth, WidgetType widgetType, WidgetFadingType fadeInType, boolean blackOut)
	{			
		this.skin = ResourcesManager.getInstance().getUiSkin();
		
		this.hiddenPartWidth = hiddenPartWidth;
		this.widgetType = widgetType;
		this.fadeInType = fadeInType;
		this.setBlackOut = blackOut;
		
		initializeListeners();
		
		initializeBlackOutButton();
					
		initializeWindow();
		
		initializeWidgetTabs();
				
		float width = WidgetType.getWidth( this.widgetType );
		float height = WidgetType.getHeight(this.widgetType);
		x = ( x == (float)Align.center )?( -width/2.0f ):x;
		y = ( y == (float)Align.center )?( -height/2.0f ):y;
		
		window.addActor( createWindow(x, y, width, height) );
	}
	
	//---Initializing widget
	private void initializeListeners()
	{
		hideWidgetListener = new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {
                 widget.hideWidget();
            }
        };
        
        toggleWidgetListener = new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {
                 widget.toggleWidget();
            }
        };
	}
	
	private void initializeBlackOutButton()
	{
		blackOutButton = new Button(skin, "blackOut");
		//blackOutButton.setSize(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		//chcemy miec pewnosc ze pokryje caly ekran, a takiej rozdzielczosci nikt nie przebije chyba
		blackOutButton.setSize(4096.0f, 4096.0f);
		//blackOutButton.setPosition( -(Runner.SCREEN_WIDTH / 2f), -(Runner.SCREEN_HEIGHT / 2f));
		blackOutButton.setPosition( -2048.0f, -2048.0f);
		
		blackOutButton.addListener( hideWidgetListener );
		
		blackOutButton.setVisible(false);
	}

	private void initializeWindow()
	{
		this.window = new Group();
		
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
	
	private Window createWindow(float x, float y, float width, float height)
	{			
		Window win = new Window("", this.skin, "default");
		win.setSize(width, height);
		win.setPosition(x, y);
		win.clearListeners();
				
		widgetBackgroundTexture = new Texture( Gdx.files.internal( WidgetType.getWidgetBackgroundPath( this.widgetType ) ) );
		widgetBackgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		widgetBackground = new Image( widgetBackgroundTexture );
		widgetBackground.setPosition(0f, 0f);
		
		closeWidgetButton = new Button(skin, "closeWidget");
		closeWidgetButton.setPosition(win.getWidth() - 100.0f, win.getHeight() - 100.0f);
		closeWidgetButton.addListener( hideWidgetListener );
		
		if( !(WidgetType.showCloseWidgetButton( this.widgetType )) )
		{
			closeWidgetButton.setVisible(false);
		}
		
		win.addActor(widgetBackground);
		win.addActor(closeWidgetButton);
		
		return win;
	}

	public void setEasing(Interpolation interpolation)
	{
		this.interpolation = interpolation;
	}
	//---
	
	//---Getting and adding actors
	public Group actor()
	{		
		final Group widgetGroup = new Group();
		
		widgetGroup.addActor(blackOutButton);
		
		widgetGroup.addActor(window);
		
		if( getToFrontOnClick )
		{
			widgetGroup.addListener( new ClickListener()
			{
				
				public void clicked(InputEvent event, float x, float y) 
	            {
					widgetGroup.toFront();
	            }
				
			}
			);

		}
		
		return widgetGroup;
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
	}
	
	public void showWidget()
	{
		if( !(this.isShowed) ) 
		{
			if( setBlackOut )
			{
				blackOutButton.setVisible(true);
				blackOutButton.toFront();
			}
			
			playFading(false);
			this.isShowed = true;
			
			setCurrentTabVisibile(true);
			
			this.window.toFront();
		}
	}
	
	public void hideWidget()
	{
		if( this.isShowed ) 
		{
			blackOutButton.setVisible(false);
			
			playFading(true);
			this.isShowed = false;
			
			setCurrentTabVisibile(false);
		}
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
	
	public void setCurrentTabVisibile(boolean visibility)
	{
		for(Actor actor: widgetTab.get(currentTab))
		{
			actor.setVisible(visibility);
		}
	}
	
	public void changeWidgetTab(int widgetTabIndex)
	{
		setCurrentTabVisibile(false);
		
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
		
		setCurrentTabVisibile(true);
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
		Button activeButton = new Button(skin, "tempTab");//buttonName);
		activeButton.setX( -550.0f + ((tabIndex - 1) * 300)  );
		activeButton.setY( 1160.0f );
		activeButton.addListener( this.getChangeWidgetTabListener( tabIndex ) );
		
		Button nonActiveButton = new Button(skin, "tempTab");//buttonName);
		nonActiveButton.setX( -550.0f + ((tabIndex - 1) * 300)  );
		nonActiveButton.setY( 1160.0f );
		nonActiveButton.addListener( this.getChangeWidgetTabListener( tabIndex ) );
		
		nonActiveButton.getColor().a /= 3.0f;
		
		activeButton.setVisible(false);
		nonActiveButton.setVisible(false);
		
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
	public boolean isShowed()
	{
		return this.isShowed;
	}
		
	public WidgetType getWidgetType()
	{
		return this.widgetType;
	}
	
	public void dispose()
	{
		this.widget = null;
		this.widgetBackgroundTexture.dispose();
	}
}
