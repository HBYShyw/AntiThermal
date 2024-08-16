package sd;

import fb.PrimitiveRanges;
import fb._Ranges;
import java.util.Iterator;
import java.util.NoSuchElementException;
import rd.Sequence;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Strings.kt */
/* loaded from: classes2.dex */
public final class e implements Sequence<PrimitiveRanges> {

    /* renamed from: a, reason: collision with root package name */
    private final CharSequence f18477a;

    /* renamed from: b, reason: collision with root package name */
    private final int f18478b;

    /* renamed from: c, reason: collision with root package name */
    private final int f18479c;

    /* renamed from: d, reason: collision with root package name */
    private final ya.p<CharSequence, Integer, ma.o<Integer, Integer>> f18480d;

    /* compiled from: Strings.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<PrimitiveRanges>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private int f18481e = -1;

        /* renamed from: f, reason: collision with root package name */
        private int f18482f;

        /* renamed from: g, reason: collision with root package name */
        private int f18483g;

        /* renamed from: h, reason: collision with root package name */
        private PrimitiveRanges f18484h;

        /* renamed from: i, reason: collision with root package name */
        private int f18485i;

        a() {
            int h10;
            h10 = _Ranges.h(e.this.f18478b, 0, e.this.f18477a.length());
            this.f18482f = h10;
            this.f18483g = h10;
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0021, code lost:
        
            if (r0 < r6.f18486j.f18479c) goto L9;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private final void b() {
            PrimitiveRanges k10;
            int P;
            int P2;
            if (this.f18483g < 0) {
                this.f18481e = 0;
                this.f18484h = null;
                return;
            }
            if (e.this.f18479c > 0) {
                int i10 = this.f18485i + 1;
                this.f18485i = i10;
            }
            if (this.f18483g <= e.this.f18477a.length()) {
                ma.o oVar = (ma.o) e.this.f18480d.invoke(e.this.f18477a, Integer.valueOf(this.f18483g));
                if (oVar == null) {
                    int i11 = this.f18482f;
                    P = v.P(e.this.f18477a);
                    this.f18484h = new PrimitiveRanges(i11, P);
                    this.f18483g = -1;
                } else {
                    int intValue = ((Number) oVar.a()).intValue();
                    int intValue2 = ((Number) oVar.b()).intValue();
                    k10 = _Ranges.k(this.f18482f, intValue);
                    this.f18484h = k10;
                    int i12 = intValue + intValue2;
                    this.f18482f = i12;
                    this.f18483g = i12 + (intValue2 == 0 ? 1 : 0);
                }
                this.f18481e = 1;
            }
            int i13 = this.f18482f;
            P2 = v.P(e.this.f18477a);
            this.f18484h = new PrimitiveRanges(i13, P2);
            this.f18483g = -1;
            this.f18481e = 1;
        }

        @Override // java.util.Iterator
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public PrimitiveRanges next() {
            if (this.f18481e == -1) {
                b();
            }
            if (this.f18481e != 0) {
                PrimitiveRanges primitiveRanges = this.f18484h;
                za.k.c(primitiveRanges, "null cannot be cast to non-null type kotlin.ranges.IntRange");
                this.f18484h = null;
                this.f18481e = -1;
                return primitiveRanges;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f18481e == -1) {
                b();
            }
            return this.f18481e == 1;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public e(CharSequence charSequence, int i10, int i11, ya.p<? super CharSequence, ? super Integer, ma.o<Integer, Integer>> pVar) {
        za.k.e(charSequence, "input");
        za.k.e(pVar, "getNextMatch");
        this.f18477a = charSequence;
        this.f18478b = i10;
        this.f18479c = i11;
        this.f18480d = pVar;
    }

    @Override // rd.Sequence
    public Iterator<PrimitiveRanges> iterator() {
        return new a();
    }
}
