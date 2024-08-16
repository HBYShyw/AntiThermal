package com.oplus.foldswitch;

import android.os.Bundle;
import com.oplus.foldswitch.IOplusFoldSwitchStateObserver;

/* loaded from: classes.dex */
public class OplusFoldSwitchStateObserver extends IOplusFoldSwitchStateObserver.Stub {
    public static final String KEY_DEVICE_FOLDING = "FSS_deviceFolding";

    @Override // com.oplus.foldswitch.IOplusFoldSwitchStateObserver
    public void onFoldingSwitchStateChanged(Bundle bundle) {
    }
}
