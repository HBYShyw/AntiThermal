package zd;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import sd.StringsJVM;
import za.DefaultConstructorMarker;

/* compiled from: CacheControl.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u001a\u0018\u00002\u00020\u0001:\u0002!\u0013Bs\b\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\t\u001a\u00020\u0004\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\u0006\u0010\u0010\u001a\u00020\u000b\u0012\u0006\u0010\u0012\u001a\u00020\u0004\u0012\u0006\u0010\u0014\u001a\u00020\u0004\u0012\u0006\u0010\u0016\u001a\u00020\u0004\u0012\u0006\u0010\u0018\u001a\u00020\u000b\u0012\u0006\u0010\u001a\u001a\u00020\u000b\u0012\u0006\u0010\u001c\u001a\u00020\u0004\u0012\u0006\u0010\u001e\u001a\u00020\u0004\u0012\u0006\u0010 \u001a\u00020\u0004\u0012\b\u0010\"\u001a\u0004\u0018\u00010\u0002¢\u0006\u0004\b#\u0010$J\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0017\u0010\u0005\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\t\u0010\u0006\u001a\u0004\b\n\u0010\bR\u0017\u0010\f\u001a\u00020\u000b8\u0007¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\u00020\u000b8\u0007¢\u0006\f\n\u0004\b\u0010\u0010\r\u001a\u0004\b\u0011\u0010\u000fR\u0017\u0010\u0012\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0006\u001a\u0004\b\u0013\u0010\bR\u0017\u0010\u0014\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0014\u0010\u0006\u001a\u0004\b\u0015\u0010\bR\u0017\u0010\u0016\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\u0016\u0010\u0006\u001a\u0004\b\u0017\u0010\bR\u0017\u0010\u0018\u001a\u00020\u000b8\u0007¢\u0006\f\n\u0004\b\u0018\u0010\r\u001a\u0004\b\u0019\u0010\u000fR\u0017\u0010\u001a\u001a\u00020\u000b8\u0007¢\u0006\f\n\u0004\b\u001a\u0010\r\u001a\u0004\b\u001b\u0010\u000fR\u0017\u0010\u001c\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\u001c\u0010\u0006\u001a\u0004\b\u001d\u0010\bR\u0017\u0010\u001e\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\u001e\u0010\u0006\u001a\u0004\b\u001f\u0010\bR\u0017\u0010 \u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b \u0010\u0006\u001a\u0004\b!\u0010\b¨\u0006%"}, d2 = {"Lzd/d;", "", "", "toString", "", "noCache", "Z", "h", "()Z", "noStore", "i", "", "maxAgeSeconds", "I", "d", "()I", "sMaxAgeSeconds", "l", "isPrivate", "b", "isPublic", "c", "mustRevalidate", "g", "maxStaleSeconds", "e", "minFreshSeconds", "f", "onlyIfCached", "k", "noTransform", "j", "immutable", "a", "headerValue", "<init>", "(ZZIIZZZIIZZZLjava/lang/String;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class CacheControl {

    /* renamed from: n, reason: collision with root package name */
    public static final b f20536n = new b(null);

    /* renamed from: o, reason: collision with root package name */
    public static final CacheControl f20537o = new a().d().a();

    /* renamed from: p, reason: collision with root package name */
    public static final CacheControl f20538p = new a().e().c(Integer.MAX_VALUE, TimeUnit.SECONDS).a();

    /* renamed from: a, reason: collision with root package name */
    private final boolean f20539a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f20540b;

    /* renamed from: c, reason: collision with root package name */
    private final int f20541c;

    /* renamed from: d, reason: collision with root package name */
    private final int f20542d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f20543e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f20544f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f20545g;

    /* renamed from: h, reason: collision with root package name */
    private final int f20546h;

    /* renamed from: i, reason: collision with root package name */
    private final int f20547i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f20548j;

    /* renamed from: k, reason: collision with root package name */
    private final boolean f20549k;

    /* renamed from: l, reason: collision with root package name */
    private final boolean f20550l;

    /* renamed from: m, reason: collision with root package name */
    private String f20551m;

    /* compiled from: CacheControl.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\r\u0010\u000eJ\f\u0010\u0004\u001a\u00020\u0003*\u00020\u0002H\u0002J\u0006\u0010\u0005\u001a\u00020\u0000J\u0016\u0010\t\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0007J\u0006\u0010\n\u001a\u00020\u0000J\u0006\u0010\f\u001a\u00020\u000b¨\u0006\u000f"}, d2 = {"Lzd/d$a;", "", "", "", "b", "d", "maxStale", "Ljava/util/concurrent/TimeUnit;", "timeUnit", "c", "e", "Lzd/d;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.d$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private boolean f20552a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f20553b;

        /* renamed from: c, reason: collision with root package name */
        private int f20554c = -1;

        /* renamed from: d, reason: collision with root package name */
        private int f20555d = -1;

        /* renamed from: e, reason: collision with root package name */
        private int f20556e = -1;

        /* renamed from: f, reason: collision with root package name */
        private boolean f20557f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f20558g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f20559h;

        private final int b(long j10) {
            if (j10 > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) j10;
        }

        public final CacheControl a() {
            return new CacheControl(this.f20552a, this.f20553b, this.f20554c, -1, false, false, false, this.f20555d, this.f20556e, this.f20557f, this.f20558g, this.f20559h, null, null);
        }

        public final a c(int maxStale, TimeUnit timeUnit) {
            za.k.e(timeUnit, "timeUnit");
            if (maxStale >= 0) {
                this.f20555d = b(timeUnit.toSeconds(maxStale));
                return this;
            }
            throw new IllegalArgumentException(za.k.l("maxStale < 0: ", Integer.valueOf(maxStale)).toString());
        }

        public final a d() {
            this.f20552a = true;
            return this;
        }

        public final a e() {
            this.f20557f = true;
            return this;
        }
    }

    /* compiled from: CacheControl.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u001e\u0010\u0006\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0004H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0007R\u0014\u0010\u000b\u001a\u00020\t8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\t8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\r\u0010\f¨\u0006\u0010"}, d2 = {"Lzd/d$b;", "", "", "characters", "", "startIndex", "a", "Lzd/t;", "headers", "Lzd/d;", "b", "FORCE_CACHE", "Lzd/d;", "FORCE_NETWORK", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.d$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final int a(String str, String str2, int i10) {
            boolean H;
            int length = str.length();
            while (i10 < length) {
                int i11 = i10 + 1;
                H = sd.v.H(str2, str.charAt(i10), false, 2, null);
                if (H) {
                    return i10;
                }
                i10 = i11;
            }
            return str.length();
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x004c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final CacheControl b(Headers headers) {
            boolean r10;
            boolean r11;
            int i10;
            CharSequence J0;
            int i11;
            String str;
            boolean r12;
            boolean r13;
            boolean r14;
            boolean r15;
            boolean r16;
            boolean r17;
            boolean r18;
            boolean r19;
            boolean r20;
            boolean r21;
            boolean r22;
            boolean r23;
            CharSequence J02;
            int U;
            Headers headers2 = headers;
            za.k.e(headers2, "headers");
            int size = headers.size();
            boolean z10 = true;
            boolean z11 = true;
            int i12 = 0;
            String str2 = null;
            boolean z12 = false;
            boolean z13 = false;
            int i13 = -1;
            int i14 = -1;
            boolean z14 = false;
            boolean z15 = false;
            boolean z16 = false;
            int i15 = -1;
            int i16 = -1;
            boolean z17 = false;
            boolean z18 = false;
            boolean z19 = false;
            while (i12 < size) {
                int i17 = i12 + 1;
                String e10 = headers2.e(i12);
                String g6 = headers2.g(i12);
                r10 = StringsJVM.r(e10, "Cache-Control", z10);
                if (!r10) {
                    r11 = StringsJVM.r(e10, "Pragma", z10);
                    if (!r11) {
                        headers2 = headers;
                        i12 = i17;
                    }
                } else if (str2 == null) {
                    str2 = g6;
                    i10 = 0;
                    while (i10 < g6.length()) {
                        int a10 = a(g6, "=,;", i10);
                        String substring = g6.substring(i10, a10);
                        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                        J0 = sd.v.J0(substring);
                        String obj = J0.toString();
                        if (a10 == g6.length() || g6.charAt(a10) == ',' || g6.charAt(a10) == ';') {
                            i11 = a10 + 1;
                            str = null;
                        } else {
                            int C = ae.d.C(g6, a10 + 1);
                            if (C < g6.length() && g6.charAt(C) == '\"') {
                                int i18 = C + 1;
                                U = sd.v.U(g6, '\"', i18, false, 4, null);
                                str = g6.substring(i18, U);
                                za.k.d(str, "this as java.lang.String…ing(startIndex, endIndex)");
                                i11 = U + 1;
                            } else {
                                i11 = a(g6, ",;", C);
                                String substring2 = g6.substring(C, i11);
                                za.k.d(substring2, "this as java.lang.String…ing(startIndex, endIndex)");
                                J02 = sd.v.J0(substring2);
                                str = J02.toString();
                            }
                        }
                        z10 = true;
                        r12 = StringsJVM.r("no-cache", obj, true);
                        if (r12) {
                            i10 = i11;
                            z12 = true;
                        } else {
                            r13 = StringsJVM.r("no-store", obj, true);
                            if (r13) {
                                i10 = i11;
                                z13 = true;
                            } else {
                                r14 = StringsJVM.r("max-age", obj, true);
                                if (r14) {
                                    i13 = ae.d.U(str, -1);
                                } else {
                                    r15 = StringsJVM.r("s-maxage", obj, true);
                                    if (r15) {
                                        i14 = ae.d.U(str, -1);
                                    } else {
                                        r16 = StringsJVM.r("private", obj, true);
                                        if (r16) {
                                            i10 = i11;
                                            z14 = true;
                                        } else {
                                            r17 = StringsJVM.r("public", obj, true);
                                            if (r17) {
                                                i10 = i11;
                                                z15 = true;
                                            } else {
                                                r18 = StringsJVM.r("must-revalidate", obj, true);
                                                if (r18) {
                                                    i10 = i11;
                                                    z16 = true;
                                                } else {
                                                    r19 = StringsJVM.r("max-stale", obj, true);
                                                    if (r19) {
                                                        i15 = ae.d.U(str, Integer.MAX_VALUE);
                                                    } else {
                                                        r20 = StringsJVM.r("min-fresh", obj, true);
                                                        if (r20) {
                                                            i16 = ae.d.U(str, -1);
                                                        } else {
                                                            r21 = StringsJVM.r("only-if-cached", obj, true);
                                                            if (r21) {
                                                                i10 = i11;
                                                                z17 = true;
                                                            } else {
                                                                r22 = StringsJVM.r("no-transform", obj, true);
                                                                if (r22) {
                                                                    i10 = i11;
                                                                    z18 = true;
                                                                } else {
                                                                    r23 = StringsJVM.r("immutable", obj, true);
                                                                    if (r23) {
                                                                        i10 = i11;
                                                                        z19 = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                i10 = i11;
                            }
                        }
                    }
                    headers2 = headers;
                    i12 = i17;
                }
                z11 = false;
                i10 = 0;
                while (i10 < g6.length()) {
                }
                headers2 = headers;
                i12 = i17;
            }
            return new CacheControl(z12, z13, i13, i14, z14, z15, z16, i15, i16, z17, z18, z19, !z11 ? null : str2, null);
        }
    }

    private CacheControl(boolean z10, boolean z11, int i10, int i11, boolean z12, boolean z13, boolean z14, int i12, int i13, boolean z15, boolean z16, boolean z17, String str) {
        this.f20539a = z10;
        this.f20540b = z11;
        this.f20541c = i10;
        this.f20542d = i11;
        this.f20543e = z12;
        this.f20544f = z13;
        this.f20545g = z14;
        this.f20546h = i12;
        this.f20547i = i13;
        this.f20548j = z15;
        this.f20549k = z16;
        this.f20550l = z17;
        this.f20551m = str;
    }

    public /* synthetic */ CacheControl(boolean z10, boolean z11, int i10, int i11, boolean z12, boolean z13, boolean z14, int i12, int i13, boolean z15, boolean z16, boolean z17, String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(z10, z11, i10, i11, z12, z13, z14, i12, i13, z15, z16, z17, str);
    }

    /* renamed from: a, reason: from getter */
    public final boolean getF20550l() {
        return this.f20550l;
    }

    /* renamed from: b, reason: from getter */
    public final boolean getF20543e() {
        return this.f20543e;
    }

    /* renamed from: c, reason: from getter */
    public final boolean getF20544f() {
        return this.f20544f;
    }

    /* renamed from: d, reason: from getter */
    public final int getF20541c() {
        return this.f20541c;
    }

    /* renamed from: e, reason: from getter */
    public final int getF20546h() {
        return this.f20546h;
    }

    /* renamed from: f, reason: from getter */
    public final int getF20547i() {
        return this.f20547i;
    }

    /* renamed from: g, reason: from getter */
    public final boolean getF20545g() {
        return this.f20545g;
    }

    /* renamed from: h, reason: from getter */
    public final boolean getF20539a() {
        return this.f20539a;
    }

    /* renamed from: i, reason: from getter */
    public final boolean getF20540b() {
        return this.f20540b;
    }

    /* renamed from: j, reason: from getter */
    public final boolean getF20549k() {
        return this.f20549k;
    }

    /* renamed from: k, reason: from getter */
    public final boolean getF20548j() {
        return this.f20548j;
    }

    /* renamed from: l, reason: from getter */
    public final int getF20542d() {
        return this.f20542d;
    }

    public String toString() {
        String str = this.f20551m;
        if (str != null) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        if (getF20539a()) {
            sb2.append("no-cache, ");
        }
        if (getF20540b()) {
            sb2.append("no-store, ");
        }
        if (getF20541c() != -1) {
            sb2.append("max-age=");
            sb2.append(getF20541c());
            sb2.append(", ");
        }
        if (getF20542d() != -1) {
            sb2.append("s-maxage=");
            sb2.append(getF20542d());
            sb2.append(", ");
        }
        if (getF20543e()) {
            sb2.append("private, ");
        }
        if (getF20544f()) {
            sb2.append("public, ");
        }
        if (getF20545g()) {
            sb2.append("must-revalidate, ");
        }
        if (getF20546h() != -1) {
            sb2.append("max-stale=");
            sb2.append(getF20546h());
            sb2.append(", ");
        }
        if (getF20547i() != -1) {
            sb2.append("min-fresh=");
            sb2.append(getF20547i());
            sb2.append(", ");
        }
        if (getF20548j()) {
            sb2.append("only-if-cached, ");
        }
        if (getF20549k()) {
            sb2.append("no-transform, ");
        }
        if (getF20550l()) {
            sb2.append("immutable, ");
        }
        if (sb2.length() == 0) {
            return "";
        }
        sb2.delete(sb2.length() - 2, sb2.length());
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        this.f20551m = sb3;
        return sb3;
    }
}
