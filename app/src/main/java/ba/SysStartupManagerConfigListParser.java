package ba;

import aa.GlobalBlackListUtils;
import aa.MaliciousPreventUtils;
import aa.SPSListUtils;
import aa.StartupDataSyncUtils;
import aa.StartupDataUtils;
import aa.UnstableAppUtils;
import android.content.Context;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: SysStartupManagerConfigListParser.java */
/* renamed from: ba.c, reason: use source file name */
/* loaded from: classes2.dex */
public class SysStartupManagerConfigListParser extends StartupBaseParser {

    /* renamed from: j, reason: collision with root package name */
    private List<String> f4633j;

    /* renamed from: k, reason: collision with root package name */
    private List<String> f4634k;

    /* renamed from: l, reason: collision with root package name */
    private List<String> f4635l;

    /* renamed from: m, reason: collision with root package name */
    private Set<String> f4636m;

    /* renamed from: n, reason: collision with root package name */
    private Set<String> f4637n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f4638o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f4639p;

    /* renamed from: q, reason: collision with root package name */
    private Context f4640q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f4641r;

    /* renamed from: s, reason: collision with root package name */
    private String f4642s;

    /* renamed from: t, reason: collision with root package name */
    private String f4643t;

    public SysStartupManagerConfigListParser(boolean z10, Context context) {
        super(context, "StartupManager.AssociateConfigList", "/startup/startup_manager.xml", "sys_startupmanager_config_list", "my_region/etc/startup/sys_startupmanager_config_list.xml", "system_ext/oplus/startup_manager.xml");
        this.f4633j = new ArrayList();
        this.f4634k = new ArrayList();
        this.f4635l = new ArrayList();
        this.f4636m = new HashSet();
        this.f4637n = new ArraySet();
        this.f4638o = false;
        this.f4639p = true;
        this.f4640q = null;
        this.f4641r = true ^ y5.b.c();
        this.f4642s = null;
        this.f4643t = null;
        this.f4638o = z10;
        this.f4640q = context;
    }

    private void w(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "mode");
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "pkgName");
        if (TextUtils.isEmpty(attributeValue) || TextUtils.isEmpty(attributeValue2)) {
            return;
        }
        if (EventType.STATE_PACKAGE_CHANGED_ADD.equals(attributeValue)) {
            this.f4633j.add(attributeValue2);
        } else if ("del".equals(attributeValue)) {
            this.f4634k.add(attributeValue2);
        }
    }

    private void x(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE);
        if (attributeValue != null) {
            if (Integer.parseInt(attributeValue) == 0) {
                this.f4639p = false;
            } else {
                this.f4639p = true;
            }
        }
    }

    @Override // ba.StartupBaseParser
    protected void a() {
    }

    @Override // ba.StartupBaseParser
    protected void c(XmlPullParser xmlPullParser) {
    }

    @Override // ba.StartupBaseParser
    protected void l(boolean z10, String str) {
        StartupDataUtils h10 = StartupDataUtils.h(this.f4640q);
        h10.H("/startup/startup_manager.xml", str);
        h10.n(this.f4636m);
        h10.l(this.f4639p);
        if (!TextUtils.isEmpty(this.f4642s) || !TextUtils.isEmpty(this.f4643t)) {
            h10.R(this.f4642s, this.f4643t);
        }
        if (this.f4641r) {
            GlobalBlackListUtils.c(this.f4640q).i(this.f4633j, this.f4634k);
        }
        UnstableAppUtils.b(this.f4640q).i(!this.f4638o, this.f4635l);
        if (UserHandle.myUserId() == 0) {
            SPSListUtils.c(this.f4640q).n(null);
        }
        MaliciousPreventUtils.i(this.f4640q).u(this.f4637n);
    }

    @Override // ba.StartupBaseParser
    void s(String str) {
        if ("/startup/startup_manager.xml".equals(str) && UserHandle.myUserId() == 0) {
            StartupDataSyncUtils.d(str);
        }
    }

    @Override // ba.StartupBaseParser
    protected void v(String str, XmlPullParser xmlPullParser) {
        if (this.f4641r && "startupBlackPkg".equals(str)) {
            w(xmlPullParser);
            return;
        }
        if ("unstableRestrictConfig".equals(str)) {
            String attributeValue = xmlPullParser.getAttributeValue(null, "isRestrict");
            String attributeValue2 = xmlPullParser.getAttributeValue(null, "crashInterval");
            String attributeValue3 = xmlPullParser.getAttributeValue(null, "crashUploadInterval");
            String attributeValue4 = xmlPullParser.getAttributeValue(null, "restrictInterval");
            this.f4635l.add(attributeValue);
            this.f4635l.add(attributeValue2);
            this.f4635l.add(attributeValue3);
            this.f4635l.add(attributeValue4);
            return;
        }
        if ("specialMonitor".equals(str)) {
            this.f4636m.add(xmlPullParser.getAttributeValue(null, "pkgName"));
            return;
        }
        if ("isNeedMonitor".equals(str)) {
            x(xmlPullParser);
            return;
        }
        if ("maliciousRecordConfig".equals(str)) {
            MaliciousPreventUtils.i(this.f4640q).s(xmlPullParser.getAttributeValue(null, "detailInterval"), xmlPullParser.getAttributeValue(null, "dateInterval"), xmlPullParser.getAttributeValue(null, "detailMaxCount"));
            MaliciousPreventUtils.i(this.f4640q).m();
            return;
        }
        if ("StartupPreventRecordDcs".equals(str)) {
            String attributeValue5 = xmlPullParser.getAttributeValue(null, "enabled");
            if (!TextUtils.isEmpty(attributeValue5)) {
                MaliciousPreventUtils.i(this.f4640q).t(Boolean.parseBoolean(attributeValue5));
            }
            int depth = xmlPullParser.getDepth();
            while (true) {
                int next = xmlPullParser.next();
                if (next == 1) {
                    return;
                }
                if (next == 3 && xmlPullParser.getDepth() <= depth) {
                    return;
                }
                if ("item".equals(xmlPullParser.getName())) {
                    String attributeValue6 = xmlPullParser.getAttributeValue(null, "whitePkg");
                    if (!TextUtils.isEmpty(attributeValue6)) {
                        LocalLog.a("StartupManager.AssociateConfigList", "parser startup record white item: " + attributeValue6);
                        this.f4637n.add(attributeValue6);
                    }
                }
            }
        } else if ("sysAppPreventStartRecordUploadConfig".equals(str)) {
            this.f4642s = xmlPullParser.getAttributeValue(null, "preSwitch");
            this.f4643t = xmlPullParser.getAttributeValue(null, "releaseSwitch");
        }
    }
}
