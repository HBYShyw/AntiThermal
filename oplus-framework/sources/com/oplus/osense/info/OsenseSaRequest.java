package com.oplus.osense.info;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OsenseSaRequest implements Parcelable {
    public static final Parcelable.Creator<OsenseSaRequest> CREATOR = new Parcelable.Creator<OsenseSaRequest>() { // from class: com.oplus.osense.info.OsenseSaRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseSaRequest createFromParcel(Parcel in) {
            return new OsenseSaRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseSaRequest[] newArray(int size) {
            return new OsenseSaRequest[size];
        }
    };
    private String mAction;
    private Bundle mInfo;
    private String mScene;
    private int mTimeout;

    public OsenseSaRequest() {
        this.mScene = "";
        this.mAction = "";
        this.mTimeout = -1;
        this.mInfo = null;
    }

    public OsenseSaRequest(String scene, String action, int timeout) {
        this.mScene = "";
        this.mAction = "";
        this.mTimeout = -1;
        this.mInfo = null;
        this.mScene = scene;
        this.mAction = action;
        this.mTimeout = timeout;
    }

    public OsenseSaRequest(Bundle info) {
        this.mScene = "";
        this.mAction = "";
        this.mTimeout = -1;
        this.mInfo = null;
        this.mInfo = info;
    }

    protected OsenseSaRequest(Parcel in) {
        this.mScene = "";
        this.mAction = "";
        this.mTimeout = -1;
        this.mInfo = null;
        readFromParcel(in);
    }

    public String getScene() {
        return this.mScene;
    }

    public String getAction() {
        return this.mAction;
    }

    public int getTimeout() {
        return this.mTimeout;
    }

    public Bundle getInfo() {
        return this.mInfo;
    }

    public String toString() {
        return "OsenseSaRequest{scene='" + this.mScene + "', action='" + this.mAction + "', timeout=" + this.mTimeout + ", info=" + this.mInfo + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mScene);
        dest.writeString(this.mAction);
        dest.writeInt(this.mTimeout);
        dest.writeBundle(this.mInfo);
    }

    protected void readFromParcel(Parcel in) {
        this.mScene = in.readString();
        this.mAction = in.readString();
        this.mTimeout = in.readInt();
        this.mInfo = in.readBundle();
    }
}
