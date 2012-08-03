package com.schwimmer.android.wifistatus;

import static android.content.Context.WIFI_SERVICE;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;

public class ToggleWifiActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);

		boolean enabled = manager.isWifiEnabled();
		showToast(enabled);
		manager.setWifiEnabled(!enabled);

		finish();
	}

	private void showToast(boolean enabled) {
		Resources resources = getResources();
		String message;
		if (enabled) {
			message = resources.getString(R.string.turning_wifi_off);
		} else {
			message = resources.getString(R.string.turning_wifi_on);
		}
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}