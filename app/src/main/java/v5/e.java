package v5;

import android.util.Log;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import kotlin.Metadata;
import za.k;

/* compiled from: LogUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\u0006\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0002J\u0018\u0010\u0007\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0002J\u0018\u0010\b\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0002J\u0006\u0010\n\u001a\u00020\t¨\u0006\r"}, d2 = {"Lv5/e;", "", "", TriggerEvent.NOTIFICATION_TAG, "msg", "Lma/f0;", "a", "d", "b", "", "c", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public final class e {

    /* renamed from: b, reason: collision with root package name */
    public static final e f19125b = new e();

    /* renamed from: a, reason: collision with root package name */
    private static d f19124a = new a();

    /* compiled from: LogUtil.kt */
    @Metadata(bv = {}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0006\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0016J\u001a\u0010\u0007\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0016J\u001a\u0010\b\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0016J\b\u0010\n\u001a\u00020\tH\u0016¨\u0006\u000b"}, d2 = {"v5/e$a", "Lv5/d;", "", TriggerEvent.NOTIFICATION_TAG, "msg", "Lma/f0;", "d", "a", "c", "", "b", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    public static final class a implements d {
        a() {
        }

        @Override // v5.d
        public void a(String str, String str2) {
            k.e(str2, "msg");
            Log.w(str, str2);
        }

        @Override // v5.d
        public boolean b() {
            return true;
        }

        @Override // v5.d
        public void c(String str, String str2) {
            k.e(str2, "msg");
            Log.e(str, str2);
        }

        @Override // v5.d
        public void d(String str, String str2) {
            k.e(str2, "msg");
            Log.d(str, str2);
        }
    }

    private e() {
    }

    public final void a(String str, String str2) {
        k.e(str2, "msg");
        f19124a.d(str, str2);
    }

    public final void b(String str, String str2) {
        k.e(str2, "msg");
        f19124a.c(str, str2);
    }

    public final boolean c() {
        return f19124a.b();
    }

    public final void d(String str, String str2) {
        k.e(str2, "msg");
        f19124a.a(str, str2);
    }
}
