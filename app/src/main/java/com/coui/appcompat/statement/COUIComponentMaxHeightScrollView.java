package com.coui.appcompat.statement;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.support.component.R$dimen;
import fb._Ranges;
import kotlin.Metadata;
import za.k;

/* compiled from: COUIComponentMaxHeightScrollView.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0014R\"\u0010\n\u001a\u00020\u00078\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r¨\u0006\u0014"}, d2 = {"Lcom/coui/appcompat/statement/COUIComponentMaxHeightScrollView;", "Lcom/coui/appcompat/statement/COUIMaxHeightScrollView;", "", "widthMeasureSpec", "heightMeasureSpec", "Lma/f0;", "onMeasure", "", "P", "Z", "isProtocolFixed", "()Z", "setProtocolFixed", "(Z)V", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUIComponentMaxHeightScrollView extends COUIMaxHeightScrollView {

    /* renamed from: P, reason: from kotlin metadata */
    private boolean isProtocolFixed;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIComponentMaxHeightScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        k.e(context, "context");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.statement.COUIMaxHeightScrollView, com.coui.appcompat.scrollview.COUIScrollView, android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int f10;
        if (getMaxHeight() > 0) {
            f10 = _Ranges.f(getMaxHeight(), View.MeasureSpec.getSize(i11));
            i11 = View.MeasureSpec.makeMeasureSpec(f10, Integer.MIN_VALUE);
        }
        if (this.isProtocolFixed && getChildCount() > 0) {
            measureChild(getChildAt(0), i10, i11);
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getChildAt(0).getMeasuredHeight() > View.MeasureSpec.getSize(i11) - getPaddingTop() ? getContext().getResources().getDimensionPixelOffset(R$dimen.coui_component_bottom_sheet_margin) : 0);
        }
        super.onMeasure(i10, i11);
    }

    public final void setProtocolFixed(boolean z10) {
        this.isProtocolFixed = z10;
    }
}
