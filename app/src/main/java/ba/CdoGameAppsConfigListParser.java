package ba;

import aa.StartupDataSyncUtils;
import android.content.Context;
import android.text.TextUtils;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: CdoGameAppsConfigListParser.java */
/* renamed from: ba.a, reason: use source file name */
/* loaded from: classes2.dex */
public class CdoGameAppsConfigListParser extends StartupBaseParser {
    public CdoGameAppsConfigListParser(Context context) {
        super(context, "StartupManager.CdoGameAppsConfig", "startup/game_pay_monitor_config.xml", "cdo_game_apps_config_list", "my_region/etc/startup/cdo_game_apps_config_list.xml", null);
    }

    @Override // ba.StartupBaseParser
    void a() {
    }

    @Override // ba.StartupBaseParser
    void c(XmlPullParser xmlPullParser) {
    }

    @Override // ba.StartupBaseParser
    void l(boolean z10, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        t("startup/game_pay_monitor_config.xml", str);
    }

    @Override // ba.StartupBaseParser
    void s(String str) {
        StartupDataSyncUtils.d(str);
    }

    @Override // ba.StartupBaseParser
    void v(String str, XmlPullParser xmlPullParser) {
    }
}
