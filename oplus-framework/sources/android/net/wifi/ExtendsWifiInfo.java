package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ExtendsWifiInfo implements Parcelable {
    public static final Parcelable.Creator<ExtendsWifiInfo> CREATOR = new Parcelable.Creator<ExtendsWifiInfo>() { // from class: android.net.wifi.ExtendsWifiInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExtendsWifiInfo createFromParcel(Parcel in) {
            ExtendsWifiInfo info = new ExtendsWifiInfo();
            info.setWifiInfo((Parcelable.Creator) ExtendsWifiInfo.getStaticFieldValue(WifiInfo.class, "CREATOR"), in);
            return info;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExtendsWifiInfo[] newArray(int size) {
            return new ExtendsWifiInfo[size];
        }
    };
    private WifiInfo mWifiInfo;

    public ExtendsWifiInfo() {
        this.mWifiInfo = null;
    }

    public ExtendsWifiInfo(WifiInfo source) {
        if (source == null) {
            return;
        }
        this.mWifiInfo = source;
    }

    public void setWifiInfo(Parcelable.Creator<WifiInfo> creator, Parcel in) {
        this.mWifiInfo = creator.createFromParcel(in);
    }

    public WifiInfo getWifiInfo() {
        return this.mWifiInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);
            field.setAccessible(true);
            Object object = field.get(null);
            return object;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.mWifiInfo.writeToParcel(dest, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
