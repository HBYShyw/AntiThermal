package fe;

import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import ie.Platform;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.m0;
import me.g;
import sd.StringsJVM;
import zd.Challenge;
import zd.Cookie;
import zd.CookieJar;
import zd.Headers;
import zd.HttpUrl;
import zd.b0;

/* compiled from: HttpHeaders.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001\u001a\u001a\u0010\n\u001a\u00020\t*\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0002\u001a\f\u0010\f\u001a\u00020\u000b*\u00020\u0006H\u0002\u001a\u0014\u0010\u000f\u001a\u00020\u000b*\u00020\u00062\u0006\u0010\u000e\u001a\u00020\rH\u0002\u001a\u000e\u0010\u0010\u001a\u0004\u0018\u00010\u0001*\u00020\u0006H\u0002\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u0001*\u00020\u0006H\u0002\u001a\u001a\u0010\u0016\u001a\u00020\t*\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0000\u001a\n\u0010\u0018\u001a\u00020\u000b*\u00020\u0017¨\u0006\u0019"}, d2 = {"Lzd/t;", "", "headerName", "", "Lzd/h;", "a", "Lme/d;", "", "result", "Lma/f0;", "c", "", "g", "", "prefix", "h", "d", "e", "Lzd/n;", "Lzd/u;", "url", "headers", "f", "Lzd/b0;", "b", "okhttp"}, k = 2, mv = {1, 6, 0})
/* renamed from: fe.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class HttpHeaders {

    /* renamed from: a, reason: collision with root package name */
    private static final me.g f11458a;

    /* renamed from: b, reason: collision with root package name */
    private static final me.g f11459b;

    static {
        g.a aVar = me.g.f15493h;
        f11458a = aVar.c("\"\\");
        f11459b = aVar.c("\t ,=");
    }

    public static final List<Challenge> a(Headers headers, String str) {
        boolean r10;
        za.k.e(headers, "<this>");
        za.k.e(str, "headerName");
        ArrayList arrayList = new ArrayList();
        int size = headers.size();
        int i10 = 0;
        while (i10 < size) {
            int i11 = i10 + 1;
            r10 = StringsJVM.r(str, headers.e(i10), true);
            if (r10) {
                try {
                    c(new me.d().E(headers.g(i10)), arrayList);
                } catch (EOFException e10) {
                    Platform.f12870a.g().j("Unable to parse challenge", 5, e10);
                }
            }
            i10 = i11;
        }
        return arrayList;
    }

    public static final boolean b(b0 b0Var) {
        boolean r10;
        za.k.e(b0Var, "<this>");
        if (za.k.a(b0Var.getF20505e().getF20797b(), "HEAD")) {
            return false;
        }
        int code = b0Var.getCode();
        if (((code >= 100 && code < 200) || code == 204 || code == 304) && ae.d.u(b0Var) == -1) {
            r10 = StringsJVM.r("chunked", b0.S(b0Var, "Transfer-Encoding", null, 2, null), true);
            if (!r10) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0079, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0079, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final void c(me.d dVar, List<Challenge> list) {
        String e10;
        Map i10;
        int J;
        String v7;
        while (true) {
            String str = null;
            while (true) {
                if (str == null) {
                    g(dVar);
                    str = e(dVar);
                    if (str == null) {
                        return;
                    }
                }
                boolean g6 = g(dVar);
                e10 = e(dVar);
                if (e10 == null) {
                    if (dVar.s()) {
                        i10 = m0.i();
                        list.add(new Challenge(str, i10));
                        return;
                    }
                    return;
                }
                J = ae.d.J(dVar, (byte) 61);
                boolean g10 = g(dVar);
                if (g6 || (!g10 && !dVar.s())) {
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    int J2 = J + ae.d.J(dVar, (byte) 61);
                    while (true) {
                        if (e10 == null) {
                            e10 = e(dVar);
                            if (g(dVar)) {
                                break;
                            } else {
                                J2 = ae.d.J(dVar, (byte) 61);
                            }
                        }
                        if (J2 == 0) {
                            break;
                        }
                        if (J2 > 1 || g(dVar)) {
                            return;
                        }
                        String d10 = h(dVar, (byte) 34) ? d(dVar) : e(dVar);
                        if (d10 == null || ((String) linkedHashMap.put(e10, d10)) != null) {
                            return;
                        }
                        if (!g(dVar) && !dVar.s()) {
                            return;
                        } else {
                            e10 = null;
                        }
                    }
                    list.add(new Challenge(str, linkedHashMap));
                    str = e10;
                }
            }
            v7 = StringsJVM.v(InnerUtils.EQUAL, J);
            Map singletonMap = Collections.singletonMap(null, za.k.l(e10, v7));
            za.k.d(singletonMap, "singletonMap<String, Str…ek + \"=\".repeat(eqCount))");
            list.add(new Challenge(str, singletonMap));
        }
    }

    private static final String d(me.d dVar) {
        if (dVar.M() == 34) {
            me.d dVar2 = new me.d();
            while (true) {
                long g02 = dVar.g0(f11458a);
                if (g02 == -1) {
                    return null;
                }
                if (dVar.L(g02) == 34) {
                    dVar2.write(dVar, g02);
                    dVar.M();
                    return dVar2.o0();
                }
                if (dVar.v0() == g02 + 1) {
                    return null;
                }
                dVar2.write(dVar, g02);
                dVar.M();
                dVar2.write(dVar, 1L);
            }
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private static final String e(me.d dVar) {
        long g02 = dVar.g0(f11459b);
        if (g02 == -1) {
            g02 = dVar.v0();
        }
        if (g02 != 0) {
            return dVar.t0(g02);
        }
        return null;
    }

    public static final void f(CookieJar cookieJar, HttpUrl httpUrl, Headers headers) {
        za.k.e(cookieJar, "<this>");
        za.k.e(httpUrl, "url");
        za.k.e(headers, "headers");
        if (cookieJar == CookieJar.f20686b) {
            return;
        }
        List<Cookie> e10 = Cookie.f20671j.e(httpUrl, headers);
        if (e10.isEmpty()) {
            return;
        }
        cookieJar.a(httpUrl, e10);
    }

    private static final boolean g(me.d dVar) {
        boolean z10 = false;
        while (!dVar.s()) {
            byte L = dVar.L(0L);
            if (L != 44) {
                if (!(L == 32 || L == 9)) {
                    break;
                }
                dVar.M();
            } else {
                dVar.M();
                z10 = true;
            }
        }
        return z10;
    }

    private static final boolean h(me.d dVar, byte b10) {
        return !dVar.s() && dVar.L(0L) == b10;
    }
}
