package vendor.oplus.hardware.osense.client;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OsenseDataRange implements Parcelable {
    public static final Parcelable.Creator<OsenseDataRange> CREATOR = new Parcelable.Creator<OsenseDataRange>() { // from class: vendor.oplus.hardware.osense.client.OsenseDataRange.1
        @Override // android.os.Parcelable.Creator
        public OsenseDataRange createFromParcel(Parcel parcel) {
            OsenseDataRange osenseDataRange = new OsenseDataRange();
            osenseDataRange.readFromParcel(parcel);
            return osenseDataRange;
        }

        @Override // android.os.Parcelable.Creator
        public OsenseDataRange[] newArray(int i) {
            return new OsenseDataRange[i];
        }
    };
    public int max = 0;
    public int min = 0;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.max);
        parcel.writeInt(this.min);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        try {
            if (readInt < 4) {
                throw new BadParcelableException("Parcelable too small");
            }
            if (parcel.dataPosition() - dataPosition < readInt) {
                this.max = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.min = parcel.readInt();
                    if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                    parcel.setDataPosition(dataPosition + readInt);
                    return;
                }
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            parcel.setDataPosition(dataPosition + readInt);
        } catch (Throwable th) {
            if (dataPosition > Integer.MAX_VALUE - readInt) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            parcel.setDataPosition(dataPosition + readInt);
            throw th;
        }
    }
}
