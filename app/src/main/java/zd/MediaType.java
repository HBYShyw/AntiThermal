package zd;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import sd.StringsJVM;
import ta.progressionUtil;
import za.DefaultConstructorMarker;

/* compiled from: MediaType.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0015B/\b\u0002\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\u0006\u0010\u0010\u001a\u00020\u0005\u0012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011¢\u0006\u0004\b\u0013\u0010\u0014J\u0016\u0010\u0004\u001a\u0004\u0018\u00010\u00022\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0007J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0005J\b\u0010\b\u001a\u00020\u0005H\u0016J\u0013\u0010\u000b\u001a\u00020\n2\b\u0010\t\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\r\u001a\u00020\fH\u0016¨\u0006\u0016"}, d2 = {"Lzd/w;", "", "Ljava/nio/charset/Charset;", "defaultValue", "c", "", "name", "e", "toString", "other", "", "equals", "", "hashCode", "mediaType", "type", "subtype", "", "parameterNamesAndValues", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.w, reason: use source file name */
/* loaded from: classes2.dex */
public final class MediaType {

    /* renamed from: e, reason: collision with root package name */
    public static final a f20732e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final Pattern f20733f = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");

    /* renamed from: g, reason: collision with root package name */
    private static final Pattern f20734g = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");

    /* renamed from: a, reason: collision with root package name */
    private final String f20735a;

    /* renamed from: b, reason: collision with root package name */
    private final String f20736b;

    /* renamed from: c, reason: collision with root package name */
    private final String f20737c;

    /* renamed from: d, reason: collision with root package name */
    private final String[] f20738d;

    /* compiled from: MediaType.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0013\u0010\u0004\u001a\u00020\u0003*\u00020\u0002H\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0003*\u00020\u0002H\u0007¢\u0006\u0004\b\u0006\u0010\u0005R\u001c\u0010\t\u001a\n \b*\u0004\u0018\u00010\u00070\u00078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\fR\u001c\u0010\u000e\u001a\n \b*\u0004\u0018\u00010\u00070\u00078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000e\u0010\n¨\u0006\u0011"}, d2 = {"Lzd/w$a;", "", "", "Lzd/w;", "a", "(Ljava/lang/String;)Lzd/w;", "b", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "PARAMETER", "Ljava/util/regex/Pattern;", "QUOTED", "Ljava/lang/String;", "TOKEN", "TYPE_SUBTYPE", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.w$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final MediaType a(String str) {
            boolean D;
            boolean q10;
            za.k.e(str, "<this>");
            Matcher matcher = MediaType.f20733f.matcher(str);
            if (matcher.lookingAt()) {
                String group = matcher.group(1);
                za.k.d(group, "typeSubtype.group(1)");
                Locale locale = Locale.US;
                za.k.d(locale, "US");
                String lowerCase = group.toLowerCase(locale);
                za.k.d(lowerCase, "this as java.lang.String).toLowerCase(locale)");
                String group2 = matcher.group(2);
                za.k.d(group2, "typeSubtype.group(2)");
                za.k.d(locale, "US");
                String lowerCase2 = group2.toLowerCase(locale);
                za.k.d(lowerCase2, "this as java.lang.String).toLowerCase(locale)");
                ArrayList arrayList = new ArrayList();
                Matcher matcher2 = MediaType.f20734g.matcher(str);
                int end = matcher.end();
                while (end < str.length()) {
                    matcher2.region(end, str.length());
                    if (matcher2.lookingAt()) {
                        String group3 = matcher2.group(1);
                        if (group3 == null) {
                            end = matcher2.end();
                        } else {
                            String group4 = matcher2.group(2);
                            if (group4 == null) {
                                group4 = matcher2.group(3);
                            } else {
                                D = StringsJVM.D(group4, "'", false, 2, null);
                                if (D) {
                                    q10 = StringsJVM.q(group4, "'", false, 2, null);
                                    if (q10 && group4.length() > 2) {
                                        group4 = group4.substring(1, group4.length() - 1);
                                        za.k.d(group4, "this as java.lang.String…ing(startIndex, endIndex)");
                                    }
                                }
                            }
                            arrayList.add(group3);
                            arrayList.add(group4);
                            end = matcher2.end();
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Parameter is not formatted correctly: \"");
                        String substring = str.substring(end);
                        za.k.d(substring, "this as java.lang.String).substring(startIndex)");
                        sb2.append(substring);
                        sb2.append("\" for: \"");
                        sb2.append(str);
                        sb2.append('\"');
                        throw new IllegalArgumentException(sb2.toString().toString());
                    }
                }
                Object[] array = arrayList.toArray(new String[0]);
                Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                return new MediaType(str, lowerCase, lowerCase2, (String[]) array, null);
            }
            throw new IllegalArgumentException(("No subtype found for: \"" + str + '\"').toString());
        }

        public final MediaType b(String str) {
            za.k.e(str, "<this>");
            try {
                return a(str);
            } catch (IllegalArgumentException unused) {
                return null;
            }
        }
    }

    private MediaType(String str, String str2, String str3, String[] strArr) {
        this.f20735a = str;
        this.f20736b = str2;
        this.f20737c = str3;
        this.f20738d = strArr;
    }

    public /* synthetic */ MediaType(String str, String str2, String str3, String[] strArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, strArr);
    }

    public static /* synthetic */ Charset d(MediaType mediaType, Charset charset, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            charset = null;
        }
        return mediaType.c(charset);
    }

    public static final MediaType f(String str) {
        return f20732e.b(str);
    }

    public final Charset c(Charset defaultValue) {
        String e10 = e("charset");
        if (e10 == null) {
            return defaultValue;
        }
        try {
            return Charset.forName(e10);
        } catch (IllegalArgumentException unused) {
            return defaultValue;
        }
    }

    public final String e(String name) {
        boolean r10;
        za.k.e(name, "name");
        int i10 = 0;
        int b10 = progressionUtil.b(0, this.f20738d.length - 1, 2);
        if (b10 < 0) {
            return null;
        }
        while (true) {
            int i11 = i10 + 2;
            r10 = StringsJVM.r(this.f20738d[i10], name, true);
            if (r10) {
                return this.f20738d[i10 + 1];
            }
            if (i10 == b10) {
                return null;
            }
            i10 = i11;
        }
    }

    public boolean equals(Object other) {
        return (other instanceof MediaType) && za.k.a(((MediaType) other).f20735a, this.f20735a);
    }

    public int hashCode() {
        return this.f20735a.hashCode();
    }

    /* renamed from: toString, reason: from getter */
    public String getF20735a() {
        return this.f20735a;
    }
}
