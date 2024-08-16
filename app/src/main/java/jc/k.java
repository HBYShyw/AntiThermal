package jc;

import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public enum k implements j.a {
    FINAL(0, 0),
    OPEN(1, 1),
    ABSTRACT(2, 2),
    SEALED(3, 3);


    /* renamed from: j, reason: collision with root package name */
    private static j.b<k> f13616j = new j.b<k>() { // from class: jc.k.a
        @Override // qc.j.b
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public k findValueByNumber(int i10) {
            return k.a(i10);
        }
    };

    /* renamed from: e, reason: collision with root package name */
    private final int f13618e;

    k(int i10, int i11) {
        this.f13618e = i11;
    }

    public static k a(int i10) {
        if (i10 == 0) {
            return FINAL;
        }
        if (i10 == 1) {
            return OPEN;
        }
        if (i10 == 2) {
            return ABSTRACT;
        }
        if (i10 != 3) {
            return null;
        }
        return SEALED;
    }

    @Override // qc.j.a
    public final int getNumber() {
        return this.f13618e;
    }
}
