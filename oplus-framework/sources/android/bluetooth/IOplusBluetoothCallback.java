package android.bluetooth;

import android.bluetooth.IOplusBluetoothCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
public interface IOplusBluetoothCallback extends IInterface {
    public static final int ADAPTER_SERVICE_ERROR_REPORT_CB = 200002;
    public static final int BLUETOOTH_DIAGNOSE_TOOL_REPORT_CB = 200003;
    public static final int BLUETOOTH_SCENARIO_INFO_REPORT_CB = 200004;
    public static final int BLUETOOTH_SERVICE_BOOT_TIME_CB = 200005;
    public static final String DESCRIPTOR = "android.bluetooth.IOplusBluetoothCallback";
    public static final int RSSI_DETECT_CB = 200001;

    void bluetoothCallbackRawdata(int i, Map<String, String> map) throws RemoteException;

    void recordAdapterErrorState(int i, String str) throws RemoteException;

    void sendNativeRecordEvent(String str, Map map) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBluetoothCallback {
        @Override // android.bluetooth.IOplusBluetoothCallback
        public void recordAdapterErrorState(int errorCode, String params) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetoothCallback
        public void bluetoothCallbackRawdata(int cbCode, Map<String, String> rawData) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetoothCallback
        public void sendNativeRecordEvent(String monitorEvent, Map monitResult) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBluetoothCallback {
        static final int TRANSACTION_bluetoothCallbackRawdata = 2;
        static final int TRANSACTION_recordAdapterErrorState = 1;
        static final int TRANSACTION_sendNativeRecordEvent = 3;

        public Stub() {
            attachInterface(this, IOplusBluetoothCallback.DESCRIPTOR);
        }

        public static IOplusBluetoothCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBluetoothCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBluetoothCallback)) {
                return (IOplusBluetoothCallback) iin;
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
                    return "recordAdapterErrorState";
                case 2:
                    return "bluetoothCallbackRawdata";
                case 3:
                    return "sendNativeRecordEvent";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, final Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusBluetoothCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBluetoothCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            recordAdapterErrorState(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int N = data.readInt();
                            final Map<String, String> _arg12 = N < 0 ? null : new HashMap<>();
                            IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.bluetooth.IOplusBluetoothCallback$Stub$$ExternalSyntheticLambda0
                                @Override // java.util.function.IntConsumer
                                public final void accept(int i) {
                                    IOplusBluetoothCallback.Stub.lambda$onTransact$0(data, _arg12, i);
                                }
                            });
                            data.enforceNoDataAvail();
                            bluetoothCallbackRawdata(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg13 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            sendNativeRecordEvent(_arg03, _arg13);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$0(Parcel data, Map _arg1, int i) {
            String k = data.readString();
            String v = data.readString();
            _arg1.put(k, v);
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBluetoothCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBluetoothCallback.DESCRIPTOR;
            }

            @Override // android.bluetooth.IOplusBluetoothCallback
            public void recordAdapterErrorState(int errorCode, String params) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothCallback.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    _data.writeString(params);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetoothCallback
            public void bluetoothCallbackRawdata(int cbCode, Map<String, String> rawData) throws RemoteException {
                final Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothCallback.DESCRIPTOR);
                    _data.writeInt(cbCode);
                    if (rawData == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(rawData.size());
                        rawData.forEach(new BiConsumer() { // from class: android.bluetooth.IOplusBluetoothCallback$Stub$Proxy$$ExternalSyntheticLambda0
                            @Override // java.util.function.BiConsumer
                            public final void accept(Object obj, Object obj2) {
                                IOplusBluetoothCallback.Stub.Proxy.lambda$bluetoothCallbackRawdata$0(_data, (String) obj, (String) obj2);
                            }
                        });
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$bluetoothCallbackRawdata$0(Parcel _data, String k, String v) {
                _data.writeString(k);
                _data.writeString(v);
            }

            @Override // android.bluetooth.IOplusBluetoothCallback
            public void sendNativeRecordEvent(String monitorEvent, Map monitResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothCallback.DESCRIPTOR);
                    _data.writeString(monitorEvent);
                    _data.writeMap(monitResult);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
