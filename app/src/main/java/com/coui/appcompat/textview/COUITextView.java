package com.coui.appcompat.textview;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUITextView extends AppCompatTextView {

    /* renamed from: l, reason: collision with root package name */
    private final Context f7908l;

    public COUITextView(Context context) {
        this(context, null);
    }

    private void p(Resources.Theme theme, int i10) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(i10, R$styleable.COUITextAppearance);
        float f10 = obtainStyledAttributes.getFloat(R$styleable.COUITextAppearance_android_lineSpacingMultiplier, 1.0f);
        if (f10 >= 1.0f) {
            setLineSpacing(0.0f, f10);
        }
        obtainStyledAttributes.recycle();
    }

    private static int q(Resources.Theme theme, AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.COUITextView, i10, i11);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUITextView_android_textAppearance, -1);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    private static boolean r(Context context, Resources.Theme theme, AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.COUITextView, i10, i11);
        float f10 = obtainStyledAttributes.getFloat(R$styleable.COUITextView_android_lineSpacingMultiplier, 1.0f);
        obtainStyledAttributes.recycle();
        return f10 != 1.0f;
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int i10) {
        super.setTextAppearance(i10);
        p(this.f7908l.getTheme(), i10);
    }

    public COUITextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.textViewStyle);
    }

    public COUITextView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        int q10;
        this.f7908l = context;
        if (r(context, context.getTheme(), attributeSet, i10, -1) || (q10 = q(context.getTheme(), attributeSet, i10, -1)) == -1) {
            return;
        }
        p(context.getTheme(), q10);
    }
}
