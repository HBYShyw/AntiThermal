package com.coui.component.responsiveui.status;

import android.util.Log;
import android.view.ComponentActivity;
import androidx.lifecycle.i;
import androidx.lifecycle.p;
import androidx.window.sidecar.FoldingFeature;
import java.util.function.Consumer;
import kotlin.Metadata;
import td.Dispatchers;
import td.h;
import za.k;

/* compiled from: WindowFeatureUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\b\u0010\u0007\u001a\u00020\u0004H\u0007J\u001e\u0010\u000e\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0007¨\u0006\u0011"}, d2 = {"Lcom/coui/component/responsiveui/status/WindowFeatureUtil;", "", "Landroidx/window/layout/FoldingFeature;", "foldingFeature", "", "isTableTopPosture", "isBookPosture", "isSupportWindowFeature", "Landroidx/activity/ComponentActivity;", "activity", "Ljava/util/function/Consumer;", "Lcom/coui/component/responsiveui/status/WindowFeature;", "action", "Lma/f0;", "trackWindowFeature", "<init>", "()V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class WindowFeatureUtil {
    public static final WindowFeatureUtil INSTANCE = new WindowFeatureUtil();

    private WindowFeatureUtil() {
    }

    public static final boolean isBookPosture(FoldingFeature foldingFeature) {
        k.e(foldingFeature, "foldingFeature");
        Log.d("WindowFeatureUtil", "[isBookPosture] state: " + foldingFeature.getState() + ", orientation: " + foldingFeature.a());
        return k.a(foldingFeature.getState(), FoldingFeature.State.f4435d) && k.a(foldingFeature.a(), FoldingFeature.Orientation.f4430c);
    }

    public static final boolean isSupportWindowFeature() {
        try {
            Class<?> cls = Class.forName("com.oplus.content.OplusFeatureConfigManager");
            Object invoke = cls.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            if (invoke == null) {
                return false;
            }
            Object invoke2 = cls.getDeclaredMethod("hasFeature", String.class).invoke(invoke, "oplus.software.display.google_extension_layout");
            if (invoke2 != null) {
                ((Boolean) invoke2).booleanValue();
                Log.d("WindowFeatureUtil", k.l("[isSupportWindowFeature] ", invoke2));
                return ((Boolean) invoke2).booleanValue();
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Boolean");
        } catch (Exception e10) {
            Log.e("WindowFeatureUtil", k.l("[isSupportWindowFeature] ", e10));
            return false;
        }
    }

    public static final boolean isTableTopPosture(FoldingFeature foldingFeature) {
        k.e(foldingFeature, "foldingFeature");
        Log.d("WindowFeatureUtil", "[isTableTopPosture] state: " + foldingFeature.getState() + ", orientation: " + foldingFeature.a());
        return k.a(foldingFeature.getState(), FoldingFeature.State.f4435d) && k.a(foldingFeature.a(), FoldingFeature.Orientation.f4431d);
    }

    public final void trackWindowFeature(ComponentActivity componentActivity, Consumer<WindowFeature> consumer) {
        k.e(componentActivity, "activity");
        k.e(consumer, "action");
        i a10 = p.a(componentActivity);
        Dispatchers dispatchers = Dispatchers.f18789a;
        h.b(a10, Dispatchers.c(), null, new WindowFeatureUtil$trackWindowFeature$1(componentActivity, consumer, null), 2, null);
    }
}
