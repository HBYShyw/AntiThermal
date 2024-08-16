package android.cmccslice;

import android.cmccslice.ICmccSliceManagerCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.OlkConstants;
import java.util.List;

/* loaded from: classes.dex */
public interface ICmccSliceManager extends IInterface {
    public static final String DESCRIPTOR = "android.cmccslice.ICmccSliceManager";

    void appProxyAbort(String str, int i) throws RemoteException;

    String getCertForTD(int i) throws RemoteException;

    String getCertOfApp() throws RemoteException;

    TrafficDescriptor getTd(int i) throws RemoteException;

    boolean isSliceEnabled() throws RemoteException;

    boolean isSliceEstablished() throws RemoteException;

    void postUrsp(int i, int i2, String str, List<UrspRule> list) throws RemoteException;

    void postUrspForQcom(int i, int i2, int i3, String str) throws RemoteException;

    void reBuildAllSlice() throws RemoteException;

    void registerCallback(ICmccSliceManagerCallback iCmccSliceManagerCallback) throws RemoteException;

    void releaseAllSlice() throws RemoteException;

    void releaseSliceNetwork(TrafficDescriptor trafficDescriptor) throws RemoteException;

    void requestSliceNetwork(TrafficDescriptor trafficDescriptor, int i) throws RemoteException;

    void setNetworkPriorType(int i) throws RemoteException;

    void setSliceEnabled(boolean z) throws RemoteException;

