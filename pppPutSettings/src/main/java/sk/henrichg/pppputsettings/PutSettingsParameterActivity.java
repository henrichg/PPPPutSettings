package sk.henrichg.pppputsettings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PutSettingsParameterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

//        PPPPSApplication.logE("PutSettingsParameterActivity.onCreate", "xxx");

        Intent intent = getIntent();
        //startupSource = intent.getIntExtra(PPApplication.EXTRA_STARTUP_SOURCE, PPApplication.STARTUP_SOURCE_SHORTCUT);
        //profile_id = intent.getLongExtra(PPApplication.EXTRA_PROFILE_ID, 0);
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

    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
