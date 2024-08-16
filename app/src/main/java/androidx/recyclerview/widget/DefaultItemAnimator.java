package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: DefaultItemAnimator.java */
/* renamed from: androidx.recyclerview.widget.e, reason: use source file name */
/* loaded from: classes.dex */
public class DefaultItemAnimator extends SimpleItemAnimator {

    /* renamed from: s, reason: collision with root package name */
    private static TimeInterpolator f3677s;

    /* renamed from: h, reason: collision with root package name */
    private ArrayList<RecyclerView.c0> f3678h = new ArrayList<>();

    /* renamed from: i, reason: collision with root package name */
    private ArrayList<RecyclerView.c0> f3679i = new ArrayList<>();

    /* renamed from: j, reason: collision with root package name */
    private ArrayList<j> f3680j = new ArrayList<>();

    /* renamed from: k, reason: collision with root package name */
    private ArrayList<i> f3681k = new ArrayList<>();

    /* renamed from: l, reason: collision with root package name */
    ArrayList<ArrayList<RecyclerView.c0>> f3682l = new ArrayList<>();

    /* renamed from: m, reason: collision with root package name */
    ArrayList<ArrayList<j>> f3683m = new ArrayList<>();

    /* renamed from: n, reason: collision with root package name */
    ArrayList<ArrayList<i>> f3684n = new ArrayList<>();

    /* renamed from: o, reason: collision with root package name */
    ArrayList<RecyclerView.c0> f3685o = new ArrayList<>();

    /* renamed from: p, reason: collision with root package name */
    ArrayList<RecyclerView.c0> f3686p = new ArrayList<>();

    /* renamed from: q, reason: collision with root package name */
    ArrayList<RecyclerView.c0> f3687q = new ArrayList<>();

    /* renamed from: r, reason: collision with root package name */
    ArrayList<RecyclerView.c0> f3688r = new ArrayList<>();

    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$a */
    /* loaded from: classes.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ArrayList f3689e;

        a(ArrayList arrayList) {
            this.f3689e = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.f3689e.iterator();
            while (it.hasNext()) {
                j jVar = (j) it.next();
                DefaultItemAnimator.this.S(jVar.f3723a, jVar.f3724b, jVar.f3725c, jVar.f3726d, jVar.f3727e);
            }
            this.f3689e.clear();
            DefaultItemAnimator.this.f3683m.remove(this.f3689e);
        }
    }

    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$b */
    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ArrayList f3691e;

