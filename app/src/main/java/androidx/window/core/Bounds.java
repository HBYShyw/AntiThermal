package androidx.window.core;

import android.graphics.Rect;
import java.util.Objects;
import kotlin.Metadata;
import za.k;

/* compiled from: Bounds.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0017\b\u0000\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u000f\u001a\u00020\t\u0012\u0006\u0010\u0011\u001a\u00020\t\u0012\u0006\u0010\u0013\u001a\u00020\t\u0012\u0006\u0010\u0016\u001a\u00020\t¢\u0006\u0004\b\u001c\u0010\u001dB\u0011\b\u0016\u0012\u0006\u0010\u001e\u001a\u00020\u0002¢\u0006\u0004\b\u001c\u0010\u001fJ\u0006\u0010\u0003\u001a\u00020\u0002J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\u0013\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\n\u001a\u00020\tH\u0016R\u0017\u0010\u000f\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0011\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\r\u0010\f\u001a\u0004\b\u0010\u0010\u000eR\u0017\u0010\u0013\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\f\u001a\u0004\b\u0012\u0010\u000eR\u0017\u0010\u0016\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u0014\u0010\f\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0017\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\u0018\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\u000eR\u0011\u0010\u001b\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a¨\u0006 "}, d2 = {"Landroidx/window/core/Bounds;", "", "Landroid/graphics/Rect;", "f", "", "toString", "other", "", "equals", "", "hashCode", "a", "I", "b", "()I", "left", "c", "top", "getRight", "right", "d", "getBottom", "bottom", "width", "height", "e", "()Z", "isZero", "<init>", "(IIII)V", "rect", "(Landroid/graphics/Rect;)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class Bounds {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final int left;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final int top;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final int right;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private final int bottom;

    public Bounds(int i10, int i11, int i12, int i13) {
        this.left = i10;
        this.top = i11;
        this.right = i12;
        this.bottom = i13;
    }

    public final int a() {
        return this.bottom - this.top;
    }

    /* renamed from: b, reason: from getter */
    public final int getLeft() {
        return this.left;
    }

    /* renamed from: c, reason: from getter */
    public final int getTop() {
        return this.top;
    }

    public final int d() {
        return this.right - this.left;
    }

    public final boolean e() {
        return a() == 0 && d() == 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!k.a(Bounds.class, other != null ? other.getClass() : null)) {
            return false;
        }
        Objects.requireNonNull(other, "null cannot be cast to non-null type androidx.window.core.Bounds");
        Bounds bounds = (Bounds) other;
        return this.left == bounds.left && this.top == bounds.top && this.right == bounds.right && this.bottom == bounds.bottom;
    }

    public final Rect f() {
        return new Rect(this.left, this.top, this.right, this.bottom);
    }

    public int hashCode() {
        return (((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom;
    }

    public String toString() {
        return Bounds.class.getSimpleName() + " { [" + this.left + ',' + this.top + ',' + this.right + ',' + this.bottom + "] }";
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public Bounds(Rect rect) {
        this(rect.left, rect.top, rect.right, rect.bottom);
        k.e(rect, "rect");
    }
}
