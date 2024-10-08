package com.oplus.oms.split.splitdownload;

import java.util.List;

/* loaded from: classes.dex */
public interface Downloader {
    long calculateDownloadSize(List<DownloadRequest> list, long j);

    boolean cancelDownloadSync(int i);

    void deferredDownload(int i, List<DownloadRequest> list, DownloadCallback downloadCallback, boolean z);

    boolean forceUserConfirm();

    long getDownloadSizeThresholdWhenUsingMobileData();

    boolean isDeferredDownloadOnlyWhenUsingWifiData();

    void startDownload(int i, List<DownloadRequest> list, DownloadCallback downloadCallback);
}
