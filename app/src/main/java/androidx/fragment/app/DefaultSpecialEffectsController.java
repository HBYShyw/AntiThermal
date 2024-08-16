package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.core.os.CancellationSignal;
import androidx.core.util.Preconditions;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.SpecialEffectsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: DefaultSpecialEffectsController.java */
/* renamed from: androidx.fragment.app.b, reason: use source file name */
/* loaded from: classes.dex */
class DefaultSpecialEffectsController extends SpecialEffectsController {

    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f2816a;

        static {
            int[] iArr = new int[SpecialEffectsController.e.c.values().length];
            f2816a = iArr;
            try {
                iArr[SpecialEffectsController.e.c.GONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f2816a[SpecialEffectsController.e.c.INVISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f2816a[SpecialEffectsController.e.c.REMOVED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f2816a[SpecialEffectsController.e.c.VISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$b */
    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List f2817e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ SpecialEffectsController.e f2818f;

        b(List list, SpecialEffectsController.e eVar) {
            this.f2817e = list;
            this.f2818f = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f2817e.contains(this.f2818f)) {
                this.f2817e.remove(this.f2818f);
                DefaultSpecialEffectsController.this.s(this.f2818f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$c */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f2820a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f2821b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ boolean f2822c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ SpecialEffectsController.e f2823d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ k f2824e;

        c(ViewGroup viewGroup, View view, boolean z10, SpecialEffectsController.e eVar, k kVar) {
            this.f2820a = viewGroup;
            this.f2821b = view;
            this.f2822c = z10;
            this.f2823d = eVar;
            this.f2824e = kVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f2820a.endViewTransition(this.f2821b);
            if (this.f2822c) {
                this.f2823d.e().a(this.f2821b);
            }
            this.f2824e.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$d */
    /* loaded from: classes.dex */
    public class d implements CancellationSignal.b {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Animator f2826a;

        d(Animator animator) {
            this.f2826a = animator;
        }

        @Override // androidx.core.os.CancellationSignal.b
        public void a() {
            this.f2826a.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$e */
    /* loaded from: classes.dex */
    public class e implements Animation.AnimationListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ViewGroup f2828e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ View f2829f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ k f2830g;

        /* compiled from: DefaultSpecialEffectsController.java */
        /* renamed from: androidx.fragment.app.b$e$a */
        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                e eVar = e.this;
                eVar.f2828e.endViewTransition(eVar.f2829f);
                e.this.f2830g.a();
            }
        }

        e(ViewGroup viewGroup, View view, k kVar) {
            this.f2828e = viewGroup;
            this.f2829f = view;
            this.f2830g = kVar;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            this.f2828e.post(new a());
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$f */
    /* loaded from: classes.dex */
    public class f implements CancellationSignal.b {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f2833a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewGroup f2834b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ k f2835c;

        f(View view, ViewGroup viewGroup, k kVar) {
            this.f2833a = view;
            this.f2834b = viewGroup;
            this.f2835c = kVar;
        }

        @Override // androidx.core.os.CancellationSignal.b
        public void a() {
            this.f2833a.clearAnimation();
            this.f2834b.endViewTransition(this.f2833a);
            this.f2835c.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$g */
    /* loaded from: classes.dex */
    public class g implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ SpecialEffectsController.e f2837e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ SpecialEffectsController.e f2838f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ boolean f2839g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ j.a f2840h;

        g(SpecialEffectsController.e eVar, SpecialEffectsController.e eVar2, boolean z10, j.a aVar) {
            this.f2837e = eVar;
            this.f2838f = eVar2;
            this.f2839g = z10;
            this.f2840h = aVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentTransition.f(this.f2837e.f(), this.f2838f.f(), this.f2839g, this.f2840h, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$h */
    /* loaded from: classes.dex */
    public class h implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FragmentTransitionImpl f2842e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ View f2843f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Rect f2844g;

        h(FragmentTransitionImpl fragmentTransitionImpl, View view, Rect rect) {
            this.f2842e = fragmentTransitionImpl;
            this.f2843f = view;
            this.f2844g = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f2842e.k(this.f2843f, this.f2844g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$i */
    /* loaded from: classes.dex */
    public class i implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ArrayList f2846e;

        i(ArrayList arrayList) {
            this.f2846e = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentTransition.A(this.f2846e, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$j */
    /* loaded from: classes.dex */
    public class j implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ m f2848e;

        j(m mVar) {
            this.f2848e = mVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f2848e.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$k */
    /* loaded from: classes.dex */
    public static class k extends l {

        /* renamed from: c, reason: collision with root package name */
        private boolean f2850c;

        /* renamed from: d, reason: collision with root package name */
        private boolean f2851d;

        /* renamed from: e, reason: collision with root package name */
        private FragmentAnim.d f2852e;

        k(SpecialEffectsController.e eVar, CancellationSignal cancellationSignal, boolean z10) {
            super(eVar, cancellationSignal);
            this.f2851d = false;
            this.f2850c = z10;
        }

        FragmentAnim.d e(Context context) {
            if (this.f2851d) {
                return this.f2852e;
            }
            FragmentAnim.d c10 = FragmentAnim.c(context, b().f(), b().e() == SpecialEffectsController.e.c.VISIBLE, this.f2850c);
            this.f2852e = c10;
            this.f2851d = true;
            return c10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$l */
    /* loaded from: classes.dex */
    public static class l {

        /* renamed from: a, reason: collision with root package name */
        private final SpecialEffectsController.e f2853a;

        /* renamed from: b, reason: collision with root package name */
        private final CancellationSignal f2854b;

        l(SpecialEffectsController.e eVar, CancellationSignal cancellationSignal) {
            this.f2853a = eVar;
            this.f2854b = cancellationSignal;
        }

        void a() {
            this.f2853a.d(this.f2854b);
        }

        SpecialEffectsController.e b() {
            return this.f2853a;
        }

        CancellationSignal c() {
            return this.f2854b;
        }

        boolean d() {
            SpecialEffectsController.e.c cVar;
            SpecialEffectsController.e.c c10 = SpecialEffectsController.e.c.c(this.f2853a.f().mView);
            SpecialEffectsController.e.c e10 = this.f2853a.e();
            return c10 == e10 || !(c10 == (cVar = SpecialEffectsController.e.c.VISIBLE) || e10 == cVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DefaultSpecialEffectsController.java */
    /* renamed from: androidx.fragment.app.b$m */
    /* loaded from: classes.dex */
    public static class m extends l {

        /* renamed from: c, reason: collision with root package name */
        private final Object f2855c;

        /* renamed from: d, reason: collision with root package name */
        private final boolean f2856d;

        /* renamed from: e, reason: collision with root package name */
        private final Object f2857e;

        m(SpecialEffectsController.e eVar, CancellationSignal cancellationSignal, boolean z10, boolean z11) {
            super(eVar, cancellationSignal);
            Object exitTransition;
            Object enterTransition;
            boolean allowEnterTransitionOverlap;
            if (eVar.e() == SpecialEffectsController.e.c.VISIBLE) {
                if (z10) {
                    enterTransition = eVar.f().getReenterTransition();
                } else {
                    enterTransition = eVar.f().getEnterTransition();
                }
                this.f2855c = enterTransition;
                if (z10) {
                    allowEnterTransitionOverlap = eVar.f().getAllowReturnTransitionOverlap();
                } else {
                    allowEnterTransitionOverlap = eVar.f().getAllowEnterTransitionOverlap();
                }
                this.f2856d = allowEnterTransitionOverlap;
            } else {
                if (z10) {
                    exitTransition = eVar.f().getReturnTransition();
                } else {
                    exitTransition = eVar.f().getExitTransition();
                }
                this.f2855c = exitTransition;
                this.f2856d = true;
            }
            if (!z11) {
                this.f2857e = null;
            } else if (z10) {
                this.f2857e = eVar.f().getSharedElementReturnTransition();
            } else {
                this.f2857e = eVar.f().getSharedElementEnterTransition();
            }
        }

        private FragmentTransitionImpl f(Object obj) {
            if (obj == null) {
                return null;
            }
            FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.f2966b;
            if (fragmentTransitionImpl != null && fragmentTransitionImpl.e(obj)) {
                return fragmentTransitionImpl;
            }
            FragmentTransitionImpl fragmentTransitionImpl2 = FragmentTransition.f2967c;
            if (fragmentTransitionImpl2 != null && fragmentTransitionImpl2.e(obj)) {
                return fragmentTransitionImpl2;
            }
            throw new IllegalArgumentException("Transition " + obj + " for fragment " + b().f() + " is not a valid framework Transition or AndroidX Transition");
        }

        FragmentTransitionImpl e() {
            FragmentTransitionImpl f10 = f(this.f2855c);
            FragmentTransitionImpl f11 = f(this.f2857e);
            if (f10 == null || f11 == null || f10 == f11) {
                return f10 != null ? f10 : f11;
            }
            throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + b().f() + " returned Transition " + this.f2855c + " which uses a different Transition  type than its shared element transition " + this.f2857e);
        }

        public Object g() {
            return this.f2857e;
        }

        Object h() {
            return this.f2855c;
        }

        public boolean i() {
            return this.f2857e != null;
        }

        boolean j() {
            return this.f2856d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultSpecialEffectsController(ViewGroup viewGroup) {
        super(viewGroup);
    }

    private void w(List<k> list, List<SpecialEffectsController.e> list2, boolean z10, Map<SpecialEffectsController.e, Boolean> map) {
        ViewGroup m10 = m();
        Context context = m10.getContext();
        ArrayList arrayList = new ArrayList();
        boolean z11 = false;
        for (k kVar : list) {
            if (kVar.d()) {
                kVar.a();
            } else {
                FragmentAnim.d e10 = kVar.e(context);
                if (e10 == null) {
                    kVar.a();
                } else {
                    Animator animator = e10.f2892b;
                    if (animator == null) {
                        arrayList.add(kVar);
                    } else {
                        SpecialEffectsController.e b10 = kVar.b();
                        Fragment f10 = b10.f();
                        if (Boolean.TRUE.equals(map.get(b10))) {
                            if (FragmentManager.H0(2)) {
                                Log.v("FragmentManager", "Ignoring Animator set on " + f10 + " as this Fragment was involved in a Transition.");
                            }
                            kVar.a();
                        } else {
                            boolean z12 = b10.e() == SpecialEffectsController.e.c.GONE;
                            if (z12) {
                                list2.remove(b10);
                            }
                            View view = f10.mView;
                            m10.startViewTransition(view);
                            animator.addListener(new c(m10, view, z12, b10, kVar));
                            animator.setTarget(view);
                            animator.start();
                            kVar.c().c(new d(animator));
                            z11 = true;
                        }
                    }
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            k kVar2 = (k) it.next();
            SpecialEffectsController.e b11 = kVar2.b();
            Fragment f11 = b11.f();
            if (z10) {
                if (FragmentManager.H0(2)) {
                    Log.v("FragmentManager", "Ignoring Animation set on " + f11 + " as Animations cannot run alongside Transitions.");
                }
                kVar2.a();
            } else if (z11) {
                if (FragmentManager.H0(2)) {
                    Log.v("FragmentManager", "Ignoring Animation set on " + f11 + " as Animations cannot run alongside Animators.");
                }
                kVar2.a();
            } else {
                View view2 = f11.mView;
                Animation animation = (Animation) Preconditions.d(((FragmentAnim.d) Preconditions.d(kVar2.e(context))).f2891a);
                if (b11.e() != SpecialEffectsController.e.c.REMOVED) {
                    view2.startAnimation(animation);
                    kVar2.a();
                } else {
                    m10.startViewTransition(view2);
                    FragmentAnim.e eVar = new FragmentAnim.e(animation, m10, view2);
                    eVar.setAnimationListener(new e(m10, view2, kVar2));
                    view2.startAnimation(eVar);
                }
                kVar2.c().c(new f(view2, m10, kVar2));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Map<SpecialEffectsController.e, Boolean> x(List<m> list, List<SpecialEffectsController.e> list2, boolean z10, SpecialEffectsController.e eVar, SpecialEffectsController.e eVar2) {
        View view;
        Object obj;
        ArrayList<View> arrayList;
        Object obj2;
        ArrayList<View> arrayList2;
        SpecialEffectsController.e eVar3;
        SpecialEffectsController.e eVar4;
        View view2;
        Object n10;
        j.a aVar;
        ArrayList<View> arrayList3;
        SpecialEffectsController.e eVar5;
        ArrayList<View> arrayList4;
        Rect rect;
        View view3;
        FragmentTransitionImpl fragmentTransitionImpl;
        SpecialEffectsController.e eVar6;
        View view4;
        boolean z11 = z10;
        SpecialEffectsController.e eVar7 = eVar;
        SpecialEffectsController.e eVar8 = eVar2;
        HashMap hashMap = new HashMap();
        FragmentTransitionImpl fragmentTransitionImpl2 = null;
        for (m mVar : list) {
            if (!mVar.d()) {
                FragmentTransitionImpl e10 = mVar.e();
                if (fragmentTransitionImpl2 == null) {
                    fragmentTransitionImpl2 = e10;
                } else if (e10 != null && fragmentTransitionImpl2 != e10) {
                    throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + mVar.b().f() + " returned Transition " + mVar.h() + " which uses a different Transition  type than other Fragments.");
                }
            }
        }
        if (fragmentTransitionImpl2 == null) {
            for (m mVar2 : list) {
                hashMap.put(mVar2.b(), Boolean.FALSE);
                mVar2.a();
            }
            return hashMap;
        }
        View view5 = new View(m().getContext());
        Rect rect2 = new Rect();
        ArrayList<View> arrayList5 = new ArrayList<>();
        ArrayList<View> arrayList6 = new ArrayList<>();
        j.a aVar2 = new j.a();
        Object obj3 = null;
        View view6 = null;
        boolean z12 = false;
        for (m mVar3 : list) {
            if (!mVar3.i() || eVar7 == null || eVar8 == null) {
                aVar = aVar2;
                arrayList3 = arrayList6;
                eVar5 = eVar7;
                arrayList4 = arrayList5;
                rect = rect2;
                view3 = view5;
                fragmentTransitionImpl = fragmentTransitionImpl2;
                eVar6 = eVar8;
                view6 = view6;
            } else {
                Object B = fragmentTransitionImpl2.B(fragmentTransitionImpl2.g(mVar3.g()));
                ArrayList<String> sharedElementSourceNames = eVar2.f().getSharedElementSourceNames();
                ArrayList<String> sharedElementSourceNames2 = eVar.f().getSharedElementSourceNames();
                ArrayList<String> sharedElementTargetNames = eVar.f().getSharedElementTargetNames();
                View view7 = view6;
                int i10 = 0;
                while (i10 < sharedElementTargetNames.size()) {
                    int indexOf = sharedElementSourceNames.indexOf(sharedElementTargetNames.get(i10));
                    ArrayList<String> arrayList7 = sharedElementTargetNames;
                    if (indexOf != -1) {
                        sharedElementSourceNames.set(indexOf, sharedElementSourceNames2.get(i10));
                    }
                    i10++;
                    sharedElementTargetNames = arrayList7;
                }
                ArrayList<String> sharedElementTargetNames2 = eVar2.f().getSharedElementTargetNames();
                if (!z11) {
                    eVar.f().getExitTransitionCallback();
                    eVar2.f().getEnterTransitionCallback();
                } else {
                    eVar.f().getEnterTransitionCallback();
                    eVar2.f().getExitTransitionCallback();
                }
                int i11 = 0;
                for (int size = sharedElementSourceNames.size(); i11 < size; size = size) {
                    aVar2.put(sharedElementSourceNames.get(i11), sharedElementTargetNames2.get(i11));
                    i11++;
                }
                j.a<String, View> aVar3 = new j.a<>();
                u(aVar3, eVar.f().mView);
                aVar3.p(sharedElementSourceNames);
                aVar2.p(aVar3.keySet());
                j.a<String, View> aVar4 = new j.a<>();
                u(aVar4, eVar2.f().mView);
                aVar4.p(sharedElementTargetNames2);
                aVar4.p(aVar2.values());
                FragmentTransition.x(aVar2, aVar4);
                v(aVar3, aVar2.keySet());
                v(aVar4, aVar2.values());
                if (aVar2.isEmpty()) {
                    arrayList5.clear();
                    arrayList6.clear();
                    aVar = aVar2;
                    arrayList3 = arrayList6;
                    eVar5 = eVar7;
                    arrayList4 = arrayList5;
                    rect = rect2;
                    view3 = view5;
                    fragmentTransitionImpl = fragmentTransitionImpl2;
                    view6 = view7;
                    obj3 = null;
                    eVar6 = eVar8;
                } else {
                    FragmentTransition.f(eVar2.f(), eVar.f(), z11, aVar3, true);
                    aVar = aVar2;
                    ArrayList<View> arrayList8 = arrayList6;
                    OneShotPreDrawListener.a(m(), new g(eVar2, eVar, z10, aVar4));
                    arrayList5.addAll(aVar3.values());
                    if (sharedElementSourceNames.isEmpty()) {
                        view6 = view7;
                    } else {
                        View view8 = (View) aVar3.get(sharedElementSourceNames.get(0));
                        fragmentTransitionImpl2.v(B, view8);
                        view6 = view8;
                    }
                    arrayList3 = arrayList8;
                    arrayList3.addAll(aVar4.values());
                    if (!sharedElementTargetNames2.isEmpty() && (view4 = (View) aVar4.get(sharedElementTargetNames2.get(0))) != null) {
                        OneShotPreDrawListener.a(m(), new h(fragmentTransitionImpl2, view4, rect2));
                        z12 = true;
                    }
                    fragmentTransitionImpl2.z(B, view5, arrayList5);
                    arrayList4 = arrayList5;
                    rect = rect2;
                    view3 = view5;
                    fragmentTransitionImpl = fragmentTransitionImpl2;
                    fragmentTransitionImpl2.t(B, null, null, null, null, B, arrayList3);
                    Boolean bool = Boolean.TRUE;
                    eVar5 = eVar;
                    hashMap.put(eVar5, bool);
                    eVar6 = eVar2;
                    hashMap.put(eVar6, bool);
                    obj3 = B;
                }
            }
            eVar7 = eVar5;
            arrayList5 = arrayList4;
            rect2 = rect;
            view5 = view3;
            eVar8 = eVar6;
            aVar2 = aVar;
            z11 = z10;
            arrayList6 = arrayList3;
            fragmentTransitionImpl2 = fragmentTransitionImpl;
        }
        View view9 = view6;
        j.a aVar5 = aVar2;
        ArrayList<View> arrayList9 = arrayList6;
        SpecialEffectsController.e eVar9 = eVar7;
        ArrayList<View> arrayList10 = arrayList5;
        Rect rect3 = rect2;
        View view10 = view5;
        FragmentTransitionImpl fragmentTransitionImpl3 = fragmentTransitionImpl2;
        boolean z13 = false;
        SpecialEffectsController.e eVar10 = eVar8;
        ArrayList arrayList11 = new ArrayList();
        Object obj4 = null;
        Object obj5 = null;
        for (m mVar4 : list) {
            if (mVar4.d()) {
                hashMap.put(mVar4.b(), Boolean.FALSE);
                mVar4.a();
            } else {
                Object g6 = fragmentTransitionImpl3.g(mVar4.h());
                SpecialEffectsController.e b10 = mVar4.b();
                boolean z14 = (obj3 == null || !(b10 == eVar9 || b10 == eVar10)) ? z13 : true;
                if (g6 == null) {
                    if (!z14) {
                        hashMap.put(b10, Boolean.FALSE);
                        mVar4.a();
                    }
                    arrayList2 = arrayList9;
                    arrayList = arrayList10;
                    view = view10;
                    n10 = obj4;
                    eVar3 = eVar10;
                    view2 = view9;
                } else {
                    ArrayList<View> arrayList12 = new ArrayList<>();
                    Object obj6 = obj4;
                    t(arrayList12, b10.f().mView);
                    if (z14) {
                        if (b10 == eVar9) {
                            arrayList12.removeAll(arrayList10);
                        } else {
                            arrayList12.removeAll(arrayList9);
                        }
                    }
                    if (arrayList12.isEmpty()) {
                        fragmentTransitionImpl3.a(g6, view10);
                        arrayList2 = arrayList9;
                        arrayList = arrayList10;
                        view = view10;
                        eVar4 = b10;
                        obj2 = obj5;
                        eVar3 = eVar10;
                        obj = obj6;
                    } else {
                        fragmentTransitionImpl3.b(g6, arrayList12);
                        view = view10;
                        obj = obj6;
                        arrayList = arrayList10;
                        obj2 = obj5;
                        arrayList2 = arrayList9;
                        eVar3 = eVar10;
                        fragmentTransitionImpl3.t(g6, g6, arrayList12, null, null, null, null);
                        if (b10.e() == SpecialEffectsController.e.c.GONE) {
                            eVar4 = b10;
                            list2.remove(eVar4);
                            ArrayList<View> arrayList13 = new ArrayList<>(arrayList12);
                            arrayList13.remove(eVar4.f().mView);
                            fragmentTransitionImpl3.r(g6, eVar4.f().mView, arrayList13);
                            OneShotPreDrawListener.a(m(), new i(arrayList12));
                        } else {
                            eVar4 = b10;
                        }
                    }
                    if (eVar4.e() == SpecialEffectsController.e.c.VISIBLE) {
                        arrayList11.addAll(arrayList12);
                        if (z12) {
                            fragmentTransitionImpl3.u(g6, rect3);
                        }
                        view2 = view9;
                    } else {
                        view2 = view9;
                        fragmentTransitionImpl3.v(g6, view2);
                    }
                    hashMap.put(eVar4, Boolean.TRUE);
                    if (mVar4.j()) {
                        obj5 = fragmentTransitionImpl3.n(obj2, g6, null);
                        n10 = obj;
                    } else {
                        n10 = fragmentTransitionImpl3.n(obj, g6, null);
                        obj5 = obj2;
                    }
                }
                eVar10 = eVar3;
                obj4 = n10;
                view9 = view2;
                view10 = view;
                arrayList10 = arrayList;
                arrayList9 = arrayList2;
                z13 = false;
            }
        }
        ArrayList<View> arrayList14 = arrayList9;
        ArrayList<View> arrayList15 = arrayList10;
        SpecialEffectsController.e eVar11 = eVar10;
        Object m10 = fragmentTransitionImpl3.m(obj5, obj4, obj3);
        for (m mVar5 : list) {
            if (!mVar5.d()) {
                Object h10 = mVar5.h();
                SpecialEffectsController.e b11 = mVar5.b();
                boolean z15 = obj3 != null && (b11 == eVar9 || b11 == eVar11);
                if (h10 != null || z15) {
                    if (!ViewCompat.Q(m())) {
                        if (FragmentManager.H0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Container " + m() + " has not been laid out. Completing operation " + b11);
                        }
                        mVar5.a();
                    } else {
                        fragmentTransitionImpl3.w(mVar5.b().f(), m10, mVar5.c(), new j(mVar5));
                    }
                }
            }
        }
        if (!ViewCompat.Q(m())) {
            return hashMap;
        }
        FragmentTransition.A(arrayList11, 4);
        ArrayList<String> o10 = fragmentTransitionImpl3.o(arrayList14);
        fragmentTransitionImpl3.c(m(), m10);
        fragmentTransitionImpl3.y(m(), arrayList15, arrayList14, o10, aVar5);
        FragmentTransition.A(arrayList11, 0);
        fragmentTransitionImpl3.A(obj3, arrayList15, arrayList14);
        return hashMap;
    }

    @Override // androidx.fragment.app.SpecialEffectsController
    void f(List<SpecialEffectsController.e> list, boolean z10) {
        SpecialEffectsController.e eVar = null;
        SpecialEffectsController.e eVar2 = null;
        for (SpecialEffectsController.e eVar3 : list) {
            SpecialEffectsController.e.c c10 = SpecialEffectsController.e.c.c(eVar3.f().mView);
            int i10 = a.f2816a[eVar3.e().ordinal()];
            if (i10 != 1 && i10 != 2 && i10 != 3) {
                if (i10 == 4 && c10 != SpecialEffectsController.e.c.VISIBLE) {
                    eVar2 = eVar3;
                }
            } else if (c10 == SpecialEffectsController.e.c.VISIBLE && eVar == null) {
                eVar = eVar3;
            }
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList(list);
        for (SpecialEffectsController.e eVar4 : list) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            eVar4.j(cancellationSignal);
            arrayList.add(new k(eVar4, cancellationSignal, z10));
            CancellationSignal cancellationSignal2 = new CancellationSignal();
            eVar4.j(cancellationSignal2);
            boolean z11 = false;
            if (z10) {
                if (eVar4 != eVar) {
                    arrayList2.add(new m(eVar4, cancellationSignal2, z10, z11));
                    eVar4.a(new b(arrayList3, eVar4));
                }
                z11 = true;
                arrayList2.add(new m(eVar4, cancellationSignal2, z10, z11));
                eVar4.a(new b(arrayList3, eVar4));
            } else {
                if (eVar4 != eVar2) {
                    arrayList2.add(new m(eVar4, cancellationSignal2, z10, z11));
                    eVar4.a(new b(arrayList3, eVar4));
                }
                z11 = true;
                arrayList2.add(new m(eVar4, cancellationSignal2, z10, z11));
                eVar4.a(new b(arrayList3, eVar4));
            }
        }
        Map<SpecialEffectsController.e, Boolean> x10 = x(arrayList2, arrayList3, z10, eVar, eVar2);
        w(arrayList, arrayList3, x10.containsValue(Boolean.TRUE), x10);
        Iterator<SpecialEffectsController.e> it = arrayList3.iterator();
        while (it.hasNext()) {
            s(it.next());
        }
        arrayList3.clear();
    }

    void s(SpecialEffectsController.e eVar) {
        eVar.e().a(eVar.f().mView);
    }

    void t(ArrayList<View> arrayList, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (ViewGroupCompat.a(viewGroup)) {
                if (arrayList.contains(view)) {
                    return;
                }
                arrayList.add(viewGroup);
                return;
            }
            int childCount = viewGroup.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = viewGroup.getChildAt(i10);
                if (childAt.getVisibility() == 0) {
                    t(arrayList, childAt);
                }
            }
            return;
        }
        if (arrayList.contains(view)) {
            return;
        }
        arrayList.add(view);
    }

    void u(Map<String, View> map, View view) {
        String G = ViewCompat.G(view);
        if (G != null) {
            map.put(G, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = viewGroup.getChildAt(i10);
                if (childAt.getVisibility() == 0) {
                    u(map, childAt);
                }
            }
        }
    }

    void v(j.a<String, View> aVar, Collection<String> collection) {
        Iterator<Map.Entry<String, View>> it = aVar.entrySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(ViewCompat.G(it.next().getValue()))) {
                it.remove();
            }
        }
    }
}
