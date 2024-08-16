package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.support.list.R$attr;
import com.support.list.R$style;

/* loaded from: classes.dex */
public class COUIJumpPreference extends COUIPreference {
    public COUIJumpPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }

    public COUIJumpPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUIJumpPreference);
    }

    public COUIJumpPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiJumpPreferenceStyle);
    }

    public COUIJumpPreference(Context context) {
        this(context, null);
    }
}
