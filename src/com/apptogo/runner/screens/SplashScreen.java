package com.apptogo.runner.screens;

import java.util.Random;

import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.animation.Loading;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SplashScreen extends BaseScreen
{
	private final boolean SKIP_ANIMATIONS = false;
	
	private enum SplashPhase
	{
		APPTOGO_LOGO_IN,
		DASHANDSMASH_LOGO_IN,
		SCREEN_SHAKEING,
		START_LOADING,
		LOADING,
		FINISHED,
		END
	}
	
	private AssetManager menuAssetManager;
	
	private SplashPhase currentPhase;
	
	Image backgroundHalo;
	
	Image appToGoLogo;
	
	Group dashAndSmashLogo;
	
	Image groupLogo;
	Animation groupLetter;
	Animation groupDust;
		
	private Label loadingLabel;
	
	Loading loadingAnimation;
	
	private MoveToAction apptogoLogoInAction;
	private MoveToAction apptogoLogoOutAction;
	private MoveToAction dashandsmashLogoInAction;
	private AlphaAction backgroundInAction;
	
	Camera camera;
	
	private int cameraShakeCounter;
	
	private CustomAction cameraShakeAction;
	private CustomAction cameraStopShakeAction;
			
	public SplashScreen(Runner runner)
	{
		super(runner);
		
		ResourcesManager.getInstance().loadResources(this);
		ResourcesManager.getInstance().getAssetManager(this).finishLoading();
		
		ResourcesManager.getInstance().loadResources(ScreenClass.STILL);
		ResourcesManager.getInstance().getAssetManager(ScreenClass.STILL).finishLoading();
	}
	
	@Override
	public void prepare()
	{
		skin = ResourcesManager.getInstance().getUiSkin( ScreenClass.STILL );
		
		initializeActions();

		currentPhase = SplashPhase.APPTOGO_LOGO_IN;
		
		//creating background
		backgroundHalo = createImage("ellipse", 0, 0);
		backgroundHalo.setSize(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		backgroundHalo.setPosition(-Runner.SCREEN_WIDTH/2.0f, -Runner.SCREEN_HEIGHT/2.0f);
		
		backgroundHalo.getColor().a = 0.0f;
		
		addToScreen(backgroundHalo);
		
		//creating Dash&Smash logo
		dashAndSmashLogo = new Group();
		setCenterPosition( dashAndSmashLogo, 800);
		
		dashAndSmashLogo.setTransform(false);
		
		groupLogo = createImage("logoSplash", 0, 0);
		setCenterPosition(groupLogo, -200);
				
		groupLetter = new Animation("d", 16, 0.03f, CharacterAnimationState.RUNNING, true, false);
		groupLetter.setPosition(-370, -110);
		
		dashAndSmashLogo.addActor(groupLogo);
		dashAndSmashLogo.addActor(groupLetter);
				
		addToScreen(dashAndSmashLogo);
		
		//creating App2go logo
		appToGoLogo = createImage("splash", 0, 0);
		setCenterPosition(appToGoLogo, -800);
		
		appToGoLogo.addAction(apptogoLogoInAction);
		
		addToScreen(appToGoLogo);

		//creating loading controls
		loadingLabel = new Label("loading", skin, "loadingSmall");
		setCenterPosition(loadingLabel, 270);
		
		loadingAnimation = new Loading();
		setCenterPosition(loadingAnimation, 240);
	}
	
	private void initializeActions()
	{
		apptogoLogoInAction = new MoveToAction();
		if( SKIP_ANIMATIONS ) apptogoLogoInAction.setDuration(0.1f);
		else                  apptogoLogoInAction.setDuration(1.5f);
		apptogoLogoInAction.setPosition(-106.0f, -200.0f);
		apptogoLogoInAction.setInterpolation(Interpolation.pow5Out);
		
		apptogoLogoOutAction = new MoveToAction();
		if( SKIP_ANIMATIONS ) apptogoLogoOutAction.setDuration(0.1f);
		else                  apptogoLogoOutAction.setDuration(0.3f);
		apptogoLogoOutAction.setPosition(-106.0f, -800.0f);
		
		dashandsmashLogoInAction = new MoveToAction();
		if( SKIP_ANIMATIONS ) dashandsmashLogoInAction.setDuration(0.1f);
		else                  dashandsmashLogoInAction.setDuration(0.5f);
		dashandsmashLogoInAction.setPosition(0, 0.0f);
		dashandsmashLogoInAction.setInterpolation(Interpolation.exp10In);
				
		backgroundInAction = new AlphaAction();
		if( SKIP_ANIMATIONS ) backgroundInAction.setDuration(0.1f);
		else                  backgroundInAction.setDuration(1.0f);
		backgroundInAction.setAlpha(1.0f);
				
		final Camera camera = menuStage.getCamera();
		
		cameraShakeCounter = 0;
		
		cameraShakeAction = new CustomAction(0f, 0){
			@Override
			public void perform() 
			{
				cameraShakeCounter++;
			
				float randomX = (( new Random() ).nextFloat() - 0.5f) * 15 * 1/cameraShakeCounter;
				float randomY = (( new Random() ).nextFloat() - 0.5f) * 15 * 1/cameraShakeCounter;
				camera.position.set(-randomX, -randomY, 0);
			}
		};
		
		cameraStopShakeAction = new CustomAction(0.4f){
			@Override
			public void perform() 
			{
				cameraShakeAction.setFinished(true);
				camera.position.set(0,0,0);
			}
		};
	}
	
	public void step()
	{
		if( currentPhase == SplashPhase.FINISHED ) 
		{
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
			
			currentPhase = SplashPhase.END;
		}
		
		else if( currentPhase == SplashPhase.APPTOGO_LOGO_IN && appToGoLogo.getActions().size <= 0 )
		{
			//start loading assets
			ResourcesManager.getInstance().loadResources(ScreenClass.MENU);

			menuAssetManager = ResourcesManager.getInstance().getAssetManager(ScreenClass.MENU);
			
			appToGoLogo.addAction(apptogoLogoOutAction);
			
			currentPhase = SplashPhase.DASHANDSMASH_LOGO_IN;
		}
		
		else if( currentPhase == SplashPhase.DASHANDSMASH_LOGO_IN && appToGoLogo.getActions().size <= 0 )
		{
			dashAndSmashLogo.addAction(dashandsmashLogoInAction);
			
			currentPhase = SplashPhase.SCREEN_SHAKEING;
		}
		
		else if(currentPhase == SplashPhase.SCREEN_SHAKEING && dashAndSmashLogo.getActions().size <= 0 )
		{
			//creating dust
			groupDust = new Animation("dust", 21, 0.03f, CharacterAnimationState.RUNNING, true, false);
			groupDust.setPosition(-470, -250);
			groupDust.scaleFrames(4);
			
			dashAndSmashLogo.addActor(groupDust);
			
			//shakeing screen
			CustomActionManager.getInstance().registerAction(cameraShakeAction);
			CustomActionManager.getInstance().registerAction(cameraStopShakeAction);
			
			//animating letter
			groupLetter.start();
			
			//fadeing in background
			backgroundHalo.addAction(backgroundInAction);
			
			currentPhase = SplashPhase.START_LOADING;
		}
		
		else if(currentPhase == SplashPhase.START_LOADING && groupDust.isFinished() )
		{
			//removing dust
			groupDust.setVisible(false);
			
			//adding loading controls
			addToScreen(loadingLabel);
			addToScreen(loadingAnimation); //uwaga tu pojawi sie drugi renderCall bo loading biezemy ze STILL
						
			currentPhase = SplashPhase.LOADING;
		}
		
		else if(currentPhase == SplashPhase.LOADING && menuAssetManager.update() )
		{
			currentPhase = SplashPhase.FINISHED;
		}
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			Gdx.app.exit();
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
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}
}
