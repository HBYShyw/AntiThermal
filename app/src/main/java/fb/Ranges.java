package fb;

import za.k;

/* compiled from: Ranges.kt */
/* renamed from: fb.e, reason: use source file name */
/* loaded from: classes2.dex */
class Ranges {
    public static final void a(boolean z10, Number number) {
        k.e(number, "step");
        if (z10) {
            return;
        }
        throw new IllegalArgumentException("Step must be positive, was: " + number + '.');
    }
}
