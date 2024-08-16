package y0;

import e1.KeyUtil;
import f1.CborException;
import i0.EncryptException;
import j0.CertUtil;
import j0.EccUtil;
import j0.HashUtil;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import r0.EllipticCurveOverFpHelper;
import r0.EllipticCurvePoint;
import t0.InvalidArgumentException;
import z0.CipherStatePair;
import z0.HandshakePattern;
import z0.NoiseCipherEnum;
import z0.NoiseDHEnum;
import z0.NoiseHandshakeEnum;
import z0.NoiseHashEnum;
import z0.NonceModeEnum;

/* compiled from: HandshakeState.java */
/* renamed from: y0.b, reason: use source file name */
/* loaded from: classes.dex */
public class HandshakeState {

    /* renamed from: x, reason: collision with root package name */
    private static final byte[] f19752x = new byte[0];

    /* renamed from: a, reason: collision with root package name */
    private final NoiseHandshakeEnum f19753a;

    /* renamed from: b, reason: collision with root package name */
    private SymmetricState f19754b;

    /* renamed from: c, reason: collision with root package name */
    private PrivateKey f19755c;

    /* renamed from: d, reason: collision with root package name */
    private PublicKey f19756d;

    /* renamed from: e, reason: collision with root package name */
    private byte[] f19757e;

    /* renamed from: f, reason: collision with root package name */
    private X509Certificate f19758f;

    /* renamed from: g, reason: collision with root package name */
    private KeyPair f19759g;

    /* renamed from: h, reason: collision with root package name */
    private PublicKey f19760h;

    /* renamed from: i, reason: collision with root package name */
    private PublicKey f19761i;

    /* renamed from: j, reason: collision with root package name */
    private byte[] f19762j;

    /* renamed from: k, reason: collision with root package name */
    private final short[] f19763k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f19764l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean f19765m;

    /* renamed from: n, reason: collision with root package name */
    private int f19766n;

    /* renamed from: o, reason: collision with root package name */
    private byte[] f19767o;

    /* renamed from: p, reason: collision with root package name */
    private final NoiseDHEnum f19768p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f19769q;

    /* renamed from: r, reason: collision with root package name */
    private CipherStatePair f19770r;

    /* renamed from: s, reason: collision with root package name */
    private int f19771s;

    /* renamed from: t, reason: collision with root package name */
    private int f19772t;

    /* renamed from: u, reason: collision with root package name */
    private int f19773u;

    /* renamed from: v, reason: collision with root package name */
    private RSChecker f19774v;

    /* renamed from: w, reason: collision with root package name */
    private int f19775w;

    /* compiled from: HandshakeState.java */
    /* renamed from: y0.b$b */
    /* loaded from: classes.dex */
    private static class b extends RSChecker {
        private b() {
        }
    }

    public HandshakeState(NoiseHandshakeEnum noiseHandshakeEnum, NoiseDHEnum noiseDHEnum, NoiseCipherEnum noiseCipherEnum, NoiseHashEnum noiseHashEnum, NonceModeEnum nonceModeEnum, int i10) {
        try {
            this.f19753a = noiseHandshakeEnum;
            this.f19768p = noiseDHEnum;
            short[] b10 = HandshakePattern.b(noiseHandshakeEnum);
            this.f19763k = b10;
            if (b10 != null) {
                this.f19754b = new SymmetricState(NoiseUtil.i(noiseHandshakeEnum, noiseDHEnum, noiseCipherEnum, noiseHashEnum), noiseCipherEnum, noiseHashEnum, nonceModeEnum);
                boolean z10 = i10 == 1;
                this.f19765m = z10;
                this.f19764l = z10;
                this.f19769q = false;
                this.f19766n = 2;
                this.f19771s = 1;
                this.f19772t = 1;
                this.f19773u = 1;
                this.f19774v = new b();
                this.f19775w = 0;
                return;
            }
            throw new IllegalArgumentException("Handshake pattern is not recognized");
        } catch (NoSuchAlgorithmException e10) {
            throw new EncryptException(e10);
        }
    }

