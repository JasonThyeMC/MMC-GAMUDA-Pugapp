package com.example.user.mmc_gamuda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartFirebaseAtBoot extends BroadcastReceiver {
    public StartFirebaseAtBoot() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        context.startService(new Intent(firebaseBg.class.getName()));
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
