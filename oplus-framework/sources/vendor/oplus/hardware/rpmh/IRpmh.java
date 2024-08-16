package vendor.oplus.hardware.rpmh;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface IRpmh extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$rpmh$IRpmh".replace('$', '.');
    public static final String HASH = "acf5f9a3960fb9a2f76f86947dff5ca9e0c86e05";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int getPowerStateSubsystemSleepState(String str, PowerStateSubsystemSleepState powerStateSubsystemSleepState) throws RemoteException;

    int getPowerStateSubsystemSleepStateList(List<PowerStateSubsystemSleepState> list) throws RemoteException;

    int triggerDumpType(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IRpmh {
        @Override // vendor.oplus.hardware.rpmh.IRpmh
        public int getPowerStateSubsystemSleepState(String name, PowerStateSubsystemSleepState subsystem) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.rpmh.IRpmh
        public int getPowerStateSubsystemSleepStateList(List<PowerStateSubsystemSleepState> subsystemList) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.rpmh.IRpmh
        public int triggerDumpType(int type) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.rpmh.IRpmh
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.rpmh.IRpmh
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IRpmh {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPowerStateSubsystemSleepState = 1;
        static final int TRANSACTION_getPowerStateSubsystemSleepStateList = 2;
        static final int TRANSACTION_triggerDumpType = 3;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IRpmh asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IRpmh)) {
                return (IRpmh) iin;
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
                    return "getPowerStateSubsystemSleepState";
                case 2:
                    return "getPowerStateSubsystemSleepStateList";
                case 3:
                    return "triggerDumpType";
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code >= 1 && code <= TRANSACTION_getInterfaceVersion) {
                data.enforceInterface(descriptor);
            }
            switch (code) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    reply.writeNoException();
                    reply.writeString(getInterfaceHash());
                    return true;
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    reply.writeNoException();
                    reply.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            PowerStateSubsystemSleepState _arg1 = new PowerStateSubsystemSleepState();
                            data.enforceNoDataAvail();
                            int _result = getPowerStateSubsystemSleepState(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            reply.writeTypedObject(_arg1, 1);
                            return true;
                        case 2:
                            ArrayList arrayList = new ArrayList();
                            data.enforceNoDataAvail();
                            int _result2 = getPowerStateSubsystemSleepStateList(arrayList);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            reply.writeTypedList(arrayList, 1);
                            return true;
                        case 3:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result3 = triggerDumpType(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRpmh {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.rpmh.IRpmh
            public int getPowerStateSubsystemSleepState(String name, PowerStateSubsystemSleepState subsystem) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method getPowerStateSubsystemSleepState is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        subsystem.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.rpmh.IRpmh
            public int getPowerStateSubsystemSleepStateList(List<PowerStateSubsystemSleepState> subsystemList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method getPowerStateSubsystemSleepStateList is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readTypedList(subsystemList, PowerStateSubsystemSleepState.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.rpmh.IRpmh
            public int triggerDumpType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method triggerDumpType is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.rpmh.IRpmh
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.oplus.hardware.rpmh.IRpmh
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }
    }
}
