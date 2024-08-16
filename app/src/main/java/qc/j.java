package qc;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* compiled from: Internal.java */
/* loaded from: classes2.dex */
public class j {

    /* renamed from: a, reason: collision with root package name */
    public static final byte[] f17315a;

    /* renamed from: b, reason: collision with root package name */
    public static final ByteBuffer f17316b;

    /* compiled from: Internal.java */
    /* loaded from: classes2.dex */
    public interface a {
        int getNumber();
    }

    /* compiled from: Internal.java */
    /* loaded from: classes2.dex */
    public interface b<T extends a> {
        T findValueByNumber(int i10);
    }

    static {
        byte[] bArr = new byte[0];
        f17315a = bArr;
        f17316b = ByteBuffer.wrap(bArr);
    }

    public static boolean a(byte[] bArr) {
        return y.e(bArr);
    }

    public static String b(byte[] bArr) {
        try {
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e10) {
            throw new RuntimeException("UTF-8 not supported?", e10);
        }
    }
}
