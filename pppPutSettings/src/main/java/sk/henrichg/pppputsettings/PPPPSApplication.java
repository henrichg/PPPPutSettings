package sk.henrichg.pppputsettings;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.config.NotificationConfigurationBuilder;
import org.acra.data.StringFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** @noinspection ExtractMethodRecommender*/
public class PPPPSApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static volatile PPPPSApplication instance;

    static final String PACKAGE_NAME = "sk.henrichg.pppputsettings";

    static final String APPLICATION_PREFS_NAME = "ppp_put_settings_preferences";

    static final String CROWDIN_URL = "https://crowdin.com/project/phoneprofilesplus";

    //static final int pid = Process.myPid();
    //static final int uid = Process.myUid();

    // TODO: DISABLE IT FOR RELEASE VERSION!!!
    private static final boolean logIntoLogCat = false /*&& BuildConfig.DEBUG*/;
    // TODO: DISABLE IT FOR RELEASE VERSION!!!
    static final boolean logIntoFile = false;
    /** @noinspection PointlessBooleanExpression*/
    static final boolean crashIntoFile = true && BuildConfig.DEBUG;
    private static final String logFilterTags = ""
//                                                  "MainActivity"
//                                                + "|PutSettingsParameterActivity"
//                                                + "|PutSettingReceiver"
            ;

