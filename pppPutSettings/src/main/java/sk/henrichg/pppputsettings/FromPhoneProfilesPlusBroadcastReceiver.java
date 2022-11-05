package sk.henrichg.pppputsettings;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

class FromPhoneProfilesPlusBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint({"LongLogTag", "InlinedApi"})
    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent == null) || (intent.getAction() == null))
            return;

//        PPPEApplication.logE("FromPhoneProfilesPlusBroadcastReceiver.onReceive", "received broadcast action="+intent.getAction());

        String action = intent.getAction();
        if (action.equals(PPPPSApplication.ACTION_PUT_SETTING_PARAMETER)) {
            //if (!intent.getBooleanExtra(PPPEApplication.EXTRA_BLOCK_PROFILE_EVENT_ACTION, false)) {
                //if (PPPEApplication.registeredLockDeviceFunctionPP ||
                //        PPPEApplication.registeredLockDeviceFunctionPPP) {
//                        PPPEApplication.logE("FromPhoneProfilesPlusBroadcastReceiver.onReceive", "put settings parameter");

                        String settingsType = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_TYPE);
                        String parameterName = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_NAME);
                        String value = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_VALUE);

                        ContentResolver contentResolver = context.getContentResolver();
                        try {
                            ContentValues contentValues = new ContentValues(2);
                            contentValues.put("name", parameterName);
                            contentValues.put("value", value);
                            // settingsType : "system", "secure", "global"
                            contentResolver.insert(Uri.parse("content://settings/" + settingsType), contentValues);
                        } catch (Exception e) {
                            Log.e("FromPhoneProfilesPlusBroadcastReceiver.onReceive", Log.getStackTraceString(e));
                        }

//                }
            //}
        }

    }

    /*
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
    */

}
