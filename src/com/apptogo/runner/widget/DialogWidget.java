package com.apptogo.runner.widget;

import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogWidget extends Widget
{

	private Label label;
	private Button yesButton;
	private Button noButton;
	
	private Skin skin;
	
	/** @param noListener podanie null spowoduje przypisanie ToggleListener tego Widgetu
	 *  @param yesListener podanie null spowoduje przypisanie ToggleListener tego Widgetu
	 */
	public DialogWidget(String message, ClickListener noListener, ClickListener yesListener)
	{
		super(Align.center, Align.center, 0f, WidgetType.SMALL, WidgetFadingType.NONE, true);
		
		if(noListener == null) noListener = this.toggleWidgetListener;
		if(yesListener == null) yesListener = this.toggleWidgetListener;
		
		skin = ResourcesManager.getInstance().getUiSkin();
		
		label = new Label(message, skin, "dialogLabel");
		label.setPosition(-300f, 50f);
                
        this.addActor(label);
	}
	
}