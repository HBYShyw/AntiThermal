package com.oplus.oms.split.splitload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.LoadListener;
import com.oplus.oms.split.splitload.listener.OnSplitLoadListener;
import com.oplus.oms.split.splitreport.SplitReporterConstant;
import com.oplus.oms.split.splitreport.SplitReporterHelper;
import com.oplus.oms.split.splitreport.SplitReporterManager;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SplitBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SplitBroadcastReceiver";

    public static void broadcast(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }
        String action = intent.getAction();
        if (SplitConstants.MAIN_PROCESS.equals(action)) {
            processIntent(intent, action);
        } else {
            context.sendBroadcast(intent);
        }
    }

    private static void processIntent(Intent intent, String action) {
        int loadType = intent.getIntExtra(SplitConstants.KEY_LOAD_TYPE, 0);
        SplitLog.d(TAG, "processIntent - type = " + loadType, new Object[0]);
        switch (loadType) {
            case 0:
                processLoadIntent(intent, action);
                return;
            case 1:
                unloadSplit(intent);
                return;
            default:
                return;
        }
    }

    private static void processLoadIntent(Intent intent, String action) {
        Bundle bundle = intent.getBundleExtra(SplitConstants.KEY_BUNDLE_LISTENER);
        if (bundle == null) {
            SplitLog.w(TAG, "processLoadIntent bundle is null, action: %s", action);
            return;
        }
        List<Intent> intents = bundle.getParcelableArrayList(SplitConstants.INTENTS);
        IBinder binder = bundle.getBinder(SplitConstants.KEY_LISTENER);
        if (intents == null || binder == null) {
            SplitLog.w(TAG, "processLoadIntent binder is null, action: %s", action);
        } else {
            LoadListener listener = LoadListener.Stub.asInterface(binder);
            loadIntent(action, intents, listener);
        }
    }

    private static void unloadSplit(Intent intent) {
        if (intent == null) {
            return;
        }
        String splitName = intent.getStringExtra(SplitConstants.KEY_SPLIT_NAME);
        if (SplitLoadManagerImpl.hasInstance()) {
            SplitReporterInfo reporterInfo = new SplitReporterInfo();
            reporterInfo.setActionStartTime(System.currentTimeMillis());
            SplitReporterManager.install(reporterInfo);
            SplitLoadManagerImpl.getInstance().unloadSplit(splitName);
            return;
        }
        SplitLog.d(TAG, "unloadSplit failed", new Object[0]);
        SplitReporterInfo reporterInfo2 = new SplitReporterInfo();
        reporterInfo2.setName(splitName);
        reporterInfo2.setResultCode(-51);
        SplitReporterHelper.reporter(SplitReporterConstant.UNLOAD_TAG, reporterInfo2);
    }

    private static void loadIntent(final String action, List<Intent> intents, final LoadListener listener) {
        if (TextUtils.isEmpty(action) || intents == null || intents.isEmpty() || listener == null) {
            SplitLog.w(TAG, "loadIntent parameter error", new Object[0]);
            return;
        }
        List<Intent> intentList = new ArrayList<>();
        for (Intent intent : intents) {
            String splitName = intent.getStringExtra(SplitConstants.KEY_SPLIT_NAME);
            if (SplitLoadManagerImpl.getInstance().getLoadedSplitNames().contains(splitName)) {
                SplitLog.i(TAG, "loadIntent split already loaded, split: %s", splitName);
            } else {
                intentList.add(intent);
            }
        }
        if (intentList.isEmpty()) {
            processLoadedCallback(listener, action, true, -1);
        } else {
            SplitLoadManagerImpl.getInstance().loadNow(intentList, new OnSplitLoadListener() { // from class: com.oplus.oms.split.splitload.SplitBroadcastReceiver.1
                @Override // com.oplus.oms.split.splitload.listener.OnSplitLoadListener
                public void onCompleted() {
                    SplitLog.i(SplitBroadcastReceiver.TAG, "onCompleted", new Object[0]);
                    SplitBroadcastReceiver.processLoadedCallback(LoadListener.this, action, true, -1);
                }

                @Override // com.oplus.oms.split.splitload.listener.OnSplitLoadListener
                public void onFailed(int errorCode) {
                    SplitLog.i(SplitBroadcastReceiver.TAG, "onFailed", new Object[0]);
                    SplitBroadcastReceiver.processLoadedCallback(LoadListener.this, action, false, errorCode);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processLoadedCallback(LoadListener listener, String action, boolean isSuccess, int errorCode) {
        try {
            listener.loadStatus(action, isSuccess, errorCode);
        } catch (RemoteException e) {
            SplitLog.e(TAG, "call back error", new Object[0]);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            SplitLog.w(TAG, "onReceive error", new Object[0]);
        } else {
            String action = intent.getAction();
            processIntent(intent, action);
        }
    }
}
