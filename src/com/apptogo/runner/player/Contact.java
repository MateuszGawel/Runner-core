package com.apptogo.runner.player;

public class Contact implements Comparable<Contact>
{
	public String name;
	public boolean status;
	
	public Contact(String name, boolean status)
	{
		this.name = name;
		this.status = status;
	}

	@Override
	public int compareTo(Contact contact) 
	{
		if( this.status == true && contact.status != true )
		{
			return -1;
		}
		else if( this.status != true && contact.status == true )
		{
			return 1;
		}
		else
		{
			return this.name.compareTo(contact.name);
		}
	}
}
