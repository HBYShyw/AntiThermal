package com.oplus.powermanager.powercurve.graph;

import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b9.PowerSipper;
import com.oplus.battery.R;

/* loaded from: classes2.dex */
public class PowerSipperPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private PowerSipper f10328e;

    /* renamed from: f, reason: collision with root package name */
    private int f10329f;

    /* renamed from: g, reason: collision with root package name */
    private ImageView f10330g;

    public PowerSipper c() {
        return this.f10328e;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.a(R.id.item_arrow_icon);
        this.f10330g = imageView;
        imageView.setVisibility(this.f10329f);
    }
}
