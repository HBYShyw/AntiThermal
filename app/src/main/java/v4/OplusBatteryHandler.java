package v4;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* compiled from: OplusBatteryHandler.java */
/* renamed from: v4.d, reason: use source file name */
/* loaded from: classes.dex */
public class OplusBatteryHandler extends Handler {

    /* renamed from: a, reason: collision with root package name */
    private Context f19085a;

    public OplusBatteryHandler(Context context, Looper looper) {
        super(looper);
        this.f19085a = context;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        int i10 = message.what;
    }
}
