package com.coui.component.responsiveui.window;

import com.coui.component.responsiveui.unit.Dp;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.Reflection;
import za.k;

/* compiled from: WindowSizeClass.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB!\b\u0002\u0012\u0006\u0010\u000e\u001a\u00020\t\u0012\u0006\u0010\u0014\u001a\u00020\u000f\u0012\u0006\u0010\u001a\u001a\u00020\u0015¢\u0006\u0004\b\u001b\u0010\u001cJ\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016R\u0017\u0010\u000e\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0014\u001a\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u001a\u001a\u00020\u00158\u0006¢\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0018\u0010\u0019¨\u0006\u001e"}, d2 = {"Lcom/coui/component/responsiveui/window/WindowSizeClass;", "", "other", "", "equals", "", "hashCode", "", "toString", "Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "a", "Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "getWindowWidthSizeClass", "()Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "windowWidthSizeClass", "Lcom/coui/component/responsiveui/window/WindowHeightSizeClass;", "b", "Lcom/coui/component/responsiveui/window/WindowHeightSizeClass;", "getWindowHeightSizeClass", "()Lcom/coui/component/responsiveui/window/WindowHeightSizeClass;", "windowHeightSizeClass", "Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;", "c", "Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;", "getWindowTotalSizeClass", "()Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;", "windowTotalSizeClass", "<init>", "(Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;Lcom/coui/component/responsiveui/window/WindowHeightSizeClass;Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class WindowSizeClass {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final WindowWidthSizeClass windowWidthSizeClass;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final WindowHeightSizeClass windowHeightSizeClass;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final WindowTotalSizeClass windowTotalSizeClass;

    /* compiled from: WindowSizeClass.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/coui/component/responsiveui/window/WindowSizeClass$Companion;", "", "()V", "TAG", "", "calculateFromSize", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "width", "Lcom/coui/component/responsiveui/unit/Dp;", "height", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final WindowSizeClass calculateFromSize(Dp width, Dp height) {
            k.e(width, "width");
            k.e(height, "height");
            return new WindowSizeClass(WindowWidthSizeClass.INSTANCE.fromWidth(width), WindowHeightSizeClass.INSTANCE.fromHeight(height), WindowTotalSizeClass.INSTANCE.fromWidthAndHeight(width, height), null);
        }
    }

    private WindowSizeClass(WindowWidthSizeClass windowWidthSizeClass, WindowHeightSizeClass windowHeightSizeClass, WindowTotalSizeClass windowTotalSizeClass) {
        this.windowWidthSizeClass = windowWidthSizeClass;
        this.windowHeightSizeClass = windowHeightSizeClass;
        this.windowTotalSizeClass = windowTotalSizeClass;
    }

    public /* synthetic */ WindowSizeClass(WindowWidthSizeClass windowWidthSizeClass, WindowHeightSizeClass windowHeightSizeClass, WindowTotalSizeClass windowTotalSizeClass, DefaultConstructorMarker defaultConstructorMarker) {
        this(windowWidthSizeClass, windowHeightSizeClass, windowTotalSizeClass);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !k.a(Reflection.b(WindowSizeClass.class), Reflection.b(other.getClass()))) {
            return false;
        }
        WindowSizeClass windowSizeClass = (WindowSizeClass) other;
        return k.a(this.windowWidthSizeClass, windowSizeClass.windowWidthSizeClass) && k.a(this.windowHeightSizeClass, windowSizeClass.windowHeightSizeClass) && k.a(this.windowTotalSizeClass, windowSizeClass.windowTotalSizeClass);
    }

    public final WindowHeightSizeClass getWindowHeightSizeClass() {
        return this.windowHeightSizeClass;
    }

    public final WindowTotalSizeClass getWindowTotalSizeClass() {
        return this.windowTotalSizeClass;
    }

    public final WindowWidthSizeClass getWindowWidthSizeClass() {
        return this.windowWidthSizeClass;
    }

    public int hashCode() {
        return (((this.windowWidthSizeClass.hashCode() * 31) + this.windowHeightSizeClass.hashCode()) * 31) + this.windowTotalSizeClass.hashCode();
    }

    public String toString() {
        return "WindowSizeClass(" + this.windowWidthSizeClass + ", " + this.windowHeightSizeClass + ", " + this.windowTotalSizeClass + ')';
    }
}
