package k0;

import c1.CryptoEngCmd;
import c1.TAInterfaceException;
import d1.CertType;
import d1.PrivKeyLabelType;
import d1.SignAlgType;
import e1.Base64Utils;
import e1.HexStringUtils;
import i0.EncryptException;
import j0.CertUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import l0.Authentication;
import l0.DeviceCertAuthConfig;
import l0.IAuthConfig;
import org.json.JSONException;
import org.json.JSONObject;
import p0.EciesDigitalEnvelope;
import p0.RsaDigitalEnvelope;
import q0.DigitalEnvelopeCipherEnum;
import t0.InvalidArgumentException;

/* compiled from: DeviceCertAuth.java */
/* renamed from: k0.c, reason: use source file name */
/* loaded from: classes.dex */
public class DeviceCertAuth extends Authentication {

    /* renamed from: a, reason: collision with root package name */
    private DeviceCertAuthConfig f14001a;

    private static String c(String str) {
        JSONObject jSONObject = new JSONObject(str);
        return jSONObject.getString("cipher") + jSONObject.getString("iv");
    }

    private static String d(String str, long j10) {
        StringBuilder sb2 = new StringBuilder();
        JSONObject jSONObject = new JSONObject(str);
        sb2.append(c(jSONObject.getJSONObject("cipherInfo").toString()));
        sb2.append(jSONObject.getString("tmpPublicKey"));
        sb2.append(j10);
        if (!jSONObject.optString("salt").isEmpty()) {
            sb2.append(jSONObject.getString("salt"));
        }
        if (!jSONObject.optString("info").isEmpty()) {
            sb2.append(jSONObject.getString("info"));
        }
        return sb2.toString();
    }

    private static X509Certificate e() {
        try {
            List<X509Certificate> b10 = CryptoEngCmd.b(CertType.DEVICE_EE_CERT_LABEL);
            if (b10.size() == 1) {
                return b10.get(0);
            }
            return null;
        } catch (TAInterfaceException unused) {
            throw new TAInterfaceException("The current phone version does not support 'get device certificate' function.");
        }
    }

    private static String f(String str, long j10) {
        StringBuilder sb2 = new StringBuilder();
        JSONObject jSONObject = new JSONObject(str);
        sb2.append(c(jSONObject.getJSONObject("cipherInfo").toString()));
        sb2.append(jSONObject.getString("protectedKey"));
        sb2.append(j10);
        return sb2.toString();
    }

