package androidx.window.sidecar;

import android.graphics.Rect;
import androidx.window.core.Bounds;
import kotlin.Metadata;
import za.k;

/* compiled from: WindowMetrics.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0011\b\u0000\u0012\u0006\u0010\f\u001a\u00020\t¢\u0006\u0004\b\u0010\u0010\u0011B\u0011\b\u0017\u0012\u0006\u0010\u000f\u001a\u00020\r¢\u0006\u0004\b\u0010\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0013\u0010\u0006\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\b\u001a\u00020\u0007H\u0016R\u0014\u0010\f\u001a\u00020\t8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u000bR\u0011\u0010\u000f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000e¨\u0006\u0013"}, d2 = {"Landroidx/window/layout/WindowMetrics;", "", "", "toString", "other", "", "equals", "", "hashCode", "Landroidx/window/core/Bounds;", "a", "Landroidx/window/core/Bounds;", "_bounds", "Landroid/graphics/Rect;", "()Landroid/graphics/Rect;", "bounds", "<init>", "(Landroidx/window/core/Bounds;)V", "(Landroid/graphics/Rect;)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class WindowMetrics {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final Bounds _bounds;

    public WindowMetrics(Bounds bounds) {
        k.e(bounds, "_bounds");
        this._bounds = bounds;
    }

    public final Rect a() {
        return this._bounds.f();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !k.a(WindowMetrics.class, other.getClass())) {
            return false;
        }
        return k.a(this._bounds, ((WindowMetrics) other)._bounds);
    }

    public int hashCode() {
        return this._bounds.hashCode();
    }

    public String toString() {
        return "WindowMetrics { bounds: " + a() + " }";
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public WindowMetrics(Rect rect) {
        this(new Bounds(rect));
        k.e(rect, "bounds");
    }
}
