package androidx.window.sidecar;

import android.app.Activity;
import kotlin.Metadata;
import wd.b;
import wd.d;
import za.k;

/* compiled from: WindowInfoTrackerImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000 \u00112\u00020\u0001:\u0001\u0012B\u0017\u0012\u0006\u0010\n\u001a\u00020\u0007\u0012\u0006\u0010\u000e\u001a\u00020\u000b¢\u0006\u0004\b\u000f\u0010\u0010J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\n\u001a\u00020\u00078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\b\u0010\tR\u0014\u0010\u000e\u001a\u00020\u000b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Landroidx/window/layout/WindowInfoTrackerImpl;", "Landroidx/window/layout/WindowInfoTracker;", "Landroid/app/Activity;", "activity", "Lwd/b;", "Landroidx/window/layout/WindowLayoutInfo;", "a", "Landroidx/window/layout/WindowMetricsCalculator;", "b", "Landroidx/window/layout/WindowMetricsCalculator;", "windowMetricsCalculator", "Landroidx/window/layout/WindowBackend;", "c", "Landroidx/window/layout/WindowBackend;", "windowBackend", "<init>", "(Landroidx/window/layout/WindowMetricsCalculator;Landroidx/window/layout/WindowBackend;)V", "d", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class WindowInfoTrackerImpl implements WindowInfoTracker {

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final WindowMetricsCalculator windowMetricsCalculator;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final WindowBackend windowBackend;

    public WindowInfoTrackerImpl(WindowMetricsCalculator windowMetricsCalculator, WindowBackend windowBackend) {
        k.e(windowMetricsCalculator, "windowMetricsCalculator");
        k.e(windowBackend, "windowBackend");
        this.windowMetricsCalculator = windowMetricsCalculator;
        this.windowBackend = windowBackend;
    }

    @Override // androidx.window.sidecar.WindowInfoTracker
    public b<WindowLayoutInfo> a(Activity activity) {
        k.e(activity, "activity");
        return d.a(new WindowInfoTrackerImpl$windowLayoutInfo$1(this, activity, null));
    }
}
