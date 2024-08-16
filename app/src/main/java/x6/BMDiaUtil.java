package x6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.view.Window;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import com.oplus.battery.R;
import f6.CommonUtil;
import f6.f;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import w5.OplusBatteryConstants;
import w6.PluginMethodCaller;
import w6.PluginSupporter;
import x1.COUISecurityAlertDialogBuilder;
import x5.UploadDataUtil;

/* compiled from: BMDiaUtil.java */
/* renamed from: x6.b, reason: use source file name */
/* loaded from: classes.dex */
public class BMDiaUtil {

    /* renamed from: g, reason: collision with root package name */
    private static BMDiaUtil f19548g;

    /* renamed from: a, reason: collision with root package name */
    private Context f19549a;

    /* renamed from: c, reason: collision with root package name */
    private AlertDialog f19551c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f19552d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f19553e = false;

    /* renamed from: f, reason: collision with root package name */
    private Handler f19554f = new a(Looper.getMainLooper());

    /* renamed from: b, reason: collision with root package name */
    private PluginMethodCaller f19550b = PluginSupporter.m().n();

    /* compiled from: BMDiaUtil.java */
    /* renamed from: x6.b$a */
    /* loaded from: classes.dex */
    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 1) {
                return;
            }
            BMDiaUtil.this.j();
        }
    }

    private BMDiaUtil(Context context) {
        this.f19549a = context;
    }

    public static synchronized BMDiaUtil f(Context context) {
        BMDiaUtil bMDiaUtil;
        synchronized (BMDiaUtil.class) {
            if (f19548g == null) {
                f19548g = new BMDiaUtil(context);
            }
            bMDiaUtil = f19548g;
        }
        return bMDiaUtil;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(SharedPreferences.Editor editor, int i10, boolean z10) {
        LocalLog.a("BMDiaUtil", "isChecked=" + z10);
        if (i10 != -1) {
            if (i10 == -2) {
                LocalLog.a("BMDiaUtil", "DialogInterface.BUTTON_NEGATIVE");
                return;
            }
            return;
        }
        if (CommonUtil.A() != 1) {
            ((PowerManager) this.f19549a.getSystemService("power")).setPowerSaveModeEnabled(false);
            PluginMethodCaller pluginMethodCaller = this.f19550b;
            if (pluginMethodCaller != null) {
                pluginMethodCaller.b(OplusBatteryConstants.f19357i.booleanValue());
            }
        }
        if (z10) {
            editor.putBoolean("HighPerDiaFlag", true);
            editor.commit();
            k("click_never_remind");
            this.f19553e = false;
            LocalLog.a("BMDiaUtil", "choose never mind");
        } else {
            editor.putBoolean("HighPerDiaFlag", false);
            editor.commit();
            LocalLog.a("BMDiaUtil", "don't choose never mind");
        }
        k("click_close_immediately");
        LocalLog.a("BMDiaUtil", "click got it");
    }

    private void h(int i10) {
        Message obtainMessage = this.f19554f.obtainMessage();
        obtainMessage.what = i10;
        this.f19554f.sendMessage(obtainMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        SharedPreferences sharedPreferences = this.f19549a.getSharedPreferences("BenchFlags", 0);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        this.f19552d = sharedPreferences.getBoolean("HighPerDiaFlag", false);
        LocalLog.a("BMDiaUtil:", "highPerDiaFlag = " + this.f19552d);
        if (!this.f19552d || this.f19553e) {
            AlertDialog alertDialog = this.f19551c;
            if (alertDialog == null || !alertDialog.isShowing()) {
                this.f19551c = null;
                Context context = this.f19549a;
                COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder = new COUISecurityAlertDialogBuilder(context, R.style.Theme_Dialog_Alert, f.u0(context));
                cOUISecurityAlertDialogBuilder.J0(this.f19549a.getResources().getString(R.string.high_temperature_shutdown_auto)).E0(true).C0(this.f19549a.getResources().getString(R.string.high_performance_dialog_never_remind)).D0(true).H0(new COUISecurityAlertDialogBuilder.g() { // from class: x6.a
                    @Override // x1.COUISecurityAlertDialogBuilder.g
                    public final void a(int i10, boolean z10) {
                        BMDiaUtil.this.g(edit, i10, z10);
                    }
                });
                cOUISecurityAlertDialogBuilder.t(this.f19549a.getResources().getString(R.string.high_performance_dialog_title_for_clean));
                cOUISecurityAlertDialogBuilder.h(this.f19549a.getResources().getString(R.string.high_performance_dialog_message_for_clean));
                cOUISecurityAlertDialogBuilder.d(false);
                AlertDialog a10 = cOUISecurityAlertDialogBuilder.a();
                this.f19551c = a10;
                if (a10 != null) {
                    try {
                        Window window = a10.getWindow();
                        window.setType(2003);
                        try {
                            f.n2(window.getAttributes(), 1);
                        } catch (Exception e10) {
                            e10.printStackTrace();
                        }
                        this.f19551c.show();
                        LocalLog.a("BMDiaUtil", "checkbox=" + this.f19551c.findViewById(R.id.coui_security_alert_dialog_checkbox));
                        Method declaredMethod = COUISecurityAlertDialogBuilder.class.getDeclaredMethod("x0", new Class[0]);
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(cOUISecurityAlertDialogBuilder, new Object[0]);
                        LocalLog.a("BMDiaUtil", "initCheckBox");
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e11) {
                        e11.printStackTrace();
                    }
                }
                LocalLog.a("BMDiaUtil", "alertDialog show high per");
            }
        }
    }

    private void k(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("dialogClick", str);
        UploadDataUtil.S0(this.f19549a).h(hashMap);
    }

    public void c() {
        AlertDialog alertDialog = this.f19551c;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        this.f19551c.dismiss();
    }

    public void d() {
        h(1);
    }

    public boolean e() {
        return this.f19553e;
    }

    public void i(boolean z10) {
        this.f19553e = z10;
    }
}
