package com.coui.component.responsiveui.layoutgrid;

import kotlin.Metadata;

/* compiled from: ILayoutGrid.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\u0013\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H&J\u0010\u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH&J\b\u0010\f\u001a\u00020\u0004H&J\b\u0010\r\u001a\u00020\u000bH&J\b\u0010\u000e\u001a\u00020\u000bH&J\b\u0010\u000f\u001a\u00020\u000bH&J\u0018\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000bH&¨\u0006\u0013"}, d2 = {"Lcom/coui/component/responsiveui/layoutgrid/ILayoutGrid;", "", "allColumnWidth", "", "", "()[[I", "allMargin", "chooseMargin", "marginType", "Lcom/coui/component/responsiveui/layoutgrid/MarginType;", "columnCount", "", "columnWidth", "gutter", "layoutGridWindowWidth", "margin", "width", "fromColumnIndex", "toColumnIndex", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public interface ILayoutGrid {
    int[][] allColumnWidth();

    int[] allMargin();

    ILayoutGrid chooseMargin(MarginType marginType);

    int columnCount();

    int[] columnWidth();

    int gutter();

    int layoutGridWindowWidth();

    int margin();

    int width(int fromColumnIndex, int toColumnIndex);
}