    private byte[] a(PublicKey publicKey) {
        if (publicKey != null) {
            int i10 = this.f19771s;
            if (i10 != 5 && i10 != 6) {
                return publicKey.getEncoded();
            }
            if (publicKey instanceof ECPublicKey) {
                ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
                return EllipticCurveOverFpHelper.b(eCPublicKey.getParams().getCurve(), eCPublicKey.getW(), this.f19771s == 5);
            }
            throw new InvalidKeyException("Only ec public key is supported");
        }
        throw new InvalidKeyException("Missing public key");
    }

    private byte[] b(PrivateKey privateKey, PublicKey publicKey) {
        if (this.f19768p == NoiseDHEnum.SECP256R1) {
            return EccUtil.a(privateKey, publicKey);
        }
        throw new NoSuchAlgorithmException(this.f19768p + " not found");
    }

    private KeyPair c() {
        return KeyUtil.e(this.f19768p.a());
    }

    private byte[] e(PublicKey publicKey) {
        int i10 = this.f19773u;
        if (i10 != 5 && i10 != 6) {
            return publicKey.getEncoded();
        }
        if (publicKey instanceof ECPublicKey) {
            ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
            return EllipticCurveOverFpHelper.b(eCPublicKey.getParams().getCurve(), eCPublicKey.getW(), this.f19773u == 5);
        }
        throw new InvalidKeyException("Only ec public key is supported");
    }

    private byte[] g() {
        PublicKey publicKey = this.f19756d;
        if (publicKey != null) {
            return a(publicKey);
        }
        throw new InvalidKeyException("Missing local static public key");
    }

    private byte[] h() {
        int i10 = this.f19772t;
        if (i10 == 2) {
            byte[] bArr = this.f19757e;
            if (bArr != null) {
                return bArr;
            }
            PublicKey publicKey = this.f19756d;
            if (publicKey != null) {
                return HashUtil.c(publicKey.getEncoded());
            }
            throw new InvalidKeyException("Missing local static public key");
        }
        if (i10 == 3) {
            X509Certificate x509Certificate = this.f19758f;
            if (x509Certificate != null) {
                return x509Certificate.getEncoded();
            }
            throw new InvalidKeyException("Missing local static certificate");
        }
        if (i10 == 4) {
            X509Certificate x509Certificate2 = this.f19758f;
            if (x509Certificate2 != null) {
                return x509Certificate2.getSerialNumber().toByteArray();
            }
            throw new InvalidKeyException("Missing local static certificate");
        }
        if (i10 != 5 && i10 != 6) {
            PublicKey publicKey2 = this.f19756d;
            if (publicKey2 != null) {
                return publicKey2.getEncoded();
            }
            throw new InvalidKeyException("Missing local static public key");
        }
        PublicKey publicKey3 = this.f19756d;
        if (publicKey3 != null) {
            if (publicKey3 instanceof ECPublicKey) {
                ECPublicKey eCPublicKey = (ECPublicKey) publicKey3;
                return EllipticCurveOverFpHelper.b(eCPublicKey.getParams().getCurve(), eCPublicKey.getW(), i10 == 5);
            }
            throw new InvalidKeyException("Only ec public key is supported");
        }
        throw new InvalidKeyException("Missing local static public key");
    }

    private PublicKey k(byte[] bArr, int i10) {
        if (i10 != 5 && i10 != 6) {
            return KeyUtil.b(bArr, "EC");
        }
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
        algorithmParameters.init(new ECGenParameterSpec(this.f19768p.a()));
        return EllipticCurvePoint.a((ECParameterSpec) algorithmParameters.getParameterSpec(ECParameterSpec.class), bArr).e();
    }

