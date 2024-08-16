package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.coui.appcompat.statement.COUIMaxHeightScrollView;
import com.support.appcompat.R$id;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIAlertDialogMaxLinearLayout extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private int f5668e;

    /* renamed from: f, reason: collision with root package name */
    private int f5669f;

    /* renamed from: g, reason: collision with root package name */
    private a f5670g;

    /* renamed from: h, reason: collision with root package name */
    private int f5671h;

    /* renamed from: i, reason: collision with root package name */
    private int f5672i;

    /* renamed from: j, reason: collision with root package name */
    private int f5673j;

    /* loaded from: classes.dex */
    public interface a {
        void a(int i10, int i11, int i12, int i13);
    }

    public COUIAlertDialogMaxLinearLayout(Context context) {
        super(context);
        this.f5673j = -1;
    }

    public int getMaxWidth() {
        return this.f5668e;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int i12 = this.f5668e;
        if (i12 != 0 && measuredWidth > i12) {
            i10 = View.MeasureSpec.makeMeasureSpec(i12, 1073741824);
            super.onMeasure(i10, i11);
            measuredHeight = getMeasuredHeight();
        }
        int i13 = this.f5669f;
        if (measuredHeight > i13) {
            i11 = View.MeasureSpec.makeMeasureSpec(i13, 1073741824);
            super.onMeasure(i10, i11);
            measuredHeight = getMeasuredHeight();
        }
        if (this.f5673j == -1 || measuredHeight >= this.f5671h) {
            return;
        }
        COUIMaxHeightScrollView cOUIMaxHeightScrollView = (COUIMaxHeightScrollView) findViewById(R$id.alert_title_scroll_view);
        int minHeight = cOUIMaxHeightScrollView.getMinHeight();
        int i14 = this.f5672i;
        if (minHeight != i14) {
            cOUIMaxHeightScrollView.setMinHeight(i14);
            super.onMeasure(i10, i11);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        a aVar = this.f5670g;
        if (aVar != null) {
            aVar.a(i10, i11, i12, i13);
        }
    }

    public void setMaxHeight(int i10) {
        this.f5669f = i10;
    }

    public void setMaxWidth(int i10) {
        this.f5668e = i10;
    }

    public void setNeedMinHeight(int i10) {
        this.f5671h = i10;
    }

    public void setNeedReMeasureLayoutId(int i10) {
        this.f5673j = i10;
    }

    public void setOnSizeChangeListener(a aVar) {
        this.f5670g = aVar;
    }

    public void setScrollMinHeight(int i10) {
        this.f5672i = i10;
    }

    public COUIAlertDialogMaxLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5673j = -1;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIAlertDialogMaxLinearLayout);
        this.f5668e = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIAlertDialogMaxLinearLayout_maxWidth, 0);
        this.f5669f = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIAlertDialogMaxLinearLayout_maxHeight, 0);
        obtainStyledAttributes.recycle();
    }
}
