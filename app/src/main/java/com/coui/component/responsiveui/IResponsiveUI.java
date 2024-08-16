package com.coui.component.responsiveui;

import android.content.Context;
import com.coui.component.responsiveui.layoutgrid.ILayoutGrid;
import com.coui.component.responsiveui.status.IWindowStatus;
import com.coui.component.responsiveui.window.LayoutGridWindowSize;
import kotlin.Metadata;

/* compiled from: IResponsiveUI.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u00012\u00020\u0002J\u0018\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H&J\u0018\u0010\t\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H&J\b\u0010\u000b\u001a\u00020\nH&J\b\u0010\f\u001a\u00020\nH&¨\u0006\r"}, d2 = {"Lcom/coui/component/responsiveui/IResponsiveUI;", "Lcom/coui/component/responsiveui/layoutgrid/ILayoutGrid;", "Lcom/coui/component/responsiveui/status/IWindowStatus;", "Landroid/content/Context;", "context", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "windowSize", "Lma/f0;", "rebuild", "onConfigurationChanged", "", "showWindowStatusInfo", "showLayoutGridInfo", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public interface IResponsiveUI extends ILayoutGrid, IWindowStatus {
    void onConfigurationChanged(Context context, LayoutGridWindowSize layoutGridWindowSize);

    void rebuild(Context context, LayoutGridWindowSize layoutGridWindowSize);

    String showLayoutGridInfo();

    String showWindowStatusInfo();
}
