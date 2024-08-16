package vendor.oplus.hardware.charger;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class testKitFeatureTestResult implements Parcelable {
    public static final Parcelable.Creator<testKitFeatureTestResult> CREATOR = new Parcelable.Creator<testKitFeatureTestResult>() { // from class: vendor.oplus.hardware.charger.testKitFeatureTestResult.1
        @Override // android.os.Parcelable.Creator
        public testKitFeatureTestResult createFromParcel(Parcel parcel) {
            testKitFeatureTestResult testkitfeaturetestresult = new testKitFeatureTestResult();
            testkitfeaturetestresult.readFromParcel(parcel);
            return testkitfeaturetestresult;
        }

        @Override // android.os.Parcelable.Creator
        public testKitFeatureTestResult[] newArray(int i) {
            return new testKitFeatureTestResult[i];
        }
    };
    public int ret = 0;
    public String str;

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
        parcel.writeString(this.str);
        parcel.writeInt(this.ret);
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
                this.str = parcel.readString();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.ret = parcel.readInt();
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
