package vendor.oplus.hardware.osense.client;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OsenseControlInfo implements Parcelable {
    public static final Parcelable.Creator<OsenseControlInfo> CREATOR = new Parcelable.Creator<OsenseControlInfo>() { // from class: vendor.oplus.hardware.osense.client.OsenseControlInfo.1
        @Override // android.os.Parcelable.Creator
        public OsenseControlInfo createFromParcel(Parcel parcel) {
            OsenseControlInfo osenseControlInfo = new OsenseControlInfo();
            osenseControlInfo.readFromParcel(parcel);
            return osenseControlInfo;
        }

        @Override // android.os.Parcelable.Creator
        public OsenseControlInfo[] newArray(int i) {
            return new OsenseControlInfo[i];
        }
    };
    public OsenseCpuControlData[] cpuParam1;
    public OsenseCpuMIGData[] cpu_mig_data;
    public OsenseGpuControlData[] param1;
    public int cpu_cluster_num = 0;
    public int gpu_cluster_num = 0;
    public int control_mask = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.cpu_cluster_num);
        parcel.writeInt(this.gpu_cluster_num);
        parcel.writeTypedArray(this.cpuParam1, i);
        parcel.writeTypedArray(this.param1, i);
        parcel.writeTypedArray(this.cpu_mig_data, i);
        parcel.writeInt(this.control_mask);
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
                this.cpu_cluster_num = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.gpu_cluster_num = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.cpuParam1 = (OsenseCpuControlData[]) parcel.createTypedArray(OsenseCpuControlData.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.param1 = (OsenseGpuControlData[]) parcel.createTypedArray(OsenseGpuControlData.CREATOR);
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.cpu_mig_data = (OsenseCpuMIGData[]) parcel.createTypedArray(OsenseCpuMIGData.CREATOR);
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.control_mask = parcel.readInt();
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.cpu_mig_data) | describeContents(this.cpuParam1) | 0 | describeContents(this.param1);
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int i = 0;
            for (Object obj2 : (Object[]) obj) {
                i |= describeContents(obj2);
            }
            return i;
        }
        if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
