package android.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.graphics.Rect;
import android.view.WindowManager;

/* loaded from: classes.dex */
public interface IOplusScrollToTopManager extends IOplusCommonFeature {
    public static final IOplusScrollToTopManager DEFAULT = new IOplusScrollToTopManager() { // from class: android.view.IOplusScrollToTopManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusScrollToTopManager;
    }

    default IOplusScrollToTopManager getDefault() {
        return DEFAULT;
    }

    default IOplusScrollToTopManager newInstance() {
        return DEFAULT;
    }

    default void init(Context context) {
    }

    default void setWindowRootView(View view, WindowManager.LayoutParams params) {
    }

    default void setWindowFrame(Rect winFrame) {
    }

    default void handleWindowFocusChanged(Context context, boolean hasFocus) {
    }

    default void postShowGuidePopupRunnable(View decorView) {
    }

    default void processPointerEvent(MotionEvent e, Context context) {
    }

    default void onWindowDying() {
    }

    default void setIsInWhiteList(boolean isInWhiteList) {
    }

    default boolean getIsInWhiteList() {
        return false;
    }

    default void setNeedShowGuidePopup(boolean needShowGuidePopup) {
    }

    default boolean getNeedShowGuildPopup() {
        return false;
    }
}
