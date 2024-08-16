package zd;

import ae.hostnames;
import com.coui.appcompat.calendar.COUIDateMonthView;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fe.dates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import sd.StringsJVM;
import za.DefaultConstructorMarker;

/* compiled from: Cookie.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u0016\u0018\u00002\u00020\u0001:\u0001'BQ\b\u0002\u0012\u0006\u0010\f\u001a\u00020\u0007\u0012\u0006\u0010\u0010\u001a\u00020\u0007\u0012\u0006\u0010\u0013\u001a\u00020\u0012\u0012\u0006\u0010\u0017\u001a\u00020\u0007\u0012\u0006\u0010\u0019\u001a\u00020\u0007\u0012\u0006\u0010\u001b\u001a\u00020\u0003\u0012\u0006\u0010\u001f\u001a\u00020\u0003\u0012\u0006\u0010!\u001a\u00020\u0003\u0012\u0006\u0010#\u001a\u00020\u0003¢\u0006\u0004\b%\u0010&J\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0017J\b\u0010\b\u001a\u00020\u0007H\u0016J\u0017\u0010\n\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0003H\u0000¢\u0006\u0004\b\n\u0010\u000bR\u0017\u0010\f\u001a\u00020\u00078\u0007¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\u00020\u00078\u0007¢\u0006\f\n\u0004\b\u0010\u0010\r\u001a\u0004\b\u0011\u0010\u000fR\u0017\u0010\u0013\u001a\u00020\u00128\u0007¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0017\u001a\u00020\u00078\u0007¢\u0006\f\n\u0004\b\u0017\u0010\r\u001a\u0004\b\u0018\u0010\u000fR\u0017\u0010\u0019\u001a\u00020\u00078\u0007¢\u0006\f\n\u0004\b\u0019\u0010\r\u001a\u0004\b\u001a\u0010\u000fR\u0017\u0010\u001b\u001a\u00020\u00038\u0007¢\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u001d\u0010\u001eR\u0017\u0010\u001f\u001a\u00020\u00038\u0007¢\u0006\f\n\u0004\b\u001f\u0010\u001c\u001a\u0004\b \u0010\u001eR\u0017\u0010!\u001a\u00020\u00038\u0007¢\u0006\f\n\u0004\b!\u0010\u001c\u001a\u0004\b\"\u0010\u001eR\u0017\u0010#\u001a\u00020\u00038\u0007¢\u0006\f\n\u0004\b#\u0010\u001c\u001a\u0004\b$\u0010\u001e¨\u0006("}, d2 = {"Lzd/m;", "", "other", "", "equals", "", "hashCode", "", "toString", "forObsoleteRfc2965", "m", "(Z)Ljava/lang/String;", "name", "Ljava/lang/String;", "i", "()Ljava/lang/String;", ThermalBaseConfig.Item.ATTR_VALUE, "n", "", "expiresAt", "J", "f", "()J", "domain", "e", Constants.MessagerConstants.PATH_KEY, "j", "secure", "Z", "l", "()Z", "httpOnly", "h", "persistent", "k", "hostOnly", "g", "<init>", "(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class Cookie {

    /* renamed from: j, reason: collision with root package name */
    public static final a f20671j = new a(null);

    /* renamed from: k, reason: collision with root package name */
    private static final Pattern f20672k = Pattern.compile("(\\d{2,4})[^\\d]*");

    /* renamed from: l, reason: collision with root package name */
    private static final Pattern f20673l = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");

    /* renamed from: m, reason: collision with root package name */
    private static final Pattern f20674m = Pattern.compile("(\\d{1,2})[^\\d]*");

    /* renamed from: n, reason: collision with root package name */
    private static final Pattern f20675n = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");

    /* renamed from: a, reason: collision with root package name */
    private final String f20676a;

    /* renamed from: b, reason: collision with root package name */
    private final String f20677b;

    /* renamed from: c, reason: collision with root package name */
    private final long f20678c;

    /* renamed from: d, reason: collision with root package name */
    private final String f20679d;

    /* renamed from: e, reason: collision with root package name */
    private final String f20680e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f20681f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f20682g;

    /* renamed from: h, reason: collision with root package name */
    private final boolean f20683h;

    /* renamed from: i, reason: collision with root package name */
    private final boolean f20684i;

    /* compiled from: Cookie.kt */
    @Metadata(bv = {}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b%\u0010&J\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0002J \u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\bH\u0002J(\u0010\u000f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u0005H\u0002J\u0010\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u0002H\u0002J\u0010\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0002J\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0002H\u0007J)\u0010\u0018\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0018\u0010\u0019J\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00150\u001c2\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u001aH\u0007R\u001c\u0010 \u001a\n \u001f*\u0004\u0018\u00010\u001e0\u001e8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b \u0010!R\u001c\u0010\"\u001a\n \u001f*\u0004\u0018\u00010\u001e0\u001e8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\"\u0010!R\u001c\u0010#\u001a\n \u001f*\u0004\u0018\u00010\u001e0\u001e8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b#\u0010!R\u001c\u0010$\u001a\n \u001f*\u0004\u0018\u00010\u001e0\u001e8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b$\u0010!¨\u0006'"}, d2 = {"Lzd/m$a;", "", "", "urlHost", "domain", "", "b", "s", "", "pos", "limit", "", "g", "input", "invert", "a", "h", "f", "Lzd/u;", "url", "setCookie", "Lzd/m;", "c", "currentTimeMillis", "d", "(JLzd/u;Ljava/lang/String;)Lzd/m;", "Lzd/t;", "headers", "", "e", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "DAY_OF_MONTH_PATTERN", "Ljava/util/regex/Pattern;", "MONTH_PATTERN", "TIME_PATTERN", "YEAR_PATTERN", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.m$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x003f, code lost:
        
            if (r0 != ':') goto L33;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private final int a(String input, int pos, int limit, boolean invert) {
            while (pos < limit) {
                int i10 = pos + 1;
                char charAt = input.charAt(pos);
                boolean z10 = false;
                if ((charAt >= ' ' || charAt == '\t') && charAt < 127) {
                    if (!(charAt <= '9' && '0' <= charAt)) {
                        if (!(charAt <= 'z' && 'a' <= charAt)) {
                            if (!(charAt <= 'Z' && 'A' <= charAt)) {
                            }
                        }
                    }
                }
                z10 = true;
                if (z10 == (!invert)) {
                    return pos;
                }
                pos = i10;
            }
            return limit;
        }

        private final boolean b(String urlHost, String domain) {
            boolean q10;
            if (za.k.a(urlHost, domain)) {
                return true;
            }
            q10 = StringsJVM.q(urlHost, domain, false, 2, null);
            return q10 && urlHost.charAt((urlHost.length() - domain.length()) - 1) == '.' && !ae.d.i(urlHost);
        }

        private final String f(String s7) {
            boolean q10;
            String j02;
            q10 = StringsJVM.q(s7, ".", false, 2, null);
            if (!q10) {
                j02 = sd.v.j0(s7, ".");
                String e10 = hostnames.e(j02);
                if (e10 != null) {
                    return e10;
                }
                throw new IllegalArgumentException();
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long g(String s7, int pos, int limit) {
            int V;
            int a10 = a(s7, pos, limit, false);
            Matcher matcher = Cookie.f20675n.matcher(s7);
            int i10 = -1;
            int i11 = -1;
            int i12 = -1;
            int i13 = -1;
            int i14 = -1;
            int i15 = -1;
            while (a10 < limit) {
                int a11 = a(s7, a10 + 1, limit, true);
                matcher.region(a10, a11);
                if (i11 == -1 && matcher.usePattern(Cookie.f20675n).matches()) {
                    String group = matcher.group(1);
                    za.k.d(group, "matcher.group(1)");
                    i11 = Integer.parseInt(group);
                    String group2 = matcher.group(2);
                    za.k.d(group2, "matcher.group(2)");
                    i14 = Integer.parseInt(group2);
                    String group3 = matcher.group(3);
                    za.k.d(group3, "matcher.group(3)");
                    i15 = Integer.parseInt(group3);
                } else if (i12 == -1 && matcher.usePattern(Cookie.f20674m).matches()) {
                    String group4 = matcher.group(1);
                    za.k.d(group4, "matcher.group(1)");
                    i12 = Integer.parseInt(group4);
                } else if (i13 == -1 && matcher.usePattern(Cookie.f20673l).matches()) {
                    String group5 = matcher.group(1);
                    za.k.d(group5, "matcher.group(1)");
                    Locale locale = Locale.US;
                    za.k.d(locale, "US");
                    String lowerCase = group5.toLowerCase(locale);
                    za.k.d(lowerCase, "this as java.lang.String).toLowerCase(locale)");
                    String pattern = Cookie.f20673l.pattern();
                    za.k.d(pattern, "MONTH_PATTERN.pattern()");
                    V = sd.v.V(pattern, lowerCase, 0, false, 6, null);
                    i13 = V / 4;
                } else if (i10 == -1 && matcher.usePattern(Cookie.f20672k).matches()) {
                    String group6 = matcher.group(1);
                    za.k.d(group6, "matcher.group(1)");
                    i10 = Integer.parseInt(group6);
                }
                a10 = a(s7, a11 + 1, limit, false);
            }
            if (70 <= i10 && i10 < 100) {
                i10 += COUIDateMonthView.MIN_YEAR;
            }
            if (i10 >= 0 && i10 < 70) {
                i10 += 2000;
            }
            if (!(i10 >= 1601)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!(i13 != -1)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!(1 <= i12 && i12 < 32)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!(i11 >= 0 && i11 < 24)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!(i14 >= 0 && i14 < 60)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (i15 >= 0 && i15 < 60) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(ae.d.f242f);
                gregorianCalendar.setLenient(false);
                gregorianCalendar.set(1, i10);
                gregorianCalendar.set(2, i13 - 1);
                gregorianCalendar.set(5, i12);
                gregorianCalendar.set(11, i11);
                gregorianCalendar.set(12, i14);
                gregorianCalendar.set(13, i15);
                gregorianCalendar.set(14, 0);
                return gregorianCalendar.getTimeInMillis();
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long h(String s7) {
            boolean D;
            try {
                long parseLong = Long.parseLong(s7);
                if (parseLong <= 0) {
                    return Long.MIN_VALUE;
                }
                return parseLong;
            } catch (NumberFormatException e10) {
                if (new sd.j("-?\\d+").b(s7)) {
                    D = StringsJVM.D(s7, "-", false, 2, null);
                    return D ? Long.MIN_VALUE : Long.MAX_VALUE;
                }
                throw e10;
            }
        }

        public final Cookie c(HttpUrl url, String setCookie) {
            za.k.e(url, "url");
            za.k.e(setCookie, "setCookie");
            return d(System.currentTimeMillis(), url, setCookie);
        }

        /* JADX WARN: Code restructure failed: missing block: B:85:0x0102, code lost:
        
            if (r1 > 253402300799999L) goto L59;
         */
        /* JADX WARN: Removed duplicated region for block: B:56:0x0114  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x013e  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x015a  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x0117  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Cookie d(long currentTimeMillis, HttpUrl url, String setCookie) {
            long j10;
            String f20716d;
            Cookie cookie;
            String str;
            String str2;
            int Z;
            String str3;
            boolean D;
            boolean r10;
            boolean r11;
            boolean r12;
            boolean r13;
            boolean r14;
            boolean r15;
            za.k.e(url, "url");
            za.k.e(setCookie, "setCookie");
            int q10 = ae.d.q(setCookie, ';', 0, 0, 6, null);
            int q11 = ae.d.q(setCookie, '=', 0, q10, 2, null);
            if (q11 == q10) {
                return null;
            }
            String W = ae.d.W(setCookie, 0, q11, 1, null);
            if ((W.length() == 0) || ae.d.x(W) != -1) {
                return null;
            }
            String V = ae.d.V(setCookie, q11 + 1, q10);
            if (ae.d.x(V) != -1) {
                return null;
            }
            int i10 = q10 + 1;
            int length = setCookie.length();
            String str4 = null;
            String str5 = null;
            boolean z10 = false;
            boolean z11 = false;
            boolean z12 = false;
            boolean z13 = true;
            long j11 = -1;
            long j12 = 253402300799999L;
            while (i10 < length) {
                int o10 = ae.d.o(setCookie, ';', i10, length);
                int o11 = ae.d.o(setCookie, '=', i10, o10);
                String V2 = ae.d.V(setCookie, i10, o11);
                String V3 = o11 < o10 ? ae.d.V(setCookie, o11 + 1, o10) : "";
                r10 = StringsJVM.r(V2, "expires", true);
                if (r10) {
                    try {
                        j12 = g(V3, 0, V3.length());
                    } catch (NumberFormatException | IllegalArgumentException unused) {
                    }
                } else {
                    r11 = StringsJVM.r(V2, "max-age", true);
                    if (r11) {
                        j11 = h(V3);
                    } else {
                        r12 = StringsJVM.r(V2, "domain", true);
                        if (r12) {
                            str4 = f(V3);
                            z13 = false;
                        } else {
                            r13 = StringsJVM.r(V2, Constants.MessagerConstants.PATH_KEY, true);
                            if (r13) {
                                str5 = V3;
                            } else {
                                r14 = StringsJVM.r(V2, "secure", true);
                                if (r14) {
                                    z10 = true;
                                } else {
                                    r15 = StringsJVM.r(V2, "httponly", true);
                                    if (r15) {
                                        z11 = true;
                                    }
                                }
                            }
                        }
                        i10 = o10 + 1;
                    }
                }
                z12 = true;
                i10 = o10 + 1;
            }
            long j13 = Long.MIN_VALUE;
            if (j11 != Long.MIN_VALUE) {
                if (j11 != -1) {
                    j13 = currentTimeMillis + (j11 <= 9223372036854775L ? j11 * 1000 : Long.MAX_VALUE);
                    long j14 = j13 >= currentTimeMillis ? 253402300799999L : 253402300799999L;
                    j10 = j14;
                } else {
                    j10 = j12;
                }
                f20716d = url.getF20716d();
                if (str4 != null) {
                    str = f20716d;
                    cookie = null;
                } else {
                    if (!b(f20716d, str4)) {
                        return null;
                    }
                    cookie = null;
                    str = str4;
                }
                if (f20716d.length() == str.length() && PublicSuffixDatabase.INSTANCE.c().c(str) == null) {
                    return cookie;
                }
                String str6 = "/";
                str2 = str5;
                if (str2 != null) {
                    D = StringsJVM.D(str2, "/", false, 2, cookie);
                    if (D) {
                        str3 = str2;
                        return new Cookie(W, V, j10, str, str3, z10, z11, z12, z13, null);
                    }
                }
                String d10 = url.d();
                Z = sd.v.Z(d10, '/', 0, false, 6, null);
                if (Z != 0) {
                    str6 = d10.substring(0, Z);
                    za.k.d(str6, "this as java.lang.String…ing(startIndex, endIndex)");
                }
                str3 = str6;
                return new Cookie(W, V, j10, str, str3, z10, z11, z12, z13, null);
            }
            j10 = j13;
            f20716d = url.getF20716d();
            if (str4 != null) {
            }
            if (f20716d.length() == str.length()) {
            }
            String str62 = "/";
            str2 = str5;
            if (str2 != null) {
            }
            String d102 = url.d();
            Z = sd.v.Z(d102, '/', 0, false, 6, null);
            if (Z != 0) {
            }
            str3 = str62;
            return new Cookie(W, V, j10, str, str3, z10, z11, z12, z13, null);
        }

        public final List<Cookie> e(HttpUrl url, Headers headers) {
            List<Cookie> j10;
            za.k.e(url, "url");
            za.k.e(headers, "headers");
            List<String> h10 = headers.h("Set-Cookie");
            int size = h10.size();
            ArrayList arrayList = null;
            int i10 = 0;
            while (i10 < size) {
                int i11 = i10 + 1;
                Cookie c10 = c(url, h10.get(i10));
                if (c10 != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(c10);
                }
                i10 = i11;
            }
            if (arrayList != null) {
                List<Cookie> unmodifiableList = Collections.unmodifiableList(arrayList);
                za.k.d(unmodifiableList, "{\n        Collections.un…ableList(cookies)\n      }");
                return unmodifiableList;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    private Cookie(String str, String str2, long j10, String str3, String str4, boolean z10, boolean z11, boolean z12, boolean z13) {
        this.f20676a = str;
        this.f20677b = str2;
        this.f20678c = j10;
        this.f20679d = str3;
        this.f20680e = str4;
        this.f20681f = z10;
        this.f20682g = z11;
        this.f20683h = z12;
        this.f20684i = z13;
    }

    public /* synthetic */ Cookie(String str, String str2, long j10, String str3, String str4, boolean z10, boolean z11, boolean z12, boolean z13, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, j10, str3, str4, z10, z11, z12, z13);
    }

    /* renamed from: e, reason: from getter */
    public final String getF20679d() {
        return this.f20679d;
    }

    public boolean equals(Object other) {
        if (other instanceof Cookie) {
            Cookie cookie = (Cookie) other;
            if (za.k.a(cookie.f20676a, this.f20676a) && za.k.a(cookie.f20677b, this.f20677b) && cookie.f20678c == this.f20678c && za.k.a(cookie.f20679d, this.f20679d) && za.k.a(cookie.f20680e, this.f20680e) && cookie.f20681f == this.f20681f && cookie.f20682g == this.f20682g && cookie.f20683h == this.f20683h && cookie.f20684i == this.f20684i) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: f, reason: from getter */
    public final long getF20678c() {
        return this.f20678c;
    }

    /* renamed from: g, reason: from getter */
    public final boolean getF20684i() {
        return this.f20684i;
    }

    /* renamed from: h, reason: from getter */
    public final boolean getF20682g() {
        return this.f20682g;
    }

    @IgnoreJRERequirement
    public int hashCode() {
        return ((((((((((((((((527 + this.f20676a.hashCode()) * 31) + this.f20677b.hashCode()) * 31) + Long.hashCode(this.f20678c)) * 31) + this.f20679d.hashCode()) * 31) + this.f20680e.hashCode()) * 31) + Boolean.hashCode(this.f20681f)) * 31) + Boolean.hashCode(this.f20682g)) * 31) + Boolean.hashCode(this.f20683h)) * 31) + Boolean.hashCode(this.f20684i);
    }

    /* renamed from: i, reason: from getter */
    public final String getF20676a() {
        return this.f20676a;
    }

    /* renamed from: j, reason: from getter */
    public final String getF20680e() {
        return this.f20680e;
    }

    /* renamed from: k, reason: from getter */
    public final boolean getF20683h() {
        return this.f20683h;
    }

    /* renamed from: l, reason: from getter */
    public final boolean getF20681f() {
        return this.f20681f;
    }

    public final String m(boolean forObsoleteRfc2965) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getF20676a());
        sb2.append('=');
        sb2.append(getF20677b());
        if (getF20683h()) {
            if (getF20678c() == Long.MIN_VALUE) {
                sb2.append("; max-age=0");
            } else {
                sb2.append("; expires=");
                sb2.append(dates.b(new Date(getF20678c())));
            }
        }
        if (!getF20684i()) {
            sb2.append("; domain=");
            if (forObsoleteRfc2965) {
                sb2.append(".");
            }
            sb2.append(getF20679d());
        }
        sb2.append("; path=");
        sb2.append(getF20680e());
        if (getF20681f()) {
            sb2.append("; secure");
        }
        if (getF20682g()) {
            sb2.append("; httponly");
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "toString()");
        return sb3;
    }

    /* renamed from: n, reason: from getter */
    public final String getF20677b() {
        return this.f20677b;
    }

    public String toString() {
        return m(false);
    }
}
