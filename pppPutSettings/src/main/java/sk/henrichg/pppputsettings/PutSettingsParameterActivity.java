package sk.henrichg.pppputsettings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PutSettingsParameterActivity extends AppCompatActivity {

    String settingsType;
    String parameterName;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

//        PPPPSApplication.logE("PutSettingsParameterActivity.onCreate", "xxx");

        Intent intent = getIntent();
        settingsType = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_TYPE);
        parameterName = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_NAME);
        value = intent.getStringExtra(PPPPSApplication.EXTRA_PUT_SETTING_PARAMETER_VALUE);
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

        if (!Permissions.grantNotificationsPermission(this)) {
            putSettingsParameter();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Permissions.NOTIFICATIONS_PERMISSION_REQUEST_CODE) {
            putSettingsParameter();
            //if (!isFinishing())
            //    finish();
        }
    }

    private void putSettingsParameter() {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        try {
            ContentValues contentValues = new ContentValues(2);
            contentValues.put("name", parameterName);
            contentValues.put("value", value);
            // settingsType : "system", "secure", "global"
            contentResolver.insert(Uri.parse("content://settings/" + settingsType), contentValues);
        } catch (Exception e) {
            Log.e("FromPhoneProfilesPlusBroadcastReceiver.onReceive", Log.getStackTraceString(e));
        }

    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
