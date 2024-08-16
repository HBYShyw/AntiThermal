package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import com.coui.appcompat.scrollview.COUINestedScrollView;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIMaxHeightNestedScrollView extends COUINestedScrollView {

    /* renamed from: v0, reason: collision with root package name */
    private int f5677v0;

    /* renamed from: w0, reason: collision with root package name */
    private int f5678w0;

    /* renamed from: x0, reason: collision with root package name */
    private a f5679x0;

    /* loaded from: classes.dex */
    public interface a {
        void a();
    }

    public COUIMaxHeightNestedScrollView(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        a aVar = this.f5679x0;
        if (aVar != null) {
            aVar.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int size = View.MeasureSpec.getSize(i11);
        if (this.f5677v0 > 0) {
            int i12 = this.f5678w0;
            if (i12 > 0) {
                size = Math.max(size, i12);
            }
            i11 = View.MeasureSpec.makeMeasureSpec(Math.min(this.f5677v0, size), Integer.MIN_VALUE);
        }
        super.onMeasure(i10, i11);
    }

    public void setConfigChangeListener(a aVar) {
        this.f5679x0 = aVar;
    }

    public void setMaxHeight(int i10) {
        this.f5677v0 = i10;
        requestLayout();
    }

    public void setMinHeight(int i10) {
        this.f5678w0 = i10;
        requestLayout();
    }

    public COUIMaxHeightNestedScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIMaxHeightNestedScrollView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIMaxHeightScrollView);
        this.f5677v0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIMaxHeightScrollView_scrollViewMaxHeight, 0);
        this.f5678w0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIMaxHeightScrollView_scrollViewMinHeight, 0);
        obtainStyledAttributes.recycle();
    }
}
