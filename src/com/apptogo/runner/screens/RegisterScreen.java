package com.apptogo.runner.screens;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class RegisterScreen extends BaseScreen
{		
	Button backButton;
	
	Label notLoggedLabel;
	Label informationLabel;
	Label additionalInformationLabel;
	
	Label nameLabel;
	Label passwordLabel;
	Label submitButton;
	
	TextField nameTextField;
	TextField passwordTextField;
	
	public RegisterScreen(Runner runner)
	{
		super(runner);	
		
		loadPlayer();
		
		fadeInOnStart();
	}
	
	public void prepare() 
	{		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
        
        notLoggedLabel = createLabel( getLangString("youreNotLoggedIn"), FontType.MEDIUM);
    	setCenterPosition(notLoggedLabel, 240.0f);
        
        informationLabel = createLabel( getLangString("giveYourAccountInformations"), FontType.MEDIUM);
        setCenterPosition(informationLabel, 170.0f);
        
        additionalInformationLabel = createLabel( getLangString("accountWillBeCreatedIfItIsNot"), FontType.SMALL);
        setCenterPosition(additionalInformationLabel, 120.0f);
        
        nameLabel = createLabel( getLangString("name") + ":", FontType.MEDIUM);
        nameLabel.setPosition( -220.0f - nameLabel.getWidth(), -25.0f);
        
        passwordLabel = createLabel( getLangString("password") + ":", FontType.MEDIUM);
        passwordLabel.setPosition( -220.0f - passwordLabel.getWidth(), -125.0f);
        
		nameTextField = new TextField("", skin, "default");
		nameTextField.setSize(510f, 50f);
		nameTextField.setPosition(-200.0f, -20.0f);
		nameTextField.setOnlyFontChars(true);
		nameTextField.setMaxLength(18);
		
		passwordTextField = new TextField("", skin, "default");
		passwordTextField.setSize(510f, 50f);
		passwordTextField.setPosition(-200.0f, -120.0f);
		passwordTextField.setOnlyFontChars(true);
		passwordTextField.setMaxLength(18);
		passwordTextField.setPasswordCharacter('*');
		passwordTextField.setPasswordMode(true);
		
		nameTextField.getStyle().background.setLeftWidth( 10.0f );
		passwordTextField.getStyle().background.setLeftWidth( 10.0f );
		
		submitButton = createLabel( getLangString("login"), FontType.MEDIUM);
		submitButton.setPosition( -(submitButton.getWidth() / 2.0f), -300.0f);
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
		addToScreen(notLoggedLabel);
		addToScreen(informationLabel);
		addToScreen(additionalInformationLabel);
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
