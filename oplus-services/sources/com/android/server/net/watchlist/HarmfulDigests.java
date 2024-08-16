package com.android.server.net.watchlist;

import com.android.internal.util.HexDump;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class HarmfulDigests {
    private final Set<String> mDigestSet;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HarmfulDigests(List<byte[]> list) {
        HashSet hashSet = new HashSet();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            hashSet.add(HexDump.toHexString(list.get(i)));
        }
        this.mDigestSet = Collections.unmodifiableSet(hashSet);
    }

    public boolean contains(byte[] bArr) {
        return this.mDigestSet.contains(HexDump.toHexString(bArr));
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Iterator<String> it = this.mDigestSet.iterator();
        while (it.hasNext()) {
            printWriter.println(it.next());
        }
        printWriter.println("");
    }
}
