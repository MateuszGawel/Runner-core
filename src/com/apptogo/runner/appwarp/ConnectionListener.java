package com.apptogo.runner.appwarp;

import com.apptogo.runner.handlers.Logger;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class ConnectionListener implements ConnectionRequestListener {

	WarpController callBack;
	
	public ConnectionListener(WarpController callBack){
		this.callBack = callBack;
	}
	
	public void onConnectDone(ConnectEvent e) {
		if(e.getResult()==WarpResponseResultCode.SUCCESS){
			callBack.onConnectDone(true);
		}else{
			Logger.log(this, "nie uda³o siê po³¹czyæ bo: " + e.getResult());
			callBack.onConnectDone(false);
		}
	}

	public void onDisconnectDone(ConnectEvent e) {
		
	}

	@Override
	public void onInitUDPDone (byte result) {
		if(result==WarpResponseResultCode.SUCCESS){
			callBack.isUDPEnabled = true;
		}
	}

}
