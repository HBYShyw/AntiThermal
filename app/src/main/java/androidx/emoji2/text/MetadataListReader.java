package androidx.emoji2.text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import t.MetadataList;

/* compiled from: MetadataListReader.java */
/* renamed from: androidx.emoji2.text.l, reason: use source file name */
/* loaded from: classes.dex */
class MetadataListReader {

    /* compiled from: MetadataListReader.java */
    /* renamed from: androidx.emoji2.text.l$a */
    /* loaded from: classes.dex */
    private static class a implements c {

        /* renamed from: a, reason: collision with root package name */
        private final ByteBuffer f2649a;

        a(ByteBuffer byteBuffer) {
            this.f2649a = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }

        @Override // androidx.emoji2.text.MetadataListReader.c
        public int a() {
            return MetadataListReader.d(this.f2649a.getShort());
        }

        @Override // androidx.emoji2.text.MetadataListReader.c
        public int b() {
            return this.f2649a.getInt();
        }

        @Override // androidx.emoji2.text.MetadataListReader.c
        public void c(int i10) {
            ByteBuffer byteBuffer = this.f2649a;
            byteBuffer.position(byteBuffer.position() + i10);
        }

        @Override // androidx.emoji2.text.MetadataListReader.c
        public long d() {
            return MetadataListReader.c(this.f2649a.getInt());
        }

        @Override // androidx.emoji2.text.MetadataListReader.c
        public long getPosition() {
            return this.f2649a.position();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MetadataListReader.java */
    /* renamed from: androidx.emoji2.text.l$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final long f2650a;

        /* renamed from: b, reason: collision with root package name */
        private final long f2651b;

        b(long j10, long j11) {
            this.f2650a = j10;
            this.f2651b = j11;
        }

        long a() {
            return this.f2650a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MetadataListReader.java */
    /* renamed from: androidx.emoji2.text.l$c */
    /* loaded from: classes.dex */
    public interface c {
        int a();

        int b();

        void c(int i10);

        long d();

        long getPosition();
    }

    private static b a(c cVar) {
        long j10;
        cVar.c(4);
        int a10 = cVar.a();
        if (a10 <= 100) {
            cVar.c(6);
            int i10 = 0;
            while (true) {
                if (i10 >= a10) {
                    j10 = -1;
                    break;
                }
                int b10 = cVar.b();
                cVar.c(4);
                j10 = cVar.d();
                cVar.c(4);
                if (1835365473 == b10) {
                    break;
                }
                i10++;
            }
            if (j10 != -1) {
                cVar.c((int) (j10 - cVar.getPosition()));
                cVar.c(12);
                long d10 = cVar.d();
                for (int i11 = 0; i11 < d10; i11++) {
                    int b11 = cVar.b();
                    long d11 = cVar.d();
                    long d12 = cVar.d();
                    if (1164798569 == b11 || 1701669481 == b11) {
                        return new b(d11 + j10, d12);
                    }
                }
            }
            throw new IOException("Cannot read metadata.");
        }
        throw new IOException("Cannot read metadata.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MetadataList b(ByteBuffer byteBuffer) {
        ByteBuffer duplicate = byteBuffer.duplicate();
        duplicate.position((int) a(new a(duplicate)).a());
        return MetadataList.h(duplicate);
    }

    static long c(int i10) {
        return i10 & 4294967295L;
    }

    static int d(short s7) {
        return s7 & 65535;
    }
}
