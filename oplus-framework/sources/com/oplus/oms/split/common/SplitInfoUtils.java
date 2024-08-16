package com.oplus.oms.split.common;

import android.text.TextUtils;
import java.util.List;

/* loaded from: classes.dex */
public class SplitInfoUtils {
    public static final int ERROR = -1;
    public static final int LENGTH = 2;
    private static final String SPLIT_SPLIT = "@";
    private static final String TAG = "SplitInfoUtils";

    private SplitInfoUtils() {
    }

    public static int getVersionCode(String versionString) {
        if (TextUtils.isEmpty(versionString)) {
            return -1;
        }
        String[] strings = versionString.split(SPLIT_SPLIT);
        if (strings.length != 2) {
            return -1;
        }
        String codeString = strings[1];
        try {
            return Integer.parseInt(codeString);
        } catch (NumberFormatException e) {
            SplitLog.e(TAG, "NumberFormatException error", new Object[0]);
            return -1;
        }
    }

    public static String getSplitNames(List<String> moudleNames) {
        if (moudleNames == null || moudleNames.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String name : moudleNames) {
            builder.append(name);
            builder.append(SPLIT_SPLIT);
        }
        return builder.toString();
    }
}
