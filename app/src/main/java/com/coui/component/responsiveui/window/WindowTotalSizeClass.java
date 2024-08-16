package com.coui.component.responsiveui.window;

import android.content.Context;
import android.util.Log;
import com.coui.component.responsiveui.ResponsiveUILog;
import com.coui.component.responsiveui.unit.Dp;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: WindowSizeClass.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0011\b\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\n"}, d2 = {"Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;", "", "", "toString", "a", "Ljava/lang/String;", ThermalBaseConfig.Item.ATTR_VALUE, "<init>", "(Ljava/lang/String;)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class WindowTotalSizeClass {
    public static final WindowTotalSizeClass Compact;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final WindowTotalSizeClass Expanded;
    public static final WindowTotalSizeClass MediumLandscape;
    public static final WindowTotalSizeClass MediumPortrait;
    public static final WindowTotalSizeClass MediumSquare;

    /* renamed from: b, reason: collision with root package name */
    private static final boolean f8224b;

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final String value;

    static {
        ResponsiveUILog responsiveUILog = ResponsiveUILog.INSTANCE;
        f8224b = responsiveUILog.getLOG_DEBUG() || responsiveUILog.isLoggable("WindowSizeClass", 3);
        Compact = new WindowTotalSizeClass("Compact");
        MediumLandscape = new WindowTotalSizeClass("MediumLandscape");
        MediumSquare = new WindowTotalSizeClass("MediumSquare");
        MediumPortrait = new WindowTotalSizeClass("MediumPortrait");
        Expanded = new WindowTotalSizeClass("Expanded");
    }

    private WindowTotalSizeClass(String str) {
        this.value = str;
    }

    public String toString() {
        return k.l(this.value, " window base-total");
    }

    /* compiled from: WindowSizeClass.kt */
    @Metadata(bv = {}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0002J\u0016\u0010\n\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0007J\u001e\u0010\n\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\rR\u0014\u0010\u000e\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0011\u001a\u00020\u00108\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0013\u0010\u000fR\u0014\u0010\u0014\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010\u000fR\u0014\u0010\u0015\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0015\u0010\u000fR\u0014\u0010\u0016\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0016\u0010\u000fR\u0014\u0010\u0018\u001a\u00020\u00178\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0018\u0010\u0019¨\u0006\u001c"}, d2 = {"Lcom/coui/component/responsiveui/window/WindowTotalSizeClass$Companion;", "", "", "widthDp", "heightDp", "Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;", "a", "Lcom/coui/component/responsiveui/unit/Dp;", "width", "height", "fromWidthAndHeight", "Landroid/content/Context;", "context", "", "Compact", "Lcom/coui/component/responsiveui/window/WindowTotalSizeClass;", "", "DEBUG", "Z", "Expanded", "MediumLandscape", "MediumPortrait", "MediumSquare", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final WindowTotalSizeClass a(float widthDp, float heightDp) {
            WindowWidthSizeClass _hide_fromWidth = WindowWidthSizeClass.INSTANCE._hide_fromWidth(widthDp);
            if (k.a(_hide_fromWidth, WindowWidthSizeClass.Compact)) {
                return WindowTotalSizeClass.Compact;
            }
            if (!k.a(_hide_fromWidth, WindowWidthSizeClass.Medium)) {
                return WindowTotalSizeClass.Expanded;
            }
            WindowHeightSizeClass _hide_fromHeight = WindowHeightSizeClass.INSTANCE._hide_fromHeight(heightDp);
            return k.a(_hide_fromHeight, WindowHeightSizeClass.Compact) ? WindowTotalSizeClass.MediumLandscape : k.a(_hide_fromHeight, WindowHeightSizeClass.Medium) ? WindowTotalSizeClass.MediumSquare : WindowTotalSizeClass.MediumPortrait;
        }

        public final WindowTotalSizeClass fromWidthAndHeight(Dp width, Dp height) {
            k.e(width, "width");
            k.e(height, "height");
            if (WindowTotalSizeClass.f8224b) {
                Log.d("WindowHeightSizeClass", "[fromWidthAndHeight] width : " + width + ", height : " + height);
            }
            float f10 = 0;
            if (width.compareTo(new Dp(f10)) >= 0) {
                if (height.compareTo(new Dp(f10)) >= 0) {
                    return a(width.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String(), height.getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String());
                }
                throw new IllegalArgumentException("Height must not be negative".toString());
            }
            throw new IllegalArgumentException("Width must not be negative".toString());
        }

        public final WindowTotalSizeClass fromWidthAndHeight(Context context, int width, int height) {
            k.e(context, "context");
            if (WindowTotalSizeClass.f8224b) {
                Log.d("WindowHeightSizeClass", "[fromWidthAndHeight] width : " + width + " pixel, height : " + height + " pixel");
            }
            if (!(width >= 0)) {
                throw new IllegalArgumentException("Height must not be negative".toString());
            }
            if (height >= 0) {
                float f10 = context.getResources().getDisplayMetrics().density;
                return a(width / f10, height / f10);
            }
            throw new IllegalArgumentException("Height must not be negative".toString());
        }
    }
}
