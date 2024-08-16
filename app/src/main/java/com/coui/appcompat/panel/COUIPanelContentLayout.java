package com.coui.appcompat.panel;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import com.coui.appcompat.buttonBar.COUIButtonBarLayout;
import com.support.panel.R$bool;
import com.support.panel.R$id;
import f2.COUIPanelMultiWindowUtils;
import f2.COUIViewMarginUtil;

/* loaded from: classes.dex */
public class COUIPanelContentLayout extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private boolean f6617e;

    public COUIPanelContentLayout(Context context) {
        this(context, null);
    }

    private int b(WindowInsets windowInsets, Configuration configuration) {
        if (windowInsets != null) {
            return windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        }
        int identifier = getContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (configuration != null) {
            return getContext().createConfigurationContext(configuration).getResources().getDimensionPixelSize(identifier);
        }
        return getContext().getResources().getDimensionPixelSize(identifier);
    }

    private boolean c(Configuration configuration) {
        if (configuration == null) {
            return getContext().getResources().getBoolean(R$bool.is_coui_bottom_sheet_dialog_in_big_screen);
        }
        return getContext().createConfigurationContext(configuration).getResources().getBoolean(R$bool.is_coui_bottom_sheet_dialog_in_big_screen);
    }

    public void a(View view) {
        if (view != null) {
            ((LinearLayout) findViewById(R$id.panel_content)).addView(view, new LinearLayout.LayoutParams(-1, -1));
        }
    }

    public void d() {
        ((LinearLayout) findViewById(R$id.panel_content)).removeAllViews();
    }

    public void e(Configuration configuration, int i10, WindowInsets windowInsets) {
        int i11 = 0;
        if (Settings.Secure.getInt(getContext().getContentResolver(), "hide_navigationbar_enable", 0) == 3) {
            boolean p10 = COUIPanelMultiWindowUtils.p(COUIPanelMultiWindowUtils.a(getContext()));
            boolean q10 = COUIPanelMultiWindowUtils.q(COUIPanelMultiWindowUtils.a(getContext()));
            boolean c10 = c(configuration);
            int b10 = b(windowInsets, configuration);
            if (p10 && q10) {
                b10 = 0;
            } else if (!c10) {
                i11 = b10;
                b10 = 0;
            }
            View findViewById = findViewById(R$id.panel_content);
            View findViewById2 = findViewById.getRootView().findViewById(R$id.coordinator);
            COUIViewMarginUtil.b(findViewById, 3, i11);
            COUIViewMarginUtil.b(findViewById2, 3, b10);
        }
    }

    public COUIButtonBarLayout getBtnBarLayout() {
        return (COUIButtonBarLayout) findViewById(R$id.bottom_bar);
    }

    public View getDivider() {
        return findViewById(R$id.divider_line);
    }

    public ImageView getDragView() {
        return (ImageView) findViewById(R$id.drag_img);
    }

    public boolean getLayoutAtMaxHeight() {
        return this.f6617e;
    }

    public int getMaxHeight() {
        return COUIPanelMultiWindowUtils.h(getContext(), null);
    }

    public COUIPanelBarView getPanelBarView() {
        return (COUIPanelBarView) findViewById(R$id.panel_drag_bar);
    }

    public void setDividerVisibility(boolean z10) {
        View findViewById = findViewById(R$id.divider_line);
        if (z10) {
            findViewById.setVisibility(0);
        } else {
            findViewById.setVisibility(8);
        }
    }

    public void setDragViewDrawable(Drawable drawable) {
        if (drawable != null) {
            ((ImageView) findViewById(R$id.drag_img)).setImageDrawable(drawable);
        }
    }

    public void setDragViewDrawableTintColor(int i10) {
        ((AppCompatImageView) findViewById(R$id.drag_img)).getDrawable().setTint(i10);
    }

    public void setLayoutAtMaxHeight(boolean z10) {
        this.f6617e = z10;
        if (z10) {
            getLayoutParams().height = -1;
        } else {
            getLayoutParams().height = -2;
        }
        requestLayout();
    }

    public COUIPanelContentLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPanelContentLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