    @Override // l0.Authentication
    public void a(IAuthConfig iAuthConfig) {
        if (iAuthConfig instanceof DeviceCertAuthConfig) {
            this.f14001a = (DeviceCertAuthConfig) iAuthConfig;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a9 A[Catch: e | IOException | InvalidKeyException | CertificateException | JSONException | c -> 0x0145, TryCatch #1 {e | IOException | InvalidKeyException | CertificateException | JSONException | c -> 0x0145, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x000f, B:8:0x0015, B:10:0x0025, B:12:0x002b, B:14:0x0031, B:22:0x0061, B:23:0x00cc, B:24:0x0085, B:25:0x00a8, B:27:0x00a9, B:28:0x0049, B:31:0x0052, B:34:0x00ec, B:35:0x00f3, B:36:0x00f4, B:37:0x00fb, B:38:0x00fc, B:40:0x0110, B:40:0x0110, B:40:0x0110, B:40:0x0110, B:40:0x0110, B:41:0x0118, B:41:0x0118, B:41:0x0118, B:41:0x0118, B:41:0x0118, B:41:0x0118, B:44:0x0135, B:44:0x0135, B:44:0x0135, B:44:0x0135, B:44:0x0135, B:44:0x0135, B:45:0x013c, B:45:0x013c, B:45:0x013c, B:45:0x013c, B:45:0x013c, B:45:0x013c, B:46:0x013d, B:46:0x013d, B:46:0x013d, B:46:0x013d, B:46:0x013d, B:46:0x013d, B:47:0x0144, B:47:0x0144, B:47:0x0144, B:47:0x0144, B:47:0x0144, B:47:0x0144), top: B:2:0x0002 }] */
    @Override // l0.Authentication
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String b(String str) {
        String a10;
        try {
            DeviceCertAuthConfig deviceCertAuthConfig = this.f14001a;
            boolean z10 = false;
            if (deviceCertAuthConfig == null) {
                deviceCertAuthConfig = new DeviceCertAuthConfig();
                deviceCertAuthConfig.d(false);
            }
            X509Certificate e10 = e();
            if (e10 != null) {
                StringBuilder sb2 = new StringBuilder(str);
                JSONObject jSONObject = new JSONObject();
                if (deviceCertAuthConfig.c()) {
                    if (deviceCertAuthConfig.a() != null) {
                        String f10 = CertUtil.f(e10);
                        if (f10 != null) {
                            String algorithm = deviceCertAuthConfig.a().getAlgorithm();
                            int hashCode = algorithm.hashCode();
                            if (hashCode != 2206) {
                                if (hashCode == 81440 && algorithm.equals("RSA")) {
                                    if (z10) {
                                        RsaDigitalEnvelope rsaDigitalEnvelope = new RsaDigitalEnvelope(DigitalEnvelopeCipherEnum.f16763k);
                                        rsaDigitalEnvelope.c(deviceCertAuthConfig.a());
                                        a10 = rsaDigitalEnvelope.a(f10.getBytes(StandardCharsets.UTF_8));
                                        sb2.append(f(a10, deviceCertAuthConfig.b()));
                                    } else if (z10) {
                                        EciesDigitalEnvelope eciesDigitalEnvelope = new EciesDigitalEnvelope(DigitalEnvelopeCipherEnum.f16763k);
                                        eciesDigitalEnvelope.c(deviceCertAuthConfig.a());
                                        a10 = eciesDigitalEnvelope.a(f10.getBytes(StandardCharsets.UTF_8));
                                        sb2.append(d(a10, deviceCertAuthConfig.b()));
                                    } else {
                                        throw new InvalidKeyException("Unsupported " + deviceCertAuthConfig.a().getAlgorithm() + " key type");
                                    }
                                    JSONObject jSONObject2 = new JSONObject(a10);
                                    jSONObject.put("deviceCertId", jSONObject2.getJSONObject("cipherInfo"));
                                    jSONObject2.remove("cipherInfo");
                                    jSONObject2.put("certVersion", deviceCertAuthConfig.b());
                                    jSONObject.put("pack", jSONObject2);
                                }
                                z10 = -1;
                                if (z10) {
                                }
                                JSONObject jSONObject22 = new JSONObject(a10);
                                jSONObject.put("deviceCertId", jSONObject22.getJSONObject("cipherInfo"));
                                jSONObject22.remove("cipherInfo");
                                jSONObject22.put("certVersion", deviceCertAuthConfig.b());
                                jSONObject.put("pack", jSONObject22);
                            } else {
                                if (algorithm.equals("EC")) {
                                    z10 = true;
                                    if (z10) {
                                    }
                                    JSONObject jSONObject222 = new JSONObject(a10);
                                    jSONObject.put("deviceCertId", jSONObject222.getJSONObject("cipherInfo"));
                                    jSONObject222.remove("cipherInfo");
                                    jSONObject222.put("certVersion", deviceCertAuthConfig.b());
                                    jSONObject.put("pack", jSONObject222);
                                }
                                z10 = -1;
                                if (z10) {
                                }
                                JSONObject jSONObject2222 = new JSONObject(a10);
                                jSONObject.put("deviceCertId", jSONObject2222.getJSONObject("cipherInfo"));
                                jSONObject2222.remove("cipherInfo");
                                jSONObject2222.put("certVersion", deviceCertAuthConfig.b());
                                jSONObject.put("pack", jSONObject2222);
                            }
                        } else {
                            throw new CertificateException("Failed to get device certificate id.");
                        }
                    } else {
                        throw new InvalidArgumentException("Peer public key is null");
                    }
                }
                String b10 = Base64Utils.b(e10.getEncoded());
                sb2.append(b10);
                jSONObject.put("deviceCert", b10);
                try {
                    jSONObject.put("sign", Base64Utils.b(HexStringUtils.b(new JSONObject(CryptoEngCmd.e(sb2.toString(), PrivKeyLabelType.DEVICE_EE_PRIV_KEY_LABEL, SignAlgType.ECDSA_SHA256)).getString("signature"))));
                    return jSONObject.toString();
                } catch (TAInterfaceException unused) {
                    throw new TAInterfaceException("The current phone version does not support 'device key sign' function.");
                }
            }
            throw new CertificateException("Failed to get device certificate.");
        } catch (TAInterfaceException | IOException | InvalidKeyException | CertificateException | JSONException | InvalidArgumentException e11) {
            throw new EncryptException(e11);
        }
    }
}
