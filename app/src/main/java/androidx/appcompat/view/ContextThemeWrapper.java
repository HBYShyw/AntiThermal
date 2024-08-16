package androidx.appcompat.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import androidx.appcompat.R$style;

/* compiled from: ContextThemeWrapper.java */
/* renamed from: androidx.appcompat.view.d, reason: use source file name */
/* loaded from: classes.dex */
public class ContextThemeWrapper extends ContextWrapper {

    /* renamed from: f, reason: collision with root package name */
    private static Configuration f559f;

    /* renamed from: a, reason: collision with root package name */
    private int f560a;

    /* renamed from: b, reason: collision with root package name */
    private Resources.Theme f561b;

    /* renamed from: c, reason: collision with root package name */
    private LayoutInflater f562c;

    /* renamed from: d, reason: collision with root package name */
    private Configuration f563d;

    /* renamed from: e, reason: collision with root package name */
    private Resources f564e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ContextThemeWrapper.java */
    /* renamed from: androidx.appcompat.view.d$a */
    /* loaded from: classes.dex */
    public static class a {
        static Context a(ContextThemeWrapper contextThemeWrapper, Configuration configuration) {
            return contextThemeWrapper.createConfigurationContext(configuration);
        }
    }

    public ContextThemeWrapper() {
        super(null);
    }

    private Resources b() {
        if (this.f564e == null) {
            Configuration configuration = this.f563d;
            if (configuration != null && !d(configuration)) {
                this.f564e = a.a(this, this.f563d).getResources();
            } else {
                this.f564e = super.getResources();
            }
        }
        return this.f564e;
    }

    private void c() {
        boolean z10 = this.f561b == null;
        if (z10) {
            this.f561b = getResources().newTheme();
            Resources.Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                this.f561b.setTo(theme);
            }
        }
        e(this.f561b, this.f560a, z10);
    }

    private static boolean d(Configuration configuration) {
        if (configuration == null) {
            return true;
        }
        if (f559f == null) {
            Configuration configuration2 = new Configuration();
            configuration2.fontScale = 0.0f;
            f559f = configuration2;
        }
        return configuration.equals(f559f);
    }

    public void a(Configuration configuration) {
        if (this.f564e == null) {
            if (this.f563d == null) {
                this.f563d = new Configuration(configuration);
                return;
            }
            throw new IllegalStateException("Override configuration has already been set");
        }
        throw new IllegalStateException("getResources() or getAssets() has already been called");
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    protected void e(Resources.Theme theme, int i10, boolean z10) {
        theme.applyStyle(i10, true);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return b();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String str) {
        if ("layout_inflater".equals(str)) {
            if (this.f562c == null) {
                this.f562c = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return this.f562c;
        }
        return getBaseContext().getSystemService(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        Resources.Theme theme = this.f561b;
        if (theme != null) {
            return theme;
        }
        if (this.f560a == 0) {
            this.f560a = R$style.Theme_AppCompat_Light;
        }
        c();
        return this.f561b;
    }

    public int getThemeResId() {
        return this.f560a;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i10) {
        if (this.f560a != i10) {
            this.f560a = i10;
            c();
        }
    }

    public ContextThemeWrapper(Context context, int i10) {
        super(context);
        this.f560a = i10;
    }

    public ContextThemeWrapper(Context context, Resources.Theme theme) {
        super(context);
        this.f561b = theme;
    }
}
