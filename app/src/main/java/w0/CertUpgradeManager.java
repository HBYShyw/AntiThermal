package w0;

import android.content.Context;
import e1.HttpUtil;
import e1.i;
import i0.EncryptException;
import j0.CertUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.json.JSONException;
import org.json.JSONObject;
import s0.CertParameters;
import t0.InvalidArgumentException;
import x0.UpgradeCertRequest;
import x0.UpgradeCertResponse;
import zd.ResponseBody;
import zd.b0;

/* compiled from: CertUpgradeManager.java */
/* renamed from: w0.a, reason: use source file name */
/* loaded from: classes.dex */
public class CertUpgradeManager {

    /* renamed from: a, reason: collision with root package name */
    private final UpgradeCertRequest f19310a;

    /* renamed from: b, reason: collision with root package name */
    private String f19311b;

    /* renamed from: c, reason: collision with root package name */
    private final Context f19312c;

    public CertUpgradeManager(Context context) {
        this(context, new UpgradeCertRequest());
    }

    private void a() {
        if (this.f19311b != null) {
            if (this.f19310a.a() == null) {
                throw new InvalidArgumentException("Biz name cannot be empty");
            }
            return;
        }
        throw new InvalidArgumentException("Hostname cannot be empty");
    }

    private int b(JSONObject jSONObject, UpgradeCertResponse upgradeCertResponse) {
        JSONObject jSONObject2 = jSONObject.getJSONObject("data");
        String optString = jSONObject2.optString("cert4Sign");
        String optString2 = jSONObject2.optString("cert4Encrypt");
        long optLong = jSONObject2.optLong("version");
        if (optString.isEmpty() || optString2.isEmpty()) {
            return -1;
        }
        X509Certificate e10 = CertUtil.e(optString.getBytes(StandardCharsets.UTF_8));
        X509Certificate e11 = CertUtil.e(optString2.getBytes(StandardCharsets.UTF_8));
        CertParameters s7 = new CertParameters.b().u(this.f19312c).v(e10).s();
        CertParameters s10 = new CertParameters.b().u(this.f19312c).v(e11).s();
        if (CertUtil.a(s7) && CertUtil.a(s10)) {
            i.a("CertUpgradeManager", "extractCertificate request " + this.f19310a.a() + " certificate online succeed and certificate verification passed.");
            upgradeCertResponse.a(this.f19310a.a(), e10, e11, optLong);
            return 200;
        }
        i.a("CertUpgradeManager", "extractCertificate request " + this.f19310a.a() + " certificate online succeed and certificate verification failed.");
        return -1;
    }

    private int c(b0 b0Var, UpgradeCertResponse upgradeCertResponse) {
        int code = b0Var.getCode();
        i.a("CertUpgradeManager", "parseResponse postServer server response code " + code);
        if (code == 200) {
            ResponseBody f20511k = b0Var.getF20511k();
            if (f20511k == null) {
                i.b("CertUpgradeManager", "parseResponse returns null on responses");
                return -1;
            }
            JSONObject jSONObject = new JSONObject(f20511k.v());
            int i10 = jSONObject.getInt("code");
            if (i10 == 200) {
                i.a("CertUpgradeManager", "parseResponse ok.");
                return b(jSONObject, upgradeCertResponse);
            }
            if (i10 != 304 && i10 != 3001403) {
                i.b("CertUpgradeManager", "parseResponse error, code = " + i10 + ", message = " + jSONObject.optString("message") + ", traceId = " + jSONObject.optString("traceId"));
            } else {
                i.a("CertUpgradeManager", "parseResponse not modified.");
                return i10;
            }
        } else {
            i.b("CertUpgradeManager", "parseResponse request error, status: " + code + " " + b0Var.getMessage());
        }
        return -1;
    }

    private int d(String str, String str2, UpgradeCertResponse upgradeCertResponse) {
        if (str != null) {
            b0 b0Var = null;
            try {
                try {
                    b0Var = HttpUtil.b(str, str2);
                    int c10 = c(b0Var, upgradeCertResponse);
                    if (b0Var != null) {
                        b0Var.close();
                    }
                    return c10;
                } catch (EncryptException | CertificateException | JSONException | InvalidArgumentException e10) {
                    i.b("CertUpgradeManager", "post parseResponse error. " + e10);
                    if (b0Var == null) {
                        return -1;
                    }
                    b0Var.close();
                    return -1;
                } catch (IOException e11) {
                    i.b("CertUpgradeManager", "post execute fail: " + e11.getClass().getName());
                    if (b0Var == null) {
                        return -1;
                    }
                    b0Var.close();
                    return -1;
                }
            } catch (Throwable th) {
                if (b0Var != null) {
                    b0Var.close();
                }
                throw th;
            }
        }
        throw new InvalidArgumentException("Missing available url");
    }

    public void e(String str) {
        this.f19310a.c(str);
    }

    public void f(String str) {
        this.f19311b = str;
    }

    public void g(long j10) {
        this.f19310a.d(j10);
    }

    public int h(UpgradeCertResponse upgradeCertResponse) {
        if (upgradeCertResponse != null) {
            a();
            String c10 = HttpUtil.c(this.f19311b, "/crypto/cert/upgrade");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("biz", this.f19310a.a());
            if (this.f19310a.b().longValue() != 0) {
                jSONObject.put("version", this.f19310a.b());
            }
            i.a("CertUpgradeManager", "upgradeCert start to request " + this.f19310a.a() + " certificate online.");
            return d(c10, jSONObject.toString(), upgradeCertResponse);
        }
        throw new InvalidArgumentException("UpgradeCertResponse is null");
    }

    public CertUpgradeManager(Context context, UpgradeCertRequest upgradeCertRequest) {
        this.f19312c = context;
        this.f19310a = upgradeCertRequest;
    }
}
