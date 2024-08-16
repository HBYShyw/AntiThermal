package com.oplus.modulehub.oassist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.pantanal.oassist.base.OAssistInput;
import com.oplus.pantanal.oassist.base.OAssistInputParam;
import com.oplus.pantanal.oassist.base.OAssistOutputBody;
import com.oplus.pantanal.oassist.base.OAssistOutputBodyContent;
import com.oplus.pantanal.oassist.service.OAssistService;
import f6.CommonUtil;
import f6.f;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import m7.OAssistContract;
import v4.GuardElfContext;
import y5.b;

/* loaded from: classes.dex */
public class BatteryAssistService extends OAssistService {
    private static final String HIGH_PERF_CLOSE_API_NAME = "close_high_performance_mode";
    private static final String HIGH_PERF_OPEN_API_NAME = "open_high_performance_mode";
    private static final String TAG = "BatteryAssistService";
    private ExecutorService mExecutor = null;

    public static OAssistOutputBody handleHighPerformanceModeClose() {
        String str;
        Context c10 = GuardElfContext.e().c();
        if (CommonUtil.A() != 1) {
            str = c10.getString(R.string.high_performance_mode_is_closed);
            LocalLog.l(TAG, "high performance mode is already off");
        } else {
            String string = c10.getString(R.string.close_high_performance_mode_success);
            if (!b.D()) {
                CommonUtil.b(c10);
            } else {
                CommonUtil.j0(c10, 1);
            }
            str = string;
        }
        return new OAssistOutputBody(new OAssistOutputBodyContent(new OAssistOutputBodyContent.HumanResponse(str, "", Uri.EMPTY), null), 0);
    }

    public static OAssistOutputBody handleHighPerformanceModeOpen() {
        String str;
        Context c10 = GuardElfContext.e().c();
        int i10 = 0;
        if (f.a1(c10)) {
            i10 = -1;
            LocalLog.b(TAG, "super power save mode is on");
            str = "";
        } else if (CommonUtil.A() == 1) {
            str = c10.getString(R.string.high_performance_mode_is_opened);
            LocalLog.l(TAG, "high performance mode is already on");
        } else {
            String string = c10.getString(R.string.open_high_performance_mode_success);
            ((PowerManager) c10.getSystemService("power")).setPowerSaveModeEnabled(false);
            if (!b.D()) {
                CommonUtil.b0(c10);
            } else {
                CommonUtil.j0(c10, 2);
            }
            str = string;
        }
        return new OAssistOutputBody(new OAssistOutputBodyContent(new OAssistOutputBodyContent.HumanResponse(str, "", Uri.EMPTY), null), i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ OAssistOutputBody lambda$callApi$0(OAssistService.a aVar) {
        try {
            Object invoke = aVar.a().invoke(null, new Object[0]);
            if (invoke instanceof OAssistOutputBody) {
                return (OAssistOutputBody) invoke;
            }
            LocalLog.a(TAG, "OAssistOutputBody is empty");
            return OAssistOutputBody.f9968g.a();
        } catch (IllegalAccessException | NumberFormatException | InvocationTargetException e10) {
            LocalLog.b(TAG, "callApi error: " + e10);
            return OAssistOutputBody.f9968g.a();
        }
    }

    private OAssistService.a matchHighPerformanceMode(boolean z10) {
        return new OAssistService.a(BatteryAssistService.class, BatteryAssistService.class.getDeclaredMethod(z10 ? "handleHighPerformanceModeOpen" : "handleHighPerformanceModeClose", new Class[0]), new ArrayList());
    }

    public static void testNoParamApi(final Context context, final String str) {
        context.bindService(new Intent(context, (Class<?>) BatteryAssistService.class), new ServiceConnection() { // from class: com.oplus.modulehub.oassist.BatteryAssistService.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    OAssistContract.a.z(iBinder).i(new OAssistInput(str, "", new ArrayList(), ""));
                } catch (RemoteException e10) {
                    LocalLog.b(BatteryAssistService.TAG, "callApi error: " + e10);
                }
                context.unbindService(this);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
            }
        }, 1);
    }

    @Override // com.oplus.pantanal.oassist.service.OAssistService
    public Future<OAssistOutputBody> callApi(final OAssistService.a aVar) {
        return Executors.newSingleThreadExecutor().submit(new Callable() { // from class: com.oplus.modulehub.oassist.a
            @Override // java.util.concurrent.Callable
            public final Object call() {
                OAssistOutputBody lambda$callApi$0;
                lambda$callApi$0 = BatteryAssistService.lambda$callApi$0(OAssistService.a.this);
                return lambda$callApi$0;
            }
        });
    }

    @Override // com.oplus.pantanal.oassist.service.OAssistService
    public OAssistService.a matchApi(String str, List<OAssistInputParam> list) {
        OAssistService.a matchHighPerformanceMode;
        try {
            if (TextUtils.equals(str, HIGH_PERF_OPEN_API_NAME)) {
                matchHighPerformanceMode = matchHighPerformanceMode(true);
            } else if (TextUtils.equals(str, HIGH_PERF_CLOSE_API_NAME)) {
                matchHighPerformanceMode = matchHighPerformanceMode(false);
            } else {
                if (!LocalLog.f()) {
                    return null;
                }
                LocalLog.a(TAG, "api not matched: " + str);
                return null;
            }
            return matchHighPerformanceMode;
        } catch (NoSuchMethodException e10) {
            LocalLog.b(TAG, "matchApi error: " + e10);
            return null;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        ExecutorService executorService = this.mExecutor;
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
