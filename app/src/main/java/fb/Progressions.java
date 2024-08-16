package fb;

import kotlin.collections.PrimitiveIterators;
import ta.progressionUtil;
import za.DefaultConstructorMarker;

/* compiled from: Progressions.kt */
/* renamed from: fb.a, reason: use source file name */
/* loaded from: classes2.dex */
public class Progressions implements Iterable<Integer>, ab.a {

    /* renamed from: h, reason: collision with root package name */
    public static final a f11406h = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final int f11407e;

    /* renamed from: f, reason: collision with root package name */
    private final int f11408f;

    /* renamed from: g, reason: collision with root package name */
    private final int f11409g;

    /* compiled from: Progressions.kt */
    /* renamed from: fb.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Progressions a(int i10, int i11, int i12) {
            return new Progressions(i10, i11, i12);
        }
    }

    public Progressions(int i10, int i11, int i12) {
        if (i12 == 0) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (i12 != Integer.MIN_VALUE) {
            this.f11407e = i10;
            this.f11408f = progressionUtil.b(i10, i11, i12);
            this.f11409g = i12;
            return;
        }
        throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
    }

    public final int d() {
        return this.f11407e;
    }

    public final int e() {
        return this.f11408f;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Progressions) {
            if (!isEmpty() || !((Progressions) obj).isEmpty()) {
                Progressions progressions = (Progressions) obj;
                if (this.f11407e != progressions.f11407e || this.f11408f != progressions.f11408f || this.f11409g != progressions.f11409g) {
                }
            }
            return true;
        }
        return false;
    }

    public final int f() {
        return this.f11409g;
    }

    @Override // java.lang.Iterable
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public PrimitiveIterators iterator() {
        return new ProgressionIterators(this.f11407e, this.f11408f, this.f11409g);
    }

    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return this.f11409g + (((this.f11407e * 31) + this.f11408f) * 31);
    }

    public boolean isEmpty() {
        if (this.f11409g > 0) {
            if (this.f11407e > this.f11408f) {
                return true;
            }
        } else if (this.f11407e < this.f11408f) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb2;
        int i10;
        if (this.f11409g > 0) {
            sb2 = new StringBuilder();
            sb2.append(this.f11407e);
            sb2.append("..");
            sb2.append(this.f11408f);
            sb2.append(" step ");
            i10 = this.f11409g;
        } else {
            sb2 = new StringBuilder();
            sb2.append(this.f11407e);
            sb2.append(" downTo ");
            sb2.append(this.f11408f);
            sb2.append(" step ");
            i10 = -this.f11409g;
        }
        sb2.append(i10);
        return sb2.toString();
    }
}
