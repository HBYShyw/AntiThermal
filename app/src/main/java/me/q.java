package me;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;
import kotlin.collections.AbstractList;
import kotlin.collections.MutableCollectionsJVM;
import kotlin.collections._Arrays;
import za.DefaultConstructorMarker;

/* compiled from: Options.kt */
/* loaded from: classes2.dex */
public final class q extends AbstractList<g> implements RandomAccess {

    /* renamed from: h, reason: collision with root package name */
    public static final a f15512h = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private final g[] f15513f;

    /* renamed from: g, reason: collision with root package name */
    private final int[] f15514g;

    /* compiled from: Options.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final void a(long j10, d dVar, int i10, List<? extends g> list, int i11, int i12, List<Integer> list2) {
            int i13;
            int i14;
            int i15;
            int i16;
            d dVar2;
            int i17 = i10;
            if (i11 < i12) {
                for (int i18 = i11; i18 < i12; i18++) {
                    if (!(list.get(i18).t() >= i17)) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                }
                g gVar = list.get(i11);
                g gVar2 = list.get(i12 - 1);
                int i19 = -1;
                if (i17 == gVar.t()) {
                    int intValue = list2.get(i11).intValue();
                    int i20 = i11 + 1;
                    g gVar3 = list.get(i20);
                    i13 = i20;
                    i14 = intValue;
                    gVar = gVar3;
                } else {
                    i13 = i11;
                    i14 = -1;
                }
                if (gVar.e(i17) != gVar2.e(i17)) {
                    int i21 = 1;
                    for (int i22 = i13 + 1; i22 < i12; i22++) {
                        if (list.get(i22 - 1).e(i17) != list.get(i22).e(i17)) {
                            i21++;
                        }
                    }
                    long c10 = j10 + c(dVar) + 2 + (i21 * 2);
                    dVar.p(i21);
                    dVar.p(i14);
                    for (int i23 = i13; i23 < i12; i23++) {
                        byte e10 = list.get(i23).e(i17);
                        if (i23 == i13 || e10 != list.get(i23 - 1).e(i17)) {
                            dVar.p(e10 & 255);
                        }
                    }
                    d dVar3 = new d();
                    while (i13 < i12) {
                        byte e11 = list.get(i13).e(i17);
                        int i24 = i13 + 1;
                        int i25 = i24;
                        while (true) {
                            if (i25 >= i12) {
                                i15 = i12;
                                break;
                            } else {
                                if (e11 != list.get(i25).e(i17)) {
                                    i15 = i25;
                                    break;
                                }
                                i25++;
                            }
                        }
                        if (i24 == i15 && i17 + 1 == list.get(i13).t()) {
                            dVar.p(list2.get(i13).intValue());
                            i16 = i15;
                            dVar2 = dVar3;
                        } else {
                            dVar.p(((int) (c10 + c(dVar3))) * i19);
                            i16 = i15;
                            dVar2 = dVar3;
                            a(c10, dVar3, i17 + 1, list, i13, i15, list2);
                        }
                        dVar3 = dVar2;
                        i13 = i16;
                        i19 = -1;
                    }
                    dVar.C0(dVar3);
                    return;
                }
                int min = Math.min(gVar.t(), gVar2.t());
                int i26 = 0;
                for (int i27 = i17; i27 < min && gVar.e(i27) == gVar2.e(i27); i27++) {
                    i26++;
                }
                long c11 = j10 + c(dVar) + 2 + i26 + 1;
                dVar.p(-i26);
                dVar.p(i14);
                int i28 = i17 + i26;
                while (i17 < i28) {
                    dVar.p(gVar.e(i17) & 255);
                    i17++;
                }
                if (i13 + 1 == i12) {
                    if (i28 == list.get(i13).t()) {
                        dVar.p(list2.get(i13).intValue());
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                d dVar4 = new d();
                dVar.p(((int) (c(dVar4) + c11)) * (-1));
                a(c11, dVar4, i28, list, i13, i12, list2);
                dVar.C0(dVar4);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        static /* synthetic */ void b(a aVar, long j10, d dVar, int i10, List list, int i11, int i12, List list2, int i13, Object obj) {
            aVar.a((i13 & 1) != 0 ? 0L : j10, dVar, (i13 & 4) != 0 ? 0 : i10, list, (i13 & 16) != 0 ? 0 : i11, (i13 & 32) != 0 ? list.size() : i12, list2);
        }

