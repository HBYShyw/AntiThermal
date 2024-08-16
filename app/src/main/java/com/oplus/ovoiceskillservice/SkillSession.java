package com.oplus.ovoiceskillservice;

import android.util.Log;
import java.util.Map;
import l7.ISkillCard;
import l7.OVoiceSkillCardFactory;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SkillSession implements ISkillSession {
    private static final String TAG = "OVSS.SkillSession";
    String mActionID;
    String mCmdData;
    String mCommand;
    String mContextType;
    Map<String, String> mParameters;
    String mSessionCode;
    SkillActionListener mSkillActionListener;
    String mUri;

    SkillSession(String str, String str2) {
        this.mSessionCode = str;
        this.mActionID = str2;
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public void cancel() {
        Log.e(TAG, "cancel");
        SkillActionListener skillActionListener = this.mSkillActionListener;
        if (skillActionListener != null) {
            skillActionListener.onCancel(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeValue(String str) {
        Log.e(TAG, "changeValue:" + str);
        Log.e(TAG, "mSkillActionListener:" + this.mSkillActionListener);
        SkillActionListener skillActionListener = this.mSkillActionListener;
        if (skillActionListener != null) {
            skillActionListener.onValueChanged(this, str);
        }
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public boolean completeAction() {
        return completeAction(0, null);
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public void finish() {
        Log.e(TAG, "finish");
        this.mSkillActionListener = null;
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public String getActionID() {
        return this.mActionID;
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public Map<String, String> getParameters() {
        return this.mParameters;
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public void setParameters(Map<String, String> map) {
        this.mParameters = map;
    }

    @Override // com.oplus.ovoiceskillservice.ISkillSession
    public boolean completeAction(int i10, ISkillCard iSkillCard) {
        if (iSkillCard == null) {
            iSkillCard = OVoiceSkillCardFactory.a("");
        }
        if (OVoiceSkillProxy.getInstance().getOVoiceSkillService() == null) {
            Log.e(TAG, "mOVoiceSkillService is null");
            return false;
        }
        Log.d(TAG, "completeAction, result:" + i10);
        try {
            OVoiceSkillProxy.getInstance().getOVoiceSkillService().n(this.mSessionCode, i10, iSkillCard.toString());
            return true;
        } catch (Exception e10) {
            e10.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SkillSession(String str, String str2, SkillActionListener skillActionListener) {
        this.mSessionCode = str;
        this.mActionID = str2;
        this.mSkillActionListener = skillActionListener;
    }
}
