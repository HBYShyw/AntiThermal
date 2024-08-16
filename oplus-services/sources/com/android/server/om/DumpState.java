package com.android.server.om;

import android.content.om.OverlayIdentifier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DumpState {
    private String mField;
    private String mOverlayName;
    private String mPackageName;
    private int mUserId = -1;
    private boolean mVerbose;

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public void setOverlyIdentifier(String str) {
        OverlayIdentifier fromString = OverlayIdentifier.fromString(str);
        this.mPackageName = fromString.getPackageName();
        this.mOverlayName = fromString.getOverlayName();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getOverlayName() {
        return this.mOverlayName;
    }

    public void setField(String str) {
        this.mField = str;
    }

    public String getField() {
        return this.mField;
    }

    public void setVerbose(boolean z) {
        this.mVerbose = z;
    }

    public boolean isVerbose() {
        return this.mVerbose;
    }
}
