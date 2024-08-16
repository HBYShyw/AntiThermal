package com.aiunit.aon.utils.core;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CommentResult implements Parcelable {
    private static final String CHARSET_UTF8 = "UTF-8";
    public static final Parcelable.Creator<CommentResult> CREATOR = new Parcelable.Creator<CommentResult>() { // from class: com.aiunit.aon.utils.core.CommentResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CommentResult createFromParcel(Parcel parcel) {
            return new CommentResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CommentResult[] newArray(int i) {
            return new CommentResult[i];
        }
    };
    private static final int SHARED_MEMORY_MIN_SIZE = 409600;
    private static final String TAG = "CommentResult";
    private Bitmap mBitmap;
    private MemShare mMemShare;
    private String mResult;

    public CommentResult() {
        this.mResult = "";
        this.mBitmap = null;
    }

    protected CommentResult(Parcel parcel) {
        readFromParcel(parcel);
    }

    public CommentResult(String str) {
        this.mResult = str;
        this.mBitmap = null;
    }

    public CommentResult(String str, Bitmap bitmap) {
        this.mResult = str;
        this.mBitmap = bitmap;
    }

    private void readFromMemory() {
        try {
            byte[] data = this.mMemShare.getData();
            if (data == null) {
                Log.w(TAG, "get data null");
                this.mResult = null;
            } else {
                this.mResult = new String(data, CHARSET_UTF8);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException " + e.getMessage());
        }
    }

    public void readFromParcel(Parcel parcel) {
        this.mResult = parcel.readString();
        this.mBitmap = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
        MemShare memShare = (MemShare) parcel.readParcelable(MemShare.class.getClassLoader());
        this.mMemShare = memShare;
        if (memShare != null) {
            readFromMemory();
        }
    }

    private void writeToMemory(String str) {
        try {
            byte[] bytes = str.getBytes(CHARSET_UTF8);
            MemShare memShare = new MemShare();
            this.mMemShare = memShare;
            memShare.setData(bytes);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException " + e.getMessage());
        }
    }

    public void appendResult(String str, String str2) {
        if (this.mResult != null) {
            try {
                JSONObject jSONObject = new JSONObject(this.mResult);
                jSONObject.put(str, str2);
                this.mResult = jSONObject.toString();
            } catch (JSONException e) {
                Log.e(TAG, "appendResult error " + e.getMessage());
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public String getResult() {
        return this.mResult;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setResult(String str) {
        this.mResult = str;
    }

    public String toString() {
        return this.mResult != null ? "CommentResult{" + this.mResult + '}' : "CommentResult NULL";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        String str = this.mResult;
        if (str == null || str.length() <= SHARED_MEMORY_MIN_SIZE) {
            parcel.writeString(this.mResult);
        } else {
            parcel.writeString(TAG);
            writeToMemory(this.mResult);
        }
        parcel.writeParcelable(this.mBitmap, i);
        parcel.writeParcelable(this.mMemShare, i);
    }
}
