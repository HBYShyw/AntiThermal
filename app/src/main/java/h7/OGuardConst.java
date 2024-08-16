package h7;

import android.net.Uri;

/* compiled from: OGuardConst.java */
/* renamed from: h7.a, reason: use source file name */
/* loaded from: classes.dex */
public class OGuardConst {

    /* renamed from: a, reason: collision with root package name */
    public static final Uri f12013a;

    /* renamed from: b, reason: collision with root package name */
    public static final Uri f12014b;

    static {
        Uri parse = Uri.parse("content://com.oplus.oguard.provider");
        f12013a = parse;
        f12014b = Uri.withAppendedPath(parse, "app_info");
    }
}
