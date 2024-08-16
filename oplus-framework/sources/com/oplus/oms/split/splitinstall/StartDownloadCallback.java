package com.oplus.oms.split.splitinstall;

import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitdownload.DownloadCallback;
import com.oplus.oms.split.splitreport.SplitReporterHelper;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class StartDownloadCallback implements DownloadCallback {
    private final SplitInstaller mInstaller;
    private final int mSessionId;
    private final SplitInstallSessionManager mSessionManager;
    private final SplitInstallInternalSessionState mSessionState;
    private List<SplitVersionInfo> mSplitInfoList;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartDownloadCallback(SplitInstaller splitInstaller, int sessionId, SplitInstallSessionManager sessionManager, List<SplitVersionInfo> splitInfoList) {
        this.mSessionId = sessionId;
        this.mSessionManager = sessionManager;
        this.mInstaller = splitInstaller;
        this.mSplitInfoList = splitInfoList;
        this.mSessionState = sessionManager.getSessionState(sessionId);
    }

    @Override // com.oplus.oms.split.splitdownload.DownloadCallback
    public void onStart() {
        this.mSessionManager.changeSessionState(this.mSessionId, 2);
        broadcastSessionStatusChange();
    }

    @Override // com.oplus.oms.split.splitdownload.DownloadCallback
    public void onCanceled() {
        this.mSessionManager.changeSessionState(this.mSessionId, 7);
        broadcastSessionStatusChange();
        reportDownloadStatus(-34);
    }

    @Override // com.oplus.oms.split.splitdownload.DownloadCallback
    public void onCanceling() {
        this.mSessionManager.changeSessionState(this.mSessionId, 9);
        broadcastSessionStatusChange();
    }

    @Override // com.oplus.oms.split.splitdownload.DownloadCallback
    public void onProgress(long currentBytes) {
        this.mSessionState.setBytesDownloaded(currentBytes);
        this.mSessionManager.changeSessionState(this.mSessionId, 2);
        broadcastSessionStatusChange();
    }

    @Override // com.oplus.oms.split.splitdownload.DownloadCallback
    public void onCompleted() {
        this.mSessionManager.changeSessionState(this.mSessionId, 3);
        broadcastSessionStatusChange();
        reportDownloadStatus(1);
        onInstall();
    }

    @Override // com.oplus.oms.split.splitdownload.DownloadCallback
    public void onError(int errorCode) {
        SplitLog.w(SplitVersionPolicy.TAG, "download onError session: %d, errorCode: %d", Integer.valueOf(this.mSessionId), Integer.valueOf(errorCode));
        if (canDowngrade()) {
            SplitLog.w(SplitVersionPolicy.TAG, "download onError: downgrade session:%d", Integer.valueOf(this.mSessionId));
            onCompleted();
            return;
        }
        int internalTransformErrorCode = transformErrorCode(errorCode);
        this.mSessionState.setErrorCode(internalTransformErrorCode);
        this.mSessionManager.changeSessionState(this.mSessionId, 6);
        broadcastSessionStatusChange();
        reportDownloadStatus(internalTransformErrorCode);
    }

    private static int transformErrorCode(int errorCode) {
        switch (errorCode) {
            case 1:
                return -6;
            case 2:
                return -10;
            default:
                return -100;
        }
    }

    private void reportDownloadStatus(int status) {
        List<SplitVersionInfo> list = this.mSplitInfoList;
        if (list == null || list.isEmpty()) {
            return;
        }
        List<SplitReporterInfo> splitReporterList = new ArrayList<>();
        for (SplitVersionInfo info : this.mSplitInfoList) {
            if (info.getFrom() == 2) {
                SplitReporterInfo reporterInfo = new SplitReporterInfo();
                reporterInfo.setType("1");
                reporterInfo.setName(info.getSplitInfo().getSplitName());
                reporterInfo.setVersion(info.getInstallVersionName());
                reporterInfo.setResultCode(status);
                splitReporterList.add(reporterInfo);
            }
        }
        if (splitReporterList.isEmpty()) {
            return;
        }
        SplitReporterHelper.reporter("download", splitReporterList);
    }

    private void onInstall() {
        this.mInstaller.install(this.mSessionId, this.mSplitInfoList);
    }

    private void broadcastSessionStatusChange() {
        this.mSessionManager.emitSessionState(this.mSessionState);
    }

    private boolean canDowngrade() {
        List<SplitVersionInfo> list = this.mSplitInfoList;
        if (list == null || list.isEmpty()) {
            return false;
        }
        List<SplitVersionInfo> downgradeList = new ArrayList<>(this.mSplitInfoList.size());
        boolean downgrade = true;
        for (SplitVersionInfo svi : this.mSplitInfoList) {
            if (svi.getFrom() == 2) {
                SplitVersionInfo downgradeInfo = svi.getDefaultVersionInfo();
                if (downgradeInfo == null || downgradeInfo.getFrom() == -1) {
                    downgrade = false;
                    SplitLog.i(SplitVersionPolicy.TAG, "split download error and no default splitInfo", new Object[0]);
                    break;
                }
                downgradeList.add(downgradeInfo);
            } else {
                downgradeList.add(svi);
            }
        }
        SplitLog.i(SplitVersionPolicy.TAG, "canDowngrade session: %d, is downgrade: %b, origin: %s, downgrade: %s", Integer.valueOf(this.mSessionId), Boolean.valueOf(downgrade), LogUtil.toString(this.mSplitInfoList), LogUtil.toString(downgradeList));
        if (downgrade) {
            this.mSplitInfoList = downgradeList;
        }
        return downgrade;
    }
}
