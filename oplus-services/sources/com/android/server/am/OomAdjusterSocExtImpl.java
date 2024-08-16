package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OomAdjusterSocExtImpl implements IOomAdjusterSocExt {
    private static final String TAG = "OomAdjusterSocExtImpl";
    OomAdjuster mOomAdjuster;

    @Override // com.android.server.am.IOomAdjusterSocExt
    public void backgroundAppsTransition(ProcessRecord processRecord, ProcessStateRecord processStateRecord) {
    }

    @Override // com.android.server.am.IOomAdjusterSocExt
    public void initPerfConfig() {
    }

    @Override // com.android.server.am.IOomAdjusterSocExt
    public void topAppRenderThreadBoost(ProcessRecord processRecord) {
    }

    public OomAdjusterSocExtImpl(Object obj) {
        this.mOomAdjuster = (OomAdjuster) obj;
    }
}
