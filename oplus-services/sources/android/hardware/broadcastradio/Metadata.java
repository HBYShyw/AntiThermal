package android.hardware.broadcastradio;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Metadata implements Parcelable {
    public static final Parcelable.Creator<Metadata> CREATOR = new Parcelable.Creator<Metadata>() { // from class: android.hardware.broadcastradio.Metadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Metadata createFromParcel(Parcel parcel) {
            return new Metadata(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Metadata[] newArray(int i) {
            return new Metadata[i];
        }
    };
    public static final int albumArt = 8;
    public static final int dabComponentName = 14;
    public static final int dabComponentNameShort = 15;
    public static final int dabEnsembleName = 10;
    public static final int dabEnsembleNameShort = 11;
    public static final int dabServiceName = 12;
    public static final int dabServiceNameShort = 13;
    public static final int programName = 9;
    public static final int rbdsPty = 2;
    public static final int rdsPs = 0;
    public static final int rdsPty = 1;
    public static final int rdsRt = 3;
    public static final int songAlbum = 6;
    public static final int songArtist = 5;
    public static final int songTitle = 4;
    public static final int stationIcon = 7;
    private int _tag;
    private Object _value;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Tag {
        public static final int albumArt = 8;
        public static final int dabComponentName = 14;
        public static final int dabComponentNameShort = 15;
        public static final int dabEnsembleName = 10;
        public static final int dabEnsembleNameShort = 11;
        public static final int dabServiceName = 12;
        public static final int dabServiceNameShort = 13;
        public static final int programName = 9;
        public static final int rbdsPty = 2;
        public static final int rdsPs = 0;
        public static final int rdsPty = 1;
        public static final int rdsRt = 3;
        public static final int songAlbum = 6;
        public static final int songArtist = 5;
        public static final int songTitle = 4;
        public static final int stationIcon = 7;
    }

    public final int getStability() {
        return 1;
    }

    public Metadata() {
        this._tag = 0;
        this._value = null;
    }

    private Metadata(Parcel parcel) {
        readFromParcel(parcel);
    }

    private Metadata(int i, Object obj) {
        this._tag = i;
        this._value = obj;
    }

    public int getTag() {
        return this._tag;
    }

    public static Metadata rdsPs(String str) {
        return new Metadata(0, str);
    }

    public String getRdsPs() {
        _assertTag(0);
        return (String) this._value;
    }

    public void setRdsPs(String str) {
        _set(0, str);
    }

    public static Metadata rdsPty(int i) {
        return new Metadata(1, Integer.valueOf(i));
    }

    public int getRdsPty() {
        _assertTag(1);
        return ((Integer) this._value).intValue();
    }

    public void setRdsPty(int i) {
        _set(1, Integer.valueOf(i));
    }

    public static Metadata rbdsPty(int i) {
        return new Metadata(2, Integer.valueOf(i));
    }

    public int getRbdsPty() {
        _assertTag(2);
        return ((Integer) this._value).intValue();
    }

    public void setRbdsPty(int i) {
        _set(2, Integer.valueOf(i));
    }

    public static Metadata rdsRt(String str) {
        return new Metadata(3, str);
    }

    public String getRdsRt() {
        _assertTag(3);
        return (String) this._value;
    }

    public void setRdsRt(String str) {
        _set(3, str);
    }

    public static Metadata songTitle(String str) {
        return new Metadata(4, str);
    }

    public String getSongTitle() {
        _assertTag(4);
        return (String) this._value;
    }

    public void setSongTitle(String str) {
        _set(4, str);
    }

    public static Metadata songArtist(String str) {
        return new Metadata(5, str);
    }

    public String getSongArtist() {
        _assertTag(5);
        return (String) this._value;
    }

    public void setSongArtist(String str) {
        _set(5, str);
    }

    public static Metadata songAlbum(String str) {
        return new Metadata(6, str);
    }

    public String getSongAlbum() {
        _assertTag(6);
        return (String) this._value;
    }

    public void setSongAlbum(String str) {
        _set(6, str);
    }

    public static Metadata stationIcon(int i) {
        return new Metadata(7, Integer.valueOf(i));
    }

    public int getStationIcon() {
        _assertTag(7);
        return ((Integer) this._value).intValue();
    }

    public void setStationIcon(int i) {
        _set(7, Integer.valueOf(i));
    }

    public static Metadata albumArt(int i) {
        return new Metadata(8, Integer.valueOf(i));
    }

    public int getAlbumArt() {
        _assertTag(8);
        return ((Integer) this._value).intValue();
    }

    public void setAlbumArt(int i) {
        _set(8, Integer.valueOf(i));
    }

    public static Metadata programName(String str) {
        return new Metadata(9, str);
    }

    public String getProgramName() {
        _assertTag(9);
        return (String) this._value;
    }

    public void setProgramName(String str) {
        _set(9, str);
    }

    public static Metadata dabEnsembleName(String str) {
        return new Metadata(10, str);
    }

    public String getDabEnsembleName() {
        _assertTag(10);
        return (String) this._value;
    }

    public void setDabEnsembleName(String str) {
        _set(10, str);
    }

    public static Metadata dabEnsembleNameShort(String str) {
        return new Metadata(11, str);
    }

    public String getDabEnsembleNameShort() {
        _assertTag(11);
        return (String) this._value;
    }

    public void setDabEnsembleNameShort(String str) {
        _set(11, str);
    }

    public static Metadata dabServiceName(String str) {
        return new Metadata(12, str);
    }

    public String getDabServiceName() {
        _assertTag(12);
        return (String) this._value;
    }

    public void setDabServiceName(String str) {
        _set(12, str);
    }

    public static Metadata dabServiceNameShort(String str) {
        return new Metadata(13, str);
    }

    public String getDabServiceNameShort() {
        _assertTag(13);
        return (String) this._value;
    }

    public void setDabServiceNameShort(String str) {
        _set(13, str);
    }

    public static Metadata dabComponentName(String str) {
        return new Metadata(14, str);
    }

    public String getDabComponentName() {
        _assertTag(14);
        return (String) this._value;
    }

    public void setDabComponentName(String str) {
        _set(14, str);
    }

    public static Metadata dabComponentNameShort(String str) {
        return new Metadata(15, str);
    }

    public String getDabComponentNameShort() {
        _assertTag(15);
        return (String) this._value;
    }

    public void setDabComponentNameShort(String str) {
        _set(15, str);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._tag);
        switch (this._tag) {
            case 0:
                parcel.writeString(getRdsPs());
                return;
            case 1:
                parcel.writeInt(getRdsPty());
                return;
            case 2:
                parcel.writeInt(getRbdsPty());
                return;
            case 3:
                parcel.writeString(getRdsRt());
                return;
            case 4:
                parcel.writeString(getSongTitle());
                return;
            case 5:
                parcel.writeString(getSongArtist());
                return;
            case 6:
                parcel.writeString(getSongAlbum());
                return;
            case 7:
                parcel.writeInt(getStationIcon());
                return;
            case 8:
                parcel.writeInt(getAlbumArt());
                return;
            case 9:
                parcel.writeString(getProgramName());
                return;
            case 10:
                parcel.writeString(getDabEnsembleName());
                return;
            case 11:
                parcel.writeString(getDabEnsembleNameShort());
                return;
            case 12:
                parcel.writeString(getDabServiceName());
                return;
            case 13:
                parcel.writeString(getDabServiceNameShort());
                return;
            case 14:
                parcel.writeString(getDabComponentName());
                return;
            case 15:
                parcel.writeString(getDabComponentNameShort());
                return;
            default:
                return;
        }
    }

    public void readFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        switch (readInt) {
            case 0:
                _set(readInt, parcel.readString());
                return;
            case 1:
                _set(readInt, Integer.valueOf(parcel.readInt()));
                return;
            case 2:
                _set(readInt, Integer.valueOf(parcel.readInt()));
                return;
            case 3:
                _set(readInt, parcel.readString());
                return;
            case 4:
                _set(readInt, parcel.readString());
                return;
            case 5:
                _set(readInt, parcel.readString());
                return;
            case 6:
                _set(readInt, parcel.readString());
                return;
            case 7:
                _set(readInt, Integer.valueOf(parcel.readInt()));
                return;
            case 8:
                _set(readInt, Integer.valueOf(parcel.readInt()));
                return;
            case 9:
                _set(readInt, parcel.readString());
                return;
            case 10:
                _set(readInt, parcel.readString());
                return;
            case 11:
                _set(readInt, parcel.readString());
                return;
            case 12:
                _set(readInt, parcel.readString());
                return;
            case 13:
                _set(readInt, parcel.readString());
                return;
            case 14:
                _set(readInt, parcel.readString());
                return;
            case 15:
                _set(readInt, parcel.readString());
                return;
            default:
                throw new IllegalArgumentException("union: unknown tag: " + readInt);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        getTag();
        return 0;
    }

    public String toString() {
        switch (this._tag) {
            case 0:
                return "android.hardware.broadcastradio.Metadata.rdsPs(" + Objects.toString(getRdsPs()) + ")";
            case 1:
                return "android.hardware.broadcastradio.Metadata.rdsPty(" + getRdsPty() + ")";
            case 2:
                return "android.hardware.broadcastradio.Metadata.rbdsPty(" + getRbdsPty() + ")";
            case 3:
                return "android.hardware.broadcastradio.Metadata.rdsRt(" + Objects.toString(getRdsRt()) + ")";
            case 4:
                return "android.hardware.broadcastradio.Metadata.songTitle(" + Objects.toString(getSongTitle()) + ")";
            case 5:
                return "android.hardware.broadcastradio.Metadata.songArtist(" + Objects.toString(getSongArtist()) + ")";
            case 6:
                return "android.hardware.broadcastradio.Metadata.songAlbum(" + Objects.toString(getSongAlbum()) + ")";
            case 7:
                return "android.hardware.broadcastradio.Metadata.stationIcon(" + getStationIcon() + ")";
            case 8:
                return "android.hardware.broadcastradio.Metadata.albumArt(" + getAlbumArt() + ")";
            case 9:
                return "android.hardware.broadcastradio.Metadata.programName(" + Objects.toString(getProgramName()) + ")";
            case 10:
                return "android.hardware.broadcastradio.Metadata.dabEnsembleName(" + Objects.toString(getDabEnsembleName()) + ")";
            case 11:
                return "android.hardware.broadcastradio.Metadata.dabEnsembleNameShort(" + Objects.toString(getDabEnsembleNameShort()) + ")";
            case 12:
                return "android.hardware.broadcastradio.Metadata.dabServiceName(" + Objects.toString(getDabServiceName()) + ")";
            case 13:
                return "android.hardware.broadcastradio.Metadata.dabServiceNameShort(" + Objects.toString(getDabServiceNameShort()) + ")";
            case 14:
                return "android.hardware.broadcastradio.Metadata.dabComponentName(" + Objects.toString(getDabComponentName()) + ")";
            case 15:
                return "android.hardware.broadcastradio.Metadata.dabComponentNameShort(" + Objects.toString(getDabComponentNameShort()) + ")";
            default:
                throw new IllegalStateException("unknown field: " + this._tag);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Metadata)) {
            return false;
        }
        Metadata metadata = (Metadata) obj;
        return this._tag == metadata._tag && Objects.deepEquals(this._value, metadata._value);
    }

    public int hashCode() {
        return Arrays.deepHashCode(Arrays.asList(Integer.valueOf(this._tag), this._value).toArray());
    }

    private void _assertTag(int i) {
        if (getTag() == i) {
            return;
        }
        throw new IllegalStateException("bad access: " + _tagString(i) + ", " + _tagString(getTag()) + " is available.");
    }

    private String _tagString(int i) {
        switch (i) {
            case 0:
                return "rdsPs";
            case 1:
                return "rdsPty";
            case 2:
                return "rbdsPty";
            case 3:
                return "rdsRt";
            case 4:
                return "songTitle";
            case 5:
                return "songArtist";
            case 6:
                return "songAlbum";
            case 7:
                return "stationIcon";
            case 8:
                return "albumArt";
            case 9:
                return "programName";
            case 10:
                return "dabEnsembleName";
            case 11:
                return "dabEnsembleNameShort";
            case 12:
                return "dabServiceName";
            case 13:
                return "dabServiceNameShort";
            case 14:
                return "dabComponentName";
            case 15:
                return "dabComponentNameShort";
            default:
                throw new IllegalStateException("unknown field: " + i);
        }
    }

    private void _set(int i, Object obj) {
        this._tag = i;
        this._value = obj;
    }
}
