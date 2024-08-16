package k0;

import l0.AuthTypeEnum;
import l0.Authentication;
import l0.IAuthConfig;

/* compiled from: AuthenticationManager.java */
/* renamed from: k0.b, reason: use source file name */
/* loaded from: classes.dex */
public class AuthenticationManager {

    /* renamed from: a, reason: collision with root package name */
    private final Authentication f14000a;

    public AuthenticationManager(AuthTypeEnum authTypeEnum) {
        this.f14000a = new AuthenticationFactory().a(authTypeEnum);
    }

    public void a(IAuthConfig iAuthConfig) {
        if (iAuthConfig != null) {
            this.f14000a.a(iAuthConfig);
        }
    }

    public String b(String str) {
        if (str != null) {
            return this.f14000a.b(str);
        }
        throw new IllegalArgumentException("message is null");
    }
}
