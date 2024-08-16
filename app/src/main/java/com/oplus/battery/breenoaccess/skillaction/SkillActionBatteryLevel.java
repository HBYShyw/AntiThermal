package com.oplus.battery.breenoaccess.skillaction;

import android.content.Context;
import com.oplus.battery.R;
import com.oplus.ovoiceskillservice.Autowired;
import com.oplus.ovoiceskillservice.ISkillSession;
import com.oplus.ovoiceskillservice.SkillActionListener;
import com.oplus.ovoiceskillservice.SkillActions;
import l7.OVoiceSkillCardFactory;
import t8.BatteryRemainTimeCalculator;
import t8.PowerUsageManager;

@SkillActions(path = "Level")
/* loaded from: classes.dex */
public class SkillActionBatteryLevel extends SkillActionListener {

    /* renamed from: a, reason: collision with root package name */
    @Autowired
    private Context f9808a;

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onSessionCreated(ISkillSession iSkillSession) {
        int r10 = PowerUsageManager.x(this.f9808a).r();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f9808a.getResources().getString(R.string.power_level_support, Integer.valueOf(r10)));
        Context context = this.f9808a;
        sb2.append(BatteryRemainTimeCalculator.d(context, BatteryRemainTimeCalculator.e(context).c(r10, -1)));
        iSkillSession.completeAction(0, OVoiceSkillCardFactory.a(sb2.toString()));
        super.onSessionCreated(iSkillSession);
    }
}
