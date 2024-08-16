package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayConfigVariableInfo implements Parcelable {
    public static final Parcelable.Creator<DisplayConfigVariableInfo> CREATOR = new Parcelable.Creator<DisplayConfigVariableInfo>() { // from class: vendor.pixelworks.hardware.display.DisplayConfigVariableInfo.1
        @Override // android.os.Parcelable.Creator
        public DisplayConfigVariableInfo createFromParcel(Parcel parcel) {
            DisplayConfigVariableInfo displayConfigVariableInfo = new DisplayConfigVariableInfo();
            displayConfigVariableInfo.readFromParcel(parcel);
            return displayConfigVariableInfo;
        }

        @Override // android.os.Parcelable.Creator
        public DisplayConfigVariableInfo[] newArray(int i) {
            return new DisplayConfigVariableInfo[i];
        }
    };
    public int[] reserved;
    public boolean valid = false;
    public int xPixels = 0;
    public int yPixels = 0;
    public float xDpi = 0.0f;
    public float yDpi = 0.0f;
    public int fps = 0;
    public long vsyncPeriodNs = 0;
    public boolean isYuv = false;
    public boolean smartPanel = false;
    public int config = 0;

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
        parcel.writeBoolean(this.valid);
        parcel.writeInt(this.xPixels);
        parcel.writeInt(this.yPixels);
        parcel.writeFloat(this.xDpi);
        parcel.writeFloat(this.yDpi);
        parcel.writeInt(this.fps);
        parcel.writeLong(this.vsyncPeriodNs);
        parcel.writeBoolean(this.isYuv);
        parcel.writeBoolean(this.smartPanel);
        parcel.writeInt(this.config);
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
                this.valid = parcel.readBoolean();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.xPixels = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.yPixels = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.xDpi = parcel.readFloat();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.yDpi = parcel.readFloat();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.fps = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.vsyncPeriodNs = parcel.readLong();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.isYuv = parcel.readBoolean();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.smartPanel = parcel.readBoolean();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.config = parcel.readInt();
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
