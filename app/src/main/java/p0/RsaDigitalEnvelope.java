package p0;

import e1.Base64Utils;
import i0.EncryptException;
import j0.AesUtil;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import org.json.JSONException;
import org.json.JSONObject;
import q0.DigitalEnvelopeCipherEnum;
import s0.CipherContainer;
import s0.CryptoParameters;
import s0.EncryptEnum;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;

/* compiled from: RsaDigitalEnvelope.java */
/* renamed from: p0.e, reason: use source file name */
/* loaded from: classes.dex */
public class RsaDigitalEnvelope {

    /* renamed from: a, reason: collision with root package name */
    private final DigitalEnvelopeCipherEnum f16544a;

    /* renamed from: b, reason: collision with root package name */
    private SecretKey f16545b = null;

    /* renamed from: c, reason: collision with root package name */
    private PublicKey f16546c;

    public RsaDigitalEnvelope(DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum) {
        this.f16544a = digitalEnvelopeCipherEnum;
    }

    public String a(byte[] bArr) {
        return b(null, bArr);
    }

    public String b(byte[] bArr, byte[] bArr2) {
        try {
            this.f16545b = RsaDigitalEnvelopeUtil.b(this.f16544a.c(), this.f16544a.b() * 8);
            if (EncryptEnum.AES == this.f16544a.c()) {
                CipherContainer b10 = AesUtil.b(new CryptoParameters.c().j(this.f16544a.a()).k(bArr2).m(this.f16545b).i(bArr).h());
                byte[] c10 = RsaDigitalEnvelopeUtil.c(this.f16545b, this.f16546c);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("protectedKey", Base64Utils.b(c10));
                jSONObject.put("cipherInfo", new JSONObject(DigitalEnvelopeUtil.a(b10)));
                return jSONObject.toString();
            }
            throw new InvalidAlgorithmException(this.f16544a.c().name());
        } catch (InvalidKeyException | NoSuchAlgorithmException | JSONException | InvalidAlgorithmException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public void c(PublicKey publicKey) {
        this.f16546c = publicKey;
    }
}
