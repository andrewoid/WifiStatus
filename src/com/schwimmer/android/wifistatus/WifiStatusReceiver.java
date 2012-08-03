package com.schwimmer.android.wifistatus;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.net.wifi.WifiManager.EXTRA_NETWORK_INFO;
import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;
import static android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;

// TODO: this class does too much
public class WifiStatusReceiver extends BroadcastReceiver {

	private NotificationHelper trayControl;

	public WifiStatusReceiver() {
		trayControl = new NotificationHelper();
	}

	private void onConnected(Context context) {	
		removeDisconntNotification(context);
	}

	private void removeDisconntNotification(Context context) {
		trayControl.remove(context, R.drawable.wifilogo_disconnected);
	}

	private void onDisconnected(Context context) {
		showDisconnectNotification(context);
	}

	private void showDisconnectNotification(Context context) {
		Intent launchIntent = getToggleWifiLaunchIntent();

		Resources resources = context.getResources();
		trayControl.add(context, R.drawable.wifilogo_disconnected,
				resources.getString(R.string.title),
				resources.getString(R.string.instructions), launchIntent);
	}

	private Intent getToggleWifiLaunchIntent() {
		Intent launchIntent = new Intent();
		launchIntent.setComponent(new ComponentName(ToggleWifiActivity.class
				.getPackage().getName(), ToggleWifiActivity.class.getName()));
		launchIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
		return launchIntent;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (WIFI_STATE_CHANGED_ACTION.equals(action)) {
			handleWifiStateChange(context, intent);
		} else if (NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			handleNetworkStateChange(context, intent);
		}
	}

	private void handleNetworkStateChange(Context context, Intent intent) {
		NetworkInfo networkInfo = (NetworkInfo) intent
				.getParcelableExtra(EXTRA_NETWORK_INFO);

		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		if (networkInfo != null) {
			DetailedState state = networkInfo.getDetailedState();

			if (isNetworkDisconnected(manager, state)) {
				onDisconnected(context);
			} else {
				onConnected(context);
			}
		}
	}

	private void handleWifiStateChange(Context context, Intent intent) {
		int state = intent.getIntExtra(EXTRA_WIFI_STATE, WIFI_STATE_UNKNOWN);
		if (isWifiDisconnected(state)) {
			onDisconnected(context);
		} else {
			onConnected(context);
		}
	}

	private boolean isWifiDisconnected(int state) {
		return state == WIFI_STATE_ENABLED || state == WIFI_STATE_ENABLING
				|| state == WIFI_STATE_UNKNOWN;
	}

	private boolean isNetworkDisconnected(WifiManager manager,
			DetailedState state) {
		return manager.isWifiEnabled()
				&& (state == DetailedState.IDLE
						|| state == DetailedState.SCANNING
						|| state == DetailedState.FAILED
						|| state == DetailedState.DISCONNECTED
						|| state == DetailedState.DISCONNECTING || state == DetailedState.SUSPENDED);
	}

}
