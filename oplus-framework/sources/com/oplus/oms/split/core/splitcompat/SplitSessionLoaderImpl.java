package com.oplus.oms.split.core.splitcompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.oplus.oms.split.common.ProcessInfoData;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.common.SplitProcessUtils;
import com.oplus.oms.split.core.splitinstall.SplitSessionLoader;
import com.oplus.oms.split.core.splitinstall.SplitSessionStatusChanger;
import com.oplus.oms.split.splitload.SplitBroadcastReceiver;
import com.oplus.oms.split.splitload.SplitLoadManagerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
class SplitSessionLoaderImpl implements SplitSessionLoader {
    private static final String TAG = "SplitSessionLoaderImpl";
    private static final long WAIT_TIME_OUT = 3000;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitSessionLoaderImpl(Executor executor) {
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState, java.lang.Object] */
    @Override // com.oplus.oms.split.core.splitinstall.SplitSessionLoader
    public void load(Context context, List<Intent> splitFileIntents, SplitSessionStatusChanger<?> changer) {
        ?? sessionState = changer.getSessionState();
        SplitLog.i(TAG, "session load state: %s", sessionState);
        if (!OplusSplitCompat.hasInstance()) {
            SplitLog.e(TAG, "session load SplitCompat not installed. sessionId: %d", Integer.valueOf(sessionState.sessionId()));
            throw new IllegalStateException("Ingestion should only be called in SplitCompat mode.");
        }
        Map<String, ArrayList<Intent>> loadMap = new HashMap<>();
        Map<String, String> processMap = new HashMap<>();
        Set<String> loadedSplit = SplitLoadManagerImpl.getInstance().getLoadedSplitNames();
        boolean allLoaded = true;
        for (Intent intent : splitFileIntents) {
            String name = intent.getStringExtra(SplitConstants.KEY_SPLIT_NAME);
            if (!skipLoadSplit(name, loadedSplit)) {
                allLoaded = false;
                Intent installed = SplitLoadManagerImpl.getInstalledSplitIntent(context, name);
                if (installed == null) {
                    SplitLog.w(TAG, "session load get null installed intent. sessionId: %d, split: %s", Integer.valueOf(sessionState.sessionId()), name);
                    changer.changeStatus(6, -2);
                    return;
                }
                setIntentToProcessMap(name, installed, loadMap, processMap);
            }
        }
        if (allLoaded) {
            SplitLog.i(TAG, "session load all module loaded, ignore load. sessionId: %d", Integer.valueOf(sessionState.sessionId()));
            changer.changeStatus(5);
        } else if (loadMap.isEmpty()) {
            SplitLog.i(TAG, "session load load map is empty. sessionId: %d", Integer.valueOf(sessionState.sessionId()));
            changer.changeStatus(6, -2);
        } else {
            processSplitIntent(context, loadMap, processMap, changer);
        }
    }

    private boolean isSplitProcessRunning(Context context, String processName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (processInfo.processName.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    private boolean skipLoadSplit(String splitName, Set<String> loadedSplits) {
        if (TextUtils.isEmpty(splitName)) {
            return true;
        }
        if (loadedSplits.contains(splitName)) {
            SplitLog.i(TAG, "session load split already loaded, ignore. split: %s", splitName);
            return true;
        }
        return false;
    }

    private void setIntentToProcessMap(String splitName, Intent intent, Map<String, ArrayList<Intent>> loadMap, Map<String, String> processMap) {
        ProcessInfoData processMain = SplitProcessUtils.getMainProcessInfoData(splitName);
        if (processMain == null) {
            ArrayList<Intent> mainProcess = loadMap.get(SplitConstants.MAIN_PROCESS);
            if (mainProcess == null) {
                ArrayList<Intent> mainProcess2 = new ArrayList<>();
                mainProcess2.add(intent);
                loadMap.put(SplitConstants.MAIN_PROCESS, mainProcess2);
                return;
            }
            mainProcess.add(intent);
            return;
        }
        String action = processMain.getActionName();
        ArrayList<Intent> intents = loadMap.get(action);
        if (intents == null) {
            ArrayList<Intent> intents2 = new ArrayList<>();
            intents2.add(intent);
            loadMap.put(action, intents2);
        } else {
            intents.add(intent);
        }
        if (!processMap.containsKey(action)) {
            processMap.put(action, processMain.getProcessName());
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState] */
    private void processSplitIntent(Context context, Map<String, ArrayList<Intent>> loadMap, Map<String, String> processMap, SplitSessionStatusChanger<?> changer) {
        ?? sessionState = changer.getSessionState();
        int sendSize = 0;
        for (String action : loadMap.keySet()) {
            String processName = processMap.get(action);
            if (processMap.containsKey(action) && !isSplitProcessRunning(context, processName)) {
                SplitLog.i(TAG, "session load process not running, ignore. sessionId: %d, process: %s", Integer.valueOf(sessionState.sessionId()), processName);
            } else {
                SplitLog.i(TAG, "session load broadcast sessionId: %d, action: %s, process: %s", Integer.valueOf(sessionState.sessionId()), action, processName);
                sendSize++;
                Bundle bundle = new Bundle();
                bundle.putBinder(SplitConstants.KEY_LISTENER, changer.getLoaderListenerBinder());
                bundle.putParcelableArrayList(SplitConstants.INTENTS, loadMap.get(action));
                Intent intent = new Intent();
                intent.setPackage(context.getPackageName());
                intent.setAction(action);
                intent.putExtra(SplitConstants.KEY_BUNDLE_LISTENER, bundle);
                intent.putExtra(SplitConstants.KEY_LOAD_TYPE, 0);
                changer.putAction(action);
                SplitBroadcastReceiver.broadcast(context, intent);
            }
        }
        if (sendSize == 0) {
            SplitLog.i(TAG, "session load success, sessionId: %d", Integer.valueOf(sessionState.sessionId()));
            changer.changeStatus(5);
        } else {
            changer.postTimeOutMessage(sessionState);
        }
    }
}
