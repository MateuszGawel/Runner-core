package com.apptogo.runner.animation;

import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class LogoAnimation 
{
	/* na razie przenosze tu kod z alpha actiona na splash image ale docelowo bedzie tu cala animacja z tym opadajacym logiem etc. dodatkowo klasa powinna miec mozliwosc zwracania animacji samego loga, tak zeby mozna bylo jej uzyc np w loading screenach albo czyms w tym styly ale generalnie nie tylko splash screen */
	private Texture splashImageTexture;
	public Image splashImage;
	private float splashImageOpacity;
	private AlphaAction action;
	private final float FADE_IN_TIME = 80; //finalnie powinno byc ~80
	
	public LogoAnimation()
	{
		splashImageTexture = (Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_SPLASH, "gfx/splash/splash.png");
		
		splashImage = new Image( splashImageTexture );
		splashImage.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - splashImage.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - splashImage.getHeight()/2.0f );
		splashImageOpacity = 0.0f;
		
		action = new AlphaAction();
	}
	
	public void play(){}
	
	public boolean isFinished()
	{
		if( splashImageOpacity < 1.0f )
		{
			return false;
		}
		return true;
	}
	
	public void dispose()
	{
		splashImageTexture.dispose();
	}
	
	public AlphaAction animate(Actor actor)
	{
		splashImageOpacity += 2.0f / FADE_IN_TIME;
		
		action.reset();
		action.setAlpha( ((splashImageOpacity>1.0f)?1.0f:splashImageOpacity) * (float)Math.sin(((splashImageOpacity>1.0f)?1.0f:splashImageOpacity)) );
		return action;
	}
}
