package r4;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import me.BufferedSink;
import me.BufferedSource;
import me.g;
import me.q;

/* compiled from: JsonReader.java */
/* loaded from: classes.dex */
public abstract class c implements Closeable {

    /* renamed from: k, reason: collision with root package name */
    private static final String[] f17483k = new String[128];

    /* renamed from: e, reason: collision with root package name */
    int f17484e;

    /* renamed from: f, reason: collision with root package name */
    int[] f17485f = new int[32];

    /* renamed from: g, reason: collision with root package name */
    String[] f17486g = new String[32];

    /* renamed from: h, reason: collision with root package name */
    int[] f17487h = new int[32];

    /* renamed from: i, reason: collision with root package name */
    boolean f17488i;

    /* renamed from: j, reason: collision with root package name */
    boolean f17489j;

    /* compiled from: JsonReader.java */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        final String[] f17490a;

        /* renamed from: b, reason: collision with root package name */
        final q f17491b;

        private a(String[] strArr, q qVar) {
            this.f17490a = strArr;
            this.f17491b = qVar;
        }

        public static a a(String... strArr) {
            try {
                g[] gVarArr = new g[strArr.length];
                me.d dVar = new me.d();
                for (int i10 = 0; i10 < strArr.length; i10++) {
                    c.o0(dVar, strArr[i10]);
                    dVar.M();
                    gVarArr[i10] = dVar.e0();
                }
                return new a((String[]) strArr.clone(), q.l(gVarArr));
            } catch (IOException e10) {
                throw new AssertionError(e10);
            }
        }
    }

    /* compiled from: JsonReader.java */
    /* loaded from: classes.dex */
    public enum b {
        BEGIN_ARRAY,
        END_ARRAY,
        BEGIN_OBJECT,
        END_OBJECT,
        NAME,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL,
        END_DOCUMENT
    }

    static {
        for (int i10 = 0; i10 <= 31; i10++) {
            f17483k[i10] = String.format("\\u%04x", Integer.valueOf(i10));
        }
        String[] strArr = f17483k;
        strArr[34] = "\\\"";
        strArr[92] = "\\\\";
        strArr[9] = "\\t";
        strArr[8] = "\\b";
        strArr[10] = "\\n";
        strArr[13] = "\\r";
        strArr[12] = "\\f";
    }

    public static c a0(BufferedSource bufferedSource) {
        return new e(bufferedSource);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void o0(BufferedSink bufferedSink, String str) {
        int i10;
        String str2;
        String[] strArr = f17483k;
        bufferedSink.t(34);
        int length = str.length();
        int i11 = 0;
        while (i10 < length) {
            char charAt = str.charAt(i10);
            if (charAt < 128) {
                str2 = strArr[charAt];
                i10 = str2 == null ? i10 + 1 : 0;
                if (i11 < i10) {
                    bufferedSink.R(str, i11, i10);
                }
                bufferedSink.E(str2);
                i11 = i10 + 1;
            } else {
                if (charAt == 8232) {
                    str2 = "\\u2028";
                } else if (charAt == 8233) {
                    str2 = "\\u2029";
                }
                if (i11 < i10) {
                }
                bufferedSink.E(str2);
                i11 = i10 + 1;
            }
        }
        if (i11 < length) {
            bufferedSink.R(str, i11, length);
        }
        bufferedSink.t(34);
    }

    public abstract boolean L();

    public abstract double N();

    public abstract int S();

    public abstract String U();

    public abstract String X();

    public abstract void c();

    public abstract b e0();

    public final String getPath() {
        return d.a(this.f17484e, this.f17485f, this.f17486g, this.f17487h);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void h0(int i10) {
        int i11 = this.f17484e;
        int[] iArr = this.f17485f;
        if (i11 == iArr.length) {
            if (i11 != 256) {
                this.f17485f = Arrays.copyOf(iArr, iArr.length * 2);
                String[] strArr = this.f17486g;
                this.f17486g = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                int[] iArr2 = this.f17487h;
                this.f17487h = Arrays.copyOf(iArr2, iArr2.length * 2);
            } else {
                throw new r4.a("Nesting too deep at " + getPath());
            }
        }
        int[] iArr3 = this.f17485f;
        int i12 = this.f17484e;
        this.f17484e = i12 + 1;
        iArr3[i12] = i10;
    }

    public abstract int i0(a aVar);

    public abstract void j0();

    public abstract void m();

    public abstract void m0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final r4.b t0(String str) {
        throw new r4.b(str + " at path " + getPath());
    }

    public abstract void u();

    public abstract void v();

    public abstract boolean w();
}