        private final long c(d dVar) {
            return dVar.v0() / 4;
        }

        /* JADX WARN: Code restructure failed: missing block: B:46:0x00e9, code lost:
        
            continue;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final q d(g... gVarArr) {
            List o02;
            List p10;
            int i10;
            za.k.e(gVarArr, "byteStrings");
            int i11 = 0;
            DefaultConstructorMarker defaultConstructorMarker = null;
            if (gVarArr.length == 0) {
                return new q(new g[0], new int[]{0, -1}, defaultConstructorMarker);
            }
            o02 = _Arrays.o0(gVarArr);
            MutableCollectionsJVM.x(o02);
            ArrayList arrayList = new ArrayList(gVarArr.length);
            for (g gVar : gVarArr) {
                arrayList.add(-1);
            }
            Integer[] numArr = (Integer[]) arrayList.toArray(new Integer[0]);
            p10 = kotlin.collections.r.p(Arrays.copyOf(numArr, numArr.length));
            int length = gVarArr.length;
            int i12 = 0;
            int i13 = 0;
            while (i12 < length) {
                i10 = kotlin.collections.r.i(o02, gVarArr[i12], 0, 0, 6, null);
                p10.set(i10, Integer.valueOf(i13));
                i12++;
                i13++;
            }
            if (((g) o02.get(0)).t() > 0) {
                int i14 = 0;
                while (i14 < o02.size()) {
                    g gVar2 = (g) o02.get(i14);
                    int i15 = i14 + 1;
                    int i16 = i15;
                    while (i16 < o02.size()) {
                        g gVar3 = (g) o02.get(i16);
                        if (gVar3.u(gVar2)) {
                            if (!(gVar3.t() != gVar2.t())) {
                                throw new IllegalArgumentException(("duplicate option: " + gVar3).toString());
                            }
                            if (((Number) p10.get(i16)).intValue() > ((Number) p10.get(i14)).intValue()) {
                                o02.remove(i16);
                                p10.remove(i16);
                            } else {
                                i16++;
                            }
                        }
                    }
                    i14 = i15;
                }
                d dVar = new d();
                b(this, 0L, dVar, 0, o02, 0, 0, p10, 53, null);
                int[] iArr = new int[(int) c(dVar)];
                while (!dVar.s()) {
                    iArr[i11] = dVar.o();
                    i11++;
                }
                Object[] copyOf = Arrays.copyOf(gVarArr, gVarArr.length);
                za.k.d(copyOf, "copyOf(this, size)");
                return new q((g[]) copyOf, iArr, defaultConstructorMarker);
            }
            throw new IllegalArgumentException("the empty byte string is not a supported option".toString());
        }
    }

    private q(g[] gVarArr, int[] iArr) {
        this.f15513f = gVarArr;
        this.f15514g = iArr;
    }

    public /* synthetic */ q(g[] gVarArr, int[] iArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVarArr, iArr);
    }

    public static final q l(g... gVarArr) {
        return f15512h.d(gVarArr);
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof g) {
            return e((g) obj);
        }
        return false;
    }

    @Override // kotlin.collections.AbstractCollection
    public int d() {
        return this.f15513f.length;
    }

    public /* bridge */ boolean e(g gVar) {
        return super.contains(gVar);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public g get(int i10) {
        return this.f15513f[i10];
    }

    public final g[] g() {
        return this.f15513f;
    }

    public final int[] h() {
        return this.f15514g;
    }

    public /* bridge */ int i(g gVar) {
        return super.indexOf(gVar);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof g) {
            return i((g) obj);
        }
        return -1;
    }

    public /* bridge */ int k(g gVar) {
        return super.lastIndexOf(gVar);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof g) {
            return k((g) obj);
        }
        return -1;
    }
}
