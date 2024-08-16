package android.app;

/* loaded from: classes.dex */
public class OplusINotificationExtImpl implements INotificationExt {
    public OplusINotificationExtImpl(Object base) {
    }

    public boolean isTotalCustom(Notification notification) {
        return notification.extras.getBoolean("oplus_total_custom_layout", false);
    }
}
