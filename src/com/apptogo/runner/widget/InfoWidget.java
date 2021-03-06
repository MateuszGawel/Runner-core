package com.apptogo.runner.widget;

import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class InfoWidget extends Widget
{
	private Table table;
	
	private Skin skin;
	
	private Label label;
	
	private Button okButton;

	public InfoWidget(String message)
	{
		super(Align.center, Align.center, 0f, WidgetType.SMALL, WidgetFadingType.NONE, true);
				
		skin = ResourcesManager.getInstance().getUiSkin( ScreenClass.MENU );
		
		okButton = new Button(skin, "yesButton");
		okButton.setPosition(270, -50.0f);
		okButton.addListener( this.toggleWidgetListener );
		
		label = new Label(message, skin, "dialogLabel");
		
		label.setWrap(true);
		
		table = new Table(skin);
		table.setSize(520.0f, 400.0f);
		table.setPosition(-350.0f, -200.0f);
		
		table.add(label).width(520.0f);
		
		this.addActor(okButton);
        this.addActor(table);
	}

	public void setLabel(String text) 
	{
		label.setText( text );
	}

	public void setMessage(String message) 
	{
		label.setText(message);
	}
}