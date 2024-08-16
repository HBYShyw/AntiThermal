package v5;

import android.os.Handler;
import android.os.Looper;
import kotlin.Metadata;
import za.k;

/* compiled from: WorkHandler.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lv5/f;", "Landroid/os/Handler;", "Landroid/os/Looper;", "looper", "<init>", "(Landroid/os/Looper;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public final class f extends Handler {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(Looper looper) {
        super(looper);
        k.e(looper, "looper");
    }
}
