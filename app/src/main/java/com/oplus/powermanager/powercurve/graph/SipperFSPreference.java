package com.oplus.powermanager.powercurve.graph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.oplus.battery.R;

/* loaded from: classes2.dex */
public class SipperFSPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private TextView f10331e;

    /* renamed from: f, reason: collision with root package name */
    private final Context f10332f;

    /* renamed from: g, reason: collision with root package name */
    View.OnClickListener f10333g;

    public SipperFSPreference(Context context) {
        super(context);
        this.f10331e = null;
        this.f10333g = null;
        this.f10332f = context;
        c();
    }

    private void c() {
        setLayoutResource(R.layout.force_stop_layout);
    }

    public void d(View.OnClickListener onClickListener) {
        this.f10333g = onClickListener;
    }

    public void e() {
        TextView textView = this.f10331e;
        if (textView != null) {
            textView.setTextColor(this.f10332f.getColor(R.color.rm_button_gray));
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.a(R.id.sipper_force_stop);
        this.f10331e = textView;
        textView.setOnClickListener(this.f10333g);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        super.onDetached();
        this.f10333g = null;
    }

    public SipperFSPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10331e = null;
        this.f10333g = null;
        this.f10332f = context;
        c();
    }

    public SipperFSPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f10331e = null;
        this.f10333g = null;
        this.f10332f = context;
        c();
    }

    public SipperFSPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10331e = null;
        this.f10333g = null;
        this.f10332f = context;
        c();
    }
}
