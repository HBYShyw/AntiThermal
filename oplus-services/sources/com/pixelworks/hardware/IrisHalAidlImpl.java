package com.pixelworks.hardware;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.NativeHandle;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import vendor.pixelworks.hardware.display.ContentSamples;
import vendor.pixelworks.hardware.display.IIris;
import vendor.pixelworks.hardware.display.IIrisCallback;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisHalAidlImpl {
    private static final boolean DEBUG = false;
    private static final String INSTANCE_NAME = IIris.DESCRIPTOR + "/default";
    private static final String TAG = "IrisHal";
    private long mCookie;
    private IBinder.DeathRecipient mDeathRecipient;
    private IIris mIris;
    private IrisCallback mIrisCallback;
    private boolean mIsDeclared;
    private final Object mLock;
    private ArrayList<Handler> mServiceReportHandlers = new ArrayList<>();
    private boolean mUseDeathRecipient;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class IrisCallback extends IIrisCallback.Stub {
        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public String getInterfaceHash() {
            return "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int getInterfaceVersion() {
            return 1;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int onCalibratePatternChanged(long j, int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onDisplayPowerChanged(long j, int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onRefreshRequested(long j) throws RemoteException {
        }

        IrisCallback() {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onFeatureChanged(int i, int[] iArr) throws RemoteException {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i2 : iArr) {
                arrayList.add(Integer.valueOf(i2));
            }
            synchronized (IrisHalAidlImpl.this.mServiceReportHandlers) {
                for (int i3 = 0; i3 < IrisHalAidlImpl.this.mServiceReportHandlers.size(); i3++) {
                    Handler handler = (Handler) IrisHalAidlImpl.this.mServiceReportHandlers.get(i3);
                    if (handler != null) {
                        Message obtain = Message.obtain();
                        obtain.what = i;
                        if (iArr.length > 0) {
                            obtain.arg1 = iArr[0];
                            Bundle bundle = new Bundle();
                            bundle.putIntegerArrayList("values", arrayList);
                            obtain.setData(bundle);
                        } else {
                            obtain.arg1 = 0;
                        }
                        handler.sendMessage(obtain);
                    }
                }
            }
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public ContentSamples onContentSamplingRequested(long j, int i, long j2) throws RemoteException {
            ContentSamples contentSamples = new ContentSamples();
            contentSamples.result = -1;
            return contentSamples;
        }
    }

    public IrisHalAidlImpl(boolean z) {
        Object obj = new Object();
        this.mLock = obj;
        this.mIsDeclared = isDeclared();
        this.mUseDeathRecipient = z;
        long myPid = Process.myPid();
        this.mCookie = myPid;
        this.mCookie = (myPid << 32) + Process.myTid();
        if (this.mIsDeclared) {
            synchronized (obj) {
                initialize();
            }
        } else {
            Log.d(TAG, "IIris AIDL is not declared");
        }
    }

    public boolean ready() {
        return this.mIris != null;
    }

    public void close() {
        Log.d(TAG, "Close IIris AIDL");
        synchronized (this.mLock) {
            IIris iIris = this.mIris;
            if (iIris != null && this.mDeathRecipient != null) {
                iIris.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            }
            this.mIris = null;
            this.mIrisCallback = null;
            this.mDeathRecipient = null;
        }
    }

    public static boolean isDeclared() {
        return ServiceManager.isDeclared(INSTANCE_NAME);
    }

    public void setServiceReportHandler(Handler handler) {
        synchronized (this.mServiceReportHandlers) {
            for (int i = 0; i < this.mServiceReportHandlers.size(); i++) {
                this.mServiceReportHandlers.set(i, null);
            }
        }
        addServiceReportHandler(handler);
    }

    public void addServiceReportHandler(Handler handler) {
        synchronized (this.mServiceReportHandlers) {
            if (!this.mServiceReportHandlers.contains(handler)) {
                this.mServiceReportHandlers.add(handler);
            }
        }
        synchronized (this.mLock) {
            if (this.mIris != null && this.mIrisCallback == null) {
                IrisCallback irisCallback = new IrisCallback();
                this.mIrisCallback = irisCallback;
                try {
                    this.mIris.registerCallback(this.mCookie, irisCallback);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to register IrisCallback", e);
                }
            }
        }
    }

    public void removeServiceReportHandler(Handler handler) {
        synchronized (this.mServiceReportHandlers) {
            int indexOf = this.mServiceReportHandlers.indexOf(handler);
            if (indexOf >= 0) {
                this.mServiceReportHandlers.set(indexOf, null);
            }
        }
    }

    public int irisConfigureSet(int i, int[] iArr) throws RemoteException {
        if (i < 0 || iArr == null) {
            Log.e(TAG, "Input parameters are wrong.");
            return -1;
        }
        if (!this.mIsDeclared) {
            Log.e(TAG, "IIris service is not declared.");
            return -2;
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            IIris iIris = this.mIris;
            if (iIris == null) {
                Log.e(TAG, "Can't get IIris");
                return -2;
            }
            try {
                return iIris.irisConfigureSet(i, iArr);
            } catch (ServiceSpecificException e) {
                return e.errorCode;
            } catch (RuntimeException unused) {
                return -1;
            }
        }
    }

    public IrisHalGetResult irisConfigureGet(int i, int[] iArr) throws RemoteException {
        if (i < 0 || iArr == null) {
            Log.e(TAG, "Input parameters are wrong.");
            return new IrisHalGetResult();
        }
        if (!this.mIsDeclared) {
            Log.e(TAG, "IIris service is not declared.");
            return new IrisHalGetResult(-2);
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            IIris iIris = this.mIris;
            if (iIris == null) {
                Log.e(TAG, "Can't get IIris");
                return new IrisHalGetResult(-2);
            }
            try {
                try {
                    return new IrisHalGetResult(0, iIris.irisConfigureGet(i, iArr));
                } catch (RuntimeException unused) {
                    return new IrisHalGetResult();
                }
            } catch (ServiceSpecificException e) {
                return new IrisHalGetResult(e.errorCode);
            }
        }
    }

    public String irisConfigureBatch(int i, String str) throws RemoteException {
        String str2 = "";
        if (i < 0 || str == null) {
            Log.e(TAG, "Input parameters are wrong.");
            return null;
        }
        if (!this.mIsDeclared) {
            Log.e(TAG, "IIris service is not declared.");
            return null;
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            IIris iIris = this.mIris;
            if (iIris != null) {
                try {
                    try {
                        str2 = iIris.irisConfigureBatch(i, str);
                    } catch (ServiceSpecificException e) {
                        Log.e(TAG, "ServiceSpecificException: " + e);
                    }
                } catch (RuntimeException e2) {
                    Log.e(TAG, "RuntimeException: " + e2);
                }
            } else {
                Log.e(TAG, "Query IIris interface failed");
            }
        }
        return str2;
    }

    public int irisConfigureBuffer(int i, NativeHandle nativeHandle, int i2) throws RemoteException {
        if (i < 0 || nativeHandle == null || i2 <= 0) {
            Log.e(TAG, "Input parameters are wrong.");
            return -1;
        }
        if (!this.mIsDeclared) {
            Log.e(TAG, "IIris service is not declared.");
            return -2;
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            if (this.mIris == null) {
                Log.e(TAG, "Can't get IIris");
                return -2;
            }
            try {
                try {
                    try {
                        return this.mIris.irisConfigureBuffer(i, 0L, ParcelFileDescriptor.dup(nativeHandle.getFileDescriptor()), i2);
                    } catch (RuntimeException unused) {
                        return -1;
                    }
                } catch (ServiceSpecificException e) {
                    return e.errorCode;
                }
            } catch (IOException unused2) {
                Log.e(TAG, "Dup file descriptor failed.");
                return -1;
            }
        }
    }

    private void initialize() {
        boolean z;
        try {
            IIris asInterface = IIris.Stub.asInterface(ServiceManager.waitForDeclaredService(INSTANCE_NAME));
            this.mIris = asInterface;
            if (asInterface != null) {
                Log.d(TAG, "Use IIris AIDL");
                synchronized (this.mServiceReportHandlers) {
                    z = !this.mServiceReportHandlers.isEmpty();
                }
                if (z) {
                    IrisCallback irisCallback = new IrisCallback();
                    this.mIrisCallback = irisCallback;
                    this.mIris.registerCallback(this.mCookie, irisCallback);
                }
                if (this.mUseDeathRecipient) {
                    this.mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.pixelworks.hardware.IrisHalAidlImpl.1
                        @Override // android.os.IBinder.DeathRecipient
                        public void binderDied() {
                            Log.w(IrisHalAidlImpl.TAG, "Noticed IIris AIDL death");
                            synchronized (IrisHalAidlImpl.this.mLock) {
                                if (IrisHalAidlImpl.this.mIris != null) {
                                    IrisHalAidlImpl.this.mIris.asBinder().unlinkToDeath(this, 0);
                                }
                                IrisHalAidlImpl.this.mIris = null;
                            }
                        }
                    };
                    this.mIris.asBinder().linkToDeath(this.mDeathRecipient, 0);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Call IIris API failed", e);
        } catch (NoSuchElementException e2) {
            this.mIsDeclared = false;
            Log.e(TAG, "Access IIris failed", e2);
        }
    }
}
