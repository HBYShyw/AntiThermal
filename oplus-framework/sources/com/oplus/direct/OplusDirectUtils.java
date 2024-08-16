package com.oplus.direct;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public class OplusDirectUtils {
    public static final boolean DBG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final String TAG = "DirectService";

    public static void onFindSuccess(IOplusDirectFindCallback callback, Bundle data) {
        if (callback != null) {
            try {
                callback.onDirectInfoFound(createSuccessResult(data));
            } catch (RemoteException e) {
                OplusLog.e(TAG, Log.getStackTraceString(e));
            } catch (Exception e2) {
                OplusLog.e(TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public static void onFindFailed(IOplusDirectFindCallback callback, String error) {
        if (callback != null) {
            try {
                callback.onDirectInfoFound(createFailedResult(error));
            } catch (RemoteException e) {
                OplusLog.e(TAG, Log.getStackTraceString(e));
            } catch (Exception e2) {
                OplusLog.e(TAG, Log.getStackTraceString(e2));
            }
        }
    }

    private static OplusDirectFindResult createSuccessResult(Bundle data) {
        OplusDirectFindResult result = new OplusDirectFindResult();
        if (data != null && !data.isEmpty()) {
            Bundle bundle = result.getBundle();
            bundle.putAll(data);
        }
        return result;
    }

    private static OplusDirectFindResult createFailedResult(String error) {
        OplusDirectFindResult result = new OplusDirectFindResult();
        Bundle bundle = result.getBundle();
        bundle.putString(OplusDirectFindResult.EXTRA_ERROR, error);
        return result;
    }
}
