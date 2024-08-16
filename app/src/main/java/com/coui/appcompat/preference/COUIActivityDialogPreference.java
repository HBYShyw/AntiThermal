package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.support.list.R$attr;

/* loaded from: classes.dex */
public class COUIActivityDialogPreference extends COUIListPreference {
    public COUIActivityDialogPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }

    public COUIActivityDialogPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIActivityDialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiActivityDialogPreferenceStyle);
    }
}
