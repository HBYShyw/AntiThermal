package t6;

import android.content.Context;
import android.content.SharedPreferences;
import u6.ThermalView;

/* compiled from: ThermalPresenter.java */
/* renamed from: t6.f, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ThermalPresenter implements IThermalPresenter {

    /* renamed from: a, reason: collision with root package name */
    protected Context f18629a;

    /* renamed from: b, reason: collision with root package name */
    protected Context f18630b;

    /* renamed from: c, reason: collision with root package name */
    protected SharedPreferences f18631c;

    /* renamed from: d, reason: collision with root package name */
    protected SharedPreferences.Editor f18632d;

    /* renamed from: e, reason: collision with root package name */
    protected ThermalView f18633e;

    public ThermalPresenter(Context context) {
        Context createDeviceProtectedStorageContext = context.createDeviceProtectedStorageContext();
        this.f18630b = createDeviceProtectedStorageContext;
        SharedPreferences sharedPreferences = createDeviceProtectedStorageContext.getSharedPreferences("high_temperature", 0);
        this.f18631c = sharedPreferences;
        this.f18632d = sharedPreferences.edit();
        this.f18629a = context;
        this.f18633e = new ThermalView(context);
    }
}
