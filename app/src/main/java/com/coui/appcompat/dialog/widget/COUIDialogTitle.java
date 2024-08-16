package com.coui.appcompat.dialog.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import androidx.appcompat.R$styleable;
import androidx.appcompat.widget.AppCompatTextView;

/* loaded from: classes.dex */
public class COUIDialogTitle extends AppCompatTextView {
    public COUIDialogTitle(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int i10, int i11) {
        int lineCount;
        super.onMeasure(i10, i11);
        Layout layout = getLayout();
        if (layout == null || (lineCount = layout.getLineCount()) <= 0 || layout.getEllipsisCount(lineCount - 1) <= 0) {
            return;
        }
        setSingleLine(false);
        setMaxLines(3);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, R$styleable.TextAppearance, R.attr.textAppearanceMedium, R.style.TextAppearance.Medium);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, 0);
        if (dimensionPixelSize != 0) {
            setTextSize(0, dimensionPixelSize);
        }
        obtainStyledAttributes.recycle();
        super.onMeasure(i10, i11);
    }

    public COUIDialogTitle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUIDialogTitle(Context context) {
        super(context);
    }
}
