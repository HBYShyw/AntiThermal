package com.coui.appcompat.tablayout;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.support.bars.R$layout;
import k2.COUIHintRedDotMemento;
import l2.COUIRippleDrawableUtil;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUITabView extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private COUITab f7824e;

    /* renamed from: f, reason: collision with root package name */
    private TextView f7825f;

    /* renamed from: g, reason: collision with root package name */
    private ImageView f7826g;

    /* renamed from: h, reason: collision with root package name */
    private COUIHintRedDot f7827h;

    /* renamed from: i, reason: collision with root package name */
    protected View f7828i;

    /* renamed from: j, reason: collision with root package name */
    private TextView f7829j;

    /* renamed from: k, reason: collision with root package name */
    private ImageView f7830k;

    /* renamed from: l, reason: collision with root package name */
    private int f7831l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f7832m;

    /* renamed from: n, reason: collision with root package name */
    private COUITabLayout f7833n;

    public COUITabView(Context context, COUITabLayout cOUITabLayout) {
        super(context);
        this.f7831l = 1;
        this.f7833n = cOUITabLayout;
        if (cOUITabLayout.I != 0) {
            ViewCompat.p0(this, ResourcesCompat.f(context.getResources(), this.f7833n.I, getContext().getTheme()));
        }
        ViewCompat.A0(this, this.f7833n.getTabPaddingStart(), this.f7833n.getTabPaddingTop(), this.f7833n.getTabPaddingEnd(), this.f7833n.getTabPaddingBottom());
        setGravity(17);
        setOrientation(0);
        setClickable(true);
        COUIRippleDrawableUtil.a(this, b(8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        COUITabLayout cOUITabLayout = this.f7833n;
        cOUITabLayout.f7789b0 = false;
        cOUITabLayout.J.requestLayout();
    }

    private void f(TextView textView, ImageView imageView) {
        COUITab cOUITab = this.f7824e;
        Drawable c10 = cOUITab != null ? cOUITab.c() : null;
        COUITab cOUITab2 = this.f7824e;
        CharSequence e10 = cOUITab2 != null ? cOUITab2.e() : null;
        COUITab cOUITab3 = this.f7824e;
        CharSequence a10 = cOUITab3 != null ? cOUITab3.a() : null;
        int i10 = 0;
        if (imageView != null) {
            if (c10 != null) {
                imageView.setImageDrawable(c10);
                imageView.setVisibility(0);
                setVisibility(0);
            } else {
                imageView.setVisibility(8);
                imageView.setImageDrawable(null);
            }
            imageView.setContentDescription(a10);
        }
        boolean z10 = !TextUtils.isEmpty(e10);
        if (textView != null) {
            if (z10) {
                CharSequence text = textView.getText();
                textView.setText(e10);
                textView.setVisibility(0);
                COUITabLayout cOUITabLayout = this.f7833n;
                if (cOUITabLayout.f7789b0) {
                    COUISlidingTabStrip cOUISlidingTabStrip = cOUITabLayout.J;
                    if (cOUISlidingTabStrip != null) {
                        cOUITabLayout.f7789b0 = false;
                        cOUISlidingTabStrip.requestLayout();
                    }
                } else if (!e10.equals(text)) {
                    this.f7833n.J.post(new Runnable() { // from class: com.coui.appcompat.tablayout.e
                        @Override // java.lang.Runnable
                        public final void run() {
                            COUITabView.this.c();
                        }
                    });
                }
                textView.setMaxLines(this.f7831l);
                setVisibility(0);
            } else {
                textView.setVisibility(8);
                textView.setText((CharSequence) null);
            }
            textView.setContentDescription(a10);
        }
        if (imageView != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            if (z10 && imageView.getVisibility() == 0) {
                i10 = b(8);
            }
            if (i10 != marginLayoutParams.bottomMargin) {
                marginLayoutParams.bottomMargin = i10;
                imageView.requestLayout();
            }
        }
        TooltipCompat.a(this, z10 ? null : a10);
    }

    int b(int i10) {
        return Math.round(getResources().getDisplayMetrics().density * i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        setTab(null);
        setSelected(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
        COUITab cOUITab = this.f7824e;
        COUIHintRedDotMemento cOUIHintRedDotMemento = null;
        View b10 = cOUITab != null ? cOUITab.b() : null;
        boolean z10 = false;
        if (b10 != null) {
            ViewParent parent = b10.getParent();
            if (parent != this) {
                if (parent != null) {
                    ((ViewGroup) parent).removeView(b10);
                }
                addView(b10, 0, new ViewGroup.LayoutParams(-2, -2));
            }
            this.f7828i = b10;
            TextView textView = this.f7825f;
            if (textView != null) {
                textView.setVisibility(8);
            }
            ImageView imageView = this.f7826g;
            if (imageView != null) {
                imageView.setVisibility(8);
                this.f7826g.setImageDrawable(null);
            }
            TextView textView2 = (TextView) b10.findViewById(R.id.text1);
            this.f7829j = textView2;
            if (textView2 != null) {
                this.f7831l = TextViewCompat.d(textView2);
            }
            this.f7830k = (ImageView) b10.findViewById(R.id.icon);
        } else {
            View view = this.f7828i;
            if (view != null) {
                removeView(view);
                this.f7828i = null;
            }
            this.f7829j = null;
            this.f7830k = null;
        }
        if (this.f7828i == null) {
            if (this.f7826g == null) {
                ImageView imageView2 = (ImageView) LayoutInflater.from(getContext()).inflate(R$layout.coui_tab_layout_icon, (ViewGroup) this, false);
                addView(imageView2, 0);
                this.f7826g = imageView2;
            }
            if (this.f7825f == null) {
                TextView textView3 = (TextView) LayoutInflater.from(getContext()).inflate(R$layout.coui_tab_layout_text, (ViewGroup) this, false);
                this.f7825f = textView3;
                addView(textView3);
                TextView textView4 = this.f7825f;
                COUITabLayout cOUITabLayout = this.f7833n;
                ViewCompat.A0(textView4, cOUITabLayout.Q, cOUITabLayout.R, cOUITabLayout.S, cOUITabLayout.T);
                this.f7831l = TextViewCompat.d(this.f7825f);
                COUIChangeTextUtil.b(this.f7825f, cOUITab != null && cOUITab.f());
            }
            COUIHintRedDot cOUIHintRedDot = this.f7827h;
            if (cOUIHintRedDot != null) {
                cOUIHintRedDotMemento = cOUIHintRedDot.b();
                removeView(this.f7827h);
            }
            this.f7827h = new COUIHintRedDot(getContext());
            this.f7827h.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            addView(this.f7827h);
            if (cOUIHintRedDotMemento != null) {
                cOUIHintRedDotMemento.a(this.f7827h);
            }
            this.f7825f.setTextSize(0, this.f7833n.getTabTextSize());
            if (cOUITab != null && cOUITab.f()) {
                this.f7825f.setTypeface(this.f7833n.V);
            } else {
                this.f7825f.setTypeface(this.f7833n.W);
            }
            this.f7825f.setIncludeFontPadding(false);
            ColorStateList colorStateList = this.f7833n.U;
            if (colorStateList != null) {
                this.f7825f.setTextColor(colorStateList);
            }
            f(this.f7825f, this.f7826g);
        } else {
            if (this.f7825f == null) {
                this.f7825f = (TextView) LayoutInflater.from(getContext()).inflate(R$layout.coui_tab_layout_text, (ViewGroup) this, false);
            }
            TextView textView5 = this.f7829j;
            if (textView5 != null || this.f7830k != null) {
                f(textView5, this.f7830k);
            }
        }
        if (cOUITab != null && cOUITab.f()) {
            z10 = true;
        }
        setSelected(z10);
    }

    public COUIHintRedDot getHintRedDot() {
        return this.f7827h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getSelectedByClick() {
        return this.f7832m;
    }

    public COUITab getTab() {
        return this.f7824e;
    }

    public TextView getTextView() {
        return this.f7825f;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ActionBar.b.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ActionBar.b.class.getName());
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        COUITab cOUITab;
        if (isEnabled() && (cOUITab = this.f7833n.P) != null && cOUITab.f7840b != this && motionEvent.getAction() == 0 && this.f7833n.f7792e0) {
            performHapticFeedback(302);
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean performClick() {
        boolean performClick = super.performClick();
        if (this.f7824e == null) {
            return performClick;
        }
        if (!performClick) {
            playSoundEffect(0);
        }
        this.f7833n.f7790c0 = false;
        this.f7832m = true;
        this.f7824e.h();
        this.f7832m = false;
        return true;
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        TextView textView = this.f7825f;
        if (textView != null) {
            textView.setEnabled(z10);
        }
        ImageView imageView = this.f7826g;
        if (imageView != null) {
            imageView.setEnabled(z10);
        }
        View view = this.f7828i;
        if (view != null) {
            view.setEnabled(z10);
        }
    }

    @Override // android.view.View
    public void setSelected(boolean z10) {
        TextView textView;
        boolean z11 = isSelected() != z10;
        super.setSelected(z10);
        if (z11 && (textView = this.f7825f) != null) {
            if (z10) {
                textView.setTypeface(this.f7833n.V);
            } else {
                textView.setTypeface(this.f7833n.W);
            }
        }
        TextView textView2 = this.f7825f;
        if (textView2 != null) {
            COUIDarkModeUtil.b(textView2, !z10);
        }
        TextView textView3 = this.f7825f;
        if (textView3 != null) {
            textView3.setSelected(z10);
        }
        ImageView imageView = this.f7826g;
        if (imageView != null) {
            imageView.setSelected(z10);
        }
        View view = this.f7828i;
        if (view != null) {
            view.setSelected(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTab(COUITab cOUITab) {
        if (cOUITab != this.f7824e) {
            this.f7824e = cOUITab;
            e();
        }
    }
}
