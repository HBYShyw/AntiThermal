package x8;

import android.net.Uri;

/* compiled from: DataBaseUtil.java */
/* renamed from: x8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class DataBaseUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final Uri f19649a;

    /* renamed from: b, reason: collision with root package name */
    public static final Uri f19650b;

    /* renamed from: c, reason: collision with root package name */
    public static final Uri f19651c;

    static {
        Uri parse = Uri.parse("content://com.oplus.powermanager");
        f19649a = parse;
        f19650b = Uri.withAppendedPath(parse, "battery_stats_list");
        f19651c = Uri.withAppendedPath(parse, "app_label_list");
    }
}
