package com.oplus.osense.eventinfo;

import com.oplus.osense.eventinfo.IOsenseEventCallback;

/* loaded from: classes.dex */
public class OsenseEventCallback extends IOsenseEventCallback.Stub {
    @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
    public void onEventSceneChanged(OsenseEventResult eventResult) {
    }

    @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
    public void onTerminateStateChanged(int pid, int uid, boolean isRegister) {
    }

    @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
    public void onRequestTerminate(int pid, String reason) {
    }

    @Override // com.oplus.osense.eventinfo.IOsenseEventCallback
    public void onProcessTerminate(String reason) {
    }
}
