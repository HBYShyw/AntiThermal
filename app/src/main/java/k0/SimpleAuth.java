package k0;

import e1.Base64Utils;
import i0.EncryptException;
import j0.HashUtil;
import j0.HmacUtil;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import l0.Authentication;
import l0.IAuthConfig;
import l0.SimpleAuthConfig;
import org.json.JSONException;
import org.json.JSONObject;
import t0.InvalidArgumentException;

/* compiled from: SimpleAuth.java */
/* renamed from: k0.f, reason: use source file name */
/* loaded from: classes.dex */
public class SimpleAuth extends Authentication {

    /* renamed from: a, reason: collision with root package name */
    private SimpleAuthConfig f14004a;

    @Override // l0.Authentication
    public void a(IAuthConfig iAuthConfig) {
        if (iAuthConfig instanceof SimpleAuthConfig) {
            this.f14004a = (SimpleAuthConfig) iAuthConfig;
        }
    }

    @Override // l0.Authentication
    public String b(String str) {
        try {
            SimpleAuthConfig simpleAuthConfig = this.f14004a;
            if (simpleAuthConfig != null && simpleAuthConfig.a() != null) {
                int nextInt = new SecureRandom().nextInt(Integer.MAX_VALUE);
                String b10 = Base64Utils.b(HmacUtil.b(str.getBytes(StandardCharsets.UTF_8), HashUtil.c((this.f14004a.a() + nextInt).getBytes(StandardCharsets.UTF_8))));
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("random", nextInt);
                jSONObject.put("sign", b10);
                return jSONObject.toString();
            }
            throw new InvalidArgumentException("secret is null");
        } catch (NoSuchAlgorithmException | JSONException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }
}
