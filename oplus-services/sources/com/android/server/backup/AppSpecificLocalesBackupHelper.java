package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.util.Slog;
import com.android.server.LocalServices;
import com.android.server.locales.LocaleManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppSpecificLocalesBackupHelper extends BlobBackupHelper {
    private static final int BLOB_VERSION = 1;
    private static final boolean DEBUG = false;
    private static final String KEY_APP_LOCALES = "app_locales";
    private static final String TAG = "AppLocalesBackupHelper";
    private final LocaleManagerInternal mLocaleManagerInternal;
    private final int mUserId;

    public AppSpecificLocalesBackupHelper(int i) {
        super(1, new String[]{KEY_APP_LOCALES});
        this.mUserId = i;
        this.mLocaleManagerInternal = (LocaleManagerInternal) LocalServices.getService(LocaleManagerInternal.class);
    }

    protected byte[] getBackupPayload(String str) {
        if (KEY_APP_LOCALES.equals(str)) {
            try {
                return this.mLocaleManagerInternal.getBackupPayload(this.mUserId);
            } catch (Exception e) {
                Slog.e(TAG, "Couldn't communicate with locale manager", e);
                return null;
            }
        }
        Slog.w(TAG, "Unexpected backup key " + str);
        return null;
    }

    protected void applyRestoredPayload(String str, byte[] bArr) {
        if (KEY_APP_LOCALES.equals(str)) {
            try {
                this.mLocaleManagerInternal.stageAndApplyRestoredPayload(bArr, this.mUserId);
                return;
            } catch (Exception e) {
                Slog.e(TAG, "Couldn't communicate with locale manager", e);
                return;
            }
        }
        Slog.w(TAG, "Unexpected restore key " + str);
    }
}
