package com.oplus.wrapper.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;

/* loaded from: classes.dex */
public class PendingIntent {
    private final android.app.PendingIntent mPendingIntent;

    public PendingIntent(android.app.PendingIntent pendingIntent) {
        this.mPendingIntent = pendingIntent;
    }

    public static android.app.PendingIntent getActivityAsUser(Context context, int requestCode, Intent intent, int flags, Bundle options, UserHandle user) {
        return android.app.PendingIntent.getActivityAsUser(context, requestCode, intent, flags, options, user);
    }

    public Intent getIntent() {
        return this.mPendingIntent.getIntent();
    }
}
