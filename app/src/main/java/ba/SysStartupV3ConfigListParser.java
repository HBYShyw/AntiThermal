package ba;

import aa.StartupDataSyncUtils;
import aa.StartupDataUtils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.os.UserHandle;
import b6.LocalLog;
import com.oplus.statistics.OplusTrack;
import f6.CommonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import u9.StartupConst;
import x9.WhiteListUtils;

/* compiled from: SysStartupV3ConfigListParser.java */
/* renamed from: ba.d, reason: use source file name */
/* loaded from: classes2.dex */
public class SysStartupV3ConfigListParser extends StartupBaseParser {

    /* renamed from: u, reason: collision with root package name */
    private static final String f4644u = StartupConst.f18934d;

    /* renamed from: j, reason: collision with root package name */
    private List<String> f4645j;

    /* renamed from: k, reason: collision with root package name */
    private List<String> f4646k;

    /* renamed from: l, reason: collision with root package name */
    private List<String> f4647l;

    /* renamed from: m, reason: collision with root package name */
    private List<String> f4648m;

    /* renamed from: n, reason: collision with root package name */
    private Map<String, Map<String, List<String>>> f4649n;

    /* renamed from: o, reason: collision with root package name */
    private StartupDataUtils f4650o;

    /* renamed from: p, reason: collision with root package name */
    private Context f4651p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f4652q;

    /* renamed from: r, reason: collision with root package name */
    private Map<String, String> f4653r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f4654s;

    /* renamed from: t, reason: collision with root package name */
    private HashSet<String> f4655t;

