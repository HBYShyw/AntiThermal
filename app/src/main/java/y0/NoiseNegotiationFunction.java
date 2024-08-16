package y0;

import e1.i;
import i0.EncryptException;
import java.security.InvalidKeyException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import m0.CryptoCore;
import n0.SessionSceneDataRepository;
import org.json.JSONException;
import s0.CryptoConfig;
import s0.NegotiationParam;
import s0.SceneConfig;
import s0.j;
import s0.n;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;
import t0.SceneNotFoundException;
import z0.NoiseCryptoConfig;
import z0.NoiseSceneData;

/* compiled from: NoiseNegotiationFunction.java */
/* renamed from: y0.d, reason: use source file name */
/* loaded from: classes.dex */
public class NoiseNegotiationFunction implements j {

    /* renamed from: a, reason: collision with root package name */
    private final CryptoCore f19781a;

    /* renamed from: b, reason: collision with root package name */
    private final Map<String, NegotiationParam> f19782b;

    /* renamed from: c, reason: collision with root package name */
    private final SessionSceneDataRepository f19783c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<String, CryptoConfig> f19784d;

    /* renamed from: e, reason: collision with root package name */
    private final Object f19785e = new Object();

    /* renamed from: f, reason: collision with root package name */
    private final Object f19786f = new Object();

    public NoiseNegotiationFunction(CryptoCore cryptoCore, Map<String, NegotiationParam> map, SessionSceneDataRepository sessionSceneDataRepository, Map<String, CryptoConfig> map2) {
        this.f19781a = cryptoCore;
        this.f19782b = map;
        this.f19783c = sessionSceneDataRepository;
        this.f19784d = map2;
    }

    private n b(String str, String str2) {
        n nVar;
        synchronized (this.f19785e) {
            n c10 = this.f19783c.c(str, str2);
            if (!(c10 instanceof NoiseSceneData)) {
                nVar = NoiseUtil.e(this.f19781a, str, str2, this.f19782b.get(str2));
                i.a("NoiseNegotiationFunction", "createAndSaveSceneData adopt and save to session");
                this.f19783c.a(str, nVar);
                this.f19784d.remove(str2);
            } else {
                nVar = (NoiseSceneData) c10;
            }
        }
        return nVar;
    }

    private n c(String str, String str2) {
        SceneConfig s7 = this.f19781a.s(str2);
        n c10 = this.f19783c.c(str, str2);
        if (c10 != null || !s7.f()) {
            return c10;
        }
        n t7 = this.f19781a.t(str, str2);
        if (t7 == null || t7.g()) {
            return null;
        }
        this.f19783c.a(str, t7);
        return t7;
    }

    @Override // s0.j
    public String a(byte[] bArr, String str, String str2) {
        try {
            n c10 = c(str, str2);
            if (!(c10 instanceof NoiseSceneData)) {
                c10 = b(str, str2);
            }
            CipherState b10 = ((NoiseSceneData) c10).n().b();
            if (!this.f19784d.containsKey(str2)) {
                synchronized (this.f19786f) {
                    if (!this.f19784d.containsKey(str2)) {
                        this.f19784d.put(str2, new NoiseCryptoConfig(c10.f()));
                    }
                }
            }
            return b10.c(null, bArr).k();
        } catch (InvalidKeyException | TimeoutException | JSONException | InvalidAlgorithmException | InvalidArgumentException | SceneNotFoundException e10) {
            throw new EncryptException(e10);
        }
    }
}
