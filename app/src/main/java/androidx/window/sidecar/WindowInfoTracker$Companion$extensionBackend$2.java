package androidx.window.sidecar;

import android.util.Log;
import androidx.window.core.ConsumerAdapter;
import androidx.window.extensions.layout.WindowLayoutComponent;
import androidx.window.sidecar.WindowInfoTracker;
import kotlin.Metadata;
import ya.a;
import za.Lambda;
import za.k;

/* compiled from: WindowInfoTracker.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u0004\u0018\u00010\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Landroidx/window/layout/ExtensionWindowLayoutInfoBackend;", "a", "()Landroidx/window/layout/ExtensionWindowLayoutInfoBackend;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class WindowInfoTracker$Companion$extensionBackend$2 extends Lambda implements a<ExtensionWindowLayoutInfoBackend> {

    /* renamed from: e, reason: collision with root package name */
    public static final WindowInfoTracker$Companion$extensionBackend$2 f4493e = new WindowInfoTracker$Companion$extensionBackend$2();

    WindowInfoTracker$Companion$extensionBackend$2() {
        super(0);
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final ExtensionWindowLayoutInfoBackend invoke() {
        boolean z10;
        String str;
        WindowLayoutComponent o10;
        try {
            ClassLoader classLoader = WindowInfoTracker.class.getClassLoader();
            SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = classLoader != null ? new SafeWindowLayoutComponentProvider(classLoader, new ConsumerAdapter(classLoader)) : null;
            if (safeWindowLayoutComponentProvider == null || (o10 = safeWindowLayoutComponentProvider.o()) == null) {
                return null;
            }
            k.d(classLoader, "loader");
            return new ExtensionWindowLayoutInfoBackend(o10, new ConsumerAdapter(classLoader));
        } catch (Throwable unused) {
            z10 = WindowInfoTracker.Companion.DEBUG;
            if (!z10) {
                return null;
            }
            str = WindowInfoTracker.Companion.TAG;
            Log.d(str, "Failed to load WindowExtensions");
            return null;
        }
    }
}
