package com.apptogo.runner.userdata;

import com.apptogo.runner.logger.Logger;

public class UserData 
{
	public String key;
	
	//player
	public boolean me;
	public String playerName;
	public float slowPercent = 0; //od 0 do 1

	//powerup
	public String powerup;
	
	
	public UserData(Object key){
		this.key = key.toString();
	}
}
