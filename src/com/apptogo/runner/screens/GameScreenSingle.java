package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Countdown;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameScreenSingle extends GameScreen
{	
	Widget finishWidget;
	
	Label restartButton;
	Label backToMenuButton;
	Label timeLabel;
	Label coinsLabel;
	
	public GameScreenSingle(Runner runner, Level level, Array<Player> enemies)
	{
		super(runner, level, enemies);
		NotificationManager.getInstance().disableAppWarpNotifications();
	}
	
	public void prepare() 
	{
		player.setCharacterType( GameWorldType.convertToCharacterType( level.worldType ) );
		
		super.prepare();	
		
		createGui();
		createLabels();
/*ODKOMENTOWAC IF W CHARACTER START() !!!
		CustomActionManager.getInstance().registerAction(new CustomAction(2f, 1, player.character) {
			
			@Override
			public void perform() {
				
				if( ScreensManager.getInstance().getCurrentScreenClass() == ScreenClass.GAME)
				{
					new Countdown(gameWorld).startCountdown();
				}
			}
		});
*/
		player.character.start(); //i to tez stad wywalic przy uruchomieniu countdown
		enemies.forEach(enemy -> enemy.character.start());
		createFinishWidget();
		
		gameGuiStage.addActor( finishWidget.actor() );
	}
	
	private void createFinishWidget()
	{
		finishWidget = new Widget(Align.center, 1240.0f, 950.0f, WidgetType.FINISH, WidgetFadingType.TOP_TO_BOTTOM, true);
		finishWidget.setEasing( Interpolation.elasticOut );
	
		restartButton = new Label( getLangString("restartLevel"), skin, "default");
		setCenterPosition(restartButton, 900f);
		restartButton.setX( restartButton.getX() + 640.0f + 250.0f );
		
		backToMenuButton = new Label( getLangString("backToMenu"), skin, "default");
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
		
		timeLabel = new Label( "0:45'23", skin, "default");
		coinsLabel = new Label( "2300", skin, "default");
		
				
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
		
		if( Gdx.input.isKeyPressed(Keys.W))
		{
			player.character.dieBottom();
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
