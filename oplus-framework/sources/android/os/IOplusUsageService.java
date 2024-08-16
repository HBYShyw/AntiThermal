package android.os;

import java.util.List;

/* loaded from: classes.dex */
public interface IOplusUsageService extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusUsageService";

    boolean accumulateDialOutDuration(int i) throws RemoteException;

    boolean accumulateHistoryCountOfReceivedMsg(int i) throws RemoteException;

    boolean accumulateHistoryCountOfSendedMsg(int i) throws RemoteException;

    boolean accumulateInComingCallDuration(int i) throws RemoteException;

    boolean deleteOplusFile(String str) throws RemoteException;

    byte[] engineerReadDevBlock(String str, int i, int i2) throws RemoteException;

    int engineerWriteDevBlock(String str, byte[] bArr, int i) throws RemoteException;

    int getApkDeleteEventRecordCount() throws RemoteException;

    List<String> getApkDeleteEventRecords(int i, int i2) throws RemoteException;

    int getApkInstallEventRecordCount() throws RemoteException;

    List<String> getApkInstallEventRecords(int i, int i2) throws RemoteException;

    List<String> getAppUsageCountHistoryRecords(int i, int i2) throws RemoteException;

    int getAppUsageHistoryRecordCount() throws RemoteException;

    List<String> getAppUsageHistoryRecords(int i, int i2) throws RemoteException;

    Bundle getComponentSerialNumber() throws RemoteException;

    List<String> getDialCountHistoryRecords(int i, int i2) throws RemoteException;

    int getDialOutDuration() throws RemoteException;

    String getDownloadStatusString(int i) throws RemoteException;

    int getFileSize(String str) throws RemoteException;

    List<String> getHistoryBootTime() throws RemoteException;

    int getHistoryCountOfReceivedMsg() throws RemoteException;

    int getHistoryCountOfSendedMsg() throws RemoteException;

    List<String> getHistoryImeiNO() throws RemoteException;

    List<String> getHistoryPcbaNO() throws RemoteException;

    int getHistoryRecordsCountOfPhoneCalls() throws RemoteException;

    int getInComingCallDuration() throws RemoteException;

    int getMaxChargeCurrent() throws RemoteException;

    int getMaxChargeTemperature() throws RemoteException;

    String getMcsConnectID() throws RemoteException;

    int getMinChargeTemperature() throws RemoteException;

    List<String> getOriginalSimcardData() throws RemoteException;

    List<String> getPhoneCallHistoryRecords(int i, int i2) throws RemoteException;

    int getProductLineLastTestFlag() throws RemoteException;

    String loadSecrecyConfig() throws RemoteException;

    void notifySnQueryCompleted(Bundle bundle) throws RemoteException;

    boolean readEntireOplusDir(String str, String str2, boolean z) throws RemoteException;

    boolean readEntireOplusFile(String str, String str2, boolean z) throws RemoteException;

    byte[] readOplusFile(String str, int i, int i2) throws RemoteException;

    boolean recordApkDeleteEvent(String str, String str2, String str3) throws RemoteException;

    boolean recordApkInstallEvent(String str, String str2, String str3) throws RemoteException;

    boolean recordMcsConnectID(String str) throws RemoteException;

    boolean saveEntireOplusDir(String str, String str2, boolean z) throws RemoteException;

    int saveEntireOplusFile(String str, String str2, boolean z) throws RemoteException;

    int saveOplusFile(int i, String str, int i2, boolean z, int i3, byte[] bArr) throws RemoteException;

    int saveSecrecyConfig(String str) throws RemoteException;

    boolean setProductLineLastTestFlag(int i) throws RemoteException;

    void shutDown() throws RemoteException;

    void testSaveSomeData(int i, String str) throws RemoteException;

    void triggerComponentInfoQuery(String str) throws RemoteException;

    boolean updateMaxChargeCurrent(int i) throws RemoteException;

    boolean updateMaxChargeTemperature(int i) throws RemoteException;

    boolean updateMinChargeTemperature(int i) throws RemoteException;

    boolean writeAppUsageHistoryRecord(String str, String str2) throws RemoteException;

    boolean writePhoneCallHistoryRecord(String str, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusUsageService {
        @Override // android.os.IOplusUsageService
        public void testSaveSomeData(int dataType, String dataContent) throws RemoteException {
        }

        @Override // android.os.IOplusUsageService
        public void triggerComponentInfoQuery(String token) throws RemoteException {
        }

        @Override // android.os.IOplusUsageService
        public void notifySnQueryCompleted(Bundle snQueryResult) throws RemoteException {
        }

        @Override // android.os.IOplusUsageService
        public Bundle getComponentSerialNumber() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getHistoryBootTime() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getOriginalSimcardData() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getHistoryImeiNO() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getHistoryPcbaNO() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public int getAppUsageHistoryRecordCount() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getAppUsageHistoryRecords(int startIndex, int endIndex) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getAppUsageCountHistoryRecords(int startIndex, int endIndex) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getDialCountHistoryRecords(int startIndex, int endIndex) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public boolean writeAppUsageHistoryRecord(String appName, String dateTime) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public int getHistoryCountOfSendedMsg() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public int getHistoryCountOfReceivedMsg() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public boolean accumulateHistoryCountOfSendedMsg(int newCountIncrease) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean accumulateHistoryCountOfReceivedMsg(int newCountIncrease) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public int getDialOutDuration() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public int getInComingCallDuration() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public boolean accumulateDialOutDuration(int durationInMinute) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean accumulateInComingCallDuration(int durationInMinute) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public int getHistoryRecordsCountOfPhoneCalls() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getPhoneCallHistoryRecords(int startIndex, int endIndex) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public boolean writePhoneCallHistoryRecord(String phoneNoStr, String dateTime) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public void shutDown() throws RemoteException {
        }

        @Override // android.os.IOplusUsageService
        public int getMinChargeTemperature() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public int getMaxChargeTemperature() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public int getMaxChargeCurrent() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public boolean updateMinChargeTemperature(int temp) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean updateMaxChargeTemperature(int temp) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean updateMaxChargeCurrent(int current) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public byte[] engineerReadDevBlock(String partion, int offset, int count) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public int engineerWriteDevBlock(String partion, byte[] content, int offset) throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public String getDownloadStatusString(int part) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public int saveSecrecyConfig(String content) throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public String loadSecrecyConfig() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public boolean setProductLineLastTestFlag(int flag) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public int getProductLineLastTestFlag() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public boolean recordApkDeleteEvent(String deleteAppPkgName, String callerAppPkgName, String dateTime) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public int getApkDeleteEventRecordCount() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getApkDeleteEventRecords(int startIndex, int endIndex) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public boolean recordApkInstallEvent(String installAppPkgName, String callerAppPkgName, String dateTime) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public int getApkInstallEventRecordCount() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public List<String> getApkInstallEventRecords(int startIndex, int endIndex) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public boolean recordMcsConnectID(String connectID) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public String getMcsConnectID() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public int getFileSize(String path) throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public byte[] readOplusFile(String path, int startPosition, int length) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusUsageService
        public int saveOplusFile(int fileMax, String path, int offset, boolean append, int length, byte[] data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public int saveEntireOplusFile(String sourcePath, String targetPath, boolean deleteSource) throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusUsageService
        public boolean readEntireOplusFile(String sourcePath, String targetPath, boolean deleteSource) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean deleteOplusFile(String filePath) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean saveEntireOplusDir(String sourceDir, String targetDir, boolean deleteSource) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusUsageService
        public boolean readEntireOplusDir(String sourceDir, String targetDir, boolean deleteSource) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusUsageService {
        static final int TRANSACTION_accumulateDialOutDuration = 20;
        static final int TRANSACTION_accumulateHistoryCountOfReceivedMsg = 17;
        static final int TRANSACTION_accumulateHistoryCountOfSendedMsg = 16;
        static final int TRANSACTION_accumulateInComingCallDuration = 21;
        static final int TRANSACTION_deleteOplusFile = 52;
        static final int TRANSACTION_engineerReadDevBlock = 32;
        static final int TRANSACTION_engineerWriteDevBlock = 33;
        static final int TRANSACTION_getApkDeleteEventRecordCount = 40;
        static final int TRANSACTION_getApkDeleteEventRecords = 41;
        static final int TRANSACTION_getApkInstallEventRecordCount = 43;
        static final int TRANSACTION_getApkInstallEventRecords = 44;
        static final int TRANSACTION_getAppUsageCountHistoryRecords = 11;
        static final int TRANSACTION_getAppUsageHistoryRecordCount = 9;
        static final int TRANSACTION_getAppUsageHistoryRecords = 10;
        static final int TRANSACTION_getComponentSerialNumber = 4;
        static final int TRANSACTION_getDialCountHistoryRecords = 12;
        static final int TRANSACTION_getDialOutDuration = 18;
        static final int TRANSACTION_getDownloadStatusString = 34;
        static final int TRANSACTION_getFileSize = 47;
        static final int TRANSACTION_getHistoryBootTime = 5;
        static final int TRANSACTION_getHistoryCountOfReceivedMsg = 15;
        static final int TRANSACTION_getHistoryCountOfSendedMsg = 14;
        static final int TRANSACTION_getHistoryImeiNO = 7;
        static final int TRANSACTION_getHistoryPcbaNO = 8;
        static final int TRANSACTION_getHistoryRecordsCountOfPhoneCalls = 22;
        static final int TRANSACTION_getInComingCallDuration = 19;
        static final int TRANSACTION_getMaxChargeCurrent = 28;
        static final int TRANSACTION_getMaxChargeTemperature = 27;
        static final int TRANSACTION_getMcsConnectID = 46;
        static final int TRANSACTION_getMinChargeTemperature = 26;
        static final int TRANSACTION_getOriginalSimcardData = 6;
        static final int TRANSACTION_getPhoneCallHistoryRecords = 23;
        static final int TRANSACTION_getProductLineLastTestFlag = 38;
        static final int TRANSACTION_loadSecrecyConfig = 36;
        static final int TRANSACTION_notifySnQueryCompleted = 3;
        static final int TRANSACTION_readEntireOplusDir = 54;
        static final int TRANSACTION_readEntireOplusFile = 51;
        static final int TRANSACTION_readOplusFile = 48;
        static final int TRANSACTION_recordApkDeleteEvent = 39;
        static final int TRANSACTION_recordApkInstallEvent = 42;
        static final int TRANSACTION_recordMcsConnectID = 45;
        static final int TRANSACTION_saveEntireOplusDir = 53;
        static final int TRANSACTION_saveEntireOplusFile = 50;
        static final int TRANSACTION_saveOplusFile = 49;
        static final int TRANSACTION_saveSecrecyConfig = 35;
        static final int TRANSACTION_setProductLineLastTestFlag = 37;
        static final int TRANSACTION_shutDown = 25;
        static final int TRANSACTION_testSaveSomeData = 1;
        static final int TRANSACTION_triggerComponentInfoQuery = 2;
        static final int TRANSACTION_updateMaxChargeCurrent = 31;
        static final int TRANSACTION_updateMaxChargeTemperature = 30;
        static final int TRANSACTION_updateMinChargeTemperature = 29;
        static final int TRANSACTION_writeAppUsageHistoryRecord = 13;
        static final int TRANSACTION_writePhoneCallHistoryRecord = 24;

        public Stub() {
            attachInterface(this, IOplusUsageService.DESCRIPTOR);
        }

        public static IOplusUsageService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusUsageService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusUsageService)) {
                return (IOplusUsageService) iin;
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
                    return "testSaveSomeData";
                case 2:
                    return "triggerComponentInfoQuery";
                case 3:
                    return "notifySnQueryCompleted";
                case 4:
                    return "getComponentSerialNumber";
                case 5:
                    return "getHistoryBootTime";
                case 6:
                    return "getOriginalSimcardData";
                case 7:
                    return "getHistoryImeiNO";
                case 8:
                    return "getHistoryPcbaNO";
                case 9:
                    return "getAppUsageHistoryRecordCount";
                case 10:
                    return "getAppUsageHistoryRecords";
                case 11:
                    return "getAppUsageCountHistoryRecords";
                case 12:
                    return "getDialCountHistoryRecords";
                case 13:
                    return "writeAppUsageHistoryRecord";
                case 14:
                    return "getHistoryCountOfSendedMsg";
                case 15:
                    return "getHistoryCountOfReceivedMsg";
                case 16:
                    return "accumulateHistoryCountOfSendedMsg";
                case 17:
                    return "accumulateHistoryCountOfReceivedMsg";
                case 18:
                    return "getDialOutDuration";
                case 19:
                    return "getInComingCallDuration";
                case 20:
                    return "accumulateDialOutDuration";
                case 21:
                    return "accumulateInComingCallDuration";
                case 22:
                    return "getHistoryRecordsCountOfPhoneCalls";
                case 23:
                    return "getPhoneCallHistoryRecords";
                case 24:
                    return "writePhoneCallHistoryRecord";
                case 25:
                    return "shutDown";
                case 26:
                    return "getMinChargeTemperature";
                case 27:
                    return "getMaxChargeTemperature";
                case 28:
                    return "getMaxChargeCurrent";
                case 29:
                    return "updateMinChargeTemperature";
                case 30:
                    return "updateMaxChargeTemperature";
                case 31:
                    return "updateMaxChargeCurrent";
                case 32:
                    return "engineerReadDevBlock";
                case 33:
                    return "engineerWriteDevBlock";
                case 34:
                    return "getDownloadStatusString";
                case 35:
                    return "saveSecrecyConfig";
                case 36:
                    return "loadSecrecyConfig";
                case 37:
                    return "setProductLineLastTestFlag";
                case 38:
                    return "getProductLineLastTestFlag";
                case 39:
                    return "recordApkDeleteEvent";
                case 40:
                    return "getApkDeleteEventRecordCount";
                case 41:
                    return "getApkDeleteEventRecords";
                case 42:
                    return "recordApkInstallEvent";
                case 43:
                    return "getApkInstallEventRecordCount";
                case 44:
                    return "getApkInstallEventRecords";
                case 45:
                    return "recordMcsConnectID";
                case 46:
                    return "getMcsConnectID";
                case 47:
                    return "getFileSize";
                case 48:
                    return "readOplusFile";
                case 49:
                    return "saveOplusFile";
                case 50:
                    return "saveEntireOplusFile";
                case 51:
                    return "readEntireOplusFile";
                case 52:
                    return "deleteOplusFile";
                case 53:
                    return "saveEntireOplusDir";
                case 54:
                    return "readEntireOplusDir";
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
                data.enforceInterface(IOplusUsageService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusUsageService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            testSaveSomeData(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            triggerComponentInfoQuery(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            Bundle _arg03 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            notifySnQueryCompleted(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            Bundle _result = getComponentSerialNumber();
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 5:
                            List<String> _result2 = getHistoryBootTime();
                            reply.writeNoException();
                            reply.writeStringList(_result2);
                            return true;
                        case 6:
                            List<String> _result3 = getOriginalSimcardData();
                            reply.writeNoException();
                            reply.writeStringList(_result3);
                            return true;
                        case 7:
                            List<String> _result4 = getHistoryImeiNO();
                            reply.writeNoException();
                            reply.writeStringList(_result4);
                            return true;
                        case 8:
                            List<String> _result5 = getHistoryPcbaNO();
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 9:
                            int _result6 = getAppUsageHistoryRecordCount();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 10:
                            int _arg04 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result7 = getAppUsageHistoryRecords(_arg04, _arg12);
                            reply.writeNoException();
                            reply.writeStringList(_result7);
                            return true;
                        case 11:
                            int _arg05 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result8 = getAppUsageCountHistoryRecords(_arg05, _arg13);
                            reply.writeNoException();
                            reply.writeStringList(_result8);
                            return true;
                        case 12:
                            int _arg06 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result9 = getDialCountHistoryRecords(_arg06, _arg14);
                            reply.writeNoException();
                            reply.writeStringList(_result9);
                            return true;
                        case 13:
                            String _arg07 = data.readString();
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result10 = writeAppUsageHistoryRecord(_arg07, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 14:
                            int _result11 = getHistoryCountOfSendedMsg();
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 15:
                            int _result12 = getHistoryCountOfReceivedMsg();
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 16:
                            int _arg08 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result13 = accumulateHistoryCountOfSendedMsg(_arg08);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 17:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result14 = accumulateHistoryCountOfReceivedMsg(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 18:
                            int _result15 = getDialOutDuration();
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 19:
                            int _result16 = getInComingCallDuration();
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 20:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result17 = accumulateDialOutDuration(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 21:
                            int _arg011 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result18 = accumulateInComingCallDuration(_arg011);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 22:
                            int _result19 = getHistoryRecordsCountOfPhoneCalls();
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 23:
                            int _arg012 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result20 = getPhoneCallHistoryRecords(_arg012, _arg16);
                            reply.writeNoException();
                            reply.writeStringList(_result20);
                            return true;
                        case 24:
                            String _arg013 = data.readString();
                            String _arg17 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result21 = writePhoneCallHistoryRecord(_arg013, _arg17);
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 25:
                            shutDown();
                            reply.writeNoException();
                            return true;
                        case 26:
                            int _result22 = getMinChargeTemperature();
                            reply.writeNoException();
                            reply.writeInt(_result22);
                            return true;
                        case 27:
                            int _result23 = getMaxChargeTemperature();
                            reply.writeNoException();
                            reply.writeInt(_result23);
                            return true;
                        case 28:
                            int _result24 = getMaxChargeCurrent();
                            reply.writeNoException();
                            reply.writeInt(_result24);
                            return true;
                        case 29:
                            int _arg014 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result25 = updateMinChargeTemperature(_arg014);
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 30:
                            int _arg015 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result26 = updateMaxChargeTemperature(_arg015);
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 31:
                            int _arg016 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result27 = updateMaxChargeCurrent(_arg016);
                            reply.writeNoException();
                            reply.writeBoolean(_result27);
                            return true;
                        case 32:
                            String _arg017 = data.readString();
                            int _arg18 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            byte[] _result28 = engineerReadDevBlock(_arg017, _arg18, _arg2);
                            reply.writeNoException();
                            reply.writeByteArray(_result28);
                            return true;
                        case 33:
                            String _arg018 = data.readString();
                            byte[] _arg19 = data.createByteArray();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result29 = engineerWriteDevBlock(_arg018, _arg19, _arg22);
                            reply.writeNoException();
                            reply.writeInt(_result29);
                            return true;
                        case 34:
                            int _arg019 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result30 = getDownloadStatusString(_arg019);
                            reply.writeNoException();
                            reply.writeString(_result30);
                            return true;
                        case 35:
                            String _arg020 = data.readString();
                            data.enforceNoDataAvail();
                            int _result31 = saveSecrecyConfig(_arg020);
                            reply.writeNoException();
                            reply.writeInt(_result31);
                            return true;
                        case 36:
                            String _result32 = loadSecrecyConfig();
                            reply.writeNoException();
                            reply.writeString(_result32);
                            return true;
                        case 37:
                            int _arg021 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result33 = setProductLineLastTestFlag(_arg021);
                            reply.writeNoException();
                            reply.writeBoolean(_result33);
                            return true;
                        case 38:
                            int _result34 = getProductLineLastTestFlag();
                            reply.writeNoException();
                            reply.writeInt(_result34);
                            return true;
                        case 39:
                            String _arg022 = data.readString();
                            String _arg110 = data.readString();
                            String _arg23 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result35 = recordApkDeleteEvent(_arg022, _arg110, _arg23);
                            reply.writeNoException();
                            reply.writeBoolean(_result35);
                            return true;
                        case 40:
                            int _result36 = getApkDeleteEventRecordCount();
                            reply.writeNoException();
                            reply.writeInt(_result36);
                            return true;
                        case 41:
                            int _arg023 = data.readInt();
                            int _arg111 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result37 = getApkDeleteEventRecords(_arg023, _arg111);
                            reply.writeNoException();
                            reply.writeStringList(_result37);
                            return true;
                        case 42:
                            String _arg024 = data.readString();
                            String _arg112 = data.readString();
                            String _arg24 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result38 = recordApkInstallEvent(_arg024, _arg112, _arg24);
                            reply.writeNoException();
                            reply.writeBoolean(_result38);
                            return true;
                        case 43:
                            int _result39 = getApkInstallEventRecordCount();
                            reply.writeNoException();
                            reply.writeInt(_result39);
                            return true;
                        case 44:
                            int _arg025 = data.readInt();
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result40 = getApkInstallEventRecords(_arg025, _arg113);
                            reply.writeNoException();
                            reply.writeStringList(_result40);
                            return true;
                        case 45:
                            String _arg026 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result41 = recordMcsConnectID(_arg026);
                            reply.writeNoException();
                            reply.writeBoolean(_result41);
                            return true;
                        case 46:
                            String _result42 = getMcsConnectID();
                            reply.writeNoException();
                            reply.writeString(_result42);
                            return true;
                        case 47:
                            String _arg027 = data.readString();
                            data.enforceNoDataAvail();
                            int _result43 = getFileSize(_arg027);
                            reply.writeNoException();
                            reply.writeInt(_result43);
                            return true;
                        case 48:
                            String _arg028 = data.readString();
                            int _arg114 = data.readInt();
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            byte[] _result44 = readOplusFile(_arg028, _arg114, _arg25);
                            reply.writeNoException();
                            reply.writeByteArray(_result44);
                            return true;
                        case 49:
                            int _arg029 = data.readInt();
                            String _arg115 = data.readString();
                            int _arg26 = data.readInt();
                            boolean _arg3 = data.readBoolean();
                            int _arg4 = data.readInt();
                            byte[] _arg5 = data.createByteArray();
                            data.enforceNoDataAvail();
                            int _result45 = saveOplusFile(_arg029, _arg115, _arg26, _arg3, _arg4, _arg5);
                            reply.writeNoException();
                            reply.writeInt(_result45);
                            return true;
                        case 50:
                            String _arg030 = data.readString();
                            String _arg116 = data.readString();
                            boolean _arg27 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result46 = saveEntireOplusFile(_arg030, _arg116, _arg27);
                            reply.writeNoException();
                            reply.writeInt(_result46);
                            return true;
                        case 51:
                            String _arg031 = data.readString();
                            String _arg117 = data.readString();
                            boolean _arg28 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result47 = readEntireOplusFile(_arg031, _arg117, _arg28);
                            reply.writeNoException();
                            reply.writeBoolean(_result47);
                            return true;
                        case 52:
                            String _arg032 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result48 = deleteOplusFile(_arg032);
                            reply.writeNoException();
                            reply.writeBoolean(_result48);
                            return true;
                        case 53:
                            String _arg033 = data.readString();
                            String _arg118 = data.readString();
                            boolean _arg29 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result49 = saveEntireOplusDir(_arg033, _arg118, _arg29);
                            reply.writeNoException();
                            reply.writeBoolean(_result49);
                            return true;
                        case 54:
                            String _arg034 = data.readString();
                            String _arg119 = data.readString();
                            boolean _arg210 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result50 = readEntireOplusDir(_arg034, _arg119, _arg210);
                            reply.writeNoException();
                            reply.writeBoolean(_result50);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusUsageService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusUsageService.DESCRIPTOR;
            }

            @Override // android.os.IOplusUsageService
            public void testSaveSomeData(int dataType, String dataContent) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(dataType);
                    _data.writeString(dataContent);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public void triggerComponentInfoQuery(String token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(token);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public void notifySnQueryCompleted(Bundle snQueryResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeTypedObject(snQueryResult, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public Bundle getComponentSerialNumber() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getHistoryBootTime() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getOriginalSimcardData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getHistoryImeiNO() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getHistoryPcbaNO() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getAppUsageHistoryRecordCount() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getAppUsageHistoryRecords(int startIndex, int endIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getAppUsageCountHistoryRecords(int startIndex, int endIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getDialCountHistoryRecords(int startIndex, int endIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean writeAppUsageHistoryRecord(String appName, String dateTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(appName);
                    _data.writeString(dateTime);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getHistoryCountOfSendedMsg() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getHistoryCountOfReceivedMsg() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean accumulateHistoryCountOfSendedMsg(int newCountIncrease) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(newCountIncrease);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean accumulateHistoryCountOfReceivedMsg(int newCountIncrease) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(newCountIncrease);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getDialOutDuration() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getInComingCallDuration() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean accumulateDialOutDuration(int durationInMinute) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(durationInMinute);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean accumulateInComingCallDuration(int durationInMinute) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(durationInMinute);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getHistoryRecordsCountOfPhoneCalls() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getPhoneCallHistoryRecords(int startIndex, int endIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean writePhoneCallHistoryRecord(String phoneNoStr, String dateTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(phoneNoStr);
                    _data.writeString(dateTime);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public void shutDown() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getMinChargeTemperature() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getMaxChargeTemperature() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getMaxChargeCurrent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean updateMinChargeTemperature(int temp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(temp);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean updateMaxChargeTemperature(int temp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(temp);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean updateMaxChargeCurrent(int current) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(current);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public byte[] engineerReadDevBlock(String partion, int offset, int count) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(partion);
                    _data.writeInt(offset);
                    _data.writeInt(count);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int engineerWriteDevBlock(String partion, byte[] content, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(partion);
                    _data.writeByteArray(content);
                    _data.writeInt(offset);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public String getDownloadStatusString(int part) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(part);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int saveSecrecyConfig(String content) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(content);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public String loadSecrecyConfig() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean setProductLineLastTestFlag(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(flag);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getProductLineLastTestFlag() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean recordApkDeleteEvent(String deleteAppPkgName, String callerAppPkgName, String dateTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(deleteAppPkgName);
                    _data.writeString(callerAppPkgName);
                    _data.writeString(dateTime);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getApkDeleteEventRecordCount() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getApkDeleteEventRecords(int startIndex, int endIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean recordApkInstallEvent(String installAppPkgName, String callerAppPkgName, String dateTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(installAppPkgName);
                    _data.writeString(callerAppPkgName);
                    _data.writeString(dateTime);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getApkInstallEventRecordCount() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public List<String> getApkInstallEventRecords(int startIndex, int endIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean recordMcsConnectID(String connectID) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(connectID);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public String getMcsConnectID() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int getFileSize(String path) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public byte[] readOplusFile(String path, int startPosition, int length) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(startPosition);
                    _data.writeInt(length);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int saveOplusFile(int fileMax, String path, int offset, boolean append, int length, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeInt(fileMax);
                    _data.writeString(path);
                    _data.writeInt(offset);
                    _data.writeBoolean(append);
                    _data.writeInt(length);
                    _data.writeByteArray(data);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public int saveEntireOplusFile(String sourcePath, String targetPath, boolean deleteSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeString(targetPath);
                    _data.writeBoolean(deleteSource);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean readEntireOplusFile(String sourcePath, String targetPath, boolean deleteSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeString(targetPath);
                    _data.writeBoolean(deleteSource);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean deleteOplusFile(String filePath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(filePath);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean saveEntireOplusDir(String sourceDir, String targetDir, boolean deleteSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(sourceDir);
                    _data.writeString(targetDir);
                    _data.writeBoolean(deleteSource);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusUsageService
            public boolean readEntireOplusDir(String sourceDir, String targetDir, boolean deleteSource) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUsageService.DESCRIPTOR);
                    _data.writeString(sourceDir);
                    _data.writeString(targetDir);
                    _data.writeBoolean(deleteSource);
                    this.mRemote.transact(54, _data, _reply, 0);
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
            return 53;
        }
    }
}
