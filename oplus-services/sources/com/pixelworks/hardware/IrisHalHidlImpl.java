package com.pixelworks.hardware;

import android.hidl.manager.V1_0.IServiceManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.Message;
import android.os.NativeHandle;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import vendor.pixelworks.hardware.display.V1_0.IIris;
import vendor.pixelworks.hardware.display.V1_0.IIrisCallback;
import vendor.pixelworks.hardware.display.V1_1.IIris;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisHalHidlImpl {
    private static final boolean DEBUG = false;
    private static final String INSTANCE_NAME = "default";
    private static final String TAG = "IrisHal";
    private long mCookie;
    private IHwBinder.DeathRecipient mDeathRecipient;
    private IIris mIris;
    private IrisCallback mIrisCallback;
    private vendor.pixelworks.hardware.display.V1_2.IIris mIrisV1_2;
    private boolean mIsDeclared;
    private final Object mLock;
    private ArrayList<Handler> mServiceReportHandlers = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class IrisCallback extends IIrisCallback.Stub {
        IrisCallback() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback
        public void onFeatureChanged(int i, ArrayList<Integer> arrayList) throws RemoteException {
            synchronized (IrisHalHidlImpl.this.mServiceReportHandlers) {
                for (int i2 = 0; i2 < IrisHalHidlImpl.this.mServiceReportHandlers.size(); i2++) {
                    Handler handler = (Handler) IrisHalHidlImpl.this.mServiceReportHandlers.get(i2);
                    if (handler != null) {
                        Message obtain = Message.obtain();
                        obtain.what = i;
                        if (arrayList.size() > 0) {
                            obtain.arg1 = arrayList.get(0).intValue();
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
    }

    public IrisHalHidlImpl() {
        Object obj = new Object();
        this.mLock = obj;
        this.mIsDeclared = isDeclared();
        long myPid = Process.myPid();
        this.mCookie = myPid;
        this.mCookie = (myPid << 32) + Process.myTid();
        if (this.mIsDeclared) {
            synchronized (obj) {
                initialize();
            }
        } else {
            Log.d(TAG, "IIris HIDL is not declared");
        }
    }

    public boolean ready() {
        return this.mIris != null;
    }

    public void close() {
        IHwBinder.DeathRecipient deathRecipient;
        Log.d(TAG, "Close IIris HIDL");
        synchronized (this.mLock) {
            try {
                IIris iIris = this.mIris;
                if (iIris != null && (deathRecipient = this.mDeathRecipient) != null) {
                    iIris.unlinkToDeath(deathRecipient);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Access IIris failed", e);
            }
            this.mIris = null;
            this.mIrisCallback = null;
            this.mDeathRecipient = null;
        }
    }

    public static boolean isDeclared() {
        try {
            return IServiceManager.getService().getTransport(IIris.kInterfaceName, INSTANCE_NAME) != 0;
        } catch (RemoteException unused) {
            return false;
        }
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
                    this.mIris.registerCallback2(this.mCookie, irisCallback);
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
            if (this.mIris == null) {
                Log.e(TAG, "Can't get IIris");
                return -2;
            }
            ArrayList<Integer> arrayList = new ArrayList<>(iArr.length);
            for (int i2 : iArr) {
                arrayList.add(Integer.valueOf(i2));
            }
            return this.mIris.irisConfigureSet(i, arrayList);
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
            if (this.mIris == null) {
                Log.e(TAG, "Can't get IIris");
                return new IrisHalGetResult(-2);
            }
            ArrayList<Integer> arrayList = new ArrayList<>(iArr.length);
            for (int i2 : iArr) {
                arrayList.add(Integer.valueOf(i2));
            }
            final IrisHalGetResult irisHalGetResult = new IrisHalGetResult();
            this.mIris.irisConfigureGet(i, arrayList, new IIris.irisConfigureGetCallback() { // from class: com.pixelworks.hardware.IrisHalHidlImpl.1
                @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback
                public void onValues(int i3, ArrayList<Integer> arrayList2) {
                    IrisHalGetResult irisHalGetResult2 = irisHalGetResult;
                    irisHalGetResult2.ret = i3;
                    irisHalGetResult2.values = new int[arrayList2.size()];
                    for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                        irisHalGetResult.values[i4] = arrayList2.get(i4).intValue();
                    }
                }
            });
            return irisHalGetResult;
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
            if (this.mIris != null) {
                final IrisHalGetResult irisHalGetResult = new IrisHalGetResult();
                this.mIris.irisConfigureBatch(i, str, new IIris.irisConfigureBatchCallback() { // from class: com.pixelworks.hardware.IrisHalHidlImpl.2
                    @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback
                    public void onValues(int i2, String str3) {
                        IrisHalGetResult irisHalGetResult2 = irisHalGetResult;
                        irisHalGetResult2.ret = i2;
                        irisHalGetResult2.json = str3;
                        if (str3 != null) {
                            Log.d(IrisHalHidlImpl.TAG, "retJson size: [" + str3.length() + "]");
                        }
                    }
                });
                str2 = irisHalGetResult.json;
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
            if (this.mIrisV1_2 == null) {
                initialize();
            }
            vendor.pixelworks.hardware.display.V1_2.IIris iIris = this.mIrisV1_2;
            if (iIris == null) {
                Log.e(TAG, "Can't get IIris");
                return -2;
            }
            return iIris.irisConfigureBuffer(i, 0L, nativeHandle, i2);
        }
    }

    private void initialize() {
        boolean z = true;
        try {
            vendor.pixelworks.hardware.display.V1_1.IIris service = vendor.pixelworks.hardware.display.V1_1.IIris.getService(true);
            this.mIris = service;
            if (service != null) {
                Log.d(TAG, "Use IIris HIDL");
                synchronized (this.mServiceReportHandlers) {
                    if (this.mServiceReportHandlers.isEmpty()) {
                        z = false;
                    }
                }
                if (z) {
                    IrisCallback irisCallback = new IrisCallback();
                    this.mIrisCallback = irisCallback;
                    this.mIris.registerCallback2(this.mCookie, irisCallback);
                }
                IHwBinder.DeathRecipient deathRecipient = new IHwBinder.DeathRecipient() { // from class: com.pixelworks.hardware.IrisHalHidlImpl.3
                    public void serviceDied(long j) {
                        Log.w(IrisHalHidlImpl.TAG, "Noticed IIris HIDL death");
                        synchronized (IrisHalHidlImpl.this.mLock) {
                            try {
                                IrisHalHidlImpl.this.mIris.unlinkToDeath(this);
                            } catch (RemoteException e) {
                                Log.e(IrisHalHidlImpl.TAG, "Access IIris failed", e);
                            }
                            IrisHalHidlImpl.this.mIris = null;
                        }
                    }
                };
                this.mDeathRecipient = deathRecipient;
                this.mIris.linkToDeath(deathRecipient, this.mCookie);
                this.mIrisV1_2 = vendor.pixelworks.hardware.display.V1_2.IIris.castFrom((IHwInterface) this.mIris);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Call IIris API failed", e);
        } catch (NoSuchElementException e2) {
            this.mIsDeclared = false;
            Log.e(TAG, "Access IIris failed", e2);
        }
    }
}
