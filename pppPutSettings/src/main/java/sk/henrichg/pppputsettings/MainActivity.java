package sk.henrichg.pppputsettings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.core.view.MenuCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PERMISSIONS_SETTINGS = 1901;

    int selectedLanguage = 0;
    String defaultLanguage = "";
    String defaultCountry = "";
    String defaultScript = "";

    static final String EXTRA_SCROLL_TO = "extra_main_activity_scroll_to";

    int scrollTo = 0;

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
        int miuiVersion = -1;
        if (PPPPSApplication.deviceIsXiaomi && PPPPSApplication.romIsMIUI) {
            String[] splits = Build.VERSION.INCREMENTAL.split("\\.");
            miuiVersion = Integer.parseInt(splits[0].substring(1));
        }

        if (PPPPSApplication.deviceIsOnePlus)
            setTheme(R.style.AppTheme_noRipple);
        else
        if (PPPPSApplication.deviceIsXiaomi && PPPPSApplication.romIsMIUI && miuiVersion >= 14)
            setTheme(R.style.AppTheme_noRipple);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            //getSupportActionBar().setHomeButtonEnabled(false);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.pppputsettings_app_name);
            getSupportActionBar().setElevation(0);
        }

        Intent intent = getIntent();
        scrollTo = intent.getIntExtra(EXTRA_SCROLL_TO, 0);

        TextView text = findViewById(R.id.activity_main_application_version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            //noinspection DataFlowIssue
            text.setText(getString(R.string.pppputsettings_about_application_version) + " " + pInfo.versionName +
                                        " (" + PackageInfoCompat.getLongVersionCode(pInfo) + ")");
        } catch (Exception e) {
            //noinspection DataFlowIssue
            text.setText("");
        }

        text = findViewById(R.id.activity_main_application_releases);
        String str1 = getString(R.string.pppputsettings_application_releases);
        String str2 = str1 + " https://github.com/henrichg/PPPPutSettings/releases" + StringConstants.STR_HARD_SPACE_DOUBLE_ARROW;
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
        //noinspection DataFlowIssue
        text.setText(sbt);
        text.setMovementMethod(LinkMovementMethod.getInstance());

        displayPermmisionsGrantStatus();

        //LocalBroadcastManager.getInstance(this).registerReceiver(refreshGUIBroadcastReceiver,
        //        new IntentFilter(PPPPSApplication.PACKAGE_NAME + ".RefreshGUIBroadcastReceiver"));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (DebugVersion.enabled)
            getMenuInflater().inflate(R.menu.main_menu_debug, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        return true;
    }

    @SuppressLint("AlwaysShowAction")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean ret = super.onPrepareOptionsMenu(menu);

        MenuItem menuItem;

        menuItem = menu.findItem(R.id.menu_support);
        if (menuItem != null)
        {
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null) {
                Drawable triangle = ContextCompat.getDrawable(this, R.drawable.ic_submenu_triangle);
                if (triangle != null) {
                    triangle.setTint(ContextCompat.getColor(this, R.color.activitySecondaryTextColor));
                    SpannableString headerTitle = new SpannableString("    " + menuItem.getTitle());
                    triangle.setBounds(0,
                            sip(1),
                            sip(10.5f),
                            sip(8.5f));
                    headerTitle.setSpan(new ImageSpan(triangle, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //headerTitle.setSpan(new ImageSpan(this, R.drawable.ic_submenu_triangle, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    subMenu.setHeaderTitle(headerTitle);
                }
            }
        }

        if (DebugVersion.enabled) {
            menuItem = menu.findItem(R.id.menu_debug);
            if (menuItem != null) {
                SubMenu subMenu = menuItem.getSubMenu();
                if (subMenu != null) {
                    Drawable triangle = ContextCompat.getDrawable(this, R.drawable.ic_submenu_triangle);
                    if (triangle != null) {
                        triangle.setTint(ContextCompat.getColor(this, R.color.activitySecondaryTextColor));
                        SpannableString headerTitle = new SpannableString("    " + menuItem.getTitle());
                        triangle.setBounds(0,
                                sip(1),
                                sip(10.5f),
                                sip(8.5f));
                        headerTitle.setSpan(new ImageSpan(triangle, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //headerTitle.setSpan(new ImageSpan(this, R.drawable.ic_submenu_triangle, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        subMenu.setHeaderTitle(headerTitle);
                    }
                }
            }
        }
        menuItem = menu.findItem(R.id.menu_discord);
        if (menuItem != null)
        {
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null) {
                Drawable triangle = ContextCompat.getDrawable(this, R.drawable.ic_submenu_triangle);
                if (triangle != null) {
                    triangle.setTint(ContextCompat.getColor(this, R.color.activitySecondaryTextColor));
                    SpannableString headerTitle = new SpannableString("    " + menuItem.getTitle());
                    triangle.setBounds(0,
                            sip(1),
                            sip(10.5f),
                            sip(8.5f));
                    headerTitle.setSpan(new ImageSpan(triangle, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //headerTitle.setSpan(new ImageSpan(this, R.drawable.ic_submenu_triangle, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    subMenu.setHeaderTitle(headerTitle);
                }
            }
        }

        //onNextLayout(editorToolbar, this::showTargetHelps);

        return ret;
    }

    int sip(float sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        int itemId = item.getItemId();
        /*if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        else*/
        if (itemId == R.id.menu_choose_language) {
            ChooseLanguageDialog chooseLanguageDialog = new ChooseLanguageDialog(this);
            chooseLanguageDialog.show();
            return true;
        }
        else
        if (itemId == R.id.menu_email_to_author) {
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(StringConstants.INTENT_DATA_MAIL_TO_COLON)); // only email apps should handle this
            String[] email = {StringConstants.AUTHOR_EMAIL};
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            String packageVersion = "";
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(PPPPSApplication.PACKAGE_NAME, 0);
                packageVersion = " - v" + pInfo.versionName + " (" + PPPPSApplication.getVersionCode(pInfo) + ")";
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            intent.putExtra(Intent.EXTRA_SUBJECT, StringConstants.PHONE_PROFILES_PLUS_PUT_SETTINGS + packageVersion + " - " + getString(R.string.pppputsettings_support_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getEmailBodyText());
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_email_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }

            return true;
        }
        else
        if (itemId == R.id.menu_xda_developers) {
            String url = PPPPSApplication.XDA_DEVELOPERS_PPP_URL;
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_web_browser_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            return true;
        }
        else
        if (itemId == R.id.menu_discord_server) {
            String url = PPPPSApplication.DISCORD_SERVER_URL;
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_web_browser_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            return true;
        }
        else
        if (itemId == R.id.menu_discord_invitation) {
            String url = PPPPSApplication.DISCORD_INVITATION_URL;
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_web_browser_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            return true;
        }
        else
        if (itemId == R.id.menu_twitter) {
            String url = PPPPSApplication.TWITTER_URL;
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_web_browser_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            return true;
        }
        else
        if (itemId == R.id.menu_reddit) {
            String url = PPPPSApplication.REDDIT_URL;
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_web_browser_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            return true;
        }
        else
        if (itemId == R.id.menu_bluesky) {
            String url = PPPPSApplication.BLUESKY_URL;
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.pppputsettings_web_browser_chooser)));
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
            return true;
        }
        else
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

        if (scrollTo != 0) {
            final ScrollView scrollView = findViewById(R.id.activity_main_scroll_view);
            final View viewToScroll = findViewById(scrollTo);
            if (viewToScroll != null) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                        PPApplication.logE("[IN_THREAD_HANDLER] PPApplication.startHandlerThread", "START run - from=ImportantInfoHelpFragment.onViewCreated (2)");
                    //noinspection DataFlowIssue
                    scrollView.scrollTo(0, viewToScroll.getTop());
                }, 200);

                scrollTo = 0;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PERMISSIONS_SETTINGS)
            reloadActivity(this, false);
    }

    /*
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
        if (Build.VERSION.SDK_INT >= 23) {
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
            //noinspection DataFlowIssue
            text.setText(str1);

            Button writeSettingsButton = findViewById(R.id.activity_main_write_settings_button);
            //noinspection DataFlowIssue
            writeSettingsButton.setOnClickListener(view -> {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse(PPPPSApplication.INTENT_DATA_PACKAGE + "sk.henrichg.pppputsettings"));
                //intent.addCategory(Intent.CATEGORY_DEFAULT);
                if (MainActivity.activityIntentExists(intent, activity)) {
                    //noinspection deprecation
                    startActivityForResult(intent, RESULT_PERMISSIONS_SETTINGS);
                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                    dialogBuilder.setMessage(R.string.pppputsettings_setting_screen_not_found_alert);
                    //dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    dialogBuilder.setPositiveButton(android.R.string.ok, null);
                    dialogBuilder.show();
                }
            });

            text = findViewById(R.id.activity_main_write_settings_status);
            if (Settings.System.canWrite(getApplicationContext())) {
                //noinspection DataFlowIssue
                text.setTextColor(ContextCompat.getColor(this, R.color.activityNormalTextColor));
                text.setText("[ " + getString(R.string.pppputsettings_modify_system_settings_granted) + " ]");
            }
            else {
                if (scrollTo == R.id.activity_main_write_settings_status)
                    //noinspection DataFlowIssue
                    text.setTextColor(ContextCompat.getColor(this, R.color.error_color));
                else
                    //noinspection DataFlowIssue
                    text.setTextColor(ContextCompat.getColor(this, R.color.activityNormalTextColor));
                text.setText("[ " + getString(R.string.pppputsettings_modify_system_settings_not_granted) + " ]");
            }

        }
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
            return !activities.isEmpty();
        } catch (Exception e) {
            //Log.e("MainActivity.activityIntentExists", Log.getStackTraceString(e));
            //PPPEApplication.recordException(e);
            return false;
        }
    }

    static void reloadActivity(Activity activity,
                                       @SuppressWarnings("SameParameterValue") boolean newIntent)
    {
        if (newIntent)
        {
            final Activity _activity = activity;
            new Handler(activity.getMainLooper()).post(() -> {
                try {
                    @SuppressLint("UnsafeIntentLaunch")
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

    /*
    private static class Language {
        String language;
        String country;
        String script;
        String name;
    }

    private static class LanguagesComparator implements Comparator<Language> {

        public int compare(Language lhs, Language rhs) {
            return PPPPSApplication.collator.compare(lhs.name, rhs.name);
        }
    }
    */

    String getEmailBodyText() {
        String body;
        body = getString(R.string.pppputsettings_acra_email_body_device) + " " +
                Settings.Global.getString(getContentResolver(), "device_name") +
                " (" + Build.MODEL + ")" + StringConstants.STR_NEWLINE_WITH_SPACE;
        body = body + getString(R.string.pppputsettings_acra_email_body_android_version) + " " + Build.VERSION.RELEASE + StringConstants.STR_DOUBLE_NEWLINE_WITH_SPACE;
        return body;
    }

}
