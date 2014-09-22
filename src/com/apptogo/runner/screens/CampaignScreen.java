package com.apptogo.runner.screens;

import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelManager;
import com.apptogo.runner.levels.LevelWorld;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class CampaignScreen extends BaseScreen
{	
	private final int POINTS_PER_STAR = 100;	
	
	private LevelManager levelManager;

	private TextButton button;
	private Button backButton;
	private Button previousWorldButton;
	private Button nextWorldButton;
	
	private Label label;
	
	private Image star;
		
	private Array<LevelWorld> worlds;
	
	private Group group;
	private int worldsCount = 0;
	
	private ClickListener moveGroupLeftListener;
	private ClickListener moveGroupRightListener;
	
	private Texture starEmptyTexture;
	private Texture starFullTexture;
	
	public CampaignScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
		
		levelManager = LevelManager.getInstance();
	
		fadeInOnStart();
	}
	
	public void prepare() 
	{	
		starEmptyTexture = ResourcesManager.getInstance().getResource(this.getSceneType(), "gfx/menu/starSmallEmpty.png");
		starFullTexture = ResourcesManager.getInstance().getResource(this.getSceneType(), "gfx/menu/starSmallFull.png");
		starFullTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		starEmptyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
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
		
        backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
		
		addToScreen(group);
		addToScreen(backButton);
	}
	
	private void addNewWorldToGroup(LevelWorld levelWorld)
	{
		Image worldBackground = GameWorldType.convertToWorldBackground( levelWorld.getWorldType(), this.getSceneType() );
		worldBackground.setPosition((-640.0f) + 1280.0f * worldsCount, -400.0f);
		
		previousWorldButton = new Button(skin, "campaignArrowLeft");
        previousWorldButton.setPosition( -570.0f + (worldsCount * 1280.0f), -100.0f );
        previousWorldButton.addListener( moveGroupRightListener );
        
        nextWorldButton = new Button(skin, "campaignArrowRight");
        nextWorldButton.setPosition( 470.0f + (worldsCount * 1280.0f), -100.0f );
        nextWorldButton.addListener( moveGroupLeftListener );
        
        int achievedStarsCount = player.getWorldScore(levelWorld) / POINTS_PER_STAR;
        
        label = new Label( GameWorldType.convertToFullName( levelWorld.getWorldType() ) + "   " + String.valueOf( achievedStarsCount ) + "/36" , skin, "default");
        label.setPosition((runner.SCREEN_WIDTH/Box2DVars.PPM) / 2.0f - ( label.getWidth() / 2.0f ) + (worldsCount * 1280.0f), 250.0f);
        setLabelFont(label, FontType.DEFAULT);
        
        star = new Image( starFullTexture );
        star.setPosition(label.getWidth() + label.getX() + 10.0f, label.getY() + ( (label.getHeight() - star.getHeight()) / 2.0f ));
		       
        group.addActor(worldBackground);
        group.addActor(previousWorldButton);
        group.addActor(nextWorldButton);
        group.addActor(label);
        group.addActor(star);
		
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
		handleInput();
	}
	
	private void drawButtons(LevelWorld levelWorld, float indent)
	{
		float buttonMargin = 45f;
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
        		setTextButtonFont(button, GameWorldType.convertToButtonFontType( levelWorld.getWorldType() ) );
        		button.addListener(new ClickListener() 
        		{
    	            public void clicked(InputEvent event, float x, float y) 
    	            {
    	            	player.setCurrentCharacter( GameWorldType.convertToCharacterType( level.worldType ) );
    	            	
    	            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_SINGLE, level, null );
    	            }
    	         });
        	}
        	else
        	{
        		button = new TextButton( "", skin, buttonLockedStyleName);
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
        	        
	        button.setPosition( buttonX + indent, buttonY - 50.0f );       
	        button.setUserObject( levelWorld.getWorldType() );
	       	
	        if( player.isLevelUnlocked(level) )
	        {
	        	button = drawStars( button, level );
	        }
	        
	        group.addActor(button);
    	}
	}
	
	private TextButton drawStars(TextButton button, Level level)
	{
		Image firstStar;
		Image secondStar;
		Image thirdStar;
		
		float starWidth = starEmptyTexture.getWidth();
		float margin = 0.0f;
		float startX = ( button.getWidth() - (3 * starWidth) - (3 * margin) ) / 2.0f;
		
		int score = player.getLevelScore(level);
		
		if( score >= 100 && score < 200 )
		{
			firstStar = new Image( starFullTexture );
			secondStar = new Image( starEmptyTexture );
			thirdStar = new Image( starEmptyTexture );
		}
		else if( score >= 200 && score < 300 )
		{
			firstStar = new Image( starFullTexture );
			secondStar = new Image( starFullTexture );
			thirdStar = new Image( starEmptyTexture );
		}
		else if( score > 300 )
		{
			firstStar = new Image( starFullTexture );
			secondStar = new Image( starFullTexture );
			thirdStar = new Image( starFullTexture );
		}
		else
		{
			firstStar = new Image( starEmptyTexture );
			secondStar = new Image( starEmptyTexture );
			thirdStar = new Image( starEmptyTexture );
		}
				
		firstStar.setPosition(startX, 0.0f);
		secondStar.setPosition(startX + starWidth + margin, -20.0f);
		thirdStar.setPosition(startX + (2 * starWidth) + (2 * margin), 0.0f);
		
		button.addActor(firstStar);
		button.addActor(secondStar);
		button.addActor(thirdStar);
		
		return button;
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
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
