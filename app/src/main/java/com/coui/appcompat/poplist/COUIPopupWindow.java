package com.coui.appcompat.poplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$style;

/* loaded from: classes.dex */
public class COUIPopupWindow extends PopupWindow {

    /* renamed from: a, reason: collision with root package name */
    private Context f6905a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f6906b;

    /* loaded from: classes.dex */
    class a extends ViewOutlineProvider {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f6907a;

        a(View view) {
            this.f6907a = view;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), this.f6907a.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_content_radius));
        }
    }

    public COUIPopupWindow(Context context) {
        this(context, null);
    }

    private void a(Context context) {
        this.f6905a = context;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R$attr.couiPopupWindowBackground});
        obtainStyledAttributes.getDrawable(0);
        setBackgroundDrawable(context.getResources().getDrawable(R$drawable.coui_free_bottom_alert_dialog_background));
        obtainStyledAttributes.recycle();
        setClippingEnabled(false);
        setElevation(0.0f);
        setExitTransition(null);
        setEnterTransition(null);
        setAnimationStyle(R$style.Animation_COUI_PopupListWindow);
    }

    private void c() {
        if (!this.f6906b || getContentView() == null) {
            return;
        }
        setBackgroundDrawable(null);
        setElevation(this.f6905a.getResources().getDimensionPixelSize(R$dimen.support_shadow_size_level_three));
        getContentView().setOutlineSpotShadowColor(ContextCompat.c(this.f6905a, R$color.coui_popup_outline_spot_shadow_color));
    }

    public void b(boolean z10) {
        if (z10) {
            setTouchable(true);
            setFocusable(true);
            setOutsideTouchable(true);
        } else {
            setFocusable(false);
            setOutsideTouchable(false);
        }
        update();
    }

    public void d(boolean z10) {
        this.f6906b = z10;
    }

    @Override // android.widget.PopupWindow
    public void setContentView(View view) {
        super.setContentView(view);
        if (view != null) {
            view.setOutlineProvider(new a(view));
            view.setClipToOutline(true);
        }
        c();
    }

    public COUIPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, androidx.appcompat.R$attr.popupWindowStyle);
    }

    public COUIPopupWindow(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIPopupWindow(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6906b = false;
        a(context);
    }
}
