package p0;

import e1.Base64Utils;
import e1.KeyUtil;
import e1.SceneUtil;
import e1.i;
import j0.EccUtil;
import j0.HkdfUtil;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.SecretKey;
import m0.CryptoCore;
import org.json.JSONObject;
import q0.EciesCurveEnum;
import q0.EciesKDFEnum;
import q0.EciesNegotiationInfo;
import q0.EciesNegotiationParam;
import q0.EciesSceneData;
import s0.BizPublicKeyConfig;
import s0.CipherContainer;
import s0.NegotiationParam;
import s0.SceneConfig;
import s0.n;
import t0.BizDataNotFoundException;
import t0.InvalidArgumentException;
import x0.UpgradeCertResponse;

/* compiled from: EciesDigitalEnvelopeUtil.java */
/* renamed from: p0.d, reason: use source file name */
/* loaded from: classes.dex */
public class EciesDigitalEnvelopeUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final Object f16543a = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EciesSceneData a(CryptoCore cryptoCore, String str, SceneConfig sceneConfig, NegotiationParam negotiationParam) {
        PublicKey publicKey = null;
        long j10 = 0;
        if (!cryptoCore.x(str)) {
            try {
                if (cryptoCore.j(str)) {
                    UpgradeCertResponse h10 = cryptoCore.p().f(str).h();
                    publicKey = h10.d().getPublicKey();
                    j10 = h10.f();
                }
            } catch (BizDataNotFoundException unused) {
                i.a("EciesDigitalEnvelopeUtil", "createAndSaveSceneData no valid domain name set");
            }
            if (publicKey == null) {
                i.a("EciesDigitalEnvelopeUtil", "createAndSaveSceneData missing " + str + " online certificate");
            }
        }
        if (publicKey == null) {
            BizPublicKeyConfig b10 = cryptoCore.p().b(str);
            if (b10 != null && b10.a() != null) {
                publicKey = KeyUtil.b(Base64Utils.a(b10.a()), "EC");
                j10 = b10.b();
            }
            if (publicKey == null) {
                i.a("EciesDigitalEnvelopeUtil", "createAndSaveSceneData missing " + str + " hardcoded public key");
            }
        }
        if (negotiationParam != null && !(negotiationParam instanceof EciesNegotiationParam)) {
            throw new InvalidArgumentException("Negotiation parameters only support type EciesNegotiationParam");
        }
        EciesSceneData b11 = b(sceneConfig, (EciesNegotiationParam) negotiationParam, publicKey, j10);
        i.a("EciesDigitalEnvelopeUtil", "createAndSaveSceneData negotiate a latest secret key");
        if (sceneConfig.f()) {
            synchronized (f16543a) {
                n t7 = cryptoCore.t(str, sceneConfig.d());
                if (t7 != null && !t7.g() && (t7 instanceof EciesSceneData)) {
                    b11 = (EciesSceneData) t7;
                }
                cryptoCore.K(str, b11);
                i.a("EciesDigitalEnvelopeUtil", "createAndSaveSceneData adopt and save to cryptoCore");
            }
        }
        return b11;
    }

    private static EciesSceneData b(SceneConfig sceneConfig, EciesNegotiationParam eciesNegotiationParam, PublicKey publicKey, long j10) {
        if (publicKey != null) {
            if (publicKey.getAlgorithm().equals("EC")) {
                EciesSceneData eciesSceneData = new EciesSceneData();
                SceneUtil.o(sceneConfig, eciesSceneData);
                EciesNegotiationInfo eciesNegotiationInfo = new EciesNegotiationInfo();
                eciesSceneData.r(f(EciesCurveEnum.NIST_P, EciesKDFEnum.HKDF256, publicKey, eciesNegotiationParam, sceneConfig.a().b() / 8, eciesNegotiationInfo));
                eciesSceneData.s(eciesNegotiationInfo);
                eciesSceneData.q(j10);
                return eciesSceneData;
            }
            throw new InvalidKeyException("Current scene only supports EC key, not " + publicKey.getAlgorithm() + ". Please specify the correct biz or biz public key.");
        }
        throw new InvalidKeyException("Missing biz public key.");
    }

    private static byte[] c(EciesCurveEnum eciesCurveEnum, PrivateKey privateKey, PublicKey publicKey) {
        if (eciesCurveEnum == EciesCurveEnum.NIST_P) {
            return EccUtil.a(privateKey, publicKey);
        }
        throw new InvalidAlgorithmParameterException("Unsupported " + eciesCurveEnum);
    }

    private static KeyPair d(EciesCurveEnum eciesCurveEnum, AlgorithmParameterSpec algorithmParameterSpec) {
        if (eciesCurveEnum == EciesCurveEnum.NIST_P) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(algorithmParameterSpec, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        }
        throw new InvalidAlgorithmParameterException("Unsupported " + eciesCurveEnum);
    }

    private static byte[] e(byte[] bArr, EciesKDFEnum eciesKDFEnum, EciesNegotiationParam eciesNegotiationParam, int i10, EciesNegotiationInfo eciesNegotiationInfo) {
        boolean z10;
        if (eciesKDFEnum == EciesKDFEnum.HKDF256) {
            byte[] bArr2 = null;
            if (eciesNegotiationParam != null) {
                bArr2 = eciesNegotiationParam.a();
                z10 = eciesNegotiationParam.b();
            } else {
                z10 = false;
            }
            byte[] bArr3 = new byte[32];
            if (z10) {
                new SecureRandom().nextBytes(bArr3);
            } else {
                Arrays.fill(bArr3, (byte) 0);
            }
            if (eciesNegotiationInfo != null) {
                if (z10) {
                    eciesNegotiationInfo.g(bArr3);
                }
                if (bArr2 != null) {
                    eciesNegotiationInfo.f(bArr2);
                }
            }
            byte[] bytes = "".getBytes(StandardCharsets.UTF_8);
            if (bArr2 == null) {
                bArr2 = bytes;
            }
            return HkdfUtil.b(bArr, bArr3, bArr2, i10);
        }
        throw new InvalidAlgorithmParameterException("Unsupported " + eciesKDFEnum);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SecretKey f(EciesCurveEnum eciesCurveEnum, EciesKDFEnum eciesKDFEnum, PublicKey publicKey, EciesNegotiationParam eciesNegotiationParam, int i10, EciesNegotiationInfo eciesNegotiationInfo) {
        if (publicKey instanceof ECPublicKey) {
            KeyPair d10 = d(eciesCurveEnum, ((ECPublicKey) publicKey).getParams());
            PublicKey publicKey2 = d10.getPublic();
            PrivateKey privateKey = d10.getPrivate();
            if (eciesNegotiationInfo != null) {
                eciesNegotiationInfo.h(publicKey2.getEncoded());
            }
            return KeyUtil.c(e(c(eciesCurveEnum, privateKey, publicKey), eciesKDFEnum, eciesNegotiationParam, i10, eciesNegotiationInfo), "AES");
        }
        throw new InvalidKeyException("Only supports 'ECPublicKey' type, not '" + publicKey.getClass().getName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String g(EciesKDFEnum eciesKDFEnum, EciesNegotiationInfo eciesNegotiationInfo, CipherContainer cipherContainer) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("tmpPublicKey", Base64Utils.b(eciesNegotiationInfo.d()));
        if (eciesKDFEnum == EciesKDFEnum.HKDF256) {
            if (eciesNegotiationInfo.c() != null) {
                jSONObject.put("salt", Base64Utils.b(eciesNegotiationInfo.c()));
            }
            if (eciesNegotiationInfo.b() != null) {
                jSONObject.put("info", Base64Utils.b(eciesNegotiationInfo.b()));
            }
            jSONObject.put("cipherInfo", new JSONObject(DigitalEnvelopeUtil.a(cipherContainer)));
            return jSONObject.toString();
        }
        throw new InvalidAlgorithmParameterException("Unsupported " + eciesKDFEnum);
    }
}
