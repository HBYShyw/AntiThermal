package f1;

import java.util.Objects;

/* compiled from: CborOther.java */
/* renamed from: f1.n, reason: use source file name */
/* loaded from: classes.dex */
public class CborOther extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final CborOtherEnum f11282c;

    public CborOther(CborOtherEnum cborOtherEnum) {
        this(cborOtherEnum, -1L);
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborOther) {
            return super.equals(obj) && this.f11282c == ((CborOther) obj).g();
        }
        return false;
    }

    public CborOtherEnum g() {
        return this.f11282c;
    }

    @Override // f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.f11282c);
    }

    public String toString() {
        return this.f11282c.name();
    }

    public CborOther(CborOtherEnum cborOtherEnum, long j10) {
        super(7, j10);
        this.f11282c = cborOtherEnum;
    }
}
