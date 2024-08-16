package s6;

import android.content.Context;
import android.os.Looper;
import b6.LocalLog;
import y5.AppFeature;

/* compiled from: ThermalFactory.java */
/* renamed from: s6.c, reason: use source file name */
/* loaded from: classes.dex */
public class ThermalFactory {

    /* renamed from: a, reason: collision with root package name */
    private static ThermalManager f18070a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ThermalFactory.java */
    /* renamed from: s6.c$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f18071a;

        static {
            int[] iArr = new int[b.values().length];
            f18071a = iArr;
            try {
                iArr[b.DIFF.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f18071a[b.TOLERANCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f18071a[b.ENV.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f18071a[b.CORE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* compiled from: ThermalFactory.java */
    /* renamed from: s6.c$b */
    /* loaded from: classes.dex */
    public enum b {
        CORE,
        TOLERANCE,
        ENV,
        DIFF
    }

    public static synchronized ThermalManager a(Context context, Looper looper) {
        ThermalManager thermalManager;
        synchronized (ThermalFactory.class) {
            if (f18070a == null) {
                if (AppFeature.t()) {
                    f18070a = b(b.DIFF, context, looper);
                } else if (AppFeature.u()) {
                    f18070a = b(b.TOLERANCE, context, looper);
                } else if (AppFeature.y()) {
                    f18070a = b(b.ENV, context, looper);
                } else {
                    f18070a = b(b.CORE, context, looper);
                }
                f18070a.t();
            }
            thermalManager = f18070a;
        }
        return thermalManager;
    }

    public static ThermalManager b(b bVar, Context context, Looper looper) {
        LocalLog.l("ThermalFactory", "create type " + bVar.name());
        int i10 = a.f18071a[bVar.ordinal()];
        if (i10 == 1) {
            return new DiffThermalManager(bVar, context, looper);
        }
        if (i10 == 2) {
            return new ToleranceThermalManager(bVar, context, looper);
        }
        if (i10 == 3) {
            return new EnvThermalManager(bVar, context, looper);
        }
        if (i10 != 4) {
            LocalLog.l("ThermalFactory", "Unrecognized type " + bVar.name());
            return null;
        }
        return new ThermalManager(bVar, context, looper);
    }
}
