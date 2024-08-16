package android.app;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusAppHeapManager extends IOplusCommonFeature {
    public static final IOplusAppHeapManager DEFAULT = new IOplusAppHeapManager() { // from class: android.app.IOplusAppHeapManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAppHeapManager;
    }

    /* renamed from: getDefault, reason: merged with bridge method [inline-methods] */
    default IOplusAppHeapManager m4getDefault() {
        return DEFAULT;
    }

    default IOplusAppHeapManager newInstance() {
        return DEFAULT;
    }

    default void updateProcessState(int state, int stateVaule) {
    }

    default void setIsInWhiteList(int isInWhiteList, String pckName) {
    }
}
