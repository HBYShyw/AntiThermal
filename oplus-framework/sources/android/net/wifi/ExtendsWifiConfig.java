package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ExtendsWifiConfig implements Parcelable {
    public static final Parcelable.Creator<ExtendsWifiConfig> CREATOR = new Parcelable.Creator<ExtendsWifiConfig>() { // from class: android.net.wifi.ExtendsWifiConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExtendsWifiConfig createFromParcel(Parcel in) {
            ExtendsWifiConfig config = new ExtendsWifiConfig();
            config.setWifiConfig((Parcelable.Creator) ExtendsWifiConfig.getStaticFieldValue(WifiConfiguration.class, "CREATOR"), in);
            return config;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExtendsWifiConfig[] newArray(int size) {
            return new ExtendsWifiConfig[size];
        }
    };
    private WifiConfiguration mWifiConfig;

    public ExtendsWifiConfig() {
        this.mWifiConfig = null;
    }

    public ExtendsWifiConfig(WifiConfiguration source) {
        if (source == null) {
            return;
        }
        this.mWifiConfig = source;
    }

    public void setWifiConfig(Parcelable.Creator<WifiConfiguration> creator, Parcel in) {
        this.mWifiConfig = creator.createFromParcel(in);
    }

    public WifiConfiguration getWifiConfig() {
        return this.mWifiConfig;
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
        this.mWifiConfig.writeToParcel(dest, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
