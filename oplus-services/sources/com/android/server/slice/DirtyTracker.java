package com.android.server.slice;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface DirtyTracker {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Persistable {
        String getFileName();

        void writeTo(XmlSerializer xmlSerializer) throws IOException;
    }

    void onPersistableDirty(Persistable persistable);
}
