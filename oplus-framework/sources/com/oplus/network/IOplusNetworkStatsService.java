package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.stats.AppFreezeConfig;
import com.oplus.network.stats.AppFreezeStatsTotal;
import com.oplus.network.stats.AppFreezeSyncTotal;
import com.oplus.network.stats.IfaceSpeedsValueTotal;
import com.oplus.network.stats.IfaceUidStatsTotal;
import com.oplus.network.stats.SpeedsValueTotal;
import com.oplus.network.stats.StatsValue;
import com.oplus.network.stats.StatsValueTotal;

/* loaded from: classes.dex */
public interface IOplusNetworkStatsService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusNetworkStatsService";

    AppFreezeStatsTotal fetchAppFreezeStats() throws RemoteException;

    AppFreezeSyncTotal fetchAppFreezeSyns() throws RemoteException;

    AppFreezeConfig getAppFreezeConfig() throws RemoteException;

    int getDenyFlag(int i) throws RemoteException;

    StatsValue getIfaceStats(String str) throws RemoteException;

    IfaceUidStatsTotal getIfnameUidStatsTotal() throws RemoteException;

    int getSocketIsLocal(long j) throws RemoteException;

    SpeedsValueTotal getSocketSpeedsTotal(int i, int i2, long[] jArr) throws RemoteException;

    StatsValue getUidPurStats(int i) throws RemoteException;

    StatsValueTotal getUidPurStatsTotal() throws RemoteException;

    IfaceSpeedsValueTotal getUidSpeedsIfindex(int i) throws RemoteException;

    StatsValueTotal getUidStatsTotal() throws RemoteException;

    boolean setAppFreezeConfig(AppFreezeConfig appFreezeConfig) throws RemoteException;

    int setBpfSocketSpeedsConfig(int i) throws RemoteException;

    int startBpfSocketSpeedsCalc(int i, boolean z) throws RemoteException;

    int stopBpfSocketSpeedsCalc(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNetworkStatsService {
        @Override // com.oplus.network.IOplusNetworkStatsService
        public StatsValueTotal getUidPurStatsTotal() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public StatsValue getUidPurStats(int uid) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public AppFreezeSyncTotal fetchAppFreezeSyns() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public AppFreezeStatsTotal fetchAppFreezeStats() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public AppFreezeConfig getAppFreezeConfig() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public boolean setAppFreezeConfig(AppFreezeConfig cfg) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public SpeedsValueTotal getSocketSpeedsTotal(int clearIntervals, int uploadSpeed, long[] limitCookies) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public IfaceSpeedsValueTotal getUidSpeedsIfindex(int clearIntervals) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public int startBpfSocketSpeedsCalc(int ifindex, boolean localBypass) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public int stopBpfSocketSpeedsCalc(int ifindex) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public int setBpfSocketSpeedsConfig(int speedsCalcInv) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public int getSocketIsLocal(long socketCookie) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public StatsValueTotal getUidStatsTotal() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public IfaceUidStatsTotal getIfnameUidStatsTotal() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public StatsValue getIfaceStats(String ifname) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkStatsService
        public int getDenyFlag(int uid) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNetworkStatsService {
        static final int TRANSACTION_fetchAppFreezeStats = 4;
        static final int TRANSACTION_fetchAppFreezeSyns = 3;
        static final int TRANSACTION_getAppFreezeConfig = 5;
        static final int TRANSACTION_getDenyFlag = 16;
        static final int TRANSACTION_getIfaceStats = 15;
        static final int TRANSACTION_getIfnameUidStatsTotal = 14;
        static final int TRANSACTION_getSocketIsLocal = 12;
        static final int TRANSACTION_getSocketSpeedsTotal = 7;
        static final int TRANSACTION_getUidPurStats = 2;
        static final int TRANSACTION_getUidPurStatsTotal = 1;
        static final int TRANSACTION_getUidSpeedsIfindex = 8;
        static final int TRANSACTION_getUidStatsTotal = 13;
        static final int TRANSACTION_setAppFreezeConfig = 6;
        static final int TRANSACTION_setBpfSocketSpeedsConfig = 11;
        static final int TRANSACTION_startBpfSocketSpeedsCalc = 9;
        static final int TRANSACTION_stopBpfSocketSpeedsCalc = 10;

        public Stub() {
            attachInterface(this, IOplusNetworkStatsService.DESCRIPTOR);
        }

        public static IOplusNetworkStatsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNetworkStatsService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNetworkStatsService)) {
                return (IOplusNetworkStatsService) iin;
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
                    return "getUidPurStatsTotal";
                case 2:
                    return "getUidPurStats";
                case 3:
                    return "fetchAppFreezeSyns";
                case 4:
                    return "fetchAppFreezeStats";
                case 5:
                    return "getAppFreezeConfig";
                case 6:
                    return "setAppFreezeConfig";
                case 7:
                    return "getSocketSpeedsTotal";
                case 8:
                    return "getUidSpeedsIfindex";
                case 9:
                    return "startBpfSocketSpeedsCalc";
                case 10:
                    return "stopBpfSocketSpeedsCalc";
                case 11:
                    return "setBpfSocketSpeedsConfig";
                case 12:
                    return "getSocketIsLocal";
                case 13:
                    return "getUidStatsTotal";
                case 14:
                    return "getIfnameUidStatsTotal";
                case 15:
                    return "getIfaceStats";
                case 16:
                    return "getDenyFlag";
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
                data.enforceInterface(IOplusNetworkStatsService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNetworkStatsService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            StatsValueTotal _result = getUidPurStatsTotal();
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 2:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            StatsValue _result2 = getUidPurStats(_arg0);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 3:
                            AppFreezeSyncTotal _result3 = fetchAppFreezeSyns();
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 4:
                            AppFreezeStatsTotal _result4 = fetchAppFreezeStats();
                            reply.writeNoException();
                            reply.writeTypedObject(_result4, 1);
                            return true;
                        case 5:
                            AppFreezeConfig _result5 = getAppFreezeConfig();
                            reply.writeNoException();
                            reply.writeTypedObject(_result5, 1);
                            return true;
                        case 6:
                            AppFreezeConfig _arg02 = (AppFreezeConfig) data.readTypedObject(AppFreezeConfig.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result6 = setAppFreezeConfig(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 7:
                            int _arg03 = data.readInt();
                            int _arg1 = data.readInt();
                            long[] _arg2 = data.createLongArray();
                            data.enforceNoDataAvail();
                            SpeedsValueTotal _result7 = getSocketSpeedsTotal(_arg03, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeTypedObject(_result7, 1);
                            return true;
                        case 8:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            IfaceSpeedsValueTotal _result8 = getUidSpeedsIfindex(_arg04);
                            reply.writeNoException();
                            reply.writeTypedObject(_result8, 1);
                            return true;
                        case 9:
                            int _arg05 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result9 = startBpfSocketSpeedsCalc(_arg05, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 10:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result10 = stopBpfSocketSpeedsCalc(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 11:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result11 = setBpfSocketSpeedsConfig(_arg07);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 12:
                            long _arg08 = data.readLong();
                            data.enforceNoDataAvail();
                            int _result12 = getSocketIsLocal(_arg08);
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 13:
                            StatsValueTotal _result13 = getUidStatsTotal();
                            reply.writeNoException();
                            reply.writeTypedObject(_result13, 1);
                            return true;
                        case 14:
                            IfaceUidStatsTotal _result14 = getIfnameUidStatsTotal();
                            reply.writeNoException();
                            reply.writeTypedObject(_result14, 1);
                            return true;
                        case 15:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            StatsValue _result15 = getIfaceStats(_arg09);
                            reply.writeNoException();
                            reply.writeTypedObject(_result15, 1);
                            return true;
                        case 16:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result16 = getDenyFlag(_arg010);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNetworkStatsService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNetworkStatsService.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public StatsValueTotal getUidPurStatsTotal() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    StatsValueTotal _result = (StatsValueTotal) _reply.readTypedObject(StatsValueTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public StatsValue getUidPurStats(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    StatsValue _result = (StatsValue) _reply.readTypedObject(StatsValue.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public AppFreezeSyncTotal fetchAppFreezeSyns() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    AppFreezeSyncTotal _result = (AppFreezeSyncTotal) _reply.readTypedObject(AppFreezeSyncTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public AppFreezeStatsTotal fetchAppFreezeStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    AppFreezeStatsTotal _result = (AppFreezeStatsTotal) _reply.readTypedObject(AppFreezeStatsTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public AppFreezeConfig getAppFreezeConfig() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    AppFreezeConfig _result = (AppFreezeConfig) _reply.readTypedObject(AppFreezeConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public boolean setAppFreezeConfig(AppFreezeConfig cfg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeTypedObject(cfg, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public SpeedsValueTotal getSocketSpeedsTotal(int clearIntervals, int uploadSpeed, long[] limitCookies) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(clearIntervals);
                    _data.writeInt(uploadSpeed);
                    _data.writeLongArray(limitCookies);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    SpeedsValueTotal _result = (SpeedsValueTotal) _reply.readTypedObject(SpeedsValueTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public IfaceSpeedsValueTotal getUidSpeedsIfindex(int clearIntervals) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(clearIntervals);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    IfaceSpeedsValueTotal _result = (IfaceSpeedsValueTotal) _reply.readTypedObject(IfaceSpeedsValueTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public int startBpfSocketSpeedsCalc(int ifindex, boolean localBypass) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(ifindex);
                    _data.writeBoolean(localBypass);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public int stopBpfSocketSpeedsCalc(int ifindex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(ifindex);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public int setBpfSocketSpeedsConfig(int speedsCalcInv) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(speedsCalcInv);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public int getSocketIsLocal(long socketCookie) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeLong(socketCookie);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public StatsValueTotal getUidStatsTotal() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    StatsValueTotal _result = (StatsValueTotal) _reply.readTypedObject(StatsValueTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public IfaceUidStatsTotal getIfnameUidStatsTotal() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    IfaceUidStatsTotal _result = (IfaceUidStatsTotal) _reply.readTypedObject(IfaceUidStatsTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public StatsValue getIfaceStats(String ifname) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeString(ifname);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    StatsValue _result = (StatsValue) _reply.readTypedObject(StatsValue.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStatsService
            public int getDenyFlag(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStatsService.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 15;
        }
    }
}
