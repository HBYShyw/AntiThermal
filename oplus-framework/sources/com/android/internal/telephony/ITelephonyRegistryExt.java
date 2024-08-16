package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import com.android.internal.telephony.IPhoneStateListenerExt;
import com.oplus.virtualcomm.VirtualCommServiceState;

/* loaded from: classes.dex */
public interface ITelephonyRegistryExt extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.ITelephonyRegistryExt";

    boolean listenWithEventList(boolean z, boolean z2, int i, String str, String str2, IPhoneStateListenerExt iPhoneStateListenerExt, int[] iArr, boolean z3) throws RemoteException;

    void notifyForRemainTimeReported(int i, String str) throws RemoteException;

    void notifyNRIconTypeChanged(int i, int i2) throws RemoteException;

    void notifyPlmnCarrierConfigChanged(int i, PersistableBundle persistableBundle) throws RemoteException;

    void notifyPollCsPsInService(int i, int i2) throws RemoteException;

    void notifyRealtimeOos(int i, boolean z) throws RemoteException;

    void notifySimRecovery(int i, int i2) throws RemoteException;

    void notifyVirtualCommEnabledChanged(boolean[] zArr) throws RemoteException;

    void notifyVirtualCommServiceStateChanged(VirtualCommServiceState virtualCommServiceState) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITelephonyRegistryExt {
        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public boolean listenWithEventList(boolean renounceFineLocationAccess, boolean renounceCoarseLocationAccess, int slotId, String pkg, String featureId, IPhoneStateListenerExt callback, int[] events, boolean notifyNow) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyPlmnCarrierConfigChanged(int slotId, PersistableBundle result) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyNRIconTypeChanged(int slotId, int type) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyVirtualCommEnabledChanged(boolean[] enabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyVirtualCommServiceStateChanged(VirtualCommServiceState serviceState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyForRemainTimeReported(int phoneId, String remainTimeData) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyPollCsPsInService(int slotId, int domain) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifySimRecovery(int slotId, int stat) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistryExt
        public void notifyRealtimeOos(int slotId, boolean oos) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITelephonyRegistryExt {
        static final int TRANSACTION_listenWithEventList = 1;
        static final int TRANSACTION_notifyForRemainTimeReported = 6;
        static final int TRANSACTION_notifyNRIconTypeChanged = 3;
        static final int TRANSACTION_notifyPlmnCarrierConfigChanged = 2;
        static final int TRANSACTION_notifyPollCsPsInService = 7;
        static final int TRANSACTION_notifyRealtimeOos = 9;
        static final int TRANSACTION_notifySimRecovery = 8;
        static final int TRANSACTION_notifyVirtualCommEnabledChanged = 4;
        static final int TRANSACTION_notifyVirtualCommServiceStateChanged = 5;

        public Stub() {
            attachInterface(this, ITelephonyRegistryExt.DESCRIPTOR);
        }

        public static ITelephonyRegistryExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ITelephonyRegistryExt.DESCRIPTOR);
            if (iin != null && (iin instanceof ITelephonyRegistryExt)) {
                return (ITelephonyRegistryExt) iin;
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
                    return "listenWithEventList";
                case 2:
                    return "notifyPlmnCarrierConfigChanged";
                case 3:
                    return "notifyNRIconTypeChanged";
                case 4:
                    return "notifyVirtualCommEnabledChanged";
                case 5:
                    return "notifyVirtualCommServiceStateChanged";
                case 6:
                    return "notifyForRemainTimeReported";
                case 7:
                    return "notifyPollCsPsInService";
                case 8:
                    return "notifySimRecovery";
                case 9:
                    return "notifyRealtimeOos";
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
                data.enforceInterface(ITelephonyRegistryExt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ITelephonyRegistryExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            boolean _arg1 = data.readBoolean();
                            int _arg2 = data.readInt();
                            String _arg3 = data.readString();
                            String _arg4 = data.readString();
                            IPhoneStateListenerExt _arg5 = IPhoneStateListenerExt.Stub.asInterface(data.readStrongBinder());
                            int[] _arg6 = data.createIntArray();
                            boolean _arg7 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result = listenWithEventList(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            PersistableBundle _arg12 = (PersistableBundle) data.readTypedObject(PersistableBundle.CREATOR);
                            data.enforceNoDataAvail();
                            notifyPlmnCarrierConfigChanged(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyNRIconTypeChanged(_arg03, _arg13);
                            reply.writeNoException();
                            return true;
                        case 4:
                            boolean[] _arg04 = data.createBooleanArray();
                            data.enforceNoDataAvail();
                            notifyVirtualCommEnabledChanged(_arg04);
                            reply.writeNoException();
                            return true;
                        case 5:
                            VirtualCommServiceState _arg05 = (VirtualCommServiceState) data.readTypedObject(VirtualCommServiceState.CREATOR);
                            data.enforceNoDataAvail();
                            notifyVirtualCommServiceStateChanged(_arg05);
                            reply.writeNoException();
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            notifyForRemainTimeReported(_arg06, _arg14);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyPollCsPsInService(_arg07, _arg15);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            notifySimRecovery(_arg08, _arg16);
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            boolean _arg17 = data.readBoolean();
                            data.enforceNoDataAvail();
                            notifyRealtimeOos(_arg09, _arg17);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ITelephonyRegistryExt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ITelephonyRegistryExt.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public boolean listenWithEventList(boolean renounceFineLocationAccess, boolean renounceCoarseLocationAccess, int slotId, String pkg, String featureId, IPhoneStateListenerExt callback, int[] events, boolean notifyNow) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeBoolean(renounceFineLocationAccess);
                    _data.writeBoolean(renounceCoarseLocationAccess);
                    _data.writeInt(slotId);
                    _data.writeString(pkg);
                    _data.writeString(featureId);
                    _data.writeStrongInterface(callback);
                    _data.writeIntArray(events);
                    _data.writeBoolean(notifyNow);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyPlmnCarrierConfigChanged(int slotId, PersistableBundle result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeTypedObject(result, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyNRIconTypeChanged(int slotId, int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(type);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyVirtualCommEnabledChanged(boolean[] enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeBooleanArray(enabled);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyVirtualCommServiceStateChanged(VirtualCommServiceState serviceState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeTypedObject(serviceState, 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyForRemainTimeReported(int phoneId, String remainTimeData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(remainTimeData);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyPollCsPsInService(int slotId, int domain) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(domain);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifySimRecovery(int slotId, int stat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(stat);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistryExt
            public void notifyRealtimeOos(int slotId, boolean oos) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITelephonyRegistryExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeBoolean(oos);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 8;
        }
    }
}
