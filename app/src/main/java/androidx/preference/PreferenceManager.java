package androidx.preference;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.content.ContextCompat;

/* compiled from: PreferenceManager.java */
/* renamed from: androidx.preference.j, reason: use source file name */
/* loaded from: classes.dex */
public class PreferenceManager {

    /* renamed from: a, reason: collision with root package name */
    private Context f3345a;

    /* renamed from: c, reason: collision with root package name */
    private SharedPreferences f3347c;

    /* renamed from: d, reason: collision with root package name */
    private SharedPreferences.Editor f3348d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f3349e;

    /* renamed from: f, reason: collision with root package name */
    private String f3350f;

    /* renamed from: g, reason: collision with root package name */
    private int f3351g;

    /* renamed from: i, reason: collision with root package name */
    private PreferenceScreen f3353i;

    /* renamed from: j, reason: collision with root package name */
    private c f3354j;

    /* renamed from: k, reason: collision with root package name */
    private a f3355k;

    /* renamed from: l, reason: collision with root package name */
    private b f3356l;

    /* renamed from: b, reason: collision with root package name */
    private long f3346b = 0;

    /* renamed from: h, reason: collision with root package name */
    private int f3352h = 0;

    /* compiled from: PreferenceManager.java */
    /* renamed from: androidx.preference.j$a */
    /* loaded from: classes.dex */
    public interface a {
        void onDisplayPreferenceDialog(Preference preference);
    }

    /* compiled from: PreferenceManager.java */
    /* renamed from: androidx.preference.j$b */
    /* loaded from: classes.dex */
    public interface b {
        void onNavigateToScreen(PreferenceScreen preferenceScreen);
    }

    /* compiled from: PreferenceManager.java */
    /* renamed from: androidx.preference.j$c */
    /* loaded from: classes.dex */
    public interface c {
        boolean onPreferenceTreeClick(Preference preference);
    }

    /* compiled from: PreferenceManager.java */
    /* renamed from: androidx.preference.j$d */
    /* loaded from: classes.dex */
    public static abstract class d {
    }

    public PreferenceManager(Context context) {
        this.f3345a = context;
        s(d(context));
    }

    public static SharedPreferences b(Context context) {
        return context.getSharedPreferences(d(context), c());
    }

    private static int c() {
        return 0;
    }

    private static String d(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private void n(boolean z10) {
        SharedPreferences.Editor editor;
        if (!z10 && (editor = this.f3348d) != null) {
            editor.apply();
        }
        this.f3349e = z10;
    }

    public <T extends Preference> T a(CharSequence charSequence) {
        PreferenceScreen preferenceScreen = this.f3353i;
        if (preferenceScreen == null) {
            return null;
        }
        return (T) preferenceScreen.e(charSequence);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SharedPreferences.Editor e() {
        if (this.f3349e) {
            if (this.f3348d == null) {
                this.f3348d = l().edit();
            }
            return this.f3348d;
        }
        return l().edit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long f() {
        long j10;
        synchronized (this) {
            j10 = this.f3346b;
            this.f3346b = 1 + j10;
        }
        return j10;
    }

    public b g() {
        return this.f3356l;
    }

    public c h() {
        return this.f3354j;
    }

    public d i() {
        return null;
    }

    public PreferenceDataStore j() {
        return null;
    }

    public PreferenceScreen k() {
        return this.f3353i;
    }

    public SharedPreferences l() {
        Context b10;
        j();
        if (this.f3347c == null) {
            if (this.f3352h != 1) {
                b10 = this.f3345a;
            } else {
                b10 = ContextCompat.b(this.f3345a);
            }
            this.f3347c = b10.getSharedPreferences(this.f3350f, this.f3351g);
        }
        return this.f3347c;
    }

    public PreferenceScreen m(Context context, int i10, PreferenceScreen preferenceScreen) {
        n(true);
        PreferenceScreen preferenceScreen2 = (PreferenceScreen) new PreferenceInflater(context, this).d(i10, preferenceScreen);
        preferenceScreen2.onAttachedToHierarchy(this);
        n(false);
        return preferenceScreen2;
    }

    public void o(a aVar) {
        this.f3355k = aVar;
    }

    public void p(b bVar) {
        this.f3356l = bVar;
    }

    public void q(c cVar) {
        this.f3354j = cVar;
    }

    public boolean r(PreferenceScreen preferenceScreen) {
        PreferenceScreen preferenceScreen2 = this.f3353i;
        if (preferenceScreen == preferenceScreen2) {
            return false;
        }
        if (preferenceScreen2 != null) {
            preferenceScreen2.onDetached();
        }
        this.f3353i = preferenceScreen;
        return true;
    }

    public void s(String str) {
        this.f3350f = str;
        this.f3347c = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t() {
        return !this.f3349e;
    }

    public void u(Preference preference) {
        a aVar = this.f3355k;
        if (aVar != null) {
            aVar.onDisplayPreferenceDialog(preference);
        }
    }
}
