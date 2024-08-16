package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class BufferInfo implements Parcelable {
    public static final Parcelable.Creator<BufferInfo> CREATOR = new Parcelable.Creator<BufferInfo>() { // from class: vendor.pixelworks.hardware.display.BufferInfo.1
        @Override // android.os.Parcelable.Creator
        public BufferInfo createFromParcel(Parcel parcel) {
            BufferInfo bufferInfo = new BufferInfo();
            bufferInfo.readFromParcel(parcel);
            return bufferInfo;
        }

        @Override // android.os.Parcelable.Creator
        public BufferInfo[] newArray(int i) {
            return new BufferInfo[i];
        }
    };
    public int[] reserved;
    public long id = 0;
    public int format = 0;
    public int type = 0;
    public int flags = 0;
    public int width = 0;
    public int height = 0;
    public int unalignedWidth = 0;
    public int unalignedHeight = 0;
    public float refreshRate = 0.0f;
    public int frcEnable = 0;
    public int frcCounter = 0;
    public long frcTimestamp = 0;

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
        parcel.writeLong(this.id);
        parcel.writeInt(this.format);
        parcel.writeInt(this.type);
        parcel.writeInt(this.flags);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeInt(this.unalignedWidth);
        parcel.writeInt(this.unalignedHeight);
        parcel.writeFloat(this.refreshRate);
        parcel.writeInt(this.frcEnable);
        parcel.writeInt(this.frcCounter);
        parcel.writeLong(this.frcTimestamp);
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
                this.id = parcel.readLong();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.format = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.type = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.flags = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.width = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.height = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.unalignedWidth = parcel.readInt();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.unalignedHeight = parcel.readInt();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.refreshRate = parcel.readFloat();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.frcEnable = parcel.readInt();
                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                        this.frcCounter = parcel.readInt();
                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                            this.frcTimestamp = parcel.readLong();
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
