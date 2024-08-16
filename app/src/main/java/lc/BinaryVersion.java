package lc;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections._Arrays;
import kotlin.collections._ArraysJvm;
import kotlin.collections._Collections;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: BinaryVersion.kt */
/* renamed from: lc.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class BinaryVersion {

    /* renamed from: f, reason: collision with root package name */
    public static final a f14660f = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final int[] f14661a;

    /* renamed from: b, reason: collision with root package name */
    private final int f14662b;

    /* renamed from: c, reason: collision with root package name */
    private final int f14663c;

    /* renamed from: d, reason: collision with root package name */
    private final int f14664d;

    /* renamed from: e, reason: collision with root package name */
    private final List<Integer> f14665e;

    /* compiled from: BinaryVersion.kt */
    /* renamed from: lc.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public BinaryVersion(int... iArr) {
        Integer E;
        Integer E2;
        Integer E3;
        List<Integer> j10;
        List d10;
        k.e(iArr, "numbers");
        this.f14661a = iArr;
        E = _Arrays.E(iArr, 0);
        this.f14662b = E != null ? E.intValue() : -1;
        E2 = _Arrays.E(iArr, 1);
        this.f14663c = E2 != null ? E2.intValue() : -1;
        E3 = _Arrays.E(iArr, 2);
        this.f14664d = E3 != null ? E3.intValue() : -1;
        if (iArr.length > 3) {
            if (iArr.length <= 1024) {
                d10 = _ArraysJvm.d(iArr);
                j10 = _Collections.z0(d10.subList(3, iArr.length));
            } else {
                throw new IllegalArgumentException("BinaryVersion with length more than 1024 are not supported. Provided length " + iArr.length + '.');
            }
        } else {
            j10 = r.j();
        }
        this.f14665e = j10;
    }

    public final int a() {
        return this.f14662b;
    }

    public final int b() {
        return this.f14663c;
    }

    public final boolean c(int i10, int i11, int i12) {
        int i13 = this.f14662b;
        if (i13 > i10) {
            return true;
        }
        if (i13 < i10) {
            return false;
        }
        int i14 = this.f14663c;
        if (i14 > i11) {
            return true;
        }
        return i14 >= i11 && this.f14664d >= i12;
    }

    public final boolean d(BinaryVersion binaryVersion) {
        k.e(binaryVersion, "version");
        return c(binaryVersion.f14662b, binaryVersion.f14663c, binaryVersion.f14664d);
    }

    public final boolean e(int i10, int i11, int i12) {
        int i13 = this.f14662b;
        if (i13 < i10) {
            return true;
        }
        if (i13 > i10) {
            return false;
        }
        int i14 = this.f14663c;
        if (i14 < i11) {
            return true;
        }
        return i14 <= i11 && this.f14664d <= i12;
    }

    public boolean equals(Object obj) {
        if (obj != null && k.a(getClass(), obj.getClass())) {
            BinaryVersion binaryVersion = (BinaryVersion) obj;
            if (this.f14662b == binaryVersion.f14662b && this.f14663c == binaryVersion.f14663c && this.f14664d == binaryVersion.f14664d && k.a(this.f14665e, binaryVersion.f14665e)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean f(BinaryVersion binaryVersion) {
        k.e(binaryVersion, "ourVersion");
        int i10 = this.f14662b;
        if (i10 == 0) {
            if (binaryVersion.f14662b == 0 && this.f14663c == binaryVersion.f14663c) {
                return true;
            }
        } else if (i10 == binaryVersion.f14662b && this.f14663c <= binaryVersion.f14663c) {
            return true;
        }
        return false;
    }

    public final int[] g() {
        return this.f14661a;
    }

    public int hashCode() {
        int i10 = this.f14662b;
        int i11 = i10 + (i10 * 31) + this.f14663c;
        int i12 = i11 + (i11 * 31) + this.f14664d;
        return i12 + (i12 * 31) + this.f14665e.hashCode();
    }

    public String toString() {
        String c02;
        int[] g6 = g();
        ArrayList arrayList = new ArrayList();
        int length = g6.length;
        for (int i10 = 0; i10 < length; i10++) {
            int i11 = g6[i10];
            if (!(i11 != -1)) {
                break;
            }
            arrayList.add(Integer.valueOf(i11));
        }
        if (arrayList.isEmpty()) {
            return "unknown";
        }
        c02 = _Collections.c0(arrayList, ".", null, null, 0, null, null, 62, null);
        return c02;
    }
}
