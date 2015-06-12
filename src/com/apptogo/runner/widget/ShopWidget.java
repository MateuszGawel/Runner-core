package com.apptogo.runner.widget;

import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
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
	private Label titileLabel;
	private Label priceLabel;
	
	private Button okButton;
	
	public ShopItem item;

	public ShopWidget()
	{
		super(Align.center, 600, 760, WidgetType.SMALL, WidgetFadingType.TOP_TO_BOTTOM, true);
		this.setEasing( Interpolation.elasticOut );
				
		skin = ResourcesManager.getInstance().getUiSkin( ScreenClass.MENU );
		
		okButton = new Button(skin, "buyButton");
		okButton.setPosition(220, 760 - 30.0f);
		
		label = new Label("", skin, "dialogLabel");
		label.setWrap(true);
		label.setAlignment(Align.topLeft);
		label.getColor().a = 0.7f;
		
		titileLabel = new Label("", skin, "woodBig");
		
		priceLabel = new Label("", skin, "coinLabel");
		priceLabel.setWidth(250);
		priceLabel.setAlignment(Align.center);
		priceLabel.setPosition(150, 760 - 70);
		
		table = new Table(skin);
		table.setSize(520.0f, 280.0f);
		table.setPosition(-350.0f, 760 - 140.0f);
		
		table.add(titileLabel).width(520.0f).height(80);
		table.row();
		table.add(label).width(520.0f).height(200);

		this.addActor(okButton);
		this.addActor(priceLabel);
        this.addActor(table);
	}
	
	public void setItem(ShopItem item)
	{
		this.label.setText(item.description);
		this.titileLabel.setText(item.title);
		
		priceLabel.setText(item.getCostLabel());
		
		this.addActor(priceLabel);
		
		this.item = item;
	}
	
	//butItem potrzebuje playera dlatego listener bedzie nadawany z ShopScreen
	public void setListener(ClickListener listener)
	{
		okButton.clearListeners();
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