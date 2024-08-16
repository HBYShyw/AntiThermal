package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransitionImpl;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.List;

/* compiled from: FragmentTransitionSupport.java */
@SuppressLint({"RestrictedApi"})
/* renamed from: androidx.transition.e, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentTransitionSupport extends FragmentTransitionImpl {

    /* compiled from: FragmentTransitionSupport.java */
    /* renamed from: androidx.transition.e$a */
    /* loaded from: classes.dex */
    class a extends Transition.f {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Rect f4097a;

        a(Rect rect) {
            this.f4097a = rect;
        }

        @Override // androidx.transition.Transition.f
        public Rect a(Transition transition) {
            return this.f4097a;
        }
    }

    /* compiled from: FragmentTransitionSupport.java */
    /* renamed from: androidx.transition.e$b */
    /* loaded from: classes.dex */
    class b implements Transition.g {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f4099a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ArrayList f4100b;

        b(View view, ArrayList arrayList) {
            this.f4099a = view;
            this.f4100b = arrayList;
        }

        @Override // androidx.transition.Transition.g
        public void a(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void b(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            transition.removeListener(this);
            this.f4099a.setVisibility(8);
            int size = this.f4100b.size();
            for (int i10 = 0; i10 < size; i10++) {
                ((View) this.f4100b.get(i10)).setVisibility(0);
            }
        }

        @Override // androidx.transition.Transition.g
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void e(Transition transition) {
        }
    }

    /* compiled from: FragmentTransitionSupport.java */
    /* renamed from: androidx.transition.e$c */
    /* loaded from: classes.dex */
    class c extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Object f4102a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ArrayList f4103b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ Object f4104c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ ArrayList f4105d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Object f4106e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ArrayList f4107f;

        c(Object obj, ArrayList arrayList, Object obj2, ArrayList arrayList2, Object obj3, ArrayList arrayList3) {
            this.f4102a = obj;
            this.f4103b = arrayList;
            this.f4104c = obj2;
            this.f4105d = arrayList2;
            this.f4106e = obj3;
            this.f4107f = arrayList3;
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void a(Transition transition) {
            Object obj = this.f4102a;
            if (obj != null) {
                FragmentTransitionSupport.this.q(obj, this.f4103b, null);
            }
            Object obj2 = this.f4104c;
            if (obj2 != null) {
                FragmentTransitionSupport.this.q(obj2, this.f4105d, null);
            }
            Object obj3 = this.f4106e;
            if (obj3 != null) {
                FragmentTransitionSupport.this.q(obj3, this.f4107f, null);
            }
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            transition.removeListener(this);
        }
    }

    /* compiled from: FragmentTransitionSupport.java */
    /* renamed from: androidx.transition.e$d */
    /* loaded from: classes.dex */
    class d extends Transition.f {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Rect f4109a;

        d(Rect rect) {
            this.f4109a = rect;
        }

        @Override // androidx.transition.Transition.f
        public Rect a(Transition transition) {
            Rect rect = this.f4109a;
            if (rect == null || rect.isEmpty()) {
                return null;
            }
            return this.f4109a;
        }
    }

    private static boolean C(Transition transition) {
        return (FragmentTransitionImpl.l(transition.getTargetIds()) && FragmentTransitionImpl.l(transition.getTargetNames()) && FragmentTransitionImpl.l(transition.getTargetTypes())) ? false : true;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void A(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        TransitionSet transitionSet = (TransitionSet) obj;
        if (transitionSet != null) {
            transitionSet.getTargets().clear();
            transitionSet.getTargets().addAll(arrayList2);
            q(transitionSet, arrayList, arrayList2);
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public Object B(Object obj) {
        if (obj == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.f((Transition) obj);
        return transitionSet;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void a(Object obj, View view) {
        if (obj != null) {
            ((Transition) obj).addTarget(view);
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void b(Object obj, ArrayList<View> arrayList) {
        Transition transition = (Transition) obj;
        if (transition == null) {
            return;
        }
        int i10 = 0;
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int i11 = transitionSet.i();
            while (i10 < i11) {
                b(transitionSet.h(i10), arrayList);
                i10++;
            }
            return;
        }
        if (C(transition) || !FragmentTransitionImpl.l(transition.getTargets())) {
            return;
        }
        int size = arrayList.size();
        while (i10 < size) {
            transition.addTarget(arrayList.get(i10));
            i10++;
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void c(ViewGroup viewGroup, Object obj) {
        TransitionManager.a(viewGroup, (Transition) obj);
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public boolean e(Object obj) {
        return obj instanceof Transition;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public Object g(Object obj) {
        if (obj != null) {
            return ((Transition) obj).mo0clone();
        }
        return null;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public Object m(Object obj, Object obj2, Object obj3) {
        Transition transition = (Transition) obj;
        Transition transition2 = (Transition) obj2;
        Transition transition3 = (Transition) obj3;
        if (transition != null && transition2 != null) {
            transition = new TransitionSet().f(transition).f(transition2).r(1);
        } else if (transition == null) {
            transition = transition2 != null ? transition2 : null;
        }
        if (transition3 == null) {
            return transition;
        }
        TransitionSet transitionSet = new TransitionSet();
        if (transition != null) {
            transitionSet.f(transition);
        }
        transitionSet.f(transition3);
        return transitionSet;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public Object n(Object obj, Object obj2, Object obj3) {
        TransitionSet transitionSet = new TransitionSet();
        if (obj != null) {
            transitionSet.f((Transition) obj);
        }
        if (obj2 != null) {
            transitionSet.f((Transition) obj2);
        }
        if (obj3 != null) {
            transitionSet.f((Transition) obj3);
        }
        return transitionSet;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void p(Object obj, View view) {
        if (obj != null) {
            ((Transition) obj).removeTarget(view);
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void q(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        Transition transition = (Transition) obj;
        int i10 = 0;
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int i11 = transitionSet.i();
            while (i10 < i11) {
                q(transitionSet.h(i10), arrayList, arrayList2);
                i10++;
            }
            return;
        }
        if (C(transition)) {
            return;
        }
        List<View> targets = transition.getTargets();
        if (targets.size() == arrayList.size() && targets.containsAll(arrayList)) {
            int size = arrayList2 == null ? 0 : arrayList2.size();
            while (i10 < size) {
                transition.addTarget(arrayList2.get(i10));
                i10++;
            }
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                transition.removeTarget(arrayList.get(size2));
            }
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void r(Object obj, View view, ArrayList<View> arrayList) {
        ((Transition) obj).addListener(new b(view, arrayList));
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void t(Object obj, Object obj2, ArrayList<View> arrayList, Object obj3, ArrayList<View> arrayList2, Object obj4, ArrayList<View> arrayList3) {
        ((Transition) obj).addListener(new c(obj2, arrayList, obj3, arrayList2, obj4, arrayList3));
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void u(Object obj, Rect rect) {
        if (obj != null) {
            ((Transition) obj).setEpicenterCallback(new d(rect));
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void v(Object obj, View view) {
        if (view != null) {
            Rect rect = new Rect();
            k(view, rect);
            ((Transition) obj).setEpicenterCallback(new a(rect));
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public void z(Object obj, View view, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet) obj;
        List<View> targets = transitionSet.getTargets();
        targets.clear();
        int size = arrayList.size();
        for (int i10 = 0; i10 < size; i10++) {
            FragmentTransitionImpl.d(targets, arrayList.get(i10));
        }
        targets.add(view);
        arrayList.add(view);
        b(transitionSet, arrayList);
    }
}
