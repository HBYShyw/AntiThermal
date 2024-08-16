package y0;

import e1.Base64Utils;
import f1.CborByteString;
import f1.CborException;
import f1.CborMap;
import org.json.JSONObject;

/* compiled from: Message.java */
/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private byte[] f19776a;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f19777b;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f19778c;

    /* renamed from: d, reason: collision with root package name */
    private int f19779d = 1;

    /* renamed from: e, reason: collision with root package name */
    private int f19780e = 1;

    public static c a(String str) {
        c cVar = new c();
        JSONObject jSONObject = new JSONObject(str);
        try {
            i(cVar, jSONObject);
        } catch (CborException e10) {
            e10.printStackTrace();
        }
        try {
            j(cVar, jSONObject);
        } catch (CborException e11) {
            e11.printStackTrace();
        }
        String optString = jSONObject.optString("payload");
        if (!optString.isEmpty()) {
            cVar.l(Base64Utils.a(optString));
        }
        return cVar;
    }

    private String d() {
        int i10 = this.f19779d;
        return i10 == 5 ? "e_comp" : i10 == 6 ? "e_uncomp" : "e";
    }

    private String h() {
        int i10 = this.f19780e;
        return i10 == 3 ? "s_cert" : i10 == 4 ? "s_sn" : i10 == 2 ? "s_id" : i10 == 5 ? "s_comp" : i10 == 6 ? "s_uncomp" : "s";
    }

    private static void i(c cVar, Object obj) {
        if (obj instanceof CborMap) {
            CborMap cborMap = (CborMap) obj;
            if (cborMap.i("e")) {
                cVar.k(((CborByteString) cborMap.k("e")).i(), 1);
                return;
            } else if (cborMap.i("e_comp")) {
                cVar.k(((CborByteString) cborMap.k("e_comp")).i(), 5);
                return;
            } else if (cborMap.i("e_uncomp")) {
                cVar.k(((CborByteString) cborMap.k("e_uncomp")).i(), 6);
                return;
            }
        }
        if (obj instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) obj;
            String optString = jSONObject.optString("e");
            if (!optString.isEmpty()) {
                cVar.k(Base64Utils.a(optString), 1);
                return;
            }
            String optString2 = jSONObject.optString("e_comp");
            if (!optString2.isEmpty()) {
                cVar.k(Base64Utils.a(optString2), 5);
                return;
            }
            String optString3 = jSONObject.optString("e_uncomp");
            if (optString3.isEmpty()) {
                return;
            }
            cVar.k(Base64Utils.a(optString3), 6);
        }
    }

    private static void j(c cVar, Object obj) {
        if (obj instanceof CborMap) {
            CborMap cborMap = (CborMap) obj;
            if (cborMap.i("s")) {
                cVar.m(((CborByteString) cborMap.k("s")).i(), 1);
                return;
            }
            if (cborMap.i("s_cert")) {
                cVar.m(((CborByteString) cborMap.k("s_cert")).i(), 3);
                return;
            }
            if (cborMap.i("s_comp")) {
                cVar.m(((CborByteString) cborMap.k("s_comp")).i(), 5);
                return;
            }
            if (cborMap.i("s_uncomp")) {
                cVar.m(((CborByteString) cborMap.k("s_uncomp")).i(), 6);
                return;
            } else if (cborMap.i("s_sn")) {
                cVar.m(((CborByteString) cborMap.k("s_sn")).i(), 4);
                return;
            } else if (cborMap.i("s_id")) {
                cVar.m(((CborByteString) cborMap.k("s_id")).i(), 2);
                return;
            }
        }
        if (obj instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) obj;
            String optString = jSONObject.optString("s");
            if (!optString.isEmpty()) {
                cVar.m(Base64Utils.a(optString), 1);
                return;
            }
            String optString2 = jSONObject.optString("s_cert");
            if (!optString2.isEmpty()) {
                cVar.m(Base64Utils.a(optString2), 3);
                return;
            }
            String optString3 = jSONObject.optString("s_comp");
            if (!optString3.isEmpty()) {
                cVar.m(Base64Utils.a(optString3), 5);
                return;
            }
            String optString4 = jSONObject.optString("s_uncomp");
            if (!optString4.isEmpty()) {
                cVar.m(Base64Utils.a(optString4), 6);
                return;
            }
            String optString5 = jSONObject.optString("s_sn");
            if (!optString5.isEmpty()) {
                cVar.m(Base64Utils.a(optString5), 4);
                return;
            }
            String optString6 = jSONObject.optString("s_id");
            if (optString6.isEmpty()) {
                return;
            }
            cVar.m(Base64Utils.a(optString6), 2);
        }
    }

    public int b() {
        return this.f19779d;
    }

    public byte[] c() {
        return this.f19776a;
    }

    public byte[] e() {
        return this.f19778c;
    }

    public int f() {
        return this.f19780e;
    }

    public byte[] g() {
        return this.f19777b;
    }

    public void k(byte[] bArr, int i10) {
        this.f19776a = bArr;
        this.f19779d = i10;
    }

    public void l(byte[] bArr) {
        this.f19778c = bArr;
    }

    public void m(byte[] bArr, int i10) {
        this.f19777b = bArr;
        this.f19780e = i10;
    }

    public String n() {
        JSONObject jSONObject = new JSONObject();
        if (this.f19776a != null) {
            jSONObject.put(d(), Base64Utils.b(this.f19776a));
        }
        if (this.f19777b != null) {
            jSONObject.put(h(), Base64Utils.b(this.f19777b));
        }
        byte[] bArr = this.f19778c;
        if (bArr != null) {
            jSONObject.put("payload", Base64Utils.b(bArr));
        }
        return jSONObject.toString();
    }
}
