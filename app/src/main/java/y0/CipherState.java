package y0;

import e1.KeyUtil;
import i0.EncryptException;
import j0.AesUtil;
import j0.HkdfUtil;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.SecretKey;
import s0.CryptoParameters;
import t0.InvalidArgumentException;
import z0.CipherInfo;
import z0.NoiseCipherEnum;
import z0.NonceModeEnum;

/* compiled from: CipherState.java */
/* renamed from: y0.a, reason: use source file name */
/* loaded from: classes.dex */
public class CipherState {

    /* renamed from: c, reason: collision with root package name */
    private final NoiseCipherEnum f19747c;

    /* renamed from: d, reason: collision with root package name */
    private final int f19748d;

    /* renamed from: e, reason: collision with root package name */
    private final int f19749e;

    /* renamed from: f, reason: collision with root package name */
    private NonceModeEnum f19750f;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f19746b = null;

    /* renamed from: a, reason: collision with root package name */
    private int f19745a = 0;

    /* renamed from: g, reason: collision with root package name */
    private byte[] f19751g = null;

    public CipherState(NoiseCipherEnum noiseCipherEnum, NonceModeEnum nonceModeEnum) {
        this.f19747c = noiseCipherEnum;
        this.f19749e = noiseCipherEnum.c();
        this.f19748d = noiseCipherEnum.b();
        this.f19750f = nonceModeEnum;
    }

    private void b(byte[] bArr, byte[] bArr2, int i10, byte[] bArr3, int i11, byte[] bArr4) {
        if (i10 >= 0 && i10 <= bArr3.length && i11 >= 0 && i11 <= bArr4.length) {
            if (bArr2 == null) {
                bArr2 = "".getBytes(StandardCharsets.UTF_8);
            }
            byte[] b10 = HkdfUtil.b(bArr, bArr2, "Noise Key".getBytes(StandardCharsets.UTF_8), i10);
            byte[] b11 = HkdfUtil.b(bArr, bArr2, "Noise IV".getBytes(StandardCharsets.UTF_8), i11);
            System.arraycopy(b10, 0, bArr3, 0, i10);
            System.arraycopy(b11, 0, bArr4, 0, i11);
            return;
        }
        throw new IllegalArgumentException();
    }

    public byte[] a(byte[] bArr, CipherInfo cipherInfo) {
        SecretKey c10;
        byte[] bArr2;
        try {
            byte[] b10 = cipherInfo.b();
            if (b10 != null) {
                if (this.f19750f == NonceModeEnum.TIME_XOR_IV) {
                    int i10 = this.f19749e;
                    byte[] bArr3 = new byte[i10];
                    int i11 = this.f19748d;
                    byte[] bArr4 = new byte[i11];
                    b(this.f19746b, this.f19751g, i10, bArr3, i11, bArr4);
                    c10 = KeyUtil.c(bArr3, "AES");
                    long e10 = cipherInfo.e();
                    if (e10 != -1) {
                        byte[] bArr5 = new byte[8];
                        NoiseUtil.q(e10, bArr5, 0);
                        bArr2 = NoiseUtil.b(bArr4, bArr5);
                    } else {
                        throw new InvalidArgumentException("The timestamp is not found");
                    }
                } else {
                    c10 = KeyUtil.c(this.f19746b, "AES");
                    if (this.f19750f == NonceModeEnum.RANDOM_IV) {
                        bArr2 = cipherInfo.c();
                        if (bArr2 == null) {
                            throw new InvalidArgumentException("The random iv is not found");
                        }
                    } else {
                        byte[] bArr6 = new byte[this.f19748d];
                        Arrays.fill(bArr6, (byte) 0);
                        if (this.f19750f == NonceModeEnum.EXPLICIT_NONCE) {
                            int d10 = cipherInfo.d();
                            if (d10 != -1) {
                                NoiseUtil.l(d10, bArr6, this.f19748d - 4);
                            } else {
                                throw new InvalidArgumentException("The explicit nonce is not found");
                            }
                        } else {
                            NoiseUtil.l(this.f19745a, bArr6, this.f19748d - 4);
                        }
                        bArr2 = bArr6;
                    }
                }
                this.f19745a++;
                return AesUtil.a(new CryptoParameters.c().j(this.f19747c.a()).m(c10).l(bArr2).i(bArr).k(b10).h());
            }
            throw new InvalidArgumentException("The cipher text is not found");
        } catch (EncryptException | InvalidArgumentException e11) {
            throw new EncryptException(e11);
        }
    }

    public CipherInfo c(byte[] bArr, byte[] bArr2) {
        SecretKey c10;
        try {
            CipherInfo cipherInfo = new CipherInfo();
            int i10 = this.f19748d;
            byte[] bArr3 = new byte[i10];
            if (this.f19750f == NonceModeEnum.TIME_XOR_IV) {
                int i11 = this.f19749e;
                byte[] bArr4 = new byte[i11];
                b(this.f19746b, this.f19751g, i11, bArr4, i10, bArr3);
                c10 = KeyUtil.c(bArr4, "AES");
                long currentTimeMillis = System.currentTimeMillis();
                cipherInfo.i(currentTimeMillis);
                byte[] bArr5 = new byte[8];
                NoiseUtil.q(currentTimeMillis, bArr5, 0);
                bArr3 = NoiseUtil.b(bArr3, bArr5);
            } else {
                c10 = KeyUtil.c(this.f19746b, "AES");
                if (this.f19750f == NonceModeEnum.RANDOM_IV) {
                    new SecureRandom().nextBytes(bArr3);
                    cipherInfo.g(bArr3);
                } else {
                    Arrays.fill(bArr3, (byte) 0);
                    NoiseUtil.l(this.f19745a, bArr3, this.f19748d - 4);
                    if (this.f19750f == NonceModeEnum.EXPLICIT_NONCE) {
                        cipherInfo.h(this.f19745a);
                    }
                }
            }
            cipherInfo.f(AesUtil.b(new CryptoParameters.c().j(this.f19747c.a()).m(c10).l(bArr3).i(bArr).k(bArr2).h()).a());
            this.f19745a++;
            return cipherInfo;
        } catch (EncryptException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public CipherState d(byte[] bArr) {
        CipherState cipherState = new CipherState(this.f19747c, this.f19750f);
        cipherState.g(bArr);
        return cipherState;
    }

    public byte[] e() {
        return this.f19746b;
    }

    public int f() {
        return this.f19749e;
    }

    public void g(byte[] bArr) {
        this.f19746b = bArr;
        this.f19745a = 0;
    }

    public void h(NonceModeEnum nonceModeEnum) {
        this.f19750f = nonceModeEnum;
    }
}
