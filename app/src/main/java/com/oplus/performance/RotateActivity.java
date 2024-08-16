package com.oplus.performance;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.oplus.battery.R;
import java.util.List;

/* loaded from: classes.dex */
public class RotateActivity extends Activity {

    /* renamed from: e, reason: collision with root package name */
    protected int f10007e = -1;

    /* renamed from: f, reason: collision with root package name */
    private Context f10008f = null;

    /* renamed from: g, reason: collision with root package name */
    private Handler f10009g = new a();

    /* loaded from: classes.dex */
    class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            RotateActivity.this.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        try {
            String stringExtra = getIntent().getStringExtra("packageName");
            Log.d("RotateActivity", "packageName: " + stringExtra);
            PackageManager packageManager = this.f10008f.getPackageManager();
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            intent.setPackage(stringExtra);
            intent.addCategory("android.intent.category.LAUNCHER");
            List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
            Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.LAUNCHER");
            ComponentName componentName = new ComponentName(queryIntentActivities.get(0).activityInfo.packageName, queryIntentActivities.get(0).activityInfo.name);
            intent2.setComponent(componentName);
            intent2.addFlags(268435456);
            Log.d("RotateActivity", "start activity: " + componentName);
            this.f10008f.startActivity(intent2);
            finish();
        } catch (Exception e10) {
            Log.e("RotateActivity", "fail to start activity," + e10);
        }
    }

    private void c() {
        this.f10007e = 5894;
        getWindow().getDecorView().setSystemUiVisibility(this.f10007e);
        getWindow().setBackgroundDrawableResource(R.color.rotate_activity_black_color);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f10009g.hasMessages(1)) {
            this.f10009g.removeMessages(1);
            Log.d("RotateActivity", "onConfigurationChanged: removeMessages");
        }
        b();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        c();
        this.f10008f = this;
        Message obtainMessage = this.f10009g.obtainMessage();
        obtainMessage.what = 1;
        this.f10009g.sendMessageDelayed(obtainMessage, 100L);
        Log.d("RotateActivity", "sendMessageDelayed: MSG_START_ACTIVITY");
    }
}
