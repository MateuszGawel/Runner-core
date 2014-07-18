package com.apptogo.runner.handlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Widget
{
	public enum WidgetOrientation
	{
		LEFT,
		RIGHT,
		TOP,
		BOTTOM
	}
	
	private float MOVE_ACTION_DURATION = 100;
	
	private float x;
	private float y;
	private float width;
	private float height;
	
	private Group group;
	private Container container;
	private ScrollPane scrollPane;
	
	private boolean isShowed = false;
	protected Widget widget = this;
	
	public Widget(float x, float y, float width, float height)//, float width, float height, WidgetOrientation orientation)
	{	
		//group = new Group();
		
		//container.setSize(width, height);
		//container.set
		group = new Group();
		group.setPosition(x, y);
		//teraz trzeba bedzie 
		//this.width = width;
		//this.height = height;
	}
	
	public void addActor(Actor actor)
	{
		group.addActor(actor);
	}
	
	private void toggle()
	{
		MoveToAction moveAction = new MoveToAction();
		moveAction.setDuration( MOVE_ACTION_DURATION );
		
		if( isShowed )
		{
			moveAction.setPosition(x, y); //hide
			
			isShowed = false;
		}
		else
		{
			moveAction.setPosition(x, y); //show
			
			isShowed = true;
		}
		
		group.addAction( moveAction );
	}
		
	public Group actor()
	{
		return group;
	}
}
