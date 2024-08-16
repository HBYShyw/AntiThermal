package kotlinx.coroutines.internal;

import kotlin.Metadata;

@Metadata(bv = {}, d1 = {"kotlinx/coroutines/internal/c0", "kotlinx/coroutines/internal/d0"}, d2 = {}, k = 4, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class b0 {
    public static final int a() {
        return SystemProps.a();
    }

    public static final int b(String str, int i10, int i11, int i12) {
        return d0.a(str, i10, i11, i12);
    }

    public static final long c(String str, long j10, long j11, long j12) {
        return d0.b(str, j10, j11, j12);
    }

    public static final String d(String str) {
        return SystemProps.b(str);
    }

    public static final boolean e(String str, boolean z10) {
        return d0.c(str, z10);
    }
}