    private PublicKey l(byte[] bArr, int i10) {
        PublicKey e10;
        if (i10 == 3) {
            return this.f19774v.a(CertUtil.e(bArr)).getPublicKey();
        }
        if (i10 == 4) {
            return this.f19774v.b(bArr).getPublicKey();
        }
        if (i10 == 2) {
            return this.f19774v.d(bArr);
        }
        if (i10 != 5 && i10 != 6) {
            e10 = KeyUtil.b(bArr, "EC");
        } else {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
            algorithmParameters.init(new ECGenParameterSpec(this.f19768p.a()));
            e10 = EllipticCurvePoint.a((ECParameterSpec) algorithmParameters.getParameterSpec(ECParameterSpec.class), bArr).e();
        }
        return this.f19774v.c(e10);
    }

    private CipherStatePair u() {
        CipherStatePair j10 = this.f19754b.j();
        if (!this.f19765m) {
            j10.c();
        }
        this.f19775w = 100;
        return j10;
    }

    public CipherStatePair d() {
        return this.f19770r;
    }

    public NoiseHandshakeEnum f() {
        return this.f19753a;
    }

    public boolean i() {
        return this.f19770r != null;
    }

    public void j() {
        try {
            byte[] bArr = this.f19767o;
            if (bArr != null) {
                this.f19754b.g(bArr);
            } else {
                this.f19754b.g(f19752x);
            }
            short s7 = this.f19763k[0];
            if (s7 != 1) {
                if (s7 == 2) {
                    if (this.f19765m) {
                        this.f19754b.g(a(this.f19759g.getPublic()));
                    } else {
                        this.f19754b.g(a(this.f19761i));
                    }
                }
            } else if (this.f19765m) {
                this.f19754b.g(g());
            } else {
                this.f19754b.g(a(this.f19760h));
            }
            short s10 = this.f19763k[1];
            if (s10 != 1) {
                if (s10 == 2) {
                    if (this.f19765m) {
                        this.f19754b.g(a(this.f19761i));
                    } else {
                        this.f19754b.g(a(this.f19759g.getPublic()));
                    }
                }
            } else if (this.f19765m) {
                this.f19754b.g(a(this.f19760h));
            } else {
                this.f19754b.g(g());
            }
            this.f19775w = 1;
        } catch (InvalidKeyException e10) {
            throw new EncryptException(e10);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x000e, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00ff, code lost:
    
        r5.f19764l = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0105, code lost:
    
        if (r6.e() == null) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0107, code lost:
    
        r1 = r5.f19754b.a(r6.e());
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0116, code lost:
    
        if (r5.f19766n < r5.f19763k.length) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0118, code lost:
    
        r5.f19770r = u();
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x011e, code lost:
    
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public byte[] m(c cVar) {
        try {
            byte[] bArr = null;
            if (this.f19764l || this.f19766n >= this.f19763k.length) {
                return null;
            }
            while (true) {
                int i10 = this.f19766n;
                short[] sArr = this.f19763k;
                if (i10 < sArr.length) {
                    this.f19766n = i10 + 1;
                    short s7 = sArr[i10];
                    if (s7 == 255) {
                        this.f19775w++;
                        this.f19764l = true;
                    } else if (s7 != 15) {
                        switch (s7) {
                            case 1:
                                if (cVar.g() != null) {
                                    this.f19760h = l(this.f19754b.a(cVar.g()), cVar.f());
                                    break;
                                } else {
                                    throw new IllegalStateException("Pattern definition error");
                                }
                            case 2:
                                if (cVar.c() != null) {
                                    PublicKey k10 = k(cVar.c(), cVar.b());
                                    this.f19761i = k10;
                                    this.f19754b.g(a(k10));
                                    if (!HandshakePattern.a(this.f19753a)) {
                                        break;
                                    } else {
                                        this.f19754b.h(a(this.f19761i));
                                        break;
                                    }
                                } else {
                                    throw new IllegalStateException("Pattern definition error");
                                }
                            case 3:
                                this.f19754b.h(b(this.f19759g.getPrivate(), this.f19761i));
                                break;
                            case 4:
                                if (this.f19765m) {
                                    this.f19754b.h(b(this.f19759g.getPrivate(), this.f19760h));
                                    break;
                                } else {
                                    this.f19754b.h(b(this.f19755c, this.f19761i));
                                    break;
                                }
                            case 5:
                                if (this.f19765m) {
                                    this.f19754b.h(b(this.f19755c, this.f19761i));
                                    break;
                                } else {
                                    this.f19754b.h(b(this.f19759g.getPrivate(), this.f19760h));
                                    break;
                                }
                            case 6:
                                this.f19754b.h(b(this.f19755c, this.f19760h));
                                break;
                        }
                    } else {
                        this.f19754b.i(this.f19762j);
                    }
                }
            }
        } catch (CborException | EncryptException | IOException | KeyException | NoSuchAlgorithmException | CertificateException | InvalidKeySpecException | InvalidParameterSpecException | InvalidArgumentException e10) {
            this.f19775w = -1;
            throw new EncryptException(e10);
        }
    }

    public void n(PrivateKey privateKey) {
        this.f19755c = privateKey;
    }

    public void o(PublicKey publicKey) {
        p(publicKey, null);
    }

    public void p(PublicKey publicKey, byte[] bArr) {
        this.f19756d = publicKey;
        this.f19757e = bArr;
        this.f19758f = null;
    }

    public void q(byte[] bArr) {
        this.f19767o = bArr;
    }

    public void r(byte[] bArr) {
        if (bArr != null && bArr.length == 32) {
            this.f19762j = bArr;
            return;
        }
        throw new IllegalArgumentException("Psk must be 32 bytes in length");
    }

    public void s(PublicKey publicKey) {
        this.f19760h = publicKey;
    }

    public void t(KeyPair keyPair) {
        if (keyPair == null || keyPair.getPublic() == null || keyPair.getPrivate() == null) {
            return;
        }
        o(keyPair.getPublic());
        n(keyPair.getPrivate());
    }

    public c v(byte[] bArr) {
        try {
            if (!this.f19764l || this.f19766n >= this.f19763k.length) {
                return null;
            }
            c cVar = new c();
            while (true) {
                int i10 = this.f19766n;
                short[] sArr = this.f19763k;
                if (i10 < sArr.length) {
                    this.f19766n = i10 + 1;
                    short s7 = sArr[i10];
                    if (s7 == 255) {
                        this.f19764l = false;
                        this.f19775w++;
                    } else if (s7 != 15) {
                        switch (s7) {
                            case 1:
                                cVar.m(this.f19754b.b(h()), this.f19772t);
                                break;
                            case 2:
                                KeyPair c10 = c();
                                this.f19759g = c10;
                                this.f19754b.g(a(c10.getPublic()));
                                if (HandshakePattern.a(this.f19753a)) {
                                    this.f19754b.h(a(this.f19759g.getPublic()));
                                }
                                cVar.k(e(this.f19759g.getPublic()), this.f19773u);
                                break;
                            case 3:
                                this.f19754b.h(b(this.f19759g.getPrivate(), this.f19761i));
                                break;
                            case 4:
                                if (this.f19765m) {
                                    this.f19754b.h(b(this.f19759g.getPrivate(), this.f19760h));
                                    break;
                                } else {
                                    this.f19754b.h(b(this.f19755c, this.f19761i));
                                    break;
                                }
                            case 5:
                                if (this.f19765m) {
                                    this.f19754b.h(b(this.f19755c, this.f19761i));
                                    break;
                                } else {
                                    this.f19754b.h(b(this.f19759g.getPrivate(), this.f19760h));
                                    break;
                                }
                            case 6:
                                this.f19754b.h(b(this.f19755c, this.f19760h));
                                break;
                        }
                    } else {
                        this.f19754b.i(this.f19762j);
                    }
                }
            }
            this.f19764l = false;
            if (bArr != null) {
                cVar.l(this.f19754b.b(bArr));
            }
            if (this.f19766n >= this.f19763k.length) {
                this.f19770r = u();
            }
            return cVar;
        } catch (CborException | EncryptException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | CertificateEncodingException e10) {
            this.f19775w = -1;
            throw new EncryptException(e10);
        }
    }
}
