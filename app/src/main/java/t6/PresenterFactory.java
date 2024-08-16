package t6;

import android.content.Context;

/* compiled from: PresenterFactory.java */
/* renamed from: t6.d, reason: use source file name */
/* loaded from: classes.dex */
public class PresenterFactory {

    /* renamed from: a, reason: collision with root package name */
    private static volatile ThermalPresenter f18624a;

    /* renamed from: b, reason: collision with root package name */
    private static volatile ThermalPresenter f18625b;

    /* renamed from: c, reason: collision with root package name */
    private static volatile ThermalPresenter f18626c;

    public static ThermalPresenter a(Context context, int i10) {
        if (i10 == 1) {
            if (f18624a == null) {
                synchronized (PresenterFactory.class) {
                    if (f18624a == null) {
                        f18624a = new HotSpotPresenter(context);
                    }
                }
            }
            return f18624a;
        }
        if (i10 == 2) {
            if (f18625b == null) {
                synchronized (PresenterFactory.class) {
                    if (f18625b == null) {
                        f18625b = new ScreenPresenter(context);
                    }
                }
            }
            return f18625b;
        }
        if (i10 != 4) {
            return null;
        }
        if (f18626c == null) {
            synchronized (PresenterFactory.class) {
                if (f18626c == null) {
                    f18626c = new HotScreenPresenter(context);
                }
            }
        }
        return f18626c;
    }
}
