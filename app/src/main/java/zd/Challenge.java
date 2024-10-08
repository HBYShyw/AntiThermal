package zd;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;

/* compiled from: Challenge.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0004\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\t\u001a\u00020\u0007\u0012\u0014\u0010\u0014\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\u0004\u0012\u00020\u00070\u0013¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016R\u0017\u0010\t\u001a\u00020\u00078\u0007¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u00078G¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0012\u001a\u00020\u000f8G¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0017"}, d2 = {"Lzd/h;", "", "other", "", "equals", "", "hashCode", "", "toString", "scheme", "Ljava/lang/String;", "c", "()Ljava/lang/String;", "b", "realm", "Ljava/nio/charset/Charset;", "a", "()Ljava/nio/charset/Charset;", "charset", "", "authParams", "<init>", "(Ljava/lang/String;Ljava/util/Map;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class Challenge {

    /* renamed from: a, reason: collision with root package name */
    private final String f20582a;

    /* renamed from: b, reason: collision with root package name */
    private final Map<String, String> f20583b;

    public Challenge(String str, Map<String, String> map) {
        String lowerCase;
        za.k.e(str, "scheme");
        za.k.e(map, "authParams");
        this.f20582a = str;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null) {
                lowerCase = null;
            } else {
                Locale locale = Locale.US;
                za.k.d(locale, "US");
                lowerCase = key.toLowerCase(locale);
                za.k.d(lowerCase, "this as java.lang.String).toLowerCase(locale)");
            }
            linkedHashMap.put(lowerCase, value);
        }
        Map<String, String> unmodifiableMap = Collections.unmodifiableMap(linkedHashMap);
        za.k.d(unmodifiableMap, "unmodifiableMap<String?, String>(newAuthParams)");
        this.f20583b = unmodifiableMap;
    }

    public final Charset a() {
        String str = this.f20583b.get("charset");
        if (str != null) {
            try {
                Charset forName = Charset.forName(str);
                za.k.d(forName, "forName(charset)");
                return forName;
            } catch (Exception unused) {
            }
        }
        Charset charset = StandardCharsets.ISO_8859_1;
        za.k.d(charset, "ISO_8859_1");
        return charset;
    }

    public final String b() {
        return this.f20583b.get("realm");
    }

    /* renamed from: c, reason: from getter */
    public final String getF20582a() {
        return this.f20582a;
    }

    public boolean equals(Object other) {
        if (other instanceof Challenge) {
            Challenge challenge = (Challenge) other;
            if (za.k.a(challenge.f20582a, this.f20582a) && za.k.a(challenge.f20583b, this.f20583b)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((899 + this.f20582a.hashCode()) * 31) + this.f20583b.hashCode();
    }

    public String toString() {
        return this.f20582a + " authParams=" + this.f20583b;
    }
}
