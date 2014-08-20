package com.apptogo.runner.screens;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelManager;
import com.apptogo.runner.levels.LevelWorld;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
		
	private Array<LevelWorld> worlds;
	
	private Group group;
	private int worldsCount = 0;
	
	private ClickListener moveGroupLeftListener;
	private ClickListener moveGroupRightListener;
	

	public CampaignScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
		levelManager = LevelManager.getInstance();
	}
	
	public void prepare() 
	{	
		group = new Group();
		group.setPosition(0.0f, 0.0f);
		
		moveGroupLeftListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
            	moveGroup(-1.0f);
            }
		};
		
		moveGroupRightListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
            	moveGroup(1.0f);
            }
		};
		
		worlds = levelManager.getCampaignLevelWorlds();	
				
		for(int w = 0; w < worlds.size; w++)
        {
        	LevelWorld levelWorld = worlds.get(w);

        	addNewWorldToGroup( levelWorld );
        }
		
        backButton = new TextButton( "<--", skin, "default");
        backButton.setSize(200f, 100f);
        backButton.setPosition( -600f, -370f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
		
		addToScreen(group);
		addToScreen(backButton);
	}
	
	private void addNewWorldToGroup(LevelWorld levelWorld)
	{
		Image worldBackground = GameWorldType.convertToWorldBackground( levelWorld.getWorldType(), this.getSceneType() );
		worldBackground.setPosition((-640.0f) + 1280.0f * worldsCount, -400.0f);
		
		previousWorldButton = new TextButton("<", skin, "default");
        previousWorldButton.setSize(100f, 100f);
        previousWorldButton.setPosition( -500.0f + (worldsCount * 1280.0f), -100.0f );
        previousWorldButton.addListener( moveGroupRightListener );
        
        nextWorldButton = new TextButton(">", skin, "default");
        nextWorldButton.setSize(100f, 100f);
        nextWorldButton.setPosition( 400.0f + (worldsCount * 1280.0f), -100.0f );
        nextWorldButton.addListener( moveGroupLeftListener );
		       
        group.addActor(worldBackground);
        group.addActor(previousWorldButton);
        group.addActor(nextWorldButton);
		
        drawButtons( levelWorld, (worldsCount * 1280.0f) );
        
		worldsCount++;		
	}
	
	private void moveGroup(float direction)
	{
		if( (group.getX() == 0.0f && direction >= 0) || (-group.getX() == ((worldsCount - 1) * 1280.0f) && direction < 0) )
		{
			return;
		}
		else
		{

			MoveToAction action = new MoveToAction();
			action.setDuration(0.2f);
			
			if( direction < 0 )
			{
				action.setX( group.getX() - 1280.0f );
			}
			else
			{
				action.setX( group.getX() + 1280.0f );
			}
			
			group.addAction(action);
		}
	}
	
	public void step()
	{
		
	}
	
	private void drawButtons(LevelWorld levelWorld, float indent)
	{
		float buttonMargin = 30f;
        int maxInRow = 4;
        int maxInColumn = 3;
        
        
    	Array<Level> levelWorldLevels = levelWorld.getLevels();
    	
    	int currentRowCounter = 0;
        int currentColumnCounter = 0;
        String buttonStyleName;
        String buttonLockedStyleName;
        
		buttonStyleName = GameWorldType.convertToButtonStyleName( levelWorld.getWorldType() );
		buttonLockedStyleName = GameWorldType.convertToButtonLockedStyleName( levelWorld.getWorldType() );
    	
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
        	        
	        button.setPosition( buttonX + indent, buttonY );       
	        button.setUserObject( levelWorld.getWorldType() );
	        
	        group.addActor(button);
    	}
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
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_CAMPAIGN;
	}


}
