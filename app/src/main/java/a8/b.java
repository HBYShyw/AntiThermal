package a8;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import java.util.Locale;

/* compiled from: LocaleSet.java */
/* loaded from: classes.dex */
public class b {

    /* renamed from: c, reason: collision with root package name */
    @SuppressLint({"DefaultLocale"})
    private static final String f98c = Locale.CHINESE.getLanguage().toLowerCase();

    /* renamed from: d, reason: collision with root package name */
    @SuppressLint({"DefaultLocale"})
    private static final String f99d = Locale.JAPANESE.getLanguage().toLowerCase();

    /* renamed from: e, reason: collision with root package name */
    @SuppressLint({"DefaultLocale"})
    private static final String f100e = Locale.KOREAN.getLanguage().toLowerCase();

    /* renamed from: a, reason: collision with root package name */
    private final a f101a;

    /* renamed from: b, reason: collision with root package name */
    private final a f102b;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LocaleSet.java */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private final Locale f103a;

        /* renamed from: b, reason: collision with root package name */
        private final String f104b;

        /* renamed from: c, reason: collision with root package name */
        private final boolean f105c;

        @SuppressLint({"DefaultLocale"})
        public a(Locale locale) {
            this.f103a = locale;
            if (locale != null) {
                String lowerCase = locale.getLanguage().toLowerCase();
                this.f104b = lowerCase;
                this.f105c = c(lowerCase);
            } else {
                this.f104b = null;
                this.f105c = false;
            }
        }

        private static boolean c(String str) {
            return b.f98c.equals(str) || b.f99d.equals(str) || b.f100e.equals(str);
        }

        public Locale a() {
            return this.f103a;
        }

        public boolean b() {
            return this.f103a != null;
        }

        public boolean d(Locale locale) {
            Locale locale2 = this.f103a;
            return locale2 == null ? locale == null : locale2.equals(locale);
        }

        public String toString() {
            Locale locale = this.f103a;
            return locale != null ? locale.toLanguageTag() : "(null)";
        }
    }

    public b(Locale locale) {
        this(locale, null);
    }

    public static b d() {
        return new b(Locale.getDefault());
    }

    public static boolean h(Locale locale) {
        if (locale == null || !TextUtils.equals(locale.getLanguage(), f98c)) {
            return false;
        }
        if (!TextUtils.isEmpty(locale.getScript())) {
            return locale.getScript().equals("Hans");
        }
        return locale.equals(Locale.SIMPLIFIED_CHINESE);
    }

    public Locale e() {
        return this.f101a.a();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof b)) {
            return false;
        }
        b bVar = (b) obj;
        return bVar.i(this.f101a.a()) && bVar.j(this.f102b.a());
    }

    public Locale f() {
        return this.f102b.a();
    }

    public boolean g() {
        return this.f102b.b();
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean i(Locale locale) {
        return this.f101a.d(locale);
    }

    public boolean j(Locale locale) {
        return this.f102b.d(locale);
    }

    public boolean k() {
        return h(f());
    }

    public final String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f101a.toString());
        if (g()) {
            sb2.append(";");
            sb2.append(this.f102b.toString());
        }
        return sb2.toString();
    }

    public b(Locale locale, Locale locale2) {
        this.f101a = new a(locale);
        this.f102b = new a(locale.equals(locale2) ? null : locale2);
    }
}
