package com.coui.component.responsiveui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import com.coui.component.responsiveui.layoutgrid.LayoutGridSystem;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.coui.component.responsiveui.proxy.ResponsiveUIProxy;
import com.coui.component.responsiveui.status.WindowStatus;
import com.coui.component.responsiveui.unit.Dp;
import com.coui.component.responsiveui.unit.DpKt;
import com.coui.component.responsiveui.window.LayoutGridWindowSize;
import com.coui.component.responsiveui.window.WindowSizeClass;
import kotlin.Metadata;
import za.k;

/* compiled from: ResponsiveUIModel.kt */
@Metadata(bv = {}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 @2\u00020\u0001:\u0001@B\u0017\u0012\u0006\u0010/\u001a\u00020*\u0012\u0006\u00106\u001a\u00020\u0004¢\u0006\u0004\b;\u0010<B!\b\u0016\u0012\u0006\u0010=\u001a\u00020*\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0007¢\u0006\u0004\b;\u0010>B!\b\u0016\u0012\u0006\u0010=\u001a\u00020*\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n¢\u0006\u0004\b;\u0010?J\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0004J\u0016\u0010\u0006\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0007J\u0016\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nJ\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\rJ\u0006\u0010\u0012\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u0011J\u0006\u0010\u0014\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0019\u001a\u00020\u0018J\u0006\u0010\u001a\u001a\u00020\nJ\u0006\u0010\u001b\u001a\u00020\nJ\u0006\u0010\u001c\u001a\u00020\u0018J\u0013\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00180\u001d¢\u0006\u0004\b\u001e\u0010\u001fJ\u0006\u0010 \u001a\u00020\nJ\u0016\u0010#\u001a\u00020\n2\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\nJ\u000e\u0010%\u001a\u00020\n2\u0006\u0010$\u001a\u00020\nJ\u0006\u0010&\u001a\u00020\nJ\u0006\u0010(\u001a\u00020'J\u0006\u0010)\u001a\u00020\u0004R\u0017\u0010/\u001a\u00020*8\u0006¢\u0006\f\n\u0004\b+\u0010,\u001a\u0004\b-\u0010.R\"\u00106\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b0\u00101\u001a\u0004\b2\u00103\"\u0004\b4\u00105R\u0014\u0010:\u001a\u0002078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b8\u00109¨\u0006A"}, d2 = {"Lcom/coui/component/responsiveui/ResponsiveUIModel;", "", "Lcom/coui/component/responsiveui/IResponsiveUI;", "getResponsiveUI", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "windowSize", "rebuild", "", "totalWidthDp", "totalHeightDp", "", "totalWidthPx", "totalHeightPx", "Landroid/content/res/Configuration;", "newConfig", "Lma/f0;", "onConfigurationChanged", "", "showWindowStatusInfo", "showLayoutGridInfo", "layoutGridWindowWidth", "Lcom/coui/component/responsiveui/layoutgrid/MarginType;", "marginType", "chooseMargin", "", "allMargin", "margin", "columnCount", "columnWidth", "", "allColumnWidth", "()[[I", "gutter", "fromColumnIndex", "toColumnIndex", "width", "gridNumber", "calculateGridWidth", "windowOrientation", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "windowSizeClass", "layoutGridWindowSize", "Landroid/content/Context;", "a", "Landroid/content/Context;", "getMContext", "()Landroid/content/Context;", "mContext", "b", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "getMWindowSize", "()Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "setMWindowSize", "(Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;)V", "mWindowSize", "Lcom/coui/component/responsiveui/proxy/ResponsiveUIProxy;", "c", "Lcom/coui/component/responsiveui/proxy/ResponsiveUIProxy;", "mResponsiveUIProxy", "<init>", "(Landroid/content/Context;Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;)V", "context", "(Landroid/content/Context;FF)V", "(Landroid/content/Context;II)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class ResponsiveUIModel {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final Context mContext;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private LayoutGridWindowSize mWindowSize;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final ResponsiveUIProxy mResponsiveUIProxy;

    public ResponsiveUIModel(Context context, LayoutGridWindowSize layoutGridWindowSize) {
        k.e(context, "mContext");
        k.e(layoutGridWindowSize, "mWindowSize");
        this.mContext = context;
        this.mWindowSize = layoutGridWindowSize;
        WindowStatus windowStatus = new WindowStatus(context.getResources().getConfiguration().orientation, WindowSizeClass.INSTANCE.calculateFromSize(DpKt.pixel2Dp(this.mWindowSize.getWidth(), context), DpKt.pixel2Dp(this.mWindowSize.getHeight(), context)), new LayoutGridWindowSize(this.mWindowSize));
        LayoutGridSystem layoutGridSystem = new LayoutGridSystem(context, windowStatus.windowSizeClass(), this.mWindowSize.getWidth());
        Log.d("ResponsiveUIModel", k.l("[init]: ", windowStatus));
        Log.d("ResponsiveUIModel", k.l("[init]: ", layoutGridSystem));
        this.mResponsiveUIProxy = new ResponsiveUIProxy(layoutGridSystem, windowStatus);
    }

    public final int[][] allColumnWidth() {
        return this.mResponsiveUIProxy.allColumnWidth();
    }

    public final int[] allMargin() {
        return this.mResponsiveUIProxy.allMargin();
    }

    public final int calculateGridWidth(int gridNumber) {
        if (gridNumber > this.mResponsiveUIProxy.columnCount()) {
            Log.w("ResponsiveUIModel", "calculateGridWidth: requested grid number larger then current grid total number, fill the whole grid");
            gridNumber = this.mResponsiveUIProxy.columnCount();
        }
        return this.mResponsiveUIProxy.width((this.mResponsiveUIProxy.columnCount() - gridNumber) / 2, (gridNumber + r0) - 1);
    }

    public final ResponsiveUIModel chooseMargin(MarginType marginType) {
        k.e(marginType, "marginType");
        this.mResponsiveUIProxy.chooseMargin(marginType);
        return this;
    }

    public final int columnCount() {
        return this.mResponsiveUIProxy.columnCount();
    }

    public final int[] columnWidth() {
        return this.mResponsiveUIProxy.columnWidth();
    }

    public final Context getMContext() {
        return this.mContext;
    }

    public final LayoutGridWindowSize getMWindowSize() {
        return this.mWindowSize;
    }

    public final IResponsiveUI getResponsiveUI() {
        return this.mResponsiveUIProxy;
    }

    public final int gutter() {
        return this.mResponsiveUIProxy.gutter();
    }

    public final LayoutGridWindowSize layoutGridWindowSize() {
        return this.mResponsiveUIProxy.layoutGridWindowSize();
    }

    public final int layoutGridWindowWidth() {
        return this.mResponsiveUIProxy.getLayoutGridWidthPixel();
    }

    public final int margin() {
        return this.mResponsiveUIProxy.margin();
    }

    public final void onConfigurationChanged(Configuration configuration) {
        k.e(configuration, "newConfig");
        this.mWindowSize.setWidth((int) new Dp(configuration.screenWidthDp).toPixel(this.mContext));
        this.mWindowSize.setHeight((int) new Dp(configuration.screenWidthDp).toPixel(this.mContext));
        this.mResponsiveUIProxy.rebuild(this.mContext, this.mWindowSize);
    }

    public final ResponsiveUIModel rebuild(LayoutGridWindowSize windowSize) {
        k.e(windowSize, "windowSize");
        this.mWindowSize = windowSize;
        this.mResponsiveUIProxy.rebuild(this.mContext, windowSize);
        return this;
    }

    public final void setMWindowSize(LayoutGridWindowSize layoutGridWindowSize) {
        k.e(layoutGridWindowSize, "<set-?>");
        this.mWindowSize = layoutGridWindowSize;
    }

    public final String showLayoutGridInfo() {
        return this.mResponsiveUIProxy.showLayoutGridInfo();
    }

    public final String showWindowStatusInfo() {
        return this.mResponsiveUIProxy.showWindowStatusInfo();
    }

    public final int width(int fromColumnIndex, int toColumnIndex) {
        return this.mResponsiveUIProxy.width(fromColumnIndex, toColumnIndex);
    }

    public final int windowOrientation() {
        return this.mResponsiveUIProxy.windowOrientation();
    }

    public final WindowSizeClass windowSizeClass() {
        return this.mResponsiveUIProxy.windowSizeClass();
    }

    public final ResponsiveUIModel rebuild(float totalWidthDp, float totalHeightDp) {
        this.mWindowSize.setWidth((int) new Dp(totalWidthDp).toPixel(this.mContext));
        this.mWindowSize.setHeight((int) new Dp(totalHeightDp).toPixel(this.mContext));
        this.mResponsiveUIProxy.rebuild(this.mContext, this.mWindowSize);
        return this;
    }

    public final ResponsiveUIModel rebuild(int totalWidthPx, int totalHeightPx) {
        this.mWindowSize.setWidth(totalWidthPx);
        this.mWindowSize.setHeight(totalHeightPx);
        this.mResponsiveUIProxy.rebuild(this.mContext, this.mWindowSize);
        return this;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ResponsiveUIModel(Context context, float f10, float f11) {
        this(context, new LayoutGridWindowSize(context, new Dp(f10), new Dp(f11)));
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ResponsiveUIModel(Context context, int i10, int i11) {
        this(context, new LayoutGridWindowSize(i10, i11));
        k.e(context, "context");
    }
}
