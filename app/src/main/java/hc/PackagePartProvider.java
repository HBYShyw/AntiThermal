package hc;

import java.util.List;

/* compiled from: PackagePartProvider.kt */
/* renamed from: hc.x, reason: use source file name */
/* loaded from: classes2.dex */
public interface PackagePartProvider {

    /* compiled from: PackagePartProvider.kt */
    /* renamed from: hc.x$a */
    /* loaded from: classes2.dex */
    public static final class a implements PackagePartProvider {

        /* renamed from: a, reason: collision with root package name */
        public static final a f12208a = new a();

        private a() {
        }

        @Override // hc.PackagePartProvider
        public List<String> a(String str) {
            List<String> j10;
            za.k.e(str, "packageFqName");
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    List<String> a(String str);
}
