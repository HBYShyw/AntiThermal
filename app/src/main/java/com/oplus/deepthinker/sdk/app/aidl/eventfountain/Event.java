package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

/* loaded from: classes.dex */
public class Event implements Parcelable {
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() { // from class: com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Event createFromParcel(Parcel parcel) {
            return new Event(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Event[] newArray(int i10) {
            return new Event[i10];
        }
    };
    private static final String TAG = "Event";
    private int mEventType;
    private Bundle mExtra;

    public Event(int i10, Bundle bundle) {
        this.mEventType = i10;
        this.mExtra = bundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return (obj instanceof Event) && this.mEventType == ((Event) obj).getEventType();
    }

    public int getEventType() {
        return this.mEventType;
    }

    public Bundle getExtra() {
        return this.mExtra;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mEventType)});
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Event{mEventType=");
        sb2.append(this.mEventType);
        sb2.append(", mExtra=");
        Object obj = this.mExtra;
        if (obj == null) {
            obj = "null";
        }
        sb2.append(obj);
        sb2.append('}');
        return sb2.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.mEventType);
        parcel.writeBundle(this.mExtra);
    }

    public Event(Parcel parcel) {
        this.mEventType = parcel.readInt();
        this.mExtra = parcel.readBundle(getClass().getClassLoader());
    }
}
