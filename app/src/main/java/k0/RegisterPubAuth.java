package k0;

import e1.Base64Utils;
import i0.EncryptException;
import j0.EccUtil;
import j0.RsaUtil;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import l0.Authentication;
import l0.IAuthConfig;
import l0.RegisterPubAuthConfig;
import t0.InvalidArgumentException;

/* compiled from: RegisterPubAuth.java */
/* renamed from: k0.e, reason: use source file name */
/* loaded from: classes.dex */
public class RegisterPubAuth extends Authentication {

    /* renamed from: a, reason: collision with root package name */
    private RegisterPubAuthConfig f14003a;

    @Override // l0.Authentication
    public void a(IAuthConfig iAuthConfig) {
        if (iAuthConfig instanceof RegisterPubAuthConfig) {
            this.f14003a = (RegisterPubAuthConfig) iAuthConfig;
        }
    }

    @Override // l0.Authentication
    public String b(String str) {
        try {
            RegisterPubAuthConfig registerPubAuthConfig = this.f14003a;
            if (registerPubAuthConfig != null && registerPubAuthConfig.a() != null) {
                String algorithm = this.f14003a.a().getAlgorithm();
                if (algorithm.equals("RSA")) {
                    return Base64Utils.b(RsaUtil.b(str.getBytes(StandardCharsets.UTF_8), this.f14003a.a()));
                }
                if (algorithm.equals("EC")) {
                    return Base64Utils.b(EccUtil.b(str.getBytes(StandardCharsets.UTF_8), this.f14003a.a()));
                }
                throw new InvalidKeyException("Current function only supports rsa/ec key, not " + algorithm);
            }
            throw new InvalidArgumentException("The key used for signing is null");
        } catch (InvalidKeyException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }
}
