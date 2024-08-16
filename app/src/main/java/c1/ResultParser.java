package c1;

import d1.CeMemType;
import d1.CryptoEngCmdType;
import e1.i;
import java.util.Arrays;
import java.util.HashMap;

/* compiled from: ResultParser.java */
/* renamed from: c1.c, reason: use source file name */
/* loaded from: classes.dex */
public class ResultParser {

    /* compiled from: ResultParser.java */
    /* renamed from: c1.c$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private CeMemType f4745a = null;

        /* renamed from: b, reason: collision with root package name */
        private int f4746b = 0;

        /* renamed from: c, reason: collision with root package name */
        private byte[] f4747c = null;

        public byte[] a() {
            return this.f4747c;
        }

        int b() {
            return this.f4746b;
        }

        CeMemType c() {
            return this.f4745a;
        }

        void d(byte[] bArr) {
            this.f4747c = bArr;
        }

        void e(int i10) {
            this.f4746b = i10;
        }

        void f(CeMemType ceMemType) {
            this.f4745a = ceMemType;
        }
    }

    /* compiled from: ResultParser.java */
    /* renamed from: c1.c$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private CryptoEngCmdType f4748a = null;

        /* renamed from: b, reason: collision with root package name */
        private boolean f4749b = false;

        /* renamed from: c, reason: collision with root package name */
        private int f4750c = 0;

        /* renamed from: d, reason: collision with root package name */
        private final HashMap<CeMemType, a> f4751d = new HashMap<>();

        void a(a aVar) {
            if (aVar == null || aVar.c() == null) {
                return;
            }
            this.f4751d.put(aVar.c(), aVar);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public CryptoEngCmdType b() {
            return this.f4748a;
        }

        public a c(CeMemType ceMemType) {
            if (ceMemType != null) {
                return this.f4751d.get(ceMemType);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean d() {
            return this.f4749b;
        }

        void e(boolean z10) {
            this.f4749b = z10;
        }

        void f(CryptoEngCmdType cryptoEngCmdType) {
            this.f4748a = cryptoEngCmdType;
        }

        void g(int i10) {
            this.f4750c = i10;
        }
    }

    public static b a(byte[] bArr) {
        if (f.f(bArr)) {
            i.a("ResultParser", "parse buffer is empty");
            return null;
        }
        b bVar = new b();
        int length = bArr.length;
        int i10 = 12;
        if (length >= 12) {
            byte[] copyOfRange = Arrays.copyOfRange(bArr, 0, 4);
            if (!f.f(copyOfRange)) {
                int a10 = f.a(copyOfRange);
                if (a10 <= 0) {
                    i.d("ResultParser", "parse invalid methodTypeCode = " + a10);
                    return null;
                }
                CryptoEngCmdType a11 = CryptoEngCmdType.a(a10);
                if (a11 != null) {
                    bVar.f(a11);
                    byte[] copyOfRange2 = Arrays.copyOfRange(bArr, 4, 8);
                    if (!f.f(copyOfRange2)) {
                        int a12 = f.a(copyOfRange2);
                        if (a12 != 0) {
                            i.b("ResultParser", "parse exeResultCode = " + a12);
                            return bVar;
                        }
                        bVar.e(true);
                        byte[] copyOfRange3 = Arrays.copyOfRange(bArr, 8, 12);
                        if (!f.f(copyOfRange3)) {
                            int a13 = f.a(copyOfRange3);
                            if (a13 > 0) {
                                bVar.g(a13);
                                for (int i11 = 0; i11 < a13; i11++) {
                                    int i12 = i10 + 8;
                                    if (i12 >= length) {
                                        i.d("ResultParser", "parse invalid (hasParsedBytesNum + RESULT_PARAM_HEAD_LEN) = " + i12 + ", totalBufferNum = " + length);
                                        return null;
                                    }
                                    a aVar = new a();
                                    int i13 = i10 + 4;
                                    byte[] copyOfRange4 = Arrays.copyOfRange(bArr, i10, i13);
                                    if (f.f(copyOfRange4)) {
                                        i.d("ResultParser", "parse CeMemTypeBytes is empty i = " + i11);
                                        return null;
                                    }
                                    CeMemType a14 = CeMemType.a(f.a(copyOfRange4));
                                    if (a14 == null) {
                                        i.d("ResultParser", "parse ceMemType is null, i = " + i11);
                                        return null;
                                    }
                                    aVar.f(a14);
                                    int i14 = i13 + 4;
                                    byte[] copyOfRange5 = Arrays.copyOfRange(bArr, i13, i14);
                                    if (f.f(copyOfRange5)) {
                                        i.d("ResultParser", "parse bufferLenBytes is empty i = " + i11);
                                        return null;
                                    }
                                    int a15 = f.a(copyOfRange5);
                                    if (a15 > 0) {
                                        aVar.e(a15);
                                        int b10 = aVar.b();
                                        int i15 = i14 + b10;
                                        if (i15 > length) {
                                            i.d("ResultParser", "parse bufferLenBytes is empty i = " + i11 + ", hasParsedBytesNum = " + i14 + ", bufferlen = " + b10 + ", totalBufferNum = " + length);
                                            return null;
                                        }
                                        byte[] copyOfRange6 = Arrays.copyOfRange(bArr, i14, i15);
                                        if (f.f(copyOfRange6)) {
                                            i.d("ResultParser", "parse resultBuffer is empty i = " + i11);
                                            return null;
                                        }
                                        aVar.d(copyOfRange6);
                                        bVar.a(aVar);
                                        i10 = i15;
                                    } else {
                                        i.d("ResultParser", "parse bufferLen is invalid, i = " + i11);
                                        i10 = i13;
                                    }
                                }
                            } else {
                                i.d("ResultParser", "parse invalid paramsNum = " + a13);
                            }
                        } else {
                            i.d("ResultParser", "parse paramsNumBytes is empty");
                            return null;
                        }
                    } else {
                        i.d("ResultParser", "parse isExeSuccessBytes is empty");
                        return null;
                    }
                } else {
                    i.d("ResultParser", "parse invalid methodType is null");
                    return null;
                }
            } else {
                i.d("ResultParser", "parse methodTypeBytes is empty");
                return null;
            }
        } else {
            i.a("ResultParser", "parse totalBufferNum = " + length);
        }
        return bVar;
    }
}
