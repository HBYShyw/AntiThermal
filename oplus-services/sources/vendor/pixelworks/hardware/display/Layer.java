package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class Layer implements Parcelable {
    public static final Parcelable.Creator<Layer> CREATOR = new Parcelable.Creator<Layer>() { // from class: vendor.pixelworks.hardware.display.Layer.1
        @Override // android.os.Parcelable.Creator
        public Layer createFromParcel(Parcel parcel) {
            Layer layer = new Layer();
            layer.readFromParcel(parcel);
            return layer;
        }

        @Override // android.os.Parcelable.Creator
        public Layer[] newArray(int i) {
            return new Layer[i];
        }
    };
    public LayerRect dstRect;
    public LayerBuffer inputBuffer;
    public int[] reserved;
    public LayerTransform transform;
    public int composition = 0;
    public int planeAlpha = 0;
    public int layerFlags = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.inputBuffer, i);
        parcel.writeInt(this.composition);
        parcel.writeTypedObject(this.dstRect, i);
        parcel.writeTypedObject(this.transform, i);
        parcel.writeInt(this.planeAlpha);
        parcel.writeInt(this.layerFlags);
        parcel.writeIntArray(this.reserved);
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
                this.inputBuffer = (LayerBuffer) parcel.readTypedObject(LayerBuffer.CREATOR);
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.composition = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.dstRect = (LayerRect) parcel.readTypedObject(LayerRect.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.transform = (LayerTransform) parcel.readTypedObject(LayerTransform.CREATOR);
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.planeAlpha = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.layerFlags = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.reserved = parcel.createIntArray();
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
        return describeContents(this.transform) | describeContents(this.inputBuffer) | 0 | describeContents(this.dstRect);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
