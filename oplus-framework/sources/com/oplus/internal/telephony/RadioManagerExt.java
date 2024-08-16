package com.oplus.internal.telephony;

import android.os.Handler;
import android.telephony.Rlog;
import com.android.internal.telephony.IOplusRadioManager;
import com.android.internal.telephony.OplusTelephonyFactory;

/* loaded from: classes.dex */
public class RadioManagerExt {
    private static final String TAG = "RadioManagerExt";
    private static RadioManagerExt sInstance;
    private IOplusRadioManager mImpl = OplusTelephonyFactory.getInstance().getFeature(IOplusRadioManager.DEFAULT, new Object[0]);

    private RadioManagerExt() {
    }

    public static RadioManagerExt getInstance() {
        RadioManagerExt radioManagerExt;
        synchronized (RadioManagerExt.class) {
            if (sInstance == null) {
                sInstance = new RadioManagerExt();
            }
            radioManagerExt = sInstance;
        }
        return radioManagerExt;
    }

    public void notifyAirplaneModeChange(boolean state) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.notifyAirplaneModeChange(state);
        } else {
            loge("notifyAirplaneModeChange: null");
        }
    }

    public boolean isPowerOnFeatureAllClosed() {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.isPowerOnFeatureAllClosed();
            return false;
        }
        loge("isPowerOnFeatureAllClosed: null");
        return false;
    }

    public void registerForRadioPowerChange(Handler h, int what, Object obj) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.registerForRadioPowerChange(h, what, obj);
        } else {
            loge("registerForRadioPowerChange: null");
        }
    }

    public void unregisterForRadioPowerChange(Handler h) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.unregisterForRadioPowerChange(h);
        } else {
            loge("unregisterForRadioPowerChange: null");
        }
    }

    public void notifySimModeChange(boolean power, int phoneId) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.notifySimModeChange(power, phoneId);
        } else {
            loge("notifySimModeChange: null");
        }
    }

    public boolean isModemPowerOff(int phoneId) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.isModemPowerOff(phoneId);
            return false;
        }
        loge("isModemPowerOff: null");
        return false;
    }

    public void setSilentRebootPropertyForAllModem(String isSilentReboot) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.setSilentRebootPropertyForAllModem(isSilentReboot);
        } else {
            loge("setSilentRebootPropertyForAllModem: null");
        }
    }

    public void forceRefreshSimState(boolean power, int phoneId) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.forceRefreshSimState(power, phoneId);
        } else {
            loge("forceRefreshSimState: null");
        }
    }

    public int setRadioPower(boolean power, int phoneId) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            return iOplusRadioManager.setRadioPower(power, phoneId);
        }
        loge("setRadioPower: null");
        return -1;
    }

    public void sendRequestBeforeSetRadioPower(boolean power, int phoneId) {
        IOplusRadioManager iOplusRadioManager = this.mImpl;
        if (iOplusRadioManager != null) {
            iOplusRadioManager.sendRequestBeforeSetRadioPower(power, phoneId);
        } else {
            loge("sendRequestBeforeSetRadioPower: null");
        }
    }

    private static void logd(String msg) {
        Rlog.d(TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(TAG, msg);
    }
}
