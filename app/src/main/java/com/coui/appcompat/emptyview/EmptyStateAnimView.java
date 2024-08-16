package com.coui.appcompat.emptyview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import w1.COUIDarkModeUtil;
import za.k;

/* compiled from: EmptyStateAnimView.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0011\u001a\u00020\u0010\u0012\u0006\u0010\u0013\u001a\u00020\u0012¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0014R*\u0010\u000f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00078\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u0016"}, d2 = {"Lcom/coui/appcompat/emptyview/EmptyStateAnimView;", "Lcom/oplus/anim/EffectiveAnimationView;", "", "widthMeasureSpec", "heightMeasureSpec", "Lma/f0;", "onMeasure", "Landroid/util/Size;", ThermalBaseConfig.Item.ATTR_VALUE, "C", "Landroid/util/Size;", "getAnimSize", "()Landroid/util/Size;", "setAnimSize", "(Landroid/util/Size;)V", "animSize", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class EmptyStateAnimView extends EffectiveAnimationView {

    /* renamed from: C, reason: from kotlin metadata */
    private Size animSize;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public EmptyStateAnimView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        k.e(context, "context");
        k.e(attributeSet, "attrs");
        COUIDarkModeUtil.b(this, false);
        this.animSize = new Size(0, 0);
    }

    public final Size getAnimSize() {
        return this.animSize;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i10, int i11) {
        setMeasuredDimension(View.MeasureSpec.makeMeasureSpec(this.animSize.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.animSize.getHeight(), 1073741824));
    }

    public final void setAnimSize(Size size) {
        k.e(size, ThermalBaseConfig.Item.ATTR_VALUE);
        this.animSize = size;
        setVisibility(size.getWidth() == 0 || size.getHeight() == 0 ? 4 : 0);
    }
}
