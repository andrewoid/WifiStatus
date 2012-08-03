package com.schwimmer.android.wifistatus;

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

		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		boolean enabled = manager.isWifiEnabled();
		showToast(enabled);

		manager.setWifiEnabled(!enabled);

		finish();
	}

	private void showToast(boolean enabled) {
		Resources resources = getResources();
		if (enabled) {
			String s = resources.getString(R.string.turning_wifi_off);
			Toast.makeText(ToggleWifiActivity.this, s, Toast.LENGTH_SHORT)
					.show();
		} else {
			String s = resources.getString(R.string.turning_wifi_on);
			Toast.makeText(ToggleWifiActivity.this, s,
					Toast.LENGTH_SHORT).show();
		}
	}
}