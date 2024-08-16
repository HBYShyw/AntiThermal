package p0;

import i0.EncryptException;
import j0.AesUtil;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import org.json.JSONException;
import q0.DigitalEnvelopeCipherEnum;
import q0.EciesCurveEnum;
import q0.EciesKDFEnum;
import q0.EciesNegotiationInfo;
import q0.EciesNegotiationParam;
import s0.CryptoParameters;
import s0.EncryptEnum;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;

/* compiled from: EciesDigitalEnvelope.java */
/* renamed from: p0.c, reason: use source file name */
/* loaded from: classes.dex */
public class EciesDigitalEnvelope {

    /* renamed from: c, reason: collision with root package name */
    private final DigitalEnvelopeCipherEnum f16538c;

    /* renamed from: e, reason: collision with root package name */
    private PublicKey f16540e;

    /* renamed from: a, reason: collision with root package name */
    private final EciesCurveEnum f16536a = EciesCurveEnum.NIST_P;

    /* renamed from: b, reason: collision with root package name */
    private final EciesKDFEnum f16537b = EciesKDFEnum.HKDF256;

    /* renamed from: d, reason: collision with root package name */
    private SecretKey f16539d = null;

    /* renamed from: f, reason: collision with root package name */
    private boolean f16541f = false;

    /* renamed from: g, reason: collision with root package name */
    private EciesNegotiationParam f16542g = null;

    public EciesDigitalEnvelope(DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum) {
        this.f16538c = digitalEnvelopeCipherEnum;
    }

    public String a(byte[] bArr) {
        return b(null, bArr);
    }

    public String b(byte[] bArr, byte[] bArr2) {
        try {
            EciesNegotiationInfo eciesNegotiationInfo = new EciesNegotiationInfo();
            this.f16539d = EciesDigitalEnvelopeUtil.f(this.f16536a, this.f16537b, this.f16540e, this.f16542g, this.f16538c.b(), eciesNegotiationInfo);
            if (EncryptEnum.AES == this.f16538c.c()) {
                return EciesDigitalEnvelopeUtil.g(this.f16537b, eciesNegotiationInfo, AesUtil.b(new CryptoParameters.c().j(this.f16538c.a()).k(bArr2).m(this.f16539d).i(bArr).h()));
            }
            throw new InvalidAlgorithmException(this.f16538c.c().name());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | JSONException | InvalidAlgorithmException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public void c(PublicKey publicKey) {
        this.f16540e = publicKey;
    }
}
