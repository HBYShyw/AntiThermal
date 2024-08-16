package com.oplus.oms.split.splitinstall;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.oplus.Telephony;
import com.oplus.oms.split.splitdownload.DownloadRequest;
import com.oplus.oms.split.splitrequest.SplitInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
final class SplitInstallInternalSessionState {
    private long mBytesDownloaded;
    private final List<DownloadRequest> mDownloadRequests;
    private int mErrorCode;
    private final List<String> mModuleNames;
    private final List<SplitInfo> mNeedInstalledSplits;
    private final List<SplitVersionInfo> mSelectNeedInstalledSplits;
    private int mSessionId;
    private List<Intent> mSplitFileIntents;
    private int mStatus;
    private long mTotalBytesToDownload;
    private PendingIntent mUserConfirmationIntent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitInstallInternalSessionState(int sessionId, List<String> moduleNames, List<SplitInfo> needInstalledSplits, List<SplitVersionInfo> selectNeedInstalledSplits, List<DownloadRequest> downloadRequests) {
        this.mSessionId = sessionId;
        this.mModuleNames = moduleNames;
        this.mNeedInstalledSplits = needInstalledSplits;
        this.mSelectNeedInstalledSplits = selectNeedInstalledSplits;
        this.mDownloadRequests = downloadRequests;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bundle transform2Bundle(SplitInstallInternalSessionState sessionState) {
        if (sessionState == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("session_id", sessionState.sessionId());
        bundle.putInt("status", sessionState.status());
        bundle.putInt(Telephony.TextBasedSmsColumns.ERROR_CODE, sessionState.mErrorCode);
        bundle.putLong("total_bytes_to_download", sessionState.mTotalBytesToDownload);
        bundle.putLong("bytes_downloaded", sessionState.mBytesDownloaded);
        bundle.putStringArrayList("module_names", (ArrayList) sessionState.moduleNames());
        bundle.putParcelable("user_confirmation_intent", sessionState.mUserConfirmationIntent);
        bundle.putParcelableArrayList("split_file_intents", (ArrayList) sessionState.mSplitFileIntents);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> moduleNames() {
        return this.mModuleNames;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<SplitInfo> needInstalledSplits() {
        return this.mNeedInstalledSplits;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<SplitVersionInfo> selectNeedInstalledSplits() {
        return this.mSelectNeedInstalledSplits;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<DownloadRequest> downloadRequests() {
        return this.mDownloadRequests;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBytesDownloaded(long bytesDownloaded) {
        if (this.mBytesDownloaded != bytesDownloaded) {
            this.mBytesDownloaded = bytesDownloaded;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTotalBytesToDownload(long totalBytesToDownload) {
        this.mTotalBytesToDownload = totalBytesToDownload;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int status() {
        return this.mStatus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStatus(int status) {
        if (this.mStatus != status) {
            this.mStatus = status;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int sessionId() {
        return this.mSessionId;
    }

    void setSessionId(int sessionId) {
        this.mSessionId = sessionId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUserConfirmationIntent(PendingIntent userConfirmationIntent) {
        this.mUserConfirmationIntent = userConfirmationIntent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSplitFileIntents(List<Intent> splitFileIntents) {
        this.mSplitFileIntents = splitFileIntents;
    }
}
