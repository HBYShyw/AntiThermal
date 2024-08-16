package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.R$animator;
import androidx.fragment.R$id;
import androidx.fragment.app.FragmentTransition;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentAnim.java */
/* renamed from: androidx.fragment.app.d, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentAnim {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentAnim.java */
    /* renamed from: androidx.fragment.app.d$a */
    /* loaded from: classes.dex */
    public class a implements CancellationSignal.b {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Fragment f2880a;

        a(Fragment fragment) {
            this.f2880a = fragment;
        }

        @Override // androidx.core.os.CancellationSignal.b
        public void a() {
            if (this.f2880a.getAnimatingAway() != null) {
                View animatingAway = this.f2880a.getAnimatingAway();
                this.f2880a.setAnimatingAway(null);
                animatingAway.clearAnimation();
            }
            this.f2880a.setAnimator(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentAnim.java */
    /* renamed from: androidx.fragment.app.d$b */
    /* loaded from: classes.dex */
    public class b implements Animation.AnimationListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ViewGroup f2881e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Fragment f2882f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ FragmentTransition.g f2883g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ CancellationSignal f2884h;

        /* compiled from: FragmentAnim.java */
        /* renamed from: androidx.fragment.app.d$b$a */
        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (b.this.f2882f.getAnimatingAway() != null) {
                    b.this.f2882f.setAnimatingAway(null);
                    b bVar = b.this;
                    bVar.f2883g.a(bVar.f2882f, bVar.f2884h);
                }
            }
        }

        b(ViewGroup viewGroup, Fragment fragment, FragmentTransition.g gVar, CancellationSignal cancellationSignal) {
            this.f2881e = viewGroup;
            this.f2882f = fragment;
            this.f2883g = gVar;
            this.f2884h = cancellationSignal;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            this.f2881e.post(new a());
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentAnim.java */
    /* renamed from: androidx.fragment.app.d$c */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f2886a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f2887b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ Fragment f2888c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ FragmentTransition.g f2889d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ CancellationSignal f2890e;

        c(ViewGroup viewGroup, View view, Fragment fragment, FragmentTransition.g gVar, CancellationSignal cancellationSignal) {
            this.f2886a = viewGroup;
            this.f2887b = view;
            this.f2888c = fragment;
            this.f2889d = gVar;
            this.f2890e = cancellationSignal;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f2886a.endViewTransition(this.f2887b);
            Animator animator2 = this.f2888c.getAnimator();
            this.f2888c.setAnimator(null);
            if (animator2 == null || this.f2886a.indexOfChild(this.f2887b) >= 0) {
                return;
            }
            this.f2889d.a(this.f2888c, this.f2890e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Fragment fragment, d dVar, FragmentTransition.g gVar) {
        View view = fragment.mView;
        ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.c(new a(fragment));
        gVar.b(fragment, cancellationSignal);
        if (dVar.f2891a != null) {
            e eVar = new e(dVar.f2891a, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            eVar.setAnimationListener(new b(viewGroup, fragment, gVar, cancellationSignal));
            fragment.mView.startAnimation(eVar);
            return;
        }
        Animator animator = dVar.f2892b;
        fragment.setAnimator(animator);
        animator.addListener(new c(viewGroup, view, fragment, gVar, cancellationSignal));
        animator.setTarget(fragment.mView);
        animator.start();
    }

    private static int b(Fragment fragment, boolean z10, boolean z11) {
        if (z11) {
            if (z10) {
                return fragment.getPopEnterAnim();
            }
            return fragment.getPopExitAnim();
        }
        if (z10) {
            return fragment.getEnterAnim();
        }
        return fragment.getExitAnim();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static d c(Context context, Fragment fragment, boolean z10, boolean z11) {
        int nextTransition = fragment.getNextTransition();
        int b10 = b(fragment, z10, z11);
        boolean z12 = false;
        fragment.setAnimations(0, 0, 0, 0);
        ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup != null) {
            int i10 = R$id.visible_removing_fragment_view_tag;
            if (viewGroup.getTag(i10) != null) {
                fragment.mContainer.setTag(i10, null);
            }
        }
        ViewGroup viewGroup2 = fragment.mContainer;
        if (viewGroup2 != null && viewGroup2.getLayoutTransition() != null) {
            return null;
        }
        Animation onCreateAnimation = fragment.onCreateAnimation(nextTransition, z10, b10);
        if (onCreateAnimation != null) {
            return new d(onCreateAnimation);
        }
        Animator onCreateAnimator = fragment.onCreateAnimator(nextTransition, z10, b10);
        if (onCreateAnimator != null) {
            return new d(onCreateAnimator);
        }
        if (b10 == 0 && nextTransition != 0) {
            b10 = d(nextTransition, z10);
        }
        if (b10 != 0) {
            boolean equals = "anim".equals(context.getResources().getResourceTypeName(b10));
            if (equals) {
                try {
                    Animation loadAnimation = AnimationUtils.loadAnimation(context, b10);
                    if (loadAnimation != null) {
                        return new d(loadAnimation);
                    }
                    z12 = true;
                } catch (Resources.NotFoundException e10) {
                    throw e10;
                } catch (RuntimeException unused) {
                }
            }
            if (!z12) {
                try {
                    Animator loadAnimator = AnimatorInflater.loadAnimator(context, b10);
                    if (loadAnimator != null) {
                        return new d(loadAnimator);
                    }
                } catch (RuntimeException e11) {
                    if (!equals) {
                        Animation loadAnimation2 = AnimationUtils.loadAnimation(context, b10);
                        if (loadAnimation2 != null) {
                            return new d(loadAnimation2);
                        }
                    } else {
                        throw e11;
                    }
                }
            }
        }
        return null;
    }

    private static int d(int i10, boolean z10) {
        if (i10 == 4097) {
            return z10 ? R$animator.fragment_open_enter : R$animator.fragment_open_exit;
        }
        if (i10 == 4099) {
            return z10 ? R$animator.fragment_fade_enter : R$animator.fragment_fade_exit;
        }
        if (i10 != 8194) {
            return -1;
        }
        return z10 ? R$animator.fragment_close_enter : R$animator.fragment_close_exit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentAnim.java */
    /* renamed from: androidx.fragment.app.d$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        public final Animation f2891a;

        /* renamed from: b, reason: collision with root package name */
        public final Animator f2892b;

        d(Animation animation) {
            this.f2891a = animation;
            this.f2892b = null;
            if (animation == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }

        d(Animator animator) {
            this.f2891a = null;
            this.f2892b = animator;
            if (animator == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentAnim.java */
    /* renamed from: androidx.fragment.app.d$e */
    /* loaded from: classes.dex */
    public static class e extends AnimationSet implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final ViewGroup f2893e;

        /* renamed from: f, reason: collision with root package name */
        private final View f2894f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f2895g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f2896h;

        /* renamed from: i, reason: collision with root package name */
        private boolean f2897i;

        /* JADX INFO: Access modifiers changed from: package-private */
        public e(Animation animation, ViewGroup viewGroup, View view) {
            super(false);
            this.f2897i = true;
            this.f2893e = viewGroup;
            this.f2894f = view;
            addAnimation(animation);
            viewGroup.post(this);
        }

        @Override // android.view.animation.AnimationSet, android.view.animation.Animation
        public boolean getTransformation(long j10, Transformation transformation) {
            this.f2897i = true;
            if (this.f2895g) {
                return !this.f2896h;
            }
            if (!super.getTransformation(j10, transformation)) {
                this.f2895g = true;
                OneShotPreDrawListener.a(this.f2893e, this);
            }
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.f2895g && this.f2897i) {
                this.f2897i = false;
                this.f2893e.post(this);
            } else {
                this.f2893e.endViewTransition(this.f2894f);
                this.f2896h = true;
            }
        }

        @Override // android.view.animation.Animation
        public boolean getTransformation(long j10, Transformation transformation, float f10) {
            this.f2897i = true;
            if (this.f2895g) {
                return !this.f2896h;
            }
            if (!super.getTransformation(j10, transformation, f10)) {
                this.f2895g = true;
                OneShotPreDrawListener.a(this.f2893e, this);
            }
            return true;
        }
    }
}
