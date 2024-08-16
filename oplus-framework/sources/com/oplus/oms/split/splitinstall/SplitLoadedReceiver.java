package com.oplus.oms.split.splitinstall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.oplus.oms.split.common.SplitLog;
import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: classes.dex */
public class SplitLoadedReceiver {
    private static final String SPLIT_LOAD_ACTION = "split.load.action";
    private static final String SPLIT_NAME = "split_name";
    private static final String SPLIT_STATUS = "split_status";
    private static final int SPLIT_STATUS_LOADED = 1;
    private static final String TAG = "SplitLoadedReceiver";
    private final HashSet<String> mHashSet;

    private SplitLoadedReceiver() {
        this.mHashSet = new HashSet<>();
    }

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        private static final SplitLoadedReceiver INSTANCE = new SplitLoadedReceiver();

        private SingletonHolder() {
        }
    }

    public static SplitLoadedReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isSplitApkLoaded(String splitName) {
        if (this.mHashSet.isEmpty() || !this.mHashSet.contains(splitName)) {
            return false;
        }
        SplitLog.d(TAG, "split apk has loader", new Object[0]);
        return true;
    }

    public void registerLoaderReceiver(Context context) {
        if (context == null) {
            SplitLog.e(TAG, "registerLoaderReceiver context is null", new Object[0]);
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(SPLIT_LOAD_ACTION);
        StatusReceiver statusReceiver = new StatusReceiver();
        context.registerReceiver(statusReceiver, filter);
    }

    /* loaded from: classes.dex */
    private class StatusReceiver extends BroadcastReceiver {
        private StatusReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> splitNames;
            if (intent == null || (splitNames = intent.getStringArrayListExtra("split_name")) == null) {
                return;
            }
            int status = intent.getIntExtra(SplitLoadedReceiver.SPLIT_STATUS, 1);
            SplitLog.i(SplitLoadedReceiver.TAG, "onReceive splitNames " + splitNames + ",status:" + status, new Object[0]);
            if (status == 1) {
                SplitLoadedReceiver.this.mHashSet.addAll(splitNames);
            }
        }
    }
}
