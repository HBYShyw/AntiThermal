package androidx.appcompat.app;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$color;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$style;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.appcompat.widget.n0;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.lifecycle.h;
import c.AppCompatResources;
import com.oplus.statistics.DataTypeConstants;
import j.SimpleArrayMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.a, LayoutInflater.Factory2 {

    /* renamed from: n0, reason: collision with root package name */
    private static final SimpleArrayMap<String, Integer> f391n0 = new SimpleArrayMap<>();

    /* renamed from: o0, reason: collision with root package name */
    private static final boolean f392o0 = false;

    /* renamed from: p0, reason: collision with root package name */
    private static final int[] f393p0 = {R.attr.windowBackground};

    /* renamed from: q0, reason: collision with root package name */
    private static final boolean f394q0 = !"robolectric".equals(Build.FINGERPRINT);

    /* renamed from: r0, reason: collision with root package name */
    private static final boolean f395r0 = true;
    PopupWindow A;
    Runnable B;
    ViewPropertyAnimatorCompat C;
    private boolean D;
    private boolean E;
    ViewGroup F;
    private TextView G;
    private View H;
    private boolean I;
    private boolean J;
    boolean K;
    boolean L;
    boolean M;
    boolean N;
    boolean O;
    private boolean P;
    private PanelFeatureState[] Q;
    private PanelFeatureState R;
    private boolean S;
    private boolean T;
    private boolean U;
    boolean V;
    private Configuration W;
    private int X;
    private int Y;
    private int Z;

    /* renamed from: a0, reason: collision with root package name */
    private boolean f396a0;

    /* renamed from: b0, reason: collision with root package name */
    private q f397b0;

    /* renamed from: c0, reason: collision with root package name */
    private q f398c0;

    /* renamed from: d0, reason: collision with root package name */
    boolean f399d0;

    /* renamed from: e0, reason: collision with root package name */
    int f400e0;

    /* renamed from: f0, reason: collision with root package name */
    private final Runnable f401f0;

    /* renamed from: g0, reason: collision with root package name */
    private boolean f402g0;

    /* renamed from: h0, reason: collision with root package name */
    private Rect f403h0;

    /* renamed from: i0, reason: collision with root package name */
    private Rect f404i0;

    /* renamed from: j0, reason: collision with root package name */
    private AppCompatViewInflater f405j0;

    /* renamed from: k0, reason: collision with root package name */
    private LayoutIncludeDetector f406k0;

    /* renamed from: l0, reason: collision with root package name */
    private OnBackInvokedDispatcher f407l0;

    /* renamed from: m0, reason: collision with root package name */
    private OnBackInvokedCallback f408m0;

    /* renamed from: n, reason: collision with root package name */
    final Object f409n;

    /* renamed from: o, reason: collision with root package name */
    final Context f410o;

    /* renamed from: p, reason: collision with root package name */
    Window f411p;

    /* renamed from: q, reason: collision with root package name */
    private o f412q;

    /* renamed from: r, reason: collision with root package name */
    final AppCompatCallback f413r;

    /* renamed from: s, reason: collision with root package name */
    ActionBar f414s;

    /* renamed from: t, reason: collision with root package name */
    MenuInflater f415t;

    /* renamed from: u, reason: collision with root package name */
    private CharSequence f416u;

    /* renamed from: v, reason: collision with root package name */
    private DecorContentParent f417v;

    /* renamed from: w, reason: collision with root package name */
    private h f418w;

    /* renamed from: x, reason: collision with root package name */
    private u f419x;

    /* renamed from: y, reason: collision with root package name */
    ActionMode f420y;

    /* renamed from: z, reason: collision with root package name */
    ActionBarContextView f421z;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class PanelFeatureState {

        /* renamed from: a, reason: collision with root package name */
        int f422a;

        /* renamed from: b, reason: collision with root package name */
        int f423b;

        /* renamed from: c, reason: collision with root package name */
        int f424c;

        /* renamed from: d, reason: collision with root package name */
        int f425d;

        /* renamed from: e, reason: collision with root package name */
        int f426e;

        /* renamed from: f, reason: collision with root package name */
        int f427f;

        /* renamed from: g, reason: collision with root package name */
        ViewGroup f428g;

        /* renamed from: h, reason: collision with root package name */
        View f429h;

        /* renamed from: i, reason: collision with root package name */
        View f430i;

        /* renamed from: j, reason: collision with root package name */
        MenuBuilder f431j;

        /* renamed from: k, reason: collision with root package name */
        ListMenuPresenter f432k;

        /* renamed from: l, reason: collision with root package name */
        Context f433l;

        /* renamed from: m, reason: collision with root package name */
        boolean f434m;

        /* renamed from: n, reason: collision with root package name */
        boolean f435n;

        /* renamed from: o, reason: collision with root package name */
        boolean f436o;

        /* renamed from: p, reason: collision with root package name */
        public boolean f437p;

        /* renamed from: q, reason: collision with root package name */
        boolean f438q = false;

        /* renamed from: r, reason: collision with root package name */
        boolean f439r;

        /* renamed from: s, reason: collision with root package name */
        Bundle f440s;

        /* JADX INFO: Access modifiers changed from: private */
        @SuppressLint({"BanParcelableUsage"})
        /* loaded from: classes.dex */
        public static class SavedState implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new a();

            /* renamed from: e, reason: collision with root package name */
            int f441e;

            /* renamed from: f, reason: collision with root package name */
            boolean f442f;

            /* renamed from: g, reason: collision with root package name */
            Bundle f443g;

            /* loaded from: classes.dex */
            class a implements Parcelable.ClassLoaderCreator<SavedState> {
                a() {
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public SavedState createFromParcel(Parcel parcel) {
                    return SavedState.j(parcel, null);
                }

                @Override // android.os.Parcelable.ClassLoaderCreator
                /* renamed from: b, reason: merged with bridge method [inline-methods] */
                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.j(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: c, reason: merged with bridge method [inline-methods] */
                public SavedState[] newArray(int i10) {
                    return new SavedState[i10];
                }
            }

            SavedState() {
            }

            static SavedState j(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.f441e = parcel.readInt();
                boolean z10 = parcel.readInt() == 1;
                savedState.f442f = z10;
                if (z10) {
                    savedState.f443g = parcel.readBundle(classLoader);
                }
                return savedState;
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i10) {
                parcel.writeInt(this.f441e);
                parcel.writeInt(this.f442f ? 1 : 0);
                if (this.f442f) {
                    parcel.writeBundle(this.f443g);
                }
            }
        }

        PanelFeatureState(int i10) {
            this.f422a = i10;
        }

        MenuView a(MenuPresenter.a aVar) {
            if (this.f431j == null) {
                return null;
            }
            if (this.f432k == null) {
                ListMenuPresenter listMenuPresenter = new ListMenuPresenter(this.f433l, R$layout.abc_list_menu_item_layout);
                this.f432k = listMenuPresenter;
                listMenuPresenter.setCallback(aVar);
                this.f431j.addMenuPresenter(this.f432k);
            }
            return this.f432k.b(this.f428g);
        }

        public boolean b() {
            if (this.f429h == null) {
                return false;
            }
            return this.f430i != null || this.f432k.a().getCount() > 0;
        }

        void c(MenuBuilder menuBuilder) {
            ListMenuPresenter listMenuPresenter;
            MenuBuilder menuBuilder2 = this.f431j;
            if (menuBuilder == menuBuilder2) {
                return;
            }
            if (menuBuilder2 != null) {
                menuBuilder2.removeMenuPresenter(this.f432k);
            }
            this.f431j = menuBuilder;
            if (menuBuilder == null || (listMenuPresenter = this.f432k) == null) {
                return;
            }
            menuBuilder.addMenuPresenter(listMenuPresenter);
        }

        void d(Context context) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme newTheme = context.getResources().newTheme();
            newTheme.setTo(context.getTheme());
            newTheme.resolveAttribute(R$attr.actionBarPopupTheme, typedValue, true);
            int i10 = typedValue.resourceId;
            if (i10 != 0) {
                newTheme.applyStyle(i10, true);
            }
            newTheme.resolveAttribute(R$attr.panelMenuListTheme, typedValue, true);
            int i11 = typedValue.resourceId;
            if (i11 != 0) {
                newTheme.applyStyle(i11, true);
            } else {
                newTheme.applyStyle(R$style.Theme_AppCompat_CompactMenu, true);
            }
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
            contextThemeWrapper.getTheme().setTo(newTheme);
            this.f433l = contextThemeWrapper;
            TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(R$styleable.AppCompatTheme);
            this.f423b = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTheme_panelBackground, 0);
            this.f427f = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if ((appCompatDelegateImpl.f400e0 & 1) != 0) {
                appCompatDelegateImpl.i0(0);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if ((appCompatDelegateImpl2.f400e0 & 4096) != 0) {
                appCompatDelegateImpl2.i0(108);
            }
            AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl3.f399d0 = false;
            appCompatDelegateImpl3.f400e0 = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements androidx.core.view.t {
        b() {
        }

        @Override // androidx.core.view.t
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            int l10 = windowInsetsCompat.l();
            int e12 = AppCompatDelegateImpl.this.e1(windowInsetsCompat, null);
            if (l10 != e12) {
                windowInsetsCompat = windowInsetsCompat.q(windowInsetsCompat.j(), e12, windowInsetsCompat.k(), windowInsetsCompat.i());
            }
            return ViewCompat.X(view, windowInsetsCompat);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ContentFrameLayout.a {
        c() {
        }

        @Override // androidx.appcompat.widget.ContentFrameLayout.a
        public void a() {
        }

        @Override // androidx.appcompat.widget.ContentFrameLayout.a
        public void onDetachedFromWindow() {
            AppCompatDelegateImpl.this.g0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* loaded from: classes.dex */
        class a extends ViewPropertyAnimatorListenerAdapter {
            a() {
            }

            @Override // androidx.core.view.ViewPropertyAnimatorListener
            public void b(View view) {
                AppCompatDelegateImpl.this.f421z.setAlpha(1.0f);
                AppCompatDelegateImpl.this.C.i(null);
                AppCompatDelegateImpl.this.C = null;
            }

            @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
            public void c(View view) {
                AppCompatDelegateImpl.this.f421z.setVisibility(0);
            }
        }

        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            appCompatDelegateImpl.A.showAtLocation(appCompatDelegateImpl.f421z, 55, 0, 0);
            AppCompatDelegateImpl.this.j0();
            if (AppCompatDelegateImpl.this.U0()) {
                AppCompatDelegateImpl.this.f421z.setAlpha(0.0f);
                AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl2.C = ViewCompat.d(appCompatDelegateImpl2.f421z).b(1.0f);
                AppCompatDelegateImpl.this.C.i(new a());
                return;
            }
            AppCompatDelegateImpl.this.f421z.setAlpha(1.0f);
            AppCompatDelegateImpl.this.f421z.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e extends ViewPropertyAnimatorListenerAdapter {
        e() {
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void b(View view) {
            AppCompatDelegateImpl.this.f421z.setAlpha(1.0f);
            AppCompatDelegateImpl.this.C.i(null);
            AppCompatDelegateImpl.this.C = null;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
        public void c(View view) {
            AppCompatDelegateImpl.this.f421z.setVisibility(0);
            if (AppCompatDelegateImpl.this.f421z.getParent() instanceof View) {
                ViewCompat.h0((View) AppCompatDelegateImpl.this.f421z.getParent());
            }
        }
    }

    /* loaded from: classes.dex */
    private class f implements ActionBarDrawerToggle {
        f() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface g {
        boolean a(int i10);

        View onCreatePanelView(int i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class h implements MenuPresenter.a {
        h() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public boolean a(MenuBuilder menuBuilder) {
            Window.Callback v02 = AppCompatDelegateImpl.this.v0();
            if (v02 == null) {
                return true;
            }
            v02.onMenuOpened(108, menuBuilder);
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
            AppCompatDelegateImpl.this.Z(menuBuilder);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements ActionMode.a {

        /* renamed from: a, reason: collision with root package name */
        private ActionMode.a f452a;

        /* loaded from: classes.dex */
        class a extends ViewPropertyAnimatorListenerAdapter {
            a() {
            }

            @Override // androidx.core.view.ViewPropertyAnimatorListener
            public void b(View view) {
                AppCompatDelegateImpl.this.f421z.setVisibility(8);
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                PopupWindow popupWindow = appCompatDelegateImpl.A;
                if (popupWindow != null) {
                    popupWindow.dismiss();
                } else if (appCompatDelegateImpl.f421z.getParent() instanceof View) {
                    ViewCompat.h0((View) AppCompatDelegateImpl.this.f421z.getParent());
                }
                AppCompatDelegateImpl.this.f421z.k();
                AppCompatDelegateImpl.this.C.i(null);
                AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl2.C = null;
                ViewCompat.h0(appCompatDelegateImpl2.F);
            }
        }

        public i(ActionMode.a aVar) {
            this.f452a = aVar;
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public void a(ActionMode actionMode) {
            this.f452a.a(actionMode);
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl.A != null) {
                appCompatDelegateImpl.f411p.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.B);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl2.f421z != null) {
                appCompatDelegateImpl2.j0();
                AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl3.C = ViewCompat.d(appCompatDelegateImpl3.f421z).b(0.0f);
                AppCompatDelegateImpl.this.C.i(new a());
            }
            AppCompatDelegateImpl appCompatDelegateImpl4 = AppCompatDelegateImpl.this;
            AppCompatCallback appCompatCallback = appCompatDelegateImpl4.f413r;
            if (appCompatCallback != null) {
                appCompatCallback.onSupportActionModeFinished(appCompatDelegateImpl4.f420y);
            }
            AppCompatDelegateImpl appCompatDelegateImpl5 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl5.f420y = null;
            ViewCompat.h0(appCompatDelegateImpl5.F);
            AppCompatDelegateImpl.this.c1();
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public boolean b(ActionMode actionMode, Menu menu) {
            return this.f452a.b(actionMode, menu);
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public boolean c(ActionMode actionMode, Menu menu) {
            ViewCompat.h0(AppCompatDelegateImpl.this.F);
            return this.f452a.c(actionMode, menu);
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public boolean d(ActionMode actionMode, MenuItem menuItem) {
            return this.f452a.d(actionMode, menuItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class j {
        static Context a(Context context, Configuration configuration) {
            return context.createConfigurationContext(configuration);
        }

        static void b(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            int i10 = configuration.densityDpi;
            int i11 = configuration2.densityDpi;
            if (i10 != i11) {
                configuration3.densityDpi = i11;
            }
        }

        static void c(Configuration configuration, Locale locale) {
            configuration.setLayoutDirection(locale);
        }

        static void d(Configuration configuration, Locale locale) {
            configuration.setLocale(locale);
        }
    }

    /* loaded from: classes.dex */
    static class k {
        static boolean a(PowerManager powerManager) {
            return powerManager.isPowerSaveMode();
        }

        static String b(Locale locale) {
            return locale.toLanguageTag();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class l {
        static void a(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            LocaleList locales = configuration.getLocales();
            LocaleList locales2 = configuration2.getLocales();
            if (locales.equals(locales2)) {
                return;
            }
            configuration3.setLocales(locales2);
            configuration3.locale = configuration2.locale;
        }

        static LocaleListCompat b(Configuration configuration) {
            return LocaleListCompat.b(configuration.getLocales().toLanguageTags());
        }

        public static void c(LocaleListCompat localeListCompat) {
            LocaleList.setDefault(LocaleList.forLanguageTags(localeListCompat.g()));
        }

        static void d(Configuration configuration, LocaleListCompat localeListCompat) {
            configuration.setLocales(LocaleList.forLanguageTags(localeListCompat.g()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class m {
        static void a(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            int i10 = configuration.colorMode & 3;
            int i11 = configuration2.colorMode;
            if (i10 != (i11 & 3)) {
                configuration3.colorMode |= i11 & 3;
            }
            int i12 = configuration.colorMode & 12;
            int i13 = configuration2.colorMode;
            if (i12 != (i13 & 12)) {
                configuration3.colorMode |= i13 & 12;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class n {
        static OnBackInvokedDispatcher a(Activity activity) {
            return activity.getOnBackInvokedDispatcher();
        }

        static OnBackInvokedCallback b(Object obj, final AppCompatDelegateImpl appCompatDelegateImpl) {
            Objects.requireNonNull(appCompatDelegateImpl);
            OnBackInvokedCallback onBackInvokedCallback = new OnBackInvokedCallback() { // from class: androidx.appcompat.app.f
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    AppCompatDelegateImpl.this.D0();
                }
            };
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(1000000, onBackInvokedCallback);
            return onBackInvokedCallback;
        }

        static void c(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class o extends WindowCallbackWrapper {

        /* renamed from: f, reason: collision with root package name */
        private g f455f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f456g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f457h;

        /* renamed from: i, reason: collision with root package name */
        private boolean f458i;

        o(Window.Callback callback) {
            super(callback);
        }

        public boolean b(Window.Callback callback, KeyEvent keyEvent) {
            try {
                this.f457h = true;
                return callback.dispatchKeyEvent(keyEvent);
            } finally {
                this.f457h = false;
            }
        }

        public void c(Window.Callback callback) {
            try {
                this.f456g = true;
                callback.onContentChanged();
            } finally {
                this.f456g = false;
            }
        }

        public void d(Window.Callback callback, int i10, Menu menu) {
            try {
                this.f458i = true;
                callback.onPanelClosed(i10, menu);
            } finally {
                this.f458i = false;
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (this.f457h) {
                return a().dispatchKeyEvent(keyEvent);
            }
            return AppCompatDelegateImpl.this.h0(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || AppCompatDelegateImpl.this.G0(keyEvent.getKeyCode(), keyEvent);
        }

        void e(g gVar) {
            this.f455f = gVar;
        }

        final android.view.ActionMode f(ActionMode.Callback callback) {
            SupportActionModeWrapper.a aVar = new SupportActionModeWrapper.a(AppCompatDelegateImpl.this.f410o, callback);
            androidx.appcompat.view.ActionMode P = AppCompatDelegateImpl.this.P(aVar);
            if (P != null) {
                return aVar.e(P);
            }
            return null;
        }

        @Override // android.view.Window.Callback
        public void onContentChanged() {
            if (this.f456g) {
                a().onContentChanged();
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onCreatePanelMenu(int i10, Menu menu) {
            if (i10 != 0 || (menu instanceof MenuBuilder)) {
                return super.onCreatePanelMenu(i10, menu);
            }
            return false;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public View onCreatePanelView(int i10) {
            View onCreatePanelView;
            g gVar = this.f455f;
            return (gVar == null || (onCreatePanelView = gVar.onCreatePanelView(i10)) == null) ? super.onCreatePanelView(i10) : onCreatePanelView;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onMenuOpened(int i10, Menu menu) {
            super.onMenuOpened(i10, menu);
            AppCompatDelegateImpl.this.J0(i10);
            return true;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onPanelClosed(int i10, Menu menu) {
            if (this.f458i) {
                a().onPanelClosed(i10, menu);
            } else {
                super.onPanelClosed(i10, menu);
                AppCompatDelegateImpl.this.K0(i10);
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onPreparePanel(int i10, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder) menu : null;
            if (i10 == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            g gVar = this.f455f;
            boolean z10 = gVar != null && gVar.a(i10);
            if (!z10) {
                z10 = super.onPreparePanel(i10, view, menu);
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return z10;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i10) {
            MenuBuilder menuBuilder;
            PanelFeatureState t02 = AppCompatDelegateImpl.this.t0(0, true);
            if (t02 != null && (menuBuilder = t02.f431j) != null) {
                super.onProvideKeyboardShortcuts(list, menuBuilder, i10);
            } else {
                super.onProvideKeyboardShortcuts(list, menu, i10);
            }
        }

        @Override // android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i10) {
            if (AppCompatDelegateImpl.this.B0() && i10 == 0) {
                return f(callback);
            }
            return super.onWindowStartingActionMode(callback, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class p extends q {

        /* renamed from: c, reason: collision with root package name */
        private final PowerManager f460c;

        p(Context context) {
            super();
            this.f460c = (PowerManager) context.getApplicationContext().getSystemService("power");
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.q
        IntentFilter b() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.q
        public int c() {
            return k.a(this.f460c) ? 2 : 1;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.q
        public void d() {
            AppCompatDelegateImpl.this.T();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class q {

        /* renamed from: a, reason: collision with root package name */
        private BroadcastReceiver f462a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a extends BroadcastReceiver {
            a() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                q.this.d();
            }
        }

        q() {
        }

        void a() {
            BroadcastReceiver broadcastReceiver = this.f462a;
            if (broadcastReceiver != null) {
                try {
                    AppCompatDelegateImpl.this.f410o.unregisterReceiver(broadcastReceiver);
                } catch (IllegalArgumentException unused) {
                }
                this.f462a = null;
            }
        }

        abstract IntentFilter b();

        abstract int c();

        abstract void d();

        void e() {
            a();
            IntentFilter b10 = b();
            if (b10 == null || b10.countActions() == 0) {
                return;
            }
            if (this.f462a == null) {
                this.f462a = new a();
            }
            AppCompatDelegateImpl.this.f410o.registerReceiver(this.f462a, b10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class r extends q {

        /* renamed from: c, reason: collision with root package name */
        private final TwilightManager f465c;

        r(TwilightManager twilightManager) {
            super();
            this.f465c = twilightManager;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.q
        IntentFilter b() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_TICK");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.q
        public int c() {
            return this.f465c.d() ? 2 : 1;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.q
        public void d() {
            AppCompatDelegateImpl.this.T();
        }
    }

    /* loaded from: classes.dex */
    private static class s {
        static void a(android.view.ContextThemeWrapper contextThemeWrapper, Configuration configuration) {
            contextThemeWrapper.applyOverrideConfiguration(configuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class t extends ContentFrameLayout {
        public t(Context context) {
            super(context);
        }

        private boolean b(int i10, int i11) {
            return i10 < -5 || i11 < -5 || i10 > getWidth() + 5 || i11 > getHeight() + 5;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.h0(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && b((int) motionEvent.getX(), (int) motionEvent.getY())) {
                AppCompatDelegateImpl.this.b0(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public void setBackgroundResource(int i10) {
            setBackgroundDrawable(AppCompatResources.b(getContext(), i10));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class u implements MenuPresenter.a {
        u() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public boolean a(MenuBuilder menuBuilder) {
            Window.Callback v02;
            if (menuBuilder != menuBuilder.getRootMenu()) {
                return true;
            }
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (!appCompatDelegateImpl.K || (v02 = appCompatDelegateImpl.v0()) == null || AppCompatDelegateImpl.this.V) {
                return true;
            }
            v02.onMenuOpened(108, menuBuilder);
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
            MenuBuilder rootMenu = menuBuilder.getRootMenu();
            boolean z11 = rootMenu != menuBuilder;
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (z11) {
                menuBuilder = rootMenu;
            }
            PanelFeatureState m02 = appCompatDelegateImpl.m0(menuBuilder);
            if (m02 != null) {
                if (z11) {
                    AppCompatDelegateImpl.this.Y(m02.f422a, m02, rootMenu);
                    AppCompatDelegateImpl.this.c0(m02, true);
                } else {
                    AppCompatDelegateImpl.this.c0(m02, z10);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatDelegateImpl(Activity activity, AppCompatCallback appCompatCallback) {
        this(activity, null, appCompatCallback, activity);
    }

    private void A0(int i10) {
        this.f400e0 = (1 << i10) | this.f400e0;
        if (this.f399d0) {
            return;
        }
        ViewCompat.c0(this.f411p.getDecorView(), this.f401f0);
        this.f399d0 = true;
    }

    private boolean F0(int i10, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        PanelFeatureState t02 = t0(i10, true);
        if (t02.f436o) {
            return false;
        }
        return P0(t02, keyEvent);
    }

    private boolean I0(int i10, KeyEvent keyEvent) {
        boolean z10;
        DecorContentParent decorContentParent;
        if (this.f420y != null) {
            return false;
        }
        boolean z11 = true;
        PanelFeatureState t02 = t0(i10, true);
        if (i10 == 0 && (decorContentParent = this.f417v) != null && decorContentParent.d() && !ViewConfiguration.get(this.f410o).hasPermanentMenuKey()) {
            if (!this.f417v.b()) {
                if (!this.V && P0(t02, keyEvent)) {
                    z11 = this.f417v.g();
                }
                z11 = false;
            } else {
                z11 = this.f417v.f();
            }
        } else {
            boolean z12 = t02.f436o;
            if (!z12 && !t02.f435n) {
                if (t02.f434m) {
                    if (t02.f439r) {
                        t02.f434m = false;
                        z10 = P0(t02, keyEvent);
                    } else {
                        z10 = true;
                    }
                    if (z10) {
                        M0(t02, keyEvent);
                    }
                }
                z11 = false;
            } else {
                c0(t02, true);
                z11 = z12;
            }
        }
        if (z11) {
            AudioManager audioManager = (AudioManager) this.f410o.getApplicationContext().getSystemService("audio");
            if (audioManager != null) {
                audioManager.playSoundEffect(0);
            } else {
                Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
        }
        return z11;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void M0(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        int i10;
        ViewGroup.LayoutParams layoutParams;
        if (panelFeatureState.f436o || this.V) {
            return;
        }
        if (panelFeatureState.f422a == 0) {
            if ((this.f410o.getResources().getConfiguration().screenLayout & 15) == 4) {
                return;
            }
        }
        Window.Callback v02 = v0();
        if (v02 != null && !v02.onMenuOpened(panelFeatureState.f422a, panelFeatureState.f431j)) {
            c0(panelFeatureState, true);
            return;
        }
        WindowManager windowManager = (WindowManager) this.f410o.getSystemService("window");
        if (windowManager == null || !P0(panelFeatureState, keyEvent)) {
            return;
        }
        ViewGroup viewGroup = panelFeatureState.f428g;
        if (viewGroup != null && !panelFeatureState.f438q) {
            View view = panelFeatureState.f430i;
            if (view != null && (layoutParams = view.getLayoutParams()) != null && layoutParams.width == -1) {
                i10 = -1;
                panelFeatureState.f435n = false;
                WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(i10, -2, panelFeatureState.f425d, panelFeatureState.f426e, DataTypeConstants.APP_LOG, 8519680, -3);
                layoutParams2.gravity = panelFeatureState.f424c;
                layoutParams2.windowAnimations = panelFeatureState.f427f;
                windowManager.addView(panelFeatureState.f428g, layoutParams2);
                panelFeatureState.f436o = true;
                if (panelFeatureState.f422a != 0) {
                    c1();
                    return;
                }
                return;
            }
        } else {
            if (viewGroup == null) {
                if (!y0(panelFeatureState) || panelFeatureState.f428g == null) {
                    return;
                }
            } else if (panelFeatureState.f438q && viewGroup.getChildCount() > 0) {
                panelFeatureState.f428g.removeAllViews();
            }
            if (x0(panelFeatureState) && panelFeatureState.b()) {
                ViewGroup.LayoutParams layoutParams3 = panelFeatureState.f429h.getLayoutParams();
                if (layoutParams3 == null) {
                    layoutParams3 = new ViewGroup.LayoutParams(-2, -2);
                }
                panelFeatureState.f428g.setBackgroundResource(panelFeatureState.f423b);
                ViewParent parent = panelFeatureState.f429h.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(panelFeatureState.f429h);
                }
                panelFeatureState.f428g.addView(panelFeatureState.f429h, layoutParams3);
                if (!panelFeatureState.f429h.hasFocus()) {
                    panelFeatureState.f429h.requestFocus();
                }
            } else {
                panelFeatureState.f438q = true;
                return;
            }
        }
        i10 = -2;
        panelFeatureState.f435n = false;
        WindowManager.LayoutParams layoutParams22 = new WindowManager.LayoutParams(i10, -2, panelFeatureState.f425d, panelFeatureState.f426e, DataTypeConstants.APP_LOG, 8519680, -3);
        layoutParams22.gravity = panelFeatureState.f424c;
        layoutParams22.windowAnimations = panelFeatureState.f427f;
        windowManager.addView(panelFeatureState.f428g, layoutParams22);
        panelFeatureState.f436o = true;
        if (panelFeatureState.f422a != 0) {
        }
    }

    private boolean O0(PanelFeatureState panelFeatureState, int i10, KeyEvent keyEvent, int i11) {
        MenuBuilder menuBuilder;
        boolean z10 = false;
        if (keyEvent.isSystem()) {
            return false;
        }
        if ((panelFeatureState.f434m || P0(panelFeatureState, keyEvent)) && (menuBuilder = panelFeatureState.f431j) != null) {
            z10 = menuBuilder.performShortcut(i10, keyEvent, i11);
        }
        if (z10 && (i11 & 1) == 0 && this.f417v == null) {
            c0(panelFeatureState, true);
        }
        return z10;
    }

    private boolean P0(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        DecorContentParent decorContentParent;
        DecorContentParent decorContentParent2;
        DecorContentParent decorContentParent3;
        if (this.V) {
            return false;
        }
        if (panelFeatureState.f434m) {
            return true;
        }
        PanelFeatureState panelFeatureState2 = this.R;
        if (panelFeatureState2 != null && panelFeatureState2 != panelFeatureState) {
            c0(panelFeatureState2, false);
        }
        Window.Callback v02 = v0();
        if (v02 != null) {
            panelFeatureState.f430i = v02.onCreatePanelView(panelFeatureState.f422a);
        }
        int i10 = panelFeatureState.f422a;
        boolean z10 = i10 == 0 || i10 == 108;
        if (z10 && (decorContentParent3 = this.f417v) != null) {
            decorContentParent3.c();
        }
        if (panelFeatureState.f430i == null && (!z10 || !(N0() instanceof ToolbarActionBar))) {
            MenuBuilder menuBuilder = panelFeatureState.f431j;
            if (menuBuilder == null || panelFeatureState.f439r) {
                if (menuBuilder == null && (!z0(panelFeatureState) || panelFeatureState.f431j == null)) {
                    return false;
                }
                if (z10 && this.f417v != null) {
                    if (this.f418w == null) {
                        this.f418w = new h();
                    }
                    this.f417v.a(panelFeatureState.f431j, this.f418w);
                }
                panelFeatureState.f431j.stopDispatchingItemsChanged();
                if (!v02.onCreatePanelMenu(panelFeatureState.f422a, panelFeatureState.f431j)) {
                    panelFeatureState.c(null);
                    if (z10 && (decorContentParent = this.f417v) != null) {
                        decorContentParent.a(null, this.f418w);
                    }
                    return false;
                }
                panelFeatureState.f439r = false;
            }
            panelFeatureState.f431j.stopDispatchingItemsChanged();
            Bundle bundle = panelFeatureState.f440s;
            if (bundle != null) {
                panelFeatureState.f431j.restoreActionViewStates(bundle);
                panelFeatureState.f440s = null;
            }
            if (!v02.onPreparePanel(0, panelFeatureState.f430i, panelFeatureState.f431j)) {
                if (z10 && (decorContentParent2 = this.f417v) != null) {
                    decorContentParent2.a(null, this.f418w);
                }
                panelFeatureState.f431j.startDispatchingItemsChanged();
                return false;
            }
            boolean z11 = KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1;
            panelFeatureState.f437p = z11;
            panelFeatureState.f431j.setQwertyMode(z11);
            panelFeatureState.f431j.startDispatchingItemsChanged();
        }
        panelFeatureState.f434m = true;
        panelFeatureState.f435n = false;
        this.R = panelFeatureState;
        return true;
    }

    private void Q0(boolean z10) {
        DecorContentParent decorContentParent = this.f417v;
        if (decorContentParent != null && decorContentParent.d() && (!ViewConfiguration.get(this.f410o).hasPermanentMenuKey() || this.f417v.e())) {
            Window.Callback v02 = v0();
            if (this.f417v.b() && z10) {
                this.f417v.f();
                if (this.V) {
                    return;
                }
                v02.onPanelClosed(108, t0(0, true).f431j);
                return;
            }
            if (v02 == null || this.V) {
                return;
            }
            if (this.f399d0 && (this.f400e0 & 1) != 0) {
                this.f411p.getDecorView().removeCallbacks(this.f401f0);
                this.f401f0.run();
            }
            PanelFeatureState t02 = t0(0, true);
            MenuBuilder menuBuilder = t02.f431j;
            if (menuBuilder == null || t02.f439r || !v02.onPreparePanel(0, t02.f430i, menuBuilder)) {
                return;
            }
            v02.onMenuOpened(108, t02.f431j);
            this.f417v.g();
            return;
        }
        PanelFeatureState t03 = t0(0, true);
        t03.f438q = true;
        c0(t03, false);
        M0(t03, null);
    }

    private boolean R(boolean z10) {
        return S(z10, true);
    }

    private int R0(int i10) {
        if (i10 == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (i10 != 9) {
            return i10;
        }
        Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
        return 109;
    }

    private boolean S(boolean z10, boolean z11) {
        if (this.V) {
            return false;
        }
        int X = X();
        int C0 = C0(this.f410o, X);
        LocaleListCompat W = Build.VERSION.SDK_INT < 33 ? W(this.f410o) : null;
        if (!z11 && W != null) {
            W = s0(this.f410o.getResources().getConfiguration());
        }
        boolean b12 = b1(C0, W, z10);
        if (X == 0) {
            r0(this.f410o).e();
        } else {
            q qVar = this.f397b0;
            if (qVar != null) {
                qVar.a();
            }
        }
        if (X == 3) {
            q0(this.f410o).e();
        } else {
            q qVar2 = this.f398c0;
            if (qVar2 != null) {
                qVar2.a();
            }
        }
        return b12;
    }

    private void U() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) this.F.findViewById(R.id.content);
        View decorView = this.f411p.getDecorView();
        contentFrameLayout.a(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        TypedArray obtainStyledAttributes = this.f410o.obtainStyledAttributes(R$styleable.AppCompatTheme);
        obtainStyledAttributes.getValue(R$styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        obtainStyledAttributes.getValue(R$styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        int i10 = R$styleable.AppCompatTheme_windowFixedWidthMajor;
        if (obtainStyledAttributes.hasValue(i10)) {
            obtainStyledAttributes.getValue(i10, contentFrameLayout.getFixedWidthMajor());
        }
        int i11 = R$styleable.AppCompatTheme_windowFixedWidthMinor;
        if (obtainStyledAttributes.hasValue(i11)) {
            obtainStyledAttributes.getValue(i11, contentFrameLayout.getFixedWidthMinor());
        }
        int i12 = R$styleable.AppCompatTheme_windowFixedHeightMajor;
        if (obtainStyledAttributes.hasValue(i12)) {
            obtainStyledAttributes.getValue(i12, contentFrameLayout.getFixedHeightMajor());
        }
        int i13 = R$styleable.AppCompatTheme_windowFixedHeightMinor;
        if (obtainStyledAttributes.hasValue(i13)) {
            obtainStyledAttributes.getValue(i13, contentFrameLayout.getFixedHeightMinor());
        }
        obtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }

    private void V(Window window) {
        if (this.f411p == null) {
            Window.Callback callback = window.getCallback();
            if (!(callback instanceof o)) {
                o oVar = new o(callback);
                this.f412q = oVar;
                window.setCallback(oVar);
                TintTypedArray v7 = TintTypedArray.v(this.f410o, null, f393p0);
                Drawable h10 = v7.h(0);
                if (h10 != null) {
                    window.setBackgroundDrawable(h10);
                }
                v7.x();
                this.f411p = window;
                if (Build.VERSION.SDK_INT < 33 || this.f407l0 != null) {
                    return;
                }
                L(null);
                return;
            }
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        throw new IllegalStateException("AppCompat has already installed itself into the Window");
    }

    private boolean V0(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View decorView = this.f411p.getDecorView();
        while (viewParent != null) {
            if (viewParent == decorView || !(viewParent instanceof View) || ViewCompat.P((View) viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
        }
        return true;
    }

    private int X() {
        int i10 = this.X;
        return i10 != -100 ? i10 : AppCompatDelegate.m();
    }

    private void Y0() {
        if (this.E) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private AppCompatActivity Z0() {
        for (Context context = this.f410o; context != null; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }
            if (!(context instanceof ContextWrapper)) {
                break;
            }
        }
        return null;
    }

    private void a0() {
        q qVar = this.f397b0;
        if (qVar != null) {
            qVar.a();
        }
        q qVar2 = this.f398c0;
        if (qVar2 != null) {
            qVar2.a();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a1(Configuration configuration) {
        Activity activity = (Activity) this.f409n;
        if (activity instanceof androidx.lifecycle.o) {
            if (((androidx.lifecycle.o) activity).getLifecycle().b().a(h.c.CREATED)) {
                activity.onConfigurationChanged(configuration);
            }
        } else {
            if (!this.U || this.V) {
                return;
            }
            activity.onConfigurationChanged(configuration);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0082  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean b1(int i10, LocaleListCompat localeListCompat, boolean z10) {
        boolean z11;
        Configuration d02 = d0(this.f410o, i10, localeListCompat, null, false);
        int p02 = p0(this.f410o);
        Configuration configuration = this.W;
        if (configuration == null) {
            configuration = this.f410o.getResources().getConfiguration();
        }
        int i11 = configuration.uiMode & 48;
        int i12 = d02.uiMode & 48;
        LocaleListCompat s02 = s0(configuration);
        LocaleListCompat s03 = localeListCompat == null ? null : s0(d02);
        int i13 = i11 != i12 ? 512 : 0;
        if (s03 != null && !s02.equals(s03)) {
            i13 = i13 | 4 | 8192;
        }
        boolean z12 = true;
        if (((~p02) & i13) != 0 && z10 && this.T && (f394q0 || this.U)) {
            Object obj = this.f409n;
            if ((obj instanceof Activity) && !((Activity) obj).isChild()) {
                ActivityCompat.m((Activity) this.f409n);
                z11 = true;
                if (!z11 || i13 == 0) {
                    z12 = z11;
                } else {
                    d1(i12, s03, (i13 & p02) == i13, null);
                }
                if (z12) {
                    Object obj2 = this.f409n;
                    if (obj2 instanceof AppCompatActivity) {
                        if ((i13 & 512) != 0) {
                            ((AppCompatActivity) obj2).onNightModeChanged(i10);
                        }
                        if ((i13 & 4) != 0) {
                            ((AppCompatActivity) this.f409n).onLocalesChanged(localeListCompat);
                        }
                    }
                }
                if (z12 && s03 != null) {
                    T0(s0(this.f410o.getResources().getConfiguration()));
                }
                return z12;
            }
        }
        z11 = false;
        if (z11) {
        }
        z12 = z11;
        if (z12) {
        }
        if (z12) {
            T0(s0(this.f410o.getResources().getConfiguration()));
        }
        return z12;
    }

    private Configuration d0(Context context, int i10, LocaleListCompat localeListCompat, Configuration configuration, boolean z10) {
        int i11;
        if (i10 == 1) {
            i11 = 16;
        } else if (i10 != 2) {
            i11 = z10 ? 0 : context.getApplicationContext().getResources().getConfiguration().uiMode & 48;
        } else {
            i11 = 32;
        }
        Configuration configuration2 = new Configuration();
        configuration2.fontScale = 0.0f;
        if (configuration != null) {
            configuration2.setTo(configuration);
        }
        configuration2.uiMode = i11 | (configuration2.uiMode & (-49));
        if (localeListCompat != null) {
            S0(configuration2, localeListCompat);
        }
        return configuration2;
    }

    private void d1(int i10, LocaleListCompat localeListCompat, boolean z10, Configuration configuration) {
        Resources resources = this.f410o.getResources();
        Configuration configuration2 = new Configuration(resources.getConfiguration());
        if (configuration != null) {
            configuration2.updateFrom(configuration);
        }
        configuration2.uiMode = i10 | (resources.getConfiguration().uiMode & (-49));
        if (localeListCompat != null) {
            S0(configuration2, localeListCompat);
        }
        resources.updateConfiguration(configuration2, null);
        int i11 = this.Y;
        if (i11 != 0) {
            this.f410o.setTheme(i11);
            this.f410o.getTheme().applyStyle(this.Y, true);
        }
        if (z10 && (this.f409n instanceof Activity)) {
            a1(configuration2);
        }
    }

    private ViewGroup e0() {
        ViewGroup viewGroup;
        Context context;
        TypedArray obtainStyledAttributes = this.f410o.obtainStyledAttributes(R$styleable.AppCompatTheme);
        int i10 = R$styleable.AppCompatTheme_windowActionBar;
        if (obtainStyledAttributes.hasValue(i10)) {
            if (obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_windowNoTitle, false)) {
                H(1);
            } else if (obtainStyledAttributes.getBoolean(i10, false)) {
                H(108);
            }
            if (obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_windowActionBarOverlay, false)) {
                H(109);
            }
            if (obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_windowActionModeOverlay, false)) {
                H(10);
            }
            this.N = obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_android_windowIsFloating, false);
            obtainStyledAttributes.recycle();
            l0();
            this.f411p.getDecorView();
            LayoutInflater from = LayoutInflater.from(this.f410o);
            if (!this.O) {
                if (this.N) {
                    viewGroup = (ViewGroup) from.inflate(R$layout.abc_dialog_title_material, (ViewGroup) null);
                    this.L = false;
                    this.K = false;
                } else if (this.K) {
                    TypedValue typedValue = new TypedValue();
                    this.f410o.getTheme().resolveAttribute(R$attr.actionBarTheme, typedValue, true);
                    if (typedValue.resourceId != 0) {
                        context = new ContextThemeWrapper(this.f410o, typedValue.resourceId);
                    } else {
                        context = this.f410o;
                    }
                    viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R$layout.abc_screen_toolbar, (ViewGroup) null);
                    DecorContentParent decorContentParent = (DecorContentParent) viewGroup.findViewById(R$id.decor_content_parent);
                    this.f417v = decorContentParent;
                    decorContentParent.setWindowCallback(v0());
                    if (this.L) {
                        this.f417v.h(109);
                    }
                    if (this.I) {
                        this.f417v.h(2);
                    }
                    if (this.J) {
                        this.f417v.h(5);
                    }
                } else {
                    viewGroup = null;
                }
            } else {
                viewGroup = this.M ? (ViewGroup) from.inflate(R$layout.abc_screen_simple_overlay_action_mode, (ViewGroup) null) : (ViewGroup) from.inflate(R$layout.abc_screen_simple, (ViewGroup) null);
            }
            if (viewGroup != null) {
                ViewCompat.z0(viewGroup, new b());
                if (this.f417v == null) {
                    this.G = (TextView) viewGroup.findViewById(R$id.title);
                }
                n0.c(viewGroup);
                ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(R$id.action_bar_activity_content);
                ViewGroup viewGroup2 = (ViewGroup) this.f411p.findViewById(R.id.content);
                if (viewGroup2 != null) {
                    while (viewGroup2.getChildCount() > 0) {
                        View childAt = viewGroup2.getChildAt(0);
                        viewGroup2.removeViewAt(0);
                        contentFrameLayout.addView(childAt);
                    }
                    viewGroup2.setId(-1);
                    contentFrameLayout.setId(R.id.content);
                    if (viewGroup2 instanceof FrameLayout) {
                        ((FrameLayout) viewGroup2).setForeground(null);
                    }
                }
                this.f411p.setContentView(viewGroup);
                contentFrameLayout.setAttachListener(new c());
                return viewGroup;
            }
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.K + ", windowActionBarOverlay: " + this.L + ", android:windowIsFloating: " + this.N + ", windowActionModeOverlay: " + this.M + ", windowNoTitle: " + this.O + " }");
        }
        obtainStyledAttributes.recycle();
        throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
    }

    private void f1(View view) {
        int c10;
        if ((ViewCompat.I(view) & 8192) != 0) {
            c10 = ContextCompat.c(this.f410o, R$color.abc_decor_view_status_guard_light);
        } else {
            c10 = ContextCompat.c(this.f410o, R$color.abc_decor_view_status_guard);
        }
        view.setBackgroundColor(c10);
    }

    private void k0() {
        if (this.E) {
            return;
        }
        this.F = e0();
        CharSequence u02 = u0();
        if (!TextUtils.isEmpty(u02)) {
            DecorContentParent decorContentParent = this.f417v;
            if (decorContentParent != null) {
                decorContentParent.setWindowTitle(u02);
            } else if (N0() != null) {
                N0().u(u02);
            } else {
                TextView textView = this.G;
                if (textView != null) {
                    textView.setText(u02);
                }
            }
        }
        U();
        L0(this.F);
        this.E = true;
        PanelFeatureState t02 = t0(0, false);
        if (this.V) {
            return;
        }
        if (t02 == null || t02.f431j == null) {
            A0(108);
        }
    }

    private void l0() {
        if (this.f411p == null) {
            Object obj = this.f409n;
            if (obj instanceof Activity) {
                V(((Activity) obj).getWindow());
            }
        }
        if (this.f411p == null) {
            throw new IllegalStateException("We have not been given a Window");
        }
    }

    private static Configuration n0(Configuration configuration, Configuration configuration2) {
        Configuration configuration3 = new Configuration();
        configuration3.fontScale = 0.0f;
        if (configuration2 != null && configuration.diff(configuration2) != 0) {
            float f10 = configuration.fontScale;
            float f11 = configuration2.fontScale;
            if (f10 != f11) {
                configuration3.fontScale = f11;
            }
            int i10 = configuration.mcc;
            int i11 = configuration2.mcc;
            if (i10 != i11) {
                configuration3.mcc = i11;
            }
            int i12 = configuration.mnc;
            int i13 = configuration2.mnc;
            if (i12 != i13) {
                configuration3.mnc = i13;
            }
            l.a(configuration, configuration2, configuration3);
            int i14 = configuration.touchscreen;
            int i15 = configuration2.touchscreen;
            if (i14 != i15) {
                configuration3.touchscreen = i15;
            }
            int i16 = configuration.keyboard;
            int i17 = configuration2.keyboard;
            if (i16 != i17) {
                configuration3.keyboard = i17;
            }
            int i18 = configuration.keyboardHidden;
            int i19 = configuration2.keyboardHidden;
            if (i18 != i19) {
                configuration3.keyboardHidden = i19;
            }
            int i20 = configuration.navigation;
            int i21 = configuration2.navigation;
            if (i20 != i21) {
                configuration3.navigation = i21;
            }
            int i22 = configuration.navigationHidden;
            int i23 = configuration2.navigationHidden;
            if (i22 != i23) {
                configuration3.navigationHidden = i23;
            }
            int i24 = configuration.orientation;
            int i25 = configuration2.orientation;
            if (i24 != i25) {
                configuration3.orientation = i25;
            }
            int i26 = configuration.screenLayout & 15;
            int i27 = configuration2.screenLayout;
            if (i26 != (i27 & 15)) {
                configuration3.screenLayout |= i27 & 15;
            }
            int i28 = configuration.screenLayout & 192;
            int i29 = configuration2.screenLayout;
            if (i28 != (i29 & 192)) {
                configuration3.screenLayout |= i29 & 192;
            }
            int i30 = configuration.screenLayout & 48;
            int i31 = configuration2.screenLayout;
            if (i30 != (i31 & 48)) {
                configuration3.screenLayout |= i31 & 48;
            }
            int i32 = configuration.screenLayout & 768;
            int i33 = configuration2.screenLayout;
            if (i32 != (i33 & 768)) {
                configuration3.screenLayout |= i33 & 768;
            }
            m.a(configuration, configuration2, configuration3);
            int i34 = configuration.uiMode & 15;
            int i35 = configuration2.uiMode;
            if (i34 != (i35 & 15)) {
                configuration3.uiMode |= i35 & 15;
            }
            int i36 = configuration.uiMode & 48;
            int i37 = configuration2.uiMode;
            if (i36 != (i37 & 48)) {
                configuration3.uiMode |= i37 & 48;
            }
            int i38 = configuration.screenWidthDp;
            int i39 = configuration2.screenWidthDp;
            if (i38 != i39) {
                configuration3.screenWidthDp = i39;
            }
            int i40 = configuration.screenHeightDp;
            int i41 = configuration2.screenHeightDp;
            if (i40 != i41) {
                configuration3.screenHeightDp = i41;
            }
            int i42 = configuration.smallestScreenWidthDp;
            int i43 = configuration2.smallestScreenWidthDp;
            if (i42 != i43) {
                configuration3.smallestScreenWidthDp = i43;
            }
            j.b(configuration, configuration2, configuration3);
        }
        return configuration3;
    }

    private int p0(Context context) {
        if (!this.f396a0 && (this.f409n instanceof Activity)) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return 0;
            }
            try {
                ActivityInfo activityInfo = packageManager.getActivityInfo(new ComponentName(context, this.f409n.getClass()), 269221888);
                if (activityInfo != null) {
                    this.Z = activityInfo.configChanges;
                }
            } catch (PackageManager.NameNotFoundException e10) {
                Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e10);
                this.Z = 0;
            }
        }
        this.f396a0 = true;
        return this.Z;
    }

    private q q0(Context context) {
        if (this.f398c0 == null) {
            this.f398c0 = new p(context);
        }
        return this.f398c0;
    }

    private q r0(Context context) {
        if (this.f397b0 == null) {
            this.f397b0 = new r(TwilightManager.a(context));
        }
        return this.f397b0;
    }

    private void w0() {
        k0();
        if (this.K && this.f414s == null) {
            Object obj = this.f409n;
            if (obj instanceof Activity) {
                this.f414s = new WindowDecorActionBar((Activity) this.f409n, this.L);
            } else if (obj instanceof Dialog) {
                this.f414s = new WindowDecorActionBar((Dialog) this.f409n);
            }
            ActionBar actionBar = this.f414s;
            if (actionBar != null) {
                actionBar.r(this.f402g0);
            }
        }
    }

    private boolean x0(PanelFeatureState panelFeatureState) {
        View view = panelFeatureState.f430i;
        if (view != null) {
            panelFeatureState.f429h = view;
            return true;
        }
        if (panelFeatureState.f431j == null) {
            return false;
        }
        if (this.f419x == null) {
            this.f419x = new u();
        }
        View view2 = (View) panelFeatureState.a(this.f419x);
        panelFeatureState.f429h = view2;
        return view2 != null;
    }

    private boolean y0(PanelFeatureState panelFeatureState) {
        panelFeatureState.d(o0());
        panelFeatureState.f428g = new t(panelFeatureState.f433l);
        panelFeatureState.f424c = 81;
        return true;
    }

    private boolean z0(PanelFeatureState panelFeatureState) {
        Context context = this.f410o;
        int i10 = panelFeatureState.f422a;
        if ((i10 == 0 || i10 == 108) && this.f417v != null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R$attr.actionBarTheme, typedValue, true);
            Resources.Theme theme2 = null;
            if (typedValue.resourceId != 0) {
                theme2 = context.getResources().newTheme();
                theme2.setTo(theme);
                theme2.applyStyle(typedValue.resourceId, true);
                theme2.resolveAttribute(R$attr.actionBarWidgetTheme, typedValue, true);
            } else {
                theme.resolveAttribute(R$attr.actionBarWidgetTheme, typedValue, true);
            }
            if (typedValue.resourceId != 0) {
                if (theme2 == null) {
                    theme2 = context.getResources().newTheme();
                    theme2.setTo(theme);
                }
                theme2.applyStyle(typedValue.resourceId, true);
            }
            if (theme2 != null) {
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
                contextThemeWrapper.getTheme().setTo(theme2);
                context = contextThemeWrapper;
            }
        }
        MenuBuilder menuBuilder = new MenuBuilder(context);
        menuBuilder.setCallback(this);
        panelFeatureState.c(menuBuilder);
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void A(Bundle bundle) {
        k0();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void B() {
        ActionBar s7 = s();
        if (s7 != null) {
            s7.t(true);
        }
    }

    public boolean B0() {
        return this.D;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void C(Bundle bundle) {
    }

    int C0(Context context, int i10) {
        if (i10 == -100) {
            return -1;
        }
        if (i10 != -1) {
            if (i10 == 0) {
                if (((UiModeManager) context.getApplicationContext().getSystemService("uimode")).getNightMode() == 0) {
                    return -1;
                }
                return r0(context).c();
            }
            if (i10 != 1 && i10 != 2) {
                if (i10 == 3) {
                    return q0(context).c();
                }
                throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
            }
        }
        return i10;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void D() {
        S(true, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean D0() {
        boolean z10 = this.S;
        this.S = false;
        PanelFeatureState t02 = t0(0, false);
        if (t02 != null && t02.f436o) {
            if (!z10) {
                c0(t02, true);
            }
            return true;
        }
        androidx.appcompat.view.ActionMode actionMode = this.f420y;
        if (actionMode != null) {
            actionMode.c();
            return true;
        }
        ActionBar s7 = s();
        return s7 != null && s7.h();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void E() {
        ActionBar s7 = s();
        if (s7 != null) {
            s7.t(false);
        }
    }

    boolean E0(int i10, KeyEvent keyEvent) {
        if (i10 == 4) {
            this.S = (keyEvent.getFlags() & 128) != 0;
        } else if (i10 == 82) {
            F0(0, keyEvent);
            return true;
        }
        return false;
    }

    boolean G0(int i10, KeyEvent keyEvent) {
        ActionBar s7 = s();
        if (s7 != null && s7.o(i10, keyEvent)) {
            return true;
        }
        PanelFeatureState panelFeatureState = this.R;
        if (panelFeatureState != null && O0(panelFeatureState, keyEvent.getKeyCode(), keyEvent, 1)) {
            PanelFeatureState panelFeatureState2 = this.R;
            if (panelFeatureState2 != null) {
                panelFeatureState2.f435n = true;
            }
            return true;
        }
        if (this.R == null) {
            PanelFeatureState t02 = t0(0, true);
            P0(t02, keyEvent);
            boolean O0 = O0(t02, keyEvent.getKeyCode(), keyEvent, 1);
            t02.f434m = false;
            if (O0) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public boolean H(int i10) {
        int R0 = R0(i10);
        if (this.O && R0 == 108) {
            return false;
        }
        if (this.K && R0 == 1) {
            this.K = false;
        }
        if (R0 == 1) {
            Y0();
            this.O = true;
            return true;
        }
        if (R0 == 2) {
            Y0();
            this.I = true;
            return true;
        }
        if (R0 == 5) {
            Y0();
            this.J = true;
            return true;
        }
        if (R0 == 10) {
            Y0();
            this.M = true;
            return true;
        }
        if (R0 == 108) {
            Y0();
            this.K = true;
            return true;
        }
        if (R0 != 109) {
            return this.f411p.requestFeature(R0);
        }
        Y0();
        this.L = true;
        return true;
    }

    boolean H0(int i10, KeyEvent keyEvent) {
        if (i10 != 4) {
            if (i10 == 82) {
                I0(0, keyEvent);
                return true;
            }
        } else if (D0()) {
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void I(int i10) {
        k0();
        ViewGroup viewGroup = (ViewGroup) this.F.findViewById(R.id.content);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.f410o).inflate(i10, viewGroup);
        this.f412q.c(this.f411p.getCallback());
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void J(View view) {
        k0();
        ViewGroup viewGroup = (ViewGroup) this.F.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.f412q.c(this.f411p.getCallback());
    }

    void J0(int i10) {
        ActionBar s7;
        if (i10 != 108 || (s7 = s()) == null) {
            return;
        }
        s7.i(true);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void K(View view, ViewGroup.LayoutParams layoutParams) {
        k0();
        ViewGroup viewGroup = (ViewGroup) this.F.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.f412q.c(this.f411p.getCallback());
    }

    void K0(int i10) {
        if (i10 == 108) {
            ActionBar s7 = s();
            if (s7 != null) {
                s7.i(false);
                return;
            }
            return;
        }
        if (i10 == 0) {
            PanelFeatureState t02 = t0(i10, true);
            if (t02.f436o) {
                c0(t02, false);
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void L(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        OnBackInvokedCallback onBackInvokedCallback;
        super.L(onBackInvokedDispatcher);
        OnBackInvokedDispatcher onBackInvokedDispatcher2 = this.f407l0;
        if (onBackInvokedDispatcher2 != null && (onBackInvokedCallback = this.f408m0) != null) {
            n.c(onBackInvokedDispatcher2, onBackInvokedCallback);
            this.f408m0 = null;
        }
        if (onBackInvokedDispatcher == null) {
            Object obj = this.f409n;
            if ((obj instanceof Activity) && ((Activity) obj).getWindow() != null) {
                this.f407l0 = n.a((Activity) this.f409n);
                c1();
            }
        }
        this.f407l0 = onBackInvokedDispatcher;
        c1();
    }

    void L0(ViewGroup viewGroup) {
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void M(Toolbar toolbar) {
        if (this.f409n instanceof Activity) {
            ActionBar s7 = s();
            if (!(s7 instanceof WindowDecorActionBar)) {
                this.f415t = null;
                if (s7 != null) {
                    s7.n();
                }
                this.f414s = null;
                if (toolbar != null) {
                    ToolbarActionBar toolbarActionBar = new ToolbarActionBar(toolbar, u0(), this.f412q);
                    this.f414s = toolbarActionBar;
                    this.f412q.e(toolbarActionBar.f499c);
                    toolbar.setBackInvokedCallbackEnabled(true);
                } else {
                    this.f412q.e(null);
                }
                u();
                return;
            }
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void N(int i10) {
        this.Y = i10;
    }

    final ActionBar N0() {
        return this.f414s;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void O(CharSequence charSequence) {
        this.f416u = charSequence;
        DecorContentParent decorContentParent = this.f417v;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(charSequence);
            return;
        }
        if (N0() != null) {
            N0().u(charSequence);
            return;
        }
        TextView textView = this.G;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public androidx.appcompat.view.ActionMode P(ActionMode.a aVar) {
        AppCompatCallback appCompatCallback;
        if (aVar != null) {
            androidx.appcompat.view.ActionMode actionMode = this.f420y;
            if (actionMode != null) {
                actionMode.c();
            }
            i iVar = new i(aVar);
            ActionBar s7 = s();
            if (s7 != null) {
                androidx.appcompat.view.ActionMode v7 = s7.v(iVar);
                this.f420y = v7;
                if (v7 != null && (appCompatCallback = this.f413r) != null) {
                    appCompatCallback.onSupportActionModeStarted(v7);
                }
            }
            if (this.f420y == null) {
                this.f420y = X0(iVar);
            }
            c1();
            return this.f420y;
        }
        throw new IllegalArgumentException("ActionMode callback can not be null.");
    }

    void S0(Configuration configuration, LocaleListCompat localeListCompat) {
        l.d(configuration, localeListCompat);
    }

    public boolean T() {
        return R(true);
    }

    void T0(LocaleListCompat localeListCompat) {
        l.c(localeListCompat);
    }

    final boolean U0() {
        ViewGroup viewGroup;
        return this.E && (viewGroup = this.F) != null && ViewCompat.Q(viewGroup);
    }

    LocaleListCompat W(Context context) {
        LocaleListCompat r10;
        if (Build.VERSION.SDK_INT >= 33 || (r10 = AppCompatDelegate.r()) == null) {
            return null;
        }
        LocaleListCompat s02 = s0(context.getApplicationContext().getResources().getConfiguration());
        LocaleListCompat b10 = LocaleOverlayHelper.b(r10, s02);
        return b10.e() ? s02 : b10;
    }

    boolean W0() {
        if (this.f407l0 == null) {
            return false;
        }
        PanelFeatureState t02 = t0(0, false);
        return (t02 != null && t02.f436o) || this.f420y != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    androidx.appcompat.view.ActionMode X0(ActionMode.a aVar) {
        androidx.appcompat.view.ActionMode actionMode;
        Context context;
        androidx.appcompat.view.ActionMode actionMode2;
        AppCompatCallback appCompatCallback;
        j0();
        androidx.appcompat.view.ActionMode actionMode3 = this.f420y;
        if (actionMode3 != null) {
            actionMode3.c();
        }
        if (!(aVar instanceof i)) {
            aVar = new i(aVar);
        }
        AppCompatCallback appCompatCallback2 = this.f413r;
        if (appCompatCallback2 != null && !this.V) {
            try {
                actionMode = appCompatCallback2.onWindowStartingSupportActionMode(aVar);
            } catch (AbstractMethodError unused) {
            }
            if (actionMode == null) {
                this.f420y = actionMode;
            } else {
                if (this.f421z == null) {
                    if (this.N) {
                        TypedValue typedValue = new TypedValue();
                        Resources.Theme theme = this.f410o.getTheme();
                        theme.resolveAttribute(R$attr.actionBarTheme, typedValue, true);
                        if (typedValue.resourceId != 0) {
                            Resources.Theme newTheme = this.f410o.getResources().newTheme();
                            newTheme.setTo(theme);
                            newTheme.applyStyle(typedValue.resourceId, true);
                            context = new ContextThemeWrapper(this.f410o, 0);
                            context.getTheme().setTo(newTheme);
                        } else {
                            context = this.f410o;
                        }
                        this.f421z = new ActionBarContextView(context);
                        PopupWindow popupWindow = new PopupWindow(context, (AttributeSet) null, R$attr.actionModePopupWindowStyle);
                        this.A = popupWindow;
                        PopupWindowCompat.b(popupWindow, 2);
                        this.A.setContentView(this.f421z);
                        this.A.setWidth(-1);
                        context.getTheme().resolveAttribute(R$attr.actionBarSize, typedValue, true);
                        this.f421z.setContentHeight(TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics()));
                        this.A.setHeight(-2);
                        this.B = new d();
                    } else {
                        ViewStubCompat viewStubCompat = (ViewStubCompat) this.F.findViewById(R$id.action_mode_bar_stub);
                        if (viewStubCompat != null) {
                            viewStubCompat.setLayoutInflater(LayoutInflater.from(o0()));
                            this.f421z = (ActionBarContextView) viewStubCompat.a();
                        }
                    }
                }
                if (this.f421z != null) {
                    j0();
                    this.f421z.k();
                    StandaloneActionMode standaloneActionMode = new StandaloneActionMode(this.f421z.getContext(), this.f421z, aVar, this.A == null);
                    if (aVar.b(standaloneActionMode, standaloneActionMode.e())) {
                        standaloneActionMode.k();
                        this.f421z.h(standaloneActionMode);
                        this.f420y = standaloneActionMode;
                        if (U0()) {
                            this.f421z.setAlpha(0.0f);
                            ViewPropertyAnimatorCompat b10 = ViewCompat.d(this.f421z).b(1.0f);
                            this.C = b10;
                            b10.i(new e());
                        } else {
                            this.f421z.setAlpha(1.0f);
                            this.f421z.setVisibility(0);
                            if (this.f421z.getParent() instanceof View) {
                                ViewCompat.h0((View) this.f421z.getParent());
                            }
                        }
                        if (this.A != null) {
                            this.f411p.getDecorView().post(this.B);
                        }
                    } else {
                        this.f420y = null;
                    }
                }
            }
            actionMode2 = this.f420y;
            if (actionMode2 != null && (appCompatCallback = this.f413r) != null) {
                appCompatCallback.onSupportActionModeStarted(actionMode2);
            }
            c1();
            return this.f420y;
        }
        actionMode = null;
        if (actionMode == null) {
        }
        actionMode2 = this.f420y;
        if (actionMode2 != null) {
            appCompatCallback.onSupportActionModeStarted(actionMode2);
        }
        c1();
        return this.f420y;
    }

    void Y(int i10, PanelFeatureState panelFeatureState, Menu menu) {
        if (menu == null) {
            if (panelFeatureState == null && i10 >= 0) {
                PanelFeatureState[] panelFeatureStateArr = this.Q;
                if (i10 < panelFeatureStateArr.length) {
                    panelFeatureState = panelFeatureStateArr[i10];
                }
            }
            if (panelFeatureState != null) {
                menu = panelFeatureState.f431j;
            }
        }
        if ((panelFeatureState == null || panelFeatureState.f436o) && !this.V) {
            this.f412q.d(this.f411p.getCallback(), i10, menu);
        }
    }

    void Z(MenuBuilder menuBuilder) {
        if (this.P) {
            return;
        }
        this.P = true;
        this.f417v.i();
        Window.Callback v02 = v0();
        if (v02 != null && !this.V) {
            v02.onPanelClosed(108, menuBuilder);
        }
        this.P = false;
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.a
    public boolean a(MenuBuilder menuBuilder, MenuItem menuItem) {
        PanelFeatureState m02;
        Window.Callback v02 = v0();
        if (v02 == null || this.V || (m02 = m0(menuBuilder.getRootMenu())) == null) {
            return false;
        }
        return v02.onMenuItemSelected(m02.f422a, menuItem);
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.a
    public void b(MenuBuilder menuBuilder) {
        Q0(true);
    }

    void b0(int i10) {
        c0(t0(i10, true), true);
    }

    void c0(PanelFeatureState panelFeatureState, boolean z10) {
        ViewGroup viewGroup;
        DecorContentParent decorContentParent;
        if (z10 && panelFeatureState.f422a == 0 && (decorContentParent = this.f417v) != null && decorContentParent.b()) {
            Z(panelFeatureState.f431j);
            return;
        }
        WindowManager windowManager = (WindowManager) this.f410o.getSystemService("window");
        if (windowManager != null && panelFeatureState.f436o && (viewGroup = panelFeatureState.f428g) != null) {
            windowManager.removeView(viewGroup);
            if (z10) {
                Y(panelFeatureState.f422a, panelFeatureState, null);
            }
        }
        panelFeatureState.f434m = false;
        panelFeatureState.f435n = false;
        panelFeatureState.f436o = false;
        panelFeatureState.f429h = null;
        panelFeatureState.f438q = true;
        if (this.R == panelFeatureState) {
            this.R = null;
        }
        if (panelFeatureState.f422a == 0) {
            c1();
        }
    }

    void c1() {
        OnBackInvokedCallback onBackInvokedCallback;
        if (Build.VERSION.SDK_INT >= 33) {
            boolean W0 = W0();
            if (W0 && this.f408m0 == null) {
                this.f408m0 = n.b(this.f407l0, this);
            } else {
                if (W0 || (onBackInvokedCallback = this.f408m0) == null) {
                    return;
                }
                n.c(this.f407l0, onBackInvokedCallback);
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void e(View view, ViewGroup.LayoutParams layoutParams) {
        k0();
        ((ViewGroup) this.F.findViewById(R.id.content)).addView(view, layoutParams);
        this.f412q.c(this.f411p.getCallback());
    }

    final int e1(WindowInsetsCompat windowInsetsCompat, Rect rect) {
        int i10;
        boolean z10;
        boolean z11;
        if (windowInsetsCompat != null) {
            i10 = windowInsetsCompat.l();
        } else {
            i10 = rect != null ? rect.top : 0;
        }
        ActionBarContextView actionBarContextView = this.f421z;
        if (actionBarContextView == null || !(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            z10 = false;
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f421z.getLayoutParams();
            if (this.f421z.isShown()) {
                if (this.f403h0 == null) {
                    this.f403h0 = new Rect();
                    this.f404i0 = new Rect();
                }
                Rect rect2 = this.f403h0;
                Rect rect3 = this.f404i0;
                if (windowInsetsCompat == null) {
                    rect2.set(rect);
                } else {
                    rect2.set(windowInsetsCompat.j(), windowInsetsCompat.l(), windowInsetsCompat.k(), windowInsetsCompat.i());
                }
                n0.a(this.F, rect2, rect3);
                int i11 = rect2.top;
                int i12 = rect2.left;
                int i13 = rect2.right;
                WindowInsetsCompat E = ViewCompat.E(this.F);
                int j10 = E == null ? 0 : E.j();
                int k10 = E == null ? 0 : E.k();
                if (marginLayoutParams.topMargin == i11 && marginLayoutParams.leftMargin == i12 && marginLayoutParams.rightMargin == i13) {
                    z11 = false;
                } else {
                    marginLayoutParams.topMargin = i11;
                    marginLayoutParams.leftMargin = i12;
                    marginLayoutParams.rightMargin = i13;
                    z11 = true;
                }
                if (i11 > 0 && this.H == null) {
                    View view = new View(this.f410o);
                    this.H = view;
                    view.setVisibility(8);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, marginLayoutParams.topMargin, 51);
                    layoutParams.leftMargin = j10;
                    layoutParams.rightMargin = k10;
                    this.F.addView(this.H, -1, layoutParams);
                } else {
                    View view2 = this.H;
                    if (view2 != null) {
                        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
                        int i14 = marginLayoutParams2.height;
                        int i15 = marginLayoutParams.topMargin;
                        if (i14 != i15 || marginLayoutParams2.leftMargin != j10 || marginLayoutParams2.rightMargin != k10) {
                            marginLayoutParams2.height = i15;
                            marginLayoutParams2.leftMargin = j10;
                            marginLayoutParams2.rightMargin = k10;
                            this.H.setLayoutParams(marginLayoutParams2);
                        }
                    }
                }
                View view3 = this.H;
                r5 = view3 != null;
                if (r5 && view3.getVisibility() != 0) {
                    f1(this.H);
                }
                if (!this.M && r5) {
                    i10 = 0;
                }
                z10 = r5;
                r5 = z11;
            } else if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = 0;
                z10 = false;
            } else {
                z10 = false;
                r5 = false;
            }
            if (r5) {
                this.f421z.setLayoutParams(marginLayoutParams);
            }
        }
        View view4 = this.H;
        if (view4 != null) {
            view4.setVisibility(z10 ? 0 : 8);
        }
        return i10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0080, code lost:
    
        if (((org.xmlpull.v1.XmlPullParser) r15).getDepth() > 1) goto L25;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View f0(View view, String str, Context context, AttributeSet attributeSet) {
        boolean z10;
        boolean z11 = false;
        if (this.f405j0 == null) {
            String string = this.f410o.obtainStyledAttributes(R$styleable.AppCompatTheme).getString(R$styleable.AppCompatTheme_viewInflaterClass);
            if (string == null) {
                this.f405j0 = new AppCompatViewInflater();
            } else {
                try {
                    this.f405j0 = (AppCompatViewInflater) this.f410o.getClassLoader().loadClass(string).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Throwable th) {
                    Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + string + ". Falling back to default.", th);
                    this.f405j0 = new AppCompatViewInflater();
                }
            }
        }
        boolean z12 = f392o0;
        boolean z13 = true;
        if (z12) {
            if (this.f406k0 == null) {
                this.f406k0 = new LayoutIncludeDetector();
            }
            if (!this.f406k0.a(attributeSet)) {
                if (!(attributeSet instanceof XmlPullParser)) {
                    z13 = V0((ViewParent) view);
                }
                z11 = z13;
            } else {
                z10 = true;
                return this.f405j0.createView(view, str, context, attributeSet, z10, z12, true, VectorEnabledTintResources.c());
            }
        }
        z10 = z11;
        return this.f405j0.createView(view, str, context, attributeSet, z10, z12, true, VectorEnabledTintResources.c());
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public Context g(Context context) {
        this.T = true;
        int C0 = C0(context, X());
        if (AppCompatDelegate.v(context)) {
            AppCompatDelegate.Q(context);
        }
        LocaleListCompat W = W(context);
        if (f395r0 && (context instanceof android.view.ContextThemeWrapper)) {
            try {
                s.a((android.view.ContextThemeWrapper) context, d0(context, C0, W, null, false));
                return context;
            } catch (IllegalStateException unused) {
            }
        }
        if (context instanceof ContextThemeWrapper) {
            try {
                ((ContextThemeWrapper) context).a(d0(context, C0, W, null, false));
                return context;
            } catch (IllegalStateException unused2) {
            }
        }
        if (!f394q0) {
            return super.g(context);
        }
        Configuration configuration = new Configuration();
        configuration.uiMode = -1;
        configuration.fontScale = 0.0f;
        Configuration configuration2 = j.a(context, configuration).getResources().getConfiguration();
        Configuration configuration3 = context.getResources().getConfiguration();
        configuration2.uiMode = configuration3.uiMode;
        Configuration d02 = d0(context, C0, W, configuration2.equals(configuration3) ? null : n0(configuration2, configuration3), true);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R$style.Theme_AppCompat_Empty);
        contextThemeWrapper.a(d02);
        boolean z10 = false;
        try {
            z10 = context.getTheme() != null;
        } catch (NullPointerException unused3) {
        }
        if (z10) {
            ResourcesCompat.f.a(contextThemeWrapper.getTheme());
        }
        return super.g(contextThemeWrapper);
    }

    void g0() {
        MenuBuilder menuBuilder;
        DecorContentParent decorContentParent = this.f417v;
        if (decorContentParent != null) {
            decorContentParent.i();
        }
        if (this.A != null) {
            this.f411p.getDecorView().removeCallbacks(this.B);
            if (this.A.isShowing()) {
                try {
                    this.A.dismiss();
                } catch (IllegalArgumentException unused) {
                }
            }
            this.A = null;
        }
        j0();
        PanelFeatureState t02 = t0(0, false);
        if (t02 == null || (menuBuilder = t02.f431j) == null) {
            return;
        }
        menuBuilder.close();
    }

    boolean h0(KeyEvent keyEvent) {
        View decorView;
        Object obj = this.f409n;
        if (((obj instanceof KeyEventDispatcher.a) || (obj instanceof AppCompatDialog)) && (decorView = this.f411p.getDecorView()) != null && KeyEventDispatcher.a(decorView, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.f412q.b(this.f411p.getCallback(), keyEvent)) {
            return true;
        }
        int keyCode = keyEvent.getKeyCode();
        return keyEvent.getAction() == 0 ? E0(keyCode, keyEvent) : H0(keyCode, keyEvent);
    }

    void i0(int i10) {
        PanelFeatureState t02;
        PanelFeatureState t03 = t0(i10, true);
        if (t03.f431j != null) {
            Bundle bundle = new Bundle();
            t03.f431j.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                t03.f440s = bundle;
            }
            t03.f431j.stopDispatchingItemsChanged();
            t03.f431j.clear();
        }
        t03.f439r = true;
        t03.f438q = true;
        if ((i10 != 108 && i10 != 0) || this.f417v == null || (t02 = t0(0, false)) == null) {
            return;
        }
        t02.f434m = false;
        P0(t02, null);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public <T extends View> T j(int i10) {
        k0();
        return (T) this.f411p.findViewById(i10);
    }

    void j0() {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.C;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.c();
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public Context l() {
        return this.f410o;
    }

    PanelFeatureState m0(Menu menu) {
        PanelFeatureState[] panelFeatureStateArr = this.Q;
        int length = panelFeatureStateArr != null ? panelFeatureStateArr.length : 0;
        for (int i10 = 0; i10 < length; i10++) {
            PanelFeatureState panelFeatureState = panelFeatureStateArr[i10];
            if (panelFeatureState != null && panelFeatureState.f431j == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final ActionBarDrawerToggle n() {
        return new f();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public int o() {
        return this.X;
    }

    final Context o0() {
        ActionBar s7 = s();
        Context k10 = s7 != null ? s7.k() : null;
        return k10 == null ? this.f410o : k10;
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return f0(view, str, context, attributeSet);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public MenuInflater q() {
        if (this.f415t == null) {
            w0();
            ActionBar actionBar = this.f414s;
            this.f415t = new SupportMenuInflater(actionBar != null ? actionBar.k() : this.f410o);
        }
        return this.f415t;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public ActionBar s() {
        w0();
        return this.f414s;
    }

    LocaleListCompat s0(Configuration configuration) {
        return l.b(configuration);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void t() {
        LayoutInflater from = LayoutInflater.from(this.f410o);
        if (from.getFactory() == null) {
            LayoutInflaterCompat.a(from, this);
        } else {
            if (from.getFactory2() instanceof AppCompatDelegateImpl) {
                return;
            }
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    protected PanelFeatureState t0(int i10, boolean z10) {
        PanelFeatureState[] panelFeatureStateArr = this.Q;
        if (panelFeatureStateArr == null || panelFeatureStateArr.length <= i10) {
            PanelFeatureState[] panelFeatureStateArr2 = new PanelFeatureState[i10 + 1];
            if (panelFeatureStateArr != null) {
                System.arraycopy(panelFeatureStateArr, 0, panelFeatureStateArr2, 0, panelFeatureStateArr.length);
            }
            this.Q = panelFeatureStateArr2;
            panelFeatureStateArr = panelFeatureStateArr2;
        }
        PanelFeatureState panelFeatureState = panelFeatureStateArr[i10];
        if (panelFeatureState != null) {
            return panelFeatureState;
        }
        PanelFeatureState panelFeatureState2 = new PanelFeatureState(i10);
        panelFeatureStateArr[i10] = panelFeatureState2;
        return panelFeatureState2;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void u() {
        if (N0() == null || s().l()) {
            return;
        }
        A0(0);
    }

    final CharSequence u0() {
        Object obj = this.f409n;
        if (obj instanceof Activity) {
            return ((Activity) obj).getTitle();
        }
        return this.f416u;
    }

    final Window.Callback v0() {
        return this.f411p.getCallback();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void x(Configuration configuration) {
        ActionBar s7;
        if (this.K && this.E && (s7 = s()) != null) {
            s7.m(configuration);
        }
        AppCompatDrawableManager.b().g(this.f410o);
        this.W = new Configuration(this.f410o.getResources().getConfiguration());
        S(false, false);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void y(Bundle bundle) {
        this.T = true;
        R(false);
        l0();
        Object obj = this.f409n;
        if (obj instanceof Activity) {
            String str = null;
            try {
                str = NavUtils.c((Activity) obj);
            } catch (IllegalArgumentException unused) {
            }
            if (str != null) {
                ActionBar N0 = N0();
                if (N0 == null) {
                    this.f402g0 = true;
                } else {
                    N0.r(true);
                }
            }
            AppCompatDelegate.d(this);
        }
        this.W = new Configuration(this.f410o.getResources().getConfiguration());
        this.U = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0058  */
    @Override // androidx.appcompat.app.AppCompatDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void z() {
        ActionBar actionBar;
        if (this.f409n instanceof Activity) {
            AppCompatDelegate.F(this);
        }
        if (this.f399d0) {
            this.f411p.getDecorView().removeCallbacks(this.f401f0);
        }
        this.V = true;
        if (this.X != -100) {
            Object obj = this.f409n;
            if ((obj instanceof Activity) && ((Activity) obj).isChangingConfigurations()) {
                f391n0.put(this.f409n.getClass().getName(), Integer.valueOf(this.X));
                actionBar = this.f414s;
                if (actionBar != null) {
                    actionBar.n();
                }
                a0();
            }
        }
        f391n0.remove(this.f409n.getClass().getName());
        actionBar = this.f414s;
        if (actionBar != null) {
        }
        a0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatDelegateImpl(Dialog dialog, AppCompatCallback appCompatCallback) {
        this(dialog.getContext(), dialog.getWindow(), appCompatCallback, dialog);
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    private AppCompatDelegateImpl(Context context, Window window, AppCompatCallback appCompatCallback, Object obj) {
        SimpleArrayMap<String, Integer> simpleArrayMap;
        Integer num;
        AppCompatActivity Z0;
        this.C = null;
        this.D = true;
        this.X = -100;
        this.f401f0 = new a();
        this.f410o = context;
        this.f413r = appCompatCallback;
        this.f409n = obj;
        if (this.X == -100 && (obj instanceof Dialog) && (Z0 = Z0()) != null) {
            this.X = Z0.getDelegate().o();
        }
        if (this.X == -100 && (num = (simpleArrayMap = f391n0).get(obj.getClass().getName())) != null) {
            this.X = num.intValue();
            simpleArrayMap.remove(obj.getClass().getName());
        }
        if (window != null) {
            V(window);
        }
        AppCompatDrawableManager.h();
    }
}
