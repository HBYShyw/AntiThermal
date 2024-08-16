package com.oplus.oiface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.oiface.IOIfaceCallback;

/* loaded from: classes.dex */
public interface IOIfaceInternalService extends IInterface {
    void bindGameTask(int i, int i2) throws RemoteException;

    void currentNetwork(int i) throws RemoteException;

    void currentPkgStatus(int i, String str, String str2, String str3) throws RemoteException;

    void enableHQV(int i) throws RemoteException;

    void enableHapticScreenCaptureService(int i) throws RemoteException;

    String generalOifaceSignal(String str) throws RemoteException;

    String getAllLoadInfo(String str) throws RemoteException;

    float getBatteryCurrentNow() throws RemoteException;

    int getBatteryFCC() throws RemoteException;

    int getBatteryRemain() throws RemoteException;

    String getChipName() throws RemoteException;

    long[] getCpuAvailableFreqTable(int i) throws RemoteException;

    int[] getCpuClusterInfo() throws RemoteException;

    int getCpuClusterNum() throws RemoteException;

    long[] getCpuCurrentFreq(int i) throws RemoteException;

    long[] getCpuLimitedFreqs(int i) throws RemoteException;

    float[] getCpuLoads(int i) throws RemoteException;

    String getCpuTimeInState() throws RemoteException;

    String getCurrentGamePackage() throws RemoteException;

    String getDeviceID() throws RemoteException;

    int getFPS(String str, int i) throws RemoteException;

    float[] getGPASystemInfo() throws RemoteException;

    int getGameModeStatus() throws RemoteException;

    long[] getGpuAvailableFreqTable() throws RemoteException;

    long getGpuCurrentFreq() throws RemoteException;

    long[] getGpuLimitedFreqs() throws RemoteException;

    float getGpuLoad() throws RemoteException;

    String[] getInstalledGameList() throws RemoteException;

    int getSuperVOOCStatus() throws RemoteException;

    String getSupportGameStartPackage() throws RemoteException;

    float[] getThermalTemps(int i) throws RemoteException;

    void notifyScreenEvent(int i) throws RemoteException;

    void oifaceControl(String str) throws RemoteException;

    void oifaceDecision(String str) throws RemoteException;

    int registerClientThroughCosa(IOIfaceCallback iOIfaceCallback, String str, int i, int i2) throws RemoteException;

    void registerGameRoleListener(int i, String str, IOIfaceCallback iOIfaceCallback) throws RemoteException;

    String registerHQV(String str, int i, String str2) throws RemoteException;

    void registerNetworkListener(int i, int i2, IOIfaceCallback iOIfaceCallback) throws RemoteException;

    void registerOifaceCallback(IOIfaceCallback iOIfaceCallback) throws RemoteException;

    void setCoolExFilterType(int i, String str) throws RemoteException;

    String setGCPEffectMode(int i) throws RemoteException;

    void setGameModeStatus(int i, String str) throws RemoteException;

    String setGeneralSignalCosa(String str, int i, int i2) throws RemoteException;

    void setGyroscopeLevel(int i) throws RemoteException;

    void setHalfHQV(int i) throws RemoteException;

    void setInstalledGameList(String[] strArr) throws RemoteException;

    void setPerfMode(int i) throws RemoteException;

    void setTouchProtection(boolean z) throws RemoteException;

    void setTouchResponsiveness(int i) throws RemoteException;

    void setTouchSensibility(int i) throws RemoteException;

    String triggerFrameStat(String str, String str2) throws RemoteException;

