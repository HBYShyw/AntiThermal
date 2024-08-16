package com.oplus.flexiblewindow;

import android.app.ActivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public interface IFlexibleWindowObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.flexiblewindow.IFlexibleWindowObserver";

    void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException;

    void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException;

    void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IFlexibleWindowObserver {
        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFlexibleWindowObserver {
        static final int TRANSACTION_onBackPressedOnTaskRoot = 1;
        static final int TRANSACTION_onTaskAppeared = 2;
        static final int TRANSACTION_onTaskInfoChanged = 3;
        static final int TRANSACTION_onTaskVanished = 4;

        public Stub() {
            attachInterface(this, IFlexibleWindowObserver.DESCRIPTOR);
        }

        public static IFlexibleWindowObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFlexibleWindowObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IFlexibleWindowObserver)) {
                return (IFlexibleWindowObserver) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onBackPressedOnTaskRoot";
                case 2:
                    return "onTaskAppeared";
                case 3:
                    return "onTaskInfoChanged";
                case 4:
                    return "onTaskVanished";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IFlexibleWindowObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IFlexibleWindowObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ActivityManager.RunningTaskInfo _arg0 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onBackPressedOnTaskRoot(_arg0);
                            return true;
                        case 2:
                            ActivityManager.RunningTaskInfo _arg02 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            SurfaceControl _arg1 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            data.enforceNoDataAvail();
                            onTaskAppeared(_arg02, _arg1);
                            return true;
                        case 3:
                            ActivityManager.RunningTaskInfo _arg03 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            SurfaceControl _arg12 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            data.enforceNoDataAvail();
                            onTaskInfoChanged(_arg03, _arg12);
                            return true;
                        case 4:
                            ActivityManager.RunningTaskInfo _arg04 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onTaskVanished(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IFlexibleWindowObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFlexibleWindowObserver.DESCRIPTOR;
            }

            @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
            public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IFlexibleWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(runningTaskInfo, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
            public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IFlexibleWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(runningTaskInfo, 0);
                    _data.writeTypedObject(surfaceControl, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
            public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IFlexibleWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(runningTaskInfo, 0);
                    _data.writeTypedObject(surfaceControl, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
            public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IFlexibleWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(runningTaskInfo, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
