package com.oplus.notification.redpackage;

/* loaded from: classes.dex */
public class AdaptationEnvelopeInfo {
    String mEnvelopeContentTag;
    String mEnvelopeFilterField;
    String mEnvelopeFilterValue;
    String mEnvelopeGroupTag;
    String mEnvelopeUserField;
    String mEnvelopeUserNameTagFirst;
    String mEnvelopeUserNameTagLast;
    String mPkgVersion;

    public String getPkgVersion() {
        return this.mPkgVersion;
    }

    public void setPkgVersion(String pkgVersion) {
        this.mPkgVersion = pkgVersion;
    }

    public String getEnvelopeFilterField() {
        return this.mEnvelopeFilterField;
    }

    public void setEnvelopeFilterField(String envelopeFilterField) {
        this.mEnvelopeFilterField = envelopeFilterField;
    }

    public String getEnvelopeFilterValue() {
        return this.mEnvelopeFilterValue;
    }

    public void setEnvelopeFilterValue(String envelopeFilterValue) {
        this.mEnvelopeFilterValue = envelopeFilterValue;
    }

    public String getEnvelopeUserField() {
        return this.mEnvelopeUserField;
    }

    public void setEnvelopeUserField(String envelopeUserField) {
        this.mEnvelopeUserField = envelopeUserField;
    }

    public String getEnvelopeGroupTag() {
        return this.mEnvelopeGroupTag;
    }

    public void setEnvelopeGroupTag(String envelopeGroupTag) {
        this.mEnvelopeGroupTag = envelopeGroupTag;
    }

    public String getEnvelopeUserNameTagFirst() {
        return this.mEnvelopeUserNameTagFirst;
    }

    public void setEnvelopeUserNameTagFirst(String envelopeUserNameTagFirst) {
        this.mEnvelopeUserNameTagFirst = envelopeUserNameTagFirst;
    }

    public String getEnvelopeUserNameTagLast() {
        return this.mEnvelopeUserNameTagLast;
    }

    public void setEnvelopeUserNameTagLast(String envelopeUserNameTagLast) {
        this.mEnvelopeUserNameTagLast = envelopeUserNameTagLast;
    }

    public String getEnvelopeContentTag() {
        return this.mEnvelopeContentTag;
    }

    public void setEnvelopeContentTag(String envelopeContentTag) {
        this.mEnvelopeContentTag = envelopeContentTag;
    }

    public String toString() {
        return this.mPkgVersion + this.mEnvelopeFilterField + this.mEnvelopeFilterValue + this.mEnvelopeUserField + this.mEnvelopeGroupTag + this.mEnvelopeUserNameTagFirst + this.mEnvelopeUserNameTagLast + this.mEnvelopeContentTag;
    }
}
