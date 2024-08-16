package com.oplus.performance;

import com.oplus.orms.OplusResourceManager;
import com.oplus.orms.info.OrmsSaParam;

/* loaded from: classes.dex */
public class OplusPerformanceManager implements IOplusPerformanceManager {
    private static final String TAG = "OplusPerformanceManager";
    private static OplusPerformanceManager sInstance = null;

    public static OplusPerformanceManager getInstance() {
        if (sInstance == null) {
            sInstance = new OplusPerformanceManager();
        }
        return sInstance;
    }

    private OplusPerformanceManager() {
    }

    @Override // com.oplus.performance.IOplusPerformanceManager
    public void ormsAction(Class clazz, String scene, String action, int timeout) {
        OplusResourceManager OrmsManager = OplusResourceManager.getInstance(clazz);
        if (OrmsManager != null) {
            OrmsManager.ormsSetSceneAction(new OrmsSaParam(scene, action, timeout));
        }
    }
}
