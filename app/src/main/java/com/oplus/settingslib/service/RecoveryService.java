package com.oplus.settingslib.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public abstract class RecoveryService extends Service {

    /* renamed from: e, reason: collision with root package name */
    private Messenger f10469e;

    /* renamed from: f, reason: collision with root package name */
    private a f10470f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class a extends AsyncTask<Messenger, Messenger, Boolean> {

        /* renamed from: a, reason: collision with root package name */
        private Context f10471a;

        /* renamed from: b, reason: collision with root package name */
        private Messenger f10472b;

        public a(Context context) {
            this.f10471a = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Boolean doInBackground(Messenger... messengerArr) {
            this.f10472b = messengerArr[0];
            return Boolean.valueOf(RecoveryService.this.a(this.f10471a));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(Boolean bool) {
            try {
                this.f10472b.send(Message.obtain((Handler) null, bool.booleanValue() ? 2 : 3));
            } catch (RemoteException e10) {
                e10.printStackTrace();
            }
        }
    }

    /* loaded from: classes2.dex */
    private static class b extends Handler {

        /* renamed from: a, reason: collision with root package name */
        private a f10474a;

        public b(a aVar) {
            this.f10474a = aVar;
        }

        private void a(Message message) {
            a aVar = this.f10474a;
            if (aVar != null) {
                try {
                    aVar.execute(message.replyTo);
                } catch (IllegalStateException e10) {
                    e10.printStackTrace();
                }
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                super.handleMessage(message);
            } else {
                a(message);
            }
        }
    }

    public abstract boolean a(Context context);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        this.f10470f = new a(this);
        Messenger messenger = new Messenger(new b(this.f10470f));
        this.f10469e = messenger;
        return messenger.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        a aVar = this.f10470f;
        if (aVar != null) {
            aVar.cancel(true);
        }
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        a aVar = this.f10470f;
        if (aVar != null) {
            aVar.cancel(true);
        }
        return super.onUnbind(intent);
    }
}
