package com.oplus.eap;

import android.app.ApplicationExitInfo;
import android.os.SharedMemory;
import com.oplus.eap.IOplusEapDataCallback;

/* loaded from: classes.dex */
public abstract class OplusEapDataCallback extends IOplusEapDataCallback.Stub {
    @Override // com.oplus.eap.IOplusEapDataCallback
    public void onAppCrashed(SharedMemory data) {
    }

    @Override // com.oplus.eap.IOplusEapDataCallback
    public void onExitInfoRecordAdded(ApplicationExitInfo exitInfo) {
    }
}
