package android.operator;

import android.operator.IOplusCotaObserverDelegate;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusOperatorManager extends IInterface {
    public static final String DESCRIPTOR = "android.operator.IOplusOperatorManager";

    Map getConfigMap(Bundle bundle) throws RemoteException;

    List<String> getCotaAppPackageNameList() throws RemoteException;

    Bundle getCotaInfo(String str) throws RemoteException;

    int getCotaMountState(String str) throws RemoteException;

    void grantCustomizedRuntimePermissions() throws RemoteException;

    boolean handleCotaCmd(Bundle bundle) throws RemoteException;

    boolean isInSimTriggeredSystemBlackList(String str) throws RemoteException;

    void mountCotaImage(Bundle bundle) throws RemoteException;

    void notifyCotaMounted() throws RemoteException;

    void notifyRegionSwitch(Bundle bundle) throws RemoteException;

    void notifySimSwitch(Bundle bundle) throws RemoteException;

    void notifySmartCustomizationStart() throws RemoteException;

    boolean registerCotaObserverDelegate(IOplusCotaObserverDelegate iOplusCotaObserverDelegate) throws RemoteException;

    void testAidl() throws RemoteException;

    void unregisterCotaObserverDelegate(IOplusCotaObserverDelegate iOplusCotaObserverDelegate) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusOperatorManager {
        @Override // android.operator.IOplusOperatorManager
        public void testAidl() throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public Map getConfigMap(Bundle bundle) throws RemoteException {
            return null;
        }

        @Override // android.operator.IOplusOperatorManager
        public void grantCustomizedRuntimePermissions() throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public void notifySmartCustomizationStart() throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public boolean isInSimTriggeredSystemBlackList(String pkgName) throws RemoteException {
            return false;
        }

        @Override // android.operator.IOplusOperatorManager
        public void notifySimSwitch(Bundle data) throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public void notifyRegionSwitch(Bundle data) throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public void mountCotaImage(Bundle data) throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public void notifyCotaMounted() throws RemoteException {
        }

        @Override // android.operator.IOplusOperatorManager
        public int getCotaMountState(String imagePath) throws RemoteException {
            return 0;
        }

        @Override // android.operator.IOplusOperatorManager
        public List<String> getCotaAppPackageNameList() throws RemoteException {
            return null;
        }

        @Override // android.operator.IOplusOperatorManager
        public boolean handleCotaCmd(Bundle bundle) throws RemoteException {
            return false;
        }

        @Override // android.operator.IOplusOperatorManager
        public Bundle getCotaInfo(String action) throws RemoteException {
            return null;
        }

        @Override // android.operator.IOplusOperatorManager
        public boolean registerCotaObserverDelegate(IOplusCotaObserverDelegate observer) throws RemoteException {
            return false;
        }

        @Override // android.operator.IOplusOperatorManager
        public void unregisterCotaObserverDelegate(IOplusCotaObserverDelegate observer) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusOperatorManager {
        static final int TRANSACTION_getConfigMap = 2;
        static final int TRANSACTION_getCotaAppPackageNameList = 11;
        static final int TRANSACTION_getCotaInfo = 13;
        static final int TRANSACTION_getCotaMountState = 10;
        static final int TRANSACTION_grantCustomizedRuntimePermissions = 3;
        static final int TRANSACTION_handleCotaCmd = 12;
        static final int TRANSACTION_isInSimTriggeredSystemBlackList = 5;
        static final int TRANSACTION_mountCotaImage = 8;
        static final int TRANSACTION_notifyCotaMounted = 9;
        static final int TRANSACTION_notifyRegionSwitch = 7;
        static final int TRANSACTION_notifySimSwitch = 6;
        static final int TRANSACTION_notifySmartCustomizationStart = 4;
        static final int TRANSACTION_registerCotaObserverDelegate = 14;
        static final int TRANSACTION_testAidl = 1;
        static final int TRANSACTION_unregisterCotaObserverDelegate = 15;

        public Stub() {
            attachInterface(this, IOplusOperatorManager.DESCRIPTOR);
        }

        public static IOplusOperatorManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusOperatorManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusOperatorManager)) {
                return (IOplusOperatorManager) iin;
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
                    return "testAidl";
                case 2:
                    return "getConfigMap";
                case 3:
                    return "grantCustomizedRuntimePermissions";
                case 4:
                    return "notifySmartCustomizationStart";
                case 5:
                    return "isInSimTriggeredSystemBlackList";
                case 6:
                    return "notifySimSwitch";
                case 7:
                    return "notifyRegionSwitch";
                case 8:
                    return "mountCotaImage";
                case 9:
                    return "notifyCotaMounted";
                case 10:
                    return "getCotaMountState";
                case 11:
                    return "getCotaAppPackageNameList";
                case 12:
                    return "handleCotaCmd";
                case 13:
                    return "getCotaInfo";
                case 14:
                    return "registerCotaObserverDelegate";
                case 15:
                    return "unregisterCotaObserverDelegate";
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
                data.enforceInterface(IOplusOperatorManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusOperatorManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            testAidl();
                            return true;
                        case 2:
                            Bundle _arg0 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Map _result = getConfigMap(_arg0);
                            reply.writeNoException();
                            reply.writeMap(_result);
                            return true;
                        case 3:
                            grantCustomizedRuntimePermissions();
                            return true;
                        case 4:
                            notifySmartCustomizationStart();
                            reply.writeNoException();
                            return true;
                        case 5:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = isInSimTriggeredSystemBlackList(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 6:
                            Bundle _arg03 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            notifySimSwitch(_arg03);
                            reply.writeNoException();
                            return true;
                        case 7:
                            Bundle _arg04 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            notifyRegionSwitch(_arg04);
                            reply.writeNoException();
                            return true;
                        case 8:
                            Bundle _arg05 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            mountCotaImage(_arg05);
                            reply.writeNoException();
                            return true;
                        case 9:
                            notifyCotaMounted();
                            reply.writeNoException();
                            return true;
                        case 10:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            int _result3 = getCotaMountState(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 11:
                            List<String> _result4 = getCotaAppPackageNameList();
                            reply.writeNoException();
                            reply.writeStringList(_result4);
                            return true;
                        case 12:
                            Bundle _arg07 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result5 = handleCotaCmd(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 13:
                            String _arg08 = data.readString();
                            data.enforceNoDataAvail();
                            Bundle _result6 = getCotaInfo(_arg08);
                            reply.writeNoException();
                            reply.writeTypedObject(_result6, 1);
                            return true;
                        case 14:
                            IOplusCotaObserverDelegate _arg09 = IOplusCotaObserverDelegate.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result7 = registerCotaObserverDelegate(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 15:
                            IOplusCotaObserverDelegate _arg010 = IOplusCotaObserverDelegate.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterCotaObserverDelegate(_arg010);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusOperatorManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusOperatorManager.DESCRIPTOR;
            }

            @Override // android.operator.IOplusOperatorManager
            public void testAidl() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public Map getConfigMap(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void grantCustomizedRuntimePermissions() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void notifySmartCustomizationStart() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public boolean isInSimTriggeredSystemBlackList(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void notifySimSwitch(Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void notifyRegionSwitch(Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void mountCotaImage(Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void notifyCotaMounted() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public int getCotaMountState(String imagePath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeString(imagePath);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public List<String> getCotaAppPackageNameList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public boolean handleCotaCmd(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public Bundle getCotaInfo(String action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeString(action);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public boolean registerCotaObserverDelegate(IOplusCotaObserverDelegate observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.operator.IOplusOperatorManager
            public void unregisterCotaObserverDelegate(IOplusCotaObserverDelegate observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOperatorManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
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
