package com.apptogo.runner.screens;

import com.apptogo.runner.animation.ObjectAnimation;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.TipManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;

public class LoadingScreen extends BaseScreen
{	    
	private ScreenType screenToLoad;
	private ResourcesManager resourcesManager;
	
	private Label loadingLabel;
	private Label tipLabel;
	
	private ObjectAnimation loadingAnimation;
	
	private Level levelToLoad;
	
	private long timeStart;
	private boolean loadingLabelIsAdded = false;
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad)
	{		
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
		timeStart = TimeUtils.millis();
		
        this.screenToLoad = screenToLoad;
        this.levelToLoad = null;
        
        resourcesManager.loadResources(screenToLoad);

        if( screenToLoad == ScreenType.SCREEN_MAIN_MENU )
        {
        	resourcesManager.loadMenuResources();
        }   
	}
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad, Level levelToLoad)
	{
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
        this.screenToLoad = screenToLoad;
        this.levelToLoad = levelToLoad;

        resourcesManager.loadResources(screenToLoad);
        resourcesManager.loadGameResources();
	}
	
	public void prepare() 
	{
		int yOffset = 0;
		String tip = "";
		String loading = getLangString("loadingLabel");
		
		if( screenToLoad == ScreenType.SCREEN_GAME_SINGLE || screenToLoad == ScreenType.SCREEN_GAME_MULTI )
		{
			//jesli cos sie stanie z tlem przy ladowaniu to tu jest pewnie wina
			if( levelToLoad != null )
			{
				setBackground( CharacterType.convertToLoadingScreenBackground( GameWorldType.convertToCharacterType( levelToLoad.worldType ) ) );
			}
			
			yOffset = 220;
			tip = TipManager.getInstance().getTip( levelToLoad.worldType );
			loading = "";
		}
		
		loadingLabel = new Label( loading, skin, "default");
		setCenterPosition(loadingLabel, 10.0f - yOffset);
		loadingLabel.setVisible(false);
		
		tipLabel = createLabel(tip, FontType.SMALL);
		setCenterPosition(tipLabel, -100.0f - yOffset);
		tipLabel.setVisible(false);
		
		loadingAnimation = new ObjectAnimation("gfx/splash/loading.pack", "loading", 19, -85.0f, -20.0f - yOffset, false, true);
		loadingAnimation.setVisible(false);
		
		
		addToScreen(loadingLabel);
		addToScreen(tipLabel);
		addToScreen(loadingAnimation);
	}
	
	public void step()
	{	
		//if( screenToLoad != ScreenType.SCREEN_GAME_SINGLE && screenToLoad != ScreenType.SCREEN_GAME_MULTI )
		//{
			if( !loadingLabelIsAdded && ( (TimeUtils.millis() - timeStart) > 750 ) )
			{
				loadingLabel.setVisible(true);
				tipLabel.setVisible(true);
				
				loadingAnimation.setVisible(true);
				loadingAnimation.start();
				
				loadingLabelIsAdded = true;
			}
		//}
		
		if( resourcesManager.getAssetManager(screenToLoad).update() ) 
		{
			if( screenToLoad == ScreenType.SCREEN_MAIN_MENU )
			{
				if( ResourcesManager.getInstance().getMenuAssetManager().update() )
				{
					ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
				}
			}
			else if( screenToLoad == ScreenType.SCREEN_GAME_SINGLE || screenToLoad == ScreenType.SCREEN_GAME_MULTI )
			{
				if( ResourcesManager.getInstance().getGameAssetManager().update() )
				{
					ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
				}
			}
			else
			{
				ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
			}
		}
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
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
		return ScreenType.SCREEN_LOADING;
	}


}