    void unRegisterGameRoleListener(IOIfaceCallback iOIfaceCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOIfaceInternalService {
        @Override // com.oplus.oiface.IOIfaceInternalService
        public void currentNetwork(int status) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void bindGameTask(int type, int tid) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void enableHQV(int enable) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String registerHQV(String packageName, int type, String config) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setHalfHQV(int half) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int registerClientThroughCosa(IOIfaceCallback oifaceCallback, String json, int uid, int pid) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String setGeneralSignalCosa(String json, int uid, int pid) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void registerNetworkListener(int thresholdMs, int minReportMs, IOIfaceCallback oifaceCallback) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void registerGameRoleListener(int type, String config, IOIfaceCallback oifaceCallback) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void unRegisterGameRoleListener(IOIfaceCallback oifaceCallback) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String getSupportGameStartPackage() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String getAllLoadInfo(String packageName) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String setGCPEffectMode(int mode) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setPerfMode(int mode) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void notifyScreenEvent(int mode) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String getDeviceID() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void currentPkgStatus(int status, String packageName, String newPIPPackage, String oldPIPPackage) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int getFPS(String packageName, int type) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String generalOifaceSignal(String signal) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void oifaceDecision(String decision) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void oifaceControl(String control) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int getCpuClusterNum() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public long[] getCpuAvailableFreqTable(int type) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public long[] getCpuLimitedFreqs(int type) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public long[] getCpuCurrentFreq(int type) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public float[] getCpuLoads(int type) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public long[] getGpuAvailableFreqTable() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public long[] getGpuLimitedFreqs() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public long getGpuCurrentFreq() throws RemoteException {
            return 0L;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public float getGpuLoad() throws RemoteException {
            return 0.0f;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public float[] getThermalTemps(int type) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void enableHapticScreenCaptureService(int enable) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setTouchSensibility(int level) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setTouchResponsiveness(int level) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setGyroscopeLevel(int level) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setTouchProtection(boolean enabled) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void registerOifaceCallback(IOIfaceCallback oifaceCallback) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int getBatteryRemain() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public float getBatteryCurrentNow() throws RemoteException {
            return 0.0f;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int getSuperVOOCStatus() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public float[] getGPASystemInfo() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setCoolExFilterType(int type, String config) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setGameModeStatus(int status, String packageName) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int getGameModeStatus() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String getCurrentGamePackage() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public void setInstalledGameList(String[] games) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String[] getInstalledGameList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String getCpuTimeInState() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String triggerFrameStat(String status, String packageName) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public String getChipName() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int[] getCpuClusterInfo() throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceInternalService
        public int getBatteryFCC() throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOIfaceInternalService {
        private static final String DESCRIPTOR = "com.oplus.oiface.IOIfaceService";
        static final int TRANSACTION_bindGameTask = 7;
        static final int TRANSACTION_currentNetwork = 1;
        static final int TRANSACTION_currentPkgStatus = 803;
        static final int TRANSACTION_enableHQV = 11;
        static final int TRANSACTION_enableHapticScreenCaptureService = 901;
        static final int TRANSACTION_generalOifaceSignal = 852;
        static final int TRANSACTION_getAllLoadInfo = 212;
        static final int TRANSACTION_getBatteryCurrentNow = 1003;
        static final int TRANSACTION_getBatteryFCC = 1016;
        static final int TRANSACTION_getBatteryRemain = 1002;
        static final int TRANSACTION_getChipName = 1014;
        static final int TRANSACTION_getCpuAvailableFreqTable = 856;
        static final int TRANSACTION_getCpuClusterInfo = 1015;
        static final int TRANSACTION_getCpuClusterNum = 855;
        static final int TRANSACTION_getCpuCurrentFreq = 858;
        static final int TRANSACTION_getCpuLimitedFreqs = 857;
        static final int TRANSACTION_getCpuLoads = 859;
        static final int TRANSACTION_getCpuTimeInState = 1012;
        static final int TRANSACTION_getCurrentGamePackage = 1009;
        static final int TRANSACTION_getDeviceID = 504;
        static final int TRANSACTION_getFPS = 851;
        static final int TRANSACTION_getGPASystemInfo = 1005;
        static final int TRANSACTION_getGameModeStatus = 1008;
        static final int TRANSACTION_getGpuAvailableFreqTable = 860;
        static final int TRANSACTION_getGpuCurrentFreq = 862;
        static final int TRANSACTION_getGpuLimitedFreqs = 861;
        static final int TRANSACTION_getGpuLoad = 863;
        static final int TRANSACTION_getInstalledGameList = 1011;
        static final int TRANSACTION_getSuperVOOCStatus = 1004;
        static final int TRANSACTION_getSupportGameStartPackage = 206;
        static final int TRANSACTION_getThermalTemps = 864;
        static final int TRANSACTION_notifyScreenEvent = 503;
        static final int TRANSACTION_oifaceControl = 854;
        static final int TRANSACTION_oifaceDecision = 853;
        static final int TRANSACTION_registerClientThroughCosa = 161;
        static final int TRANSACTION_registerGameRoleListener = 204;
        static final int TRANSACTION_registerHQV = 12;
        static final int TRANSACTION_registerNetworkListener = 202;
        static final int TRANSACTION_registerOifaceCallback = 1001;
        static final int TRANSACTION_setCoolExFilterType = 1006;
        static final int TRANSACTION_setGCPEffectMode = 211;
        static final int TRANSACTION_setGameModeStatus = 1007;
        static final int TRANSACTION_setGeneralSignalCosa = 162;
        static final int TRANSACTION_setGyroscopeLevel = 904;
        static final int TRANSACTION_setHalfHQV = 13;
        static final int TRANSACTION_setInstalledGameList = 1010;
        static final int TRANSACTION_setPerfMode = 502;
        static final int TRANSACTION_setTouchProtection = 905;
        static final int TRANSACTION_setTouchResponsiveness = 903;
        static final int TRANSACTION_setTouchSensibility = 902;
        static final int TRANSACTION_triggerFrameStat = 1013;
        static final int TRANSACTION_unRegisterGameRoleListener = 205;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOIfaceInternalService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOIfaceInternalService)) {
                return (IOIfaceInternalService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    currentNetwork(_arg0);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int _arg1 = data.readInt();
                    bindGameTask(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    enableHQV(_arg03);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg12 = data.readInt();
                    String _arg2 = data.readString();
                    String _result = registerHQV(_arg04, _arg12, _arg2);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    setHalfHQV(_arg05);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    IOIfaceCallback _arg06 = IOIfaceCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg13 = data.readString();
                    int _arg22 = data.readInt();
                    int _arg3 = data.readInt();
                    int _result2 = registerClientThroughCosa(_arg06, _arg13, _arg22, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case TRANSACTION_setGeneralSignalCosa /* 162 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg23 = data.readInt();
                    String _result3 = setGeneralSignalCosa(_arg07, _arg14, _arg23);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg15 = data.readInt();
                    IOIfaceCallback _arg24 = IOIfaceCallback.Stub.asInterface(data.readStrongBinder());
                    registerNetworkListener(_arg08, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    String _arg16 = data.readString();
                    IOIfaceCallback _arg25 = IOIfaceCallback.Stub.asInterface(data.readStrongBinder());
                    registerGameRoleListener(_arg09, _arg16, _arg25);
                    reply.writeNoException();
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    IOIfaceCallback _arg010 = IOIfaceCallback.Stub.asInterface(data.readStrongBinder());
                    unRegisterGameRoleListener(_arg010);
                    reply.writeNoException();
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getSupportGameStartPackage();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _result5 = setGCPEffectMode(_arg011);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _result6 = getAllLoadInfo(_arg012);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 502:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    setPerfMode(_arg013);
                    reply.writeNoException();
                    return true;
                case 503:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    notifyScreenEvent(_arg014);
                    reply.writeNoException();
                    return true;
                case 504:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getDeviceID();
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 803:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    String _arg17 = data.readString();
                    String _arg26 = data.readString();
                    String _arg32 = data.readString();
                    currentPkgStatus(_arg015, _arg17, _arg26, _arg32);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getFPS /* 851 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    int _arg18 = data.readInt();
                    int _result8 = getFPS(_arg016, _arg18);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case TRANSACTION_generalOifaceSignal /* 852 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    String _result9 = generalOifaceSignal(_arg017);
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case TRANSACTION_oifaceDecision /* 853 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    oifaceDecision(_arg018);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_oifaceControl /* 854 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    oifaceControl(_arg019);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getCpuClusterNum /* 855 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result10 = getCpuClusterNum();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case TRANSACTION_getCpuAvailableFreqTable /* 856 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    long[] _result11 = getCpuAvailableFreqTable(_arg020);
                    reply.writeNoException();
                    reply.writeLongArray(_result11);
                    return true;
                case TRANSACTION_getCpuLimitedFreqs /* 857 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    long[] _result12 = getCpuLimitedFreqs(_arg021);
                    reply.writeNoException();
                    reply.writeLongArray(_result12);
                    return true;
                case TRANSACTION_getCpuCurrentFreq /* 858 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    long[] _result13 = getCpuCurrentFreq(_arg022);
                    reply.writeNoException();
                    reply.writeLongArray(_result13);
                    return true;
                case TRANSACTION_getCpuLoads /* 859 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    float[] _result14 = getCpuLoads(_arg023);
                    reply.writeNoException();
                    reply.writeFloatArray(_result14);
                    return true;
                case TRANSACTION_getGpuAvailableFreqTable /* 860 */:
                    data.enforceInterface(DESCRIPTOR);
                    long[] _result15 = getGpuAvailableFreqTable();
                    reply.writeNoException();
                    reply.writeLongArray(_result15);
                    return true;
                case TRANSACTION_getGpuLimitedFreqs /* 861 */:
                    data.enforceInterface(DESCRIPTOR);
                    long[] _result16 = getGpuLimitedFreqs();
                    reply.writeNoException();
                    reply.writeLongArray(_result16);
                    return true;
                case TRANSACTION_getGpuCurrentFreq /* 862 */:
                    data.enforceInterface(DESCRIPTOR);
                    long _result17 = getGpuCurrentFreq();
                    reply.writeNoException();
                    reply.writeLong(_result17);
                    return true;
                case TRANSACTION_getGpuLoad /* 863 */:
                    data.enforceInterface(DESCRIPTOR);
                    float _result18 = getGpuLoad();
                    reply.writeNoException();
                    reply.writeFloat(_result18);
                    return true;
                case TRANSACTION_getThermalTemps /* 864 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    float[] _result19 = getThermalTemps(_arg024);
                    reply.writeNoException();
                    reply.writeFloatArray(_result19);
                    return true;
                case TRANSACTION_enableHapticScreenCaptureService /* 901 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    enableHapticScreenCaptureService(_arg025);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setTouchSensibility /* 902 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    setTouchSensibility(_arg026);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setTouchResponsiveness /* 903 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    setTouchResponsiveness(_arg027);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setGyroscopeLevel /* 904 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    setGyroscopeLevel(_arg028);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setTouchProtection /* 905 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg029 = data.readInt() != 0;
                    setTouchProtection(_arg029);
                    reply.writeNoException();
                    return true;
                case 1001:
                    data.enforceInterface(DESCRIPTOR);
                    IOIfaceCallback _arg030 = IOIfaceCallback.Stub.asInterface(data.readStrongBinder());
                    registerOifaceCallback(_arg030);
                    reply.writeNoException();
                    return true;
                case 1002:
                    data.enforceInterface(DESCRIPTOR);
                    int _result20 = getBatteryRemain();
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 1003:
                    data.enforceInterface(DESCRIPTOR);
                    float _result21 = getBatteryCurrentNow();
                    reply.writeNoException();
                    reply.writeFloat(_result21);
                    return true;
                case 1004:
                    data.enforceInterface(DESCRIPTOR);
                    int _result22 = getSuperVOOCStatus();
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 1005:
                    data.enforceInterface(DESCRIPTOR);
                    float[] _result23 = getGPASystemInfo();
                    reply.writeNoException();
                    reply.writeFloatArray(_result23);
                    return true;
                case 1006:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    String _arg19 = data.readString();
                    setCoolExFilterType(_arg031, _arg19);
                    reply.writeNoException();
                    return true;
                case 1007:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    String _arg110 = data.readString();
                    setGameModeStatus(_arg032, _arg110);
                    reply.writeNoException();
                    return true;
                case 1008:
                    data.enforceInterface(DESCRIPTOR);
                    int _result24 = getGameModeStatus();
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 1009:
                    data.enforceInterface(DESCRIPTOR);
                    String _result25 = getCurrentGamePackage();
                    reply.writeNoException();
                    reply.writeString(_result25);
                    return true;
                case 1010:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg033 = data.createStringArray();
                    setInstalledGameList(_arg033);
                    reply.writeNoException();
                    return true;
                case 1011:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result26 = getInstalledGameList();
                    reply.writeNoException();
                    reply.writeStringArray(_result26);
                    return true;
                case TRANSACTION_getCpuTimeInState /* 1012 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result27 = getCpuTimeInState();
                    reply.writeNoException();
                    reply.writeString(_result27);
                    return true;
                case TRANSACTION_triggerFrameStat /* 1013 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    String _arg111 = data.readString();
                    String _result28 = triggerFrameStat(_arg034, _arg111);
                    reply.writeNoException();
                    reply.writeString(_result28);
                    return true;
                case 1014:
                    data.enforceInterface(DESCRIPTOR);
                    String _result29 = getChipName();
                    reply.writeNoException();
                    reply.writeString(_result29);
                    return true;
                case 1015:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result30 = getCpuClusterInfo();
                    reply.writeNoException();
                    reply.writeIntArray(_result30);
                    return true;
                case 1016:
                    data.enforceInterface(DESCRIPTOR);
                    int _result31 = getBatteryFCC();
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IOIfaceInternalService {
            public static IOIfaceInternalService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void currentNetwork(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().currentNetwork(status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void bindGameTask(int type, int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().bindGameTask(type, tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void enableHQV(int enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableHQV(enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String registerHQV(String packageName, int type, String config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(type);
                    _data.writeString(config);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerHQV(packageName, type, config);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setHalfHQV(int half) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(half);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHalfHQV(half);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int registerClientThroughCosa(IOIfaceCallback oifaceCallback, String json, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(oifaceCallback != null ? oifaceCallback.asBinder() : null);
                    _data.writeString(json);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerClientThroughCosa(oifaceCallback, json, uid, pid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String setGeneralSignalCosa(String json, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(json);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setGeneralSignalCosa, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setGeneralSignalCosa(json, uid, pid);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void registerNetworkListener(int thresholdMs, int minReportMs, IOIfaceCallback oifaceCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(thresholdMs);
                    _data.writeInt(minReportMs);
                    _data.writeStrongBinder(oifaceCallback != null ? oifaceCallback.asBinder() : null);
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerNetworkListener(thresholdMs, minReportMs, oifaceCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void registerGameRoleListener(int type, String config, IOIfaceCallback oifaceCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(config);
                    _data.writeStrongBinder(oifaceCallback != null ? oifaceCallback.asBinder() : null);
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerGameRoleListener(type, config, oifaceCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void unRegisterGameRoleListener(IOIfaceCallback oifaceCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(oifaceCallback != null ? oifaceCallback.asBinder() : null);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unRegisterGameRoleListener(oifaceCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String getSupportGameStartPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSupportGameStartPackage();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String getAllLoadInfo(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(212, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllLoadInfo(packageName);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String setGCPEffectMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setGCPEffectMode(mode);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setPerfMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(502, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPerfMode(mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void notifyScreenEvent(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(503, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyScreenEvent(mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String getDeviceID() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(504, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceID();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void currentPkgStatus(int status, String packageName, String newPIPPackage, String oldPIPPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(packageName);
                    _data.writeString(newPIPPackage);
                    _data.writeString(oldPIPPackage);
                    boolean _status = this.mRemote.transact(803, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().currentPkgStatus(status, packageName, newPIPPackage, oldPIPPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int getFPS(String packageName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getFPS, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFPS(packageName, type);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String generalOifaceSignal(String signal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(signal);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_generalOifaceSignal, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().generalOifaceSignal(signal);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void oifaceDecision(String decision) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(decision);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_oifaceDecision, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().oifaceDecision(decision);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void oifaceControl(String control) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(control);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_oifaceControl, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().oifaceControl(control);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int getCpuClusterNum() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCpuClusterNum, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuClusterNum();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public long[] getCpuAvailableFreqTable(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCpuAvailableFreqTable, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuAvailableFreqTable(type);
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public long[] getCpuLimitedFreqs(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCpuLimitedFreqs, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuLimitedFreqs(type);
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public long[] getCpuCurrentFreq(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCpuCurrentFreq, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuCurrentFreq(type);
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public float[] getCpuLoads(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCpuLoads, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuLoads(type);
                    }
                    _reply.readException();
                    float[] _result = _reply.createFloatArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public long[] getGpuAvailableFreqTable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getGpuAvailableFreqTable, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGpuAvailableFreqTable();
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public long[] getGpuLimitedFreqs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getGpuLimitedFreqs, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGpuLimitedFreqs();
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public long getGpuCurrentFreq() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getGpuCurrentFreq, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGpuCurrentFreq();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public float getGpuLoad() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getGpuLoad, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGpuLoad();
                    }
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public float[] getThermalTemps(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getThermalTemps, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getThermalTemps(type);
                    }
                    _reply.readException();
                    float[] _result = _reply.createFloatArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void enableHapticScreenCaptureService(int enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_enableHapticScreenCaptureService, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableHapticScreenCaptureService(enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setTouchSensibility(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setTouchSensibility, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTouchSensibility(level);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setTouchResponsiveness(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setTouchResponsiveness, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTouchResponsiveness(level);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setGyroscopeLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setGyroscopeLevel, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGyroscopeLevel(level);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setTouchProtection(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setTouchProtection, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTouchProtection(enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void registerOifaceCallback(IOIfaceCallback oifaceCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(oifaceCallback != null ? oifaceCallback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1001, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerOifaceCallback(oifaceCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int getBatteryRemain() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1002, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBatteryRemain();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public float getBatteryCurrentNow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1003, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBatteryCurrentNow();
                    }
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int getSuperVOOCStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1004, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSuperVOOCStatus();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public float[] getGPASystemInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1005, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGPASystemInfo();
                    }
                    _reply.readException();
                    float[] _result = _reply.createFloatArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setCoolExFilterType(int type, String config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(config);
                    boolean _status = this.mRemote.transact(1006, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCoolExFilterType(type, config);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setGameModeStatus(int status, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(1007, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGameModeStatus(status, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int getGameModeStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1008, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGameModeStatus();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String getCurrentGamePackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1009, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentGamePackage();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public void setInstalledGameList(String[] games) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(games);
                    boolean _status = this.mRemote.transact(1010, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setInstalledGameList(games);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String[] getInstalledGameList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1011, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstalledGameList();
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String getCpuTimeInState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCpuTimeInState, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuTimeInState();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String triggerFrameStat(String status, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(status);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_triggerFrameStat, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().triggerFrameStat(status, packageName);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public String getChipName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1014, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getChipName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int[] getCpuClusterInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1015, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCpuClusterInfo();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceInternalService
            public int getBatteryFCC() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1016, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBatteryFCC();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IOIfaceInternalService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IOIfaceInternalService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
