package com.apptogo.runner.screens;

import com.apptogo.runner.enums.CharacterType;
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
import com.apptogo.runner.player.Player;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	private LevelManager levelManager;

	private TextButton button;
	private Button backButton;
	private Button previousWorldButton;
	private Button nextWorldButton;
	
	private Label label;
	
	private Button star;
	
	private float starWidth = 50;
	private float starHeight = 50;
		
	private Array<LevelWorld> worlds;
	
	private Group backgroundGroup;
	private Group group;
	private int worldsCount = 0;
	
	private Array<Actor> arrows;
	
	private int currentGroupId;
	
	private ClickListener moveGroupLeftListener;
	private ClickListener moveGroupRightListener;
		
	public CampaignScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
		
		levelManager = LevelManager.getInstance();
	
		fadeInOnStart();
	}
	
	public void prepare() 
	{
		backgroundGroup = new Group();
		backgroundGroup.setPosition(0.0f, 0.0f);
		
		backgroundGroup.setTransform(false);
		
		group = new Group();
		group.setPosition(0.0f, 0.0f);
		
		group.setTransform(false);
		
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
		
		arrows = new Array<Actor>();
				
		currentGroupId = 0;
		
		for(int w = 0; w < worlds.size; w++)
        {
        	LevelWorld levelWorld = worlds.get(w);

        	addNewWorldToGroup( levelWorld );
        }
		
		for(int i = 2; i < arrows.size; i++) 
		{
			arrows.get(i).setVisible(false);
		}
		
		hideLastAndFirstArrow();
		
        backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
		        
        addToBackground(backgroundGroup);
        addToScreen(group);
		addToScreen(backButton);
	}
	
	private void addNewWorldToGroup(LevelWorld levelWorld)
	{
		Image worldBackground = createImage(GameWorldType.convertToWorldBackgroundPath( levelWorld.getWorldType(), this.getSceneType() ), (-Runner.SCREEN_WIDTH/2.0f) + Runner.SCREEN_WIDTH * worldsCount, -400.0f);
		
		previousWorldButton = new Button(skin, "campaignArrowLeft");
        previousWorldButton.setPosition( -570.0f + (worldsCount * Runner.SCREEN_WIDTH), -100.0f );
        previousWorldButton.addListener( moveGroupRightListener );
        
        nextWorldButton = new Button(skin, "campaignArrowRight");
        nextWorldButton.setPosition( 470.0f + (worldsCount * Runner.SCREEN_WIDTH), -100.0f );
        nextWorldButton.addListener( moveGroupLeftListener );
        
        arrows.add(previousWorldButton);
        arrows.add(nextWorldButton);
        
        int achievedStarsCount = player.statistics.getWorldStars(levelWorld);
        
        label = new Label(levelWorld.label + "   " + String.valueOf( achievedStarsCount ) + "/36" , skin, "default");
        label.setPosition((Runner.SCREEN_WIDTH/Box2DVars.PPM) / 2.0f - ( label.getWidth() / 2.0f ) + (worldsCount * Runner.SCREEN_WIDTH), 250.0f);
        
        star = new Button(skin, "starSmallFull");
        star.setPosition( label.getWidth() + label.getX() + 10.0f, label.getY() + ( (label.getHeight() - starHeight) / 2.0f ));
		       
        backgroundGroup.addActor(worldBackground);
        
        group.addActor(previousWorldButton);
        group.addActor(nextWorldButton);
        group.addActor(label);
        group.addActor(star);
		
        drawButtons( levelWorld, (worldsCount * Runner.SCREEN_WIDTH) );
        
		worldsCount++;		
	}
	
	private void moveGroup(float direction)
	{
		if( (group.getX() == 0.0f && direction >= 0) || (-group.getX() == ((worldsCount - 1) * Runner.SCREEN_WIDTH) && direction < 0) )
		{
			return;
		}
		else
		{
			MoveToAction action = new MoveToAction();
			MoveToAction backgroundAction = new MoveToAction();
															
			action.setDuration(0.2f);
			backgroundAction.setDuration(0.2f);
			
			arrows.get(currentGroupId * 2).setVisible(false);
			arrows.get((currentGroupId * 2) + 1).setVisible(false);
			
			if( direction < 0 )
			{
				action.setX( group.getX() - Runner.SCREEN_WIDTH );
				backgroundAction.setX( group.getX() - Runner.SCREEN_WIDTH );
				
				currentGroupId++;
			}
			else
			{
				action.setX( group.getX() + Runner.SCREEN_WIDTH );
				backgroundAction.setX( group.getX() + Runner.SCREEN_WIDTH );
				
				currentGroupId--;
			}
			
			arrows.get(currentGroupId * 2).setVisible(true);
			arrows.get((currentGroupId * 2) + 1).setVisible(true);
			
			hideLastAndFirstArrow();
			
			backgroundGroup.addAction(backgroundAction);
			
			group.addAction(action);
		}
	}
	
	private void hideLastAndFirstArrow()
	{
		arrows.first().setVisible(false);
		arrows.get( arrows.size - 1 ).setVisible(false);
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
        	
        	if( player.isLevelUnlocked(level, levelWorld) )
        	{
        		button = new TextButton( level.buttonLabel, skin, buttonStyleName);
        		button.addListener(new ClickListener() 
        		{
    	            public void clicked(InputEvent event, float x, float y) 
    	            {
    	            	player.setCharacterType( CharacterType.ARCHER );//GameWorldType.convertToCharacterType( level.worldType ) );
    	            	player.save();
    	            	//ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_SINGLE, level, null ); - odpalenie singla, wykomentowane do testow
    	            	
    	            	Array<Player> players = new Array<Player>();
    	            	Player enemyPlayer1 = new Player("dupek", CharacterType.ARCHER);
    	            	Player enemyPlayer2 = new Player("cipek", CharacterType.ALIEN);
    	            	//Player enemyPlayer3 = new Player("siurek", CharacterType.ARCHER);
    	            	players.add(enemyPlayer1);
    	            	players.add(enemyPlayer2);
    	            	//players.add(enemyPlayer3);
    	            	ScreensManager.getInstance().createLoadingGameScreen( ScreenType.SCREEN_GAME_SINGLE, level, players );
    	            }
    	         });
        	}
        	else
        	{
        		button = new TextButton( "", skin, buttonLockedStyleName);
        	}
        	        	
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
	       	
	        if( player.isLevelUnlocked(level, levelWorld) )
	        {
	        	button = drawStars( button, level );
	        }
	        
	        group.addActor(button);
    	}
	}
	
	private TextButton drawStars(TextButton button, Level level)
	{
		Button firstStar;
		Button secondStar;
		Button thirdStar;
		
		float margin = 0.0f;
		float startX = ( button.getWidth() - (3 * starWidth) - (3 * margin) ) / 2.0f;
		
		int score = player.statistics.getLevelScore(level);
		
		if( score >= 100 && score < 200 )
		{
			firstStar = new Button(skin, "starSmallFull");
			firstStar.setPosition(startX, 0.0f);
			
			secondStar = new Button(skin, "starSmallEmpty");
			secondStar.setPosition( startX + starWidth + margin, -20.0f);
			
			thirdStar = new Button(skin, "starSmallEmpty");
			thirdStar.setPosition( startX + (2 * starWidth) + (2 * margin), 0.0f);
		}
		else if( score >= 200 && score < 300 )
		{
			firstStar = new Button(skin, "starSmallFull");			
			secondStar = new Button(skin, "starSmallFull");
			thirdStar = new Button(skin, "starSmallEmpty");
		}
		else if( score > 300 )
		{
			firstStar = new Button(skin, "starSmallFull");		
			secondStar = new Button(skin, "starSmallFull");			
			thirdStar = new Button(skin, "starSmallFull");
		}
		else
		{
			firstStar = new Button(skin, "starSmallEmpty");
			secondStar = new Button(skin, "starSmallEmpty");		
			thirdStar = new Button(skin, "starSmallEmpty");
		}
		
		firstStar.setPosition(startX, 0.0f);
		secondStar.setPosition( startX + starWidth + margin, -20.0f);
		thirdStar.setPosition( startX + (2 * starWidth) + (2 * margin), 0.0f);
						
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
