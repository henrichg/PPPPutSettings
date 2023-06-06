package sk.henrichg.pppputsettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocaleChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ((intent != null) && (intent.getAction() != null) && intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            PPPPSApplication.collator = PPPPSApplication.getCollator();
            PPPPSApplication.createExclamationNotificationChannel(context.getApplicationContext(), true);
        }
    }

}
