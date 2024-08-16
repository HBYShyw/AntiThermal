package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LutData implements Parcelable {
    public static final Parcelable.Creator<LutData> CREATOR = new Parcelable.Creator<LutData>() { // from class: vendor.pixelworks.hardware.display.LutData.1
        @Override // android.os.Parcelable.Creator
        public LutData createFromParcel(Parcel parcel) {
            LutData lutData = new LutData();
            lutData.readFromParcel(parcel);
            return lutData;
        }

        @Override // android.os.Parcelable.Creator
        public LutData[] newArray(int i) {
            return new LutData[i];
        }
    };
    public int[] gridEntries;
    public int[] lutEntries;
    public int dim = 0;
    public int gridSize = 0;
    public boolean validLutEntries = false;
    public boolean validGridEntries = false;

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
        parcel.writeInt(this.dim);
        parcel.writeInt(this.gridSize);
        parcel.writeIntArray(this.lutEntries);
        parcel.writeBoolean(this.validLutEntries);
        parcel.writeIntArray(this.gridEntries);
        parcel.writeBoolean(this.validGridEntries);
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
                this.dim = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.gridSize = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.lutEntries = parcel.createIntArray();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.validLutEntries = parcel.readBoolean();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.gridEntries = parcel.createIntArray();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.validGridEntries = parcel.readBoolean();
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
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
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
}
