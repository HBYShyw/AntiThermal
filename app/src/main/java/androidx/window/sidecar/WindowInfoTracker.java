package androidx.window.sidecar;

import android.app.Activity;
import android.content.Context;
import kotlin.Metadata;
import ma.h;
import ma.j;
import wd.b;
import za.Reflection;
import za.k;

/* compiled from: WindowInfoTracker.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000 \u00062\u00020\u0001:\u0001\u0007J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\b"}, d2 = {"Landroidx/window/layout/WindowInfoTracker;", "", "Landroid/app/Activity;", "activity", "Lwd/b;", "Landroidx/window/layout/WindowLayoutInfo;", "a", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public interface WindowInfoTracker {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = Companion.f4488a;

    /* compiled from: WindowInfoTracker.kt */
    @Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0019\u0010\u0017J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007R\u0014\u0010\t\u001a\u00020\u00068\u0002X\u0082D¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u0016\u0010\r\u001a\u0004\u0018\u00010\n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\fR\u0016\u0010\u0011\u001a\u00020\u000e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R#\u0010\u0018\u001a\u0004\u0018\u00010\u00128@X\u0080\u0084\u0002¢\u0006\u0012\n\u0004\b\u0013\u0010\u0014\u0012\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u000b\u0010\u0015¨\u0006\u001a"}, d2 = {"Landroidx/window/layout/WindowInfoTracker$Companion;", "", "Landroid/content/Context;", "context", "Landroidx/window/layout/WindowInfoTracker;", "d", "", "b", "Z", "DEBUG", "", "c", "Ljava/lang/String;", "TAG", "Landroidx/window/layout/WindowInfoTrackerDecorator;", "e", "Landroidx/window/layout/WindowInfoTrackerDecorator;", "decorator", "Landroidx/window/layout/WindowBackend;", "extensionBackend$delegate", "Lma/h;", "()Landroidx/window/layout/WindowBackend;", "getExtensionBackend$window_release$annotations", "()V", "extensionBackend", "<init>", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        private static final boolean DEBUG = false;

        /* renamed from: d, reason: collision with root package name */
        private static final h<ExtensionWindowLayoutInfoBackend> f4491d;

        /* renamed from: e, reason: collision with root package name and from kotlin metadata */
        private static WindowInfoTrackerDecorator decorator;

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ Companion f4488a = new Companion();

        /* renamed from: c, reason: collision with root package name and from kotlin metadata */
        private static final String TAG = Reflection.b(WindowInfoTracker.class).v();

        static {
            h<ExtensionWindowLayoutInfoBackend> b10;
            b10 = j.b(WindowInfoTracker$Companion$extensionBackend$2.f4493e);
            f4491d = b10;
            decorator = EmptyDecorator.f4412a;
        }

        private Companion() {
        }

        public final WindowBackend c() {
            return f4491d.getValue();
        }

        public final WindowInfoTracker d(Context context) {
            k.e(context, "context");
            WindowBackend c10 = c();
            if (c10 == null) {
                c10 = SidecarWindowBackend.INSTANCE.a(context);
            }
            return decorator.a(new WindowInfoTrackerImpl(WindowMetricsCalculatorCompat.f4509a, c10));
        }
    }

    b<WindowLayoutInfo> a(Activity activity);
}
