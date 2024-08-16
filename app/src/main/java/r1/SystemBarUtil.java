package r1;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import androidx.core.view.WindowInsetsControllerCompat;
import kotlin.Metadata;
import ma.Unit;
import w1.COUIDarkModeUtil;
import za.k;

/* compiled from: SystemBarUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u0018\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0003J\u0010\u0010\f\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\u0006H\u0007¨\u0006\u000f"}, d2 = {"Lr1/c;", "", "Landroid/content/Context;", "context", "", "c", "Landroid/app/Activity;", "activity", "Landroid/view/WindowInsets;", "insets", "Lma/f0;", "b", "d", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: r1.c, reason: use source file name */
/* loaded from: classes.dex */
public final class SystemBarUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final SystemBarUtil f17468a = new SystemBarUtil();

    private SystemBarUtil() {
    }

    private static final void b(Activity activity, WindowInsets windowInsets) {
        int i10 = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        if (c(activity)) {
            i10 = 0;
        }
        activity.findViewById(R.id.content).setPadding(0, 0, 0, i10);
    }

    public static final boolean c(Context context) {
        k.e(context, "context");
        int i10 = Settings.Secure.getInt(context.getContentResolver(), "hide_navigationbar_enable", 0);
        return i10 == 2 || i10 == 3;
    }

    public static final void d(final Activity activity) {
        k.e(activity, "activity");
        Window window = activity.getWindow();
        window.setDecorFitsSystemWindows(false);
        window.setNavigationBarContrastEnforced(false);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.layoutInDisplayCutoutMode = 3;
        Unit unit = Unit.f15173a;
        window.setAttributes(attributes);
        window.getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: r1.b
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                WindowInsets e10;
                e10 = SystemBarUtil.e(activity, view, windowInsets);
                return e10;
            }
        });
        window.setStatusBarColor(0);
        window.setNavigationBarColor(0);
        new WindowInsetsControllerCompat(activity.getWindow(), window.getDecorView()).b(!COUIDarkModeUtil.a(activity));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final WindowInsets e(Activity activity, View view, WindowInsets windowInsets) {
        k.e(activity, "$activity");
        k.d(windowInsets, "insets");
        b(activity, windowInsets);
        return windowInsets;
    }
}
