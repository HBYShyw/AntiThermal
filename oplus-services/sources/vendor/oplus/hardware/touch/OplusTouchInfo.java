package vendor.oplus.hardware.touch;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OplusTouchInfo implements Parcelable {
    public static final Parcelable.Creator<OplusTouchInfo> CREATOR = new Parcelable.Creator<OplusTouchInfo>() { // from class: vendor.oplus.hardware.touch.OplusTouchInfo.1
        @Override // android.os.Parcelable.Creator
        public OplusTouchInfo createFromParcel(Parcel parcel) {
            OplusTouchInfo oplusTouchInfo = new OplusTouchInfo();
            oplusTouchInfo.readFromParcel(parcel);
            return oplusTouchInfo;
        }

        @Override // android.os.Parcelable.Creator
        public OplusTouchInfo[] newArray(int i) {
            return new OplusTouchInfo[i];
        }
    };
    public String info;
    public long time = 0;
    public int deviceId = 0;
    public int nodeFlag = 0;
    public int data = 0;

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
        parcel.writeLong(this.time);
        parcel.writeInt(this.deviceId);
        parcel.writeInt(this.nodeFlag);
        parcel.writeInt(this.data);
        parcel.writeString(this.info);
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
                this.time = parcel.readLong();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.deviceId = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.nodeFlag = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.data = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.info = parcel.readString();
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
