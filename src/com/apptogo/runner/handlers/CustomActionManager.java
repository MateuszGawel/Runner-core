package com.apptogo.runner.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CustomActionManager {

	private static CustomActionManager INSTANCE;
	private List<CustomAction> actions = new ArrayList<CustomAction>();
	private List<CustomAction> actionsCreated = new ArrayList<CustomAction>();
	
	public static void create()
	{
		INSTANCE = new CustomActionManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static CustomActionManager getInstance(){ return INSTANCE; }
	
	/**Dodaj akcje ktora wykona perform po ustalonym delay*/
	public void registerAction(CustomAction action){
		this.actionsCreated.add(action);
		action.setRegistered(true);
	}
	
	public void unregisterAction(CustomAction action){
		ListIterator<CustomAction> iter = actions.listIterator();
		while(iter.hasNext()) {
		    if (action == iter.next()){
		        iter.remove();
		        action.setRegistered(false);
		        break;
		    }
		}
	}
	
	private void handleActions(float delta){
		ListIterator<CustomAction> iter = actions.listIterator();
		while(iter.hasNext()) {
		    CustomAction action = iter.next();
		    action.act(delta);
		    if (action.isFinished()) {
		        iter.remove();
		    }
		}
	}

	public void prepareActions(){
		for(CustomAction action : actionsCreated){
			actions.add(action);
		}
		actionsCreated.clear();
	}
	
	public void act(float delta){
		handleActions(delta);
		
		for(CustomAction action : actionsCreated){
			actions.add(action);
		}
		actionsCreated.clear();
	}
}
