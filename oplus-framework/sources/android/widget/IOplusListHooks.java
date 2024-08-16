package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusListHooks extends IOplusCommonFeature {
    public static final IOplusListHooks DEFAULT = new IOplusListHooks() { // from class: android.widget.IOplusListHooks.1
    };

    default IOplusListHooks getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusListHooks;
    }

    default FastScroller getFastScroller(AbsListView absListView, int style) {
        return new FastScroller(absListView, style);
    }

    default void setOptEnable(boolean enable) {
    }

    default void setForceUsingSpring(boolean enable) {
    }
}
