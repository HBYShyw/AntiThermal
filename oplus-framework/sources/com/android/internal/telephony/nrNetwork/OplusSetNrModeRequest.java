package com.android.internal.telephony.nrNetwork;

import android.os.Message;

/* loaded from: classes.dex */
public class OplusSetNrModeRequest {
    private boolean forceReport;
    private int mode;
    private Message onComplete;
    private int slotId;

    public OplusSetNrModeRequest() {
    }

    public OplusSetNrModeRequest(int slotId, int mode, boolean forceReport, Message onComplete) {
        this.slotId = slotId;
        this.mode = mode;
        this.forceReport = forceReport;
        this.onComplete = onComplete;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isForceReport() {
        return this.forceReport;
    }

    public void setForceReport(boolean forceReport) {
        this.forceReport = forceReport;
    }

    public Message getOnCompleteMsg() {
        return this.onComplete;
    }

    public void setOnCompleteMsg(Message onComplete) {
        this.onComplete = onComplete;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("slotId = " + this.slotId);
        sb.append(" ,mode = " + this.mode);
        sb.append(" ,forceReport = " + this.forceReport);
        sb.append(" ,onComplete = " + this.onComplete);
        return sb.toString();
    }
}
