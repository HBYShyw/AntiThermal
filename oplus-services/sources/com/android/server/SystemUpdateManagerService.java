package com.android.server;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.ISystemUpdateManager;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SystemUpdateManagerService extends ISystemUpdateManager.Stub {
    private static final String INFO_FILE = "system-update-info.xml";
    private static final int INFO_FILE_VERSION = 0;
    private static final String KEY_BOOT_COUNT = "boot-count";
    private static final String KEY_INFO_BUNDLE = "info-bundle";
    private static final String KEY_UID = "uid";
    private static final String KEY_VERSION = "version";
    private static final String TAG = "SystemUpdateManagerService";
    private static final String TAG_INFO = "info";
    private static final int UID_UNKNOWN = -1;
    private final Context mContext;
    private final AtomicFile mFile;
    private int mLastStatus;
    private int mLastUid;
    private final Object mLock;

    public SystemUpdateManagerService(Context context) {
        Object obj = new Object();
        this.mLock = obj;
        this.mLastUid = -1;
        this.mLastStatus = 0;
        this.mContext = context;
        this.mFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), INFO_FILE));
        synchronized (obj) {
            loadSystemUpdateInfoLocked();
        }
    }

    public void updateSystemUpdateInfo(PersistableBundle persistableBundle) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", TAG);
        int i = persistableBundle.getInt("status", 0);
        if (i == 0) {
            Slog.w(TAG, "Invalid status info. Ignored");
            return;
        }
        int callingUid = Binder.getCallingUid();
        int i2 = this.mLastUid;
        if (i2 == -1 || i2 == callingUid || i != 1) {
            synchronized (this.mLock) {
                saveSystemUpdateInfoLocked(persistableBundle, callingUid);
            }
            return;
        }
        Slog.i(TAG, "Inactive updater reporting IDLE status. Ignored");
    }

    public Bundle retrieveSystemUpdateInfo() {
        Bundle loadSystemUpdateInfoLocked;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_SYSTEM_UPDATE_INFO") == -1 && this.mContext.checkCallingOrSelfPermission("android.permission.RECOVERY") == -1) {
            throw new SecurityException("Can't read system update info. Requiring READ_SYSTEM_UPDATE_INFO or RECOVERY permission.");
        }
        synchronized (this.mLock) {
            loadSystemUpdateInfoLocked = loadSystemUpdateInfoLocked();
        }
        return loadSystemUpdateInfoLocked;
    }

    private Bundle loadSystemUpdateInfoLocked() {
        PersistableBundle persistableBundle = null;
        try {
            FileInputStream openRead = this.mFile.openRead();
            try {
                persistableBundle = readInfoFileLocked(Xml.resolvePullParser(openRead));
                if (openRead != null) {
                    openRead.close();
                }
            } finally {
            }
        } catch (FileNotFoundException unused) {
            Slog.i(TAG, "No existing info file " + this.mFile.getBaseFile());
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read the info file:", e);
        } catch (XmlPullParserException e2) {
            Slog.e(TAG, "Failed to parse the info file:", e2);
        }
        if (persistableBundle == null) {
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        if (persistableBundle.getInt(KEY_VERSION, -1) == -1) {
            Slog.w(TAG, "Invalid info file (invalid version). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int i = persistableBundle.getInt(KEY_UID, -1);
        if (i == -1) {
            Slog.w(TAG, "Invalid info file (invalid UID). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int i2 = persistableBundle.getInt(KEY_BOOT_COUNT, -1);
        if (i2 == -1 || i2 != getBootCount()) {
            Slog.w(TAG, "Outdated info file. Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        PersistableBundle persistableBundle2 = persistableBundle.getPersistableBundle(KEY_INFO_BUNDLE);
        if (persistableBundle2 == null) {
            Slog.w(TAG, "Invalid info file (missing info). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int i3 = persistableBundle2.getInt("status", 0);
        if (i3 == 0) {
            Slog.w(TAG, "Invalid info file (invalid status). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        this.mLastStatus = i3;
        this.mLastUid = i;
        return new Bundle(persistableBundle2);
    }

    private void saveSystemUpdateInfoLocked(PersistableBundle persistableBundle, int i) {
        PersistableBundle persistableBundle2 = new PersistableBundle();
        persistableBundle2.putPersistableBundle(KEY_INFO_BUNDLE, persistableBundle);
        persistableBundle2.putInt(KEY_VERSION, 0);
        persistableBundle2.putInt(KEY_UID, i);
        persistableBundle2.putInt(KEY_BOOT_COUNT, getBootCount());
        if (writeInfoFileLocked(persistableBundle2)) {
            this.mLastUid = i;
            this.mLastStatus = persistableBundle.getInt("status");
        }
    }

    private PersistableBundle readInfoFileLocked(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return null;
            }
            if (next == 2 && TAG_INFO.equals(typedXmlPullParser.getName())) {
                return PersistableBundle.restoreFromXml(typedXmlPullParser);
            }
        }
    }

    private boolean writeInfoFileLocked(PersistableBundle persistableBundle) {
        FileOutputStream startWrite;
        FileOutputStream fileOutputStream = null;
        try {
            startWrite = this.mFile.startWrite();
        } catch (IOException | XmlPullParserException e) {
            e = e;
        }
        try {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, TAG_INFO);
            persistableBundle.saveToXml(resolveSerializer);
            resolveSerializer.endTag((String) null, TAG_INFO);
            resolveSerializer.endDocument();
            this.mFile.finishWrite(startWrite);
            return true;
        } catch (IOException | XmlPullParserException e2) {
            e = e2;
            fileOutputStream = startWrite;
            Slog.e(TAG, "Failed to save the info file:", e);
            if (fileOutputStream == null) {
                return false;
            }
            this.mFile.failWrite(fileOutputStream);
            return false;
        }
    }

    private Bundle removeInfoFileAndGetDefaultInfoBundleLocked() {
        if (this.mFile.exists()) {
            Slog.i(TAG, "Removing info file");
            this.mFile.delete();
        }
        this.mLastStatus = 0;
        this.mLastUid = -1;
        Bundle bundle = new Bundle();
        bundle.putInt("status", 0);
        return bundle;
    }

    private int getBootCount() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "boot_count", 0);
    }
}
