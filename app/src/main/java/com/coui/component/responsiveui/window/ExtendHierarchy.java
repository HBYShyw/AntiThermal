package com.coui.component.responsiveui.window;

import android.content.Context;
import com.coui.component.responsiveui.unit.Dp;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ExtendHierarchy.kt */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/coui/component/responsiveui/window/ExtendHierarchy;", "", "()V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public final class ExtendHierarchy {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: a, reason: collision with root package name */
    private static final Dp f8215a = new Dp(280);

    /* renamed from: b, reason: collision with root package name */
    private static final Dp f8216b = new Dp(360);

    /* compiled from: ExtendHierarchy.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u0006\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004J\u001e\u0010\u000e\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/coui/component/responsiveui/window/ExtendHierarchy$Companion;", "", "()V", "EXPANDED_EXTEND_HIERARCHY_PARENT_WIDTH", "Lcom/coui/component/responsiveui/unit/Dp;", "MEDIUM_EXTEND_HIERARCHY_PARENT_WIDTH", "childWindowWidth", "", "context", "Landroid/content/Context;", "windowWidthSizeClass", "Lcom/coui/component/responsiveui/window/WindowWidthSizeClass;", "windowWidth", "", "parentWindowWidth", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final float childWindowWidth(Context context, WindowWidthSizeClass windowWidthSizeClass, int windowWidth) {
            k.e(context, "context");
            k.e(windowWidthSizeClass, "windowWidthSizeClass");
            return windowWidth - parentWindowWidth(context, windowWidthSizeClass, windowWidth);
        }

        public final float parentWindowWidth(Context context, WindowWidthSizeClass windowWidthSizeClass, int windowWidth) {
            k.e(context, "context");
            k.e(windowWidthSizeClass, "windowWidthSizeClass");
            return k.a(windowWidthSizeClass, WindowWidthSizeClass.Medium) ? ExtendHierarchy.f8215a.toPixel(context) : k.a(windowWidthSizeClass, WindowWidthSizeClass.Expanded) ? ExtendHierarchy.f8216b.toPixel(context) : windowWidth;
        }

        public final Dp childWindowWidth(WindowWidthSizeClass windowWidthSizeClass, Dp windowWidth) {
            k.e(windowWidthSizeClass, "windowWidthSizeClass");
            k.e(windowWidth, "windowWidth");
            return windowWidth.minus(parentWindowWidth(windowWidthSizeClass, windowWidth));
        }

        public final Dp parentWindowWidth(WindowWidthSizeClass windowWidthSizeClass, Dp windowWidth) {
            k.e(windowWidthSizeClass, "windowWidthSizeClass");
            k.e(windowWidth, "windowWidth");
            return k.a(windowWidthSizeClass, WindowWidthSizeClass.Medium) ? ExtendHierarchy.f8215a : k.a(windowWidthSizeClass, WindowWidthSizeClass.Expanded) ? ExtendHierarchy.f8216b : windowWidth;
        }
    }

    private ExtendHierarchy() {
    }
}
