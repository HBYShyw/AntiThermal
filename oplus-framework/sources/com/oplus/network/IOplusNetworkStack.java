package com.oplus.network;

import android.net.Network;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.INetworkDiagnosisCb;
import com.oplus.network.IOplusNetScoreChange;

/* loaded from: classes.dex */
public interface IOplusNetworkStack extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusNetworkStack";

    int getNetworkRtt(Network network) throws RemoteException;

    int getNetworkScore(Network network) throws RemoteException;

    OplusNetworkKPI getUidKpi(int i, Network network) throws RemoteException;

    boolean oplusAddAppDnsConfig(String[] strArr, int[] iArr) throws RemoteException;

    boolean oplusClearAppDnsConfig() throws RemoteException;

    boolean oplusDelAppDnsConfig(String[] strArr) throws RemoteException;

    int oplusGetUidByPort(int i, String str) throws RemoteException;

    boolean oplusSetRedirectPort(int i, int i2) throws RemoteException;

    void registerTcpScoreChange(IOplusNetScoreChange iOplusNetScoreChange) throws RemoteException;

    boolean startDiagnosis(Network network, INetworkDiagnosisCb iNetworkDiagnosisCb) throws RemoteException;

    void unregisterTcpScoreChange(IOplusNetScoreChange iOplusNetScoreChange) throws RemoteException;

    void videoFrameLag(boolean z) throws RemoteException;

    void videoStart() throws RemoteException;

    void videoStop() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNetworkStack {
        @Override // com.oplus.network.IOplusNetworkStack
        public void registerTcpScoreChange(IOplusNetScoreChange scorechange) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public void unregisterTcpScoreChange(IOplusNetScoreChange scorechange) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public boolean oplusAddAppDnsConfig(String[] hostname, int[] ipaddr) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public boolean oplusDelAppDnsConfig(String[] hostname) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public boolean oplusClearAppDnsConfig() throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public int oplusGetUidByPort(int port, String callPackageName) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public boolean oplusSetRedirectPort(int action, int port) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public void videoStart() throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public void videoStop() throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public void videoFrameLag(boolean lag) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public int getNetworkScore(Network network) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public int getNetworkRtt(Network network) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public boolean startDiagnosis(Network network, INetworkDiagnosisCb cb) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkStack
        public OplusNetworkKPI getUidKpi(int uid, Network network) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNetworkStack {
        static final int TRANSACTION_getNetworkRtt = 12;
        static final int TRANSACTION_getNetworkScore = 11;
        static final int TRANSACTION_getUidKpi = 14;
        static final int TRANSACTION_oplusAddAppDnsConfig = 3;
        static final int TRANSACTION_oplusClearAppDnsConfig = 5;
        static final int TRANSACTION_oplusDelAppDnsConfig = 4;
        static final int TRANSACTION_oplusGetUidByPort = 6;
        static final int TRANSACTION_oplusSetRedirectPort = 7;
        static final int TRANSACTION_registerTcpScoreChange = 1;
        static final int TRANSACTION_startDiagnosis = 13;
        static final int TRANSACTION_unregisterTcpScoreChange = 2;
        static final int TRANSACTION_videoFrameLag = 10;
        static final int TRANSACTION_videoStart = 8;
        static final int TRANSACTION_videoStop = 9;

        public Stub() {
            attachInterface(this, IOplusNetworkStack.DESCRIPTOR);
        }

        public static IOplusNetworkStack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNetworkStack.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNetworkStack)) {
                return (IOplusNetworkStack) iin;
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
                    return "registerTcpScoreChange";
                case 2:
                    return "unregisterTcpScoreChange";
                case 3:
                    return "oplusAddAppDnsConfig";
                case 4:
                    return "oplusDelAppDnsConfig";
                case 5:
                    return "oplusClearAppDnsConfig";
                case 6:
                    return "oplusGetUidByPort";
                case 7:
                    return "oplusSetRedirectPort";
                case 8:
                    return "videoStart";
                case 9:
                    return "videoStop";
                case 10:
                    return "videoFrameLag";
                case 11:
                    return "getNetworkScore";
                case 12:
                    return "getNetworkRtt";
                case 13:
                    return "startDiagnosis";
                case 14:
                    return "getUidKpi";
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
                data.enforceInterface(IOplusNetworkStack.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNetworkStack.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusNetScoreChange _arg0 = IOplusNetScoreChange.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerTcpScoreChange(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            IOplusNetScoreChange _arg02 = IOplusNetScoreChange.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterTcpScoreChange(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            String[] _arg03 = data.createStringArray();
                            int[] _arg1 = data.createIntArray();
                            data.enforceNoDataAvail();
                            boolean _result = oplusAddAppDnsConfig(_arg03, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 4:
                            String[] _arg04 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result2 = oplusDelAppDnsConfig(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 5:
                            boolean _result3 = oplusClearAppDnsConfig();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 6:
                            int _arg05 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            int _result4 = oplusGetUidByPort(_arg05, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 7:
                            int _arg06 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = oplusSetRedirectPort(_arg06, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 8:
                            videoStart();
                            reply.writeNoException();
                            return true;
                        case 9:
                            videoStop();
                            reply.writeNoException();
                            return true;
                        case 10:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            videoFrameLag(_arg07);
                            reply.writeNoException();
                            return true;
                        case 11:
                            Network _arg08 = (Network) data.readTypedObject(Network.CREATOR);
                            data.enforceNoDataAvail();
                            int _result6 = getNetworkScore(_arg08);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 12:
                            Network _arg09 = (Network) data.readTypedObject(Network.CREATOR);
                            data.enforceNoDataAvail();
                            int _result7 = getNetworkRtt(_arg09);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 13:
                            Network _arg010 = (Network) data.readTypedObject(Network.CREATOR);
                            INetworkDiagnosisCb _arg14 = INetworkDiagnosisCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result8 = startDiagnosis(_arg010, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 14:
                            int _arg011 = data.readInt();
                            Network _arg15 = (Network) data.readTypedObject(Network.CREATOR);
                            data.enforceNoDataAvail();
                            OplusNetworkKPI _result9 = getUidKpi(_arg011, _arg15);
                            reply.writeNoException();
                            reply.writeTypedObject(_result9, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNetworkStack {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNetworkStack.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public void registerTcpScoreChange(IOplusNetScoreChange scorechange) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeStrongInterface(scorechange);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public void unregisterTcpScoreChange(IOplusNetScoreChange scorechange) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeStrongInterface(scorechange);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public boolean oplusAddAppDnsConfig(String[] hostname, int[] ipaddr) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeStringArray(hostname);
                    _data.writeIntArray(ipaddr);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public boolean oplusDelAppDnsConfig(String[] hostname) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeStringArray(hostname);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public boolean oplusClearAppDnsConfig() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public int oplusGetUidByPort(int port, String callPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeInt(port);
                    _data.writeString(callPackageName);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public boolean oplusSetRedirectPort(int action, int port) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeInt(action);
                    _data.writeInt(port);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public void videoStart() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public void videoStop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public void videoFrameLag(boolean lag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeBoolean(lag);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public int getNetworkScore(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public int getNetworkRtt(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public boolean startDiagnosis(Network network, INetworkDiagnosisCb cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkStack
            public OplusNetworkKPI getUidKpi(int uid, Network network) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkStack.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeTypedObject(network, 0);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    OplusNetworkKPI _result = (OplusNetworkKPI) _reply.readTypedObject(OplusNetworkKPI.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 13;
        }
    }
}
