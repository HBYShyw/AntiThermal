package i9;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import com.oplus.sceneservice.sdk.dataprovider.bean.PhoneStatusInfo;
import h9.PhoneStatusColumns;
import l9.CursorUtils;
import l9.LogUtils;

/* compiled from: PhoneStatusManager.java */
/* renamed from: i9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class PhoneStatusManager extends BaseManager<PhoneStatusInfo> {

    /* renamed from: b, reason: collision with root package name */
    private static volatile PhoneStatusManager f12679b;

    private PhoneStatusManager(Context context) {
        super(context);
    }

    public static PhoneStatusManager g(Context context) {
        if (f12679b == null) {
            synchronized (PhoneStatusManager.class) {
                if (f12679b == null) {
                    f12679b = new PhoneStatusManager(context);
                }
            }
        }
        return f12679b;
    }

    @Override // i9.BaseManager
    public Uri b() {
        return PhoneStatusColumns.f12015a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // i9.BaseManager
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public PhoneStatusInfo a(Cursor cursor) {
        PhoneStatusInfo phoneStatusInfo = new PhoneStatusInfo();
        phoneStatusInfo.mCurrentLatitude = CursorUtils.a(cursor, "LATITUDE").doubleValue();
        phoneStatusInfo.mCurrentLongitude = CursorUtils.a(cursor, "LONGITUDE").doubleValue();
        phoneStatusInfo.mConnectedWifiName = CursorUtils.d(cursor, "CONNECTED_WIFI");
        phoneStatusInfo.mConnectedWifiBssid = CursorUtils.d(cursor, "CONNECTED_WIFI_BSSID");
        phoneStatusInfo.mAroundWifiBssid_1 = CursorUtils.d(cursor, "ARROUND_WIFI_BSSID_1");
        phoneStatusInfo.mAroundWifiName_1 = CursorUtils.d(cursor, "ARROUND_WIFI_1");
        phoneStatusInfo.mAroundWifiBssid_2 = CursorUtils.d(cursor, "ARROUND_WIFI_BSSID_2");
        phoneStatusInfo.mAroundWifiName_2 = CursorUtils.d(cursor, "ARROUND_WIFI_2");
        phoneStatusInfo.mAroundWifiBssid_3 = CursorUtils.d(cursor, "ARROUND_WIFI_BSSID_3");
        phoneStatusInfo.mAroundWifiName_3 = CursorUtils.d(cursor, "ARROUND_WIFI_3");
        phoneStatusInfo.mAroundWifiBssid_4 = CursorUtils.d(cursor, "ARROUND_WIFI_BSSID_4");
        phoneStatusInfo.mAroundWifiName_4 = CursorUtils.d(cursor, "ARROUND_WIFI_4");
        phoneStatusInfo.mAroundWifiBssid_5 = CursorUtils.d(cursor, "ARROUND_WIFI_BSSID_5");
        phoneStatusInfo.mAroundWifiName_5 = CursorUtils.d(cursor, "ARROUND_WIFI_5");
        phoneStatusInfo.mLastSuccessfulUpdateLocationTime = CursorUtils.c(cursor, "LAST_SUCCESSFUL_UPDATE_LOCATION_TIME").longValue();
        phoneStatusInfo.mCurrentLocationInfo = CursorUtils.d(cursor, "LOCATION_INFO");
        phoneStatusInfo.mLastLocationInfo = CursorUtils.d(cursor, "LAST_LOCATION_INFO");
        return phoneStatusInfo;
    }

    public PhoneStatusInfo h() {
        return e(null, null, null);
    }

    public boolean i(Context context, ContentObserver contentObserver, boolean z10) {
        try {
            context.getContentResolver().registerContentObserver(b(), z10, contentObserver);
            return true;
        } catch (Throwable th) {
            LogUtils.b("PhoneStatusManager", "registerContentObserver: throwable " + th);
            return false;
        }
    }

    public boolean j(Context context, ContentObserver contentObserver) {
        try {
            context.getContentResolver().unregisterContentObserver(contentObserver);
            return true;
        } catch (Throwable th) {
            LogUtils.b("PhoneStatusManager", "unRegisterContentObserver: throwable " + th);
            return false;
        }
    }
}
