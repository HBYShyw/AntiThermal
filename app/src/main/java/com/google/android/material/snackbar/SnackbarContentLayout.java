package com.google.android.material.snackbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;

/* loaded from: classes.dex */
public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback {

    /* renamed from: e, reason: collision with root package name */
    private TextView f9242e;

    /* renamed from: f, reason: collision with root package name */
    private Button f9243f;

    /* renamed from: g, reason: collision with root package name */
    private int f9244g;

    public SnackbarContentLayout(Context context) {
        this(context, null);
    }

    private static void c(View view, int i10, int i11) {
        if (ViewCompat.S(view)) {
            ViewCompat.A0(view, ViewCompat.C(view), i10, ViewCompat.B(view), i11);
        } else {
            view.setPadding(view.getPaddingLeft(), i10, view.getPaddingRight(), i11);
        }
    }

    private boolean d(int i10, int i11, int i12) {
        boolean z10;
        if (i10 != getOrientation()) {
            setOrientation(i10);
            z10 = true;
        } else {
            z10 = false;
        }
        if (this.f9242e.getPaddingTop() == i11 && this.f9242e.getPaddingBottom() == i12) {
            return z10;
        }
        c(this.f9242e, i11, i12);
        return true;
    }

    @Override // com.google.android.material.snackbar.ContentViewCallback
    public void a(int i10, int i11) {
        this.f9242e.setAlpha(0.0f);
        long j10 = i11;
        long j11 = i10;
        this.f9242e.animate().alpha(1.0f).setDuration(j10).setStartDelay(j11).start();
        if (this.f9243f.getVisibility() == 0) {
            this.f9243f.setAlpha(0.0f);
            this.f9243f.animate().alpha(1.0f).setDuration(j10).setStartDelay(j11).start();
        }
    }

    @Override // com.google.android.material.snackbar.ContentViewCallback
    public void b(int i10, int i11) {
        this.f9242e.setAlpha(1.0f);
        long j10 = i11;
        long j11 = i10;
        this.f9242e.animate().alpha(0.0f).setDuration(j10).setStartDelay(j11).start();
        if (this.f9243f.getVisibility() == 0) {
            this.f9243f.setAlpha(1.0f);
            this.f9243f.animate().alpha(0.0f).setDuration(j10).setStartDelay(j11).start();
        }
    }

    public Button getActionView() {
        return this.f9243f;
    }

    public TextView getMessageView() {
        return this.f9242e;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.f9242e = (TextView) findViewById(R$id.snackbar_text);
        this.f9243f = (Button) findViewById(R$id.snackbar_action);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0045, code lost:
    
        if (d(1, r0, r0 - r2) != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0053, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0054, code lost:
    
        if (r1 == false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0056, code lost:
    
        super.onMeasure(r8, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0059, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0050, code lost:
    
        if (d(0, r0, r0) != false) goto L24;
     */
    @Override // android.widget.LinearLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        boolean z10 = true;
        if (getOrientation() == 1) {
            return;
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.design_snackbar_padding_vertical_2lines);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.design_snackbar_padding_vertical);
        boolean z11 = this.f9242e.getLayout().getLineCount() > 1;
        if (!z11 || this.f9244g <= 0 || this.f9243f.getMeasuredWidth() <= this.f9244g) {
            if (!z11) {
                dimensionPixelSize = dimensionPixelSize2;
            }
        }
    }

    public void setMaxInlineActionWidth(int i10) {
        this.f9244g = i10;
    }

    public SnackbarContentLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
