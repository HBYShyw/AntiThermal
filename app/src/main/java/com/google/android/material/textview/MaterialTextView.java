package com.google.android.material.textview;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.R$attr;
import com.google.android.material.R$styleable;
import d4.MaterialThemeOverlay;
import z3.MaterialAttributes;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class MaterialTextView extends AppCompatTextView {
    public MaterialTextView(Context context) {
        this(context, null);
    }

    private void p(Resources.Theme theme, int i10) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(i10, R$styleable.MaterialTextAppearance);
        int s7 = s(getContext(), obtainStyledAttributes, R$styleable.MaterialTextAppearance_android_lineHeight, R$styleable.MaterialTextAppearance_lineHeight);
        obtainStyledAttributes.recycle();
        if (s7 >= 0) {
            setLineHeight(s7);
        }
    }

    private static boolean q(Context context) {
        return MaterialAttributes.b(context, R$attr.textAppearanceLineHeightEnabled, true);
    }

    private static int r(Resources.Theme theme, AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.MaterialTextView, i10, i11);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.MaterialTextView_android_textAppearance, -1);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    private static int s(Context context, TypedArray typedArray, int... iArr) {
        int i10 = -1;
        for (int i11 = 0; i11 < iArr.length && i10 < 0; i11++) {
            i10 = MaterialResources.d(context, typedArray, iArr[i11], -1);
        }
        return i10;
    }

    private static boolean t(Context context, Resources.Theme theme, AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.MaterialTextView, i10, i11);
        int s7 = s(context, obtainStyledAttributes, R$styleable.MaterialTextView_android_lineHeight, R$styleable.MaterialTextView_lineHeight);
        obtainStyledAttributes.recycle();
        return s7 != -1;
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView
    public void setTextAppearance(Context context, int i10) {
        super.setTextAppearance(context, i10);
        if (q(context)) {
            p(context.getTheme(), i10);
        }
    }

    public MaterialTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.textViewStyle);
    }

    public MaterialTextView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public MaterialTextView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, i11), attributeSet, i10);
        int r10;
        Context context2 = getContext();
        if (q(context2)) {
            Resources.Theme theme = context2.getTheme();
            if (t(context2, theme, attributeSet, i10, i11) || (r10 = r(theme, attributeSet, i10, i11)) == -1) {
                return;
            }
            p(theme, r10);
        }
    }
}
