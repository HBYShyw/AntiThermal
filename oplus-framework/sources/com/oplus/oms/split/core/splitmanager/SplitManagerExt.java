package com.oplus.oms.split.core.splitmanager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.ArraySet;
import com.oplus.oms.split.common.NetworkUtil;
import com.oplus.oms.split.common.ProcessInfoData;
import com.oplus.oms.split.common.ProcessUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitInfoData;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.common.SplitProcessUtils;
import com.oplus.oms.split.core.ISplitManagerExt;
import com.oplus.oms.split.splitdownload.DownloadRequest;
import com.oplus.oms.split.splitdownload.DownloadUtil;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import com.oplus.oms.split.splitload.SplitApplicationLoaders;
import com.oplus.oms.split.splitload.SplitBroadcastReceiver;
import com.oplus.oms.split.splitload.SplitLoadManager;
import com.oplus.oms.split.splitload.SplitLoadManagerImpl;
import com.oplus.oms.split.splitload.listener.OnSplitLoadListener;
import com.oplus.oms.split.splitrequest.SplitInstallUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class SplitManagerExt implements ISplitManagerExt {
    private static final String TAG = "SplitManagerExt";

    private SplitManagerExt() {
    }

    public static ISplitManagerExt get() {
        return Holder.sInstance;
    }

    @Override // com.oplus.oms.split.core.ISplitManagerExt
    public void setNetworkStrategy(int netWorkingType) {
        NetworkUtil.setNetWorkStrategy(netWorkingType);
    }

    @Override // com.oplus.oms.split.core.ISplitManagerExt
    public int getSplitVersionCode(Context context, String splitName) {
        return SplitInstallUtil.getSplitInstallVersion(context, splitName);
    }

    @Override // com.oplus.oms.split.core.ISplitManagerExt
    public boolean scheduledDownload(Context context, List<String> splitNames) {
        if (context == null || splitNames == null || splitNames.size() == 0) {
            SplitLog.w(TAG, "scheduledDownload context is null or splitNames is null", new Object[0]);
            return false;
        }
        List<DownloadRequest> downloadRequests = DownloadUtil.buildDownloadRequests(context, splitNames);
        if (downloadRequests == null || downloadRequests.size() == 0) {
            SplitLog.w(TAG, "There are no plugins that need to be downloaded from the cloud", new Object[0]);
            return false;
        }
        ISplitUpdateManager updateManager = OMSInitializer.get().getSplitConfiguration().getUpdateManager();
        if (updateManager == null) {
            SplitLog.w(TAG, "the downloader is null. it can not execute scheduledDownload", new Object[0]);
            return false;
        }
        return updateManager.queryVersionFromCloud(context, downloadRequests, true);
    }

    @Override // com.oplus.oms.split.core.ISplitManagerExt
    public void unloadSplit(Context context, String splitName) {
        if (context == null || splitName == null) {
            SplitLog.w(TAG, "unloadSplit context is null or splitNames is null", new Object[0]);
            return;
        }
        List<ProcessInfoData> subProcess = SplitProcessUtils.getSubProcessInfoData(splitName);
        if (subProcess != null && !subProcess.isEmpty()) {
            Set<String> processNames = new ArraySet<>(subProcess.size());
            for (ProcessInfoData pi : subProcess) {
                processNames.add(pi.getProcessName());
            }
            ProcessUtil.killProcess(context, processNames);
        }
        ProcessInfoData mainProcess = SplitProcessUtils.getMainProcessInfoData(splitName);
        String action = null;
        if (mainProcess == null) {
            action = SplitConstants.MAIN_PROCESS;
        } else if (ProcessUtil.isProcessAlive(context, mainProcess.getProcessName())) {
            action = mainProcess.getActionName();
        }
        if (TextUtils.isEmpty(action)) {
            SplitLog.w(TAG, "unloadSplit action is null, split: %s", splitName);
            return;
        }
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(context.getPackageName());
        intent.putExtra(SplitConstants.KEY_SPLIT_NAME, splitName);
        intent.putExtra(SplitConstants.KEY_LOAD_TYPE, 1);
        SplitBroadcastReceiver.broadcast(context, intent);
    }

    @Override // com.oplus.oms.split.core.ISplitManagerExt
    public void loadNow(Application app, Set<SplitInfoData> splitInfoDataSet) {
        if (!SplitLoadManagerImpl.hasInstance()) {
            SplitLog.w(TAG, "SplitLoadManagerImpl has not initialization", new Object[0]);
            return;
        }
        SplitLoadManager splitLoadManager = SplitLoadManagerImpl.getInstance();
        if (splitLoadManager == null) {
            SplitLog.w(TAG, "splitLoadManager null", new Object[0]);
            return;
        }
        List<Intent> splitFileIntents = new ArrayList<>();
        for (SplitInfoData splitInfoData : splitInfoDataSet) {
            Intent intent = SplitLoadManagerImpl.getInstalledSplitIntent(app, splitInfoData.getSplitName());
            if (intent != null) {
                SplitLog.i(TAG, splitInfoData.getSplitName() + " need to loadNow", new Object[0]);
                splitFileIntents.add(intent);
            }
        }
        if (splitFileIntents.isEmpty()) {
            SplitLog.i(TAG, "all splits has loaded", new Object[0]);
        } else {
            splitLoadManager.loadNow(splitFileIntents, new OnSplitLoadListener() { // from class: com.oplus.oms.split.core.splitmanager.SplitManagerExt.1
                @Override // com.oplus.oms.split.splitload.listener.OnSplitLoadListener
                public void onCompleted() {
                    SplitLog.i(SplitManagerExt.TAG, "onCompleted", new Object[0]);
                }

                @Override // com.oplus.oms.split.splitload.listener.OnSplitLoadListener
                public void onFailed(int errorCode) {
                    SplitLog.w(SplitManagerExt.TAG, "onFailed " + errorCode, new Object[0]);
                }
            });
        }
    }

    @Override // com.oplus.oms.split.core.ISplitManagerExt
    public ClassLoader getSplitClassLoader(String splitName) {
        return SplitApplicationLoaders.getInstance().getClassLoader(splitName);
    }

    /* loaded from: classes.dex */
    private static class Holder {
        static final SplitManagerExt sInstance = new SplitManagerExt();

        private Holder() {
        }
    }
}
