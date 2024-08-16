package com.oplus.uah;

import android.util.Log;
import com.oplus.uah.info.UAHEventRequest;
import com.oplus.uah.info.UAHResRequest;
import com.oplus.uah.info.UAHRuleCtrlRequest;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UAHPerfManager {
    private static final String UAH_JNI_NAME = "UahPerf_Jni";
    private static final String TAG = "UAH-" + UAHPerfManager.class.getSimpleName();
    private static volatile UAHPerfManager sInstance = null;
    private static boolean sJniReady = false;

    static native int nativeUahEventAcquire(int i, String str, String str2, int i2, List list);

    static native String nativeUahGetHistory();

    static native int nativeUahGetModeStatus(String str, int i);

    static native String nativeUahReadFile(String str, int i);

    static native void nativeUahRelease(int i);

    static native int nativeUahResAcquire(List list, int i, String str, int i2);

    static native int nativeUahRuleCtrl(int i, int i2, List list);

    private UAHPerfManager() {
        try {
            System.loadLibrary(UAH_JNI_NAME);
            sJniReady = true;
        } catch (UnsatisfiedLinkError e) {
            sJniReady = false;
            Log.e(TAG, "loadLibrary UnsatisfiedLinkError");
        }
    }

    public static UAHPerfManager getInstance() {
        if (sInstance == null) {
            synchronized (UAHPerfManager.class) {
                if (sInstance == null) {
                    sInstance = new UAHPerfManager();
                }
            }
        }
        return sInstance;
    }

    public int tansSa2Event(int eventId, String sceneName, String identity, int timeout) {
        if (!sJniReady) {
            Log.e(TAG, "tansSa2Event failed, jni not ready");
            return -1;
        }
        return nativeUahEventAcquire(eventId, sceneName, identity, timeout, null);
    }

    public int tansUahRuleCtrl(int ruleId, int ruleState, List list) {
        if (!sJniReady) {
            Log.e(TAG, "tansUahRuleCtrl failed, jni not ready");
            return -1;
        }
        return nativeUahRuleCtrl(ruleId, ruleState, list);
    }

    public int uahEventAcquire(String identity, UAHEventRequest request) {
        if (!sJniReady) {
            Log.e(TAG, "uahEventAcquire failed, jni not ready");
            return -1;
        }
        int eventId = request.getEventId();
        String sceneName = request.getSceneName();
        int timeout = request.getTimeout();
        ArrayList list = request.getList();
        return nativeUahEventAcquire(eventId, sceneName, identity, timeout, list);
    }

    public int uahResAcquire(String identity, UAHResRequest request) {
        if (!sJniReady) {
            Log.e(TAG, "uahResAcquire failed, jni not ready");
            return -1;
        }
        ArrayList list = request.getList();
        int timeout = request.getTimeout();
        return nativeUahResAcquire(list, list.size(), identity, timeout);
    }

    public void uahRelease(int handle) {
        if (!sJniReady) {
            Log.e(TAG, "uahRelease failed, jni not ready");
        } else {
            nativeUahRelease(handle);
        }
    }

    public int getModeStatus(String identity, int mode) {
        if (!sJniReady) {
            Log.e(TAG, "getModeStatus failed, jni not ready");
            return -1;
        }
        return nativeUahGetModeStatus(identity, mode);
    }

    public void uahRuleCtrl(String identity, UAHRuleCtrlRequest mRuleCtrl) {
        if (!sJniReady) {
            Log.e(TAG, "uahRuleCtrl failed, jni not ready");
            return;
        }
        int ruleId = mRuleCtrl.getRuleId();
        int ruleState = mRuleCtrl.getRuleState();
        ArrayList list = mRuleCtrl.getList();
        nativeUahRuleCtrl(ruleId, ruleState, list);
    }

    private long[][] parseStr(String str) {
        String newStr = str.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "]").replaceAll("\\],", "\\];");
        String[] newStrArray = newStr.split(";");
        long[][] value2D = new long[newStrArray.length];
        for (int i = 0; i < newStrArray.length; i++) {
            int indexOfOpenBracket = newStrArray[i].indexOf("[");
            int indexOfLastBracket = newStrArray[i].lastIndexOf("]");
            newStrArray[i] = newStrArray[i].substring(indexOfOpenBracket + 1, indexOfLastBracket);
            String[] tmp = newStrArray[i].split(",");
            long[] value = new long[tmp.length];
            for (int j = 0; j < tmp.length; j++) {
                value[j] = Long.valueOf(tmp[j].trim()).longValue();
            }
            value2D[i] = value;
        }
        return value2D;
    }

    public long[][][] getGetHistory() {
        long[][][] result = new long[0][];
        if (!sJniReady) {
            Log.e(TAG, "getGetHistory failed, jni not ready");
            return result;
        }
        String strValue = nativeUahGetHistory();
        if (strValue.startsWith("start:") || strValue.endsWith("end")) {
            String tmp1 = strValue.replaceAll("start:", "");
            String strTmp = tmp1.replaceAll("end", "");
            String[] groupStr = strTmp.split("]];");
            if (groupStr == null || groupStr.length < 1) {
                Log.e(TAG, "getGetHistory empty history data,return");
                return null;
            }
            long[][][] result3D = new long[groupStr.length][];
            for (int i = 0; i < groupStr.length; i++) {
                if (i < groupStr.length) {
                    groupStr[i] = groupStr[i] + "]]";
                }
                long[][] result2D = parseStr(groupStr[i]);
                result3D[i] = result2D;
            }
            return result3D;
        }
        Log.e(TAG, "getGetHistory invalid format value,return null");
        return null;
    }

    public String uahReadFile(String identity, int mode) {
        if (!sJniReady) {
            Log.e(TAG, "uahReadFile failed, jni not ready");
            return null;
        }
        return nativeUahReadFile(identity, mode);
    }
}
