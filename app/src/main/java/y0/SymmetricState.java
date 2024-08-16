package y0;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import z0.CipherInfo;
import z0.CipherStatePair;
import z0.NoiseCipherEnum;
import z0.NoiseHashEnum;
import z0.NonceModeEnum;

/* compiled from: SymmetricState.java */
/* renamed from: y0.h, reason: use source file name */
/* loaded from: classes.dex */
public class SymmetricState {

    /* renamed from: a, reason: collision with root package name */
    private CipherState f19793a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f19794b;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f19795c;

    /* renamed from: d, reason: collision with root package name */
    private byte[] f19796d;

    /* renamed from: e, reason: collision with root package name */
    private MessageDigest f19797e;

    public SymmetricState(String str, NoiseCipherEnum noiseCipherEnum, NoiseHashEnum noiseHashEnum, NonceModeEnum nonceModeEnum) {
        MessageDigest g6 = NoiseUtil.g(noiseHashEnum);
        this.f19797e = g6;
        int digestLength = g6.getDigestLength();
        this.f19795c = new byte[digestLength];
        this.f19796d = new byte[digestLength];
        this.f19794b = false;
        this.f19793a = new CipherState(noiseCipherEnum, nonceModeEnum);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= digestLength) {
            System.arraycopy(bytes, 0, this.f19795c, 0, bytes.length);
            byte[] bArr = this.f19795c;
            Arrays.fill(bArr, bytes.length, bArr.length, (byte) 0);
        } else {
            this.f19795c = c(bytes);
        }
        System.arraycopy(this.f19795c, 0, this.f19796d, 0, digestLength);
    }

    private byte[] c(byte[]... bArr) {
        this.f19797e.reset();
        for (byte[] bArr2 : bArr) {
            this.f19797e.update(bArr2);
        }
        return this.f19797e.digest();
    }

    private void d(byte[] bArr, byte[] bArr2, int i10, int i11, byte[]... bArr3) {
        int length = bArr3.length;
        int digestLength = this.f19797e.getDigestLength();
        int i12 = digestLength + 1;
        byte[] bArr4 = new byte[i12];
        byte[] f10 = f(bArr, bArr2, i10, i11);
        bArr4[0] = 1;
        byte[] f11 = f(f10, bArr4, 0, 1);
        System.arraycopy(f11, 0, bArr3[0], 0, bArr3[0].length);
        System.arraycopy(f11, 0, bArr4, 0, digestLength);
        if (length == 1) {
            return;
        }
        bArr4[digestLength] = 2;
        byte[] f12 = f(f10, bArr4, 0, i12);
        System.arraycopy(f12, 0, bArr3[1], 0, bArr3[1].length);
        System.arraycopy(f12, 0, bArr4, 0, digestLength);
        if (length == 2) {
            return;
        }
        bArr4[digestLength] = 3;
        byte[] f13 = f(f10, bArr4, 0, i12);
        System.arraycopy(f13, 0, bArr3[2], 0, bArr3[2].length);
        System.arraycopy(f13, 0, bArr4, 0, digestLength);
    }

    private void e(byte[] bArr, byte[] bArr2, byte[]... bArr3) {
        d(bArr, bArr2, 0, bArr2.length, bArr3);
    }

    private byte[] f(byte[] bArr, byte[] bArr2, int i10, int i11) {
        if (this.f19797e.getAlgorithm().equals("SHA-256")) {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(bArr, "HmacSHA256"));
            mac.update(bArr2, i10, i11);
            return mac.doFinal();
        }
        throw new NoSuchAlgorithmException("Hmac " + this.f19797e.getAlgorithm() + " not found");
    }

    public byte[] a(byte[] bArr) {
        byte[] a10 = this.f19794b ? this.f19793a.a(this.f19795c, CipherInfo.a(bArr)) : bArr;
        g(bArr);
        return a10;
    }

    public byte[] b(byte[] bArr) {
        if (this.f19794b) {
            bArr = this.f19793a.c(this.f19795c, bArr).j();
        }
        g(bArr);
        return bArr;
    }

    public void g(byte[] bArr) {
        this.f19795c = c(this.f19795c, bArr);
    }

    public void h(byte[] bArr) {
        byte[] bArr2 = new byte[this.f19793a.f()];
        byte[] bArr3 = this.f19796d;
        e(bArr3, bArr, bArr3, bArr2);
        this.f19793a.g(bArr2);
        this.f19794b = true;
    }

    public void i(byte[] bArr) {
        int digestLength = this.f19797e.getDigestLength();
        byte[] bArr2 = new byte[this.f19793a.f()];
        int f10 = this.f19793a.f();
        byte[] bArr3 = new byte[f10];
        byte[] bArr4 = this.f19796d;
        e(bArr4, bArr, bArr4, bArr2, bArr3);
        g(bArr2);
        if (f10 > digestLength) {
            byte[] bArr5 = new byte[digestLength];
            System.arraycopy(bArr3, 0, bArr5, 0, digestLength);
            bArr3 = bArr5;
        }
        this.f19793a.g(bArr3);
        this.f19794b = true;
    }

    public CipherStatePair j() {
        byte[] bArr = new byte[this.f19793a.f()];
        byte[] bArr2 = new byte[this.f19793a.f()];
        d(this.f19796d, new byte[0], 0, 0, bArr, bArr2);
        return new CipherStatePair(this.f19793a.d(bArr), this.f19793a.d(bArr2));
    }
}
