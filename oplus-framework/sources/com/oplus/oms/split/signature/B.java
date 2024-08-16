package com.oplus.oms.split.signature;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/* loaded from: classes.dex */
final class B implements A {
    private final long bLong;
    private final long cLong;
    private final FileChannel fileChannel;

    /* JADX INFO: Access modifiers changed from: package-private */
    public B(FileChannel var1, long var2, long var4) {
        this.fileChannel = var1;
        this.bLong = var2;
        this.cLong = var4;
    }

    @Override // com.oplus.oms.split.signature.A
    public long a() {
        return this.cLong;
    }

    @Override // com.oplus.oms.split.signature.A
    public void a(MessageDigest[] var1, long var2, int var4) throws IOException {
        MappedByteBuffer var7;
        long var5 = this.bLong + var2;
        FileChannel fileChannel = this.fileChannel;
        if (fileChannel == null || (var7 = fileChannel.map(FileChannel.MapMode.READ_ONLY, var5, var4)) == null || var1 == null) {
            return;
        }
        var7.load();
        for (MessageDigest var11 : var1) {
            var7.position(0);
            var11.update(var7);
        }
    }
}
