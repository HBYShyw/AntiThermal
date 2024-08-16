package w7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.smartCharge.SmartChargeAlarmReceiver;
import f6.ChargeUtil;
import i0.Crypto;
import i0.Session;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import org.json.JSONObject;
import s0.CryptoInitParameters;
import s0.EncryptAlgorithmEnum;
import s0.NegotiationAlgorithmEnum;
import s0.SceneConfig;
import w4.Affair;
import w4.IAffairCallback;
import zd.MediaType;
import zd.OkHttpClient;
import zd.RequestBody;
import zd.z;

/* compiled from: ChargeAuthenticationController.java */
/* renamed from: w7.a, reason: use source file name */
/* loaded from: classes.dex */
public class ChargeAuthenticationController implements IAffairCallback {

    /* renamed from: g, reason: collision with root package name */
    public static final MediaType f19387g = MediaType.f("application/json; charset=utf-8");

    /* renamed from: h, reason: collision with root package name */
    private static volatile ChargeAuthenticationController f19388h = null;

    /* renamed from: e, reason: collision with root package name */
    private Context f19389e;

    /* renamed from: f, reason: collision with root package name */
    private Handler f19390f;

    /* compiled from: ChargeAuthenticationController.java */
    /* renamed from: w7.a$a */
    /* loaded from: classes.dex */
    private class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 100:
                    ChargeAuthenticationController.this.registerAction();
                    return;
                case 101:
                    ChargeAuthenticationController.this.g();
                    return;
                case 102:
                    ChargeAuthenticationController.this.a();
                    return;
                default:
                    return;
            }
        }
    }

    private ChargeAuthenticationController(Context context) {
        this.f19389e = context;
        HandlerThread handlerThread = new HandlerThread("chargeAuthenticationController");
        handlerThread.start();
        this.f19390f = new a(handlerThread.getLooper());
    }

    public static ChargeAuthenticationController b(Context context) {
        if (f19388h == null) {
            synchronized (ChargeAuthenticationController.class) {
                if (f19388h == null) {
                    f19388h = new ChargeAuthenticationController(context);
                }
            }
        }
        return f19388h;
    }

    private void e(int i10) {
        LocalLog.a("chargeAuthenticationController", "setAlarmForAuthentication");
        AlarmManager alarmManager = (AlarmManager) this.f19389e.getSystemService("alarm");
        Intent intent = new Intent(this.f19389e, (Class<?>) SmartChargeAlarmReceiver.class);
        intent.setAction("com.oplus.app.charge.authentication.per.day");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f19389e, 0, intent, 67108864);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, i10);
        alarmManager.setExact(1, calendar.getTimeInMillis(), broadcast);
    }

    public void a() {
        try {
            long c10 = Crypto.c("wls-tx");
            LocalLog.b("chargeAuthenticationController", "certVersion: " + c10);
            String valueOf = String.valueOf(c10);
            Session a10 = Crypto.a("wls-tx");
            a10.d(valueOf, "scene_ec_gcm256");
            String g6 = a10.g("scene_ec_gcm256");
            OkHttpClient okHttpClient = new OkHttpClient();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("certVersion", valueOf);
            String v7 = okHttpClient.u(new z.a().m(this.f19389e.getResources().getString(R.string.tx_cert_url)).a("cipherInfo", g6).g(RequestBody.c(f19387g, String.valueOf(jSONObject))).b()).execute().getF20511k().v();
            LocalLog.b("chargeAuthenticationController", "dataFromServer: " + v7);
            JSONObject jSONObject2 = new JSONObject(v7);
            LocalLog.b("chargeAuthenticationController", "jsonObject: " + jSONObject2);
            String string = jSONObject2.getString("data");
            LocalLog.b("chargeAuthenticationController", "serverCipher: " + string);
            String str = string.split("\"keyData\":")[1];
            String substring = str.substring(1, str.length() + (-2));
            ChargeUtil.x(substring);
            LocalLog.b("chargeAuthenticationController", "data: " + substring);
            Crypto.f(a10);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public void c() {
        this.f19390f.sendEmptyMessage(101);
    }

    public void d() {
        this.f19390f.sendEmptyMessage(102);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 != 207) {
            return;
        }
        int nextInt = new Random().nextInt(13) + 9;
        LocalLog.b("chargeAuthenticationController", "randomHour = " + nextInt);
        e(nextInt);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    public void f() {
        this.f19390f.sendEmptyMessage(100);
    }

    public void g() {
        Affair.f().i(this, EventType.SCENE_MODE_READING);
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_READING);
        HashMap hashMap = new HashMap();
        hashMap.put("wls-tx", this.f19389e.getResources().getString(R.string.tx_url));
        SceneConfig sceneConfig = new SceneConfig();
        sceneConfig.k("scene_ec_gcm256");
        sceneConfig.j(NegotiationAlgorithmEnum.EC);
        sceneConfig.g(EncryptAlgorithmEnum.f17990n);
        sceneConfig.i(true);
        sceneConfig.h(86400);
        ArrayList arrayList = new ArrayList();
        arrayList.add(sceneConfig);
        Crypto.e(new CryptoInitParameters.b(this.f19389e).j(hashMap).k(arrayList).i());
        LocalLog.b("chargeAuthenticationController", "Crypto init!");
    }
}
