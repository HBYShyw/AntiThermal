package com.oplus.oms.split.core.splitmanager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import com.oplus.oms.split.common.NetworkUtil;
import com.oplus.oms.split.common.ProcessUtil;
import com.oplus.oms.split.common.ReflectUtil;
import com.oplus.oms.split.common.SplitInfoData;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.common.SplitProcessUtils;
import com.oplus.oms.split.core.IOMSInitializer;
import com.oplus.oms.split.core.SampleLogger;
import com.oplus.oms.split.core.SplitConfiguration;
import com.oplus.oms.split.core.splitcompat.OplusSplitCompat;
import com.oplus.oms.split.splitdownload.Downloader;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import com.oplus.oms.split.splitinstall.SplitApkLoadStrategy;
import com.oplus.oms.split.splitinstall.SplitInstallSupervisorImpl;
import com.oplus.oms.split.splitinstall.VersionQuery;
import com.oplus.oms.split.splitload.SplitLoadManagerImpl;
import com.oplus.oms.split.splitload.SplitLoadStrategy;
import com.oplus.oms.split.splitreport.OmsSplitReporter;
import com.oplus.oms.split.splitreport.SplitReporterManager;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class OMSInitializer implements IOMSInitializer {
    private static final String TAG = "OMSInitializer";
    private Class<? extends Activity> mConfirmActivityName;
    private Downloader mDownloader;
    private boolean mOnApplicationCreate;
    private boolean mOnAttachBaseContextCheck;
    private List<String> mProcessNameList;
    private boolean mQueryStartUp;
    private SplitConfiguration mSplitConfiguration;
    private Set<SplitInfoData> mSplitList;
    private ISplitUpdateManager mUpdateManager;

    private OMSInitializer() {
        this.mSplitList = new HashSet();
        this.mOnAttachBaseContextCheck = false;
        this.mOnApplicationCreate = false;
    }

    public static IOMSInitializer get() {
        return Holder.sInstance;
    }

    private void init(Context base, SplitConfiguration configuration, String processName, List<String> installProcessList) {
        SplitLoadManagerImpl.install(base, configuration.getSplitLoadMode(), processName, installProcessList);
        SplitLoadManagerImpl.getInstance().clear();
        SplitLoadManagerImpl.getInstance().injectPathClassloader();
        SplitPathManager.install(base);
        OplusSplitCompat.install(base);
    }

    @Override // com.oplus.oms.split.core.IOMSInitializer
    public void onAttachBaseContext(Context base, SplitConfiguration config) {
        if (this.mOnAttachBaseContextCheck) {
            return;
        }
        this.mOnAttachBaseContextCheck = true;
        this.mSplitConfiguration = config;
        ReflectUtil.setAppClassLoader(base.getClassLoader());
        SplitConfiguration configuration = config == null ? SplitConfiguration.newBuilder().build() : config;
        SplitLog.setSplitLogImp(new SampleLogger(true));
        SplitReporterManager.install(new OmsSplitReporter(base));
        String currentProcessName = ProcessUtil.getProcessName(base);
        String mainProcess = base.getPackageName();
        List<String> workProcesses = configuration.getWorkProcesses();
        this.mProcessNameList = workProcesses;
        if (workProcesses == null) {
            this.mProcessNameList = new ArrayList();
        }
        this.mProcessNameList.add(mainProcess);
        SplitLog.d(TAG, "onAttachBaseContext - currentProcess = " + currentProcessName + " , workProcessList = " + this.mProcessNameList.toString(), new Object[0]);
        if (this.mProcessNameList.contains(currentProcessName)) {
            this.mDownloader = configuration.getDownloader();
            this.mUpdateManager = configuration.getUpdateManager();
            this.mQueryStartUp = configuration.getQueryStartUp();
            this.mConfirmActivityName = configuration.getConfirmActivityName();
            SplitLoadStrategy.setMultiSplitLoadStrategy(configuration.getSplitLoadMode());
            NetworkUtil.setNetWorkStrategy(configuration.getNetWorkingType());
            NetworkUtil.setNetworkUpdateTime(configuration.getUpdateTimeByHours());
            SplitApkLoadStrategy.getInstatnce().setLocalFirstStrategyStatus(configuration.getLocalFirst());
            SplitApkLoadStrategy.getInstatnce().setCustomProvider(configuration.getCustomProvider());
            init(base, configuration, currentProcessName, this.mProcessNameList);
            return;
        }
        Set<SplitInfoData> splitInfoDataSet = SplitProcessUtils.getSplitInfoDataFromProcess(currentProcessName);
        if (splitInfoDataSet == null || splitInfoDataSet.isEmpty()) {
            SplitLog.w(TAG, "the current process: + " + currentProcessName + " get the splitInfoData is empty", new Object[0]);
        } else {
            init(base, configuration, currentProcessName, this.mProcessNameList);
            this.mSplitList = splitInfoDataSet;
        }
    }

    @Override // com.oplus.oms.split.core.IOMSInitializer
    public void onApplicationCreate(Application app) {
        if (this.mOnApplicationCreate) {
            SplitLog.w(TAG, "onApplicationCreate already called, ignore", new Object[0]);
            return;
        }
        this.mOnApplicationCreate = true;
        String currentProcessName = ProcessUtil.getProcessName(app);
        if (this.mProcessNameList == null) {
            this.mProcessNameList = new ArrayList();
        }
        if (this.mProcessNameList.contains(currentProcessName)) {
            SplitInstallSupervisorImpl.install(app, this.mDownloader, this.mUpdateManager, this.mConfirmActivityName, true);
            if (this.mQueryStartUp) {
                VersionQuery.getInstance().queryAsync(app, this.mUpdateManager, null);
            }
        }
    }

    @Override // com.oplus.oms.split.core.IOMSInitializer
    public void onApplicationGetResources(Resources res) {
        if (SplitLoadManagerImpl.hasInstance() && res != null) {
            SplitLoadManagerImpl.getInstance().getResources(res);
        }
    }

    @Override // com.oplus.oms.split.core.IOMSInitializer
    public SplitConfiguration getSplitConfiguration() {
        return this.mSplitConfiguration;
    }

    /* loaded from: classes.dex */
    private static class Holder {
        static final OMSInitializer sInstance = new OMSInitializer();

        private Holder() {
        }
    }
}
