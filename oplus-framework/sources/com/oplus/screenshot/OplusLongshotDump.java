package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemProperties;

/* loaded from: classes.dex */
public class OplusLongshotDump implements Parcelable {
    public static final String JSON_POSTFIX_LIST = "_list";
    public static final String JSON_POSTFIX_RECT = "_rect";
    public static final String JSON_PREFIX_CHILD = "child_";
    public static final String JSON_PREFIX_PARENT = "parent_";
    public static final String JSON_PREFIX_SCROLL = "scroll_";
    public static final String JSON_PREFIX_WINDOW = "window_";
    public static final String TAG = "LongshotDump";
    public static final boolean DBG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final Parcelable.Creator<OplusLongshotDump> CREATOR = new Parcelable.Creator<OplusLongshotDump>() { // from class: com.oplus.screenshot.OplusLongshotDump.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusLongshotDump createFromParcel(Parcel in) {
            return new OplusLongshotDump(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusLongshotDump[] newArray(int size) {
            return new OplusLongshotDump[size];
        }
    };
    private Rect mScrollRect = new Rect();
    private OplusLongshotComponentName mComponent = null;
    private long mSpendDump = 0;
    private long mSpendCalc = 0;
    private long mSpendPack = 0;
    private int mDumpCount = 0;
    private int mScrollCount = 0;

    public OplusLongshotDump() {
    }

    public OplusLongshotDump(Parcel in) {
        readFromParcel(in);
    }

    public String toString() {
        return "[rect=" + this.mScrollRect + ":" + this.mComponent + ":dump=" + this.mSpendDump + ":calc=" + this.mSpendCalc + ":pack=" + this.mSpendPack + ":dumpCount=" + this.mDumpCount + ":scrollCount=" + this.mScrollCount + "]";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        this.mScrollRect.writeToParcel(out, flags);
        writeComponent(out, flags);
        out.writeLong(this.mSpendDump);
        out.writeLong(this.mSpendCalc);
        out.writeLong(this.mSpendPack);
        out.writeInt(this.mDumpCount);
    }

    public void readFromParcel(Parcel in) {
        this.mScrollRect.readFromParcel(in);
        readComponent(in);
        this.mSpendDump = in.readLong();
        this.mSpendCalc = in.readLong();
        this.mSpendPack = in.readLong();
        this.mDumpCount = in.readInt();
    }

    public void setScrollRect(Rect rect) {
        this.mScrollRect.set(rect);
    }

    public void setScrollComponent(OplusLongshotComponentName component) {
        this.mComponent = component;
    }

    public void setSpendDump(long spendDump) {
        this.mSpendDump = spendDump;
    }

    public void setSpendCalc(long spendCalc) {
        this.mSpendCalc = spendCalc;
    }

    public void setSpendPack(long spendPack) {
        this.mSpendPack = spendPack;
    }

    public void setDumpCount(int dumpCount) {
        this.mDumpCount = dumpCount;
    }

    public void setScrollCount(int scrollCount) {
        this.mScrollCount = scrollCount;
    }

    public long getTotalSpend() {
        return this.mSpendDump + this.mSpendCalc + this.mSpendPack;
    }

    public int getDumpCount() {
        return this.mDumpCount;
    }

    private void writeComponent(Parcel out, int flags) {
        if (this.mComponent != null) {
            out.writeInt(1);
            this.mComponent.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
    }

    private void readComponent(Parcel in) {
        if (1 == in.readInt()) {
            this.mComponent.readFromParcel(in);
        }
    }
}
