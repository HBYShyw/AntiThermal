package p0;

import e1.Base64Utils;
import e1.i;
import i0.EncryptException;
import j0.AesUtil;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import javax.crypto.SecretKey;
import m0.CryptoCore;
import n0.SessionSceneDataRepository;
import org.json.JSONException;
import q0.EciesCryptoConfig;
import q0.EciesNegotiationInfo;
import q0.EciesSceneData;
import s0.CryptoConfig;
import s0.CryptoParameters;
import s0.EncryptAlgorithmEnum;
import s0.EncryptEnum;
import s0.NegotiationParam;
import s0.SceneConfig;
import s0.j;
import s0.n;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;
import t0.SceneNotFoundException;

/* compiled from: EccDigitalEnvelopeFunction.java */
/* renamed from: p0.b, reason: use source file name */
/* loaded from: classes.dex */
public class EccDigitalEnvelopeFunction implements j {

    /* renamed from: a, reason: collision with root package name */
    private final CryptoCore f16530a;

    /* renamed from: b, reason: collision with root package name */
    private final Map<String, NegotiationParam> f16531b;

    /* renamed from: c, reason: collision with root package name */
    private final SessionSceneDataRepository f16532c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<String, CryptoConfig> f16533d;

    /* renamed from: e, reason: collision with root package name */
    private final Object f16534e = new Object();

    /* renamed from: f, reason: collision with root package name */
    private final Object f16535f = new Object();

    public EccDigitalEnvelopeFunction(CryptoCore cryptoCore, Map<String, NegotiationParam> map, SessionSceneDataRepository sessionSceneDataRepository, Map<String, CryptoConfig> map2) {
        this.f16530a = cryptoCore;
        this.f16531b = map;
        this.f16532c = sessionSceneDataRepository;
        this.f16533d = map2;
    }

    private EciesSceneData b(String str, String str2) {
        EciesSceneData a10 = EciesDigitalEnvelopeUtil.a(this.f16530a, str, this.f16530a.s(str2), this.f16531b.get(str2));
        synchronized (this.f16534e) {
            n c10 = this.f16532c.c(str, str2);
            if (!(c10 instanceof EciesSceneData)) {
                i.a("EccDigitalEnvelopeFunction", "createAndSaveSceneData adopt and save to session");
                this.f16532c.a(str, a10);
                this.f16533d.remove(str2);
            } else {
                a10 = (EciesSceneData) c10;
            }
        }
        return a10;
    }

    private n c(String str, String str2) {
        SceneConfig s7 = this.f16530a.s(str2);
        n c10 = this.f16532c.c(str, str2);
        if (c10 != null || !s7.f()) {
            return c10;
        }
        n t7 = this.f16530a.t(str, str2);
        if (t7 == null || t7.g()) {
            return null;
        }
        this.f16532c.a(str, t7);
        return t7;
    }

    @Override // s0.j
    public String a(byte[] bArr, String str, String str2) {
        try {
            n c10 = c(str, str2);
            if (!(c10 instanceof EciesSceneData)) {
                c10 = b(str, str2);
            }
            SecretKey o10 = ((EciesSceneData) c10).o();
            EncryptAlgorithmEnum b10 = c10.b();
            if (EncryptEnum.AES == b10.a()) {
                String a10 = DigitalEnvelopeUtil.a(AesUtil.b(new CryptoParameters.c().j(CryptoParameters.b.a(b10.c())).k(bArr).m(o10).h()));
                if (!this.f16533d.containsKey(str2)) {
                    synchronized (this.f16535f) {
                        if (!this.f16533d.containsKey(str2)) {
                            EciesNegotiationInfo p10 = ((EciesSceneData) c10).p();
                            String b11 = Base64Utils.b(p10.d());
                            long f10 = c10.f();
                            long n10 = ((EciesSceneData) c10).n();
                            i.a("EccDigitalEnvelopeFunction", "encrypt start packing digital envelopes, using server public key version " + n10);
                            this.f16533d.put(str2, new EciesCryptoConfig(b11, f10, n10, p10.c(), p10.b()));
                        }
                    }
                }
                return a10;
            }
            throw new InvalidAlgorithmException(b10.a().name());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | JSONException | InvalidAlgorithmException | InvalidArgumentException | SceneNotFoundException e10) {
            throw new EncryptException(e10);
        }
    }
}
