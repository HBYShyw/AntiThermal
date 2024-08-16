package w9;

import android.text.TextUtils;
import java.util.Locale;
import java.util.Objects;

/* compiled from: LocaleSet.java */
/* loaded from: classes2.dex */
public class c {

    /* renamed from: d, reason: collision with root package name */
    private static final String f19425d = Locale.CHINESE.getLanguage().toLowerCase();

    /* renamed from: e, reason: collision with root package name */
    private static final String f19426e = Locale.JAPANESE.getLanguage().toLowerCase();

    /* renamed from: f, reason: collision with root package name */
    private static final String f19427f = Locale.KOREAN.getLanguage().toLowerCase();

    /* renamed from: a, reason: collision with root package name */
    private final a f19428a;

    /* renamed from: b, reason: collision with root package name */
    private final a f19429b;

    /* renamed from: c, reason: collision with root package name */
    private volatile transient int f19430c;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LocaleSet.java */
    /* loaded from: classes2.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private final Locale f19431a;

        /* renamed from: b, reason: collision with root package name */
        private final String f19432b;

        /* renamed from: c, reason: collision with root package name */
        private final boolean f19433c;

        public a(Locale locale) {
            this.f19431a = locale;
            if (locale != null) {
                String lowerCase = locale.getLanguage().toLowerCase();
                this.f19432b = lowerCase;
                this.f19433c = c(lowerCase);
            } else {
                this.f19432b = null;
                this.f19433c = false;
            }
        }

        private static boolean c(String str) {
            return c.f19425d.equals(str) || c.f19426e.equals(str) || c.f19427f.equals(str);
        }

        public Locale a() {
            return this.f19431a;
        }

        public boolean b() {
            return this.f19431a != null;
        }

        public boolean d(Locale locale) {
            return Objects.equals(this.f19431a, locale);
        }

        public String toString() {
            Locale locale = this.f19431a;
            return locale != null ? locale.toLanguageTag() : "(null)";
        }
    }

    public c(Locale locale) {
        this(locale, null);
    }

    public static c d() {
        return new c(Locale.getDefault());
    }

    public static boolean h(Locale locale) {
        if (locale == null || !TextUtils.equals(locale.getLanguage(), f19425d)) {
            return false;
        }
        if (!TextUtils.isEmpty(locale.getScript())) {
            return locale.getScript().equals("Hans");
        }
        return locale.equals(Locale.SIMPLIFIED_CHINESE);
    }

    public Locale e() {
        return this.f19428a.a();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        return cVar.i(this.f19428a.a()) && cVar.j(this.f19429b.a());
    }

    public Locale f() {
        return this.f19429b.a();
    }

    public boolean g() {
        return this.f19429b.b();
    }

    public int hashCode() {
        int i10 = this.f19430c;
        if (i10 != 0) {
            return i10;
        }
        int hashCode = this.f19428a.hashCode() ^ this.f19429b.hashCode();
        this.f19430c = hashCode;
        return hashCode;
    }

    public boolean i(Locale locale) {
        return this.f19428a.d(locale);
    }

    public boolean j(Locale locale) {
        return this.f19429b.d(locale);
    }

    public boolean k() {
        return h(f());
    }

    public final String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f19428a.toString());
        if (g()) {
            sb2.append(";");
            sb2.append(this.f19429b.toString());
        }
        return sb2.toString();
    }

    public c(Locale locale, Locale locale2) {
        this.f19430c = 0;
        this.f19428a = new a(locale);
        this.f19429b = new a(Objects.equals(locale, locale2) ? null : locale2);
    }
}
