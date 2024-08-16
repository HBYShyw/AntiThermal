package com.oplus.ocs;

import androidx.annotation.Keep;
import com.oplus.oms.split.common.ProcessInfoData;
import com.oplus.oms.split.common.SplitInfoData;
import java.util.ArrayList;
import java.util.HashMap;

@Keep
/* loaded from: classes.dex */
public final class OmsConfig {
    public static final String DEFAULT_SPLIT_INFO_VERSION = "1.0";
    public static final String OMS_ID = "splitApk";
    public static final boolean OMS_MODE = true;
    public static final String VERSION_NAME = "1.0.0";
    public static final HashMap<String, SplitInfoData> sSplitMap;
    public static final String[] DYNAMIC_FEATURES = {"battery_secret_plugin"};
    public static final HashMap<String, ProcessInfoData> sProcessMap = new HashMap<>();

    static {
        HashMap<String, SplitInfoData> hashMap = new HashMap<>();
        sSplitMap = hashMap;
        hashMap.put("battery_secret_plugin", new SplitInfoData("battery_secret_plugin", "null", new ArrayList()));
    }
}
