package com.oplus.oms.split.core.splitinstall;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.ArraySet;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.listener.OplusStateUpdatedListener;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;
import com.oplus.oms.split.core.tasks.OplusTask;
import com.oplus.oms.split.core.tasks.OplusTaskWrapper;
import com.oplus.oms.split.splitinstall.SplitInstallSupervisorImpl;
import com.oplus.oms.split.splitinstall.SplitInstallerExecutor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes.dex */
class OplusSplitInstallManagerImpl<S extends OplusSplitInstallSessionState> implements OplusSplitInstallManager<S> {
    private static final String KEY_FUSED_MODULES = "shadow.bundletool.com.android.dynamic.apk.fused.modules";
    private static final String TAG = "SplitInstallManagerImpl";
    private final Context mContext;
    private final SplitInstallListenerRegistry<S> mRegistry;
    private final OplusSplitInstallSessionStateFactory<S> mStateFactory;

    public OplusSplitInstallManagerImpl(Context context, OplusSplitInstallSessionStateFactory<S> stateFactory) {
        this.mContext = context;
        this.mStateFactory = stateFactory;
        SplitInstallListenerRegistry<S> splitInstallListenerRegistry = new SplitInstallListenerRegistry<>(context, stateFactory);
        this.mRegistry = splitInstallListenerRegistry;
        SplitInstallSupervisorImpl.getSplitInstallSupervisor().setInstallListenerRegister(splitInstallListenerRegistry);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public void registerListener(OplusStateUpdatedListener<S> oplusStateUpdatedListener) {
        getRegistry().registerListener(oplusStateUpdatedListener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public void unregisterListener(OplusStateUpdatedListener<S> oplusStateUpdatedListener) {
        getRegistry().unregisterListener(oplusStateUpdatedListener);
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<Integer> startInstall(OplusSplitInstallRequest request) {
        return startInstall(request.getModuleNames());
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public boolean startConfirmationDialogForResult(S sessionState, Activity activity, int requestCode) throws IntentSender.SendIntentException {
        if (sessionState.status() == 8 && sessionState.resolutionIntent() != null) {
            activity.startIntentSenderForResult(sessionState.resolutionIntent().getIntentSender(), requestCode, null, 0, 0, 0);
            return true;
        }
        return false;
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<Void> cancelInstall(int sessionId) {
        SplitLog.i(TAG, "cancelInstall " + sessionId, new Object[0]);
        OplusTaskWrapper<Void> taskWrapper = new OplusTaskWrapper<>();
        SplitInstallerExecutor.getCopyExecutor().execute(new CancelInstallTask(taskWrapper, sessionId));
        return taskWrapper.getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<S> getSessionState(int sessionId) {
        SplitLog.i(TAG, "getSessionState " + sessionId, new Object[0]);
        OplusTaskWrapper<S> taskWrapper = new OplusTaskWrapper<>();
        SplitInstallerExecutor.getCopyExecutor().execute(new GetSessionStateTask(this.mStateFactory, taskWrapper, sessionId));
        return taskWrapper.getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<List<S>> getSessionStates() {
        SplitLog.i(TAG, "getSessionState ", new Object[0]);
        OplusTaskWrapper<List<S>> taskWrapper = new OplusTaskWrapper<>();
        SplitInstallerExecutor.getCopyExecutor().execute(new GetSessionStatesTask(this.mStateFactory, taskWrapper));
        return taskWrapper.getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<Void> deferredInstall(List<String> moduleNames) {
        return new OplusTaskWrapper().getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<Void> deferredLanguageInstall(List<Locale> languages) {
        return new OplusTaskWrapper().getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<Void> deferredLanguageUninstall(List<Locale> languages) {
        return new OplusTaskWrapper().getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public OplusTask<Void> deferredUninstall(List<String> moduleNames) {
        return new OplusTaskWrapper().getTask();
    }

    OplusTask<Integer> startInstall(List<String> moduleNames) {
        SplitLog.i(TAG, "startInstall " + moduleNames, new Object[0]);
        OplusTaskWrapper<Integer> taskWrapper = new OplusTaskWrapper<>();
        SplitInstallerExecutor.getCopyExecutor().execute(new StartInstallTask(taskWrapper, moduleNames));
        return taskWrapper.getTask();
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public Set<String> getInstalledModules() {
        Set<String> installed = new ArraySet<>();
        Set<String> installedAAB = getInstalledSplitInstallInfo();
        if (installedAAB != null && !installedAAB.isEmpty()) {
            installed.addAll(installedAAB);
        }
        Set<String> loadedSplit = LoadedSplitFetcherSingleton.get().loadedSplits();
        installed.addAll(loadedSplit);
        return installed;
    }

    @Override // com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager
    public Set<String> getInstalledLanguages() {
        return Collections.emptySet();
    }

    private Set<String> getInstalledSplitInstallInfo() {
        Set<String> installedModules = getFusedModules();
        String[] splitNames = getSplitInstallInfo();
        if (splitNames == null) {
            SplitLog.d(TAG, "No splits are found or app cannot be found in package manager.", new Object[0]);
            return installedModules;
        }
        String splitNamesStr = Arrays.toString(splitNames);
        SplitLog.d(TAG, splitNamesStr.length() != 0 ? "Split names are: ".concat(splitNamesStr) : "Split names are: ", new Object[0]);
        for (String splitName : splitNames) {
            if (!splitName.startsWith("config.")) {
                installedModules.add(cutSplitName(splitName));
            }
        }
        return installedModules;
    }

    private Set<String> getFusedModules() {
        Set<String> fusedModules = new HashSet<>();
        try {
            ApplicationInfo appInfo = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128);
            if (appInfo.metaData == null) {
                SplitLog.d(TAG, "App has no applicationInfo or metaData", new Object[0]);
            } else {
                String fusedName = appInfo.metaData.getString(KEY_FUSED_MODULES);
                if (fusedName == null || fusedName.isEmpty()) {
                    SplitLog.d(TAG, "App has no fused modules.", new Object[0]);
                } else {
                    Collections.addAll(fusedModules, fusedName.split(",", -1));
                    fusedModules.remove("");
                }
            }
            return fusedModules;
        } catch (PackageManager.NameNotFoundException e) {
            SplitLog.w(TAG, "App is not found in PackageManager", new Object[0]);
            return fusedModules;
        }
    }

    private String[] getSplitInstallInfo() {
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.splitNames;
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            SplitLog.d(TAG, "App is not found in PackageManager", new Object[0]);
            return null;
        }
    }

    private String cutSplitName(String splitName) {
        return splitName.split("\\.config\\.")[0];
    }

    SplitInstallListenerRegistry<S> getRegistry() {
        return this.mRegistry;
    }
}
