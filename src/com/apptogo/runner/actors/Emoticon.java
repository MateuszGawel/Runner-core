package com.apptogo.runner.actors;

import java.util.HashMap;

import com.apptogo.runner.enums.EmoticonType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Emoticon extends Group 
{
	Image cloud;
	HashMap<EmoticonType, Image> images;
	
	public Emoticon()
	{
		cloud = new Image( ResourcesManager.getInstance().getAtlasRegion("cloud") );
		
		setSize(cloud.getWidth(), cloud.getHeight());
		
		this.addActor(cloud);
		
		images = new HashMap<EmoticonType, Image>();
		
		for(EmoticonType emoticonType : EmoticonType.values())
		{
			images.put(emoticonType, EmoticonType.convertToImage(emoticonType));
		}
		
		for(Image image : images.values())
		{
			image.setVisible(false);
			
			image.setPosition(cloud.getWidth() / 2f - image.getWidth() / 2f, cloud.getHeight() / 2f - image.getHeight() / 2f);
			
			addActor(image);
		}		
		
		this.getColor().a = 0;
	}
	
	public void show(EmoticonType emoticonType)
	{
		for(Image image : images.values())
		{
			image.setVisible(false);
		}	
		
		images.get(emoticonType).setVisible(true);
		
		this.clearActions();
		this.getColor().a = 0;
		
		AlphaAction show = new AlphaAction();
		show.setAlpha(1);
		show.setDuration(0.7f);
		
		AlphaAction keep = new AlphaAction();
		keep.setAlpha(1);
		keep.setDuration(2f);
		
		AlphaAction hide = new AlphaAction();
		hide.setAlpha(0);
		hide.setDuration(0.5f);
		
		SequenceAction sequence = new SequenceAction(show, keep, hide);
		
		this.addAction(sequence);
	}
}
