package com.oplus.direct;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusDirectFindResult implements Parcelable {
    public static final Parcelable.Creator<OplusDirectFindResult> CREATOR = new Parcelable.Creator<OplusDirectFindResult>() { // from class: com.oplus.direct.OplusDirectFindResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusDirectFindResult createFromParcel(Parcel in) {
            return new OplusDirectFindResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusDirectFindResult[] newArray(int size) {
            return new OplusDirectFindResult[size];
        }
    };
    public static final String ERROR_NO_MAINWIN = "no_mainwin";
    public static final String ERROR_NO_TEXT = "no_text";
    public static final String ERROR_NO_VIEW = "no_view";
    public static final String ERROR_NO_VIEWROOT = "no_viewroot";
    public static final String ERROR_UNKNOWN_CMD = "unknown_cmd";
    public static final String EXTRA_ERROR = "direct_find_error";
    public static final String EXTRA_NO_IDNAMES = "no_idnames";
    public static final String EXTRA_RESULT_TEXT = "result_text";
    private static final String TAG = "OplusDirectFindResult";
    private final Bundle mBundle = new Bundle();

    public OplusDirectFindResult() {
    }

    public OplusDirectFindResult(Parcel in) {
        readFromParcel(in);
    }

    public String toString() {
        return "Result=" + this.mBundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        this.mBundle.writeToParcel(out, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mBundle.readFromParcel(in);
    }

    public Bundle getBundle() {
        return this.mBundle;
    }
}
