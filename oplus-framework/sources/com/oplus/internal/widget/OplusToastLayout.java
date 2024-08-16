package com.oplus.internal.widget;

import android.R;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/* loaded from: classes.dex */
public class OplusToastLayout extends LinearLayout {
    private static final boolean DBG = false;
    private static final int ELEVATION = 10;
    private static final float ELEVATION_ALPHA = 0.3f;
    private static final int HORIZONTAL_MARGIN_DELTA = 8;
    private static final int LAYOUT_MIN_HEIGHT_DP = 34;
    private static final float PIXEL_OFFSET = 0.5f;
    private static final String TAG = "OplusToastLayout";
    private static final float TEXT_ADD_SPACING = 0.0f;
    private static final float TEXT_MULTI_SPACING = 1.2f;
    private static final int TOAST_MARGIN_LEFT = 16;
    private final int mTextColor;
    private final Rect mTextPadding;
    private final float mTextSize;
    private final Typeface mTypeface;
    private final WindowManager mWm;

    public OplusToastLayout(Context context) {
        this(context, null);
    }

    public OplusToastLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Rect rect = new Rect();
        this.mTextPadding = rect;
        setBackgroundResource(201850937);
        this.mTextColor = getResources().getColor(201719863);
        this.mTextSize = getResources().getDimension(201654381);
        rect.left = getResources().getDimensionPixelSize(201654382);
        rect.top = getResources().getDimensionPixelSize(201654383);
        rect.right = getResources().getDimensionPixelSize(201654384);
        rect.bottom = getResources().getDimensionPixelSize(201654385);
        this.mTypeface = Typeface.create("sans-serif-medium", 0);
        setId(201457796);
        this.mWm = (WindowManager) context.getSystemService("window");
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextView textView = (TextView) findViewById(R.id.message);
        textView.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        textView.setGravity(17);
        textView.setPadding(this.mTextPadding.left, this.mTextPadding.top, this.mTextPadding.right, this.mTextPadding.bottom);
        textView.setTextColor(this.mTextColor);
        textView.setTextSize(0, this.mTextSize);
        Typeface typeface = this.mTypeface;
        if (typeface != null) {
            textView.setTypeface(typeface, 0);
        }
        textView.setMinHeight(dp2px(34));
        textView.setLineSpacing(0.0f, TEXT_MULTI_SPACING);
        final int cornerSize = getResources().getDimensionPixelSize(201654462);
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() { // from class: com.oplus.internal.widget.OplusToastLayout.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setAlpha(0.3f);
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerSize);
            }
        };
        setOutlineProvider(viewOutlineProvider);
        setElevation(dp2px(10));
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        Point displaySize = new Point();
        this.mWm.getDefaultDisplay().getRealSize(displaySize);
        int targetWidth = displaySize.x - (dp2px(16) * 2);
        if (size > targetWidth) {
            super.onMeasure(View.MeasureSpec.makeSafeMeasureSpec(targetWidth, mode), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int dp2px(int dp) {
        return (int) ((getContext().getResources().getDisplayMetrics().density * dp) + 0.5f);
    }
}
