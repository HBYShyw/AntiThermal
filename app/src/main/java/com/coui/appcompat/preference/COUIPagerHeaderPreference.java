package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.support.list.R$layout;

/* loaded from: classes.dex */
public class COUIPagerHeaderPreference extends Preference {
    public COUIPagerHeaderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.coui_pager_header_preference);
    }
}
