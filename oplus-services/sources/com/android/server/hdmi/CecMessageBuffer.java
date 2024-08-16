package com.android.server.hdmi;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class CecMessageBuffer {
    private List<HdmiCecMessage> mBuffer = new ArrayList();
    private HdmiControlService mHdmiControlService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CecMessageBuffer(HdmiControlService hdmiControlService) {
        this.mHdmiControlService = hdmiControlService;
    }

    public boolean bufferMessage(HdmiCecMessage hdmiCecMessage) {
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode == 4 || opcode == 13) {
            bufferImageOrTextViewOn(hdmiCecMessage);
            return true;
        }
        if (opcode == 112) {
            bufferSystemAudioModeRequest(hdmiCecMessage);
            return true;
        }
        if (opcode == 128) {
            bufferRoutingChange(hdmiCecMessage);
            return true;
        }
        if (opcode == 130) {
            bufferActiveSource(hdmiCecMessage);
            return true;
        }
        if (opcode != 134) {
            return false;
        }
        bufferSetStreamPath(hdmiCecMessage);
        return true;
    }

    public void processMessages() {
        for (final HdmiCecMessage hdmiCecMessage : this.mBuffer) {
            this.mHdmiControlService.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.CecMessageBuffer.1
                @Override // java.lang.Runnable
                public void run() {
                    CecMessageBuffer.this.mHdmiControlService.handleCecCommand(hdmiCecMessage);
                }
            });
        }
        this.mBuffer.clear();
    }

    private void bufferActiveSource(HdmiCecMessage hdmiCecMessage) {
        if (replaceMessageIfBuffered(hdmiCecMessage, 130)) {
            return;
        }
        this.mBuffer.add(hdmiCecMessage);
    }

    private void bufferImageOrTextViewOn(HdmiCecMessage hdmiCecMessage) {
        if (replaceMessageIfBuffered(hdmiCecMessage, 4) || replaceMessageIfBuffered(hdmiCecMessage, 13)) {
            return;
        }
        this.mBuffer.add(hdmiCecMessage);
    }

    private void bufferSystemAudioModeRequest(HdmiCecMessage hdmiCecMessage) {
        if (replaceMessageIfBuffered(hdmiCecMessage, HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE)) {
            return;
        }
        this.mBuffer.add(hdmiCecMessage);
    }

    private void bufferRoutingChange(HdmiCecMessage hdmiCecMessage) {
        if (replaceMessageIfBuffered(hdmiCecMessage, 128)) {
            return;
        }
        this.mBuffer.add(hdmiCecMessage);
    }

    private void bufferSetStreamPath(HdmiCecMessage hdmiCecMessage) {
        if (replaceMessageIfBuffered(hdmiCecMessage, 134)) {
            return;
        }
        this.mBuffer.add(hdmiCecMessage);
    }

    public List<HdmiCecMessage> getBuffer() {
        return new ArrayList(this.mBuffer);
    }

    private boolean replaceMessageIfBuffered(HdmiCecMessage hdmiCecMessage, int i) {
        for (int i2 = 0; i2 < this.mBuffer.size(); i2++) {
            if (this.mBuffer.get(i2).getOpcode() == i) {
                this.mBuffer.set(i2, hdmiCecMessage);
                return true;
            }
        }
        return false;
    }
}
