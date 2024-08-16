package com.coui.appcompat.statement;

import a3.COUITextViewCompatUtil;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.coui.appcompat.button.COUIButton;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$bool;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUIFullPageStatement extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private TextView f7715e;

    /* renamed from: f, reason: collision with root package name */
    private COUIButton f7716f;

    /* renamed from: g, reason: collision with root package name */
    private TextView f7717g;

    /* renamed from: h, reason: collision with root package name */
    private TextView f7718h;

    /* renamed from: i, reason: collision with root package name */
    private LayoutInflater f7719i;

    /* renamed from: j, reason: collision with root package name */
    private Context f7720j;

    /* renamed from: k, reason: collision with root package name */
    private d f7721k;

    /* renamed from: l, reason: collision with root package name */
    private COUIMaxHeightScrollView f7722l;

    /* renamed from: m, reason: collision with root package name */
    private ScrollView f7723m;

    /* renamed from: n, reason: collision with root package name */
    private LinearLayoutCompat f7724n;

    /* renamed from: o, reason: collision with root package name */
    private int f7725o;

    /* renamed from: p, reason: collision with root package name */
    private int f7726p;

    /* renamed from: q, reason: collision with root package name */
    private COUIMaxHeightScrollView f7727q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIFullPageStatement.this.f7721k != null) {
                COUIFullPageStatement.this.f7721k.a();
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
            if (COUIFullPageStatement.this.f7721k != null) {
                COUIFullPageStatement.this.f7721k.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* loaded from: classes.dex */
        class a implements View.OnTouchListener {
            a() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        }

        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIFullPageStatement.this.f7727q.getHeight() < COUIFullPageStatement.this.f7727q.getMaxHeight()) {
                COUIFullPageStatement.this.f7727q.setOnTouchListener(new a());
            }
        }
    }

    /* loaded from: classes.dex */
    public interface d {
        void a();

        void b();
    }

    public COUIFullPageStatement(Context context) {
        this(context, null);
    }

    private void c() {
        if (this.f7727q == null) {
            return;
        }
        post(new c());
    }

    private void d() {
        LayoutInflater layoutInflater = (LayoutInflater) this.f7720j.getSystemService("layout_inflater");
        this.f7719i = layoutInflater;
        View inflate = layoutInflater.inflate(this.f7726p, this);
        this.f7715e = (TextView) inflate.findViewById(R$id.txt_statement);
        this.f7716f = (COUIButton) inflate.findViewById(R$id.btn_confirm);
        this.f7722l = (COUIMaxHeightScrollView) inflate.findViewById(R$id.scroll_text);
        this.f7717g = (TextView) inflate.findViewById(R$id.txt_exit);
        this.f7718h = (TextView) inflate.findViewById(R$id.txt_title);
        this.f7727q = (COUIMaxHeightScrollView) inflate.findViewById(R$id.title_scroll_view);
        this.f7723m = (ScrollView) inflate.findViewById(R$id.scroll_button);
        this.f7724n = (LinearLayoutCompat) inflate.findViewById(R$id.custom_functional_area);
        e();
        c();
        COUIChangeTextUtil.c(this.f7715e, 2);
        this.f7716f.setSingleLine(false);
        this.f7716f.setMaxLines(2);
        this.f7716f.setOnClickListener(new a());
        this.f7717g.setOnClickListener(new b());
        COUITextViewCompatUtil.a(this.f7717g);
    }

    public void e() {
        if (this.f7726p == R$layout.coui_full_page_statement_tiny) {
            return;
        }
        boolean z10 = getContext().getResources().getBoolean(R$bool.is_small_window);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f7723m.getLayoutParams();
        if (z10) {
            layoutParams.height = getContext().getResources().getDimensionPixelSize(R$dimen.coui_full_page_statement_button_scrollview_height);
        } else {
            layoutParams.height = -2;
        }
        this.f7723m.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.f7727q.getLayoutParams();
        layoutParams2.setMargins(0, getContext().getResources().getDimensionPixelSize(R$dimen.coui_full_page_statement_text_button_padding), 0, getContext().getResources().getDimensionPixelSize(R$dimen.coui_full_page_statement_content_margin));
        this.f7727q.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.f7716f.getLayoutParams();
        layoutParams3.setMargins(0, getContext().getResources().getDimensionPixelSize(R$dimen.coui_full_page_statement_button_margin_top), 0, getContext().getResources().getDimensionPixelSize(R$dimen.coui_full_page_statement_button_margin));
        this.f7716f.setLayoutParams(layoutParams3);
    }

    public TextView getAppStatement() {
        return this.f7715e;
    }

    public COUIButton getConfirmButton() {
        return this.f7716f;
    }

    public TextView getExitButton() {
        return this.f7717g;
    }

    public COUIMaxHeightScrollView getScrollTextView() {
        return this.f7722l;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f7726p == R$layout.coui_full_page_statement_tiny) {
            return;
        }
        this.f7716f.getLayoutParams().width = getContext().createConfigurationContext(configuration).getResources().getDimensionPixelOffset(R$dimen.coui_full_page_statement_button_width);
        e();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.f7726p == R$layout.coui_full_page_statement_tiny) {
            ViewParent parent = this.f7723m.getParent();
            if (parent instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) parent;
                int bottom = ((linearLayout.getBottom() - linearLayout.getTop()) - this.f7723m.getTop()) - this.f7723m.getMeasuredHeight();
                ScrollView scrollView = this.f7723m;
                scrollView.layout(scrollView.getLeft(), this.f7723m.getTop() + bottom, this.f7723m.getRight(), this.f7723m.getBottom() + bottom);
            }
        }
    }

    public void setAppStatement(CharSequence charSequence) {
        this.f7715e.setText(charSequence);
    }

    public void setAppStatementTextColor(int i10) {
        this.f7715e.setTextColor(i10);
    }

    public void setButtonDisableColor(int i10) {
        this.f7716f.setDisabledColor(i10);
    }

    public void setButtonDrawableColor(int i10) {
        this.f7716f.setDrawableColor(i10);
    }

    public void setButtonListener(d dVar) {
        this.f7721k = dVar;
    }

    public void setButtonText(CharSequence charSequence) {
        this.f7716f.setText(charSequence);
    }

    public void setCustomView(View view) {
        LinearLayoutCompat linearLayoutCompat = this.f7724n;
        if (linearLayoutCompat != null) {
            if (view == null) {
                linearLayoutCompat.removeAllViews();
                this.f7724n.setVisibility(8);
            } else {
                linearLayoutCompat.setVisibility(0);
                this.f7724n.removeAllViews();
                this.f7724n.addView(view);
            }
        }
    }

    public void setExitButtonText(CharSequence charSequence) {
        this.f7717g.setText(charSequence);
    }

    public void setExitTextColor(int i10) {
        this.f7717g.setTextColor(i10);
    }

    public void setStatementMaxHeight(int i10) {
        this.f7722l.setMaxHeight(i10);
    }

    public void setTitleText(CharSequence charSequence) {
        this.f7718h.setText(charSequence);
    }

    public COUIFullPageStatement(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiFullPageStatementStyle);
    }

    public COUIFullPageStatement(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_COUI_COUIFullPageStatement);
    }

    public COUIFullPageStatement(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7720j = context;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f7725o = attributeSet.getStyleAttribute();
        } else {
            this.f7725o = i10;
        }
        TypedArray obtainStyledAttributes = this.f7720j.obtainStyledAttributes(attributeSet, R$styleable.COUIFullPageStatement, i10, i11);
        String string = obtainStyledAttributes.getString(R$styleable.COUIFullPageStatement_exitButtonText);
        String string2 = obtainStyledAttributes.getString(R$styleable.COUIFullPageStatement_bottomButtonText);
        String string3 = obtainStyledAttributes.getString(R$styleable.COUIFullPageStatement_couiFullPageStatementTitleText);
        this.f7726p = obtainStyledAttributes.getResourceId(R$styleable.COUIFullPageStatement_android_layout, R$layout.coui_full_page_statement);
        d();
        this.f7715e.setText(obtainStyledAttributes.getString(R$styleable.COUIFullPageStatement_appStatement));
        int color = obtainStyledAttributes.getColor(R$styleable.COUIFullPageStatement_couiFullPageStatementTextButtonColor, 0);
        if (color != 0) {
            this.f7717g.setTextColor(color);
        }
        this.f7715e.setTextColor(obtainStyledAttributes.getColor(R$styleable.COUIFullPageStatement_couiFullPageStatementTextColor, 0));
        if (string2 != null) {
            this.f7716f.setText(string2);
        }
        if (string != null) {
            this.f7717g.setText(string);
        }
        if (string3 != null) {
            this.f7718h.setText(string3);
        }
        obtainStyledAttributes.recycle();
    }
}
