package com.oplus.view.analysis;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.OplusLongshotViewContent;
import android.view.OplusLongshotViewUtils;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusWindowNode implements Parcelable {
    public static final Parcelable.Creator<OplusWindowNode> CREATOR = new Parcelable.Creator<OplusWindowNode>() { // from class: com.oplus.view.analysis.OplusWindowNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusWindowNode createFromParcel(Parcel in) {
            return new OplusWindowNode(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusWindowNode[] newArray(int size) {
            return new OplusWindowNode[size];
        }
    };
    private String mClassName;
    private final Rect mCoverRect;
    private final Rect mDecorRect;
    private boolean mIsNavigationBar;
    private boolean mIsStatusBar;
    private String mPackageName;
    private int mSurfaceLayer;
    private final Rect mTempRect;
    private long mTimeSpend;

    public OplusWindowNode(View view, boolean isStatusBar, boolean isNavigationBar) {
        this.mTempRect = new Rect();
        this.mCoverRect = new Rect();
        Rect rect = new Rect();
        this.mDecorRect = rect;
        this.mPackageName = null;
        this.mClassName = null;
        this.mTimeSpend = 0L;
        this.mSurfaceLayer = 0;
        this.mIsStatusBar = false;
        this.mIsNavigationBar = false;
        long timeStart = SystemClock.uptimeMillis();
        view.getBoundsOnScreen(rect, true);
        if (view instanceof ViewGroup) {
            List<OplusLongshotViewContent> coverContents = new ArrayList<>();
            OplusLongshotViewUtils utils = new OplusLongshotViewUtils(view.getContext());
            utils.findCoverRect(1, (ViewGroup) view, null, coverContents, null, null, null, true);
            for (OplusLongshotViewContent coverContent : coverContents) {
                View coverView = coverContent.getView();
                coverView.getBoundsOnScreen(this.mTempRect, true);
                this.mCoverRect.union(this.mTempRect);
            }
        }
        if (this.mCoverRect.isEmpty()) {
            this.mCoverRect.set(this.mDecorRect);
        }
        this.mPackageName = view.getContext().getPackageName();
        this.mClassName = view.getClass().getName();
        this.mTimeSpend = SystemClock.uptimeMillis() - timeStart;
        this.mIsStatusBar = isStatusBar;
        this.mIsNavigationBar = isNavigationBar;
    }

    public OplusWindowNode(Parcel in) {
        this.mTempRect = new Rect();
        this.mCoverRect = new Rect();
        this.mDecorRect = new Rect();
        this.mPackageName = null;
        this.mClassName = null;
        this.mTimeSpend = 0L;
        this.mSurfaceLayer = 0;
        this.mIsStatusBar = false;
        this.mIsNavigationBar = false;
        readFromParcel(in);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Window[");
        if (this.mIsStatusBar) {
            sb.append("StatusBar][");
        } else if (this.mIsNavigationBar) {
            sb.append("NavigationBar][");
        }
        if (this.mPackageName != null) {
            sb.append("package=");
            sb.append(this.mPackageName.toString());
            sb.append(":");
        }
        if (this.mClassName != null) {
            sb.append("class=");
            sb.append(this.mClassName.toString());
            sb.append(":");
        }
        sb.append("decor=");
        sb.append(this.mDecorRect);
        sb.append("cover=");
        sb.append(this.mCoverRect);
        sb.append(":spend=");
        sb.append(this.mTimeSpend);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        this.mDecorRect.writeToParcel(parcel, i);
        this.mCoverRect.writeToParcel(parcel, i);
        writeString(parcel, this.mPackageName);
        writeString(parcel, this.mClassName);
        parcel.writeLong(this.mTimeSpend);
        parcel.writeInt(this.mSurfaceLayer);
        parcel.writeInt(this.mIsStatusBar ? 1 : 0);
        parcel.writeInt(this.mIsNavigationBar ? 1 : 0);
    }

    public void readFromParcel(Parcel in) {
        this.mDecorRect.readFromParcel(in);
        this.mCoverRect.readFromParcel(in);
        this.mPackageName = readString(in);
        this.mClassName = readString(in);
        this.mTimeSpend = in.readLong();
        this.mSurfaceLayer = in.readInt();
        this.mIsStatusBar = 1 == in.readInt();
        this.mIsNavigationBar = 1 == in.readInt();
    }

    public Rect getDecorRect() {
        return this.mDecorRect;
    }

    public Rect getCoverRect() {
        return this.mCoverRect;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getClassName() {
        return this.mClassName;
    }

    public int getSurfaceLayer() {
        return this.mSurfaceLayer;
    }

    public void setSurfaceLayer(int surfaceLayer) {
        this.mSurfaceLayer = surfaceLayer;
    }

    public boolean isStatusBar() {
        return this.mIsStatusBar;
    }

    public void setStatusBar(boolean value) {
        this.mIsStatusBar = value;
    }

    public boolean isNavigationBar() {
        return this.mIsNavigationBar;
    }

    public void setNavigationBar(boolean value) {
        this.mIsNavigationBar = value;
    }

    private void writeString(Parcel out, String s) {
        if (s != null) {
            out.writeInt(1);
            out.writeString(s);
        } else {
            out.writeInt(0);
        }
    }

    private String readString(Parcel in) {
        if (1 == in.readInt()) {
            return in.readString();
        }
        return null;
    }
}
