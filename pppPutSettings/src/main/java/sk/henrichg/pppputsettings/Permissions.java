package sk.henrichg.pppputsettings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

class Permissions {

    //static final int PERMISSIONS_REQUEST_CODE = 9091;
    static final int NOTIFICATIONS_PERMISSION_REQUEST_CODE = 9900;

    @SuppressWarnings("UnusedReturnValue")
    static boolean grantNotificationsPermission(final Activity activity) {
        if (Build.VERSION.SDK_INT >= 33) {
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                if (!notificationManager.areNotificationsEnabled()) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                    dialogBuilder.setTitle(R.string.pppputsettings_app_name);
                    dialogBuilder.setMessage(R.string.pppputsettings_notifications_permission_text);
                    //dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    dialogBuilder.setPositiveButton(R.string.pppputsettings_enable_notificaitons_button, (dialog, which) -> {

                        boolean ok = false;

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, PPPPSApplication.PACKAGE_NAME);

                        if (MainActivity.activityIntentExists(intent, activity.getApplicationContext())) {
                            try {
                                //activity.startActivity(intent);
                                activity.startActivityForResult(intent, NOTIFICATIONS_PERMISSION_REQUEST_CODE);
                                ok = true;
                            } catch (Exception e) {
                                PPPPSApplication.recordException(e);
                            }
                        }
                        if (!ok) {
                            AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(activity);
                            dialogBuilder1.setMessage(R.string.pppputsettings_setting_screen_not_found_alert);
                            //dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                            dialogBuilder1.setPositiveButton(android.R.string.ok, null);
                            AlertDialog _dialog = dialogBuilder1.create();

//                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                                    @Override
//                                    public void onShow(DialogInterface dialog) {
//                                        Button positive = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
//                                        if (positive != null) positive.setAllCaps(false);
//                                        Button negative = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
//                                        if (negative != null) negative.setAllCaps(false);
//                                    }
//                                });

                            if (!activity.isFinishing())
                                _dialog.show();
                        }

                    });
                    //dialogBuilder.setNegativeButton(R.string.extender_dont_enable_notificaitons_button, null);

                    AlertDialog dialog = dialogBuilder.create();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);

                    if (!activity.isFinishing())
                        dialog.show();

                    return true;
                }
            }
        }
        return false;
    }

    /*
    static boolean hasPermission(Context context, String permission) {
        return context.checkPermission(permission, PPPPSApplication.pid, PPPPSApplication.uid) == PackageManager.PERMISSION_GRANTED;
    }
    */

    /** @noinspection SameParameterValue*/
    static void writeSettingsNotGranted(Context context, int scrollTo) {
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + "sk.henrichg.pppputsettings"));
            //intent.addCategory(Intent.CATEGORY_DEFAULT);

            intent.putExtra(MainActivity.EXTRA_SCROLL_TO, scrollTo);

            if (MainActivity.activityIntentExists(intent, context)) {
                PPPPSApplication.createExclamationNotificationChannel(context, false);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), PPPPSApplication.EXCLAMATION_NOTIFICATION_CHANNEL)
                        .setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.error_color))
                        .setSmallIcon(R.drawable.ic_pppps_notification/*ic_exclamation_notify*/) // notification icon
                        .setLargeIcon(BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.ic_exclamation_notification))
                        .setContentTitle(context.getString(R.string.pppputsettings_not_granted_write_settings_title)) // title for notification
                        .setContentText(context.getString(R.string.pppputsettings_not_granted_write_settings_text)) // message for notification
                        .setAutoCancel(true); // clear notification after click
                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.pppputsettings_not_granted_write_settings_text)));
                mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                mBuilder.setCategory(NotificationCompat.CATEGORY_RECOMMENDATION);
                mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                //mBuilder.setGroup(PPApplication.ACTION_FOR_EXTERNAL_APPLICATION_NOTIFICATION_GROUP);

                @SuppressLint("UnspecifiedImmutableFlag")
                PendingIntent pi = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mBuilder.setOnlyAlertOnce(true);

                NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
                try {
                    mNotificationManager.notify(
                            PPPPSApplication.NOT_GRANTED_WRITE_SETTINGS_NOTIFICATION_TAG,
                            PPPPSApplication.NOT_GRANTED_WRITE_SETTINGS_NOTIFICATION_ID, mBuilder.build());
                } catch (SecurityException en) {
                    Log.e("Permissions.writeSettingsNotGranted", Log.getStackTraceString(en));
                } catch (Exception e) {
                    //Log.e("Permissions.writeSettingsNotGranted", Log.getStackTraceString(e));
                    PPPPSApplication.recordException(e);
                }

            }
        }
    }

}
