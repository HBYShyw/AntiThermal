package com.oplus.wrapper.telephony;

import android.app.PendingIntent;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SmsManager {
    private final android.telephony.SmsManager mSmsManager;

    public SmsManager(android.telephony.SmsManager smsManager) {
        this.mSmsManager = smsManager;
    }

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        this.mSmsManager.sendTextMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent, priority, expectMore, validityPeriod);
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        this.mSmsManager.sendMultipartTextMessage(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, priority, expectMore, validityPeriod);
    }

    public ArrayList<android.telephony.SmsMessage> getAllMessagesFromIcc() {
        return this.mSmsManager.getAllMessagesFromIcc();
    }

    public boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {
        return this.mSmsManager.copyMessageToIcc(smsc, pdu, status);
    }

    public boolean deleteMessageFromIcc(int messageIndex) {
        return this.mSmsManager.deleteMessageFromIcc(messageIndex);
    }
}
