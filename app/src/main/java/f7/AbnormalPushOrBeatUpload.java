package f7;

import android.content.Context;
import b6.LocalLog;
import java.util.HashMap;
import x5.UploadDataUtil;

/* compiled from: AbnormalPushOrBeatUpload.java */
/* renamed from: f7.a, reason: use source file name */
/* loaded from: classes.dex */
public class AbnormalPushOrBeatUpload {

    /* renamed from: b, reason: collision with root package name */
    private static final String f11397b = "a";

    /* renamed from: a, reason: collision with root package name */
    private Context f11398a;

    public AbnormalPushOrBeatUpload(Context context) {
        this.f11398a = context;
    }

    public void a(String str, int i10, int i11) {
        HashMap hashMap = new HashMap();
        hashMap.put("pkgName", str);
        hashMap.put("message_period", Long.valueOf(i10));
        hashMap.put("messageType", Long.valueOf(i11));
        UploadDataUtil.S0(this.f11398a).q0(hashMap);
        LocalLog.a(f11397b, "upload: pkgName = " + str + ", message_period = " + i10 + ", messageType = " + i11);
    }
}
