package com.oplus.orms.info;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OrmsNotifyParam implements Parcelable {
    public static final Parcelable.Creator<OrmsNotifyParam> CREATOR = new Parcelable.Creator<OrmsNotifyParam>() { // from class: com.oplus.orms.info.OrmsNotifyParam.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OrmsNotifyParam createFromParcel(Parcel in) {
            return new OrmsNotifyParam(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OrmsNotifyParam[] newArray(int size) {
            return new OrmsNotifyParam[size];
        }
    };
    public int msgSrc;
    public int msgType;
    public int param1;
    public int param2;
    public int param3;
    public String param4;

    public OrmsNotifyParam() {
        this.msgSrc = -1;
        this.msgType = -1;
        this.param1 = -1;
        this.param2 = -1;
        this.param3 = -1;
        this.param4 = "";
    }

    public OrmsNotifyParam(int msgSrc, int msgType) {
        this.msgSrc = -1;
        this.msgType = -1;
        this.param1 = -1;
        this.param2 = -1;
        this.param3 = -1;
        this.param4 = "";
        this.msgSrc = msgSrc;
        this.msgType = msgType;
    }

    public OrmsNotifyParam(int msgSrc, int msgType, int param1, int param2, int param3, String param4) {
        this.msgSrc = -1;
        this.msgType = -1;
        this.param1 = -1;
        this.param2 = -1;
        this.param3 = -1;
        this.param4 = "";
        this.msgSrc = msgSrc;
        this.msgType = msgType;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    protected OrmsNotifyParam(Parcel in) {
        this.msgSrc = -1;
        this.msgType = -1;
        this.param1 = -1;
        this.param2 = -1;
        this.param3 = -1;
        this.param4 = "";
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.msgSrc);
        dest.writeInt(this.msgType);
        dest.writeInt(this.param1);
        dest.writeInt(this.param2);
        dest.writeInt(this.param3);
        dest.writeString(this.param4);
    }

    public void readFromParcel(Parcel in) {
        this.msgSrc = in.readInt();
        this.msgType = in.readInt();
        this.param1 = in.readInt();
        this.param2 = in.readInt();
        this.param3 = in.readInt();
        this.param4 = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "OrmsNotifyParam{msgSrc=" + this.msgSrc + ", msgType=" + this.msgType + ", param1=" + this.param1 + ", param2=" + this.param2 + ", param3=" + this.param3 + ", param4='" + this.param4 + "'}";
    }
}
