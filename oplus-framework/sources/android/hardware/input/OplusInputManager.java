package android.hardware.input;

import android.hardware.input.IInputManager;
import android.hardware.input.IOplusInputManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.view.MotionEvent;
import com.oplus.debug.InputLog;

/* loaded from: classes.dex */
public class OplusInputManager {
    public static final String TAG = "OplusInputManager";
    private final IBinder mICallBack = new Binder();
    private IOplusInputManager mIOplusInputManager;

    public OplusInputManager() {
        try {
            InputLog.d(TAG, "init start");
            IInputManager im = IInputManager.Stub.asInterface(ServiceManager.getService("input"));
            InputLog.d(TAG, "init, IInputManager =" + im);
            if (im == null) {
                InputLog.e(TAG, "init failed, unknown IInputManager!");
            } else {
                IOplusInputManager asInterface = IOplusInputManager.Stub.asInterface(im.asBinder().getExtension());
                this.mIOplusInputManager = asInterface;
                if (asInterface == null) {
                    InputLog.e(TAG, "init failed, unknown IOplusInputManager!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void injectShoulderTouchEvent(MotionEvent motionEvent, int mode) {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "injectShoulderTouchEvent failed, oim is null!");
            return;
        }
        InputLog.d(TAG, "injectShoulderTouchEvent in oims, motionEvent = " + motionEvent + " mode = " + mode);
        try {
            this.mIOplusInputManager.injectShoulderTouchEvent(motionEvent, mode);
        } catch (Exception e) {
            InputLog.e(TAG, "injectShoulderTouchEvent failed, error:" + e);
        }
    }

    public void setNeedMergeTouchEvent(boolean enabled) {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "setNeedMergeTouchEvent failed, oim is null!");
            return;
        }
        InputLog.d(TAG, "setNeedMergeTouchEvent in oims, enabled = " + enabled);
        try {
            synchronized (OplusInputManager.class) {
                this.mIOplusInputManager.setNeedMergeTouchEvent(enabled);
            }
        } catch (Exception e) {
            InputLog.e(TAG, "setNeedMergeTouchEvent failed, error:" + e);
        }
    }

    public boolean setShoulderTouchInfo(Bundle info) {
        boolean shoulderTouchInfo;
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "setShoulderTouchInfo failed, oim is null!");
            return false;
        }
        InputLog.d(TAG, "setShoulderTouchInfo in oims, info = " + info);
        try {
            synchronized (OplusInputManager.class) {
                shoulderTouchInfo = this.mIOplusInputManager.setShoulderTouchInfo(info);
            }
            return shoulderTouchInfo;
        } catch (Exception e) {
            InputLog.e(TAG, "setShoulderTouchInfo failed, error:" + e);
            return false;
        }
    }

    public void registerAppDeathListener() {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "registerAppDeathListener failed, oim is null!");
            return;
        }
        InputLog.d(TAG, "registerAppDeathListener in oims");
        try {
            synchronized (OplusInputManager.class) {
                this.mIOplusInputManager.registerAppDeathListener(this.mICallBack);
            }
        } catch (Exception e) {
            InputLog.e(TAG, "registerAppDeathListener failed, error:" + e);
        }
    }

    public void unRegisterAppDeathListener() {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "unRegisterAppDeathListener failed, oim is null!");
            return;
        }
        InputLog.d(TAG, "unRegisterAppDeathListener in oims");
        try {
            synchronized (OplusInputManager.class) {
                this.mIOplusInputManager.unRegisterAppDeathListener(this.mICallBack);
            }
        } catch (Exception e) {
            InputLog.e(TAG, "unRegisterAppDeathListener failed, error:" + e);
        }
    }

    public boolean setInputReportingThreshold(String trackerEnabled, String statisticsEnabled, float latencyThreshold, float latencyIntervalThreshold) {
        boolean inputReportingThreshold;
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "setInputReportingThreshold failed, oim is null");
            return false;
        }
        try {
            synchronized (OplusInputManager.class) {
                inputReportingThreshold = this.mIOplusInputManager.setInputReportingThreshold(trackerEnabled, statisticsEnabled, latencyThreshold, latencyIntervalThreshold);
            }
            return inputReportingThreshold;
        } catch (Exception e) {
            InputLog.e(TAG, "setInputReportingThreshold failed, error:" + e);
            return false;
        }
    }

    public boolean resetInputReportingThreshold() {
        boolean resetInputReportingThreshold;
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "resetInputReportingThreshold failed, oim is null");
            return false;
        }
        try {
            synchronized (OplusInputManager.class) {
                resetInputReportingThreshold = this.mIOplusInputManager.resetInputReportingThreshold();
            }
            return resetInputReportingThreshold;
        } catch (Exception e) {
            InputLog.e(TAG, "resetInputReportingThreshold failed, error:" + e);
            return false;
        }
    }

    public void registerOplusInputJitterObserver(IOplusInputJitterObserver observer) {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "registerOplusInputJitterObserver failed, oim is null");
            return;
        }
        try {
            synchronized (OplusInputManager.class) {
                this.mIOplusInputManager.registerOplusInputJitterObserver(observer);
            }
        } catch (Exception e) {
            InputLog.e(TAG, "registerOplusInputJitterObserver failed, error:" + e);
        }
    }

    public void unregisterOplusInputJitterObserver(IOplusInputJitterObserver observer) {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "unregisterOplusInputJitterObserver failed, oim is null");
            return;
        }
        try {
            synchronized (OplusInputManager.class) {
                this.mIOplusInputManager.unregisterOplusInputJitterObserver(observer);
            }
        } catch (Exception e) {
            InputLog.e(TAG, "unregisterOplusInputJitterObserver failed, error:" + e);
        }
    }

    public void setNumberByInput(String code) {
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "sendVerifyCode failed, oim is null");
            return;
        }
        try {
            synchronized (OplusInputManager.class) {
                this.mIOplusInputManager.setNumberByInput(code);
            }
        } catch (Exception e) {
            InputLog.e(TAG, "sendVerifyCode failed, error:" + e);
        }
    }

    public boolean setQuickTouchOptimization(int optionStatus) {
        boolean quickTouchOptimization;
        if (this.mIOplusInputManager == null) {
            InputLog.e(TAG, "setQuickTouchOptimization failed, oim is null");
            return false;
        }
        try {
            synchronized (OplusInputManager.class) {
                quickTouchOptimization = this.mIOplusInputManager.setQuickTouchOptimization(optionStatus);
            }
            return quickTouchOptimization;
        } catch (Exception e) {
            InputLog.e(TAG, "setQuickTouchOptimization failed, error:" + e);
            return false;
        }
    }
}
