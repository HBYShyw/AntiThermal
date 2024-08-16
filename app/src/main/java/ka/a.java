package ka;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.security.SecureRandom;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private final double f14221a;

    /* renamed from: b, reason: collision with root package name */
    private final ArrayList<String> f14222b;

    /* renamed from: c, reason: collision with root package name */
    private final ArrayList<String> f14223c;

    /* renamed from: d, reason: collision with root package name */
    private final double f14224d;

    /* renamed from: f, reason: collision with root package name */
    private final c f14226f;

    /* renamed from: e, reason: collision with root package name */
    private final SecureRandom f14225e = new SecureRandom();

    /* renamed from: g, reason: collision with root package name */
    private boolean f14227g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f14228h = false;

    public a(double d10, double d11, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        this.f14221a = d10;
        this.f14222b = arrayList;
        this.f14223c = arrayList2;
        this.f14224d = d11;
        this.f14226f = new c(arrayList.size(), d10);
    }

    public b a(String str) {
        if (str == null) {
            try {
                throw new la.a(" originData is null");
            } catch (la.a e10) {
                e10.printStackTrace();
                return null;
            }
        }
        if (!this.f14227g) {
            try {
                this.f14227g = true;
                double d10 = this.f14221a;
                if (d10 < UserProfileInfo.Constant.NA_LAT_LON || d10 > 20.0d) {
                    throw new la.a("epsilon less than 0 or larger than 20");
                }
                ArrayList<String> arrayList = this.f14222b;
                if (arrayList == null || this.f14223c == null) {
                    throw new la.a("null prior or swapDataset");
                }
                if (arrayList.size() <= 0) {
                    throw new la.a("empty prior");
                }
                if (this.f14222b.size() > (Math.exp(this.f14221a) * 3.0d) + 2.0d) {
                    throw new la.a("prior size larger than 3*exp(epsilon)+2");
                }
                if (this.f14223c.size() < 2) {
                    throw new la.a("swapDataset size less than 2");
                }
                double d11 = this.f14224d;
                if (d11 < UserProfileInfo.Constant.NA_LAT_LON || d11 > 1.0d) {
                    throw new la.a("swapProb less than 0 or larger than 1.0");
                }
            } catch (la.a e11) {
                this.f14228h = true;
                e11.printStackTrace();
            }
        }
        if (this.f14228h) {
            return null;
        }
        if (this.f14222b.contains(str)) {
            return this.f14222b.size() == 1 ? new b(str, false) : new b(this.f14226f.a(this.f14222b, str, this.f14225e), true);
        }
        if (this.f14225e.nextDouble() >= this.f14224d) {
            return new b(str, false);
        }
        ArrayList<String> arrayList2 = this.f14223c;
        return new b(arrayList2.get(this.f14225e.nextInt(arrayList2.size())), false);
    }
}
