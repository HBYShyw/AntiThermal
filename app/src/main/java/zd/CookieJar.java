package zd;

import java.util.List;
import kotlin.Metadata;

/* compiled from: CookieJar.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0001\bJ\u001e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H&J\u0016\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\n"}, d2 = {"Lzd/n;", "", "Lzd/u;", "url", "", "Lzd/m;", "cookies", "Lma/f0;", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.n, reason: use source file name */
/* loaded from: classes2.dex */
public interface CookieJar {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20685a = a.f20687a;

    /* renamed from: b, reason: collision with root package name */
    public static final CookieJar f20686b = new a.C0125a();

    /* compiled from: CookieJar.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0007B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0003\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0001¨\u0006\b"}, d2 = {"Lzd/n$a;", "", "Lzd/n;", "NO_COOKIES", "Lzd/n;", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.n$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f20687a = new a();

        /* compiled from: CookieJar.kt */
        @Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u001e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0016J\u0016\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\f"}, d2 = {"Lzd/n$a$a;", "Lzd/n;", "Lzd/u;", "url", "", "Lzd/m;", "cookies", "Lma/f0;", "a", "b", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: zd.n$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        private static final class C0125a implements CookieJar {
            @Override // zd.CookieJar
            public void a(HttpUrl httpUrl, List<Cookie> list) {
                za.k.e(httpUrl, "url");
                za.k.e(list, "cookies");
            }

            @Override // zd.CookieJar
            public List<Cookie> b(HttpUrl url) {
                List<Cookie> j10;
                za.k.e(url, "url");
                j10 = kotlin.collections.r.j();
                return j10;
            }
        }

        private a() {
        }
    }

    void a(HttpUrl httpUrl, List<Cookie> list);

    List<Cookie> b(HttpUrl url);
}
