package com.coui.component.responsiveui.layoutgrid;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.collections.Arrays;
import kotlin.collections.ArraysJVM;
import kotlin.collections._ArraysJvm;
import sd.v;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LayoutGridSystem.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0015\n\u0002\b!\b\u0082\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0010\u001a\u00020\u0005\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0010\u0012\u001a\u00020\u0005\u0012\u0006\u0010\u0013\u001a\u00020\u000b¢\u0006\u0004\b*\u0010+J\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\t\u0010\t\u001a\u00020\u0005HÆ\u0003J\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nHÆ\u0003¢\u0006\u0004\b\f\u0010\rJ\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\t\u0010\u000f\u001a\u00020\u000bHÆ\u0003J>\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0010\u001a\u00020\u00052\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\b\b\u0002\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u000bHÆ\u0001¢\u0006\u0004\b\u0014\u0010\u0015R\"\u0010\u0010\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR(\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000b0\n8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\r\"\u0004\b\u001f\u0010 R\"\u0010\u0012\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b!\u0010\u0017\u001a\u0004\b\"\u0010\u0019\"\u0004\b#\u0010\u001bR\"\u0010\u0013\u001a\u00020\u000b8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b$\u0010%\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)¨\u0006,"}, d2 = {"Lcom/coui/component/responsiveui/layoutgrid/LayoutGrid;", "", "other", "", "equals", "", "hashCode", "", "toString", "component1", "", "", "component2", "()[[I", "component3", "component4", "columnCount", "columnsWidth", "gutter", "margin", "copy", "(I[[II[I)Lcom/coui/component/responsiveui/layoutgrid/LayoutGrid;", "a", "I", "getColumnCount", "()I", "setColumnCount", "(I)V", "b", "[[I", "getColumnsWidth", "setColumnsWidth", "([[I)V", "c", "getGutter", "setGutter", "d", "[I", "getMargin", "()[I", "setMargin", "([I)V", "<init>", "(I[[II[I)V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final /* data */ class LayoutGrid {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private int columnCount;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private int[][] columnsWidth;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private int gutter;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private int[] margin;

    public LayoutGrid(int i10, int[][] iArr, int i11, int[] iArr2) {
        k.e(iArr, "columnsWidth");
        k.e(iArr2, "margin");
        this.columnCount = i10;
        this.columnsWidth = iArr;
        this.gutter = i11;
        this.margin = iArr2;
    }

    public static /* synthetic */ LayoutGrid copy$default(LayoutGrid layoutGrid, int i10, int[][] iArr, int i11, int[] iArr2, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = layoutGrid.columnCount;
        }
        if ((i12 & 2) != 0) {
            iArr = layoutGrid.columnsWidth;
        }
        if ((i12 & 4) != 0) {
            i11 = layoutGrid.gutter;
        }
        if ((i12 & 8) != 0) {
            iArr2 = layoutGrid.margin;
        }
        return layoutGrid.copy(i10, iArr, i11, iArr2);
    }

    /* renamed from: component1, reason: from getter */
    public final int getColumnCount() {
        return this.columnCount;
    }

    /* renamed from: component2, reason: from getter */
    public final int[][] getColumnsWidth() {
        return this.columnsWidth;
    }

    /* renamed from: component3, reason: from getter */
    public final int getGutter() {
        return this.gutter;
    }

    /* renamed from: component4, reason: from getter */
    public final int[] getMargin() {
        return this.margin;
    }

    public final LayoutGrid copy(int columnCount, int[][] columnsWidth, int gutter, int[] margin) {
        k.e(columnsWidth, "columnsWidth");
        k.e(margin, "margin");
        return new LayoutGrid(columnCount, columnsWidth, gutter, margin);
    }

    public boolean equals(Object other) {
        boolean c10;
        if (this == other) {
            return true;
        }
        if (!k.a(LayoutGrid.class, other == null ? null : other.getClass())) {
            return false;
        }
        Objects.requireNonNull(other, "null cannot be cast to non-null type com.coui.component.responsiveui.layoutgrid.LayoutGrid");
        LayoutGrid layoutGrid = (LayoutGrid) other;
        if (this.columnCount != layoutGrid.columnCount) {
            return false;
        }
        c10 = Arrays.c(this.columnsWidth, layoutGrid.columnsWidth);
        return c10 && this.gutter == layoutGrid.gutter && java.util.Arrays.equals(this.margin, layoutGrid.margin);
    }

    public final int getColumnCount() {
        return this.columnCount;
    }

    public final int[][] getColumnsWidth() {
        return this.columnsWidth;
    }

    public final int getGutter() {
        return this.gutter;
    }

    public final int[] getMargin() {
        return this.margin;
    }

    public int hashCode() {
        int a10;
        int i10 = this.columnCount * 31;
        a10 = ArraysJVM.a(this.columnsWidth);
        return ((((i10 + a10) * 31) + this.gutter) * 31) + java.util.Arrays.hashCode(this.margin);
    }

    public final void setColumnCount(int i10) {
        this.columnCount = i10;
    }

    public final void setColumnsWidth(int[][] iArr) {
        k.e(iArr, "<set-?>");
        this.columnsWidth = iArr;
    }

    public final void setGutter(int i10) {
        this.gutter = i10;
    }

    public final void setMargin(int[] iArr) {
        k.e(iArr, "<set-?>");
        this.margin = iArr;
    }

    public String toString() {
        List d10;
        int P;
        int P2;
        List d11;
        StringBuffer stringBuffer = new StringBuffer("[LayoutGrid] columnCount = " + this.columnCount + ", ");
        stringBuffer.append("gutter = " + this.gutter + ", ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("margins = ");
        d10 = _ArraysJvm.d(this.margin);
        sb2.append(d10);
        sb2.append(", ");
        stringBuffer.append(sb2.toString());
        stringBuffer.append("columnWidth = [");
        for (int[] iArr : this.columnsWidth) {
            d11 = _ArraysJvm.d(iArr);
            stringBuffer.append(d11.toString());
            stringBuffer.append(", ");
        }
        k.d(stringBuffer, ThermalBaseConfig.Item.ATTR_VALUE);
        P = v.P(stringBuffer);
        P2 = v.P(stringBuffer);
        stringBuffer.delete(P - 1, P2 + 1);
        stringBuffer.append("]");
        String stringBuffer2 = stringBuffer.toString();
        k.d(stringBuffer2, "value.toString()");
        return stringBuffer2;
    }
}
