package sk.henrichg.pppputsettings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.pm.PackageInfoCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private static final int RESULT_PERMISSIONS_SETTINGS = 1901;

    /*
    private final BroadcastReceiver refreshGUIBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive( Context context, Intent intent ) {
            //PPPEApplication.logE("MainActivity.refreshGUIBroadcastReceiver", "xxx");
        }
    };
    */

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_main);

        //PPPEApplication.logE("MainActivity.onCreated", "xxx");

        if (getSupportActionBar() != null) {
            //getSupportActionBar().setHomeButtonEnabled(false);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.pppputsettings_app_name);
            getSupportActionBar().setElevation(0);
        }

        TextView text = findViewById(R.id.activity_main_application_version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            text.setText(getString(R.string.pppputsettings_about_application_version) + " " + pInfo.versionName +
                                        " (" + PackageInfoCompat.getLongVersionCode(pInfo) + ")");
        } catch (Exception e) {
            text.setText("");
        }

        text = findViewById(R.id.activity_main_application_releases);
        String str1 = getString(R.string.pppputsettings_application_releases);
        String str2 = str1 + " https://github.com/henrichg/PPPPutSettings/releases" + " \u21D2";
        Spannable sbt = new SpannableString(str2);
        sbt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(@NonNull View textView) {
                String url = "https://github.com/henrichg/PPPPutSettings/releases";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.pppputsettings_web_browser_chooser)));
                } catch (Exception ignored) {}
            }
        };
        sbt.setSpan(clickableSpan, str1.length()+1, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //sbt.setSpan(new UnderlineSpan(), str1.length()+1, str2.length(), 0);
        text.setText(sbt);
        text.setMovementMethod(LinkMovementMethod.getInstance());

        displayPermmisionsGrantStatus();

        //LocalBroadcastManager.getInstance(this).registerReceiver(refreshGUIBroadcastReceiver,
        //        new IntentFilter(PPPPSApplication.PACKAGE_NAME + ".RefreshGUIBroadcastReceiver"));

    }

    /*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    */

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        if (Build.VERSION.SDK_INT >= 28) {
            menu.setGroupDividerEnabled(true);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean ret = super.onPrepareOptionsMenu(menu);

        MenuItem menuItem;

        menuItem = menu.findItem(R.id.menu_debug);
        if (menuItem != null) {
            menuItem.setVisible(DebugVersion.enabled);
            menuItem.setEnabled(DebugVersion.enabled);
        }

        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        /*if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        else*/
        if (DebugVersion.debugMenuItems(itemId, this))
            return true;
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Permissions.grantNotificationsPermission(this);
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PERMISSIONS_SETTINGS)
            reloadActivity(this, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (requestCode == Permissions.PERMISSIONS_REQUEST_CODE) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                //if (!onlyNotification) {
                //PPPPSApplication.showToast(getApplicationContext(),
                //        getString(R.string.extender_app_name) + ": " +
                //                getString(R.string.extender_toast_permissions_not_granted),
                //        Toast.LENGTH_SHORT);
                //}
            }
            reloadActivity(this, false);

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    */

    @SuppressLint({"SetTextI18n", "BatteryLife"})
    private void displayPermmisionsGrantStatus() {
        final Activity activity = this;

        TextView text;
        String str1;

        text = findViewById(R.id.activity_main_permission_write_settings);
        str1 = getString(R.string.pppputsettings_permissions_write_settings);
        //if (Permissions.checkSMSMMSPermissions(activity))
        //    str2 = str1 + " [ " + getString(R.string.extender_permissions_granted) + " ]";
        //else
        //    str2 = str1 + " [ " + getString(R.string.extender_permissions_not_granted) + " ]";
        //sbt = new SpannableString(str2);
        //sbt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), str1.length() + 1, str2.length(), 0);
        //text.setText(sbt);
        text.setText(str1);

        Button writeSettingsButton = findViewById(R.id.activity_main_write_settings_button);
        writeSettingsButton.setOnClickListener(view -> {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + "sk.henrichg.pppputsettings"));
            //intent.addCategory(Intent.CATEGORY_DEFAULT);
            if (MainActivity.activityIntentExists(intent, activity)) {
                //noinspection deprecation
                startActivity(intent);
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                dialogBuilder.setMessage(R.string.pppputsettings_setting_screen_not_found_alert);
                //dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                dialogBuilder.setPositiveButton(android.R.string.ok, null);
                dialogBuilder.show();
            }
        });

    }

    /*
    private static boolean activityActionExists(@SuppressWarnings("SameParameterValue") String action,
                                                Context context) {
        try {
            final Intent intent = new Intent(action);
            List<ResolveInfo> activities = context.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
            return activities.size() > 0;
        } catch (Exception e) {
            //Log.e("MainActivity.activityActionExists", Log.getStackTraceString(e));
            //PPPEApplication.recordException(e);
            return false;
        }
    }
    */

    static boolean activityIntentExists(Intent intent, Context context) {
        try {
            List<ResolveInfo> activities = context.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
            return activities.size() > 0;
        } catch (Exception e) {
            //Log.e("MainActivity.activityIntentExists", Log.getStackTraceString(e));
            //PPPEApplication.recordException(e);
            return false;
        }
    }

    /*
    private static void reloadActivity(Activity activity,
                                       @SuppressWarnings("SameParameterValue") boolean newIntent)
    {
        if (newIntent)
        {
            final Activity _activity = activity;
            new Handler(activity.getMainLooper()).post(() -> {
                try {
                    Intent intent = _activity.getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    _activity.finish();
                    _activity.overridePendingTransition(0, 0);

                    _activity.startActivity(intent);
                    _activity.overridePendingTransition(0, 0);
                } catch (Exception ignored) {}
            });
        }
        else
            activity.recreate();
    }
    */

}
