package com.oplus.epona;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public interface Dumper {
    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    String key();
}
