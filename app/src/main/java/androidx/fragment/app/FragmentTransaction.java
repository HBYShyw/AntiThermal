package androidx.fragment.app;

import android.view.ViewGroup;
import androidx.lifecycle.h;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/* compiled from: FragmentTransaction.java */
/* renamed from: androidx.fragment.app.r, reason: use source file name */
/* loaded from: classes.dex */
public abstract class FragmentTransaction {

    /* renamed from: a, reason: collision with root package name */
    private final FragmentFactory f2938a;

    /* renamed from: b, reason: collision with root package name */
    private final ClassLoader f2939b;

    /* renamed from: d, reason: collision with root package name */
    int f2941d;

    /* renamed from: e, reason: collision with root package name */
    int f2942e;

    /* renamed from: f, reason: collision with root package name */
    int f2943f;

    /* renamed from: g, reason: collision with root package name */
    int f2944g;

    /* renamed from: h, reason: collision with root package name */
    int f2945h;

    /* renamed from: i, reason: collision with root package name */
    boolean f2946i;

    /* renamed from: k, reason: collision with root package name */
    String f2948k;

    /* renamed from: l, reason: collision with root package name */
    int f2949l;

    /* renamed from: m, reason: collision with root package name */
    CharSequence f2950m;

    /* renamed from: n, reason: collision with root package name */
    int f2951n;

    /* renamed from: o, reason: collision with root package name */
    CharSequence f2952o;

    /* renamed from: p, reason: collision with root package name */
    ArrayList<String> f2953p;

    /* renamed from: q, reason: collision with root package name */
    ArrayList<String> f2954q;

    /* renamed from: s, reason: collision with root package name */
    ArrayList<Runnable> f2956s;

    /* renamed from: c, reason: collision with root package name */
    ArrayList<a> f2940c = new ArrayList<>();

    /* renamed from: j, reason: collision with root package name */
    boolean f2947j = true;

    /* renamed from: r, reason: collision with root package name */
    boolean f2955r = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransaction.java */
    /* renamed from: androidx.fragment.app.r$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        int f2957a;

        /* renamed from: b, reason: collision with root package name */
        Fragment f2958b;

        /* renamed from: c, reason: collision with root package name */
        int f2959c;

        /* renamed from: d, reason: collision with root package name */
        int f2960d;

        /* renamed from: e, reason: collision with root package name */
        int f2961e;

        /* renamed from: f, reason: collision with root package name */
        int f2962f;

        /* renamed from: g, reason: collision with root package name */
        h.c f2963g;

        /* renamed from: h, reason: collision with root package name */
        h.c f2964h;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i10, Fragment fragment) {
            this.f2957a = i10;
            this.f2958b = fragment;
            h.c cVar = h.c.RESUMED;
            this.f2963g = cVar;
            this.f2964h = cVar;
        }

        a(int i10, Fragment fragment, h.c cVar) {
            this.f2957a = i10;
            this.f2958b = fragment;
            this.f2963g = fragment.mMaxState;
            this.f2964h = cVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentTransaction(FragmentFactory fragmentFactory, ClassLoader classLoader) {
        this.f2938a = fragmentFactory;
        this.f2939b = classLoader;
    }

    public FragmentTransaction b(int i10, Fragment fragment) {
        o(i10, fragment, null, 1);
        return this;
    }

    public FragmentTransaction c(int i10, Fragment fragment, String str) {
        o(i10, fragment, str, 1);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentTransaction d(ViewGroup viewGroup, Fragment fragment, String str) {
        fragment.mContainer = viewGroup;
        return c(viewGroup.getId(), fragment, str);
    }

    public FragmentTransaction e(Fragment fragment, String str) {
        o(0, fragment, str, 1);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(a aVar) {
        this.f2940c.add(aVar);
        aVar.f2959c = this.f2941d;
        aVar.f2960d = this.f2942e;
        aVar.f2961e = this.f2943f;
        aVar.f2962f = this.f2944g;
    }

    public FragmentTransaction g(String str) {
        if (this.f2947j) {
            this.f2946i = true;
            this.f2948k = str;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }

    public FragmentTransaction h(Fragment fragment) {
        f(new a(7, fragment));
        return this;
    }

    public abstract int i();

    public abstract int j();

    public abstract void k();

    public abstract void l();

    public FragmentTransaction m(Fragment fragment) {
        f(new a(6, fragment));
        return this;
    }

    public FragmentTransaction n() {
        if (!this.f2946i) {
            this.f2947j = false;
            return this;
        }
        throw new IllegalStateException("This transaction is already being added to the back stack");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(int i10, Fragment fragment, String str, int i11) {
        Class<?> cls = fragment.getClass();
        int modifiers = cls.getModifiers();
        if (!cls.isAnonymousClass() && Modifier.isPublic(modifiers) && (!cls.isMemberClass() || Modifier.isStatic(modifiers))) {
            if (str != null) {
                String str2 = fragment.mTag;
                if (str2 != null && !str.equals(str2)) {
                    throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + str);
                }
                fragment.mTag = str;
            }
            if (i10 != 0) {
                if (i10 != -1) {
                    int i12 = fragment.mFragmentId;
                    if (i12 != 0 && i12 != i10) {
                        throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + i10);
                    }
                    fragment.mFragmentId = i10;
                    fragment.mContainerId = i10;
                } else {
                    throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + str + " to container view with no id");
                }
            }
            f(new a(i11, fragment));
            return;
        }
        throw new IllegalStateException("Fragment " + cls.getCanonicalName() + " must be a public static class to be  properly recreated from instance state.");
    }

    public abstract boolean p();

    public FragmentTransaction q(Fragment fragment) {
        f(new a(3, fragment));
        return this;
    }

    public FragmentTransaction r(int i10, Fragment fragment) {
        return s(i10, fragment, null);
    }

    public FragmentTransaction s(int i10, Fragment fragment, String str) {
        if (i10 != 0) {
            o(i10, fragment, str, 2);
            return this;
        }
        throw new IllegalArgumentException("Must use non-zero containerViewId");
    }

    public FragmentTransaction t(Fragment fragment, h.c cVar) {
        f(new a(10, fragment, cVar));
        return this;
    }

    public FragmentTransaction u(boolean z10) {
        this.f2955r = z10;
        return this;
    }
}
