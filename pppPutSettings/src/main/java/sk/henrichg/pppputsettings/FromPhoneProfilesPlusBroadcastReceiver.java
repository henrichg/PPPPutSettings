package sk.henrichg.pppputsettings;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

class FromPhoneProfilesPlusBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint({"LongLogTag", "InlinedApi"})
    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent == null) || (intent.getAction() == null))
            return;

//        PPPEApplication.logE("FromPhoneProfilesPlusBroadcastReceiver.onReceive", "received broadcast action="+intent.getAction());

        String action = intent.getAction();
    }

    static private void showPermissionNotification(Context context, String title, String text,
                                                    int notificationID, String notificationTag) {
        //noinspection UnnecessaryLocalVariable
        String nTitle = title;
        //noinspection UnnecessaryLocalVariable
        String nText = text;
        PPPPSApplication.createGrantPermissionNotificationChannel(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, PPPPSApplication.GRANT_PERMISSION_NOTIFICATION_CHANNEL)
                .setColor(ContextCompat.getColor(context, R.color.notificationDecorationColor))
                .setSmallIcon(R.drawable.ic_exclamation_notify) // notification icon
                .setContentTitle(nTitle) // title for notification
                .setContentText(nText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(nText))
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setCategory(NotificationCompat.CATEGORY_RECOMMENDATION);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setOnlyAlertOnce(true);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        try {
            mNotificationManager.notify(notificationTag, notificationID, mBuilder.build());
        } catch (Exception e) {
            Log.e("FromPhoneProfilesPlusBroadcastReceiver.showPermissionNotification", Log.getStackTraceString(e));
        }
    }

}
