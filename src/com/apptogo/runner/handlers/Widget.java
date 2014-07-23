package com.apptogo.runner.handlers;

import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Widget
{	
	public enum WidgetType
	{
		SMALL,
		MEDIUM,
		BIG
	}
	
	public enum WidgetFadingType
	{
		NONE,
		ALPHA_ANIMATION,
		LEFT_TO_RIGHT,
		RIGHT_TO_LEFT,
		TOP_TO_BOTTOM,
		BOTTOM_TO_TOP
	}
	
	private float FADE_IN_DURATION = 0.55f;
	
	private float x;
	private float y;
	private float hiddenPartWidth;
	private float width;
	private float height;
	private Group group;
	
	private Group groupWithBlackOutButton;
	private Button blackOutButton;
	
	private Container container;
	private ScrollPane scrollPane;
	private Window win;
	
	private Skin skin;
	
	private WidgetType widgetType;
	private WidgetFadingType fadeInType;
	
	private Interpolation interpolation;
	
	private boolean isShowed = false;
	private boolean setBlackOut = false;
	
	protected Widget widget = this;
	
	/** @param hiddenPartWidth Jesli fading nie porusza widgetem to powinien byc ustawiony na 0 
	 *  @param x Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w pionie
	 *  @param y Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w poziomie*/
	public Widget(float x, float y, float hiddenPartWidth, WidgetType widgetType, WidgetFadingType fadeInType, boolean blackOut)
	{	
		this.skin = ResourcesManager.getInstance().getUiSkin();
		
		groupWithBlackOutButton = new Group();
		
		initializeBlackOutButton();
		this.setBlackOut = blackOut;
		
		this.widgetType = widgetType;
		this.fadeInType = fadeInType;
		
		createWindow();	

		if( x == (float)Align.center ) this.x = ((Runner.SCREEN_WIDTH - this.width) / 2.0f) - (Runner.SCREEN_WIDTH / 2.0f);
		else this.x = x;
		
		if( y == (float)Align.center ) this.y = ((Runner.SCREEN_HEIGHT - this.height) / 2.0f) - (Runner.SCREEN_HEIGHT / 2.0f);
		else this.y = y;
		
		this.win.setPosition(this.x, this.y);
		
		this.hiddenPartWidth = hiddenPartWidth;
		
		group = new Group();
		
		if( this.fadeInType == WidgetFadingType.ALPHA_ANIMATION || this.fadeInType == WidgetFadingType.NONE)
		{
			group.setVisible(false);
		}	
		
		group.addActor(win);
	}
	
	public void addActor(Actor actor)
	{
		group.addActor(actor);
	}
	
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
			}
			
			playFading(false);
			this.isShowed = true;
		}
	}
	
	public void hideWidget()
	{
		if( this.isShowed ) 
		{
			blackOutButton.setVisible(false);
			
			playFading(true);
			this.isShowed = false;
		}
	}
	
	private void playFading(boolean hide) //or show[false]
	{
		TemporalAction action = null;
		
		resetPosition();
		
		if( this.fadeInType == WidgetFadingType.NONE ) //ten przypadek wymaga specjalnego potraktowania (bo tu nie ma zadnej akcji)
		{
			if( hide ) 
			{
				this.group.setVisible(false);
			}
			else 
			{
				this.group.setVisible(true);
			}
			
			return; 
		}
		else if( this.fadeInType == WidgetFadingType.ALPHA_ANIMATION )
		{
			this.group.setColor( this.group.getColor().r, this.group.getColor().g, this.group.getColor().b, 0f);
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
		action.setReverse(hide);
		
		action.setInterpolation(interpolation);
		
		this.group.setVisible(true);
		this.group.addAction(action);
	}
		
	public Group actor()
	{
		groupWithBlackOutButton.addActor(group);
		return groupWithBlackOutButton;
	}
	
	private void createWindow()
	{		
		String styleName = "";
		
		if( this.widgetType == WidgetType.SMALL )
		{
			styleName = "small";
			this.width = 800f;
			this.height = 320f;
		}
		else if( this.widgetType == WidgetType.MEDIUM )
		{
			styleName = "medium";
			this.width = 960f;
			this.height = 480f;
		}
		else //if( this.widgetType == WidgetType.BIG )
		{
			styleName = "big";
			this.width = 1024f;
			this.height = 640f;
		}		
		
		this.win = new Window("", this.skin, styleName);
		this.win.setSize(this.width, this.height);
	}
	
	private void resetPosition()
	{
		this.group.setPosition(0f, 0f);
	}

	public void setToggleButton(boolean setToggleButton)
	{
		this.setToggleButton(setToggleButton, 0f, 0f);
	}
	
	/** @param marginVertical przesuniecie przycisku w pionie [Align.center aby wysrodkowac]
	 *  @param marginHorizontal przesuniecie przycisku w poziomie [Align.center aby wysrodkowac] */
	public void setToggleButton(boolean setToggleButton, float marginHorizontal, float marginVertical) 
	{
		if( setToggleButton )
		{
			Button button = new Button();
			
			//---ustawienie typu button i jego wymiarow
			if( this.fadeInType == WidgetFadingType.LEFT_TO_RIGHT || this.fadeInType == WidgetFadingType.RIGHT_TO_LEFT )
			{
				button = new Button(this.skin, "toggleWidgetVertical");
				button.setSize(50f, 150f);
			}
			else if( this.fadeInType == WidgetFadingType.TOP_TO_BOTTOM || this.fadeInType == WidgetFadingType.BOTTOM_TO_TOP )
			{
				button = new Button(this.skin, "toggleWidgetHorizontal");
				button.setSize(150f, 50f);
			}
			else
			{
				Logger.log(this, "toggleButton dozwolony tylko dla fadingu zmieniajacego polozenie widgetu!");
				return;
			}
			//-----------------------------------------
			
			//---ustawienie pozycji buttona
			if( this.fadeInType == WidgetFadingType.LEFT_TO_RIGHT)
			{
				button.setPosition( this.x + this.width, this.y + this.height - button.getHeight() );
			}
			else if( this.fadeInType == WidgetFadingType.RIGHT_TO_LEFT)
			{
				button.setPosition( this.x - button.getWidth(), this.y + this.height - button.getHeight() );
			}
			else if( this.fadeInType == WidgetFadingType.TOP_TO_BOTTOM)
			{
				button.setPosition( this.x + this.width - button.getWidth(), this.y - button.getHeight() );
			}
			else //if( this.fadeInType == WidgetFadingType.BOTTOM_TO_TOP)
			{
				button.setPosition( this.x + this.width - button.getWidth(), this.y + this.height );
			}
			
			button.setPosition( button.getX() + marginHorizontal, button.getY() + marginVertical);
			if( marginHorizontal == (float)Align.center )
			{
				button.setX( this.x + ((this.width - button.getWidth()) / 2f) );
			}
			if( marginVertical == (float)Align.center )
			{
				button.setY( this.y + ((this.height - button.getHeight()) / 2f) );
			}
			//------------------------
			
			button.addListener(new ClickListener() {
	            public void clicked(InputEvent event, float x, float y) 
	            {
	                 widget.toggleWidget();
	            }
	         });
			
			this.group.addActor(button);
		}
		else {}
	}
	
	private void initializeBlackOutButton()
	{
		blackOutButton = new Button(skin, "blackOut");
		blackOutButton.setSize(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		blackOutButton.setPosition( -(Runner.SCREEN_WIDTH / 2f), -(Runner.SCREEN_HEIGHT / 2f));
		
		blackOutButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 widget.hideWidget();
            }
         });
		
		blackOutButton.setVisible(false);
		
		groupWithBlackOutButton.addActor(blackOutButton);
	}
	
	public ClickListener getToggleListener()
	{
		return new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	widget.toggleWidget();
            }
		};
	}
	
	public void setEasing(Interpolation interpolation)
	{
		this.interpolation = interpolation;
	}
}
