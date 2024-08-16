package com.oplus.wrapper.telephony;

/* loaded from: classes.dex */
public class SmsMessage {
    private final android.telephony.SmsMessage mSmsMessage;

    public SmsMessage(android.telephony.SmsMessage smsMessage) {
        this.mSmsMessage = smsMessage;
    }

    public int getSubId() {
        return this.mSmsMessage.getSubId();
    }

    public void setSubId(int subId) {
        this.mSmsMessage.setSubId(subId);
    }

    public boolean isWrappedSmsMessageNull() {
        return this.mSmsMessage.mWrappedSmsMessage == null;
    }
}
