package androidx.core.app;

import android.app.NotificationManager;
import android.content.Context;
import java.util.HashSet;
import java.util.Set;

/* compiled from: NotificationManagerCompat.java */
/* renamed from: androidx.core.app.g, reason: use source file name */
/* loaded from: classes.dex */
public final class NotificationManagerCompat {

    /* renamed from: c, reason: collision with root package name */
    private static final Object f2114c = new Object();

    /* renamed from: d, reason: collision with root package name */
    private static Set<String> f2115d = new HashSet();

    /* renamed from: e, reason: collision with root package name */
    private static final Object f2116e = new Object();

    /* renamed from: a, reason: collision with root package name */
    private final Context f2117a;

    /* renamed from: b, reason: collision with root package name */
    private final NotificationManager f2118b;

    private NotificationManagerCompat(Context context) {
        this.f2117a = context;
        this.f2118b = (NotificationManager) context.getSystemService("notification");
    }

    public static NotificationManagerCompat b(Context context) {
        return new NotificationManagerCompat(context);
    }

    public boolean a() {
        return this.f2118b.areNotificationsEnabled();
    }
}
