package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

/* compiled from: FontRequest.java */
/* renamed from: androidx.core.provider.e, reason: use source file name */
/* loaded from: classes.dex */
public final class FontRequest {

    /* renamed from: a, reason: collision with root package name */
    private final String f2229a;

    /* renamed from: b, reason: collision with root package name */
    private final String f2230b;

    /* renamed from: c, reason: collision with root package name */
    private final String f2231c;

    /* renamed from: d, reason: collision with root package name */
    private final List<List<byte[]>> f2232d;

    /* renamed from: e, reason: collision with root package name */
    private final int f2233e = 0;

    /* renamed from: f, reason: collision with root package name */
    private final String f2234f;

    public FontRequest(String str, String str2, String str3, List<List<byte[]>> list) {
        this.f2229a = (String) Preconditions.d(str);
        this.f2230b = (String) Preconditions.d(str2);
        this.f2231c = (String) Preconditions.d(str3);
        this.f2232d = (List) Preconditions.d(list);
        this.f2234f = a(str, str2, str3);
    }

    private String a(String str, String str2, String str3) {
        return str + "-" + str2 + "-" + str3;
    }

    public List<List<byte[]>> b() {
        return this.f2232d;
    }

    public int c() {
        return this.f2233e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String d() {
        return this.f2234f;
    }

    public String e() {
        return this.f2229a;
    }

    public String f() {
        return this.f2230b;
    }

    public String g() {
        return this.f2231c;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("FontRequest {mProviderAuthority: " + this.f2229a + ", mProviderPackage: " + this.f2230b + ", mQuery: " + this.f2231c + ", mCertificates:");
        for (int i10 = 0; i10 < this.f2232d.size(); i10++) {
            sb2.append(" [");
            List<byte[]> list = this.f2232d.get(i10);
            for (int i11 = 0; i11 < list.size(); i11++) {
                sb2.append(" \"");
                sb2.append(Base64.encodeToString(list.get(i11), 0));
                sb2.append("\"");
            }
            sb2.append(" ]");
        }
        sb2.append("}");
        sb2.append("mCertificatesArray: " + this.f2233e);
        return sb2.toString();
    }
}
