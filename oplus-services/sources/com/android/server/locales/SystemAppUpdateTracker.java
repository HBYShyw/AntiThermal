package com.android.server.locales;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.LocaleList;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemAppUpdateTracker {
    private static final String ATTR_NAME = "name";
    private static final String PACKAGE_XML_TAG = "package";
    private static final String SYSTEM_APPS_XML_TAG = "system_apps";
    private static final String TAG = "SystemAppUpdateTracker";
    private final Context mContext;
    private final Object mFileLock;
    private final LocaleManagerService mLocaleManagerService;
    private final Set<String> mUpdatedApps;
    private final AtomicFile mUpdatedAppsFile;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemAppUpdateTracker(LocaleManagerService localeManagerService) {
        this(localeManagerService.mContext, localeManagerService, new AtomicFile(new File(Environment.getDataSystemDirectory(), "locale_manager_service_updated_system_apps.xml")));
    }

    @VisibleForTesting
    SystemAppUpdateTracker(Context context, LocaleManagerService localeManagerService, AtomicFile atomicFile) {
        this.mFileLock = new Object();
        this.mUpdatedApps = new HashSet();
        this.mContext = context;
        this.mLocaleManagerService = localeManagerService;
        this.mUpdatedAppsFile = atomicFile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init() {
        loadUpdatedSystemApps();
    }

    private void loadUpdatedSystemApps() {
        if (this.mUpdatedAppsFile.getBaseFile().exists()) {
            FileInputStream fileInputStream = null;
            try {
                try {
                    fileInputStream = this.mUpdatedAppsFile.openRead();
                    readFromXml(fileInputStream);
                } catch (IOException | XmlPullParserException e) {
                    Slog.e(TAG, "loadUpdatedSystemApps: Could not parse storage file ", e);
                }
            } finally {
                IoUtils.closeQuietly(fileInputStream);
            }
        }
    }

    private void readFromXml(InputStream inputStream) throws XmlPullParserException, IOException {
        TypedXmlPullParser newFastPullParser = Xml.newFastPullParser();
        newFastPullParser.setInput(inputStream, StandardCharsets.UTF_8.name());
        XmlUtils.beginDocument(newFastPullParser, SYSTEM_APPS_XML_TAG);
        int depth = newFastPullParser.getDepth();
        while (XmlUtils.nextElementWithin(newFastPullParser, depth)) {
            if (newFastPullParser.getName().equals("package")) {
                String attributeValue = newFastPullParser.getAttributeValue((String) null, "name");
                if (!TextUtils.isEmpty(attributeValue)) {
                    this.mUpdatedApps.add(attributeValue);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageUpdateFinished(String str, int i) {
        try {
            if (this.mUpdatedApps.contains(str) || !isUpdatedSystemApp(str)) {
                return;
            }
            int userId = UserHandle.getUserId(i);
            if (this.mLocaleManagerService.getInstallingPackageName(str, userId) == null) {
                return;
            }
            try {
                LocaleList applicationLocales = this.mLocaleManagerService.getApplicationLocales(str, userId);
                if (!applicationLocales.isEmpty()) {
                    this.mLocaleManagerService.notifyInstallerOfAppWhoseLocaleChanged(str, userId, applicationLocales);
                }
            } catch (RemoteException unused) {
            }
            updateBroadcastedAppsList(str);
        } catch (Exception e) {
            Slog.e(TAG, "Exception in onPackageUpdateFinished.", e);
        }
    }

    private void updateBroadcastedAppsList(String str) {
        synchronized (this.mFileLock) {
            this.mUpdatedApps.add(str);
            writeUpdatedAppsFileLocked();
        }
    }

    private void writeUpdatedAppsFileLocked() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = this.mUpdatedAppsFile.startWrite();
            writeToXmlLocked(fileOutputStream);
            this.mUpdatedAppsFile.finishWrite(fileOutputStream);
        } catch (IOException e) {
            this.mUpdatedAppsFile.failWrite(fileOutputStream);
            Slog.e(TAG, "Failed to persist the updated apps list", e);
        }
    }

    private void writeToXmlLocked(OutputStream outputStream) throws IOException {
        TypedXmlSerializer newFastSerializer = Xml.newFastSerializer();
        newFastSerializer.setOutput(outputStream, StandardCharsets.UTF_8.name());
        newFastSerializer.startDocument((String) null, Boolean.TRUE);
        newFastSerializer.startTag((String) null, SYSTEM_APPS_XML_TAG);
        for (String str : this.mUpdatedApps) {
            newFastSerializer.startTag((String) null, "package");
            newFastSerializer.attribute((String) null, "name", str);
            newFastSerializer.endTag((String) null, "package");
        }
        newFastSerializer.endTag((String) null, SYSTEM_APPS_XML_TAG);
        newFastSerializer.endDocument();
    }

    private boolean isUpdatedSystemApp(String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.mContext.getPackageManager().getApplicationInfo(str, PackageManager.ApplicationInfoFlags.of(1048576L));
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        return (applicationInfo == null || (applicationInfo.flags & 128) == 0) ? false : true;
    }

    @VisibleForTesting
    Set<String> getUpdatedApps() {
        return this.mUpdatedApps;
    }
}
