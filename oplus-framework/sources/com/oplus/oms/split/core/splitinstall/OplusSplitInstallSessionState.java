package com.oplus.oms.split.core.splitinstall;

import android.app.PendingIntent;
import android.content.Intent;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public abstract class OplusSplitInstallSessionState {
    protected long mBytesDownloaded;
    protected int mErrorCode;
    protected List<String> mModuleNames;
    protected int mSessionId;
    protected List<Intent> mSplitFileIntents;
    protected int mStatus;
    protected long mTotalBytesToDownload;
    protected PendingIntent mUserConfirmationIntent;

    protected OplusSplitInstallSessionState(int sessionId, int status, int errorCode, List<String> moduleNames, PendingIntent userConfirmationIntent, List<Intent> splitFileIntents) {
        this.mSessionId = sessionId;
        this.mStatus = status;
        this.mErrorCode = errorCode;
        this.mModuleNames = moduleNames;
        this.mUserConfirmationIntent = userConfirmationIntent;
        this.mSplitFileIntents = splitFileIntents;
    }

    protected void setDownloadedBytes(long downloadedBytes) {
        this.mBytesDownloaded = downloadedBytes;
    }

    protected void setTotalBytesToDownload(long total) {
        this.mTotalBytesToDownload = total;
    }

    public List<String> moduleNames() {
        return this.mModuleNames;
    }

    public int sessionId() {
        return this.mSessionId;
    }

    public long bytesDownloaded() {
        return this.mBytesDownloaded;
    }

    public long totalBytesToDownload() {
        return this.mTotalBytesToDownload;
    }

    public int errorCode() {
        return this.mErrorCode;
    }

    public int status() {
        return this.mStatus;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PendingIntent resolutionIntent() {
        return this.mUserConfirmationIntent;
    }

    public List<String> languages() {
        return Collections.EMPTY_LIST;
    }
}
