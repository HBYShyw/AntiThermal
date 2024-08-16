package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class DebugBean extends TrackEvent {
    private static final String DEBUG = "debug";
    private boolean mFlag;

    public DebugBean(Context context, boolean z10) {
        super(context);
        this.mFlag = z10;
        addTrackInfo(DEBUG, z10);
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.DEBUG_TYPE;
    }

    public boolean getFlag() {
        return this.mFlag;
    }

    public void setFlag(boolean z10) {
        this.mFlag = z10;
        addTrackInfo(DEBUG, z10);
    }

    public String toString() {
        return "type is :" + getEventType() + "\nflag is :" + getFlag() + "\n";
    }
}
