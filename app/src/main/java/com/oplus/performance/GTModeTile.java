package com.oplus.performance;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import java.io.IOException;
import x1.COUIAlertDialogBuilder;

/* loaded from: classes.dex */
public class GTModeTile extends TileService {

    /* renamed from: e, reason: collision with root package name */
    private AlertDialog f9991e;

    /* renamed from: f, reason: collision with root package name */
    private MediaPlayer f9992f;

    /* renamed from: g, reason: collision with root package name */
    private Context f9993g;

    /* renamed from: h, reason: collision with root package name */
    private ContentObserver f9994h = new a(new Handler());

    /* loaded from: classes.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.l("GTModeTile", "GTModeObserver updateTile");
            GTModeTile.this.q();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        @SuppressLint({"NewApi"})
        public void run() {
            Intent intent = new Intent(GTModeTile.this, (Class<?>) TransparentActivity.class);
            intent.addFlags(268435456);
            GTModeTile.this.startActivityAndCollapse(PendingIntent.getActivity(GTModeTile.this.f9993g, 0, intent, 335544320));
            GTModeTile.this.o();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements MediaPlayer.OnCompletionListener {
        c() {
        }

        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (GTModeTile.this.f9992f != null) {
                GTModeTile.this.f9992f.release();
                GTModeTile.this.f9992f = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AnimationDrawable f9998e;

        d(AnimationDrawable animationDrawable) {
            this.f9998e = animationDrawable;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f9998e.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ImageView f10000e;

        e(ImageView imageView) {
            this.f10000e = imageView;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f10000e.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ WindowManager f10002e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ View f10003f;

        f(WindowManager windowManager, View view) {
            this.f10002e = windowManager;
            this.f10003f = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f10002e.removeView(this.f10003f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements DialogInterface.OnClickListener {
        g() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            GTModeTile.this.j();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements DialogInterface.OnClickListener {
        h() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            GTModeTile.this.f9991e.dismiss();
        }
    }

    @SuppressLint({"WrongConstant"})
    private void h() {
        i();
        if (getQsTile().getState() != 2) {
            if (l()) {
                if (isLocked()) {
                    unlockAndRun(new b());
                    return;
                } else {
                    o();
                    return;
                }
            }
            j();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("gt_mode_broadcast_intent_close_action");
        intent.addFlags(16777216);
        intent.setComponent(new ComponentName(getPackageName(), GTModeBroadcastReceiver.class.getName()));
        sendBroadcast(intent);
    }

    private void i() {
        StatusBarManager statusBarManager = (StatusBarManager) getSystemService("statusbar");
        if (statusBarManager != null) {
            try {
                statusBarManager.collapsePanels();
            } catch (Exception e10) {
                LocalLog.b("GTModeTile", e10.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"WrongConstant"})
    public void j() {
        String[] stringArray;
        if (GTModeBroadcastReceiver.j(this) && y5.b.w() && y5.b.v()) {
            String f10 = GTModeBroadcastReceiver.f();
            boolean z10 = false;
            z10 = false;
            if (f10 != null && (stringArray = getResources().getStringArray(R.array.gt_video_app_list)) != null) {
                boolean z11 = false;
                for (String str : stringArray) {
                    if (TextUtils.equals(str, f10)) {
                        z11 = true;
                    }
                }
                z10 = z11;
            }
            LocalLog.d("GTModeTile", "topApp =" + f10 + "isVideo =" + z10);
            if (!z10) {
                p();
                m();
                GTModeBroadcastReceiver.o(this);
            }
        } else {
            p();
            m();
            GTModeBroadcastReceiver.o(this);
        }
        n();
        Intent intent = new Intent();
        intent.setAction("gt_mode_broadcast_intent_open_action");
        intent.addFlags(16777216);
        intent.setComponent(new ComponentName(getPackageName(), GTModeBroadcastReceiver.class.getName()));
        sendBroadcast(intent);
    }

    private Context k(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.Theme_Demo);
        COUIThemeOverlay.i().b(contextThemeWrapper);
        return contextThemeWrapper;
    }

    private boolean l() {
        boolean z10 = getSharedPreferences("sp_first_show_gt_dialog", 0).getBoolean("sp_first_show_gt_dialog_key", true);
        if (z10 && TextUtils.equals(Settings.System.getString(getContentResolver(), "gtmode_open_first_time"), "no")) {
            return false;
        }
        return z10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [android.media.MediaPlayer] */
    /* JADX WARN: Type inference failed for: r0v4, types: [android.content.res.AssetFileDescriptor] */
    private void m() {
        int identifier;
        this.f9992f = new MediaPlayer();
        PackageManager packageManager = getPackageManager();
        Resources resources = null;
        try {
            resources = packageManager.getResourcesForApplication("com.android.rm.gt.overlay");
            identifier = resources.getIdentifier("gt_mode_open_sound", "raw", "com.android.rm.gt.overlay");
        } catch (PackageManager.NameNotFoundException unused) {
            LocalLog.a("GTModeTile", "get sound fail");
            try {
                resources = packageManager.getResourcesForApplication("com.oplus.battery.overlay.gtanim");
            } catch (PackageManager.NameNotFoundException unused2) {
                LocalLog.a("GTModeTile", "get sound fail overlay");
            }
            identifier = resources.getIdentifier("gt_mode_open_sound", "raw", "com.oplus.battery.overlay.gtanim");
        }
        if (identifier != -1) {
            AssetFileDescriptor openRawResourceFd = resources.openRawResourceFd(identifier);
            try {
            } catch (IOException e10) {
                e10.printStackTrace();
            }
            try {
                try {
                    this.f9992f.setDataSource(openRawResourceFd.getFileDescriptor(), openRawResourceFd.getStartOffset(), openRawResourceFd.getLength());
                    this.f9992f.prepare();
                    openRawResourceFd.close();
                } catch (IOException unused3) {
                    LocalLog.a("GTModeTile", "get fd exception");
                    if (openRawResourceFd != 0) {
                        openRawResourceFd.close();
                    }
                }
                this.f9992f.setVolume(0.5f, 0.5f);
                this.f9992f.start();
                openRawResourceFd = this.f9992f;
                openRawResourceFd.setOnCompletionListener(new c());
            } catch (Throwable th) {
                if (openRawResourceFd != 0) {
                    try {
                        openRawResourceFd.close();
                    } catch (IOException e11) {
                        e11.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    private void n() {
        getSharedPreferences("sp_first_show_gt_dialog", 0).edit().putBoolean("sp_first_show_gt_dialog_key", false).apply();
        Settings.System.putString(getContentResolver(), "gtmode_open_first_time", "no");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(k(this), f6.f.k(this.f9993g));
        cOUIAlertDialogBuilder.h0(R.string.rm_gt_mode_dialog_title);
        cOUIAlertDialogBuilder.Y(R.string.rm_gt_mode_dialog_content);
        cOUIAlertDialogBuilder.d(false);
        cOUIAlertDialogBuilder.e0(R.string.rm_gt_mode_dialog_confirm, new g());
        cOUIAlertDialogBuilder.a0(R.string.rm_gt_mode_dialog_cancel, new h());
        if (this.f9991e == null) {
            this.f9991e = cOUIAlertDialogBuilder.a();
        }
        Window window = this.f9991e.getWindow();
        this.f9991e.getWindow().setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        try {
            if (this.f9991e.isShowing()) {
                return;
            }
            this.f9991e.show();
        } catch (Exception e11) {
            LocalLog.a("GTModeTile", "show confirm dialog e = " + e11);
        }
    }

    @SuppressLint({"WrongConstant"})
    private void p() {
        WindowManager windowManager = (WindowManager) getSystemService("window");
        Drawable drawable = null;
        View inflate = LayoutInflater.from(this).inflate(R.layout.gt_mode_activity_layout, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.gt_mode_iv);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.gt_mode_background);
        try {
            Resources resourcesForApplication = getPackageManager().getResourcesForApplication("com.android.rm.gt.overlay");
            if (resourcesForApplication != null) {
                drawable = resourcesForApplication.getDrawable(resourcesForApplication.getIdentifier("gt_mode_anim", "drawable", "com.android.rm.gt.overlay"));
            }
        } catch (Exception unused) {
            LocalLog.a("GTModeTile", "loadDrawable fail");
            drawable = getResources().getDrawable(R.drawable.gt_mode_anim);
        }
        imageView.setImageDrawable(drawable);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1);
        layoutParams.type = 2015;
        layoutParams.flags = 234882560;
        layoutParams.format = -3;
        layoutParams.layoutInDisplayCutoutMode = 1;
        windowManager.addView(inflate, layoutParams);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.98f);
        alphaAnimation.setDuration(400L);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.98f, 0.98f);
        alphaAnimation2.setDuration(1000L);
        alphaAnimation2.setStartOffset(400L);
        AlphaAnimation alphaAnimation3 = new AlphaAnimation(0.98f, 0.0f);
        alphaAnimation3.setDuration(600L);
        alphaAnimation3.setStartOffset(1400L);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(alphaAnimation2);
        animationSet.addAnimation(alphaAnimation3);
        imageView2.startAnimation(animationSet);
        long j10 = 0;
        for (int i10 = 0; i10 < animationDrawable.getNumberOfFrames(); i10++) {
            j10 += animationDrawable.getDuration(i10);
        }
        Handler handler = new Handler();
        handler.postDelayed(new d(animationDrawable), 200L);
        handler.postDelayed(new e(imageView), j10 + 200);
        handler.postDelayed(new f(windowManager, inflate), 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        Tile qsTile = getQsTile();
        if (qsTile == null) {
            return;
        }
        int intForUser = Settings.System.getIntForUser(getContentResolver(), "gt_mode_state_setting", 0, 0);
        if (intForUser == 1) {
            qsTile.setState(2);
            qsTile.setIcon(Icon.createWithResource(this, R.drawable.ic_gt_icon_white_active));
            qsTile.setSubtitle("");
        } else if (intForUser == 0) {
            qsTile.setState(1);
            qsTile.setIcon(Icon.createWithResource(this, R.drawable.ic_gt_icon_black_unactive));
            qsTile.setSubtitle("");
        }
        if (!y5.b.E()) {
            qsTile.setState(0);
        }
        qsTile.setLabel(getString(R.string.rm_gt_mode_tile_title));
        qsTile.updateTile();
    }

    @Override // android.service.quicksettings.TileService, android.app.Service
    public IBinder onBind(Intent intent) {
        q();
        getContentResolver().registerContentObserver(Settings.System.getUriFor("gt_mode_state_setting"), false, this.f9994h);
        return super.onBind(intent);
    }

    @Override // android.service.quicksettings.TileService
    public void onClick() {
        h();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f9993g = getApplicationContext();
    }

    @Override // android.service.quicksettings.TileService
    public void onStartListening() {
        q();
        super.onStartListening();
    }

    @Override // android.service.quicksettings.TileService
    public void onStopListening() {
        super.onStopListening();
    }

    @Override // android.service.quicksettings.TileService
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        getContentResolver().unregisterContentObserver(this.f9994h);
        return super.onUnbind(intent);
    }
}
