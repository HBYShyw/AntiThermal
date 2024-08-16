package com.oplus.deepthinker.sdk.app.userprofile.labels;

import android.os.Bundle;
import android.text.TextUtils;
import com.oplus.deepthinker.sdk.app.IProviderFeature;
import com.oplus.deepthinker.sdk.app.SDKLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AppActionPeriodLabel {
    private String mPackage;
    private Map<Double, Double> mPeriodMap;

    public AppActionPeriodLabel(String str, Map<Double, Double> map) {
        this.mPackage = str;
        this.mPeriodMap = map;
    }

    private void constructFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            String[] split = str.split(",");
            if (split.length < 3) {
                return;
            }
            this.mPackage = split[0];
            this.mPeriodMap = new HashMap();
            for (int i10 = 1; i10 < split.length; i10 += 2) {
                try {
                    Double valueOf = Double.valueOf(Double.parseDouble(split[i10]));
                    Double valueOf2 = Double.valueOf(Double.parseDouble(split[i10 + 1]));
                    if (valueOf != null && valueOf2 != null) {
                        this.mPeriodMap.put(valueOf, valueOf2);
                    }
                } catch (Exception e10) {
                    SDKLog.e("construct app active error, ignore cur pair.\n" + e10.toString());
                }
            }
        } catch (Throwable th) {
            SDKLog.e("Error when construct from string: " + th);
        }
    }

    public static List<AppActionPeriodLabel> constructList(String str) {
        if (str == null) {
            return null;
        }
        String[] split = str.split(";");
        if (split.length <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : split) {
            if (str2 != null) {
                AppActionPeriodLabel appActionPeriodLabel = new AppActionPeriodLabel(str2);
                if (appActionPeriodLabel.validate()) {
                    arrayList.add(appActionPeriodLabel);
                }
            }
        }
        return arrayList;
    }

    public static String listToString(List<AppActionPeriodLabel> list) {
        String appActionPeriodLabel;
        if (list == null || list.size() <= 0) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        for (AppActionPeriodLabel appActionPeriodLabel2 : list) {
            if (appActionPeriodLabel2 != null && (appActionPeriodLabel = appActionPeriodLabel2.toString()) != null) {
                sb2.append(appActionPeriodLabel);
                sb2.append(";");
            }
        }
        sb2.deleteCharAt(sb2.length() - 1);
        return sb2.toString();
    }

    public String getPackage() {
        return this.mPackage;
    }

    public Map<Double, Double> getPeriodMap() {
        return this.mPeriodMap;
    }

    public String toString() {
        if (this.mPackage == null || this.mPeriodMap == null) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mPackage);
        sb2.append(",");
        int i10 = 0;
        int size = this.mPeriodMap.size() - 1;
        for (Map.Entry<Double, Double> entry : this.mPeriodMap.entrySet()) {
            sb2.append(entry.getKey());
            sb2.append(",");
            sb2.append(entry.getValue());
            int i11 = i10 + 1;
            if (i10 < size) {
                sb2.append(",");
            }
            i10 = i11;
        }
        return sb2.toString();
    }

    public boolean validate() {
        Map<Double, Double> map;
        return (TextUtils.isEmpty(this.mPackage) || (map = this.mPeriodMap) == null || map.size() <= 0) ? false : true;
    }

    public AppActionPeriodLabel(Bundle bundle) {
        String str;
        try {
            str = bundle.getString(IProviderFeature.USERPROFILE_QUERY_RESULT);
        } catch (Throwable th) {
            SDKLog.e("Get app active result error: " + th);
            str = null;
        }
        constructFromString(str);
    }

    public AppActionPeriodLabel(String str) {
        constructFromString(str);
    }
}
