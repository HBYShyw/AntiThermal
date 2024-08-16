package com.android.server.pm;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.SigningInfo;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.LocalServices;
import com.android.server.backup.BackupUtils;
import com.android.server.hdmi.HdmiCecKeycode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import libcore.util.HexEncoding;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShortcutPackageInfo {
    private static final String ATTR_BACKUP_ALLOWED = "allow-backup";
    private static final String ATTR_BACKUP_ALLOWED_INITIALIZED = "allow-backup-initialized";
    private static final String ATTR_BACKUP_SOURCE_BACKUP_ALLOWED = "bk_src_backup-allowed";
    private static final String ATTR_BACKUP_SOURCE_VERSION = "bk_src_version";
    private static final String ATTR_LAST_UPDATE_TIME = "last_udpate_time";
    private static final String ATTR_SHADOW = "shadow";
    private static final String ATTR_SIGNATURE_HASH = "hash";
    private static final String ATTR_VERSION = "version";
    private static final String TAG = "ShortcutService";
    static final String TAG_ROOT = "package-info";
    private static final String TAG_SIGNATURE = "signature";
    private boolean mBackupAllowedInitialized;
    private boolean mIsShadow;
    private long mLastUpdateTime;
    private ArrayList<byte[]> mSigHashes;
    private long mVersionCode;
    private long mBackupSourceVersionCode = -1;
    private boolean mBackupAllowed = false;
    private boolean mBackupSourceBackupAllowed = false;

    private ShortcutPackageInfo(long j, long j2, ArrayList<byte[]> arrayList, boolean z) {
        this.mVersionCode = j;
        this.mLastUpdateTime = j2;
        this.mIsShadow = z;
        this.mSigHashes = arrayList;
    }

    public static ShortcutPackageInfo newEmpty() {
        return new ShortcutPackageInfo(-1L, 0L, new ArrayList(0), false);
    }

    public boolean isShadow() {
        return this.mIsShadow;
    }

    public void setShadow(boolean z) {
        this.mIsShadow = z;
    }

    public long getVersionCode() {
        return this.mVersionCode;
    }

    public long getBackupSourceVersionCode() {
        return this.mBackupSourceVersionCode;
    }

    @VisibleForTesting
    public boolean isBackupSourceBackupAllowed() {
        return this.mBackupSourceBackupAllowed;
    }

    public long getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public boolean isBackupAllowed() {
        return this.mBackupAllowed;
    }

    public void updateFromPackageInfo(PackageInfo packageInfo) {
        if (packageInfo != null) {
            this.mVersionCode = packageInfo.getLongVersionCode();
            this.mLastUpdateTime = packageInfo.lastUpdateTime;
            this.mBackupAllowed = ShortcutService.shouldBackupApp(packageInfo);
            this.mBackupAllowedInitialized = true;
        }
    }

    public boolean hasSignatures() {
        return this.mSigHashes.size() > 0;
    }

    public int canRestoreTo(ShortcutService shortcutService, PackageInfo packageInfo, boolean z) {
        if (!BackupUtils.signaturesMatch(this.mSigHashes, packageInfo, (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class))) {
            Slog.w(TAG, "Can't restore: Package signature mismatch");
            return HdmiCecKeycode.CEC_KEYCODE_RESTORE_VOLUME_FUNCTION;
        }
        if (!ShortcutService.shouldBackupApp(packageInfo) || !this.mBackupSourceBackupAllowed) {
            Slog.w(TAG, "Can't restore: package didn't or doesn't allow backup");
            return HdmiCecKeycode.CEC_KEYCODE_MUTE_FUNCTION;
        }
        if (z || packageInfo.getLongVersionCode() >= this.mBackupSourceVersionCode) {
            return 0;
        }
        Slog.w(TAG, String.format("Can't restore: package current version %d < backed up version %d", Long.valueOf(packageInfo.getLongVersionCode()), Long.valueOf(this.mBackupSourceVersionCode)));
        return 100;
    }

    @VisibleForTesting
    public static ShortcutPackageInfo generateForInstalledPackageForTest(ShortcutService shortcutService, String str, int i) {
        PackageInfo packageInfoWithSignatures = shortcutService.getPackageInfoWithSignatures(str, i);
        SigningInfo signingInfo = packageInfoWithSignatures.signingInfo;
        if (signingInfo == null) {
            Slog.e(TAG, "Can't get signatures: package=" + str);
            return null;
        }
        ShortcutPackageInfo shortcutPackageInfo = new ShortcutPackageInfo(packageInfoWithSignatures.getLongVersionCode(), packageInfoWithSignatures.lastUpdateTime, BackupUtils.hashSignatureArray(signingInfo.getApkContentsSigners()), false);
        shortcutPackageInfo.mBackupSourceBackupAllowed = ShortcutService.shouldBackupApp(packageInfoWithSignatures);
        shortcutPackageInfo.mBackupSourceVersionCode = packageInfoWithSignatures.getLongVersionCode();
        return shortcutPackageInfo;
    }

    public void refreshSignature(ShortcutService shortcutService, ShortcutPackageItem shortcutPackageItem) {
        if (this.mIsShadow) {
            shortcutService.wtf("Attempted to refresh package info for shadow package " + shortcutPackageItem.getPackageName() + ", user=" + shortcutPackageItem.getOwnerUserId());
            return;
        }
        PackageInfo packageInfoWithSignatures = shortcutService.getPackageInfoWithSignatures(shortcutPackageItem.getPackageName(), shortcutPackageItem.getPackageUserId());
        if (packageInfoWithSignatures == null) {
            Slog.w(TAG, "Package not found: " + shortcutPackageItem.getPackageName());
            return;
        }
        SigningInfo signingInfo = packageInfoWithSignatures.signingInfo;
        if (signingInfo == null) {
            Slog.w(TAG, "Not refreshing signature for " + shortcutPackageItem.getPackageName() + " since it appears to have no signing info.");
            return;
        }
        this.mSigHashes = BackupUtils.hashSignatureArray(signingInfo.getApkContentsSigners());
    }

    public void saveToXml(ShortcutService shortcutService, TypedXmlSerializer typedXmlSerializer, boolean z) throws IOException {
        if (z && !this.mBackupAllowedInitialized) {
            shortcutService.wtf("Backup happened before mBackupAllowed is initialized.");
        }
        typedXmlSerializer.startTag((String) null, TAG_ROOT);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_VERSION, this.mVersionCode);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_LAST_UPDATE_TIME, this.mLastUpdateTime);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_SHADOW, this.mIsShadow);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_BACKUP_ALLOWED, this.mBackupAllowed);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_BACKUP_ALLOWED_INITIALIZED, this.mBackupAllowedInitialized);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_BACKUP_SOURCE_VERSION, this.mBackupSourceVersionCode);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_BACKUP_SOURCE_BACKUP_ALLOWED, this.mBackupSourceBackupAllowed);
        for (int i = 0; i < this.mSigHashes.size(); i++) {
            typedXmlSerializer.startTag((String) null, TAG_SIGNATURE);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_SIGNATURE_HASH, Base64.getEncoder().encodeToString(this.mSigHashes.get(i)));
            typedXmlSerializer.endTag((String) null, TAG_SIGNATURE);
        }
        typedXmlSerializer.endTag((String) null, TAG_ROOT);
    }

    public void loadFromXml(TypedXmlPullParser typedXmlPullParser, boolean z) throws IOException, XmlPullParserException {
        long parseLongAttribute = ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_VERSION, -1L);
        long parseLongAttribute2 = ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_LAST_UPDATE_TIME);
        int i = 1;
        boolean z2 = z || ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_SHADOW);
        long parseLongAttribute3 = ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_BACKUP_SOURCE_VERSION, -1L);
        boolean parseBooleanAttribute = ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_BACKUP_ALLOWED, true);
        boolean parseBooleanAttribute2 = ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_BACKUP_SOURCE_BACKUP_ALLOWED, true);
        ArrayList<byte[]> arrayList = new ArrayList<>();
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == i || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next == 2) {
                int depth2 = typedXmlPullParser.getDepth();
                String name = typedXmlPullParser.getName();
                if (depth2 == depth + 1) {
                    name.hashCode();
                    if (name.equals(TAG_SIGNATURE)) {
                        arrayList.add(Base64.getDecoder().decode(ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_SIGNATURE_HASH)));
                        i = 1;
                    }
                }
                ShortcutService.warnForInvalidTag(depth2, name);
                i = 1;
            }
        }
        if (z) {
            this.mVersionCode = -1L;
            this.mBackupSourceVersionCode = parseLongAttribute;
            this.mBackupSourceBackupAllowed = parseBooleanAttribute;
        } else {
            this.mVersionCode = parseLongAttribute;
            this.mBackupSourceVersionCode = parseLongAttribute3;
            this.mBackupSourceBackupAllowed = parseBooleanAttribute2;
        }
        this.mLastUpdateTime = parseLongAttribute2;
        this.mIsShadow = z2;
        this.mSigHashes = arrayList;
        this.mBackupAllowed = false;
        this.mBackupAllowedInitialized = false;
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println();
        printWriter.print(str);
        printWriter.println("PackageInfo:");
        printWriter.print(str);
        printWriter.print("  IsShadow: ");
        printWriter.print(this.mIsShadow);
        printWriter.print(this.mIsShadow ? " (not installed)" : " (installed)");
        printWriter.println();
        printWriter.print(str);
        printWriter.print("  Version: ");
        printWriter.print(this.mVersionCode);
        printWriter.println();
        if (this.mBackupAllowedInitialized) {
            printWriter.print(str);
            printWriter.print("  Backup Allowed: ");
            printWriter.print(this.mBackupAllowed);
            printWriter.println();
        }
        if (this.mBackupSourceVersionCode != -1) {
            printWriter.print(str);
            printWriter.print("  Backup source version: ");
            printWriter.print(this.mBackupSourceVersionCode);
            printWriter.println();
            printWriter.print(str);
            printWriter.print("  Backup source backup allowed: ");
            printWriter.print(this.mBackupSourceBackupAllowed);
            printWriter.println();
        }
        printWriter.print(str);
        printWriter.print("  Last package update time: ");
        printWriter.print(this.mLastUpdateTime);
        printWriter.println();
        for (int i = 0; i < this.mSigHashes.size(); i++) {
            printWriter.print(str);
            printWriter.print("    ");
            printWriter.print("SigHash: ");
            printWriter.println(HexEncoding.encode(this.mSigHashes.get(i)));
        }
    }
}
