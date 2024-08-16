package com.android.server.signedconfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManagerInternal;
import android.net.Uri;
import android.os.Bundle;
import android.util.Slog;
import com.android.server.LocalServices;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SignedConfigService {
    private static final boolean DBG = false;
    private static final String KEY_GLOBAL_SETTINGS = "android.settings.global";
    private static final String KEY_GLOBAL_SETTINGS_SIGNATURE = "android.settings.global.signature";
    private static final String TAG = "SignedConfig";
    private final Context mContext;
    private final PackageManagerInternal mPacMan = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class UpdateReceiver extends BroadcastReceiver {
        private UpdateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            new SignedConfigService(context).handlePackageBroadcast(intent);
        }
    }

    public SignedConfigService(Context context) {
        this.mContext = context;
    }

    void handlePackageBroadcast(Intent intent) {
        Uri data = intent.getData();
        String schemeSpecificPart = data == null ? null : data.getSchemeSpecificPart();
        if (schemeSpecificPart == null) {
            return;
        }
        int identifier = this.mContext.getUser().getIdentifier();
        PackageInfo packageInfo = this.mPacMan.getPackageInfo(schemeSpecificPart, 128L, 1000, identifier);
        if (packageInfo == null) {
            Slog.w(TAG, "Got null PackageInfo for " + schemeSpecificPart + "; user " + identifier);
            return;
        }
        Bundle bundle = packageInfo.applicationInfo.metaData;
        if (bundle != null && bundle.containsKey(KEY_GLOBAL_SETTINGS) && bundle.containsKey(KEY_GLOBAL_SETTINGS_SIGNATURE)) {
            SignedConfigEvent signedConfigEvent = new SignedConfigEvent();
            try {
                signedConfigEvent.type = 1;
                signedConfigEvent.fromPackage = schemeSpecificPart;
                new GlobalSettingsConfigApplicator(this.mContext, schemeSpecificPart, signedConfigEvent).applyConfig(new String(Base64.getDecoder().decode(bundle.getString(KEY_GLOBAL_SETTINGS)), StandardCharsets.UTF_8), bundle.getString(KEY_GLOBAL_SETTINGS_SIGNATURE));
            } catch (IllegalArgumentException unused) {
                Slog.e(TAG, "Failed to base64 decode global settings config from " + schemeSpecificPart);
                signedConfigEvent.status = 2;
            } finally {
                signedConfigEvent.send();
            }
        }
    }

    public static void registerUpdateReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(new UpdateReceiver(), intentFilter);
    }
}
