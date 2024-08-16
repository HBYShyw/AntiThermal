package androidx.window.sidecar;

import android.app.Activity;
import android.graphics.Rect;
import androidx.window.core.Bounds;
import androidx.window.extensions.layout.FoldingFeature;
import androidx.window.extensions.layout.WindowLayoutInfo;
import androidx.window.sidecar.FoldingFeature;
import androidx.window.sidecar.HardwareFoldingFeature;
import com.oplus.thermalcontrol.ThermalControlConfig;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import za.k;

/* compiled from: ExtensionsWindowLayoutInfoAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J!\u0010\u000b\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u001f\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\rH\u0000¢\u0006\u0004\b\u0010\u0010\u0011¨\u0006\u0014"}, d2 = {"Landroidx/window/layout/ExtensionsWindowLayoutInfoAdapter;", "", "Landroid/app/Activity;", "activity", "Landroidx/window/core/Bounds;", "bounds", "", "c", "Landroidx/window/extensions/layout/FoldingFeature;", "oemFeature", "Landroidx/window/layout/FoldingFeature;", "a", "(Landroid/app/Activity;Landroidx/window/extensions/layout/FoldingFeature;)Landroidx/window/layout/FoldingFeature;", "Landroidx/window/extensions/layout/WindowLayoutInfo;", "info", "Landroidx/window/layout/WindowLayoutInfo;", "b", "(Landroid/app/Activity;Landroidx/window/extensions/layout/WindowLayoutInfo;)Landroidx/window/layout/WindowLayoutInfo;", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class ExtensionsWindowLayoutInfoAdapter {

    /* renamed from: a, reason: collision with root package name */
    public static final ExtensionsWindowLayoutInfoAdapter f4424a = new ExtensionsWindowLayoutInfoAdapter();

    private ExtensionsWindowLayoutInfoAdapter() {
    }

    private final boolean c(Activity activity, Bounds bounds) {
        Rect a10 = WindowMetricsCalculatorCompat.f4509a.a(activity).a();
        if (bounds.e()) {
            return false;
        }
        if (bounds.d() != a10.width() && bounds.a() != a10.height()) {
            return false;
        }
        if (bounds.d() >= a10.width() || bounds.a() >= a10.height()) {
            return (bounds.d() == a10.width() && bounds.a() == a10.height()) ? false : true;
        }
        return false;
    }

    public final FoldingFeature a(Activity activity, FoldingFeature oemFeature) {
        HardwareFoldingFeature.Type a10;
        FoldingFeature.State state;
        k.e(activity, "activity");
        k.e(oemFeature, "oemFeature");
        int type = oemFeature.getType();
        if (type == 1) {
            a10 = HardwareFoldingFeature.Type.INSTANCE.a();
        } else {
            if (type != 2) {
                return null;
            }
            a10 = HardwareFoldingFeature.Type.INSTANCE.b();
        }
        int state2 = oemFeature.getState();
        if (state2 == 1) {
            state = FoldingFeature.State.f4434c;
        } else {
            if (state2 != 2) {
                return null;
            }
            state = FoldingFeature.State.f4435d;
        }
        Rect bounds = oemFeature.getBounds();
        k.d(bounds, "oemFeature.bounds");
        if (!c(activity, new Bounds(bounds))) {
            return null;
        }
        Rect bounds2 = oemFeature.getBounds();
        k.d(bounds2, "oemFeature.bounds");
        return new HardwareFoldingFeature(new Bounds(bounds2), a10, state);
    }

    public final WindowLayoutInfo b(Activity activity, WindowLayoutInfo info) {
        FoldingFeature foldingFeature;
        k.e(activity, "activity");
        k.e(info, "info");
        List<androidx.window.extensions.layout.FoldingFeature> displayFeatures = info.getDisplayFeatures();
        k.d(displayFeatures, "info.displayFeatures");
        ArrayList arrayList = new ArrayList();
        for (androidx.window.extensions.layout.FoldingFeature foldingFeature2 : displayFeatures) {
            if (foldingFeature2 instanceof androidx.window.extensions.layout.FoldingFeature) {
                ExtensionsWindowLayoutInfoAdapter extensionsWindowLayoutInfoAdapter = f4424a;
                k.d(foldingFeature2, ThermalControlConfig.CONFIG_TYPE_FEATURE);
                foldingFeature = extensionsWindowLayoutInfoAdapter.a(activity, foldingFeature2);
            } else {
                foldingFeature = null;
            }
            if (foldingFeature != null) {
                arrayList.add(foldingFeature);
            }
        }
        return new WindowLayoutInfo(arrayList);
    }
}
