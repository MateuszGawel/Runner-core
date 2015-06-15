package com.apptogo.runner.screens;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
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
	
	Label nameLabel;
	Label passwordLabel;
	Button submitButton;
	
	TextField nameTextField;
	TextField passwordTextField;
	
	TextButton signboard;
	
	OrthographicCamera camera;
	
	boolean cameraFocused = false;
	boolean focusCamera = false;
	
	public RegisterScreen(Runner runner)
	{
		super(runner);
	}
	
	public void prepare() 
	{
		setBackground("mainMenuScreenBackground");
		
		camera = (OrthographicCamera) this.menuStage.getCamera();
		
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
            	
        nameLabel = new Label( getLangString("name") + ":", skin, "coinLabel");
        nameLabel.setPosition( -100.0f - nameLabel.getWidth(), 52f);
        
        passwordLabel = new Label( getLangString("password") + ":", skin, "coinLabel");
        passwordLabel.setPosition( -100.0f - passwordLabel.getWidth(), -48.0f);
        
		nameTextField = new TextField("", skin, "default");
		
		nameTextField.getStyle().background.setLeftWidth(10);
		nameTextField.getStyle().background.setBottomHeight(10);
		
		nameTextField.setSize(360f, 50f);
		nameTextField.setPosition(-80.0f, 55.0f);
		nameTextField.setOnlyFontChars(true);
		nameTextField.setMaxLength(9);
		
		passwordTextField = new TextField("", skin, "default");
		passwordTextField.setSize(360f, 50f);
		passwordTextField.setPosition(-80.0f, -45.0f);
		passwordTextField.setOnlyFontChars(true);
		passwordTextField.setMaxLength(9);
		passwordTextField.setPasswordCharacter('*');
		passwordTextField.setPasswordMode(true);
		
		ClickListener unfocusListener = new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) 
            {
				if(cameraFocused)
				{
					focusCamera = true;
				}
            }
		};
		
		ClickListener focusListener = new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) 
	        {
				if(!cameraFocused)
				{
					focusCamera = true;
				}
	        }
		};
		
		if( Box2DVars.ZOOM_CAMERA_IN_REGISTER_SCREEN )
		{
			registerWidget.actor().addListener(unfocusListener);
			background.addListener(unfocusListener);
			nameLabel.addListener(unfocusListener);
			passwordLabel.addListener(unfocusListener);
				
			nameTextField.addListener(focusListener);
			passwordTextField.addListener(focusListener);
		}
		
		submitButton = new Button(skin, "login");
		setCenterPosition(submitButton, -200);
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
		
		Label infoLabel = new Label("If you do not have account it will be created", skin, "default");
		setCenterPosition(infoLabel, -280);
		
		signboard = new TextButton("REGISTER", skin, "signboard"); // createImage("signboard", 0, 0);
		setCenterPosition(signboard, 175);
		
		addToScreen(backButton);
		
		addToScreen(registerWidget.actor());
		
		addToScreen(signboard);
		
		addToScreen(infoLabel);
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
		
		if(focusCamera && !cameraFocused)
		{
			handleCamera(true);
		}
		else if(focusCamera && cameraFocused)
		{
			handleCamera(false);
		}
	}
	
	private void handleCamera(boolean focus)
	{
		Vector2 target;
		float cameraViewWidth = this.camera.frustum.planePoints[1].x - this.camera.frustum.planePoints[0].x;
		
		if(focus)
		{
			target = new Vector2(0, -150);
		}
		else
		{
			target = new Vector2(0,0);
		}
		
		if((camera.position.x != target.x || camera.position.y != target.y))
		{
			this.camera.position.x += (-camera.position.x + target.x ) * 0.1f;
			this.camera.position.y += (-camera.position.y + target.y ) * 0.1f;
		}
		if(Math.abs(camera.position.x - target.x) < 0.2f && Math.abs(camera.position.y - target.y) < 0.2f)
		{
			cameraFocused = focus;
			focusCamera = false;
		}
		
		if(focus && cameraViewWidth > 900)
		{
			this.camera.zoom -= 0.02;
		}
		else if(!focus && cameraViewWidth < runner.SCREEN_WIDTH)
		{
			this.camera.zoom += 0.02;
		}
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
