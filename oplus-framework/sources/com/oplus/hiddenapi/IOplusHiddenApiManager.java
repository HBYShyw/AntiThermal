package com.oplus.hiddenapi;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.os.Handler;
import android.os.SystemProperties;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusHiddenApiManager extends IOplusCommonFeature {
    public static final boolean DEBUG_DETAIL = false;
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final IOplusHiddenApiManager DEFAULT = new IOplusHiddenApiManager() { // from class: com.oplus.hiddenapi.IOplusHiddenApiManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusHiddenApiManagerExt;
    }

    default IOplusHiddenApiManager getDefault() {
        return DEFAULT;
    }

    default void initAndRegisterSettingsListener(Context context, Handler handler) {
    }

    default List<String> getExemptions(String packageName) {
        return Collections.emptyList();
    }

    default void dump(PrintWriter writer, String[] args) {
    }
}
