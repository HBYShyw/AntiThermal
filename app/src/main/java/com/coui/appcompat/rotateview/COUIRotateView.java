package com.coui.appcompat.rotateview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIRotateView extends AppCompatImageView {

    /* renamed from: n, reason: collision with root package name */
    private static final int[] f7305n = {R$attr.supportExpanded};

    /* renamed from: h, reason: collision with root package name */
    private Interpolator f7306h;

    /* renamed from: i, reason: collision with root package name */
    private long f7307i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f7308j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f7309k;

    /* renamed from: l, reason: collision with root package name */
    private int f7310l;

    /* renamed from: m, reason: collision with root package name */
    private b f7311m;

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIRotateView.this.f7309k = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIRotateView.this.f7309k = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIRotateView.this.f7309k = true;
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a(boolean z10);
    }

    public COUIRotateView(Context context) {
        this(context, null);
    }

    @Deprecated
    public void d() {
        int i10 = this.f7310l;
        if (i10 == 1) {
            animate().rotation(0.0f);
            this.f7308j = false;
        } else if (i10 == 0) {
            setExpanded(false);
        }
    }

    @Deprecated
    public void e() {
        int i10 = this.f7310l;
        if (i10 == 1) {
            animate().rotation(180.0f);
            this.f7308j = true;
        } else if (i10 == 0) {
            setExpanded(true);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public int[] onCreateDrawableState(int i10) {
        if (this.f7310l == 0) {
            if (this.f7308j) {
                int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 1);
                ImageView.mergeDrawableStates(onCreateDrawableState, f7305n);
                return onCreateDrawableState;
            }
            return super.onCreateDrawableState(i10);
        }
        return super.onCreateDrawableState(i10);
    }

    public void setExpanded(boolean z10) {
        if (this.f7308j == z10) {
            return;
        }
        int i10 = this.f7310l;
        if (i10 == 1) {
            if (this.f7309k) {
                return;
            }
            this.f7308j = z10;
            setRotation(z10 ? 180.0f : 0.0f);
        } else if (i10 == 0) {
            this.f7308j = z10;
            refreshDrawableState();
        }
        b bVar = this.f7311m;
        if (bVar != null) {
            bVar.a(this.f7308j);
        }
    }

    public void setOnRotateStateChangeListener(b bVar) {
        this.f7311m = bVar;
    }

    public COUIRotateView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIRotateView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, 0);
        this.f7306h = PathInterpolatorCompat.a(0.133f, 0.0f, 0.3f, 1.0f);
        this.f7307i = 400L;
        this.f7308j = false;
        this.f7309k = false;
        this.f7311m = null;
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIRotateView);
            this.f7310l = obtainStyledAttributes.getInteger(R$styleable.COUIRotateView_supportRotateType, 0);
            this.f7308j = obtainStyledAttributes.getBoolean(R$styleable.COUIRotateView_supportExpanded, false);
            obtainStyledAttributes.recycle();
        }
        int i11 = this.f7310l;
        if (i11 == 1) {
            animate().setDuration(this.f7307i).setInterpolator(this.f7306h).setListener(new a());
        } else if (i11 == 0) {
            refreshDrawableState();
        }
    }
}
