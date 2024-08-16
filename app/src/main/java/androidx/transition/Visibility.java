package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.Transition;

/* loaded from: classes.dex */
public abstract class Visibility extends Transition {

    /* renamed from: f, reason: collision with root package name */
    private static final String[] f4073f = {"android:visibility:visibility", "android:visibility:parent"};

    /* renamed from: e, reason: collision with root package name */
    private int f4074e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f4075a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f4076b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f4077c;

        a(ViewGroup viewGroup, View view, View view2) {
            this.f4075a = viewGroup;
            this.f4076b = view;
            this.f4077c = view2;
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void b(Transition transition) {
            a0.b(this.f4075a).remove(this.f4076b);
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            this.f4077c.setTag(R$id.save_overlay_view, null);
            a0.b(this.f4075a).remove(this.f4076b);
            transition.removeListener(this);
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void e(Transition transition) {
            if (this.f4076b.getParent() == null) {
                a0.b(this.f4075a).add(this.f4076b);
            } else {
                Visibility.this.cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends AnimatorListenerAdapter implements Transition.g {

        /* renamed from: a, reason: collision with root package name */
        private final View f4079a;

        /* renamed from: b, reason: collision with root package name */
        private final int f4080b;

        /* renamed from: c, reason: collision with root package name */
        private final ViewGroup f4081c;

        /* renamed from: d, reason: collision with root package name */
        private final boolean f4082d;

        /* renamed from: e, reason: collision with root package name */
        private boolean f4083e;

        /* renamed from: f, reason: collision with root package name */
        boolean f4084f = false;

        b(View view, int i10, boolean z10) {
            this.f4079a = view;
            this.f4080b = i10;
            this.f4081c = (ViewGroup) view.getParent();
            this.f4082d = z10;
            g(true);
        }

        private void f() {
            if (!this.f4084f) {
                d0.i(this.f4079a, this.f4080b);
                ViewGroup viewGroup = this.f4081c;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            g(false);
        }

        private void g(boolean z10) {
            ViewGroup viewGroup;
            if (!this.f4082d || this.f4083e == z10 || (viewGroup = this.f4081c) == null) {
                return;
            }
            this.f4083e = z10;
            a0.c(viewGroup, z10);
        }

        @Override // androidx.transition.Transition.g
        public void a(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void b(Transition transition) {
            g(false);
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            f();
            transition.removeListener(this);
        }

        @Override // androidx.transition.Transition.g
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void e(Transition transition) {
            g(true);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f4084f = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            if (this.f4084f) {
                return;
            }
            d0.i(this.f4079a, this.f4080b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            if (this.f4084f) {
                return;
            }
            d0.i(this.f4079a, 0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        boolean f4085a;

        /* renamed from: b, reason: collision with root package name */
        boolean f4086b;

        /* renamed from: c, reason: collision with root package name */
        int f4087c;

        /* renamed from: d, reason: collision with root package name */
        int f4088d;

        /* renamed from: e, reason: collision with root package name */
        ViewGroup f4089e;

        /* renamed from: f, reason: collision with root package name */
        ViewGroup f4090f;

        c() {
        }
    }

    public Visibility() {
        this.f4074e = 3;
    }

    private c b(TransitionValues transitionValues, TransitionValues transitionValues2) {
        c cVar = new c();
        cVar.f4085a = false;
        cVar.f4086b = false;
        if (transitionValues != null && transitionValues.f4152a.containsKey("android:visibility:visibility")) {
            cVar.f4087c = ((Integer) transitionValues.f4152a.get("android:visibility:visibility")).intValue();
            cVar.f4089e = (ViewGroup) transitionValues.f4152a.get("android:visibility:parent");
        } else {
            cVar.f4087c = -1;
            cVar.f4089e = null;
        }
        if (transitionValues2 != null && transitionValues2.f4152a.containsKey("android:visibility:visibility")) {
            cVar.f4088d = ((Integer) transitionValues2.f4152a.get("android:visibility:visibility")).intValue();
            cVar.f4090f = (ViewGroup) transitionValues2.f4152a.get("android:visibility:parent");
        } else {
            cVar.f4088d = -1;
            cVar.f4090f = null;
        }
        if (transitionValues != null && transitionValues2 != null) {
            int i10 = cVar.f4087c;
            int i11 = cVar.f4088d;
            if (i10 == i11 && cVar.f4089e == cVar.f4090f) {
                return cVar;
            }
            if (i10 != i11) {
                if (i10 == 0) {
                    cVar.f4086b = false;
                    cVar.f4085a = true;
                } else if (i11 == 0) {
                    cVar.f4086b = true;
                    cVar.f4085a = true;
                }
            } else if (cVar.f4090f == null) {
                cVar.f4086b = false;
                cVar.f4085a = true;
            } else if (cVar.f4089e == null) {
                cVar.f4086b = true;
                cVar.f4085a = true;
            }
        } else if (transitionValues == null && cVar.f4088d == 0) {
            cVar.f4086b = true;
            cVar.f4085a = true;
        } else if (transitionValues2 == null && cVar.f4087c == 0) {
            cVar.f4086b = false;
            cVar.f4085a = true;
        }
        return cVar;
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.f4152a.put("android:visibility:visibility", Integer.valueOf(transitionValues.f4153b.getVisibility()));
        transitionValues.f4152a.put("android:visibility:parent", transitionValues.f4153b.getParent());
        int[] iArr = new int[2];
        transitionValues.f4153b.getLocationOnScreen(iArr);
        transitionValues.f4152a.put("android:visibility:screenLocation", iArr);
    }

    public int a() {
        return this.f4074e;
    }

    public Animator c(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    @Override // androidx.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        c b10 = b(transitionValues, transitionValues2);
        if (!b10.f4085a) {
            return null;
        }
        if (b10.f4089e == null && b10.f4090f == null) {
            return null;
        }
        if (b10.f4086b) {
            return d(viewGroup, transitionValues, b10.f4087c, transitionValues2, b10.f4088d);
        }
        return f(viewGroup, transitionValues, b10.f4087c, transitionValues2, b10.f4088d);
    }

    public Animator d(ViewGroup viewGroup, TransitionValues transitionValues, int i10, TransitionValues transitionValues2, int i11) {
        if ((this.f4074e & 1) != 1 || transitionValues2 == null) {
            return null;
        }
        if (transitionValues == null) {
            View view = (View) transitionValues2.f4153b.getParent();
            if (b(getMatchedTransitionValues(view, false), getTransitionValues(view, false)).f4085a) {
                return null;
            }
        }
        return c(viewGroup, transitionValues2.f4153b, transitionValues, transitionValues2);
    }

    public Animator e(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0089, code lost:
    
        if (r17.mCanRemoveViews != false) goto L43;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Animator f(ViewGroup viewGroup, TransitionValues transitionValues, int i10, TransitionValues transitionValues2, int i11) {
        View view;
        boolean z10;
        boolean z11;
        View view2;
        if ((this.f4074e & 2) != 2 || transitionValues == null) {
            return null;
        }
        View view3 = transitionValues.f4153b;
        View view4 = transitionValues2 != null ? transitionValues2.f4153b : null;
        int i12 = R$id.save_overlay_view;
        View view5 = (View) view3.getTag(i12);
        if (view5 != null) {
            view2 = null;
            z11 = true;
        } else if (view4 == null || view4.getParent() == null) {
            if (view4 != null) {
                view = null;
                z10 = false;
                if (z10) {
                    if (view3.getParent() != null) {
                        if (view3.getParent() instanceof View) {
                            View view6 = (View) view3.getParent();
                            if (!b(getTransitionValues(view6, true), getMatchedTransitionValues(view6, true)).f4085a) {
                                view4 = TransitionUtils.a(viewGroup, view3, view6);
                            } else {
                                int id2 = view6.getId();
                                if (view6.getParent() == null) {
                                    if (id2 != -1) {
                                        if (viewGroup.findViewById(id2) != null) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                    view2 = view;
                    z11 = false;
                    view5 = view3;
                }
                z11 = false;
                View view7 = view;
                view5 = view4;
                view2 = view7;
            }
            view4 = null;
            view = null;
            z10 = true;
            if (z10) {
            }
            z11 = false;
            View view72 = view;
            view5 = view4;
            view2 = view72;
        } else {
            if (i11 == 4 || view3 == view4) {
                view = view4;
                z10 = false;
                view4 = null;
                if (z10) {
                }
                z11 = false;
                View view722 = view;
                view5 = view4;
                view2 = view722;
            }
            view4 = null;
            view = null;
            z10 = true;
            if (z10) {
            }
            z11 = false;
            View view7222 = view;
            view5 = view4;
            view2 = view7222;
        }
        if (view5 == null) {
            if (view2 == null) {
                return null;
            }
            int visibility = view2.getVisibility();
            d0.i(view2, 0);
            Animator e10 = e(viewGroup, view2, transitionValues, transitionValues2);
            if (e10 != null) {
                b bVar = new b(view2, i11, true);
                e10.addListener(bVar);
                AnimatorUtils.a(e10, bVar);
                addListener(bVar);
            } else {
                d0.i(view2, visibility);
            }
            return e10;
        }
        if (!z11) {
            int[] iArr = (int[]) transitionValues.f4152a.get("android:visibility:screenLocation");
            int i13 = iArr[0];
            int i14 = iArr[1];
            int[] iArr2 = new int[2];
            viewGroup.getLocationOnScreen(iArr2);
            view5.offsetLeftAndRight((i13 - iArr2[0]) - view5.getLeft());
            view5.offsetTopAndBottom((i14 - iArr2[1]) - view5.getTop());
            a0.b(viewGroup).add(view5);
        }
        Animator e11 = e(viewGroup, view5, transitionValues, transitionValues2);
        if (!z11) {
            if (e11 == null) {
                a0.b(viewGroup).remove(view5);
            } else {
                view3.setTag(i12, view5);
                addListener(new a(viewGroup, view5, view3));
            }
        }
        return e11;
    }

    public void g(int i10) {
        if ((i10 & (-4)) == 0) {
            this.f4074e = i10;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }

    @Override // androidx.transition.Transition
    public String[] getTransitionProperties() {
        return f4073f;
    }

    @Override // androidx.transition.Transition
    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null && transitionValues2 == null) {
            return false;
        }
        if (transitionValues != null && transitionValues2 != null && transitionValues2.f4152a.containsKey("android:visibility:visibility") != transitionValues.f4152a.containsKey("android:visibility:visibility")) {
            return false;
        }
        c b10 = b(transitionValues, transitionValues2);
        if (b10.f4085a) {
            return b10.f4087c == 0 || b10.f4088d == 0;
        }
        return false;
    }

    @SuppressLint({"RestrictedApi"})
    public Visibility(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4074e = 3;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4132e);
        int g6 = TypedArrayUtils.g(obtainStyledAttributes, (XmlResourceParser) attributeSet, "transitionVisibilityMode", 0, 0);
        obtainStyledAttributes.recycle();
        if (g6 != 0) {
            g(g6);
        }
    }
}
