package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.fragment.R$id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SpecialEffectsController.java */
/* renamed from: androidx.fragment.app.x, reason: use source file name */
/* loaded from: classes.dex */
public abstract class SpecialEffectsController {

    /* renamed from: a, reason: collision with root package name */
    private final ViewGroup f3043a;

    /* renamed from: b, reason: collision with root package name */
    final ArrayList<e> f3044b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    final ArrayList<e> f3045c = new ArrayList<>();

    /* renamed from: d, reason: collision with root package name */
    boolean f3046d = false;

    /* renamed from: e, reason: collision with root package name */
    boolean f3047e = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.x$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ d f3048e;

        a(d dVar) {
            this.f3048e = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SpecialEffectsController.this.f3044b.contains(this.f3048e)) {
                this.f3048e.e().a(this.f3048e.f().mView);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.x$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ d f3050e;

        b(d dVar) {
            this.f3050e = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            SpecialEffectsController.this.f3044b.remove(this.f3050e);
            SpecialEffectsController.this.f3045c.remove(this.f3050e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.x$c */
    /* loaded from: classes.dex */
    public static /* synthetic */ class c {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f3052a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f3053b;

        static {
            int[] iArr = new int[e.b.values().length];
            f3053b = iArr;
            try {
                iArr[e.b.ADDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3053b[e.b.REMOVING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3053b[e.b.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[e.c.values().length];
            f3052a = iArr2;
            try {
                iArr2[e.c.REMOVED.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3052a[e.c.VISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3052a[e.c.GONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f3052a[e.c.INVISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.x$d */
    /* loaded from: classes.dex */
    public static class d extends e {

        /* renamed from: h, reason: collision with root package name */
        private final FragmentStateManager f3054h;

        d(e.c cVar, e.b bVar, FragmentStateManager fragmentStateManager, CancellationSignal cancellationSignal) {
            super(cVar, bVar, fragmentStateManager.k(), cancellationSignal);
            this.f3054h = fragmentStateManager;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.e
        public void c() {
            super.c();
            this.f3054h.m();
        }

        @Override // androidx.fragment.app.SpecialEffectsController.e
        void l() {
            if (g() == e.b.ADDING) {
                Fragment k10 = this.f3054h.k();
                View findFocus = k10.mView.findFocus();
                if (findFocus != null) {
                    k10.setFocusedView(findFocus);
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + k10);
                    }
                }
                View requireView = f().requireView();
                if (requireView.getParent() == null) {
                    this.f3054h.b();
                    requireView.setAlpha(0.0f);
                }
                if (requireView.getAlpha() == 0.0f && requireView.getVisibility() == 0) {
                    requireView.setVisibility(4);
                }
                requireView.setAlpha(k10.getPostOnViewCreatedAlpha());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.x$e */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        private c f3055a;

        /* renamed from: b, reason: collision with root package name */
        private b f3056b;

        /* renamed from: c, reason: collision with root package name */
        private final Fragment f3057c;

        /* renamed from: d, reason: collision with root package name */
        private final List<Runnable> f3058d = new ArrayList();

        /* renamed from: e, reason: collision with root package name */
        private final HashSet<CancellationSignal> f3059e = new HashSet<>();

        /* renamed from: f, reason: collision with root package name */
        private boolean f3060f = false;

        /* renamed from: g, reason: collision with root package name */
        private boolean f3061g = false;

        /* compiled from: SpecialEffectsController.java */
        /* renamed from: androidx.fragment.app.x$e$a */
        /* loaded from: classes.dex */
        class a implements CancellationSignal.b {
            a() {
            }

            @Override // androidx.core.os.CancellationSignal.b
            public void a() {
                e.this.b();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: SpecialEffectsController.java */
        /* renamed from: androidx.fragment.app.x$e$b */
        /* loaded from: classes.dex */
        public enum b {
            NONE,
            ADDING,
            REMOVING
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: SpecialEffectsController.java */
        /* renamed from: androidx.fragment.app.x$e$c */
        /* loaded from: classes.dex */
        public enum c {
            REMOVED,
            VISIBLE,
            GONE,
            INVISIBLE;

            /* JADX INFO: Access modifiers changed from: package-private */
            public static c b(int i10) {
                if (i10 == 0) {
                    return VISIBLE;
                }
                if (i10 == 4) {
                    return INVISIBLE;
                }
                if (i10 == 8) {
                    return GONE;
                }
                throw new IllegalArgumentException("Unknown visibility " + i10);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static c c(View view) {
                if (view.getAlpha() == 0.0f && view.getVisibility() == 0) {
                    return INVISIBLE;
                }
                return b(view.getVisibility());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public void a(View view) {
                int i10 = c.f3052a[ordinal()];
                if (i10 == 1) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    if (viewGroup != null) {
                        if (FragmentManager.H0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Removing view " + view + " from container " + viewGroup);
                        }
                        viewGroup.removeView(view);
                        return;
                    }
                    return;
                }
                if (i10 == 2) {
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to VISIBLE");
                    }
                    view.setVisibility(0);
                    return;
                }
                if (i10 == 3) {
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to GONE");
                    }
                    view.setVisibility(8);
                    return;
                }
                if (i10 != 4) {
                    return;
                }
                if (FragmentManager.H0(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to INVISIBLE");
                }
                view.setVisibility(4);
            }
        }

        e(c cVar, b bVar, Fragment fragment, CancellationSignal cancellationSignal) {
            this.f3055a = cVar;
            this.f3056b = bVar;
            this.f3057c = fragment;
            cancellationSignal.c(new a());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void a(Runnable runnable) {
            this.f3058d.add(runnable);
        }

        final void b() {
            if (h()) {
                return;
            }
            this.f3060f = true;
            if (this.f3059e.isEmpty()) {
                c();
                return;
            }
            Iterator it = new ArrayList(this.f3059e).iterator();
            while (it.hasNext()) {
                ((CancellationSignal) it.next()).a();
            }
        }

        public void c() {
            if (this.f3061g) {
                return;
            }
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "SpecialEffectsController: " + this + " has called complete.");
            }
            this.f3061g = true;
            Iterator<Runnable> it = this.f3058d.iterator();
            while (it.hasNext()) {
                it.next().run();
            }
        }

        public final void d(CancellationSignal cancellationSignal) {
            if (this.f3059e.remove(cancellationSignal) && this.f3059e.isEmpty()) {
                c();
            }
        }

        public c e() {
            return this.f3055a;
        }

        public final Fragment f() {
            return this.f3057c;
        }

        b g() {
            return this.f3056b;
        }

        final boolean h() {
            return this.f3060f;
        }

        final boolean i() {
            return this.f3061g;
        }

        public final void j(CancellationSignal cancellationSignal) {
            l();
            this.f3059e.add(cancellationSignal);
        }

        final void k(c cVar, b bVar) {
            int i10 = c.f3053b[bVar.ordinal()];
            if (i10 == 1) {
                if (this.f3055a == c.REMOVED) {
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f3057c + " mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = " + this.f3056b + " to ADDING.");
                    }
                    this.f3055a = c.VISIBLE;
                    this.f3056b = b.ADDING;
                    return;
                }
                return;
            }
            if (i10 != 2) {
                if (i10 == 3 && this.f3055a != c.REMOVED) {
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f3057c + " mFinalState = " + this.f3055a + " -> " + cVar + ". ");
                    }
                    this.f3055a = cVar;
                    return;
                }
                return;
            }
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f3057c + " mFinalState = " + this.f3055a + " -> REMOVED. mLifecycleImpact  = " + this.f3056b + " to REMOVING.");
            }
            this.f3055a = c.REMOVED;
            this.f3056b = b.REMOVING;
        }

        void l() {
        }

        public String toString() {
            return "Operation {" + Integer.toHexString(System.identityHashCode(this)) + "} {mFinalState = " + this.f3055a + "} {mLifecycleImpact = " + this.f3056b + "} {mFragment = " + this.f3057c + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpecialEffectsController(ViewGroup viewGroup) {
        this.f3043a = viewGroup;
    }

    private void a(e.c cVar, e.b bVar, FragmentStateManager fragmentStateManager) {
        synchronized (this.f3044b) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            e h10 = h(fragmentStateManager.k());
            if (h10 != null) {
                h10.k(cVar, bVar);
                return;
            }
            d dVar = new d(cVar, bVar, fragmentStateManager, cancellationSignal);
            this.f3044b.add(dVar);
            dVar.a(new a(dVar));
            dVar.a(new b(dVar));
        }
    }

    private e h(Fragment fragment) {
        Iterator<e> it = this.f3044b.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.f().equals(fragment) && !next.h()) {
                return next;
            }
        }
        return null;
    }

    private e i(Fragment fragment) {
        Iterator<e> it = this.f3045c.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.f().equals(fragment) && !next.h()) {
                return next;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpecialEffectsController n(ViewGroup viewGroup, FragmentManager fragmentManager) {
        return o(viewGroup, fragmentManager.A0());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpecialEffectsController o(ViewGroup viewGroup, SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        int i10 = R$id.special_effects_controller_view_tag;
        Object tag = viewGroup.getTag(i10);
        if (tag instanceof SpecialEffectsController) {
            return (SpecialEffectsController) tag;
        }
        SpecialEffectsController a10 = specialEffectsControllerFactory.a(viewGroup);
        viewGroup.setTag(i10, a10);
        return a10;
    }

    private void q() {
        Iterator<e> it = this.f3044b.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.g() == e.b.ADDING) {
                next.k(e.c.b(next.f().requireView().getVisibility()), e.b.NONE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(e.c cVar, FragmentStateManager fragmentStateManager) {
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + fragmentStateManager.k());
        }
        a(cVar, e.b.ADDING, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing hide operation for fragment " + fragmentStateManager.k());
        }
        a(e.c.GONE, e.b.NONE, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing remove operation for fragment " + fragmentStateManager.k());
        }
        a(e.c.REMOVED, e.b.REMOVING, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing show operation for fragment " + fragmentStateManager.k());
        }
        a(e.c.VISIBLE, e.b.NONE, fragmentStateManager);
    }

    abstract void f(List<e> list, boolean z10);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        if (this.f3047e) {
            return;
        }
        if (!ViewCompat.P(this.f3043a)) {
            j();
            this.f3046d = false;
            return;
        }
        synchronized (this.f3044b) {
            if (!this.f3044b.isEmpty()) {
                ArrayList arrayList = new ArrayList(this.f3045c);
                this.f3045c.clear();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    e eVar = (e) it.next();
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Cancelling operation " + eVar);
                    }
                    eVar.b();
                    if (!eVar.i()) {
                        this.f3045c.add(eVar);
                    }
                }
                q();
                ArrayList arrayList2 = new ArrayList(this.f3044b);
                this.f3044b.clear();
                this.f3045c.addAll(arrayList2);
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    ((e) it2.next()).l();
                }
                f(arrayList2, this.f3046d);
                this.f3046d = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        String str;
        String str2;
        boolean P = ViewCompat.P(this.f3043a);
        synchronized (this.f3044b) {
            q();
            Iterator<e> it = this.f3044b.iterator();
            while (it.hasNext()) {
                it.next().l();
            }
            Iterator it2 = new ArrayList(this.f3045c).iterator();
            while (it2.hasNext()) {
                e eVar = (e) it2.next();
                if (FragmentManager.H0(2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SpecialEffectsController: ");
                    if (P) {
                        str2 = "";
                    } else {
                        str2 = "Container " + this.f3043a + " is not attached to window. ";
                    }
                    sb2.append(str2);
                    sb2.append("Cancelling running operation ");
                    sb2.append(eVar);
                    Log.v("FragmentManager", sb2.toString());
                }
                eVar.b();
            }
            Iterator it3 = new ArrayList(this.f3044b).iterator();
            while (it3.hasNext()) {
                e eVar2 = (e) it3.next();
                if (FragmentManager.H0(2)) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("SpecialEffectsController: ");
                    if (P) {
                        str = "";
                    } else {
                        str = "Container " + this.f3043a + " is not attached to window. ";
                    }
                    sb3.append(str);
                    sb3.append("Cancelling pending operation ");
                    sb3.append(eVar2);
                    Log.v("FragmentManager", sb3.toString());
                }
                eVar2.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        if (this.f3047e) {
            this.f3047e = false;
            g();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e.b l(FragmentStateManager fragmentStateManager) {
        e h10 = h(fragmentStateManager.k());
        e.b g6 = h10 != null ? h10.g() : null;
        e i10 = i(fragmentStateManager.k());
        return (i10 == null || !(g6 == null || g6 == e.b.NONE)) ? g6 : i10.g();
    }

    public ViewGroup m() {
        return this.f3043a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        synchronized (this.f3044b) {
            q();
            this.f3047e = false;
            int size = this.f3044b.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                e eVar = this.f3044b.get(size);
                e.c c10 = e.c.c(eVar.f().mView);
                e.c e10 = eVar.e();
                e.c cVar = e.c.VISIBLE;
                if (e10 == cVar && c10 != cVar) {
                    this.f3047e = eVar.f().isPostponed();
                    break;
                }
                size--;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(boolean z10) {
        this.f3046d = z10;
    }
}
