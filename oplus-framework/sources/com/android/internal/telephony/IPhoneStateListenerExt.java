package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import com.oplus.virtualcomm.VirtualCommServiceState;

/* loaded from: classes.dex */
public interface IPhoneStateListenerExt extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneStateListenerExt";

    void onImsRemainTimeReported(String str) throws RemoteException;

    void onNRIconTypeChanged(int i, int i2) throws RemoteException;

    void onPlmnCarrierConfigChanged(int i, PersistableBundle persistableBundle) throws RemoteException;

    void onPollCsPsInService(int i, int i2) throws RemoteException;

    void onRealtimeOos(int i, boolean z) throws RemoteException;

    void onSimRecovery(int i, int i2) throws RemoteException;

    void onVMEnabledChanged(boolean[] zArr) throws RemoteException;

    void onVMServiceStateChanged(VirtualCommServiceState virtualCommServiceState) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPhoneStateListenerExt {
        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onPlmnCarrierConfigChanged(int slotId, PersistableBundle result) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onNRIconTypeChanged(int slotId, int type) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onVMEnabledChanged(boolean[] enabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onVMServiceStateChanged(VirtualCommServiceState serviceState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onImsRemainTimeReported(String remainTimeData) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onPollCsPsInService(int slotId, int domain) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onSimRecovery(int slotId, int stat) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onRealtimeOos(int phoneId, boolean oos) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPhoneStateListenerExt {
        static final int TRANSACTION_onImsRemainTimeReported = 5;
        static final int TRANSACTION_onNRIconTypeChanged = 2;
        static final int TRANSACTION_onPlmnCarrierConfigChanged = 1;
        static final int TRANSACTION_onPollCsPsInService = 6;
        static final int TRANSACTION_onRealtimeOos = 8;
        static final int TRANSACTION_onSimRecovery = 7;
        static final int TRANSACTION_onVMEnabledChanged = 3;
        static final int TRANSACTION_onVMServiceStateChanged = 4;

        public Stub() {
            attachInterface(this, IPhoneStateListenerExt.DESCRIPTOR);
        }

        public static IPhoneStateListenerExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPhoneStateListenerExt.DESCRIPTOR);
            if (iin != null && (iin instanceof IPhoneStateListenerExt)) {
                return (IPhoneStateListenerExt) iin;
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
                    return "onPlmnCarrierConfigChanged";
                case 2:
                    return "onNRIconTypeChanged";
                case 3:
                    return "onVMEnabledChanged";
                case 4:
                    return "onVMServiceStateChanged";
                case 5:
                    return "onImsRemainTimeReported";
                case 6:
                    return "onPollCsPsInService";
                case 7:
                    return "onSimRecovery";
                case 8:
                    return "onRealtimeOos";
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
                data.enforceInterface(IPhoneStateListenerExt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPhoneStateListenerExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            PersistableBundle _arg1 = (PersistableBundle) data.readTypedObject(PersistableBundle.CREATOR);
                            data.enforceNoDataAvail();
                            onPlmnCarrierConfigChanged(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            onNRIconTypeChanged(_arg02, _arg12);
                            return true;
                        case 3:
                            boolean[] _arg03 = data.createBooleanArray();
                            data.enforceNoDataAvail();
                            onVMEnabledChanged(_arg03);
                            return true;
                        case 4:
                            VirtualCommServiceState _arg04 = (VirtualCommServiceState) data.readTypedObject(VirtualCommServiceState.CREATOR);
                            data.enforceNoDataAvail();
                            onVMServiceStateChanged(_arg04);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            onImsRemainTimeReported(_arg05);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            onPollCsPsInService(_arg06, _arg13);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            onSimRecovery(_arg07, _arg14);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            boolean _arg15 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onRealtimeOos(_arg08, _arg15);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPhoneStateListenerExt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPhoneStateListenerExt.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onPlmnCarrierConfigChanged(int slotId, PersistableBundle result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeTypedObject(result, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onNRIconTypeChanged(int slotId, int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(type);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onVMEnabledChanged(boolean[] enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeBooleanArray(enabled);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onVMServiceStateChanged(VirtualCommServiceState serviceState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeTypedObject(serviceState, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onImsRemainTimeReported(String remainTimeData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeString(remainTimeData);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onPollCsPsInService(int slotId, int domain) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(domain);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onSimRecovery(int slotId, int stat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(stat);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListenerExt
            public void onRealtimeOos(int phoneId, boolean oos) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPhoneStateListenerExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(oos);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 7;
        }
    }
}
