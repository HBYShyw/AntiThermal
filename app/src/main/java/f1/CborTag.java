package f1;

import java.util.Objects;

/* compiled from: CborTag.java */
/* renamed from: f1.s, reason: use source file name */
/* loaded from: classes.dex */
public class CborTag extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final long f11302c;

    public CborTag(long j10) {
        super(6, -1L);
        this.f11302c = j10;
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborTag) {
            return super.equals(obj) && this.f11302c == ((CborTag) obj).f11302c;
        }
        return false;
    }

    public long g() {
        return this.f11302c;
    }

    @Override // f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Long.valueOf(this.f11302c));
    }

    public String toString() {
        return "Tag(" + this.f11302c + ")";
    }
}
