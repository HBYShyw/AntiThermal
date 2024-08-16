package com.coui.appcompat.tips.def;

import a3.COUITextViewCompatUtil;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import c3.IDefaultTopTips;
import c3.OnLinesChangedListener;
import com.coui.appcompat.tips.COUIMarqueeTextView;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;

/* loaded from: classes.dex */
public class COUIDefaultTopTipsView extends ConstraintLayout implements IDefaultTopTips {
    private int B;
    private View.OnClickListener C;
    private View.OnClickListener D;
    private View.OnClickListener E;
    private ImageView F;
    private ImageView G;
    private COUIMarqueeTextView H;
    private boolean I;
    private TextView J;
    private TextView K;
    private final ConstraintSet L;
    private OnLinesChangedListener M;
    private int N;
    private int O;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIDefaultTopTipsView.this.D != null) {
                COUIDefaultTopTipsView.this.D.onClick(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIDefaultTopTipsView.this.C != null) {
                COUIDefaultTopTipsView.this.C.onClick(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIDefaultTopTipsView.this.E != null) {
                COUIDefaultTopTipsView.this.E.onClick(view);
            }
        }
    }

    public COUIDefaultTopTipsView(Context context) {
        this(context, null);
    }

    private void G() {
        this.L.j(this);
        ConstraintSet constraintSet = this.L;
        int i10 = R$id.title;
        int i11 = R$id.close;
        constraintSet.l(i10, 7, i11, 6);
        this.L.H(i10, 7, getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_btn_margin_right));
        this.L.l(i10, 4, 0, 4);
        ConstraintSet constraintSet2 = this.L;
        int i12 = R$id.ignore;
        constraintSet2.l(i12, 3, i10, 3);
        this.L.H(i12, 3, 0);
        ConstraintSet constraintSet3 = this.L;
        int i13 = R$id.action;
        constraintSet3.l(i13, 3, i10, 3);
        this.L.H(i13, 3, 0);
        this.L.K(i11, 0);
        this.L.K(i12, 4);
        this.L.K(i13, 4);
        this.L.K(i12, TextUtils.isEmpty(this.K.getText()) ? 8 : 4);
        this.L.K(i13, TextUtils.isEmpty(this.J.getText()) ? 8 : 4);
        this.L.d(this);
    }

    private void H() {
        this.L.j(this);
        if (K()) {
            ConstraintSet constraintSet = this.L;
            int i10 = R$id.title;
            constraintSet.l(i10, 7, 0, 7);
            if (TextUtils.isEmpty(this.J.getText()) && TextUtils.isEmpty(this.K.getText())) {
                this.L.l(i10, 4, 0, 4);
            } else {
                this.L.l(i10, 4, -1, 4);
            }
            this.L.H(i10, 7, getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_btn_margin_right));
            ConstraintSet constraintSet2 = this.L;
            int i11 = R$id.ignore;
            constraintSet2.l(i11, 3, i10, 4);
            this.L.l(i11, 4, 0, 4);
            ConstraintSet constraintSet3 = this.L;
            Resources resources = getContext().getResources();
            int i12 = R$dimen.coui_toptips_view_btn_top_margin;
            constraintSet3.H(i11, 3, resources.getDimensionPixelSize(i12));
            ConstraintSet constraintSet4 = this.L;
            Resources resources2 = getContext().getResources();
            int i13 = R$dimen.coui_toptips_view_multi_btn_text_bottom_margin;
            constraintSet4.H(i11, 4, resources2.getDimensionPixelSize(i13));
            ConstraintSet constraintSet5 = this.L;
            int i14 = R$id.action;
            constraintSet5.l(i14, 3, i10, 4);
            this.L.l(i14, 4, 0, 4);
            this.L.H(i14, 3, getContext().getResources().getDimensionPixelSize(i12));
            this.L.H(i14, 4, getContext().getResources().getDimensionPixelSize(i13));
            this.L.l(R$id.image, 4, -1, 4);
        } else {
            ConstraintSet constraintSet6 = this.L;
            int i15 = R$id.title;
            int i16 = R$id.ignore;
            constraintSet6.l(i15, 7, i16, 6);
            this.L.l(i15, 4, 0, 4);
            this.L.H(i15, 7, getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_btn_margin_right));
            this.L.l(i16, 3, i15, 3);
            this.L.l(i16, 4, i15, 4);
            this.L.H(i16, 3, 0);
            this.L.H(i16, 4, 0);
            ConstraintSet constraintSet7 = this.L;
            int i17 = R$id.action;
            constraintSet7.l(i17, 3, i15, 3);
            this.L.l(i17, 4, i15, 4);
            this.L.H(i17, 3, 0);
            this.L.H(i17, 4, 0);
            this.L.l(R$id.image, 4, i15, 4);
        }
        if (this.M != null && this.N != this.H.getLineCount()) {
            int lineCount = this.H.getLineCount();
            this.N = lineCount;
            this.M.a(lineCount);
        }
        this.L.K(R$id.close, 4);
        this.L.K(R$id.ignore, TextUtils.isEmpty(this.K.getText()) ? 8 : 0);
        this.L.K(R$id.action, TextUtils.isEmpty(this.J.getText()) ? 8 : 0);
        this.L.d(this);
    }

    private boolean K() {
        if (this.H.getLineCount() > 1) {
            return true;
        }
        if (this.H.getMaxLines() == 1) {
            return false;
        }
        float measureText = this.H.getPaint().measureText(this.H.getText().toString());
        TextView textView = TextUtils.isEmpty(this.K.getText()) ? this.J : this.K;
        boolean z10 = (TextUtils.isEmpty(this.J.getText()) && TextUtils.isEmpty(this.K.getText())) ? false : true;
        if (ViewCompat.x(this) != 1) {
            return ((int) Math.max(measureText + ((float) this.H.getLeft()), (float) this.H.getRight())) + getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_btn_margin) >= (z10 ? textView.getLeft() : getRight());
        }
        return (z10 ? textView.getRight() : getLeft()) + getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_btn_margin) >= ((int) Math.min(measureText + ((float) this.H.getRight()), (float) this.H.getLeft()));
    }

    private void L(TextView textView, int i10) {
        textView.measure(ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(i10, Integer.MIN_VALUE), 0, -2), ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(textView.getMeasuredHeight(), Integer.MIN_VALUE), 0, -2));
    }

    private void M(int i10, Drawable drawable) {
        if (i10 == 4) {
            this.G.setImageDrawable(drawable);
            I(1);
            return;
        }
        throw new IllegalArgumentException("setBtnDrawableImpl parameter 'which' is wrong");
    }

    private void N(int i10, CharSequence charSequence) {
        if (i10 == 2) {
            this.K.setText(charSequence);
            I(0);
        } else {
            if (i10 == 3) {
                this.J.setText(charSequence);
                I(0);
                return;
            }
            throw new IllegalArgumentException("setBtnTextImpl parameter 'which' is wrong");
        }
    }

    public final void I(int i10) {
        if (i10 == 0) {
            H();
        } else {
            G();
        }
        this.B = i10;
    }

    protected void J() {
        LayoutInflater.from(getContext()).inflate(R$layout.coui_default_toptips, this);
        this.F = (ImageView) findViewById(R$id.image);
        this.H = (COUIMarqueeTextView) findViewById(R$id.title);
        TextView textView = (TextView) findViewById(R$id.ignore);
        this.K = textView;
        COUITextViewCompatUtil.b(textView);
        this.K.setOnClickListener(new a());
        TextView textView2 = (TextView) findViewById(R$id.action);
        this.J = textView2;
        COUITextViewCompatUtil.b(textView2);
        this.J.setOnClickListener(new b());
        ImageView imageView = (ImageView) findViewById(R$id.close);
        this.G = imageView;
        imageView.setOnClickListener(new c());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (ViewCompat.x(this) == 1) {
            TextView textView = this.J;
            textView.layout(textView.getLeft(), this.J.getTop(), this.J.getLeft() + this.J.getMeasuredWidth(), this.J.getBottom());
            this.K.layout(this.J.getRight(), this.K.getTop(), this.J.getRight() + this.K.getMeasuredWidth(), this.K.getBottom());
        } else {
            TextView textView2 = this.J;
            textView2.layout(textView2.getRight() - this.J.getMeasuredWidth(), this.J.getTop(), this.J.getRight(), this.J.getBottom());
            this.K.layout(this.J.getLeft() - this.K.getMeasuredWidth(), this.K.getTop(), this.J.getLeft(), this.K.getBottom());
        }
        if (this.B == 0 && this.I) {
            this.I = false;
            H();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        int measuredWidth = getMeasuredWidth() - ((((ConstraintLayout.LayoutParams) this.H.getLayoutParams()).getMarginStart() + this.F.getMeasuredWidth()) + ((ConstraintLayout.LayoutParams) this.F.getLayoutParams()).getMarginStart());
        int i12 = measuredWidth >> 1;
        if (this.J.getMeasuredWidth() <= i12) {
            this.O++;
        }
        if (this.K.getMeasuredWidth() <= i12) {
            this.O += 2;
        }
        int i13 = this.O;
        if (i13 == 0) {
            L(this.J, i12);
            L(this.K, i12);
        } else if (i13 == 1) {
            L(this.K, measuredWidth - this.J.getMeasuredWidth());
        } else if (i13 == 2) {
            L(this.J, measuredWidth - this.K.getMeasuredWidth());
        }
        this.O = 0;
    }

    @Override // c3.IDefaultTopTips
    public void setCloseBtnListener(View.OnClickListener onClickListener) {
        this.E = onClickListener;
    }

    @Override // c3.IDefaultTopTips
    public void setCloseDrawable(Drawable drawable) {
        M(4, drawable);
    }

    @Override // c3.IDefaultTopTips
    public void setNegativeButton(CharSequence charSequence) {
        N(2, charSequence);
    }

    @Override // c3.IDefaultTopTips
    public void setNegativeButtonListener(View.OnClickListener onClickListener) {
        this.D = onClickListener;
    }

    public void setOnLinesChangedListener(OnLinesChangedListener onLinesChangedListener) {
        this.M = onLinesChangedListener;
    }

    @Override // c3.IDefaultTopTips
    public void setPositiveButton(CharSequence charSequence) {
        N(3, charSequence);
    }

    @Override // c3.IDefaultTopTips
    public void setPositiveButtonListener(View.OnClickListener onClickListener) {
        this.C = onClickListener;
    }

    @Override // c3.IDefaultTopTips
    public void setStartIcon(Drawable drawable) {
        this.F.setImageDrawable(drawable);
    }

    @Override // c3.IDefaultTopTips
    public void setTipsText(CharSequence charSequence) {
        this.I = true;
        this.H.setText(charSequence);
    }

    @Override // c3.IDefaultTopTips
    public void setTipsTextColor(int i10) {
        this.H.setTextColor(i10);
    }

    public COUIDefaultTopTipsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIDefaultTopTipsView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.B = 0;
        this.I = true;
        this.L = new ConstraintSet();
        this.N = -1;
        this.O = 0;
        J();
    }
}
