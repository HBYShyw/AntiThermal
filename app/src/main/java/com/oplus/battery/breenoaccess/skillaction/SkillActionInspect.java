package com.oplus.battery.breenoaccess.skillaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.oplus.battery.R;
import com.oplus.ovoiceskillservice.Autowired;
import com.oplus.ovoiceskillservice.ISkillSession;
import com.oplus.ovoiceskillservice.SkillActionListener;
import com.oplus.ovoiceskillservice.SkillActions;
import com.oplus.powermanager.fuelgaue.PowerInspectActivity;
import l7.OVoiceSkillCardFactory;

@SkillActions(path = "Inspect")
/* loaded from: classes.dex */
public class SkillActionInspect extends SkillActionListener {

    /* renamed from: a, reason: collision with root package name */
    @Autowired
    private Context f9809a;

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onSessionCreated(ISkillSession iSkillSession) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.oplus.battery", PowerInspectActivity.class.getName()));
        intent.setFlags(268435456);
        iSkillSession.completeAction(0, OVoiceSkillCardFactory.a(this.f9809a.getResources().getString(R.string.power_inspect_support)));
        this.f9809a.startActivity(intent);
        super.onSessionCreated(iSkillSession);
    }
}
