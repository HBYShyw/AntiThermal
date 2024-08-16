package com.coui.appcompat.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import f2.COUIPanelMultiWindowUtils;

/* loaded from: classes.dex */
public class COUIMaxHeightDraggableVerticalLinearLayout extends COUIDraggableVerticalLinearLayout {

    /* renamed from: o, reason: collision with root package name */
    private int f6595o;

    public COUIMaxHeightDraggableVerticalLinearLayout(Context context) {
        super(context);
    }

    public int getMaxHeight() {
        return COUIPanelMultiWindowUtils.h(getContext(), null);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        if (this.f6595o == 8 && i10 == 0) {
            measure(View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getHeight(), 1073741824));
        }
        this.f6595o = i10;
        super.onWindowVisibilityChanged(i10);
    }

    public COUIMaxHeightDraggableVerticalLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUIMaxHeightDraggableVerticalLinearLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
