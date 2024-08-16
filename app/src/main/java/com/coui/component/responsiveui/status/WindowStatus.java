package com.coui.component.responsiveui.status;

import com.coui.component.responsiveui.window.LayoutGridWindowSize;
import com.coui.component.responsiveui.window.WindowSizeClass;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: WindowStatus.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0016\b\u0086\b\u0018\u00002\u00020\u0001B!\u0012\b\b\u0002\u0010\r\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b&\u0010'J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\t\u001a\u00020\bH\u0016J\t\u0010\n\u001a\u00020\u0002HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0004HÆ\u0003J\t\u0010\f\u001a\u00020\u0006HÆ\u0003J'\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u0006HÆ\u0001J\t\u0010\u000f\u001a\u00020\u0002HÖ\u0001J\u0013\u0010\u0013\u001a\u00020\u00122\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010HÖ\u0003R\"\u0010\r\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\"\u0010\u0005\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\"\u0010\u0007\u001a\u00020\u00068\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b \u0010!\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%¨\u0006("}, d2 = {"Lcom/coui/component/responsiveui/status/WindowStatus;", "Lcom/coui/component/responsiveui/status/IWindowStatus;", "", "windowOrientation", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "windowSizeClass", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "layoutGridWindowSize", "", "toString", "component1", "component2", "component3", "orientation", "copy", "hashCode", "", "other", "", "equals", "a", "I", "getOrientation", "()I", "setOrientation", "(I)V", "b", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "getWindowSizeClass", "()Lcom/coui/component/responsiveui/window/WindowSizeClass;", "setWindowSizeClass", "(Lcom/coui/component/responsiveui/window/WindowSizeClass;)V", "c", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "getLayoutGridWindowSize", "()Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "setLayoutGridWindowSize", "(Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;)V", "<init>", "(ILcom/coui/component/responsiveui/window/WindowSizeClass;Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;)V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final /* data */ class WindowStatus implements IWindowStatus {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private int orientation;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private WindowSizeClass windowSizeClass;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private LayoutGridWindowSize layoutGridWindowSize;

    public WindowStatus(int i10, WindowSizeClass windowSizeClass, LayoutGridWindowSize layoutGridWindowSize) {
        k.e(windowSizeClass, "windowSizeClass");
        k.e(layoutGridWindowSize, "layoutGridWindowSize");
        this.orientation = i10;
        this.windowSizeClass = windowSizeClass;
        this.layoutGridWindowSize = layoutGridWindowSize;
    }

    public static /* synthetic */ WindowStatus copy$default(WindowStatus windowStatus, int i10, WindowSizeClass windowSizeClass, LayoutGridWindowSize layoutGridWindowSize, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = windowStatus.orientation;
        }
        if ((i11 & 2) != 0) {
            windowSizeClass = windowStatus.windowSizeClass;
        }
        if ((i11 & 4) != 0) {
            layoutGridWindowSize = windowStatus.layoutGridWindowSize;
        }
        return windowStatus.copy(i10, windowSizeClass, layoutGridWindowSize);
    }

    /* renamed from: component1, reason: from getter */
    public final int getOrientation() {
        return this.orientation;
    }

    /* renamed from: component2, reason: from getter */
    public final WindowSizeClass getWindowSizeClass() {
        return this.windowSizeClass;
    }

    /* renamed from: component3, reason: from getter */
    public final LayoutGridWindowSize getLayoutGridWindowSize() {
        return this.layoutGridWindowSize;
    }

    public final WindowStatus copy(int orientation, WindowSizeClass windowSizeClass, LayoutGridWindowSize layoutGridWindowSize) {
        k.e(windowSizeClass, "windowSizeClass");
        k.e(layoutGridWindowSize, "layoutGridWindowSize");
        return new WindowStatus(orientation, windowSizeClass, layoutGridWindowSize);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WindowStatus)) {
            return false;
        }
        WindowStatus windowStatus = (WindowStatus) other;
        return this.orientation == windowStatus.orientation && k.a(this.windowSizeClass, windowStatus.windowSizeClass) && k.a(this.layoutGridWindowSize, windowStatus.layoutGridWindowSize);
    }

    public final LayoutGridWindowSize getLayoutGridWindowSize() {
        return this.layoutGridWindowSize;
    }

    public final int getOrientation() {
        return this.orientation;
    }

    public final WindowSizeClass getWindowSizeClass() {
        return this.windowSizeClass;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.orientation) * 31) + this.windowSizeClass.hashCode()) * 31) + this.layoutGridWindowSize.hashCode();
    }

    @Override // com.coui.component.responsiveui.status.IWindowStatus
    public LayoutGridWindowSize layoutGridWindowSize() {
        return this.layoutGridWindowSize;
    }

    public final void setLayoutGridWindowSize(LayoutGridWindowSize layoutGridWindowSize) {
        k.e(layoutGridWindowSize, "<set-?>");
        this.layoutGridWindowSize = layoutGridWindowSize;
    }

    public final void setOrientation(int i10) {
        this.orientation = i10;
    }

    public final void setWindowSizeClass(WindowSizeClass windowSizeClass) {
        k.e(windowSizeClass, "<set-?>");
        this.windowSizeClass = windowSizeClass;
    }

    public String toString() {
        return "WindowStatus { orientation = " + this.orientation + ", windowSizeClass = " + this.windowSizeClass + ", windowSize = " + this.layoutGridWindowSize + " }";
    }

    @Override // com.coui.component.responsiveui.status.IWindowStatus
    public int windowOrientation() {
        return this.orientation;
    }

    @Override // com.coui.component.responsiveui.status.IWindowStatus
    public WindowSizeClass windowSizeClass() {
        return this.windowSizeClass;
    }

    public /* synthetic */ WindowStatus(int i10, WindowSizeClass windowSizeClass, LayoutGridWindowSize layoutGridWindowSize, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this((i11 & 1) != 0 ? 0 : i10, windowSizeClass, layoutGridWindowSize);
    }
}
