package android.app;

/* loaded from: classes.dex */
public class OplusINotificationManagerExtImpl implements INotificationManagerExt {
    public OplusINotificationManagerExtImpl(Object base) {
    }

    public void fixTotalCustom(Notification notification) {
        notification.extras.remove("oplus_total_custom_layout");
        notification.extras.remove("appPackage");
    }
}
