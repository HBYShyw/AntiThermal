package com.oplus.orms;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.view.MotionEvent;
import com.oplus.orms.info.OrmsCtrlDataParam;
import com.oplus.orms.info.OrmsNotifyParam;
import com.oplus.orms.info.OrmsSaParam;

/* loaded from: classes.dex */
public interface IOplusResourceManager extends IOplusCommonFeature {
    public static final IOplusResourceManager DEFAULT = new IOplusResourceManager() { // from class: com.oplus.orms.IOplusResourceManager.1
    };
    public static final String NAME = "IOplusResourceManager";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusResourceManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default long ormsSetSceneAction(OrmsSaParam ormsSaParam) {
        return -1L;
    }

    default void ormsClrSceneAction(long handle) {
    }

    default void ormsSetNotification(OrmsNotifyParam ormsNotifyParam) {
    }

    default void ormsSetCtrlData(OrmsCtrlDataParam ormsCtrlDataParam) {
    }

    default void ormsClrCtrlData() {
    }

    default int ormsGetModeStatus(int mode) {
        return -1;
    }

    default long[][][] ormsGetPerfLimit() {
        return null;
    }

    default void ormsSendFling(MotionEvent ev, int duration) {
    }
}
