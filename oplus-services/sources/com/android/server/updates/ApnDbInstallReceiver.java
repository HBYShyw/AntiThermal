package com.android.server.updates;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ApnDbInstallReceiver extends ConfigUpdateInstallReceiver {
    private static final Uri UPDATE_APN_DB = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "update_db");

    public ApnDbInstallReceiver() {
        super("/data/misc/apns/", "apns-conf.xml", "metadata/", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected void postInstall(Context context, Intent intent) {
        context.getContentResolver().delete(UPDATE_APN_DB, null, null);
    }
}
