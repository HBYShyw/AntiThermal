package d0;

import c0.SupportSQLiteOpenHelper;

/* compiled from: FrameworkSQLiteOpenHelperFactory.java */
/* renamed from: d0.c, reason: use source file name */
/* loaded from: classes.dex */
public final class FrameworkSQLiteOpenHelperFactory implements SupportSQLiteOpenHelper.c {
    @Override // c0.SupportSQLiteOpenHelper.c
    public SupportSQLiteOpenHelper a(SupportSQLiteOpenHelper.b bVar) {
        return new FrameworkSQLiteOpenHelper(bVar.f4725a, bVar.f4726b, bVar.f4727c);
    }
}
