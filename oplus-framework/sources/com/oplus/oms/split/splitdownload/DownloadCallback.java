package com.oplus.oms.split.splitdownload;

/* loaded from: classes.dex */
public interface DownloadCallback {
    void onCanceled();

    void onCanceling();

    void onCompleted();

    void onError(int i);

    void onProgress(long j);

    void onStart();
}
