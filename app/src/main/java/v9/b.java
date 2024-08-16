package v9;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

/* compiled from: ActivityDelegate.java */
/* loaded from: classes2.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private final Activity f19192a;

    /* renamed from: b, reason: collision with root package name */
    private final a f19193b;

    /* JADX WARN: Multi-variable type inference failed */
    public b(Activity activity) {
        this.f19192a = activity;
        a aVar = (a) activity;
        this.f19193b = aVar;
        if (aVar.getStatusType() == 2) {
            activity.getWindow().addFlags(67108864);
        }
    }

    public static String a(String str) {
        return str.split("\\/")[r1.length - 1];
    }

    public static void e(Activity activity, int i10) {
        int a10 = x9.c.a();
        if ((a10 >= 6 || a10 == 0) && i10 == 1) {
            View decorView = activity.getWindow().getDecorView();
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 8192);
        }
    }

    public static void f(Activity activity) {
        String charSequence = activity.getTitle().toString();
        Log.d("ActivityDelegate", "setTitle fullTitle:" + charSequence);
        activity.setTitle(a(charSequence));
    }

    public void b() {
        e(this.f19192a, this.f19193b.getStatusType());
    }

    public void c(AppCompatDelegate appCompatDelegate) {
        ActionBar s7 = appCompatDelegate.s();
        if (s7 != null) {
            s7.s(this.f19193b.isHomeAsUpEnabled());
        }
        if (this.f19193b.isTitleNeedUpdate()) {
            f(this.f19192a);
        }
    }

    public void d(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return;
        }
        this.f19192a.finish();
    }
}
