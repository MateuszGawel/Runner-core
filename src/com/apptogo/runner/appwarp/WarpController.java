package com.apptogo.runner.appwarp;


import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONObject;

import com.apptogo.runner.handlers.Logger;
import com.badlogic.gdx.utils.Json;
import com.shephertz.app42.gaming.multiplayer.client.Constants;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

public class WarpController {

	private static WarpController INSTANCE = new WarpController();
	public static WarpController getInstance() { return INSTANCE; }


	
	private final String apiKey = "60e7bc7bbade5df33eaf9f5f7a430e481c6a3d490fdbc1fa7f8aa0400908e0be";
	private final String secretKey = "aac11a07b0b6aea604fe66e5cdb2ba79407b3176f27a993815288578ff30aa15";
	
	private WarpClient warpClient;
	
	private String localUser;
	private String roomId;
	
	private boolean isConnected = false;
	boolean isUDPEnabled = false;
	
	private WarpListener warpListener ;
	
	private int STATE;
	
	// Game state constants
	public static final int WAITING = 1;
	public static final int STARTED = 2;
	public static final int COMPLETED = 3;
	public static final int FINISHED = 4;
	
	// Game completed constants
	public static final int GAME_WIN = 5;
	public static final int GAME_LOOSE = 6;
	public static final int ENEMY_LEFT = 7;
	
	public WarpController() 
	{
		WarpClient.initialize(apiKey, secretKey);
		
		try 
		{
			warpClient = WarpClient.getInstance();
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		warpClient.addChatRequestListener(new ChatListener(this));
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
	}
	
	public void setListener(WarpListener listener)
	{
		this.warpListener = listener;
	}
		
	public void startApp(String localUser)
	{
		this.localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}
	
	public void stopApp()
	{
		if(isConnected)
		{
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
		}
		warpClient.disconnect();
	}
		
	public void sendGameUpdate(String msg)
	{
		if(isConnected)
		{
			if(isUDPEnabled)
			{
				warpClient.sendUDPUpdatePeers((localUser+"#@"+msg).getBytes());
			}
			else
			{
				warpClient.sendUpdatePeers((localUser+"#@"+msg).getBytes());
			}
		}
	}
	
	public void updateResult(int code, String msg){
		if(isConnected){
			STATE = COMPLETED;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}
	
	public void onConnectDone(boolean status)
	{
		if(status)
		{
			warpClient.initUDP();
			warpClient.joinRoomInRange(1, 1, false);
		}
		else
		{
			isConnected = false;
			handleError();
		}
	}
	
	public void onDisconnectDone(boolean status){
		
	}
	
	public void onRoomCreated(String roomId){
		if(roomId!=null){
			warpClient.joinRoom(roomId);
		}else{
			handleError();
		}
	}
	
	public void onJoinRoomDone(RoomEvent event)
	{
		if(event.getResult()==WarpResponseResultCode.SUCCESS)
		{
			this.roomId = event.getData().getId();
			warpClient.subscribeRoom(roomId);
		}
		else if(event.getResult()==WarpResponseResultCode.RESOURCE_NOT_FOUND)
		{
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("result", "");
			warpClient.createRoom("superjumper", "shephertz", 2, data);
		}
		else
		{
			warpClient.disconnect();
			handleError();
		}
	}
	
	public void onRoomSubscribed(String roomId){
		Logger.log(this, "onSubscribeRoomDone: "+roomId);
		if(roomId!=null){
			isConnected = true;
			warpClient.getLiveRoomInfo(roomId);
		}else{
			warpClient.disconnect();
			handleError();
		}
	}
	
	public void onGetLiveRoomInfo(String[] liveUsers){
		Logger.log(this, "onGetLiveRoomInfo: "+liveUsers.length);
		if(liveUsers!=null){
			if(liveUsers.length==2){
				startGame();	
			}else{
				Logger.log(this, "czekam na gracza");
				waitForOtherUser();
			}
		}
		else{
			warpClient.disconnect();
			handleError();
		}
	}
	
	public void onUserJoinedRoom(String roomId, String userName){
		/*
		 * if room id is same and username is different then start the game
		 */
		if(localUser.equals(userName)==false){
			startGame();
		}
	}

	public void onSendChatDone(boolean status){
		Logger.log(this, "onSendChatDone: "+status);
	}
	
	public void onGameUpdateReceived(String message){
//		log("onMoveUpdateReceived: message"+ message );
		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@")+2, message.length());
		if(!localUser.equals(userName)){
			warpListener.onGameUpdateReceived(data);
		}
	}
	
	public void onResultUpdateReceived(String userName, int code){
		if(localUser.equals(userName)==false){
			STATE = FINISHED;
			warpListener.onGameFinished(code, true);
		}else{
			warpListener.onGameFinished(code, false);
		}
	}
	
	public void onUserLeftRoom(String roomId, String userName){
		Logger.log(this, "onUserLeftRoom "+userName+" in room "+roomId);
		if(STATE==STARTED && !localUser.equals(userName)){// Game Started and other user left the room
			warpListener.onGameFinished(ENEMY_LEFT, true);
		}
	}
	
	public int getState(){
		return this.STATE;
	}
	

	
	private void startGame(){
		STATE = STARTED;
		warpListener.onGameStarted("Start the Game");
	}
	
	private void waitForOtherUser(){
		STATE = WAITING;
		warpListener.onWaitingStarted("Waiting for other user");
	}
	
	private void handleError(){
		if(roomId!=null && roomId.length()>0){
			warpClient.deleteRoom(roomId);
		}
		disconnect();
	}
	
	public void handleLeave(){
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
			if(STATE!=STARTED){
				warpClient.deleteRoom(roomId);
			}
			warpClient.disconnect();
		}
	}
	
	private void disconnect(){
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
		warpClient.removeZoneRequestListener(new ZoneListener(this));
		warpClient.removeRoomRequestListener(new RoomListener(this));
		warpClient.removeNotificationListener(new NotificationListener(this));
		warpClient.disconnect();
	}
}
