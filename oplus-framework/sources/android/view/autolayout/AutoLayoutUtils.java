package android.view.autolayout;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.internal.util.ConcurrentUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AutoLayoutUtils {
    public static final String TAG = "AutoLayout";

    public static AutoLayoutViewInfo getViewInfo(View view) {
        return (AutoLayoutViewInfo) view.getViewWrapper().getViewExt().getViewInfo();
    }

    public static int getViewMarginLeft(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && (params instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
            return marginParams.leftMargin;
        }
        return 0;
    }

    public static int getViewMarginRight(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && (params instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
            return marginParams.rightMargin;
        }
        return 0;
    }

    public static int getViewLeftFromRoot(View view) {
        int left = view.getLeft();
        for (ViewParent parent = view.getParent(); parent != null && (parent instanceof ViewGroup); parent = parent.getParent()) {
            left += ((ViewGroup) parent).getLeft();
        }
        return left;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static int strToInt(String value, int defaultValue) {
        if (isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Log.e("AutoLayout", "parse string to int error");
            return defaultValue;
        }
    }

    public static void registerDeviceStateCallBack(Context context) {
        if (context != null) {
            try {
                DeviceStateManager deviceStateManager = (DeviceStateManager) context.getSystemService("device_state");
                deviceStateManager.registerCallback(ConcurrentUtils.DIRECT_EXECUTOR, new DeviceStateManager.DeviceStateCallback() { // from class: android.view.autolayout.AutoLayoutUtils$$ExternalSyntheticLambda0
                    public final void onStateChanged(int i) {
                        AutoLayoutPolicyFactory.setDeviceState(i);
                    }
                });
            } catch (IllegalStateException e) {
            }
        }
    }

    public static boolean shouldRedrawThisImage(View view) {
        AutoLayoutViewInfo viewInfo = getViewInfo(view);
        if (!viewInfo.getIsImageType() || viewInfo.getHeightType() == 4) {
            return false;
        }
        if ((viewInfo.getHeightType() == 2 || viewInfo.getHeightType() == 1) && viewInfo.getWidthType() == 3) {
            return true;
        }
        if (viewInfo.getWidthType() == 2 && viewInfo.getHeightType() == 1) {
            return true;
        }
        if (viewInfo.getWidthType() == 1 && viewInfo.getHeightType() == 1 && viewInfo.getIsFlatView()) {
            return true;
        }
        return viewInfo.getWidthType() == 2 && viewInfo.getHeightType() == 2;
    }

    public static List<View> getDirectVisibleChildrenList(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        List<View> directVisibleChildren = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child.getVisibility() == 0 && isViewWithinTargetBounds(child, viewGroup)) {
                directVisibleChildren.add(child);
            }
        }
        return directVisibleChildren;
    }

    public static boolean isViewWithinTargetBounds(View paramView, View targetView) {
        paramView.getMeasuredWidth();
        paramView.getMeasuredHeight();
        int targetViewWidth = targetView.getMeasuredWidth();
        int targetViewHeight = targetView.getMeasuredHeight();
        if (paramView.getLeft() < 0 || paramView.getRight() > targetViewWidth || paramView.getTop() < 0 || paramView.getBottom() > targetViewHeight) {
            return false;
        }
        return true;
    }

    public static void appendExtraInfo(View view, String content) {
        AutoLayoutViewInfo viewInfo = getViewInfo(view);
        viewInfo.setExtraInfo(content);
    }
}
