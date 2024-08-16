package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LayerBuffer implements Parcelable {
    public static final Parcelable.Creator<LayerBuffer> CREATOR = new Parcelable.Creator<LayerBuffer>() { // from class: vendor.pixelworks.hardware.display.LayerBuffer.1
        @Override // android.os.Parcelable.Creator
        public LayerBuffer createFromParcel(Parcel parcel) {
            LayerBuffer layerBuffer = new LayerBuffer();
            layerBuffer.readFromParcel(parcel);
            return layerBuffer;
        }

        @Override // android.os.Parcelable.Creator
        public LayerBuffer[] newArray(int i) {
            return new LayerBuffer[i];
        }
    };
    public BufferInfo bufferInfo;
    public ColorMetaData colorMetadata;
    public int width = 0;
    public int height = 0;
    public int unalignedWidth = 0;
    public int unalignedHeight = 0;
    public int acquireFenceFd = 0;
    public int releaseFenceFd = 0;
    public int flags = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeInt(this.unalignedWidth);
        parcel.writeInt(this.unalignedHeight);
        parcel.writeTypedObject(this.colorMetadata, i);
        parcel.writeInt(this.acquireFenceFd);
        parcel.writeInt(this.releaseFenceFd);
        parcel.writeInt(this.flags);
        parcel.writeTypedObject(this.bufferInfo, i);
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
                this.width = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.height = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.unalignedWidth = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.unalignedHeight = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.colorMetadata = (ColorMetaData) parcel.readTypedObject(ColorMetaData.CREATOR);
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.acquireFenceFd = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.releaseFenceFd = parcel.readInt();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.flags = parcel.readInt();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.bufferInfo = (BufferInfo) parcel.readTypedObject(BufferInfo.CREATOR);
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
        return describeContents(this.bufferInfo) | describeContents(this.colorMetadata) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
