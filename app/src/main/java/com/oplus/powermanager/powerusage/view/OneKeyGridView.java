package com.oplus.powermanager.powerusage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import b6.LocalLog;

/* loaded from: classes2.dex */
public class OneKeyGridView extends GridView {

    /* renamed from: e, reason: collision with root package name */
    private int f10396e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f10397f;

    public OneKeyGridView(Context context) {
        super(context);
        this.f10396e = 6;
        this.f10397f = true;
        setNumColumns(6);
    }

    public void a() {
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() > 0) {
            int min = Math.min(adapter.getCount(), this.f10396e);
            setNumColumns(min);
            int requestedColumnWidth = getRequestedColumnWidth();
            int requestedHorizontalSpacing = getRequestedHorizontalSpacing();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            LocalLog.a("OneKeyGridView", "setGridWidth: iconWidth=" + requestedColumnWidth + " spacing=" + requestedHorizontalSpacing + " cols=" + min + " mFirstVisible=" + this.f10397f);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("setGridWidth: set=");
            int i10 = ((requestedColumnWidth + requestedHorizontalSpacing) * min) - requestedHorizontalSpacing;
            sb2.append(i10);
            sb2.append(" oldWidth=");
            sb2.append(layoutParams.width);
            LocalLog.a("OneKeyGridView", sb2.toString());
            if (i10 != layoutParams.width) {
                layoutParams.width = i10;
                setLayoutParams(layoutParams);
                return;
            }
            return;
        }
        LocalLog.b("OneKeyGridView", "adapter is null or size 0");
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        a();
    }

    public void setMaxColumns(int i10) {
        this.f10396e = i10;
    }

    public OneKeyGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10396e = 6;
        this.f10397f = true;
    }

    public OneKeyGridView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f10396e = 6;
        this.f10397f = true;
    }
}
