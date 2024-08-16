package com.oplus.orms.info;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OrmsSaParam implements Parcelable {
    public static final Parcelable.Creator<OrmsSaParam> CREATOR = new Parcelable.Creator<OrmsSaParam>() { // from class: com.oplus.orms.info.OrmsSaParam.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OrmsSaParam createFromParcel(Parcel in) {
            return new OrmsSaParam(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OrmsSaParam[] newArray(int size) {
            return new OrmsSaParam[size];
        }
    };
    public String action;
    public String scene;
    public int timeout;

    public OrmsSaParam() {
        this.scene = "";
        this.action = "";
        this.timeout = -1;
    }

    public OrmsSaParam(String scene, String action, int timeout) {
        this.scene = "";
        this.action = "";
        this.timeout = -1;
        this.scene = scene;
        this.action = action;
        this.timeout = timeout;
    }

    protected OrmsSaParam(Parcel in) {
        this.scene = "";
        this.action = "";
        this.timeout = -1;
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.scene);
        dest.writeString(this.action);
        dest.writeInt(this.timeout);
    }

    public void readFromParcel(Parcel in) {
        this.scene = in.readString();
        this.action = in.readString();
        this.timeout = in.readInt();
    }

    public String toString() {
        return "OrmsSaParam{scene='" + this.scene + "', action='" + this.action + "', timeout=" + this.timeout + '}';
    }
}
