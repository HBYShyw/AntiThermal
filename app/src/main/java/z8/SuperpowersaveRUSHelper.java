package z8;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.thermalcontrol.ThermalControlConfig;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: SuperpowersaveRUSHelper.java */
/* renamed from: z8.c, reason: use source file name */
/* loaded from: classes2.dex */
public class SuperpowersaveRUSHelper {

    /* renamed from: b, reason: collision with root package name */
    private static final Uri f20300b = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* renamed from: c, reason: collision with root package name */
    private static final HashMap<String, String> f20301c = new HashMap<>();

    /* renamed from: d, reason: collision with root package name */
    public static SuperpowersaveRUSHelper f20302d = null;

    /* renamed from: a, reason: collision with root package name */
    private Object f20303a = new Object();

    private SuperpowersaveRUSHelper(Context context) {
        String b10 = b(context, "sys_powersave_policy_config_list");
        if (!TextUtils.isEmpty(b10)) {
            e(context, b10);
        }
        a();
    }

    private void a() {
        HashMap<String, String> hashMap = f20301c;
        if (!hashMap.containsKey("cpu_policy_enable")) {
            hashMap.put("cpu_policy_enable", "true");
            return;
        }
        if (!hashMap.containsKey("hypnus_policy_enable")) {
            hashMap.put("hypnus_policy_enable", "true");
            return;
        }
        if (!hashMap.containsKey("wifi_scan_always_disable")) {
            hashMap.put("wifi_scan_always_disable", "true");
            return;
        }
        if (!hashMap.containsKey("wifi_ap_state_disable")) {
            hashMap.put("wifi_ap_state_disable", "true");
            return;
        }
        if (!hashMap.containsKey("bluetooth_state_disable")) {
            hashMap.put("bluetooth_state_disable", "true");
            return;
        }
        if (!hashMap.containsKey("gps_state_disable")) {
            hashMap.put("gps_state_disable", "true");
            return;
        }
        if (!hashMap.containsKey("osie_vision_effect_disable")) {
            hashMap.put("osie_vision_effect_disable", "true");
            return;
        }
        if (!hashMap.containsKey("aod_curved_display_disable")) {
            hashMap.put("aod_curved_display_disable", "true");
            return;
        }
        if (!hashMap.containsKey("call_curved_display_disable")) {
            hashMap.put("call_curved_display_disable", UserProfileInfo.Constant.TAG_PURE_MANUAL);
            return;
        }
        if (!hashMap.containsKey("oplus_color_mode_disable")) {
            hashMap.put("oplus_color_mode_disable", "1");
            return;
        }
        if (!hashMap.containsKey("sync_automatically_disable")) {
            hashMap.put("sync_automatically_disable", "true");
            return;
        }
        if (!hashMap.containsKey("auto_rotation_disable")) {
            hashMap.put("auto_rotation_disable", "true");
            return;
        }
        if (!hashMap.containsKey("color_dark_mode_enabled")) {
            hashMap.put("color_dark_mode_enabled", "true");
        } else if (!hashMap.containsKey("black_screen_gesture_disable")) {
            hashMap.put("black_screen_gesture_disable", "true");
        } else {
            if (hashMap.containsKey("power_save_mode_enabled")) {
                return;
            }
            hashMap.put("power_save_mode_enabled", "true");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0049 A[Catch: Exception -> 0x003f, TRY_LEAVE, TryCatch #0 {Exception -> 0x003f, blocks: (B:18:0x002d, B:20:0x0033, B:7:0x0049, B:5:0x0041), top: B:17:0x002d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String b(Context context, String str) {
        Cursor cursor;
        String string;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            cursor = context.getContentResolver().query(f20300b, strArr, "filtername=\"" + str + "\"", null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        int columnIndex = cursor.getColumnIndex(ThermalControlConfig.COLUMN_NAME_XML);
                        cursor.moveToNext();
                        string = cursor.getString(columnIndex);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (Exception e10) {
                    e = e10;
                    if (cursor != null) {
                        cursor.close();
                    }
                    LocalLog.l("SuperpowersaveRUSHelper", "We can not get Filtrate app data from provider,because of " + e);
                    return null;
                }
            }
            LocalLog.l("SuperpowersaveRUSHelper", "The Filtrate app cursor is null !!!");
            string = null;
            if (cursor != null) {
            }
            return string;
        } catch (Exception e11) {
            e = e11;
            cursor = null;
        }
    }

    public static SuperpowersaveRUSHelper c(Context context) {
        SuperpowersaveRUSHelper superpowersaveRUSHelper;
        synchronized (SuperpowersaveRUSHelper.class) {
            if (f20302d == null) {
                f20302d = new SuperpowersaveRUSHelper(context);
            }
            superpowersaveRUSHelper = f20302d;
        }
        return superpowersaveRUSHelper;
    }

    private void e(Context context, String str) {
        int next;
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new StringReader(str));
            newPullParser.nextTag();
            do {
                next = newPullParser.next();
                if (next == 2 && newPullParser.getName().equals("policy_item")) {
                    String attributeValue = newPullParser.getAttributeValue(null, "name");
                    if (newPullParser.next() == 4) {
                        f20301c.put(attributeValue, newPullParser.getText());
                    }
                }
            } while (next != 1);
        } catch (IOException e10) {
            LocalLog.l("SuperpowersaveRUSHelper", "failed parsing " + e10);
        } catch (IndexOutOfBoundsException e11) {
            LocalLog.l("SuperpowersaveRUSHelper", "failed parsing " + e11);
        } catch (NullPointerException e12) {
            LocalLog.l("SuperpowersaveRUSHelper", "failed parsing " + e12);
        } catch (NumberFormatException e13) {
            LocalLog.l("SuperpowersaveRUSHelper", "failed parsing " + e13);
        } catch (XmlPullParserException e14) {
            LocalLog.l("SuperpowersaveRUSHelper", "failed parsing " + e14);
        }
    }

    public String d(String str, String str2) {
        synchronized (this.f20303a) {
            HashMap<String, String> hashMap = f20301c;
            if (hashMap.containsKey(str)) {
                str2 = hashMap.get(str);
            }
        }
        return str2;
    }

    public void f(Context context) {
        String b10 = b(context, "sys_powersave_policy_config_list");
        synchronized (this.f20303a) {
            f20301c.clear();
            if (!TextUtils.isEmpty(b10)) {
                e(context, b10);
            }
            a();
        }
    }
}
