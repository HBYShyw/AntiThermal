package k7;

import android.content.ComponentName;
import android.os.IBinder;

/* compiled from: IBinderCallback.java */
/* renamed from: k7.c, reason: use source file name */
/* loaded from: classes.dex */
public interface IBinderCallback {
    void onServiceConnected(ComponentName componentName, IBinder iBinder);

    void onServiceDisconnected(ComponentName componentName);
}
