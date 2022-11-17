package sk.henrichg.pppputsettings;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

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

}
