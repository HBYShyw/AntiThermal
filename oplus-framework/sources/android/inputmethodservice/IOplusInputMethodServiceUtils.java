package android.inputmethodservice;

import android.app.Dialog;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.database.ContentObserver;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.view.View;
import android.view.WindowInsets;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public interface IOplusInputMethodServiceUtils extends IOplusCommonFeature {
    public static final IOplusInputMethodServiceUtils DEFAULT = new IOplusInputMethodServiceUtils() { // from class: android.inputmethodservice.IOplusInputMethodServiceUtils.1
    };

    default IOplusInputMethodServiceUtils getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusInputMethodServiceUtils;
    }

    default void init(Context context) {
    }

    @Deprecated
    default boolean getDockSide() {
        return false;
    }

    default void onChange(Uri uri) {
    }

    default void updateNavigationGuardColor(Dialog window) {
    }

    default void updateNavigationGuardColorDelay(Dialog window) {
    }

    default void onComputeRaise(InputMethodService.Insets mTmpInsets, Dialog window) {
    }

    @Deprecated
    default void uploadData(long time) {
    }

    default void onCreateAndRegister(ContentObserver contentObserver) {
    }

    default boolean showRaiseKeyboard(WindowInsets insets) {
        return false;
    }

    default void onDestroy() {
    }

    default boolean hideImmediately(int flag, Dialog window) {
        return false;
    }

    default int changeFlag(int flag) {
        return flag;
    }

    @Deprecated
    default boolean skipInsetChange(int flag) {
        return true;
    }

    default boolean shouldPreventTouch() {
        return false;
    }

    default boolean isFoldDisplayOpen() {
        return false;
    }

    @Deprecated
    default Interpolator replaceIMEInterpolator(Interpolator interpolator) {
        return interpolator;
    }

    @Deprecated
    default int replaceIMEDurationMs(boolean show, int time) {
        return time;
    }

    @Deprecated
    default boolean setInsetAnimationTid(int type) {
        return false;
    }

    default void forceDarkWithoutTheme(Context context, View view, boolean useAutoDark) {
    }
}
