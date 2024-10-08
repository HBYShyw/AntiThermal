package com.android.server.backup.utils;

import android.app.backup.IBackupManagerMonitor;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.Signature;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Slog;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.FileMetadata;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.restore.RestorePolicy;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TarBackupReader {
    private static final int TAR_HEADER_LENGTH_FILESIZE = 12;
    private static final int TAR_HEADER_LENGTH_MODE = 8;
    private static final int TAR_HEADER_LENGTH_MODTIME = 12;
    private static final int TAR_HEADER_LENGTH_PATH = 100;
    private static final int TAR_HEADER_LENGTH_PATH_PREFIX = 155;
    private static final int TAR_HEADER_LONG_RADIX = 8;
    private static final int TAR_HEADER_OFFSET_FILESIZE = 124;
    private static final int TAR_HEADER_OFFSET_MODE = 100;
    private static final int TAR_HEADER_OFFSET_MODTIME = 136;
    private static final int TAR_HEADER_OFFSET_PATH = 0;
    private static final int TAR_HEADER_OFFSET_PATH_PREFIX = 345;
    private static final int TAR_HEADER_OFFSET_TYPE_CHAR = 156;
    private final BytesReadListener mBytesReadListener;
    private final InputStream mInputStream;
    private IBackupManagerMonitor mMonitor;
    private byte[] mWidgetData = null;

    public TarBackupReader(InputStream inputStream, BytesReadListener bytesReadListener, IBackupManagerMonitor iBackupManagerMonitor) {
        this.mInputStream = inputStream;
        this.mBytesReadListener = bytesReadListener;
        this.mMonitor = iBackupManagerMonitor;
    }

    public FileMetadata readTarHeaders() throws IOException {
        byte[] bArr = new byte[512];
        if (!readTarHeader(bArr)) {
            return null;
        }
        try {
            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.size = extractRadix(bArr, 124, 12, 8);
            fileMetadata.mtime = extractRadix(bArr, 136, 12, 8);
            fileMetadata.mode = extractRadix(bArr, 100, 8, 8);
            fileMetadata.path = extractString(bArr, 345, 155);
            String extractString = extractString(bArr, 0, 100);
            if (extractString.length() > 0) {
                if (fileMetadata.path.length() > 0) {
                    fileMetadata.path += '/';
                }
                fileMetadata.path += extractString;
            }
            byte b = bArr[156];
            if (b == 120) {
                boolean readPaxExtendedHeader = readPaxExtendedHeader(fileMetadata);
                if (readPaxExtendedHeader) {
                    readPaxExtendedHeader = readTarHeader(bArr);
                }
                if (!readPaxExtendedHeader) {
                    throw new IOException("Bad or missing pax header");
                }
                b = bArr[156];
            }
            if (b == 0) {
                return null;
            }
            if (b == 48) {
                fileMetadata.type = 1;
            } else if (b == 53) {
                fileMetadata.type = 2;
                if (fileMetadata.size != 0) {
                    Slog.w(BackupManagerService.TAG, "Directory entry with nonzero size in header");
                    fileMetadata.size = 0L;
                }
            } else {
                Slog.e(BackupManagerService.TAG, "Unknown tar entity type: " + ((int) b));
                throw new IOException("Unknown entity type " + ((int) b));
            }
            if ("shared/".regionMatches(0, fileMetadata.path, 0, 7)) {
                fileMetadata.path = fileMetadata.path.substring(7);
                fileMetadata.packageName = UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE;
                fileMetadata.domain = "shared";
                Slog.i(BackupManagerService.TAG, "File in shared storage: " + fileMetadata.path);
            } else if ("apps/".regionMatches(0, fileMetadata.path, 0, 5)) {
                String substring = fileMetadata.path.substring(5);
                fileMetadata.path = substring;
                int indexOf = substring.indexOf(47);
                if (indexOf < 0) {
                    throw new IOException("Illegal semantic path in " + fileMetadata.path);
                }
                fileMetadata.packageName = fileMetadata.path.substring(0, indexOf);
                String substring2 = fileMetadata.path.substring(indexOf + 1);
                fileMetadata.path = substring2;
                if (!substring2.equals(UserBackupManagerService.BACKUP_MANIFEST_FILENAME) && !fileMetadata.path.equals(UserBackupManagerService.BACKUP_METADATA_FILENAME)) {
                    int indexOf2 = fileMetadata.path.indexOf(47);
                    if (indexOf2 < 0) {
                        throw new IOException("Illegal semantic path in non-manifest " + fileMetadata.path);
                    }
                    fileMetadata.domain = fileMetadata.path.substring(0, indexOf2);
                    fileMetadata.path = fileMetadata.path.substring(indexOf2 + 1);
                }
            }
            return fileMetadata;
        } catch (IOException e) {
            Slog.e(BackupManagerService.TAG, "Parse error in header: " + e.getMessage());
            throw e;
        }
    }

    private static int readExactly(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        int i3 = 0;
        while (i3 < i2) {
            int read = inputStream.read(bArr, i + i3, i2 - i3);
            if (read <= 0) {
                break;
            }
            i3 += read;
        }
        return i3;
    }

    public Signature[] readAppManifestAndReturnSignatures(FileMetadata fileMetadata) throws IOException {
        long j = fileMetadata.size;
        if (j > 65536) {
            throw new IOException("Restore manifest too big; corrupt? size=" + fileMetadata.size);
        }
        byte[] bArr = new byte[(int) j];
        long readExactly = readExactly(this.mInputStream, bArr, 0, (int) j);
        long j2 = fileMetadata.size;
        if (readExactly == j2) {
            this.mBytesReadListener.onBytesRead(j2);
            String[] strArr = new String[1];
            try {
                int extractLine = extractLine(bArr, 0, strArr);
                int parseInt = Integer.parseInt(strArr[0]);
                if (parseInt == 1) {
                    int extractLine2 = extractLine(bArr, extractLine, strArr);
                    String str = strArr[0];
                    if (str.equals(fileMetadata.packageName)) {
                        int extractLine3 = extractLine(bArr, extractLine2, strArr);
                        fileMetadata.version = Integer.parseInt(strArr[0]);
                        int extractLine4 = extractLine(bArr, extractLine3, strArr);
                        Integer.parseInt(strArr[0]);
                        int extractLine5 = extractLine(bArr, extractLine4, strArr);
                        fileMetadata.installerPackageName = strArr[0].length() > 0 ? strArr[0] : null;
                        int extractLine6 = extractLine(bArr, extractLine5, strArr);
                        fileMetadata.hasApk = strArr[0].equals("1");
                        int extractLine7 = extractLine(bArr, extractLine6, strArr);
                        int parseInt2 = Integer.parseInt(strArr[0]);
                        if (parseInt2 > 0) {
                            Signature[] signatureArr = new Signature[parseInt2];
                            for (int i = 0; i < parseInt2; i++) {
                                extractLine7 = extractLine(bArr, extractLine7, strArr);
                                signatureArr[i] = new Signature(strArr[0]);
                            }
                            return signatureArr;
                        }
                        Slog.i(BackupManagerService.TAG, "Missing signature on backed-up package " + fileMetadata.packageName);
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 42, null, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName));
                    } else {
                        Slog.i(BackupManagerService.TAG, "Expected package " + fileMetadata.packageName + " but restore manifest claims " + str);
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 43, null, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName), "android.app.backup.extra.LOG_MANIFEST_PACKAGE_NAME", str));
                    }
                } else {
                    Slog.i(BackupManagerService.TAG, "Unknown restore manifest version " + parseInt + " for package " + fileMetadata.packageName);
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 44, null, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName), "android.app.backup.extra.LOG_EVENT_PACKAGE_VERSION", (long) parseInt));
                }
            } catch (NumberFormatException unused) {
                Slog.w(BackupManagerService.TAG, "Corrupt restore manifest for package " + fileMetadata.packageName);
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 46, null, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName));
            } catch (IllegalArgumentException e) {
                Slog.w(BackupManagerService.TAG, e.getMessage());
            }
            return null;
        }
        throw new IOException("Unexpected EOF in manifest");
    }

    public RestorePolicy chooseRestorePolicy(PackageManager packageManager, boolean z, FileMetadata fileMetadata, Signature[] signatureArr, PackageManagerInternal packageManagerInternal, int i, Context context) {
        return chooseRestorePolicy(packageManager, z, fileMetadata, signatureArr, packageManagerInternal, i, BackupEligibilityRules.forBackup(packageManager, packageManagerInternal, i, context));
    }

    public RestorePolicy chooseRestorePolicy(PackageManager packageManager, boolean z, FileMetadata fileMetadata, Signature[] signatureArr, PackageManagerInternal packageManagerInternal, int i, BackupEligibilityRules backupEligibilityRules) {
        RestorePolicy restorePolicy;
        if (signatureArr == null) {
            return RestorePolicy.IGNORE;
        }
        RestorePolicy restorePolicy2 = RestorePolicy.IGNORE;
        try {
            PackageInfo packageInfoAsUser = packageManager.getPackageInfoAsUser(fileMetadata.packageName, AudioFormat.OPUS, i);
            ApplicationInfo applicationInfo = packageInfoAsUser.applicationInfo;
            int i2 = applicationInfo.flags;
            if (backupEligibilityRules.isAppBackupAllowed(applicationInfo)) {
                if (UserHandle.isCore(packageInfoAsUser.applicationInfo.uid) && packageInfoAsUser.applicationInfo.backupAgentName == null) {
                    Slog.w(BackupManagerService.TAG, "Package " + fileMetadata.packageName + " is system level with no agent");
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 38, packageInfoAsUser, 2, null);
                }
                if (backupEligibilityRules.signaturesMatch(signatureArr, packageInfoAsUser)) {
                    if ((packageInfoAsUser.applicationInfo.flags & 131072) != 0) {
                        Slog.i(BackupManagerService.TAG, "Package has restoreAnyVersion; taking data");
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 34, packageInfoAsUser, 3, null);
                        restorePolicy2 = RestorePolicy.ACCEPT;
                    } else if (packageInfoAsUser.getLongVersionCode() >= fileMetadata.version) {
                        Slog.i(BackupManagerService.TAG, "Sig + version match; taking data");
                        restorePolicy2 = RestorePolicy.ACCEPT;
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 35, packageInfoAsUser, 3, null);
                    } else if (z) {
                        Slog.i(BackupManagerService.TAG, "Data version " + fileMetadata.version + " is newer than installed version " + packageInfoAsUser.getLongVersionCode() + " - requiring apk");
                        restorePolicy2 = RestorePolicy.ACCEPT_IF_APK;
                    } else {
                        Slog.i(BackupManagerService.TAG, "Data requires newer version " + fileMetadata.version + "; ignoring");
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 36, packageInfoAsUser, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_OLD_VERSION", fileMetadata.version));
                    }
                } else {
                    Slog.w(BackupManagerService.TAG, "Restore manifest signatures do not match installed application for " + fileMetadata.packageName);
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 37, packageInfoAsUser, 3, null);
                }
            } else {
                Slog.i(BackupManagerService.TAG, "Restore manifest from " + fileMetadata.packageName + " but allowBackup=false");
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 39, packageInfoAsUser, 3, null);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            if (z) {
                Slog.i(BackupManagerService.TAG, "Package " + fileMetadata.packageName + " not installed; requiring apk in dataset");
                restorePolicy = RestorePolicy.ACCEPT_IF_APK;
            } else {
                restorePolicy = RestorePolicy.IGNORE;
            }
            restorePolicy2 = restorePolicy;
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 40, null, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName), "android.app.backup.extra.LOG_POLICY_ALLOW_APKS", z));
        }
        if (restorePolicy2 == RestorePolicy.ACCEPT_IF_APK && !fileMetadata.hasApk) {
            Slog.i(BackupManagerService.TAG, "Cannot restore package " + fileMetadata.packageName + " without the matching .apk");
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 41, null, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName));
        }
        return restorePolicy2;
    }

    public void skipTarPadding(long j) throws IOException {
        long j2 = (j + 512) % 512;
        if (j2 > 0) {
            int i = 512 - ((int) j2);
            if (readExactly(this.mInputStream, new byte[i], 0, i) == i) {
                this.mBytesReadListener.onBytesRead(i);
                return;
            }
            throw new IOException("Unexpected EOF in padding");
        }
    }

    public void readMetadata(FileMetadata fileMetadata) throws IOException {
        long j = fileMetadata.size;
        if (j > 65536) {
            throw new IOException("Metadata too big; corrupt? size=" + fileMetadata.size);
        }
        int i = (int) j;
        byte[] bArr = new byte[i];
        long readExactly = readExactly(this.mInputStream, bArr, 0, (int) j);
        long j2 = fileMetadata.size;
        if (readExactly == j2) {
            this.mBytesReadListener.onBytesRead(j2);
            String[] strArr = new String[1];
            int extractLine = extractLine(bArr, 0, strArr);
            int parseInt = Integer.parseInt(strArr[0]);
            if (parseInt == 1) {
                int extractLine2 = extractLine(bArr, extractLine, strArr);
                String str = strArr[0];
                if (fileMetadata.packageName.equals(str)) {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr, extractLine2, i - extractLine2);
                    DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                    while (byteArrayInputStream.available() > 0) {
                        int readInt = dataInputStream.readInt();
                        int readInt2 = dataInputStream.readInt();
                        if (readInt2 > 65536) {
                            throw new IOException("Datum " + Integer.toHexString(readInt) + " too big; corrupt? size=" + fileMetadata.size);
                        }
                        if (readInt == 33549569) {
                            byte[] bArr2 = new byte[readInt2];
                            this.mWidgetData = bArr2;
                            dataInputStream.read(bArr2);
                        } else {
                            Slog.i(BackupManagerService.TAG, "Ignoring metadata blob " + Integer.toHexString(readInt) + " for " + fileMetadata.packageName);
                            dataInputStream.skipBytes(readInt2);
                        }
                    }
                    return;
                }
                Slog.w(BackupManagerService.TAG, "Metadata mismatch: package " + fileMetadata.packageName + " but widget data for " + str);
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 47, null, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName), "android.app.backup.extra.LOG_WIDGET_PACKAGE_NAME", str));
                return;
            }
            Slog.w(BackupManagerService.TAG, "Unsupported metadata version " + parseInt);
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 48, null, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", fileMetadata.packageName), "android.app.backup.extra.LOG_EVENT_PACKAGE_VERSION", (long) parseInt));
            return;
        }
        throw new IOException("Unexpected EOF in widget data");
    }

    private static int extractLine(byte[] bArr, int i, String[] strArr) throws IOException {
        int length = bArr.length;
        if (i >= length) {
            throw new IOException("Incomplete data");
        }
        int i2 = i;
        while (i2 < length && bArr[i2] != 10) {
            i2++;
        }
        strArr[0] = new String(bArr, i, i2 - i);
        return i2 + 1;
    }

    private boolean readTarHeader(byte[] bArr) throws IOException {
        int readExactly = readExactly(this.mInputStream, bArr, 0, 512);
        if (readExactly == 0) {
            return false;
        }
        if (readExactly < 512) {
            throw new IOException("Unable to read full block header");
        }
        this.mBytesReadListener.onBytesRead(512L);
        return true;
    }

    private boolean readPaxExtendedHeader(FileMetadata fileMetadata) throws IOException {
        long j = fileMetadata.size;
        if (j > 32768) {
            Slog.w(BackupManagerService.TAG, "Suspiciously large pax header size " + fileMetadata.size + " - aborting");
            throw new IOException("Sanity failure: pax header size " + fileMetadata.size);
        }
        int i = ((int) ((j + 511) >> 9)) * 512;
        byte[] bArr = new byte[i];
        int i2 = 0;
        if (readExactly(this.mInputStream, bArr, 0, i) < i) {
            throw new IOException("Unable to read full pax header");
        }
        this.mBytesReadListener.onBytesRead(i);
        int i3 = (int) fileMetadata.size;
        do {
            int i4 = i2 + 1;
            while (i4 < i3 && bArr[i4] != 32) {
                i4++;
            }
            if (i4 >= i3) {
                throw new IOException("Invalid pax data");
            }
            int extractRadix = (int) extractRadix(bArr, i2, i4 - i2, 10);
            int i5 = i4 + 1;
            i2 += extractRadix;
            int i6 = i2 - 1;
            int i7 = i5 + 1;
            while (bArr[i7] != 61 && i7 <= i6) {
                i7++;
            }
            if (i7 > i6) {
                throw new IOException("Invalid pax declaration");
            }
            String str = new String(bArr, i5, i7 - i5, "UTF-8");
            String str2 = new String(bArr, i7 + 1, (i6 - i7) - 1, "UTF-8");
            if ("path".equals(str)) {
                fileMetadata.path = str2;
            } else if ("size".equals(str)) {
                fileMetadata.size = Long.parseLong(str2);
            } else {
                Slog.i(BackupManagerService.TAG, "Unhandled pax key: " + i5);
            }
        } while (i2 < i3);
        return true;
    }

    private static long extractRadix(byte[] bArr, int i, int i2, int i3) throws IOException {
        int i4 = i2 + i;
        long j = 0;
        while (i < i4) {
            int i5 = bArr[i];
            if (i5 == 0 || i5 == 32) {
                break;
            }
            if (i5 < 48 || i5 > (i3 + 48) - 1) {
                throw new IOException("Invalid number in header: '" + ((char) i5) + "' for radix " + i3);
            }
            j = (i5 - 48) + (i3 * j);
            i++;
        }
        return j;
    }

    private static String extractString(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i2 + i;
        int i4 = i;
        while (i4 < i3 && bArr[i4] != 0) {
            i4++;
        }
        return new String(bArr, i, i4 - i, "US-ASCII");
    }

    private static void hexLog(byte[] bArr) {
        int length = bArr.length;
        StringBuilder sb = new StringBuilder(64);
        int i = 0;
        while (length > 0) {
            sb.append(String.format("%04x   ", Integer.valueOf(i)));
            int i2 = length <= 16 ? length : 16;
            for (int i3 = 0; i3 < i2; i3++) {
                sb.append(String.format("%02x ", Byte.valueOf(bArr[i + i3])));
            }
            Slog.i("hexdump", sb.toString());
            sb.setLength(0);
            length -= i2;
            i += i2;
        }
    }

    public IBackupManagerMonitor getMonitor() {
        return this.mMonitor;
    }

    public byte[] getWidgetData() {
        return this.mWidgetData;
    }
}
