package kc;

import java.io.InputStream;
import jc.m;
import ma.o;
import ma.u;
import qc.g;
import wa.Closeable;
import za.k;

/* compiled from: readPackageFragment.kt */
/* renamed from: kc.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class readPackageFragment {
    public static final o<m, BuiltInsBinaryVersion> a(InputStream inputStream) {
        m mVar;
        k.e(inputStream, "<this>");
        try {
            BuiltInsBinaryVersion a10 = BuiltInsBinaryVersion.f14279g.a(inputStream);
            if (a10.h()) {
                g d10 = g.d();
                BuiltInsProtoBuf.a(d10);
                mVar = m.R(inputStream, d10);
            } else {
                mVar = null;
            }
            o<m, BuiltInsBinaryVersion> a11 = u.a(mVar, a10);
            Closeable.a(inputStream, null);
            return a11;
        } finally {
        }
    }
}
