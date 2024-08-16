package android.view.autolayout;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class AutoLayoutCommonMeasurePolicy implements IAutoLayoutMeasurePolicy {
    private static final float FULL_SIZE_RATIO = 0.9f;
    private static final float LARGE_SIZE_RATIO = 0.3f;
    private static final float SMALL_SIZE_RATIO = 0.05f;

    @Override // android.view.autolayout.IAutoLayoutMeasurePolicy
    public int[] beforeMeasure(View view, int widthMeasureSpec, int heightMeasureSpec) {
        AutoLayoutUtils.getViewInfo(view).reset();
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

    @Override // android.view.autolayout.IAutoLayoutMeasurePolicy
    public int[] hookSetMeasureDimension(View view, int measuredWidth, int measuredHeight) {
        return new int[]{measuredWidth, measuredHeight};
    }

    @Override // android.view.autolayout.IAutoLayoutMeasurePolicy
    public void afterMeasure(View view) {
        fillViewInfoAfterMeasure(view);
    }

    @Override // android.view.autolayout.IAutoLayoutMeasurePolicy
    public void setOriginalDisplayMetrics(DisplayMetrics originalDisplayMetrics) {
    }

    private boolean isImageType(View view) {
        if (view instanceof ImageView) {
            return true;
        }
        return false;
    }

    private void fillViewInfoAfterMeasure(View view) {
        if (view.getVisibility() != 0) {
            return;
        }
        AutoLayoutViewInfo viewInfo = AutoLayoutUtils.getViewInfo(view);
        if (isImageType(view)) {
            viewInfo.setIsImageType(true);
        }
        int totalHeight = view.getRootView().getMeasuredHeight();
        int totalWidth = view.getRootView().getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        int viewWidth = view.getMeasuredWidth();
        if (viewHeight == 0 || viewWidth == 0) {
            return;
        }
        if (viewHeight < totalHeight * SMALL_SIZE_RATIO) {
            viewInfo.setHeightType(4);
        } else if (viewHeight < totalHeight * 0.3f) {
            viewInfo.setHeightType(1);
        } else if (viewHeight >= totalHeight * 0.3f && viewHeight < totalHeight * FULL_SIZE_RATIO) {
            viewInfo.setHeightType(2);
        } else if (viewHeight >= totalHeight * FULL_SIZE_RATIO) {
            viewInfo.setHeightType(3);
        }
        if (viewWidth < totalWidth * SMALL_SIZE_RATIO) {
            viewInfo.setWidthType(4);
        } else if (viewWidth < totalWidth * 0.3f) {
            viewInfo.setWidthType(1);
        } else if (viewWidth >= totalWidth * 0.3f && viewWidth < totalWidth * FULL_SIZE_RATIO) {
            viewInfo.setWidthType(2);
        } else if (viewWidth >= totalWidth * FULL_SIZE_RATIO) {
            viewInfo.setWidthType(3);
        }
        if (viewWidth > viewHeight * 2) {
            viewInfo.setIsFlatView(true);
        }
    }
}
