package com.android.internal.widget.floatingtoolbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class OverflowPanelViewHelperExtImpl implements IOverflowPanelViewHelperExt {
    private static final int OVER_FLOW_FIRST_ITEM_HEIGHT_DP = 52;
    private static final int OVER_FLOW_FIRST_ITEM_PADDING_TOP_DP = 12;
    private static final int OVER_FLOW_NORMAL_ITEM_HEIGHT_DP = 40;
    private static final float PIXEL_OFFSET = 0.5f;
    private int mConvertViewPosition;
    private boolean mOpenOverflowUpwards;
    private int mOverFlowMenuCount;

    public OverflowPanelViewHelperExtImpl(Object base) {
    }

    public void setOverflowMenuCount(int count) {
        this.mOverFlowMenuCount = count;
    }

    public void setConvertViewPosition(int position) {
        this.mConvertViewPosition = position;
    }

    public void setOverflowDirection(boolean upward) {
        this.mOpenOverflowUpwards = upward;
    }

    private void setConvertViewPadding(View convertView, int sidePadding, int minimumWidth) {
        int oplusOsMinWidth = convertView.getResources().getDimensionPixelSize(201654402);
        int minimumWidth2 = Math.max(oplusOsMinWidth, minimumWidth);
        convertView.setMinimumWidth(minimumWidth2);
        convertView.setMinimumHeight(dp2px(convertView.getContext(), 40));
        ViewGroup.LayoutParams params = convertView.getLayoutParams();
        ViewGroup.LayoutParams params2 = params == null ? new ViewGroup.LayoutParams(minimumWidth2, 0) : params;
        int paddingTop = 0;
        int paddingBottom = 0;
        boolean z = this.mOpenOverflowUpwards;
        if (z && this.mConvertViewPosition == 0) {
            paddingTop = dp2px(convertView.getContext(), 12);
            params2.height = dp2px(convertView.getContext(), 52);
            convertView.setLayoutParams(params2);
        } else if (!z && this.mConvertViewPosition == this.mOverFlowMenuCount - 1) {
            paddingBottom = dp2px(convertView.getContext(), 12);
            params2.height = dp2px(convertView.getContext(), 52);
            convertView.setLayoutParams(params2);
        } else {
            params2.height = dp2px(convertView.getContext(), 40);
            convertView.setLayoutParams(params2);
        }
        convertView.setPadding(sidePadding, paddingTop, sidePadding, paddingBottom);
    }

    public void hookGetView(View convertView, int sidePadding, int minimumWidth) {
        setConvertViewPadding(convertView, sidePadding, minimumWidth);
    }

    public int hookGetSidePaddingRes(int defaultValue) {
        return 201654467;
    }

    private int dp2px(Context context, int dp) {
        return (int) ((context.getResources().getDisplayMetrics().density * dp) + 0.5f);
    }
}
