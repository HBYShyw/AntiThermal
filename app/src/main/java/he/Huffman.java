package he;

import com.oplus.statistics.DataTypeConstants;
import kotlin.Metadata;
import kotlin.collections._ArraysJvm;
import me.BufferedSink;
import me.BufferedSource;

/* compiled from: Huffman.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0007B\t\b\u0002¢\u0006\u0004\b\u0013\u0010\u0014J \u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0002H\u0002J\u0016\u0010\f\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u000e\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\bJ\u001e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\n¨\u0006\u0015"}, d2 = {"Lhe/k;", "", "", "symbol", "code", "codeBitCount", "Lma/f0;", "a", "Lme/g;", "source", "Lme/e;", "sink", "c", "bytes", "d", "Lme/f;", "", "byteCount", "b", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class Huffman {

    /* renamed from: c, reason: collision with root package name */
    private static final byte[] f12458c;

    /* renamed from: a, reason: collision with root package name */
    public static final Huffman f12456a = new Huffman();

    /* renamed from: b, reason: collision with root package name */
    private static final int[] f12457b = {8184, 8388568, 268435426, 268435427, 268435428, 268435429, 268435430, 268435431, 268435432, 16777194, 1073741820, 268435433, 268435434, 1073741821, 268435435, 268435436, 268435437, 268435438, 268435439, 268435440, 268435441, 268435442, 1073741822, 268435443, 268435444, 268435445, 268435446, 268435447, 268435448, 268435449, 268435450, 268435451, 20, 1016, 1017, 4090, 8185, 21, 248, 2042, 1018, DataTypeConstants.PERIOD_DATA, 249, 2043, 250, 22, 23, 24, 0, 1, 2, 25, 26, 27, 28, 29, 30, 31, 92, 251, 32764, 32, 4091, DataTypeConstants.SETTING_KEY, 8186, 33, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 252, 115, 253, 8187, 524272, 8188, 16380, 34, 32765, 3, 35, 4, 36, 5, 37, 38, 39, 6, 116, 117, 40, 41, 42, 7, 43, 118, 44, 8, 9, 45, 119, 120, 121, 122, 123, 32766, 2044, 16381, 8189, 268435452, 1048550, 4194258, 1048551, 1048552, 4194259, 4194260, 4194261, 8388569, 4194262, 8388570, 8388571, 8388572, 8388573, 8388574, 16777195, 8388575, 16777196, 16777197, 4194263, 8388576, 16777198, 8388577, 8388578, 8388579, 8388580, 2097116, 4194264, 8388581, 4194265, 8388582, 8388583, 16777199, 4194266, 2097117, 1048553, 4194267, 4194268, 8388584, 8388585, 2097118, 8388586, 4194269, 4194270, 16777200, 2097119, 4194271, 8388587, 8388588, 2097120, 2097121, 4194272, 2097122, 8388589, 4194273, 8388590, 8388591, 1048554, 4194274, 4194275, 4194276, 8388592, 4194277, 4194278, 8388593, 67108832, 67108833, 1048555, 524273, 4194279, 8388594, 4194280, 33554412, 67108834, 67108835, 67108836, 134217694, 134217695, 67108837, 16777201, 33554413, 524274, 2097123, 67108838, 134217696, 134217697, 67108839, 134217698, 16777202, 2097124, 2097125, 67108840, 67108841, 268435453, 134217699, 134217700, 134217701, 1048556, 16777203, 1048557, 2097126, 4194281, 2097127, 2097128, 8388595, 4194282, 4194283, 33554414, 33554415, 16777204, 16777205, 67108842, 8388596, 67108843, 134217702, 67108844, 67108845, 134217703, 134217704, 134217705, 134217706, 134217707, 268435454, 134217708, 134217709, 134217710, 134217711, 134217712, 67108846};

    /* renamed from: d, reason: collision with root package name */
    private static final a f12459d = new a();

    static {
        byte[] bArr = {13, 23, 28, 28, 28, 28, 28, 28, 28, 24, 30, 28, 28, 30, 28, 28, 28, 28, 28, 28, 28, 28, 30, 28, 28, 28, 28, 28, 28, 28, 28, 28, 6, 10, 10, 12, 13, 6, 8, 11, 10, 10, 8, 11, 8, 6, 6, 6, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 8, 15, 6, 12, 10, 13, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 7, 8, 13, 19, 13, 14, 6, 15, 5, 6, 5, 6, 5, 6, 6, 6, 5, 7, 7, 6, 6, 6, 5, 6, 7, 6, 5, 5, 6, 7, 7, 7, 7, 7, 15, 11, 14, 13, 28, 20, 22, 20, 20, 22, 22, 22, 23, 22, 23, 23, 23, 23, 23, 24, 23, 24, 24, 22, 23, 24, 23, 23, 23, 23, 21, 22, 23, 22, 23, 23, 24, 22, 21, 20, 22, 22, 23, 23, 21, 23, 22, 22, 24, 21, 22, 23, 23, 21, 21, 22, 21, 23, 22, 23, 23, 20, 22, 22, 22, 23, 22, 22, 23, 26, 26, 20, 19, 22, 23, 22, 25, 26, 26, 26, 27, 27, 26, 24, 25, 19, 21, 26, 27, 27, 26, 27, 24, 21, 21, 26, 26, 28, 27, 27, 27, 20, 24, 20, 21, 22, 21, 21, 23, 22, 22, 25, 25, 24, 24, 26, 23, 26, 27, 26, 26, 27, 27, 27, 27, 27, 28, 27, 27, 27, 27, 27, 26};
        f12458c = bArr;
        int length = bArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            f12456a.a(i10, f12457b[i10], f12458c[i10]);
        }
    }

    private Huffman() {
    }

    private final void a(int i10, int i11, int i12) {
        a aVar = new a(i10, i12);
        a aVar2 = f12459d;
        while (i12 > 8) {
            i12 -= 8;
            int i13 = (i11 >>> i12) & 255;
            a[] f12460a = aVar2.getF12460a();
            za.k.b(f12460a);
            a aVar3 = f12460a[i13];
            if (aVar3 == null) {
                aVar3 = new a();
                f12460a[i13] = aVar3;
            }
            aVar2 = aVar3;
        }
        int i14 = 8 - i12;
        int i15 = (i11 << i14) & 255;
        a[] f12460a2 = aVar2.getF12460a();
        za.k.b(f12460a2);
        _ArraysJvm.m(f12460a2, aVar, i15, (1 << i14) + i15);
    }

    public final void b(BufferedSource bufferedSource, long j10, BufferedSink bufferedSink) {
        za.k.e(bufferedSource, "source");
        za.k.e(bufferedSink, "sink");
        a aVar = f12459d;
        int i10 = 0;
        long j11 = 0;
        int i11 = 0;
        while (j11 < j10) {
            j11++;
            i10 = (i10 << 8) | ae.d.d(bufferedSource.M(), 255);
            i11 += 8;
            while (i11 >= 8) {
                a[] f12460a = aVar.getF12460a();
                za.k.b(f12460a);
                aVar = f12460a[(i10 >>> (i11 - 8)) & 255];
                za.k.b(aVar);
                if (aVar.getF12460a() == null) {
                    bufferedSink.t(aVar.getF12461b());
                    i11 -= aVar.getF12462c();
                    aVar = f12459d;
                } else {
                    i11 -= 8;
                }
            }
        }
        while (i11 > 0) {
            a[] f12460a2 = aVar.getF12460a();
            za.k.b(f12460a2);
            a aVar2 = f12460a2[(i10 << (8 - i11)) & 255];
            za.k.b(aVar2);
            if (aVar2.getF12460a() != null || aVar2.getF12462c() > i11) {
                return;
            }
            bufferedSink.t(aVar2.getF12461b());
            i11 -= aVar2.getF12462c();
            aVar = f12459d;
        }
    }

    public final void c(me.g gVar, BufferedSink bufferedSink) {
        za.k.e(gVar, "source");
        za.k.e(bufferedSink, "sink");
        int t7 = gVar.t();
        int i10 = 0;
        long j10 = 0;
        int i11 = 0;
        while (i10 < t7) {
            int i12 = i10 + 1;
            int d10 = ae.d.d(gVar.e(i10), 255);
            int i13 = f12457b[d10];
            byte b10 = f12458c[d10];
            j10 = (j10 << b10) | i13;
            i11 += b10;
            while (i11 >= 8) {
                i11 -= 8;
                bufferedSink.t((int) (j10 >> i11));
            }
            i10 = i12;
        }
        if (i11 > 0) {
            bufferedSink.t((int) ((j10 << (8 - i11)) | (255 >>> i11)));
        }
    }

    public final int d(me.g bytes) {
        za.k.e(bytes, "bytes");
        long j10 = 0;
        for (int i10 = 0; i10 < bytes.t(); i10++) {
            j10 += f12458c[ae.d.d(bytes.e(i10), 255)];
        }
        return (int) ((j10 + 7) >> 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Huffman.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u000b\b\u0002\u0018\u00002\u00020\u0001B\t\b\u0016¢\u0006\u0004\b\u000e\u0010\u000fB\u0019\b\u0016\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\u0010\u001a\u00020\u0007¢\u0006\u0004\b\u000e\u0010\u0011R!\u0010\u0003\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0000\u0018\u00010\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006R\u0017\u0010\b\u001a\u00020\u00078\u0006¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\f\u001a\u00020\u00078\u0006¢\u0006\f\n\u0004\b\f\u0010\t\u001a\u0004\b\r\u0010\u000b¨\u0006\u0012"}, d2 = {"Lhe/k$a;", "", "", "children", "[Lhe/k$a;", "a", "()[Lhe/k$a;", "", "symbol", "I", "b", "()I", "terminalBitCount", "c", "<init>", "()V", "bits", "(II)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.k$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final a[] f12460a;

        /* renamed from: b, reason: collision with root package name */
        private final int f12461b;

        /* renamed from: c, reason: collision with root package name */
        private final int f12462c;

        public a() {
            this.f12460a = new a[256];
            this.f12461b = 0;
            this.f12462c = 0;
        }

        /* renamed from: a, reason: from getter */
        public final a[] getF12460a() {
            return this.f12460a;
        }

        /* renamed from: b, reason: from getter */
        public final int getF12461b() {
            return this.f12461b;
        }

        /* renamed from: c, reason: from getter */
        public final int getF12462c() {
            return this.f12462c;
        }

        public a(int i10, int i11) {
            this.f12460a = null;
            this.f12461b = i10;
            int i12 = i11 & 7;
            this.f12462c = i12 == 0 ? 8 : i12;
        }
    }
}
