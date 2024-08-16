package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.IOplusNetdEventCb;
import com.oplus.network.stats.StatsValueTotal;

/* loaded from: classes.dex */
public interface IOplusNetworkCmdService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusNetworkCmdService";

    boolean addDevMap(int i) throws RemoteException;

    boolean attachProgram(String str, boolean z) throws RemoteException;

    void clearSocketTos(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) throws RemoteException;

    boolean clearVirtualDeviceBindInfo(String str) throws RemoteException;

    boolean detachProgram(String str) throws RemoteException;

    StatsValueTotal getAllTetherStats() throws RemoteException;

    String getProcSysNet(int i, int i2, String str, String str2) throws RemoteException;

    boolean interfaceAddAddress(String str, String str2, int i) throws RemoteException;

    boolean interfaceDelAddress(String str, String str2, int i) throws RemoteException;

    boolean ipForwardingEnableSet(String str, boolean z) throws RemoteException;

    String oplusNetdCmdParse(String str, int[] iArr) throws RemoteException;

    void registerOplusNetdEvent(IOplusNetdEventCb iOplusNetdEventCb) throws RemoteException;

    boolean removeDevMap(int i) throws RemoteException;

    void setSocketTos(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) throws RemoteException;

    boolean setVirtualDeviceBindInfo(String str, String str2, String str3) throws RemoteException;

    boolean setVirtualDeviceBindInfoV4V6(String str, String str2, String str3, String str4) throws RemoteException;

    boolean startIpClient(String str, String str2, String str3) throws RemoteException;

    boolean startIpv6(String str, String str2) throws RemoteException;

    boolean startUpstreamIpv6Forwarding(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i3) throws RemoteException;

    boolean stopIpClient(String str) throws RemoteException;

    boolean stopIpv6(String str, String str2) throws RemoteException;

    boolean stopUpstreamIpv6Forwarding(int i, int i2, byte[] bArr) throws RemoteException;

    boolean tetherApplyDnsInterfaces() throws RemoteException;

    boolean tetherOffloadRuleAdd(int i, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3, int i4) throws RemoteException;

    boolean tetherOffloadRuleRemove(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    void unregisterOplusNetdEvent(IOplusNetdEventCb iOplusNetdEventCb) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNetworkCmdService {
        @Override // com.oplus.network.IOplusNetworkCmdService
        public void setSocketTos(String uid, String srcAdd, String srcPort, String desAdd, String desPort, String tos, String ifaceName, String protocol) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public void clearSocketTos(String uid, String srcAdd, String srcPort, String desAdd, String desPort, String tos, String ifaceName, String protocol) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public String oplusNetdCmdParse(String cmd, int[] param) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public void registerOplusNetdEvent(IOplusNetdEventCb cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public void unregisterOplusNetdEvent(IOplusNetdEventCb cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean setVirtualDeviceBindInfo(String virtualDevice, String bindDevice, String peerIp) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean clearVirtualDeviceBindInfo(String virtualDevice) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean ipForwardingEnableSet(String tag, boolean enable) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean setVirtualDeviceBindInfoV4V6(String virtualDevice, String bindDevice, String peerIpv4, String peerIpV6) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean interfaceAddAddress(String iface, String addrStr, int prefixLength) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean interfaceDelAddress(String iface, String addrStr, int prefixLength) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean tetherApplyDnsInterfaces() throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean tetherOffloadRuleAdd(int iif, byte[] macAddr, byte[] neigh6, int oif, byte[] ethSrcMac, byte[] ethDstMac, int ethProto, int pmtu) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean tetherOffloadRuleRemove(int iif, byte[] macAddr, byte[] neigh6) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean startUpstreamIpv6Forwarding(int downstreamIfindex, int upstreamIfindex, byte[] inDstMac, byte[] outSrcMac, byte[] outDstMac, int mtu) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean stopUpstreamIpv6Forwarding(int downstreamIfindex, int upstreamIfindex, byte[] inDstMac) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean attachProgram(String iface, boolean downstream) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean detachProgram(String iface) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean addDevMap(int ifIndex) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean removeDevMap(int ifIndex) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public StatsValueTotal getAllTetherStats() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public String getProcSysNet(int ipversion, int which, String intf, String parameter) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean startIpv6(String inIface, String extIface) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean stopIpv6(String inIface, String extIface) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean startIpClient(String iface, String staticAddr, String gateway) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusNetworkCmdService
        public boolean stopIpClient(String iface) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNetworkCmdService {
        static final int TRANSACTION_addDevMap = 19;
        static final int TRANSACTION_attachProgram = 17;
        static final int TRANSACTION_clearSocketTos = 2;
        static final int TRANSACTION_clearVirtualDeviceBindInfo = 7;
        static final int TRANSACTION_detachProgram = 18;
        static final int TRANSACTION_getAllTetherStats = 21;
        static final int TRANSACTION_getProcSysNet = 22;
        static final int TRANSACTION_interfaceAddAddress = 10;
        static final int TRANSACTION_interfaceDelAddress = 11;
        static final int TRANSACTION_ipForwardingEnableSet = 8;
        static final int TRANSACTION_oplusNetdCmdParse = 3;
        static final int TRANSACTION_registerOplusNetdEvent = 4;
        static final int TRANSACTION_removeDevMap = 20;
        static final int TRANSACTION_setSocketTos = 1;
        static final int TRANSACTION_setVirtualDeviceBindInfo = 6;
        static final int TRANSACTION_setVirtualDeviceBindInfoV4V6 = 9;
        static final int TRANSACTION_startIpClient = 25;
        static final int TRANSACTION_startIpv6 = 23;
        static final int TRANSACTION_startUpstreamIpv6Forwarding = 15;
        static final int TRANSACTION_stopIpClient = 26;
        static final int TRANSACTION_stopIpv6 = 24;
        static final int TRANSACTION_stopUpstreamIpv6Forwarding = 16;
        static final int TRANSACTION_tetherApplyDnsInterfaces = 12;
        static final int TRANSACTION_tetherOffloadRuleAdd = 13;
        static final int TRANSACTION_tetherOffloadRuleRemove = 14;
        static final int TRANSACTION_unregisterOplusNetdEvent = 5;

        public Stub() {
            attachInterface(this, IOplusNetworkCmdService.DESCRIPTOR);
        }

        public static IOplusNetworkCmdService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNetworkCmdService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNetworkCmdService)) {
                return (IOplusNetworkCmdService) iin;
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
                    return "setSocketTos";
                case 2:
                    return "clearSocketTos";
                case 3:
                    return "oplusNetdCmdParse";
                case 4:
                    return "registerOplusNetdEvent";
                case 5:
                    return "unregisterOplusNetdEvent";
                case 6:
                    return "setVirtualDeviceBindInfo";
                case 7:
                    return "clearVirtualDeviceBindInfo";
                case 8:
                    return "ipForwardingEnableSet";
                case 9:
                    return "setVirtualDeviceBindInfoV4V6";
                case 10:
                    return "interfaceAddAddress";
                case 11:
                    return "interfaceDelAddress";
                case 12:
                    return "tetherApplyDnsInterfaces";
                case 13:
                    return "tetherOffloadRuleAdd";
                case 14:
                    return "tetherOffloadRuleRemove";
                case 15:
                    return "startUpstreamIpv6Forwarding";
                case 16:
                    return "stopUpstreamIpv6Forwarding";
                case 17:
                    return "attachProgram";
                case 18:
                    return "detachProgram";
                case 19:
                    return "addDevMap";
                case 20:
                    return "removeDevMap";
                case 21:
                    return "getAllTetherStats";
                case 22:
                    return "getProcSysNet";
                case 23:
                    return "startIpv6";
                case 24:
                    return "stopIpv6";
                case 25:
                    return "startIpClient";
                case 26:
                    return "stopIpClient";
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
                data.enforceInterface(IOplusNetworkCmdService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNetworkCmdService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            String _arg1 = data.readString();
                            String _arg2 = data.readString();
                            String _arg3 = data.readString();
                            String _arg4 = data.readString();
                            String _arg5 = data.readString();
                            String _arg6 = data.readString();
                            String _arg7 = data.readString();
                            data.enforceNoDataAvail();
                            setSocketTos(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            String _arg12 = data.readString();
                            String _arg22 = data.readString();
                            String _arg32 = data.readString();
                            String _arg42 = data.readString();
                            String _arg52 = data.readString();
                            String _arg62 = data.readString();
                            String _arg72 = data.readString();
                            data.enforceNoDataAvail();
                            clearSocketTos(_arg02, _arg12, _arg22, _arg32, _arg42, _arg52, _arg62, _arg72);
                            reply.writeNoException();
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            int[] _arg13 = data.createIntArray();
                            data.enforceNoDataAvail();
                            String _result = oplusNetdCmdParse(_arg03, _arg13);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 4:
                            IOplusNetdEventCb _arg04 = IOplusNetdEventCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusNetdEvent(_arg04);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IOplusNetdEventCb _arg05 = IOplusNetdEventCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterOplusNetdEvent(_arg05);
                            reply.writeNoException();
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            String _arg14 = data.readString();
                            String _arg23 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = setVirtualDeviceBindInfo(_arg06, _arg14, _arg23);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = clearVirtualDeviceBindInfo(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            boolean _arg15 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result4 = ipForwardingEnableSet(_arg08, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 9:
                            String _arg09 = data.readString();
                            String _arg16 = data.readString();
                            String _arg24 = data.readString();
                            String _arg33 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result5 = setVirtualDeviceBindInfoV4V6(_arg09, _arg16, _arg24, _arg33);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 10:
                            String _arg010 = data.readString();
                            String _arg17 = data.readString();
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result6 = interfaceAddAddress(_arg010, _arg17, _arg25);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 11:
                            String _arg011 = data.readString();
                            String _arg18 = data.readString();
                            int _arg26 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = interfaceDelAddress(_arg011, _arg18, _arg26);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 12:
                            boolean _result8 = tetherApplyDnsInterfaces();
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 13:
                            int _arg012 = data.readInt();
                            byte[] _arg19 = data.createByteArray();
                            byte[] _arg27 = data.createByteArray();
                            int _arg34 = data.readInt();
                            byte[] _arg43 = data.createByteArray();
                            byte[] _arg53 = data.createByteArray();
                            int _arg63 = data.readInt();
                            int _arg73 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = tetherOffloadRuleAdd(_arg012, _arg19, _arg27, _arg34, _arg43, _arg53, _arg63, _arg73);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 14:
                            int _arg013 = data.readInt();
                            byte[] _arg110 = data.createByteArray();
                            byte[] _arg28 = data.createByteArray();
                            data.enforceNoDataAvail();
                            boolean _result10 = tetherOffloadRuleRemove(_arg013, _arg110, _arg28);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 15:
                            int _arg014 = data.readInt();
                            int _arg111 = data.readInt();
                            byte[] _arg29 = data.createByteArray();
                            byte[] _arg35 = data.createByteArray();
                            byte[] _arg44 = data.createByteArray();
                            int _arg54 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result11 = startUpstreamIpv6Forwarding(_arg014, _arg111, _arg29, _arg35, _arg44, _arg54);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 16:
                            int _arg015 = data.readInt();
                            int _arg112 = data.readInt();
                            byte[] _arg210 = data.createByteArray();
                            data.enforceNoDataAvail();
                            boolean _result12 = stopUpstreamIpv6Forwarding(_arg015, _arg112, _arg210);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 17:
                            String _arg016 = data.readString();
                            boolean _arg113 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result13 = attachProgram(_arg016, _arg113);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 18:
                            String _arg017 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result14 = detachProgram(_arg017);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 19:
                            int _arg018 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result15 = addDevMap(_arg018);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 20:
                            int _arg019 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result16 = removeDevMap(_arg019);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 21:
                            StatsValueTotal _result17 = getAllTetherStats();
                            reply.writeNoException();
                            reply.writeTypedObject(_result17, 1);
                            return true;
                        case 22:
                            int _arg020 = data.readInt();
                            int _arg114 = data.readInt();
                            String _arg211 = data.readString();
                            String _arg36 = data.readString();
                            data.enforceNoDataAvail();
                            String _result18 = getProcSysNet(_arg020, _arg114, _arg211, _arg36);
                            reply.writeNoException();
                            reply.writeString(_result18);
                            return true;
                        case 23:
                            String _arg021 = data.readString();
                            String _arg115 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result19 = startIpv6(_arg021, _arg115);
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 24:
                            String _arg022 = data.readString();
                            String _arg116 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result20 = stopIpv6(_arg022, _arg116);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 25:
                            String _arg023 = data.readString();
                            String _arg117 = data.readString();
                            String _arg212 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result21 = startIpClient(_arg023, _arg117, _arg212);
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 26:
                            String _arg024 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result22 = stopIpClient(_arg024);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNetworkCmdService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNetworkCmdService.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public void setSocketTos(String uid, String srcAdd, String srcPort, String desAdd, String desPort, String tos, String ifaceName, String protocol) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(uid);
                    _data.writeString(srcAdd);
                    _data.writeString(srcPort);
                    _data.writeString(desAdd);
                    _data.writeString(desPort);
                    _data.writeString(tos);
                    _data.writeString(ifaceName);
                    _data.writeString(protocol);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public void clearSocketTos(String uid, String srcAdd, String srcPort, String desAdd, String desPort, String tos, String ifaceName, String protocol) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(uid);
                    _data.writeString(srcAdd);
                    _data.writeString(srcPort);
                    _data.writeString(desAdd);
                    _data.writeString(desPort);
                    _data.writeString(tos);
                    _data.writeString(ifaceName);
                    _data.writeString(protocol);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public String oplusNetdCmdParse(String cmd, int[] param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(cmd);
                    _data.writeIntArray(param);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public void registerOplusNetdEvent(IOplusNetdEventCb cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public void unregisterOplusNetdEvent(IOplusNetdEventCb cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean setVirtualDeviceBindInfo(String virtualDevice, String bindDevice, String peerIp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(virtualDevice);
                    _data.writeString(bindDevice);
                    _data.writeString(peerIp);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean clearVirtualDeviceBindInfo(String virtualDevice) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(virtualDevice);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean ipForwardingEnableSet(String tag, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(tag);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean setVirtualDeviceBindInfoV4V6(String virtualDevice, String bindDevice, String peerIpv4, String peerIpV6) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(virtualDevice);
                    _data.writeString(bindDevice);
                    _data.writeString(peerIpv4);
                    _data.writeString(peerIpV6);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean interfaceAddAddress(String iface, String addrStr, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(addrStr);
                    _data.writeInt(prefixLength);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean interfaceDelAddress(String iface, String addrStr, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(addrStr);
                    _data.writeInt(prefixLength);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean tetherApplyDnsInterfaces() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean tetherOffloadRuleAdd(int iif, byte[] macAddr, byte[] neigh6, int oif, byte[] ethSrcMac, byte[] ethDstMac, int ethProto, int pmtu) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(iif);
                    _data.writeByteArray(macAddr);
                    _data.writeByteArray(neigh6);
                    _data.writeInt(oif);
                    _data.writeByteArray(ethSrcMac);
                    _data.writeByteArray(ethDstMac);
                    _data.writeInt(ethProto);
                    _data.writeInt(pmtu);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean tetherOffloadRuleRemove(int iif, byte[] macAddr, byte[] neigh6) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(iif);
                    _data.writeByteArray(macAddr);
                    _data.writeByteArray(neigh6);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean startUpstreamIpv6Forwarding(int downstreamIfindex, int upstreamIfindex, byte[] inDstMac, byte[] outSrcMac, byte[] outDstMac, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(downstreamIfindex);
                    _data.writeInt(upstreamIfindex);
                    _data.writeByteArray(inDstMac);
                    _data.writeByteArray(outSrcMac);
                    _data.writeByteArray(outDstMac);
                    _data.writeInt(mtu);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean stopUpstreamIpv6Forwarding(int downstreamIfindex, int upstreamIfindex, byte[] inDstMac) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(downstreamIfindex);
                    _data.writeInt(upstreamIfindex);
                    _data.writeByteArray(inDstMac);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean attachProgram(String iface, boolean downstream) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeBoolean(downstream);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean detachProgram(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean addDevMap(int ifIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(ifIndex);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean removeDevMap(int ifIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(ifIndex);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public StatsValueTotal getAllTetherStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    StatsValueTotal _result = (StatsValueTotal) _reply.readTypedObject(StatsValueTotal.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public String getProcSysNet(int ipversion, int which, String intf, String parameter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeInt(ipversion);
                    _data.writeInt(which);
                    _data.writeString(intf);
                    _data.writeString(parameter);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean startIpv6(String inIface, String extIface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(inIface);
                    _data.writeString(extIface);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean stopIpv6(String inIface, String extIface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(inIface);
                    _data.writeString(extIface);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean startIpClient(String iface, String staticAddr, String gateway) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(staticAddr);
                    _data.writeString(gateway);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetworkCmdService
            public boolean stopIpClient(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkCmdService.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 25;
        }
    }
}
