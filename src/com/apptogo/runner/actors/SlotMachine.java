package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class SlotMachine extends Group
{
	enum SlotMachinePhase
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
	
	SlotMachinePhase phase = SlotMachinePhase.END;
	
	public String value = null;
		
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

		width = slots.first().getWidth();
		height = slots.first().getHeight();
		
		this.setSize(width, height * 3);
	}
	
	void reset()
	{
		slots.shuffle();
		
		speeds = new Array<Float>();
				
		for(float i = 0.01f; i < 0.4f; i += 0.01f)
		{
			speeds.add( (float)Math.sin(i) );
		}
		
		value = null;
		
		currentSlotIndex = 0;
	}
	
	public void start(float delay)
	{
		if(phase == SlotMachinePhase.END)
		{
			reset();
			
			MoveToAction delayAction = new MoveToAction();
			delayAction.setPosition(0, 0);
			delayAction.setDuration(delay);
			
			MoveToAction action = new MoveToAction();
			action.setPosition(0, height*2);
			action.setDuration(speed);
			
			slots.first().addAction( new SequenceAction(delayAction, action) );
			
			phase = SlotMachinePhase.GO;
		}
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if( this.phase != SlotMachinePhase.END )
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
					
					if(this.phase == SlotMachinePhase.LAST)
					{
						action.setPosition(0, height);
						
						phase = SlotMachinePhase.END;
					}
					else
					{
						action.setPosition(0, height*2);
						
						if(speeds.size == 2) //bo jeszcze jedna akcja do samej gory + jedna do polowy
						{
							this.phase = SlotMachinePhase.LAST;
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
				if( slot.getY() == this.height ) value = slot.getName();
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
