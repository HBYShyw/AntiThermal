package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.server.app.GameManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PortRole implements Parcelable {
    public static final Parcelable.Creator<PortRole> CREATOR = new Parcelable.Creator<PortRole>() { // from class: android.hardware.usb.PortRole.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PortRole createFromParcel(Parcel parcel) {
            return new PortRole(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PortRole[] newArray(int i) {
            return new PortRole[i];
        }
    };
    public static final int dataRole = 1;
    public static final int mode = 2;
    public static final int powerRole = 0;
    private int _tag;
    private Object _value;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Tag {
        public static final int dataRole = 1;
        public static final int mode = 2;
        public static final int powerRole = 0;
    }

    public final int getStability() {
        return 1;
    }

    public PortRole() {
        this._tag = 0;
        this._value = (byte) 0;
    }

    private PortRole(Parcel parcel) {
        readFromParcel(parcel);
    }

    private PortRole(int i, Object obj) {
        this._tag = i;
        this._value = obj;
    }

    public int getTag() {
        return this._tag;
    }

    public static PortRole powerRole(byte b) {
        return new PortRole(0, Byte.valueOf(b));
    }

    public byte getPowerRole() {
        _assertTag(0);
        return ((Byte) this._value).byteValue();
    }

    public void setPowerRole(byte b) {
        _set(0, Byte.valueOf(b));
    }

    public static PortRole dataRole(byte b) {
        return new PortRole(1, Byte.valueOf(b));
    }

    public byte getDataRole() {
        _assertTag(1);
        return ((Byte) this._value).byteValue();
    }

    public void setDataRole(byte b) {
        _set(1, Byte.valueOf(b));
    }

    public static PortRole mode(byte b) {
        return new PortRole(2, Byte.valueOf(b));
    }

    public byte getMode() {
        _assertTag(2);
        return ((Byte) this._value).byteValue();
    }

    public void setMode(byte b) {
        _set(2, Byte.valueOf(b));
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._tag);
        int i2 = this._tag;
        if (i2 == 0) {
            parcel.writeByte(getPowerRole());
        } else if (i2 == 1) {
            parcel.writeByte(getDataRole());
        } else {
            if (i2 != 2) {
                return;
            }
            parcel.writeByte(getMode());
        }
    }

    public void readFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == 0) {
            _set(readInt, Byte.valueOf(parcel.readByte()));
            return;
        }
        if (readInt == 1) {
            _set(readInt, Byte.valueOf(parcel.readByte()));
        } else {
            if (readInt == 2) {
                _set(readInt, Byte.valueOf(parcel.readByte()));
                return;
            }
            throw new IllegalArgumentException("union: unknown tag: " + readInt);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        getTag();
        return 0;
    }

    private void _assertTag(int i) {
        if (getTag() == i) {
            return;
        }
        throw new IllegalStateException("bad access: " + _tagString(i) + ", " + _tagString(getTag()) + " is available.");
    }

    private String _tagString(int i) {
        if (i == 0) {
            return "powerRole";
        }
        if (i == 1) {
            return "dataRole";
        }
        if (i == 2) {
            return GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY;
        }
        throw new IllegalStateException("unknown field: " + i);
    }

    private void _set(int i, Object obj) {
        this._tag = i;
        this._value = obj;
    }
}
