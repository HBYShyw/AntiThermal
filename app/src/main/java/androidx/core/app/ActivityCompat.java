package androidx.core.app;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import java.util.Arrays;
import java.util.HashSet;

/* compiled from: ActivityCompat.java */
/* renamed from: androidx.core.app.a, reason: use source file name */
/* loaded from: classes.dex */
public class ActivityCompat extends ContextCompat {

    /* renamed from: c, reason: collision with root package name */
    private static d f2111c;

    /* compiled from: ActivityCompat.java */
    /* renamed from: androidx.core.app.a$a */
    /* loaded from: classes.dex */
    static class a {
        static void a(Activity activity) {
            activity.finishAffinity();
        }

        static void b(Activity activity, Intent intent, int i10, Bundle bundle) {
            activity.startActivityForResult(intent, i10, bundle);
        }

        static void c(Activity activity, IntentSender intentSender, int i10, Intent intent, int i11, int i12, int i13, Bundle bundle) {
            activity.startIntentSenderForResult(intentSender, i10, intent, i11, i12, i13, bundle);
        }
    }

    /* compiled from: ActivityCompat.java */
    /* renamed from: androidx.core.app.a$b */
    /* loaded from: classes.dex */
    static class b {
        static void a(Activity activity) {
            activity.finishAfterTransition();
        }

        static void b(Activity activity) {
            activity.postponeEnterTransition();
        }

        static void c(Activity activity, SharedElementCallback sharedElementCallback) {
            activity.setEnterSharedElementCallback(sharedElementCallback);
        }

        static void d(Activity activity, SharedElementCallback sharedElementCallback) {
            activity.setExitSharedElementCallback(sharedElementCallback);
        }

        static void e(Activity activity) {
            activity.startPostponedEnterTransition();
        }
    }

    /* compiled from: ActivityCompat.java */
    /* renamed from: androidx.core.app.a$c */
    /* loaded from: classes.dex */
    static class c {
        static void a(Object obj) {
            ((SharedElementCallback.OnSharedElementsReadyListener) obj).onSharedElementsReady();
        }

        static void b(Activity activity, String[] strArr, int i10) {
            activity.requestPermissions(strArr, i10);
        }

        static boolean c(Activity activity, String str) {
            return activity.shouldShowRequestPermissionRationale(str);
        }
    }

    /* compiled from: ActivityCompat.java */
    /* renamed from: androidx.core.app.a$d */
    /* loaded from: classes.dex */
    public interface d {
        boolean a(Activity activity, String[] strArr, int i10);
    }

    /* compiled from: ActivityCompat.java */
    /* renamed from: androidx.core.app.a$e */
    /* loaded from: classes.dex */
    public interface e {
        void validateRequestPermissionsRequestCode(int i10);
    }

    public static void j(Activity activity) {
        a.a(activity);
    }

    public static void k(Activity activity) {
        b.a(activity);
    }

    public static void l(Activity activity) {
        b.b(activity);
    }

    public static void m(Activity activity) {
        activity.recreate();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void n(Activity activity, String[] strArr, int i10) {
        d dVar = f2111c;
        if (dVar == null || !dVar.a(activity, strArr, i10)) {
            HashSet hashSet = new HashSet();
            for (int i11 = 0; i11 < strArr.length; i11++) {
                if (!TextUtils.isEmpty(strArr[i11])) {
                    if (!BuildCompat.c() && TextUtils.equals(strArr[i11], "android.permission.POST_NOTIFICATIONS")) {
                        hashSet.add(Integer.valueOf(i11));
                    }
                } else {
                    throw new IllegalArgumentException("Permission request for permissions " + Arrays.toString(strArr) + " must not contain null or empty values");
                }
            }
            int size = hashSet.size();
            String[] strArr2 = size > 0 ? new String[strArr.length - size] : strArr;
            if (size > 0) {
                if (size == strArr.length) {
                    return;
                }
                int i12 = 0;
                for (int i13 = 0; i13 < strArr.length; i13++) {
                    if (!hashSet.contains(Integer.valueOf(i13))) {
                        strArr2[i12] = strArr[i13];
                        i12++;
                    }
                }
            }
            if (activity instanceof e) {
                ((e) activity).validateRequestPermissionsRequestCode(i10);
            }
            c.b(activity, strArr, i10);
        }
    }

    public static void o(Activity activity, SharedElementCallback sharedElementCallback) {
        b.c(activity, null);
    }

    public static void p(Activity activity, SharedElementCallback sharedElementCallback) {
        b.d(activity, null);
    }

    public static boolean q(Activity activity, String str) {
        if (BuildCompat.c() || !TextUtils.equals("android.permission.POST_NOTIFICATIONS", str)) {
            return c.c(activity, str);
        }
        return false;
    }

    public static void r(Activity activity, Intent intent, int i10, Bundle bundle) {
        a.b(activity, intent, i10, bundle);
    }

    public static void s(Activity activity, IntentSender intentSender, int i10, Intent intent, int i11, int i12, int i13, Bundle bundle) {
        a.c(activity, intentSender, i10, intent, i11, i12, i13, bundle);
    }

    public static void t(Activity activity) {
        b.e(activity);
    }
}
