package com.apptogo.runner.handlers;

import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

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
	
	private float FADE_IN_DURATION = 0.15f;
	
	private float x;
	private float y;
	private float hiddenPartWidth;
	private float width;
	private float height;
	private Skin skin;
	private Group group;
	private Container container;
	private ScrollPane scrollPane;
	private Window win;
	
	private WidgetType widgetType;
	private WidgetFadingType fadeInType;
	
	private boolean isShowed = false;
	protected Widget widget = this;
	
	/** @param hiddenPartWidth Jesli fading nie porusza widgetem to powinien byc ustawiony na 0 
	 *  @param x Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w pionie
	 *  @param y Ustawienie na 1 [Align.center] spowoduje wysrodkowanie w poziomie*/
	public Widget(float x, float y, float hiddenPartWidth, WidgetType widgetType, WidgetFadingType fadeInType)//, float width, float height, WidgetOrientation orientation)
	{	
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
	
	public void showWidget()
	{
		if( !(this.isShowed) ) toggleWidget(false);
	}
	
	public void hideWidget()
	{
		if( this.isShowed ) toggleWidget(true);
	}
	
	private void toggleWidget(boolean hide) //or show[false]
	{
		TemporalAction action = null;
		
		resetPosition();
		
		if( this.fadeInType == WidgetFadingType.NONE ) //ten przypadek wymaga specjalnego potraktowania (bo tu nie ma zadnej akcji)
		{
			if( hide ) 
			{
				this.group.setVisible(false);
				isShowed = false;
			}
			else 
			{
				this.group.setVisible(true);
				isShowed = true;
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
		
		this.group.setVisible(true);
		this.group.addAction(action);
		
		this.isShowed = true;
	}
		
	public Group actor()
	{
		return group;
	}
	
	private void createWindow()
	{
		Skin skin = ResourcesManager.getInstance().getUiSkin();
		
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
		
		this.win = new Window("", skin, styleName);
		this.win.setSize(this.width, this.height);
	}
	
	private void resetPosition()
	{
		this.group.setPosition(0f, 0f);
	}
}
