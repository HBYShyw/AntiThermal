package com.oplus.network;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.network.IOplusUidNwChange;
import com.oplus.network.IOplusUidPurStats;
import com.oplus.network.stats.AppFreezeStatsInfo;
import com.oplus.network.stats.AppFreezeSyncInfo;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class OplusUidPurStats {
    public static final boolean DBG = true;
    private static final String OPLUS_UID_PUR_STATS_SERVICE = "oplusuidpurstats";
    public static final String SRV_NAME = "oplusuidpurstats";
    public static final String TAG = "OplusUidPurStats";
    private static OplusUidPurStats mInstance;
    private IOplusUidPurStats sStatsService;
    private final ArrayList<IUidNwChangeCb> mCbList = new ArrayList<>();
    private IOplusUidNwChange mCb = new IOplusUidNwChange.Stub() { // from class: com.oplus.network.OplusUidPurStats.1
        @Override // com.oplus.network.IOplusUidNwChange
        public void uidNetworkChange(int uid, boolean networkFail, boolean isForegroundApp) throws RemoteException {
            Log.d(OplusUidPurStats.TAG, "uidNetworkChange " + uid + "," + networkFail + "," + isForegroundApp);
            synchronized (OplusUidPurStats.this.mCbList) {
                Iterator it = OplusUidPurStats.this.mCbList.iterator();
                while (it.hasNext()) {
                    IUidNwChangeCb cb = (IUidNwChangeCb) it.next();
                    cb.uidNetworkChange(uid, networkFail, isForegroundApp);
                }
            }
        }

        @Override // com.oplus.network.IOplusUidNwChange
        public void appFreezeDataNotify(String data) throws RemoteException {
            Log.d(OplusUidPurStats.TAG, "appFreezeDataNotify !");
            synchronized (OplusUidPurStats.this.mCbList) {
                Iterator it = OplusUidPurStats.this.mCbList.iterator();
                while (it.hasNext()) {
                    IUidNwChangeCb cb = (IUidNwChangeCb) it.next();
                    cb.appFreezeDataNotify(data);
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface IUidNwChangeCb {
        void appFreezeDataNotify(String str);

        void uidNetworkChange(int i, boolean z, boolean z2);
    }

    private synchronized IOplusUidPurStats getUidPurStatsService() throws RemoteException {
        if (this.sStatsService == null) {
            this.sStatsService = IOplusUidPurStats.Stub.asInterface(ServiceManager.getService("oplusuidpurstats"));
            Log.d(TAG, "getUidPurStatsService return " + this.sStatsService);
            IOplusUidPurStats iOplusUidPurStats = this.sStatsService;
            if (iOplusUidPurStats == null) {
                Log.e(TAG, "service get failed oplusuidpurstats");
            } else {
                iOplusUidPurStats.registerUidNwStatusChange(this.mCb);
            }
        }
        return this.sStatsService;
    }

    public OplusUidPurStats() {
        try {
            getUidPurStatsService();
        } catch (RemoteException e) {
            Log.e(TAG, "getUidPurStatsService failed" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static OplusUidPurStats getInstance() {
        OplusUidPurStats oplusUidPurStats;
        synchronized (OplusUidPurStats.class) {
            if (mInstance == null) {
                mInstance = new OplusUidPurStats();
            }
            oplusUidPurStats = mInstance;
        }
        return oplusUidPurStats;
    }

    public boolean getStatsCheckStatus() {
        try {
            return getUidPurStatsService().getStatsCheckStatus();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int[] getNoDataUids() {
        try {
            return getUidPurStatsService().getNoDataUids();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUidNoData(int uid) {
        try {
            return getUidPurStatsService().isUidNoData(uid);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void registerUidNwStatusChange(IUidNwChangeCb cb) {
        Log.d(TAG, "registerUidNwStatusChange start!");
        synchronized (this.mCbList) {
            if (!this.mCbList.contains(cb)) {
                this.mCbList.add(cb);
            }
        }
    }

    public void unregisterUidNwStatusChange(IUidNwChangeCb cb) {
        synchronized (this.mCbList) {
            if (this.mCbList.contains(cb)) {
                this.mCbList.remove(cb);
            }
        }
    }

    public String fetchAppFreezeSynInfoList() {
        try {
            JSONArray jsonArray = new JSONArray();
            AppFreezeSyncInfo[] array = getUidPurStatsService().fetchAppFreezeSynInfoList();
            if (array == null) {
                return null;
            }
            int size = array.length;
            for (int i = 0; i < size; i++) {
                JSONObject jobj = new JSONObject();
                jobj.put("mUid", array[i].mSyn.mUid);
                jobj.put("mOccurFlag", array[i].mSyn.mOccurFlag);
                jobj.put("mAddBlockTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mRemoveBlockTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mSendResetTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mEnterFgTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mEnterBgTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mAppName", array[i].mAppName);
                jsonArray.put(jobj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getClass().getSimpleName() + "," + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String fetchAppFreezeStatsInfoList() {
        try {
            JSONArray jsonArray = new JSONArray();
            AppFreezeStatsInfo[] array = getUidPurStatsService().fetchAppFreezeStatsInfoList();
            if (array == null) {
                return null;
            }
            int size = array.length;
            for (int i = 0; i < size; i++) {
                JSONObject jobj = new JSONObject();
                jobj.put("mUid", array[i].mStats.mUid);
                jobj.put("mOccurFlag", array[i].mStats.mOccurFlag);
                jobj.put("mRxBytes", array[i].mStats.mRxBytes);
                jobj.put("mTxBytes", array[i].mStats.mTxBytes);
                jobj.put("mRxCount", array[i].mStats.mRxCount);
                jobj.put("mTxCount", array[i].mStats.mTxCount);
                jobj.put("mAddBlockTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mRemoveBlockTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mSendResetTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mEnterFgTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mEnterBgTime", array[i].mHistory.mAddBlockTime);
                jobj.put("mAppName", array[i].mAppName);
                jsonArray.put(jobj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getClass().getSimpleName() + "," + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
