package com.oplus.crmodel;

import android.os.Build;
import android.os.SystemProperties;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ConfidentialRealModel {
    private static final String TAG = "CRmodel";
    private String mFactoryProductName = SystemProperties.get("ro.build.display.full_id");
    private String mMtkFactoryProductName = SystemProperties.get("ro.mediatek.version.release");

    public boolean ConfidentialRealModelOk(String cmPackageName) {
        boolean isConfVersion = "true".equals(SystemProperties.get("persist.version.confidential"));
        if (!isConfVersion || 0 == 0) {
            return false;
        }
        return true;
    }

    public void changeToRealModel() {
        String realModelName = null;
        String str = this.mFactoryProductName;
        if (str != null && str.length() != 0) {
            String str2 = this.mFactoryProductName;
            realModelName = str2.substring(0, str2.indexOf("_"));
        } else {
            String str3 = this.mMtkFactoryProductName;
            if (str3 != null && str3.length() != 0) {
                String str4 = this.mMtkFactoryProductName;
                realModelName = str4.substring(0, str4.indexOf("_"));
            }
        }
        if (realModelName != null && realModelName.length() != 0) {
            try {
                Field field = Build.class.getField("MODEL");
                field.setAccessible(true);
                field.set(Build.class, realModelName);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
