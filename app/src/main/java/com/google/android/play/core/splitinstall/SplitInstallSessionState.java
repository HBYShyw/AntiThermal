package com.google.android.play.core.splitinstall;

import android.app.PendingIntent;
import android.content.Intent;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;
import java.util.List;

/* loaded from: classes.dex */
public class SplitInstallSessionState extends OplusSplitInstallSessionState {
    public SplitInstallSessionState(int i10, int i11, int i12, long j10, long j11, List<String> list, PendingIntent pendingIntent, List<Intent> list2) {
        super(i10, i11, i12, list, pendingIntent, list2);
        setDownloadedBytes(j10);
        setTotalBytesToDownload(j11);
    }

    public List<Intent> a() {
        return ((OplusSplitInstallSessionState) this).mSplitFileIntents;
    }

    public long bytesDownloaded() {
        return super.bytesDownloaded();
    }

    public int errorCode() {
        return super.errorCode();
    }

    public boolean hasTerminalStatus() {
        return status() == 0 || status() == 5 || status() == 6 || status() == 7;
    }

    public List<String> languages() {
        return super.languages();
    }

    public List<String> moduleNames() {
        return super.moduleNames();
    }

    public PendingIntent resolutionIntent() {
        return super.resolutionIntent();
    }

    public int sessionId() {
        return super.sessionId();
    }

    public int status() {
        return super.status();
    }

    public String toString() {
        return "SplitInstallSessionState{sessionId=" + sessionId() + ", status=" + status() + ", errorCode=" + errorCode() + ", bytesDownloaded=" + bytesDownloaded() + ",totalBytesToDownload=" + totalBytesToDownload() + ",moduleNames=" + moduleNames() + "}";
    }

    public long totalBytesToDownload() {
        return super.totalBytesToDownload();
    }
}
