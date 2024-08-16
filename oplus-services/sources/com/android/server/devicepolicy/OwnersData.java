package com.android.server.devicepolicy;

import android.app.admin.SystemUpdateInfo;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class OwnersData {
    private static final String ATTR_CAN_ACCESS_DEVICE_IDS = "canAccessDeviceIds";
    private static final String ATTR_COMPONENT_NAME = "component";
    private static final String ATTR_DEVICE_OWNER_TYPE_VALUE = "value";
    private static final String ATTR_FREEZE_RECORD_END = "end";
    private static final String ATTR_FREEZE_RECORD_START = "start";
    private static final String ATTR_MIGRATED_POST_UPGRADE = "migratedPostUpgrade";
    private static final String ATTR_MIGRATED_TO_POLICY_ENGINE = "migratedToPolicyEngine";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PACKAGE = "package";
    private static final String ATTR_PROFILE_OWNER_OF_ORG_OWNED_DEVICE = "isPoOrganizationOwnedDevice";
    private static final String ATTR_REMOTE_BUGREPORT_HASH = "remoteBugreportHash";
    private static final String ATTR_REMOTE_BUGREPORT_URI = "remoteBugreportUri";
    private static final String ATTR_SIZE = "size";
    private static final String ATTR_USERID = "userId";
    private static final boolean DEBUG = false;
    private static final String DEVICE_OWNER_XML = "device_owner_2.xml";
    private static final String PROFILE_OWNER_XML = "profile_owner.xml";
    private static final String TAG = "DevicePolicyManagerService";
    private static final String TAG_DEVICE_OWNER = "device-owner";
    private static final String TAG_DEVICE_OWNER_CONTEXT = "device-owner-context";
    private static final String TAG_DEVICE_OWNER_PROTECTED_PACKAGES = "device-owner-protected-packages";
    private static final String TAG_DEVICE_OWNER_TYPE = "device-owner-type";
    private static final String TAG_FREEZE_PERIOD_RECORD = "freeze-record";
    private static final String TAG_PENDING_OTA_INFO = "pending-ota-info";
    private static final String TAG_POLICY_ENGINE_MIGRATION = "policy-engine-migration";
    private static final String TAG_PROFILE_OWNER = "profile-owner";
    private static final String TAG_ROOT = "root";
    private static final String TAG_SYSTEM_UPDATE_POLICY = "system-update-policy";
    OwnerInfo mDeviceOwner;

    @Deprecated
    ArrayMap<String, List<String>> mDeviceOwnerProtectedPackages;
    private final PolicyPathProvider mPathProvider;
    LocalDate mSystemUpdateFreezeEnd;
    LocalDate mSystemUpdateFreezeStart;
    SystemUpdateInfo mSystemUpdateInfo;
    SystemUpdatePolicy mSystemUpdatePolicy;
    int mDeviceOwnerUserId = -10000;
    final ArrayMap<String, Integer> mDeviceOwnerTypes = new ArrayMap<>();
    final ArrayMap<Integer, OwnerInfo> mProfileOwners = new ArrayMap<>();
    boolean mMigratedToPolicyEngine = false;
    boolean mPoliciesMigratedPostUpdate = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OwnersData(PolicyPathProvider policyPathProvider) {
        this.mPathProvider = policyPathProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void load(int[] iArr) {
        new DeviceOwnerReadWriter().readFromFileLocked();
        for (int i : iArr) {
            new ProfileOwnerReadWriter(i).readFromFileLocked();
        }
        OwnerInfo ownerInfo = this.mProfileOwners.get(Integer.valueOf(this.mDeviceOwnerUserId));
        ComponentName componentName = ownerInfo != null ? ownerInfo.admin : null;
        if (this.mDeviceOwner == null || componentName == null) {
            return;
        }
        Slog.w(TAG, String.format("User %d has both DO and PO, which is not supported", Integer.valueOf(this.mDeviceOwnerUserId)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean writeDeviceOwner() {
        return new DeviceOwnerReadWriter().writeToFileLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean writeProfileOwner(int i) {
        return new ProfileOwnerReadWriter(i).writeToFileLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        boolean z;
        boolean z2 = true;
        if (this.mDeviceOwner != null) {
            indentingPrintWriter.println("Device Owner: ");
            indentingPrintWriter.increaseIndent();
            this.mDeviceOwner.dump(indentingPrintWriter);
            indentingPrintWriter.println("User ID: " + this.mDeviceOwnerUserId);
            indentingPrintWriter.decreaseIndent();
            z = true;
        } else {
            z = false;
        }
        if (this.mSystemUpdatePolicy != null) {
            if (z) {
                indentingPrintWriter.println();
            }
            indentingPrintWriter.println("System Update Policy: " + this.mSystemUpdatePolicy);
            z = true;
        }
        ArrayMap<Integer, OwnerInfo> arrayMap = this.mProfileOwners;
        if (arrayMap != null) {
            for (Map.Entry<Integer, OwnerInfo> entry : arrayMap.entrySet()) {
                if (z) {
                    indentingPrintWriter.println();
                }
                indentingPrintWriter.println("Profile Owner (User " + entry.getKey() + "): ");
                indentingPrintWriter.increaseIndent();
                entry.getValue().dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
                z = true;
            }
        }
        if (this.mSystemUpdateInfo != null) {
            if (z) {
                indentingPrintWriter.println();
            }
            indentingPrintWriter.println("Pending System Update: " + this.mSystemUpdateInfo);
        } else {
            z2 = z;
        }
        if (this.mSystemUpdateFreezeStart == null && this.mSystemUpdateFreezeEnd == null) {
            return;
        }
        if (z2) {
            indentingPrintWriter.println();
        }
        indentingPrintWriter.println("System update freeze record: " + getSystemUpdateFreezePeriodRecordAsString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSystemUpdateFreezePeriodRecordAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("start: ");
        LocalDate localDate = this.mSystemUpdateFreezeStart;
        if (localDate != null) {
            sb.append(localDate.toString());
        } else {
            sb.append("null");
        }
        sb.append("; end: ");
        LocalDate localDate2 = this.mSystemUpdateFreezeEnd;
        if (localDate2 != null) {
            sb.append(localDate2.toString());
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public File getDeviceOwnerFile() {
        return new File(this.mPathProvider.getDataSystemDirectory(), DEVICE_OWNER_XML);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public File getProfileOwnerFile(int i) {
        return new File(this.mPathProvider.getUserSystemDirectory(i), PROFILE_OWNER_XML);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static abstract class FileReadWriter {
        private final File mFile;

        abstract boolean readInner(TypedXmlPullParser typedXmlPullParser, int i, String str);

        abstract boolean shouldWrite();

        abstract void writeInner(TypedXmlSerializer typedXmlSerializer) throws IOException;

        protected FileReadWriter(File file) {
            this.mFile = file;
        }

        boolean writeToFileLocked() {
            if (!shouldWrite()) {
                if (this.mFile.exists() && !this.mFile.delete()) {
                    Slog.e(OwnersData.TAG, "Failed to remove " + this.mFile.getPath());
                }
                return true;
            }
            AtomicFile atomicFile = new AtomicFile(this.mFile);
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream startWrite = atomicFile.startWrite();
                try {
                    TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                    resolveSerializer.startDocument((String) null, Boolean.TRUE);
                    resolveSerializer.startTag((String) null, OwnersData.TAG_ROOT);
                    writeInner(resolveSerializer);
                    resolveSerializer.endTag((String) null, OwnersData.TAG_ROOT);
                    resolveSerializer.endDocument();
                    resolveSerializer.flush();
                    atomicFile.finishWrite(startWrite);
                    return true;
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = startWrite;
                    Slog.e(OwnersData.TAG, "Exception when writing", e);
                    if (fileOutputStream == null) {
                        return false;
                    }
                    atomicFile.failWrite(fileOutputStream);
                    return false;
                }
            } catch (IOException e2) {
                e = e2;
            }
        }

        void readFromFileLocked() {
            if (!this.mFile.exists()) {
                return;
            }
            FileInputStream fileInputStream = null;
            try {
                try {
                    fileInputStream = new AtomicFile(this.mFile).openRead();
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                    int i = 0;
                    while (true) {
                        int next = resolvePullParser.next();
                        if (next == 1) {
                            break;
                        }
                        if (next == 2) {
                            i++;
                            String name = resolvePullParser.getName();
                            if (i == 1) {
                                if (!OwnersData.TAG_ROOT.equals(name)) {
                                    Slog.e(OwnersData.TAG, "Invalid root tag: " + name);
                                    return;
                                }
                            } else if (!readInner(resolvePullParser, i, name)) {
                                return;
                            }
                        } else if (next == 3) {
                            i--;
                        }
                    }
                } catch (IOException | XmlPullParserException e) {
                    Slog.e(OwnersData.TAG, "Error parsing owners information file", e);
                }
            } finally {
                IoUtils.closeQuietly(fileInputStream);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class DeviceOwnerReadWriter extends FileReadWriter {
        protected DeviceOwnerReadWriter() {
            super(OwnersData.this.getDeviceOwnerFile());
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        boolean shouldWrite() {
            OwnersData ownersData = OwnersData.this;
            return (ownersData.mDeviceOwner == null && ownersData.mSystemUpdatePolicy == null && ownersData.mSystemUpdateInfo == null) ? false : true;
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        void writeInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            OwnerInfo ownerInfo = OwnersData.this.mDeviceOwner;
            if (ownerInfo != null) {
                ownerInfo.writeToXml(typedXmlSerializer, OwnersData.TAG_DEVICE_OWNER);
                typedXmlSerializer.startTag((String) null, OwnersData.TAG_DEVICE_OWNER_CONTEXT);
                typedXmlSerializer.attributeInt((String) null, OwnersData.ATTR_USERID, OwnersData.this.mDeviceOwnerUserId);
                typedXmlSerializer.endTag((String) null, OwnersData.TAG_DEVICE_OWNER_CONTEXT);
            }
            if (!OwnersData.this.mDeviceOwnerTypes.isEmpty()) {
                for (Map.Entry<String, Integer> entry : OwnersData.this.mDeviceOwnerTypes.entrySet()) {
                    typedXmlSerializer.startTag((String) null, OwnersData.TAG_DEVICE_OWNER_TYPE);
                    typedXmlSerializer.attribute((String) null, OwnersData.ATTR_PACKAGE, entry.getKey());
                    typedXmlSerializer.attributeInt((String) null, OwnersData.ATTR_DEVICE_OWNER_TYPE_VALUE, entry.getValue().intValue());
                    typedXmlSerializer.endTag((String) null, OwnersData.TAG_DEVICE_OWNER_TYPE);
                }
            }
            if (OwnersData.this.mSystemUpdatePolicy != null) {
                typedXmlSerializer.startTag((String) null, OwnersData.TAG_SYSTEM_UPDATE_POLICY);
                OwnersData.this.mSystemUpdatePolicy.saveToXml(typedXmlSerializer);
                typedXmlSerializer.endTag((String) null, OwnersData.TAG_SYSTEM_UPDATE_POLICY);
            }
            SystemUpdateInfo systemUpdateInfo = OwnersData.this.mSystemUpdateInfo;
            if (systemUpdateInfo != null) {
                systemUpdateInfo.writeToXml(typedXmlSerializer, OwnersData.TAG_PENDING_OTA_INFO);
            }
            OwnersData ownersData = OwnersData.this;
            if (ownersData.mSystemUpdateFreezeStart != null || ownersData.mSystemUpdateFreezeEnd != null) {
                typedXmlSerializer.startTag((String) null, OwnersData.TAG_FREEZE_PERIOD_RECORD);
                LocalDate localDate = OwnersData.this.mSystemUpdateFreezeStart;
                if (localDate != null) {
                    typedXmlSerializer.attribute((String) null, OwnersData.ATTR_FREEZE_RECORD_START, localDate.toString());
                }
                LocalDate localDate2 = OwnersData.this.mSystemUpdateFreezeEnd;
                if (localDate2 != null) {
                    typedXmlSerializer.attribute((String) null, OwnersData.ATTR_FREEZE_RECORD_END, localDate2.toString());
                }
                typedXmlSerializer.endTag((String) null, OwnersData.TAG_FREEZE_PERIOD_RECORD);
            }
            typedXmlSerializer.startTag((String) null, OwnersData.TAG_POLICY_ENGINE_MIGRATION);
            typedXmlSerializer.attributeBoolean((String) null, OwnersData.ATTR_MIGRATED_TO_POLICY_ENGINE, OwnersData.this.mMigratedToPolicyEngine);
            typedXmlSerializer.attributeBoolean((String) null, OwnersData.ATTR_MIGRATED_POST_UPGRADE, OwnersData.this.mPoliciesMigratedPostUpdate);
            typedXmlSerializer.endTag((String) null, OwnersData.TAG_POLICY_ENGINE_MIGRATION);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x0053, code lost:
        
            if (r9.equals(com.android.server.devicepolicy.OwnersData.TAG_DEVICE_OWNER_CONTEXT) == false) goto L7;
         */
        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        boolean readInner(TypedXmlPullParser typedXmlPullParser, int i, String str) {
            char c = 2;
            if (i > 2) {
                return true;
            }
            str.hashCode();
            switch (str.hashCode()) {
                case -2101756875:
                    if (str.equals(OwnersData.TAG_PENDING_OTA_INFO)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -2020438916:
                    if (str.equals(OwnersData.TAG_DEVICE_OWNER)) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -1900517026:
                    break;
                case -725102274:
                    if (str.equals(OwnersData.TAG_POLICY_ENGINE_MIGRATION)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -465393379:
                    if (str.equals(OwnersData.TAG_DEVICE_OWNER_PROTECTED_PACKAGES)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 544117227:
                    if (str.equals(OwnersData.TAG_DEVICE_OWNER_TYPE)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1303827527:
                    if (str.equals(OwnersData.TAG_FREEZE_PERIOD_RECORD)) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 1748301720:
                    if (str.equals(OwnersData.TAG_SYSTEM_UPDATE_POLICY)) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    OwnersData.this.mSystemUpdateInfo = SystemUpdateInfo.readFromXml(typedXmlPullParser);
                    return true;
                case 1:
                    OwnersData.this.mDeviceOwner = OwnerInfo.readFromXml(typedXmlPullParser);
                    OwnersData.this.mDeviceOwnerUserId = 0;
                    return true;
                case 2:
                    OwnersData ownersData = OwnersData.this;
                    ownersData.mDeviceOwnerUserId = typedXmlPullParser.getAttributeInt((String) null, OwnersData.ATTR_USERID, ownersData.mDeviceOwnerUserId);
                    return true;
                case 3:
                    OwnersData.this.mMigratedToPolicyEngine = typedXmlPullParser.getAttributeBoolean((String) null, OwnersData.ATTR_MIGRATED_TO_POLICY_ENGINE, false);
                    OwnersData.this.mPoliciesMigratedPostUpdate = typedXmlPullParser.getAttributeBoolean((String) null, OwnersData.ATTR_MIGRATED_POST_UPGRADE, false);
                    return true;
                case 4:
                    String attributeValue = typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_PACKAGE);
                    int attributeInt = typedXmlPullParser.getAttributeInt((String) null, OwnersData.ATTR_SIZE, 0);
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < attributeInt; i2++) {
                        arrayList.add(typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_NAME + i2));
                    }
                    OwnersData ownersData2 = OwnersData.this;
                    if (ownersData2.mDeviceOwnerProtectedPackages == null) {
                        ownersData2.mDeviceOwnerProtectedPackages = new ArrayMap<>();
                    }
                    OwnersData.this.mDeviceOwnerProtectedPackages.put(attributeValue, arrayList);
                    return true;
                case 5:
                    OwnersData.this.mDeviceOwnerTypes.put(typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_PACKAGE), Integer.valueOf(typedXmlPullParser.getAttributeInt((String) null, OwnersData.ATTR_DEVICE_OWNER_TYPE_VALUE, 0)));
                    return true;
                case 6:
                    String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_FREEZE_RECORD_START);
                    String attributeValue3 = typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_FREEZE_RECORD_END);
                    if (attributeValue2 != null && attributeValue3 != null) {
                        OwnersData.this.mSystemUpdateFreezeStart = LocalDate.parse(attributeValue2);
                        OwnersData.this.mSystemUpdateFreezeEnd = LocalDate.parse(attributeValue3);
                        OwnersData ownersData3 = OwnersData.this;
                        if (ownersData3.mSystemUpdateFreezeStart.isAfter(ownersData3.mSystemUpdateFreezeEnd)) {
                            Slog.e(OwnersData.TAG, "Invalid system update freeze record loaded");
                            OwnersData ownersData4 = OwnersData.this;
                            ownersData4.mSystemUpdateFreezeStart = null;
                            ownersData4.mSystemUpdateFreezeEnd = null;
                        }
                    }
                    return true;
                case 7:
                    OwnersData.this.mSystemUpdatePolicy = SystemUpdatePolicy.restoreFromXml(typedXmlPullParser);
                    return true;
                default:
                    Slog.e(OwnersData.TAG, "Unexpected tag: " + str);
                    return false;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class ProfileOwnerReadWriter extends FileReadWriter {
        private final int mUserId;

        ProfileOwnerReadWriter(int i) {
            super(OwnersData.this.getProfileOwnerFile(i));
            this.mUserId = i;
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        boolean shouldWrite() {
            return OwnersData.this.mProfileOwners.get(Integer.valueOf(this.mUserId)) != null;
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        void writeInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            OwnerInfo ownerInfo = OwnersData.this.mProfileOwners.get(Integer.valueOf(this.mUserId));
            if (ownerInfo != null) {
                ownerInfo.writeToXml(typedXmlSerializer, OwnersData.TAG_PROFILE_OWNER);
            }
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        boolean readInner(TypedXmlPullParser typedXmlPullParser, int i, String str) {
            if (i > 2) {
                return true;
            }
            str.hashCode();
            if (str.equals(OwnersData.TAG_PROFILE_OWNER)) {
                OwnersData.this.mProfileOwners.put(Integer.valueOf(this.mUserId), OwnerInfo.readFromXml(typedXmlPullParser));
                return true;
            }
            Slog.e(OwnersData.TAG, "Unexpected tag: " + str);
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class OwnerInfo {
        public final ComponentName admin;
        public boolean isOrganizationOwnedDevice;
        public final String packageName;
        public String remoteBugreportHash;
        public String remoteBugreportUri;

        /* JADX INFO: Access modifiers changed from: package-private */
        public OwnerInfo(ComponentName componentName, String str, String str2, boolean z) {
            this.admin = componentName;
            this.packageName = componentName.getPackageName();
            this.remoteBugreportUri = str;
            this.remoteBugreportHash = str2;
            this.isOrganizationOwnedDevice = z;
        }

        public void writeToXml(TypedXmlSerializer typedXmlSerializer, String str) throws IOException {
            typedXmlSerializer.startTag((String) null, str);
            ComponentName componentName = this.admin;
            if (componentName != null) {
                typedXmlSerializer.attribute((String) null, OwnersData.ATTR_COMPONENT_NAME, componentName.flattenToString());
            }
            String str2 = this.remoteBugreportUri;
            if (str2 != null) {
                typedXmlSerializer.attribute((String) null, OwnersData.ATTR_REMOTE_BUGREPORT_URI, str2);
            }
            String str3 = this.remoteBugreportHash;
            if (str3 != null) {
                typedXmlSerializer.attribute((String) null, OwnersData.ATTR_REMOTE_BUGREPORT_HASH, str3);
            }
            boolean z = this.isOrganizationOwnedDevice;
            if (z) {
                typedXmlSerializer.attributeBoolean((String) null, OwnersData.ATTR_PROFILE_OWNER_OF_ORG_OWNED_DEVICE, z);
            }
            typedXmlSerializer.endTag((String) null, str);
        }

        public static OwnerInfo readFromXml(TypedXmlPullParser typedXmlPullParser) {
            String attributeValue = typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_COMPONENT_NAME);
            String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_REMOTE_BUGREPORT_URI);
            String attributeValue3 = typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_REMOTE_BUGREPORT_HASH);
            boolean equals = "true".equals(typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_PROFILE_OWNER_OF_ORG_OWNED_DEVICE)) | "true".equals(typedXmlPullParser.getAttributeValue((String) null, OwnersData.ATTR_CAN_ACCESS_DEVICE_IDS));
            if (attributeValue == null) {
                Slog.e(OwnersData.TAG, "Owner component not found");
                return null;
            }
            ComponentName unflattenFromString = ComponentName.unflattenFromString(attributeValue);
            if (unflattenFromString == null) {
                Slog.e(OwnersData.TAG, "Owner component not parsable: " + attributeValue);
                return null;
            }
            return new OwnerInfo(unflattenFromString, attributeValue2, attributeValue3, equals);
        }

        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("admin=" + this.admin);
            indentingPrintWriter.println("package=" + this.packageName);
            indentingPrintWriter.println("isOrganizationOwnedDevice=" + this.isOrganizationOwnedDevice);
        }
    }
}
