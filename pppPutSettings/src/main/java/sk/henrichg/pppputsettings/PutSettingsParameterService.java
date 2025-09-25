package sk.henrichg.pppputsettings;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

public class PutSettingsParameterService extends Service {

//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

//        Log.e("PutSettingsParameterService.onStartCommand", "xxxxxxxxxx");

        String settingsType = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_TYPE);
        String parameterName = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_NAME);
        String parameterValue = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_VALUE);

//        Log.e("PutSettingsParameterService.onStartCommand", "settingsType="+settingsType);
//        Log.e("PutSettingsParameterService.onStartCommand", "parameterName="+parameterName);
//        Log.e("PutSettingsParameterService.onStartCommand", "parameterValue="+parameterValue);

        boolean canWrite = true;
        if (Build.VERSION.SDK_INT >= 23)
            canWrite = Settings.System.canWrite(getApplicationContext());

        if (canWrite) {
            if ((settingsType != null) && (parameterName != null) && (parameterValue != null)) {
//                if (settingsType.equals(SETTINGS_TYPE_SPECIAL)) {
//                    try {
//                        if (parameterName.equals(PARAMETER_SET_WIFI_ENABLED)) {
//                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                            if (wifiManager != null) {
//                                if (parameterValue.equals("0"))
//                                    wifiManager.setWifiEnabled(false);
//                                if (parameterValue.equals("1"))
//                                    wifiManager.setWifiEnabled(true);
//                            }
//                        }
//                    } catch (SecurityException e1) {
//                        if (parameterName.equals(PARAMETER_SET_WIFI_ENABLED)) {
////                            Log.e("PutSettingsParameterActivity.putSettingsParameter", "not granted WRITE_SETTINGS ??");
//                            Permissions.writeSettingsNotGranted(getApplicationContext(), R.id.activity_main_permission_write_settings);
//                        }
//                    } catch (Exception e2) {
//                        //PPPPSApplication.recordException(e2);
//                    }
//                } else {

//                Log.e("PutSettingsParameterService.onStartCommand", "change setting");

                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                try {
                    ContentValues contentValues = new ContentValues(2);
                    contentValues.put("name", parameterName);
                    contentValues.put("value", parameterValue);
                    // settingsType : "system", "secure", "global"
                    contentResolver.insert(Uri.parse("content://settings/" + settingsType), contentValues);
                } catch (SecurityException e1) {
                    //Log.e("PutSettingsParameterActivity.putSettingsParameter", "not granted WRITE_SETTINGS ??");
                    Permissions.writeSettingsNotGranted(getApplicationContext(), R.id.activity_main_permission_write_settings);
                } catch (Exception e2) {
                    //PPPPSApplication.recordException(e2);
                }
//                }
            }
        } else {
//            Log.e("PutSettingsParameterActivity.putSettingsParameter", "not granted WRITE_SETTINGS");
            Permissions.writeSettingsNotGranted(getApplicationContext(), R.id.activity_main_permission_write_settings);
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}