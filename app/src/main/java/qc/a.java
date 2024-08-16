package qc;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import qc.q;

/* compiled from: AbstractMessageLite.java */
/* loaded from: classes2.dex */
public abstract class a implements q {

    /* renamed from: e, reason: collision with root package name */
    protected int f17251e = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w b() {
        return new w(this);
    }

    public void c(OutputStream outputStream) {
        int serializedSize = getSerializedSize();
        f J = f.J(outputStream, f.u(f.v(serializedSize) + serializedSize));
        J.o0(serializedSize);
        a(J);
        J.I();
    }

    /* compiled from: AbstractMessageLite.java */
    /* renamed from: qc.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static abstract class AbstractC0092a<BuilderType extends AbstractC0092a> implements q.a {
        /* JADX INFO: Access modifiers changed from: protected */
        public static w c(q qVar) {
            return new w(qVar);
        }

        @Override // qc.q.a
        /* renamed from: b */
        public abstract BuilderType m(e eVar, g gVar);

        /* compiled from: AbstractMessageLite.java */
        /* renamed from: qc.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0093a extends FilterInputStream {

            /* renamed from: e, reason: collision with root package name */
            private int f17252e;

            /* JADX INFO: Access modifiers changed from: package-private */
            public C0093a(InputStream inputStream, int i10) {
                super(inputStream);
                this.f17252e = i10;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int available() {
                return Math.min(super.available(), this.f17252e);
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read() {
                if (this.f17252e <= 0) {
                    return -1;
                }
                int read = super.read();
                if (read >= 0) {
                    this.f17252e--;
                }
                return read;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public long skip(long j10) {
                long skip = super.skip(Math.min(j10, this.f17252e));
                if (skip >= 0) {
                    this.f17252e = (int) (this.f17252e - skip);
                }
                return skip;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read(byte[] bArr, int i10, int i11) {
                int i12 = this.f17252e;
                if (i12 <= 0) {
                    return -1;
                }
                int read = super.read(bArr, i10, Math.min(i11, i12));
                if (read >= 0) {
                    this.f17252e -= read;
                }
                return read;
            }
        }
    }
}
