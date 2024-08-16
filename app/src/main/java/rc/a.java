package rc;

import za.DefaultConstructorMarker;

/* compiled from: DescriptorRenderer.kt */
/* loaded from: classes2.dex */
public enum a {
    NO_ARGUMENTS(false, false, 3, null),
    UNLESS_EMPTY(true, false, 2, null),
    ALWAYS_PARENTHESIZED(true, true);


    /* renamed from: e, reason: collision with root package name */
    private final boolean f17697e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f17698f;

    a(boolean z10, boolean z11) {
        this.f17697e = z10;
        this.f17698f = z11;
    }

    public final boolean b() {
        return this.f17697e;
    }

    public final boolean c() {
        return this.f17698f;
    }

    /* synthetic */ a(boolean z10, boolean z11, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? false : z10, (i10 & 2) != 0 ? false : z11);
    }
}
