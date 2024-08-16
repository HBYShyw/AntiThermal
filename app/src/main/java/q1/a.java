package q1;

import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.WindowCompat;
import com.coui.appcompat.baseview.base.COUIBaseActivity;
import com.coui.appcompat.theme.COUIThemeOverlay;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import kotlin.Metadata;
import r1.FoldSettingsHelper;
import r1.SystemBarUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ActivityDelegate.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\u000f\u0012\u0006\u0010\u0013\u001a\u00020\u0012¢\u0006\u0004\b\u0014\u0010\u0015J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\u0006\u0010\u0004\u001a\u00020\u0002J\u0006\u0010\u0005\u001a\u00020\u0002J-\u0010\r\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00062\u000e\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\t0\b2\u0006\u0010\f\u001a\u00020\u000b¢\u0006\u0004\b\r\u0010\u000eJ\u000e\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u000f¨\u0006\u0016"}, d2 = {"Lq1/a;", "", "Lma/f0;", "e", "a", "b", "", "requestCode", "", "", "permissions", "", "grantResults", "d", "(I[Ljava/lang/String;[I)V", "Landroid/view/MenuItem;", "item", "c", "Lcom/coui/appcompat/baseview/base/COUIBaseActivity;", "activity", "<init>", "(Lcom/coui/appcompat/baseview/base/COUIBaseActivity;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class a {

    /* renamed from: f, reason: collision with root package name */
    public static final C0087a f16791f = new C0087a(null);

    /* renamed from: a, reason: collision with root package name */
    private final COUIBaseActivity f16792a;

    /* renamed from: b, reason: collision with root package name */
    private final ArrayList<String> f16793b;

    /* renamed from: c, reason: collision with root package name */
    private final ArrayList<String> f16794c;

    /* renamed from: d, reason: collision with root package name */
    private final ArrayList<String> f16795d;

    /* renamed from: e, reason: collision with root package name */
    private b f16796e;

    /* compiled from: ActivityDelegate.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lq1/a$a;", "", "", "REQUEST_PERMISSION_CODE", "I", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: q1.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0087a {
        private C0087a() {
        }

        public /* synthetic */ C0087a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: ActivityDelegate.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\n"}, d2 = {"Lq1/a$b;", "Lr1/a$a;", "", "foldStatus", "Lma/f0;", "a", "Lcom/coui/appcompat/baseview/base/COUIBaseActivity;", "activity", "<init>", "(Lcom/coui/appcompat/baseview/base/COUIBaseActivity;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    private static final class b implements FoldSettingsHelper.a {

        /* renamed from: a, reason: collision with root package name */
        private final WeakReference<COUIBaseActivity> f16797a;

        public b(COUIBaseActivity cOUIBaseActivity) {
            k.e(cOUIBaseActivity, "activity");
            this.f16797a = new WeakReference<>(cOUIBaseActivity);
        }

        @Override // r1.FoldSettingsHelper.a
        public void a(int i10) {
            COUIBaseActivity cOUIBaseActivity = this.f16797a.get();
            if (cOUIBaseActivity == null) {
                return;
            }
            cOUIBaseActivity.setRequestedOrientation(cOUIBaseActivity.g(i10));
            cOUIBaseActivity.h(i10);
        }
    }

    public a(COUIBaseActivity cOUIBaseActivity) {
        k.e(cOUIBaseActivity, "activity");
        this.f16792a = cOUIBaseActivity;
        this.f16793b = new ArrayList<>();
        this.f16794c = new ArrayList<>();
        this.f16795d = new ArrayList<>();
    }

    private final void e() {
        this.f16792a.k(this.f16795d);
    }

    public final void a() {
        int statusType = this.f16792a.getStatusType();
        if (statusType == 0) {
            SystemBarUtil systemBarUtil = SystemBarUtil.f17468a;
            SystemBarUtil.d(this.f16792a);
            ActionBar supportActionBar = this.f16792a.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.s(this.f16792a.isHomeAsUpEnabled());
            }
        } else if (statusType == 1) {
            WindowCompat.b(this.f16792a.getWindow(), false);
            this.f16792a.getWindow().setStatusBarColor(0);
        }
        COUIThemeOverlay.i().b(this.f16792a);
        if (this.f16792a.f()) {
            FoldSettingsHelper foldSettingsHelper = FoldSettingsHelper.f17462a;
            foldSettingsHelper.d(this.f16792a);
            if (foldSettingsHelper.e()) {
                COUIBaseActivity cOUIBaseActivity = this.f16792a;
                cOUIBaseActivity.setRequestedOrientation(cOUIBaseActivity.g(foldSettingsHelper.c()));
                b bVar = new b(this.f16792a);
                this.f16796e = bVar;
                foldSettingsHelper.f(bVar);
            }
        }
    }

    public final void b() {
        if (this.f16792a.f()) {
            FoldSettingsHelper foldSettingsHelper = FoldSettingsHelper.f17462a;
            if (foldSettingsHelper.e()) {
                b bVar = this.f16796e;
                if (bVar != null) {
                    foldSettingsHelper.h(bVar);
                } else {
                    k.s("observer");
                    throw null;
                }
            }
        }
    }

    public final void c(MenuItem menuItem) {
        k.e(menuItem, "item");
        if (menuItem.getItemId() == 16908332) {
            this.f16792a.finish();
        }
    }

    public final void d(int requestCode, String[] permissions, int[] grantResults) {
        k.e(permissions, "permissions");
        k.e(grantResults, "grantResults");
        if (requestCode == 1000) {
            int i10 = 0;
            if (!(grantResults.length == 0)) {
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<String> arrayList2 = new ArrayList<>();
                int length = permissions.length - 1;
                if (length >= 0) {
                    while (true) {
                        int i11 = i10 + 1;
                        if (grantResults[i10] == 0) {
                            arrayList.add(permissions[i10]);
                        } else {
                            arrayList2.add(permissions[i10]);
                        }
                        if (i11 > length) {
                            break;
                        } else {
                            i10 = i11;
                        }
                    }
                }
                this.f16792a.i(arrayList);
                this.f16792a.j(arrayList2);
            }
        }
        e();
    }
}
