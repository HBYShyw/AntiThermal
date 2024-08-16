package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.app.usage.UsageStatsManagerInternal;
import com.android.server.LocalServices;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UsageStatsBackupHelper extends BlobBackupHelper {
    static final int BLOB_VERSION = 1;
    static final boolean DEBUG = false;
    static final String KEY_USAGE_STATS = "usage_stats";
    static final String TAG = "UsgStatsBackupHelper";
    private final int mUserId;

    public UsageStatsBackupHelper(int i) {
        super(1, new String[]{KEY_USAGE_STATS});
        this.mUserId = i;
    }

    protected byte[] getBackupPayload(String str) {
        if (!KEY_USAGE_STATS.equals(str)) {
            return null;
        }
        UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeInt(0);
            dataOutputStream.write(usageStatsManagerInternal.getBackupPayload(this.mUserId, str));
        } catch (IOException unused) {
            byteArrayOutputStream.reset();
        }
        return byteArrayOutputStream.toByteArray();
    }

    protected void applyRestoredPayload(String str, byte[] bArr) {
        if (KEY_USAGE_STATS.equals(str)) {
            UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
            try {
                dataInputStream.readInt();
                int length = bArr.length - 4;
                byte[] bArr2 = new byte[length];
                dataInputStream.read(bArr2, 0, length);
                usageStatsManagerInternal.applyRestoredPayload(this.mUserId, str, bArr2);
            } catch (IOException unused) {
            }
        }
    }
}
