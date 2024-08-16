package com.android.server.security;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.os.UserHandle;
import android.security.IKeyChainService;
import android.util.Slog;
import com.android.server.DeviceIdleInternal;
import com.android.server.LocalServices;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class KeyChainSystemService extends SystemService {
    private static final int KEYCHAIN_IDLE_WHITELIST_DURATION_MS = 30000;
    private static final String TAG = "KeyChainSystemService";
    private final BroadcastReceiver mPackageReceiver;

    public KeyChainSystemService(Context context) {
        super(context);
        this.mPackageReceiver = new BroadcastReceiver() { // from class: com.android.server.security.KeyChainSystemService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getPackage() != null) {
                    return;
                }
                try {
                    Intent intent2 = new Intent(IKeyChainService.class.getName());
                    ComponentName resolveSystemService = intent2.resolveSystemService(KeyChainSystemService.this.getContext().getPackageManager(), 0);
                    if (resolveSystemService == null) {
                        return;
                    }
                    intent2.setComponent(resolveSystemService);
                    intent2.setAction(intent.getAction());
                    KeyChainSystemService.this.startServiceInBackgroundAsUser(intent2, UserHandle.of(getSendingUserId()));
                } catch (RuntimeException e) {
                    Slog.e(KeyChainSystemService.TAG, "Unable to forward package removed broadcast to KeyChain", e);
                }
            }
        };
    }

    public void onStart() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        try {
            getContext().registerReceiverAsUser(this.mPackageReceiver, UserHandle.ALL, intentFilter, null, null);
        } catch (RuntimeException e) {
            Slog.w(TAG, "Unable to register for package removed broadcast", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startServiceInBackgroundAsUser(Intent intent, UserHandle userHandle) {
        if (intent.getComponent() == null) {
            return;
        }
        ((DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class)).addPowerSaveTempWhitelistApp(Process.myUid(), intent.getComponent().getPackageName(), 30000L, userHandle.getIdentifier(), false, 304, "keychain");
        getContext().startServiceAsUser(intent, userHandle);
    }
}
