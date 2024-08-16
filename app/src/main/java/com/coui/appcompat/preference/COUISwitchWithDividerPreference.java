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
public class COUISwitchWithDividerPreference extends COUISwitchPreference {
    private c A;

    /* renamed from: y, reason: collision with root package name */
    private LinearLayout f7040y;

    /* renamed from: z, reason: collision with root package name */
    private LinearLayout f7041z;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUISwitchWithDividerPreference.this.A != null) {
                COUISwitchWithDividerPreference.this.A.a();
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUISwitchWithDividerPreference.super.onClick();
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a();
    }

    public COUISwitchWithDividerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSwitchWithDividerPreferenceStyle);
    }

    @Override // com.coui.appcompat.preference.COUISwitchPreference, androidx.preference.SwitchPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R$id.main_layout);
        this.f7040y = linearLayout;
        if (linearLayout != null) {
            linearLayout.setOnClickListener(new a());
            this.f7040y.setClickable(isSelectable());
        }
        LinearLayout linearLayout2 = (LinearLayout) preferenceViewHolder.itemView.findViewById(R$id.switch_layout);
        this.f7041z = linearLayout2;
        if (linearLayout2 != null) {
            linearLayout2.setOnClickListener(new b());
            this.f7041z.setClickable(isSelectable());
        }
    }

    public COUISwitchWithDividerPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUISwitchWithDividerPreference);
    }

    public COUISwitchWithDividerPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }
}
