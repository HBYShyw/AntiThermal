package com.oplus.osense;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.Log;
import cn.teddymobile.free.anteater.resources.UriConstants;
import com.oplus.osense.IOsenseResManager;
import com.oplus.osense.info.OsenseCtrlDataRequest;
import com.oplus.osense.info.OsenseNotifyRequest;
import com.oplus.osense.info.OsenseSaRequest;
import com.oplus.uah.UAHAdaptHelper;
import java.security.SecureRandom;
import java.util.Arrays;

/* loaded from: classes.dex */
public class OsenseResManager {
    private static final String OSENSE_SERVICE = "osensemanager";
    private static final String TAG = OsenseResManager.class.getSimpleName();
    private static volatile OsenseResManager sInstance = null;
    private IOsenseResManager mService;
    private UAHAdaptHelper mUAHAdaptHelper = UAHAdaptHelper.getInstance();

    public static OsenseResManager getInstance() {
        if (sInstance == null) {
            synchronized (OsenseResManager.class) {
                if (sInstance == null) {
                    sInstance = new OsenseResManager();
                }
            }
        }
        return sInstance;
    }

    private IOsenseResManager getService() {
        IOsenseResManager oSenseResMgr = this.mService;
        if (oSenseResMgr != null) {
            return oSenseResMgr;
        }
        IOsenseResManager asInterface = IOsenseResManager.Stub.asInterface(ServiceManager.getService(OSENSE_SERVICE));
        this.mService = asInterface;
        return asInterface;
    }

