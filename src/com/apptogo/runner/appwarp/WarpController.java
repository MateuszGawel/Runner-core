package com.apptogo.runner.appwarp;


import java.util.HashMap;

import com.apptogo.runner.logger.Logger;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

public class WarpController 
{
	private static WarpController instance = new WarpController();
	public static WarpController getInstance()
	{
		return instance;
	}
	
	private boolean showLog = true;
	
	private final String apiKey = "60e7bc7bbade5df33eaf9f5f7a430e481c6a3d490fdbc1fa7f8aa0400908e0be";
	private final String secretKey = "aac11a07b0b6aea604fe66e5cdb2ba79407b3176f27a993815288578ff30aa15";
	
	private WarpClient warpClient;
	
	private String localUser;
	private String roomId;
	
	public boolean isOnline = false;
	public boolean isConnected = false;
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
		initAppwarp();
		
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addChatRequestListener(new ChatListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
		//warpClient.addLobbyRequestListener(new LobbyListener(this));
	}
	
	public void setListener(WarpListener listener)
	{
		this.warpListener = listener;
	}
	
	private void initAppwarp()
	{
		try 
		{
			WarpClient.initialize(apiKey, secretKey);
			warpClient = WarpClient.getInstance();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
			warpClient.unsubscribeLobby();
			warpClient.leaveLobby();
		}
		
		warpClient.disconnect();
	}
	
	private void startGame()
	{
		STATE = STARTED;
		warpListener.onGameStarted("Start the Game");
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
	
	public void updateResult(int code, String msg)
	{
		if(isConnected)
		{
			STATE = COMPLETED;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}
	
	public int getState()
	{
		return this.STATE;
	}
			
	private void waitForOtherUser()
	{
		STATE = WAITING;
		warpListener.onWaitingStarted("Waiting for other user");
	}
	
	private void handleError()
	{
		if( roomId!=null && roomId.length() > 0 )
		{
			warpClient.deleteRoom(roomId);
		}
		disconnect();
	}
	
	public void handleLeave()
	{
		if(isConnected)
		{
			
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
			
			if( STATE != STARTED )
			{
				warpClient.deleteRoom(roomId);
				Logger.log(this, "USUWAM POKOJ" + roomId);
			}
			
			warpClient.disconnect();
		}
	}
	
	private void disconnect()
	{
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
		warpClient.removeZoneRequestListener(new ZoneListener(this));
		warpClient.removeRoomRequestListener(new RoomListener(this));
		warpClient.removeNotificationListener(new NotificationListener(this));
		warpClient.disconnect();
		
		isOnline = false;
	}
	
	public void joinRandomRoom()
	{
		//bo tylko takie pokoje bedziemy tworzyc i tylko takie nas interesuja
		warpClient.joinRoomInRange(4, 4, false);
	}
	
	//---CHAT LISTENER
	public void onSendChatDone(boolean status)
	{
		Logger.log(this, "onSendChatDone: " + status);
	}
	//---
	
	//---CONNECTION LISTENER
	public void onConnectDone(boolean status)
	{
		Logger.log(this, "onConnectDone: " + status);
		
		if(status)
		{
			warpClient.initUDP();
			warpClient.joinLobby();
			warpClient.subscribeLobby();
			//warpClient.joinRoomInRange(0, 2, false);
			isOnline = true;
		}
		else
		{
			isConnected = false;
			handleError();
		}
	}
	public void onDisconnectDone(boolean status)
	{
		Logger.log(this, "onDisconnectDone: " + status);
	}
	public void setIsUDPEnabled(boolean isEnabled)
	{
		this.isUDPEnabled = isEnabled;
	}
	//---
	
	//---NOTIFICATION LISTENER
	public void onUserJoinedRoom(String roomId, String userName)
	{
		Logger.log(this, "onUserJoinedRoom: roomId = " + roomId + ", userName = " + userName);
		
		warpClient.getLiveRoomInfo(roomId);
		
		NotificationManager.getInstance().screamMyName();
		
		/*
		 * if room id is same and username is different then start the game
		 */
		//if( localUser.equals(userName) == false )
		//{
		//	startGame();
		//}
	}
	public void onUserLeftRoom(String roomId, String userName)
	{
		Logger.log(this, "onUserLeftRoom: roomId = " + roomId + ", userName = " + userName);
		if(STATE==STARTED && !localUser.equals(userName))
		{// Game Started and other user left the room
			warpListener.onGameFinished(ENEMY_LEFT, true);
		}
	}
	public void onUpdatePeersReceived(String message)
	{
		Logger.log(this, "onUpdatePeersReceived: " + message);
		
		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@")+2, message.length());
		
		if( !localUser.equals(userName) )
		{
			warpListener.onGameUpdateReceived(data);
		}
	}
	public void onUserChangeRoomProperty(String userName, int code)
	{
		Logger.log(this, "onUserChangeRoomProperty: userName = " + userName + ", code = " + code);
		
		if( localUser.equals(userName) == false )
		{
			STATE = FINISHED;
			warpListener.onGameFinished(code, true);
		}
		else
		{
			warpListener.onGameFinished(code, false);
		}
	}
	public void onUserJoinedLobby(LobbyData data, String userName)
	{
		Logger.log(this, "onUserJoinedLobby: lobbyName = " + data.getName() + ", maxUsers = " + data.getMaxUsers() + ", userName = " + userName);
	}
	//---
	
	//---ROOM LISTENER
	public void onJoinRoomDone(RoomEvent event)
	{
		Logger.log(this, "onJoinRoomDone: " + event.getResult());
		
		if( event.getResult() == WarpResponseResultCode.SUCCESS )
		{// success case
			this.roomId = event.getData().getId();
			warpClient.subscribeRoom(roomId);
			Logger.log(this, "jest");
		}
		else if( event.getResult() == WarpResponseResultCode.RESOURCE_NOT_FOUND )
		{// no such room found
			Logger.log(this, "nie ma");
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("result", "");
			warpClient.createRoom("superjumper", "shephertz", 4, data);
		}
		else
		{
			warpClient.disconnect();
			handleError();
		}
	}
	public void onSubscribeRoomDone(String roomId)
	{
		Logger.log(this, "onSubscribeRoomDone: " + roomId);
		
		if( roomId != null )
		{
			isConnected = true;
			warpClient.getLiveRoomInfo(roomId);
		}
		else
		{
			warpClient.disconnect();
			handleError();
		}
	}
	public void onGetLiveRoomInfoDone(String[] liveUsers)
	{
		Logger.log(this, "onGetLiveRoomInfo: liveUsersCount = " + liveUsers.length);
		if(liveUsers!=null)
		{
			if( liveUsers.length == 3 )
			{
				startGame();	
			}
			else
			{
				waitForOtherUser();
			}
		}
		else
		{
			warpClient.disconnect();
			handleError();
		}
	}
	//---
	
	//---ZONE LISTENER
	public void onCreateRoomDone(String roomId)
	{
		Logger.log(this, "onCreateRoomDone: " + roomId);
		
		if( roomId != null )
		{
			warpClient.joinRoom(roomId);
		}
		else
		{
			handleError();
		}
	}
	//--
	
	//---LOBBY LISTENER
	public void onJoinLobbyDone(LobbyData data)
	{
		Logger.log(this, "onJoinLobbyDone: name = " + data.getName());
	}
	public void onSubscribeLobbyDone(LobbyData data)
	{
		Logger.log(this, "onSubscribeLobbyDone: id = " + data.getId());
	}
	//---
}
