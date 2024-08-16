package com.android.server.app;

import android.content.pm.PackageManager;
import android.os.UserHandle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class GameClassifierImpl implements GameClassifier {
    private final PackageManager mPackageManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameClassifierImpl(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    @Override // com.android.server.app.GameClassifier
    public boolean isGame(String str, UserHandle userHandle) {
        try {
            return this.mPackageManager.getApplicationInfoAsUser(str, 0, userHandle.getIdentifier()).category == 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
