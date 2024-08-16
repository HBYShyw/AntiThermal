package com.android.server.policy;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class GlobalKeyIntent {
    private static final String EXTRA_BEGAN_FROM_NON_INTERACTIVE = "EXTRA_BEGAN_FROM_NON_INTERACTIVE";
    private final boolean mBeganFromNonInteractive;
    private final ComponentName mComponentName;
    private final KeyEvent mKeyEvent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GlobalKeyIntent(ComponentName componentName, KeyEvent keyEvent, boolean z) {
        this.mComponentName = componentName;
        this.mKeyEvent = new KeyEvent(keyEvent);
        this.mBeganFromNonInteractive = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Intent getIntent() {
        return new Intent("android.intent.action.GLOBAL_BUTTON").setComponent(this.mComponentName).setFlags(268435456).putExtra("android.intent.extra.KEY_EVENT", this.mKeyEvent).putExtra(EXTRA_BEGAN_FROM_NON_INTERACTIVE, this.mBeganFromNonInteractive);
    }

    public KeyEvent getKeyEvent() {
        return this.mKeyEvent;
    }

    public boolean beganFromNonInteractive() {
        return this.mBeganFromNonInteractive;
    }

    public static GlobalKeyIntent from(Intent intent) {
        if (intent.getAction() != "android.intent.action.GLOBAL_BUTTON") {
            Log.wtf("GlobalKeyIntent", "Intent should be ACTION_GLOBAL_BUTTON");
            return null;
        }
        return new GlobalKeyIntent(intent.getComponent(), (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT", KeyEvent.class), intent.getBooleanExtra(EXTRA_BEGAN_FROM_NON_INTERACTIVE, false));
    }
}
