package com.android.server.usb;

import android.R;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.XmlResourceParser;
import android.hardware.usb.AccessoryFilter;
import android.hardware.usb.DeviceFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.Immutable;
import com.android.internal.app.IntentForwarderActivity;
import com.android.internal.content.PackageMonitor;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.PackageManagerService;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.usb.MtpNotificationManager;
import com.android.server.utils.EventLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsbProfileGroupSettingsManager {
    private static final boolean DEBUG = false;
    private static final int DUMPSYS_LOG_BUFFER = 200;
    private static final String TAG = "UsbProfileGroupSettingsManager";
    private static EventLogger sEventLogger;
    private static final File sSingleUserSettingsFile = new File("/data/system/usb_device_manager.xml");
    private final Context mContext;
    private final boolean mDisablePermissionDialogs;

    @GuardedBy({"mLock"})
    private boolean mIsWriteSettingsScheduled;
    private final Object mLock;
    private final MtpNotificationManager mMtpNotificationManager;
    private final PackageManager mPackageManager;
    MyPackageMonitor mPackageMonitor;
    private final UserHandle mParentUser;
    private final AtomicFile mSettingsFile;
    private final UsbSettingsManager mSettingsManager;
    private final UsbHandlerManager mUsbHandlerManager;
    IUsbProfileGroupSettingsManagerExt mUsbProfileGroupSettingsManagerExt;
    private final UserManager mUserManager;

    @GuardedBy({"mLock"})
    private final HashMap<DeviceFilter, UserPackage> mDevicePreferenceMap = new HashMap<>();

    @GuardedBy({"mLock"})
    private final ArrayMap<DeviceFilter, ArraySet<UserPackage>> mDevicePreferenceDeniedMap = new ArrayMap<>();

    @GuardedBy({"mLock"})
    private final HashMap<AccessoryFilter, UserPackage> mAccessoryPreferenceMap = new HashMap<>();

    @GuardedBy({"mLock"})
    private final ArrayMap<AccessoryFilter, ArraySet<UserPackage>> mAccessoryPreferenceDeniedMap = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    @Immutable
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class UserPackage {
        final String packageName;
        final UserHandle user;

        private UserPackage(String str, UserHandle userHandle) {
            this.packageName = str;
            this.user = userHandle;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof UserPackage)) {
                return false;
            }
            UserPackage userPackage = (UserPackage) obj;
            return this.user.equals(userPackage.user) && this.packageName.equals(userPackage.packageName);
        }

        public int hashCode() {
            return (this.user.hashCode() * 31) + this.packageName.hashCode();
        }

        public String toString() {
            return this.user.getIdentifier() + SliceClientPermissions.SliceAuthority.DELIMITER + this.packageName;
        }

        public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
            long start = dualDumpOutputStream.start(str, j);
            dualDumpOutputStream.write("user_id", 1120986464257L, this.user.getIdentifier());
            dualDumpOutputStream.write("package_name", 1138166333442L, this.packageName);
            dualDumpOutputStream.end(start);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class MyPackageMonitor extends PackageMonitor {
        private MyPackageMonitor() {
        }

        public void onPackageAdded(String str, int i) {
            if (UsbProfileGroupSettingsManager.this.mUserManager.isSameProfileGroup(UsbProfileGroupSettingsManager.this.mParentUser.getIdentifier(), UserHandle.getUserId(i))) {
                UsbProfileGroupSettingsManager.this.handlePackageAdded(new UserPackage(str, UserHandle.getUserHandleForUid(i)));
            }
        }

        public void onPackageRemoved(String str, int i) {
            if (UsbProfileGroupSettingsManager.this.mUserManager.isSameProfileGroup(UsbProfileGroupSettingsManager.this.mParentUser.getIdentifier(), UserHandle.getUserId(i))) {
                UsbProfileGroupSettingsManager.this.clearDefaults(str, UserHandle.getUserHandleForUid(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbProfileGroupSettingsManager(Context context, UserHandle userHandle, UsbSettingsManager usbSettingsManager, UsbHandlerManager usbHandlerManager) {
        Object obj = new Object();
        this.mLock = obj;
        this.mUsbProfileGroupSettingsManagerExt = (IUsbProfileGroupSettingsManagerExt) ExtLoader.type(IUsbProfileGroupSettingsManagerExt.class).create();
        this.mPackageMonitor = new MyPackageMonitor();
        try {
            Context createPackageContextAsUser = context.createPackageContextAsUser(PackageManagerService.PLATFORM_PACKAGE_NAME, 0, userHandle);
            this.mContext = context;
            this.mPackageManager = context.getPackageManager();
            this.mSettingsManager = usbSettingsManager;
            this.mUserManager = (UserManager) context.getSystemService("user");
            this.mParentUser = userHandle;
            this.mSettingsFile = new AtomicFile(new File(Environment.getUserSystemDirectory(userHandle.getIdentifier()), "usb_device_manager.xml"), "usb-state");
            this.mDisablePermissionDialogs = context.getResources().getBoolean(R.bool.use_lock_pattern_drawable);
            synchronized (obj) {
                if (UserHandle.SYSTEM.equals(userHandle)) {
                    upgradeSingleUserLocked();
                }
                readSettingsLocked();
            }
            this.mPackageMonitor.getWrapper().getExtImpl().register(context, (Looper) null, UserHandle.ALL, true, new int[]{2, 7});
            this.mMtpNotificationManager = new MtpNotificationManager(createPackageContextAsUser, new MtpNotificationManager.OnOpenInAppListener() { // from class: com.android.server.usb.UsbProfileGroupSettingsManager$$ExternalSyntheticLambda0
                @Override // com.android.server.usb.MtpNotificationManager.OnOpenInAppListener
                public final void onOpenInApp(UsbDevice usbDevice) {
                    UsbProfileGroupSettingsManager.this.lambda$new$0(usbDevice);
                }
            });
            this.mUsbHandlerManager = usbHandlerManager;
            sEventLogger = new EventLogger(200, "UsbProfileGroupSettingsManager activity");
        } catch (PackageManager.NameNotFoundException unused) {
            throw new RuntimeException("Missing android package");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(UsbDevice usbDevice) {
        resolveActivity(createDeviceAttachedIntent(usbDevice), usbDevice, false);
    }

    public void unregisterReceivers() {
        this.mPackageMonitor.unregister();
        this.mMtpNotificationManager.unregister();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeUser(UserHandle userHandle) {
        synchronized (this.mLock) {
            Iterator<Map.Entry<DeviceFilter, UserPackage>> it = this.mDevicePreferenceMap.entrySet().iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (it.next().getValue().user.equals(userHandle)) {
                    it.remove();
                    z = true;
                }
            }
            Iterator<Map.Entry<AccessoryFilter, UserPackage>> it2 = this.mAccessoryPreferenceMap.entrySet().iterator();
            while (it2.hasNext()) {
                if (it2.next().getValue().user.equals(userHandle)) {
                    it2.remove();
                    z = true;
                }
            }
            int size = this.mDevicePreferenceDeniedMap.size();
            for (int i = 0; i < size; i++) {
                ArraySet<UserPackage> valueAt = this.mDevicePreferenceDeniedMap.valueAt(i);
                for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                    if (valueAt.valueAt(size2).user.equals(userHandle)) {
                        valueAt.removeAt(size2);
                        z = true;
                    }
                }
            }
            int size3 = this.mAccessoryPreferenceDeniedMap.size();
            for (int i2 = 0; i2 < size3; i2++) {
                ArraySet<UserPackage> valueAt2 = this.mAccessoryPreferenceDeniedMap.valueAt(i2);
                for (int size4 = valueAt2.size() - 1; size4 >= 0; size4--) {
                    if (valueAt2.valueAt(size4).user.equals(userHandle)) {
                        valueAt2.removeAt(size4);
                        z = true;
                    }
                }
            }
            if (z) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    private void readPreference(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        UserHandle userHandle = this.mParentUser;
        int attributeCount = xmlPullParser.getAttributeCount();
        String str = null;
        for (int i = 0; i < attributeCount; i++) {
            if ("package".equals(xmlPullParser.getAttributeName(i))) {
                str = xmlPullParser.getAttributeValue(i);
            }
            if ("user".equals(xmlPullParser.getAttributeName(i))) {
                userHandle = this.mUserManager.getUserForSerialNumber(Integer.parseInt(xmlPullParser.getAttributeValue(i)));
            }
        }
        XmlUtils.nextElement(xmlPullParser);
        if ("usb-device".equals(xmlPullParser.getName())) {
            DeviceFilter read = DeviceFilter.read(xmlPullParser);
            if (userHandle != null) {
                this.mDevicePreferenceMap.put(read, new UserPackage(str, userHandle));
            }
        } else if ("usb-accessory".equals(xmlPullParser.getName())) {
            AccessoryFilter read2 = AccessoryFilter.read(xmlPullParser);
            if (userHandle != null) {
                this.mAccessoryPreferenceMap.put(read2, new UserPackage(str, userHandle));
            }
        }
        XmlUtils.nextElement(xmlPullParser);
    }

    private void readPreferenceDeniedList(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        int depth = xmlPullParser.getDepth();
        if (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            if ("usb-device".equals(xmlPullParser.getName())) {
                DeviceFilter read = DeviceFilter.read(xmlPullParser);
                while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
                    if ("user-package".equals(xmlPullParser.getName())) {
                        try {
                            int readIntAttribute = XmlUtils.readIntAttribute(xmlPullParser, "user");
                            String readStringAttribute = XmlUtils.readStringAttribute(xmlPullParser, "package");
                            if (readStringAttribute == null) {
                                Slog.e(TAG, "Unable to parse package name");
                            }
                            ArraySet<UserPackage> arraySet = this.mDevicePreferenceDeniedMap.get(read);
                            if (arraySet == null) {
                                arraySet = new ArraySet<>();
                                this.mDevicePreferenceDeniedMap.put(read, arraySet);
                            }
                            arraySet.add(new UserPackage(readStringAttribute, UserHandle.of(readIntAttribute)));
                        } catch (ProtocolException e) {
                            Slog.e(TAG, "Unable to parse user id", e);
                        }
                    }
                }
            } else if ("usb-accessory".equals(xmlPullParser.getName())) {
                AccessoryFilter read2 = AccessoryFilter.read(xmlPullParser);
                while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
                    if ("user-package".equals(xmlPullParser.getName())) {
                        try {
                            int readIntAttribute2 = XmlUtils.readIntAttribute(xmlPullParser, "user");
                            String readStringAttribute2 = XmlUtils.readStringAttribute(xmlPullParser, "package");
                            if (readStringAttribute2 == null) {
                                Slog.e(TAG, "Unable to parse package name");
                            }
                            ArraySet<UserPackage> arraySet2 = this.mAccessoryPreferenceDeniedMap.get(read2);
                            if (arraySet2 == null) {
                                arraySet2 = new ArraySet<>();
                                this.mAccessoryPreferenceDeniedMap.put(read2, arraySet2);
                            }
                            arraySet2.add(new UserPackage(readStringAttribute2, UserHandle.of(readIntAttribute2)));
                        } catch (ProtocolException e2) {
                            Slog.e(TAG, "Unable to parse user id", e2);
                        }
                    }
                }
            }
            while (xmlPullParser.getDepth() > depth) {
                xmlPullParser.nextTag();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @GuardedBy({"mLock"})
    private void upgradeSingleUserLocked() {
        int eventType;
        File file = sSingleUserSettingsFile;
        if (!file.exists()) {
            return;
        }
        this.mDevicePreferenceMap.clear();
        this.mAccessoryPreferenceMap.clear();
        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                FileInputStream fileInputStream3 = new FileInputStream(file);
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream3);
                    XmlUtils.nextElement(resolvePullParser);
                    while (true) {
                        eventType = resolvePullParser.getEventType();
                        if (eventType == 1) {
                            break;
                        } else if ("preference".equals(resolvePullParser.getName())) {
                            readPreference(resolvePullParser);
                        } else {
                            XmlUtils.nextElement(resolvePullParser);
                        }
                    }
                    IoUtils.closeQuietly(fileInputStream3);
                    fileInputStream = eventType;
                } catch (IOException | XmlPullParserException e) {
                    e = e;
                    fileInputStream2 = fileInputStream3;
                    Log.wtf(TAG, "Failed to read single-user settings", e);
                    IoUtils.closeQuietly(fileInputStream2);
                    fileInputStream = fileInputStream2;
                    scheduleWriteSettingsLocked();
                    sSingleUserSettingsFile.delete();
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream3;
                    IoUtils.closeQuietly(fileInputStream);
                    throw th;
                }
            } catch (IOException | XmlPullParserException e2) {
                e = e2;
            }
            scheduleWriteSettingsLocked();
            sSingleUserSettingsFile.delete();
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @GuardedBy({"mLock"})
    private void readSettingsLocked() {
        this.mDevicePreferenceMap.clear();
        this.mAccessoryPreferenceMap.clear();
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = this.mSettingsFile.openRead();
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                XmlUtils.nextElement(resolvePullParser);
                while (resolvePullParser.getEventType() != 1) {
                    String name = resolvePullParser.getName();
                    if ("preference".equals(name)) {
                        readPreference(resolvePullParser);
                    } else if ("preference-denied-list".equals(name)) {
                        readPreferenceDeniedList(resolvePullParser);
                    } else {
                        XmlUtils.nextElement(resolvePullParser);
                    }
                }
            } catch (FileNotFoundException unused) {
            } catch (Exception e) {
                Slog.e(TAG, "error reading settings file, deleting to start fresh", e);
                this.mSettingsFile.delete();
            }
        } finally {
            IoUtils.closeQuietly(fileInputStream);
        }
    }

    @GuardedBy({"mLock"})
    private void scheduleWriteSettingsLocked() {
        if (this.mIsWriteSettingsScheduled) {
            return;
        }
        this.mIsWriteSettingsScheduled = true;
        AsyncTask.execute(new Runnable() { // from class: com.android.server.usb.UsbProfileGroupSettingsManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                UsbProfileGroupSettingsManager.this.lambda$scheduleWriteSettingsLocked$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleWriteSettingsLocked$1() {
        FileOutputStream fileOutputStream;
        IOException e;
        synchronized (this.mLock) {
            try {
                fileOutputStream = this.mSettingsFile.startWrite();
            } catch (IOException e2) {
                fileOutputStream = null;
                e = e2;
            }
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(fileOutputStream);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                resolveSerializer.startTag((String) null, "settings");
                for (DeviceFilter deviceFilter : this.mDevicePreferenceMap.keySet()) {
                    resolveSerializer.startTag((String) null, "preference");
                    resolveSerializer.attribute((String) null, "package", this.mDevicePreferenceMap.get(deviceFilter).packageName);
                    resolveSerializer.attribute((String) null, "user", String.valueOf(getSerial(this.mDevicePreferenceMap.get(deviceFilter).user)));
                    deviceFilter.write(resolveSerializer);
                    resolveSerializer.endTag((String) null, "preference");
                }
                for (AccessoryFilter accessoryFilter : this.mAccessoryPreferenceMap.keySet()) {
                    resolveSerializer.startTag((String) null, "preference");
                    resolveSerializer.attribute((String) null, "package", this.mAccessoryPreferenceMap.get(accessoryFilter).packageName);
                    resolveSerializer.attribute((String) null, "user", String.valueOf(getSerial(this.mAccessoryPreferenceMap.get(accessoryFilter).user)));
                    accessoryFilter.write(resolveSerializer);
                    resolveSerializer.endTag((String) null, "preference");
                }
                int size = this.mDevicePreferenceDeniedMap.size();
                for (int i = 0; i < size; i++) {
                    DeviceFilter keyAt = this.mDevicePreferenceDeniedMap.keyAt(i);
                    ArraySet<UserPackage> valueAt = this.mDevicePreferenceDeniedMap.valueAt(i);
                    resolveSerializer.startTag((String) null, "preference-denied-list");
                    keyAt.write(resolveSerializer);
                    int size2 = valueAt.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        UserPackage valueAt2 = valueAt.valueAt(i2);
                        resolveSerializer.startTag((String) null, "user-package");
                        resolveSerializer.attribute((String) null, "user", String.valueOf(getSerial(valueAt2.user)));
                        resolveSerializer.attribute((String) null, "package", valueAt2.packageName);
                        resolveSerializer.endTag((String) null, "user-package");
                    }
                    resolveSerializer.endTag((String) null, "preference-denied-list");
                }
                int size3 = this.mAccessoryPreferenceDeniedMap.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    AccessoryFilter keyAt2 = this.mAccessoryPreferenceDeniedMap.keyAt(i3);
                    ArraySet<UserPackage> valueAt3 = this.mAccessoryPreferenceDeniedMap.valueAt(i3);
                    resolveSerializer.startTag((String) null, "preference-denied-list");
                    keyAt2.write(resolveSerializer);
                    int size4 = valueAt3.size();
                    for (int i4 = 0; i4 < size4; i4++) {
                        UserPackage valueAt4 = valueAt3.valueAt(i4);
                        resolveSerializer.startTag((String) null, "user-package");
                        resolveSerializer.attribute((String) null, "user", String.valueOf(getSerial(valueAt4.user)));
                        resolveSerializer.attribute((String) null, "package", valueAt4.packageName);
                        resolveSerializer.endTag((String) null, "user-package");
                    }
                    resolveSerializer.endTag((String) null, "preference-denied-list");
                }
                resolveSerializer.endTag((String) null, "settings");
                resolveSerializer.endDocument();
                this.mSettingsFile.finishWrite(fileOutputStream);
            } catch (IOException e3) {
                e = e3;
                Slog.e(TAG, "Failed to write settings", e);
                if (fileOutputStream != null) {
                    this.mSettingsFile.failWrite(fileOutputStream);
                }
                this.mIsWriteSettingsScheduled = false;
            }
            this.mIsWriteSettingsScheduled = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<DeviceFilter> getDeviceFilters(PackageManager packageManager, ResolveInfo resolveInfo) {
        ArrayList<DeviceFilter> arrayList;
        XmlResourceParser xmlResourceParser = null;
        ArrayList<DeviceFilter> arrayList2 = null;
        xmlResourceParser = null;
        try {
            try {
                XmlResourceParser loadXmlMetaData = resolveInfo.activityInfo.loadXmlMetaData(packageManager, "android.hardware.usb.action.USB_DEVICE_ATTACHED");
                try {
                    if (loadXmlMetaData == null) {
                        Slog.w(TAG, "no meta-data for " + resolveInfo);
                        if (loadXmlMetaData != null) {
                            loadXmlMetaData.close();
                        }
                        return null;
                    }
                    XmlUtils.nextElement(loadXmlMetaData);
                    while (loadXmlMetaData.getEventType() != 1) {
                        if ("usb-device".equals(loadXmlMetaData.getName())) {
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList<>(1);
                            }
                            arrayList2.add(DeviceFilter.read(loadXmlMetaData));
                        }
                        XmlUtils.nextElement(loadXmlMetaData);
                    }
                    loadXmlMetaData.close();
                    return arrayList2;
                } catch (Exception e) {
                    e = e;
                    xmlResourceParser = loadXmlMetaData;
                    arrayList = null;
                    Slog.w(TAG, "Unable to load component info " + resolveInfo.toString(), e);
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    return arrayList;
                } catch (Throwable th) {
                    th = th;
                    xmlResourceParser = loadXmlMetaData;
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
            arrayList = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<AccessoryFilter> getAccessoryFilters(PackageManager packageManager, ResolveInfo resolveInfo) {
        ArrayList<AccessoryFilter> arrayList;
        XmlResourceParser xmlResourceParser = null;
        ArrayList<AccessoryFilter> arrayList2 = null;
        xmlResourceParser = null;
        try {
            try {
                XmlResourceParser loadXmlMetaData = resolveInfo.activityInfo.loadXmlMetaData(packageManager, "android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
                try {
                    if (loadXmlMetaData == null) {
                        Slog.w(TAG, "no meta-data for " + resolveInfo);
                        if (loadXmlMetaData != null) {
                            loadXmlMetaData.close();
                        }
                        return null;
                    }
                    XmlUtils.nextElement(loadXmlMetaData);
                    while (loadXmlMetaData.getEventType() != 1) {
                        if ("usb-accessory".equals(loadXmlMetaData.getName())) {
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList<>(1);
                            }
                            arrayList2.add(AccessoryFilter.read(loadXmlMetaData));
                        }
                        XmlUtils.nextElement(loadXmlMetaData);
                    }
                    loadXmlMetaData.close();
                    return arrayList2;
                } catch (Exception e) {
                    e = e;
                    xmlResourceParser = loadXmlMetaData;
                    arrayList = null;
                    Slog.w(TAG, "Unable to load component info " + resolveInfo.toString(), e);
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    return arrayList;
                } catch (Throwable th) {
                    th = th;
                    xmlResourceParser = loadXmlMetaData;
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
            arrayList = null;
        }
    }

    private boolean packageMatchesLocked(ResolveInfo resolveInfo, UsbDevice usbDevice, UsbAccessory usbAccessory) {
        ArrayList<AccessoryFilter> accessoryFilters;
        ArrayList<DeviceFilter> deviceFilters;
        if (isForwardMatch(resolveInfo)) {
            return true;
        }
        if (usbDevice != null && (deviceFilters = getDeviceFilters(this.mPackageManager, resolveInfo)) != null) {
            int size = deviceFilters.size();
            for (int i = 0; i < size; i++) {
                if (deviceFilters.get(i).matches(usbDevice)) {
                    return true;
                }
            }
        }
        if (usbAccessory != null && (accessoryFilters = getAccessoryFilters(this.mPackageManager, resolveInfo)) != null) {
            int size2 = accessoryFilters.size();
            for (int i2 = 0; i2 < size2; i2++) {
                if (accessoryFilters.get(i2).matches(usbAccessory)) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<ResolveInfo> queryIntentActivitiesForAllProfiles(Intent intent) {
        List enabledProfiles = this.mUserManager.getEnabledProfiles(this.mParentUser.getIdentifier());
        ArrayList<ResolveInfo> arrayList = new ArrayList<>();
        int size = enabledProfiles.size();
        for (int i = 0; i < size; i++) {
            arrayList.addAll(this.mSettingsManager.getSettingsForUser(((UserInfo) enabledProfiles.get(i)).id).queryIntentActivities(intent));
        }
        return arrayList;
    }

    private boolean isForwardMatch(ResolveInfo resolveInfo) {
        return resolveInfo.getComponentInfo().name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE);
    }

    private ArrayList<ResolveInfo> preferHighPriority(ArrayList<ResolveInfo> arrayList) {
        SparseArray sparseArray = new SparseArray();
        SparseIntArray sparseIntArray = new SparseIntArray();
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = arrayList.get(i);
            if (isForwardMatch(resolveInfo)) {
                arrayList2.add(resolveInfo);
            } else {
                if (sparseIntArray.indexOfKey(resolveInfo.targetUserId) < 0) {
                    sparseIntArray.put(resolveInfo.targetUserId, Integer.MIN_VALUE);
                    sparseArray.put(resolveInfo.targetUserId, new ArrayList());
                }
                int i2 = sparseIntArray.get(resolveInfo.targetUserId);
                ArrayList arrayList3 = (ArrayList) sparseArray.get(resolveInfo.targetUserId);
                int i3 = resolveInfo.priority;
                if (i3 == i2) {
                    arrayList3.add(resolveInfo);
                } else if (i3 > i2) {
                    sparseIntArray.put(resolveInfo.targetUserId, i3);
                    arrayList3.clear();
                    arrayList3.add(resolveInfo);
                }
            }
        }
        ArrayList<ResolveInfo> arrayList4 = new ArrayList<>(arrayList2);
        int size2 = sparseArray.size();
        for (int i4 = 0; i4 < size2; i4++) {
            arrayList4.addAll((Collection) sparseArray.valueAt(i4));
        }
        return arrayList4;
    }

    private ArrayList<ResolveInfo> removeForwardIntentIfNotNeeded(ArrayList<ResolveInfo> arrayList) {
        int size = arrayList.size();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            ResolveInfo resolveInfo = arrayList.get(i3);
            if (!isForwardMatch(resolveInfo)) {
                if (UserHandle.getUserHandleForUid(resolveInfo.activityInfo.applicationInfo.uid).equals(this.mParentUser)) {
                    i++;
                } else {
                    i2++;
                }
            }
        }
        if (i != 0 && i2 != 0) {
            return arrayList;
        }
        ArrayList<ResolveInfo> arrayList2 = new ArrayList<>(i + i2);
        for (int i4 = 0; i4 < size; i4++) {
            ResolveInfo resolveInfo2 = arrayList.get(i4);
            if (!isForwardMatch(resolveInfo2)) {
                arrayList2.add(resolveInfo2);
            }
        }
        return arrayList2;
    }

    private ArrayList<ResolveInfo> getDeviceMatchesLocked(UsbDevice usbDevice, Intent intent) {
        ArrayList<ResolveInfo> arrayList = new ArrayList<>();
        ArrayList<ResolveInfo> queryIntentActivitiesForAllProfiles = queryIntentActivitiesForAllProfiles(intent);
        int size = queryIntentActivitiesForAllProfiles.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = queryIntentActivitiesForAllProfiles.get(i);
            if (packageMatchesLocked(resolveInfo, usbDevice, null)) {
                arrayList.add(resolveInfo);
            }
        }
        return removeForwardIntentIfNotNeeded(preferHighPriority(arrayList));
    }

    private ArrayList<ResolveInfo> getAccessoryMatchesLocked(UsbAccessory usbAccessory, Intent intent) {
        ArrayList<ResolveInfo> arrayList = new ArrayList<>();
        ArrayList<ResolveInfo> queryIntentActivitiesForAllProfiles = queryIntentActivitiesForAllProfiles(intent);
        int size = queryIntentActivitiesForAllProfiles.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = queryIntentActivitiesForAllProfiles.get(i);
            if (packageMatchesLocked(resolveInfo, null, usbAccessory)) {
                arrayList.add(resolveInfo);
            }
        }
        return removeForwardIntentIfNotNeeded(preferHighPriority(arrayList));
    }

    public void deviceAttached(UsbDevice usbDevice) {
        Intent createDeviceAttachedIntent = createDeviceAttachedIntent(usbDevice);
        this.mContext.sendBroadcastAsUser(createDeviceAttachedIntent, UserHandle.ALL);
        resolveActivity(createDeviceAttachedIntent, usbDevice, true);
    }

    private void resolveActivity(Intent intent, UsbDevice usbDevice, boolean z) {
        ArrayList<ResolveInfo> deviceMatchesLocked;
        ActivityInfo defaultActivityLocked;
        synchronized (this.mLock) {
            deviceMatchesLocked = getDeviceMatchesLocked(usbDevice, intent);
            defaultActivityLocked = getDefaultActivityLocked(deviceMatchesLocked, this.mDevicePreferenceMap.get(new DeviceFilter(usbDevice)));
        }
        if (z && MtpNotificationManager.shouldShowNotification(this.mPackageManager, usbDevice) && defaultActivityLocked == null) {
            this.mMtpNotificationManager.showNotification(usbDevice);
        } else {
            if (this.mUsbProfileGroupSettingsManagerExt.resolveActivityForOtgTest()) {
                return;
            }
            resolveActivity(intent, deviceMatchesLocked, defaultActivityLocked, usbDevice, null);
        }
    }

    public void deviceAttachedForFixedHandler(UsbDevice usbDevice, ComponentName componentName) {
        Intent createDeviceAttachedIntent = createDeviceAttachedIntent(usbDevice);
        this.mContext.sendBroadcastAsUser(createDeviceAttachedIntent, UserHandle.of(ActivityManager.getCurrentUser()));
        try {
            ApplicationInfo applicationInfoAsUser = this.mPackageManager.getApplicationInfoAsUser(componentName.getPackageName(), 0, this.mParentUser.getIdentifier());
            this.mSettingsManager.mUsbService.getPermissionsForUser(UserHandle.getUserId(applicationInfoAsUser.uid)).grantDevicePermission(usbDevice, applicationInfoAsUser.uid);
            Intent intent = new Intent(createDeviceAttachedIntent);
            intent.setComponent(componentName);
            try {
                this.mContext.startActivityAsUser(intent, this.mParentUser);
            } catch (ActivityNotFoundException unused) {
                Slog.e(TAG, "unable to start activity " + intent);
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            Slog.e(TAG, "Default USB handling package (" + componentName.getPackageName() + ") not found  for user " + this.mParentUser);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void usbDeviceRemoved(UsbDevice usbDevice) {
        this.mMtpNotificationManager.hideNotification(usbDevice.getDeviceId());
    }

    public void accessoryAttached(UsbAccessory usbAccessory) {
        ArrayList<ResolveInfo> accessoryMatchesLocked;
        ActivityInfo defaultActivityLocked;
        Intent intent = new Intent("android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
        intent.putExtra("accessory", usbAccessory);
        intent.addFlags(285212672);
        synchronized (this.mLock) {
            accessoryMatchesLocked = getAccessoryMatchesLocked(usbAccessory, intent);
            defaultActivityLocked = getDefaultActivityLocked(accessoryMatchesLocked, this.mAccessoryPreferenceMap.get(new AccessoryFilter(usbAccessory)));
        }
        sEventLogger.enqueue(new EventLogger.StringEvent("accessoryAttached: " + intent));
        resolveActivity(intent, accessoryMatchesLocked, defaultActivityLocked, null, usbAccessory);
    }

    private void resolveActivity(Intent intent, ArrayList<ResolveInfo> arrayList, ActivityInfo activityInfo, UsbDevice usbDevice, UsbAccessory usbAccessory) {
        ArraySet<UserPackage> arraySet;
        if (usbDevice != null) {
            arraySet = this.mDevicePreferenceDeniedMap.get(new DeviceFilter(usbDevice));
        } else {
            arraySet = usbAccessory != null ? this.mAccessoryPreferenceDeniedMap.get(new AccessoryFilter(usbAccessory)) : null;
        }
        if (arraySet != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ActivityInfo activityInfo2 = arrayList.get(size).activityInfo;
                if (arraySet.contains(new UserPackage(activityInfo2.packageName, UserHandle.getUserHandleForUid(activityInfo2.applicationInfo.uid)))) {
                    arrayList.remove(size);
                }
            }
        }
        if (arrayList.size() == 0) {
            if (usbAccessory != null) {
                this.mUsbHandlerManager.showUsbAccessoryUriActivity(usbAccessory, this.mParentUser);
                return;
            }
            return;
        }
        if (activityInfo != null) {
            UsbUserPermissionManager permissionsForUser = this.mSettingsManager.mUsbService.getPermissionsForUser(UserHandle.getUserId(activityInfo.applicationInfo.uid));
            if (usbDevice != null) {
                permissionsForUser.grantDevicePermission(usbDevice, activityInfo.applicationInfo.uid);
            } else if (usbAccessory != null) {
                permissionsForUser.grantAccessoryPermission(usbAccessory, activityInfo.applicationInfo.uid);
            }
            try {
                intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                this.mContext.startActivityAsUser(intent, UserHandle.getUserHandleForUid(activityInfo.applicationInfo.uid));
                return;
            } catch (ActivityNotFoundException e) {
                Slog.e(TAG, "startActivity failed", e);
                return;
            }
        }
        if (arrayList.size() == 1) {
            this.mUsbHandlerManager.confirmUsbHandler(arrayList.get(0), usbDevice, usbAccessory);
        } else {
            this.mUsbHandlerManager.selectUsbHandler(arrayList, this.mParentUser, intent);
        }
    }

    private ActivityInfo getDefaultActivityLocked(ArrayList<ResolveInfo> arrayList, UserPackage userPackage) {
        ActivityInfo activityInfo;
        if (userPackage != null) {
            Iterator<ResolveInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                ResolveInfo next = it.next();
                ActivityInfo activityInfo2 = next.activityInfo;
                if (activityInfo2 != null && userPackage.equals(new UserPackage(activityInfo2.packageName, UserHandle.getUserHandleForUid(activityInfo2.applicationInfo.uid)))) {
                    return next.activityInfo;
                }
            }
        }
        if (arrayList.size() == 1 && (activityInfo = arrayList.get(0).activityInfo) != null) {
            if (this.mDisablePermissionDialogs) {
                return activityInfo;
            }
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            if (applicationInfo != null && (applicationInfo.flags & 1) != 0) {
                return activityInfo;
            }
        }
        return null;
    }

    @GuardedBy({"mLock"})
    private boolean clearCompatibleMatchesLocked(UserPackage userPackage, DeviceFilter deviceFilter) {
        ArrayList arrayList = new ArrayList();
        for (DeviceFilter deviceFilter2 : this.mDevicePreferenceMap.keySet()) {
            if (deviceFilter.contains(deviceFilter2) && !this.mDevicePreferenceMap.get(deviceFilter2).equals(userPackage)) {
                arrayList.add(deviceFilter2);
            }
        }
        if (!arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mDevicePreferenceMap.remove((DeviceFilter) it.next());
            }
        }
        return !arrayList.isEmpty();
    }

    @GuardedBy({"mLock"})
    private boolean clearCompatibleMatchesLocked(UserPackage userPackage, AccessoryFilter accessoryFilter) {
        ArrayList arrayList = new ArrayList();
        for (AccessoryFilter accessoryFilter2 : this.mAccessoryPreferenceMap.keySet()) {
            if (accessoryFilter.contains(accessoryFilter2) && !this.mAccessoryPreferenceMap.get(accessoryFilter2).equals(userPackage)) {
                arrayList.add(accessoryFilter2);
            }
        }
        if (!arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mAccessoryPreferenceMap.remove((AccessoryFilter) it.next());
            }
        }
        return !arrayList.isEmpty();
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x006b, code lost:
    
        if (0 == 0) goto L30;
     */
    @GuardedBy({"mLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handlePackageAddedLocked(UserPackage userPackage, ActivityInfo activityInfo, String str) {
        XmlResourceParser xmlResourceParser = null;
        boolean z = false;
        try {
            try {
                xmlResourceParser = activityInfo.loadXmlMetaData(this.mPackageManager, str);
            } catch (Exception e) {
                Slog.w(TAG, "Unable to load component info " + activityInfo.toString(), e);
            }
            if (xmlResourceParser == null) {
                return false;
            }
            XmlUtils.nextElement(xmlResourceParser);
            while (xmlResourceParser.getEventType() != 1) {
                String name = xmlResourceParser.getName();
                if ("usb-device".equals(name)) {
                    if (clearCompatibleMatchesLocked(userPackage, DeviceFilter.read(xmlResourceParser))) {
                        z = true;
                        XmlUtils.nextElement(xmlResourceParser);
                    } else {
                        XmlUtils.nextElement(xmlResourceParser);
                    }
                } else {
                    if ("usb-accessory".equals(name)) {
                        if (!clearCompatibleMatchesLocked(userPackage, AccessoryFilter.read(xmlResourceParser))) {
                        }
                        z = true;
                    }
                    XmlUtils.nextElement(xmlResourceParser);
                }
            }
            xmlResourceParser.close();
            return z;
        } finally {
            if (0 != 0) {
                xmlResourceParser.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageAdded(UserPackage userPackage) {
        synchronized (this.mLock) {
            try {
                try {
                    ActivityInfo[] activityInfoArr = this.mPackageManager.getPackageInfoAsUser(userPackage.packageName, 129, userPackage.user.getIdentifier()).activities;
                    if (activityInfoArr == null) {
                        return;
                    }
                    boolean z = false;
                    for (int i = 0; i < activityInfoArr.length; i++) {
                        if (handlePackageAddedLocked(userPackage, activityInfoArr[i], "android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                            z = true;
                        }
                        if (handlePackageAddedLocked(userPackage, activityInfoArr[i], "android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                            z = true;
                        }
                    }
                    if (z) {
                        scheduleWriteSettingsLocked();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Slog.e(TAG, "handlePackageUpdate could not find package " + userPackage, e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int getSerial(UserHandle userHandle) {
        return this.mUserManager.getUserSerialNumber(userHandle.getIdentifier());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDevicePackage(UsbDevice usbDevice, String str, UserHandle userHandle) {
        DeviceFilter deviceFilter = new DeviceFilter(usbDevice);
        synchronized (this.mLock) {
            boolean z = true;
            if (str == null) {
                if (this.mDevicePreferenceMap.remove(deviceFilter) == null) {
                    z = false;
                }
            } else {
                UserPackage userPackage = new UserPackage(str, userHandle);
                z = true ^ userPackage.equals(this.mDevicePreferenceMap.get(deviceFilter));
                if (z) {
                    this.mDevicePreferenceMap.put(deviceFilter, userPackage);
                }
            }
            if (z) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addDevicePackagesToDenied(UsbDevice usbDevice, String[] strArr, UserHandle userHandle) {
        ArraySet<UserPackage> arraySet;
        if (strArr.length == 0) {
            return;
        }
        DeviceFilter deviceFilter = new DeviceFilter(usbDevice);
        synchronized (this.mLock) {
            if (this.mDevicePreferenceDeniedMap.containsKey(deviceFilter)) {
                arraySet = this.mDevicePreferenceDeniedMap.get(deviceFilter);
            } else {
                ArraySet<UserPackage> arraySet2 = new ArraySet<>();
                this.mDevicePreferenceDeniedMap.put(deviceFilter, arraySet2);
                arraySet = arraySet2;
            }
            boolean z = false;
            for (String str : strArr) {
                UserPackage userPackage = new UserPackage(str, userHandle);
                if (!arraySet.contains(userPackage)) {
                    arraySet.add(userPackage);
                    z = true;
                }
            }
            if (z) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAccessoryPackagesToDenied(UsbAccessory usbAccessory, String[] strArr, UserHandle userHandle) {
        ArraySet<UserPackage> arraySet;
        if (strArr.length == 0) {
            return;
        }
        AccessoryFilter accessoryFilter = new AccessoryFilter(usbAccessory);
        synchronized (this.mLock) {
            if (this.mAccessoryPreferenceDeniedMap.containsKey(accessoryFilter)) {
                arraySet = this.mAccessoryPreferenceDeniedMap.get(accessoryFilter);
            } else {
                ArraySet<UserPackage> arraySet2 = new ArraySet<>();
                this.mAccessoryPreferenceDeniedMap.put(accessoryFilter, arraySet2);
                arraySet = arraySet2;
            }
            boolean z = false;
            for (String str : strArr) {
                UserPackage userPackage = new UserPackage(str, userHandle);
                if (!arraySet.contains(userPackage)) {
                    arraySet.add(userPackage);
                    z = true;
                }
            }
            if (z) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeDevicePackagesFromDenied(UsbDevice usbDevice, String[] strArr, UserHandle userHandle) {
        DeviceFilter deviceFilter = new DeviceFilter(usbDevice);
        synchronized (this.mLock) {
            ArraySet<UserPackage> arraySet = this.mDevicePreferenceDeniedMap.get(deviceFilter);
            if (arraySet != null) {
                int length = strArr.length;
                int i = 0;
                boolean z = false;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    UserPackage userPackage = new UserPackage(strArr[i], userHandle);
                    if (arraySet.contains(userPackage)) {
                        arraySet.remove(userPackage);
                        if (arraySet.size() == 0) {
                            this.mDevicePreferenceDeniedMap.remove(deviceFilter);
                            z = true;
                            break;
                        }
                        z = true;
                    }
                    i++;
                }
                if (z) {
                    scheduleWriteSettingsLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAccessoryPackagesFromDenied(UsbAccessory usbAccessory, String[] strArr, UserHandle userHandle) {
        AccessoryFilter accessoryFilter = new AccessoryFilter(usbAccessory);
        synchronized (this.mLock) {
            ArraySet<UserPackage> arraySet = this.mAccessoryPreferenceDeniedMap.get(accessoryFilter);
            if (arraySet != null) {
                int length = strArr.length;
                int i = 0;
                boolean z = false;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    UserPackage userPackage = new UserPackage(strArr[i], userHandle);
                    if (arraySet.contains(userPackage)) {
                        arraySet.remove(userPackage);
                        if (arraySet.size() == 0) {
                            this.mAccessoryPreferenceDeniedMap.remove(accessoryFilter);
                            z = true;
                            break;
                        }
                        z = true;
                    }
                    i++;
                }
                if (z) {
                    scheduleWriteSettingsLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessoryPackage(UsbAccessory usbAccessory, String str, UserHandle userHandle) {
        AccessoryFilter accessoryFilter = new AccessoryFilter(usbAccessory);
        synchronized (this.mLock) {
            boolean z = true;
            if (str == null) {
                if (this.mAccessoryPreferenceMap.remove(accessoryFilter) == null) {
                    z = false;
                }
            } else {
                UserPackage userPackage = new UserPackage(str, userHandle);
                z = true ^ userPackage.equals(this.mAccessoryPreferenceMap.get(accessoryFilter));
                if (z) {
                    this.mAccessoryPreferenceMap.put(accessoryFilter, userPackage);
                }
            }
            if (z) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasDefaults(String str, UserHandle userHandle) {
        UserPackage userPackage = new UserPackage(str, userHandle);
        synchronized (this.mLock) {
            if (this.mDevicePreferenceMap.values().contains(userPackage)) {
                return true;
            }
            return this.mAccessoryPreferenceMap.values().contains(userPackage);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearDefaults(String str, UserHandle userHandle) {
        UserPackage userPackage = new UserPackage(str, userHandle);
        synchronized (this.mLock) {
            if (clearPackageDefaultsLocked(userPackage)) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    private boolean clearPackageDefaultsLocked(UserPackage userPackage) {
        boolean z;
        synchronized (this.mLock) {
            if (this.mDevicePreferenceMap.containsValue(userPackage)) {
                z = false;
                for (DeviceFilter deviceFilter : (DeviceFilter[]) this.mDevicePreferenceMap.keySet().toArray(new DeviceFilter[0])) {
                    if (userPackage.equals(this.mDevicePreferenceMap.get(deviceFilter))) {
                        this.mDevicePreferenceMap.remove(deviceFilter);
                        z = true;
                    }
                }
            } else {
                z = false;
            }
            if (this.mAccessoryPreferenceMap.containsValue(userPackage)) {
                for (AccessoryFilter accessoryFilter : (AccessoryFilter[]) this.mAccessoryPreferenceMap.keySet().toArray(new AccessoryFilter[0])) {
                    if (userPackage.equals(this.mAccessoryPreferenceMap.get(accessoryFilter))) {
                        this.mAccessoryPreferenceMap.remove(accessoryFilter);
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        synchronized (this.mLock) {
            dualDumpOutputStream.write("parent_user_id", 1120986464257L, this.mParentUser.getIdentifier());
            for (DeviceFilter deviceFilter : this.mDevicePreferenceMap.keySet()) {
                long start2 = dualDumpOutputStream.start("device_preferences", 2246267895810L);
                deviceFilter.dump(dualDumpOutputStream, "filter", 1146756268033L);
                this.mDevicePreferenceMap.get(deviceFilter).dump(dualDumpOutputStream, "user_package", 1146756268034L);
                dualDumpOutputStream.end(start2);
            }
            for (AccessoryFilter accessoryFilter : this.mAccessoryPreferenceMap.keySet()) {
                long start3 = dualDumpOutputStream.start("accessory_preferences", 2246267895811L);
                accessoryFilter.dump(dualDumpOutputStream, "filter", 1146756268033L);
                this.mAccessoryPreferenceMap.get(accessoryFilter).dump(dualDumpOutputStream, "user_package", 1146756268034L);
                dualDumpOutputStream.end(start3);
            }
        }
        sEventLogger.dump(new DualOutputStreamDumpSink(dualDumpOutputStream, 1138166333444L));
        dualDumpOutputStream.end(start);
    }

    private static Intent createDeviceAttachedIntent(UsbDevice usbDevice) {
        Intent intent = new Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intent.putExtra("device", usbDevice);
        intent.addFlags(285212672);
        return intent;
    }
}
