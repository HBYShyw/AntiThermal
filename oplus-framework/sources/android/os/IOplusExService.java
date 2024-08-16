package android.os;

import android.graphics.RectF;
import android.os.IOplusExInputCallBack;
import android.os.IOplusGestureCallBack;
import com.oplus.globalgesture.IOplusGlobalGestureObserver;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusExService extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusExService";

    void dealScreenoffGesture(int i) throws RemoteException;

    boolean getGestureState(int i) throws RemoteException;

    void pauseExInputEvent() throws RemoteException;

    void pilferPointers() throws RemoteException;

    boolean registerGlobalGestureObserver(IOplusGlobalGestureObserver iOplusGlobalGestureObserver, String str, int i, List<RectF> list) throws RemoteException;

    boolean registerInputEvent(IOplusExInputCallBack iOplusExInputCallBack) throws RemoteException;

    boolean registerRawInputEvent(IOplusExInputCallBack iOplusExInputCallBack) throws RemoteException;

    boolean registerScreenoffGesture(IOplusGestureCallBack iOplusGestureCallBack) throws RemoteException;

    void resumeExInputEvent() throws RemoteException;

    void setGestureState(int i, boolean z) throws RemoteException;

    boolean unregisterGlobalGestureObserver(IOplusGlobalGestureObserver iOplusGlobalGestureObserver) throws RemoteException;

    void unregisterInputEvent(IOplusExInputCallBack iOplusExInputCallBack) throws RemoteException;

    void unregisterScreenoffGesture(IOplusGestureCallBack iOplusGestureCallBack) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusExService {
        @Override // android.os.IOplusExService
        public boolean registerInputEvent(IOplusExInputCallBack callBack) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusExService
        public boolean registerRawInputEvent(IOplusExInputCallBack callBack) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusExService
        public void unregisterInputEvent(IOplusExInputCallBack callBack) throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public void pauseExInputEvent() throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public void resumeExInputEvent() throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public boolean registerScreenoffGesture(IOplusGestureCallBack callBack) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusExService
        public void unregisterScreenoffGesture(IOplusGestureCallBack callBack) throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public void dealScreenoffGesture(int nGesture) throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public void setGestureState(int nGesture, boolean isOpen) throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public boolean getGestureState(int nGesture) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusExService
        public void pilferPointers() throws RemoteException {
        }

        @Override // android.os.IOplusExService
        public boolean registerGlobalGestureObserver(IOplusGlobalGestureObserver observer, String tag, int config, List<RectF> validRegion) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusExService
        public boolean unregisterGlobalGestureObserver(IOplusGlobalGestureObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusExService {
        static final int TRANSACTION_dealScreenoffGesture = 8;
        static final int TRANSACTION_getGestureState = 10;
        static final int TRANSACTION_pauseExInputEvent = 4;
        static final int TRANSACTION_pilferPointers = 11;
        static final int TRANSACTION_registerGlobalGestureObserver = 12;
        static final int TRANSACTION_registerInputEvent = 1;
        static final int TRANSACTION_registerRawInputEvent = 2;
        static final int TRANSACTION_registerScreenoffGesture = 6;
        static final int TRANSACTION_resumeExInputEvent = 5;
        static final int TRANSACTION_setGestureState = 9;
        static final int TRANSACTION_unregisterGlobalGestureObserver = 13;
        static final int TRANSACTION_unregisterInputEvent = 3;
        static final int TRANSACTION_unregisterScreenoffGesture = 7;

        public Stub() {
            attachInterface(this, IOplusExService.DESCRIPTOR);
        }

        public static IOplusExService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusExService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusExService)) {
                return (IOplusExService) iin;
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
                    return "registerInputEvent";
                case 2:
                    return "registerRawInputEvent";
                case 3:
                    return "unregisterInputEvent";
                case 4:
                    return "pauseExInputEvent";
                case 5:
                    return "resumeExInputEvent";
                case 6:
                    return "registerScreenoffGesture";
                case 7:
                    return "unregisterScreenoffGesture";
                case 8:
                    return "dealScreenoffGesture";
                case 9:
                    return "setGestureState";
                case 10:
                    return "getGestureState";
                case 11:
                    return "pilferPointers";
                case 12:
                    return "registerGlobalGestureObserver";
                case 13:
                    return "unregisterGlobalGestureObserver";
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
                data.enforceInterface(IOplusExService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusExService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusExInputCallBack _arg0 = IOplusExInputCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = registerInputEvent(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            IOplusExInputCallBack _arg02 = IOplusExInputCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = registerRawInputEvent(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            IOplusExInputCallBack _arg03 = IOplusExInputCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterInputEvent(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            pauseExInputEvent();
                            reply.writeNoException();
                            return true;
                        case 5:
                            resumeExInputEvent();
                            reply.writeNoException();
                            return true;
                        case 6:
                            IOplusGestureCallBack _arg04 = IOplusGestureCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result3 = registerScreenoffGesture(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 7:
                            IOplusGestureCallBack _arg05 = IOplusGestureCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterScreenoffGesture(_arg05);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            dealScreenoffGesture(_arg06);
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _arg07 = data.readInt();
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setGestureState(_arg07, _arg1);
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _arg08 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = getGestureState(_arg08);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 11:
                            pilferPointers();
                            reply.writeNoException();
                            return true;
                        case 12:
                            IOplusGlobalGestureObserver _arg09 = IOplusGlobalGestureObserver.Stub.asInterface(data.readStrongBinder());
                            String _arg12 = data.readString();
                            int _arg2 = data.readInt();
                            List<RectF> _arg3 = data.createTypedArrayList(RectF.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result5 = registerGlobalGestureObserver(_arg09, _arg12, _arg2, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 13:
                            IOplusGlobalGestureObserver _arg010 = IOplusGlobalGestureObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result6 = unregisterGlobalGestureObserver(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusExService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusExService.DESCRIPTOR;
            }

            @Override // android.os.IOplusExService
            public boolean registerInputEvent(IOplusExInputCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public boolean registerRawInputEvent(IOplusExInputCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void unregisterInputEvent(IOplusExInputCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void pauseExInputEvent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void resumeExInputEvent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public boolean registerScreenoffGesture(IOplusGestureCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void unregisterScreenoffGesture(IOplusGestureCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void dealScreenoffGesture(int nGesture) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeInt(nGesture);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void setGestureState(int nGesture, boolean isOpen) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeInt(nGesture);
                    _data.writeBoolean(isOpen);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public boolean getGestureState(int nGesture) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeInt(nGesture);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public void pilferPointers() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public boolean registerGlobalGestureObserver(IOplusGlobalGestureObserver observer, String tag, int config, List<RectF> validRegion) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    _data.writeString(tag);
                    _data.writeInt(config);
                    _data.writeTypedList(validRegion, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusExService
            public boolean unregisterGlobalGestureObserver(IOplusGlobalGestureObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExService.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 12;
        }
    }
}
