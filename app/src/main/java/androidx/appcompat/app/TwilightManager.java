package androidx.appcompat.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import androidx.core.content.PermissionChecker;
import com.oplus.statistics.util.TimeInfoUtil;
import java.util.Calendar;

/* compiled from: TwilightManager.java */
/* renamed from: androidx.appcompat.app.p, reason: use source file name */
/* loaded from: classes.dex */
class TwilightManager {

    /* renamed from: d, reason: collision with root package name */
    private static TwilightManager f516d;

    /* renamed from: a, reason: collision with root package name */
    private final Context f517a;

    /* renamed from: b, reason: collision with root package name */
    private final LocationManager f518b;

    /* renamed from: c, reason: collision with root package name */
    private final a f519c = new a();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TwilightManager.java */
    /* renamed from: androidx.appcompat.app.p$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        boolean f520a;

        /* renamed from: b, reason: collision with root package name */
        long f521b;

        a() {
        }
    }

    TwilightManager(Context context, LocationManager locationManager) {
        this.f517a = context;
        this.f518b = locationManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TwilightManager a(Context context) {
        if (f516d == null) {
            Context applicationContext = context.getApplicationContext();
            f516d = new TwilightManager(applicationContext, (LocationManager) applicationContext.getSystemService("location"));
        }
        return f516d;
    }

    @SuppressLint({"MissingPermission"})
    private Location b() {
        Location c10 = PermissionChecker.b(this.f517a, "android.permission.ACCESS_COARSE_LOCATION") == 0 ? c("network") : null;
        Location c11 = PermissionChecker.b(this.f517a, "android.permission.ACCESS_FINE_LOCATION") == 0 ? c("gps") : null;
        return (c11 == null || c10 == null) ? c11 != null ? c11 : c10 : c11.getTime() > c10.getTime() ? c11 : c10;
    }

    private Location c(String str) {
        try {
            if (this.f518b.isProviderEnabled(str)) {
                return this.f518b.getLastKnownLocation(str);
            }
            return null;
        } catch (Exception e10) {
            Log.d("TwilightManager", "Failed to get last known location", e10);
            return null;
        }
    }

    private boolean e() {
        return this.f519c.f521b > System.currentTimeMillis();
    }

    private void f(Location location) {
        long j10;
        a aVar = this.f519c;
        long currentTimeMillis = System.currentTimeMillis();
        TwilightCalculator b10 = TwilightCalculator.b();
        b10.a(currentTimeMillis - TimeInfoUtil.MILLISECOND_OF_A_DAY, location.getLatitude(), location.getLongitude());
        b10.a(currentTimeMillis, location.getLatitude(), location.getLongitude());
        boolean z10 = b10.f515c == 1;
        long j11 = b10.f514b;
        long j12 = b10.f513a;
        b10.a(currentTimeMillis + TimeInfoUtil.MILLISECOND_OF_A_DAY, location.getLatitude(), location.getLongitude());
        long j13 = b10.f514b;
        if (j11 == -1 || j12 == -1) {
            j10 = 43200000 + currentTimeMillis;
        } else {
            j10 = (currentTimeMillis > j12 ? j13 + 0 : currentTimeMillis > j11 ? j12 + 0 : j11 + 0) + 60000;
        }
        aVar.f520a = z10;
        aVar.f521b = j10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        a aVar = this.f519c;
        if (e()) {
            return aVar.f520a;
        }
        Location b10 = b();
        if (b10 != null) {
            f(b10);
            return aVar.f520a;
        }
        Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int i10 = Calendar.getInstance().get(11);
        return i10 < 6 || i10 >= 22;
    }
}
