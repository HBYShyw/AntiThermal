package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LayerTransform implements Parcelable {
    public static final Parcelable.Creator<LayerTransform> CREATOR = new Parcelable.Creator<LayerTransform>() { // from class: vendor.pixelworks.hardware.display.LayerTransform.1
        @Override // android.os.Parcelable.Creator
        public LayerTransform createFromParcel(Parcel parcel) {
            LayerTransform layerTransform = new LayerTransform();
            layerTransform.readFromParcel(parcel);
            return layerTransform;
        }

        @Override // android.os.Parcelable.Creator
        public LayerTransform[] newArray(int i) {
            return new LayerTransform[i];
        }
    };
    public float rotation = 0.0f;
    public boolean flipHorizontal = false;
    public boolean flipVertical = false;

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
        parcel.writeFloat(this.rotation);
        parcel.writeBoolean(this.flipHorizontal);
        parcel.writeBoolean(this.flipVertical);
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
                this.rotation = parcel.readFloat();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.flipHorizontal = parcel.readBoolean();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.flipVertical = parcel.readBoolean();
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
}
