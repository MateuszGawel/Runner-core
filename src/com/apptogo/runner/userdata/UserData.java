package com.apptogo.runner.userdata;

import com.apptogo.runner.logger.Logger;

public class UserData 
{
	public String key;
	public String arg1;
	
	public UserData(Object key)
	{
		this.key = key.toString();
	}
	
	public UserData(Object key, Object arg1)
	{
		this.key = key.toString();
		this.arg1 = arg1.toString();
	}
	
	public static String key(Object object)
	{
		UserData userData = (UserData)object;
		return userData.key;
	}
}
