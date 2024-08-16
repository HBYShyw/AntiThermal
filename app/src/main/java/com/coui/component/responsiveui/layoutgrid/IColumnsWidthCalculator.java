package com.coui.component.responsiveui.layoutgrid;

import com.coui.component.responsiveui.unit.Dp;
import kotlin.Metadata;

/* compiled from: IColumnsWidthCalculator.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0015\n\u0000\bf\u0018\u00002\u00020\u0001J3\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH&¢\u0006\u0002\u0010\nJ(\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\tH&¨\u0006\f"}, d2 = {"Lcom/coui/component/responsiveui/layoutgrid/IColumnsWidthCalculator;", "", "calculate", "", "Lcom/coui/component/responsiveui/unit/Dp;", "layoutGridWidth", "margin", "gutter", "columnCount", "", "(Lcom/coui/component/responsiveui/unit/Dp;Lcom/coui/component/responsiveui/unit/Dp;Lcom/coui/component/responsiveui/unit/Dp;I)[Lcom/coui/component/responsiveui/unit/Dp;", "", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public interface IColumnsWidthCalculator {
    int[] calculate(int layoutGridWidth, int margin, int gutter, int columnCount);

    Dp[] calculate(Dp layoutGridWidth, Dp margin, Dp gutter, int columnCount);
}
