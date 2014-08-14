package com.apptogo.runner.screens;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelManager;
import com.apptogo.runner.levels.LevelWorld;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.world.GameWorld.GameWorldType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class CampaignScreen extends BaseScreen
{	
		
	private LevelManager levelManager;

	private TextButton button;
	private TextButton backButton;
	private TextButton previousWorldButton;
	private TextButton nextWorldButton;
	
	private TextButton levelInformationWidget;
	private TextButton playButton;
	
	private Array<LevelWorld> worlds;
	private int currentLevelWorld;
	
	private Array<Actor> missionButtons;

	public CampaignScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
		levelManager = LevelManager.getInstance();
	}
	
	public void prepare() 
	{	
		setBackground("ui/menuBackgrounds/campaignScreenBackgroundWildWest.png");
		
        backButton = new TextButton( "<--", skin, "default");
        backButton.setSize(200f, 100f);
        backButton.setPosition( -600f, 270f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
        previousWorldButton = new TextButton("<", skin, "default");
        previousWorldButton.setSize(100f, 100f);
        previousWorldButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - previousWorldButton.getWidth()/2.0f - 550f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - previousWorldButton.getHeight()/2.0f );
        previousWorldButton.addListener( 
        		new ClickListener(){
        			public void clicked(InputEvent event, float x, float y) 
                    {
        				changeCurrentLevelWorld(currentLevelWorld-1);
                    }
        		}
        		);
        
        nextWorldButton = new TextButton(">", skin, "default");
        nextWorldButton.setSize(100f, 100f);
        nextWorldButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - nextWorldButton.getWidth()/2.0f + 550f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - nextWorldButton.getHeight()/2.0f );
        nextWorldButton.addListener( 
        		new ClickListener(){
        			public void clicked(InputEvent event, float x, float y) 
                    {
        				changeCurrentLevelWorld(currentLevelWorld+1);
                    }
        		}
        		);
        
        playButton = new TextButton("play", skin, "default");
        playButton.setSize(420f, 130f);
        playButton.setPosition(50f, -330f);
        
        levelInformationWidget = new TextButton("level Info", skin, "default");
        levelInformationWidget.setSize(420f, 420f);
        levelInformationWidget.setPosition(50f, -200f);
               
        addToScreen(previousWorldButton);
        addToScreen(nextWorldButton);
        addToScreen(backButton);
        addToScreen(levelInformationWidget);
        addToScreen(playButton);
                
        missionButtons = new Array<Actor>();
        
        worlds = levelManager.getCampaignLevelWorlds();
        currentLevelWorld = 0; //czy warto to jakos obslugiwac? zeby np zapamietywal gdzie ostatnio byl player przed zmiana screena, albo zeby zawsze pokazywal "najnowsze" misje
	
        drawButtons();
        refreshButtons();
	}
	
	public void step()
	{
		
	}
	
	private void drawButtons()
	{
		float buttonMargin = 15f;
        int maxInRow = 3;
        int maxInColumn = 4;
        
        for(int w = 0; w < worlds.size; w++)
        {
        	LevelWorld levelWorld = worlds.get(w);
        	Array<Level> levelWorldLevels = levelWorld.getLevels();
        	
        	int currentRowCounter = 0;
            int currentColumnCounter = 0;
            String buttonStyleName;
            String buttonLockedStyleName;
            
            if( levelWorld.getWorldType() == GameWorldType.WILDWEST )
    		{
    			buttonStyleName = "westWildCampaignButton";
    			buttonLockedStyleName = "westWildCampaignLockedButton";
    		}
            else //if( levelWorld.getWorldType() == GameWorldType.FOSTER )
            {
            	buttonStyleName = "forestCampaignButton"; 
            	buttonLockedStyleName = "forestCampaignLockedButton";
            }
    		//else if( currentLevelWorld.getWorldType() == GameWorldType.SPACE )
    		//{
    		//	buttonStyleName = "spaceCampaignButton";
    		//	buttonLockedStyleName = "spaceCampaignLockedButton";
    		//}
        	
        	for(int l = 0; l < levelWorldLevels.size; l++)
        	{
        		final Level level = levelWorldLevels.get(l);
            	
            	if( player.isLevelUnlocked(level) )
            	{
            		button = new TextButton( level.buttonLabel, skin, buttonStyleName);
            		button.addListener(new ClickListener() 
            		{
        	            public void clicked(InputEvent event, float x, float y) 
        	            {
        	            	Array<CharacterType> characterTypes = new Array<CharacterType>();
        	            	characterTypes.add(player.getCurrentCharacter());
        	            	
        	            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_SINGLE, level, player, null );
        	            }
        	         });
            	}
            	else
            	{
            		button = new TextButton( level.buttonLabel, skin, buttonLockedStyleName);
            	}
            	
            	//nalezy dostosowac grafiki, tymczasem jako workaround:
            	button.setSize(130f, 130f);
            	
            	float buttonX = ( ((Runner.SCREEN_WIDTH / Box2DVars.PPM) - ((maxInRow * button.getWidth()) + ((maxInRow - 1) * buttonMargin)) / 2f) + (currentColumnCounter * (button.getWidth() + buttonMargin)) );
            	float buttonY = ( -button.getHeight() ) - ( ((Runner.SCREEN_HEIGHT / Box2DVars.PPM) - ((maxInColumn * button.getHeight()) + ((maxInColumn - 1) * buttonMargin)) / 2f) + (currentRowCounter * (button.getHeight() + buttonMargin)) );
            	
            	if( ++currentColumnCounter >= maxInRow ) 
        		{
        			if( ++currentRowCounter > maxInColumn )
        			{
        				Logger.log(this, "Max liczba level do wyswietlenia to: " + String.valueOf(maxInColumn * maxInRow));
        			}
        			currentColumnCounter = 0;
        		}
            	        
    	        button.setPosition( buttonX - 220f, buttonY - 50f );       
    	        button.setUserObject( levelWorld.getWorldType() );
    	        button.setVisible(false);
    	        
    	        missionButtons.add(button);
    	        addToScreen(button);
        	}
        }
	}
	
	private void refreshButtons()
	{
		GameWorldType actorType = worlds.get(currentLevelWorld).getWorldType();
		
		for(int i = 0; i < missionButtons.size; i++) missionButtons.get(i).setVisible(false);
		
		for(int i = 0; i < missionButtons.size; i++)
		{
			if( (GameWorldType)missionButtons.get(i).getUserObject() == actorType )
			{
				missionButtons.get(i).setVisible(true);
			}
		}
	}
	
	private void changeCurrentLevelWorld(int levelWorldIndex)
	{
		if( levelWorldIndex >= worlds.size)
		{
			levelWorldIndex = 0;
		}
		else if( levelWorldIndex < 0 )
		{
			levelWorldIndex = worlds.size - 1;
		}
		
		this.currentLevelWorld = levelWorldIndex;
		
		drawButtons();
		refreshButtons();
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
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_CAMPAIGN;
	}


}
