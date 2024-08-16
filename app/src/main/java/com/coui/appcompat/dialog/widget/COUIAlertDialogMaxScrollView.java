package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIAlertDialogMaxScrollView extends ScrollView {

    /* renamed from: e, reason: collision with root package name */
    private int f5674e;

    /* renamed from: f, reason: collision with root package name */
    private int f5675f;

    /* renamed from: g, reason: collision with root package name */
    private a f5676g;

    /* loaded from: classes.dex */
    public interface a {
        void a(int i10, int i11, int i12, int i13);
    }

    public COUIAlertDialogMaxScrollView(Context context) {
        super(context);
    }

    public int getMaxWidth() {
        return this.f5674e;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        boolean z10;
        super.onMeasure(i10, i11);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int i12 = this.f5674e;
        boolean z11 = true;
        if (i12 == 0 || measuredWidth <= i12) {
            z10 = false;
        } else {
            i10 = View.MeasureSpec.makeMeasureSpec(i12, 1073741824);
            z10 = true;
        }
        int i13 = this.f5675f;
        if (measuredHeight > i13) {
            i11 = View.MeasureSpec.makeMeasureSpec(i13, 1073741824);
        } else {
            z11 = z10;
        }
        if (z11) {
            super.onMeasure(i10, i11);
        }
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        a aVar = this.f5676g;
        if (aVar != null) {
            aVar.a(i10, i11, i12, i13);
        }
    }

    public void setMaxHeight(int i10) {
        this.f5675f = i10;
    }

    public void setMaxWidth(int i10) {
        this.f5674e = i10;
    }

    public void setOnSizeChangeListener(a aVar) {
        this.f5676g = aVar;
    }

    public COUIAlertDialogMaxScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIAlertDialogMaxLinearLayout);
        this.f5674e = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIAlertDialogMaxLinearLayout_maxWidth, 0);
        this.f5675f = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIAlertDialogMaxLinearLayout_maxHeight, 0);
        obtainStyledAttributes.recycle();
    }
}
