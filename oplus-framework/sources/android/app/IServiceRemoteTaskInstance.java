package android.app;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IServiceRemoteTaskInstance extends IInterface {
    public static final String DESCRIPTOR = "android.app.IServiceRemoteTaskInstance";

    void changeRemoteTaskFocus(boolean z) throws RemoteException;

    Bundle getBundle() throws RemoteException;

    int getOrientation() throws RemoteException;

    Rect getRemoteTaskBounds() throws RemoteException;

    String getRemoteTaskInstancePackageId() throws RemoteException;

    String getRemoteTaskInstanceUUID() throws RemoteException;

    Intent getRemoteTaskIntent() throws RemoteException;

    boolean isRemoteTaskInstanceInFocus() throws RemoteException;

    boolean isRemoteTaskInstanceShowingSecuredContent() throws RemoteException;

    boolean isRemoteTaskResizeable() throws RemoteException;

    boolean isTouchRequired() throws RemoteException;

    void notifyRemoteDisplayOrientationChanged(int i) throws RemoteException;

    void notifyRemoteDisplayResolutionChanged(Rect rect, int i) throws RemoteException;

    void notifyRemoteTaskInputEvent() throws RemoteException;

    void removeRemoteTask() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IServiceRemoteTaskInstance {
        @Override // android.app.IServiceRemoteTaskInstance
        public boolean isTouchRequired() throws RemoteException {
            return false;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public int getOrientation() throws RemoteException {
            return 0;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public Rect getRemoteTaskBounds() throws RemoteException {
            return null;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public boolean isRemoteTaskResizeable() throws RemoteException {
            return false;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public String getRemoteTaskInstanceUUID() throws RemoteException {
            return null;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public Intent getRemoteTaskIntent() throws RemoteException {
            return null;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public String getRemoteTaskInstancePackageId() throws RemoteException {
            return null;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public boolean isRemoteTaskInstanceShowingSecuredContent() throws RemoteException {
            return false;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public boolean isRemoteTaskInstanceInFocus() throws RemoteException {
            return false;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public Bundle getBundle() throws RemoteException {
            return null;
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public void removeRemoteTask() throws RemoteException {
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public void changeRemoteTaskFocus(boolean inFocus) throws RemoteException {
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public void notifyRemoteDisplayResolutionChanged(Rect newBounds, int newDPI) throws RemoteException {
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public void notifyRemoteDisplayOrientationChanged(int newOrientation) throws RemoteException {
        }

        @Override // android.app.IServiceRemoteTaskInstance
        public void notifyRemoteTaskInputEvent() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IServiceRemoteTaskInstance {
        static final int TRANSACTION_changeRemoteTaskFocus = 12;
        static final int TRANSACTION_getBundle = 10;
        static final int TRANSACTION_getOrientation = 2;
        static final int TRANSACTION_getRemoteTaskBounds = 3;
        static final int TRANSACTION_getRemoteTaskInstancePackageId = 7;
        static final int TRANSACTION_getRemoteTaskInstanceUUID = 5;
        static final int TRANSACTION_getRemoteTaskIntent = 6;
        static final int TRANSACTION_isRemoteTaskInstanceInFocus = 9;
        static final int TRANSACTION_isRemoteTaskInstanceShowingSecuredContent = 8;
        static final int TRANSACTION_isRemoteTaskResizeable = 4;
        static final int TRANSACTION_isTouchRequired = 1;
        static final int TRANSACTION_notifyRemoteDisplayOrientationChanged = 14;
        static final int TRANSACTION_notifyRemoteDisplayResolutionChanged = 13;
        static final int TRANSACTION_notifyRemoteTaskInputEvent = 15;
        static final int TRANSACTION_removeRemoteTask = 11;

        public Stub() {
            attachInterface(this, IServiceRemoteTaskInstance.DESCRIPTOR);
        }

        public static IServiceRemoteTaskInstance asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IServiceRemoteTaskInstance.DESCRIPTOR);
            if (iin != null && (iin instanceof IServiceRemoteTaskInstance)) {
                return (IServiceRemoteTaskInstance) iin;
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
                    return "isTouchRequired";
                case 2:
                    return "getOrientation";
                case 3:
                    return "getRemoteTaskBounds";
                case 4:
                    return "isRemoteTaskResizeable";
                case 5:
                    return "getRemoteTaskInstanceUUID";
                case 6:
                    return "getRemoteTaskIntent";
                case 7:
                    return "getRemoteTaskInstancePackageId";
                case 8:
                    return "isRemoteTaskInstanceShowingSecuredContent";
                case 9:
                    return "isRemoteTaskInstanceInFocus";
                case 10:
                    return "getBundle";
                case 11:
                    return "removeRemoteTask";
                case 12:
                    return "changeRemoteTaskFocus";
                case 13:
                    return "notifyRemoteDisplayResolutionChanged";
                case 14:
                    return "notifyRemoteDisplayOrientationChanged";
                case 15:
                    return "notifyRemoteTaskInputEvent";
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
                data.enforceInterface(IServiceRemoteTaskInstance.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IServiceRemoteTaskInstance.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _result = isTouchRequired();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            int _result2 = getOrientation();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            Rect _result3 = getRemoteTaskBounds();
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 4:
                            boolean _result4 = isRemoteTaskResizeable();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            String _result5 = getRemoteTaskInstanceUUID();
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 6:
                            Intent _result6 = getRemoteTaskIntent();
                            reply.writeNoException();
                            reply.writeTypedObject(_result6, 1);
                            return true;
                        case 7:
                            String _result7 = getRemoteTaskInstancePackageId();
                            reply.writeNoException();
                            reply.writeString(_result7);
                            return true;
                        case 8:
                            boolean _result8 = isRemoteTaskInstanceShowingSecuredContent();
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 9:
                            boolean _result9 = isRemoteTaskInstanceInFocus();
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 10:
                            Bundle _result10 = getBundle();
                            reply.writeNoException();
                            reply.writeTypedObject(_result10, 1);
                            return true;
                        case 11:
                            removeRemoteTask();
                            return true;
                        case 12:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            changeRemoteTaskFocus(_arg0);
                            return true;
                        case 13:
                            Rect _arg02 = (Rect) data.readTypedObject(Rect.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyRemoteDisplayResolutionChanged(_arg02, _arg1);
                            return true;
                        case 14:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyRemoteDisplayOrientationChanged(_arg03);
                            return true;
                        case 15:
                            notifyRemoteTaskInputEvent();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IServiceRemoteTaskInstance {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IServiceRemoteTaskInstance.DESCRIPTOR;
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public boolean isTouchRequired() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public int getOrientation() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public Rect getRemoteTaskBounds() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    Rect _result = (Rect) _reply.readTypedObject(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public boolean isRemoteTaskResizeable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public String getRemoteTaskInstanceUUID() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public Intent getRemoteTaskIntent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    Intent _result = (Intent) _reply.readTypedObject(Intent.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public String getRemoteTaskInstancePackageId() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public boolean isRemoteTaskInstanceShowingSecuredContent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public boolean isRemoteTaskInstanceInFocus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public Bundle getBundle() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public void removeRemoteTask() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public void changeRemoteTaskFocus(boolean inFocus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    _data.writeBoolean(inFocus);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public void notifyRemoteDisplayResolutionChanged(Rect newBounds, int newDPI) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    _data.writeTypedObject(newBounds, 0);
                    _data.writeInt(newDPI);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public void notifyRemoteDisplayOrientationChanged(int newOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    _data.writeInt(newOrientation);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IServiceRemoteTaskInstance
            public void notifyRemoteTaskInputEvent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IServiceRemoteTaskInstance.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 14;
        }
    }
}
