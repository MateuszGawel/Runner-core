package com.apptogo.runner.userdata;

import com.apptogo.runner.logger.Logger;

public class UserData 
{
	public String key;
	
	//player
	public String playerName;
	public String slowAmmount;

	//powerup
	public String powerup;
	
	public UserData(Object key){
		this.key = key.toString();
	}
}
