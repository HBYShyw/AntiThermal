package ia;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalConfigUtil;
import f6.f;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import x9.IconUtils;

/* compiled from: ThermalCustomizeChargeUtils.java */
/* renamed from: ia.c, reason: use source file name */
/* loaded from: classes2.dex */
public class ThermalCustomizeChargeUtils {

    /* renamed from: d, reason: collision with root package name */
    public static final boolean f12692d = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* renamed from: e, reason: collision with root package name */
    private static volatile ThermalCustomizeChargeUtils f12693e;

    /* renamed from: a, reason: collision with root package name */
    private Map<String, Integer> f12694a;

    /* renamed from: b, reason: collision with root package name */
    private ArrayList<String> f12695b;

    /* renamed from: c, reason: collision with root package name */
    private Context f12696c;

    private ThermalCustomizeChargeUtils(Context context) {
        HashMap hashMap = new HashMap();
        this.f12694a = hashMap;
        this.f12695b = null;
        this.f12696c = context;
        if (hashMap.isEmpty()) {
            a();
        }
    }

    private void a() {
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File thermalConfigFile = ThermalConfigUtil.thermalConfigFile("sys_customize_charge_config.xml");
            if (!thermalConfigFile.exists()) {
                if (f12692d) {
                    LocalLog.d("ThermalCustomizeChargeUtils", "customizeCharge config not exist");
                }
            } else {
                Document parse = newDocumentBuilder.parse(thermalConfigFile);
                if (parse == null) {
                    LocalLog.b("ThermalCustomizeChargeUtils", "customizeCharge, null doc!");
                } else {
                    b(parse);
                }
            }
        } catch (IOException e10) {
            LocalLog.b("ThermalCustomizeChargeUtils", "customizeCharge IOException:" + e10);
        } catch (ParserConfigurationException e11) {
            LocalLog.b("ThermalCustomizeChargeUtils", "customizeCharge ParserConfigurationException:" + e11);
        } catch (SAXException e12) {
            LocalLog.b("ThermalCustomizeChargeUtils", "customizeCharge SAXException:" + e12);
        }
    }

    private void b(Document document) {
        Element element;
        String tagName;
        Element documentElement = document.getDocumentElement();
        if (documentElement == null) {
            LocalLog.b("ThermalCustomizeChargeUtils", "commonParseConfigForDocument, root == null");
            return;
        }
        String trim = documentElement.getTagName().trim();
        if (f12692d) {
            LocalLog.d("ThermalCustomizeChargeUtils", "xml root is " + trim);
        }
        if (trim != null && trim.equals("sys_customize_charge_list")) {
            NodeList childNodes = documentElement.getChildNodes();
            for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
                Node item = childNodes.item(i10);
                if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null && tagName.equals("ChargeConfigItem")) {
                    j(element);
                }
            }
            return;
        }
        LocalLog.b("ThermalCustomizeChargeUtils", "xml root tag is not sys_customize_charge_list");
    }

    public static ThermalCustomizeChargeUtils h(Context context) {
        if (f12693e == null) {
            synchronized (ThermalCustomizeChargeUtils.class) {
                if (f12693e == null) {
                    f12693e = new ThermalCustomizeChargeUtils(context);
                }
            }
        }
        return f12693e;
    }

    private boolean i(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            this.f12696c.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void j(Element element) {
        Element element2;
        String tagName;
        this.f12694a.clear();
        SharedPreferences sharedPreferences = this.f12696c.getSharedPreferences("customize_charge_state", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        NodeList childNodes = element.getChildNodes();
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if ((item instanceof Element) && (tagName = (element2 = (Element) item).getTagName()) != null && element2.getTextContent() != null) {
                String textContent = element2.getTextContent();
                if (f12692d) {
                    LocalLog.a("ThermalCustomizeChargeUtils", "customize_pkgName =" + tagName + ", chargeLevel =" + textContent);
                }
                if (textContent != null) {
                    String[] split = textContent.split("_");
                    if (split.length == 2) {
                        this.f12694a.put(tagName, Integer.valueOf(Integer.parseInt(split[0])));
                        if (!sharedPreferences.getBoolean("is_init", false)) {
                            if (TextUtils.equals(split[1], "1")) {
                                edit.putBoolean(tagName, true).apply();
                            } else {
                                edit.putBoolean(tagName, false).apply();
                            }
                        }
                    }
                }
            }
        }
        edit.putBoolean("is_init", true).commit();
    }

    public Drawable c(String str) {
        Context context = this.f12696c;
        if (context == null) {
            return null;
        }
        try {
            return IconUtils.b(this.f12696c, context.getPackageManager().getApplicationIcon(str));
        } catch (Exception unused) {
            return null;
        }
    }

    public String d(String str) {
        try {
            return this.f12696c.getPackageManager().getApplicationLabel(this.f12696c.getPackageManager().getApplicationInfo(str, 128)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public ArrayList<String> e() {
        if (this.f12694a.isEmpty()) {
            return null;
        }
        ArrayList<String> arrayList = this.f12695b;
        if (arrayList != null) {
            arrayList.clear();
        }
        this.f12695b = new ArrayList<>();
        for (String str : this.f12694a.keySet()) {
            if (i(str)) {
                LocalLog.a("ThermalCustomizeChargeUtils", "getCurrentAppList addApp=" + str);
                this.f12695b.add(str);
            }
        }
        return this.f12695b;
    }

    public boolean f(String str) {
        return this.f12696c.getSharedPreferences("customize_charge_state", 0).getBoolean(str, false);
    }

    public int g(String str) {
        if (!f.J0(this.f12696c) || !f.A(this.f12696c) || this.f12694a.isEmpty() || !this.f12694a.containsKey(str)) {
            return 0;
        }
        int intValue = this.f12694a.getOrDefault(str, 2).intValue();
        if (f12692d) {
            LocalLog.a("ThermalCustomizeChargeUtils", "getCustomizeChargeLevel  pkg=" + str + " ,level =" + intValue);
        }
        return intValue;
    }

    public void k() {
        if (!this.f12694a.isEmpty()) {
            this.f12694a.clear();
        }
        SharedPreferences.Editor edit = this.f12696c.getSharedPreferences("customize_charge_state", 0).edit();
        edit.putBoolean("is_init", false);
        edit.apply();
    }

    public void l(String str, boolean z10) {
        SharedPreferences.Editor edit = this.f12696c.getSharedPreferences("customize_charge_state", 0).edit();
        edit.putBoolean(str, z10);
        edit.apply();
    }
}
