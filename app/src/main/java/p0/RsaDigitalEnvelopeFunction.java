package p0;

import e1.Base64Utils;
import e1.KeyUtil;
import e1.i;
import i0.EncryptException;
import j0.AesUtil;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import javax.crypto.SecretKey;
import m0.CryptoCore;
import n0.SessionSceneDataRepository;
import org.json.JSONException;
import q0.RsaCryptoConfig;
import q0.RsaSceneData;
import s0.BizPublicKeyConfig;
import s0.CryptoConfig;
import s0.CryptoParameters;
import s0.EncryptAlgorithmEnum;
import s0.EncryptEnum;
import s0.SceneConfig;
import s0.j;
import s0.n;
import t0.BizDataNotFoundException;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;
import t0.SceneNotFoundException;
import x0.UpgradeCertResponse;

/* compiled from: RsaDigitalEnvelopeFunction.java */
/* renamed from: p0.f, reason: use source file name */
/* loaded from: classes.dex */
public class RsaDigitalEnvelopeFunction implements j {

    /* renamed from: a, reason: collision with root package name */
    private final CryptoCore f16547a;

    /* renamed from: b, reason: collision with root package name */
    private final SessionSceneDataRepository f16548b;

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, CryptoConfig> f16549c;

    /* renamed from: d, reason: collision with root package name */
    private final Object f16550d = new Object();

    /* renamed from: e, reason: collision with root package name */
    private final Object f16551e = new Object();

    public RsaDigitalEnvelopeFunction(CryptoCore cryptoCore, SessionSceneDataRepository sessionSceneDataRepository, Map<String, CryptoConfig> map) {
        this.f16547a = cryptoCore;
        this.f16548b = sessionSceneDataRepository;
        this.f16549c = map;
    }

    private RsaSceneData b(String str, String str2) {
        RsaSceneData a10 = RsaDigitalEnvelopeUtil.a(this.f16547a, str, this.f16547a.s(str2));
        synchronized (this.f16550d) {
            n c10 = this.f16548b.c(str, str2);
            if (!(c10 instanceof RsaSceneData)) {
                i.a("RsaDigitalEnvelopeFunction", "createAndSaveSceneData adopt and save to session");
                this.f16548b.a(str, a10);
                this.f16549c.remove(str2);
            } else {
                a10 = (RsaSceneData) c10;
            }
        }
        return a10;
    }

    private RsaCryptoConfig c(String str, RsaSceneData rsaSceneData) {
        PublicKey publicKey = null;
        long j10 = 0;
        if (!this.f16547a.x(str)) {
            try {
                if (this.f16547a.j(str)) {
                    UpgradeCertResponse h10 = this.f16547a.p().f(str).h();
                    publicKey = h10.d().getPublicKey();
                    j10 = h10.f();
                }
            } catch (BizDataNotFoundException unused) {
                i.a("RsaDigitalEnvelopeFunction", "createRsaCryptoConfig No valid domain name set.");
            }
            if (publicKey == null) {
                i.a("RsaDigitalEnvelopeFunction", "createRsaCryptoConfig missing " + str + " online certificate.");
            }
        }
        if (publicKey == null) {
            BizPublicKeyConfig b10 = this.f16547a.p().b(str);
            if (b10 != null && b10.a() != null) {
                publicKey = KeyUtil.b(Base64Utils.a(b10.a()), "RSA");
                j10 = b10.b();
            }
            if (publicKey == null) {
                i.a("RsaDigitalEnvelopeFunction", "createRsaCryptoConfig missing " + str + " hardcoded public key.");
            }
        }
        long j11 = j10;
        if (publicKey != null) {
            if (publicKey.getAlgorithm().equals("RSA")) {
                return new RsaCryptoConfig(Base64Utils.b(RsaDigitalEnvelopeUtil.c(rsaSceneData.n(), publicKey)), rsaSceneData.f(), j11);
            }
            throw new InvalidKeyException("Current scene only supports rsa key, not " + publicKey.getAlgorithm() + ". Please specify the correct biz or biz public Key.");
        }
        throw new InvalidKeyException("Missing biz public key.");
    }

    private n d(String str, String str2) {
        SceneConfig s7 = this.f16547a.s(str2);
        n c10 = this.f16548b.c(str, str2);
        if (c10 != null || !s7.f()) {
            return c10;
        }
        n t7 = this.f16547a.t(str, str2);
        if (t7 == null || t7.g()) {
            return null;
        }
        this.f16548b.a(str, t7);
        return t7;
    }

    @Override // s0.j
    public String a(byte[] bArr, String str, String str2) {
        try {
            n d10 = d(str, str2);
            if (!(d10 instanceof RsaSceneData)) {
                d10 = b(str, str2);
            }
            if (!this.f16549c.containsKey(str2)) {
                synchronized (this.f16551e) {
                    if (!this.f16549c.containsKey(str2)) {
                        RsaCryptoConfig c10 = c(str, (RsaSceneData) d10);
                        i.a("RsaDigitalEnvelopeFunction", "encrypt start packing digital envelopes, using server public key version " + c10.b());
                        this.f16549c.put(str2, c10);
                    }
                }
            }
            SecretKey n10 = ((RsaSceneData) d10).n();
            EncryptAlgorithmEnum b10 = d10.b();
            if (EncryptEnum.AES == b10.a()) {
                return DigitalEnvelopeUtil.a(AesUtil.b(new CryptoParameters.c().j(CryptoParameters.b.a(b10.c())).k(bArr).m(n10).h()));
            }
            throw new InvalidAlgorithmException(b10.a().name());
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | JSONException | InvalidAlgorithmException | InvalidArgumentException | SceneNotFoundException e10) {
            throw new EncryptException(e10);
        }
    }
}
