package com.oplus.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusTypeCastingHelper;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusAppFullScreenLayoutLandscape extends LinearLayout {
    private Context mContext;
    private int mDefaultWidth;
    private int mLastFontFlipFlag;
    private Locale mLocale;

    public OplusAppFullScreenLayoutLandscape(Context context) {
        this(context, null);
    }

    public OplusAppFullScreenLayoutLandscape(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OplusAppFullScreenLayoutLandscape(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = null;
        this.mDefaultWidth = 96;
        this.mLocale = null;
        this.mLastFontFlipFlag = -1;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mLocale = context.getResources().getConfiguration().locale;
        boolean hasHeteroFeature = OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_SCREEN_HETEROMORPHISM);
        this.mDefaultWidth = hasHeteroFeature ? 136 : 96;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        if (child != null) {
            int childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec), Integer.MIN_VALUE);
            int childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mDefaultWidth, 1073741824);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            setMeasuredDimension(this.mDefaultWidth, child.getMeasuredWidth());
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        View child = getChildAt(0);
        if (child != null) {
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int centerTop = ((bottom - top) - childHeight) / 2;
            int centerLeft = ((right - left) - childWidth) / 2;
            child.layout(centerLeft, centerTop, centerLeft + childWidth, centerTop + childHeight);
            return;
        }
        super.onLayout(b, left, top, right, bottom);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig != null) {
            TextView textView = (TextView) findViewById(201457774);
            if (newConfig.locale != this.mLocale) {
                this.mLocale = newConfig.locale;
                if (textView != null) {
                    textView.setText(201588933);
                    return;
                }
                return;
            }
            OplusBaseConfiguration baseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, newConfig);
            if (baseConfiguration != null && baseConfiguration.mOplusExtraConfiguration.mFlipFont != this.mLastFontFlipFlag) {
                if (textView != null) {
                    textView.setTypeface(Typeface.DEFAULT);
                }
                this.mLastFontFlipFlag = baseConfiguration.mOplusExtraConfiguration.mFlipFont;
            }
        }
    }
}
