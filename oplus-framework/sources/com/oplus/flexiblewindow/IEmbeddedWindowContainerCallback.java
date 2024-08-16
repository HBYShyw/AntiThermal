package com.oplus.flexiblewindow;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IEmbeddedWindowContainerCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback";

    void adjustFlexiblePositionForIme(boolean z, int i) throws RemoteException;

    void autoScaleToOriginPosition() throws RemoteException;

    void notifyCanvasContainerReleaseTasks() throws RemoteException;

    void notifyDragStart() throws RemoteException;

    void notifyFlexibleTaskVanish(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void notifyTaskEmbeddedStatus(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z) throws RemoteException;

    void notifyTaskRectOrientationChange(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect) throws RemoteException;

    void notifyTaskToFullScreen(int i) throws RemoteException;

    void onCanvasPositionChanged() throws RemoteException;

    void onRecentsAnimationExecuting(boolean z, int i) throws RemoteException;

    void startReplaceSplitWhenNormalSplit(int i, Intent intent, int i2, int i3) throws RemoteException;

    void startThreeSplitFromNormalSplit(Intent intent, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IEmbeddedWindowContainerCallback {
        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void adjustFlexiblePositionForIme(boolean imeVisible, int offset) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void onRecentsAnimationExecuting(boolean executing, int reorderMode) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void autoScaleToOriginPosition() throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void startThreeSplitFromNormalSplit(Intent intent, int userId) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void startReplaceSplitWhenNormalSplit(int taskId, Intent intent, int userId, int index) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyFlexibleTaskVanish(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyCanvasContainerReleaseTasks() throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void onCanvasPositionChanged() throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyDragStart() throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyTaskToFullScreen(int taskId) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyTaskRectOrientationChange(ActivityManager.RunningTaskInfo taskInfo, Rect rect) throws RemoteException {
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyTaskEmbeddedStatus(ActivityManager.RunningTaskInfo taskInfo, boolean isBind) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEmbeddedWindowContainerCallback {
        static final int TRANSACTION_adjustFlexiblePositionForIme = 1;
        static final int TRANSACTION_autoScaleToOriginPosition = 3;
        static final int TRANSACTION_notifyCanvasContainerReleaseTasks = 7;
        static final int TRANSACTION_notifyDragStart = 9;
        static final int TRANSACTION_notifyFlexibleTaskVanish = 6;
        static final int TRANSACTION_notifyTaskEmbeddedStatus = 12;
        static final int TRANSACTION_notifyTaskRectOrientationChange = 11;
        static final int TRANSACTION_notifyTaskToFullScreen = 10;
        static final int TRANSACTION_onCanvasPositionChanged = 8;
        static final int TRANSACTION_onRecentsAnimationExecuting = 2;
        static final int TRANSACTION_startReplaceSplitWhenNormalSplit = 5;
        static final int TRANSACTION_startThreeSplitFromNormalSplit = 4;

        public Stub() {
            attachInterface(this, IEmbeddedWindowContainerCallback.DESCRIPTOR);
        }

        public static IEmbeddedWindowContainerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IEmbeddedWindowContainerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IEmbeddedWindowContainerCallback)) {
                return (IEmbeddedWindowContainerCallback) iin;
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
                    return "adjustFlexiblePositionForIme";
                case 2:
                    return "onRecentsAnimationExecuting";
                case 3:
                    return "autoScaleToOriginPosition";
                case 4:
                    return "startThreeSplitFromNormalSplit";
                case 5:
                    return "startReplaceSplitWhenNormalSplit";
                case 6:
                    return "notifyFlexibleTaskVanish";
                case 7:
                    return "notifyCanvasContainerReleaseTasks";
                case 8:
                    return "onCanvasPositionChanged";
                case 9:
                    return "notifyDragStart";
                case 10:
                    return "notifyTaskToFullScreen";
                case 11:
                    return "notifyTaskRectOrientationChange";
                case 12:
                    return "notifyTaskEmbeddedStatus";
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
                data.enforceInterface(IEmbeddedWindowContainerCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            adjustFlexiblePositionForIme(_arg0, _arg1);
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            onRecentsAnimationExecuting(_arg02, _arg12);
                            return true;
                        case 3:
                            autoScaleToOriginPosition();
                            return true;
                        case 4:
                            Intent _arg03 = (Intent) data.readTypedObject(Intent.CREATOR);
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            startThreeSplitFromNormalSplit(_arg03, _arg13);
                            return true;
                        case 5:
                            int _arg04 = data.readInt();
                            Intent _arg14 = (Intent) data.readTypedObject(Intent.CREATOR);
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            startReplaceSplitWhenNormalSplit(_arg04, _arg14, _arg2, _arg3);
                            return true;
                        case 6:
                            ActivityManager.RunningTaskInfo _arg05 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            notifyFlexibleTaskVanish(_arg05);
                            return true;
                        case 7:
                            notifyCanvasContainerReleaseTasks();
                            reply.writeNoException();
                            return true;
                        case 8:
                            onCanvasPositionChanged();
                            return true;
                        case 9:
                            notifyDragStart();
                            return true;
                        case 10:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyTaskToFullScreen(_arg06);
                            return true;
                        case 11:
                            ActivityManager.RunningTaskInfo _arg07 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            Rect _arg15 = (Rect) data.readTypedObject(Rect.CREATOR);
                            data.enforceNoDataAvail();
                            notifyTaskRectOrientationChange(_arg07, _arg15);
                            return true;
                        case 12:
                            ActivityManager.RunningTaskInfo _arg08 = (ActivityManager.RunningTaskInfo) data.readTypedObject(ActivityManager.RunningTaskInfo.CREATOR);
                            boolean _arg16 = data.readBoolean();
                            data.enforceNoDataAvail();
                            notifyTaskEmbeddedStatus(_arg08, _arg16);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IEmbeddedWindowContainerCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IEmbeddedWindowContainerCallback.DESCRIPTOR;
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void adjustFlexiblePositionForIme(boolean imeVisible, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeBoolean(imeVisible);
                    _data.writeInt(offset);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void onRecentsAnimationExecuting(boolean executing, int reorderMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeBoolean(executing);
                    _data.writeInt(reorderMode);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void autoScaleToOriginPosition() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void startThreeSplitFromNormalSplit(Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void startReplaceSplitWhenNormalSplit(int taskId, Intent intent, int userId, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(userId);
                    _data.writeInt(index);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void notifyFlexibleTaskVanish(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeTypedObject(taskInfo, 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void notifyCanvasContainerReleaseTasks() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void onCanvasPositionChanged() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void notifyDragStart() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void notifyTaskToFullScreen(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void notifyTaskRectOrientationChange(ActivityManager.RunningTaskInfo taskInfo, Rect rect) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeTypedObject(taskInfo, 0);
                    _data.writeTypedObject(rect, 0);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
            public void notifyTaskEmbeddedStatus(ActivityManager.RunningTaskInfo taskInfo, boolean isBind) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IEmbeddedWindowContainerCallback.DESCRIPTOR);
                    _data.writeTypedObject(taskInfo, 0);
                    _data.writeBoolean(isBind);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 11;
        }
    }
}
