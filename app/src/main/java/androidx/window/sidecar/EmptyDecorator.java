package androidx.window.sidecar;

import kotlin.Metadata;
import za.k;

/* compiled from: WindowInfoTracker.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\bÂ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0007"}, d2 = {"Landroidx/window/layout/EmptyDecorator;", "Landroidx/window/layout/WindowInfoTrackerDecorator;", "Landroidx/window/layout/WindowInfoTracker;", "tracker", "a", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class EmptyDecorator implements WindowInfoTrackerDecorator {

    /* renamed from: a, reason: collision with root package name */
    public static final EmptyDecorator f4412a = new EmptyDecorator();

    private EmptyDecorator() {
    }

    @Override // androidx.window.sidecar.WindowInfoTrackerDecorator
    public WindowInfoTracker a(WindowInfoTracker tracker) {
        k.e(tracker, "tracker");
        return tracker;
    }
}
