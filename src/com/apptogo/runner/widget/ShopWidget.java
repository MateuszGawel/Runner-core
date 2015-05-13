package com.apptogo.runner.widget;

import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.shop.ShopItem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ShopWidget extends Widget
{
	private Table table;
	
	private Skin skin;
	
	private Label label;
	private Label priceLabel;
	
	private Button okButton;
	
	public ShopItem item;

	public ShopWidget()
	{
		super(Align.center, Align.center, 0f, WidgetType.SMALL, WidgetFadingType.NONE, true);
				
		skin = ResourcesManager.getInstance().getUiSkin( ScreenClass.MENU );
		
		okButton = new Button(skin, "yesButton");
		okButton.setPosition(270, -50.0f);
		
		label = new Label("", skin, "dialogLabel");
		label.setWrap(true);
		
		priceLabel = new Label("", skin, "dialogLabel");
		priceLabel.setY(-80);
		
		table = new Table(skin);
		table.setSize(520.0f, 400.0f);
		table.setPosition(-350.0f, -200.0f);
		
		table.add(label).width(520.0f);
		
		this.addActor(okButton);
		this.addActor(priceLabel);
        this.addActor(table);
	}
	
	public void setItem(ShopItem item)
	{
		this.label.setText(item.description);
		
		this.priceLabel.setText( String.valueOf( item.prices.get( item.currentLevel - 1 ) ) );
		this.priceLabel.setX(290 - priceLabel.getWidth()/2.0f);
		
		this.item = item;
	}
	
	//butItem potrzebuje playera dlatego listener bedzie nadawany z ShopScreen
	public void setListener(ClickListener listener)
	{
		okButton.addListener(listener);
	}
	
	public void shakePrice()
	{
		if( this.priceLabel.getActions().size == 0 )
		{
			float y = this.priceLabel.getY();
			this.priceLabel.setY(y + 15);
			
			MoveToAction action = new MoveToAction();
			action.setPosition(this.priceLabel.getX(), y);
			action.setInterpolation(Interpolation.bounceOut);
			action.setDuration(0.5f);
			
			this.priceLabel.addAction(action);
		}
	}
}