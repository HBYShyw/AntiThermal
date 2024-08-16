package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusNetworkManagementService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusNetworkManagementService";

    boolean addDomainRestrictionList(int i, String[] strArr) throws RemoteException;

    boolean addNetworkRestriction(int i, String[] strArr) throws RemoteException;

    void clearUidTos(String str, String str2, String str3) throws RemoteException;

    String executeShellToSetIptables(String str) throws RemoteException;

    void increaseTCPSyncRetryForSpecificKernel() throws RemoteException;

    String oplusNetdCmdParse(String str, int i) throws RemoteException;

    String oplusNetdGetSysProc(int i, int i2, String str, String str2) throws RemoteException;

    boolean removeAllDomainRestrictionList(int i) throws RemoteException;

    boolean removeDomainRestrictionList(int i, String[] strArr) throws RemoteException;

    boolean removeNetworkRestriction(int i, String[] strArr) throws RemoteException;

    boolean removeNetworkRestrictionAll(int i) throws RemoteException;

    boolean setDomainRestrictionMode(int i) throws RemoteException;

    void setFirewallAllowListForNetworkType(int i, int i2, int[] iArr) throws RemoteException;

    void setFirewallUidRuleForNetworkType(int i, int i2, int i3) throws RemoteException;

    boolean setNetworkRestriction(int i) throws RemoteException;

    void setTestId(int i) throws RemoteException;

    void setUidTos(String str, String str2, String str3) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNetworkManagementService {
        @Override // com.oplus.network.IOplusNetworkManagementService
        public void setFirewallUidRuleForNetworkType(int type, int uid, int rule) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public void setUidTos(String uid, String tos, String ifaceName) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public void clearUidTos(String uid, String tos, String ifaceName) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public String oplusNetdCmdParse(String cmd, int params) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public String oplusNetdGetSysProc(int ipversion, int which, String intf, String parameter) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public String executeShellToSetIptables(String commandline) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean setNetworkRestriction(int pattern) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean removeNetworkRestrictionAll(int pattern) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public void setTestId(int i) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean setDomainRestrictionMode(int mode) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean addDomainRestrictionList(int mode, String[] list) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean removeDomainRestrictionList(int mode, String[] list) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean removeAllDomainRestrictionList(int mode) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public void increaseTCPSyncRetryForSpecificKernel() throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean addNetworkRestriction(int pattern, String[] list) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public boolean removeNetworkRestriction(int pattern, String[] list) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkManagementService
        public void setFirewallAllowListForNetworkType(int type, int rule, int[] uids) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNetworkManagementService {
        static final int TRANSACTION_addDomainRestrictionList = 11;
        static final int TRANSACTION_addNetworkRestriction = 15;
        static final int TRANSACTION_clearUidTos = 3;
        static final int TRANSACTION_executeShellToSetIptables = 6;
        static final int TRANSACTION_increaseTCPSyncRetryForSpecificKernel = 14;
        static final int TRANSACTION_oplusNetdCmdParse = 4;
        static final int TRANSACTION_oplusNetdGetSysProc = 5;
        static final int TRANSACTION_removeAllDomainRestrictionList = 13;
        static final int TRANSACTION_removeDomainRestrictionList = 12;
        static final int TRANSACTION_removeNetworkRestriction = 16;
        static final int TRANSACTION_removeNetworkRestrictionAll = 8;
        static final int TRANSACTION_setDomainRestrictionMode = 10;
        static final int TRANSACTION_setFirewallAllowListForNetworkType = 17;
        static final int TRANSACTION_setFirewallUidRuleForNetworkType = 1;
        static final int TRANSACTION_setNetworkRestriction = 7;
        static final int TRANSACTION_setTestId = 9;
        static final int TRANSACTION_setUidTos = 2;

        public Stub() {
            attachInterface(this, IOplusNetworkManagementService.DESCRIPTOR);
        }

        public static IOplusNetworkManagementService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNetworkManagementService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNetworkManagementService)) {
                return (IOplusNetworkManagementService) iin;
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
                    return "setFirewallUidRuleForNetworkType";
                case 2:
                    return "setUidTos";
                case 3:
                    return "clearUidTos";
                case 4:
                    return "oplusNetdCmdParse";
                case 5:
                    return "oplusNetdGetSysProc";
                case 6:
                    return "executeShellToSetIptables";
                case 7:
                    return "setNetworkRestriction";
                case 8:
                    return "removeNetworkRestrictionAll";
                case 9:
                    return "setTestId";
                case 10:
                    return "setDomainRestrictionMode";
                case 11:
                    return "addDomainRestrictionList";
                case 12:
                    return "removeDomainRestrictionList";
                case 13:
                    return "removeAllDomainRestrictionList";
                case 14:
                    return "increaseTCPSyncRetryForSpecificKernel";
                case 15:
                    return "addNetworkRestriction";
                case 16:
                    return "removeNetworkRestriction";
                case 17:
                    return "setFirewallAllowListForNetworkType";
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
                data.enforceInterface(IOplusNetworkManagementService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNetworkManagementService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            setFirewallUidRuleForNetworkType(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            String _arg12 = data.readString();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            setUidTos(_arg02, _arg12, _arg22);
                            reply.writeNoException();
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            String _arg13 = data.readString();
                            String _arg23 = data.readString();
                            data.enforceNoDataAvail();
                            clearUidTos(_arg03, _arg13, _arg23);
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result = oplusNetdCmdParse(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg15 = data.readInt();
                            String _arg24 = data.readString();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            String _result2 = oplusNetdGetSysProc(_arg05, _arg15, _arg24, _arg3);
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            String _result3 = executeShellToSetIptables(_arg06);
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = setNetworkRestriction(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = removeNetworkRestrictionAll(_arg08);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            setTestId(_arg09);
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result6 = setDomainRestrictionMode(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            String[] _arg16 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result7 = addDomainRestrictionList(_arg011, _arg16);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            String[] _arg17 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result8 = removeDomainRestrictionList(_arg012, _arg17);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = removeAllDomainRestrictionList(_arg013);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 14:
                            increaseTCPSyncRetryForSpecificKernel();
                            reply.writeNoException();
                            return true;
                        case 15:
                            int _arg014 = data.readInt();
                            String[] _arg18 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result10 = addNetworkRestriction(_arg014, _arg18);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 16:
                            int _arg015 = data.readInt();
                            String[] _arg19 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result11 = removeNetworkRestriction(_arg015, _arg19);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 17:
                            int _arg016 = data.readInt();
                            int _arg110 = data.readInt();
                            int[] _arg25 = data.createIntArray();
                            data.enforceNoDataAvail();
                            setFirewallAllowListForNetworkType(_arg016, _arg110, _arg25);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNetworkManagementService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNetworkManagementService.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public void setFirewallUidRuleForNetworkType(int type, int uid, int rule) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(uid);
                    _data.writeInt(rule);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public void setUidTos(String uid, String tos, String ifaceName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeString(uid);
                    _data.writeString(tos);
                    _data.writeString(ifaceName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public void clearUidTos(String uid, String tos, String ifaceName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeString(uid);
                    _data.writeString(tos);
                    _data.writeString(ifaceName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public String oplusNetdCmdParse(String cmd, int params) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeString(cmd);
                    _data.writeInt(params);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public String oplusNetdGetSysProc(int ipversion, int which, String intf, String parameter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(ipversion);
                    _data.writeInt(which);
                    _data.writeString(intf);
                    _data.writeString(parameter);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public String executeShellToSetIptables(String commandline) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeString(commandline);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean setNetworkRestriction(int pattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean removeNetworkRestrictionAll(int pattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public void setTestId(int i) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(i);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean setDomainRestrictionMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean addDomainRestrictionList(int mode, String[] list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStringArray(list);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean removeDomainRestrictionList(int mode, String[] list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStringArray(list);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean removeAllDomainRestrictionList(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public void increaseTCPSyncRetryForSpecificKernel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean addNetworkRestriction(int pattern, String[] list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    _data.writeStringArray(list);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public boolean removeNetworkRestriction(int pattern, String[] list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    _data.writeStringArray(list);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkManagementService
            public void setFirewallAllowListForNetworkType(int type, int rule, int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkManagementService.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(rule);
                    _data.writeIntArray(uids);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 16;
        }
    }
}
