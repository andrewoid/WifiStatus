package com.schwimmer.android.wifistatus;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;

public class ToggleWifi extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		boolean enabled = manager.isWifiEnabled();
		showToast(enabled);

		manager.setWifiEnabled(!enabled);

		finish();
	}

	private void showToast(boolean enabled) {
		if (enabled) {
			Toast.makeText(ToggleWifi.this, "Wifi OFF.", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(ToggleWifi.this, "Turning Wifi ON...",
					Toast.LENGTH_SHORT).show();
		}
	}
}