package com.apptogo.runner.widget;

import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogWidget extends Widget
{
	private Table table;
	
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
		
		yesButton = new Button(skin, "yesButton");
		yesButton.setPosition(270, 10);
		yesButton.addListener(yesListener);
		
		noButton = new Button(skin, "noButton");
		noButton.setPosition(270, -100);
		noButton.addListener(noListener);
		
		label = new Label(message, skin, "dialogLabel");
		
		//przy czcionce WOODFONT max szerokosc wiersza to 23 znaki [lub 356px]
		LabelStyle ls = label.getStyle();
		ls.font = FontType.convertToFont( FontType.WOODFONT );
		ls.fontColor = FontType.convertToColor( FontType.WOODFONT );
		label.setStyle(ls);
		label.setWrap(true);
		
		table = new Table(skin);
		table.setSize(520.0f, 400.0f);
		table.setPosition(-350.0f, -200.0f);
		
		table.add(label).width(520.0f);
		
		this.addActor(yesButton);
		this.addActor(noButton);
        this.addActor(table);
	}

	public void setYesListener(ClickListener listener) 
	{
		yesButton.clearListeners();
		yesButton.addListener(listener);
	}
	
	public void setNoListener(ClickListener listener) 
	{
		noButton.clearListeners();
		noButton.addListener(listener);
	}

	public void setLabel(String text) 
	{
		label.setText( text );
	}
}