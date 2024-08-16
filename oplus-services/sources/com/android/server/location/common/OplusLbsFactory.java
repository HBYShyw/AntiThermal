package com.android.server.location.common;

import android.content.Context;
import android.util.Log;
import com.android.server.location.common.OplusLbsFeatureList;
import dalvik.system.PathClassLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OplusLbsFactory implements IOplusCommonFactory {
    private static final String LBS_FEATURE_SERVICE_FACTORY_IMPL_NAME = "com.android.server.location.OplusLbsFactoryImpl";
    private static final String OPLUS_LBS_COMMON_EXT_JAR_PATH = "/system_ext/framework/oplus-lbs-services.jar";
    private static final String OPLUS_LMS = "com.android.server.location.OplusLocationManagerService";
    private static final String TAG = "OplusLbsFactory";
    private static Context sContext;
    private static volatile OplusLbsFactory sInstance;

    public static OplusLbsFactory getInstance() {
        if (sInstance == null) {
            synchronized (OplusLbsFactory.class) {
                if (sInstance == null) {
                    try {
                        sInstance = (OplusLbsFactory) newInstance(OPLUS_LBS_COMMON_EXT_JAR_PATH, LBS_FEATURE_SERVICE_FACTORY_IMPL_NAME);
                    } catch (Exception e) {
                        Log.e(TAG, " Reflect exception getInstance: " + e.toString());
                        sInstance = new OplusLbsFactory();
                    }
                }
            }
        }
        return sInstance;
    }

    @Override // com.android.server.location.common.IOplusCommonFactory
    public boolean isValid(int i) {
        return i < OplusLbsFeatureList.OplusIndex.EndLbsFrameworkFactory.ordinal() && i > OplusLbsFeatureList.OplusIndex.StartLbsFrameworkFactory.ordinal();
    }

    public static void init(Context context) {
        sContext = context;
    }

    private static Object newInstance(String str) throws Exception {
        return Class.forName(str).getConstructor(new Class[0]).newInstance(new Object[0]);
    }

    static Object newInstance(String str, String str2) throws Exception {
        return Class.forName(str2, false, new PathClassLoader(str, OplusLbsFactory.class.getClassLoader())).getConstructor(new Class[0]).newInstance(new Object[0]);
    }
}
