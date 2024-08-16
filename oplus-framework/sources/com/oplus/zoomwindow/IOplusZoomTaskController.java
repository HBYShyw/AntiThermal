package com.oplus.zoomwindow;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IRecentsAnimationController;
import android.window.WindowContainerToken;
import com.oplus.zoomwindow.IOplusZoomTaskInfoCallback;
import com.oplus.zoomwindow.IOplusZoomTaskListener;

/* loaded from: classes.dex */
public interface IOplusZoomTaskController extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.zoomwindow.IOplusZoomTaskController";

    void getCurrentZoomInfo(int i, IOplusZoomTaskInfoCallback iOplusZoomTaskInfoCallback) throws RemoteException;

    void onTransitionEnd(int i) throws RemoteException;

    void onTransitionStart() throws RemoteException;

    void onZoomRotateChanged(int i, int i2, OplusZoomTaskInfo oplusZoomTaskInfo) throws RemoteException;

    void onZoomStateChanged(OplusZoomTaskInfo oplusZoomTaskInfo) throws RemoteException;

    boolean recentAnimationFinished(int i, int i2, Rect rect, int i3, Bundle bundle, IRecentsAnimationController iRecentsAnimationController, boolean z, boolean z2) throws RemoteException;

    boolean registerZoomTaskListener(IOplusZoomTaskListener iOplusZoomTaskListener) throws RemoteException;

    void requestChangeZoomTask(WindowContainerToken windowContainerToken, int i, boolean z) throws RemoteException;

    void setInputToken(WindowContainerToken windowContainerToken, IBinder iBinder) throws RemoteException;

    boolean unregisterZoomTaskListener(IOplusZoomTaskListener iOplusZoomTaskListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusZoomTaskController {
        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public boolean registerZoomTaskListener(IOplusZoomTaskListener zoomTaskListener) throws RemoteException {
            return false;
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public boolean unregisterZoomTaskListener(IOplusZoomTaskListener zoomTaskListener) throws RemoteException {
            return false;
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void onZoomStateChanged(OplusZoomTaskInfo zoomTaskInfo) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void onZoomRotateChanged(int fromRotation, int toRotation, OplusZoomTaskInfo zoomTaskInfo) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void requestChangeZoomTask(WindowContainerToken token, int flag, boolean anim) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void onTransitionEnd(int seq) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void setInputToken(WindowContainerToken token, IBinder channelToken) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void onTransitionStart() throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public boolean recentAnimationFinished(int taskId, int type, Rect rect, int orientation, Bundle bOptions, IRecentsAnimationController controller, boolean moveHomeToTop, boolean sendUserLeaveHint) throws RemoteException {
            return false;
        }

        @Override // com.oplus.zoomwindow.IOplusZoomTaskController
        public void getCurrentZoomInfo(int taskId, IOplusZoomTaskInfoCallback oplusZoomTaskInfoCallback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusZoomTaskController {
        static final int TRANSACTION_getCurrentZoomInfo = 10;
        static final int TRANSACTION_onTransitionEnd = 6;
        static final int TRANSACTION_onTransitionStart = 8;
        static final int TRANSACTION_onZoomRotateChanged = 4;
        static final int TRANSACTION_onZoomStateChanged = 3;
        static final int TRANSACTION_recentAnimationFinished = 9;
        static final int TRANSACTION_registerZoomTaskListener = 1;
        static final int TRANSACTION_requestChangeZoomTask = 5;
        static final int TRANSACTION_setInputToken = 7;
        static final int TRANSACTION_unregisterZoomTaskListener = 2;

        public Stub() {
            attachInterface(this, IOplusZoomTaskController.DESCRIPTOR);
        }

        public static IOplusZoomTaskController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusZoomTaskController.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusZoomTaskController)) {
                return (IOplusZoomTaskController) iin;
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
                    return "registerZoomTaskListener";
                case 2:
                    return "unregisterZoomTaskListener";
                case 3:
                    return "onZoomStateChanged";
                case 4:
                    return "onZoomRotateChanged";
                case 5:
                    return "requestChangeZoomTask";
                case 6:
                    return "onTransitionEnd";
                case 7:
                    return "setInputToken";
                case 8:
                    return "onTransitionStart";
                case 9:
                    return "recentAnimationFinished";
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
                data.enforceInterface(IOplusZoomTaskController.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusZoomTaskController.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusZoomTaskListener _arg0 = IOplusZoomTaskListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = registerZoomTaskListener(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            IOplusZoomTaskListener _arg02 = IOplusZoomTaskListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = unregisterZoomTaskListener(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            OplusZoomTaskInfo _arg03 = (OplusZoomTaskInfo) data.readTypedObject(OplusZoomTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomStateChanged(_arg03);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg1 = data.readInt();
                            OplusZoomTaskInfo _arg2 = (OplusZoomTaskInfo) data.readTypedObject(OplusZoomTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomRotateChanged(_arg04, _arg1, _arg2);
                            return true;
                        case 5:
                            WindowContainerToken _arg05 = (WindowContainerToken) data.readTypedObject(WindowContainerToken.CREATOR);
                            int _arg12 = data.readInt();
                            boolean _arg22 = data.readBoolean();
                            data.enforceNoDataAvail();
                            requestChangeZoomTask(_arg05, _arg12, _arg22);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            onTransitionEnd(_arg06);
                            return true;
                        case 7:
                            WindowContainerToken _arg07 = (WindowContainerToken) data.readTypedObject(WindowContainerToken.CREATOR);
                            IBinder _arg13 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            setInputToken(_arg07, _arg13);
                            return true;
                        case 8:
                            onTransitionStart();
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _arg08 = data.readInt();
                            int _arg14 = data.readInt();
                            Rect _arg23 = (Rect) data.readTypedObject(Rect.CREATOR);
                            int _arg3 = data.readInt();
                            Bundle _arg4 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            IRecentsAnimationController _arg5 = IRecentsAnimationController.Stub.asInterface(data.readStrongBinder());
                            boolean _arg6 = data.readBoolean();
                            boolean _arg7 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result3 = recentAnimationFinished(_arg08, _arg14, _arg23, _arg3, _arg4, _arg5, _arg6, _arg7);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 10:
                            int _arg09 = data.readInt();
                            IOplusZoomTaskInfoCallback _arg15 = IOplusZoomTaskInfoCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            getCurrentZoomInfo(_arg09, _arg15);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusZoomTaskController {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusZoomTaskController.DESCRIPTOR;
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public boolean registerZoomTaskListener(IOplusZoomTaskListener zoomTaskListener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeStrongInterface(zoomTaskListener);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public boolean unregisterZoomTaskListener(IOplusZoomTaskListener zoomTaskListener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeStrongInterface(zoomTaskListener);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void onZoomStateChanged(OplusZoomTaskInfo zoomTaskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeTypedObject(zoomTaskInfo, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void onZoomRotateChanged(int fromRotation, int toRotation, OplusZoomTaskInfo zoomTaskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeInt(fromRotation);
                    _data.writeInt(toRotation);
                    _data.writeTypedObject(zoomTaskInfo, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void requestChangeZoomTask(WindowContainerToken token, int flag, boolean anim) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeTypedObject(token, 0);
                    _data.writeInt(flag);
                    _data.writeBoolean(anim);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void onTransitionEnd(int seq) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeInt(seq);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void setInputToken(WindowContainerToken token, IBinder channelToken) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeTypedObject(token, 0);
                    _data.writeStrongBinder(channelToken);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void onTransitionStart() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public boolean recentAnimationFinished(int taskId, int type, Rect rect, int orientation, Bundle bOptions, IRecentsAnimationController controller, boolean moveHomeToTop, boolean sendUserLeaveHint) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(type);
                    _data.writeTypedObject(rect, 0);
                    _data.writeInt(orientation);
                    _data.writeTypedObject(bOptions, 0);
                    _data.writeStrongInterface(controller);
                    _data.writeBoolean(moveHomeToTop);
                    _data.writeBoolean(sendUserLeaveHint);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskController
            public void getCurrentZoomInfo(int taskId, IOplusZoomTaskInfoCallback oplusZoomTaskInfoCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskController.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeStrongInterface(oplusZoomTaskInfoCallback);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 9;
        }
    }
}
