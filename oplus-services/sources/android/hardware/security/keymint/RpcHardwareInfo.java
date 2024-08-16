package android.hardware.security.keymint;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RpcHardwareInfo implements Parcelable {
    public static final Parcelable.Creator<RpcHardwareInfo> CREATOR = new Parcelable.Creator<RpcHardwareInfo>() { // from class: android.hardware.security.keymint.RpcHardwareInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RpcHardwareInfo createFromParcel(Parcel parcel) {
            RpcHardwareInfo rpcHardwareInfo = new RpcHardwareInfo();
            rpcHardwareInfo.readFromParcel(parcel);
            return rpcHardwareInfo;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RpcHardwareInfo[] newArray(int i) {
            return new RpcHardwareInfo[i];
        }
    };
    public static final int CURVE_25519 = 2;
    public static final int CURVE_NONE = 0;
    public static final int CURVE_P256 = 1;
    public static final int MIN_SUPPORTED_NUM_KEYS_IN_CSR = 20;
    public String rpcAuthorName;
    public String uniqueId;
    public int versionNumber = 0;
    public int supportedEekCurve = 0;
    public int supportedNumKeysInCsr = 4;

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
        parcel.writeInt(this.versionNumber);
        parcel.writeString(this.rpcAuthorName);
        parcel.writeInt(this.supportedEekCurve);
        parcel.writeString(this.uniqueId);
        parcel.writeInt(this.supportedNumKeysInCsr);
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
                this.versionNumber = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.rpcAuthorName = parcel.readString();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.supportedEekCurve = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.uniqueId = parcel.readString();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.supportedNumKeysInCsr = parcel.readInt();
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
