package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;
import androidx.appcompat.R$styleable;
import androidx.core.widget.PopupWindowCompat;

/* loaded from: classes.dex */
class AppCompatPopupWindow extends PopupWindow {

    /* renamed from: b, reason: collision with root package name */
    private static final boolean f967b = false;

    /* renamed from: a, reason: collision with root package name */
    private boolean f968a;

    public AppCompatPopupWindow(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        a(context, attributeSet, i10, 0);
    }

    private void a(Context context, AttributeSet attributeSet, int i10, int i11) {
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, R$styleable.PopupWindow, i10, i11);
        int i12 = R$styleable.PopupWindow_overlapAnchor;
        if (w10.s(i12)) {
            b(w10.a(i12, false));
        }
        setBackgroundDrawable(w10.g(R$styleable.PopupWindow_android_popupBackground));
        w10.x();
    }

    private void b(boolean z10) {
        if (f967b) {
            this.f968a = z10;
        } else {
            PopupWindowCompat.a(this, z10);
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i10, int i11) {
        if (f967b && this.f968a) {
            i11 -= view.getHeight();
        }
        super.showAsDropDown(view, i10, i11);
    }

    @Override // android.widget.PopupWindow
    public void update(View view, int i10, int i11, int i12, int i13) {
        if (f967b && this.f968a) {
            i11 -= view.getHeight();
        }
        super.update(view, i10, i11, i12, i13);
    }

    public AppCompatPopupWindow(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        a(context, attributeSet, i10, i11);
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i10, int i11, int i12) {
        if (f967b && this.f968a) {
            i11 -= view.getHeight();
        }
        super.showAsDropDown(view, i10, i11, i12);
    }
}
