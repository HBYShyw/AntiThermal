package n2;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.support.component.R$id;
import com.support.component.R$layout;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: TorchTipGroup.kt */
@Metadata(bv = {}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001&B\u000f\u0012\u0006\u0010#\u001a\u00020\"¢\u0006\u0004\b$\u0010%J,\u0010\b\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0003J\b\u0010\n\u001a\u00020\tH\u0002J\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002J\"\u0010\f\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\u000e\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0010J\u0010\u0010\u0015\u001a\u00020\u00052\b\b\u0001\u0010\u0014\u001a\u00020\u0013J\u0006\u0010\u0016\u001a\u00020\u0005R\"\u0010\u0018\u001a\u00020\u00178\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0017\u0010\u001e\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u001e\u0010\u001f\u001a\u0004\b \u0010!¨\u0006'"}, d2 = {"Ln2/j;", "", "Landroid/view/View;", "view", "Lkotlin/Function0;", "Lma/f0;", "onActionDown", "onActionUp", "e", "Landroid/widget/LinearLayout;", "j", "i", "d", "Landroid/view/ViewGroup;", "viewGroup", "c", "", "torchTip", "m", "", "torchTipResId", "l", "g", "", "showTorchTip", "Z", "getShowTorchTip", "()Z", "k", "(Z)V", "bottomTorchTip", "Landroid/widget/LinearLayout;", "h", "()Landroid/widget/LinearLayout;", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: n2.j, reason: use source file name */
/* loaded from: classes.dex */
public final class TorchTipGroup {

    /* renamed from: i, reason: collision with root package name */
    public static final a f15700i = new a(null);

    /* renamed from: j, reason: collision with root package name */
    private static final PathInterpolator f15701j = new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f);

    /* renamed from: a, reason: collision with root package name */
    private final Context f15702a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f15703b;

    /* renamed from: c, reason: collision with root package name */
    private final LinearLayout f15704c;

    /* renamed from: d, reason: collision with root package name */
    private final LinearLayout f15705d;

    /* renamed from: e, reason: collision with root package name */
    private final LinearLayout f15706e;

    /* renamed from: f, reason: collision with root package name */
    private final LinearLayout f15707f;

    /* renamed from: g, reason: collision with root package name */
    private final LinearLayout[] f15708g;

    /* renamed from: h, reason: collision with root package name */
    private int f15709h;

    /* compiled from: TorchTipGroup.kt */
    @Metadata(bv = {}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u001e\u0010\u001fJ \u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0007J \u0010\b\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0007J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\tH\u0007R\u0014\u0010\r\u001a\u00020\t8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\t8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000f\u0010\u000eR\u0014\u0010\u0011\u001a\u00020\u00108\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0014\u001a\u00020\u00138\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00138\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0016\u0010\u0015R\u0014\u0010\u0017\u001a\u00020\u00138\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0017\u0010\u0015R\u0014\u0010\u0018\u001a\u00020\u00138\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0018\u0010\u0015R\u0014\u0010\u0019\u001a\u00020\u00138\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0019\u0010\u0015R\u0014\u0010\u001a\u001a\u00020\u00138\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001a\u0010\u0015R\u0014\u0010\u001c\u001a\u00020\u001b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001c\u0010\u001d¨\u0006 "}, d2 = {"Ln2/j$a;", "", "Landroid/view/View;", "view", "Lkotlin/Function0;", "Lma/f0;", "onEnd", "d", "a", "", "orientation", "", "c", "ANGLE_COUNT", "I", "ANGLE_INCREASE_STEP", "", "ANIMATION_DURATION", "J", "", "APPEAR_ANIM_END", "F", "APPEAR_ANIM_START", "DISAPPEAR_CONTROL_X1", "DISAPPEAR_CONTROL_X2", "DISAPPEAR_CONTROL_Y1", "DISAPPEAR_CONTROL_Y2", "Landroid/view/animation/PathInterpolator;", "animPathInterpolator", "Landroid/view/animation/PathInterpolator;", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: n2.j$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: TorchTipGroup.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
        /* renamed from: n2.j$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static final class C0079a extends Lambda implements ya.a<Unit> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0079a f15710e = new C0079a();

            C0079a() {
                super(0);
            }

            public final void a() {
            }

            @Override // ya.a
            public /* bridge */ /* synthetic */ Unit invoke() {
                a();
                return Unit.f15173a;
            }
        }

        /* compiled from: Animator.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "animator", "Lma/f0;", "onAnimationRepeat", "onAnimationEnd", "onAnimationCancel", "onAnimationStart", "core-ktx_release"}, k = 1, mv = {1, 5, 1})
        /* renamed from: n2.j$a$b */
        /* loaded from: classes.dex */
        public static final class b implements Animator.AnimatorListener {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ View f15711a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ ya.a f15712b;

            public b(View view, ya.a aVar) {
                this.f15711a = view;
                this.f15712b = aVar;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                k.e(animator, "animator");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                k.e(animator, "animator");
                this.f15711a.setVisibility(8);
                this.f15711a.setAlpha(1.0f);
                this.f15712b.invoke();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
                k.e(animator, "animator");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                k.e(animator, "animator");
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: TorchTipGroup.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
        /* renamed from: n2.j$a$c */
        /* loaded from: classes.dex */
        public static final class c extends Lambda implements ya.a<Unit> {

            /* renamed from: e, reason: collision with root package name */
            public static final c f15713e = new c();

            c() {
                super(0);
            }

            public final void a() {
            }

            @Override // ya.a
            public /* bridge */ /* synthetic */ Unit invoke() {
                a();
                return Unit.f15173a;
            }
        }

        /* compiled from: Animator.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "animator", "Lma/f0;", "onAnimationRepeat", "onAnimationEnd", "onAnimationCancel", "onAnimationStart", "core-ktx_release"}, k = 1, mv = {1, 5, 1})
        /* renamed from: n2.j$a$d */
        /* loaded from: classes.dex */
        public static final class d implements Animator.AnimatorListener {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ ya.a f15714a;

            public d(ya.a aVar) {
                this.f15714a = aVar;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                k.e(animator, "animator");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                k.e(animator, "animator");
                this.f15714a.invoke();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
                k.e(animator, "animator");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                k.e(animator, "animator");
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void b(a aVar, View view, ya.a aVar2, int i10, Object obj) {
            if ((i10 & 2) != 0) {
                aVar2 = C0079a.f15710e;
            }
            aVar.a(view, aVar2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void e(a aVar, View view, ya.a aVar2, int i10, Object obj) {
            if ((i10 & 2) != 0) {
                aVar2 = c.f15713e;
            }
            aVar.d(view, aVar2);
        }

        public final void a(View view, ya.a<Unit> aVar) {
            k.e(view, "view");
            k.e(aVar, "onEnd");
            if (view.getVisibility() == 0) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, view.getAlpha(), 0.0f);
                ofFloat.setInterpolator(TorchTipGroup.f15701j);
                ofFloat.setDuration(250L);
                k.d(ofFloat, "");
                ofFloat.addListener(new b(view, aVar));
                ofFloat.start();
            }
        }

        public final boolean c(int orientation) {
            return ((orientation / 90) % 4) % 2 == 0;
        }

        public final void d(View view, ya.a<Unit> aVar) {
            k.e(view, "view");
            k.e(aVar, "onEnd");
            if (view.getVisibility() == 0) {
                return;
            }
            view.setAlpha(0.0f);
            view.setVisibility(0);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, view.getAlpha(), 1.0f);
            ofFloat.setInterpolator(TorchTipGroup.f15701j);
            ofFloat.setDuration(250L);
            k.d(ofFloat, "");
            ofFloat.addListener(new d(aVar));
            ofFloat.start();
        }
    }

    public TorchTipGroup(Context context) {
        k.e(context, "context");
        this.f15702a = context;
        LinearLayout j10 = j();
        j10.setId(R$id.coui_component_scan_view_torch_tip_bottom);
        j10.setLayoutParams(new ConstraintLayout.LayoutParams(-2, -2));
        Unit unit = Unit.f15173a;
        this.f15704c = j10;
        LinearLayout j11 = j();
        j11.setId(R$id.coui_component_scan_view_torch_tip_left);
        j11.setRotation(90.0f);
        j11.setVisibility(8);
        j11.setLayoutParams(new ConstraintLayout.LayoutParams(-2, -2));
        this.f15705d = j11;
        LinearLayout j12 = j();
        j12.setId(R$id.coui_component_scan_view_torch_tip_flipped);
        j12.setRotation(180.0f);
        j12.setVisibility(8);
        j12.setLayoutParams(new ConstraintLayout.LayoutParams(-2, -2));
        this.f15706e = j12;
        LinearLayout j13 = j();
        j13.setId(R$id.coui_component_scan_view_torch_tip_right);
        j13.setRotation(270.0f);
        j13.setVisibility(8);
        j13.setLayoutParams(new ConstraintLayout.LayoutParams(-2, -2));
        this.f15707f = j13;
        this.f15708g = new LinearLayout[]{j10, j13, j12, j11};
        this.f15709h = -1;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private final void e(View view, final ya.a<Unit> aVar, final ya.a<Unit> aVar2) {
        view.setOnTouchListener(new View.OnTouchListener() { // from class: n2.i
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean f10;
                f10 = TorchTipGroup.f(ya.a.this, aVar2, view2, motionEvent);
                return f10;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean f(ya.a aVar, ya.a aVar2, View view, MotionEvent motionEvent) {
        k.e(aVar, "$onActionDown");
        k.e(aVar2, "$onActionUp");
        int action = motionEvent.getAction();
        if (action == 0) {
            aVar.invoke();
            return true;
        }
        if (action != 1 && action != 3) {
            return false;
        }
        aVar2.invoke();
        return false;
    }

    private final LinearLayout j() {
        View inflate = View.inflate(this.f15702a, R$layout.coui_component_scan_view_torch_tip, null);
        k.d(inflate, "");
        inflate.setVisibility(8);
        inflate.setAlpha(0.0f);
        return (LinearLayout) inflate;
    }

    public final void c(ViewGroup viewGroup) {
        k.e(viewGroup, "viewGroup");
        viewGroup.addView(this.f15704c);
        viewGroup.addView(this.f15705d);
        viewGroup.addView(this.f15707f);
        viewGroup.addView(this.f15706e);
    }

    public final void d(ya.a<Unit> aVar, ya.a<Unit> aVar2) {
        k.e(aVar, "onActionDown");
        k.e(aVar2, "onActionUp");
        for (LinearLayout linearLayout : this.f15708g) {
            e(linearLayout, aVar, aVar2);
        }
    }

    public final void g() {
        int i10 = this.f15709h;
        if (i10 >= 0) {
            a.b(f15700i, this.f15708g[i10], null, 2, null);
        }
    }

    /* renamed from: h, reason: from getter */
    public final LinearLayout getF15704c() {
        return this.f15704c;
    }

    public final View i() {
        int i10 = this.f15709h;
        if (i10 >= 0) {
            return this.f15708g[i10];
        }
        return null;
    }

    public final void k(boolean z10) {
        this.f15703b = z10;
    }

    public final void l(int i10) {
        for (LinearLayout linearLayout : this.f15708g) {
            ((TextView) linearLayout.findViewById(R$id.torch_tip_content)).setText(i10);
        }
    }

    public final void m(CharSequence charSequence) {
        k.e(charSequence, "torchTip");
        for (LinearLayout linearLayout : this.f15708g) {
            ((TextView) linearLayout.findViewById(R$id.torch_tip_content)).setText(charSequence);
        }
    }
}
