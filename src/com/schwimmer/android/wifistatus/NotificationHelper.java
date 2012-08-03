package com.schwimmer.android.wifistatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationHelper {

	public void add(Context context, int iconId, String title,
			String instructions, Intent launchIntent) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(iconId, null, 0);
		notification.flags |= Notification.FLAG_NO_CLEAR;

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				launchIntent, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(context, title, instructions,
				contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		notificationManager.notify(iconId, notification);

	}

	public void remove(Context context, int iconId) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.cancel(iconId);

	}
}
