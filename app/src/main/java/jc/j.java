package jc;

import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public enum j implements j.a {
    DECLARATION(0, 0),
    FAKE_OVERRIDE(1, 1),
    DELEGATION(2, 2),
    SYNTHESIZED(3, 3);


    /* renamed from: j, reason: collision with root package name */
    private static j.b<j> f13609j = new j.b<j>() { // from class: jc.j.a
        @Override // qc.j.b
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public j findValueByNumber(int i10) {
            return j.a(i10);
        }
    };

    /* renamed from: e, reason: collision with root package name */
    private final int f13611e;

    j(int i10, int i11) {
        this.f13611e = i11;
    }

    public static j a(int i10) {
        if (i10 == 0) {
            return DECLARATION;
        }
        if (i10 == 1) {
            return FAKE_OVERRIDE;
        }
        if (i10 == 2) {
            return DELEGATION;
        }
        if (i10 != 3) {
            return null;
        }
        return SYNTHESIZED;
    }

    @Override // qc.j.a
    public final int getNumber() {
        return this.f13611e;
    }
}
