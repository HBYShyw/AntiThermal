package com.android.server.pm;

import android.os.FileUtils;
import android.util.AtomicFile;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import libcore.io.IoUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageUsage extends AbstractStatsBase<Map<String, PackageSetting>> {
    private static final String USAGE_FILE_MAGIC = "PACKAGE_USAGE__VERSION_";
    private static final String USAGE_FILE_MAGIC_VERSION_1 = "PACKAGE_USAGE__VERSION_1";
    private boolean mIsHistoricalPackageUsageAvailable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageUsage() {
        super("package-usage.list", "PackageUsage_DiskWriter", true);
        this.mIsHistoricalPackageUsageAvailable = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isHistoricalPackageUsageAvailable() {
        return this.mIsHistoricalPackageUsageAvailable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void writeInternal(Map<String, PackageSetting> map) {
        FileOutputStream fileOutputStream;
        AtomicFile file = getFile();
        try {
            fileOutputStream = file.startWrite();
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                FileUtils.setPermissions(file.getBaseFile().getPath(), 416, 1000, 1032);
                StringBuilder sb = new StringBuilder();
                sb.append(USAGE_FILE_MAGIC_VERSION_1);
                sb.append('\n');
                bufferedOutputStream.write(sb.toString().getBytes(StandardCharsets.US_ASCII));
                for (PackageSetting packageSetting : map.values()) {
                    if (packageSetting != null && packageSetting.getPkgState() != null && packageSetting.getPkgState().getLatestPackageUseTimeInMills() != 0) {
                        sb.setLength(0);
                        sb.append(packageSetting.getPackageName());
                        for (long j : packageSetting.getPkgState().getLastPackageUsageTimeInMills()) {
                            sb.append(' ');
                            sb.append(j);
                        }
                        sb.append('\n');
                        bufferedOutputStream.write(sb.toString().getBytes(StandardCharsets.US_ASCII));
                    }
                }
                bufferedOutputStream.flush();
                file.finishWrite(fileOutputStream);
            } catch (IOException e) {
                e = e;
                if (fileOutputStream != null) {
                    file.failWrite(fileOutputStream);
                }
                Log.e("PackageManager", "Failed to write package usage times", e);
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void readInternal(Map<String, PackageSetting> map) {
        BufferedInputStream bufferedInputStream = null;
        try {
            try {
                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(getFile().openRead());
                try {
                    StringBuilder sb = new StringBuilder();
                    String readLine = readLine(bufferedInputStream2, sb);
                    if (readLine != null) {
                        if (USAGE_FILE_MAGIC_VERSION_1.equals(readLine)) {
                            readVersion1LP(map, bufferedInputStream2, sb);
                        } else {
                            readVersion0LP(map, bufferedInputStream2, sb, readLine);
                        }
                    }
                    IoUtils.closeQuietly(bufferedInputStream2);
                } catch (FileNotFoundException unused) {
                    bufferedInputStream = bufferedInputStream2;
                    this.mIsHistoricalPackageUsageAvailable = false;
                    IoUtils.closeQuietly(bufferedInputStream);
                } catch (IOException e) {
                    e = e;
                    bufferedInputStream = bufferedInputStream2;
                    Log.w("PackageManager", "Failed to read package usage times", e);
                    IoUtils.closeQuietly(bufferedInputStream);
                } catch (Throwable th) {
                    th = th;
                    bufferedInputStream = bufferedInputStream2;
                    IoUtils.closeQuietly(bufferedInputStream);
                    throw th;
                }
            } catch (FileNotFoundException unused2) {
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void readVersion0LP(Map<String, PackageSetting> map, InputStream inputStream, StringBuilder sb, String str) throws IOException {
        while (str != null) {
            String[] split = str.split(" ");
            if (split.length != 2) {
                throw new IOException("Failed to parse " + str + " as package-timestamp pair.");
            }
            PackageSetting packageSetting = map.get(split[0]);
            if (packageSetting != null) {
                long parseAsLong = parseAsLong(split[1]);
                for (int i = 0; i < 8; i++) {
                    packageSetting.getPkgState().setLastPackageUsageTimeInMills(i, parseAsLong);
                }
            }
            str = readLine(inputStream, sb);
        }
    }

    private void readVersion1LP(Map<String, PackageSetting> map, InputStream inputStream, StringBuilder sb) throws IOException {
        while (true) {
            String readLine = readLine(inputStream, sb);
            if (readLine == null) {
                return;
            }
            String[] split = readLine.split(" ");
            if (split.length != 9) {
                throw new IOException("Failed to parse " + readLine + " as a timestamp array.");
            }
            int i = 0;
            PackageSetting packageSetting = map.get(split[0]);
            if (packageSetting != null) {
                while (i < 8) {
                    int i2 = i + 1;
                    packageSetting.getPkgState().setLastPackageUsageTimeInMills(i, parseAsLong(split[i2]));
                    i = i2;
                }
            }
        }
    }

    private long parseAsLong(String str) throws IOException {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new IOException("Failed to parse " + str + " as a long.", e);
        }
    }

    private String readLine(InputStream inputStream, StringBuilder sb) throws IOException {
        return readToken(inputStream, sb, '\n');
    }

    private String readToken(InputStream inputStream, StringBuilder sb, char c) throws IOException {
        sb.setLength(0);
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                if (sb.length() == 0) {
                    return null;
                }
                throw new IOException("Unexpected EOF");
            }
            if (read == c) {
                return sb.toString();
            }
            sb.append((char) read);
        }
    }
}
