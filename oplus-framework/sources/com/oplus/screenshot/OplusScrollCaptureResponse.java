package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.screenshot.IOplusScrollCaptureConnection;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OplusScrollCaptureResponse implements Parcelable {
    public static final Parcelable.Creator<OplusScrollCaptureResponse> CREATOR = new Parcelable.Creator<OplusScrollCaptureResponse>() { // from class: com.oplus.screenshot.OplusScrollCaptureResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusScrollCaptureResponse createFromParcel(Parcel in) {
            return new OplusScrollCaptureResponse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusScrollCaptureResponse[] newArray(int size) {
            return new OplusScrollCaptureResponse[size];
        }
    };
    private IOplusScrollCaptureConnection mConnection;
    private final OplusScrollCaptureResponseInner mResponse;

    public OplusScrollCaptureResponse(OplusScrollCaptureResponseInner response) {
        this.mResponse = response;
    }

    protected OplusScrollCaptureResponse(Parcel in) {
        this.mResponse = (OplusScrollCaptureResponseInner) in.readParcelable(OplusScrollCaptureResponseInner.class.getClassLoader());
    }

    public boolean isConnected() {
        return this.mResponse.isConnected();
    }

    public void close() {
        this.mResponse.close();
    }

    public String getDescription() {
        return this.mResponse.getDescription();
    }

    public IOplusScrollCaptureConnection getConnection() {
        IOplusScrollCaptureConnectionInner con;
        if (this.mConnection == null && (con = this.mResponse.getConnection()) != null) {
            this.mConnection = IOplusScrollCaptureConnection.Stub.asInterface(con.asBinder());
        }
        return this.mConnection;
    }

    public Rect getWindowBounds() {
        return this.mResponse.getWindowBounds();
    }

    public Rect getBoundsInWindow() {
        return this.mResponse.getBoundsInWindow();
    }

    public String getWindowTitle() {
        return this.mResponse.getWindowTitle();
    }

    public String getPackageName() {
        return this.mResponse.getPackageName();
    }

    public ArrayList<String> getMessages() {
        return this.mResponse.getMessages();
    }

    public String toString() {
        return "OplusScrollCaptureResponse { description = " + getDescription() + ", connection = " + getConnection() + ", windowBounds = " + getWindowBounds() + ", boundsInWindow = " + getBoundsInWindow() + ", windowTitle = " + getWindowTitle() + ", packageName = " + getPackageName() + ", messages = " + getMessages() + " }";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mResponse, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
