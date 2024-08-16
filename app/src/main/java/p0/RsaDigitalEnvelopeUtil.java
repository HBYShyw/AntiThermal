package p0;

import e1.KeyUtil;
import e1.SceneUtil;
import e1.i;
import j0.RsaUtil;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import m0.CryptoCore;
import q0.RsaSceneData;
import s0.EncryptAlgorithmEnum;
import s0.EncryptEnum;
import s0.SceneConfig;
import s0.n;
import t0.InvalidAlgorithmException;

/* compiled from: RsaDigitalEnvelopeUtil.java */
/* renamed from: p0.g, reason: use source file name */
/* loaded from: classes.dex */
public class RsaDigitalEnvelopeUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final Object f16552a = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RsaSceneData a(CryptoCore cryptoCore, String str, SceneConfig sceneConfig) {
        RsaSceneData rsaSceneData = new RsaSceneData();
        SceneUtil.o(sceneConfig, rsaSceneData);
        EncryptAlgorithmEnum a10 = sceneConfig.a();
        rsaSceneData.o(b(a10.a(), a10.b()));
        i.a("RsaDigitalEnvelopeUtil", "createAndSaveSceneData generate a latest secret key.");
        if (sceneConfig.f()) {
            synchronized (f16552a) {
                n t7 = cryptoCore.t(str, sceneConfig.d());
                if (t7 != null && !t7.g() && (t7 instanceof RsaSceneData)) {
                    rsaSceneData = (RsaSceneData) t7;
                }
                i.a("RsaDigitalEnvelopeUtil", "createAndSaveSceneData adopt and save to cryptoCore");
                cryptoCore.K(str, rsaSceneData);
            }
        }
        return rsaSceneData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SecretKey b(EncryptEnum encryptEnum, int i10) {
        if (EncryptEnum.AES == encryptEnum) {
            return KeyUtil.d(i10);
        }
        throw new InvalidAlgorithmException(encryptEnum.name());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] c(SecretKey secretKey, PublicKey publicKey) {
        if (publicKey != null) {
            if (publicKey.getAlgorithm().equals("RSA")) {
                return RsaUtil.a(secretKey.getEncoded(), publicKey);
            }
            throw new InvalidKeyException("Current scene only supports rsa key, not " + publicKey.getAlgorithm() + ". Please specify the correct biz or biz public Key.");
        }
        throw new InvalidKeyException("Biz public key is null, please check whether the biz host is configured correctly.");
    }
}
