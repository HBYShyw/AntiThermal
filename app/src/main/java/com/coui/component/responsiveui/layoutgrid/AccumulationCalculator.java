package com.coui.component.responsiveui.layoutgrid;

import bb.MathJVM;
import com.coui.component.responsiveui.unit.Dp;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import kotlin.Metadata;
import kotlin.collections._Arrays;
import za.k;

/* compiled from: AccumulationCalculator.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0015\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J3\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016¢\u0006\u0002\u0010\u000bJ(\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\n2\u0006\u0010\b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\r"}, d2 = {"Lcom/coui/component/responsiveui/layoutgrid/AccumulationCalculator;", "Lcom/coui/component/responsiveui/layoutgrid/IColumnsWidthCalculator;", "()V", "calculate", "", "Lcom/coui/component/responsiveui/unit/Dp;", "layoutGridWidth", "margin", "gutter", "columnCount", "", "(Lcom/coui/component/responsiveui/unit/Dp;Lcom/coui/component/responsiveui/unit/Dp;Lcom/coui/component/responsiveui/unit/Dp;I)[Lcom/coui/component/responsiveui/unit/Dp;", "", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public final class AccumulationCalculator implements IColumnsWidthCalculator {
    @Override // com.coui.component.responsiveui.layoutgrid.IColumnsWidthCalculator
    public Dp[] calculate(Dp layoutGridWidth, Dp margin, Dp gutter, int columnCount) {
        int D;
        int a10;
        k.e(layoutGridWidth, "layoutGridWidth");
        k.e(margin, "margin");
        k.e(gutter, "gutter");
        Dp[] dpArr = new Dp[columnCount];
        int i10 = 0;
        for (int i11 = 0; i11 < columnCount; i11++) {
            dpArr[i11] = new Dp(0.0f);
        }
        float f10 = columnCount - 1;
        if ((gutter.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() * f10) + (margin.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() * 2.0d) > layoutGridWidth.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String()) {
            return dpArr;
        }
        double d10 = ((layoutGridWidth.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() - (margin.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() * 2.0d)) - (f10 * gutter.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String())) / columnCount;
        double d11 = UserProfileInfo.Constant.NA_LAT_LON;
        D = _Arrays.D(dpArr);
        if (D >= 0) {
            while (true) {
                int i12 = i10 + 1;
                a10 = MathJVM.a((i12 * d10) - d11);
                dpArr[i10] = new Dp(a10);
                d11 += a10;
                if (i10 == D) {
                    break;
                }
                i10 = i12;
            }
        }
        return dpArr;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.IColumnsWidthCalculator
    public int[] calculate(int layoutGridWidth, int margin, int gutter, int columnCount) {
        int C;
        int a10;
        int[] iArr = new int[columnCount];
        double d10 = (columnCount - 1) * gutter;
        double d11 = margin * 2.0d;
        double d12 = layoutGridWidth;
        if (d10 + d11 > d12) {
            return iArr;
        }
        double d13 = ((d12 - d11) - d10) / columnCount;
        double d14 = UserProfileInfo.Constant.NA_LAT_LON;
        int i10 = 0;
        C = _Arrays.C(iArr);
        if (C >= 0) {
            while (true) {
                int i11 = i10 + 1;
                a10 = MathJVM.a((i11 * d13) - d14);
                iArr[i10] = a10;
                d14 += a10;
                if (i10 == C) {
                    break;
                }
                i10 = i11;
            }
        }
        return iArr;
    }
}
