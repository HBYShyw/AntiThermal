package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeNetworkManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeNetworkManagerService";

    void addAppMeteredDataBlackList(List<String> list) throws RemoteException;

    void addAppWlanDataBlackList(List<String> list) throws RemoteException;

    void addDomainRestrictionList(int i, List<String> list) throws RemoteException;

    void addNetworkRestriction(int i, List<String> list) throws RemoteException;

    List<String> getAppMeteredDataBlackList() throws RemoteException;

    List<String> getAppWlanDataBlackList() throws RemoteException;

    List<String> getDomainRestrictionList(int i) throws RemoteException;

    int getDomainRestrictionMode() throws RemoteException;

    String getNetworkControlMode(String str) throws RemoteException;

    List<String> getNetworkRestrictionList(int i) throws RemoteException;

    int getNetworkRestrictionMode() throws RemoteException;

    int getUserApnMgrPolicies() throws RemoteException;

    void removeAllDomainRestrictionList(int i) throws RemoteException;

    void removeAppMeteredDataBlackList(List<String> list) throws RemoteException;

    void removeAppWlanDataBlackList(List<String> list) throws RemoteException;

    void removeDomainRestrictionList(int i, List<String> list) throws RemoteException;

    void removeNetworkRestriction(int i, List<String> list) throws RemoteException;

    void removeNetworkRestrictionAll(int i) throws RemoteException;

    void setDomainRestrictionMode(int i) throws RemoteException;

    void setNetworkControlMode(ComponentName componentName, int i) throws RemoteException;

    void setNetworkRestriction(int i) throws RemoteException;

    void setUserApnMgrPolicies(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeNetworkManagerService {
        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void setNetworkRestriction(int pattern) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void addNetworkRestriction(int pattern, List<String> list) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void removeNetworkRestriction(int pattern, List<String> list) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void removeNetworkRestrictionAll(int pattern) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public List<String> getNetworkRestrictionList(int pattern) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void setUserApnMgrPolicies(int mode) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public int getUserApnMgrPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void addAppMeteredDataBlackList(List<String> pkgs) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void addAppWlanDataBlackList(List<String> pkgs) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void removeAppMeteredDataBlackList(List<String> addressList) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void removeAppWlanDataBlackList(List<String> addressList) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public List<String> getAppMeteredDataBlackList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public List<String> getAppWlanDataBlackList() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void setNetworkControlMode(ComponentName componentName, int mode) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public String getNetworkControlMode(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public int getNetworkRestrictionMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void setDomainRestrictionMode(int mode) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public int getDomainRestrictionMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void addDomainRestrictionList(int mode, List<String> list) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public List<String> getDomainRestrictionList(int mode) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void removeDomainRestrictionList(int mode, List<String> list) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeNetworkManagerService
        public void removeAllDomainRestrictionList(int mode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeNetworkManagerService {
        static final int TRANSACTION_addAppMeteredDataBlackList = 8;
        static final int TRANSACTION_addAppWlanDataBlackList = 9;
        static final int TRANSACTION_addDomainRestrictionList = 19;
        static final int TRANSACTION_addNetworkRestriction = 2;
        static final int TRANSACTION_getAppMeteredDataBlackList = 12;
        static final int TRANSACTION_getAppWlanDataBlackList = 13;
        static final int TRANSACTION_getDomainRestrictionList = 20;
        static final int TRANSACTION_getDomainRestrictionMode = 18;
        static final int TRANSACTION_getNetworkControlMode = 15;
        static final int TRANSACTION_getNetworkRestrictionList = 5;
        static final int TRANSACTION_getNetworkRestrictionMode = 16;
        static final int TRANSACTION_getUserApnMgrPolicies = 7;
        static final int TRANSACTION_removeAllDomainRestrictionList = 22;
        static final int TRANSACTION_removeAppMeteredDataBlackList = 10;
        static final int TRANSACTION_removeAppWlanDataBlackList = 11;
        static final int TRANSACTION_removeDomainRestrictionList = 21;
        static final int TRANSACTION_removeNetworkRestriction = 3;
        static final int TRANSACTION_removeNetworkRestrictionAll = 4;
        static final int TRANSACTION_setDomainRestrictionMode = 17;
        static final int TRANSACTION_setNetworkControlMode = 14;
        static final int TRANSACTION_setNetworkRestriction = 1;
        static final int TRANSACTION_setUserApnMgrPolicies = 6;

        public Stub() {
            attachInterface(this, IOplusCustomizeNetworkManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeNetworkManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeNetworkManagerService)) {
                return (IOplusCustomizeNetworkManagerService) iin;
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
                    return "setNetworkRestriction";
                case 2:
                    return "addNetworkRestriction";
                case 3:
                    return "removeNetworkRestriction";
                case 4:
                    return "removeNetworkRestrictionAll";
                case 5:
                    return "getNetworkRestrictionList";
                case 6:
                    return "setUserApnMgrPolicies";
                case 7:
                    return "getUserApnMgrPolicies";
                case 8:
                    return "addAppMeteredDataBlackList";
                case 9:
                    return "addAppWlanDataBlackList";
                case 10:
                    return "removeAppMeteredDataBlackList";
                case 11:
                    return "removeAppWlanDataBlackList";
                case 12:
                    return "getAppMeteredDataBlackList";
                case 13:
                    return "getAppWlanDataBlackList";
                case 14:
                    return "setNetworkControlMode";
                case 15:
                    return "getNetworkControlMode";
                case 16:
                    return "getNetworkRestrictionMode";
                case 17:
                    return "setDomainRestrictionMode";
                case 18:
                    return "getDomainRestrictionMode";
                case 19:
                    return "addDomainRestrictionList";
                case 20:
                    return "getDomainRestrictionList";
                case 21:
                    return "removeDomainRestrictionList";
                case 22:
                    return "removeAllDomainRestrictionList";
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
                data.enforceInterface(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            setNetworkRestriction(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            List<String> _arg1 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addNetworkRestriction(_arg02, _arg1);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            List<String> _arg12 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeNetworkRestriction(_arg03, _arg12);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            removeNetworkRestrictionAll(_arg04);
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result = getNetworkRestrictionList(_arg05);
                            reply.writeNoException();
                            reply.writeStringList(_result);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            setUserApnMgrPolicies(_arg06);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _result2 = getUserApnMgrPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 8:
                            List<String> _arg07 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addAppMeteredDataBlackList(_arg07);
                            reply.writeNoException();
                            return true;
                        case 9:
                            List<String> _arg08 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addAppWlanDataBlackList(_arg08);
                            reply.writeNoException();
                            return true;
                        case 10:
                            List<String> _arg09 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeAppMeteredDataBlackList(_arg09);
                            reply.writeNoException();
                            return true;
                        case 11:
                            List<String> _arg010 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeAppWlanDataBlackList(_arg010);
                            reply.writeNoException();
                            return true;
                        case 12:
                            List<String> _result3 = getAppMeteredDataBlackList();
                            reply.writeNoException();
                            reply.writeStringList(_result3);
                            return true;
                        case 13:
                            List<String> _result4 = getAppWlanDataBlackList();
                            reply.writeNoException();
                            reply.writeStringList(_result4);
                            return true;
                        case 14:
                            ComponentName _arg011 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            setNetworkControlMode(_arg011, _arg13);
                            reply.writeNoException();
                            return true;
                        case 15:
                            String _arg012 = data.readString();
                            data.enforceNoDataAvail();
                            String _result5 = getNetworkControlMode(_arg012);
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 16:
                            int _result6 = getNetworkRestrictionMode();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 17:
                            int _arg013 = data.readInt();
                            data.enforceNoDataAvail();
                            setDomainRestrictionMode(_arg013);
                            reply.writeNoException();
                            return true;
                        case 18:
                            int _result7 = getDomainRestrictionMode();
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 19:
                            int _arg014 = data.readInt();
                            List<String> _arg14 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addDomainRestrictionList(_arg014, _arg14);
                            reply.writeNoException();
                            return true;
                        case 20:
                            int _arg015 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result8 = getDomainRestrictionList(_arg015);
                            reply.writeNoException();
                            reply.writeStringList(_result8);
                            return true;
                        case 21:
                            int _arg016 = data.readInt();
                            List<String> _arg15 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            removeDomainRestrictionList(_arg016, _arg15);
                            reply.writeNoException();
                            return true;
                        case 22:
                            int _arg017 = data.readInt();
                            data.enforceNoDataAvail();
                            removeAllDomainRestrictionList(_arg017);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeNetworkManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeNetworkManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void setNetworkRestriction(int pattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void addNetworkRestriction(int pattern, List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    _data.writeStringList(list);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void removeNetworkRestriction(int pattern, List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    _data.writeStringList(list);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void removeNetworkRestrictionAll(int pattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public List<String> getNetworkRestrictionList(int pattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void setUserApnMgrPolicies(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public int getUserApnMgrPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void addAppMeteredDataBlackList(List<String> pkgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgs);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void addAppWlanDataBlackList(List<String> pkgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgs);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void removeAppMeteredDataBlackList(List<String> addressList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeStringList(addressList);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void removeAppWlanDataBlackList(List<String> addressList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeStringList(addressList);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public List<String> getAppMeteredDataBlackList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public List<String> getAppWlanDataBlackList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void setNetworkControlMode(ComponentName componentName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public String getNetworkControlMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public int getNetworkRestrictionMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void setDomainRestrictionMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public int getDomainRestrictionMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void addDomainRestrictionList(int mode, List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStringList(list);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public List<String> getDomainRestrictionList(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void removeDomainRestrictionList(int mode, List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStringList(list);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeNetworkManagerService
            public void removeAllDomainRestrictionList(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeNetworkManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 21;
        }
    }
}