    void unregisterCallback(ICmccSliceManagerCallback iCmccSliceManagerCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICmccSliceManager {
        @Override // android.cmccslice.ICmccSliceManager
        public void registerCallback(ICmccSliceManagerCallback cb) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void unregisterCallback(ICmccSliceManagerCallback cb) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void requestSliceNetwork(TrafficDescriptor td, int precedence) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void releaseSliceNetwork(TrafficDescriptor td) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void appProxyAbort(String pkgName, int key) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public TrafficDescriptor getTd(int networkCapabilities) throws RemoteException {
            return null;
        }

        @Override // android.cmccslice.ICmccSliceManager
        public String getCertForTD(int networkCapabilities) throws RemoteException {
            return null;
        }

        @Override // android.cmccslice.ICmccSliceManager
        public String getCertOfApp() throws RemoteException {
            return null;
        }

        @Override // android.cmccslice.ICmccSliceManager
        public boolean isSliceEstablished() throws RemoteException {
            return false;
        }

        @Override // android.cmccslice.ICmccSliceManager
        public boolean isSliceEnabled() throws RemoteException {
            return false;
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void setSliceEnabled(boolean enable) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void postUrsp(int slotIndex, int type, String originalUrsp, List<UrspRule> urspList) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void releaseAllSlice() throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void reBuildAllSlice() throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void setNetworkPriorType(int type) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManager
        public void postUrspForQcom(int slotIndex, int noOfRules, int indexOfRule, String jsonUrsp) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICmccSliceManager {
        static final int TRANSACTION_appProxyAbort = 5;
        static final int TRANSACTION_getCertForTD = 7;
        static final int TRANSACTION_getCertOfApp = 8;
        static final int TRANSACTION_getTd = 6;
        static final int TRANSACTION_isSliceEnabled = 10;
        static final int TRANSACTION_isSliceEstablished = 9;
        static final int TRANSACTION_postUrsp = 12;
        static final int TRANSACTION_postUrspForQcom = 16;
        static final int TRANSACTION_reBuildAllSlice = 14;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_releaseAllSlice = 13;
        static final int TRANSACTION_releaseSliceNetwork = 4;
        static final int TRANSACTION_requestSliceNetwork = 3;
        static final int TRANSACTION_setNetworkPriorType = 15;
        static final int TRANSACTION_setSliceEnabled = 11;
        static final int TRANSACTION_unregisterCallback = 2;

        public Stub() {
            attachInterface(this, ICmccSliceManager.DESCRIPTOR);
        }

        public static ICmccSliceManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICmccSliceManager.DESCRIPTOR);
            if (iin != null && (iin instanceof ICmccSliceManager)) {
                return (ICmccSliceManager) iin;
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
                    return OlkConstants.FUN_REGISTER_CALLBACK;
                case 2:
                    return "unregisterCallback";
                case 3:
                    return "requestSliceNetwork";
                case 4:
                    return "releaseSliceNetwork";
                case 5:
                    return "appProxyAbort";
                case 6:
                    return "getTd";
                case 7:
                    return "getCertForTD";
                case 8:
                    return "getCertOfApp";
                case 9:
                    return "isSliceEstablished";
                case 10:
                    return "isSliceEnabled";
                case 11:
                    return "setSliceEnabled";
                case 12:
                    return "postUrsp";
                case 13:
                    return "releaseAllSlice";
                case 14:
                    return "reBuildAllSlice";
                case 15:
                    return "setNetworkPriorType";
                case 16:
                    return "postUrspForQcom";
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
                data.enforceInterface(ICmccSliceManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICmccSliceManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ICmccSliceManagerCallback _arg0 = ICmccSliceManagerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerCallback(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            ICmccSliceManagerCallback _arg02 = ICmccSliceManagerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterCallback(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            TrafficDescriptor _arg03 = (TrafficDescriptor) data.readTypedObject(TrafficDescriptor.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            requestSliceNetwork(_arg03, _arg1);
                            reply.writeNoException();
                            return true;
                        case 4:
                            TrafficDescriptor _arg04 = (TrafficDescriptor) data.readTypedObject(TrafficDescriptor.CREATOR);
                            data.enforceNoDataAvail();
                            releaseSliceNetwork(_arg04);
                            reply.writeNoException();
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            appProxyAbort(_arg05, _arg12);
                            reply.writeNoException();
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            TrafficDescriptor _result = getTd(_arg06);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result2 = getCertForTD(_arg07);
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 8:
                            String _result3 = getCertOfApp();
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 9:
                            boolean _result4 = isSliceEstablished();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 10:
                            boolean _result5 = isSliceEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 11:
                            boolean _arg08 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSliceEnabled(_arg08);
                            reply.writeNoException();
                            return true;
                        case 12:
                            int _arg09 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg2 = data.readString();
                            List<UrspRule> _arg3 = data.createTypedArrayList(UrspRule.CREATOR);
                            data.enforceNoDataAvail();
                            postUrsp(_arg09, _arg13, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 13:
                            releaseAllSlice();
                            reply.writeNoException();
                            return true;
                        case 14:
                            reBuildAllSlice();
                            reply.writeNoException();
                            return true;
                        case 15:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            setNetworkPriorType(_arg010);
                            reply.writeNoException();
                            return true;
                        case 16:
                            int _arg011 = data.readInt();
                            int _arg14 = data.readInt();
                            int _arg22 = data.readInt();
                            String _arg32 = data.readString();
                            data.enforceNoDataAvail();
                            postUrspForQcom(_arg011, _arg14, _arg22, _arg32);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICmccSliceManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICmccSliceManager.DESCRIPTOR;
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void registerCallback(ICmccSliceManagerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void unregisterCallback(ICmccSliceManagerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void requestSliceNetwork(TrafficDescriptor td, int precedence) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeTypedObject(td, 0);
                    _data.writeInt(precedence);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void releaseSliceNetwork(TrafficDescriptor td) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeTypedObject(td, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void appProxyAbort(String pkgName, int key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(key);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public TrafficDescriptor getTd(int networkCapabilities) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeInt(networkCapabilities);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    TrafficDescriptor _result = (TrafficDescriptor) _reply.readTypedObject(TrafficDescriptor.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public String getCertForTD(int networkCapabilities) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeInt(networkCapabilities);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public String getCertOfApp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public boolean isSliceEstablished() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public boolean isSliceEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void setSliceEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void postUrsp(int slotIndex, int type, String originalUrsp, List<UrspRule> urspList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(type);
                    _data.writeString(originalUrsp);
                    _data.writeTypedList(urspList, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void releaseAllSlice() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void reBuildAllSlice() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void setNetworkPriorType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManager
            public void postUrspForQcom(int slotIndex, int noOfRules, int indexOfRule, String jsonUrsp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICmccSliceManager.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(noOfRules);
                    _data.writeInt(indexOfRule);
                    _data.writeString(jsonUrsp);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
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
