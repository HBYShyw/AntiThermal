package vendor.oplus.hardware.osense.client;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OsenseAidlHalSaRequest implements Parcelable {
    public static final Parcelable.Creator<OsenseAidlHalSaRequest> CREATOR = new Parcelable.Creator<OsenseAidlHalSaRequest>() { // from class: vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest.1
        @Override // android.os.Parcelable.Creator
        public OsenseAidlHalSaRequest createFromParcel(Parcel parcel) {
            OsenseAidlHalSaRequest osenseAidlHalSaRequest = new OsenseAidlHalSaRequest();
            osenseAidlHalSaRequest.readFromParcel(parcel);
            return osenseAidlHalSaRequest;
        }

        @Override // android.os.Parcelable.Creator
        public OsenseAidlHalSaRequest[] newArray(int i) {
            return new OsenseAidlHalSaRequest[i];
        }
    };
    public String action;
    public String scene;
    public int timeout = 0;
    public long request = 0;

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
        parcel.writeString(this.scene);
        parcel.writeString(this.action);
        parcel.writeInt(this.timeout);
        parcel.writeLong(this.request);
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
                this.scene = parcel.readString();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.action = parcel.readString();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.timeout = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.request = parcel.readLong();
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
