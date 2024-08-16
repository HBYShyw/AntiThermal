package android.bluetooth;

import android.bluetooth.IOplusBluetooth;
import android.bluetooth.IOplusBluetoothCallback;
import android.bluetooth.IOplusBluetoothOobDataCallback;
import android.content.AttributionSource;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.bluetooth.OplusBluetoothQoSData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
public interface IOplusBluetooth extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IOplusBluetooth";

    void addCarkit(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean allowToEnableFddMode() throws RemoteException;

    void clearCommunicationDevice(int i, String str) throws RemoteException;

    boolean createBondOutOfBand(int i, BluetoothDevice bluetoothDevice, OplusBluetoothOobData oplusBluetoothOobData, OplusBluetoothOobData oplusBluetoothOobData2, AttributionSource attributionSource) throws RemoteException;

    void disableAutoConnectPolicy(BluetoothDevice bluetoothDevice) throws RemoteException;

    void enableAutoConnectPolicy(BluetoothDevice bluetoothDevice) throws RemoteException;

    void generateLocalOobData(int i, IOplusBluetoothOobDataCallback iOplusBluetoothOobDataCallback, AttributionSource attributionSource) throws RemoteException;

    int[] getBluetoothConnectedAppPID() throws RemoteException;

    int getBluetoothConnectionCount() throws RemoteException;

    Map<String, String> getBluetoothMonitorReport(int i, boolean z) throws RemoteException;

    int getBluetoothRecordConnectedType() throws RemoteException;

    int getConfigDelayReport(BluetoothDevice bluetoothDevice, String str) throws RemoteException;

    OplusBluetoothQoSData getLinkStatus() throws RemoteException;

    String getRandomAddress() throws RemoteException;

    int getRemoteClass(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getRemoteDelayReport(BluetoothDevice bluetoothDevice) throws RemoteException;

    long getRemoteOplusFeatures(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    boolean isAbsoluteVolumeOn(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isBluetoothRecordConnected() throws RemoteException;

    boolean isBluetoothScoAvailableOffCall() throws RemoteException;

    boolean isCarkit(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isSupportAbsoluteVolume(BluetoothDevice bluetoothDevice) throws RemoteException;

    void onVoipCallStateChange(int i, String str) throws RemoteException;

    void oplusEnableVerboseLogging(boolean z) throws RemoteException;

    boolean oplusSetBTCTddMode(int i) throws RemoteException;

    void registerBluetoothCallback(IOplusBluetoothCallback iOplusBluetoothCallback) throws RemoteException;

    void rejectScoState(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str, boolean z7) throws RemoteException;

    void removeCarkit(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean setAbsoluteVolumeOn(BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    void setBLBlackOrWhiteList(List<String> list, int i, boolean z) throws RemoteException;

    void setCommunicationDevice(int i, String str, int i2) throws RemoteException;

    void setConfigDelayReport(BluetoothDevice bluetoothDevice, String str, int i) throws RemoteException;

    void setInsecureRfcommEnhanceMode(String str, String str2, int i) throws RemoteException;

    void setMode(int i, int i2, String str) throws RemoteException;

    void setSpeakerphoneOn(boolean z, int i, String str) throws RemoteException;

    void startBluetoothSco(int i, String str) throws RemoteException;

    void stopBluetoothSco(int i, String str) throws RemoteException;

    void triggerFirmwareCrash(String str) throws RemoteException;

    void unRegisterBluetoothCallback(IOplusBluetoothCallback iOplusBluetoothCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBluetooth {
        @Override // android.bluetooth.IOplusBluetooth
        public void setBLBlackOrWhiteList(List<String> addressList, int btCustomizeMode, boolean enable) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean isBluetoothScoAvailableOffCall() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public int getBluetoothConnectionCount() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public int[] getBluetoothConnectedAppPID() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void enableAutoConnectPolicy(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void disableAutoConnectPolicy(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void triggerFirmwareCrash(String callingPackage) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void oplusEnableVerboseLogging(boolean verbose) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean isCarkit(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void addCarkit(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void removeCarkit(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public Map<String, String> getBluetoothMonitorReport(int monitorId, boolean reset) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void onVoipCallStateChange(int callState, String packageName) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void registerBluetoothCallback(IOplusBluetoothCallback iCallback) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void unRegisterBluetoothCallback(IOplusBluetoothCallback iCallback) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void setMode(int mode, int callingPid, String callingPackage) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public int getBluetoothRecordConnectedType() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean isBluetoothRecordConnected() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void setSpeakerphoneOn(boolean on, int callingPid, String callingPackage) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void startBluetoothSco(int callingPid, String callingPackage) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void stopBluetoothSco(int callingPid, String callingPackage) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void setCommunicationDevice(int callingPid, String callingPackage, int deviceType) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void clearCommunicationDevice(int callingPid, String callingPackage) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public String getRandomAddress() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void rejectScoState(boolean scostate, boolean isforeground, boolean isSetMode, boolean isRecordingActive, boolean isPlaybackActive, boolean isPackageInFocus, String callingPackage, boolean detectResult) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean oplusSetBTCTddMode(int tddMode) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean allowToEnableFddMode() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void setInsecureRfcommEnhanceMode(String address, String uuid, int mode) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean createBondOutOfBand(int transport, BluetoothDevice device, OplusBluetoothOobData remoteP192Data, OplusBluetoothOobData remoteP256Data, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void generateLocalOobData(int transport, IOplusBluetoothOobDataCallback callback, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean isSupportAbsoluteVolume(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean isAbsoluteVolumeOn(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public boolean setAbsoluteVolumeOn(BluetoothDevice device, boolean on) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public OplusBluetoothQoSData getLinkStatus() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public int getRemoteClass(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public void setConfigDelayReport(BluetoothDevice device, String avPlayer, int delayReport) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetooth
        public int getRemoteDelayReport(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public int getConfigDelayReport(BluetoothDevice device, String avPlayer) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IOplusBluetooth
        public long getRemoteOplusFeatures(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return 0L;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBluetooth {
        static final int TRANSACTION_addCarkit = 10;
        static final int TRANSACTION_allowToEnableFddMode = 27;
        static final int TRANSACTION_clearCommunicationDevice = 23;
        static final int TRANSACTION_createBondOutOfBand = 29;
        static final int TRANSACTION_disableAutoConnectPolicy = 6;
        static final int TRANSACTION_enableAutoConnectPolicy = 5;
        static final int TRANSACTION_generateLocalOobData = 30;
        static final int TRANSACTION_getBluetoothConnectedAppPID = 4;
        static final int TRANSACTION_getBluetoothConnectionCount = 3;
        static final int TRANSACTION_getBluetoothMonitorReport = 12;
        static final int TRANSACTION_getBluetoothRecordConnectedType = 17;
        static final int TRANSACTION_getConfigDelayReport = 38;
        static final int TRANSACTION_getLinkStatus = 34;
        static final int TRANSACTION_getRandomAddress = 24;
        static final int TRANSACTION_getRemoteClass = 35;
        static final int TRANSACTION_getRemoteDelayReport = 37;
        static final int TRANSACTION_getRemoteOplusFeatures = 39;
        static final int TRANSACTION_isAbsoluteVolumeOn = 32;
        static final int TRANSACTION_isBluetoothRecordConnected = 18;
        static final int TRANSACTION_isBluetoothScoAvailableOffCall = 2;
        static final int TRANSACTION_isCarkit = 9;
        static final int TRANSACTION_isSupportAbsoluteVolume = 31;
        static final int TRANSACTION_onVoipCallStateChange = 13;
        static final int TRANSACTION_oplusEnableVerboseLogging = 8;
        static final int TRANSACTION_oplusSetBTCTddMode = 26;
        static final int TRANSACTION_registerBluetoothCallback = 14;
        static final int TRANSACTION_rejectScoState = 25;
        static final int TRANSACTION_removeCarkit = 11;
        static final int TRANSACTION_setAbsoluteVolumeOn = 33;
        static final int TRANSACTION_setBLBlackOrWhiteList = 1;
        static final int TRANSACTION_setCommunicationDevice = 22;
        static final int TRANSACTION_setConfigDelayReport = 36;
        static final int TRANSACTION_setInsecureRfcommEnhanceMode = 28;
        static final int TRANSACTION_setMode = 16;
        static final int TRANSACTION_setSpeakerphoneOn = 19;
        static final int TRANSACTION_startBluetoothSco = 20;
        static final int TRANSACTION_stopBluetoothSco = 21;
        static final int TRANSACTION_triggerFirmwareCrash = 7;
        static final int TRANSACTION_unRegisterBluetoothCallback = 15;

        public Stub() {
            attachInterface(this, IOplusBluetooth.DESCRIPTOR);
        }

        public static IOplusBluetooth asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBluetooth.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBluetooth)) {
                return (IOplusBluetooth) iin;
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
                    return "setBLBlackOrWhiteList";
                case 2:
                    return "isBluetoothScoAvailableOffCall";
                case 3:
                    return "getBluetoothConnectionCount";
                case 4:
                    return "getBluetoothConnectedAppPID";
                case 5:
                    return "enableAutoConnectPolicy";
                case 6:
                    return "disableAutoConnectPolicy";
                case 7:
                    return "triggerFirmwareCrash";
                case 8:
                    return "oplusEnableVerboseLogging";
                case 9:
                    return "isCarkit";
                case 10:
                    return "addCarkit";
                case 11:
                    return "removeCarkit";
                case 12:
                    return "getBluetoothMonitorReport";
                case 13:
                    return "onVoipCallStateChange";
                case 14:
                    return "registerBluetoothCallback";
                case 15:
                    return "unRegisterBluetoothCallback";
                case 16:
                    return "setMode";
                case 17:
                    return "getBluetoothRecordConnectedType";
                case 18:
                    return "isBluetoothRecordConnected";
                case 19:
                    return "setSpeakerphoneOn";
                case 20:
                    return "startBluetoothSco";
                case 21:
                    return "stopBluetoothSco";
                case 22:
                    return "setCommunicationDevice";
                case 23:
                    return "clearCommunicationDevice";
                case 24:
                    return "getRandomAddress";
                case 25:
                    return "rejectScoState";
                case 26:
                    return "oplusSetBTCTddMode";
                case 27:
                    return "allowToEnableFddMode";
                case 28:
                    return "setInsecureRfcommEnhanceMode";
                case 29:
                    return "createBondOutOfBand";
                case 30:
                    return "generateLocalOobData";
                case 31:
                    return "isSupportAbsoluteVolume";
                case 32:
                    return "isAbsoluteVolumeOn";
                case 33:
                    return "setAbsoluteVolumeOn";
                case 34:
                    return "getLinkStatus";
                case 35:
                    return "getRemoteClass";
                case 36:
                    return "setConfigDelayReport";
                case 37:
                    return "getRemoteDelayReport";
                case 38:
                    return "getConfigDelayReport";
                case 39:
                    return "getRemoteOplusFeatures";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, final Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusBluetooth.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBluetooth.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<String> _arg0 = data.createStringArrayList();
                            int _arg1 = data.readInt();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBLBlackOrWhiteList(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            boolean _result = isBluetoothScoAvailableOffCall();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            int _result2 = getBluetoothConnectionCount();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 4:
                            int[] _result3 = getBluetoothConnectedAppPID();
                            reply.writeNoException();
                            reply.writeIntArray(_result3);
                            return true;
                        case 5:
                            BluetoothDevice _arg02 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            enableAutoConnectPolicy(_arg02);
                            return true;
                        case 6:
                            BluetoothDevice _arg03 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            disableAutoConnectPolicy(_arg03);
                            return true;
                        case 7:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            triggerFirmwareCrash(_arg04);
                            return true;
                        case 8:
                            boolean _arg05 = data.readBoolean();
                            data.enforceNoDataAvail();
                            oplusEnableVerboseLogging(_arg05);
                            return true;
                        case 9:
                            BluetoothDevice _arg06 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result4 = isCarkit(_arg06);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 10:
                            BluetoothDevice _arg07 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            addCarkit(_arg07);
                            return true;
                        case 11:
                            BluetoothDevice _arg08 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            removeCarkit(_arg08);
                            return true;
                        case 12:
                            int _arg09 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            Map<String, String> _result5 = getBluetoothMonitorReport(_arg09, _arg12);
                            reply.writeNoException();
                            if (_result5 == null) {
                                reply.writeInt(-1);
                            } else {
                                reply.writeInt(_result5.size());
                                _result5.forEach(new BiConsumer() { // from class: android.bluetooth.IOplusBluetooth$Stub$$ExternalSyntheticLambda0
                                    @Override // java.util.function.BiConsumer
                                    public final void accept(Object obj, Object obj2) {
                                        IOplusBluetooth.Stub.lambda$onTransact$0(reply, (String) obj, (String) obj2);
                                    }
                                });
                            }
                            return true;
                        case 13:
                            int _arg010 = data.readInt();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            onVoipCallStateChange(_arg010, _arg13);
                            return true;
                        case 14:
                            IOplusBluetoothCallback _arg011 = IOplusBluetoothCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerBluetoothCallback(_arg011);
                            return true;
                        case 15:
                            IOplusBluetoothCallback _arg012 = IOplusBluetoothCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unRegisterBluetoothCallback(_arg012);
                            return true;
                        case 16:
                            int _arg013 = data.readInt();
                            int _arg14 = data.readInt();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            setMode(_arg013, _arg14, _arg22);
                            return true;
                        case 17:
                            int _result6 = getBluetoothRecordConnectedType();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 18:
                            boolean _result7 = isBluetoothRecordConnected();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 19:
                            boolean _arg014 = data.readBoolean();
                            int _arg15 = data.readInt();
                            String _arg23 = data.readString();
                            data.enforceNoDataAvail();
                            setSpeakerphoneOn(_arg014, _arg15, _arg23);
                            return true;
                        case 20:
                            int _arg015 = data.readInt();
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            startBluetoothSco(_arg015, _arg16);
                            return true;
                        case 21:
                            int _arg016 = data.readInt();
                            String _arg17 = data.readString();
                            data.enforceNoDataAvail();
                            stopBluetoothSco(_arg016, _arg17);
                            return true;
                        case 22:
                            int _arg017 = data.readInt();
                            String _arg18 = data.readString();
                            int _arg24 = data.readInt();
                            data.enforceNoDataAvail();
                            setCommunicationDevice(_arg017, _arg18, _arg24);
                            return true;
                        case 23:
                            int _arg018 = data.readInt();
                            String _arg19 = data.readString();
                            data.enforceNoDataAvail();
                            clearCommunicationDevice(_arg018, _arg19);
                            return true;
                        case 24:
                            String _result8 = getRandomAddress();
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 25:
                            boolean _arg019 = data.readBoolean();
                            boolean _arg110 = data.readBoolean();
                            boolean _arg25 = data.readBoolean();
                            boolean _arg3 = data.readBoolean();
                            boolean _arg4 = data.readBoolean();
                            boolean _arg5 = data.readBoolean();
                            String _arg6 = data.readString();
                            boolean _arg7 = data.readBoolean();
                            data.enforceNoDataAvail();
                            rejectScoState(_arg019, _arg110, _arg25, _arg3, _arg4, _arg5, _arg6, _arg7);
                            return true;
                        case 26:
                            int _arg020 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = oplusSetBTCTddMode(_arg020);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 27:
                            boolean _result10 = allowToEnableFddMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 28:
                            String _arg021 = data.readString();
                            String _arg111 = data.readString();
                            int _arg26 = data.readInt();
                            data.enforceNoDataAvail();
                            setInsecureRfcommEnhanceMode(_arg021, _arg111, _arg26);
                            return true;
                        case 29:
                            int _arg022 = data.readInt();
                            BluetoothDevice _arg112 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            OplusBluetoothOobData _arg27 = (OplusBluetoothOobData) data.readTypedObject(OplusBluetoothOobData.CREATOR);
                            OplusBluetoothOobData _arg32 = (OplusBluetoothOobData) data.readTypedObject(OplusBluetoothOobData.CREATOR);
                            AttributionSource _arg42 = (AttributionSource) data.readTypedObject(AttributionSource.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result11 = createBondOutOfBand(_arg022, _arg112, _arg27, _arg32, _arg42);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 30:
                            int _arg023 = data.readInt();
                            IOplusBluetoothOobDataCallback _arg113 = IOplusBluetoothOobDataCallback.Stub.asInterface(data.readStrongBinder());
                            AttributionSource _arg28 = (AttributionSource) data.readTypedObject(AttributionSource.CREATOR);
                            data.enforceNoDataAvail();
                            generateLocalOobData(_arg023, _arg113, _arg28);
                            return true;
                        case 31:
                            BluetoothDevice _arg024 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result12 = isSupportAbsoluteVolume(_arg024);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 32:
                            BluetoothDevice _arg025 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result13 = isAbsoluteVolumeOn(_arg025);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 33:
                            BluetoothDevice _arg026 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            boolean _arg114 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result14 = setAbsoluteVolumeOn(_arg026, _arg114);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 34:
                            OplusBluetoothQoSData _result15 = getLinkStatus();
                            reply.writeNoException();
                            reply.writeTypedObject(_result15, 1);
                            return true;
                        case 35:
                            BluetoothDevice _arg027 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            int _result16 = getRemoteClass(_arg027);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 36:
                            BluetoothDevice _arg028 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            String _arg115 = data.readString();
                            int _arg29 = data.readInt();
                            data.enforceNoDataAvail();
                            setConfigDelayReport(_arg028, _arg115, _arg29);
                            reply.writeNoException();
                            return true;
                        case 37:
                            BluetoothDevice _arg029 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            data.enforceNoDataAvail();
                            int _result17 = getRemoteDelayReport(_arg029);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 38:
                            BluetoothDevice _arg030 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            String _arg116 = data.readString();
                            data.enforceNoDataAvail();
                            int _result18 = getConfigDelayReport(_arg030, _arg116);
                            reply.writeNoException();
                            reply.writeInt(_result18);
                            return true;
                        case 39:
                            BluetoothDevice _arg031 = (BluetoothDevice) data.readTypedObject(BluetoothDevice.CREATOR);
                            AttributionSource _arg117 = (AttributionSource) data.readTypedObject(AttributionSource.CREATOR);
                            data.enforceNoDataAvail();
                            long _result19 = getRemoteOplusFeatures(_arg031, _arg117);
                            reply.writeNoException();
                            reply.writeLong(_result19);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$0(Parcel reply, String k, String v) {
            reply.writeString(k);
            reply.writeString(v);
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBluetooth {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBluetooth.DESCRIPTOR;
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void setBLBlackOrWhiteList(List<String> addressList, int btCustomizeMode, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeStringList(addressList);
                    _data.writeInt(btCustomizeMode);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean isBluetoothScoAvailableOffCall() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public int getBluetoothConnectionCount() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public int[] getBluetoothConnectedAppPID() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void enableAutoConnectPolicy(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void disableAutoConnectPolicy(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void triggerFirmwareCrash(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void oplusEnableVerboseLogging(boolean verbose) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeBoolean(verbose);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean isCarkit(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void addCarkit(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void removeCarkit(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public Map<String, String> getBluetoothMonitorReport(int monitorId, boolean reset) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(monitorId);
                    _data.writeBoolean(reset);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int N = _reply.readInt();
                    final Map<String, String> _result = N < 0 ? null : new HashMap<>();
                    IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.bluetooth.IOplusBluetooth$Stub$Proxy$$ExternalSyntheticLambda0
                        @Override // java.util.function.IntConsumer
                        public final void accept(int i) {
                            IOplusBluetooth.Stub.Proxy.lambda$getBluetoothMonitorReport$0(_reply, _result, i);
                        }
                    });
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$getBluetoothMonitorReport$0(Parcel _reply, Map _result, int i) {
                String k = _reply.readString();
                String v = _reply.readString();
                _result.put(k, v);
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void onVoipCallStateChange(int callState, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(callState);
                    _data.writeString(packageName);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void registerBluetoothCallback(IOplusBluetoothCallback iCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeStrongInterface(iCallback);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void unRegisterBluetoothCallback(IOplusBluetoothCallback iCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeStrongInterface(iCallback);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void setMode(int mode, int callingPid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(callingPid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public int getBluetoothRecordConnectedType() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean isBluetoothRecordConnected() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void setSpeakerphoneOn(boolean on, int callingPid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeBoolean(on);
                    _data.writeInt(callingPid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void startBluetoothSco(int callingPid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(callingPid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void stopBluetoothSco(int callingPid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(callingPid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void setCommunicationDevice(int callingPid, String callingPackage, int deviceType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(callingPid);
                    _data.writeString(callingPackage);
                    _data.writeInt(deviceType);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void clearCommunicationDevice(int callingPid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(callingPid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public String getRandomAddress() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void rejectScoState(boolean scostate, boolean isforeground, boolean isSetMode, boolean isRecordingActive, boolean isPlaybackActive, boolean isPackageInFocus, String callingPackage, boolean detectResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeBoolean(scostate);
                    _data.writeBoolean(isforeground);
                    _data.writeBoolean(isSetMode);
                    _data.writeBoolean(isRecordingActive);
                    _data.writeBoolean(isPlaybackActive);
                    _data.writeBoolean(isPackageInFocus);
                    _data.writeString(callingPackage);
                    _data.writeBoolean(detectResult);
                    this.mRemote.transact(25, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean oplusSetBTCTddMode(int tddMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(tddMode);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean allowToEnableFddMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void setInsecureRfcommEnhanceMode(String address, String uuid, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(uuid);
                    _data.writeInt(mode);
                    this.mRemote.transact(28, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean createBondOutOfBand(int transport, BluetoothDevice device, OplusBluetoothOobData remoteP192Data, OplusBluetoothOobData remoteP256Data, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(transport);
                    _data.writeTypedObject(device, 0);
                    _data.writeTypedObject(remoteP192Data, 0);
                    _data.writeTypedObject(remoteP256Data, 0);
                    _data.writeTypedObject(attributionSource, 0);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void generateLocalOobData(int transport, IOplusBluetoothOobDataCallback callback, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeInt(transport);
                    _data.writeStrongInterface(callback);
                    _data.writeTypedObject(attributionSource, 0);
                    this.mRemote.transact(30, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean isSupportAbsoluteVolume(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean isAbsoluteVolumeOn(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public boolean setAbsoluteVolumeOn(BluetoothDevice device, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    _data.writeBoolean(on);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public OplusBluetoothQoSData getLinkStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    OplusBluetoothQoSData _result = (OplusBluetoothQoSData) _reply.readTypedObject(OplusBluetoothQoSData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public int getRemoteClass(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public void setConfigDelayReport(BluetoothDevice device, String avPlayer, int delayReport) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    _data.writeString(avPlayer);
                    _data.writeInt(delayReport);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public int getRemoteDelayReport(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public int getConfigDelayReport(BluetoothDevice device, String avPlayer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    _data.writeString(avPlayer);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetooth
            public long getRemoteOplusFeatures(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetooth.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    _data.writeTypedObject(attributionSource, 0);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 38;
        }
    }
}
