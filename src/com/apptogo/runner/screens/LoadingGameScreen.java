package com.apptogo.runner.screens;

import com.apptogo.runner.animation.ObjectAnimation;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.TipManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class LoadingGameScreen extends BaseScreen
{	    
	
	private ResourcesManager resourcesManager;
	
	private Label tipLabel;
	
	private ObjectAnimation loadingAnimation;
	
	private ScreenType screenToLoad;
	private Level levelToLoad;
	private Array<Player> players;
	
	public LoadingGameScreen(Runner runner, ScreenType screenToLoad, Level levelToLoad, Array<Player> players)
	{
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
        this.screenToLoad = screenToLoad;
        this.levelToLoad = levelToLoad;
        this.players = players;

        resourcesManager.loadResources(screenToLoad);
	}
	
	public void prepare() 
	{
		//jesli cos sie stanie z tlem przy ladowaniu to tu jest pewnie wina
		if( levelToLoad != null )
		{
			setBackground( CharacterType.convertToLoadingScreenBackground( GameWorldType.convertToCharacterType( levelToLoad.worldType ) ) );
		}
		
		String tip = TipManager.getInstance().getTip( levelToLoad.worldType );
				
		tipLabel = createLabel(tip, FontType.SMALL);
		setCenterPosition(tipLabel, -320.0f);
		tipLabel.setVisible(true);
		Logger.log(this, ResourcesManager.getInstance().getAssetManager(ScreenClass.STILL).getDiagnostics() );
		loadingAnimation = new ObjectAnimation("gfx/splash/loading.pack", "loading", 19, -85.0f, -240.0f, false, true);
		loadingAnimation.setVisible(true);
		loadingAnimation.start();
		
		addToScreen(tipLabel);
		addToScreen(loadingAnimation);
	}
	
	public void step()
	{			
		if( resourcesManager.getAssetManager(screenToLoad).update() ) 
		{
			if( resourcesManager.getAssetManager(screenToLoad).update() )
			{
				ScreensManager.getInstance().createGameScreen(screenToLoad, levelToLoad, players);
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
		return ScreenType.SCREEN_LOADING_GAME;
	}


}
