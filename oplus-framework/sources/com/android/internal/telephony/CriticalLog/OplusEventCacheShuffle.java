package com.android.internal.telephony.CriticalLog;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.OplusManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusEventCacheShuffle {
    private static final int MAX_COUNT = 100;
    private static final String TAG = "OplusEventCacheShuffle";
    private static OplusEventCacheShuffle sInstance = null;
    private HandlerThread mCacheLogThread;
    private IEventCacheCb mCb;
    private Handler mHandler;
    private final BroadcastReceiver mReceiver;
    private Context mContext = ActivityThread.currentApplication().getApplicationContext();
    private final ArrayList<Object> mList = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface IEventCacheCb {
        void eventUpload(Object obj);
    }

    /* loaded from: classes.dex */
    private class CacheLogHandler extends Handler {
        public CacheLogHandler(Looper looper) {
            super(looper);
        }
    }

    private OplusEventCacheShuffle() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.CriticalLog.OplusEventCacheShuffle.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
                    OplusEventCacheShuffle.this.uploadAllMsg("date change");
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        HandlerThread handlerThread = new HandlerThread("OplusCacheLogThread");
        this.mCacheLogThread = handlerThread;
        handlerThread.start();
        this.mHandler = new CacheLogHandler(this.mCacheLogThread.getLooper());
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.DATE_CHANGED");
        this.mContext.registerReceiver(broadcastReceiver, filter);
        this.mCb = new IEventCacheCb() { // from class: com.android.internal.telephony.CriticalLog.OplusEventCacheShuffle.2
            @Override // com.android.internal.telephony.CriticalLog.OplusEventCacheShuffle.IEventCacheCb
            public void eventUpload(Object mObject) {
                OplusCriticalLogInfo mCLInfo = (OplusCriticalLogInfo) mObject;
                if (TextUtils.isEmpty(mCLInfo.mTag)) {
                    mCLInfo.mTag = OplusManager.NETWORK_TAG;
                }
                OplusManager.writeLogToPartition(mCLInfo.mType, mCLInfo.mLog, mCLInfo.mTag, mCLInfo.mIssue, mCLInfo.mDesc);
            }
        };
    }

    public static OplusEventCacheShuffle getInstance() {
        OplusEventCacheShuffle oplusEventCacheShuffle;
        synchronized (OplusEventCacheShuffle.class) {
            if (sInstance == null) {
                sInstance = new OplusEventCacheShuffle();
            }
            oplusEventCacheShuffle = sInstance;
        }
        return oplusEventCacheShuffle;
    }

    public void addEvent(Object o) {
        synchronized (this.mList) {
            this.mList.add(o);
        }
        if (checkUpload()) {
            uploadAllMsg("max count");
        }
    }

    private boolean checkUpload() {
        boolean z;
        synchronized (this.mList) {
            z = this.mList.size() >= 100;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadAllMsg(final String reason) {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.CriticalLog.OplusEventCacheShuffle.3
            @Override // java.lang.Runnable
            public void run() {
                OplusEventCacheShuffle.this.logd("uploadAllMsg for " + reason);
                synchronized (OplusEventCacheShuffle.this.mList) {
                    Collections.shuffle(OplusEventCacheShuffle.this.mList);
                    Iterator it = OplusEventCacheShuffle.this.mList.iterator();
                    while (it.hasNext()) {
                        Object i = it.next();
                        OplusEventCacheShuffle.this.mCb.eventUpload(i);
                    }
                    OplusEventCacheShuffle.this.mList.clear();
                }
            }
        });
    }

    void logd(String s) {
        Log.d(TAG, s);
    }
}
