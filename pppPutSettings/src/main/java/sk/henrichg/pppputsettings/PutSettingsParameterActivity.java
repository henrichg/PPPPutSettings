package sk.henrichg.pppputsettings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PutSettingsParameterActivity extends AppCompatActivity {

    String settingsType;
    String parameterName;
    String parameterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

//        PPPPSApplication.logE("PutSettingsParameterActivity.onCreate", "xxx");

        Intent intent = getIntent();
        settingsType = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_TYPE);
        parameterName = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_NAME);
        parameterValue = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_VALUE);
    }

    /*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    */

    @Override
    protected void onStart()
    {
        super.onStart();

        putSettingsParameter();
        if (!isFinishing())
            finish();
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Permissions.NOTIFICATIONS_PERMISSION_REQUEST_CODE) {
            putSettingsParameter();
            if (!isFinishing())
                finish();
        }
    }
    */

    private void putSettingsParameter() {
//        Log.e("PutSettingsParameterActivity.putSettingsParameter", "settingsType="+settingsType);
//        Log.e("PutSettingsParameterActivity.putSettingsParameter", "parameterName="+parameterName);
//        Log.e("PutSettingsParameterActivity.putSettingsParameter", "parameterValue="+parameterValue);
//
        boolean canWrite = true;
        if (Build.VERSION.SDK_INT >= 23)
            canWrite = Settings.System.canWrite(getApplicationContext());

        if (canWrite) {
            if ((settingsType != null) && (parameterName != null) && (parameterValue != null)) {
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                try {
                    ContentValues contentValues = new ContentValues(2);
                    contentValues.put("name", parameterName);
                    contentValues.put("value", parameterValue);
                    // settingsType : "system", "secure", "global"
                    contentResolver.insert(Uri.parse("content://settings/" + settingsType), contentValues);
                } catch (SecurityException e1) {
                    Log.e("PutSettingsParameterActivity.putSettingsParameter", "not granted WRITE_SETTINGS ??");
                    Permissions.writeSettingsNotGranted(getApplicationContext(), R.id.activity_main_permission_write_settings);
                } catch (Exception e2) {
                    //PPPPSApplication.recordException(e2);
                }
            }
        } else {
            Log.e("PutSettingsParameterActivity.putSettingsParameter", "not granted WRITE_SETTINGS");
            Permissions.writeSettingsNotGranted(getApplicationContext(), R.id.activity_main_permission_write_settings);
        }

    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
