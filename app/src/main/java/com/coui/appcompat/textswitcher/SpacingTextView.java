package com.coui.appcompat.textswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;

/* loaded from: classes.dex */
public class SpacingTextView extends AppCompatTextView {
    public SpacingTextView(Context context) {
        super(context);
    }

    private void p(CharSequence charSequence, TextView.BufferType bufferType) {
        if (charSequence == null) {
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        for (int i10 = 0; i10 < charSequence.length(); i10++) {
            sb2.append("" + charSequence.charAt(i10));
        }
        sb2.append(" ");
        super.setText(sb2, bufferType);
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        p(charSequence, bufferType);
    }

    public SpacingTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SpacingTextView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