    public SysStartupV3ConfigListParser(Context context) {
        super(context, "StartupManager.SysStartupV3ConfigListParser", "/startup/sys_startup_v3_config_list.xml", "sys_startup_v3_config_list", f4644u, null);
        this.f4645j = new ArrayList();
        this.f4646k = new ArrayList();
        this.f4647l = new ArrayList();
        this.f4648m = new ArrayList();
        this.f4649n = null;
        this.f4652q = false;
        this.f4653r = new HashMap();
        this.f4654s = true;
        this.f4655t = new HashSet<>();
        StartupDataUtils h10 = StartupDataUtils.h(context);
        this.f4650o = h10;
        this.f4651p = context;
        this.f4654s = h10.i();
        LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "mIsNeedMonitor:" + this.f4654s);
    }

    private void x() {
        LocalLog.a("StartupManager.SysStartupV3ConfigListParser", "updateDynamicConfig!");
        PackageManager packageManager = this.f4651p.getPackageManager();
        ArrayList arrayList = new ArrayList();
        if (CommonUtil.T(this.f4651p)) {
            LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "in bootReg,use local packages info");
            arrayList.addAll(this.f4655t);
        } else {
            List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(128);
            if (installedApplications == null) {
                return;
            }
            int size = installedApplications.size();
            for (int i10 = 0; i10 < size; i10++) {
                arrayList.add(installedApplications.get(i10).packageName);
            }
        }
        Map<String, Map<String, List<String>>> w10 = this.f4650o.w();
        this.f4649n = w10;
        this.f4652q = w10.containsKey("emptyKey");
        y(arrayList, this.f4649n.get("1"), true);
        y(arrayList, this.f4649n.get("0"), false);
        if (!this.f4650o.C(this.f4649n.get("1"), this.f4649n.get("0"), true, false)) {
            this.f4653r.put("parse_failed", "true");
        }
        if (!this.f4654s || this.f4653r.isEmpty()) {
            return;
        }
        OplusTrack.onCommon(this.f4651p, "20089", "startup_v3_exception_info", this.f4653r);
    }

    private void y(List<String> list, Map<String, List<String>> map, boolean z10) {
        Iterator<String> it;
        boolean z11;
        List<String> list2 = list;
        List<String> list3 = z10 ? this.f4645j : this.f4646k;
        List<String> list4 = map.get("userCare");
        List<String> list5 = map.get("switch");
        List<String> list6 = z10 ? this.f4647l : this.f4648m;
        List<String> arrayList = new ArrayList<>();
        List<String> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList();
        if (this.f4652q) {
            LocalLog.a("StartupManager.SysStartupV3ConfigListParser", "init 3.0 config, allowedList: " + list5 + " userCaredList: " + list4);
            list5.addAll(m(z10 ? "/startup/bootallow.txt" : "/startup/associate_startup_whitelist.txt"));
            list4.addAll(m(z10 ? "/startup/bootfixed.txt" : "/startup/fixedRecord.txt"));
        }
        for (int i10 = 0; i10 < list.size(); i10++) {
            String str = list2.get(i10);
            if (list4.contains(str)) {
                if (list5.contains(str) || list6.contains(str)) {
                    arrayList2.add(str);
                }
                arrayList.add(str);
            } else if (list3.contains(str)) {
                arrayList2.add(str);
                arrayList.add(str);
            }
        }
        if (z10) {
            arrayList3.addAll(WhiteListUtils.b(this.f4651p));
        } else {
            arrayList3.addAll(WhiteListUtils.a(this.f4651p));
        }
        arrayList2.addAll(arrayList3);
        arrayList.addAll(arrayList3);
        Set<String> j10 = this.f4650o.j();
        if (j10.isEmpty()) {
            LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "monitorList need to add special app : melody");
            j10.add("com.oplus.melody");
        }
        LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "monitorList:" + j10);
        if (this.f4654s) {
            StringBuilder sb2 = new StringBuilder();
            boolean z12 = false;
            for (Iterator<String> it2 = j10.iterator(); it2.hasNext(); it2 = it) {
                String next = it2.next();
                boolean z13 = true;
                if (list2.contains(next)) {
                    it = it2;
                    z11 = false;
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    it = it2;
                    sb3.append("monitor app not in installed list, pkgName:");
                    sb3.append(next);
                    LocalLog.l("StartupManager.SysStartupV3ConfigListParser", sb3.toString());
                    sb2.append("=not_in_install");
                    z11 = true;
                }
                if (list4.contains(next) && !list5.contains(next) && list6.contains(next)) {
                    LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "user close in hideList, pkgName:" + next);
                    sb2.append("=user_close_in_hideList");
                    z11 = true;
                }
                if (!arrayList2.contains(next)) {
                    LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "monitor app not in white list, pkgName:" + next);
                    arrayList2.add(next);
                    sb2.append("=not_in_allow");
                    z11 = true;
                }
                if (arrayList.contains(next)) {
                    z13 = z11;
                } else {
                    LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "monitor app not in pkgList, pkgName:" + next);
                    arrayList.add(next);
                    sb2.append("=not_in_pkgList");
                }
                if (z13) {
                    sb2.append(next);
                    sb2.append("#");
                    z12 = z13;
                }
                list2 = list;
            }
            if (z12) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                sb2.append("isAutoStart:");
                sb2.append(z10);
                sb2.append("=parseFile:");
                sb2.append(this.f4626b);
                sb2.append("=Version:");
                sb2.append(this.f4625a);
                sb2.append("=BootTime:");
                sb2.append(elapsedRealtime);
            }
            LocalLog.l("StartupManager.SysStartupV3ConfigListParser", "list_exception:" + ((Object) sb2));
            if (!sb2.toString().isEmpty()) {
                this.f4653r.put("list_exception", sb2.toString());
            }
        }
        map.put("userCare", list4);
        map.put("switch", arrayList2);
        map.put("pkgName", arrayList);
    }

    @Override // ba.StartupBaseParser
    void a() {
    }

    @Override // ba.StartupBaseParser
    void c(XmlPullParser xmlPullParser) {
    }

    @Override // ba.StartupBaseParser
    void l(boolean z10, String str) {
        t("/startup/sys_startup_v3_config_list.xml", str);
        r("/startup/autostart_white_list.txt", this.f4645j);
        r("/startup/associate_white_list.txt", this.f4646k);
        r("/startup/autostart_hide_list.txt", this.f4647l);
        r("/startup/associate_hide_list.txt", this.f4648m);
        x();
    }

    @Override // ba.StartupBaseParser
    void s(String str) {
        if ("/startup/sys_startup_v3_config_list.xml".equals(str) && UserHandle.myUserId() == 0) {
            StartupDataSyncUtils.d(str);
        }
    }

    @Override // ba.StartupBaseParser
    void v(String str, XmlPullParser xmlPullParser) {
        if (str.equals("startup")) {
            String attributeValue = xmlPullParser.getAttributeValue(null, "pkgName");
            String attributeValue2 = xmlPullParser.getAttributeValue(null, "all");
            String attributeValue3 = xmlPullParser.getAttributeValue(null, "type");
            if (attributeValue2 == null || attributeValue3 == null || attributeValue == null) {
                return;
            }
            this.f4655t.add(attributeValue);
            int parseInt = Integer.parseInt(attributeValue2, 2);
            List<String> list = "1".equals(attributeValue3) ? this.f4645j : this.f4646k;
            List<String> list2 = "1".equals(attributeValue3) ? this.f4647l : this.f4648m;
            if ((parseInt & 1) == 1) {
                list.add(attributeValue);
            }
            if ((parseInt & 2) == 2) {
                list2.add(attributeValue);
            }
        }
    }

    public Map<String, Map<String, List<String>>> w() {
        return this.f4649n;
    }
}
