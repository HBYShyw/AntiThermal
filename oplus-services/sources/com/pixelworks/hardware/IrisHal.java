package com.pixelworks.hardware;

import android.os.Handler;
import android.os.NativeHandle;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisHal {
    private static final String TAG = "IrisHal";
    private IrisHalAidlImpl mAidlImpl;
    private IrisHalHidlImpl mHidlImpl;
    private boolean mUseAidl = false;
    private boolean mHasIris = false;
    private final String PROP_IRIS_SUPPORT = "sys.pxlw.iris.support";

    public IrisHal() {
        createHalImpl(true);
    }

    public IrisHal(boolean z) {
        createHalImpl(z);
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public void close() {
        if (this.mHasIris) {
            if (this.mUseAidl) {
                this.mAidlImpl.close();
            } else {
                this.mHidlImpl.close();
            }
        }
    }

    public void setServiceReportHandler(Handler handler) {
        if (this.mHasIris) {
            if (this.mUseAidl) {
                this.mAidlImpl.setServiceReportHandler(handler);
            } else {
                this.mHidlImpl.setServiceReportHandler(handler);
            }
        }
    }

    public void addServiceReportHandler(Handler handler) {
        if (this.mHasIris) {
            if (this.mUseAidl) {
                this.mAidlImpl.addServiceReportHandler(handler);
            } else {
                this.mHidlImpl.addServiceReportHandler(handler);
            }
        }
    }

    public void removeServiceReportHandler(Handler handler) {
        if (this.mHasIris) {
            if (this.mUseAidl) {
                this.mAidlImpl.removeServiceReportHandler(handler);
            } else {
                this.mHidlImpl.removeServiceReportHandler(handler);
            }
        }
    }

    public int irisConfigureSet(int i, int[] iArr) {
        if (!this.mHasIris) {
            return 0;
        }
        try {
            if (this.mUseAidl) {
                return this.mAidlImpl.irisConfigureSet(i, iArr);
            }
            return this.mHidlImpl.irisConfigureSet(i, iArr);
        } catch (RemoteException e) {
            Log.e(TAG, "Access IIris failed", e);
            return -1;
        }
    }

    public IrisHalGetResult irisConfigureGet(int i, int[] iArr) {
        IrisHalGetResult irisConfigureGet;
        IrisHalGetResult irisHalGetResult = new IrisHalGetResult();
        if (!this.mHasIris) {
            return irisHalGetResult;
        }
        try {
            if (this.mUseAidl) {
                irisConfigureGet = this.mAidlImpl.irisConfigureGet(i, iArr);
            } else {
                irisConfigureGet = this.mHidlImpl.irisConfigureGet(i, iArr);
            }
            return irisConfigureGet;
        } catch (RemoteException e) {
            Log.e(TAG, "Access IIris failed", e);
            return irisHalGetResult;
        }
    }

    public String irisConfigureBatch(int i, String str) {
        String irisConfigureBatch;
        String str2 = new String("");
        if (!this.mHasIris) {
            return str2;
        }
        try {
            if (this.mUseAidl) {
                irisConfigureBatch = this.mAidlImpl.irisConfigureBatch(i, str);
            } else {
                irisConfigureBatch = this.mHidlImpl.irisConfigureBatch(i, str);
            }
            return irisConfigureBatch;
        } catch (RemoteException e) {
            Log.e(TAG, "Access IIris failed", e);
            return str2;
        }
    }

    public int irisConfigureBuffer(int i, NativeHandle nativeHandle, int i2) {
        int irisConfigureBuffer;
        if (!this.mHasIris) {
            return -1;
        }
        try {
            if (this.mUseAidl) {
                irisConfigureBuffer = this.mAidlImpl.irisConfigureBuffer(i, nativeHandle, i2);
            } else {
                irisConfigureBuffer = this.mHidlImpl.irisConfigureBuffer(i, nativeHandle, i2);
            }
            return irisConfigureBuffer;
        } catch (RemoteException e) {
            Log.e(TAG, "Access IIris failed", e);
            return -1;
        }
    }

    private void createHalImpl(boolean z) {
        try {
            if (IrisHalAidlImpl.isDeclared()) {
                IrisHalAidlImpl irisHalAidlImpl = new IrisHalAidlImpl(z);
                this.mAidlImpl = irisHalAidlImpl;
                if (irisHalAidlImpl.ready()) {
                    this.mUseAidl = true;
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (NullPointerException unused) {
        } catch (SecurityException unused2) {
            Log.w(TAG, "SELinux denied IIris AIDL");
        }
        try {
            if (IrisHalHidlImpl.isDeclared()) {
                IrisHalHidlImpl irisHalHidlImpl = new IrisHalHidlImpl();
                this.mHidlImpl = irisHalHidlImpl;
                if (irisHalHidlImpl.ready()) {
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (NullPointerException unused3) {
        } catch (SecurityException unused4) {
            Log.w(TAG, "SELinux denied IIris HIDL");
        }
        Log.i(TAG, "No found IIris service");
    }
}
