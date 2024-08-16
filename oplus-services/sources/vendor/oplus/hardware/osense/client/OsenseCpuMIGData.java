package vendor.oplus.hardware.osense.client;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OsenseCpuMIGData implements Parcelable {
    public static final Parcelable.Creator<OsenseCpuMIGData> CREATOR = new Parcelable.Creator<OsenseCpuMIGData>() { // from class: vendor.oplus.hardware.osense.client.OsenseCpuMIGData.1
        @Override // android.os.Parcelable.Creator
        public OsenseCpuMIGData createFromParcel(Parcel parcel) {
            OsenseCpuMIGData osenseCpuMIGData = new OsenseCpuMIGData();
            osenseCpuMIGData.readFromParcel(parcel);
            return osenseCpuMIGData;
        }

        @Override // android.os.Parcelable.Creator
        public OsenseCpuMIGData[] newArray(int i) {
            return new OsenseCpuMIGData[i];
        }
    };
    public int migUp = 0;
    public int migDown = 0;

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
        parcel.writeInt(this.migUp);
        parcel.writeInt(this.migDown);
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
                this.migUp = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.migDown = parcel.readInt();
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
