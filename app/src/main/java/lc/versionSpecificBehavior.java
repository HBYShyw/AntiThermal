package lc;

import za.k;

/* compiled from: versionSpecificBehavior.kt */
/* renamed from: lc.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class versionSpecificBehavior {
    public static final boolean a(BinaryVersion binaryVersion) {
        k.e(binaryVersion, "version");
        return binaryVersion.a() == 1 && binaryVersion.b() >= 4;
    }

    public static final boolean b(BinaryVersion binaryVersion) {
        k.e(binaryVersion, "version");
        return a(binaryVersion);
    }
}
