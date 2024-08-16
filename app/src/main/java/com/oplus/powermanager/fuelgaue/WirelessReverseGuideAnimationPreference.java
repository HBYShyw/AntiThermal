package com.oplus.powermanager.fuelgaue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class WirelessReverseGuideAnimationPreference extends COUIPreference {
    private Context F;
    private EffectiveAnimationView G;

    public WirelessReverseGuideAnimationPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        h(context);
    }

    private void h(Context context) {
        this.F = context;
        setLayoutResource(R.layout.wireless_reverse_guide_animation);
    }

    private void i(View view) {
        view.setForceDarkAllowed(false);
        EffectiveAnimationView effectiveAnimationView = (EffectiveAnimationView) view.findViewById(R.id.wireless_reverse_guide_animation);
        this.G = effectiveAnimationView;
        effectiveAnimationView.s(true);
        if (COUIDarkModeUtil.a(this.F)) {
            this.G.setImageAssetsFolder("images/");
            this.G.setAnimation("guide_animation_dark.json");
        } else {
            this.G.setAnimation("guide_animation.json");
        }
    }

    public void j() {
        EffectiveAnimationView effectiveAnimationView = this.G;
        if (effectiveAnimationView == null || effectiveAnimationView.r()) {
            return;
        }
        this.G.u();
    }

    public void l() {
        EffectiveAnimationView effectiveAnimationView = this.G;
        if (effectiveAnimationView == null) {
            return;
        }
        effectiveAnimationView.j();
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
        i(preferenceViewHolder.itemView);
        j();
    }

    public WirelessReverseGuideAnimationPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        h(context);
    }

    public WirelessReverseGuideAnimationPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        h(context);
    }

    public WirelessReverseGuideAnimationPreference(Context context) {
        super(context);
        h(context);
    }
}
