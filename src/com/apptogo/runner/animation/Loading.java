package com.apptogo.runner.animation;

import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;


public class Loading extends Group
{
	private int count = 7;
	private int margin = 22;
	
	private float speed = 4;
	
	private int counter = 0;
	
	private Array<Image> bases = new Array<Image>();
	private Array<Image> covers = new Array<Image>();
	
	private boolean isStarted = true;
	
	public Loading()
	{
		this.setSize(count*margin, margin);

		AtlasRegion base = ResourcesManager.getInstance().getAtlasRegion(ScreenClass.STILL, "loadingBase");
		AtlasRegion cover = ResourcesManager.getInstance().getAtlasRegion(ScreenClass.STILL, "loadingCover");
		
		for(int i = 0; i < count; i++)
		{
			Image baseImage = new Image( base );
			baseImage.setPosition(i * margin + base.offsetX, base.offsetY);
			
			bases.add( baseImage );
			
			Image coverImage = new Image( cover );
			coverImage.setPosition(i * margin + base.offsetX / 2.0f, base.offsetY / 2.0f);
			coverImage.getColor().a = 0.0f;
			
			covers.add( coverImage );
		}
		
		for(Image b : bases) this.addActor(b);
		for(Image c : covers) this.addActor(c);
		
		this.setTransform(false);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		counter++;
		
		if(isStarted)
		{
			for(int i = 0; i < covers.size; i++)
			{
				float timeStep = (counter % ( (count + 3) * speed) ) / speed;
				
				if( timeStep > i && timeStep < i + 1 )
					covers.get(i).getColor().a = 0.5f;
				else if( covers.get(i).getColor().a >= 0.05f )
					covers.get(i).getColor().a -= 0.05f;
				else
					covers.get(i).getColor().a = 0.0f;
			}
		}
	}
	
	public void start()
	{
		this.isStarted = true;
		counter = 0;
	}
}
