package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.content.Intent;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitreport.SplitReporterConstant;
import com.oplus.oms.split.splitreport.SplitReporterHelper;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SplitInstallerImpl implements SplitInstaller {
    private static final String TAG = "SplitInstallerImpl";
    private final Context mAppContext;
    private final SplitInstallSessionManager mSessionManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitInstallerImpl(Context context, SplitInstallSessionManager sessionManager) {
        this.mAppContext = context;
        this.mSessionManager = sessionManager;
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstaller
    public void install(int sessionId, List<SplitVersionInfo> splitInfoList) {
        startCopyAndExtractLib(sessionId, splitInfoList);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0126 A[Catch: all -> 0x013d, TryCatch #3 {all -> 0x013d, blocks: (B:11:0x00a6, B:19:0x00e4, B:22:0x012a, B:32:0x0126, B:36:0x00c0, B:39:0x00db, B:40:0x00de), top: B:10:0x00a6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startCopyAndExtractLib(int sessionId, List<SplitVersionInfo> needInstallSplits) {
        boolean installCompleted;
        SplitLog.i(TAG, "startCopyAndExtractLib sessionId: %d", Integer.valueOf(sessionId));
        SplitInstallInternalSessionState state = this.mSessionManager.getSessionState(sessionId);
        this.mSessionManager.changeSessionState(sessionId, 4);
        this.mSessionManager.emitSessionState(state);
        List<SplitReporterInfo> reporterInfoList = new ArrayList<>();
        boolean installCompleted2 = true;
        for (SplitVersionInfo info : needInstallSplits) {
            SplitReporterInfo reporterInfo = new SplitReporterInfo(info.getSplitInfo().getSplitName(), String.valueOf(info.getInstallVersionCode()));
            reporterInfo.setAction(SplitReporterConstant.SplitInstallCode.ACTION_PART_INSTALL);
            reporterInfoList.add(reporterInfo);
            long installStart = System.currentTimeMillis();
            File splitDir = SplitPathManager.require().getSplitDir(info.getSplitInfo().getSplitName(), true);
            SplitLog.d(TAG, "onPreDownloadSplits " + splitDir, new Object[0]);
            SplitDownloadPreprocessor processor = null;
            try {
                try {
                    processor = new SplitDownloadPreprocessor(splitDir);
                    int installType = processor.install(this.mAppContext, info);
                    reporterInfo.setType(String.valueOf(installType));
                    installCompleted = installCompleted2;
                } catch (InstallException e) {
                    e = e;
                    reporterInfo.setTimeCost(System.currentTimeMillis() - installStart);
                    reporterInfo.setResultCode(e.getErrorCode());
                    SplitLog.w(TAG, "Failed to copy internal splits, sessionId: %d, code:%d, msg: %s", Integer.valueOf(sessionId), Integer.valueOf(e.getErrorCode()), e.getMessage());
                    state = this.mSessionManager.getSessionState(sessionId);
                    state.setErrorCode(e.getErrorCode() == 0 ? -100 : e.getErrorCode());
                    this.mSessionManager.changeSessionState(sessionId, 6);
                    this.mSessionManager.emitSessionState(state);
                    installCompleted2 = false;
                    FileUtil.closeQuietly(processor);
                    if (installCompleted2) {
                    }
                    if (reporterInfoList.isEmpty()) {
                    }
                } catch (Throwable th) {
                    e = th;
                    FileUtil.closeQuietly(processor);
                    throw e;
                }
                try {
                    try {
                        reporterInfo.setTimeCost(System.currentTimeMillis() - installStart);
                        FileUtil.closeQuietly(processor);
                        installCompleted2 = installCompleted;
                    } catch (InstallException e2) {
                        e = e2;
                        reporterInfo.setTimeCost(System.currentTimeMillis() - installStart);
                        reporterInfo.setResultCode(e.getErrorCode());
                        SplitLog.w(TAG, "Failed to copy internal splits, sessionId: %d, code:%d, msg: %s", Integer.valueOf(sessionId), Integer.valueOf(e.getErrorCode()), e.getMessage());
                        state = this.mSessionManager.getSessionState(sessionId);
                        state.setErrorCode(e.getErrorCode() == 0 ? -100 : e.getErrorCode());
                        this.mSessionManager.changeSessionState(sessionId, 6);
                        this.mSessionManager.emitSessionState(state);
                        installCompleted2 = false;
                        FileUtil.closeQuietly(processor);
                        if (installCompleted2) {
                        }
                        if (reporterInfoList.isEmpty()) {
                        }
                    }
                } catch (Throwable th2) {
                    e = th2;
                    FileUtil.closeQuietly(processor);
                    throw e;
                }
            } catch (IOException | IllegalStateException e3) {
                SplitLog.e(TAG, "SplitDownloadPreprocessor sessionId: %d, error: %s", Integer.valueOf(sessionId), e3.getMessage());
                try {
                    throw new InstallException(-100, e3);
                } catch (InstallException e4) {
                    e = e4;
                    reporterInfo.setTimeCost(System.currentTimeMillis() - installStart);
                    reporterInfo.setResultCode(e.getErrorCode());
                    SplitLog.w(TAG, "Failed to copy internal splits, sessionId: %d, code:%d, msg: %s", Integer.valueOf(sessionId), Integer.valueOf(e.getErrorCode()), e.getMessage());
                    state = this.mSessionManager.getSessionState(sessionId);
                    state.setErrorCode(e.getErrorCode() == 0 ? -100 : e.getErrorCode());
                    this.mSessionManager.changeSessionState(sessionId, 6);
                    this.mSessionManager.emitSessionState(state);
                    installCompleted2 = false;
                    FileUtil.closeQuietly(processor);
                    if (installCompleted2) {
                    }
                    if (reporterInfoList.isEmpty()) {
                    }
                }
            }
        }
        if (installCompleted2) {
            onInstallComplete(sessionId, state);
        }
        if (reporterInfoList.isEmpty()) {
            SplitReporterHelper.reporter(SplitReporterConstant.INSTALL_TAG, reporterInfoList);
        }
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstaller
    public void skipInstall(int sessionId, SplitInstallInternalSessionState state) {
        onInstallComplete(sessionId, state);
    }

    private void onInstallComplete(int sessionId, SplitInstallInternalSessionState state) {
        if (state == null) {
            state = this.mSessionManager.getSessionState(sessionId);
        }
        List<SplitInfo> needInstalledSplits = state.needInstalledSplits();
        List<Intent> splitFileIntents = new ArrayList<>(needInstalledSplits.size());
        for (SplitInfo split : needInstalledSplits) {
            Intent intent = new Intent();
            intent.putExtra(SplitConstants.KEY_SPLIT_NAME, split.getSplitName());
            splitFileIntents.add(intent);
        }
        state.setSplitFileIntents(splitFileIntents);
        state.setStatus(10);
        this.mSessionManager.changeSessionState(sessionId, 10);
        this.mSessionManager.emitSessionState(state);
    }
}
