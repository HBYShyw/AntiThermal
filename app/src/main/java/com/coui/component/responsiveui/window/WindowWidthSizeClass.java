package com.coui.component.responsiveui.window;

import android.content.Context;
import android.util.Log;
import com.coui.component.responsiveui.ResponsiveUILog;
import com.coui.component.responsiveui.breakpoints.Breakpoints;
import com.coui.component.responsiveui.unit.Dp;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: WindowSizeClass.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0011\b\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\n"}, d2 = {"Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "", "", "toString", "a", "Ljava/lang/String;", ThermalBaseConfig.Item.ATTR_VALUE, "<init>", "(Ljava/lang/String;)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class WindowWidthSizeClass {
    public static final WindowWidthSizeClass Compact;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final WindowWidthSizeClass Expanded;
    public static final WindowWidthSizeClass Medium;

    /* renamed from: b, reason: collision with root package name */
    private static final boolean f8226b;

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final String value;

    static {
        ResponsiveUILog responsiveUILog = ResponsiveUILog.INSTANCE;
        f8226b = responsiveUILog.getLOG_DEBUG() || responsiveUILog.isLoggable("WindowSizeClass", 3);
        Compact = new WindowWidthSizeClass("Compact");
        Medium = new WindowWidthSizeClass("Medium");
        Expanded = new WindowWidthSizeClass("Expanded");
    }

    private WindowWidthSizeClass(String str) {
        this.value = str;
    }

    public String toString() {
        return k.l(this.value, " window base-width");
    }

    /* compiled from: WindowSizeClass.kt */
    @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0010J\u0015\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0012H\u0001¢\u0006\u0002\b\u0013R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/coui/component/responsiveui/window/WindowWidthSizeClass$Companion;", "", "()V", "Compact", "Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "DEBUG", "", "Expanded", "Medium", "TAG", "", "fromWidth", "context", "Landroid/content/Context;", "width", "", "Lcom/coui/component/responsiveui/unit/Dp;", "widthDp", "", "_hide_fromWidth", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final WindowWidthSizeClass _hide_fromWidth(float widthDp) {
            return widthDp < Breakpoints.BP_MEDIUM_WINDOW_BASE_WIDTH.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() ? WindowWidthSizeClass.Compact : widthDp < Breakpoints.BP_EXPANDED_WINDOW_BASE_WIDTH.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() ? WindowWidthSizeClass.Medium : WindowWidthSizeClass.Expanded;
        }

        public final WindowWidthSizeClass fromWidth(Dp width) {
            k.e(width, "width");
            if (WindowWidthSizeClass.f8226b) {
                Log.d("WindowWidthSizeClass", k.l("[fromWidth] width : ", width));
            }
            if (width.compareTo(new Dp((float) 0)) >= 0) {
                return _hide_fromWidth(width.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String());
            }
            throw new IllegalArgumentException("Width must not be negative".toString());
        }

        public final WindowWidthSizeClass fromWidth(Context context, int width) {
            k.e(context, "context");
            if (WindowWidthSizeClass.f8226b) {
                Log.d("WindowWidthSizeClass", "[fromWidth] width : " + width + " pixel");
            }
            if (width >= 0) {
                return _hide_fromWidth(width / context.getResources().getDisplayMetrics().density);
            }
            throw new IllegalArgumentException("Width must not be negative".toString());
        }
    }
}
