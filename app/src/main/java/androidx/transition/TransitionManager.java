package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.core.view.ViewCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: TransitionManager.java */
/* renamed from: androidx.transition.s, reason: use source file name */
/* loaded from: classes.dex */
public class TransitionManager {

    /* renamed from: a, reason: collision with root package name */
    private static Transition f4139a = new AutoTransition();

    /* renamed from: b, reason: collision with root package name */
    private static ThreadLocal<WeakReference<j.a<ViewGroup, ArrayList<Transition>>>> f4140b = new ThreadLocal<>();

    /* renamed from: c, reason: collision with root package name */
    static ArrayList<ViewGroup> f4141c = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TransitionManager.java */
    /* renamed from: androidx.transition.s$a */
    /* loaded from: classes.dex */
    public static class a implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

        /* renamed from: e, reason: collision with root package name */
        Transition f4142e;

        /* renamed from: f, reason: collision with root package name */
        ViewGroup f4143f;

        /* compiled from: TransitionManager.java */
        /* renamed from: androidx.transition.s$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0011a extends TransitionListenerAdapter {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ j.a f4144a;

            C0011a(j.a aVar) {
                this.f4144a = aVar;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // androidx.transition.Transition.g
            public void c(Transition transition) {
                ((ArrayList) this.f4144a.get(a.this.f4143f)).remove(transition);
                transition.removeListener(this);
            }
        }

        a(Transition transition, ViewGroup viewGroup) {
            this.f4142e = transition;
            this.f4143f = viewGroup;
        }

        private void a() {
            this.f4143f.getViewTreeObserver().removeOnPreDrawListener(this);
            this.f4143f.removeOnAttachStateChangeListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            a();
            if (!TransitionManager.f4141c.remove(this.f4143f)) {
                return true;
            }
            j.a<ViewGroup, ArrayList<Transition>> b10 = TransitionManager.b();
            ArrayList<Transition> arrayList = b10.get(this.f4143f);
            ArrayList arrayList2 = null;
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                b10.put(this.f4143f, arrayList);
            } else if (arrayList.size() > 0) {
                arrayList2 = new ArrayList(arrayList);
            }
            arrayList.add(this.f4142e);
            this.f4142e.addListener(new C0011a(b10));
            this.f4142e.captureValues(this.f4143f, false);
            if (arrayList2 != null) {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    ((Transition) it.next()).resume(this.f4143f);
                }
            }
            this.f4142e.playTransition(this.f4143f);
            return true;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            a();
            TransitionManager.f4141c.remove(this.f4143f);
            ArrayList<Transition> arrayList = TransitionManager.b().get(this.f4143f);
            if (arrayList != null && arrayList.size() > 0) {
                Iterator<Transition> it = arrayList.iterator();
                while (it.hasNext()) {
                    it.next().resume(this.f4143f);
                }
            }
            this.f4142e.clearValues(true);
        }
    }

    public static void a(ViewGroup viewGroup, Transition transition) {
        if (f4141c.contains(viewGroup) || !ViewCompat.Q(viewGroup)) {
            return;
        }
        f4141c.add(viewGroup);
        if (transition == null) {
            transition = f4139a;
        }
        Transition mo0clone = transition.mo0clone();
        d(viewGroup, mo0clone);
        Scene.c(viewGroup, null);
        c(viewGroup, mo0clone);
    }

    static j.a<ViewGroup, ArrayList<Transition>> b() {
        j.a<ViewGroup, ArrayList<Transition>> aVar;
        WeakReference<j.a<ViewGroup, ArrayList<Transition>>> weakReference = f4140b.get();
        if (weakReference != null && (aVar = weakReference.get()) != null) {
            return aVar;
        }
        j.a<ViewGroup, ArrayList<Transition>> aVar2 = new j.a<>();
        f4140b.set(new WeakReference<>(aVar2));
        return aVar2;
    }

    private static void c(ViewGroup viewGroup, Transition transition) {
        if (transition == null || viewGroup == null) {
            return;
        }
        a aVar = new a(transition, viewGroup);
        viewGroup.addOnAttachStateChangeListener(aVar);
        viewGroup.getViewTreeObserver().addOnPreDrawListener(aVar);
    }

    private static void d(ViewGroup viewGroup, Transition transition) {
        ArrayList<Transition> arrayList = b().get(viewGroup);
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<Transition> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().pause(viewGroup);
            }
        }
        if (transition != null) {
            transition.captureValues(viewGroup, true);
        }
        Scene b10 = Scene.b(viewGroup);
        if (b10 != null) {
            b10.a();
        }
    }
}
