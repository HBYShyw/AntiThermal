package com.coui.appcompat.tips.def;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import c3.IDefaultTopTips;
import c3.OnLinesChangedListener;
import com.coui.appcompat.tips.COUICustomTopTips;
import com.support.appcompat.R$color;
import com.support.control.R$dimen;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIDefaultTopTips extends COUICustomTopTips implements IDefaultTopTips {

    /* renamed from: s, reason: collision with root package name */
    private IDefaultTopTips f7950s;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements OnLinesChangedListener {
        a() {
        }

        @Override // c3.OnLinesChangedListener
        public void a(int i10) {
            if (i10 <= 1) {
                COUIDefaultTopTips.this.setRadius(r1.getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_single_line_radius));
            } else {
                COUIDefaultTopTips.this.setRadius(r1.getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_radius));
            }
        }
    }

    public COUIDefaultTopTips(Context context) {
        this(context, null);
    }

    @Override // com.coui.appcompat.tips.COUICustomTopTips
    protected void c() {
        COUIDarkModeUtil.b(this, false);
        this.f7950s = d();
        setRadius(getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_single_line_radius));
        setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.c(getContext(), R$color.coui_color_bottom_bar)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IDefaultTopTips d() {
        COUIDefaultTopTipsView cOUIDefaultTopTipsView = new COUIDefaultTopTipsView(getContext());
        cOUIDefaultTopTipsView.setOnLinesChangedListener(new a());
        cOUIDefaultTopTipsView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        setContentView(cOUIDefaultTopTipsView);
        return cOUIDefaultTopTipsView;
    }

    @Override // com.coui.appcompat.tips.COUICustomTopTips
    public int getContentViewId() {
        return 0;
    }

    @Override // c3.IDefaultTopTips
    public void setCloseBtnListener(View.OnClickListener onClickListener) {
        this.f7950s.setCloseBtnListener(onClickListener);
    }

    @Override // c3.IDefaultTopTips
    public void setCloseDrawable(Drawable drawable) {
        this.f7950s.setCloseDrawable(drawable);
    }

    @Override // c3.IDefaultTopTips
    public void setNegativeButton(CharSequence charSequence) {
        this.f7950s.setNegativeButton(charSequence);
    }

    @Override // c3.IDefaultTopTips
    public void setNegativeButtonListener(View.OnClickListener onClickListener) {
        this.f7950s.setNegativeButtonListener(onClickListener);
    }

    @Override // c3.IDefaultTopTips
    public void setPositiveButton(CharSequence charSequence) {
        this.f7950s.setPositiveButton(charSequence);
    }

    @Override // c3.IDefaultTopTips
    public void setPositiveButtonListener(View.OnClickListener onClickListener) {
        this.f7950s.setPositiveButtonListener(onClickListener);
    }

    @Override // c3.IDefaultTopTips
    public void setStartIcon(Drawable drawable) {
        this.f7950s.setStartIcon(drawable);
    }

    @Override // c3.IDefaultTopTips
    public void setTipsText(CharSequence charSequence) {
        this.f7950s.setTipsText(charSequence);
    }

    @Override // c3.IDefaultTopTips
    public void setTipsTextColor(int i10) {
        this.f7950s.setTipsTextColor(i10);
    }

    public COUIDefaultTopTips(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIDefaultTopTips(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
