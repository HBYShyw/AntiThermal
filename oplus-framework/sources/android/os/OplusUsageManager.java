package android.os;

import android.os.IOplusUsageService;
import android.util.Log;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusUsageManager {
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_E = true;
    private static final boolean DEBUG_W = true;
    public static final String SERVICE_NAME = "usage";
    public static final String TAG = "OplusUsageManager";
    private static OplusUsageManager mInstance = null;
    private IOplusUsageService mOplusUsageService;

    private OplusUsageManager() {
        this.mOplusUsageService = null;
        IBinder serviceBinder = ServiceManager.getService(SERVICE_NAME);
        this.mOplusUsageService = IOplusUsageService.Stub.asInterface(serviceBinder);
    }

    public static OplusUsageManager getOplusUsageManager() {
        if (mInstance == null) {
            mInstance = new OplusUsageManager();
        }
        return mInstance;
    }

    public void testSaveSomeData(int dataType, String dataContent) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                iOplusUsageService.testSaveSomeData(dataType, dataContent);
                return;
            } catch (RemoteException exce) {
                Log.e(TAG, "testSaveSomeData failed!", exce);
                return;
            }
        }
        Log.w(TAG, "testSaveSomeData:service not publish!");
    }

    public void triggerComponentInfoQuery(String token) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                iOplusUsageService.triggerComponentInfoQuery(token);
                return;
            } catch (RemoteException exce) {
                Log.e(TAG, "triggerComponentInfoQuery failed!", exce);
                return;
            }
        }
        Log.w(TAG, "triggerComponentInfoQuery: service not publish!");
    }

    public void notifySnQueryCompleted(Bundle snQueryResult) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                iOplusUsageService.notifySnQueryCompleted(snQueryResult);
                return;
            } catch (RemoteException exce) {
                Log.e(TAG, "notifySnQueryCompleted failed!", exce);
                return;
            }
        }
        Log.w(TAG, "notifySnQueryCompleted: service not publish!");
    }

    public Bundle getComponentSerialNumber() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getComponentSerialNumber();
            } catch (RemoteException exce) {
                Log.e(TAG, "getComponentSerialNumber failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getComponentSerialNumber: service not publish!");
        return null;
    }

    public List<String> getHistoryBootTime() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getHistoryBootTime();
            } catch (RemoteException exce) {
                Log.e(TAG, "getHistoryBootTime failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getHistoryBootTime:service not publish!");
        return null;
    }

    public List<String> getHistoryImeiNO() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getHistoryImeiNO();
            } catch (RemoteException exce) {
                Log.e(TAG, "getHistoryImeiNO failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getHistoryImeiNO:service not publish!");
        return null;
    }

    public List<String> getOriginalSimcardData() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getOriginalSimcardData();
            } catch (RemoteException exce) {
                Log.e(TAG, "getOriginalImeiMeidNO failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getOriginalImeiMeidNO:service not publish!");
        return null;
    }

    public List<String> getHistoryPcbaNO() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getHistoryPcbaNO();
            } catch (RemoteException exce) {
                Log.e(TAG, "getHistoryPcbaNO failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getHistoryPcbaNO:service not publish!");
        return null;
    }

    public int getAppUsageHistoryRecordCount() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getAppUsageHistoryRecordCount();
            } catch (RemoteException exce) {
                Log.e(TAG, "getAppUsageHistoryRecordCount failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getAppUsageHistoryRecordCount:service not publish!");
        return 0;
    }

    public List<String> getAppUsageHistoryRecords(int startIndex, int endIndex) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getAppUsageHistoryRecords(startIndex, endIndex);
            } catch (RemoteException exce) {
                Log.e(TAG, "getAppUsageHistoryRecords failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getAppUsageHistoryRecords:service not publish!");
        return null;
    }

    public List<String> getAppUsageCountHistoryRecords(int startIndex, int endIndex) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getAppUsageCountHistoryRecords(startIndex, endIndex);
            } catch (RemoteException exce) {
                Log.e(TAG, "getAppUsageCountHistoryRecords failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getAppUsageCountHistoryRecords:service not publish!");
        return null;
    }

    public List<String> getDialCountHistoryRecords(int startIndex, int endIndex) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getDialCountHistoryRecords(startIndex, endIndex);
            } catch (RemoteException exce) {
                Log.e(TAG, "getDialCountHistoryRecords failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getDialCountHistoryRecords:service not publish!");
        return null;
    }

    public boolean writeAppUsageHistoryRecord(String appName, String dateTime) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.writeAppUsageHistoryRecord(appName, dateTime);
            } catch (RemoteException exce) {
                Log.e(TAG, "getAppUsageHistoryRecords failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "getAppUsageHistoryRecords:service not publish!");
        return false;
    }

    public int getHistoryCountOfSendedMsg() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getHistoryCountOfSendedMsg();
            } catch (RemoteException exce) {
                Log.e(TAG, "getHistoryCountOfSendedMsg failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getHistoryCountOfSendedMsg:service not publish!");
        return 0;
    }

    public int getHistoryCountOfReceivedMsg() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getHistoryCountOfReceivedMsg();
            } catch (RemoteException exce) {
                Log.e(TAG, "getHistoryCountOfReceivedMsg failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getHistoryCountOfReceivedMsg:service not publish!");
        return 0;
    }

    public boolean accumulateHistoryCountOfSendedMsg(int newCountIncrease) {
        if (newCountIncrease <= 0) {
            Log.w(TAG, "accumulateHistoryCountOfSendedMsg:illegal param!");
            return false;
        }
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.accumulateHistoryCountOfSendedMsg(newCountIncrease);
            } catch (RemoteException exce) {
                Log.e(TAG, "accumulateHistoryCountOfSendedMsg failed!", exce);
            }
        } else {
            Log.w(TAG, "accumulateHistoryCountOfSendedMsg:service not publish!");
        }
        return false;
    }

    public boolean accumulateHistoryCountOfReceivedMsg(int newCountIncrease) {
        if (newCountIncrease <= 0) {
            Log.w(TAG, "accumulateHistoryCountOfReceivedMsg:illegal param!");
            return false;
        }
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.accumulateHistoryCountOfReceivedMsg(newCountIncrease);
            } catch (RemoteException exce) {
                Log.e(TAG, "accumulateHistoryCountOfReceivedMsg failed!", exce);
            }
        } else {
            Log.w(TAG, "accumulateHistoryCountOfReceivedMsg:service not publish!");
        }
        return false;
    }

    public int getDialOutDuration() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getDialOutDuration();
            } catch (RemoteException exce) {
                Log.e(TAG, "getDialOutDuration failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getDialOutDuration:service not publish!");
        return 0;
    }

    public int getInComingCallDuration() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getInComingCallDuration();
            } catch (RemoteException exce) {
                Log.e(TAG, "getInComingCallDuration failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getInComingCallDuration:service not publish!");
        return 0;
    }

    public boolean accumulateDialOutDuration(int durationInMinute) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.accumulateDialOutDuration(durationInMinute);
            } catch (RemoteException exce) {
                Log.e(TAG, "accumulateDialOutDuration failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "accumulateDialOutDuration:service not publish!");
        return false;
    }

    public boolean accumulateInComingCallDuration(int durationInMinute) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.accumulateInComingCallDuration(durationInMinute);
            } catch (RemoteException exce) {
                Log.e(TAG, "accumulateInComingCallDuration failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "accumulateInComingCallDuration:service not publish!");
        return false;
    }

    public int getHistoryRecordsCountOfPhoneCalls() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getHistoryRecordsCountOfPhoneCalls();
            } catch (RemoteException exce) {
                Log.e(TAG, "getHistoryRecordsCountOfPhoneCalls failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getHistoryRecordsCountOfPhoneCalls:service not publish!");
        return 0;
    }

    public List<String> getPhoneCallHistoryRecords(int startIndex, int endIndex) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getPhoneCallHistoryRecords(startIndex, endIndex);
            } catch (RemoteException exce) {
                Log.e(TAG, "getPhoneCallHistoryRecords failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getPhoneCallHistoryRecords:service not publish!");
        return null;
    }

    public boolean writePhoneCallHistoryRecord(String phoneNoStr, String dateTime) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.writePhoneCallHistoryRecord(phoneNoStr, dateTime);
            } catch (RemoteException exce) {
                Log.e(TAG, "writePhoneCallHistoryRecord failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "writePhoneCallHistoryRecord:service not publish!");
        return false;
    }

    public boolean updateMaxChargeCurrent(int current) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.updateMaxChargeCurrent(current);
            } catch (RemoteException exce) {
                Log.e(TAG, "updateMaxChargeCurrent failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "updateMaxChargeCurrent:service not publish!");
        return false;
    }

    public boolean updateMaxChargeTemperature(int temp) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.updateMaxChargeCurrent(temp);
            } catch (RemoteException exce) {
                Log.e(TAG, "updateMaxChargeTemperature failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "updateMaxChargeTemperature:service not publish!");
        return false;
    }

    public boolean updateMinChargeTemperature(int temp) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.updateMinChargeTemperature(temp);
            } catch (RemoteException exce) {
                Log.e(TAG, "updateMinChargeTemperature failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "updateMinChargeTemperature:service not publish!");
        return false;
    }

    public int getMaxChargeCurrent() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getMaxChargeCurrent();
            } catch (RemoteException exce) {
                Log.e(TAG, "getMaxChargeCurrent failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getMaxChargeCurrent:service not publish!");
        return 0;
    }

    public int getMaxChargeTemperature() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getMaxChargeTemperature();
            } catch (RemoteException exce) {
                Log.e(TAG, "getMaxChargeTemperature failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getMaxChargeTemperature:service not publish!");
        return 0;
    }

    public int getMinChargeTemperature() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getMinChargeTemperature();
            } catch (RemoteException exce) {
                Log.e(TAG, "getMinChargeTemperature failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getMinChargeTemperature:service not publish!");
        return 0;
    }

    public byte[] engineerReadDevBlock(String partion, int offset, int count) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.engineerReadDevBlock(partion, offset, count);
            } catch (RemoteException exce) {
                Log.e(TAG, "engineerReadDevBlock failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "engineerReadDevBlock:service not publish!");
        return null;
    }

    public int engineerWriteDevBlock(String partion, byte[] content, int offset) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.engineerWriteDevBlock(partion, content, offset);
            } catch (RemoteException exce) {
                Log.e(TAG, "engineerWriteDevBlock failed!", exce);
                return -1;
            }
        }
        Log.w(TAG, "engineerWriteDevBlock:service not publish!");
        return -1;
    }

    public String getDownloadStatusString(int part) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getDownloadStatusString(part);
            } catch (RemoteException exce) {
                Log.e(TAG, "getDownloadStatusString failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getDownloadStatusString:service not publish!");
        return null;
    }

    public String loadSecrecyConfig() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.loadSecrecyConfig();
            } catch (RemoteException exce) {
                Log.e(TAG, "loadSecrecyConfig failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "loadSecrecyConfig:service not publish!");
        return null;
    }

    public int saveSecrecyConfig(String content) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.saveSecrecyConfig(content);
            } catch (RemoteException exce) {
                Log.e(TAG, "saveSecrecyConfig failed!", exce);
                return -1;
            }
        }
        Log.w(TAG, "saveSecrecyConfig:service not publish!");
        return -1;
    }

    public boolean setProductLineLastTestFlag(int flag) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.setProductLineLastTestFlag(flag);
            } catch (RemoteException exce) {
                Log.e(TAG, "setProductLineLastTestFlag failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "setProductLineLastTestFlag:service not publish!");
        return false;
    }

    public int getProductLineLastTestFlag() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getProductLineLastTestFlag();
            } catch (RemoteException exce) {
                Log.e(TAG, "getProductLineLastTestFlag failed!", exce);
                return -1;
            }
        }
        Log.w(TAG, "getProductLineLastTestFlag:service not publish!");
        return -1;
    }

    public boolean recordApkDeleteEvent(String deleteAppPkgName, String callerAppPkgName, String dateTime) {
        if (deleteAppPkgName == null || deleteAppPkgName.isEmpty()) {
            Log.w(TAG, "recordApkDeleteEvent:deleteAppPkgName empty!");
            return false;
        }
        if (callerAppPkgName == null || callerAppPkgName.isEmpty()) {
            Log.w(TAG, "recordApkDeleteEvent:callerAppPkgName empty!");
            return false;
        }
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.recordApkDeleteEvent(deleteAppPkgName, callerAppPkgName, dateTime);
            } catch (RemoteException exce) {
                Log.e(TAG, "recordApkDeleteEvent failed!", exce);
            }
        } else {
            Log.w(TAG, "recordApkDeleteEvent:service not publish!");
        }
        return false;
    }

    public int getApkDeleteEventRecordCount() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getApkDeleteEventRecordCount();
            } catch (RemoteException exce) {
                Log.e(TAG, "getApkDeleteEventRecordCount failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getApkDeleteEventRecordCount:service not publish!");
        return 0;
    }

    public List<String> getApkDeleteEventRecords(int startIndex, int endIndex) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getApkDeleteEventRecords(startIndex, endIndex);
            } catch (RemoteException exce) {
                Log.e(TAG, "getApkDeleteEventRecords failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getApkDeleteEventRecords:service not publish!");
        return null;
    }

    public boolean recordApkInstallEvent(String installAppPkgName, String callerAppPkgName, String dateTime) {
        if (installAppPkgName == null || installAppPkgName.isEmpty()) {
            Log.w(TAG, "recordApkInstallEvent:deleteAppPkgName empty!");
            return false;
        }
        if (callerAppPkgName == null || callerAppPkgName.isEmpty()) {
            Log.w(TAG, "recordApkInstallEvent:callerAppPkgName empty!");
            return false;
        }
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.recordApkInstallEvent(installAppPkgName, callerAppPkgName, dateTime);
            } catch (RemoteException exce) {
                Log.e(TAG, "recordApkInstallEvent failed!", exce);
            }
        } else {
            Log.w(TAG, "recordApkInstallEvent:service not publish!");
        }
        return false;
    }

    public int getApkInstallEventRecordCount() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getApkInstallEventRecordCount();
            } catch (RemoteException exce) {
                Log.e(TAG, "getApkInstallEventRecordCount failed!", exce);
                return 0;
            }
        }
        Log.w(TAG, "getApkInstallEventRecordCount:service not publish!");
        return 0;
    }

    public List<String> getApkInstallEventRecords(int startIndex, int endIndex) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getApkInstallEventRecords(startIndex, endIndex);
            } catch (RemoteException exce) {
                Log.e(TAG, "getApkInstallEventRecords failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getApkInstallEventRecords:service not publish!");
        return null;
    }

    public boolean recordMcsConnectID(String connectID) {
        if (connectID == null || connectID.isEmpty()) {
            Log.w(TAG, "recordMcsConnectID:connectID empty!");
            return false;
        }
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.recordMcsConnectID(connectID);
            } catch (RemoteException exce) {
                Log.e(TAG, "recordMcsConnectID failed!", exce);
            }
        } else {
            Log.w(TAG, "recordMcsConnectID:service not publish!");
        }
        return false;
    }

    public String getMcsConnectID() {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getMcsConnectID();
            } catch (RemoteException exce) {
                Log.e(TAG, "getMcsConnectID failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "getMcsConnectID:service not publish!");
        return null;
    }

    public int getFileSize(String path) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.getFileSize(path);
            } catch (RemoteException exce) {
                Log.e(TAG, "getFileSize failed!", exce);
                return -1;
            }
        }
        Log.w(TAG, "getFileSize:service not publish!");
        return -1;
    }

    public byte[] readOplusFile(String path, int startPosition, int length) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.readOplusFile(path, startPosition, length);
            } catch (RemoteException exce) {
                Log.e(TAG, "readOplusFile failed!", exce);
                return null;
            }
        }
        Log.w(TAG, "readOplusFile:service not publish!");
        return null;
    }

    public int saveOplusFile(int fileMax, String path, int offset, boolean append, int length, byte[] data) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.saveOplusFile(fileMax, path, offset, append, length, data);
            } catch (RemoteException exce) {
                Log.e(TAG, "saveOplusFile failed!", exce);
                return -1;
            }
        }
        Log.w(TAG, "saveOplusFile:service not publish!");
        return -1;
    }

    public int saveEntireOplusFile(String sourcePath, String targetPath, boolean deleteSource) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.saveEntireOplusFile(sourcePath, targetPath, deleteSource);
            } catch (RemoteException exce) {
                Log.e(TAG, "saveEntireOplusFile failed!", exce);
                return -1;
            }
        }
        Log.w(TAG, "saveEntireOplusFile:service not publish!");
        return -1;
    }

    public boolean readEntireOplusFile(String sourcePath, String targetPath, boolean deleteSource) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.readEntireOplusFile(sourcePath, targetPath, deleteSource);
            } catch (RemoteException exce) {
                Log.e(TAG, "readEntireOplusFile failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "readEntireOplusFile:service not publish!");
        return false;
    }

    public boolean deleteOplusFile(String filePath) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.deleteOplusFile(filePath);
            } catch (RemoteException exce) {
                Log.e(TAG, "deleteOplusFile failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "deleteOplusFile:service not publish!");
        return false;
    }

    public boolean saveEntireOplusDir(String sourceDir, String targetDir, boolean deleteSource) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.saveEntireOplusDir(sourceDir, targetDir, deleteSource);
            } catch (RemoteException exce) {
                Log.e(TAG, "saveEntireOplusDir failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "saveEntireOplusDir:service not publish!");
        return false;
    }

    public boolean readEntireOplusDir(String sourceDir, String targetDir, boolean deleteSource) {
        IOplusUsageService iOplusUsageService = this.mOplusUsageService;
        if (iOplusUsageService != null) {
            try {
                return iOplusUsageService.readEntireOplusDir(sourceDir, targetDir, deleteSource);
            } catch (RemoteException exce) {
                Log.e(TAG, "saveEntireOplusDir failed!", exce);
                return false;
            }
        }
        Log.w(TAG, "saveEntireOplusDir:service not publish!");
        return false;
    }
}
