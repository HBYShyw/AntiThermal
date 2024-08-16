package u0;

import c1.CryptoEngCmd;
import c1.TAInterfaceException;
import d1.HmacAlg;
import e1.Base64Utils;
import i0.EncryptException;
import j0.HashUtil;
import j0.HmacUtil;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;
import s0.CryptoParameters;
import t0.InvalidAlgorithmException;
import v0.GukConfig;
import v0.GukSignatureData;

/* compiled from: GukManager.java */
/* renamed from: u0.a, reason: use source file name */
/* loaded from: classes.dex */
public class GukManager {

    /* renamed from: a, reason: collision with root package name */
    private GukConfig f18842a;

    /* renamed from: b, reason: collision with root package name */
    private String f18843b;

    /* renamed from: c, reason: collision with root package name */
    private String f18844c;

    /* renamed from: d, reason: collision with root package name */
    private final CryptoParameters.b f18845d = CryptoParameters.b.f17974f;

    public GukManager() {
        GukConfig gukConfig = new GukConfig();
        this.f18842a = gukConfig;
        this.f18843b = gukConfig.c();
        this.f18844c = null;
    }

    private static String a(String str, GukConfig gukConfig) {
        try {
            return CryptoEngCmd.d(str, gukConfig.e(), gukConfig.b(), gukConfig.d(), HmacAlg.a(gukConfig.a()));
        } catch (TAInterfaceException unused) {
            throw new TAInterfaceException("The current phone version does not support 'group key' function.");
        }
    }

    public void b(GukConfig gukConfig) {
        if (gukConfig != null) {
            this.f18842a = gukConfig;
            this.f18843b = gukConfig.c();
            this.f18844c = null;
        }
    }

    public GukSignatureData c(byte[] bArr) {
        try {
            JSONObject jSONObject = new JSONObject(a(this.f18843b, this.f18842a));
            String string = jSONObject.getString("okm");
            this.f18844c = jSONObject.getString("version");
            byte[] c10 = HashUtil.c(bArr);
            byte[] b10 = HmacUtil.b(c10, Base64Utils.a(string));
            GukSignatureData gukSignatureData = new GukSignatureData();
            gukSignatureData.p(b10);
            gukSignatureData.i(c10);
            gukSignatureData.l(this.f18843b);
            gukSignatureData.n(this.f18844c);
            gukSignatureData.k(this.f18842a.b());
            gukSignatureData.o(this.f18842a.e());
            gukSignatureData.m(this.f18842a.d());
            gukSignatureData.j(this.f18842a.a());
            return gukSignatureData;
        } catch (TAInterfaceException | NoSuchAlgorithmException | JSONException | InvalidAlgorithmException e10) {
            throw new EncryptException(e10);
        }
    }
}
