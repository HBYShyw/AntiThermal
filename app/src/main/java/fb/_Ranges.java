package fb;

import fb.Progressions;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Ranges.kt */
/* renamed from: fb.f, reason: use source file name */
/* loaded from: classes2.dex */
public class _Ranges extends Ranges {
    public static float b(float f10, float f11) {
        return f10 < f11 ? f11 : f10;
    }

    public static int c(int i10, int i11) {
        return i10 < i11 ? i11 : i10;
    }

    public static long d(long j10, long j11) {
        return j10 < j11 ? j11 : j10;
    }

    public static float e(float f10, float f11) {
        return f10 > f11 ? f11 : f10;
    }

    public static int f(int i10, int i11) {
        return i10 > i11 ? i11 : i10;
    }

    public static long g(long j10, long j11) {
        return j10 > j11 ? j11 : j10;
    }

    public static int h(int i10, int i11, int i12) {
        if (i11 <= i12) {
            return i10 < i11 ? i11 : i10 > i12 ? i12 : i10;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + i12 + " is less than minimum " + i11 + '.');
    }

    public static Progressions i(int i10, int i11) {
        return Progressions.f11406h.a(i10, i11, -1);
    }

    public static Progressions j(Progressions progressions, int i10) {
        k.e(progressions, "<this>");
        Ranges.a(i10 > 0, Integer.valueOf(i10));
        Progressions.a aVar = Progressions.f11406h;
        int d10 = progressions.d();
        int e10 = progressions.e();
        if (progressions.f() <= 0) {
            i10 = -i10;
        }
        return aVar.a(d10, e10, i10);
    }

    public static PrimitiveRanges k(int i10, int i11) {
        if (i11 <= Integer.MIN_VALUE) {
            return PrimitiveRanges.f11414i.a();
        }
        return new PrimitiveRanges(i10, i11 - 1);
    }
}
