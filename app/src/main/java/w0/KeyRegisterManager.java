package w0;

import e1.Base64Utils;
import e1.HttpUtil;
import e1.i;
import i0.AuthException;
import i0.EncryptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.SecureRandom;
import k0.AuthenticationManager;
import l0.AuthTypeEnum;
import l0.DeviceCertAuthConfig;
import l0.SimpleAuthConfig;
import org.json.JSONObject;
import t0.InvalidArgumentException;
import v0.GukConfig;
import x0.CustomSimpleAuthCallback;
import zd.ResponseBody;
import zd.b0;

/* compiled from: KeyRegisterManager.java */
/* renamed from: w0.b, reason: use source file name */
/* loaded from: classes.dex */
public class KeyRegisterManager {

    /* renamed from: a, reason: collision with root package name */
    private String f19313a;

    /* renamed from: b, reason: collision with root package name */
    private String f19314b;

    /* renamed from: c, reason: collision with root package name */
    private String f19315c;

    /* renamed from: d, reason: collision with root package name */
    private String f19316d;

    /* renamed from: e, reason: collision with root package name */
    private int f19317e;

    /* renamed from: f, reason: collision with root package name */
    private String f19318f;

    /* renamed from: g, reason: collision with root package name */
    private long f19319g = 15724800;

    /* renamed from: h, reason: collision with root package name */
    private String f19320h;

    /* renamed from: i, reason: collision with root package name */
    private DeviceCertAuthConfig f19321i;

    /* renamed from: j, reason: collision with root package name */
    private CustomSimpleAuthCallback f19322j;

    private void a() {
        if (this.f19320h != null) {
            if (this.f19316d != null && this.f19315c != null) {
                if (this.f19313a != null) {
                    if (this.f19314b == null) {
                        throw new InvalidArgumentException("Biz name cannot be empty");
                    }
                    return;
                }
                throw new InvalidArgumentException("Device id cannot be empty");
            }
            throw new InvalidArgumentException("Public key cannot be empty");
        }
        throw new InvalidArgumentException("Url cannot be empty");
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x010d, code lost:
    
        if (0 == 0) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("deviceId", this.f19313a);
        jSONObject.put("biz", this.f19314b);
        jSONObject.put("publicKey4Sign", this.f19315c);
        jSONObject.put("publicKey4Encrypt", this.f19316d);
        jSONObject.put("authType", this.f19317e);
        jSONObject.put("authMsg", this.f19318f);
        jSONObject.put("expireTime", this.f19319g);
        b0 b0Var = null;
        try {
            try {
                i.a("KeyRegisterManager", "postServer start to register application public key online, auth type = " + this.f19317e + ", biz = " + this.f19314b);
                b0Var = HttpUtil.b(this.f19320h, jSONObject.toString());
                int code = b0Var.getCode();
                i.a("KeyRegisterManager", "postServer server response code " + code);
                if (code == 200) {
                    ResponseBody f20511k = b0Var.getF20511k();
                    if (f20511k == null) {
                        i.b("KeyRegisterManager", "postServer returns null on responses");
                        b0Var.close();
                        return false;
                    }
                    JSONObject jSONObject2 = new JSONObject(f20511k.v());
                    if (jSONObject2.getInt("code") == 200) {
                        i.a("KeyRegisterManager", "postServer register application public key online succeed, biz = " + this.f19314b);
                        b0Var.close();
                        return true;
                    }
                    i.b("KeyRegisterManager", "postServer register application public key failed, biz = " + this.f19314b + ", message = " + jSONObject2.optString("message") + ", traceId = " + jSONObject2.optString("traceId"));
                }
            } catch (IOException e10) {
                i.b("KeyRegisterManager", "postServer execute fail: " + e10.getClass().getName());
            }
            b0Var.close();
            return false;
        } catch (Throwable th) {
            if (0 != 0) {
                b0Var.close();
            }
            throw th;
        }
    }

    private void j() {
        String str = null;
        if (this.f19321i != null) {
            try {
                AuthTypeEnum authTypeEnum = AuthTypeEnum.DEVICE_CERT;
                AuthenticationManager authenticationManager = new AuthenticationManager(authTypeEnum);
                authenticationManager.a(this.f19321i);
                str = authenticationManager.b(this.f19313a + this.f19314b + this.f19315c + this.f19316d + authTypeEnum.a() + this.f19319g);
                this.f19317e = authTypeEnum.a();
            } catch (EncryptException e10) {
                i.a("KeyRegisterManager", "sign attempt to sign with the device key, but failed. " + e10);
            }
        }
        if (str == null) {
            try {
                AuthTypeEnum authTypeEnum2 = AuthTypeEnum.PSK;
                AuthenticationManager authenticationManager2 = new AuthenticationManager(authTypeEnum2);
                byte[] bArr = new byte[32];
                new SecureRandom().nextBytes(bArr);
                GukConfig gukConfig = new GukConfig();
                gukConfig.f(this.f19314b.getBytes(StandardCharsets.UTF_8));
                gukConfig.g(bArr);
                authenticationManager2.a(gukConfig);
                str = authenticationManager2.b(this.f19313a + this.f19314b + this.f19315c + this.f19316d + authTypeEnum2.a() + this.f19319g);
                this.f19317e = authTypeEnum2.a();
            } catch (EncryptException e11) {
                i.a("KeyRegisterManager", "sign attempt to sign with the group key, but failed. " + e11);
            }
        }
        if (str == null) {
            try {
                if (this.f19322j != null) {
                    str = this.f19322j.a(this.f19313a + this.f19314b + this.f19315c + this.f19316d + this.f19322j.b() + this.f19319g);
                    this.f19317e = this.f19322j.b();
                } else {
                    AuthTypeEnum authTypeEnum3 = AuthTypeEnum.SIMPLE;
                    AuthenticationManager authenticationManager3 = new AuthenticationManager(authTypeEnum3);
                    SimpleAuthConfig simpleAuthConfig = new SimpleAuthConfig();
                    simpleAuthConfig.b(this.f19314b);
                    authenticationManager3.a(simpleAuthConfig);
                    str = authenticationManager3.b(this.f19313a + this.f19314b + this.f19315c + this.f19316d + authTypeEnum3.a() + this.f19319g);
                    this.f19317e = authTypeEnum3.a();
                }
            } catch (EncryptException e12) {
                i.b("KeyRegisterManager", "sign sign error using simple scheme. " + e12);
                e12.printStackTrace();
            }
        }
        if (str != null) {
            this.f19318f = str;
            return;
        }
        throw new AuthException("Sign error, application key info sign failed!");
    }

    public boolean c() {
        a();
        j();
        if (this.f19318f != null) {
            return b();
        }
        throw new AuthException("Sign error, application key info sign failed!");
    }

    public void d(String str) {
        this.f19314b = str;
    }

    public void e(DeviceCertAuthConfig deviceCertAuthConfig) {
        this.f19321i = deviceCertAuthConfig;
    }

    public void f(String str) {
        this.f19313a = str;
    }

    public void g(String str) {
        this.f19320h = HttpUtil.c(str, "/crypto/cert/register");
    }

    public void h(PublicKey publicKey) {
        if (publicKey == null) {
            return;
        }
        this.f19316d = Base64Utils.b(publicKey.getEncoded());
    }

    public void i(PublicKey publicKey) {
        if (publicKey == null) {
            return;
        }
        this.f19315c = Base64Utils.b(publicKey.getEncoded());
    }
}
