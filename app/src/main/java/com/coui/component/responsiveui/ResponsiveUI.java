package com.coui.component.responsiveui;

import android.content.Context;
import android.util.Log;
import com.coui.component.responsiveui.layoutgrid.LayoutGridSystem;
import com.coui.component.responsiveui.proxy.ResponsiveUIProxy;
import com.coui.component.responsiveui.status.WindowStatus;
import com.coui.component.responsiveui.unit.DpKt;
import com.coui.component.responsiveui.window.LayoutGridWindowSize;
import com.coui.component.responsiveui.window.WindowSizeClass;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ResponsiveUI.kt */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/coui/component/responsiveui/ResponsiveUI;", "", "()V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public final class ResponsiveUI {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* compiled from: ResponsiveUI.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/coui/component/responsiveui/ResponsiveUI$Companion;", "", "()V", "TAG", "", "getInstance", "Lcom/coui/component/responsiveui/IResponsiveUI;", "context", "Landroid/content/Context;", "windowSize", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final IResponsiveUI getInstance(Context context, LayoutGridWindowSize windowSize) {
            k.e(context, "context");
            k.e(windowSize, "windowSize");
            WindowStatus windowStatus = new WindowStatus(context.getResources().getConfiguration().orientation, WindowSizeClass.INSTANCE.calculateFromSize(DpKt.pixel2Dp(windowSize.getWidth(), context), DpKt.pixel2Dp(windowSize.getHeight(), context)), new LayoutGridWindowSize(windowSize));
            LayoutGridSystem layoutGridSystem = new LayoutGridSystem(context, windowStatus.windowSizeClass(), windowSize.getWidth());
            Log.d("ResponsiveUI", k.l("[init]: ", windowStatus));
            Log.d("ResponsiveUI", k.l("[init]: ", layoutGridSystem));
            return new ResponsiveUIProxy(layoutGridSystem, windowStatus);
        }
    }

    private ResponsiveUI() {
    }
}
