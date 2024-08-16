package com.android.server.am;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.RemoteException;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.app.IBatteryStats;
import com.android.internal.util.FrameworkStatsLog;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DataConnectionStats extends BroadcastReceiver {
    private static final boolean DEBUG = false;
    private static final String TAG = "DataConnectionStats";
    private final Context mContext;
    private final Handler mListenerHandler;
    private final PhoneStateListener mPhoneStateListener;
    private ServiceState mServiceState;
    private SignalStrength mSignalStrength;
    private int mSimState = 5;
    private int mDataState = 0;
    private int mNrState = 0;
    private final IBatteryStats mBatteryStats = BatteryStatsService.getService();

    public DataConnectionStats(Context context, Handler handler) {
        this.mContext = context;
        this.mListenerHandler = handler;
        this.mPhoneStateListener = new PhoneStateListenerImpl(new PhoneStateListenerExecutor(handler));
    }

    public void startMonitoring() {
        ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).listen(this.mPhoneStateListener, FrameworkStatsLog.DREAM_UI_EVENT_REPORTED);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this, intentFilter, null, this.mListenerHandler);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            updateSimState(intent);
            notePhoneDataConnectionState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notePhoneDataConnectionState() {
        if (this.mServiceState == null) {
            return;
        }
        int i = this.mSimState;
        boolean z = ((i == 5 || i == 0) || isCdma()) && hasService() && this.mDataState == 2;
        NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, 1);
        int accessNetworkTechnology = networkRegistrationInfo != null ? networkRegistrationInfo.getAccessNetworkTechnology() : 0;
        if (this.mNrState == 3) {
            accessNetworkTechnology = 20;
        }
        try {
            this.mBatteryStats.notePhoneDataConnectionState(accessNetworkTechnology, z, this.mServiceState.getState(), this.mServiceState.getNrFrequencyRange());
        } catch (RemoteException e) {
            Log.w(TAG, "Error noting data connection state", e);
        }
    }

    private void updateSimState(Intent intent) {
        String stringExtra = intent.getStringExtra("ss");
        if ("ABSENT".equals(stringExtra)) {
            this.mSimState = 1;
            return;
        }
        if ("READY".equals(stringExtra)) {
            this.mSimState = 5;
            return;
        }
        if ("LOCKED".equals(stringExtra)) {
            String stringExtra2 = intent.getStringExtra("reason");
            if ("PIN".equals(stringExtra2)) {
                this.mSimState = 2;
                return;
            } else if ("PUK".equals(stringExtra2)) {
                this.mSimState = 3;
                return;
            } else {
                this.mSimState = 4;
                return;
            }
        }
        this.mSimState = 0;
    }

    private boolean isCdma() {
        SignalStrength signalStrength = this.mSignalStrength;
        return (signalStrength == null || signalStrength.isGsm()) ? false : true;
    }

    private boolean hasService() {
        ServiceState serviceState = this.mServiceState;
        return (serviceState == null || serviceState.getState() == 1 || this.mServiceState.getState() == 3) ? false : true;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class PhoneStateListenerExecutor implements Executor {
        private final Handler mHandler;

        PhoneStateListenerExecutor(Handler handler) {
            this.mHandler = handler;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            if (this.mHandler.post(runnable)) {
                return;
            }
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class PhoneStateListenerImpl extends PhoneStateListener {
        PhoneStateListenerImpl(Executor executor) {
            super(executor);
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            DataConnectionStats.this.mSignalStrength = signalStrength;
        }

        @Override // android.telephony.PhoneStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            DataConnectionStats.this.mServiceState = serviceState;
            DataConnectionStats.this.mNrState = serviceState.getNrState();
            DataConnectionStats.this.notePhoneDataConnectionState();
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            DataConnectionStats.this.mDataState = i;
            DataConnectionStats.this.notePhoneDataConnectionState();
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int i) {
            DataConnectionStats.this.notePhoneDataConnectionState();
        }
    }
}
