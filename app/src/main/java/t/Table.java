package t;

import java.nio.ByteBuffer;

/* compiled from: Table.java */
/* renamed from: t.c, reason: use source file name */
/* loaded from: classes.dex */
public class Table {

    /* renamed from: a, reason: collision with root package name */
    protected int f18509a;

    /* renamed from: b, reason: collision with root package name */
    protected ByteBuffer f18510b;

    /* renamed from: c, reason: collision with root package name */
    private int f18511c;

    /* renamed from: d, reason: collision with root package name */
    private int f18512d;

    /* renamed from: e, reason: collision with root package name */
    d f18513e = d.a();

    /* JADX INFO: Access modifiers changed from: protected */
    public int a(int i10) {
        return i10 + this.f18510b.getInt(i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int b(int i10) {
        if (i10 < this.f18512d) {
            return this.f18510b.getShort(this.f18511c + i10);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void c(int i10, ByteBuffer byteBuffer) {
        this.f18510b = byteBuffer;
        if (byteBuffer != null) {
            this.f18509a = i10;
            int i11 = i10 - byteBuffer.getInt(i10);
            this.f18511c = i11;
            this.f18512d = this.f18510b.getShort(i11);
            return;
        }
        this.f18509a = 0;
        this.f18511c = 0;
        this.f18512d = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int d(int i10) {
        int i11 = i10 + this.f18509a;
        return i11 + this.f18510b.getInt(i11) + 4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int e(int i10) {
        int i11 = i10 + this.f18509a;
        return this.f18510b.getInt(i11 + this.f18510b.getInt(i11));
    }
}
