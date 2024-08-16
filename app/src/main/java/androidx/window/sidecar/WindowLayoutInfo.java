package androidx.window.sidecar;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections._Collections;
import za.k;

/* compiled from: WindowLayoutInfo.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0017\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0004\b\u000f\u0010\u0010J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0013\u0010\u0006\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\b\u001a\u00020\u0007H\u0016R\u001d\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\t8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\u000b\u0010\r¨\u0006\u0011"}, d2 = {"Landroidx/window/layout/WindowLayoutInfo;", "", "", "toString", "other", "", "equals", "", "hashCode", "", "Landroidx/window/layout/DisplayFeature;", "a", "Ljava/util/List;", "()Ljava/util/List;", "displayFeatures", "<init>", "(Ljava/util/List;)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class WindowLayoutInfo {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final List<DisplayFeature> displayFeatures;

    /* JADX WARN: Multi-variable type inference failed */
    public WindowLayoutInfo(List<? extends DisplayFeature> list) {
        k.e(list, "displayFeatures");
        this.displayFeatures = list;
    }

    public final List<DisplayFeature> a() {
        return this.displayFeatures;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !k.a(WindowLayoutInfo.class, other.getClass())) {
            return false;
        }
        return k.a(this.displayFeatures, ((WindowLayoutInfo) other).displayFeatures);
    }

    public int hashCode() {
        return this.displayFeatures.hashCode();
    }

    public String toString() {
        String c02;
        c02 = _Collections.c0(this.displayFeatures, ", ", "WindowLayoutInfo{ DisplayFeatures[", "] }", 0, null, null, 56, null);
        return c02;
    }
}
