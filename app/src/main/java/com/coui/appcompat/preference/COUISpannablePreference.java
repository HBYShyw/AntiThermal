package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.support.list.R$attr;
import com.support.list.R$style;

/* loaded from: classes.dex */
public class COUISpannablePreference extends COUIPreference {
    public COUISpannablePreference(Context context) {
        this(context, null);
    }

    public COUISpannablePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSpannablePreferenceStyle);
    }

    public COUISpannablePreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10, R$style.Preference_COUI_COUISpannablePreference);
    }
}
