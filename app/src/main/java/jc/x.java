package jc;

import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public enum x implements j.a {
    INTERNAL(0, 0),
    PRIVATE(1, 1),
    PROTECTED(2, 2),
    PUBLIC(3, 3),
    PRIVATE_TO_THIS(4, 4),
    LOCAL(5, 5);


    /* renamed from: l, reason: collision with root package name */
    private static j.b<x> f13902l = new j.b<x>() { // from class: jc.x.a
        @Override // qc.j.b
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public x findValueByNumber(int i10) {
            return x.a(i10);
        }
    };

    /* renamed from: e, reason: collision with root package name */
    private final int f13904e;

    x(int i10, int i11) {
        this.f13904e = i11;
    }

    public static x a(int i10) {
        if (i10 == 0) {
            return INTERNAL;
        }
        if (i10 == 1) {
            return PRIVATE;
        }
        if (i10 == 2) {
            return PROTECTED;
        }
        if (i10 == 3) {
            return PUBLIC;
        }
        if (i10 == 4) {
            return PRIVATE_TO_THIS;
        }
        if (i10 != 5) {
            return null;
        }
        return LOCAL;
    }

    @Override // qc.j.a
    public final int getNumber() {
        return this.f13904e;
    }
}
