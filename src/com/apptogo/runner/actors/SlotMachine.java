package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class SlotMachine extends Group
{
	enum phase
	{
		GO,
		LAST,
		END
	}
	
	Array<Image> slots;
	int currentSlotIndex;
	
	float width, height;
	
	float speed = 0.1f;

	Array<Float> speeds;
	
	phase p = phase.GO;
		
	public SlotMachine(String frameRegionName, int framesCount)
	{
		super();
		
		this.debug();
		
		slots = new Array<Image>();
		
		for(AtlasRegion region : ResourcesManager.getInstance().getAtlasRegionArray(frameRegionName, framesCount))
		{			
			slots.add( new Image(region) );
		
			slots.peek().setName(region.name);
			slots.peek().setPosition(0, 0);
			this.addActor(slots.peek());
		}

		slots.shuffle();
		
		speeds = new Array<Float>();
				
		for(float i = 0.01f; i < 0.4f; i += 0.01f)
		{
			speeds.add( (float)Math.sin(i) );
		}
		
		width = slots.first().getWidth();
		height = slots.first().getHeight();
		
		this.setSize(width, height * 3);
		
		MoveToAction action = new MoveToAction();
		action.setPosition(0, height*2);
		action.setDuration(speed);
		
		slots.first().addAction(action);
		currentSlotIndex = 0;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if( this.p != phase.END )
		{
			if( slots.get(currentSlotIndex).getY() >= this.height )
			{
				int tempIndex = (currentSlotIndex + 1 >= slots.size) ? 0 : currentSlotIndex+1;
				
				if( slots.get(tempIndex).getActions().size > 0 )
				{
					//pass
				}
				else
				{
					currentSlotIndex = (currentSlotIndex + 1 >= slots.size) ? 0 : currentSlotIndex+1;
					
					MoveToAction action = new MoveToAction();
					
					if(this.p == phase.LAST)
					{
						action.setPosition(0, height);
						
						p = phase.END;
					}
					else
					{
						action.setPosition(0, height*2);
						
						if(speeds.size == 2) //bo jeszcze jedna akcja do samej gory + jedna do polowy
						{
							this.p = phase.LAST;
						}
					}
					
					action.setDuration( speeds.removeIndex(0) );					
					
					slots.get(currentSlotIndex).addAction(action);					
				}
			}
		}
		else
		{
			for(Image slot : slots)
			{
				if( slot.getY() == this.height ) Logger.log(this, slot.getName() );
			}
		}
		
		for(Image slot : slots)
		{
			if(slot.getActions().size <= 0 && slot.getY() >= this.height * 2)
			{
				slot.setPosition(0, 0);
			}
		}
	}
}
