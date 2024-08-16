package com.oplus.zoomwindow;

import android.app.ActivityManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import com.oplus.zoomwindow.IOplusZoomTaskInfoCallback;

/* loaded from: classes.dex */
public interface IOplusZoomTaskListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.zoomwindow.IOplusZoomTaskListener";

    void getCurrentZoomInfo(int i, IOplusZoomTaskInfoCallback iOplusZoomTaskInfoCallback) throws RemoteException;

    void notifyShowCompatibilityToast(int i, int i2, String str, String str2, Bundle bundle) throws RemoteException;

    void notifySystemEvent(int i) throws RemoteException;

    void notifyZoomTaskEvent(int i, int i2) throws RemoteException;

    void onRemoteTransitionCancel(int i, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException;

    void onRemoteTransitionStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException;

    void onZoomEnter(OplusZoomTaskInfo oplusZoomTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException;

    void onZoomExit(OplusZoomTaskInfo oplusZoomTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onZoomTaskChanged(OplusZoomTaskInfo oplusZoomTaskInfo) throws RemoteException;

    void requestChangeZoomState(int i, int i2, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusZoomTaskListener {
        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void onZoomEnter(OplusZoomTaskInfo zoomTaskInfo, ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void onZoomTaskChanged(OplusZoomTaskInfo zoomTaskInfo) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void onZoomExit(OplusZoomTaskInfo zoomTaskInfo, ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void notifyZoomTaskEvent(int taskId, int event) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void notifySystemEvent(int event) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void requestChangeZoomState(int taskId, int flag, boolean anim) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void onRemoteTransitionStart(int seq, RemoteAnimationTarget[] target) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void onRemoteTransitionCancel(int seq, IRemoteAnimationFinishedCallback finishCallback) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void notifyShowCompatibilityToast(int type, int userId, String target, String callPkg, Bundle extension) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
        public void getCurrentZoomInfo(int taskId, IOplusZoomTaskInfoCallback oplusZoomTaskInfoCallback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusZoomTaskListener {
        static final int TRANSACTION_getCurrentZoomInfo = 10;
        static final int TRANSACTION_notifyShowCompatibilityToast = 9;
        static final int TRANSACTION_notifySystemEvent = 5;
        static final int TRANSACTION_notifyZoomTaskEvent = 4;
        static final int TRANSACTION_onRemoteTransitionCancel = 8;
        static final int TRANSACTION_onRemoteTransitionStart = 7;
        static final int TRANSACTION_onZoomEnter = 1;
        static final int TRANSACTION_onZoomExit = 3;
        static final int TRANSACTION_onZoomTaskChanged = 2;
        static final int TRANSACTION_requestChangeZoomState = 6;

        public Stub() {
            attachInterface(this, IOplusZoomTaskListener.DESCRIPTOR);
        }

        public static IOplusZoomTaskListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusZoomTaskListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusZoomTaskListener)) {
                return (IOplusZoomTaskListener) iin;
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
                    return "onZoomEnter";
                case 2:
                    return "onZoomTaskChanged";
                case 3:
                    return "onZoomExit";
                case 4:
                    return "notifyZoomTaskEvent";
                case 5:
                    return "notifySystemEvent";
                case 6:
                    return "requestChangeZoomState";
                case 7:
                    return "onRemoteTransitionStart";
                case 8:
                    return "onRemoteTransitionCancel";
                case 9:
                    return "notifyShowCompatibilityToast";
                case 10:
                    return "getCurrentZoomInfo";
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
                data.enforceInterface(IOplusZoomTaskListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusZoomTaskListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusZoomTaskInfo _arg0 = (OplusZoomTaskInfo) data.readTypedObject(OplusZoomTaskInfo.CREATOR);
                            ActivityManager.RunningTaskInfo _arg1 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            SurfaceControl _arg2 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomEnter(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            OplusZoomTaskInfo _arg02 = (OplusZoomTaskInfo) data.readTypedObject(OplusZoomTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomTaskChanged(_arg02);
                            return true;
                        case 3:
                            OplusZoomTaskInfo _arg03 = (OplusZoomTaskInfo) data.readTypedObject(OplusZoomTaskInfo.CREATOR);
                            ActivityManager.RunningTaskInfo _arg12 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomExit(_arg03, _arg12);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyZoomTaskEvent(_arg04, _arg13);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            notifySystemEvent(_arg05);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg14 = data.readInt();
                            boolean _arg22 = data.readBoolean();
                            data.enforceNoDataAvail();
                            requestChangeZoomState(_arg06, _arg14, _arg22);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            RemoteAnimationTarget[] _arg15 = (RemoteAnimationTarget[]) data.createTypedArray(RemoteAnimationTarget.CREATOR);
                            data.enforceNoDataAvail();
                            onRemoteTransitionStart(_arg07, _arg15);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            IRemoteAnimationFinishedCallback _arg16 = IRemoteAnimationFinishedCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            onRemoteTransitionCancel(_arg08, _arg16);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            int _arg17 = data.readInt();
                            String _arg23 = data.readString();
                            String _arg3 = data.readString();
                            Bundle _arg4 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            notifyShowCompatibilityToast(_arg09, _arg17, _arg23, _arg3, _arg4);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            IOplusZoomTaskInfoCallback _arg18 = IOplusZoomTaskInfoCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            getCurrentZoomInfo(_arg010, _arg18);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusZoomTaskListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusZoomTaskListener.DESCRIPTOR;
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void onZoomEnter(OplusZoomTaskInfo zoomTaskInfo, ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeTypedObject(zoomTaskInfo, 0);
                    _data.writeTypedObject(taskInfo, 0);
                    _data.writeTypedObject(leash, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void onZoomTaskChanged(OplusZoomTaskInfo zoomTaskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeTypedObject(zoomTaskInfo, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void onZoomExit(OplusZoomTaskInfo zoomTaskInfo, ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeTypedObject(zoomTaskInfo, 0);
                    _data.writeTypedObject(taskInfo, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void notifyZoomTaskEvent(int taskId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(event);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void notifySystemEvent(int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(event);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void requestChangeZoomState(int taskId, int flag, boolean anim) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(flag);
                    _data.writeBoolean(anim);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void onRemoteTransitionStart(int seq, RemoteAnimationTarget[] target) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(seq);
                    _data.writeTypedArray(target, 0);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void onRemoteTransitionCancel(int seq, IRemoteAnimationFinishedCallback finishCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(seq);
                    _data.writeStrongInterface(finishCallback);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void notifyShowCompatibilityToast(int type, int userId, String target, String callPkg, Bundle extension) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(userId);
                    _data.writeString(target);
                    _data.writeString(callPkg);
                    _data.writeTypedObject(extension, 0);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskListener
            public void getCurrentZoomInfo(int taskId, IOplusZoomTaskInfoCallback oplusZoomTaskInfoCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskListener.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeStrongInterface(oplusZoomTaskInfoCallback);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 9;
        }
    }
}
