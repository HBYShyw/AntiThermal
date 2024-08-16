package com.android.server.updates;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LangIdInstallReceiver extends ConfigUpdateInstallReceiver {
    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected boolean verifyVersion(int i, int i2) {
        return true;
    }

    public LangIdInstallReceiver() {
        super("/data/misc/textclassifier/", "lang_id.model", "metadata/lang_id", "version");
    }
}
