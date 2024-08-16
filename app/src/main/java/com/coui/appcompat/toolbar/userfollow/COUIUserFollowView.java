package com.coui.appcompat.toolbar.userfollow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.motion.widget.TransitionBuilder;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.coui.appcompat.toolbar.userfollow.COUIUserFollowView;
import com.coui.appcompat.toolbar.userfollow.IUserFollowView;
import com.support.appcompat.R$attr;
import com.support.nearx.R$color;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIUserFollowView extends MotionLayout implements IUserFollowView, MotionLayout.i {

    /* renamed from: k1, reason: collision with root package name */
    public static final int f8030k1 = View.generateViewId();

    /* renamed from: l1, reason: collision with root package name */
    public static final int f8031l1 = View.generateViewId();

    /* renamed from: m1, reason: collision with root package name */
    public static final int f8032m1 = View.generateViewId();

    /* renamed from: n1, reason: collision with root package name */
    public static final int f8033n1 = View.generateViewId();

    /* renamed from: o1, reason: collision with root package name */
    public static final int f8034o1 = View.generateViewId();

    /* renamed from: p1, reason: collision with root package name */
    public static int f8035p1 = 7;

    /* renamed from: q1, reason: collision with root package name */
    public static int f8036q1 = 1;

    /* renamed from: r1, reason: collision with root package name */
    public static int f8037r1 = 2;

    /* renamed from: s1, reason: collision with root package name */
    public static int f8038s1 = 4;
    private COUIButton S0;
    private TextView T0;
    private TextView U0;
    private COUIRoundImageView V0;
    private boolean W0;
    private boolean X0;
    private boolean Y0;
    private boolean Z0;

    /* renamed from: a1, reason: collision with root package name */
    private IUserFollowView.a f8039a1;

    /* renamed from: b1, reason: collision with root package name */
    private MotionLayout.i f8040b1;

    /* renamed from: c1, reason: collision with root package name */
    private int f8041c1;

    /* renamed from: d1, reason: collision with root package name */
    private int f8042d1;

    /* renamed from: e1, reason: collision with root package name */
    private int f8043e1;

    /* renamed from: f1, reason: collision with root package name */
    private final ConstraintSet f8044f1;

    /* renamed from: g1, reason: collision with root package name */
    private int f8045g1;

    /* renamed from: h1, reason: collision with root package name */
    private final ConstraintSet f8046h1;

    /* renamed from: i1, reason: collision with root package name */
    private int f8047i1;

    /* renamed from: j1, reason: collision with root package name */
    private MotionScene f8048j1;

    public COUIUserFollowView(Context context) {
        this(context, null);
    }

    private static int F0(Context context, float f10) {
        return Math.round(TypedValue.applyDimension(1, f10, context.getResources().getDisplayMetrics()));
    }

    private void G0() {
        Barrier barrier = new Barrier(getContext());
        barrier.setId(f8034o1);
        barrier.setType(6);
        barrier.setReferencedIds(new int[]{f8031l1, f8032m1});
        addView(barrier);
    }

    private void H0() {
        COUIButton cOUIButton = new COUIButton(getContext(), null, R$attr.couiSmallButtonColorStyle);
        this.S0 = cOUIButton;
        cOUIButton.setId(f8033n1);
        this.S0.setMaxLines(1);
        this.S0.setGravity(17);
        this.S0.setPadding(0, 0, 0, 0);
        this.S0.setText("关注");
        this.S0.setOnClickListener(new View.OnClickListener() { // from class: e3.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUIUserFollowView.this.O0(view);
            }
        });
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(F0(getContext(), 52.0f), F0(getContext(), 28.0f));
        layoutParams.f1876p = f8034o1;
        layoutParams.f1879s = 0;
        layoutParams.f1860h = 0;
        layoutParams.f1866k = 0;
        layoutParams.f1886z = 0.0f;
        layoutParams.setMarginEnd(F0(getContext(), 8.0f));
        addView(this.S0, layoutParams);
    }

    private void I0() {
        COUIRoundImageView cOUIRoundImageView = new COUIRoundImageView(getContext());
        this.V0 = cOUIRoundImageView;
        cOUIRoundImageView.setId(f8030k1);
        this.V0.setHasBorder(true);
        this.V0.setOutCircleColor(ContextCompat.c(getContext(), R$color.coui_userfollow_default_image_stroke_bg));
        this.V0.setImageDrawable(new ColorDrawable(getContext().getColor(R$color.coui_userfollow_default_image_bg)));
        int F0 = F0(getContext(), 24.0f);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(F0, F0);
        layoutParams.f1877q = 0;
        layoutParams.f1860h = 0;
        layoutParams.f1866k = 0;
        layoutParams.setMarginStart(F0(getContext(), 8.0f));
        layoutParams.f1886z = 0.0f;
        layoutParams.G = 2;
        addView(this.V0, layoutParams);
    }

    private void K0() {
        TextView textView = new TextView(getContext(), null, R$attr.supportSubtitleTextAppearance);
        this.U0 = textView;
        textView.setId(f8032m1);
        this.U0.setEllipsize(TextUtils.TruncateAt.END);
        this.U0.setMaxLines(1);
        this.U0.setText("");
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);
        layoutParams.f1877q = f8031l1;
        layoutParams.f1878r = f8033n1;
        layoutParams.f1862i = 0;
        layoutParams.T = true;
        layoutParams.f1886z = 0.0f;
        layoutParams.setMarginEnd(F0(getContext(), 8.0f));
        addView(this.U0, layoutParams);
    }

    private void L0() {
        TextView textView = new TextView(getContext(), null, R$attr.supportTitleTextAppearance);
        this.T0 = textView;
        textView.setId(f8031l1);
        this.T0.setEllipsize(TextUtils.TruncateAt.END);
        this.T0.setMaxLines(1);
        this.T0.setText("用户名");
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);
        layoutParams.f1860h = 0;
        layoutParams.f1864j = f8032m1;
        layoutParams.f1878r = f8033n1;
        layoutParams.f1876p = f8030k1;
        layoutParams.T = true;
        layoutParams.f1886z = 0.0f;
        layoutParams.G = 2;
        layoutParams.setMarginStart(F0(getContext(), 8.0f));
        layoutParams.setMarginEnd(F0(getContext(), 8.0f));
        addView(this.T0, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void O0(View view) {
        setFollowing(!N0());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void P0() {
        this.f8044f1.j(this);
        this.f8046h1.j(this);
        MotionScene.b a10 = TransitionBuilder.a(J0(), View.generateViewId(), this.f8045g1, this.f8044f1, this.f8047i1, this.f8046h1);
        a10.E(this.f8043e1);
        J0().f(a10);
        J0().Q(a10);
        setScene(J0());
        v0(this.f8045g1, this.f8047i1);
    }

    private void Q0(int i10, int i11) {
        ConstraintSet j02 = j0(this.f8047i1);
        int i12 = f8037r1;
        S0(j02, (i11 & i12) == i12);
        int i13 = f8038s1;
        T0(j02, (i11 & i13) == i13);
        int i14 = f8036q1;
        R0(j02, (i11 & i14) == i14);
        v0(this.f8045g1, this.f8047i1);
    }

    protected MotionScene J0() {
        if (this.f8048j1 == null) {
            this.f8048j1 = new MotionScene(this);
        }
        return this.f8048j1;
    }

    public boolean M0() {
        return this.Y0;
    }

    public boolean N0() {
        return this.W0;
    }

    protected void R0(ConstraintSet constraintSet, boolean z10) {
        if (this.S0 == null) {
            return;
        }
        if (z10) {
            int i10 = f8033n1;
            constraintSet.E(i10, "TextSize", 12.0f);
            constraintSet.I(i10, "Text", "已关注");
            constraintSet.D(i10, "TextColor", COUIContextUtil.a(getContext(), R$attr.couiColorOnSecondary));
            constraintSet.D(i10, "DrawableColor", COUIContextUtil.a(getContext(), R$attr.couiColorSecondary));
            return;
        }
        int i11 = f8033n1;
        constraintSet.E(i11, "TextSize", 14.0f);
        constraintSet.I(i11, "Text", "关注");
        constraintSet.D(i11, "TextColor", -1);
        constraintSet.D(i11, "DrawableColor", COUIContextUtil.a(getContext(), R$attr.couiColorPrimary));
    }

    protected void S0(ConstraintSet constraintSet, boolean z10) {
        if (z10) {
            int i10 = f8032m1;
            constraintSet.l(i10, 3, f8031l1, 4);
            constraintSet.l(i10, 4, 0, 4);
            constraintSet.l(i10, 7, f8033n1, 6);
            return;
        }
        int i11 = f8032m1;
        constraintSet.l(i11, 3, 0, 4);
        constraintSet.l(i11, 4, -1, 4);
        constraintSet.l(i11, 7, f8031l1, 7);
    }

    protected void T0(ConstraintSet constraintSet, boolean z10) {
        if (z10) {
            constraintSet.G(f8033n1, 1.0f);
        } else {
            constraintSet.G(f8033n1, 0.0f);
        }
    }

    public synchronized void U0() {
        Q0(getCurState(), getTargetState());
        y0();
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void a(MotionLayout motionLayout, int i10, int i11, float f10) {
        MotionLayout.i iVar = this.f8040b1;
        if (iVar != null) {
            iVar.a(motionLayout, i10, i11, f10);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void b(MotionLayout motionLayout, int i10, int i11) {
        MotionLayout.i iVar = this.f8040b1;
        if (iVar != null) {
            iVar.b(motionLayout, i10, i11);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void d(MotionLayout motionLayout, int i10, boolean z10, float f10) {
        MotionLayout.i iVar = this.f8040b1;
        if (iVar != null) {
            iVar.d(motionLayout, i10, z10, f10);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void e(MotionLayout motionLayout, int i10) {
        setCurState(this.f8042d1 & f8035p1);
        int i11 = this.f8047i1;
        this.f8047i1 = this.f8045g1;
        this.f8045g1 = i11;
        MotionLayout.i iVar = this.f8040b1;
        if (iVar != null) {
            iVar.e(motionLayout, i10);
        }
    }

    public COUIButton getButton() {
        return this.S0;
    }

    public int getCurState() {
        return this.f8041c1;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public COUIRoundImageView getImage() {
        return this.V0;
    }

    public COUIRoundImageView getImageView() {
        return this.V0;
    }

    public TextView getSubTitle() {
        return this.U0;
    }

    public int getTargetState() {
        return this.f8042d1;
    }

    public TextView getTitle() {
        return this.T0;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setAnimate(boolean z10) {
        this.Y0 = z10;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setBtnBg(Drawable drawable) {
        this.S0.setBackground(drawable);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setBtnText(CharSequence charSequence) {
        this.S0.setText(charSequence);
    }

    public void setCurState(int i10) {
        this.f8041c1 = i10;
    }

    public void setDuration(int i10) {
        this.f8043e1 = i10;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFill(boolean z10) {
        if (this.X0 == z10) {
            return;
        }
        this.X0 = z10;
        if (z10) {
            setTargetState(getTargetState() | f8038s1);
        } else {
            setTargetState(getTargetState() & (~f8038s1));
        }
        if (M0() && isAttachedToWindow()) {
            U0();
        } else {
            if (isAttachedToWindow()) {
                return;
            }
            setCurState(getTargetState() & f8035p1);
            this.f8046h1.j(this);
            T0(this.f8046h1, this.X0);
            this.f8046h1.d(this);
        }
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFollowTitle(CharSequence charSequence) {
        this.T0.setText(charSequence);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFollowTitleColor(int i10) {
        this.T0.setTextColor(i10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFollowing(boolean z10) {
        if (this.W0 == z10) {
            return;
        }
        this.W0 = z10;
        if (z10) {
            setTargetState(getTargetState() | f8036q1);
        } else {
            setTargetState(getTargetState() & (~f8036q1));
        }
        IUserFollowView.a aVar = this.f8039a1;
        if (aVar != null) {
            aVar.a(this, N0());
        }
        if (M0() && isAttachedToWindow()) {
            U0();
        } else {
            if (isAttachedToWindow()) {
                return;
            }
            setCurState(getTargetState() & f8035p1);
            this.f8046h1.j(this);
            R0(this.f8046h1, this.W0);
            this.f8046h1.d(this);
        }
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setImage(Drawable drawable) {
        this.V0.setImageDrawable(drawable);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setOnStateChangeListener(IUserFollowView.a aVar) {
        this.f8039a1 = aVar;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setSubFollowTitle(CharSequence charSequence) {
        this.U0.setText(charSequence);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setSubFollowTitleColor(int i10) {
        this.U0.setTextColor(i10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setSubFollowTitleEnable(boolean z10) {
        this.Z0 = z10;
        if (z10) {
            setTargetState(getTargetState() | f8037r1);
        } else {
            setTargetState(getTargetState() & (~f8037r1));
        }
        if (M0() && isAttachedToWindow()) {
            U0();
        } else {
            if (isAttachedToWindow()) {
                return;
            }
            setCurState(getTargetState() & f8035p1);
            this.f8046h1.j(this);
            S0(this.f8046h1, this.Z0);
            this.f8046h1.d(this);
        }
    }

    public void setTargetState(int i10) {
        this.f8042d1 = i10;
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout
    public void setTransitionListener(MotionLayout.i iVar) {
        this.f8040b1 = iVar;
    }

    public COUIUserFollowView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setImage(Bitmap bitmap) {
        this.V0.setImageBitmap(bitmap);
    }

    public COUIUserFollowView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.W0 = false;
        this.X0 = false;
        this.Y0 = true;
        this.Z0 = false;
        this.f8041c1 = 0;
        this.f8042d1 = 0;
        this.f8043e1 = 300;
        this.f8044f1 = new ConstraintSet();
        this.f8045g1 = View.generateViewId();
        this.f8046h1 = new ConstraintSet();
        this.f8047i1 = View.generateViewId();
        I0();
        L0();
        K0();
        G0();
        H0();
        super.setTransitionListener(this);
        post(new Runnable() { // from class: e3.b
            @Override // java.lang.Runnable
            public final void run() {
                COUIUserFollowView.this.P0();
            }
        });
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setImage(int i10) {
        this.V0.setImageResource(i10);
    }
}
