package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class UserActionBean extends TrackEvent {
    private static final String ACTION_AMOUNT = "actionAmount";
    private static final String ACTION_CODE = "actionCode";
    private static final String ACTION_TIME = "actionTime";
    private int mAmount;
    private int mCode;
    private String mDate;

    public UserActionBean(Context context, int i10, String str, int i11) {
        super(context);
        this.mCode = i10;
        this.mDate = str;
        this.mAmount = i11;
        addTrackInfo(ACTION_CODE, i10);
        addTrackInfo(ACTION_AMOUNT, this.mAmount);
        addTrackInfo(ACTION_TIME, this.mDate);
    }

    public int getActionAmount() {
        return this.mAmount;
    }

    public int getActionCode() {
        return this.mCode;
    }

    public String getActionDate() {
        return this.mDate;
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.USER_ACTION;
    }

    public void setActionAmount(int i10) {
        this.mAmount = i10;
        addTrackInfo(ACTION_AMOUNT, i10);
    }

    public void setActionCode(int i10) {
        this.mCode = i10;
        addTrackInfo(ACTION_CODE, i10);
    }

    public void setActionDate(String str) {
        this.mDate = str;
        addTrackInfo(ACTION_TIME, str);
    }

    public String toString() {
        return "action code is: " + getActionCode() + "\naction amount is: " + getActionAmount() + "\naction date is: " + getActionDate() + "\n";
    }
}
