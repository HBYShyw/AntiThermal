package vendor.oplus.hardware.osense.client;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OsenseAidlHalNotifyRequest implements Parcelable {
    public static final Parcelable.Creator<OsenseAidlHalNotifyRequest> CREATOR = new Parcelable.Creator<OsenseAidlHalNotifyRequest>() { // from class: vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest.1
        @Override // android.os.Parcelable.Creator
        public OsenseAidlHalNotifyRequest createFromParcel(Parcel parcel) {
            OsenseAidlHalNotifyRequest osenseAidlHalNotifyRequest = new OsenseAidlHalNotifyRequest();
            osenseAidlHalNotifyRequest.readFromParcel(parcel);
            return osenseAidlHalNotifyRequest;
        }

        @Override // android.os.Parcelable.Creator
        public OsenseAidlHalNotifyRequest[] newArray(int i) {
            return new OsenseAidlHalNotifyRequest[i];
        }
    };
    public int msg_src = 0;
    public int msg_type = 0;
    public int param1 = 0;
    public int param2 = 0;
    public int param3 = 0;
    public String param4;

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
        parcel.writeInt(this.msg_src);
        parcel.writeInt(this.msg_type);
        parcel.writeInt(this.param1);
        parcel.writeInt(this.param2);
        parcel.writeInt(this.param3);
        parcel.writeString(this.param4);
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
                this.msg_src = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.msg_type = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.param1 = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.param2 = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.param3 = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.param4 = parcel.readString();
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
