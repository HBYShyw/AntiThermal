package android.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.graphics.Bitmap;
import android.graphics.Rect;
import com.oplus.screenshot.IOplusLongshotController;
import com.oplus.screenshot.OplusLongshotViewInfo;
import com.oplus.view.IOplusScrollBarEffect;
import com.oplus.view.OplusScrollBarEffect;

/* loaded from: classes.dex */
public interface IOplusViewHooks extends IOplusScrollBarEffect.ViewCallback, IOplusCommonFeature {
    public static final IOplusViewHooks DEFAULT = new IOplusViewHooks() { // from class: android.view.IOplusViewHooks.1
    };

    default IOplusViewHooks getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusViewHooks;
    }

    @Override // com.oplus.view.IOplusScrollBarEffect.ViewCallback
    default boolean awakenScrollBars() {
        return false;
    }

    @Override // com.oplus.view.IOplusScrollBarEffect.ViewCallback
    default boolean isLayoutRtl() {
        return false;
    }

    default boolean isLongshotConnected() {
        return false;
    }

    default boolean isOplusOSStyle() {
        return false;
    }

    default boolean isOplusStyle() {
        return false;
    }

    default void performClick() {
    }

    default int getOverScrollMode(int overScrollMode) {
        return overScrollMode;
    }

    default IOplusScrollBarEffect getScrollBarEffect() {
        return OplusScrollBarEffect.NO_EFFECT;
    }

    default boolean findViewsLongshotInfo(OplusLongshotViewInfo info) {
        return false;
    }

    default IOplusLongshotController getLongshotController() {
        return null;
    }

    default boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent, int oldScrollY, boolean result) {
        return false;
    }

    default Bitmap getOplusCustomDrawingCache(Rect clip, int viewTop, int mPrivateFlags) {
        return null;
    }

    default boolean needHook() {
        return false;
    }
}
