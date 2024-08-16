package com.android.server.backup;

import android.app.backup.BackupDataOutput;
import android.app.backup.BlobBackupHelper;
import android.os.ParcelFileDescriptor;
import com.android.server.LocalServices;
import com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppGrammaticalGenderBackupHelper extends BlobBackupHelper {
    private static final int BLOB_VERSION = 1;
    private static final String KEY_APP_GENDER = "app_gender";
    private final GrammaticalInflectionManagerInternal mGrammarInflectionManagerInternal;
    private final int mUserId;

    public AppGrammaticalGenderBackupHelper(int i) {
        super(1, new String[]{KEY_APP_GENDER});
        this.mUserId = i;
        this.mGrammarInflectionManagerInternal = (GrammaticalInflectionManagerInternal) LocalServices.getService(GrammaticalInflectionManagerInternal.class);
    }

    public void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
        if ((backupDataOutput.getTransportFlags() & 1) == 0) {
            return;
        }
        super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
    }

    protected byte[] getBackupPayload(String str) {
        GrammaticalInflectionManagerInternal grammaticalInflectionManagerInternal;
        if (!KEY_APP_GENDER.equals(str) || (grammaticalInflectionManagerInternal = this.mGrammarInflectionManagerInternal) == null) {
            return null;
        }
        return grammaticalInflectionManagerInternal.getBackupPayload(this.mUserId);
    }

    protected void applyRestoredPayload(String str, byte[] bArr) {
        GrammaticalInflectionManagerInternal grammaticalInflectionManagerInternal;
        if (!KEY_APP_GENDER.equals(str) || (grammaticalInflectionManagerInternal = this.mGrammarInflectionManagerInternal) == null) {
            return;
        }
        grammaticalInflectionManagerInternal.stageAndApplyRestoredPayload(bArr, this.mUserId);
    }
}
