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
public class OplusAppFullscreenLayoutPortrait extends LinearLayout {
    private Context mContext;
    private int mDefaultHeight;
    private int mLastFontFlipFlag;
    private Locale mLocale;

    public OplusAppFullscreenLayoutPortrait(Context context) {
        this(context, null);
    }

    public OplusAppFullscreenLayoutPortrait(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OplusAppFullscreenLayoutPortrait(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = null;
        this.mDefaultHeight = 96;
        this.mLocale = null;
        this.mLastFontFlipFlag = -1;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mLocale = context.getResources().getConfiguration().locale;
        boolean hasHeteroFeature = OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_SCREEN_HETEROMORPHISM);
        this.mDefaultHeight = hasHeteroFeature ? 136 : 96;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = View.MeasureSpec.makeMeasureSpec(this.mDefaultHeight, 1073741824);
        super.onMeasure(widthMeasureSpec, heightSpec);
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
