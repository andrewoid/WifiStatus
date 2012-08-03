package com.schwimmer.android.wifistatus;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiStatusReceiver extends BroadcastReceiver {
	
	private void onConnected(Context context) {
		Log.i(WifiStatusReceiver.class.getName(), 
				"removing disconnected wifi notification");
		
		TrayControl.remove(
				context, 
				R.drawable.wifilogo_disconnected);
	}
	
	private void onDisconnected(Context context) {
		Log.i(WifiStatusReceiver.class.getName(), 
				"state == idle | scanning | failed");
		
		Resources resources = context.getResources();
		Intent launchIntent = new Intent();
		launchIntent.setComponent( 
				new ComponentName(ToggleWifi.class.getPackage().getName(),
						ToggleWifi.class.getName()) );
		launchIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		
		TrayControl.add(context,
				R.drawable.wifilogo_disconnected, 
				resources.getString(R.string.title), 
				resources.getString(R.string.instructions), 
				launchIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(WifiStatusReceiver.class.getName(), "Intent Received");
		
		WifiManager manager = (WifiManager) 
			context.getSystemService(Context.WIFI_SERVICE);
		
		String action = intent.getAction();
		if ( WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action) ) {
			int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 
					WifiManager.WIFI_STATE_UNKNOWN);
			
			if ( state == WifiManager.WIFI_STATE_ENABLED ||
					state == WifiManager.WIFI_STATE_ENABLING ||
					state == WifiManager.WIFI_STATE_UNKNOWN ) {
				
				onDisconnected(context);
				
			}
			else {
				onConnected(context);
			}
			
		}
		else if ( WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action) ) {

			
			NetworkInfo networkInfo = 
				(NetworkInfo) intent.getParcelableExtra(
						WifiManager.EXTRA_NETWORK_INFO);
			

			if ( networkInfo != null ) {
				DetailedState state = networkInfo.getDetailedState();

				Log.i(WifiStatusReceiver.class.getName(), 
						state.toString());
				
				if ( isDisconnected(manager, state) ) {

					onDisconnected(context);
					
				}
				else {
					onConnected(context);
				}
			}
			else {
				Log.i(WifiStatusReceiver.class.getName(), 
						"NetworkInfo == null");
			}
		}
		
		
		
	}

	private boolean isDisconnected(WifiManager manager, DetailedState state) {
		return manager.isWifiEnabled() && 
				( state == DetailedState.IDLE || 
				state == DetailedState.SCANNING || 
				state == DetailedState.FAILED ||
				state == DetailedState.DISCONNECTED ||
				state == DetailedState.DISCONNECTING ||
				state == DetailedState.SUSPENDED );
	}

}
