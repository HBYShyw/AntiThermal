package com.oplus.view;

import android.os.Bundle;
import com.oplus.util.OplusLog;

@Deprecated
/* loaded from: classes.dex */
public class OplusLCDInfo {
    public static final String KEY_SCREEN_PRODUCT_TIME = "screen_product_time";
    public static final String KEY_SUB_SCREEN_PRODUCT_TIME = "sub_screen_product_time";
    private static final String TAG = "OplusLCDInfo";
    private static volatile OplusLCDInfo sOplusLCDInfo = null;
    private String mSerialNum = null;
    private String mSubSerialNum = null;

    private OplusLCDInfo() {
    }

    public static OplusLCDInfo getInstance() {
        if (sOplusLCDInfo == null) {
            synchronized (OplusLCDInfo.class) {
                if (sOplusLCDInfo == null) {
                    sOplusLCDInfo = new OplusLCDInfo();
                }
            }
        }
        return sOplusLCDInfo;
    }

    public void setScreenProductionTime(String num) {
        OplusLog.d(TAG, "set screen production time: " + num);
        this.mSerialNum = num;
    }

    public void setSubScreenProductionTime(String num) {
        OplusLog.d(TAG, "set sub screen production time: " + num);
        this.mSubSerialNum = num;
    }

    public Bundle getScreenProductionTime() {
        Bundle data = new Bundle();
        String str = this.mSerialNum;
        if (str != null) {
            data.putString(KEY_SCREEN_PRODUCT_TIME, str);
            OplusLog.d(TAG, "screen production time: " + this.mSerialNum);
            String str2 = this.mSubSerialNum;
            if (str2 != null && !this.mSerialNum.equals(str2)) {
                data.putString(KEY_SUB_SCREEN_PRODUCT_TIME, this.mSubSerialNum);
                OplusLog.d(TAG, "sub screen production time: " + this.mSubSerialNum);
            }
            return data;
        }
        OplusLog.d(TAG, "screen production time is null");
        return null;
    }
}
