package com.oplus.uifirst;

import android.util.IntArray;
import java.util.Collection;

/* loaded from: classes.dex */
public class OplusUIFirstManagerExtImpl implements IOplusUIFirstManagerExt {
    private OplusUIFirstManager mInstace = OplusUIFirstManager.getInstance();

    public OplusUIFirstManagerExtImpl(Object base) {
    }

    public void onAppStatusChanged(int status, String packageName, String activityName) {
        this.mInstace.onAppStatusChanged(status, packageName, activityName);
    }

    public void setTaskAsRemoteAnimationUx(int appPid, int renderThreadTid, IntArray hwuiTasks, String packageName, boolean isRemoteAnimation) {
        this.mInstace.setTaskAsRemoteAnimationUx(appPid, renderThreadTid, hwuiTasks, packageName, isRemoteAnimation);
    }

    public int[] adjustUxProcess(String packageName, int status, int appPid, int renderThreadTid, IntArray hwuiTasks, Collection<Integer> glThreads, boolean isRemoteAnimation) {
        return this.mInstace.adjustUxProcess(packageName, status, appPid, renderThreadTid, hwuiTasks, glThreads, isRemoteAnimation);
    }

    public void handleProcessStop(String packageName, int uid, int pid) {
        this.mInstace.handleProcessStop(packageName, uid, pid);
    }

    public void addApplicationGlThread(String packageName, int pid, int tid) {
        this.mInstace.addApplicationGlThread(packageName, pid, tid);
    }

    public void removeApplicationGlThread(String packageName, int pid, int tid) {
        this.mInstace.removeApplicationGlThread(packageName, pid, tid);
    }

    public void setRenderThreadTid(String packageName, int pid, int tid) {
        this.mInstace.setRenderThreadTid(packageName, pid, tid);
    }

    public void setUxThreadValue(int pid, int tid, String value) {
        this.mInstace.setUxThreadValue(pid, tid, value);
    }

    public void setUxThreadValueByFile(int pid, int tid, int value) {
        this.mInstace.setUxThreadValueByFile(pid, tid, value);
    }

    public void setImFlag(int tid, int im) {
        this.mInstace.setImFlag(tid, im);
    }
}
