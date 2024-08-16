package com.oplus.wrapper.telecom;

import android.telecom.Call;

/* loaded from: classes.dex */
public class Call {

    /* loaded from: classes.dex */
    public static class Details {
        private final Call.Details mDetails;

        public Details(Call.Details details) {
            this.mDetails = details;
        }

        public String getTelecomCallId() {
            return this.mDetails.getTelecomCallId();
        }
    }
}
