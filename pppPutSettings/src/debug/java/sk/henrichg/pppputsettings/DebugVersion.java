package sk.henrichg.pppputsettings;

import android.app.Activity;
import android.content.Intent;

class DebugVersion {
    static final boolean enabled = true;

    static boolean debugMenuItems(int menuItem, Activity activity) {

        if (menuItem == R.id.menu_show_log_file) {
            Intent intentLaunch = new Intent(activity.getApplicationContext(), LogCrashActivity.class);
            activity.startActivity(intentLaunch);

            return true;
        }
        else
            return false;
    }

}
