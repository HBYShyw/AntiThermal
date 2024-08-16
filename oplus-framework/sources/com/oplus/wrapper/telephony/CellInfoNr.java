package com.oplus.wrapper.telephony;

import android.telephony.CellIdentityNr;

/* loaded from: classes.dex */
public class CellInfoNr {
    private final android.telephony.CellInfoNr mCellInfoNr = new android.telephony.CellInfoNr();

    public android.telephony.CellInfoNr getCellInfoNr() {
        return this.mCellInfoNr;
    }

    public void setCellIdentity(CellIdentityNr cid) {
        this.mCellInfoNr.setCellIdentity(cid);
    }
}
