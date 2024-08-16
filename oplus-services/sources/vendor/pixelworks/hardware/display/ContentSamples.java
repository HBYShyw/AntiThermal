package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ContentSamples implements Parcelable {
    public static final Parcelable.Creator<ContentSamples> CREATOR = new Parcelable.Creator<ContentSamples>() { // from class: vendor.pixelworks.hardware.display.ContentSamples.1
        @Override // android.os.Parcelable.Creator
        public ContentSamples createFromParcel(Parcel parcel) {
            ContentSamples contentSamples = new ContentSamples();
            contentSamples.readFromParcel(parcel);
            return contentSamples;
        }

        @Override // android.os.Parcelable.Creator
        public ContentSamples[] newArray(int i) {
            return new ContentSamples[i];
        }
    };
    public int result = 0;
    public long[] samples0;
    public long[] samples1;
    public long[] samples2;
    public long[] samples3;

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
        parcel.writeInt(this.result);
        parcel.writeLongArray(this.samples0);
        parcel.writeLongArray(this.samples1);
        parcel.writeLongArray(this.samples2);
        parcel.writeLongArray(this.samples3);
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
                this.result = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.samples0 = parcel.createLongArray();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.samples1 = parcel.createLongArray();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.samples2 = parcel.createLongArray();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.samples3 = parcel.createLongArray();
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
