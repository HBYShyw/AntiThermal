package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.support.appcompat.R$dimen;

/* loaded from: classes.dex */
public class COUIAlertDialogClipCornerLinearLayout extends LinearLayoutCompat {

    /* loaded from: classes.dex */
    class a extends ViewOutlineProvider {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f5666a;

        a(int i10) {
            this.f5666a = i10;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, COUIAlertDialogClipCornerLinearLayout.this.getMeasuredWidth(), COUIAlertDialogClipCornerLinearLayout.this.getMeasuredHeight(), this.f5666a);
        }
    }

    public COUIAlertDialogClipCornerLinearLayout(Context context) {
        super(context);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_dialog_background_corner_radius);
        setClipToOutline(true);
        setOutlineProvider(new a(dimensionPixelOffset));
    }

    public COUIAlertDialogClipCornerLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUIAlertDialogClipCornerLinearLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
