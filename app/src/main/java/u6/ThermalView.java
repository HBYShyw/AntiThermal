package u6;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.battery.R;

/* compiled from: ThermalView.java */
/* renamed from: u6.a, reason: use source file name */
/* loaded from: classes.dex */
public class ThermalView {

    /* renamed from: j, reason: collision with root package name */
    private static boolean f18875j = true;

    /* renamed from: a, reason: collision with root package name */
    private Context f18876a;

    /* renamed from: b, reason: collision with root package name */
    private Context f18877b;

    /* renamed from: c, reason: collision with root package name */
    private NotificationManager f18878c;

    /* renamed from: d, reason: collision with root package name */
    private NotificationChannel f18879d;

    /* renamed from: e, reason: collision with root package name */
    private NotificationChannel f18880e;

    /* renamed from: f, reason: collision with root package name */
    private SharedPreferences f18881f;

    /* renamed from: g, reason: collision with root package name */
    private HandlerThread f18882g;

    /* renamed from: h, reason: collision with root package name */
    private Handler f18883h;

    /* renamed from: i, reason: collision with root package name */
    private int f18884i;

    /* compiled from: ThermalView.java */
    /* renamed from: u6.a$a */
    /* loaded from: classes.dex */
    class a extends Handler {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f18885a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f18886b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ String f18887c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ Notification.BigTextStyle f18888d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Notification.Builder f18889e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Looper looper, int i10, int i11, String str, Notification.BigTextStyle bigTextStyle, Notification.Builder builder) {
            super(looper);
            this.f18885a = i10;
            this.f18886b = i11;
            this.f18887c = str;
            this.f18888d = bigTextStyle;
            this.f18889e = builder;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i10 = message.what;
            if (i10 <= 1 || i10 >= this.f18885a + 1) {
                return;
            }
            int i11 = message.getData().getInt("time", 0);
            String str = ThermalView.this.f18876a.getString(this.f18886b, Integer.valueOf(i11)) + "\n" + this.f18887c;
            LocalLog.a("ThermalView", "msg = " + message.what + " second = " + i11);
            this.f18888d.bigText(str);
            this.f18889e.setContentText(str).setStyle(this.f18888d);
            ThermalView.this.f18878c.notifyAsUser("ThermalView", 12, this.f18889e.build(), UserHandle.CURRENT);
        }
    }

    public ThermalView(Context context) {
        this.f18876a = context;
        this.f18877b = context.createDeviceProtectedStorageContext();
        this.f18878c = (NotificationManager) this.f18876a.getSystemService("notification");
        String string = this.f18876a.getString(R.string.thermal_channel);
        NotificationChannel notificationChannel = new NotificationChannel("ThermalChanelId", string, 3);
        this.f18879d = notificationChannel;
        notificationChannel.setDescription(string);
        this.f18879d.enableLights(true);
        this.f18879d.setLightColor(-16711936);
        this.f18879d.enableVibration(true);
        this.f18878c.createNotificationChannel(this.f18879d);
        NotifyUtil.v(this.f18876a).W(this.f18879d, R.string.thermal_channel);
        NotificationChannel notificationChannel2 = new NotificationChannel("AbnormalPowerConsumption", this.f18876a.getString(R.string.notify_screenoff_unrestrict_title), 4);
        this.f18880e = notificationChannel2;
        notificationChannel2.setDescription("AbnormalPowerConsumption");
        this.f18880e.enableLights(true);
        this.f18880e.setLightColor(-16711936);
        this.f18880e.enableVibration(true);
        this.f18878c.createNotificationChannel(this.f18880e);
        NotifyUtil.v(this.f18876a).W(this.f18880e, R.string.notify_screenoff_unrestrict_title);
        this.f18881f = this.f18877b.getSharedPreferences("high_temperature", 0);
        HandlerThread handlerThread = new HandlerThread("ThermalViewHandler");
        this.f18882g = handlerThread;
        handlerThread.start();
    }

    public static void g(boolean z10) {
        f18875j = z10;
    }

    public void c() {
        LocalLog.a("ThermalView", "cancel notify in high temp");
        if (this.f18883h != null) {
            int i10 = 1;
            while (i10 < this.f18884i) {
                i10++;
                this.f18883h.removeMessages(i10);
            }
        }
        this.f18878c.cancelAsUser("ThermalView", 12, UserHandle.ALL);
    }

    public void d() {
        this.f18878c.cancelAsUser("ThermalView", 13, UserHandle.CURRENT);
    }

    public void e(int i10, String str, int i11) {
        LocalLog.a("ThermalView", "notify in");
        Notification.Builder builder = new Notification.Builder(this.f18876a, this.f18880e.getId());
        Intent intent = new Intent();
        if (this.f18881f.getBoolean("first_step_notify", false)) {
            intent.setAction("oplus.intent.action.ACTION_THERMAL_NOTIFY_FIRST");
        } else if (this.f18881f.getBoolean("second_step_notify", false)) {
            intent.setAction("oplus.intent.action.ACTION_THERMAL_NOTIFY_SECOND");
        }
        this.f18884i = i11;
        intent.setPackage("com.oplus.battery");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f18876a, 0, intent, 67108864);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        StringBuilder sb2 = new StringBuilder();
        int i12 = 1;
        sb2.append(this.f18876a.getString(i10, Integer.valueOf(i11)));
        sb2.append("\n");
        sb2.append(str);
        bigTextStyle.bigText(sb2.toString());
        builder.setContentText(this.f18876a.getString(i10, Integer.valueOf(i11)) + "\n" + str).setChannelId(this.f18880e.getId()).setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(broadcast).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(true).setOnlyAlertOnce(true).setAutoCancel(false);
        Notification build = builder.build();
        if (f18875j) {
            this.f18878c.notifyAsUser("ThermalView", 12, build, UserHandle.CURRENT);
        }
        this.f18883h = new a(this.f18882g.getLooper(), i11, i10, str, bigTextStyle, builder);
        if (f18875j) {
            while (i12 < i11) {
                Message obtainMessage = this.f18883h.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("time", i11 - i12);
                int i13 = i12 + 1;
                obtainMessage.what = i13;
                LocalLog.a("ThermalView", "delay " + i12 + " and msg " + obtainMessage.what);
                obtainMessage.setData(bundle);
                this.f18883h.sendMessageDelayed(obtainMessage, ((long) i12) * 1000);
                i12 = i13;
            }
        }
    }

    public void f(String str) {
        Notification.Builder builder = new Notification.Builder(this.f18876a, this.f18879d.getId());
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(str);
        builder.setContentText(str).setChannelId(this.f18879d.getId()).setWhen(System.currentTimeMillis()).setShowWhen(true).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).setAutoCancel(false);
        this.f18878c.notifyAsUser("ThermalView", 13, builder.build(), UserHandle.CURRENT);
    }
}
