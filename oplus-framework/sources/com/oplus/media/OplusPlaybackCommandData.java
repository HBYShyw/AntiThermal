package com.oplus.media;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusPlaybackCommandData extends OplusMediaEventData {
    public static final int COMMAND_FAVORITE_LIKE = 6;
    public static final int COMMAND_FAVORITE_UNLIKE = 7;
    public static final int COMMAND_NEXT = 5;
    public static final int COMMAND_PAUSE = 2;
    public static final int COMMAND_PLAY = 1;
    public static final int COMMAND_PREVIOUS = 4;
    public static final int COMMAND_STOP = 3;
    public static final Parcelable.Creator<OplusPlaybackCommandData> CREATOR = new Parcelable.Creator<OplusPlaybackCommandData>() { // from class: com.oplus.media.OplusPlaybackCommandData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPlaybackCommandData createFromParcel(Parcel in) {
            return new OplusPlaybackCommandData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPlaybackCommandData[] newArray(int size) {
            return new OplusPlaybackCommandData[size];
        }
    };
    private int mCommand;

    public OplusPlaybackCommandData() {
    }

    public OplusPlaybackCommandData(int command) {
        this.mCommand = command;
    }

    public OplusPlaybackCommandData(Parcel in) {
        this.mCommand = in.readInt();
    }

    public int getCommand() {
        return this.mCommand;
    }

    public void setCommand(int command) {
        this.mCommand = command;
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.mCommand);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
