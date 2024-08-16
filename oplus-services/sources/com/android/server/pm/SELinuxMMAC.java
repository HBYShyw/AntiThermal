package com.android.server.pm;

import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Slog;
import android.util.Xml;
import com.android.server.compat.PlatformCompat;
import com.android.server.pm.Policy;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.SharedUserApi;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SELinuxMMAC {
    private static final boolean DEBUG_POLICY = false;
    private static final boolean DEBUG_POLICY_INSTALL = false;
    private static final boolean DEBUG_POLICY_ORDER = false;
    private static final String DEFAULT_SEINFO = "default";
    private static final String PRIVILEGED_APP_STR = ":privapp";
    static final long SELINUX_LATEST_CHANGES = 143539591;
    static final long SELINUX_R_CHANGES = 168782947;
    static final String TAG = "SELinuxMMAC";
    private static final String TARGETSDKVERSION_STR = ":targetSdkVersion=";
    private static List<File> sMacPermissions;
    private static List<Policy> sPolicies = new ArrayList();
    private static boolean sPolicyRead;

    static {
        ArrayList arrayList = new ArrayList();
        sMacPermissions = arrayList;
        arrayList.add(new File(Environment.getRootDirectory(), "/etc/selinux/plat_mac_permissions.xml"));
        File file = new File(Environment.getSystemExtDirectory(), "/etc/selinux/system_ext_mac_permissions.xml");
        if (file.exists()) {
            sMacPermissions.add(file);
        }
        File file2 = new File(Environment.getProductDirectory(), "/etc/selinux/product_mac_permissions.xml");
        if (file2.exists()) {
            sMacPermissions.add(file2);
        }
        File file3 = new File(Environment.getVendorDirectory(), "/etc/selinux/vendor_mac_permissions.xml");
        if (file3.exists()) {
            sMacPermissions.add(file3);
        }
        File file4 = new File(Environment.getOdmDirectory(), "/etc/selinux/odm_mac_permissions.xml");
        if (file4.exists()) {
            sMacPermissions.add(file4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x007e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean readInstallPolicy() {
        char c;
        synchronized (sPolicies) {
            if (sPolicyRead) {
                return true;
            }
            ArrayList arrayList = new ArrayList();
            XmlPullParser newPullParser = Xml.newPullParser();
            int size = sMacPermissions.size();
            FileReader fileReader = null;
            int i = 0;
            while (i < size) {
                File file = sMacPermissions.get(i);
                try {
                    try {
                        FileReader fileReader2 = new FileReader(file);
                        try {
                            Slog.d(TAG, "Using policy file " + file);
                            newPullParser.setInput(fileReader2);
                            newPullParser.nextTag();
                            newPullParser.require(2, null, "policy");
                            while (newPullParser.next() != 3) {
                                if (newPullParser.getEventType() == 2) {
                                    String name = newPullParser.getName();
                                    if (name.hashCode() == -902467798 && name.equals("signer")) {
                                        c = 0;
                                        if (c != 0) {
                                            arrayList.add(readSignerOrThrow(newPullParser));
                                        } else {
                                            skip(newPullParser);
                                        }
                                    }
                                    c = 65535;
                                    if (c != 0) {
                                    }
                                }
                            }
                            IoUtils.closeQuietly(fileReader2);
                            i++;
                            fileReader = fileReader2;
                        } catch (IOException e) {
                            e = e;
                            fileReader = fileReader2;
                            Slog.w(TAG, "Exception parsing " + file, e);
                            IoUtils.closeQuietly(fileReader);
                            return false;
                        } catch (IllegalArgumentException | IllegalStateException | XmlPullParserException e2) {
                            e = e2;
                            fileReader = fileReader2;
                            Slog.w(TAG, "Exception @" + newPullParser.getPositionDescription() + " while parsing " + file + ":" + e);
                            IoUtils.closeQuietly(fileReader);
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            fileReader = fileReader2;
                            IoUtils.closeQuietly(fileReader);
                            throw th;
                        }
                    } catch (IOException e3) {
                        e = e3;
                    } catch (IllegalArgumentException | IllegalStateException | XmlPullParserException e4) {
                        e = e4;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            PolicyComparator policyComparator = new PolicyComparator();
            Collections.sort(arrayList, policyComparator);
            if (policyComparator.foundDuplicate()) {
                Slog.w(TAG, "ERROR! Duplicate entries found parsing mac_permissions.xml files");
                return false;
            }
            synchronized (sPolicies) {
                sPolicies.clear();
                sPolicies.addAll(arrayList);
                sPolicyRead = true;
            }
            return true;
        }
    }

    private static Policy readSignerOrThrow(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, "signer");
        Policy.PolicyBuilder policyBuilder = new Policy.PolicyBuilder();
        String attributeValue = xmlPullParser.getAttributeValue(null, "signature");
        if (attributeValue != null) {
            policyBuilder.addSignature(attributeValue);
        }
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if ("seinfo".equals(name)) {
                    policyBuilder.setGlobalSeinfoOrThrow(xmlPullParser.getAttributeValue(null, "value"));
                    readSeinfo(xmlPullParser);
                } else if ("package".equals(name)) {
                    readPackageOrThrow(xmlPullParser, policyBuilder);
                } else if ("cert".equals(name)) {
                    policyBuilder.addSignature(xmlPullParser.getAttributeValue(null, "signature"));
                    readCert(xmlPullParser);
                } else {
                    skip(xmlPullParser);
                }
            }
        }
        return policyBuilder.build();
    }

    private static void readPackageOrThrow(XmlPullParser xmlPullParser, Policy.PolicyBuilder policyBuilder) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, "package");
        String attributeValue = xmlPullParser.getAttributeValue(null, "name");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if ("seinfo".equals(xmlPullParser.getName())) {
                    policyBuilder.addInnerPackageMapOrThrow(attributeValue, xmlPullParser.getAttributeValue(null, "value"));
                    readSeinfo(xmlPullParser);
                } else {
                    skip(xmlPullParser);
                }
            }
        }
    }

    private static void readCert(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, "cert");
        xmlPullParser.nextTag();
    }

    private static void readSeinfo(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, "seinfo");
        xmlPullParser.nextTag();
    }

    private static void skip(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        if (xmlPullParser.getEventType() != 2) {
            throw new IllegalStateException();
        }
        int i = 1;
        while (i != 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                i++;
            } else if (next == 3) {
                i--;
            }
        }
    }

    private static int getTargetSdkVersionForSeInfo(AndroidPackage androidPackage, SharedUserApi sharedUserApi, PlatformCompat platformCompat) {
        if (sharedUserApi != null && sharedUserApi.getPackages().size() != 0) {
            return sharedUserApi.getSeInfoTargetSdkVersion();
        }
        ApplicationInfo generateAppInfoWithoutState = AndroidPackageUtils.generateAppInfoWithoutState(androidPackage);
        if (platformCompat.isChangeEnabledInternal(SELINUX_LATEST_CHANGES, generateAppInfoWithoutState)) {
            return Math.max(10000, androidPackage.getTargetSdkVersion());
        }
        if (platformCompat.isChangeEnabledInternal(SELINUX_R_CHANGES, generateAppInfoWithoutState)) {
            return Math.max(30, androidPackage.getTargetSdkVersion());
        }
        return androidPackage.getTargetSdkVersion();
    }

    public static String getSeInfo(PackageState packageState, AndroidPackage androidPackage, SharedUserApi sharedUserApi, PlatformCompat platformCompat) {
        boolean isPrivileged;
        int targetSdkVersionForSeInfo = getTargetSdkVersionForSeInfo(androidPackage, sharedUserApi, platformCompat);
        if (sharedUserApi != null) {
            isPrivileged = packageState.isPrivileged() | sharedUserApi.isPrivileged();
        } else {
            isPrivileged = packageState.isPrivileged();
        }
        return getSeInfo(androidPackage, isPrivileged, targetSdkVersionForSeInfo);
    }

    public static String getSeInfo(AndroidPackage androidPackage, boolean z, int i) {
        String str;
        synchronized (sPolicies) {
            str = null;
            if (sPolicyRead) {
                Iterator<Policy> it = sPolicies.iterator();
                while (it.hasNext() && (str = it.next().getMatchedSeInfo(androidPackage)) == null) {
                }
            }
        }
        if (str == null) {
            str = "default";
        }
        if (z) {
            str = str + PRIVILEGED_APP_STR;
        }
        return str + TARGETSDKVERSION_STR + i;
    }
}
