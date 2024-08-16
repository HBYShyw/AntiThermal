package com.oplus.deepthinker.sdk.app;

import android.content.Context;
import android.text.TextUtils;

/* loaded from: classes.dex */
public interface ICommonFeature {
    public static final int BIND_APP_TYPE = 4;
    public static final int BIND_APP_UNUSE = 2;
    public static final int BIND_APP_USAGE_SORT = 6;
    public static final int BIND_DEEP_SLEEP = 3;
    public static final int BIND_EVENT_HANDLE = 5;
    public static final int BIND_IDLE_SCREEN = 8;
    public static final int BIND_INTENT_DECISION = 10;
    public static final int BIND_PERIOD_TOP_APPS = 7;
    public static final int BIND_PLATFORM = 1;
    public static final int BIND_PLATFORM_POOL = 0;
    public static final int BIND_SMART_GPS = 9;

    default ICommonFeature defaultFeatureImpl() {
        return null;
    }

    default ICommonFeature getFeatureImpl(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return (ICommonFeature) Class.forName(str).getConstructor(Context.class).newInstance(context);
            } catch (Throwable th) {
                SDKLog.w("ICommonFeature", "error when getFeatureImpl: " + th);
            }
        }
        return defaultFeatureImpl();
    }
}
