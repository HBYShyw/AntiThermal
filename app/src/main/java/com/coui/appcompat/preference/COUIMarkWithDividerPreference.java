package com.coui.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.preference.PreferenceViewHolder;
import com.support.list.R$attr;
import com.support.list.R$id;
import com.support.list.R$style;

/* loaded from: classes.dex */
public class COUIMarkWithDividerPreference extends COUIMarkPreference {

    /* renamed from: e, reason: collision with root package name */
    private LinearLayout f6953e;

    /* renamed from: f, reason: collision with root package name */
    private LinearLayout f6954f;

    /* renamed from: g, reason: collision with root package name */
    private c f6955g;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIMarkWithDividerPreference.this.f6955g != null) {
                COUIMarkWithDividerPreference.this.f6955g.a();
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIMarkWithDividerPreference.super.onClick();
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a();
    }

    public COUIMarkWithDividerPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }

    @Override // com.coui.appcompat.preference.COUIMarkPreference, androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R$id.main_layout);
        this.f6953e = linearLayout;
        if (linearLayout != null) {
            linearLayout.setOnClickListener(new a());
            this.f6953e.setClickable(isSelectable());
        }
        LinearLayout linearLayout2 = (LinearLayout) preferenceViewHolder.itemView.findViewById(R$id.radio_layout);
        this.f6954f = linearLayout2;
        if (linearLayout2 != null) {
            linearLayout2.setOnClickListener(new b());
            this.f6954f.setClickable(isSelectable());
        }
    }

    public COUIMarkWithDividerPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUIRadioWithDividerPreference);
    }

    public COUIMarkWithDividerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiRadioWithDividerPreferenceStyle);
    }
}
