package android.app;

import android.app.Notification;

/* loaded from: classes.dex */
public class MessagingStyleExtImpl implements IMessagingStyleExt {
    private static final int OPLUS_CONVERSATION_TYPE_LEGACY = 0;
    private Notification.MessagingStyle mBase;

    public MessagingStyleExtImpl(Object base) {
        this.mBase = (Notification.MessagingStyle) base;
    }

    public int getConversationType(int type) {
        return 0;
    }
}
