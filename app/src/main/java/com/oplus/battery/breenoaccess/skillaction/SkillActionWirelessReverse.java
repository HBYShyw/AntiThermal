package com.oplus.battery.breenoaccess.skillaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.oplus.battery.R;
import com.oplus.ovoiceskillservice.Autowired;
import com.oplus.ovoiceskillservice.ISkillSession;
import com.oplus.ovoiceskillservice.SkillActionListener;
import com.oplus.ovoiceskillservice.SkillActions;
import com.oplus.powermanager.fuelgaue.WirelessReverseChargingActivity;
import l7.ISkillCard;
import l7.OVoiceSkillCardFactory;
import y5.b;

@SkillActions(path = "Reverse")
/* loaded from: classes.dex */
public class SkillActionWirelessReverse extends SkillActionListener {

    /* renamed from: a, reason: collision with root package name */
    @Autowired
    private Context f9811a;

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onSessionCreated(ISkillSession iSkillSession) {
        Intent intent;
        ISkillCard a10;
        if (!b.x()) {
            a10 = OVoiceSkillCardFactory.a(this.f9811a.getResources().getString(R.string.power_wireless_reserve_not_support));
            intent = null;
        } else {
            intent = new Intent();
            intent.setComponent(new ComponentName("com.oplus.battery", WirelessReverseChargingActivity.class.getName()));
            intent.setFlags(268435456);
            a10 = OVoiceSkillCardFactory.a(this.f9811a.getResources().getString(R.string.power_wireless_reserve_support));
        }
        iSkillSession.completeAction(0, a10);
        if (intent != null) {
            this.f9811a.startActivity(intent);
        }
        super.onSessionCreated(iSkillSession);
    }
}
