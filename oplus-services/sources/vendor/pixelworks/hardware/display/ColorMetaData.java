package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ColorMetaData implements Parcelable {
    public static final Parcelable.Creator<ColorMetaData> CREATOR = new Parcelable.Creator<ColorMetaData>() { // from class: vendor.pixelworks.hardware.display.ColorMetaData.1
        @Override // android.os.Parcelable.Creator
        public ColorMetaData createFromParcel(Parcel parcel) {
            ColorMetaData colorMetaData = new ColorMetaData();
            colorMetaData.readFromParcel(parcel);
            return colorMetaData;
        }

        @Override // android.os.Parcelable.Creator
        public ColorMetaData[] newArray(int i) {
            return new ColorMetaData[i];
        }
    };
    public byte[] dynamicMetaDataPayload;
    public int colorPrimaries = 0;
    public int range = 0;
    public int transfer = 0;
    public boolean lightLevelSEIEnabled = false;
    public int maxContentLightLevel = 0;
    public int minPicAverageLightLevel = 0;
    public boolean dynamicMetaDataValid = false;
    public int dynamicMetaDataLen = 0;

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
        parcel.writeInt(this.colorPrimaries);
        parcel.writeInt(this.range);
        parcel.writeInt(this.transfer);
        parcel.writeBoolean(this.lightLevelSEIEnabled);
        parcel.writeInt(this.maxContentLightLevel);
        parcel.writeInt(this.minPicAverageLightLevel);
        parcel.writeBoolean(this.dynamicMetaDataValid);
        parcel.writeInt(this.dynamicMetaDataLen);
        parcel.writeByteArray(this.dynamicMetaDataPayload);
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
                this.colorPrimaries = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.range = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.transfer = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.lightLevelSEIEnabled = parcel.readBoolean();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.maxContentLightLevel = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.minPicAverageLightLevel = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.dynamicMetaDataValid = parcel.readBoolean();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.dynamicMetaDataLen = parcel.readInt();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.dynamicMetaDataPayload = parcel.createByteArray();
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
}
