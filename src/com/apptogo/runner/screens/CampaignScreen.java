package com.apptogo.runner.screens;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class CampaignScreen extends BaseScreen
{	
		
	private LevelManager levelManager;
	
	private Label label;
	private TextButton button;
	private TextButton backButton;
	private TextButton previousWorldButton;
	private TextButton nextWorldButton;
	
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
		setBackground("ui/menuBackgrounds/campaignScreenBackground.png");
		
		label = new Label( getLangString("campaignLabel"), skin);
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 250 );
        
        backButton = new TextButton( getLangString("backButton"), skin, "default");
        backButton.setSize(300f, 100f);
        backButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - backButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - backButton.getHeight()/2.0f - 330f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
        previousWorldButton = new TextButton("<", skin, "default");
        previousWorldButton.setSize(100f, 100f);
        previousWorldButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - previousWorldButton.getWidth()/2.0f - 420f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - previousWorldButton.getHeight()/2.0f );
        previousWorldButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	changeCurrentLevelWorld( currentLevelWorld - 1 );
            }
         });
        
        nextWorldButton = new TextButton(">", skin, "default");
        nextWorldButton.setSize(100f, 100f);
        nextWorldButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - nextWorldButton.getWidth()/2.0f + 420f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - nextWorldButton.getHeight()/2.0f );
        nextWorldButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	changeCurrentLevelWorld( currentLevelWorld + 1 );
            }
         });
        
        addToScreen(previousWorldButton);
        addToScreen(nextWorldButton);
        addToScreen(backButton);
        addToScreen(label);
                
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
		float buttonMargin = 20f;
        int maxInRow = 4;
        int maxInColumn = 3;
        
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
        	            	
        	            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_SINGLE, level, characterTypes );
        	            }
        	         });
            	}
            	else
            	{
            		button = new TextButton( level.buttonLabel, skin, buttonLockedStyleName);
            	}
            	
            	//nalezy dostosowac grafiki, tymczasem jako workaround:
            	button.setSize(140f, 140f);
            	
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
            	        
    	        button.setPosition( buttonX, buttonY );       
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
