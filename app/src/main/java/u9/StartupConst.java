package u9;

import android.net.Uri;
import android.os.OplusBaseEnvironment;
import java.io.File;

/* compiled from: StartupConst.java */
/* renamed from: u9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupConst {

    /* renamed from: a, reason: collision with root package name */
    public static final Uri f18931a = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* renamed from: b, reason: collision with root package name */
    public static final Uri f18932b = Uri.parse("content://com.oplus.startup.provider");

    /* renamed from: c, reason: collision with root package name */
    public static final String f18933c;

    /* renamed from: d, reason: collision with root package name */
    public static final String f18934d;

    /* renamed from: e, reason: collision with root package name */
    public static final String f18935e;

    /* renamed from: f, reason: collision with root package name */
    public static final int[] f18936f;

    static {
        String absolutePath = OplusBaseEnvironment.getMyRegionDirectory().getAbsolutePath();
        f18933c = absolutePath;
        f18934d = absolutePath + "/etc/startup/sys_startup_v3_config_list.xml";
        StringBuilder sb2 = new StringBuilder();
        String str = File.separator;
        sb2.append(str);
        sb2.append("data");
        sb2.append(str);
        sb2.append("oplus");
        sb2.append(str);
        sb2.append("os");
        sb2.append(str);
        sb2.append("startup");
        sb2.append(str);
        f18935e = sb2.toString();
        f18936f = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};
    }
}
