package com.coui.component.responsiveui.proxy;

import android.content.Context;
import android.util.Log;
import com.coui.component.responsiveui.IResponsiveUI;
import com.coui.component.responsiveui.layoutgrid.ILayoutGrid;
import com.coui.component.responsiveui.layoutgrid.LayoutGridSystem;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.coui.component.responsiveui.status.IWindowStatus;
import com.coui.component.responsiveui.status.WindowStatus;
import com.coui.component.responsiveui.unit.DpKt;
import com.coui.component.responsiveui.window.LayoutGridWindowSize;
import com.coui.component.responsiveui.window.WindowSizeClass;
import kotlin.Metadata;
import za.k;

/* compiled from: ResponsiveUIProxy.kt */
@Metadata(bv = {}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 -2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001-B\u0017\u0012\u0006\u0010*\u001a\u00020'\u0012\u0006\u0010&\u001a\u00020#¢\u0006\u0004\b+\u0010,J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0096\u0001¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\b\u001a\u00020\u0005H\u0096\u0001J\u0011\u0010\u000b\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0096\u0001J\t\u0010\r\u001a\u00020\fH\u0096\u0001J\t\u0010\u000e\u001a\u00020\u0005H\u0096\u0001J\t\u0010\u000f\u001a\u00020\fH\u0096\u0001J\t\u0010\u0010\u001a\u00020\fH\u0096\u0001J\t\u0010\u0011\u001a\u00020\fH\u0096\u0001J\u0019\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\fH\u0096\u0001J\t\u0010\u0016\u001a\u00020\u0015H\u0096\u0001J\t\u0010\u0017\u001a\u00020\fH\u0096\u0001J\t\u0010\u0019\u001a\u00020\u0018H\u0096\u0001J\u0018\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u0015H\u0016J\u0018\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u0015H\u0016J\b\u0010!\u001a\u00020 H\u0016J\b\u0010\"\u001a\u00020 H\u0016R\u0014\u0010&\u001a\u00020#8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b$\u0010%R\u0014\u0010*\u001a\u00020'8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b(\u0010)¨\u0006."}, d2 = {"Lcom/coui/component/responsiveui/proxy/ResponsiveUIProxy;", "Lcom/coui/component/responsiveui/IResponsiveUI;", "Lcom/coui/component/responsiveui/layoutgrid/ILayoutGrid;", "Lcom/coui/component/responsiveui/status/IWindowStatus;", "", "", "allColumnWidth", "()[[I", "allMargin", "Lcom/coui/component/responsiveui/layoutgrid/MarginType;", "marginType", "chooseMargin", "", "columnCount", "columnWidth", "gutter", "layoutGridWindowWidth", "margin", "fromColumnIndex", "toColumnIndex", "width", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "layoutGridWindowSize", "windowOrientation", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "windowSizeClass", "Landroid/content/Context;", "context", "windowSize", "Lma/f0;", "onConfigurationChanged", "rebuild", "", "showWindowStatusInfo", "showLayoutGridInfo", "Lcom/coui/component/responsiveui/status/WindowStatus;", "c", "Lcom/coui/component/responsiveui/status/WindowStatus;", "windowStatus", "Lcom/coui/component/responsiveui/layoutgrid/LayoutGridSystem;", "d", "Lcom/coui/component/responsiveui/layoutgrid/LayoutGridSystem;", "layoutGridSystem", "<init>", "(Lcom/coui/component/responsiveui/layoutgrid/LayoutGridSystem;Lcom/coui/component/responsiveui/status/WindowStatus;)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class ResponsiveUIProxy implements IResponsiveUI, ILayoutGrid, IWindowStatus {

    /* renamed from: a, reason: collision with root package name */
    private final /* synthetic */ LayoutGridSystem f8191a;

    /* renamed from: b, reason: collision with root package name */
    private final /* synthetic */ WindowStatus f8192b;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final WindowStatus windowStatus;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private final LayoutGridSystem layoutGridSystem;

    public ResponsiveUIProxy(LayoutGridSystem layoutGridSystem, WindowStatus windowStatus) {
        k.e(layoutGridSystem, "layoutGridSystem");
        k.e(windowStatus, "windowStatus");
        this.f8191a = layoutGridSystem;
        this.f8192b = windowStatus;
        this.windowStatus = windowStatus;
        this.layoutGridSystem = layoutGridSystem;
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int[][] allColumnWidth() {
        return this.f8191a.allColumnWidth();
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int[] allMargin() {
        return this.f8191a.allMargin();
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public ILayoutGrid chooseMargin(MarginType marginType) {
        k.e(marginType, "marginType");
        return this.f8191a.chooseMargin(marginType);
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int columnCount() {
        return this.f8191a.columnCount();
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int[] columnWidth() {
        return this.f8191a.columnWidth();
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int gutter() {
        return this.f8191a.gutter();
    }

    @Override // com.coui.component.responsiveui.status.IWindowStatus
    public LayoutGridWindowSize layoutGridWindowSize() {
        return this.f8192b.layoutGridWindowSize();
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    /* renamed from: layoutGridWindowWidth */
    public int getLayoutGridWidthPixel() {
        return this.f8191a.getLayoutGridWidthPixel();
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int margin() {
        return this.f8191a.margin();
    }

    @Override // com.coui.component.responsiveui.IResponsiveUI
    public void onConfigurationChanged(Context context, LayoutGridWindowSize layoutGridWindowSize) {
        k.e(context, "context");
        k.e(layoutGridWindowSize, "windowSize");
        rebuild(context, layoutGridWindowSize);
    }

    @Override // com.coui.component.responsiveui.IResponsiveUI
    public void rebuild(Context context, LayoutGridWindowSize layoutGridWindowSize) {
        k.e(context, "context");
        k.e(layoutGridWindowSize, "windowSize");
        WindowStatus windowStatus = this.windowStatus;
        windowStatus.setOrientation(context.getResources().getConfiguration().orientation);
        windowStatus.setLayoutGridWindowSize(layoutGridWindowSize);
        windowStatus.setWindowSizeClass(WindowSizeClass.INSTANCE.calculateFromSize(DpKt.pixel2Dp(layoutGridWindowSize.getWidth(), context), DpKt.pixel2Dp(layoutGridWindowSize.getHeight(), context)));
        this.layoutGridSystem.rebuild(context, this.windowStatus.getWindowSizeClass(), layoutGridWindowSize.getWidth());
        Log.d("ResponsiveUIProxy", k.l("[rebuild]: ", this.windowStatus));
        Log.d("ResponsiveUIProxy", k.l("[rebuild]: ", this.layoutGridSystem));
    }

    @Override // com.coui.component.responsiveui.IResponsiveUI
    public String showLayoutGridInfo() {
        return String.valueOf(this.layoutGridSystem);
    }

    @Override // com.coui.component.responsiveui.IResponsiveUI
    public String showWindowStatusInfo() {
        return String.valueOf(this.windowStatus);
    }

    @Override // com.coui.component.responsiveui.layoutgrid.ILayoutGrid
    public int width(int fromColumnIndex, int toColumnIndex) {
        return this.f8191a.width(fromColumnIndex, toColumnIndex);
    }

    @Override // com.coui.component.responsiveui.status.IWindowStatus
    public int windowOrientation() {
        return this.f8192b.windowOrientation();
    }

    @Override // com.coui.component.responsiveui.status.IWindowStatus
    public WindowSizeClass windowSizeClass() {
        return this.f8192b.windowSizeClass();
    }
}
