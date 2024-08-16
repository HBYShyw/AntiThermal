package android.view.performance;

import android.common.IOplusCommonFeature;

/* loaded from: classes.dex */
public interface IOplusViewRootPerfInjector extends IOplusCommonFeature {
    public static final IOplusViewRootPerfInjector DEFAULT = new IOplusViewRootPerfInjector() { // from class: android.view.performance.IOplusViewRootPerfInjector.1
    };

    default void initViewRoomImpl() {
    }

    default void checkIsFragmentAnimUI() {
    }

    default boolean isFragmentAnimUI() {
        return false;
    }

    default void setIsFragmentAnimUI(boolean isFragmentAnimUI) {
    }

    default boolean checkTraversalsImmediatelyProssible(boolean isFirst) {
        return false;
    }

    default boolean checkTraversalsImmediatelyProssibleInTraversals(boolean isFirst, boolean mIsInTraversal) {
        return false;
    }

    default boolean disableRelayout() {
        return false;
    }
}
