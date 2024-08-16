package android.cmccslice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface ICmccSliceManagerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.cmccslice.ICmccSliceManagerCallback";

    void onPostUrsp(int i, List<UrspRule> list) throws RemoteException;

    void onPostUrspForQcom(int i, int i2, int i3, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICmccSliceManagerCallback {
        @Override // android.cmccslice.ICmccSliceManagerCallback
        public void onPostUrsp(int slotid, List<UrspRule> urspList) throws RemoteException {
        }

        @Override // android.cmccslice.ICmccSliceManagerCallback
        public void onPostUrspForQcom(int slotIndex, int noOfRules, int indexOfRule, String jsonUrsp) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICmccSliceManagerCallback {
        static final int TRANSACTION_onPostUrsp = 1;
        static final int TRANSACTION_onPostUrspForQcom = 2;

        public Stub() {
            attachInterface(this, ICmccSliceManagerCallback.DESCRIPTOR);
        }

        public static ICmccSliceManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICmccSliceManagerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof ICmccSliceManagerCallback)) {
                return (ICmccSliceManagerCallback) iin;
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
                    return "onPostUrsp";
                case 2:
                    return "onPostUrspForQcom";
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
                data.enforceInterface(ICmccSliceManagerCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICmccSliceManagerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            List<UrspRule> _arg1 = data.createTypedArrayList(UrspRule.CREATOR);
                            data.enforceNoDataAvail();
                            onPostUrsp(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            onPostUrspForQcom(_arg02, _arg12, _arg2, _arg3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICmccSliceManagerCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICmccSliceManagerCallback.DESCRIPTOR;
            }

            @Override // android.cmccslice.ICmccSliceManagerCallback
            public void onPostUrsp(int slotid, List<UrspRule> urspList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICmccSliceManagerCallback.DESCRIPTOR);
                    _data.writeInt(slotid);
                    _data.writeTypedList(urspList, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.cmccslice.ICmccSliceManagerCallback
            public void onPostUrspForQcom(int slotIndex, int noOfRules, int indexOfRule, String jsonUrsp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICmccSliceManagerCallback.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(noOfRules);
                    _data.writeInt(indexOfRule);
                    _data.writeString(jsonUrsp);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
