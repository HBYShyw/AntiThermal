package com.coui.component.responsiveui.layoutgrid;

import com.support.responsive.R$dimen;
import java.util.Arrays;
import kotlin.Metadata;

/* compiled from: LayoutGridSystem.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B'\b\u0002\u0012\b\b\u0001\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0001\u0010\b\u001a\u00020\u0006\u0012\b\b\u0001\u0010\t\u001a\u00020\u0006¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005j\u0002\b\fj\u0002\b\r¨\u0006\u000e"}, d2 = {"Lcom/coui/component/responsiveui/layoutgrid/MarginType;", "", "", "resId", "e", "[I", "", "compatId", "mediumId", "expandedId", "<init>", "(Ljava/lang/String;IIII)V", "MARGIN_SMALL", "MARGIN_LARGE", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public enum MarginType {
    MARGIN_SMALL(R$dimen.layout_grid_margin_compat_small, R$dimen.layout_grid_margin_medium_small, R$dimen.layout_grid_margin_expanded_small),
    MARGIN_LARGE(R$dimen.layout_grid_margin_compat_large, R$dimen.layout_grid_margin_medium_large, R$dimen.layout_grid_margin_expanded_large);


    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private final int[] resId;

    MarginType(int i10, int i11, int i12) {
        this.resId = new int[]{i10, i11, i12};
    }

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static MarginType[] valuesCustom() {
        MarginType[] valuesCustom = values();
        return (MarginType[]) Arrays.copyOf(valuesCustom, valuesCustom.length);
    }

    /* renamed from: resId, reason: from getter */
    public final int[] getResId() {
        return this.resId;
    }
}
