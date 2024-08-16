package com.oplus.battery.breenoaccess.skillaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.oplus.battery.R;
import com.oplus.ovoiceskillservice.Autowired;
import com.oplus.ovoiceskillservice.ISkillSession;
import com.oplus.ovoiceskillservice.SkillActionListener;
import com.oplus.ovoiceskillservice.SkillActions;
import com.oplus.powermanager.fuelgaue.WirelessChargingSettingsActivity;
import l7.ISkillCard;
import l7.OVoiceSkillCardFactory;
import y5.b;

@SkillActions(path = "Charging")
/* loaded from: classes.dex */
public class SkillActionWirelessCharging extends SkillActionListener {

    /* renamed from: a, reason: collision with root package name */
    @Autowired
    private Context f9810a;

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onSessionCreated(ISkillSession iSkillSession) {
        Intent intent;
        ISkillCard a10;
        if (!b.x()) {
            a10 = OVoiceSkillCardFactory.a(this.f9810a.getResources().getString(R.string.power_wireless_charging_not_support));
            intent = null;
        } else {
            intent = new Intent();
            intent.setComponent(new ComponentName("com.oplus.battery", WirelessChargingSettingsActivity.class.getName()));
            intent.setFlags(268435456);
            a10 = OVoiceSkillCardFactory.a(this.f9810a.getResources().getString(R.string.power_wireless_charging_support));
        }
        iSkillSession.completeAction(0, a10);
        if (intent != null) {
            this.f9810a.startActivity(intent);
        }
        super.onSessionCreated(iSkillSession);
    }
}
