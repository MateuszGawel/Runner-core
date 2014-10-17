package com.apptogo.runner.enums;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public enum PowerupType
{
	NONE, SUPERJUMP, SUPERSPEED;
	
	public static Array<PowerupType> values;
	
	static
	{
		values = new Array<PowerupType>( PowerupType.values() );
		values.removeValue(PowerupType.NONE, true);
	}
	
	static public PowerupType parseFromString(String key)
	{
		if( key.equals( PowerupType.NONE.toString() ) )
		{
			return PowerupType.NONE;
		}
		else if( key.equals( PowerupType.SUPERJUMP.toString() ) )
		{
			return PowerupType.SUPERJUMP;
		}
		else if( key.equals( PowerupType.SUPERSPEED.toString() ) )
		{
			return PowerupType.SUPERSPEED;
		}
		//else if() kolejne powerupy
		else return null;
	}
	
	static public Button convertToPowerupButton(PowerupType powerupType, CharacterType characterType)
	{
		Skin skin = ResourcesManager.getInstance().getGuiSkin();
		
		Button button = new Button(skin, "banditBlankButton");
		
		if(powerupType == PowerupType.NONE)
		{
			if(characterType == CharacterType.BANDIT)
			{
				button = new Button(skin, "banditBlankButton");
			}
			else if(characterType == CharacterType.ARCHER)
			{
				button = new Button(skin, "archerBlankButton");
			}
			else if(characterType == CharacterType.ALIEN)
			{
				button = new Button(skin, "alienBlankButton");
			}
		}
		else if(powerupType == PowerupType.SUPERJUMP)
		{
			if(characterType == CharacterType.BANDIT)
			{
				button = new Button(skin, "banditSuperJumpButton");
			}
			else if(characterType == CharacterType.ARCHER)
			{
				button = new Button(skin, "archerSuperJumpButton");
			}
			else if(characterType == CharacterType.ALIEN)
			{
				button = new Button(skin, "alienSuperJumpButton");
			}
		}
		else if(powerupType == PowerupType.SUPERSPEED)
		{
			if(characterType == CharacterType.BANDIT)
			{
				button = new Button(skin, "banditSuperSpeedButton");
			}
			else if(characterType == CharacterType.ARCHER)
			{
				button = new Button(skin, "archerSuperSpeedButton");
			}
			else if(characterType == CharacterType.ALIEN)
			{
				button = new Button(skin, "alienSuperSpeedButton");
			}
		}
		
		button.setPosition(20/PPM, 20/PPM);
		button.setSize(button.getWidth()/PPM, button.getHeight()/PPM);
		button.setBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight());
		
		return button;
	}

	public static PowerupType getRandom() 
	{
		double random = Math.random();
		
		random = ( random >= 1.0 )?0.9:random;
		
		int index = (int) Math.floor(random * (values.size));
		
		return values.get( index );
	}
}