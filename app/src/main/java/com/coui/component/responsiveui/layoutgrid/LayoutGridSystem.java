package com.coui.component.responsiveui.layoutgrid;

import android.content.Context;
import android.util.Log;
import com.coui.component.responsiveui.unit.Dp;
import com.coui.component.responsiveui.unit.DpKt;
import com.coui.component.responsiveui.window.WindowSizeClass;
import com.coui.component.responsiveui.window.WindowWidthSizeClass;
import com.support.responsive.R$dimen;
import fb._Ranges;
import kotlin.Metadata;
import za.k;

/* compiled from: LayoutGridSystem.kt */
@Metadata(bv = {}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0011\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u0000 82\u00020\u0001:\u00018B\u001f\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u001c\u001a\u00020\u001b\u0012\u0006\u0010\u001d\u001a\u00020\n¢\u0006\u0004\b6\u00107J \u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\f\u001a\u00020\nH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016J\u0015\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0016¢\u0006\u0004\b\u0010\u0010\u0011J\b\u0010\u0012\u001a\u00020\rH\u0016J\b\u0010\u0013\u001a\u00020\nH\u0016J\b\u0010\u0014\u001a\u00020\nH\u0016J\u0018\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nH\u0016J\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u0018H\u0016J\u001e\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\nJ\b\u0010!\u001a\u00020 H\u0016R\u0014\u0010#\u001a\u00020\r8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\t\u0010\"R\u0016\u0010&\u001a\u00020\n8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b$\u0010%R\u0016\u0010(\u001a\u00020\n8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b'\u0010%R\u0016\u0010+\u001a\u00020\b8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b)\u0010*R\u0016\u0010.\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b,\u0010-R\u0016\u00101\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b/\u00100R\u0016\u00105\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b3\u00104¨\u00069"}, d2 = {"Lcom/coui/component/responsiveui/layoutgrid/LayoutGridSystem;", "Lcom/coui/component/responsiveui/layoutgrid/ILayoutGrid;", "Landroid/content/Context;", "context", "Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "windowWidthSizeClass", "Lcom/coui/component/responsiveui/layoutgrid/IColumnsWidthCalculator;", "calculator", "Lcom/coui/component/responsiveui/layoutgrid/LayoutGrid;", "a", "", "layoutGridWindowWidth", "columnCount", "", "columnWidth", "", "allColumnWidth", "()[[I", "allMargin", "margin", "gutter", "fromColumnIndex", "toColumnIndex", "width", "Lcom/coui/component/responsiveui/layoutgrid/MarginType;", "marginType", "chooseMargin", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "windowSizeClass", "windowWidth", "Lma/f0;", "rebuild", "", "toString", "[I", "marginPixel", "b", "I", "gutterPixel", "c", "layoutGridWidthPixel", "d", "Lcom/coui/component/responsiveui/layoutgrid/LayoutGrid;", "layoutGrid", "e", "Lcom/coui/component/responsiveui/layoutgrid/MarginType;", "selectedMargin", "f", "Lcom/coui/component/responsiveui/layoutgrid/IColumnsWidthCalculator;", "columnsWidthCalculator", "", "g", "Z", "layoutGridCalculateWithDp", "<init>", "(Landroid/content/Context;Lcom/coui/component/responsiveui/window/WindowSizeClass;I)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class LayoutGridSystem implements ILayoutGrid {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final int[] marginPixel;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private int gutterPixel;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private int layoutGridWidthPixel;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private LayoutGrid layoutGrid;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private MarginType selectedMargin;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private IColumnsWidthCalculator columnsWidthCalculator;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private boolean layoutGridCalculateWithDp;

    public LayoutGridSystem(Context context, WindowSizeClass windowSizeClass, int i10) {
        k.e(context, "context");
        k.e(windowSizeClass, "windowSizeClass");
        this.marginPixel = new int[MarginType.valuesCustom().length];
        this.selectedMargin = MarginType.MARGIN_LARGE;
        this.columnsWidthCalculator = new AccumulationCalculator();
        rebuild(context, windowSizeClass, i10);
    }

    private final LayoutGrid a(Context context, WindowWidthSizeClass windowWidthSizeClass, IColumnsWidthCalculator calculator) {
        int i10;
        if (k.a(windowWidthSizeClass, WindowWidthSizeClass.Compact)) {
            i10 = 4;
        } else {
            i10 = k.a(windowWidthSizeClass, WindowWidthSizeClass.Medium) ? 8 : 12;
        }
        MarginType[] valuesCustom = MarginType.valuesCustom();
        int length = valuesCustom.length;
        int[][] iArr = new int[length];
        for (int i11 = 0; i11 < length; i11++) {
            iArr[i11] = new int[i10];
        }
        if (this.layoutGridCalculateWithDp) {
            Dp pixel2Dp = DpKt.pixel2Dp(this.layoutGridWidthPixel, context);
            int length2 = valuesCustom.length;
            Dp[] dpArr = new Dp[length2];
            for (int i12 = 0; i12 < length2; i12++) {
                dpArr[i12] = DpKt.pixel2Dp(this.marginPixel[i12], context);
            }
            Dp pixel2Dp2 = DpKt.pixel2Dp(this.gutterPixel, context);
            Dp[][] dpArr2 = new Dp[length2];
            for (int i13 = 0; i13 < length2; i13++) {
                dpArr2[i13] = calculator.calculate(pixel2Dp, dpArr[i13], pixel2Dp2, i10);
            }
            for (MarginType marginType : valuesCustom) {
                if (i10 > 0) {
                    int i14 = 0;
                    while (true) {
                        int i15 = i14 + 1;
                        iArr[marginType.ordinal()][i14] = (int) dpArr2[marginType.ordinal()][i14].toPixel(context);
                        if (i15 >= i10) {
                            break;
                        }
                        i14 = i15;
                    }
                }
            }
        } else {
            for (MarginType marginType2 : valuesCustom) {
                iArr[marginType2.ordinal()] = calculator.calculate(this.layoutGridWidthPixel, this.marginPixel[marginType2.ordinal()], this.gutterPixel, i10);
            }
        }
        LayoutGrid layoutGrid = new LayoutGrid(i10, iArr, this.gutterPixel, this.marginPixel);
        Log.d("LayoutGridSystem", "[calculateLayoutGrid] widthSizeClass: " + windowWidthSizeClass + ", layoutGridWindowWidth: " + this.layoutGridWidthPixel + ", " + layoutGrid);
        return layoutGrid;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int[][] allColumnWidth() {
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            return layoutGrid.getColumnsWidth();
        }
        k.s("layoutGrid");
        throw null;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int[] allMargin() {
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            return layoutGrid.getMargin();
        }
        k.s("layoutGrid");
        throw null;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public ILayoutGrid chooseMargin(MarginType marginType) {
        k.e(marginType, "marginType");
        this.selectedMargin = marginType;
        return this;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int columnCount() {
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            return layoutGrid.getColumnCount();
        }
        k.s("layoutGrid");
        throw null;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int[] columnWidth() {
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            return layoutGrid.getColumnsWidth()[this.selectedMargin.ordinal()];
        }
        k.s("layoutGrid");
        throw null;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int gutter() {
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            return layoutGrid.getGutter();
        }
        k.s("layoutGrid");
        throw null;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    /* renamed from: layoutGridWindowWidth, reason: from getter */
    public int getLayoutGridWidthPixel() {
        return this.layoutGridWidthPixel;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int margin() {
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            return layoutGrid.getMargin()[this.selectedMargin.ordinal()];
        }
        k.s("layoutGrid");
        throw null;
    }

    public final void rebuild(Context context, WindowSizeClass windowSizeClass, int i10) {
        int dimensionPixelSize;
        k.e(context, "context");
        k.e(windowSizeClass, "windowSizeClass");
        for (MarginType marginType : MarginType.valuesCustom()) {
            int[] iArr = this.marginPixel;
            int ordinal = marginType.ordinal();
            WindowWidthSizeClass windowWidthSizeClass = windowSizeClass.getWindowWidthSizeClass();
            if (k.a(windowWidthSizeClass, WindowWidthSizeClass.Compact)) {
                dimensionPixelSize = context.getResources().getDimensionPixelSize(marginType.getResId()[0]);
            } else {
                dimensionPixelSize = k.a(windowWidthSizeClass, WindowWidthSizeClass.Medium) ? context.getResources().getDimensionPixelSize(marginType.getResId()[1]) : context.getResources().getDimensionPixelSize(marginType.getResId()[2]);
            }
            iArr[ordinal] = dimensionPixelSize;
        }
        this.gutterPixel = context.getResources().getDimensionPixelSize(R$dimen.layout_grid_gutter);
        this.layoutGridWidthPixel = i10;
        this.layoutGrid = a(context, windowSizeClass.getWindowWidthSizeClass(), this.columnsWidthCalculator);
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("layout-grid width = ");
        sb2.append(this.layoutGridWidthPixel);
        sb2.append(", current margin = ");
        sb2.append(margin());
        sb2.append(", ");
        LayoutGrid layoutGrid = this.layoutGrid;
        if (layoutGrid != null) {
            sb2.append(layoutGrid);
            return sb2.toString();
        }
        k.s("layoutGrid");
        throw null;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int width(int fromColumnIndex, int toColumnIndex) {
        int f10;
        int c10;
        f10 = _Ranges.f(fromColumnIndex, toColumnIndex);
        c10 = _Ranges.c(fromColumnIndex, toColumnIndex);
        if (f10 >= 0) {
            LayoutGrid layoutGrid = this.layoutGrid;
            if (layoutGrid == null) {
                k.s("layoutGrid");
                throw null;
            }
            if (!(c10 < layoutGrid.getColumnCount())) {
                LayoutGrid layoutGrid2 = this.layoutGrid;
                if (layoutGrid2 != null) {
                    throw new IllegalArgumentException(k.l("column index must be less than ", Integer.valueOf(layoutGrid2.getColumnCount())));
                }
                k.s("layoutGrid");
                throw null;
            }
            int i10 = c10 - f10;
            LayoutGrid layoutGrid3 = this.layoutGrid;
            if (layoutGrid3 == null) {
                k.s("layoutGrid");
                throw null;
            }
            int gutter = i10 * layoutGrid3.getGutter();
            if (f10 <= c10) {
                while (true) {
                    int i11 = f10 + 1;
                    LayoutGrid layoutGrid4 = this.layoutGrid;
                    if (layoutGrid4 == null) {
                        k.s("layoutGrid");
                        throw null;
                    }
                    gutter += layoutGrid4.getColumnsWidth()[this.selectedMargin.ordinal()][f10];
                    if (f10 == c10) {
                        break;
                    }
                    f10 = i11;
                }
            }
            return gutter;
        }
        throw new IllegalArgumentException("column index must not be negative");
    }
}
