package e7;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.ArrayList;
import java.util.List;

/* compiled from: SimpleAppTypeUtil.java */
/* renamed from: e7.b, reason: use source file name */
/* loaded from: classes.dex */
public class SimpleAppTypeUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final ArrayList<String> f10972a = new a();

    /* compiled from: SimpleAppTypeUtil.java */
    /* renamed from: e7.b$a */
    /* loaded from: classes.dex */
    class a extends ArrayList<String> {
        a() {
            add("com.android.bluetooth");
            add("com.baidu.netdisk");
            add("com.android.mms");
        }
    }

    public static ArrayList<String> a(Context context) {
        PackageManager packageManager;
        String str;
        ArrayList<String> arrayList = new ArrayList<>();
        if (context == null || (packageManager = context.getPackageManager()) == null) {
            return arrayList;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/*;image/*;audio/*;video/*");
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"text/*", "image/*", "audio/*", "video/*"});
        List<ResolveInfo> queryIntentActivitiesAsUser = packageManager.queryIntentActivitiesAsUser(intent, 65536, context.getUserId());
        if (queryIntentActivitiesAsUser == null) {
            return arrayList;
        }
        for (ResolveInfo resolveInfo : queryIntentActivitiesAsUser) {
            ComponentInfo componentInfo = resolveInfo.activityInfo;
            if (componentInfo == null) {
                componentInfo = resolveInfo.serviceInfo;
            }
            if (componentInfo != null && (str = componentInfo.packageName) != null && !arrayList.contains(str)) {
                arrayList.add(componentInfo.packageName);
            }
        }
        arrayList.removeAll(f10972a);
        return arrayList;
    }
}
