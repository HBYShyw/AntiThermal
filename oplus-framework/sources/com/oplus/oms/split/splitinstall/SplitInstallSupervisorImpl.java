package com.oplus.oms.split.splitinstall;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.NetworkUtil;
import com.oplus.oms.split.common.SplitAABInfoProvider;
import com.oplus.oms.split.common.SplitBaseInfoProvider;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.splitinstall.SplitInstallListenerRegistry;
import com.oplus.oms.split.splitdownload.DownloadRequest;
import com.oplus.oms.split.splitdownload.Downloader;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import com.oplus.oms.split.splitdownload.SplitUpdateInfo;
import com.oplus.oms.split.splitinstall.SplitDownloadPreprocessor;
import com.oplus.oms.split.splitinstall.SplitVersionPolicy;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;
import com.oplus.oms.split.splitreport.SplitReportUtil;
import com.oplus.oms.split.splitreport.SplitReporterConstant;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInfoManager;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public final class SplitInstallSupervisorImpl extends SplitInstallSupervisor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final AtomicReference<SplitInstallSupervisor> REFERENCE = new AtomicReference<>();
    private static final String TAG = "SplitInstallSupervisorImpl";
    private final Context mAppContext;
    private final long mDownloadSizeThresholdValue;
    private final List<String> mDynamicFeatures;
    private final Set<String> mInstalledSplitForAAB;
    private final SplitInstaller mInstaller;
    private final Class<?> mObtainUserConfirmationActivityClass;
    private final SplitInstallSessionManager mSessionManager;
    private final ISplitUpdateManager mUpdateManager;
    private final Downloader mUserDownloader;
    private final boolean mVerifySignature;

    private SplitInstallSupervisorImpl(Context appContext, SplitInstallSessionManager sessionManager, Downloader userDownloader, ISplitUpdateManager updateManager, Class<? extends Activity> obtainUserConfirmationActivityClass, boolean verifySignature) {
        this.mAppContext = appContext;
        this.mSessionManager = sessionManager;
        this.mUserDownloader = userDownloader;
        this.mInstaller = new SplitInstallerImpl(appContext, sessionManager);
        this.mUpdateManager = updateManager;
        long downloadSizeThreshold = 0;
        if (userDownloader != null) {
            downloadSizeThreshold = userDownloader.getDownloadSizeThresholdWhenUsingMobileData();
            SplitLog.d(TAG, "downloadSizeThreshold = " + downloadSizeThreshold, new Object[0]);
        }
        this.mDownloadSizeThresholdValue = downloadSizeThreshold < 0 ? Long.MAX_VALUE : downloadSizeThreshold;
        this.mInstalledSplitForAAB = new SplitAABInfoProvider(appContext).getInstalledSplitsForAAB();
        this.mObtainUserConfirmationActivityClass = obtainUserConfirmationActivityClass;
        this.mVerifySignature = verifySignature;
        String[] dynamicFeaturesArray = SplitBaseInfoProvider.getDynamicFeatures();
        List<String> asList = dynamicFeaturesArray == null ? null : Arrays.asList(dynamicFeaturesArray);
        this.mDynamicFeatures = asList;
        if (asList == null) {
            SplitLog.w(TAG, "Can't read dynamicFeatures from SplitBaseInfoProvider", new Object[0]);
        }
    }

    public static void install(Context context, Downloader downloader, ISplitUpdateManager updateManager, Class<? extends Activity> obtainUserConfirmationActivityClass, boolean verifySignature) {
        AtomicReference<SplitInstallSupervisor> atomicReference = REFERENCE;
        if (atomicReference.get() == null) {
            atomicReference.set(new SplitInstallSupervisorImpl(context, new SplitInstallSessionManagerImpl(context), downloader, updateManager, obtainUserConfirmationActivityClass, verifySignature));
        }
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void setInstallListenerRegister(SplitInstallListenerRegistry registry) {
        if (REFERENCE.get() != null) {
            SplitLog.i(TAG, "setInstallListenerRegister", new Object[0]);
            this.mSessionManager.setInstallListenerRegistry(registry);
        }
    }

    public static SplitInstallSupervisor getSplitInstallSupervisor() {
        return REFERENCE.get();
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void startInstall(final List<String> moduleNames, final SplitInstallSupervisor.Callback callback) {
        if (callback == null || moduleNames == null || moduleNames.isEmpty()) {
            return;
        }
        int errorCode = onPreInstallSplits(moduleNames);
        if (errorCode != 0) {
            SplitReportUtil.reportInstallResult(moduleNames, SplitReporterConstant.SplitInstallCode.ACTION_TOTAL_INSTALL, errorCode);
            callback.onError(bundleErrorCode(errorCode));
            return;
        }
        final List<SplitInfo> needInstallSplits = getNeed2BeInstalledSplits(moduleNames);
        SplitLog.i(TAG, "startInstall moduleNames: %s, needInstallSplits: %s", moduleNames, LogUtil.toString(needInstallSplits));
        if (needInstallSplits.isEmpty()) {
            SplitLog.w(TAG, "startInstall split names not exists: %s", moduleNames);
            SplitReportUtil.reportInstallResult(moduleNames, SplitReporterConstant.SplitInstallCode.ACTION_TOTAL_INSTALL, -3);
            callback.onError(bundleErrorCode(-3));
            return;
        }
        SplitVersionPolicy.selectSplitVersionList(this.mAppContext, needInstallSplits, this.mUpdateManager, new SplitVersionPolicy.VersionPolicyCallBack() { // from class: com.oplus.oms.split.splitinstall.SplitInstallSupervisorImpl$$ExternalSyntheticLambda1
            @Override // com.oplus.oms.split.splitinstall.SplitVersionPolicy.VersionPolicyCallBack
            public final void setSplitVersionInfoList(int i, List list) {
                SplitInstallSupervisorImpl.this.lambda$startInstall$0(moduleNames, needInstallSplits, callback, i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startInstall$0(List moduleNames, List needInstallSplits, SplitInstallSupervisor.Callback callback, int resultCode, List selectedSplits) {
        SplitLog.i(TAG, "select split version status: %d, splits: %s", Integer.valueOf(resultCode), LogUtil.toString(selectedSplits));
        if (resultCode == 0) {
            startDownloadSplits(moduleNames, needInstallSplits, selectedSplits, callback);
        } else {
            SplitReportUtil.reportInstallResult(moduleNames, SplitReporterConstant.SplitInstallCode.ACTION_TOTAL_INSTALL, -2);
            callback.onError(bundleErrorCode(-2));
        }
    }

    private void startDownloadSplits(List<String> moduleNames, List<SplitInfo> needInstallSplits, List<SplitVersionInfo> selectedNeedInstallSplits, SplitInstallSupervisor.Callback callback) {
        SplitInstallInternalSessionState sessionState;
        if (this.mSessionManager.isActiveSessionsLimitExceeded()) {
            onTaskError(moduleNames, SplitReporterConstant.SplitInstallCode.ACTION_TOTAL_INSTALL, -1, callback);
            return;
        }
        int sessionId = getSessionId(needInstallSplits, selectedNeedInstallSplits);
        SplitLog.i(TAG, "startDownloadSplits sessionId: %d", Integer.valueOf(sessionId));
        SplitInstallInternalSessionState sessionState2 = this.mSessionManager.getSessionState(sessionId);
        boolean needUserConfirmation = sessionState2 != null && sessionState2.status() == 8;
        if (!needUserConfirmation && this.mSessionManager.isIncompatibleWithExistingSession(moduleNames)) {
            onTaskError(moduleNames, SplitReporterConstant.SplitInstallCode.ACTION_TOTAL_INSTALL, -8, callback);
            return;
        }
        SplitLog.i(TAG, "startDownloadSplits sessionId: %d, uninstalled: %s", Integer.valueOf(sessionId), LogUtil.toString(selectedNeedInstallSplits));
        List<DownloadRequest> downloadRequests = createDownloadRequests(selectedNeedInstallSplits);
        if (sessionState2 != null) {
            sessionState = sessionState2;
        } else {
            sessionState = new SplitInstallInternalSessionState(sessionId, moduleNames, needInstallSplits, selectedNeedInstallSplits, downloadRequests);
        }
        if (selectedNeedInstallSplits.isEmpty()) {
            SplitLog.i(TAG, "Splits[%s] already installed. sessionId: %d", LogUtil.toString(selectedNeedInstallSplits), Integer.valueOf(sessionId));
            callback.onStartInstall(sessionId, null);
            this.mInstaller.skipInstall(sessionId, sessionState);
            return;
        }
        try {
            long[] result = onPreDownloadSplits(selectedNeedInstallSplits);
            callback.onStartInstall(sessionId, null);
            this.mSessionManager.setSessionState(sessionId, sessionState);
            startDownloadSplitsInternal(selectedNeedInstallSplits, sessionId, sessionState, downloadRequests, result);
        } catch (IOException | IllegalStateException e) {
            SplitLog.i(TAG, "onPreDownloadSplits fail sessionId: %d, error: %s", Integer.valueOf(sessionId), e.getMessage());
            callback.onStartInstall(sessionId, null);
            onInstallError(moduleNames, sessionState, SplitReporterConstant.SplitInstallCode.ACTION_TOTAL_INSTALL, -100);
        }
    }

    private void startDownloadSplitsInternal(List<SplitVersionInfo> selectedNeedInstallSplits, int sessionId, SplitInstallInternalSessionState sessionState, List<DownloadRequest> downloadRequests, long[] result) {
        long totalBytesToDownload = result[0];
        Downloader downloader = this.mUserDownloader;
        long realTotalBytesNeedToDownload = downloader == null ? result[1] : downloader.calculateDownloadSize(downloadRequests, result[1]);
        SplitLog.i(TAG, "startDownloadSplits sessionId: %d, totalBytesToDownload: %d, realTotalBytesNeedToDownload: %d ", Integer.valueOf(sessionId), Long.valueOf(totalBytesToDownload), Long.valueOf(realTotalBytesNeedToDownload));
        sessionState.setTotalBytesToDownload(totalBytesToDownload);
        StartDownloadCallback downloadCallback = new StartDownloadCallback(this.mInstaller, sessionId, this.mSessionManager, selectedNeedInstallSplits);
        if (this.mUserDownloader == null) {
            SplitLog.i(TAG, "User Downloader is null, skip download, sessionId: %d", Integer.valueOf(sessionId));
            downloadCallback.onCompleted();
            return;
        }
        if (realTotalBytesNeedToDownload <= 0) {
            SplitLog.i(TAG, "Splits have been downloaded, install them directly! sessionId: %d", Integer.valueOf(sessionId));
            downloadCallback.onCompleted();
            return;
        }
        if (!NetworkUtil.isAllowUseNet(this.mAppContext)) {
            this.mSessionManager.changeSessionState(sessionId, 1);
            this.mSessionManager.emitSessionState(sessionState);
            downloadCallback.onError(-7);
        } else if (checkDownloadUserConfirmation(sessionId, sessionState, downloadRequests, realTotalBytesNeedToDownload, downloadCallback)) {
            SplitLog.i(TAG, "call Downloader.startDownload, session: %d", Integer.valueOf(sessionId));
            this.mSessionManager.changeSessionState(sessionId, 1);
            this.mSessionManager.emitSessionState(sessionState);
            this.mUserDownloader.startDownload(sessionId, downloadRequests, downloadCallback);
        }
    }

    private void onTaskError(List<String> moduleNames, String action, int errorCode, SplitInstallSupervisor.Callback callback) {
        SplitLog.w(TAG, "startDownloadSplits error code: %d, splits: %s", Integer.valueOf(errorCode), moduleNames);
        SplitReportUtil.reportInstallResult(moduleNames, action, errorCode);
        callback.onError(bundleErrorCode(errorCode));
    }

    private void onInstallError(List<String> moduleNames, SplitInstallInternalSessionState sessionState, String action, int errorCode) {
        SplitReportUtil.reportInstallResult(moduleNames, action, errorCode);
        sessionState.setStatus(6);
        sessionState.setErrorCode(errorCode);
        this.mSessionManager.emitSessionState(sessionState);
    }

    private boolean checkDownloadUserConfirmation(int sessionId, SplitInstallInternalSessionState sessionState, List<DownloadRequest> downloadRequests, long realTotalBytesNeedToDownload, StartDownloadCallback downloadCallback) {
        if (!this.mUserDownloader.forceUserConfirm() && (!NetworkUtil.isDataConnected(this.mAppContext) || realTotalBytesNeedToDownload <= this.mDownloadSizeThresholdValue)) {
            return true;
        }
        SplitLog.i(TAG, "startUserConfirmationActivity, session: %d, confirm class: %s", Integer.valueOf(sessionId), this.mObtainUserConfirmationActivityClass);
        if (this.mObtainUserConfirmationActivityClass == null) {
            this.mSessionManager.changeSessionState(sessionId, 1);
            this.mSessionManager.emitSessionState(sessionState);
            downloadCallback.onError(-7);
            return false;
        }
        startUserConfirmationActivity(sessionState, realTotalBytesNeedToDownload, downloadRequests);
        return false;
    }

    private static int getSessionId(List<SplitInfo> needInstallSplits, List<SplitVersionInfo> selectedNeedInstallSplits) {
        if (selectedNeedInstallSplits == null || selectedNeedInstallSplits.isEmpty()) {
            int sessionId = createSessionId(needInstallSplits);
            return sessionId;
        }
        int sessionId2 = createSessionId(selectedNeedInstallSplits);
        return sessionId2;
    }

    private int onPreInstallSplits(List<String> moduleNames) {
        if (!getInstalledSplitForAAB().isEmpty()) {
            if (!getInstalledSplitForAAB().containsAll(moduleNames)) {
                return -3;
            }
            return 0;
        }
        int errorCode = checkInternalErrorCode();
        if (errorCode == 0) {
            return checkRequestErrorCode(moduleNames);
        }
        return errorCode;
    }

    private int checkRequestErrorCode(List<String> moduleNames) {
        if (isRequestInvalid(moduleNames)) {
            SplitLog.w(TAG, "isRequestInvalid error", new Object[0]);
            return -3;
        }
        if (isModuleAvailable(moduleNames)) {
            return 0;
        }
        SplitLog.w(TAG, "isModuleAvailable error", new Object[0]);
        return -2;
    }

    private int checkInternalErrorCode() {
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        if (manager == null) {
            SplitLog.w(TAG, "Failed to fetch SplitInfoManager instance!", new Object[0]);
            return -100;
        }
        Collection<SplitInfo> allSplits = manager.getAllSplitInfo(this.mAppContext);
        if (allSplits == null || allSplits.isEmpty()) {
            SplitLog.w(TAG, "Failed to parse json file of split info!", new Object[0]);
            return -100;
        }
        String baseAppVersionName = manager.getBaseAppVersionName(this.mAppContext);
        String versionName = SplitBaseInfoProvider.getVersionName();
        if (TextUtils.isEmpty(baseAppVersionName) || !baseAppVersionName.equals(versionName)) {
            SplitLog.w(TAG, "Failed to match base app version-name excepted base app version " + versionName + " but" + baseAppVersionName + "!", new Object[0]);
            return -100;
        }
        String omsId = manager.getOmsId(this.mAppContext);
        String baseAppOmsId = SplitBaseInfoProvider.getOmsId();
        if (!TextUtils.isEmpty(omsId) && omsId.equals(baseAppOmsId)) {
            return 0;
        }
        SplitLog.w(TAG, "Failed to match base app oms-version excepted " + baseAppOmsId + " but " + omsId + "!", new Object[0]);
        return -100;
    }

    private List<SplitInfo> getNeed2BeInstalledSplits(List<String> moduleNames) {
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        List<SplitInfo> needInstallSplitInfos = manager.getSplitInfos(this.mAppContext, moduleNames);
        final Set<String> dependenciesSplits = new HashSet<>(0);
        for (SplitInfo info : needInstallSplitInfos) {
            if (info.getDependencies() != null) {
                dependenciesSplits.addAll(info.getDependencies());
            }
        }
        if (!dependenciesSplits.isEmpty()) {
            Objects.requireNonNull(dependenciesSplits);
            moduleNames.forEach(new Consumer() { // from class: com.oplus.oms.split.splitinstall.SplitInstallSupervisorImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    dependenciesSplits.remove((String) obj);
                }
            });
            SplitLog.i(TAG, "Add dependencies %s automatically for install splits %s ", dependenciesSplits, moduleNames);
            List<SplitInfo> dependenciesSplitInfos = manager.getSplitInfos(this.mAppContext, dependenciesSplits);
            dependenciesSplitInfos.addAll(needInstallSplitInfos);
            return dependenciesSplitInfos;
        }
        return needInstallSplitInfos;
    }

    private boolean isRequestInvalid(List<String> moduleNames) {
        return moduleNames == null || moduleNames.isEmpty() || this.mDynamicFeatures == null || !new HashSet(this.mDynamicFeatures).containsAll(moduleNames);
    }

    private boolean isModuleAvailable(List<String> moduleNames) {
        if (moduleNames == null || moduleNames.isEmpty()) {
            return false;
        }
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        Collection<SplitInfo> splitInfoList = manager.getAllSplitInfo(this.mAppContext);
        if (splitInfoList.isEmpty()) {
            return false;
        }
        for (String moduleName : moduleNames) {
            for (SplitInfo info : splitInfoList) {
                if (info.getSplitName().equals(moduleName)) {
                    return checkSplitInfo(info);
                }
            }
        }
        return true;
    }

    private boolean checkSplitInfo(SplitInfo info) {
        return isCPUArchMatched(info) && isMinSdkVersionMatched(info);
    }

    private boolean isMinSdkVersionMatched(SplitInfo splitInfo) {
        return splitInfo.getMinSdkVersion() <= Build.VERSION.SDK_INT;
    }

    private boolean isCPUArchMatched(SplitInfo splitInfo) {
        try {
            splitInfo.getPrimaryLibData(this.mAppContext);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private List<DownloadRequest> createDownloadRequests(Collection<SplitVersionInfo> needInstallSplits) {
        if (needInstallSplits == null || needInstallSplits.isEmpty()) {
            return Collections.emptyList();
        }
        List<DownloadRequest> requests = new ArrayList<>(needInstallSplits.size());
        for (SplitVersionInfo splitInfo : needInstallSplits) {
            if (splitInfo.getFrom() == 2) {
                String splitName = splitInfo.getSplitInfo().getSplitName();
                SplitUpdateInfo updateInfo = splitInfo.getSplitDownloadInfo();
                String url = updateInfo != null ? updateInfo.getUrl() : splitInfo.getSplitInfo().getMasterApkUrl();
                String md5 = updateInfo != null ? updateInfo.getMd5() : splitInfo.getSplitInfo().getMasterApkMd5();
                long size = updateInfo != null ? updateInfo.getSize() : splitInfo.getSplitInfo().getMasterApkSize();
                SplitPathManager pathManager = SplitPathManager.require();
                DownloadRequest request = DownloadRequest.newBuilder().url(url).md5(md5).size(size).currentVersion(splitInfo.getInstallVersion()).moduleName(splitInfo.getSplitInfo().getSplitName()).extra(splitInfo.getSplitInfo().getInfoForSplit()).savePath(pathManager.getSplitDirDownloadDir(splitName, "" + splitInfo.getInstallVersionCode(), true).getAbsolutePath()).saveFileName(pathManager.getSplitApkFileName(splitName)).build();
                requests.add(request);
            }
        }
        return requests;
    }

    private Set<String> getInstalledSplitForAAB() {
        return this.mInstalledSplitForAAB;
    }

    private long[] onPreDownloadSplits(Collection<SplitVersionInfo> splitInfoList) throws IOException, IllegalStateException {
        Iterator<SplitVersionInfo> it;
        SplitInstallSupervisorImpl splitInstallSupervisorImpl = this;
        long totalBytesToDownload = 0;
        long realTotalBytesNeedToDownload = 0;
        Iterator<SplitVersionInfo> it2 = splitInfoList.iterator();
        while (it2.hasNext()) {
            SplitVersionInfo splitInfo = it2.next();
            File splitDir = SplitPathManager.require().getSplitDir(splitInfo.getSplitInfo().getSplitName(), true);
            SplitDownloadPreprocessor processor = new SplitDownloadPreprocessor(splitDir);
            try {
                List<SplitDownloadPreprocessor.SplitFile> splitApkList = processor.load(splitInstallSupervisorImpl.mAppContext, splitInfo, splitInstallSupervisorImpl.mVerifySignature);
                FileUtil.closeQuietly(processor);
                SplitUpdateInfo updateInfo = splitInfo.getSplitDownloadInfo();
                long size = updateInfo != null ? updateInfo.getSize() : splitInfo.getSplitInfo().getMasterApkSize();
                totalBytesToDownload += size;
                for (SplitDownloadPreprocessor.SplitFile splitApk : splitApkList) {
                    if (splitApk.exists()) {
                        it = it2;
                    } else {
                        it = it2;
                        realTotalBytesNeedToDownload += splitApk.mRealSize;
                    }
                    it2 = it;
                }
                splitInstallSupervisorImpl = this;
            } catch (Throwable th) {
                FileUtil.closeQuietly(processor);
                throw th;
            }
        }
        return new long[]{totalBytesToDownload, realTotalBytesNeedToDownload};
    }

    private void startUserConfirmationActivity(SplitInstallInternalSessionState sessionState, long realTotalBytesNeedToDownload, List<DownloadRequest> requests) {
        Intent intent = new Intent();
        intent.putExtra("sessionId", sessionState.sessionId());
        intent.putParcelableArrayListExtra("downloadRequests", (ArrayList) requests);
        intent.putExtra("realTotalBytesNeedToDownload", realTotalBytesNeedToDownload);
        intent.putStringArrayListExtra("moduleNames", (ArrayList) sessionState.moduleNames());
        intent.setClass(this.mAppContext, this.mObtainUserConfirmationActivityClass);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mAppContext, 0, intent, 201326592);
        sessionState.setUserConfirmationIntent(pendingIntent);
        this.mSessionManager.changeSessionState(sessionState.sessionId(), 8);
        this.mSessionManager.emitSessionState(sessionState);
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void deferredInstall(List<Bundle> moduleNames, SplitInstallSupervisor.Callback callback) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void deferredUninstall(List<Bundle> moduleNames, SplitInstallSupervisor.Callback callback) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void cancelInstall(int sessionId, SplitInstallSupervisor.Callback callback) {
        if (callback == null) {
            return;
        }
        SplitLog.i(TAG, "start to cancel session id %d installation", Integer.valueOf(sessionId));
        SplitInstallInternalSessionState sessionState = this.mSessionManager.getSessionState(sessionId);
        boolean ret = false;
        if (sessionState == null) {
            SplitLog.i(TAG, "Session id is not found!", new Object[0]);
            callback.onError(bundleErrorCode(-4));
            return;
        }
        if (sessionState.status() == 1 || sessionState.status() == 2) {
            Downloader downloader = this.mUserDownloader;
            if (downloader != null && downloader.cancelDownloadSync(sessionId)) {
                ret = true;
            }
            SplitLog.d(TAG, "task[%d] is cancel: %b", Integer.valueOf(sessionId), Boolean.valueOf(ret));
            if (ret) {
                callback.onCancelInstall(sessionId, null);
                return;
            } else {
                callback.onError(bundleErrorCode(-3));
                return;
            }
        }
        callback.onError(bundleErrorCode(-3));
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void getSessionState(int sessionId, SplitInstallSupervisor.Callback callback) {
        if (callback == null) {
            return;
        }
        SplitInstallInternalSessionState sessionStateVariant = this.mSessionManager.getSessionState(sessionId);
        if (sessionStateVariant == null) {
            callback.onError(bundleErrorCode(-4));
        } else {
            callback.onGetSession(sessionId, SplitInstallInternalSessionState.transform2Bundle(sessionStateVariant));
        }
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public void getSessionStates(SplitInstallSupervisor.Callback callback) {
        if (callback == null) {
            return;
        }
        List<SplitInstallInternalSessionState> sessionStateVariantList = this.mSessionManager.getSessionStates();
        if (sessionStateVariantList.isEmpty()) {
            callback.onGetSessionStates(Collections.emptyList());
            return;
        }
        List<Bundle> sessionStates = new ArrayList<>(0);
        for (SplitInstallInternalSessionState sessionStateVariant : sessionStateVariantList) {
            sessionStates.add(SplitInstallInternalSessionState.transform2Bundle(sessionStateVariant));
        }
        callback.onGetSessionStates(sessionStates);
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public boolean continueInstallWithUserConfirmation(int sessionId) {
        SplitInstallInternalSessionState sessionState = this.mSessionManager.getSessionState(sessionId);
        if (this.mUserDownloader != null && sessionState != null) {
            StartDownloadCallback downloadCallback = new StartDownloadCallback(this.mInstaller, sessionId, this.mSessionManager, sessionState.selectNeedInstalledSplits());
            this.mSessionManager.changeSessionState(sessionId, 1);
            this.mSessionManager.emitSessionState(sessionState);
            this.mUserDownloader.startDownload(sessionState.sessionId(), sessionState.downloadRequests(), downloadCallback);
            return true;
        }
        return false;
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor
    public boolean cancelInstallWithoutUserConfirmation(int sessionId) {
        SplitInstallInternalSessionState sessionState = this.mSessionManager.getSessionState(sessionId);
        if (sessionState != null) {
            this.mSessionManager.changeSessionState(sessionState.sessionId(), 7);
            this.mSessionManager.emitSessionState(sessionState);
            return true;
        }
        return false;
    }
}
