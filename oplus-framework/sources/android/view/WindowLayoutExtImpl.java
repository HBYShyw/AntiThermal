package android.view;

import android.graphics.Rect;
import android.view.WindowManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.flexiblewindow.FlexibleWindowManager;
import com.oplus.widget.OplusMaxLinearLayout;
import com.oplus.zoomwindow.OplusZoomWindowManager;

/* loaded from: classes.dex */
public class WindowLayoutExtImpl implements IWindowLayoutExt {
    private static final boolean SUPPORT_REVISE_SQUARE_DISPLAY_ORIENTATION = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_REVISE_SQUARE_DISPLAY_ORIENTATION);
    private WindowLayout mBase;

    public WindowLayoutExtImpl(Object base) {
        this.mBase = (WindowLayout) base;
    }

    public boolean isCutoutModeShow(int cutoutMode) {
        return cutoutMode == 5;
    }

    public boolean inZoomWindowMode(int windowMode) {
        return windowMode == 100;
    }

    public void adjustWindowFrame(WindowManager.LayoutParams attrs, Rect windowBounds, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) {
        FlexibleWindowManager.getInstance().adjustWindowFrame(attrs, windowBounds, windowingMode, outDisplayFrame, outParentFrame);
        if (windowingMode == 100) {
            OplusZoomWindowManager.getInstance().adjustWindowFrameForZoom(attrs, windowingMode, outDisplayFrame, outParentFrame);
        }
    }

    public void adjustDisplayCutoutSafeExceptMaybeBars(WindowManager.LayoutParams attrs, Rect displayFrame, Rect displayCutoutSafeExceptMaybeBars) {
        if (attrs != null && displayFrame != null && displayCutoutSafeExceptMaybeBars != null && SUPPORT_REVISE_SQUARE_DISPLAY_ORIENTATION && attrs.layoutInDisplayCutoutMode == 1) {
            if (displayFrame.width() < displayFrame.height()) {
                displayCutoutSafeExceptMaybeBars.left = Integer.MIN_VALUE;
                displayCutoutSafeExceptMaybeBars.right = OplusMaxLinearLayout.INVALID_MAX_VALUE;
            } else {
                displayCutoutSafeExceptMaybeBars.top = Integer.MIN_VALUE;
                displayCutoutSafeExceptMaybeBars.bottom = OplusMaxLinearLayout.INVALID_MAX_VALUE;
            }
        }
    }

    public int adjustDisplayCutoutMode(WindowManager.LayoutParams attrs, InsetsState state) {
        if (state.getWrapper().getExtImpl().getExtraDisplayCutoutMode() == -1) {
            return attrs.layoutInDisplayCutoutMode;
        }
        return state.getWrapper().getExtImpl().getExtraDisplayCutoutMode();
    }
}
