package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class StaticEventBean extends TrackEvent {
    private static final String EVENT_BODY = "eventBody";
    private static final String UPLOAD_MODE = "uploadMode";
    private String mBody;
    private int mUploadMode;

    public StaticEventBean(Context context, int i10, String str) {
        super(context);
        this.mUploadMode = i10;
        this.mBody = str;
        addTrackInfo(UPLOAD_MODE, i10);
        addTrackInfo(EVENT_BODY, this.mBody);
    }

    public String getBody() {
        return this.mBody;
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.STATIC_EVENT_TYPE;
    }

    public int getUploadMode() {
        return this.mUploadMode;
    }

    public void setBody(String str) {
        this.mBody = str;
        addTrackInfo(EVENT_BODY, str);
    }

    public void setUploadMode(int i10) {
        this.mUploadMode = i10;
        addTrackInfo(UPLOAD_MODE, i10);
    }

    public String toString() {
        return "uploadMode is :" + this.mUploadMode + "\nbody is :" + getBody() + "\n";
    }
}
