package android.content;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusActionFilter implements Parcelable {
    public static final Parcelable.Creator<OplusActionFilter> CREATOR = new Parcelable.Creator<OplusActionFilter>() { // from class: android.content.OplusActionFilter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusActionFilter createFromParcel(Parcel source) {
            return new OplusActionFilter(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusActionFilter[] newArray(int size) {
            return new OplusActionFilter[size];
        }
    };
    private String mFilterName;
    private String mFilterValue;

    /* JADX INFO: Access modifiers changed from: protected */
    public OplusActionFilter(String filterName, String filterValue) {
        this.mFilterName = filterName;
        this.mFilterValue = filterValue;
    }

    protected OplusActionFilter(Parcel in) {
        this.mFilterName = in.readString();
        this.mFilterValue = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFilterName);
        dest.writeString(this.mFilterValue);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getFilterName() {
        return this.mFilterName;
    }

    public String getFilterValue() {
        return this.mFilterValue;
    }

    public String toString() {
        return "OplusActionFilter{filterName=" + this.mFilterName + ", filterValue=" + this.mFilterValue + "}";
    }
}
