package com.android.server.wm;

import android.os.Environment;
import com.android.server.wm.BaseAppSnapshotPersister;
import java.io.File;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final /* synthetic */ class ActivitySnapshotController$$ExternalSyntheticLambda0 implements BaseAppSnapshotPersister.DirectoryResolver {
    @Override // com.android.server.wm.BaseAppSnapshotPersister.DirectoryResolver
    public final File getSystemDirectoryForUser(int i) {
        return Environment.getDataSystemCeDirectory(i);
    }
}
