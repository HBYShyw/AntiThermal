package com.oplus.osense.info;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OsenseNotifyRequest implements Parcelable {
    public static final Parcelable.Creator<OsenseNotifyRequest> CREATOR = new Parcelable.Creator<OsenseNotifyRequest>() { // from class: com.oplus.osense.info.OsenseNotifyRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseNotifyRequest createFromParcel(Parcel in) {
            return new OsenseNotifyRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseNotifyRequest[] newArray(int size) {
            return new OsenseNotifyRequest[size];
        }
    };
    private int mMsgSrc;
    private int mMsgType;
    private int mParam1;
    private int mParam2;
    private int mParam3;
    private String mParam4;

    public OsenseNotifyRequest() {
        this.mMsgSrc = -1;
        this.mMsgType = -1;
        this.mParam1 = -1;
        this.mParam2 = -1;
        this.mParam3 = -1;
        this.mParam4 = "";
    }

    public OsenseNotifyRequest(int msgSrc, int msgType) {
        this.mMsgSrc = -1;
        this.mMsgType = -1;
        this.mParam1 = -1;
        this.mParam2 = -1;
        this.mParam3 = -1;
        this.mParam4 = "";
        this.mMsgSrc = msgSrc;
        this.mMsgType = msgType;
    }

    public OsenseNotifyRequest(int msgSrc, int msgType, int param1, int param2, int param3, String param4) {
        this.mMsgSrc = -1;
        this.mMsgType = -1;
        this.mParam1 = -1;
        this.mParam2 = -1;
        this.mParam3 = -1;
        this.mParam4 = "";
        this.mMsgSrc = msgSrc;
        this.mMsgType = msgType;
        this.mParam1 = param1;
        this.mParam2 = param2;
        this.mParam3 = param3;
        this.mParam4 = param4;
    }

    protected OsenseNotifyRequest(Parcel in) {
        this.mMsgSrc = -1;
        this.mMsgType = -1;
        this.mParam1 = -1;
        this.mParam2 = -1;
        this.mParam3 = -1;
        this.mParam4 = "";
        readFromParcel(in);
    }

    public int getMsgSrc() {
        return this.mMsgSrc;
    }

    public int getMsgType() {
        return this.mMsgType;
    }

    public int getParam1() {
        return this.mParam1;
    }

    public int getParam2() {
        return this.mParam2;
    }

    public int getParam3() {
        return this.mParam3;
    }

    public String getParam4() {
        return this.mParam4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMsgSrc);
        dest.writeInt(this.mMsgType);
        dest.writeInt(this.mParam1);
        dest.writeInt(this.mParam2);
        dest.writeInt(this.mParam3);
        dest.writeString(this.mParam4);
    }

    public void readFromParcel(Parcel in) {
        this.mMsgSrc = in.readInt();
        this.mMsgType = in.readInt();
        this.mParam1 = in.readInt();
        this.mParam2 = in.readInt();
        this.mParam3 = in.readInt();
        this.mParam4 = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "OsenseNotifyRequest{msgSrc=" + this.mMsgSrc + ", msgType=" + this.mMsgType + ", param1=" + this.mParam1 + ", param2=" + this.mParam2 + ", param3=" + this.mParam3 + ", param4='" + this.mParam4 + "'}";
    }
}