        b(ArrayList arrayList) {
            this.f3691e = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.f3691e.iterator();
            while (it.hasNext()) {
                DefaultItemAnimator.this.R((i) it.next());
            }
            this.f3691e.clear();
            DefaultItemAnimator.this.f3684n.remove(this.f3691e);
        }
    }

    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$c */
    /* loaded from: classes.dex */
    class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ArrayList f3693e;

        c(ArrayList arrayList) {
            this.f3693e = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.f3693e.iterator();
            while (it.hasNext()) {
                DefaultItemAnimator.this.Q((RecyclerView.c0) it.next());
            }
            this.f3693e.clear();
            DefaultItemAnimator.this.f3682l.remove(this.f3693e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$d */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RecyclerView.c0 f3695a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f3696b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f3697c;

        d(RecyclerView.c0 c0Var, ViewPropertyAnimator viewPropertyAnimator, View view) {
            this.f3695a = c0Var;
            this.f3696b = viewPropertyAnimator;
            this.f3697c = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f3696b.setListener(null);
            this.f3697c.setAlpha(1.0f);
            DefaultItemAnimator.this.G(this.f3695a);
            DefaultItemAnimator.this.f3687q.remove(this.f3695a);
            DefaultItemAnimator.this.V();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            DefaultItemAnimator.this.H(this.f3695a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$e */
    /* loaded from: classes.dex */
    public class e extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RecyclerView.c0 f3699a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f3700b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f3701c;

        e(RecyclerView.c0 c0Var, View view, ViewPropertyAnimator viewPropertyAnimator) {
            this.f3699a = c0Var;
            this.f3700b = view;
            this.f3701c = viewPropertyAnimator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f3700b.setAlpha(1.0f);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f3701c.setListener(null);
            DefaultItemAnimator.this.A(this.f3699a);
            DefaultItemAnimator.this.f3685o.remove(this.f3699a);
            DefaultItemAnimator.this.V();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            DefaultItemAnimator.this.B(this.f3699a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$f */
    /* loaded from: classes.dex */
    public class f extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RecyclerView.c0 f3703a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f3704b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f3705c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f3706d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f3707e;

        f(RecyclerView.c0 c0Var, int i10, View view, int i11, ViewPropertyAnimator viewPropertyAnimator) {
            this.f3703a = c0Var;
            this.f3704b = i10;
            this.f3705c = view;
            this.f3706d = i11;
            this.f3707e = viewPropertyAnimator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.f3704b != 0) {
                this.f3705c.setTranslationX(0.0f);
            }
            if (this.f3706d != 0) {
                this.f3705c.setTranslationY(0.0f);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f3707e.setListener(null);
            DefaultItemAnimator.this.E(this.f3703a);
            DefaultItemAnimator.this.f3686p.remove(this.f3703a);
            DefaultItemAnimator.this.V();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            DefaultItemAnimator.this.F(this.f3703a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$g */
    /* loaded from: classes.dex */
    public class g extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ i f3709a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f3710b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f3711c;

        g(i iVar, ViewPropertyAnimator viewPropertyAnimator, View view) {
            this.f3709a = iVar;
            this.f3710b = viewPropertyAnimator;
            this.f3711c = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f3710b.setListener(null);
            this.f3711c.setAlpha(1.0f);
            this.f3711c.setTranslationX(0.0f);
            this.f3711c.setTranslationY(0.0f);
            DefaultItemAnimator.this.C(this.f3709a.f3717a, true);
            DefaultItemAnimator.this.f3688r.remove(this.f3709a.f3717a);
            DefaultItemAnimator.this.V();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            DefaultItemAnimator.this.D(this.f3709a.f3717a, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$h */
    /* loaded from: classes.dex */
    public class h extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ i f3713a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f3714b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f3715c;

        h(i iVar, ViewPropertyAnimator viewPropertyAnimator, View view) {
            this.f3713a = iVar;
            this.f3714b = viewPropertyAnimator;
            this.f3715c = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f3714b.setListener(null);
            this.f3715c.setAlpha(1.0f);
            this.f3715c.setTranslationX(0.0f);
            this.f3715c.setTranslationY(0.0f);
            DefaultItemAnimator.this.C(this.f3713a.f3718b, false);
            DefaultItemAnimator.this.f3688r.remove(this.f3713a.f3718b);
            DefaultItemAnimator.this.V();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            DefaultItemAnimator.this.D(this.f3713a.f3718b, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$j */
    /* loaded from: classes.dex */
    public static class j {

        /* renamed from: a, reason: collision with root package name */
        public RecyclerView.c0 f3723a;

        /* renamed from: b, reason: collision with root package name */
        public int f3724b;

        /* renamed from: c, reason: collision with root package name */
        public int f3725c;

        /* renamed from: d, reason: collision with root package name */
        public int f3726d;

        /* renamed from: e, reason: collision with root package name */
        public int f3727e;

        j(RecyclerView.c0 c0Var, int i10, int i11, int i12, int i13) {
            this.f3723a = c0Var;
            this.f3724b = i10;
            this.f3725c = i11;
            this.f3726d = i12;
            this.f3727e = i13;
        }
    }

    private void T(RecyclerView.c0 c0Var) {
        View view = c0Var.itemView;
        ViewPropertyAnimator animate = view.animate();
        this.f3687q.add(c0Var);
        animate.setDuration(o()).alpha(0.0f).setListener(new d(c0Var, animate, view)).start();
    }

    private void W(List<i> list, RecyclerView.c0 c0Var) {
        for (int size = list.size() - 1; size >= 0; size--) {
            i iVar = list.get(size);
            if (Y(iVar, c0Var) && iVar.f3717a == null && iVar.f3718b == null) {
                list.remove(iVar);
            }
        }
    }

    private void X(i iVar) {
        RecyclerView.c0 c0Var = iVar.f3717a;
        if (c0Var != null) {
            Y(iVar, c0Var);
        }
        RecyclerView.c0 c0Var2 = iVar.f3718b;
        if (c0Var2 != null) {
            Y(iVar, c0Var2);
        }
    }

    private boolean Y(i iVar, RecyclerView.c0 c0Var) {
        boolean z10 = false;
        if (iVar.f3718b == c0Var) {
            iVar.f3718b = null;
        } else {
            if (iVar.f3717a != c0Var) {
                return false;
            }
            iVar.f3717a = null;
            z10 = true;
        }
        c0Var.itemView.setAlpha(1.0f);
        c0Var.itemView.setTranslationX(0.0f);
        c0Var.itemView.setTranslationY(0.0f);
        C(c0Var, z10);
        return true;
    }

    private void Z(RecyclerView.c0 c0Var) {
        if (f3677s == null) {
            f3677s = new ValueAnimator().getInterpolator();
        }
        c0Var.itemView.animate().setInterpolator(f3677s);
        j(c0Var);
    }

    void Q(RecyclerView.c0 c0Var) {
        View view = c0Var.itemView;
        ViewPropertyAnimator animate = view.animate();
        this.f3685o.add(c0Var);
        animate.alpha(1.0f).setDuration(l()).setListener(new e(c0Var, view, animate)).start();
    }

    void R(i iVar) {
        RecyclerView.c0 c0Var = iVar.f3717a;
        View view = c0Var == null ? null : c0Var.itemView;
        RecyclerView.c0 c0Var2 = iVar.f3718b;
        View view2 = c0Var2 != null ? c0Var2.itemView : null;
        if (view != null) {
            ViewPropertyAnimator duration = view.animate().setDuration(m());
            this.f3688r.add(iVar.f3717a);
            duration.translationX(iVar.f3721e - iVar.f3719c);
            duration.translationY(iVar.f3722f - iVar.f3720d);
            duration.alpha(0.0f).setListener(new g(iVar, duration, view)).start();
        }
        if (view2 != null) {
            ViewPropertyAnimator animate = view2.animate();
            this.f3688r.add(iVar.f3718b);
            animate.translationX(0.0f).translationY(0.0f).setDuration(m()).alpha(1.0f).setListener(new h(iVar, animate, view2)).start();
        }
    }

    void S(RecyclerView.c0 c0Var, int i10, int i11, int i12, int i13) {
        View view = c0Var.itemView;
        int i14 = i12 - i10;
        int i15 = i13 - i11;
        if (i14 != 0) {
            view.animate().translationX(0.0f);
        }
        if (i15 != 0) {
            view.animate().translationY(0.0f);
        }
        ViewPropertyAnimator animate = view.animate();
        this.f3686p.add(c0Var);
        animate.setDuration(n()).setListener(new f(c0Var, i14, view, i15, animate)).start();
    }

    void U(List<RecyclerView.c0> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            list.get(size).itemView.animate().cancel();
        }
    }

    void V() {
        if (p()) {
            return;
        }
        i();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean g(RecyclerView.c0 c0Var, List<Object> list) {
        return !list.isEmpty() || super.g(c0Var, list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public void j(RecyclerView.c0 c0Var) {
        View view = c0Var.itemView;
        view.animate().cancel();
        int size = this.f3680j.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            if (this.f3680j.get(size).f3723a == c0Var) {
                view.setTranslationY(0.0f);
                view.setTranslationX(0.0f);
                E(c0Var);
                this.f3680j.remove(size);
            }
        }
        W(this.f3681k, c0Var);
        if (this.f3678h.remove(c0Var)) {
            view.setAlpha(1.0f);
            G(c0Var);
        }
        if (this.f3679i.remove(c0Var)) {
            view.setAlpha(1.0f);
            A(c0Var);
        }
        for (int size2 = this.f3684n.size() - 1; size2 >= 0; size2--) {
            ArrayList<i> arrayList = this.f3684n.get(size2);
            W(arrayList, c0Var);
            if (arrayList.isEmpty()) {
                this.f3684n.remove(size2);
            }
        }
        for (int size3 = this.f3683m.size() - 1; size3 >= 0; size3--) {
            ArrayList<j> arrayList2 = this.f3683m.get(size3);
            int size4 = arrayList2.size() - 1;
            while (true) {
                if (size4 < 0) {
                    break;
                }
                if (arrayList2.get(size4).f3723a == c0Var) {
                    view.setTranslationY(0.0f);
                    view.setTranslationX(0.0f);
                    E(c0Var);
                    arrayList2.remove(size4);
                    if (arrayList2.isEmpty()) {
                        this.f3683m.remove(size3);
                    }
                } else {
                    size4--;
                }
            }
        }
        for (int size5 = this.f3682l.size() - 1; size5 >= 0; size5--) {
            ArrayList<RecyclerView.c0> arrayList3 = this.f3682l.get(size5);
            if (arrayList3.remove(c0Var)) {
                view.setAlpha(1.0f);
                A(c0Var);
                if (arrayList3.isEmpty()) {
                    this.f3682l.remove(size5);
                }
            }
        }
        this.f3687q.remove(c0Var);
        this.f3685o.remove(c0Var);
        this.f3688r.remove(c0Var);
        this.f3686p.remove(c0Var);
        V();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public void k() {
        int size = this.f3680j.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            j jVar = this.f3680j.get(size);
            View view = jVar.f3723a.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            E(jVar.f3723a);
            this.f3680j.remove(size);
        }
        for (int size2 = this.f3678h.size() - 1; size2 >= 0; size2--) {
            G(this.f3678h.get(size2));
            this.f3678h.remove(size2);
        }
        int size3 = this.f3679i.size();
        while (true) {
            size3--;
            if (size3 < 0) {
                break;
            }
            RecyclerView.c0 c0Var = this.f3679i.get(size3);
            c0Var.itemView.setAlpha(1.0f);
            A(c0Var);
            this.f3679i.remove(size3);
        }
        for (int size4 = this.f3681k.size() - 1; size4 >= 0; size4--) {
            X(this.f3681k.get(size4));
        }
        this.f3681k.clear();
        if (p()) {
            for (int size5 = this.f3683m.size() - 1; size5 >= 0; size5--) {
                ArrayList<j> arrayList = this.f3683m.get(size5);
                for (int size6 = arrayList.size() - 1; size6 >= 0; size6--) {
                    j jVar2 = arrayList.get(size6);
                    View view2 = jVar2.f3723a.itemView;
                    view2.setTranslationY(0.0f);
                    view2.setTranslationX(0.0f);
                    E(jVar2.f3723a);
                    arrayList.remove(size6);
                    if (arrayList.isEmpty()) {
                        this.f3683m.remove(arrayList);
                    }
                }
            }
            for (int size7 = this.f3682l.size() - 1; size7 >= 0; size7--) {
                ArrayList<RecyclerView.c0> arrayList2 = this.f3682l.get(size7);
                for (int size8 = arrayList2.size() - 1; size8 >= 0; size8--) {
                    RecyclerView.c0 c0Var2 = arrayList2.get(size8);
                    c0Var2.itemView.setAlpha(1.0f);
                    A(c0Var2);
                    arrayList2.remove(size8);
                    if (arrayList2.isEmpty()) {
                        this.f3682l.remove(arrayList2);
                    }
                }
            }
            for (int size9 = this.f3684n.size() - 1; size9 >= 0; size9--) {
                ArrayList<i> arrayList3 = this.f3684n.get(size9);
                for (int size10 = arrayList3.size() - 1; size10 >= 0; size10--) {
                    X(arrayList3.get(size10));
                    if (arrayList3.isEmpty()) {
                        this.f3684n.remove(arrayList3);
                    }
                }
            }
            U(this.f3687q);
            U(this.f3686p);
            U(this.f3685o);
            U(this.f3688r);
            i();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean p() {
        return (this.f3679i.isEmpty() && this.f3681k.isEmpty() && this.f3680j.isEmpty() && this.f3678h.isEmpty() && this.f3686p.isEmpty() && this.f3687q.isEmpty() && this.f3685o.isEmpty() && this.f3688r.isEmpty() && this.f3683m.isEmpty() && this.f3682l.isEmpty() && this.f3684n.isEmpty()) ? false : true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public void u() {
        boolean z10 = !this.f3678h.isEmpty();
        boolean z11 = !this.f3680j.isEmpty();
        boolean z12 = !this.f3681k.isEmpty();
        boolean z13 = !this.f3679i.isEmpty();
        if (z10 || z11 || z13 || z12) {
            Iterator<RecyclerView.c0> it = this.f3678h.iterator();
            while (it.hasNext()) {
                T(it.next());
            }
            this.f3678h.clear();
            if (z11) {
                ArrayList<j> arrayList = new ArrayList<>();
                arrayList.addAll(this.f3680j);
                this.f3683m.add(arrayList);
                this.f3680j.clear();
                a aVar = new a(arrayList);
                if (z10) {
                    ViewCompat.d0(arrayList.get(0).f3723a.itemView, aVar, o());
                } else {
                    aVar.run();
                }
            }
            if (z12) {
                ArrayList<i> arrayList2 = new ArrayList<>();
                arrayList2.addAll(this.f3681k);
                this.f3684n.add(arrayList2);
                this.f3681k.clear();
                b bVar = new b(arrayList2);
                if (z10) {
                    ViewCompat.d0(arrayList2.get(0).f3717a.itemView, bVar, o());
                } else {
                    bVar.run();
                }
            }
            if (z13) {
                ArrayList<RecyclerView.c0> arrayList3 = new ArrayList<>();
                arrayList3.addAll(this.f3679i);
                this.f3682l.add(arrayList3);
                this.f3679i.clear();
                c cVar = new c(arrayList3);
                if (!z10 && !z11 && !z12) {
                    cVar.run();
                } else {
                    ViewCompat.d0(arrayList3.get(0).itemView, cVar, (z10 ? o() : 0L) + Math.max(z11 ? n() : 0L, z12 ? m() : 0L));
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean w(RecyclerView.c0 c0Var) {
        Z(c0Var);
        c0Var.itemView.setAlpha(0.0f);
        this.f3679i.add(c0Var);
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean x(RecyclerView.c0 c0Var, RecyclerView.c0 c0Var2, int i10, int i11, int i12, int i13) {
        if (c0Var == c0Var2) {
            return y(c0Var, i10, i11, i12, i13);
        }
        float translationX = c0Var.itemView.getTranslationX();
        float translationY = c0Var.itemView.getTranslationY();
        float alpha = c0Var.itemView.getAlpha();
        Z(c0Var);
        int i14 = (int) ((i12 - i10) - translationX);
        int i15 = (int) ((i13 - i11) - translationY);
        c0Var.itemView.setTranslationX(translationX);
        c0Var.itemView.setTranslationY(translationY);
        c0Var.itemView.setAlpha(alpha);
        if (c0Var2 != null) {
            Z(c0Var2);
            c0Var2.itemView.setTranslationX(-i14);
            c0Var2.itemView.setTranslationY(-i15);
            c0Var2.itemView.setAlpha(0.0f);
        }
        this.f3681k.add(new i(c0Var, c0Var2, i10, i11, i12, i13));
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean y(RecyclerView.c0 c0Var, int i10, int i11, int i12, int i13) {
        View view = c0Var.itemView;
        int translationX = i10 + ((int) view.getTranslationX());
        int translationY = i11 + ((int) c0Var.itemView.getTranslationY());
        Z(c0Var);
        int i14 = i12 - translationX;
        int i15 = i13 - translationY;
        if (i14 == 0 && i15 == 0) {
            E(c0Var);
            return false;
        }
        if (i14 != 0) {
            view.setTranslationX(-i14);
        }
        if (i15 != 0) {
            view.setTranslationY(-i15);
        }
        this.f3680j.add(new j(c0Var, translationX, translationY, i12, i13));
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean z(RecyclerView.c0 c0Var) {
        Z(c0Var);
        this.f3678h.add(c0Var);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DefaultItemAnimator.java */
    /* renamed from: androidx.recyclerview.widget.e$i */
    /* loaded from: classes.dex */
    public static class i {

        /* renamed from: a, reason: collision with root package name */
        public RecyclerView.c0 f3717a;

        /* renamed from: b, reason: collision with root package name */
        public RecyclerView.c0 f3718b;

        /* renamed from: c, reason: collision with root package name */
        public int f3719c;

        /* renamed from: d, reason: collision with root package name */
        public int f3720d;

        /* renamed from: e, reason: collision with root package name */
        public int f3721e;

        /* renamed from: f, reason: collision with root package name */
        public int f3722f;

        private i(RecyclerView.c0 c0Var, RecyclerView.c0 c0Var2) {
            this.f3717a = c0Var;
            this.f3718b = c0Var2;
        }

        public String toString() {
            return "ChangeInfo{oldHolder=" + this.f3717a + ", newHolder=" + this.f3718b + ", fromX=" + this.f3719c + ", fromY=" + this.f3720d + ", toX=" + this.f3721e + ", toY=" + this.f3722f + '}';
        }

        i(RecyclerView.c0 c0Var, RecyclerView.c0 c0Var2, int i10, int i11, int i12, int i13) {
            this(c0Var, c0Var2);
            this.f3719c = i10;
            this.f3720d = i11;
            this.f3721e = i12;
            this.f3722f = i13;
        }
    }
}
