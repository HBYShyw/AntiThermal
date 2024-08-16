package r1;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import b2.COUILog;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import za.k;

/* compiled from: FoldSettingsHelper.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0017B\t\b\u0002¢\u0006\u0004\b\u0015\u0010\u0016J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006R\"\u0010\u000b\u001a\u00020\n8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0014\u001a\u00020\u00118F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0018"}, d2 = {"Lr1/a;", "", "Landroid/content/Context;", "context", "Lma/f0;", "d", "Lr1/a$a;", "observer", "f", "h", "", "foldStatus", "I", "c", "()I", "g", "(I)V", "", "e", "()Z", "isSupportFoldScreen", "<init>", "()V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: r1.a, reason: use source file name */
/* loaded from: classes.dex */
public final class FoldSettingsHelper {

    /* renamed from: c, reason: collision with root package name */
    private static ContentObserver f17464c;

    /* renamed from: e, reason: collision with root package name */
    private static Context f17466e;

    /* renamed from: a, reason: collision with root package name */
    public static final FoldSettingsHelper f17462a = new FoldSettingsHelper();

    /* renamed from: b, reason: collision with root package name */
    private static int f17463b = -1;

    /* renamed from: d, reason: collision with root package name */
    private static final ArrayList<a> f17465d = new ArrayList<>();

    /* compiled from: FoldSettingsHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0006"}, d2 = {"Lr1/a$a;", "", "", "foldStatus", "Lma/f0;", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: r1.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(int i10);
    }

    /* compiled from: FoldSettingsHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"r1/a$b", "Landroid/database/ContentObserver;", "", "selfChange", "Lma/f0;", "onChange", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: r1.a$b */
    /* loaded from: classes.dex */
    public static final class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            FoldSettingsHelper foldSettingsHelper = FoldSettingsHelper.f17462a;
            Context context = FoldSettingsHelper.f17466e;
            if (context != null) {
                foldSettingsHelper.g(Settings.Global.getInt(context.getContentResolver(), "oplus_system_folding_mode", -1));
                COUILog.a("FoldSettingsHelper", k.l("FoldSettings.onChange=", Integer.valueOf(foldSettingsHelper.c())));
                Iterator it = FoldSettingsHelper.f17465d.iterator();
                while (it.hasNext()) {
                    ((a) it.next()).a(FoldSettingsHelper.f17462a.c());
                }
                return;
            }
            k.s("appContext");
            throw null;
        }
    }

    private FoldSettingsHelper() {
    }

    public final int c() {
        return f17463b;
    }

    public final void d(Context context) {
        k.e(context, "context");
        Context applicationContext = context.getApplicationContext();
        k.d(applicationContext, "context.applicationContext");
        f17466e = applicationContext;
        if (applicationContext != null) {
            f17463b = Settings.Global.getInt(applicationContext.getContentResolver(), "oplus_system_folding_mode", -1);
        } else {
            k.s("appContext");
            throw null;
        }
    }

    public final boolean e() {
        return f17463b != -1;
    }

    public final void f(a aVar) {
        k.e(aVar, "observer");
        if (f17464c == null) {
            b bVar = new b(new Handler(Looper.getMainLooper()));
            Context context = f17466e;
            if (context != null) {
                context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("oplus_system_folding_mode"), false, bVar);
                f17464c = bVar;
            } else {
                k.s("appContext");
                throw null;
            }
        }
        f17465d.add(aVar);
    }

    public final void g(int i10) {
        f17463b = i10;
    }

    public final void h(a aVar) {
        k.e(aVar, "observer");
        ArrayList<a> arrayList = f17465d;
        arrayList.remove(aVar);
        if (arrayList.isEmpty()) {
            ContentObserver contentObserver = f17464c;
            if (contentObserver != null) {
                Context context = f17466e;
                if (context == null) {
                    k.s("appContext");
                    throw null;
                }
                context.getContentResolver().unregisterContentObserver(contentObserver);
            }
            f17464c = null;
        }
    }
}
