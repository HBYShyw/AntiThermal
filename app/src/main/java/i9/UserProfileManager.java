package i9;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import h9.UserProfileColumns;
import java.util.ArrayList;
import l9.CursorUtils;
import l9.LogUtils;
import l9.NumberUtils;

/* compiled from: UserProfileManager.java */
/* renamed from: i9.f, reason: use source file name */
/* loaded from: classes2.dex */
public class UserProfileManager extends BaseManager<UserProfileInfo> {

    /* renamed from: b, reason: collision with root package name */
    private static volatile UserProfileManager f12683b;

    private UserProfileManager(Context context) {
        super(context);
    }

    public static UserProfileManager g(Context context) {
        if (f12683b == null) {
            synchronized (UserProfileManager.class) {
                if (f12683b == null) {
                    f12683b = new UserProfileManager(context);
                }
            }
        }
        return f12683b;
    }

    private UserProfileInfo h(String str) {
        return e("tag=?", new String[]{str}, null);
    }

    @Override // i9.BaseManager
    public Uri b() {
        return UserProfileColumns.f12021a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // i9.BaseManager
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public UserProfileInfo a(Cursor cursor) {
        UserProfileInfo userProfileInfo = new UserProfileInfo();
        userProfileInfo.mId = CursorUtils.b(cursor, "_id");
        userProfileInfo.mTravelMode = CursorUtils.b(cursor, "travel_mode");
        userProfileInfo.mDefaultMapApp = CursorUtils.b(cursor, "default_map");
        userProfileInfo.mHomeLatitude = CursorUtils.a(cursor, "home_latitude").doubleValue();
        userProfileInfo.mHomeLongitude = CursorUtils.a(cursor, "home_longitude").doubleValue();
        userProfileInfo.mCompanyLatitude = CursorUtils.a(cursor, "company_latitude").doubleValue();
        userProfileInfo.mCompanyLongitude = CursorUtils.a(cursor, "company_longitude").doubleValue();
        userProfileInfo.mTag = CursorUtils.d(cursor, TriggerEvent.NOTIFICATION_TAG);
        userProfileInfo.mHomeLatLonType = CursorUtils.d(cursor, "home_latlon_type");
        userProfileInfo.mCompanyLatLonType = CursorUtils.d(cursor, "company_latlon_type");
        userProfileInfo.mHomeWifiName = CursorUtils.d(cursor, "home_wifi_name");
        userProfileInfo.mHomeWifiBssid = CursorUtils.d(cursor, "home_wifi_bssid");
        userProfileInfo.mCompanyWifiName = CursorUtils.d(cursor, "company_wifi_name");
        userProfileInfo.mCompanyWifiBssid = CursorUtils.d(cursor, "company_wifi_bssid");
        userProfileInfo.mHomeAddress = CursorUtils.d(cursor, "home_address");
        userProfileInfo.mCompanyAddress = CursorUtils.d(cursor, "company_address");
        userProfileInfo.mLeaveHomeHour = NumberUtils.b(CursorUtils.d(cursor, "leave_home_hour"), -1);
        userProfileInfo.mLeaveHomeMin = NumberUtils.b(CursorUtils.d(cursor, "leave_home_min"), -1);
        userProfileInfo.mLeaveCompanyHour = NumberUtils.b(CursorUtils.d(cursor, "leave_company_hour"), -1);
        userProfileInfo.mLeaveCompanyMin = CursorUtils.b(cursor, "leave_company_min");
        userProfileInfo.mArriveHomeHour = NumberUtils.b(CursorUtils.d(cursor, "arrive_home_hour"), -1);
        userProfileInfo.mArriveHomeMin = NumberUtils.b(CursorUtils.d(cursor, "arrive_home_min"), -1);
        userProfileInfo.mArriveCompanyHour = NumberUtils.b(CursorUtils.d(cursor, "arrive_company_hour"), -1);
        userProfileInfo.mArriveCompanyMin = NumberUtils.b(CursorUtils.d(cursor, "arrive_company_min"), -1);
        userProfileInfo.mStartSleepTime = CursorUtils.d(cursor, "start_sleep_time");
        userProfileInfo.mEndSleepTime = CursorUtils.d(cursor, "end_sleep_time");
        userProfileInfo.mResidentLatitude = NumberUtils.a(CursorUtils.d(cursor, "resident_latitude"));
        userProfileInfo.mResidentLongitude = NumberUtils.a(CursorUtils.d(cursor, "resident_longitude"));
        userProfileInfo.mUserProfileModify = CursorUtils.b(cursor, "user_profile_modify");
        userProfileInfo.mDiffTag = CursorUtils.d(cursor, "diff_tag");
        return userProfileInfo;
    }

    public UserProfileInfo i() {
        ArrayList<UserProfileInfo> c10 = c(UserProfileColumns.f12023c, null, null, null, null);
        if (l9.e.a(c10)) {
            return null;
        }
        return c10.get(0);
    }

    public UserProfileInfo j() {
        return h("1");
    }

    public UserProfileInfo k() {
        return h(UserProfileInfo.Constant.TAG_PURE_MANUAL);
    }

    public UserProfileInfo l() {
        return h("0");
    }

    public boolean m(Context context, ContentObserver contentObserver, boolean z10) {
        try {
            context.getContentResolver().registerContentObserver(UserProfileColumns.f12023c, z10, contentObserver);
            return true;
        } catch (Throwable th) {
            LogUtils.b("UserProfileManager", "registerFinalUserProfileObserver: throwable " + th);
            return false;
        }
    }

    public boolean n(Context context, ContentObserver contentObserver) {
        try {
            context.getContentResolver().unregisterContentObserver(contentObserver);
            return true;
        } catch (Throwable th) {
            LogUtils.b("UserProfileManager", "unRegisterFinalUserProfileObserver: throwable " + th);
            return false;
        }
    }
}
