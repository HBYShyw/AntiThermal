package ce;

import ae.d;
import com.oplus.statistics.util.TimeInfoUtil;
import fe.dates;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.k;
import zd.CacheControl;
import zd.Headers;
import zd.b0;
import zd.z;

/* compiled from: CacheStrategy.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0002\n\u0005B\u001d\b\u0000\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0007¢\u0006\u0004\b\f\u0010\rR\u0019\u0010\u0003\u001a\u0004\u0018\u00010\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006R\u0019\u0010\b\u001a\u0004\u0018\u00010\u00078\u0006¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Lce/b;", "", "Lzd/z;", "networkRequest", "Lzd/z;", "b", "()Lzd/z;", "Lzd/b0;", "cacheResponse", "Lzd/b0;", "a", "()Lzd/b0;", "<init>", "(Lzd/z;Lzd/b0;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ce.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class CacheStrategy {

    /* renamed from: c, reason: collision with root package name */
    public static final a f5335c = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final z f5336a;

    /* renamed from: b, reason: collision with root package name */
    private final b0 f5337b;

    /* compiled from: CacheStrategy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\b\u0010\tJ\u0016\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\n"}, d2 = {"Lce/b$a;", "", "Lzd/b0;", "response", "Lzd/z;", "request", "", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ce.b$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean a(b0 response, z request) {
            k.e(response, "response");
            k.e(request, "request");
            int code = response.getCode();
            if (code != 200 && code != 410 && code != 414 && code != 501 && code != 203 && code != 204) {
                if (code != 307) {
                    if (code != 308 && code != 404 && code != 405) {
                        switch (code) {
                            case 300:
                            case 301:
                                break;
                            case 302:
                                break;
                            default:
                                return false;
                        }
                    }
                }
                if (b0.S(response, "Expires", null, 2, null) == null && response.c().getF20541c() == -1 && !response.c().getF20544f() && !response.c().getF20543e()) {
                    return false;
                }
            }
            return (response.c().getF20540b() || request.b().getF20540b()) ? false : true;
        }
    }

    /* compiled from: CacheStrategy.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\r\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\t\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e¢\u0006\u0004\b\u0010\u0010\u0011J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0005\u001a\u00020\u0004H\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\b\u001a\u00020\u0006H\u0002J\u0010\u0010\u000b\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0002J\u0006\u0010\f\u001a\u00020\u0004¨\u0006\u0012"}, d2 = {"Lce/b$b;", "", "", "f", "Lce/b;", "c", "", "d", "a", "Lzd/z;", "request", "e", "b", "nowMillis", "Lzd/b0;", "cacheResponse", "<init>", "(JLzd/z;Lzd/b0;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ce.b$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final long f5338a;

        /* renamed from: b, reason: collision with root package name */
        private final z f5339b;

        /* renamed from: c, reason: collision with root package name */
        private final b0 f5340c;

        /* renamed from: d, reason: collision with root package name */
        private Date f5341d;

        /* renamed from: e, reason: collision with root package name */
        private String f5342e;

        /* renamed from: f, reason: collision with root package name */
        private Date f5343f;

        /* renamed from: g, reason: collision with root package name */
        private String f5344g;

        /* renamed from: h, reason: collision with root package name */
        private Date f5345h;

        /* renamed from: i, reason: collision with root package name */
        private long f5346i;

        /* renamed from: j, reason: collision with root package name */
        private long f5347j;

        /* renamed from: k, reason: collision with root package name */
        private String f5348k;

        /* renamed from: l, reason: collision with root package name */
        private int f5349l;

        public b(long j10, z zVar, b0 b0Var) {
            boolean r10;
            boolean r11;
            boolean r12;
            boolean r13;
            boolean r14;
            k.e(zVar, "request");
            this.f5338a = j10;
            this.f5339b = zVar;
            this.f5340c = b0Var;
            this.f5349l = -1;
            if (b0Var != null) {
                this.f5346i = b0Var.getF20515o();
                this.f5347j = b0Var.getF20516p();
                Headers f20510j = b0Var.getF20510j();
                int i10 = 0;
                int size = f20510j.size();
                while (i10 < size) {
                    int i11 = i10 + 1;
                    String e10 = f20510j.e(i10);
                    String g6 = f20510j.g(i10);
                    r10 = StringsJVM.r(e10, "Date", true);
                    if (r10) {
                        this.f5341d = dates.a(g6);
                        this.f5342e = g6;
                    } else {
                        r11 = StringsJVM.r(e10, "Expires", true);
                        if (r11) {
                            this.f5345h = dates.a(g6);
                        } else {
                            r12 = StringsJVM.r(e10, "Last-Modified", true);
                            if (r12) {
                                this.f5343f = dates.a(g6);
                                this.f5344g = g6;
                            } else {
                                r13 = StringsJVM.r(e10, "ETag", true);
                                if (r13) {
                                    this.f5348k = g6;
                                } else {
                                    r14 = StringsJVM.r(e10, "Age", true);
                                    if (r14) {
                                        this.f5349l = d.U(g6, -1);
                                    }
                                }
                            }
                        }
                    }
                    i10 = i11;
                }
            }
        }

        private final long a() {
            Date date = this.f5341d;
            long max = date != null ? Math.max(0L, this.f5347j - date.getTime()) : 0L;
            int i10 = this.f5349l;
            if (i10 != -1) {
                max = Math.max(max, TimeUnit.SECONDS.toMillis(i10));
            }
            long j10 = this.f5347j;
            return max + (j10 - this.f5346i) + (this.f5338a - j10);
        }

        private final CacheStrategy c() {
            if (this.f5340c == null) {
                return new CacheStrategy(this.f5339b, null);
            }
            if (this.f5339b.f() && this.f5340c.getF20509i() == null) {
                return new CacheStrategy(this.f5339b, null);
            }
            if (!CacheStrategy.f5335c.a(this.f5340c, this.f5339b)) {
                return new CacheStrategy(this.f5339b, null);
            }
            CacheControl b10 = this.f5339b.b();
            if (!b10.getF20539a() && !e(this.f5339b)) {
                CacheControl c10 = this.f5340c.c();
                long a10 = a();
                long d10 = d();
                if (b10.getF20541c() != -1) {
                    d10 = Math.min(d10, TimeUnit.SECONDS.toMillis(b10.getF20541c()));
                }
                long j10 = 0;
                long millis = b10.getF20547i() != -1 ? TimeUnit.SECONDS.toMillis(b10.getF20547i()) : 0L;
                if (!c10.getF20545g() && b10.getF20546h() != -1) {
                    j10 = TimeUnit.SECONDS.toMillis(b10.getF20546h());
                }
                if (!c10.getF20539a()) {
                    long j11 = millis + a10;
                    if (j11 < j10 + d10) {
                        b0.a e02 = this.f5340c.e0();
                        if (j11 >= d10) {
                            e02.a("Warning", "110 HttpURLConnection \"Response is stale\"");
                        }
                        if (a10 > TimeInfoUtil.MILLISECOND_OF_A_DAY && f()) {
                            e02.a("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                        }
                        return new CacheStrategy(null, e02.c());
                    }
                }
                String str = this.f5348k;
                String str2 = "If-Modified-Since";
                if (str != null) {
                    str2 = "If-None-Match";
                } else if (this.f5343f != null) {
                    str = this.f5344g;
                } else if (this.f5341d != null) {
                    str = this.f5342e;
                } else {
                    return new CacheStrategy(this.f5339b, null);
                }
                Headers.a f10 = this.f5339b.getF20798c().f();
                k.b(str);
                f10.c(str2, str);
                return new CacheStrategy(this.f5339b.h().e(f10.d()).b(), this.f5340c);
            }
            return new CacheStrategy(this.f5339b, null);
        }

        private final long d() {
            Long valueOf;
            b0 b0Var = this.f5340c;
            k.b(b0Var);
            if (b0Var.c().getF20541c() != -1) {
                return TimeUnit.SECONDS.toMillis(r0.getF20541c());
            }
            Date date = this.f5345h;
            if (date != null) {
                Date date2 = this.f5341d;
                valueOf = date2 != null ? Long.valueOf(date2.getTime()) : null;
                long time = date.getTime() - (valueOf == null ? this.f5347j : valueOf.longValue());
                if (time > 0) {
                    return time;
                }
                return 0L;
            }
            if (this.f5343f == null || this.f5340c.getF20505e().getF20796a().m() != null) {
                return 0L;
            }
            Date date3 = this.f5341d;
            valueOf = date3 != null ? Long.valueOf(date3.getTime()) : null;
            long longValue = valueOf == null ? this.f5346i : valueOf.longValue();
            Date date4 = this.f5343f;
            k.b(date4);
            long time2 = longValue - date4.getTime();
            if (time2 > 0) {
                return time2 / 10;
            }
            return 0L;
        }

        private final boolean e(z request) {
            return (request.d("If-Modified-Since") == null && request.d("If-None-Match") == null) ? false : true;
        }

        private final boolean f() {
            b0 b0Var = this.f5340c;
            k.b(b0Var);
            return b0Var.c().getF20541c() == -1 && this.f5345h == null;
        }

        public final CacheStrategy b() {
            CacheStrategy c10 = c();
            return (c10.getF5336a() == null || !this.f5339b.b().getF20548j()) ? c10 : new CacheStrategy(null, null);
        }
    }

    public CacheStrategy(z zVar, b0 b0Var) {
        this.f5336a = zVar;
        this.f5337b = b0Var;
    }

    /* renamed from: a, reason: from getter */
    public final b0 getF5337b() {
        return this.f5337b;
    }

    /* renamed from: b, reason: from getter */
    public final z getF5336a() {
        return this.f5336a;
    }
}
