package z0;

import e1.Base64Utils;
import f1.CborByteString;
import f1.CborHelper;
import f1.CborMap;
import f1.CborUnsignedInteger;
import org.json.JSONObject;

/* compiled from: CipherInfo.java */
/* renamed from: z0.a, reason: use source file name */
/* loaded from: classes.dex */
public class CipherInfo {

    /* renamed from: a, reason: collision with root package name */
    private int f20165a = -1;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f20166b = null;

    /* renamed from: c, reason: collision with root package name */
    private long f20167c = -1;

    /* renamed from: d, reason: collision with root package name */
    private byte[] f20168d = null;

    public static CipherInfo a(byte[] bArr) {
        CipherInfo cipherInfo = new CipherInfo();
        CborMap b10 = CborHelper.b(bArr);
        if (b10.i("nonce")) {
            cipherInfo.h(((Integer) ((CborUnsignedInteger) b10.k("nonce")).h()).intValue());
        }
        if (b10.i("iv")) {
            cipherInfo.g(((CborByteString) b10.k("iv")).i());
        }
        if (b10.i("time")) {
            cipherInfo.i(((Long) ((CborUnsignedInteger) b10.k("time")).h()).longValue());
        }
        if (b10.i("cipher")) {
            cipherInfo.f(((CborByteString) b10.k("cipher")).i());
        }
        return cipherInfo;
    }

    public byte[] b() {
        return this.f20168d;
    }

    public byte[] c() {
        return this.f20166b;
    }

    public int d() {
        return this.f20165a;
    }

    public long e() {
        return this.f20167c;
    }

    public void f(byte[] bArr) {
        this.f20168d = bArr;
    }

    public void g(byte[] bArr) {
        this.f20166b = bArr;
    }

    public void h(int i10) {
        this.f20165a = i10;
    }

    public void i(long j10) {
        this.f20167c = j10;
    }

    public byte[] j() {
        CborMap cborMap = new CborMap();
        int i10 = this.f20165a;
        if (i10 != -1) {
            cborMap.o("nonce", new CborUnsignedInteger(i10));
        }
        byte[] bArr = this.f20166b;
        if (bArr != null) {
            cborMap.o("iv", new CborByteString(bArr));
        }
        long j10 = this.f20167c;
        if (j10 != -1) {
            cborMap.o("time", new CborUnsignedInteger(j10));
        }
        byte[] bArr2 = this.f20168d;
        if (bArr2 != null) {
            cborMap.o("cipher", new CborByteString(bArr2));
        }
        return cborMap.f();
    }

    public String k() {
        JSONObject jSONObject = new JSONObject();
        int i10 = this.f20165a;
        if (i10 != -1) {
            jSONObject.put("nonce", i10);
        }
        byte[] bArr = this.f20166b;
        if (bArr != null) {
            jSONObject.put("iv", Base64Utils.b(bArr));
        }
        long j10 = this.f20167c;
        if (j10 != -1) {
            jSONObject.put("time", j10);
        }
        byte[] bArr2 = this.f20168d;
        if (bArr2 != null) {
            jSONObject.put("cipher", Base64Utils.b(bArr2));
        }
        return jSONObject.toString();
    }
}
