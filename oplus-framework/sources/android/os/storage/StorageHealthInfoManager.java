package android.os.storage;

import android.content.Context;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class StorageHealthInfoManager {
    public static final double PE_CYCLE_LEVEL = 1800.0d;
    public static final int PE_CYCLE_MAX = 30;
    public static final int RR_THREHOLD = 30;
    public static final int RTBB_THREHOLD = 30;
    public static final String STORAGEHEALTHINFO_SERVICE = "storage_healthinfo";
    private static final String TAG = "StorageHealthInfoManager";
    public static final int WAF_THREHOLD = 5;
    IStorageHealthInfoService mService;

    public StorageHealthInfoManager(Context context, IStorageHealthInfoService service) {
        this.mService = service;
    }

    public Map<String, String> getStorageHealthInfoMap() throws RemoteException {
        String[] strStorageHealthInfo = getstrStorageHealthInfo();
        String[] strStorageHealthInfoItem = getStorageHealthInfoItem();
        if (strStorageHealthInfo == null || strStorageHealthInfoItem == null || strStorageHealthInfo.length != strStorageHealthInfoItem.length) {
            return null;
        }
        Map<String, String> logMap = new HashMap<>();
        for (int i = 0; i < strStorageHealthInfoItem.length; i++) {
            logMap.put(strStorageHealthInfoItem[i], strStorageHealthInfo[i]);
        }
        return logMap;
    }

    public boolean judgeStorageHealthInfo(Map<String, String> logMap) throws RemoteException {
        Map<String, String> newLogMap;
        if (logMap == null || logMap.get("badBlock_runtim") == null || logMap.get("readReclaim") == null || logMap.get("TBW") == null || logMap.get("eraseXLCCntAvg") == null || (newLogMap = getStorageHealthInfoMap()) == null) {
            return false;
        }
        String storageSize = newLogMap.get("StorageSize");
        int capacity = Integer.parseInt(storageSize.substring(0, storageSize.length() - 1));
        int dValueRTBB = Integer.parseInt(newLogMap.get("badBlock_runtim")) - Integer.parseInt(logMap.get("badBlock_runtim"));
        int dValueRR = Integer.parseInt(newLogMap.get("readReclaim")) - Integer.parseInt(logMap.get("readReclaim"));
        int dValueTBW = Integer.parseInt(newLogMap.get("TBW")) - Integer.parseInt(logMap.get("TBW"));
        int dValuePE = Integer.parseInt(newLogMap.get("eraseXLCCntAvg")) - Integer.parseInt(logMap.get("eraseXLCCntAvg"));
        float dValueWAF = 0.0f;
        if (dValueTBW > 0 && dValuePE > 0) {
            dValueWAF = ((dValuePE * capacity) * 1024) / (dValueTBW * 100);
        }
        return dValueWAF > 0.0f && dValueWAF <= 5.0f && dValueRR <= 30 && dValueRTBB <= 30;
    }

    public boolean checkStorageHealthInfo() throws RemoteException {
        Map<String, String> newLogMap = getStorageHealthInfoMap();
        if (newLogMap == null) {
            return false;
        }
        int valuePE = Integer.parseInt(newLogMap.get("eraseXLCCntAvg"));
        if (valuePE > 30) {
            return false;
        }
        return true;
    }

    public String[] getstrStorageHealthInfo() throws RemoteException {
        return this.mService.getstrStorageHealthInfo();
    }

    public String[] getStorageHealthInfoItem() throws RemoteException {
        return this.mService.getStorageHealthInfoItem();
    }

    public byte[] getStorageOriginalInfo() throws RemoteException {
        return this.mService.getStorageOriginalInfo();
    }
}
