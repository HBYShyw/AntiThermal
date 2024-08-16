package com.google.android.play.core.splitinstall;

import android.app.PendingIntent;
import android.os.Bundle;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionStateFactory;

/* loaded from: classes.dex */
class SplitInstallSessionStateFactoryImpl implements OplusSplitInstallSessionStateFactory<SplitInstallSessionState> {
    /* renamed from: create, reason: merged with bridge method [inline-methods] */
    public SplitInstallSessionState m1create(Bundle bundle) {
        return new SplitInstallSessionState(bundle.getInt("session_id"), bundle.getInt("status"), bundle.getInt("error_code"), bundle.getLong("bytes_downloaded"), bundle.getLong("total_bytes_to_download"), bundle.getStringArrayList("module_names"), (PendingIntent) bundle.getParcelable("user_confirmation_intent"), bundle.getParcelableArrayList("split_file_intents"));
    }

    public SplitInstallSessionState newState(SplitInstallSessionState splitInstallSessionState, int i10, int i11) {
        return new SplitInstallSessionState(splitInstallSessionState.sessionId(), i10, i11, splitInstallSessionState.bytesDownloaded(), splitInstallSessionState.totalBytesToDownload(), splitInstallSessionState.moduleNames(), splitInstallSessionState.resolutionIntent(), splitInstallSessionState.a());
    }

    public SplitInstallSessionState newState(SplitInstallSessionState splitInstallSessionState, int i10) {
        return new SplitInstallSessionState(splitInstallSessionState.sessionId(), i10, splitInstallSessionState.errorCode(), splitInstallSessionState.bytesDownloaded(), splitInstallSessionState.totalBytesToDownload(), splitInstallSessionState.moduleNames(), splitInstallSessionState.resolutionIntent(), splitInstallSessionState.a());
    }
}
