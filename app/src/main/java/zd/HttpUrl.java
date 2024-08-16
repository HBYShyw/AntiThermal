package zd;

import ae.hostnames;
import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import fb.PrimitiveRanges;
import fb.Progressions;
import fb._Ranges;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import sd.StringsJVM;
import sd._Strings;
import za.DefaultConstructorMarker;

/* compiled from: HttpUrl.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0010 \n\u0002\b\u0013\u0018\u00002\u00020\u0001:\u0002;1Bc\b\u0000\u0012\u0006\u0010\u0015\u001a\u00020\b\u0012\u0006\u00103\u001a\u00020\b\u0012\u0006\u00104\u001a\u00020\b\u0012\u0006\u0010\u0019\u001a\u00020\b\u0012\u0006\u0010\u001b\u001a\u00020\u0012\u0012\f\u00105\u001a\b\u0012\u0004\u0012\u00020\b0)\u0012\u0010\u00106\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\b\u0018\u00010)\u0012\b\u00107\u001a\u0004\u0018\u00010\b\u0012\u0006\u00108\u001a\u00020\b¢\u0006\u0004\b9\u0010:J\u000f\u0010\u0003\u001a\u00020\u0002H\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\u000f\u0010\u0006\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\t\u001a\u00020\bJ\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u00002\u0006\u0010\n\u001a\u00020\bJ\u0006\u0010\r\u001a\u00020\fJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\n\u001a\u00020\bJ\u0013\u0010\u0011\u001a\u00020\u00102\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0014\u001a\u00020\bH\u0016R\u0017\u0010\u0015\u001a\u00020\b8\u0007¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\u00020\b8\u0007¢\u0006\f\n\u0004\b\u0019\u0010\u0016\u001a\u0004\b\u001a\u0010\u0018R\u0017\u0010\u001b\u001a\u00020\u00128\u0007¢\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u001d\u0010\u001eR\u0017\u0010\u001f\u001a\u00020\u00108\u0006¢\u0006\f\n\u0004\b\u001f\u0010 \u001a\u0004\b!\u0010\"R\u0011\u0010$\u001a\u00020\b8G¢\u0006\u0006\u001a\u0004\b#\u0010\u0018R\u0011\u0010&\u001a\u00020\b8G¢\u0006\u0006\u001a\u0004\b%\u0010\u0018R\u0011\u0010(\u001a\u00020\b8G¢\u0006\u0006\u001a\u0004\b'\u0010\u0018R\u0017\u0010,\u001a\b\u0012\u0004\u0012\u00020\b0)8G¢\u0006\u0006\u001a\u0004\b*\u0010+R\u0013\u0010.\u001a\u0004\u0018\u00010\b8G¢\u0006\u0006\u001a\u0004\b-\u0010\u0018R\u0013\u00100\u001a\u0004\u0018\u00010\b8G¢\u0006\u0006\u001a\u0004\b/\u0010\u0018R\u0013\u00102\u001a\u0004\u0018\u00010\b8G¢\u0006\u0006\u001a\u0004\b1\u0010\u0018¨\u0006<"}, d2 = {"Lzd/u;", "", "Ljava/net/URL;", "r", "()Ljava/net/URL;", "Ljava/net/URI;", "q", "()Ljava/net/URI;", "", "n", "link", "o", "Lzd/u$a;", "j", "k", "other", "", "equals", "", "hashCode", "toString", "scheme", "Ljava/lang/String;", "p", "()Ljava/lang/String;", "host", "h", "port", "I", "l", "()I", "isHttps", "Z", "i", "()Z", "g", "encodedUsername", "c", "encodedPassword", "d", "encodedPath", "", "e", "()Ljava/util/List;", "encodedPathSegments", "f", "encodedQuery", "m", "query", "b", "encodedFragment", "username", "password", "pathSegments", "queryNamesAndValues", "fragment", "url", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.u, reason: use source file name */
/* loaded from: classes2.dex */
public final class HttpUrl {

    /* renamed from: k, reason: collision with root package name */
    public static final b f20711k = new b(null);

    /* renamed from: l, reason: collision with root package name */
    private static final char[] f20712l = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /* renamed from: a, reason: collision with root package name */
    private final String f20713a;

    /* renamed from: b, reason: collision with root package name */
    private final String f20714b;

    /* renamed from: c, reason: collision with root package name */
    private final String f20715c;

    /* renamed from: d, reason: collision with root package name */
    private final String f20716d;

    /* renamed from: e, reason: collision with root package name */
    private final int f20717e;

    /* renamed from: f, reason: collision with root package name */
    private final List<String> f20718f;

    /* renamed from: g, reason: collision with root package name */
    private final List<String> f20719g;

    /* renamed from: h, reason: collision with root package name */
    private final String f20720h;

    /* renamed from: i, reason: collision with root package name */
    private final String f20721i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f20722j;

