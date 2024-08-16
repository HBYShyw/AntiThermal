package com.oplus.wallpaper;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class OplusWallpaperInfo implements Parcelable {
    public static final Parcelable.Creator<OplusWallpaperInfo> CREATOR = new Parcelable.Creator<OplusWallpaperInfo>() { // from class: com.oplus.wallpaper.OplusWallpaperInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusWallpaperInfo createFromParcel(Parcel source) {
            return new OplusWallpaperInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusWallpaperInfo[] newArray(int size) {
            return new OplusWallpaperInfo[size];
        }
    };
    public boolean homeTarget;
    public SurfaceControl wallpaperSueface;

    public OplusWallpaperInfo() {
    }

    public OplusWallpaperInfo(Parcel in) {
        this.homeTarget = in.readByte() != 0;
        int wallpaperFlag = in.readInt();
        if (wallpaperFlag > 0) {
            this.wallpaperSueface = (SurfaceControl) in.readTypedObject(SurfaceControl.CREATOR);
        }
    }

    public OplusWallpaperInfo(OplusWallpaperInfo in) {
        if (in != null) {
            this.homeTarget = in.homeTarget;
            this.wallpaperSueface = in.wallpaperSueface;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.homeTarget ? (byte) 1 : (byte) 0);
        if (this.wallpaperSueface != null) {
            parcel.writeInt(1);
            parcel.writeTypedObject(this.wallpaperSueface, 0);
        } else {
            parcel.writeInt(0);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusWallpaperInfo = { ");
        sb.append(" homeTarget = " + this.homeTarget);
        sb.append("}");
        return sb.toString();
    }
}
