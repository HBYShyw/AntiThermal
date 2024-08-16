package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.LocaleManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.app.AppLocalesStorageHelper;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.BuildCompat;
import androidx.core.os.LocaleListCompat;
import j.ArraySet;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/* compiled from: AppCompatDelegate.java */
/* renamed from: androidx.appcompat.app.e, reason: use source file name */
/* loaded from: classes.dex */
public abstract class AppCompatDelegate {

    /* renamed from: e, reason: collision with root package name */
    static AppLocalesStorageHelper.a f473e = new AppLocalesStorageHelper.a(new AppLocalesStorageHelper.b());

    /* renamed from: f, reason: collision with root package name */
    private static int f474f = -100;

    /* renamed from: g, reason: collision with root package name */
    private static LocaleListCompat f475g = null;

    /* renamed from: h, reason: collision with root package name */
    private static LocaleListCompat f476h = null;

    /* renamed from: i, reason: collision with root package name */
    private static Boolean f477i = null;

    /* renamed from: j, reason: collision with root package name */
    private static boolean f478j = false;

    /* renamed from: k, reason: collision with root package name */
    private static final ArraySet<WeakReference<AppCompatDelegate>> f479k = new ArraySet<>();

    /* renamed from: l, reason: collision with root package name */
    private static final Object f480l = new Object();

    /* renamed from: m, reason: collision with root package name */
    private static final Object f481m = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatDelegate.java */
    /* renamed from: androidx.appcompat.app.e$a */
    /* loaded from: classes.dex */
    public static class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static LocaleList a(String str) {
            return LocaleList.forLanguageTags(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatDelegate.java */
    /* renamed from: androidx.appcompat.app.e$b */
    /* loaded from: classes.dex */
    public static class b {
        static LocaleList a(Object obj) {
            return ((LocaleManager) obj).getApplicationLocales();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static void b(Object obj, LocaleList localeList) {
            ((LocaleManager) obj).setApplicationLocales(localeList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void F(AppCompatDelegate appCompatDelegate) {
        synchronized (f480l) {
            G(appCompatDelegate);
        }
    }

    private static void G(AppCompatDelegate appCompatDelegate) {
        synchronized (f480l) {
            Iterator<WeakReference<AppCompatDelegate>> it = f479k.iterator();
            while (it.hasNext()) {
                AppCompatDelegate appCompatDelegate2 = it.next().get();
                if (appCompatDelegate2 == appCompatDelegate || appCompatDelegate2 == null) {
                    it.remove();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void Q(final Context context) {
        if (v(context)) {
            if (BuildCompat.c()) {
                if (f478j) {
                    return;
                }
                f473e.execute(new Runnable() { // from class: androidx.appcompat.app.d
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppCompatDelegate.w(context);
                    }
                });
                return;
            }
            synchronized (f481m) {
                LocaleListCompat localeListCompat = f475g;
                if (localeListCompat == null) {
                    if (f476h == null) {
                        f476h = LocaleListCompat.b(AppLocalesStorageHelper.b(context));
                    }
                    if (f476h.e()) {
                    } else {
                        f475g = f476h;
                    }
                } else if (!localeListCompat.equals(f476h)) {
                    LocaleListCompat localeListCompat2 = f475g;
                    f476h = localeListCompat2;
                    AppLocalesStorageHelper.a(context, localeListCompat2.g());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(AppCompatDelegate appCompatDelegate) {
        synchronized (f480l) {
            G(appCompatDelegate);
            f479k.add(new WeakReference<>(appCompatDelegate));
        }
    }

    public static AppCompatDelegate h(Activity activity, AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl(activity, appCompatCallback);
    }

    public static AppCompatDelegate i(Dialog dialog, AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl(dialog, appCompatCallback);
    }

    public static LocaleListCompat k() {
        if (BuildCompat.c()) {
            Object p10 = p();
            if (p10 != null) {
                return LocaleListCompat.h(b.a(p10));
            }
        } else {
            LocaleListCompat localeListCompat = f475g;
            if (localeListCompat != null) {
                return localeListCompat;
            }
        }
        return LocaleListCompat.d();
    }

    public static int m() {
        return f474f;
    }

    static Object p() {
        Context l10;
        Iterator<WeakReference<AppCompatDelegate>> it = f479k.iterator();
        while (it.hasNext()) {
            AppCompatDelegate appCompatDelegate = it.next().get();
            if (appCompatDelegate != null && (l10 = appCompatDelegate.l()) != null) {
                return l10.getSystemService("locale");
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LocaleListCompat r() {
        return f475g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean v(Context context) {
        if (f477i == null) {
            try {
                Bundle bundle = AppLocalesMetadataHolderService.a(context).metaData;
                if (bundle != null) {
                    f477i = Boolean.valueOf(bundle.getBoolean("autoStoreLocales"));
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.d("AppCompatDelegate", "Checking for metadata for AppLocalesMetadataHolderService : Service not found");
                f477i = Boolean.FALSE;
            }
        }
        return f477i.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void w(Context context) {
        AppLocalesStorageHelper.c(context);
        f478j = true;
    }

    public abstract void A(Bundle bundle);

    public abstract void B();

    public abstract void C(Bundle bundle);

    public abstract void D();

    public abstract void E();

    public abstract boolean H(int i10);

    public abstract void I(int i10);

    public abstract void J(View view);

    public abstract void K(View view, ViewGroup.LayoutParams layoutParams);

    public void L(OnBackInvokedDispatcher onBackInvokedDispatcher) {
    }

    public abstract void M(Toolbar toolbar);

    public void N(int i10) {
    }

    public abstract void O(CharSequence charSequence);

    public abstract ActionMode P(ActionMode.a aVar);

    public abstract void e(View view, ViewGroup.LayoutParams layoutParams);

    @Deprecated
    public void f(Context context) {
    }

    public Context g(Context context) {
        f(context);
        return context;
    }

    public abstract <T extends View> T j(int i10);

    public Context l() {
        return null;
    }

    public abstract ActionBarDrawerToggle n();

    public int o() {
        return -100;
    }

    public abstract MenuInflater q();

    public abstract ActionBar s();

    public abstract void t();

    public abstract void u();

    public abstract void x(Configuration configuration);

    public abstract void y(Bundle bundle);

    public abstract void z();
}
