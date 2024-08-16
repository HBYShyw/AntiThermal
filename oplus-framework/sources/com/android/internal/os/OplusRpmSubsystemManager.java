package com.android.internal.os;

import android.content.Context;
import android.os.Handler;
import android.os.PowerStateSubsystemSleepState;
import android.os.RpmAidlManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusRpmSubsystemManager {
    private static final long MILLISECONDS_IN_MINUTE = 60000;
    private static final String TAG = "OplusRpmSubsystemManager";
    private static boolean sDebug = false;
    private Context mContext;
    private Handler mHandler;
    private int mOplusRpmhAvailableSummary;
    private final double mHundredPercentage = 100.0d;
    private ArrayList<PowerStateSubsystemSleepState> mPowerStateSubsystemSleepStateList = new ArrayList<>();
    private ArrayList<PowerStateSubsystemSleepState> mLastPowerStateSubsystemSleepStateList = new ArrayList<>();
    private long mCurrentElapseRealTime = 0;
    private long mLastUpdatedElapseRealTime = 0;
    private String mLastStepRpmSuspendRatioSummary = null;
    private String mRpmSuspendRatioSummay = null;
    private boolean mIsInitialized = false;

    public OplusRpmSubsystemManager(Context context, Handler handler) {
        this.mContext = null;
        this.mHandler = null;
        this.mContext = context;
        this.mHandler = handler;
    }

    private void init() {
        if (!this.mIsInitialized) {
            Slog.d(TAG, "init...");
            sDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);
            this.mCurrentElapseRealTime = SystemClock.elapsedRealtime();
            try {
                ArrayList<PowerStateSubsystemSleepState> powerStateSubsystemSleepStateList = RpmAidlManager.getInstance().getPowerStateSubsystemSleepStateList();
                this.mPowerStateSubsystemSleepStateList = powerStateSubsystemSleepStateList;
                if (powerStateSubsystemSleepStateList == null) {
                    Slog.d(TAG, "mPowerStateSubsystemSleepStateList == null");
                } else {
                    this.mIsInitialized = true;
                }
            } catch (Exception e) {
                Slog.d(TAG, "init fail");
            }
        }
    }

    public void measureOplusRpmMasterStatsDelta() {
        ArrayList<PowerStateSubsystemSleepState> arrayList;
        this.mLastStepRpmSuspendRatioSummary = this.mRpmSuspendRatioSummay;
        Slog.d(TAG, "measureOplusRpmMasterStatsDelta... ");
        long elapsedRealTimeDelta = this.mCurrentElapseRealTime - this.mLastUpdatedElapseRealTime;
        Slog.d(TAG, "elapsedRealTimeDelta :" + elapsedRealTimeDelta);
        if (elapsedRealTimeDelta <= 0 || (arrayList = this.mPowerStateSubsystemSleepStateList) == null || this.mLastPowerStateSubsystemSleepStateList == null || arrayList.size() != this.mLastPowerStateSubsystemSleepStateList.size()) {
            return;
        }
        StringBuilder summary = new StringBuilder("[");
        for (int i = 0; i < this.mPowerStateSubsystemSleepStateList.size(); i++) {
            PowerStateSubsystemSleepState current = this.mPowerStateSubsystemSleepStateList.get(i);
            PowerStateSubsystemSleepState last = this.mLastPowerStateSubsystemSleepStateList.get(i);
            double ratio = ((current.residencyInMsecSinceBoot - last.residencyInMsecSinceBoot) / elapsedRealTimeDelta) * 100.0d;
            summary.append(String.format(current.name + ":%.2f", Double.valueOf(ratio)));
            if (i != this.mPowerStateSubsystemSleepStateList.size() - 1) {
                summary.append(",");
            }
        }
        summary.append("]");
        this.mRpmSuspendRatioSummay = summary.toString();
        if (sDebug) {
            Slog.d(TAG, "mLastStepRpmSuspendRatioSummary: " + this.mLastStepRpmSuspendRatioSummary);
        }
        Slog.d(TAG, "mRpmSuspendRatioSummay: " + this.mRpmSuspendRatioSummay);
    }

    public void trigger() {
        init();
        this.mLastUpdatedElapseRealTime = this.mCurrentElapseRealTime;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mCurrentElapseRealTime = elapsedRealtime;
        long elapsedRealTimeDelta = elapsedRealtime - this.mLastUpdatedElapseRealTime;
        Slog.d(TAG, "elapsedRealTimeDelta :" + elapsedRealTimeDelta);
        if (elapsedRealTimeDelta >= MILLISECONDS_IN_MINUTE && this.mIsInitialized && getPowerStateSubsystemSleepStateList() != null) {
            measureOplusRpmMasterStatsDelta();
        }
    }

    public ArrayList<PowerStateSubsystemSleepState> getPowerStateSubsystemSleepStateList() {
        ArrayList<PowerStateSubsystemSleepState> arrayList;
        ArrayList<PowerStateSubsystemSleepState> arrayList2 = this.mPowerStateSubsystemSleepStateList;
        if (arrayList2 == null || (arrayList = this.mLastPowerStateSubsystemSleepStateList) == null) {
            return null;
        }
        copySourceToDestinationDeeply(arrayList2, arrayList);
        try {
            this.mPowerStateSubsystemSleepStateList = RpmAidlManager.getInstance().getPowerStateSubsystemSleepStateList();
        } catch (Exception e) {
            Log.d(TAG, "getPowerStateList fail");
        }
        if (sDebug) {
            Slog.d(TAG, "mLastPowerStateSubsystemSleepStateList: " + this.mLastPowerStateSubsystemSleepStateList);
            Slog.d(TAG, "mPowerStateSubsystemSleepStateList: " + this.mPowerStateSubsystemSleepStateList);
        }
        return this.mPowerStateSubsystemSleepStateList;
    }

    public void copySourceToDestinationDeeply(ArrayList<PowerStateSubsystemSleepState> source, ArrayList<PowerStateSubsystemSleepState> destination) {
        if (source == null || destination == null) {
            return;
        }
        destination.clear();
        Iterator<PowerStateSubsystemSleepState> it = source.iterator();
        while (it.hasNext()) {
            PowerStateSubsystemSleepState powerStateSubsystemSleepState = it.next();
            PowerStateSubsystemSleepState state = new PowerStateSubsystemSleepState();
            state.name = powerStateSubsystemSleepState.name;
            state.version = powerStateSubsystemSleepState.version;
            state.residencyInMsecSinceBoot = powerStateSubsystemSleepState.residencyInMsecSinceBoot;
            state.totalTransitions = powerStateSubsystemSleepState.totalTransitions;
            state.lastEntryTimestampMs = powerStateSubsystemSleepState.lastEntryTimestampMs;
            state.supportedOnlyInSuspend = powerStateSubsystemSleepState.supportedOnlyInSuspend;
            destination.add(state);
        }
    }

    public void onBatteryDrained() {
        Slog.d(TAG, "battery drained... ");
        trigger();
    }

    public void onScreenStateChaned(boolean screenOn) {
    }

    public void onBootCompleted() {
        Slog.d(TAG, "onBootCompleted... ");
        scheduleUpdateRpmPath(30000L);
    }

    public String getLastStepRpmSuspendRatioSummary() {
        return this.mLastStepRpmSuspendRatioSummary;
    }

    public void scheduleUpdateRpmPath(long delay) {
    }
}
