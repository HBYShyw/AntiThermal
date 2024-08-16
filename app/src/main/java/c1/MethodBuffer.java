package c1;

import android.text.TextUtils;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import d1.CeMemType;
import d1.CryptoEngCmdType;
import e1.i;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* compiled from: MethodBuffer.java */
/* renamed from: c1.b, reason: use source file name */
/* loaded from: classes.dex */
public class MethodBuffer {

    /* renamed from: a, reason: collision with root package name */
    private final CryptoEngCmdType f4742a;

    /* renamed from: b, reason: collision with root package name */
    private int f4743b = 0;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f4744c = null;

    public MethodBuffer(CryptoEngCmdType cryptoEngCmdType) {
        this.f4742a = cryptoEngCmdType;
    }

    private MethodBuffer a(byte[] bArr) {
        if (!f.f(bArr)) {
            byte[] bArr2 = this.f4744c;
            if (bArr2 == null) {
                this.f4744c = bArr;
            } else {
                this.f4744c = f(bArr2, bArr);
            }
            this.f4743b++;
        } else {
            i.d("MethodBuffer", "appendBytes bytes is empty");
        }
        return this;
    }

    private static byte[] f(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0) {
            return bArr2;
        }
        if (bArr2 == null || bArr2.length == 0) {
            return bArr;
        }
        byte[] copyOf = Arrays.copyOf(bArr, bArr.length + bArr2.length);
        System.arraycopy(bArr2, 0, copyOf, bArr.length, bArr2.length);
        return copyOf;
    }

    private static byte[] g(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return f(f(bArr, bArr2), bArr3);
    }

    private static byte[] h(CeMemType ceMemType, byte[] bArr) {
        if (ceMemType != null) {
            try {
                if (!f.f(bArr)) {
                    byte[] e10 = f.e(ceMemType.b());
                    byte[] e11 = f.e(bArr.length);
                    if (!f.f(e10) && !f.f(bArr) && !f.f(e11)) {
                        return g(e10, e11, bArr);
                    }
                    i.d("MethodBuffer", "getParamBuffer at least one array is empty");
                    return null;
                }
            } catch (Exception e12) {
                e12.printStackTrace();
                return null;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getParamBuffer methodParamType = ");
        sb2.append(ceMemType);
        sb2.append(", paramBuffer = ");
        sb2.append(bArr == null ? null : new String(bArr, StandardCharsets.UTF_8));
        i.d("MethodBuffer", sb2.toString());
        return null;
    }

    public MethodBuffer b(CeMemType ceMemType, byte[] bArr) {
        a(h(ceMemType, bArr));
        return this;
    }

    public MethodBuffer c(CeMemType ceMemType, int i10) {
        a(h(ceMemType, f.e(i10)));
        return this;
    }

    public MethodBuffer d(CeMemType ceMemType, String str) {
        if (ceMemType != null && !TextUtils.isEmpty(str)) {
            a(h(ceMemType, f.d(str)));
        }
        return this;
    }

    public byte[] e() {
        CryptoEngCmdType cryptoEngCmdType = this.f4742a;
        if (cryptoEngCmdType != null) {
            return g(f(f.e(cryptoEngCmdType.b()), f.e(DataLinkConstants.RUS_UPDATE)), f.e(this.f4743b), this.f4744c);
        }
        return null;
    }
}
