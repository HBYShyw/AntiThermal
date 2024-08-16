package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.util.Slog;
import com.android.server.LocalServices;
import com.android.server.people.PeopleServiceInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class PeopleBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_CONVERSATIONS = "people_conversation_infos";
    private static final int STATE_VERSION = 1;
    private static final String TAG = PeopleBackupHelper.class.getSimpleName();
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PeopleBackupHelper(int i) {
        super(1, new String[]{KEY_CONVERSATIONS});
        this.mUserId = i;
    }

    protected byte[] getBackupPayload(String str) {
        if (!KEY_CONVERSATIONS.equals(str)) {
            Slog.w(TAG, "Unexpected backup key " + str);
            return new byte[0];
        }
        return ((PeopleServiceInternal) LocalServices.getService(PeopleServiceInternal.class)).getBackupPayload(this.mUserId);
    }

    protected void applyRestoredPayload(String str, byte[] bArr) {
        if (!KEY_CONVERSATIONS.equals(str)) {
            Slog.w(TAG, "Unexpected restore key " + str);
            return;
        }
        ((PeopleServiceInternal) LocalServices.getService(PeopleServiceInternal.class)).restore(this.mUserId, bArr);
    }
}
