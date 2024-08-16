package android.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public interface IOplusViewRootUtil extends IOplusCommonFeature {
    public static final IOplusViewRootUtil DEFAULT = new IOplusViewRootUtil() { // from class: android.view.IOplusViewRootUtil.1
    };
    public static final String NAME = "OplusViewRootUtil";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusViewRootUtil;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initSwipState(Display display, Context context) {
    }

    default void initSwipState(Display display, Context context, boolean isDisplayCompatApp) {
    }

    default boolean needScale(int noncompatDensity, int density, Display display) {
        return false;
    }

    default boolean swipeFromBottom(MotionEvent event, int noncompatDensity, int density, Display display) {
        return false;
    }

    default float getCompactScale() {
        return 1.0f;
    }

    default int getScreenHeight() {
        return 1;
    }

    default int getScreenWidth() {
        return 1;
    }

    default void checkGestureConfig(Context context) {
    }

    default DisplayInfo getDisplayInfo() {
        return null;
    }

    default IOplusLongshotViewHelper getOplusLongshotViewHelper(WeakReference<ViewRootImpl> viewAncestor) {
        return null;
    }
}
