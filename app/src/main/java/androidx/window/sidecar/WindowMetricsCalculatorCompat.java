package androidx.window.sidecar;

import android.app.Activity;
import kotlin.Metadata;
import za.k;

/* compiled from: WindowMetricsCalculatorCompat.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\t\u001a\u00020\u00068\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010\b¨\u0006\f"}, d2 = {"Landroidx/window/layout/WindowMetricsCalculatorCompat;", "Landroidx/window/layout/WindowMetricsCalculator;", "Landroid/app/Activity;", "activity", "Landroidx/window/layout/WindowMetrics;", "a", "", "b", "Ljava/lang/String;", "TAG", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class WindowMetricsCalculatorCompat implements WindowMetricsCalculator {

    /* renamed from: a, reason: collision with root package name */
    public static final WindowMetricsCalculatorCompat f4509a = new WindowMetricsCalculatorCompat();

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private static final String TAG;

    static {
        String simpleName = WindowMetricsCalculatorCompat.class.getSimpleName();
        k.d(simpleName, "WindowMetricsCalculatorC…at::class.java.simpleName");
        TAG = simpleName;
    }

    private WindowMetricsCalculatorCompat() {
    }

    public WindowMetrics a(Activity activity) {
        k.e(activity, "activity");
        return new WindowMetrics(ActivityCompatHelperApi30.f4409a.a(activity));
    }
}
