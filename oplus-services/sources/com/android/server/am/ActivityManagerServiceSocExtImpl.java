package com.android.server.am;

import android.os.Handler;
import android.os.Message;
import com.mediatek.server.MtkSystemServer;
import com.mediatek.server.MtkSystemServiceFactory;
import com.mediatek.server.am.AmsExt;
import com.mediatek.server.anr.AnrManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ActivityManagerServiceSocExtImpl implements IActivityManagerServiceSocExt {
    private static final String TAG = "ActivityManagerServiceSocExtImpl";
    public static MtkSystemServer sMtkSystemServerIns = MtkSystemServer.getInstance();
    ActivityManagerService mAms;
    public AmsExt mAmsExt = MtkSystemServiceFactory.getInstance().makeAmsExt();
    public AnrManager mAnrManager = MtkSystemServiceFactory.getInstance().makeAnrManager();

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void addPidLocked(ProcessRecord processRecord) {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void appDiedLocked(ProcessRecord processRecord, int i) {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void compactAllSystem() {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void perfHint(ProcessRecord processRecord, int i) {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void removePidLocked(ProcessRecord processRecord) {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void updateForceStopKillFlag() {
    }

    public ActivityManagerServiceSocExtImpl(Object obj) {
        this.mAms = (ActivityManagerService) obj;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public boolean delayMessage(Handler handler, Message message, int i, int i2) {
        return this.mAnrManager.delayMessage(handler, message, i, i2);
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void addAnrManagerService() {
        this.mAnrManager.AddAnrManagerService();
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void startAnrManagerService(int i) {
        this.mAnrManager.startAnrManagerService(i);
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void writeBootCompletedEvent() {
        this.mAnrManager.writeEvent(9001);
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public boolean isAnrDeferrable() {
        return this.mAnrManager.isAnrDeferrable();
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void onAddErrorToDropBox(String str, String str2, int i) {
        this.mAmsExt.onAddErrorToDropBox(str, str2, i);
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public Object getAnrManager() {
        return this.mAnrManager;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public Object getAmsExt() {
        return this.mAmsExt;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void onNotifyAppCrash(int i, int i2, String str) {
        this.mAmsExt.onNotifyAppCrash(i, i2, str);
    }
}
