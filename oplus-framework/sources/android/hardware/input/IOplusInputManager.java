package android.hardware.input;

import android.hardware.input.IOplusInputJitterObserver;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public interface IOplusInputManager extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.input.IOplusInputManager";

    void injectShoulderTouchEvent(MotionEvent motionEvent, int i) throws RemoteException;

    void registerAppDeathListener(IBinder iBinder) throws RemoteException;

    void registerOplusInputJitterObserver(IOplusInputJitterObserver iOplusInputJitterObserver) throws RemoteException;

    boolean resetInputReportingThreshold() throws RemoteException;

    boolean setInputReportingThreshold(String str, String str2, float f, float f2) throws RemoteException;

    void setNeedMergeTouchEvent(boolean z) throws RemoteException;

    void setNumberByInput(String str) throws RemoteException;

    boolean setQuickTouchOptimization(int i) throws RemoteException;

    boolean setShoulderTouchInfo(Bundle bundle) throws RemoteException;

    void unRegisterAppDeathListener(IBinder iBinder) throws RemoteException;

    void unregisterOplusInputJitterObserver(IOplusInputJitterObserver iOplusInputJitterObserver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusInputManager {
        @Override // android.hardware.input.IOplusInputManager
        public void injectShoulderTouchEvent(MotionEvent motionEvent, int mode) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public void setNeedMergeTouchEvent(boolean enabled) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public boolean setShoulderTouchInfo(Bundle info) throws RemoteException {
            return false;
        }

        @Override // android.hardware.input.IOplusInputManager
        public void registerAppDeathListener(IBinder client) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public void unRegisterAppDeathListener(IBinder client) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public boolean setInputReportingThreshold(String trackerEnabled, String statisticsEnabled, float latencyThreshold, float latencyIntervalThreshold) throws RemoteException {
            return false;
        }

        @Override // android.hardware.input.IOplusInputManager
        public boolean resetInputReportingThreshold() throws RemoteException {
            return false;
        }

        @Override // android.hardware.input.IOplusInputManager
        public void registerOplusInputJitterObserver(IOplusInputJitterObserver observer) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public void unregisterOplusInputJitterObserver(IOplusInputJitterObserver observer) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public void setNumberByInput(String code) throws RemoteException {
        }

        @Override // android.hardware.input.IOplusInputManager
        public boolean setQuickTouchOptimization(int optionStatus) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusInputManager {
        static final int TRANSACTION_injectShoulderTouchEvent = 1;
        static final int TRANSACTION_registerAppDeathListener = 4;
        static final int TRANSACTION_registerOplusInputJitterObserver = 8;
        static final int TRANSACTION_resetInputReportingThreshold = 7;
        static final int TRANSACTION_setInputReportingThreshold = 6;
        static final int TRANSACTION_setNeedMergeTouchEvent = 2;
        static final int TRANSACTION_setNumberByInput = 10;
        static final int TRANSACTION_setQuickTouchOptimization = 11;
        static final int TRANSACTION_setShoulderTouchInfo = 3;
        static final int TRANSACTION_unRegisterAppDeathListener = 5;
        static final int TRANSACTION_unregisterOplusInputJitterObserver = 9;

        public Stub() {
            attachInterface(this, IOplusInputManager.DESCRIPTOR);
        }

        public static IOplusInputManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusInputManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusInputManager)) {
                return (IOplusInputManager) iin;
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
                    return "injectShoulderTouchEvent";
                case 2:
                    return "setNeedMergeTouchEvent";
                case 3:
                    return "setShoulderTouchInfo";
                case 4:
                    return "registerAppDeathListener";
                case 5:
                    return "unRegisterAppDeathListener";
                case 6:
                    return "setInputReportingThreshold";
                case 7:
                    return "resetInputReportingThreshold";
                case 8:
                    return "registerOplusInputJitterObserver";
                case 9:
                    return "unregisterOplusInputJitterObserver";
                case 10:
                    return "setNumberByInput";
                case 11:
                    return "setQuickTouchOptimization";
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
                data.enforceInterface(IOplusInputManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusInputManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            MotionEvent _arg0 = (MotionEvent) data.readTypedObject(MotionEvent.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            injectShoulderTouchEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setNeedMergeTouchEvent(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            Bundle _arg03 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result = setShoulderTouchInfo(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 4:
                            IBinder _arg04 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            registerAppDeathListener(_arg04);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IBinder _arg05 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            unRegisterAppDeathListener(_arg05);
                            reply.writeNoException();
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            String _arg12 = data.readString();
                            float _arg2 = data.readFloat();
                            float _arg3 = data.readFloat();
                            data.enforceNoDataAvail();
                            boolean _result2 = setInputReportingThreshold(_arg06, _arg12, _arg2, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 7:
                            boolean _result3 = resetInputReportingThreshold();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 8:
                            IOplusInputJitterObserver _arg07 = IOplusInputJitterObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusInputJitterObserver(_arg07);
                            reply.writeNoException();
                            return true;
                        case 9:
                            IOplusInputJitterObserver _arg08 = IOplusInputJitterObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterOplusInputJitterObserver(_arg08);
                            reply.writeNoException();
                            return true;
                        case 10:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            setNumberByInput(_arg09);
                            reply.writeNoException();
                            return true;
                        case 11:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = setQuickTouchOptimization(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusInputManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusInputManager.DESCRIPTOR;
            }

            @Override // android.hardware.input.IOplusInputManager
            public void injectShoulderTouchEvent(MotionEvent motionEvent, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeTypedObject(motionEvent, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public void setNeedMergeTouchEvent(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public boolean setShoulderTouchInfo(Bundle info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public void registerAppDeathListener(IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeStrongBinder(client);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public void unRegisterAppDeathListener(IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeStrongBinder(client);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public boolean setInputReportingThreshold(String trackerEnabled, String statisticsEnabled, float latencyThreshold, float latencyIntervalThreshold) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeString(trackerEnabled);
                    _data.writeString(statisticsEnabled);
                    _data.writeFloat(latencyThreshold);
                    _data.writeFloat(latencyIntervalThreshold);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public boolean resetInputReportingThreshold() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public void registerOplusInputJitterObserver(IOplusInputJitterObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public void unregisterOplusInputJitterObserver(IOplusInputJitterObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public void setNumberByInput(String code) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeString(code);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.input.IOplusInputManager
            public boolean setQuickTouchOptimization(int optionStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusInputManager.DESCRIPTOR);
                    _data.writeInt(optionStatus);
                    this.mRemote.transact(11, _data, _reply, 0);
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
            return 10;
        }
    }
}
