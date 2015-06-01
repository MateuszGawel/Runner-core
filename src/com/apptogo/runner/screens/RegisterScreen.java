package com.apptogo.runner.screens;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class RegisterScreen extends BaseScreen
{		
	Button backButton;
	
	private Widget registerWidget;
	
	Label notLoggedLabel;
	
	Label nameLabel;
	Label passwordLabel;
	TextButton submitButton;
	
	TextField nameTextField;
	TextField passwordTextField;
	
	public RegisterScreen(Runner runner)
	{
		super(runner);
	}
	
	public void prepare() 
	{
		setBackground("mainMenuScreenBackground");
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
        
        registerWidget = new Widget(Align.center, -360.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
        registerWidget.toggleWidget();
        
        notLoggedLabel = new Label( "LOGIN / CREATE ACCOUNT", skin, "coinLabelBig");
    	setCenterPosition(notLoggedLabel, 100.0f);
    	
        nameLabel = new Label( getLangString("name") + ":", skin, "coinLabel");
        nameLabel.setPosition( -120.0f - nameLabel.getWidth(), -10f);
        
        passwordLabel = new Label( getLangString("password") + ":", skin, "coinLabel");
        passwordLabel.setPosition( -120.0f - passwordLabel.getWidth(), -110.0f);
        
		nameTextField = new TextField("", skin, "default");
		
		nameTextField.getStyle().background.setLeftWidth(10);
		nameTextField.getStyle().background.setBottomHeight(10);
		
		nameTextField.setSize(410f, 50f);
		nameTextField.setPosition(-100.0f, -5.0f);
		nameTextField.setOnlyFontChars(true);
		nameTextField.setMaxLength(18);
		
		passwordTextField = new TextField("", skin, "default");
		passwordTextField.setSize(410f, 50f);
		passwordTextField.setPosition(-100.0f, -105.0f);
		passwordTextField.setOnlyFontChars(true);
		passwordTextField.setMaxLength(18);
		passwordTextField.setPasswordCharacter('*');
		passwordTextField.setPasswordMode(true);
		
		submitButton = new TextButton( getLangString("login"), skin, "default");
		submitButton.setSize(220, 120);
		setCenterPosition(submitButton, -280);
		submitButton.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) 
            {
				if( parseInput() )
				{
					player.setName( nameTextField.getText() );
					player.setPassword( passwordTextField.getText() );
					player.save();
										
					loadScreenAfterFadeOut(ScreenType.SCREEN_MULTIPLAYER);
				}
            }
			
		});
		
		addToScreen(backButton);
		
		addToScreen(registerWidget.actor());
		
		addToScreen(notLoggedLabel);
		//addToScreen(informationLabel);
		//addToScreen(additionalInformationLabel);
		addToScreen(nameLabel);
		addToScreen(passwordLabel);
		addToScreen(nameTextField);
		addToScreen(passwordTextField);
		addToScreen(submitButton);
	}
	
	private boolean parseInput()
	{
		Array<String> bannedStrings = new Array<String>( new String[]{ " ", "?" } );
		
		String name = nameTextField.getText();
		String password = passwordTextField.getText();
		
		if( name.trim().length() > 0 && password.trim().length() > 0 ) //String lenght test
		{
			for(String string: bannedStrings) //Banned strings test
			{
				if( name.contains(string) || password.contains(string) )
				{
					return false;
				}
			}
			
			//! test czy istnieje taki gracz
			//! test czy jesli istnieje to haslo jest poprawne
			
			return true; //Everything is ok
		}
		return false;
	}
	
	public void step()
	{
		handleInput();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			WarpController.getInstance().stopApp();
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
	}
	
	@Override
	public void hide() 
	{
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_REGISTER;
	}
}
