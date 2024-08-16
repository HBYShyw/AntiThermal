package com.android.server.location.gnss;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssSatelliteBlocklistHelper {
    private static final String BLOCKLIST_DELIMITER = ",";
    private static final String TAG = "GnssBlocklistHelper";
    private final GnssSatelliteBlocklistCallback mCallback;
    private final Context mContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface GnssSatelliteBlocklistCallback {
        void onUpdateSatelliteBlocklist(int[] iArr, int[] iArr2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GnssSatelliteBlocklistHelper(Context context, Looper looper, GnssSatelliteBlocklistCallback gnssSatelliteBlocklistCallback) {
        this.mContext = context;
        this.mCallback = gnssSatelliteBlocklistCallback;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("gnss_satellite_blocklist"), true, new ContentObserver(new Handler(looper)) { // from class: com.android.server.location.gnss.GnssSatelliteBlocklistHelper.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                GnssSatelliteBlocklistHelper.this.updateSatelliteBlocklist();
            }
        }, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSatelliteBlocklist() {
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "gnss_satellite_blocklist");
        if (string == null) {
            string = "";
        }
        Log.i(TAG, String.format("Update GNSS satellite blocklist: %s", string));
        try {
            List<Integer> parseSatelliteBlocklist = parseSatelliteBlocklist(string);
            if (parseSatelliteBlocklist.size() % 2 != 0) {
                Log.e(TAG, "blocklist string has odd number of values.Aborting updateSatelliteBlocklist");
                return;
            }
            int size = parseSatelliteBlocklist.size() / 2;
            int[] iArr = new int[size];
            int[] iArr2 = new int[size];
            for (int i = 0; i < size; i++) {
                int i2 = i * 2;
                iArr[i] = parseSatelliteBlocklist.get(i2).intValue();
                iArr2[i] = parseSatelliteBlocklist.get(i2 + 1).intValue();
            }
            this.mCallback.onUpdateSatelliteBlocklist(iArr, iArr2);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Exception thrown when parsing blocklist string.", e);
        }
    }

    @VisibleForTesting
    static List<Integer> parseSatelliteBlocklist(String str) throws NumberFormatException {
        String[] split = str.split(BLOCKLIST_DELIMITER);
        ArrayList arrayList = new ArrayList(split.length);
        for (String str2 : split) {
            String trim = str2.trim();
            if (!"".equals(trim)) {
                int parseInt = Integer.parseInt(trim);
                if (parseInt < 0) {
                    throw new NumberFormatException("Negative value is invalid.");
                }
                arrayList.add(Integer.valueOf(parseInt));
            }
        }
        return arrayList;
    }
}
