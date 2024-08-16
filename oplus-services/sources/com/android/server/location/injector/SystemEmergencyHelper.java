package com.android.server.location.injector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.server.FgThread;
import com.android.server.location.LocationManagerService;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemEmergencyHelper extends EmergencyHelper {
    private final Context mContext;
    boolean mIsInEmergencyCall;
    TelephonyManager mTelephonyManager;
    private final EmergencyCallTelephonyCallback mEmergencyCallTelephonyCallback = new EmergencyCallTelephonyCallback();
    long mEmergencyCallEndRealtimeMs = Long.MIN_VALUE;

    public SystemEmergencyHelper(Context context) {
        this.mContext = context;
    }

    public synchronized void onSystemReady() {
        if (this.mTelephonyManager != null) {
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        Objects.requireNonNull(telephonyManager);
        TelephonyManager telephonyManager2 = telephonyManager;
        this.mTelephonyManager = telephonyManager;
        telephonyManager.registerTelephonyCallback(FgThread.getExecutor(), this.mEmergencyCallTelephonyCallback);
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.location.injector.SystemEmergencyHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.NEW_OUTGOING_CALL".equals(intent.getAction())) {
                    synchronized (SystemEmergencyHelper.this) {
                        try {
                            SystemEmergencyHelper systemEmergencyHelper = SystemEmergencyHelper.this;
                            systemEmergencyHelper.mIsInEmergencyCall = systemEmergencyHelper.mTelephonyManager.isEmergencyNumber(intent.getStringExtra("android.intent.extra.PHONE_NUMBER"));
                        } catch (IllegalStateException e) {
                            Log.w(LocationManagerService.TAG, "Failed to call TelephonyManager.isEmergencyNumber().", e);
                        }
                    }
                }
            }
        }, new IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0033, code lost:
    
        if (r7.mTelephonyManager.isInEmergencySmsMode() != false) goto L21;
     */
    @Override // com.android.server.location.injector.EmergencyHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized boolean isInEmergency(long j) {
        boolean z = false;
        if (this.mTelephonyManager == null) {
            return false;
        }
        boolean z2 = this.mEmergencyCallEndRealtimeMs != Long.MIN_VALUE && SystemClock.elapsedRealtime() - this.mEmergencyCallEndRealtimeMs < j;
        if (!this.mIsInEmergencyCall && !z2 && !this.mTelephonyManager.getEmergencyCallbackMode()) {
        }
        z = true;
        return z;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class EmergencyCallTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        EmergencyCallTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            if (i == 0) {
                synchronized (SystemEmergencyHelper.this) {
                    SystemEmergencyHelper systemEmergencyHelper = SystemEmergencyHelper.this;
                    if (systemEmergencyHelper.mIsInEmergencyCall) {
                        systemEmergencyHelper.mEmergencyCallEndRealtimeMs = SystemClock.elapsedRealtime();
                        SystemEmergencyHelper.this.mIsInEmergencyCall = false;
                    }
                }
            }
        }
    }
}
