package f1;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/* compiled from: CborObject.java */
/* renamed from: f1.m, reason: use source file name */
/* loaded from: classes.dex */
public abstract class CborObject {

    /* renamed from: a, reason: collision with root package name */
    private final int f11280a;

    /* renamed from: b, reason: collision with root package name */
    private long f11281b;

    /* JADX INFO: Access modifiers changed from: protected */
    public CborObject(int i10, long j10) {
        this.f11280a = i10;
        e(j10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(boolean z10, String str) {
        if (!z10) {
            throw new IllegalArgumentException(str);
        }
    }

    public int b() {
        return this.f11280a;
    }

    public long c() {
        return this.f11281b;
    }

    public boolean d() {
        return this.f11281b != -1;
    }

    public void e(long j10) {
        if (CborTagType.a(j10)) {
            this.f11281b = j10;
            return;
        }
        throw new IllegalArgumentException("the given tag value is considered invalid.");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CborObject)) {
            return false;
        }
        CborObject cborObject = (CborObject) obj;
        return this.f11281b == cborObject.f11281b && this.f11280a == cborObject.f11280a;
    }

    public final byte[] f() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new CborEncoder(byteArrayOutputStream).g(this);
        return byteArrayOutputStream.toByteArray();
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.f11280a), Long.valueOf(this.f11281b));
    }
}