    private long obtainRandomHandle() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return Math.abs(random.nextLong());
    }

    public long setSceneAction(String identity, OsenseSaRequest request) {
        Bundle info;
        if (this.mUAHAdaptHelper.getAidlService()) {
            return this.mUAHAdaptHelper.adaptSetSceneAction(identity, request);
        }
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "setSceneAction... service is null ");
            return -1L;
        }
        long handle = obtainRandomHandle();
        try {
            String scene = request.getScene();
            String action = request.getAction();
            int timeout = request.getTimeout();
            if (TextUtils.isEmpty(action)) {
                info = request.getInfo();
                if (info == null) {
                    Log.e(TAG, "setSceneAction... the bundle of OsenseSaRequest is null ");
                    return -1L;
                }
                String act = info.getString(Telephony.WapPush.ACTION, "");
                if (TextUtils.isEmpty(act)) {
                    Log.e(TAG, "setSceneAction... the bundle of OsenseSaRequest is null ");
                    return -1L;
                }
                info.putString("identity", identity);
            } else {
                info = new Bundle();
                info.putString("identity", identity);
                info.putString(UriConstants.RESULT_COLUMN_SCENE, scene);
                info.putString(Telephony.WapPush.ACTION, action);
                info.putInt("timeout", timeout);
                info.putLong("handle", handle);
            }
            service.osenseSetSceneAction(info);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call osenseSetSceneAction: " + e);
        }
        return handle;
    }

    public void clrSceneAction(String identity, long handle) {
        if (this.mUAHAdaptHelper.getAidlService()) {
            this.mUAHAdaptHelper.adaptClrSceneAction(identity, handle);
            return;
        }
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "clrSceneAction... service is null");
            return;
        }
        try {
            service.osenseClrSceneAction(identity, handle);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call osenseClrSceneAction: " + e);
        }
    }

    public void setNotification(String identity, OsenseNotifyRequest request) {
        if (this.mUAHAdaptHelper.getAidlService()) {
            this.mUAHAdaptHelper.adaptSetNotification(identity, request);
            return;
        }
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "setNotification... service is null");
            return;
        }
        try {
            service.osenseSetNotification(identity, request);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call osenseSetNotification: " + e);
        }
    }

    public void setCtrlData(String identity, OsenseCtrlDataRequest ctrlData) {
        if (this.mUAHAdaptHelper.getAidlService()) {
            this.mUAHAdaptHelper.setControlData(ctrlData);
            return;
        }
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "setCtrlData... service is null");
            return;
        }
        try {
            service.osenseSetCtrlData(identity, ctrlData);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call osenseSetCtrlData: " + e);
        }
    }

    public void clrCtrlData(String identity) {
        if (this.mUAHAdaptHelper.getAidlService()) {
            this.mUAHAdaptHelper.clearControlData(identity);
            return;
        }
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "clrCtrlData... service is null");
            return;
        }
        try {
            service.osenseClrCtrlData(identity);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call osenseClrCtrlData: " + e);
        }
    }

    public int getModeStatus(String identity, int mode) {
        if (this.mUAHAdaptHelper.getAidlService()) {
            return this.mUAHAdaptHelper.adaptGetModeStatus(identity, mode);
        }
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "getModeStatus... service is null");
            return -1;
        }
        try {
            return service.osenseGetModeStatus(identity, mode);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call osenseGetModeStatus: " + e);
            return -1;
        }
    }

    public long[][][] getPerfLimit(String identity) {
        if (this.mUAHAdaptHelper.getAidlService()) {
            return this.mUAHAdaptHelper.adaptGetPerfLimit(identity);
        }
        long[][][] result = new long[0][];
        long[] jArr = new long[0];
        IOsenseResManager service = getService();
        if (service == null) {
            Log.e(TAG, "getModeStatus... service is null");
            return null;
        }
        try {
            long[] temp = service.osenseGetPerfLimit(identity);
            if (temp != null) {
                try {
                    if (temp.length >= 5) {
                        int index = 0 + 1;
                        try {
                            int timeLen = (int) temp[0];
                            int index2 = index + 1;
                            try {
                                int cpuLen = (int) temp[index];
                                int index3 = index2 + 1;
                                try {
                                    int gpuLen = (int) temp[index2];
                                    int index4 = index3 + 1;
                                    try {
                                        int schedLen = (int) temp[index3];
                                        int index5 = index4 + 1;
                                        try {
                                            int limitLen = (int) temp[index4];
                                            result = new long[limitLen][];
                                            long total = (((cpuLen * 4) + timeLen + (gpuLen * 4) + (schedLen * 2)) * limitLen) + 5;
                                            if (temp.length == total) {
                                                int i = 0;
                                                int index6 = index5;
                                                while (i < limitLen) {
                                                    long[][] limit = new long[11];
                                                    int i2 = index6 + timeLen;
                                                    int index7 = i2;
                                                    try {
                                                        limit[0] = Arrays.copyOfRange(temp, index6, i2);
                                                        int index8 = index7 + cpuLen;
                                                        limit[1] = Arrays.copyOfRange(temp, index7, index8);
                                                        int index9 = index8 + cpuLen;
                                                        limit[2] = Arrays.copyOfRange(temp, index8, index9);
                                                        int index10 = index9 + cpuLen;
                                                        limit[3] = Arrays.copyOfRange(temp, index9, index10);
                                                        int index11 = index10 + cpuLen;
                                                        limit[4] = Arrays.copyOfRange(temp, index10, index11);
                                                        int i3 = index11 + gpuLen;
                                                        index7 = i3;
                                                        limit[5] = Arrays.copyOfRange(temp, index11, i3);
                                                        int i4 = index7 + gpuLen;
                                                        int index12 = i4;
                                                        try {
                                                            limit[6] = Arrays.copyOfRange(temp, index7, i4);
                                                            int index13 = index12 + gpuLen;
                                                            limit[7] = Arrays.copyOfRange(temp, index12, index13);
                                                            int index14 = index13 + gpuLen;
                                                            limit[8] = Arrays.copyOfRange(temp, index13, index14);
                                                            int i5 = index14 + schedLen;
                                                            index12 = i5;
                                                            limit[9] = Arrays.copyOfRange(temp, index14, i5);
                                                            int index15 = index12 + schedLen;
                                                            try {
                                                                limit[10] = Arrays.copyOfRange(temp, index12, index15);
                                                                result[i] = limit;
                                                                i++;
                                                                index6 = index15;
                                                            } catch (Exception e) {
                                                                e = e;
                                                                e.printStackTrace();
                                                                return null;
                                                            }
                                                        } catch (Exception e2) {
                                                            e = e2;
                                                        }
                                                    } catch (Exception e3) {
                                                        e = e3;
                                                    }
                                                }
                                            }
                                        } catch (Exception e4) {
                                            e = e4;
                                        }
                                    } catch (Exception e5) {
                                        e = e5;
                                    }
                                } catch (Exception e6) {
                                    e = e6;
                                }
                            } catch (Exception e7) {
                                e = e7;
                            }
                        } catch (Exception e8) {
                            e = e8;
                        }
                    }
                } catch (Exception e9) {
                    e = e9;
                }
            }
            return result;
        } catch (Exception e10) {
            Log.e(TAG, "try to get performance limit! because " + e10.getMessage());
            e10.printStackTrace();
            return null;
        }
    }
}