    /* compiled from: HttpUrl.kt */
    @Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010!\n\u0002\b\u000e\u0018\u00002\u00020\u0001:\u0001!B\u0007¢\u0006\u0004\bD\u0010EJ\b\u0010\u0003\u001a\u00020\u0002H\u0002J \u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0002J0\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bH\u0002J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0010\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\b\u0010\u0011\u001a\u00020\bH\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0015\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u0004J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u0004J\u000e\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0004J\u000e\u0010\u001b\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u0002J\u0010\u0010\u001d\u001a\u00020\u00002\b\u0010\u001c\u001a\u0004\u0018\u00010\u0004J\u000f\u0010\u001e\u001a\u00020\u0000H\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0006\u0010!\u001a\u00020 J\b\u0010\"\u001a\u00020\u0004H\u0016J!\u0010$\u001a\u00020\u00002\b\u0010#\u001a\u0004\u0018\u00010 2\u0006\u0010\u0005\u001a\u00020\u0004H\u0000¢\u0006\u0004\b$\u0010%R$\u0010\u0012\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010&\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\"\u0010+\u001a\u00020\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b+\u0010&\u001a\u0004\b,\u0010(\"\u0004\b-\u0010*R\"\u0010.\u001a\u00020\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b.\u0010&\u001a\u0004\b/\u0010(\"\u0004\b0\u0010*R$\u0010\u0018\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0018\u0010&\u001a\u0004\b1\u0010(\"\u0004\b2\u0010*R\"\u0010\u001a\u001a\u00020\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u001a\u00103\u001a\u0004\b4\u00105\"\u0004\b6\u00107R \u00109\u001a\b\u0012\u0004\u0012\u00020\u0004088\u0000X\u0080\u0004¢\u0006\f\n\u0004\b9\u0010:\u001a\u0004\b;\u0010<R,\u0010=\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u0001088\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b=\u0010:\u001a\u0004\b>\u0010<\"\u0004\b?\u0010@R$\u0010A\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bA\u0010&\u001a\u0004\bB\u0010(\"\u0004\bC\u0010*¨\u0006F"}, d2 = {"Lzd/u$a;", "", "", "b", "", "input", "startPos", "limit", "Lma/f0;", "u", "pos", "", "addTrailingSlash", "alreadyEncoded", "s", "m", "n", "q", "scheme", "v", "username", "D", "password", "p", "host", "l", "port", "r", "encodedQuery", "c", "t", "()Lzd/u$a;", "Lzd/u;", "a", "toString", "base", "o", "(Lzd/u;Ljava/lang/String;)Lzd/u$a;", "Ljava/lang/String;", "k", "()Ljava/lang/String;", "C", "(Ljava/lang/String;)V", "encodedUsername", "h", "z", "encodedPassword", "e", "x", "i", "A", "I", "j", "()I", "B", "(I)V", "", "encodedPathSegments", "Ljava/util/List;", "f", "()Ljava/util/List;", "encodedQueryNamesAndValues", "g", "y", "(Ljava/util/List;)V", "encodedFragment", "d", "w", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.u$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: i, reason: collision with root package name */
        public static final C0128a f20723i = new C0128a(null);

        /* renamed from: a, reason: collision with root package name */
        private String f20724a;

        /* renamed from: d, reason: collision with root package name */
        private String f20727d;

        /* renamed from: f, reason: collision with root package name */
        private final List<String> f20729f;

        /* renamed from: g, reason: collision with root package name */
        private List<String> f20730g;

        /* renamed from: h, reason: collision with root package name */
        private String f20731h;

        /* renamed from: b, reason: collision with root package name */
        private String f20725b = "";

        /* renamed from: c, reason: collision with root package name */
        private String f20726c = "";

        /* renamed from: e, reason: collision with root package name */
        private int f20728e = -1;

        /* compiled from: HttpUrl.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ \u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J\u001c\u0010\b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J \u0010\t\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J \u0010\n\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002R\u0014\u0010\u000b\u001a\u00020\u00028\u0000X\u0080T¢\u0006\u0006\n\u0004\b\u000b\u0010\f¨\u0006\u000f"}, d2 = {"Lzd/u$a$a;", "", "", "input", "", "pos", "limit", "g", "h", "f", "e", "INVALID_HOST", "Ljava/lang/String;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: zd.u$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0128a {
            private C0128a() {
            }

            public /* synthetic */ C0128a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int e(String input, int pos, int limit) {
                try {
                    int parseInt = Integer.parseInt(b.b(HttpUrl.f20711k, input, pos, limit, "", false, false, false, false, null, 248, null));
                    boolean z10 = false;
                    if (1 <= parseInt && parseInt < 65536) {
                        z10 = true;
                    }
                    if (z10) {
                        return parseInt;
                    }
                    return -1;
                } catch (NumberFormatException unused) {
                    return -1;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int f(String input, int pos, int limit) {
                while (pos < limit) {
                    char charAt = input.charAt(pos);
                    if (charAt != '[') {
                        if (charAt == ':') {
                            return pos;
                        }
                        pos++;
                    }
                    do {
                        pos++;
                        if (pos < limit) {
                        }
                        pos++;
                    } while (input.charAt(pos) != ']');
                    pos++;
                }
                return limit;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int g(String input, int pos, int limit) {
                if (limit - pos < 2) {
                    return -1;
                }
                char charAt = input.charAt(pos);
                if ((za.k.f(charAt, 97) < 0 || za.k.f(charAt, 122) > 0) && (za.k.f(charAt, 65) < 0 || za.k.f(charAt, 90) > 0)) {
                    return -1;
                }
                int i10 = pos + 1;
                while (i10 < limit) {
                    int i11 = i10 + 1;
                    char charAt2 = input.charAt(i10);
                    if (!(((((('a' <= charAt2 && charAt2 < '{') || ('A' <= charAt2 && charAt2 < '[')) || ('0' <= charAt2 && charAt2 < ':')) || charAt2 == '+') || charAt2 == '-') || charAt2 == '.')) {
                        if (charAt2 == ':') {
                            return i10;
                        }
                        return -1;
                    }
                    i10 = i11;
                }
                return -1;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int h(String str, int i10, int i11) {
                int i12 = 0;
                while (i10 < i11) {
                    int i13 = i10 + 1;
                    char charAt = str.charAt(i10);
                    if (charAt != '\\' && charAt != '/') {
                        break;
                    }
                    i12++;
                    i10 = i13;
                }
                return i12;
            }
        }

        public a() {
            ArrayList arrayList = new ArrayList();
            this.f20729f = arrayList;
            arrayList.add("");
        }

        private final int b() {
            int i10 = this.f20728e;
            if (i10 != -1) {
                return i10;
            }
            b bVar = HttpUrl.f20711k;
            String str = this.f20724a;
            za.k.b(str);
            return bVar.c(str);
        }

        private final boolean m(String input) {
            boolean r10;
            if (za.k.a(input, ".")) {
                return true;
            }
            r10 = StringsJVM.r(input, "%2e", true);
            return r10;
        }

        private final boolean n(String input) {
            boolean r10;
            boolean r11;
            boolean r12;
            if (za.k.a(input, "..")) {
                return true;
            }
            r10 = StringsJVM.r(input, "%2e.", true);
            if (r10) {
                return true;
            }
            r11 = StringsJVM.r(input, ".%2e", true);
            if (r11) {
                return true;
            }
            r12 = StringsJVM.r(input, "%2e%2e", true);
            return r12;
        }

        private final void q() {
            List<String> list = this.f20729f;
            if ((list.remove(list.size() - 1).length() == 0) && (!this.f20729f.isEmpty())) {
                List<String> list2 = this.f20729f;
                list2.set(list2.size() - 1, "");
            } else {
                this.f20729f.add("");
            }
        }

        private final void s(String str, int i10, int i11, boolean z10, boolean z11) {
            String b10 = b.b(HttpUrl.f20711k, str, i10, i11, " \"<>^`{}|/\\?#", z11, false, false, false, null, 240, null);
            if (m(b10)) {
                return;
            }
            if (n(b10)) {
                q();
                return;
            }
            List<String> list = this.f20729f;
            if (list.get(list.size() - 1).length() == 0) {
                List<String> list2 = this.f20729f;
                list2.set(list2.size() - 1, b10);
            } else {
                this.f20729f.add(b10);
            }
            if (z10) {
                this.f20729f.add("");
            }
        }

        private final void u(String str, int i10, int i11) {
            if (i10 == i11) {
                return;
            }
            char charAt = str.charAt(i10);
            if (charAt != '/' && charAt != '\\') {
                List<String> list = this.f20729f;
                list.set(list.size() - 1, "");
            } else {
                this.f20729f.clear();
                this.f20729f.add("");
                i10++;
            }
            while (true) {
                int i12 = i10;
                while (i12 < i11) {
                    i10 = ae.d.p(str, "/\\", i12, i11);
                    boolean z10 = i10 < i11;
                    s(str, i12, i10, z10, true);
                    if (z10) {
                        i12 = i10 + 1;
                    }
                }
                return;
            }
        }

        public final void A(String str) {
            this.f20727d = str;
        }

        public final void B(int i10) {
            this.f20728e = i10;
        }

        public final void C(String str) {
            this.f20724a = str;
        }

        public final a D(String username) {
            za.k.e(username, "username");
            z(b.b(HttpUrl.f20711k, username, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, null, 251, null));
            return this;
        }

        public final HttpUrl a() {
            int u7;
            int u10;
            ArrayList arrayList;
            String str = this.f20724a;
            if (str != null) {
                b bVar = HttpUrl.f20711k;
                String g6 = b.g(bVar, this.f20725b, 0, 0, false, 7, null);
                String g10 = b.g(bVar, this.f20726c, 0, 0, false, 7, null);
                String str2 = this.f20727d;
                if (str2 != null) {
                    int b10 = b();
                    List<String> list = this.f20729f;
                    u7 = kotlin.collections.s.u(list, 10);
                    ArrayList arrayList2 = new ArrayList(u7);
                    Iterator<T> it = list.iterator();
                    while (it.hasNext()) {
                        arrayList2.add(b.g(HttpUrl.f20711k, (String) it.next(), 0, 0, false, 7, null));
                    }
                    List<String> list2 = this.f20730g;
                    if (list2 == null) {
                        arrayList = null;
                    } else {
                        u10 = kotlin.collections.s.u(list2, 10);
                        ArrayList arrayList3 = new ArrayList(u10);
                        for (String str3 : list2) {
                            arrayList3.add(str3 == null ? null : b.g(HttpUrl.f20711k, str3, 0, 0, true, 3, null));
                        }
                        arrayList = arrayList3;
                    }
                    String str4 = this.f20731h;
                    return new HttpUrl(str, g6, g10, str2, b10, arrayList2, arrayList, str4 != null ? b.g(HttpUrl.f20711k, str4, 0, 0, false, 7, null) : null, toString());
                }
                throw new IllegalStateException("host == null");
            }
            throw new IllegalStateException("scheme == null");
        }

        public final a c(String encodedQuery) {
            List<String> list = null;
            if (encodedQuery != null) {
                b bVar = HttpUrl.f20711k;
                String b10 = b.b(bVar, encodedQuery, 0, 0, " \"'<>#", true, false, true, false, null, EventType.SCENE_MODE_GAME, null);
                if (b10 != null) {
                    list = bVar.i(b10);
                }
            }
            y(list);
            return this;
        }

        /* renamed from: d, reason: from getter */
        public final String getF20731h() {
            return this.f20731h;
        }

        /* renamed from: e, reason: from getter */
        public final String getF20726c() {
            return this.f20726c;
        }

        public final List<String> f() {
            return this.f20729f;
        }

        public final List<String> g() {
            return this.f20730g;
        }

        /* renamed from: h, reason: from getter */
        public final String getF20725b() {
            return this.f20725b;
        }

        /* renamed from: i, reason: from getter */
        public final String getF20727d() {
            return this.f20727d;
        }

        /* renamed from: j, reason: from getter */
        public final int getF20728e() {
            return this.f20728e;
        }

        /* renamed from: k, reason: from getter */
        public final String getF20724a() {
            return this.f20724a;
        }

        public final a l(String host) {
            za.k.e(host, "host");
            String e10 = hostnames.e(b.g(HttpUrl.f20711k, host, 0, 0, false, 7, null));
            if (e10 != null) {
                A(e10);
                return this;
            }
            throw new IllegalArgumentException(za.k.l("unexpected host: ", host));
        }

        public final a o(HttpUrl base, String input) {
            String M0;
            int p10;
            int i10;
            int i11;
            String str;
            boolean z10;
            int i12;
            String str2;
            int i13;
            boolean z11;
            boolean A;
            boolean A2;
            String str3 = input;
            za.k.e(str3, "input");
            int z12 = ae.d.z(str3, 0, 0, 3, null);
            int B = ae.d.B(str3, z12, 0, 2, null);
            C0128a c0128a = f20723i;
            int g6 = c0128a.g(str3, z12, B);
            String str4 = "this as java.lang.String…ing(startIndex, endIndex)";
            char c10 = 65535;
            boolean z13 = true;
            if (g6 != -1) {
                A = StringsJVM.A(str3, "https:", z12, true);
                if (A) {
                    this.f20724a = "https";
                    z12 += 6;
                } else {
                    A2 = StringsJVM.A(str3, "http:", z12, true);
                    if (A2) {
                        this.f20724a = "http";
                        z12 += 5;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Expected URL scheme 'http' or 'https' but was '");
                        String substring = str3.substring(0, g6);
                        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                        sb2.append(substring);
                        sb2.append('\'');
                        throw new IllegalArgumentException(sb2.toString());
                    }
                }
            } else if (base != null) {
                this.f20724a = base.getF20713a();
            } else {
                if (input.length() > 6) {
                    M0 = _Strings.M0(str3, 6);
                    str3 = za.k.l(M0, "...");
                }
                throw new IllegalArgumentException(za.k.l("Expected URL scheme 'http' or 'https' but no scheme was found for ", str3));
            }
            int h10 = c0128a.h(str3, z12, B);
            char c11 = '?';
            char c12 = '#';
            if (h10 < 2 && base != null && za.k.a(base.getF20713a(), this.f20724a)) {
                this.f20725b = base.g();
                this.f20726c = base.c();
                this.f20727d = base.getF20716d();
                this.f20728e = base.getF20717e();
                this.f20729f.clear();
                this.f20729f.addAll(base.e());
                if (z12 == B || str3.charAt(z12) == '#') {
                    c(base.f());
                }
                i10 = B;
            } else {
                int i14 = z12 + h10;
                boolean z14 = false;
                boolean z15 = false;
                while (true) {
                    p10 = ae.d.p(str3, "@/\\?#", i14, B);
                    char charAt = p10 != B ? str3.charAt(p10) : c10;
                    if (charAt == c10 || charAt == c12 || charAt == '/' || charAt == '\\' || charAt == c11) {
                        break;
                    }
                    if (charAt == '@') {
                        if (!z14) {
                            int o10 = ae.d.o(str3, COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, i14, p10);
                            b bVar = HttpUrl.f20711k;
                            z10 = z13;
                            i12 = B;
                            String str5 = str4;
                            String b10 = b.b(bVar, input, i14, o10, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                            if (z15) {
                                b10 = this.f20725b + "%40" + b10;
                            }
                            this.f20725b = b10;
                            if (o10 != p10) {
                                this.f20726c = b.b(bVar, input, o10 + 1, p10, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                                z11 = z10;
                            } else {
                                z11 = z14;
                            }
                            z14 = z11;
                            str2 = str5;
                            z15 = z10;
                            i13 = p10;
                        } else {
                            z10 = z13;
                            i12 = B;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(this.f20726c);
                            sb3.append("%40");
                            str2 = str4;
                            i13 = p10;
                            sb3.append(b.b(HttpUrl.f20711k, input, i14, p10, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null));
                            this.f20726c = sb3.toString();
                        }
                        i14 = i13 + 1;
                        str4 = str2;
                        z13 = z10;
                        B = i12;
                        c12 = '#';
                        c11 = '?';
                        c10 = 65535;
                    }
                }
                boolean z16 = z13;
                String str6 = str4;
                i10 = B;
                C0128a c0128a2 = f20723i;
                int f10 = c0128a2.f(str3, i14, p10);
                int i15 = f10 + 1;
                if (i15 < p10) {
                    i11 = i14;
                    this.f20727d = hostnames.e(b.g(HttpUrl.f20711k, input, i14, f10, false, 4, null));
                    int e10 = c0128a2.e(str3, i15, p10);
                    this.f20728e = e10;
                    if (!(e10 != -1 ? z16 : false)) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Invalid URL port: \"");
                        String substring2 = str3.substring(i15, p10);
                        za.k.d(substring2, str6);
                        sb4.append(substring2);
                        sb4.append('\"');
                        throw new IllegalArgumentException(sb4.toString().toString());
                    }
                    str = str6;
                } else {
                    i11 = i14;
                    str = str6;
                    b bVar2 = HttpUrl.f20711k;
                    this.f20727d = hostnames.e(b.g(bVar2, input, i11, f10, false, 4, null));
                    String str7 = this.f20724a;
                    za.k.b(str7);
                    this.f20728e = bVar2.c(str7);
                }
                if (!(this.f20727d != null ? z16 : false)) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Invalid URL host: \"");
                    String substring3 = str3.substring(i11, f10);
                    za.k.d(substring3, str);
                    sb5.append(substring3);
                    sb5.append('\"');
                    throw new IllegalArgumentException(sb5.toString().toString());
                }
                z12 = p10;
            }
            int i16 = i10;
            int p11 = ae.d.p(str3, "?#", z12, i16);
            u(str3, z12, p11);
            if (p11 < i16 && str3.charAt(p11) == '?') {
                int o11 = ae.d.o(str3, '#', p11, i16);
                b bVar3 = HttpUrl.f20711k;
                this.f20730g = bVar3.i(b.b(bVar3, input, p11 + 1, o11, " \"'<>#", true, false, true, false, null, EventType.SCENE_MODE_AUDIO_CALL, null));
                p11 = o11;
            }
            if (p11 < i16 && str3.charAt(p11) == '#') {
                this.f20731h = b.b(HttpUrl.f20711k, input, p11 + 1, i16, "", true, false, false, true, null, 176, null);
            }
            return this;
        }

        public final a p(String password) {
            za.k.e(password, "password");
            x(b.b(HttpUrl.f20711k, password, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, null, 251, null));
            return this;
        }

        public final a r(int port) {
            boolean z10 = false;
            if (1 <= port && port < 65536) {
                z10 = true;
            }
            if (z10) {
                B(port);
                return this;
            }
            throw new IllegalArgumentException(za.k.l("unexpected port: ", Integer.valueOf(port)).toString());
        }

        public final a t() {
            String f20727d = getF20727d();
            A(f20727d == null ? null : new sd.j("[\"<>^`{|}]").c(f20727d, ""));
            int size = f().size();
            int i10 = 0;
            for (int i11 = 0; i11 < size; i11++) {
                f().set(i11, b.b(HttpUrl.f20711k, f().get(i11), 0, 0, "[]", true, true, false, false, null, 227, null));
            }
            List<String> g6 = g();
            if (g6 != null) {
                int size2 = g6.size();
                while (i10 < size2) {
                    int i12 = i10 + 1;
                    String str = g6.get(i10);
                    g6.set(i10, str == null ? null : b.b(HttpUrl.f20711k, str, 0, 0, "\\^`{|}", true, true, true, false, null, 195, null));
                    i10 = i12;
                }
            }
            String f20731h = getF20731h();
            w(f20731h != null ? b.b(HttpUrl.f20711k, f20731h, 0, 0, " \"#<>\\^`{|}", true, true, false, true, null, 163, null) : null);
            return this;
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x003d, code lost:
        
            if ((getF20726c().length() > 0) != false) goto L17;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00b5, code lost:
        
            if (r1 != r2.c(r3)) goto L38;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String toString() {
            boolean H;
            StringBuilder sb2 = new StringBuilder();
            if (getF20724a() != null) {
                sb2.append(getF20724a());
                sb2.append("://");
            } else {
                sb2.append("//");
            }
            if (!(getF20725b().length() > 0)) {
            }
            sb2.append(getF20725b());
            if (getF20726c().length() > 0) {
                sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                sb2.append(getF20726c());
            }
            sb2.append('@');
            if (getF20727d() != null) {
                String f20727d = getF20727d();
                za.k.b(f20727d);
                H = sd.v.H(f20727d, COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, false, 2, null);
                if (H) {
                    sb2.append('[');
                    sb2.append(getF20727d());
                    sb2.append(']');
                } else {
                    sb2.append(getF20727d());
                }
            }
            if (getF20728e() != -1 || getF20724a() != null) {
                int b10 = b();
                if (getF20724a() != null) {
                    b bVar = HttpUrl.f20711k;
                    String f20724a = getF20724a();
                    za.k.b(f20724a);
                }
                sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                sb2.append(b10);
            }
            b bVar2 = HttpUrl.f20711k;
            bVar2.h(f(), sb2);
            if (g() != null) {
                sb2.append('?');
                List<String> g6 = g();
                za.k.b(g6);
                bVar2.j(g6, sb2);
            }
            if (getF20731h() != null) {
                sb2.append('#');
                sb2.append(getF20731h());
            }
            String sb3 = sb2.toString();
            za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
            return sb3;
        }

        public final a v(String scheme) {
            boolean r10;
            boolean r11;
            za.k.e(scheme, "scheme");
            r10 = StringsJVM.r(scheme, "http", true);
            if (r10) {
                C("http");
            } else {
                r11 = StringsJVM.r(scheme, "https", true);
                if (!r11) {
                    throw new IllegalArgumentException(za.k.l("unexpected scheme: ", scheme));
                }
                C("https");
            }
            return this;
        }

        public final void w(String str) {
            this.f20731h = str;
        }

        public final void x(String str) {
            za.k.e(str, "<set-?>");
            this.f20726c = str;
        }

        public final void y(List<String> list) {
            this.f20730g = list;
        }

        public final void z(String str) {
            za.k.e(str, "<set-?>");
            this.f20725b = str;
        }
    }

    /* compiled from: HttpUrl.kt */
    @Metadata(bv = {}, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0019\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b7\u00108J,\u0010\u000b\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\bH\u0002J\u001c\u0010\f\u001a\u00020\b*\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0002JV\u0010\u0014\u001a\u00020\n*\u00020\u00022\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0012H\u0002J\u0010\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0003H\u0007J%\u0010\u001b\u001a\u00020\n*\b\u0012\u0004\u0012\u00020\u00030\u00172\n\u0010\u001a\u001a\u00060\u0018j\u0002`\u0019H\u0000¢\u0006\u0004\b\u001b\u0010\u001cJ'\u0010\u001d\u001a\u00020\n*\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00172\n\u0010\u001a\u001a\u00060\u0018j\u0002`\u0019H\u0000¢\u0006\u0004\b\u001d\u0010\u001cJ\u001b\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u001e*\u00020\u0003H\u0000¢\u0006\u0004\b\u001f\u0010 J\u0013\u0010\"\u001a\u00020!*\u00020\u0003H\u0007¢\u0006\u0004\b\"\u0010#J1\u0010$\u001a\u00020\u0003*\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\bH\u0000¢\u0006\u0004\b$\u0010%Jc\u0010&\u001a\u00020\u0003*\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\b2\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0012H\u0000¢\u0006\u0004\b&\u0010'R\u0014\u0010(\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b(\u0010)R\u0014\u0010*\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b*\u0010)R\u0014\u0010+\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b+\u0010)R\u0014\u0010-\u001a\u00020,8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b-\u0010.R\u0014\u0010/\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b/\u0010)R\u0014\u00100\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b0\u0010)R\u0014\u00101\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b1\u0010)R\u0014\u00102\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b2\u0010)R\u0014\u00103\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b3\u0010)R\u0014\u00104\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b4\u0010)R\u0014\u00105\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b5\u0010)R\u0014\u00106\u001a\u00020\u00038\u0000X\u0080T¢\u0006\u0006\n\u0004\b6\u0010)¨\u00069"}, d2 = {"Lzd/u$b;", "", "Lme/d;", "", "encoded", "", "pos", "limit", "", "plusIsSpace", "Lma/f0;", "l", "e", "input", "encodeSet", "alreadyEncoded", "strict", "unicodeAllowed", "Ljava/nio/charset/Charset;", "charset", "k", "scheme", "c", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "out", "h", "(Ljava/util/List;Ljava/lang/StringBuilder;)V", "j", "", "i", "(Ljava/lang/String;)Ljava/util/List;", "Lzd/u;", "d", "(Ljava/lang/String;)Lzd/u;", "f", "(Ljava/lang/String;IIZ)Ljava/lang/String;", "a", "(Ljava/lang/String;IILjava/lang/String;ZZZZLjava/nio/charset/Charset;)Ljava/lang/String;", "FORM_ENCODE_SET", "Ljava/lang/String;", "FRAGMENT_ENCODE_SET", "FRAGMENT_ENCODE_SET_URI", "", "HEX_DIGITS", "[C", "PASSWORD_ENCODE_SET", "PATH_SEGMENT_ENCODE_SET", "PATH_SEGMENT_ENCODE_SET_URI", "QUERY_COMPONENT_ENCODE_SET", "QUERY_COMPONENT_ENCODE_SET_URI", "QUERY_COMPONENT_REENCODE_SET", "QUERY_ENCODE_SET", "USERNAME_ENCODE_SET", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.u$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ String b(b bVar, String str, int i10, int i11, String str2, boolean z10, boolean z11, boolean z12, boolean z13, Charset charset, int i12, Object obj) {
            return bVar.a(str, (i12 & 1) != 0 ? 0 : i10, (i12 & 2) != 0 ? str.length() : i11, str2, (i12 & 8) != 0 ? false : z10, (i12 & 16) != 0 ? false : z11, (i12 & 32) != 0 ? false : z12, (i12 & 64) != 0 ? false : z13, (i12 & 128) != 0 ? null : charset);
        }

        private final boolean e(String str, int i10, int i11) {
            int i12 = i10 + 2;
            return i12 < i11 && str.charAt(i10) == '%' && ae.d.G(str.charAt(i10 + 1)) != -1 && ae.d.G(str.charAt(i12)) != -1;
        }

        public static /* synthetic */ String g(b bVar, String str, int i10, int i11, boolean z10, int i12, Object obj) {
            if ((i12 & 1) != 0) {
                i10 = 0;
            }
            if ((i12 & 2) != 0) {
                i11 = str.length();
            }
            if ((i12 & 4) != 0) {
                z10 = false;
            }
            return bVar.f(str, i10, i11, z10);
        }

        /* JADX WARN: Code restructure failed: missing block: B:36:0x005f, code lost:
        
            if (e(r16, r5, r18) == false) goto L41;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private final void k(me.d dVar, String str, int i10, int i11, String str2, boolean z10, boolean z11, boolean z12, boolean z13, Charset charset) {
            boolean H;
            int i12 = i10;
            me.d dVar2 = null;
            while (i12 < i11) {
                int codePointAt = str.codePointAt(i12);
                if (!z10 || (codePointAt != 9 && codePointAt != 10 && codePointAt != 12 && codePointAt != 13)) {
                    if (codePointAt == 43 && z12) {
                        dVar.E(z10 ? "+" : "%2B");
                    } else {
                        if (codePointAt >= 32 && codePointAt != 127 && (codePointAt < 128 || z13)) {
                            H = sd.v.H(str2, (char) codePointAt, false, 2, null);
                            if (!H) {
                                if (codePointAt == 37) {
                                    if (z10) {
                                        if (z11) {
                                        }
                                    }
                                }
                                dVar.K0(codePointAt);
                                i12 += Character.charCount(codePointAt);
                            }
                        }
                        if (dVar2 == null) {
                            dVar2 = new me.d();
                        }
                        if (charset != null && !za.k.a(charset, StandardCharsets.UTF_8)) {
                            dVar2.H0(str, i12, Character.charCount(codePointAt) + i12, charset);
                        } else {
                            dVar2.K0(codePointAt);
                        }
                        while (!dVar2.s()) {
                            int M = dVar2.M() & 255;
                            dVar.t(37);
                            dVar.t(HttpUrl.f20712l[(M >> 4) & 15]);
                            dVar.t(HttpUrl.f20712l[M & 15]);
                        }
                        i12 += Character.charCount(codePointAt);
                    }
                }
                i12 += Character.charCount(codePointAt);
            }
        }

        private final void l(me.d dVar, String str, int i10, int i11, boolean z10) {
            int i12;
            while (i10 < i11) {
                int codePointAt = str.codePointAt(i10);
                if (codePointAt == 37 && (i12 = i10 + 2) < i11) {
                    int G = ae.d.G(str.charAt(i10 + 1));
                    int G2 = ae.d.G(str.charAt(i12));
                    if (G != -1 && G2 != -1) {
                        dVar.t((G << 4) + G2);
                        i10 = i12 + Character.charCount(codePointAt);
                    }
                    dVar.K0(codePointAt);
                    i10 += Character.charCount(codePointAt);
                } else {
                    if (codePointAt == 43 && z10) {
                        dVar.t(32);
                        i10++;
                    }
                    dVar.K0(codePointAt);
                    i10 += Character.charCount(codePointAt);
                }
            }
        }

        public final String a(String str, int i10, int i11, String str2, boolean z10, boolean z11, boolean z12, boolean z13, Charset charset) {
            boolean H;
            za.k.e(str, "<this>");
            za.k.e(str2, "encodeSet");
            int i12 = i10;
            while (i12 < i11) {
                int codePointAt = str.codePointAt(i12);
                if (codePointAt >= 32 && codePointAt != 127 && (codePointAt < 128 || z13)) {
                    H = sd.v.H(str2, (char) codePointAt, false, 2, null);
                    if (!H) {
                        if (codePointAt == 37) {
                            if (z10) {
                                if (z11) {
                                    if (!e(str, i12, i11)) {
                                        me.d dVar = new me.d();
                                        dVar.R(str, i10, i12);
                                        k(dVar, str, i12, i11, str2, z10, z11, z12, z13, charset);
                                        return dVar.o0();
                                    }
                                    if (codePointAt == 43 || !z12) {
                                        i12 += Character.charCount(codePointAt);
                                    } else {
                                        me.d dVar2 = new me.d();
                                        dVar2.R(str, i10, i12);
                                        k(dVar2, str, i12, i11, str2, z10, z11, z12, z13, charset);
                                        return dVar2.o0();
                                    }
                                }
                            }
                        }
                        if (codePointAt == 43) {
                        }
                        i12 += Character.charCount(codePointAt);
                    }
                }
                me.d dVar22 = new me.d();
                dVar22.R(str, i10, i12);
                k(dVar22, str, i12, i11, str2, z10, z11, z12, z13, charset);
                return dVar22.o0();
            }
            String substring = str.substring(i10, i11);
            za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            return substring;
        }

        public final int c(String scheme) {
            za.k.e(scheme, "scheme");
            if (za.k.a(scheme, "http")) {
                return 80;
            }
            return za.k.a(scheme, "https") ? 443 : -1;
        }

        public final HttpUrl d(String str) {
            za.k.e(str, "<this>");
            return new a().o(null, str).a();
        }

        public final String f(String str, int i10, int i11, boolean z10) {
            za.k.e(str, "<this>");
            int i12 = i10;
            while (i12 < i11) {
                int i13 = i12 + 1;
                char charAt = str.charAt(i12);
                if (charAt == '%' || (charAt == '+' && z10)) {
                    me.d dVar = new me.d();
                    dVar.R(str, i10, i12);
                    l(dVar, str, i12, i11, z10);
                    return dVar.o0();
                }
                i12 = i13;
            }
            String substring = str.substring(i10, i11);
            za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            return substring;
        }

        public final void h(List<String> list, StringBuilder sb2) {
            za.k.e(list, "<this>");
            za.k.e(sb2, "out");
            int size = list.size();
            for (int i10 = 0; i10 < size; i10++) {
                sb2.append('/');
                sb2.append(list.get(i10));
            }
        }

        public final List<String> i(String str) {
            int U;
            int U2;
            za.k.e(str, "<this>");
            ArrayList arrayList = new ArrayList();
            int i10 = 0;
            while (i10 <= str.length()) {
                U = sd.v.U(str, '&', i10, false, 4, null);
                if (U == -1) {
                    U = str.length();
                }
                int i11 = U;
                U2 = sd.v.U(str, '=', i10, false, 4, null);
                if (U2 != -1 && U2 <= i11) {
                    String substring = str.substring(i10, U2);
                    za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                    arrayList.add(substring);
                    String substring2 = str.substring(U2 + 1, i11);
                    za.k.d(substring2, "this as java.lang.String…ing(startIndex, endIndex)");
                    arrayList.add(substring2);
                } else {
                    String substring3 = str.substring(i10, i11);
                    za.k.d(substring3, "this as java.lang.String…ing(startIndex, endIndex)");
                    arrayList.add(substring3);
                    arrayList.add(null);
                }
                i10 = i11 + 1;
            }
            return arrayList;
        }

        public final void j(List<String> list, StringBuilder sb2) {
            PrimitiveRanges k10;
            Progressions j10;
            za.k.e(list, "<this>");
            za.k.e(sb2, "out");
            k10 = _Ranges.k(0, list.size());
            j10 = _Ranges.j(k10, 2);
            int d10 = j10.d();
            int e10 = j10.e();
            int f10 = j10.f();
            if ((f10 <= 0 || d10 > e10) && (f10 >= 0 || e10 > d10)) {
                return;
            }
            while (true) {
                int i10 = d10 + f10;
                String str = list.get(d10);
                String str2 = list.get(d10 + 1);
                if (d10 > 0) {
                    sb2.append('&');
                }
                sb2.append(str);
                if (str2 != null) {
                    sb2.append('=');
                    sb2.append(str2);
                }
                if (d10 == e10) {
                    return;
                } else {
                    d10 = i10;
                }
            }
        }
    }

    public HttpUrl(String str, String str2, String str3, String str4, int i10, List<String> list, List<String> list2, String str5, String str6) {
        za.k.e(str, "scheme");
        za.k.e(str2, "username");
        za.k.e(str3, "password");
        za.k.e(str4, "host");
        za.k.e(list, "pathSegments");
        za.k.e(str6, "url");
        this.f20713a = str;
        this.f20714b = str2;
        this.f20715c = str3;
        this.f20716d = str4;
        this.f20717e = i10;
        this.f20718f = list;
        this.f20719g = list2;
        this.f20720h = str5;
        this.f20721i = str6;
        this.f20722j = za.k.a(str, "https");
    }

    public final String b() {
        int U;
        if (this.f20720h == null) {
            return null;
        }
        U = sd.v.U(this.f20721i, '#', 0, false, 6, null);
        String substring = this.f20721i.substring(U + 1);
        za.k.d(substring, "this as java.lang.String).substring(startIndex)");
        return substring;
    }

    public final String c() {
        int U;
        int U2;
        if (this.f20715c.length() == 0) {
            return "";
        }
        U = sd.v.U(this.f20721i, COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, this.f20713a.length() + 3, false, 4, null);
        U2 = sd.v.U(this.f20721i, '@', 0, false, 6, null);
        String substring = this.f20721i.substring(U + 1, U2);
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public final String d() {
        int U;
        U = sd.v.U(this.f20721i, '/', this.f20713a.length() + 3, false, 4, null);
        String str = this.f20721i;
        String substring = this.f20721i.substring(U, ae.d.p(str, "?#", U, str.length()));
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public final List<String> e() {
        int U;
        U = sd.v.U(this.f20721i, '/', this.f20713a.length() + 3, false, 4, null);
        String str = this.f20721i;
        int p10 = ae.d.p(str, "?#", U, str.length());
        ArrayList arrayList = new ArrayList();
        while (U < p10) {
            int i10 = U + 1;
            int o10 = ae.d.o(this.f20721i, '/', i10, p10);
            String substring = this.f20721i.substring(i10, o10);
            za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            arrayList.add(substring);
            U = o10;
        }
        return arrayList;
    }

    public boolean equals(Object other) {
        return (other instanceof HttpUrl) && za.k.a(((HttpUrl) other).f20721i, this.f20721i);
    }

    public final String f() {
        int U;
        if (this.f20719g == null) {
            return null;
        }
        U = sd.v.U(this.f20721i, '?', 0, false, 6, null);
        int i10 = U + 1;
        String str = this.f20721i;
        String substring = this.f20721i.substring(i10, ae.d.o(str, '#', i10, str.length()));
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public final String g() {
        if (this.f20714b.length() == 0) {
            return "";
        }
        int length = this.f20713a.length() + 3;
        String str = this.f20721i;
        String substring = this.f20721i.substring(length, ae.d.p(str, ":@", length, str.length()));
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    /* renamed from: h, reason: from getter */
    public final String getF20716d() {
        return this.f20716d;
    }

    public int hashCode() {
        return this.f20721i.hashCode();
    }

    /* renamed from: i, reason: from getter */
    public final boolean getF20722j() {
        return this.f20722j;
    }

    public final a j() {
        a aVar = new a();
        aVar.C(this.f20713a);
        aVar.z(g());
        aVar.x(c());
        aVar.A(this.f20716d);
        aVar.B(this.f20717e != f20711k.c(this.f20713a) ? this.f20717e : -1);
        aVar.f().clear();
        aVar.f().addAll(e());
        aVar.c(f());
        aVar.w(b());
        return aVar;
    }

    public final a k(String link) {
        za.k.e(link, "link");
        try {
            return new a().o(this, link);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    /* renamed from: l, reason: from getter */
    public final int getF20717e() {
        return this.f20717e;
    }

    public final String m() {
        if (this.f20719g == null) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        f20711k.j(this.f20719g, sb2);
        return sb2.toString();
    }

    public final String n() {
        a k10 = k("/...");
        za.k.b(k10);
        return k10.D("").p("").a().getF20721i();
    }

    public final HttpUrl o(String link) {
        za.k.e(link, "link");
        a k10 = k(link);
        if (k10 == null) {
            return null;
        }
        return k10.a();
    }

    /* renamed from: p, reason: from getter */
    public final String getF20713a() {
        return this.f20713a;
    }

    public final URI q() {
        String aVar = j().t().toString();
        try {
            return new URI(aVar);
        } catch (URISyntaxException e10) {
            try {
                URI create = URI.create(new sd.j("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]").c(aVar, ""));
                za.k.d(create, "{\n      // Unlikely edge…Unexpected!\n      }\n    }");
                return create;
            } catch (Exception unused) {
                throw new RuntimeException(e10);
            }
        }
    }

    public final URL r() {
        try {
            return new URL(this.f20721i);
        } catch (MalformedURLException e10) {
            throw new RuntimeException(e10);
        }
    }

    /* renamed from: toString, reason: from getter */
    public String getF20721i() {
        return this.f20721i;
    }
}
