package com.apptogo.runner.widget;

import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InfoWidget extends Widget
{

	private Label label;
	private Button okButton;
	
	private Skin skin;
	
	public InfoWidget(String message)
	{
		super(Align.center, Align.center, 0f, WidgetType.SMALL, WidgetFadingType.NONE, true);
				
		skin = ResourcesManager.getInstance().getUiSkin();
		
		label = new Label(message, skin, "dialogLabel");
		label.setPosition(-300f, 50f);
                
        this.addActor(label);
	}
	
}