//    static final boolean deviceIsXiaomi = isXiaomi();
//    static final boolean deviceIsOnePlus = isOnePlus();
//    static final boolean romIsMIUI = isMIUIROM();

    // for new log.txt and crash.txt is in /Android/data/sk.henrichg.phoneprofilesplusextender/files
    //public static final String EXPORT_PATH = "/PhoneProfilesPlusExtender";
    static final String LOG_FILENAME = "log.txt";

    //static final String GRANT_PERMISSION_NOTIFICATION_CHANNEL = "pppPutSettings_grant_permission";

    static final String EXTRA_PUT_SETTING_PARAMETER_TYPE = "extra_put_setting_parameter_type";
    static final String EXTRA_PUT_SETTING_PARAMETER_NAME = "extra_put_setting_parameter_name";
    static final String EXTRA_PUT_SETTING_PARAMETER_VALUE = "extra_put_setting_parameter_value";

    static volatile ExecutorService basicExecutorPool = null;

    static volatile Collator collator = null;

    //static final int pid = Process.myPid();
    //static final int uid = Process.myUid();

    static final String EXCLAMATION_NOTIFICATION_CHANNEL = "pppPutSettings_exclamation";
    static final int NOT_GRANTED_WRITE_SETTINGS_NOTIFICATION_ID = 111;
    static final String NOT_GRANTED_WRITE_SETTINGS_NOTIFICATION_TAG = PACKAGE_NAME+"_NOT_GRANTED_WRITE_SETTINGS_NOTIFICATION";

    static final String INTENT_DATA_PACKAGE = "package:";
    //static final String EXTRA_PKG_NAME = "extra_pkgname";

    static final String XDA_DEVELOPERS_PPP_URL = "https://forum.xda-developers.com/t/phoneprofilesplus.3799429/";
    //static final String TWITTER_URL = "https://x.com/henrichg";
    static final String REDDIT_URL = "https://www.reddit.com/user/henrichg/";
    static final String BLUESKY_URL = "https://bsky.app/profile/henrichg.bsky.social";
    static final String DISCORD_SERVER_URL = "https://discord.com/channels/1258733423426670633/1258733424504737936";
    static final String DISCORD_INVITATION_URL = "https://discord.gg/Yb5hgAstQ3";
    static final String MASTODON_URL = "https://mastodon.social/@henrichg";

    @Override
    public void onCreate() {
        super.onCreate();

        // This is required : https://www.acra.ch/docs/Troubleshooting-Guide#applicationoncreate
        if (ACRA.isACRASenderServiceProcess()) {
            Log.e("################# PPPPSApplication.onCreate", "ACRA.isACRASenderServiceProcess()");
            return;
        }

/*        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.KEY_VALUE_LIST);
        //builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class)
        //        .setResText(R.string.acra_toast_text)
        //        .setEnabled(true);
        builder.getPluginConfigurationBuilder(NotificationConfigurationBuilder.class)
                .setResChannelName(R.string.extender_notification_channel_crash_report)
                .setResChannelImportance(NotificationManager.IMPORTANCE_DEFAULT)
                .setResIcon(R.drawable.ic_exclamation_notify)
                .setResTitle(R.string.extender_acra_notification_title)
                .setResText(R.string.extender_acra_notification_text)
                .setResSendButtonIcon(0)
                .setResDiscardButtonIcon(0)
                .setSendOnClick(true)
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setMailTo("henrich.gron@gmail.com")
                .setResSubject(R.string.extender_acra_email_subject_text)
                .setResBody(R.string.extender_acra_email_body_text)
                .setReportAsFile(true)
                .setReportFileName("crash_report.txt")
                .setEnabled(true);

        ACRA.init(this, builder);

        // don't schedule anything in crash reporter process
        if (ACRA.isACRASenderServiceProcess())
            return;
*/

        instance = this;

        if (checkAppReplacingState())
            return;

        //Log.e("##### PPPPSApplication.onCreate", "Start  uid="+uid);

        //PPPPSApplication.createGrantPermissionNotificationChannel(this, true);
        PPPPSApplication.createExclamationNotificationChannel(this, true);

        Log.e("##### PPPPSApplication.onCreate", "after cerate notification channel");

        ////////////////////////////////////////////////////////////////////////////////////
        // Bypass Android's hidden API restrictions
        // !!! WARNING - this is required also for android.jar from android-hidden-api !!!
        // https://github.com/tiann/FreeReflection
        /*if (Build.VERSION.SDK_INT >= 28) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});

                if (getRuntime != null) {
                    Object vmRuntime = getRuntime.invoke(null);
                    if (setHiddenApiExemptions != null)
                        setHiddenApiExemptions.invoke(vmRuntime, new Object[]{new String[]{"L"}});
                }
            } catch (Exception e) {
                //Log.e("PPPPSApplication.onCreate", Log.getStackTraceString(e));
                PPPEApplication.recordException(e);
            }
        }*/
        //////////////////////////////////////////

        // Fix for FC: java.lang.IllegalArgumentException: register too many Broadcast Receivers
        //LoadedApkHuaWei.hookHuaWeiVerifier(this);

        /*
        // set up ANR-WatchDog
        ANRWatchDog anrWatchDog = new ANRWatchDog();
        //anrWatchDog.setReportMainThreadOnly();
        anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError error) {
                Crashlytics.getInstance().core.logException(error);
            }
        });
        anrWatchDog.start();
        */

        try {
            PPPPSApplication.setCustomKey("DEBUG", BuildConfig.DEBUG);
        } catch (Exception ignored) {}

    }

    // workaround for: java.lang.NullPointerException: Attempt to invoke virtual method
    // 'android.content.res.AssetManager android.content.res.Resources.getAssets()' on a null object reference
    // https://issuetracker.google.com/issues/36972466
    @SuppressLint("LongLogTag")
    private boolean checkAppReplacingState() {
        if (getResources() == null) {
            Log.w("PPPEApplication.onCreate", "app is replacing...kill");
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        //super.attachBaseContext(base);
        super.attachBaseContext(LocaleHelper.onAttach(base));
        //Reflection.unseal(base);

        collator = getCollator();

        // This is required : https://www.acra.ch/docs/Troubleshooting-Guide#applicationoncreate
        if (ACRA.isACRASenderServiceProcess()) {
            Log.e("################# PPPPSApplication.attachBaseContext", "ACRA.isACRASenderServiceProcess()");
            return;
        }

        String packageVersion = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(PPPPSApplication.PACKAGE_NAME, 0);
            packageVersion = " - v" + pInfo.versionName + " (" + PPPPSApplication.getVersionCode(pInfo) + ")";
        } catch (Exception ignored) {
        }

        String body;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            body = getString(R.string.pppputsettings_acra_email_body_device) + " " +
                    Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME) +
                    " (" + Build.MODEL + ")" + StringConstants.STR_NEWLINE_WITH_SPACE;
        else {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer))
                body = getString(R.string.pppputsettings_acra_email_body_device) + " " + model + StringConstants.STR_NEWLINE_WITH_SPACE;
            else
                body = getString(R.string.pppputsettings_acra_email_body_device) + " " + manufacturer + " " + model + StringConstants.STR_NEWLINE_WITH_SPACE;
        }
        body = body + getString(R.string.pppputsettings_acra_email_body_android_version) + " " + Build.VERSION.RELEASE + StringConstants.STR_DOUBLE_NEWLINE_WITH_SPACE;
        body = body + getString(R.string.pppputsettings_acra_email_body_text);

        Log.e("##### PPPPSApplication.attachBaseContext", "ACRA inittialization");

        ReportField[] reportContent = new ReportField[] {
                ReportField.REPORT_ID,
                ReportField.ANDROID_VERSION,
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.PHONE_MODEL,
                ReportField.PRODUCT,
                //ReportField.APPLICATION_LOG,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.BRAND,
                ReportField.BUILD,
                //BUILD_CONFIG !!! must be removed because in it is also encrypt_contacts_key, encrypt_contacts_salt
                ReportField.CRASH_CONFIGURATION,
                ReportField.TOTAL_MEM_SIZE,
                ReportField.USER_APP_START_DATE,
                ReportField.USER_CRASH_DATE,

                ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT,

                ReportField.SHARED_PREFERENCES,

                ReportField.DEVICE_FEATURES,
                //ReportField.DEVICE_ID
                ReportField.DISPLAY,
                //DROPBOX
                //ReportField.DUMPSYS_MEMINFO,
                ReportField.ENVIRONMENT,
                //ReportField.FILE_PATH,
                ReportField.INITIAL_CONFIGURATION,
                //ReportField.INSTALLATION_ID,
                //ReportField.IS_SILENT,
                //ReportField.MEDIA_CODEC_LIST,
                //ReportField.PACKAGE_NAME,
                //ReportField.RADIOLOG,
                ReportField.SETTINGS_GLOBAL,
                ReportField.SETTINGS_SECURE,
                ReportField.SETTINGS_SYSTEM,
                //STACK_TRACE_HASH
                //ReportField.THREAD_DETAILS,
                //ReportField.USER_COMMENT,
                //ReportField.USER_EMAIL,
                //ReportField.USER_IP,
                ReportField.EVENTSLOG
        };

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder()
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.KEY_VALUE_LIST)
                .withReportContent(reportContent)
                ;

        builder.withPluginConfigurations(
                new NotificationConfigurationBuilder()
                        .withChannelName(getString(R.string.pppputsettings_notification_channel_crash_report))
                        //.withChannelImportance(NotificationManager.IMPORTANCE_HIGH)
                        .withResIcon(R.drawable.ic_pppps_notification)
                        .withTitle(/*"!!! " +*/ getString(R.string.pppputsettings_acra_notification_title))
                        .withText(getString(R.string.pppputsettings_acra_notification_text))
                        .withResSendButtonIcon(0)
                        .withResDiscardButtonIcon(0)
                        .withSendOnClick(true)
                        .withColor(ContextCompat.getColor(base, R.color.errorColor))
                        .withChannelId(EXCLAMATION_NOTIFICATION_CHANNEL)
                        .withEnabled(true)
                        .build(),
                new MailSenderConfigurationBuilder()
                        .withMailTo(StringConstants.AUTHOR_EMAIL)
                        .withSubject(StringConstants.PHONE_PROFILES_PLUS_PUT_SETTINGS + packageVersion + " - " + getString(R.string.pppputsettings_acra_email_subject_text))
                        .withBody(body)
                        .withReportAsFile(true)
                        .withReportFileName("crash_report.txt")
                        .withEnabled(false) // must be false because of custom report sender
                        .build()
        );

        ACRA.DEV_LOGGING = false;

        ACRA.init(this, builder);

        /*
        //if (BuildConfig.DEBUG) {
        long actualVersionCode = 0;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            //actualVersionCode = pInfo.versionCode;
            actualVersionCode = PackageInfoCompat.getLongVersionCode(pInfo);
        } catch (Exception ignored) {}

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(base, actualVersionCode));
        //}
        */
    }

    //--------------------------------------------------------------

    /*
    private static boolean isXiaomi() {
        final String XIOMI = "xiaomi";
        return Build.BRAND.equalsIgnoreCase(XIOMI) ||
                Build.MANUFACTURER.equalsIgnoreCase(XIOMI) ||
                Build.FINGERPRINT.toLowerCase().contains(XIOMI);
    }

    private static boolean isOnePlus() {
        final String ONEPLUS = "oneplus";
        return Build.BRAND.equalsIgnoreCase(ONEPLUS) ||
                Build.MANUFACTURER.equalsIgnoreCase(ONEPLUS) ||
                Build.FINGERPRINT.toLowerCase().contains(ONEPLUS);
    }

    private static boolean isMIUIROM() {
        boolean miuiRom1 = false;
        boolean miuiRom2 = false;
        boolean miuiRom3 = false;

        String line;
        BufferedReader input;
        try {
            java.lang.Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.code");
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            miuiRom1 = !line.isEmpty();
            input.close();

            if (!miuiRom1) {
                p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");
                input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
                line = input.readLine();
                miuiRom2 = !line.isEmpty();
                input.close();
            }

            if (!miuiRom1 && !miuiRom2) {
                p = Runtime.getRuntime().exec("getprop ro.miui.internal.storage");
                input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
                line = input.readLine();
                miuiRom3 = !line.isEmpty();
                input.close();
            }

        } catch (Exception ex) {
            //Log.e("PPPPSApplication.isMIUIROM", Log.getStackTraceString(ex));
            PPPPSApplication.recordException(ex);
        }

        return miuiRom1 || miuiRom2 || miuiRom3;
    }
    */

    static void createBasicExecutorPool() {
        if (PPPPSApplication.basicExecutorPool == null)
            PPPPSApplication.basicExecutorPool = Executors.newCachedThreadPool();
    }

    //--------------------------------------------------------------

    static private void resetLog()
    {
        /*File sd = Environment.getExternalStorageDirectory();
        File exportDir = new File(sd, EXPORT_PATH);
        if (!(exportDir.exists() && exportDir.isDirectory()))
            //noinspection ResultOfMethodCallIgnored
            exportDir.mkdirs();*/

        File path = instance.getApplicationContext().getExternalFilesDir(null);
        File logFile = new File(path, LOG_FILENAME);
        //noinspection ResultOfMethodCallIgnored
        logFile.delete();
    }

    /** @noinspection SameParameterValue*/
    static private void logIntoFile(String type, String tag, String text)
    {
        if (!logIntoFile)
            return;

        if (instance == null)
            return;

        try
        {
            File path = instance.getApplicationContext().getExternalFilesDir(null);

            /*// warnings when logIntoFile == false
            File sd = Environment.getExternalStorageDirectory();
            File exportDir = new File(sd, EXPORT_PATH);
            if (!(exportDir.exists() && exportDir.isDirectory()))
                //noinspection ResultOfMethodCallIgnored
                exportDir.mkdirs();

            File logFile = new File(sd, EXPORT_PATH + "/" + LOG_FILENAME);*/

            File logFile = new File(path, LOG_FILENAME);

            if (logFile.length() > 1024 * 100000)
                resetLog();

            if (!logFile.exists())
            {
                //noinspection ResultOfMethodCallIgnored
                logFile.createNewFile();
            }

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            String log = "";
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yy HH:mm:ss:S");
            String time = sdf.format(Calendar.getInstance().getTimeInMillis());
            log = log + time + " [ " + type + " ] [ " + tag + " ]: " + text;
            buf.append(log);
            buf.newLine();
            buf.flush();
            buf.close();
        }
        catch (IOException ignored) {
            //Log.e("PPPPSApplication.logIntoFile", Log.getStackTraceString(e));
        }
    }

    private static boolean logContainsFilterTag(String tag)
    {
        boolean contains = false;
        String[] filterTags = logFilterTags.split(StringConstants.STR_SPLIT_REGEX);
        for (String filterTag : filterTags) {
            if (!filterTag.contains("!")) {
                if (tag.contains(filterTag)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /** @noinspection ConstantValue*/
    static private boolean logEnabled() {
        return (logIntoLogCat || logIntoFile);
    }

    /*
    static public void logI(String tag, String text)
    {
        if (!logEnabled())
            return;

        if (logContainsFilterTag(tag))
        {
            //if (logIntoLogCat) Log.i(tag, text);
            if (logIntoLogCat) Log.i(tag, "[ "+tag+" ]" + StringConstants.STR_COLON_WITH_SPACE + text);
            logIntoFile("I", tag, text);
        }
    }
    */

    /*
    static public void logW(String tag, String text)
    {
        if (!logEnabled())
            return;

        if (logContainsFilterTag(tag))
        {
            //if (logIntoLogCat) Log.w(tag, text);
            if (logIntoLogCat) Log.w(tag, "[ "+tag+" ]" + StringConstants.STR_COLON_WITH_SPACE + text);
            logIntoFile("W", tag, text);
        }
    }
    */

    @SuppressWarnings("unused")
    static public void logE(String tag, String text)
    {
        if (!logEnabled())
            return;

        if (logContainsFilterTag(tag))
        {
            //if (logIntoLogCat) Log.e(tag, text);
            if (logIntoLogCat) Log.e(tag, "[ "+tag+" ]" + StringConstants.STR_COLON_WITH_SPACE + text);
            logIntoFile("E", tag, text);
        }
    }

    /*
    static public void logD(String tag, String text)
    {
        if (!logEnabled())
            return;

        if (logContainsFilterTag(tag))
        {
            //if (logIntoLogCat) Log.d(tag, text);
            if (logIntoLogCat) Log.d(tag, "[ "+tag+" ]" + StringConstants.STR_COLON_WITH_SPACE + text);
            logIntoFile("D", tag, text);
        }
    }
    */

    // ACRA -------------------------------------------------------------------------

    static void recordException(Throwable ex) {
        try {
            //FirebaseCrashlytics.getInstance().recordException(ex);
            ACRA.getErrorReporter().handleException(ex);
        } catch (Exception ignored) {}
    }

    /*
    static void logToACRA(String s) {
        try {
            //FirebaseCrashlytics.getInstance().log(s);
            ACRA.getErrorReporter().putCustomData("Log", s);
        } catch (Exception ignored) {}
    }
    */

    /*
    static void setCustomKey(String key, int value) {
        try {
            //FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            ACRA.getErrorReporter().putCustomData(key, String.valueOf(value));
        } catch (Exception ignored) {}
    }
    */

    /*
    static void setCustomKey(String key, String value) {
        try {
            //FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            ACRA.getErrorReporter().putCustomData(key, value);
        } catch (Exception ignored) {}
    }
    */

    @SuppressWarnings("SameParameterValue")
    static void setCustomKey(String key, boolean value) {
        try {
            //FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            ACRA.getErrorReporter().putCustomData(key, String.valueOf(value));
        } catch (Exception ignored) {}
    }

    // Google Analytics ----------------------------------------------------------------------------

    /*
    static void logAnalyticsEvent(String itemId, String itemName, String contentType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    */

    //---------------------------------------------------------------------------------------------

    /*
    static boolean hasSystemFeature(Context context, @SuppressWarnings("SameParameterValue") String feature) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.hasSystemFeature(feature);
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isScreenOn(PowerManager powerManager) {
        return powerManager.isInteractive();
    }

    static int getVersionCode(PackageInfo pInfo) {
        //return pInfo.versionCode;
        return (int) PackageInfoCompat.getLongVersionCode(pInfo);
    }
    */

    static Collator getCollator()
    {
        Locale appLocale;

        // application locale
        appLocale = Locale.getDefault();

        // get collator for application locale
        return Collator.getInstance(appLocale);
    }

    static int getVersionCode(PackageInfo pInfo) {
        //return pInfo.versionCode;
        return (int) PackageInfoCompat.getLongVersionCode(pInfo);
    }

    /*
    static void createGrantPermissionNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
                if (notificationManager.getNotificationChannel(PPPPSApplication.GRANT_PERMISSION_NOTIFICATION_CHANNEL) != null)
                    return;

                // The user-visible name of the channel.
                CharSequence name = context.getString(R.string.pppputsettings_notification_channel_grant_permission);
                // The user-visible description of the channel.
                String description = context.getString(R.string.pppputsettings_notification_channel_grant_permission_description);

                NotificationChannel channel = new NotificationChannel(PPPPSApplication.GRANT_PERMISSION_NOTIFICATION_CHANNEL, name, NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                //channel.setImportance(importance);
                channel.setDescription(description);
                channel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                //channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                //channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                channel.setBypassDnd(true);

                notificationManager.createNotificationChannel(channel);
            } catch (Exception e) {
                //Log.e("PPPPSApplication.createGrantPermissionNotificationChannel", Log.getStackTraceString(e));
                PPPPSApplication.recordException(e);
            }
        }
    }
    */

    static void createExclamationNotificationChannel(Context context, boolean forceChange) {
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
                if ((!forceChange) && (notificationManager.getNotificationChannel(EXCLAMATION_NOTIFICATION_CHANNEL) != null))
                    return;

                // The user-visible name of the channel.
                CharSequence name = context.getString(R.string.pppputsettings_notification_channel_exclamation);
                // The user-visible description of the channel.
                String description = "";

                NotificationChannel channel = new NotificationChannel(EXCLAMATION_NOTIFICATION_CHANNEL, name, NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                channel.setDescription(description);
                channel.enableLights(true);
                channel.enableVibration(true);
                //channel.setSound(null, null);
                //channel.setShowBadge(false);
                channel.setBypassDnd(true);

                notificationManager.createNotificationChannel(channel);
            } catch (Exception e) {
                PPPPSApplication.recordException(e);
            }
        }
    }

}
