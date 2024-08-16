package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.StaticLayoutBuilderCompat;
import com.google.android.material.internal.CollapsingTextHelper;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import java.lang.reflect.Field;
import m1.COUIEaseInterpolator;

/* loaded from: classes.dex */
public class COUICollapsingToolbarLayout extends CollapsingToolbarLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String ELLIPSIS_NORMAL = "…";
    private static final String ELLIPSIS_TWO_DOTS = "‥";
    private static final boolean IS_DEBUG = false;
    private static final String TAG = "COUICollapsingToolbarLayout";
    private static final PathInterpolator TITLE_PATH_INTERPOLATOR = new COUIEaseInterpolator();
    private Rect mCollapsedBounds;
    private int mCollapsedSubtitleMarginTopIfNotHidden;
    private RectF mCurrentBounds;
    private Rect mExpandedBounds;
    private View mIconView;
    private ViewTreeObserver.OnGlobalLayoutListener mIconViewFirstLayoutListener;
    private StaticLayout mTextLayout;
    private TextPaint mTextPaint;
    private int mTitleTranslateOffset;
    private int mToolbarNormalPaddingEnd;
    private int mToolbarNormalPaddingStart;

    public COUICollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    private void addIconView() {
        View view = this.mIconView;
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof CollapsingToolbarLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) ((CollapsingToolbarLayout.LayoutParams) layoutParams)).gravity = 8388691;
        }
        addView(this.mIconView, getChildCount());
        this.mIconView.getViewTreeObserver().addOnGlobalLayoutListener(this.mIconViewFirstLayoutListener);
    }

    private int calculateCollapsedBound() {
        View findSubtitleContentView;
        int measuredHeight;
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (!(viewGroup instanceof COUICollapsableAppBarLayout)) {
            return 0;
        }
        COUICollapsableAppBarLayout cOUICollapsableAppBarLayout = (COUICollapsableAppBarLayout) viewGroup;
        if (cOUICollapsableAppBarLayout.isSubtitleHideEnable() || (findSubtitleContentView = cOUICollapsableAppBarLayout.findSubtitleContentView()) == null || findSubtitleContentView.getVisibility() == 8 || (measuredHeight = findSubtitleContentView.getMeasuredHeight()) == 0) {
            return 0;
        }
        return ((this.mCollapsedBounds.height() - ((int) this.collapsingTextHelper.getCollapsedTextHeight())) / 2) - (measuredHeight + this.mCollapsedSubtitleMarginTopIfNotHidden);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        TextDirectionHeuristicCompat textDirectionHeuristicCompat;
        if (ViewCompat.x(this) == 1) {
            textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.f2295d;
        } else {
            textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.f2294c;
        }
        return textDirectionHeuristicCompat.a(charSequence, 0, charSequence.length());
    }

    private StaticLayout createStaticLayout() {
        float floatValue = ((Float) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "scale")).floatValue();
        if (floatValue == 1.0f || floatValue == 0.0f) {
            return null;
        }
        CharSequence text = this.collapsingTextHelper.getText();
        try {
            return StaticLayoutBuilderCompat.obtain(text, this.mTextPaint, (int) (this.mCurrentBounds.width() / floatValue)).setEllipsize(TextUtils.TruncateAt.END).setIsRtl(calculateIsRtl(text)).setAlignment(Layout.Alignment.ALIGN_NORMAL).setIncludePad(false).setMaxLines(1).build();
        } catch (StaticLayoutBuilderCompat.StaticLayoutBuilderCompatException e10) {
            Log.e(TAG, e10.getCause().getMessage(), e10);
            return null;
        }
    }

    private Object getReflectField(Class cls, Object obj, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (Exception e10) {
            Log.e(TAG, "getReflectField error: " + e10.getMessage());
            return null;
        }
    }

    private float getScale() {
        try {
            return ((Float) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "scale")).floatValue();
        } catch (Exception unused) {
            return 1.0f;
        }
    }

    private Rect getTextHelperCollapsedBounds() {
        try {
            return (Rect) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "collapsedBounds");
        } catch (Exception unused) {
            return null;
        }
    }

    private RectF getTextHelperCurrentBounds() {
        try {
            return (RectF) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "currentBounds");
        } catch (Exception unused) {
            return null;
        }
    }

    private Rect getTextHelperExpandedBounds() {
        try {
            return (Rect) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "expandedBounds");
        } catch (Exception unused) {
            return null;
        }
    }

    private StaticLayout getTextLayout() {
        try {
            return (StaticLayout) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "textLayout");
        } catch (Exception unused) {
            return null;
        }
    }

    private TextPaint getTextPaint() {
        try {
            return (TextPaint) getReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "textPaint");
        } catch (Exception unused) {
            return null;
        }
    }

    private void init(AttributeSet attributeSet) {
        resetTextHelperInterpolator();
        this.mTextPaint = getTextPaint();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUICollapsingToolbarLayout);
            int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUICollapsingToolbarLayout_iconView, 0);
            obtainStyledAttributes.recycle();
            setIconView(resourceId);
        }
    }

    private void setReflectField(Class cls, Object obj, String str, Object obj2) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (Exception e10) {
            Log.e(TAG, "setReflectField error: " + e10.getMessage());
        }
    }

    private void translateTitleIfNeed() {
        if (this.mCollapsedBounds == null) {
            return;
        }
        int calculateCollapsedBound = calculateCollapsedBound();
        this.mTitleTranslateOffset = calculateCollapsedBound;
        if (calculateCollapsedBound != 0) {
            this.mCollapsedBounds.offset(0, calculateCollapsedBound);
            this.collapsingTextHelper.recalculate();
        }
    }

    private void updateTextLayoutIfNeed() {
        StaticLayout staticLayout;
        StaticLayout createStaticLayout;
        if (isExpanded() || isCollapsed()) {
            return;
        }
        boolean z10 = true;
        if (getMaxLines() > 1 || (staticLayout = this.mTextLayout) == null) {
            return;
        }
        CharSequence text = staticLayout.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (!text.toString().contains(ELLIPSIS_TWO_DOTS) && !text.toString().contains(ELLIPSIS_NORMAL)) {
            z10 = false;
        }
        if (!z10 || (createStaticLayout = createStaticLayout()) == null) {
            return;
        }
        setReflectField(this.collapsingTextHelper.getClass(), this.collapsingTextHelper, "textLayout", createStaticLayout);
        this.mTextLayout = createStaticLayout;
    }

    @Override // com.google.android.material.appbar.CollapsingToolbarLayout, android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public CollapsingTextHelper getCollapsingTextHelper() {
        return this.collapsingTextHelper;
    }

    public StaticLayout getCollapsingTextLayout() {
        return this.mTextLayout;
    }

    public TextPaint getCollapsingTextPaint() {
        return this.mTextPaint;
    }

    public View getIconView() {
        return this.mIconView;
    }

    public boolean isCollapsed() {
        return this.collapsingTextHelper.getExpansionFraction() == 1.0f;
    }

    public boolean isExpanded() {
        return this.collapsingTextHelper.getExpansionFraction() == 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.CollapsingToolbarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        bringChildToFront(this.mIconView);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.CollapsingToolbarLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        updateTextLayoutIfNeed();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.CollapsingToolbarLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        this.mTextLayout = getTextLayout();
        if (this.mCurrentBounds.isEmpty()) {
            this.mCurrentBounds = getTextHelperCurrentBounds();
        }
        if (this.mCollapsedBounds.isEmpty()) {
            this.mCollapsedBounds = getTextHelperCollapsedBounds();
        }
        if (this.mExpandedBounds.isEmpty()) {
            this.mExpandedBounds = getTextHelperExpandedBounds();
        }
        translateTitleIfNeed();
        View view = this.mIconView;
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        if (calculateIsRtl(this.mTextLayout.getText())) {
            this.mCollapsedBounds.left += this.mIconView.getMeasuredWidth();
            this.mExpandedBounds.left += this.mIconView.getMeasuredWidth();
        } else {
            this.mCollapsedBounds.right -= this.mIconView.getMeasuredWidth();
            this.mExpandedBounds.right -= this.mIconView.getMeasuredWidth();
        }
        this.collapsingTextHelper.recalculate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.CollapsingToolbarLayout, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (COUIResponsiveUtils.i(getContext(), getMeasuredWidth())) {
            this.mToolbarNormalPaddingStart = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_start_compat);
            this.mToolbarNormalPaddingEnd = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_end_compat);
        } else if (COUIResponsiveUtils.h(getContext(), getMeasuredWidth())) {
            this.mToolbarNormalPaddingStart = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_start_medium);
            this.mToolbarNormalPaddingEnd = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_end_medium);
        } else if (COUIResponsiveUtils.g(getContext(), getMeasuredWidth())) {
            this.mToolbarNormalPaddingStart = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_start_expanded);
            this.mToolbarNormalPaddingEnd = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_end_expanded);
        }
        setExpandedTitleMarginStart(this.mToolbarNormalPaddingStart);
        setExpandedTitleMarginEnd(this.mToolbarNormalPaddingEnd);
    }

    public void resetTextHelperInterpolator() {
        CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
        PathInterpolator pathInterpolator = TITLE_PATH_INTERPOLATOR;
        collapsingTextHelper.setTextSizeInterpolator(pathInterpolator);
        this.collapsingTextHelper.setPositionInterpolator(pathInterpolator);
    }

    public void setIconView(int i10) {
        setIconView(i10 == 0 ? null : LayoutInflater.from(getContext()).inflate(i10, (ViewGroup) this, false));
    }

    @Override // com.google.android.material.appbar.CollapsingToolbarLayout
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        post(new Runnable() { // from class: com.google.android.material.appbar.COUICollapsingToolbarLayout.2
            @Override // java.lang.Runnable
            public void run() {
                COUICollapsingToolbarLayout.this.updateIconViewLocationIfNeed();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateIconViewLocationIfNeed() {
        View view;
        float f10;
        StaticLayout staticLayout = this.mTextLayout;
        if (staticLayout == null) {
            return;
        }
        CharSequence text = staticLayout.getText();
        if (TextUtils.isEmpty(text) || (view = this.mIconView) == null || view.getVisibility() != 0) {
            return;
        }
        boolean z10 = text.toString().contains(ELLIPSIS_TWO_DOTS) || text.toString().contains(ELLIPSIS_NORMAL);
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        float scale = getScale();
        boolean calculateIsRtl = calculateIsRtl(text);
        int i10 = (int) ((fontMetricsInt.descent - fontMetricsInt.ascent) * scale);
        int measuredHeight = (int) (((getMeasuredHeight() - this.mCurrentBounds.top) - i10) + (this.mTitleTranslateOffset * this.collapsingTextHelper.getExpansionFraction()));
        int measuredWidth = (getMeasuredWidth() - this.mIconView.getMeasuredWidth()) - getExpandedTitleMarginEnd();
        if (z10) {
            if (!isExpanded()) {
                measuredWidth = (int) (calculateIsRtl ? this.mCurrentBounds.width() + getExpandedTitleMarginStart() : this.mCurrentBounds.right);
            }
        } else {
            int lineWidth = (int) (this.mTextLayout.getLineWidth(0) * scale);
            if (calculateIsRtl) {
                f10 = getMeasuredWidth() - this.mCurrentBounds.right;
            } else {
                f10 = this.mCurrentBounds.left;
            }
            measuredWidth = Math.min((int) (f10 + lineWidth), measuredWidth);
        }
        ViewGroup.LayoutParams layoutParams = this.mIconView.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            layoutParams.height = i10;
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.bottomMargin = measuredHeight;
            marginLayoutParams.setMarginStart(measuredWidth);
            this.mIconView.setLayoutParams(layoutParams);
        }
    }

    public COUICollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICollapsingToolbarLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.mCollapsedSubtitleMarginTopIfNotHidden = 0;
        this.mToolbarNormalPaddingStart = 0;
        this.mToolbarNormalPaddingEnd = 0;
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
        this.mIconViewFirstLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.google.android.material.appbar.COUICollapsingToolbarLayout.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (COUICollapsingToolbarLayout.this.mIconView == null) {
                    return;
                }
                COUICollapsingToolbarLayout.this.mIconView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                COUICollapsingToolbarLayout.this.updateIconViewLocationIfNeed();
            }
        };
        init(attributeSet);
        this.mCollapsedSubtitleMarginTopIfNotHidden = context.getResources().getDimensionPixelOffset(R$dimen.coui_appbar_subtitle_collapsed_margin_top);
        this.mToolbarNormalPaddingStart = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_start);
        this.mToolbarNormalPaddingEnd = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_title_expanded_margin_end);
    }

    public void setIconView(View view) {
        View view2 = this.mIconView;
        if (view2 == view) {
            return;
        }
        if (view == null) {
            removeView(view2);
            this.mIconView = null;
        } else {
            this.mIconView = view;
            addIconView();
        }
    }
}
