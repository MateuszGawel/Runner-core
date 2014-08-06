package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelManager;
import com.apptogo.runner.levels.LevelWorld;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class CampaignScreen extends BaseScreen{	
		
	private LevelManager levelManager;
	
	private Label label;
	private TextButton button;
	private TextButton backButton;

	public CampaignScreen(Runner runner)
	{
		super(runner);	
		levelManager = LevelManager.getInstance();
	}
	
	public void prepare() 
	{	
		setBackground("ui/menuBackgrounds/campaignScreenBackground.png");
		
		label = new Label( getLangString("campaignLabel"), skin);
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 250 );
        
        backButton = new TextButton( getLangString("backButton"), skin, "default");
        backButton.setWidth(500f);
        backButton.setHeight(100f);
        backButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - backButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - backButton.getHeight()/2.0f - 330f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
        addToScreen(backButton);
        addToScreen(label);
        
        Array<LevelWorld> worlds = levelManager.getMultiplayerLevelWorlds();
        
        float buttonXStart = ((runner.SCREEN_WIDTH/Box2DVars.PPM) / 2f) - 425f,  buttonX = buttonXStart;
        float buttonYStart = ((runner.SCREEN_HEIGHT/Box2DVars.PPM) / 2f) + 80f, buttonY = buttonYStart;
        float buttonSize = 140f;
        float buttonMargin = 20f;
        float maxInRow = 4f;
        
        for(int i=0; i<worlds.size; i++) Logger.log(this, worlds.get(i).name + " levelsCount = " + String.valueOf( worlds.get(i).levels.size ) );
        /*
        for(int i = 0; i < levels.size; i++)
        {
        	final Level level = levels.get(i);
        	
	        button = new TextButton( level.buttonLabel, skin, "default");
	        button.setWidth(buttonSize);
	        button.setHeight(buttonSize);
	        button.setPosition( buttonX, buttonY );       
	        
	        button.addListener(new ClickListener() {
	            public void clicked(InputEvent event, float x, float y) 
	            {
	            	ScreensManager.getInstance().createLoadingScreen( level, ScreenType.SCREEN_GAME_SINGLE );
	            }
	         });
	        
	        addToScreen(button);
	        
	        //obliczanie kolejnej pozycji
	        if( buttonX >= buttonXStart + ( (buttonSize + buttonMargin) * maxInRow )  )
	        {
	        	buttonX = buttonXStart;
	        	buttonY -= (buttonSize + buttonMargin);
	        }
	        else buttonX += buttonSize + buttonMargin;
        }*/
	}
	
	public void step()
	{
		
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_UPGRADE;
	}


}
