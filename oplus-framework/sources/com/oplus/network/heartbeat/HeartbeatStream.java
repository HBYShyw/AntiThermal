package com.oplus.network.heartbeat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.net.Socket;

/* loaded from: classes.dex */
public final class HeartbeatStream {
    public static final int HBA_ENUM_ERR_DATA_NETWORK_UNAVAILABLE = 6;
    public static final int HBA_ENUM_ERR_DYNAMIC_CYCLE_UNAVAILABLE = 11;
    public static final int HBA_ENUM_ERR_INVALID_SOCKET = 4;
    public static final int HBA_ENUM_ERR_IPC = 1;
    public static final int HBA_ENUM_ERR_IPV6_NOT_SUPPORT = 2;
    public static final int HBA_ENUM_ERR_LACK_RESOURCE = 7;
    public static final int HBA_ENUM_ERR_NON_WHITELIST_APP = 12;
    public static final int HBA_ENUM_ERR_PROXY_ALREADY_EXIST = 5;
    public static final int HBA_ENUM_ERR_RCV_TIMEOUT = 10;
    public static final int HBA_ENUM_ERR_REPEAT_REQUEST = 3;
    public static final int HBA_ENUM_ERR_RUNTIME = 8;
    public static final int HBA_ENUM_ERR_SND_TIMEOUT = 9;
    public static final int HBA_ENUM_ERR_UNKNOWN = 99;
    public static final int HBA_ENUM_PROCESS_OK = 0;
    public static final int HBA_IND_PAUSE = 11;
    public static final int HBA_IND_TIMEOUT = 12;
    public static final int HBA_REQ_ESTABLISHED = 1;
    public static final int HBA_REQ_PAUSE = 2;
    public static final int HBA_REQ_RESUME = 3;
    public static final int HBA_REQ_STOP = 4;
    private static final int MAX_CYCLE = 3600;
    private static final int MAX_PAYLOAD_LEN = 128;
    private static final int MAX_STEP_CYCLE = 600;
    private static final int MAX_STEP_CYCLE_SUCCESS_NUM = 10;
    private static final int MAX_TCP_RETRIES2 = 15;
    private static final int MIN_CYCLE = 180;
    private static final int MIN_STEP_CYCLE = 30;
    private static final int MIN_STEP_CYCLE_SUCCESS_NUM = 1;
    private static final int MIN_TCP_RETRIES2 = 3;
    public static final String PROXY_KEY_NONE = null;
    private static final String TAG = "HeartbeatStream";
    private final Handler mHandler;
    private final HeartbeatListener mHeartbeatListener;
    private HeartbeatManager mHeartbeatManager;
    private final HeartbeatSettings mHeartbeatSettings;
    private String mProxyKey = PROXY_KEY_NONE;

    public HeartbeatStream(HeartbeatSettings settings, Handler handler) {
        Preconditions.checkNotNull(settings, "heartbeat settings can't be null!");
        Preconditions.checkNotNull(handler, "handler can't be null!");
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        if (!this.mHeartbeatManager.isHeartbeatAvailabel()) {
            throw new IllegalArgumentException("heartbeat service unavailable!");
        }
        if (!checkSettingslegal(settings)) {
            throw new IllegalArgumentException("heartbeat settings is illegal!");
        }
        this.mHandler = handler;
        this.mHeartbeatSettings = settings;
        try {
            this.mHeartbeatListener = new HeartbeatListener(handler.getLooper()) { // from class: com.oplus.network.heartbeat.HeartbeatStream.1
                @Override // com.oplus.network.heartbeat.HeartbeatListener
                public void onHeartbeatStateUpdate(int event, int err, int destroy, int[] args) {
                    Log.i(HeartbeatStream.TAG, "heartbeat state update event=" + event + " err=" + err + " destroy=" + destroy);
                    if (HeartbeatStream.this.mHandler != null) {
                        Message message = HeartbeatStream.this.mHandler.obtainMessage(event);
                        Bundle bundle = new Bundle();
                        bundle.putInt("err", err);
                        bundle.putInt("destroy", destroy);
                        if (args != null && args.length > 0) {
                            bundle.putInt("curr_cycle", args[0]);
                        }
                        message.obj = bundle;
                        HeartbeatStream.this.mHandler.sendMessage(message);
                    }
                }
            };
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("failed to init HeartbeatStream, this must be done in the looper thread!", e);
        }
    }

