package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.PreferenceCategory;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUIPreferenceCategory extends PreferenceCategory {
    public COUIPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreferenceCategory, 0, 0).recycle();
    }
}
