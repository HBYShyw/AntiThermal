package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.support.list.R$attr;
import com.support.list.R$id;
import com.support.list.R$style;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUISlideSelectPreference extends COUIPreference {
    private int F;
    Context G;
    CharSequence H;
    private TextView I;

    public COUISlideSelectPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSlideSelectPreferenceStyle);
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        int i10 = R$id.coui_preference;
        View a10 = preferenceViewHolder.a(i10);
        if (a10 == null) {
            return;
        }
        a10.setTag(new Object());
        View findViewById = a10.findViewById(i10);
        if (findViewById != null) {
            int i11 = this.F;
            if (i11 == 1) {
                findViewById.setClickable(false);
            } else if (i11 == 2) {
                findViewById.setClickable(true);
            }
        }
        TextView textView = (TextView) a10.findViewById(R$id.coui_statusText_select);
        this.I = textView;
        if (textView != null) {
            CharSequence charSequence = this.H;
            if (!TextUtils.isEmpty(charSequence)) {
                this.I.setText(charSequence);
                this.I.setVisibility(0);
            } else {
                this.I.setVisibility(8);
            }
        }
    }

    public COUISlideSelectPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUISelectPreference);
    }

    public COUISlideSelectPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10);
        this.F = 0;
        this.G = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISlideSelectPreference, i10, i11);
        this.H = obtainStyledAttributes.getText(R$styleable.COUISlideSelectPreference_coui_select_status1);
        obtainStyledAttributes.recycle();
    }
}
