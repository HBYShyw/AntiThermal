package com.oplus.thermalcontrol;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import b6.LocalLog;
import com.oplus.horae.a;

/* loaded from: classes2.dex */
public class ThermalStatusListener implements IInterface {
    private static final int MSG_THEAML_STATUS = 20;
    private static final int NULL_TIME = 0;
    private static final int ONE_SECOND = 1000;
    private static final String TAG = "ThermalStatusListener";
    private static volatile ThermalStatusListener sThermalStatusListener;
    private Context mContext;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private int mStatus;
    private String mHeatInfo = "";
    private int mTsensorCpuTemp = Integer.MIN_VALUE;
    private float mTemp = 0.0f;
    private a.AbstractBinderC0018a mCallBack = new a.AbstractBinderC0018a() { // from class: com.oplus.thermalcontrol.ThermalStatusListener.2
        @Override // com.oplus.horae.a
        public void empty1() {
        }

        @Override // com.oplus.horae.a
        public void empty2() {
        }

        @Override // com.oplus.horae.a
        public void notifyAmbientThermal(int i10) {
            LocalLog.a(ThermalStatusListener.TAG, "notifyAmbientThermal:" + i10);
            if (ThermalControllerCenter.getInstance(ThermalStatusListener.this.mContext).isThermalControlTest()) {
                LocalLog.a(ThermalStatusListener.TAG, "ThermalControl in test, not respond to ambient temperature report.");
            } else {
                ThermalControlUtils.getInstance(ThermalStatusListener.this.mContext).setAmbientTemperature(i10);
            }
        }

        @Override // com.oplus.horae.a
        public void notifyThermalBroadCast(int i10, int i11) {
        }

        @Override // com.oplus.horae.a
        public void notifyThermalSource(int i10, int i11, String str) {
            ThermalStatusListener.this.mTemp = (float) ((i11 * 1.0d) / 1000.0d);
            ThermalControlUtils.getInstance(ThermalStatusListener.this.mContext).setThermalStatusFromBroadcast(ThermalStatusListener.this.mStatus, ThermalStatusListener.this.mTemp);
            if (ThermalStatusListener.this.mStatus != i10 || !ThermalStatusListener.this.mHeatInfo.equals(str)) {
                ThermalStatusListener.this.mStatus = i10;
                ThermalStatusListener.this.mHeatInfo = str;
                ThermalControllerCenter.getInstance(ThermalStatusListener.this.mContext).sendTempGearChangedMessage(ThermalStatusListener.this.mStatus, 0L, str);
            }
            LocalLog.a(ThermalStatusListener.TAG, "status = " + i10 + ", temp = " + ThermalStatusListener.this.mTemp + ", heatInfo=" + str);
        }

        @Override // com.oplus.horae.a
        public void notifyThermalStatus(int i10) {
            Message obtainMessage = ThermalStatusListener.this.mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("status", i10);
            obtainMessage.setData(bundle);
            obtainMessage.what = 20;
            ThermalStatusListener.this.mHandler.sendMessageDelayed(obtainMessage, 1000L);
        }

        @Override // com.oplus.horae.a
        public void notifyTsensorTemp(int i10) {
            if (ThermalControllerCenter.getInstance(ThermalStatusListener.this.mContext).isThermalControlTest()) {
                LocalLog.a(ThermalStatusListener.TAG, "ThermalControl in test, not respond to Tsensor temperature report.");
            } else if (ThermalStatusListener.this.mTsensorCpuTemp != i10) {
                ThermalStatusListener.this.mTsensorCpuTemp = i10;
                ThermalControllerCenter.getInstance(ThermalStatusListener.this.mContext).sendTsensorTempChangedMessage(i10);
            }
        }
    };

    public ThermalStatusListener(Context context) {
        this.mStatus = -1;
        this.mContext = context;
        this.mStatus = ThermalControlUtils.getInstance(context).getCurrentThermalStatus();
        HandlerThread handlerThread = new HandlerThread("ThermalControl");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.oplus.thermalcontrol.ThermalStatusListener.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 20) {
                    int i10 = message.getData().getInt("status", -1);
                    LocalLog.a(ThermalStatusListener.TAG, "mStatus = " + ThermalStatusListener.this.mStatus + ", status = " + i10 + ", mTemp = " + ThermalStatusListener.this.mTemp);
                    if (ThermalStatusListener.this.mTemp < 35.0f) {
                        ThermalStatusListener thermalStatusListener = ThermalStatusListener.this;
                        thermalStatusListener.mTemp = ThermalControlUtils.getInstance(thermalStatusListener.mContext).getTempFromBind();
                    }
                    if (ThermalStatusListener.this.mStatus != i10) {
                        ThermalStatusListener.this.mStatus = i10;
                        ThermalControlUtils.getInstance(ThermalStatusListener.this.mContext).setThermalStatusFromBroadcast(ThermalStatusListener.this.mStatus, ThermalStatusListener.this.mTemp);
                        ThermalControllerCenter.getInstance(ThermalStatusListener.this.mContext).sendTempGearChangedMessage(ThermalStatusListener.this.mStatus, 0L, "");
                    }
                }
            }
        };
    }

    public static void destroyListener() {
        sThermalStatusListener = null;
    }

    public static ThermalStatusListener getInstance(Context context) {
        if (sThermalStatusListener == null) {
            synchronized (ThermalStatusListener.class) {
                if (sThermalStatusListener == null) {
                    sThermalStatusListener = new ThermalStatusListener(context);
                }
            }
        }
        return sThermalStatusListener;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.mCallBack.asBinder();
    }
}
