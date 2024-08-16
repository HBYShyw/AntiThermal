package com.oplus.wrapper.nfc;

import android.content.Context;

/* loaded from: classes.dex */
public class NfcAdapter {
    private final android.nfc.NfcAdapter mNfcAdapter;

    public NfcAdapter(Context context) {
        this.mNfcAdapter = android.nfc.NfcAdapter.getNfcAdapter(context);
    }

    public android.nfc.NfcAdapter getNfcAdapter() {
        return this.mNfcAdapter;
    }

    public boolean disable(boolean persist) {
        return this.mNfcAdapter.disable(persist);
    }

    public boolean enable() {
        return this.mNfcAdapter.enable();
    }
}