    public void establishHeartbeat() {
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        String establishHeartbeat = this.mHeartbeatManager.establishHeartbeat(this.mHeartbeatSettings, this.mHeartbeatListener);
        this.mProxyKey = establishHeartbeat;
        if (TextUtils.isEmpty(establishHeartbeat)) {
            reportErr(1, 1, 0);
        }
    }

    public void stopHeartbeat() {
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        if (!this.mHeartbeatManager.stopHeartbeat(this.mProxyKey)) {
            reportErr(4, 1, 1);
        }
    }

    public void pauseHeartbeat() {
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        if (!this.mHeartbeatManager.pauseHeartbeat(this.mProxyKey)) {
            reportErr(2, 1, 1);
        }
    }

    public void resumeHeartbeat() {
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        if (!this.mHeartbeatManager.resumeHeartbeat(this.mProxyKey)) {
            reportErr(3, 1, 0);
        }
    }

    private void reportErr(int event, int err, int destroy) {
        Log.e(TAG, "heartbeat state update event=" + event + " err=" + err + " destroy=" + destroy);
        Handler handler = this.mHandler;
        if (handler != null) {
            Message message = handler.obtainMessage(event);
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putInt("destroy", destroy);
            message.obj = bundle;
            this.mHandler.sendMessage(message);
        }
    }

    private boolean checkSettingslegal(HeartbeatSettings settings) {
        Socket socket = settings.getSocket();
        if (socket == null || !socket.isConnected()) {
            Log.e(TAG, "invalid socket!");
            return false;
        }
        if (settings.getSaddr() == null || settings.getDaddr() == null) {
            Log.e(TAG, "invalid address!");
            return false;
        }
        try {
            if (socket.getKeepAlive()) {
                Log.e(TAG, "heartbeat socket keep alive on, turn it off!");
                socket.setKeepAlive(false);
            }
        } catch (Exception e) {
            Log.e(TAG, "failed to get/set socket keep alive, e:" + e);
        }
        byte[] send = settings.getSendPayload();
        if (send == null || send.length > 128) {
            Log.e(TAG, "invalid send payload!");
            return false;
        }
        byte[] reply = settings.getReplyPayload();
        if (reply == null || reply.length > 128) {
            Log.e(TAG, "invalid reply payload!");
            return false;
        }
        int cycle = settings.getCycle();
        if (cycle < 180 || cycle > MAX_CYCLE) {
            Log.e(TAG, "invalid init cycle!");
            return false;
        }
        int allowDynamicCycle = settings.getIsAllowDynamicCycle();
        if (allowDynamicCycle == 1) {
            if (this.mHeartbeatManager == null) {
                this.mHeartbeatManager = HeartbeatManager.getInstance();
            }
            if (!this.mHeartbeatManager.isHeartbeatDynamicCycleEnabled()) {
                Log.e(TAG, "dynamic cycle disabled!");
                return false;
            }
            int maxCycle = settings.getMaxCycle();
            if (maxCycle < cycle || maxCycle > MAX_CYCLE) {
                Log.e(TAG, "invalid max cycle!");
                return false;
            }
            int stepCycle = settings.getStepCycle();
            if (stepCycle < 30 || stepCycle > 600) {
                Log.e(TAG, "invalid step cycle!");
                return false;
            }
            int stepCycleSuccessNum = settings.getStepCycleSuccessNum();
            if (stepCycleSuccessNum < 1 || stepCycleSuccessNum > 10) {
                Log.e(TAG, "invalid step cycle success num!");
                return false;
            }
        }
        int tcpRetries2 = settings.getTcpRetries2();
        if (tcpRetries2 >= 3 && tcpRetries2 <= 15) {
            return true;
        }
        Log.e(TAG, "invalid tcp retries2!");
        return false;
    }
}
