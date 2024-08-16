package vendor.oplus.hardware.osense.client;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OsenseCpuControlData implements Parcelable {
    public static final Parcelable.Creator<OsenseCpuControlData> CREATOR = new Parcelable.Creator<OsenseCpuControlData>() { // from class: vendor.oplus.hardware.osense.client.OsenseCpuControlData.1
        @Override // android.os.Parcelable.Creator
        public OsenseCpuControlData createFromParcel(Parcel parcel) {
            OsenseCpuControlData osenseCpuControlData = new OsenseCpuControlData();
            osenseCpuControlData.readFromParcel(parcel);
            return osenseCpuControlData;
        }

        @Override // android.os.Parcelable.Creator
        public OsenseCpuControlData[] newArray(int i) {
            return new OsenseCpuControlData[i];
        }
    };
    public int control_type = 0;
    public OsenseDataRange core;
    public OsenseDataRange freq;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.core, i);
        parcel.writeTypedObject(this.freq, i);
        parcel.writeInt(this.control_type);
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
                Parcelable.Creator<OsenseDataRange> creator = OsenseDataRange.CREATOR;
                this.core = (OsenseDataRange) parcel.readTypedObject(creator);
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.freq = (OsenseDataRange) parcel.readTypedObject(creator);
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.control_type = parcel.readInt();
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.freq) | describeContents(this.core) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
