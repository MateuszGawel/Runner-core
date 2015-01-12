package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Countdown;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.news.News;
import com.apptogo.runner.news.NewsManager;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;

public class GameScreenSingle extends GameScreen
{	
	Widget finishWidget;
	
	Label restartButton;
	Label backToMenuButton;
	Label timeLabel;
	Label coinsLabel;
	
	public GameScreenSingle(Runner runner)
	{
		super(runner);
		NotificationManager.getInstance().disableAppWarpNotifications();//uwaga - to powoduje ze tak czy siak jest wywolywana funkcja z notifManagera w Character (i na starcie w gameWorld) - moze spowalniac program :<
	}
	
	public void prepare() 
	{
		Logger.log(this, "prepare z gamescreensingle");
		player.setCharacterType( GameWorldType.convertToCharacterType( level.worldType ) );
		
		super.prepare();	
		
		createGui();
		createLabels();
		
		
		CustomActionManager.getInstance().registerAction(new CustomAction(2f, 1, player.character) {
			
			@Override
			public void perform() {
				new Countdown(world).startCountdown();
			}
		});
		
		createFinishWidget();
		
		gameGuiStage.addActor( finishWidget.actor() );
	}
	
	private void createFinishWidget()
	{
		finishWidget = new Widget("settings", Align.center, 1240.0f, 950.0f, WidgetType.FINISH, WidgetFadingType.TOP_TO_BOTTOM, true);
		finishWidget.setEasing( Interpolation.elasticOut );
	
		restartButton = createLabel( getLangString("restartLevel"), FontType.BIG);
		setCenterPosition(restartButton, 900f);
		restartButton.setX( restartButton.getX() + 640.0f + 250.0f );
		
		backToMenuButton = createLabel( getLangString("backToMenu"), FontType.BIG);
		setCenterPosition(backToMenuButton, 900f);
		backToMenuButton.setX( backToMenuButton.getX() + 640.0f - 250.0f );
				
        restartButton.addListener
        ( 
        		new ClickListener()
        		{
        			public void clicked(InputEvent event, float x, float y) 
                    {
        				//jeszcze raz ladujemy? a moze jakos go wrocic i jakos zrestartowac level? 
                    }
		});
        		        
		backToMenuButton.addListener
		( 
				new ClickListener()
				{
					public void clicked(InputEvent event, float x, float y) 
		            {
						ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		            }
		});
		
		timeLabel = createLabel( "0:45'23", FontType.BIG);
		coinsLabel = createLabel( "2300", FontType.BIG);
		
				
		Table finishTable = new Table();
				
		finishTable.add( timeLabel ).width(600.0f).height(45.0f).center().pad(0, 0, 20, 0);
		finishTable.row();
		finishTable.add( coinsLabel ).width(600.0f).height(45.0f).center().pad(0, 0, 0, 0);
				
		finishTable.setSize(600.0f, 110.0f);
		
		setCenterPosition(finishTable, 1100.0f);
		finishTable.setX( finishTable.getX() + 640.0f );
		
		finishTable.debug();
		
        finishWidget.addActor(backToMenuButton);
        finishWidget.addActor(restartButton);
        finishWidget.addActor(finishTable);  
	}
	
	@Override
	public void handleInput()
	{
		if( Gdx.input.isKeyPressed(Keys.F))
		{
			finishWidget.toggleWidget();
		}
		
		if( Input.isPressed() ) 
		{
			startLabel.remove();
		}
		super.handleInput();
	}
	
	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_GAME_SINGLE;
	}
}
